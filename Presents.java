package holidaycardCS401;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Presents implements MyShape
{
	// Represent a Tree in two parts -- a Polygon for the top part 
	// (the branches) and another Polygon for the trunk.  Since the
	// trunk is rectangular, a Rectangle2D could have been used, but
	// to keep consistent (especially with the move() method) I used
	// Polygon objects for both.
	private Polygon ribbon;
        private Polygon ribbon2;
	private Polygon base;
        private Line2D line1, line2, line3, line4;
        private Rectangle2D perimeter;
        
	// X, Y and size instance variables
	private int X, Y;
	private int size;

	private boolean isHighlighted;
	
	// Create a new Tree object.  Note how the Polygons are built,
	// adding one point at a time to each.  If you plot the points out
	// on paper you will see how the shapes are formed.  This method
	// uses a setUp method as shown below to allow for the Tree to
	// be regenerated internally (i.e. outside the constructor)
	public Presents(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;
                line1 = new Line2D.Double(X-size/2,Y,X-size/4,Y-size/4);
		line2 = new Line2D.Double(X-size/2,Y,X-size/8,Y-size/8);
		line3 = new Line2D.Double(X-size/2,Y,X-2*size/3,Y-size/4);
		line4 = new Line2D.Double(X-size/2,Y,X-4*size/5,Y-size/8);
		perimeter = new Rectangle2D.Double(X-size,Y-size,size,size);
               
		setUp();
	}
	
	// Create the actual parts of the Present.  
	private void setUp()
	{
             
		ribbon = new Polygon();
		ribbon.addPoint(X-3*size/8,Y);
		ribbon.addPoint(X-5*size/8,Y);
		ribbon.addPoint(X-5*size/8,Y+size);
                ribbon.addPoint(X-3*size/8,Y+size);
                
                ribbon2 = new Polygon();
		ribbon2.addPoint(X-size,Y+3*size/8);
		ribbon2.addPoint(X-size,Y+5*size/8);
		ribbon2.addPoint(X,Y+5*size/8);
                ribbon2.addPoint(X,Y+3*size/8);
                
                
		base = new Polygon();
		base.addPoint(X,Y);
		base.addPoint(X-size,Y);               
		base.addPoint(X-size,Y+size);
		base.addPoint(X,Y+size);
	}

	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	// The Polygon class can also be drawn with a predefined method in
	// the Graphics2D class.  There are two versions of this method:
	// 1) draw() which only draws the outline of the shape
	// 2) fill() which draws a solid shape
	// In this class the draw() method is used when the object is
	// highlighted.
	// The colors chosen are RGB colors I looked up on the Web.  Take a
	// look and use colors you like for your shapes.
	public void draw(Graphics2D g)
	{
                if (!isHighlighted)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
                        g.draw(line1);
                        g.draw(line2);
                        g.draw(line3);
                        g.draw(line4);
                
		g.setColor(new Color(0,180,0));
		if (isHighlighted)
			g.draw(base);
		else
			g.fill(base);
		g.setColor(new Color(210,0,0));
		if (isHighlighted)
			g.draw(ribbon);
		else
			g.fill(ribbon);
                
                g.setColor(new Color(210,0,0));
		if (isHighlighted)
			g.draw(ribbon2);
		else
			g.fill(ribbon2);
	}

	// Looking at the API, we see that Polygon has a translate() method
	// which can be useful to us.  All we have to do is calculate the
	// difference of the new (x,y) and the old (X,Y) and then call
	// translate() for both parts of the tree.
	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
                
		ribbon.translate(deltaX, deltaY);
                ribbon2.translate(deltaX, deltaY);                
		base.translate(deltaX, deltaY);
                
                line1.setLine(X-size/2,Y,X-size/4,Y-size/4);
		line2.setLine(X-size/2,Y,X-size/8,Y-size/8);
		line3.setLine(X-size/2,Y,X-2*size/3,Y-size/4);
		line4.setLine(X-size/2,Y,X-4*size/5,Y-size/8);
               
		perimeter.setFrame(X-size,Y-size,size,size);
                
		X = x;
		Y = y;
	}

	// Polygon also has a contains() method, so this method is also
	// simple
	public boolean contains(double x, double y)
	{
		if (ribbon.contains(x,y))
			return true;
                if (ribbon2.contains(x,y))
			return true;
		if (base.contains(x,y))
			return true;
                if (perimeter.contains(x,y))
			return true;
                
		return false;
	}

	// The move() method for the Polygons that are in Tree are not
	// reconfigured like in Snowflake, so we can't use the trick used
	// there.  Instead, we just change the size and call setUp() to
	// regenerate all of the objects.
	public void resize(int newsize)
	{
		size = newsize;
		setUp();
	}

	// Note again the format
	public String saveData()
	{
		return ("Presents:" + X + ":" + Y + ":" + size);
	}
}