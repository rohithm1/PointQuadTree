import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 * @author Sanjana Goli and Rohith Mandavilli -- updated draw and findColliders
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe

	private List<Blob> blobs;						// all the blobs
	private List<Blob> colliders;					// the blobs who collided at this step
	private char blobType = 'b';						// what type of blob to create
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);

		blobs = new ArrayList<Blob>();
		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds an blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}			
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
			if(k == 'c')
				colliders = new ArrayList<Blob>();
		}
		else if (k == 't') {
			test();
		}
		else { // set the type for new blobs
			blobType = k;			
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		// Ask all the blobs to draw themselves.
		// Ask the colliders to draw themselves in red.
		for(Blob b: blobs) 
		{
			//draws the blob red if it's contained in colliders
			if((colliders != null) && (colliders.contains(b))) 
			{
				g.setColor(Color.RED);
				b.draw(g);
			} else {
				g.setColor(Color.BLACK);
				b.draw(g);
			}
		}
		
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 */
	private void findColliders() {
		// TODO: YOUR CODE HERE
		// Create the tree
		// For each blob, see if anybody else collided with it
		PointQuadtree <Blob> tree = new PointQuadtree<Blob>(blobs.get(0), 0, 0, 800, 600); //creates the tree
		//adds the blobs from the list to the tree
		for(int i = 1; i < blobs.size(); i++) {
			tree.insert(blobs.get(i));

		}
		

		colliders = new ArrayList<Blob>();
		List <Blob> points = tree.allPoints();
		for(Blob b: points)
		{
			//creates a list of potential colliders for blob b
			List<Blob> pot = tree.findInCircle(b.getX(), b.getY(), 2*b.getR());
			
			if(pot.size() <= 1) pot.remove(0); //removing itself from list of potential colliders
			for(Blob checker: pot)
			{
				if(b.contains(checker.getX(), checker.getY()) && colliders != null)
				{
					//adds to the colliders if the blob and the checker intersect
					colliders.add(checker);
					colliders.add(b);
				}
			}
		}
	
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Check for collisions
		if (blobs.size() > 0) {
			findColliders();
			//remove any of the colliders from blob if they're still there
			if (collisionHandler=='d') {
				if(colliders != null)
				{
					blobs.removeAll(colliders);
					colliders = null;
				}
			}
		}
		// Now update the drawing
		repaint();
	}
	
	public void test() {
		//parallel blobs should not collide with each other because they are 20 pixels apart
		Blob b1 = new Bouncer(400, 300, width, height);
		Blob b2 = new Bouncer(400, 320, width, height);
		b1.setVelocity(5, 0);
		b2.setVelocity(5, 0);
		blobs.add(b1);
		blobs.add(b2);
		

		
		//blobs headed toward each other should collide and turn red
		Blob b3 = new Bouncer(400, 200, width, height);
		Blob b4 = new Bouncer(450, 200, width, height);
		b3.setVelocity(5, 0);
		b4.setVelocity(-5, 0);
		blobs.add(b3);
		blobs.add(b4);
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}
