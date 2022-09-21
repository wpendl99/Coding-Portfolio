package spell;

public class Node implements INode {
    private int count;
    private Node[] children;

    public Node() {
        setCount(0);
        initChildren();
    }

    @Override
    public int getValue() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    public void addChild(char letter){
        int index = getIndex(letter);
        this.children[index] = new Node();
    }

    @Override
    public INode[] getChildren() {
        return children;
    }

    public void initChildren() {
        this.children = new Node[26];
    }

    public Node nodes(char letter){
        int index = getIndex(letter);
        if (children[index] != null){
            return children[index];
        }
        return null;
    }

    public static int getIndex(char letter){
        return letter - 'a';
    }

    public static char getLetter(int index){
        return (char)('a' + index);
    }
}
