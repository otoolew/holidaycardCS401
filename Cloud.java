package holidaycardCS401;


import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Cloud implements MyShape
{
	// Build a Cloud using composition.  
	private Ellipse2D circ1, circ2, circ3, circ4, circ5  ;
        private Rectangle2D perimeter;
	// X,Y position
	private int X, Y;

	// size of the Cloud
	private int size;

	private boolean isHighlighted;

	// Create a new Cloud.
        // the perimeter variable is set to allow the Cloud to have
	// area (for selecting purposes).
	public Cloud(int startX, int startY, int sz)
	{
		X = startX;
		Y = startY;
		size = sz;		
		
		isHighlighted = false;
                setUp();
	}
        
        private void setUp()
	{
		circ1 = new Ellipse2D.Double(X-15,Y,size,size);
		circ2 = new Ellipse2D.Double(X-9,Y,3*size/2,3*size/2);
		circ3 = new Ellipse2D.Double(X,Y,2*size,2*size);
                circ4 = new Ellipse2D.Double(X+12,Y,3*size/2,3*size/2);
                circ5 = new Ellipse2D.Double(X+25,Y,size,size);
	}
//3*size/2,3*size/2
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}

	// Draw the Snowflake "onto" the Graphics2D parameter that is passed
	// in.  This method will be called from a JFrame or JPanel, which
	// is where the Graphics2D object originates.  Since a Snowflake is
	// really just 4 lines, draw each of the lines.  See the Line2D class
	// and the Shape interface for more information.
	public void draw(Graphics2D g)
	{
	if (!isHighlighted){
            g.setColor(Color.white);
            g.draw(circ1);
            g.draw(circ2);
            g.draw(circ3);
            g.draw(circ4);
            g.draw(circ5);
            g.fill(circ1);
            g.fill(circ2);
            g.fill(circ3);
            g.fill(circ4);
            g.fill(circ5);
        }else
            g.setColor(Color.red);
            g.draw(circ1);
            g.draw(circ2);
            g.draw(circ3);
            g.draw(circ4);
            g.draw(circ5);
	}

	// All this method is doing is resetting the X and Y coordinates,
	// and then updating the lines and rectangle to reflect the new
	// location.
	public void move(int x, int y)
	{
		X = x;
		Y = y;
		circ1.setFrame(X-15,Y,size,size);
		circ2.setFrame(X-9,Y,3*size/2,3*size/2);
		circ3.setFrame(X,Y,2*size,2*size);
                circ4.setFrame(X+12,Y,3*size/2,3*size/2);
                circ5.setFrame(X+25,Y,size,size);
		perimeter = new Rectangle2D.Double(X-size,Y-size,size,size);
	}

	// Note the somewhat sneaky way this is implemented.  Note that the
	// move method reconfigures the figure using the current size.  So
	// just call move() using the same X,Y values (i.e. moving 0) which
	// ends up resizing the figure.
	public void resize(int newsize)
	{
		size = newsize;
		move(X,Y);
	}
	
	// Note how simple this method is, since the Rectangle2D class
	// already has a contains() method that works as we want it to.
	public boolean contains(double x, double y)
	{
		if (circ1.contains(x,y))
			return true;
		if (circ2.contains(x,y))
			return true;
                if (circ3.contains(x,y))
			return true;
                if (circ4.contains(x,y))
			return true;
                if (circ5.contains(x,y))
			return true;
		return false;
	}

	// Note how this string is formatted.  Your other classes should
	// format the string in the same way.
	public String saveData()
	{
		return ("Snowflake:" + X + ":" + Y + ":" + size);
	}

    
}