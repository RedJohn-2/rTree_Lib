package app;
import rtree.IRTree;
import rtree.Region;
import rtree.TreeNode;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicDp extends JPanel {
    private IRTree<String> tree = null;
    private List<Color> colors;

    public GraphicDp() {
        colors = new ArrayList<>();
    }

    public void setTree(IRTree<String> tree) {
        this.tree = tree;
        repaint();
    }

    public void add(Region region, String value) {
        tree.add(region, value);
        repaint();
    }

    public void delete(Region region) {
        tree.delete(region);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        addColors();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (tree != null) {
            drawRoot(g, tree.getRoot(), getHeight());
            drawBorder(g, tree.getRoot(), getHeight());
        }
    }

    private void addColors() {
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
    }

    private void drawRoot(Graphics g, TreeNode<String> node, int h) {
        g.setColor(colors.get(0));
        g.fillRect((int)node.getRegion().getMinP().getX(), (int)(h - node.getRegion().getMaxP().getY()),
                (int)(node.getRegion().getMaxP().getX() - node.getRegion().getMinP().getX()),
                (int)(node.getRegion().getMaxP().getY() - node.getRegion().getMinP().getY()));
        drawTree(g, node, 1, h);
    }

    private void drawTree(Graphics g, TreeNode<String> node, int index, int h) {
        g.setColor(colors.get(index));
        if (!node.isLeaf()) {
            for (TreeNode<String> entry : node.getEntries()) {
                g.fillRect((int)entry.getRegion().getMinP().getX(), (int)(h - entry.getRegion().getMaxP().getY()),
                        (int)(entry.getRegion().getMaxP().getX() - entry.getRegion().getMinP().getX()),
                        (int)(entry.getRegion().getMaxP().getY() - entry.getRegion().getMinP().getY()));
            }

            for (TreeNode<String> entry : node.getEntries()) {
                drawTree(g, entry, index + 1, h);
            }
        }
    }

    private void drawBorder(Graphics g, TreeNode<String> node, int h) {
        g.setColor(Color.BLACK);
        g.drawRect((int)node.getRegion().getMinP().getX(), (int)(h - node.getRegion().getMaxP().getY()),
                (int)(node.getRegion().getMaxP().getX() - node.getRegion().getMinP().getX()),
                (int)(node.getRegion().getMaxP().getY() - node.getRegion().getMinP().getY()));
        if (node.getValue() != null) {
            g.drawString(node.getValue(), (int)node.getRegion().getMinP().getX() + 1, (int)(h - node.getRegion().getMaxP().getY()) + 10);
        }
        if (!node.isLeaf()) {
            for (TreeNode<String> entry : node.getEntries()) {
                drawBorder(g, entry, h);
            }
        }
    }
}
