import java.util.*;

class SnapshotArray {

    private ArrayList<TreeMap<Integer, Integer>> lst;

    private int versionID;

    public SnapshotArray(int length) {
        // Initialize ArrayList with Empty TreeMaps in it
        lst = new ArrayList<TreeMap<Integer, Integer>>(length);
        versionID = 0;

        // Fill every index of List with Empty Maps
        for (int i = 0; i < length; i++) {
            lst.add(new TreeMap<Integer, Integer>());
        }

        // Set inital value of every index to 0 as specified in problem definition
        for (TreeMap<Integer, Integer> map : lst) {
            map.put(versionID, 0);
        }
    }

    // Set's the current snapshot version with the desired value
    public void set(int index, int val) {
        lst.get(index).put(versionID, val);
    }

    // Increments snapshot version and returns the previously snapshot version
    public int snap() {
        versionID++;
        return versionID - 1;
    }

    //
    public int get(int index, int snap_id) {
        return lst.get(index).floorEntry(snap_id).getValue();
    }
}

/**
 * Your SnapshotArray object will be instantiated and called as such:
 * SnapshotArray obj = new SnapshotArray(length);
 * obj.set(index,val);
 * int param_2 = obj.snap();
 * int param_3 = obj.get(index,snap_id);
 */