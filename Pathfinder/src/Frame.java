import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 6485949987448926243L;
	int size;
	double a1, a2;
	char currKey = (char) 0;
	boolean showSteps, btnHover;
	String mode;
	
	Handler h;
	APathfinder pathfinding;
	JFrame frame;
	Node startNode, endNode;
	
	Timer timer = new Timer(100, this);
	int r = randomWithRange(0, 255);
	int G = randomWithRange(0, 255);
	int b = randomWithRange(0, 255);
	
	public static void main(String[] args) {
		new Frame();
	}
	
	public Frame() {
		h = new Handler(this);
		size = 25;
		mode = "Map Creation";
		showSteps = true;
		btnHover = false;
		setLayout(null);
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		//set up pathfinding
		pathfinding = new APathfinder(this, size);
		pathfinding.setDiagonal(true);
		
		//calculate value of a in speed function 1
		a1 = (5000.0000 / (Math.pow(25.0000/5000,  1/49)));
		a2 = 625.0000;
		
		//set up frame
		frame = new JFrame();
		frame.setContentPane(this);
		frame.setTitle("Pathfinding Visualizer");
		frame.getContentPane().setPreferredSize(new Dimension(700, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//adds controls
		h.addAll();
		
		this.revalidate();
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//grab dimensions of panel
		int height = getHeight();
		
		//if no pathfinding is found
		if(pathfinding.isNoPath()) {
			//set timer for animation
			timer.setDelay(50);
			timer.start();
			
			//set text of run button to clear
			h.getB("Run").setText("Clear");
			
			//set mode to no path
			mode = "No Path";
			
			//set up flicker animation
			Color flicker = new Color(r, G, b);
			g.setColor(flicker);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			//place no path text on screen in center
			h.noPathTBounds();
			h.getL("noPathT").setVisible(true);
			this.add(h.getL("noPathT"));
			this.revalidate();
		}
		
		//if pathfinding is complete
		if(pathfinding.isComplete()) {
			h.getB("Run").setText("Clear");
			
			//set timer delay, start for background animation
			timer.setDelay(50);
			timer.start();
			
			//make background change colour
			Color flicker = new Color(r, G, b);
			g.setColor(flicker);
			g.fillRect(0,  0, getWidth(), getHeight());
			
			//set completed mode
			mode = "Completed";
		}
		
		//draws grid
		g.setColor(Color.lightGray);
		int row, column = 0;
		for(row = 0; row < 1400; row += size) {
			g.drawRect(row, column, size, size);
			for(column = 0; column < 750; column += size) {
				g.drawRect(row,  column,  size,  size);
			}
		}
		
		//draws all borders
		g.setColor(Color.black);
		for(int i = 0; i < pathfinding.getBorderList().size(); i++) {
			g.fillRect(pathfinding.getBorderList().get(i).getX() + 1, pathfinding.getBorderList().get(i).getY() + 1, size - 1, size -1);
		}
		
		//draws all open Nodes (pathfinding nodes)
		for(int i = 0; i < pathfinding.getOpenList().size(); i++) {
			Node current = pathfinding.getOpenList().get(i);
			g.setColor(Style.greenHighlight);
			g.fillRect(current.getX() + 1, current.getY() + 1, size - 1, size - 1);

			drawInfo(current, g);
		}
		
		//draws all closed nodes
		for(int i = 0; i < pathfinding.getClosedList().size(); i++) {
			Node current = pathfinding.getClosedList().get(i);
			g.setColor(Style.redHighlight);
			g.fillRect(current.getX() + 1, current.getY() + 1, size - 1, size - 1);
			
			drawInfo(current, g);
		}
		
		//draws all final path nodes
				for(int i = 0; i < pathfinding.getPathList().size(); i++) {
					Node current = pathfinding.getPathList().get(i);
					g.setColor(Style.blueHighlight);
					g.fillRect(current.getX() + 1, current.getY() + 1, size - 1, size - 1);

					drawInfo(current, g);
				}
		
		//draw start of path
		if(startNode != null) {
			g.setColor(Color.blue);
			g.fillRect(startNode.getX() + 1, startNode.getY() + 1, size - 1, size -1);		
		}
		
		//draw end of path
		if(endNode != null) {
			g.setColor(Color.red);
			g.fillRect(endNode.getX() + 1, endNode.getY(), size - 1, size -1);
		}
		
		//if control panel is being hovered, change colours
		if(btnHover) {
			g.setColor(Style.darkText);
			h.hoverColour();
		} else {
			g.setColor(Style.btnPanel);
			h.nonHoverColour();
		}
		
		//drawing control panel rectangle
		g.fillRect(10, height - 100, 315, 90);
		
		//setting mode text
		h.getL("modeText").setText("Mode: " + mode);
		
		//positioning all controls
		h.position();
		
		//setting numbers in pathfinding lists
		h.getL("openC").setText(Integer.toString(pathfinding.getOpenList().size()));
		if(pathfinding.getClosedList().size() > 0) {
			//h.getL("closedC").setText(Integer.toString(pathfinding.getClosedList().size()));
		}
        if(pathfinding.getPathList().size() > 0) {
        	//h.getL("pathC").setText(Integer.toString(pathfinding.getPathList().size()));	
        }
		
		//setting speed number text in showSteps
		if(showSteps) {
			h.getL("speedC").setText(Integer.toString(h.getS("speed").getValue()));
		}
		
		//getting values from checkboxes
		pathfinding.setDiagonal(h.getC("diagonalCheck").isSelected());
		pathfinding.setTrig(h.getC("trigCheck").isSelected());
	}
	
	//draws fCost on current node
	public void drawInfo(Node current, Graphics g) {
		g.setFont(Style.nums);
		g.setColor(Color.black);
		g.drawString(Integer.toString(current.getFCost()), current.getX() + 3, current.getY() + 11);
		g.setFont(Style.smallNums);
		g.drawString(Integer.toString(current.getGCost()), current.getX() + 3, current.getY() + 22);
	}
	
	public void mapCalculations(MouseEvent e) {
		//if left mouse button is clicked
		if(SwingUtilities.isLeftMouseButton(e)) {
			//if s key is pressed create start node
			
			if(currKey == 'e' || currKey == 'E') {
				int xRollover = e.getX() % size;
				int yRollover = e.getY() % size;
				
				if(endNode == null) {
					endNode = new Node(e.getX() - xRollover, e.getY() - yRollover);
				} else {
					endNode.setXY(e.getX() - xRollover, e.getY() - yRollover);
				}
				repaint();
			} 
			//if e is pressed create end node
			else if(currKey == 's' || currKey == 'S') {
				int xRollover = e.getX() % size;
				int yRollover = e.getY() % size;
				
				if(startNode == null) {
					startNode = new Node(e.getX() - xRollover, e.getY() - yRollover);
				} else {
					startNode.setXY(e.getX() - xRollover, e.getY() - yRollover);
				}
				repaint();
			}
			//else create wall
			else {
				int xBorder = e.getX() - (e.getX() % size);
				int yBorder = e.getY() - (e.getY() % size);
					
				Node newBorder = new Node(xBorder, yBorder);
				pathfinding.addBorder(newBorder);
					
				repaint();
			}
		} 
		//if right mouse button is clicked
		else if(SwingUtilities.isRightMouseButton(e)) {
			int mouseX = e.getX() - (e.getX() % size);
			int mouseY = e.getY() - (e.getY() % size);
			//if s is pressed remove start node
			if(currKey == 's' || currKey == 'S') {
				if(startNode != null && mouseX == startNode.getX() && startNode.getY() == mouseY);
				startNode = null;
				repaint();
			} 
			//if e is pressed remove end node
			else if(currKey == 'e' || currKey == 'E') {
				if(endNode != null && mouseX == endNode.getX() && endNode.getY() == mouseY);
				endNode = null;
				repaint();
			}
			//else remove wall
			else {
				int location = pathfinding.searchBorder(mouseX, mouseY);
				if(location != -1) {
					pathfinding.removeBorder(location);
				}
				repaint();	
			}
		}
	}
	
	void start() {
		if(startNode != null && endNode != null) {
			pathfinding.setup(startNode, endNode);
			setSpeed();
			timer.start();
		} else {
			System.out.println("ERROR: Needs start and end points tor run.");
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		currKey = key;
		
		//start if space is pressed
		if(currKey == KeyEvent.VK_SPACE) {
			h.getB("Run").setText("Stop");
			start();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		currKey = (char) 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mapCalculations(e);
	}

	@Override
	//track movement of mouse
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int height = this.getHeight();
		
		//detects whether if mouse is within button panel
		//g.fillRect(10, height - 100, 315, 90);
		if(x >= 10 && x <= 315 && y >= (height - 100) && y <= (height-10)) {
			btnHover = true;
		} else {
			btnHover = false;
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mapCalculations(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//moves one step ahead in path finding (called on timer)
		if(pathfinding.isRunning()) {
			pathfinding.findPath(pathfinding.getPar());
			mode = "Running";
		}
		
		//actions of run / stop / clear button
		if(e.getActionCommand() != null) {
			if(e.getActionCommand().equals("Run") && pathfinding.isRunning()) {
				h.getB("Run").setText("Stop");
				start();
			}
			else if(e.getActionCommand().equals("Clear")) {
				h.getB("Run").setText("Run");
				mode = "Map Creation";
				h.getL("noPathT").setVisible(false);
				pathfinding.reset();
			}
			else if(e.getActionCommand().equals("Stop")) {
				h.getB("Run").setText("Start");
				timer.stop();
			}
			else if(e.getActionCommand().equals("Start")) {
				h.getB("Run").setText("Stop");
				timer.start();
			}
			
			if(e.getActionCommand().equals("Run") ) {
				if(startNode != null && endNode != null) {
					h.getB("Run").setText("Stop");		
				}	
				start();
			}
			
			if(e.getActionCommand().equals("Clear Board")) {
				startNode = null;
				endNode = null;
				repaint();
				h.getB("ClearBoard").setText("Set Board");
			} else if(e.getActionCommand().equals("Set Board")) {
				startNode = new Node(200, (getHeight() - 500));
				//endNode = new Node(700 % size, );
			}
		}
		repaint();
	}

	//sets the speed of the slider to the pathfinding algorithm
	public void setSpeed() {
		int delay = 0;
		int value = h.getS("speed").getValue();
		
		if(value == 0) {
			timer.stop();
		} else if(value >= 1 && value < 50) {
			if(!timer.isRunning()) {
				timer.start();
			}
			
			//exponential function. value (1) == delay (5000). value (50) == delay (25)
			delay = (int)(a1 * (Math.pow(25/5000.0000, value / 49.0000)));
		} else if(value >= 50 && value <= 100) {
			if(!timer.isRunning()) {
				timer.start();
			}
			
			//exponential function. value (50) == delay(25). value (100) == delay (1)
			delay = (int)(a2 * (Math.pow(1/25.0000,  value/50.0000)));
		}
		timer.setDelay(delay);
	}
	
	boolean showSteps() {
		return showSteps;
	}
	
	int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int)(Math.random() * range) + min;
	}
}