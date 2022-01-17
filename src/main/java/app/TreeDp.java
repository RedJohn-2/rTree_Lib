package app;
import rtree.IRTree;
import rtree.Region;
import rtree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TreeDp extends JPanel {
    private IRTree<String> tree = null;
    private boolean treeCreated = false;
    private List<TreeNode<String>> startNodes = new ArrayList<>();
    private int height = 300;

    public TreeDp() {
    }

    public void addStartNodes(TreeNode<String> node) {
        startNodes.add(node);
        if (startNodes.size() == 2) {
            treeCreated = true;
        }
    }

    public IRTree<String> getTree() {
        return tree;
    }

    public void add(Region region, String value) {
        tree.add(region, value);
        repaint();
    }

    public void delete(Region region) {
        tree.delete(region);
        repaint();
    }

    public void setTree(IRTree<String> tree) {
        this.tree = tree;
        treeCreated = true;
        repaint();
    }

    public boolean isTreeCreated() {
        return treeCreated;
    }


    public List<TreeNode<String>> getStartNodes() {
        return startNodes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
