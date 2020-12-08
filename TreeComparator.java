import java.util.Comparator;

/**
 * @author Mark Tao, produce a Huffman tree that compresses and decompresses all bytes of a file
 */

//compares the frequency elements in the priority queue.
public class TreeComparator implements Comparator<BinaryTree<TreeData>> {

    @Override
    public int compare(BinaryTree<TreeData> o1, BinaryTree<TreeData> o2) {
        if (o1.getData().getFrequency() < o2.getData().getFrequency()) {
            return -1;
        }
        else if (o1.getData().getFrequency() > o2.getData().getFrequency()) {
            return 1;
        }
        return 0;
    }
}
