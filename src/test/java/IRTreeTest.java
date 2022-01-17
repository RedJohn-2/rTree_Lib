import org.junit.Test;
import rtree.IRTree;
import rtree.Point;
import rtree.RTree;
import rtree.Region;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class IRTreeTest {

    @Test
    public void add() {
        IRTree<Integer> tree = new RTree<>(2, new Region(new Point(10, 10), new Point(100, 100)), 6,
                new Region(new Point(400, 400), new Point(500, 500)), 9);
        tree.add(new Region(new Point(340, 500), new Point(600, 700)), 23);
        boolean actual = tree.contain(new Region(new Point(340, 500), new Point(600, 700)));
        assertTrue(actual);
        assertEquals(tree.getRoot().getRegion(), new Region(new Point(10, 10), new Point(600, 700)));
    }

    @Test
    public void search() {
        IRTree<Integer> tree = new RTree<>(2, new Region(new Point(10, 10), new Point(100, 100)), 11,
                new Region(new Point(400, 400), new Point(500, 500)), 74);
        Set<Region> actual = tree.search(new Region(new Point(150, 150), new Point(500, 500)));
        Set<Region> expected = new HashSet<>();
        expected.add(new Region(new Point(400, 400), new Point(500, 500)));
        assertEquals(actual, expected);
    }

    @Test
    public void delete() {
        IRTree<Double> tree = new RTree<>(2, new Region(new Point(10, 10), new Point(100, 100)), 3.5,
                new Region(new Point(400, 400), new Point(500, 500)), 89.5);
        tree.add(new Region(new Point(340, 500), new Point(600, 700)), 17.0);
        tree.delete(new Region(new Point(10, 10), new Point(100, 100)));
        boolean actual = tree.contain(new Region(new Point(10, 10), new Point(100, 100)));
        assertFalse(actual);
        assertEquals(tree.getRoot().getRegion(), new Region(new Point(340, 400), new Point(600, 700)));
    }
}