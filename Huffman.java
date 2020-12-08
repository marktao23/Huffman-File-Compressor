import java.io.*;
import java.util.*;

/**
 * @author Mark Tao, produce a Huffman tree that compresses and decompresses all bytes of a file
 */

public class Huffman {

    //creating the building the map function
    public static Map<Character, Integer> buildMap() throws IOException {

        //creating an empty map
        Map<Character, Integer> frequencyTracker = new TreeMap<>();

        try {
            //try finding the file and reading the first character
            BufferedReader input = new BufferedReader(new FileReader("input/test.txt"));
            int characterRed = input.read();

            //loop through all characters and add them to the map. If it's already in there, increment its value by 1.
            while (characterRed != -1) {

                if (!frequencyTracker.containsKey((char) characterRed)) {
                    frequencyTracker.put((char) characterRed, 1);
                } else if (frequencyTracker.containsKey((char) characterRed)) {
                    frequencyTracker.put((char) characterRed, frequencyTracker.get((char) characterRed) + 1);
                }
                    characterRed = input.read();
                }

            //catch if the file isn't found
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
        return frequencyTracker;
    }

    //function that builds the binary trees and places them in the queue
    public static BinaryTree<TreeData> PriorityQueue(Map<Character, Integer> frequencyTracker) {

        //builds the comparator
        Comparator<BinaryTree<TreeData>> comparator = new TreeComparator();

        //builds the priority queue, calling the comparator function on the entire map
        PriorityQueue<BinaryTree<TreeData>> pQueue = new PriorityQueue<BinaryTree<TreeData>>(frequencyTracker.size(), comparator);
        Set<Character> temp = frequencyTracker.keySet();

        //loop through all map elements, create a binary tree for them, and add them to the queue
        for (char key: temp) {
            TreeData node = new TreeData(key, frequencyTracker.get(key));
            BinaryTree<TreeData> tree = new BinaryTree<TreeData>(node);
            pQueue.add(tree);
        }
        //loop that builds the huffman tree
        while (pQueue.size() > 1) {
            //getting first two largest frequencies
            BinaryTree<TreeData> firstNode = pQueue.remove();
            BinaryTree<TreeData> secondNode = pQueue.remove();

            //creating the first node with its data as the sum of the node's frequencies
            long sum = firstNode.getData().getFrequency() + secondNode.getData().getFrequency();

            //building the actual huffman tree
            BinaryTree<TreeData> huffmanTree = new BinaryTree<>(new TreeData(null, sum), firstNode, secondNode);

            //adding to the priority queue so that the loop can continue
            pQueue.add(huffmanTree);

        }
        BinaryTree<TreeData> finalTree = pQueue.remove();
        return finalTree;
    }

    //helper function for retrieving the code
    public static void helper(BinaryTree<TreeData> tree, Map<Character, String> mapOfCodes, String path) {

        //if there's a leaf, recursively run this function again with that tree's character
        if (tree.isLeaf()) {
            char character = tree.getData().getCharacter();
            mapOfCodes.put(character, path);
        }

        //if there's a left leaf, build the path as 0
        if (tree.hasLeft()) {
            path += "0";
            helper(tree.getLeft(), mapOfCodes, path);
        }

        //if there's a right leaf, build the path as 1
        if (tree.hasRight()) {
            path += "1";
            helper(tree.getRight(), mapOfCodes, path);
        }
    }

    //function that actually retrieves the code and returns the map of codes.
    public static Map<Character, String> codeRetrieval(BinaryTree<TreeData> tree) {
        Map<Character, String> mapOfCodes = new TreeMap<Character, String>();
        helper(tree, mapOfCodes, " ");
        return mapOfCodes;
    }

    //compresser method
    public static void compress(Map<Character, String> mapPath, String filename, String outputFile) throws IOException {
        int output;

        //opening the file
        BufferedReader input = new BufferedReader(new FileReader(filename));
        BufferedBitWriter bitOutput = new BufferedBitWriter(outputFile);

        try {
            //while loop that writes all of the characters into bits
            while ((output = input.read()) != -1) {

                //creating new character variable that represents the input.read() value
                char character = (char) output;

                //get the value associated with the string
                String value = mapPath.get(character);

                //loop through and write all of the bits
                for (int y = 0; y < value.length(); y++) {
                    bitOutput.writeBit(value.charAt(y) == '1');
                }
            }
        }
        //catch any error
        catch (Exception e) {
            System.err.println(e);
        }
        //close the file, at the very end.
        finally {
            input.close();
            bitOutput.close();
        }
    }

    //decompressor method
    public static void decompress(String filename, BinaryTree<TreeData> trackerTree) throws IOException {

        String output = filename + ", decompressed version.";
        BufferedWriter outputOfBits = new BufferedWriter(new FileWriter(output));
        BufferedBitReader reader = new BufferedBitReader("inputs/compression.txt");
        BinaryTree<TreeData> tree = trackerTree;

        //if there's a next line
        while (reader.hasNext()) {

            //boolean that checks if the bit reads right
            boolean isRight = reader.readBit();

            if (isRight) {
                tree = tree.getRight();
            }
            //else, go left
            else {
                tree = tree.getLeft();
            }
            //if the leaf exists, then get the character and match the leaf with its value and write it
            if (tree.isLeaf()) {
                char character = tree.getData().getCharacter();
                outputOfBits.write(character);
                tree = trackerTree;
            }
        }

        //close the file
        reader.close();
        outputOfBits.close();

    }

    public static void main(String[] args) {
        //calling all functions and running it on test case
        try {
            Map<Character, Integer> a = buildMap();
            BinaryTree<TreeData> huffmanTree = PriorityQueue(a);
            Map<Character, String> mapOfCodes = codeRetrieval(huffmanTree);
            compress(mapOfCodes,  "inputs/test.txt", "inputs/compression.txt");
            decompress("inputs/compression.txt", huffmanTree);
        }
        //catch any error or exception
        catch (FileNotFoundException e) {
            System.err.println("file not found");
        }
        catch (IOException e) {
            System.err.println("error");
        }

    }
}
