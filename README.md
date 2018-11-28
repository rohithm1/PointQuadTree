# PointQuadTree
Purpose of the project is to detect blobs when they run into each other and when clicked on.

Suppose we have a bunch of animated blobs. How can we determine if the mouse was pressed in one of them? How can we detect when two of them bump into each other? We've seen the inefficient approach to determining which was clicked on — test each one. And we can imagine an inefficient approach to detecting collisions — look at all pairs. But we can do better, and would need to do better in some applications requiring speed.

Just as a binary search tree enables faster look-up than linear search in one dimension (based on some notion of less than or greater than), a point quadtree enables faster look-up in two dimensions.
