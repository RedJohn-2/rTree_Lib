package rtree.utils;
import rtree.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RTreeUtils {
    public static Region findMinRoundRegion(Region[] regions) {
        double maxX = 0;
        double maxY = 0;
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;

        for (Region region : regions) {
            if (region.getMinP().getX() < minX) {
                minX = region.getMinP().getX();
            }

            if (region.getMinP().getY() < minY) {
                minY = region.getMinP().getY();
            }

            if (region.getMaxP().getX() > maxX) {
                maxX = region.getMaxP().getX();
            }

            if (region.getMaxP().getY() > maxY) {
                maxY = region.getMaxP().getY();
            }
        }

        return new Region(new Point(minX, minY), new Point(maxX, maxY));
    }

    public static boolean isCrossing(Region rec1, Region rec2) {
        return !(rec1.getMinP().getX() > rec2.getMaxP().getX()) &&
                !(rec1.getMaxP().getX() < rec2.getMinP().getX()) &&
                !(rec1.getMaxP().getY() < rec2.getMinP().getY()) &&
                !(rec1.getMinP().getY() > rec2.getMaxP().getY());
    }

    public static RTree<String> readFromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        Scanner scanner;
        int index = 0;
        List<Region> regions = new ArrayList<>();
        List<String> values = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter("\\s+");
            Region rect = new Region(new Point(0, 0), new Point(0, 0));
            while (scanner.hasNext()) {
                String x = scanner.next();
                if (index == 0) {
                    rect.getMinP().setY(Double.parseDouble(x));
                } else if (index == 1) {
                    rect.getMaxP().setY(Double.parseDouble(x));
                } else if (index == 2) {
                    rect.getMinP().setX(Double.parseDouble(x));
                } else if (index == 3) {
                    rect.getMaxP().setX(Double.parseDouble(x));
                } else {
                    values.add(x);
                }
                index++;
            }
            regions.add(rect);
            index = 0;
        }
        reader.close();
        return createRTree(regions.toArray(new Region[0]), values.toArray(new String[0]));
    }

    private static RTree<String> createRTree(Region[] regions, String[] values) {
        if (regions.length < 2) return null;
        RTree<String> tree = new RTree<>(2, regions[0], values[0], regions[1], values[1]);
        for (int i = 2; i < regions.length && i < values.length; i++) {
            tree.add(regions[i], values[i]);
        }
        return tree;
    }
}
