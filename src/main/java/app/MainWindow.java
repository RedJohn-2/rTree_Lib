package app;
import rtree.Point;
import rtree.RTree;
import rtree.Region;
import rtree.TreeNode;
import rtree.utils.RTreeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final TreeDp dp;
    private final GraphicDp gdp = new GraphicDp();
    private TextField bottom = new TextField("Нижняя граница");
    private TextField top = new TextField("Верхняя граница");
    private TextField left = new TextField("Левая граница");
    private TextField right = new TextField("Правая граница");
    private TextField minEntries = new TextField("Минимальное количество узлов");
    private TextField value = new TextField("Значение");
    private TextArea answer = new TextArea();
    private TextArea tree = new TextArea();

    public MainWindow() throws HeadlessException {
        dp = new TreeDp();
        JButton createTree = new JButton("Создать дерево");
        JButton addTree = new JButton("Добавить регион");
        JButton searchRegions = new JButton("Найти пересекаемые регионы");
        JButton deleteRegions = new JButton("Удалить регион");
        JButton containRegions = new JButton("Проверить существование региона");
        JButton loadButton = new JButton("Загрузить из файла");
        answer.setSize(15, 100);
        dp.add(createTree);
        dp.add(addTree);
        dp.add(deleteRegions);
        dp.add(containRegions);
        dp.add(searchRegions);
        dp.add(loadButton);
        dp.add(bottom);
        dp.add(top);
        dp.add(left);
        dp.add(right);
        dp.add(minEntries);
        dp.add(value);
        dp.add(answer);
        dp.add(tree);

        addTree.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.add(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))), value.getText());
                gdp.add(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))), value.getText());
                tree.setText(textTree(dp.getTree().getRoot(), "1."));
            } else {
                dp.addStartNodes(new TreeNode<>(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))), value.getText()));
            }
            resetTextField();
        });

        deleteRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.delete(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
                gdp.delete(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))));
                resetTextField();
                tree.setText(textTree(dp.getTree().getRoot(), "1."));
            }
        });

        searchRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                StringBuilder ans = new StringBuilder();
                for (Region region : dp.getTree().search(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText()))))) {
                    ans.append("{(").append(region.getMinP().getY()).append("; ").append(region.getMaxP().getY()).append(") ").append("(").append(region.getMinP().getX()).append("; ").append(region.getMaxP().getX()).append(")}\n");
                }

                answer.setText(ans.toString());
                resetTextField();
            }
        });

        containRegions.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                answer.setText("" + dp.getTree().contain(new Region(new Point(Double.parseDouble(left.getText()), Double.parseDouble(bottom.getText())),
                        new Point(Double.parseDouble(right.getText()), Double.parseDouble(top.getText())))));
                resetTextField();
            }
        });

        createTree.addActionListener(e -> {
            if (dp.isTreeCreated()) {
                dp.setTree(new RTree<>(Integer.parseInt(minEntries.getText()), dp.getStartNodes().get(0).getRegion(),
                        dp.getStartNodes().get(0).getValue(), dp.getStartNodes().get(1).getRegion(), dp.getStartNodes().get(1).getValue()));
                gdp.setTree(new RTree<>(Integer.parseInt(minEntries.getText()), dp.getStartNodes().get(0).getRegion(),
                        dp.getStartNodes().get(0).getValue(), dp.getStartNodes().get(1).getRegion(), dp.getStartNodes().get(1).getValue()));
                resetTextField();
                tree.setText(textTree(dp.getTree().getRoot(), "1."));
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fileOpen = new JFileChooser();
            int ret = fileOpen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpen.getSelectedFile();
                try {
                    dp.setTree(RTreeUtils.readFromFile(file.getAbsolutePath()));
                    gdp.setTree(RTreeUtils.readFromFile(file.getAbsolutePath()));
                    tree.setText(textTree(dp.getTree().getRoot(), "1."));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bottom.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        top.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        left.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        right.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        minEntries.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        value.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                TextField source = (TextField)e.getComponent();
                source.setText("");
            }
        });

        createJFrame();

        this.add(dp);

    }

    private void createJFrame() {
        JFrame mw1 = new JFrame();
        mw1.add(gdp);
        mw1.setSize(800, 800);
        mw1.setVisible(true);
    }

    private void resetTextField() {
        bottom.setText("Нижняя граница");
        top.setText("Верхняя граница");
        left.setText("Левая граница");
        right.setText("Правая граница");
        minEntries.setText("Минимальное количество узлов");
        value.setText("Значение");
    }

    private String textTree(TreeNode<String> node, String level) {
        StringBuilder line = new StringBuilder(level + " (" + node.getRegion().getMinP().getY() + " " + node.getRegion().getMaxP().getY() + ")" +
                "(" + node.getRegion().getMinP().getX() + " " + node.getRegion().getMaxP().getX() + ")");
        if (node.getValue() != null) {
            line.append("  -->  ").append(node.getValue());
        }
        if (node.isLeaf()) return line.toString();
        for (int i = 0; i < node.getSize(); i++) {
            if (node.getSize() > i) {
                line.append("\n");
                line.append(" ".repeat(level.length()));
                line.append(textTree(node.getEntries().get(i), level + (i + 1) + '.'));

            }
        }
        return line.toString();
    }

}
