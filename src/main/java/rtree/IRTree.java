package rtree;

import java.util.Set;

public interface IRTree<T> {

    void add(Region region, T value);

    Set<Region> search(Region region);

    TreeNode<T> getRoot();

    boolean contain(Region region);

    void delete(Region region);

    Set<Region> getLeaves();

}
