import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * @author Sanjana Goli and Rohith Mandavilli -- updated helperFindInCircle and findInCircle methods for extra credit
 */
public class PointQuadtreeExtra<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtreeExtra<E> c1, c2, c3, c4;	// children
	private List<E> check;
	private int rectWidth = 10;
	private int rectHeight = 15;

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtreeExtra(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}
	public List<E> getCheck()
	{
		return check;
	}
	
	public int getRectWidth()
	{
		return rectWidth;
	}
	
	public int getRectHeight()
	{
		return rectHeight;
	}
	public void setRectWidth(int r)
	{
		rectWidth = r;
	}
	public void setRectHeight(int r)
	{
		rectHeight = r;
	}
	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtreeExtra<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 * Point inserted needs to be in order
	 */
	//---done
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		double x = p2.getX();
		double y = p2.getY();
		E topLeft = null;
		E bottomRight = null;
		if(x>x1 && x < point.getX())								//is x on left side
		{
			if(y>y1 && y<point.getY())								//is y on top
			{
				if(hasChild(2))											//quad 2!
					c2.insert(p2);
				else
				{
					topLeft = getTopLeft(point, 2);
					bottomRight = getBottomRight(point, 2);
					c2 = new PointQuadtreeExtra(p2, (int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY());				//helper method for each corner
				}
			}
			else if(y>point.getY() && y<y2)							//is y on bottom
			{
				if(hasChild(3))											//quad 3!
					c3.insert(p2);
				else
				{
					topLeft = getTopLeft(point, 3);
					bottomRight = getBottomRight(point, 3);
					c3 = new PointQuadtreeExtra(p2, (int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY());	
				}
			}
		}
		else if(x > point.getX() && x < x2)						//is x on right side
		{
			if(y>y1 && y<point.getY())								//is y on top
			{
				if(hasChild(1))											//quad 1!				
					c1.insert(p2);
				else
				{
					topLeft = getTopLeft(point, 1);
					bottomRight = getBottomRight(point, 1);
					c1 = new PointQuadtreeExtra(p2, (int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY());
				}
			}
			else if(y>point.getY() && y<y2)						//is y on bottom
			{
				if(hasChild(4))											//quad 4!
					c4.insert(p2);
				else
				{
					topLeft = getTopLeft(point, 4);
					bottomRight = getBottomRight(point, 4);
					c4 = new PointQuadtreeExtra(p2, (int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY());	
				}
			}
		}
		
	}
	
	public E getTopLeft(E point, int quad)
	{
		E p = (E) new Dot(0,0);
		if(quad == 1)
		{
			p.setX(point.getX());
			p.setY(y1);
		}
		if(quad ==2)
		{
			p.setX(x1);
			p.setY(y1);
		}
		if(quad == 3)
		{
			p.setY(point.getY());
			p.setX(x1);
		}
		if(quad == 4)
		{
			p.setX(point.getX());
			p.setY(point.getY());
		}
		
		return p;
		
	}
	public E getBottomRight(E point, int quad)
	{
		E p = (E) new Dot(0,0);
		if(quad ==1)
		{
			p.setX(x2);
			p.setY(point.getY());
		}
		if(quad == 2)
		{
			p.setX(point.getX());
			p.setY(point.getY());
		}
		if(quad == 3)
		{
			p.setX(point.getX());
			p.setY(y2);
		}
		if(quad == 4)
		{
			p.setX(x2);
			p.setY(y2);
		}
		return p;
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		
		int num = 1;
		if (c1 != null) num += c1.size();
		if (c2 != null) num += c2.size();
		if (c3 != null) num += c3.size();
		if (c4 != null) num += c4.size();
		return num;
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	//---done
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		List<E> points = new ArrayList<E>();
		addToAllPoints(points);
		
		return points;
		
	}	
	//accumulator to help with allPoints() method
	private void addToAllPoints(List<E> points) {
		points.add(point);
		if(c1 != null) c1.addToAllPoints(points);
		if(c2 != null) c2.addToAllPoints(points);
		if(c3 != null) c3.addToAllPoints(points);
		if(c4 != null) c4.addToAllPoints(points);
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	//find in circle takes in checked and shape parameters for the extra credit in order to update which points have been checked based on the shape
	public List<E> findInCircle(double cx, double cy, double cr, List<E> checked, boolean query, char s) {
		// TODO: YOUR CODE HERE
		List<E> points = new ArrayList<E>();
		helperFindInCircle(points, cx, cy, cr, checked, query, s);
		check = checked;
		return points;
		
	}
	
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		List<E> points = new ArrayList<E>();
		helperFindInCircle(points, cx, cy, cr);
		return points;
		
	}
	
	private void helperFindInCircle(List<E> points, double cx, double cy, double cr) {
		
		if(Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2))
		{
			
			if(Geometry.pointInCircle(point.getX(), point.getY(), (int)cx, (int)cy, cr)) 
				points.add((E)new Blob(point.getX(), point.getY()));
			if(c1 != null) c1.helperFindInCircle(points, cx, cy, cr);
			if(c2 != null) c2.helperFindInCircle(points, cx, cy, cr);
			if(c3 != null) c3.helperFindInCircle(points, cx, cy, cr);
			if(c4 != null) c4.helperFindInCircle(points, cx, cy, cr);
		}
	}
	
	
	
	
	// TODO: YOUR CODE HERE for any helper methods
	
	//accumulator method for points and checked --> used for findInCircle method
	private void helperFindInCircle(List<E> points, double cx, double cy, double cr, List<E> checked, boolean query, char s) {
		if(Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2))
		{
			if(query == true)
			{
				checked.add((E)new Dot(point.getX(), point.getY()));
			}
			//Extra Credit
			//checks specifically if the point is in the rectangle or circle depending on what shape is passed in 
			//continues to recursively check for all children
			if(s == 'c' && Geometry.pointInCircle(point.getX(), point.getY(), (int)cx, (int)cy, cr)) 
				points.add((E)new Dot(point.getX(), point.getY()));
			else if(s == 'r' && GeometryExtra.pointInRectangle(point.getX(), point.getY(), (int) cx, (int) cy, rectWidth, rectHeight))
				points.add((E)new Dot(point.getX(), point.getY()));
			if(c1 != null) c1.helperFindInCircle(points, cx, cy, cr, checked, query, s);
			if(c2 != null) c2.helperFindInCircle(points, cx, cy, cr, checked, query, s);
			if(c3 != null) c3.helperFindInCircle(points, cx, cy, cr, checked, query, s);
			if(c4 != null) c4.helperFindInCircle(points, cx, cy, cr, checked, query, s);
		}
	}

}
