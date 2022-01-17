package rtree;
import rtree.utils.RTreeUtils;
import java.util.*;

public class RTree<T> implements IRTree<T> {
    private final int minEntries;
    private final int maxEntries;
    private TreeNode<T> root;

    public RTree(int minEntries, Region rec1, T v1, Region rec2, T v2) {
        this.minEntries = minEntries;
        maxEntries = minEntries * 2;
        root = new TreeNode<>(RTreeUtils.findMinRoundRegion(new Region[]{rec1, rec2}));
        addRegion(root, new TreeNode<>(rec1, root, v1));
        addRegion(root, new TreeNode<>(rec2, root, v2));
    }

    private void addRegion(TreeNode<T> parent, TreeNode<T> node) {
        parent.getEntries().add(node);
        parent.setRegion(RTreeUtils.findMinRoundRegion(new Region[]{parent.getRegion(), node.getRegion()}));
        node.setParent(parent);
        if (parent.getSize() > maxEntries) {
            if (parent == root) {
                root = new TreeNode<>(parent.getRegion());
                root.getEntries().add(parent);
                parent.setParent(root);
            }
            partition(parent.getParent(), parent);
        }
    }

    private void deleteRegion(TreeNode<T> parent, TreeNode<T> node) {
        parent.getEntries().remove(node);
        rebuildTree(parent);
        if (parent == root && parent.getSize() == 1) {
            root = parent.getEntries().get(0);
        } else if (parent.getSize() < minEntries) {
            merge(parent);
        }
    }
    private void rebuildTree(TreeNode<T> node) {
        node.setRegion(RTreeUtils.findMinRoundRegion(node.getEntryRegion().toArray(new Region[0])));
        if (node == root) return;
        rebuildTree(node.getParent());
    }

    private void merge(TreeNode<T> node) {
        double minArea = Double.POSITIVE_INFINITY;
        TreeNode<T> closeNode = node.getParent().getEntries().get(0);
        Region minRegion = closeNode.getRegion();
        for (TreeNode<T> entry : node.getParent().getEntries()) {
            if (entry.equals(node)) continue;
            if (RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea() < minArea) {
                minArea = RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea();
                closeNode = entry;
                minRegion = RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), entry.getRegion()});
            }
        }

        closeNode.setRegion(minRegion);
        closeNode.getEntries().addAll(node.getEntries());

        if (closeNode.getSize() > maxEntries) {
            partition(node.getParent(), closeNode);
        }

        deleteRegion(node.getParent(), node);

    }

    @Override
    public Set<Region> search(Region region) {
        return new HashSet<>(searchRegion(root, region));
    }

    private Set<Region> searchRegion(TreeNode<T> node, Region region) {
        if (RTreeUtils.isCrossing(region, node.getRegion())) {
            Set<Region> leaves = new HashSet<>();
            if (node.isLeaf()) {
                leaves.add(node.getRegion());
            } else {
                for (TreeNode<T> entry : node.getEntries()) {
                    leaves.addAll(searchRegion(entry, region));
                }
            }

            return leaves;
        }

        return new HashSet<>();

    }

    @Override
    public void add(Region region, T value) {
        if (!contain(region))
        addNode(root, new TreeNode<>(region, value));
    }

    private void addNode(TreeNode<T> node, TreeNode<T> leaf) {
        if (node.getEntries().get(0).isLeaf() || node.getEntries().get(0) == null) {
            addRegion(node, leaf);
            return;
        }

        double minArea = Double.POSITIVE_INFINITY;
        TreeNode<T> nodeWithMinArea = node.getEntries().get(0);
        for (TreeNode<T> entry: node.getEntries()) {
            if (RTreeUtils.findMinRoundRegion(new Region[]{leaf.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea() < minArea) {
                nodeWithMinArea = entry;
                minArea = RTreeUtils.findMinRoundRegion(new Region[]{leaf.getRegion(), entry.getRegion()}).getArea() - entry.getRegion().getArea();
            }
        }
        addNode(nodeWithMinArea, leaf);
        node.setRegion(RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), nodeWithMinArea.getRegion()}));
    }

    private void partition(TreeNode<T> parent, TreeNode<T> node) {
        double maxSize = 0;
        TreeNode<T> firstNode = node.getEntries().get(0);
        TreeNode<T> secondNode = node.getEntries().get(1);
        for (int i = 0; i < node.getEntries().size() - 1; i++) {
            for (int j = 1; j < node.getEntries().size(); j++) {
                if (RTreeUtils.findMinRoundRegion(new Region[]{node.getEntries().get(i).getRegion(),
                        node.getEntries().get(j).getRegion()}).getArea() > maxSize) {
                    firstNode = node.getEntries().get(i);
                    secondNode = node.getEntries().get(j);
                    maxSize = RTreeUtils.findMinRoundRegion(new Region[]{node.getEntries().get(i).getRegion(),
                            node.getEntries().get(j).getRegion()}).getArea();
                }
            }
        }
        TreeNode<T> firstNewNode = new TreeNode<>(firstNode.getRegion(), parent);
        TreeNode<T> secondNewNode = new TreeNode<>(secondNode.getRegion(), parent);

        for(TreeNode<T> entry : node.getEntries()) {
            if (firstNewNode.getSize() >= minEntries + 1) {
                addRegion(secondNewNode, entry);
                continue;
            } if (secondNewNode.getSize() >= minEntries + 1) {
                addRegion(firstNewNode, entry);
                continue;
            } if (RTreeUtils.findMinRoundRegion(new Region[]{entry.getRegion(), firstNewNode.getRegion()}).getArea() - firstNewNode.getRegion().getArea()
                   > RTreeUtils.findMinRoundRegion(new Region[]{entry.getRegion(), secondNewNode.getRegion()}).getArea() - secondNewNode.getRegion().getArea()) {
               addRegion(secondNewNode, entry);
           } else {
               addRegion(firstNewNode, entry);
           }
        }
        parent.getEntries().remove(node);
        addRegion(parent, firstNewNode);
        addRegion(parent, secondNewNode);
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public boolean contain(Region region) {
        return contain(root, region);
    }

    private boolean contain(TreeNode<T> node, Region region) {
        if (node.isLeaf()) {
            return region.equals(node.getRegion());
        }

        if (RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), region}).getArea() > node.getRegion().getArea()) {
                return false;
        }

        for (TreeNode<T> entry : node.getEntries()) {
            if (contain(entry, region)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void delete(Region region) {
        delete(root, region);
    }

    private boolean delete(TreeNode<T> node, Region region) {
        if (node.isLeaf()) {
            if (region.equals(node.getRegion())) {
                deleteRegion(node.getParent(), node);
                return true;
            }
            return false;
        }

        if (RTreeUtils.findMinRoundRegion(new Region[]{node.getRegion(), region}).getArea() > node.getRegion().getArea()) {
            return false;
        }

        for (TreeNode<T> entry : node.getEntries()) {
            if (delete(entry, region)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<Region> getLeaves() {
        return new HashSet<>(getLeaves(root));
    }

    private Set<Region> getLeaves(TreeNode<T> node) {
        Set<Region> leaves = new HashSet<>();
        if (node.isLeaf()) {
            leaves.add(node.getRegion());
        } else {
            for (TreeNode<T> entry : node.getEntries()) {
                leaves.addAll(getLeaves(entry));
            }
        }

        return leaves;
    }

}
