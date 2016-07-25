package holidaycardCS401;


import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Cabin implements MyShape
{
	// Represent a Tree in two parts -- a Polygon for the top part 
	// (the branches) and another Polygon for the trunk.  Since the
	// trunk is rectangular, a Rectangle2D could have been used, but
	// to keep consistent (especially with the move() method) I used
	// Polygon objects for both.
        
	private Polygon roof;
	private Polygon base;
        private Polygon chimney;
        private Polygon door;
        private Polygon window;
        private Polygon window2;
        
        
		
	// X, Y and size instance variables
	private int X, Y;
	private int size;

	private boolean isHighlighted;
	
	// Create a new Cabin object.  Note how the Polygons are built,
	// adding one point at a time to each.  If you plot the points out
	// on paper you will see how the shapes are formed.  This method
	// uses a setUp method as shown below to allow for the Tree to
	// be regenerated internally (i.e. outside the constructor)
	public Cabin(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;

		setUp();
	}
	
	// Create the actual parts of the Tree.  For your shapes you
	// will likely use trial and error to get nice looking results
	// (I used a LOT of trial and error for mine).
	private void setUp()
                
	{
                           
		roof = new Polygon();
		roof.addPoint(X,Y);
		roof.addPoint(X-size,Y);
		roof.addPoint(X-size/2,Y-1*size/2);
                
		base = new Polygon();                
		base.addPoint(X,Y);
		base.addPoint(X-size,Y);               
		base.addPoint(X-size,Y+size);
		base.addPoint(X,Y+size);
                
                door = new Polygon();               
		door.addPoint(X-3*size/8,Y+size/2);
		door.addPoint(X-5*size/8,Y+size/2);
		door.addPoint(X-5*size/8,Y+size);
		door.addPoint(X-3*size/8,Y+size);
                
                chimney = new Polygon(); 
                chimney.addPoint(X,Y);
		chimney.addPoint(X-size/4,Y);
		chimney.addPoint(X-size/4,Y-size/2);
		chimney.addPoint(X,Y-size/2);
                
                window = new Polygon(); 
                window.addPoint(X-5*size/8,Y-2+size/2);
		window.addPoint(X-7*size/8,Y-2+size/2);
		window.addPoint(X-7*size/8,Y-2+size/4);
		window.addPoint(X-5*size/8,Y-2+size/4);
                
                window2 = new Polygon();               
                window2.addPoint(X-1*size/8,Y-2+size/2);
		window2.addPoint(X-3*size/8,Y-2+size/2);
		window2.addPoint(X-3*size/8,Y-2+size/4);
		window2.addPoint(X-1*size/8,Y-2+size/4);

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
		     
                g.setColor(new Color(100,0,0));
                    if (isHighlighted){
                g.draw(chimney);    
                    }else{
                g.fill(chimney);    
                    }  
            
                g.setColor(new Color(0,0,0));
                    if (isHighlighted){
                g.draw(roof);    
                    }else{
                g.fill(roof);    
                    }
                    
		g.setColor(new Color(61,43,3));
                    if (isHighlighted){
                g.draw(base);    
                    }else{
                g.fill(base);    
                    }
                    
                g.setColor(new Color(215,0,0));
                    if (isHighlighted){
                g.draw(door);    
                    }else{
                g.fill(door);    
                    }
                     
                g.setColor(new Color(175,175,225));
                    if (isHighlighted){
                g.draw(window);    
                    }else{
                g.fill(window);    
                    }
                
                g.setColor(new Color(175,175,225));
                    if (isHighlighted){
                g.draw(window2);    
                    }else{
                g.fill(window2);    
                    }               
		
	}

	// Looking at the API, we see that Polygon has a translate() method
	// which can be useful to us.  All we have to do is calculate the
	// difference of the new (x,y) and the old (X,Y) and then call
	// translate() for both parts of the tree.
	public void move(int x, int y)
	{
		int deltaX = x - X;
		int deltaY = y - Y;
		roof.translate(deltaX, deltaY);
		base.translate(deltaX, deltaY);
                door.translate(deltaX, deltaY);
                window.translate(deltaX, deltaY);
                window2.translate(deltaX, deltaY);
                chimney.translate(deltaX, deltaY);
		X = x;
		Y = y;
	}

	// Polygon also has a contains() method, so this method is also
	// simple
	public boolean contains(double x, double y)
	{
		if (roof.contains(x,y))
			return true;
		if (base.contains(x,y))
			return true;
                if (chimney.contains(x,y))
			return true;
                if (door.contains(x,y))
			return true;
                if (window.contains(x,y))
			return true;
                if (window2.contains(x,y))
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
		return ("Cabin:" + X + ":" + Y + ":" + size);
	}
}