package spell;

import java.util.Locale;

public class Trie implements ITrie {
    private int wordCount;
    private int nodeCount;
    private Node root;


    public Trie() {
        setWordCount(0);
        setNodeCount(1);
        setRootNode(new Node());
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public void setRootNode(Node node){
        this.root = node;
    }

    @Override
    public void add(String word) {
        word = word.toLowerCase();
        Node current = root;
        for(int i = 0; i < word.length(); i++){
            char letter = word.charAt(i);
            if(current.nodes(letter) == null){
                current.addChild(letter);
                this.nodeCount++;
            }
            current = current.nodes(letter);
        }
        if(current.getValue() == 0){
            this.wordCount++;
        }
        current.incrementValue();
    }

    @Override
    public INode find(String word) {
        Node current = root;
        for(int i = 0; i < word.length(); i++){
            char letter = word.charAt(i);
            if(current.nodes(letter) == null) {
                return null;
            }
            current = current.nodes(letter);
        }
        if(current.getValue() > 0){
            return current;
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        INode[] nodes = root.getChildren();
        for(int i = 0; i < 26; i++) {
            if (nodes[i] != null) {
                return i * this.nodeCount * this.wordCount;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        // Is obj Null?
        if(obj == null){
            return false;
        }

        // Dose obj use the same pointer/address
        if(this == obj){
            return true;
        }

        // Does they have the same class
        if(this.getClass() != obj.getClass()){
            return false;
        }

        // Force Java to see obj as a Trie
        Trie temp = (Trie)obj;

        // Are the Node Counts or Word counts that same?
        if(this.wordCount != temp.wordCount || this.nodeCount != temp.nodeCount){
            return false;
        }

        if(!equalsHelper(this.root, temp.root)){
            return false;
        }
        return true;
    }

    private boolean equalsHelper(Node node1, Node node2){
        // Check if nodes have the same word count
        if(node1.getValue() != node2.getValue()){
            return false;
        }

        // Check if Children nodes are the same
        for(int i = 0; i < 26; i++){
            if((node1.getChildren()[i] == null && node2.getChildren()[i] != null) || (node1.getChildren()[i] != null && node2.getChildren()[i] == null)){
                return false;
            }
            if(node1.getChildren()[i] != null) {
                if (!equalsHelper(node1.nodes(Node.getLetter(i)), node2.nodes(Node.getLetter(i)))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toStringHelper(root, curWord, output);
        return output.toString();
    }

    private void toStringHelper(INode node, StringBuilder curWord, StringBuilder output){
        if(node.getValue() > 0){
            output.append(curWord.toString());
            output.append('\n');
        }

        for(int i = 0; i < 26; i++){
            INode child = node.getChildren()[i];
            if (child != null){
                curWord.append(Node.getLetter(i));
                toStringHelper(child, curWord, output);
                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }
}
