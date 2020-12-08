/**
 * @author Mark Tao, produce a Huffman tree that compresses and decompresses all bytes of a file
 */

public class TreeData {
    
    //initializing both character and frequency
    public char character;
    public int frequency;

    //calling constructors
    public TreeData(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public TreeData(int frequency) {
        this.frequency = frequency;
    }

    public TreeData(Character character, long sum) {
    }

    public TreeData(char key, Integer integer) {
    }

    //getters
    public char getCharacter() {
        return character;
    }
    public int getFrequency() {
        return frequency;
    }

    //setters
    public void setCharacter(char character) {
        this.character = character;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
