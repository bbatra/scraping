package Practice;

/**
 * Created by Bharat on 02/03/15.
 */
public class BST
{
    int size=0;
    Node root;

    public BST(int rootVal)
    {
        Node r = new Node(null, rootVal);
        this.root = r;
        this.size++;
    }

    public void addNode(int val)
    {
        if(this.root!=null)
        {

        }
        else
        {
            Node curr = new Node(null, val );

        }
    }
}
class Node
{
    Node parent, left, right;
    int value;
    public Node(Node p, int val)
    {
        this.parent = p;
        this.value = val;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
