package rtree;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private Region region;
    private TreeNode<T> parent = null;
    private List<TreeNode<T>> entries = new ArrayList<>();
    private T value = null;

    public TreeNode(Region region) {
        this.region = region;
    }

    public TreeNode(Region region, TreeNode<T> parent) {
        this.region = region;
        this.parent = parent;
    }

    public TreeNode(Region region, T value) {
        this.region = region;
        this.value = value;
    }

    public TreeNode(Region region, TreeNode<T> parent, T value) {
        this.region = region;
        this.parent = parent;
        this.value = value;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return entries.isEmpty();
    }


    public List<Region> getEntryRegion() {
        List<Region> regions = new ArrayList<>();
        for (TreeNode<T> node : entries) {
            regions.add(node.getRegion());
        }

        return regions;
    }

    public List<TreeNode<T>> getEntries() {
        return entries;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getSize() {
        return entries.size();
    }
}
