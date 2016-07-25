package holidaycardCS401;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import javax.security.auth.callback.ConfirmationCallback;
/**
 * University of Pittsburgh
 * CS401 Java Development
 * Fall 2014
 * @author William O'Toole
 */
// Create enum types that will be useful in the program
enum Figures {TREE,SNOWFLAKE,GREETING,CABIN,CLOUD,PRESENTS};
enum Mode {NONE,DRAW,SELECTED,MOVING, COPY};

// Code extracted from Oracle Java Example programs.  See link below for full code:
// http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintUIWindow.java
class thePrintPanel implements Printable
{
    JPanel panelToPrint;

    public int print(Graphics g, PageFormat pf, int page) throws PrinterException
    {
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform t = new AffineTransform();
        t.scale(0.9, 0.9);
        g2d.transform(t);
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now print the window and its visible contents */
        panelToPrint.printAll(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    public thePrintPanel(JPanel p)
    {
    	panelToPrint = p;
    }
}

public class Assig5 {
    
    private ShapePanel drawPanel;
    private JPanel buttonPanel;
    private JButton makeShape;
    private JRadioButton makeTree, makeFlake, makeGreet, makeCabin, makePresents, makeCloud;
    private ButtonGroup shapeGroup;
    private Figures currShape;
    private JLabel msg;
    private JMenuBar theBar;
    private JMenu fileMenu;
    private JMenuItem endProgram, printScene, load, save, newScene, saveAs;
    private JPopupMenu popper;
    private JMenuItem delete, resize, copy, paste, deSelect;
    private JFrame theWindow;
    MyShape copyShape, tmpShape;
    //private ArrayList<MyShape> copyList;
    private String[] shapeInfo, copyArray;
    private String fileName;
    private boolean copied=false;
    private boolean changesMade=false;
    private boolean saved= false;
    // This ArrayList is used to store the shapes in the program.
    // It is specified to be of type MyShape, so objects of any class
    // that implements the MyShape interface can be stored in here.
    // See Section 7.13 in your text for more info on ArrayList.
    private ArrayList<MyShape> shapeList;	
    private MyShape newShape;
    private PrintWriter saveFile;
    private File file;
    private Scanner scan;
    private JFileChooser fileCho;
            
    
    public Assig5()
    {
        drawPanel = new ShapePanel(640, 480);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2));

        makeShape = new JButton("Click to Draw");

        ButtonHandler bhandler = new ButtonHandler();
        makeShape.addActionListener(bhandler);

        buttonPanel.add(makeShape);
        msg = new JLabel("");
        buttonPanel.add(msg);

        makeTree = new JRadioButton("Tree", false);
        makeFlake = new JRadioButton("Snowflake", true);
        makeGreet = new JRadioButton("Greeting", false);
        makeCabin = new JRadioButton("Cabin", false);
        makePresents = new JRadioButton("Presents", false);
        makeCloud = new JRadioButton("Cloud", false);



        RadioHandler rhandler = new RadioHandler();
        makeTree.addItemListener(rhandler);
        makeFlake.addItemListener(rhandler);
        makeGreet.addItemListener(rhandler);
        makeCabin.addItemListener(rhandler);
        makePresents.addItemListener(rhandler);
        makeCloud.addItemListener(rhandler);

        buttonPanel.add(makeFlake);
        buttonPanel.add(makeTree);
        buttonPanel.add(makeGreet);
        buttonPanel.add(makeCloud);
        buttonPanel.add(makeCabin);
        buttonPanel.add(makePresents);

        // A ButtonGroup allows a set of JRadioButtons to be associated
        // together such that only one can be selected at a time
        shapeGroup = new ButtonGroup();
        shapeGroup.add(makeFlake);
        shapeGroup.add(makeTree);
        shapeGroup.add(makeGreet);
        shapeGroup.add(makeCloud);
        shapeGroup.add(makeCabin);
        shapeGroup.add(makePresents);

        currShape = Figures.SNOWFLAKE;
        drawPanel.setMode(Mode.NONE);

        theWindow = new JFrame("Greeting Card Maker!");
        Container c = theWindow.getContentPane();
        drawPanel.setBackground(Color.lightGray);
        c.add(drawPanel, BorderLayout.NORTH);
        c.add(buttonPanel, BorderLayout.SOUTH);

        // Note how the menu is created.  First we make a JMenuBar, then
        // we put a JMenu in it, then we put JMenuItems in the JMenu.  We
        // can have multiple JMenus if we like.  JMenuItems generate
        // ActionEvents, just like JButtons, so we just have to link an
        // ActionListener to them.
        theBar = new JMenuBar();
        theWindow.setJMenuBar(theBar);
        fileMenu = new JMenu("File");
        theBar.add(fileMenu);
        
        newScene = new JMenuItem("New");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As...");
        printScene = new JMenuItem("Print");
        endProgram = new JMenuItem("Exit");

        fileMenu.add(newScene);
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(printScene);
        fileMenu.add(endProgram);
        
        //must add handle instructions
        newScene.addActionListener(bhandler);
        load.addActionListener(bhandler);
        save.addActionListener(bhandler);
        saveAs.addActionListener(bhandler);
        printScene.addActionListener(bhandler);
        endProgram.addActionListener(bhandler);

        // JPopupMenu() also holds JMenuItems.  To see how it is actually
        // brought out, see the mouseReleased() method in the ShapePanel class
        // below.
        popper = new JPopupMenu();
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        resize = new JMenuItem("Resize");
        delete = new JMenuItem("Delete");
        deSelect = new JMenuItem("DeSelect");

        deSelect.addActionListener(bhandler);
        resize.addActionListener(bhandler);
        delete.addActionListener(bhandler);
        copy.addActionListener(bhandler);
        paste.addActionListener(bhandler);

        popper.add(copy);
        popper.add(paste);
        popper.add(resize);
        popper.add(delete);
        popper.add(deSelect);
        paste.setEnabled(copied);
        theWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        theWindow.pack();
        theWindow.setVisible(true);
    }

    public static void main(String [] args){
            new Assig5();
    }

    // See Section 12.4 for information on JRadioButtons.  Note that the
    // text uses ActionListeners to handle JRadioButtons.  Clicking on
    // a JRadioButton actually generates both an ActionEvent and an
    // ItemEvent.  I am using the ItemEvent here.  To handle the event,
    // all I am doing is changing a state variable that will affect the
    // MouseListener in the ShapePanel.
    private class RadioHandler implements ItemListener
    {
        public void itemStateChanged(ItemEvent e){
            if (e.getSource() == makeTree)
                    currShape = Figures.TREE;
            else if (e.getSource() == makeFlake)
                    currShape = Figures.SNOWFLAKE;
            else if (e.getSource() == makeGreet)
                    currShape = Figures.GREETING;
            else if (e.getSource() == makeCabin)
                    currShape = Figures.CABIN;
            else if (e.getSource() == makeCloud)
                    currShape = Figures.CLOUD;
            else if (e.getSource() == makePresents)
                    currShape = Figures.PRESENTS;
        }
    }

    // Note how the makeShape button and moveIt menu item are handled 
    // -- we again simply set the state in the panel so that the mouse will 
    // actually do the work.  The state needs to be set back in the mouse
    // listener.
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == makeShape)
            {
                if (makeShape.getText().equals("Click to Draw"))
                {
                    drawPanel.setMode(Mode.DRAW);
                    msg.setText("Position new shapes with mouse");
                    makeShape.setText("Click to Edit");
                }
                else
                {
                    drawPanel.setMode(Mode.NONE);
                    msg.setText("Edit shapes with mouse");
                    makeShape.setText("Click to Draw");
                }
            }
            else if (e.getSource() == deSelect)
            {
                    boolean ans = drawPanel.deSelected();

                    if (ans)
                    {
                            msg.setText("Shape no longer selected.");
                            drawPanel.repaint();
                    }
            }
            else if (e.getSource() == delete)
            {
                    boolean ans = drawPanel.deleteSelected();
                    if (ans)
                    {
                            msg.setText("Shape deleted");
                            drawPanel.repaint();
                    }
            }
            else if (e.getSource() == copy)
            {
                copied = true;
                drawPanel.setMode(Mode.COPY);
                boolean ans = drawPanel.copySelected();
                if (ans)
                {
                    String cStr = shapeList.get(PAGE_EXISTS).saveData();
                    shapeInfo = cStr.split(":");
                    copied = true;
                    copy.setEnabled(false); 
                    paste.setEnabled(true);
                    msg.setText("Shape copied");
                    drawPanel.repaint();
                }
            }
            else if (e.getSource() == paste)
            {  
                boolean ans = drawPanel.pasteSelected();
                if (ans)
                {
                    drawPanel.setMode(Mode.COPY);
                    msg.setText("Click where you want to paste");
                    makeShape.setEnabled(false);
                    paste.setEnabled(false);
                    drawPanel.repaint();
                }
            }
            else if (e.getSource() == resize)
            {
                int newSize = Integer.parseInt(JOptionPane.showInputDialog("Input New Size"));
                boolean ans = drawPanel.resizeSelected(newSize);

                if (ans){
                    msg.setText("Shape resized");
                    drawPanel.repaint();
                }
            }
            else if (e.getSource() == newScene)
            {
                if(JOptionPane.showConfirmDialog
                (null, "Start a New Card??","New", 
                JOptionPane.YES_NO_OPTION)==0 ){    
                if (!saved){
                    fileName = JOptionPane.showInputDialog("Name the File to Save or Cancel to Not Save.");
                    if (fileName != null){
                        file = new File(fileName);
                        saved = true;
                        try{
                            saveFile = new PrintWriter(new FileOutputStream(file, false));
                            for (MyShape s : shapeList)
                            saveFile.println(s.saveData());
                        }
                        catch (FileNotFoundException nf){
                            msg.setText("File Not Found");
                            }
                            changesMade = false;
                            saveFile.close();
                        }        
                    }
                }
                shapeList = new ArrayList<MyShape>();
                drawPanel.repaint(); 
            }
            else if (e.getSource() == load)
            {
                if(changesMade)
                {
                    if (JOptionPane.showConfirmDialog(
                            null, "Progress not Saved", "Save Now?", JOptionPane.YES_NO_OPTION) == 0)
                    {
                        if (!saved)
                        {
                            fileName = JOptionPane.showInputDialog("Enter your file name");
                            if (fileName != null)
                            {
                                file = new File(fileName);
                                saved = true;
                            }
                        }
                        if (saved)
                        {
                            try
                            {
                                saveFile = new PrintWriter(new FileOutputStream(file, false));
                                for (MyShape s : shapeList)
                                saveFile.println(s.saveData());
                            }
                            catch (FileNotFoundException nf2)
                            {
                                msg.setText("File Not Found");
                            }
                            changesMade = false;
                            saveFile.close();
                        }
                    }    
                }
                fileCho = new JFileChooser();
                int returnVal = fileCho.showOpenDialog(theWindow);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    file = fileCho.getSelectedFile();
                    shapeList = new ArrayList<MyShape>();
                    try
                    {
                        scan = new Scanner(file);
                    }
                    catch (FileNotFoundException nf3){
                        msg.setText("Error Reading File");
                    }
                    while (scan.hasNextLine())
                    {
                        fileName = scan.nextLine();
                        copyArray = fileName.split(":");

                        if (copyArray[0].equals("Cloud"))
                        {
                            newShape = new Cloud(Integer.parseInt(copyArray[1]), 
                                    Integer.parseInt(copyArray[2]),
                                    Integer.parseInt(copyArray[3]));    
                        }

                        if (copyArray[0].equals("Cabin"))
                        {
                            newShape = new Cabin(Integer.parseInt(copyArray[1]), 
                                    Integer.parseInt(copyArray[2]),
                                    Integer.parseInt(copyArray[3]));    
                        }
                        if (copyArray[0].equals("Tree"))
                        {
                            newShape = new Tree(Integer.parseInt(copyArray[1]), 
                                    Integer.parseInt(copyArray[2]),
                                    Integer.parseInt(copyArray[3]));    
                        }
                        if (copyArray[0].equals("Snowflake"))
                        {
                            newShape = new Snowflake(Integer.parseInt(copyArray[1]), 
                                    Integer.parseInt(copyArray[2]),
                                    Integer.parseInt(copyArray[3]));    
                        }
                        if (copyArray[0].equals("Present"))
                        {
                            newShape = new Presents(Integer.parseInt(copyArray[1]), 
                                    Integer.parseInt(copyArray[2]),
                                    Integer.parseInt(copyArray[3]));    
                        }
                        if (copyArray[0].equals("Greeting"))
                        {
                            newShape = new Greeting(Integer.parseInt(copyArray[1]),
                                Integer.parseInt(copyArray[2]), 
                                Integer.parseInt(copyArray[3]));
                            ((MyText) newShape).setText(copyArray[4]);
                        }
                        shapeList.add(newShape);
                        drawPanel.repaint();
                    }
                    scan.close();
                    msg.setText("File Loaded");
                    saved = true;
                    changesMade = false;
                }
                else
                {
                    msg.setText("File Not Loaded");
                }
            }
            else if (e.getSource() == save)
            {            
                if (!saved)
                {
                    fileName = JOptionPane.showInputDialog("Input File Name");
                    if (fileName != null)
                    {
                        file = new File(fileName);
                        saved = true;
                        try{
                            saveFile = new PrintWriter(new FileOutputStream(file, false));
                            for (MyShape s : shapeList)
                            saveFile.println(s.saveData());
                            msg.setText("File Saved.");
                        }
                        catch (FileNotFoundException nf4){
                            msg.setText("File Not Found");
                            }
                            changesMade = false;
                            saveFile.close();
                    }
                    else{
                        msg.setText("File Name:  Null");    
                    }
                }               
            }
            else if (e.getSource() == saveAs)
            {
                if(JOptionPane.showConfirmDialog
                (null, "Save File As...","Do you want to Save As a differnt File?", 
                JOptionPane.YES_NO_OPTION)==0 )
                {                  
                fileName = JOptionPane.showInputDialog("Input File Name");
                if (fileName != null){
                    file = new File(fileName);
                    saved = true;
                    try
                    {
                        saveFile = new PrintWriter(new FileOutputStream(file, false));
                        for (MyShape s : shapeList)
                        saveFile.println(s.saveData());
                    }
                    catch (FileNotFoundException nf5)
                    {
                        msg.setText("File Not Found");
                    }
                    changesMade = false;
                    saveFile.close();
                    }                         
                }
            }
            else if (e.getSource() == printScene)
            {
                Printable thePPanel = new thePrintPanel(drawPanel); 
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(thePPanel);
                boolean ok = job.printDialog();
                if (ok) 
                {
                    try {
                        job.print();
                    } 
                    catch (PrinterException ex) {
                    /* The job did not successfully complete */
                    }
                }
            }
            else if (e.getSource() == endProgram)
            {
                System.exit(0);
            }
        }
    }

    // Here we are extending JPanel.  This way we can use all of the
    // properties of JPanel (including generating MouseEvents) and also
    // add new instance data and methods, as shown below.  Since this is
    // an inner class, it can access instance variables from the A5Help
    // class if necessary.
    private class ShapePanel extends JPanel
    {

        // These instance variables are used to store the desired size
        // of the panel.  See method getPreferredSize() below.
        private int prefwid, prefht;

        // Store index of the selected MyShape.  This allows the Shape
        // to be moved and updated.
        private int selindex;

        // Keep track of positions where mouse is moved on the display.
        // This is used by mouse event handlers when moving the shapes.
        private int x1, y1, x2, y2; 

        private boolean popped; // has popup menu been activated?

        private Mode mode;   // Keep track of the current Mode

        public ShapePanel (int pwid, int pht)
        {
            shapeList = new ArrayList<MyShape>(); // create empty ArrayList
            selindex = -1;

            prefwid = pwid;	// values used by getPreferredSize method below
            prefht = pht;   // (which is called implicitly).  This enables
            // the JPanel to request the room that it needs.
            // However, the JFrame is not required to honor
            // that request.

            setOpaque(true);// Paint all pixels here (See API)

            setBackground(Color.lightGray);

            addMouseListener(new MyMouseListener());
            addMouseMotionListener(new MyMover());
            popped = false;
        }  // end of constructor

        // Mouse Listenser for differnt Mouse Clicks
        private class MyMouseListener extends MouseAdapter
        {
            public void mousePressed(MouseEvent e)
            {
                x1 = e.getX();  // store where mouse is when clicked
                y1 = e.getY();

                // left click and either NONE or SELECTED mode
                if (!e.isPopupTrigger() && 
                    (mode == Mode.NONE ||mode == Mode.SELECTED)) 
                {   												    
                    if (selindex >= 0)								
                    {
                            unSelect();// unselect previous shape
                            mode = Mode.NONE;
                    }
                    selindex = getSelected(x1, y1);  // find shape mouse is clicked on

                    if (selindex >= 0)
                    {
                        mode = Mode.SELECTED;  	// Now in SELECTED mode for shape
                        deSelect.setEnabled(true);
                        delete.setEnabled(true);
                        resize.setEnabled(true);
                        copy.setEnabled(true);
                // Check for double-click.  If so, show dialog to update text of
                // the current text shape (will do nothing if shape is not a MyText)
                        MyShape curr = shapeList.get(selindex);
                        if (curr instanceof MyText && e.getClickCount() == 2)
                        {
                                String newText = JOptionPane.showInputDialog(theWindow, 
                                                  "Enter new text [Cancel for no change]");
                                if (newText != null)
                                        ((MyText) curr).setText(newText);
                        }
                        
                    }
                    repaint();	//  Make sure updates are redrawn
                }

                if (!e.isPopupTrigger() && mode == Mode.COPY){

                    if (shapeInfo[0].equals("Cloud")){
                        newShape = new Cloud(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }
                    if (shapeInfo[0].equals("Cabin")){
                        newShape = new Cabin(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }
                    if (shapeInfo[0].equals("Tree")){
                        newShape = new Tree(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }

                    if (shapeInfo[0].equals("Snowflake")){
                        newShape = new Cabin(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }

                    if (shapeInfo[0].equals("Presents")){
                        newShape = new Cabin(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }

                    if (shapeInfo[0].equals("Greeting")){
                    newShape = new Cabin(Integer.parseInt(shapeInfo[1]),
                                Integer.parseInt(shapeInfo[2]),
                                Integer.parseInt(shapeInfo[3]));    
                        }
  
                    shapeList.add(newShape);
                    shapeList.get(shapeList.size()-1).move(x1,y1);
                    unSelect();
                    copied = false;
                    makeShape.setEnabled(true);
                    paste.setEnabled(false);
    
                    mode = Mode.NONE;
                    msg.setText("Pasted succesfully");
                    repaint();                     
                }
                // if button is the popup menu trigger, show popup menu
                else if (e.isPopupTrigger() && selindex >= 0)  
                {								              
                        popper.show(ShapePanel.this, x1, y1);     
                        popped = true;							   
                }											  
            }

            public void mouseReleased(MouseEvent e)
            {
                if (mode == Mode.DRAW)
                { // in DRAW mode, create the new Shape
                  // and add it to the list of Shapes.  In this
                   // case we need to distinguish between the
                   // shapes since we are calling constructors
                    if (currShape == Figures.TREE)
                    {
                            newShape = new Tree(x1,y1,50);
                    }
                    else if (currShape == Figures.SNOWFLAKE)
                    {
                            newShape = new Snowflake(x1,y1,10);
                    }
                    else if (currShape == Figures.GREETING)
                    {
                            newShape = new Greeting(x1,y1,30);
                    }
                    else if (currShape == Figures.CABIN)
                    {
                            newShape = new Cabin(x1,y1,50);
                    }
                    else if (currShape == Figures.CLOUD)
                    {
                            newShape = new Cloud(x1,y1,10);
                    }
                    else if (currShape == Figures.PRESENTS)
                    {
                            newShape = new Presents(x1,y1,50);
                    }
                    addShape(newShape);
                }
                // In MOVING mode, set mode back to NONE and unselect shape (since 
                // the move is finished when we release the mouse).
                else if (mode == Mode.MOVING) 
                {
                    mode = Mode.NONE;
                    unSelect();  
                    makeShape.setEnabled(true);
                    repaint();
                }
                else if (mode == Mode.COPY) 
                {	
                    popper.show(ShapePanel.this, x1, y1);
                    makeShape.setEnabled(true);
                    paste.setEnabled(true);
                    delete.setEnabled(false);
                    resize.setEnabled(false);
                    copy.setEnabled(false);
                    repaint();
                }
                // if button is the popup menu trigger, show the popup menu
                else if (e.isPopupTrigger() && selindex >= 0) 
                {							
                    popper.show(ShapePanel.this, x1, y1); 
                }
                // unset popped since mouse is being released
                popped = false;  
            }
        }

        // the MouseMotionAdapter has the same idea as the MouseAdapter
        // above, but with only 2 methods.  The method not implemented
        // here is mouseMoved.  The difference between the two is whether or
        // not the mouse is pressed at the time.  Since we require a "click and
        // hold" to move our objects, we are using mouseDragged and not
        // mouseMoved.
        private class MyMover extends MouseMotionAdapter
        {
                public void mouseDragged(MouseEvent e)
                {
                        x2 = e.getX();   // store where mouse is now
                        y2 = e.getY();

                        // Note how easy moving the shapes is, since the "work"
                        // is done within the various shape classes.  All we do
                        // here is call the appropriate method.  However, we don't 
                        // want to accidentally move the selected shape with a right click
                        // so we make sure the popup menu has not been activated.
                        if ((mode == Mode.SELECTED || mode == Mode.MOVING) && !popped)
                        {
                                MyShape s = shapeList.get(selindex);
                                mode = Mode.MOVING;
                                s.move(x2, y2);
                        }
                        repaint();	// Repaint screen to show updates
                }
        }

        // Check to see if point (x,y) is within any of the shapes.  If
        // so, select that shape and highlight it so user can see.  Again,
        // note that we do not care which shape we are selecting here --
        // it only matters that it has the MyShape interface methods.
        // This version of getSelected() always considers the shapes from
        // beginning to end of the ArrayList.  Thus, if a shape is "under"
        // or "within" a shape that was previously created, it will not
        // be possible to select the "inner" shape.  In your assignment you
        // must redo this method so that it allows all shapes to be selected.
        // Think about how you would do this.
        private int getSelected(double x, double y)
        {                                             
                for (int i = 0; i < shapeList.size(); i++)
                {
                        if (shapeList.get(i).contains(x, y))
                        {
                                shapeList.get(i).highlight(true); 
                                return i;
                        }
                }
                return -1;
        }

        public void unSelect()
        {
                if (selindex >= 0)
                {
                        shapeList.get(selindex).highlight(false);
                        selindex = -1;
                }
        }
        public boolean deSelected()
        {
                if (selindex >= 0)
                {
                        unSelect();

                        return true;
                }
                else return false;
        }

        public boolean deleteSelected()
        {
                if (selindex >= 0)
                {
                        shapeList.remove(selindex);
                        selindex = -1;
                        return true;
                }
                else return false;
        }
        // CHECK HERE!!!!!!!!!!1
        public Figures copyShape(){
            String cShape = shapeList.get(selindex).saveData();
            return currShape;

        }
        public boolean copySelected()
        {
                if (selindex >= 0)
                {
                        shapeList.get(selindex);

                        return true;
                }
                else return false;
        }
        public boolean pasteSelected()
        {
            if (selindex >= 0)
            {
            
            msg.setText("Click where you want to paste");
            makeShape.setEnabled(false);
            paste.setEnabled(false);
                return true;
            }
            else return false;
        }

        public boolean resizeSelected(int s){
            if (selindex >= 0)
            {
                shapeList.get(selindex).resize(s);
                selindex = -1;	
                return true;
            }
            else return false;
        }

        public void changeSize(int cSize){

        }
        // set Mode
        public void setMode(Mode newMode){
                mode = newMode;
        }
        // Add shape
        private void addShape(MyShape newshape){
                shapeList.add(newshape);
                repaint();	// repaint so we can see new shape
        }

            // Method called implicitly by the JFrame to determine how much
            // space this JPanel wants.  Be sure to include this method in
            // your program so that your panel will be sized correctly.
        public Dimension getPreferredSize()
        {
                return new Dimension(prefwid, prefht);
        }

            // This method enables the shapes to be seen.  Note the parameter,
            // which is implicitly passed.  To draw the shapes, we in turn
            // call the draw() method for each shape.  The real work is in the draw()
            // method for each MyShape
        public void paintComponent (Graphics g)    
        {
            super.paintComponent(g);         // don't forget this line!
            Graphics2D g2d = (Graphics2D) g;
            for (int i = 0; i < shapeList.size(); i++)
            {
                    shapeList.get(i).draw(g2d);
            }
        }
    } // end of ShapePanel
}
