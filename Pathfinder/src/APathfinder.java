import java.util.ArrayList;

public class APathfinder {
	
	//variables
	private int size, diagonalMoveCost;
	private long runTime;
	private double kValue;
	private Frame frame;
	private Node startNode, endNode, par;
	private boolean diagonal, running, noPath, complete, trig;
	
	//arraylists
	private ArrayList<Node> borders, open, closed, path;
	private Sort sort = new Sort();
	
	//constructors
	public APathfinder(int size) {
		this.size = size;
		
		diagonalMoveCost = (int) (Math.sqrt(2 * (Math.pow(size,  2))));
		kValue = Math.PI / 2;
		diagonal = true;
		trig = false;
		running = false;
		complete = false;
		
		borders = new ArrayList<Node>();
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		path = new ArrayList<Node>();
	}
	
	public APathfinder(Frame frame, int size) {
		this.frame = frame;
		this.size = size;
		
		diagonalMoveCost = (int)(Math.sqrt(2 * (Math.pow(size,  2))));
		kValue = Math.PI / 2;
		diagonal = true;
		trig = false;
		running = false;
		complete = false;
		
		borders = new ArrayList<Node>();
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		path = new ArrayList<Node>();
	}
	
	public APathfinder(Frame frame, int size, Node start, Node end) {
		this.frame = frame;
		this.size = size;
		startNode = start;
		endNode = end;
		
		diagonalMoveCost = (int)(Math.sqrt(2 * (Math.pow(size,  2))));
		diagonal = true;
		trig = false;
		running = false;
		complete = false;
		
		borders = new ArrayList<Node>();
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		path = new ArrayList<Node>();
	}
	
	//this is what sets off the start of the algorithm
	public void start(Node s, Node e) {
		running = true;
		startNode = s;
		startNode.setGCost(0);
		endNode = e;
		
		//adding starting node to the closed list
		addClosed(startNode);
		
		long startTime = System.currentTimeMillis();
		
		findPath(startNode);
		
		complete = true;
		long endTime = System.currentTimeMillis();
		runTime = endTime - startTime;
		System.out.println("Completed: " + runTime + "ms");
	}
	
	//similar to start()
	public void setup(Node s, Node e) {
		running = true;
		startNode = s;
		startNode.setGCost(0);
		par = startNode;
		endNode = e;
		
		//adding the start node to the closed list
		addClosed(startNode);
	}
	
	public void setStart(Node s) {
		startNode = s;
		startNode.setGCost(0);
	}
	
	public void setEnd(Node e) {
		endNode = e;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public Node getStart() {
		return startNode;
	}
	
	public Node getEnd() {
		return endNode;
	}
	
	public Node getPar() {
		return par;
	}
	
	public boolean isNoPath() {
		return noPath;
	}
	
	public boolean isTrig() {
		return trig;
	}
	
	public void setDiagonal(boolean d) {
		diagonal = d;
	}
	
	public void setTrig(boolean t) {
		trig = t;
	}
	
	public void setSize(int s) {
		size = s;
		diagonalMoveCost = (int) (Math.sqrt(2 * (Math.pow(size, 2))));
	}
	
	public void findPath(Node parent) {
		Node openNode = null;
		
		if(diagonal) {
			//detects and adds one step of nodes to open list
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(i == 1 && j == 1) {
						continue;
					}
					
					int possibleX = (parent.getX() - size) + (size * i);
					int possibleY = (parent.getY() - size) + (size * j);
					
					//possible coords of borders
					//using (crossBorderX, parent.getY())
					//and (parent.getX(), crossBorder())
					//to see if there are any borders in the way
					int crossBorderX = parent.getX() + (possibleX - parent.getX());
					int crossBorderY = parent.getY() + (possibleY - parent.getY());
					
					//disables ability to cut corners around borders
					if(searchBorder(crossBorderX, parent.getY()) != -1 | searchBorder(parent.getX(), crossBorderY) != -1 && ((j == 0 | j == 2) && i != 1)) {
						continue;
					}
					calculateNodeValues(possibleX, possibleY, openNode, parent);
				}
			}
		} else if(!trig) {
			//detects and adds one step of nodes to open list
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if((i == 0 && j == 0) || (i == 0 && j == 2) || 
						(i == 1 && j == 1) || (i == 2 && j == 0) ||
						(i == 2 && j == 2)) {
						continue;
					}
					int possibleX = (parent.getX() - size) + (size * i);
					int possibleY = (parent.getY() - size) + (size * j);

					calculateNodeValues(possibleX, possibleY, openNode, parent);
				}
			} 
		} else {
			for(int i = 0; i < 4; i++) {
				//uses cos and sin functions to get circle of
				//points around parent
				int possibleX = (int) Math.round(parent.getX() + (-size * Math.cos(kValue * i)));
				int possibleY = (int) Math.round(parent.getY() + (-size * Math.sin(kValue * i)));
			
				calculateNodeValues(possibleX, possibleY, openNode, parent);
			}
		}
		
		//set the new parent node
		parent = lowestFCost();
		
		if(parent == null) {
			System.out.println("END> NO PATH");
			noPath = true;
			running = false;
			frame.repaint();
			return;
		}
		
		if(Node.isEqual(parent,  endNode)) {
			endNode.setParent(parent.getParent());
			
			connectPath();
			running = false;
			complete = true;
			frame.repaint();
			return;
		}
		
		//remove parent node from open list
		removeOpen(parent);
		//add parent node to closed list
		addClosed(parent);
		
		
		//allows correction for shortest path during runtime
		//when new parent node is selected -> checks all adjacent
		//open nodes -> then checks if the Gscore of parent + openNode distance from parent
		//is less than the current G score of the open node ->
		//if true -> sets the parent of open node as new parent -> 
		//and recalculates G and F values
		if(diagonal) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; i < 3; i++) {
					if(i == 1 && j == 1) {
						continue;
					}
					
					int possibleX = (parent.getX() - size) + (size * i);
					int possibleY = (parent.getY() - size) + (size * j);
					Node openCheck = getOpenNode(possibleX, possibleY);
					
					//if spot being checked, is an open node
					if(openCheck != null) {
						int distanceX = parent.getX() - openCheck.getX();
						int distanceY = parent.getY() - openCheck.getY();
						int newGCost = parent.getGCost();
						
						if(distanceX != 0 && distanceY != 0) {
							newGCost += diagonalMoveCost;
						} else {
							newGCost += size;
						}
						
						if(newGCost < openCheck.getGCost()) {
							int s = searchOpen(possibleX, possibleY);
							open.get(s).setParent(parent);
							open.get(s).setGCost(newGCost);
							open.get(s).setFCost(open.get(s).getGCost() + open.get(s).getHCost());
						}
					}
				}
			}
		}
		if(!frame.showSteps()) {
			findPath(parent);
		} else {
			par = parent;
		}
	}
	
	public void calculateNodeValues(int x, int y, Node openNode, Node parent) {
		//if the node is already a border node or a closed node
		//or an already open node, then don't make open node
		if(searchBorder(x, y) != -1 | searchClosed(x, y) != -1 | searchOpen(x, y) != -1) {
			return;
		}
		
		//create an open node with the available x and y coords
		openNode = new Node(x, y);
		
		//set the parent of the open node
		openNode.setParent(parent);
		
		//Calculating g cost
		//cost to move from parent node to one open node
		//(x and y separately)
		int GxMoveCost = openNode.getX() - parent.getX();
		int GyMoveCost = openNode.getY() - parent.getY();
		int gCost = parent.getGCost();
				
		if(GxMoveCost != 0 && GyMoveCost != 0) {
			gCost += diagonalMoveCost;
		} else {
			gCost += size;
		}
		
		openNode.setGCost(gCost);
		
		//calculate h cost
		int HxDiff = Math.abs(endNode.getX() - openNode.getX());
		int HyDiff = Math.abs(endNode.getY() - openNode.getY());
		int hCost = HxDiff + HyDiff;
		openNode.setHCost(hCost);
		
		//calculate f cost
		int fCost = gCost + hCost;
		openNode.setFCost(fCost);
		
		addOpen(openNode);
	}
	
	//algorithm that dictates what is the shortest path once algorithm has finished running
	public void connectPath() {
		if(getPathList().size() == 0) {
			Node parentNode = endNode.getParent();
			
			while(!Node.isEqual(parentNode, startNode)) {
				addPath(parentNode);
				
				for(int i = 0; i < getClosedList().size(); i++) {
					Node current = getClosedList().get(i);
					
					if(Node.isEqual(current, parentNode)) {
						parentNode = current.getParent();
						break;
					}
				}
			}
			reverse(getPathList());
		}
	}
	
	//methods to add walls (called borders), open nodes, closed nodes 
	//and paths to their respective array lists
	public void addBorder(Node node) {
		if(borders.size() == 0) {
			borders.add(node);
		} else if(!checkBorderDuplicate(node)) {
			borders.add(node);
		}
	}
	
	public void addOpen(Node node) {
		if(open.size() == 0) {
			open.add(node);
		} else if(!checkOpenDuplicate(node)) {
			open.add(node);
		}
	}
	
	public void addClosed(Node node) {
		if(closed.size() == 0) {
			closed.add(node);
		} else if(!checkClosedDuplicate(node)) {
			closed.add(node);
		} else return;
	}
	
	public void addPath(Node node) {
		if(path.size() == 0) path.add(node);
		else path.add(node);
	}
	
	//remove vars from respective arraylists
	public void removePath(int location) {
		path.remove(location);
	}
	
	public void removeBorder(int location) {
		borders.remove(location);
	}
	
	public void removeOpen(int location) {
		open.remove(location);
	}
	
	public void removeOpen(Node node) {
		for(int i = 0; i < open.size(); i++) {
			if(node.getX() == open.get(i).getX() && node.getY() == open.get(i).getY()) {
				open.remove(i);
			}
		}
	}
	
	public void removeClosed(int location) {
		closed.remove(location);
	}
	
	public boolean checkBorderDuplicate(Node node) {
		for(int i = 0; i < borders.size(); i++) {
			if(node.getX() == borders.get(i).getX() && node.getY() == borders.get(i).getY()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkOpenDuplicate(Node node) {
		for(int i = 0; i < open.size(); i++) {
			if(node.getX() == open.get(i).getX() && node.getY() == open.get(i).getY()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkClosedDuplicate(Node node) {
		for(int i = 0; i < closed.size(); i++) {
			if(node.getX() == closed.get(i).getX() && node.getY() == closed.get(i).getY()) {
				return true;
			}
		}
		
		return false;
	}
	
	public int searchBorder(int xSearch, int ySearch) {
		int location = -1;
		
		for(int i = 0; i < borders.size(); i++) {
			if(borders.get(i).getX() == xSearch && borders.get(i).getY() == ySearch) {
				location = i;
				break;
			}
		}
		return location;
	}
	
	public int searchClosed(int xSearch, int ySearch) {
		int location = -1;

		for (int i = 0; i < closed.size(); i++) {
			if (closed.get(i).getX() == xSearch && closed.get(i).getY() == ySearch) {
				location = i;
				break;
			}
		}
		return location;
	}
	
	public int searchOpen(int xSearch, int ySearch) {
		int location = -1;
		
		for(int i = 0; i < open.size(); i++) {
			if(open.get(i).getX() == xSearch && open.get(i).getY() == ySearch) {
				location = i;
				break;
			}
		}
		return location;
	}
	
	public void reverse(ArrayList<Node> list) {
		int l = list.size() - 1;
		
		for(int i = 0; i < l; i++) {
			Object temp = list.get(i);
			list.remove(i);
			list.add(i, list.get(l-1));
			list.remove(l);
			list.add(l, (Node) temp);
			l--;
		}
	}
	
	public Node lowestFCost() {
		if(open.size() > 0) {
			sort.bubbleSort(open);
			return open.get(0);
		}
		return null;
	}
	
	public ArrayList<Node> getBorderList() {
		return borders;
	}
	
	public ArrayList<Node> getOpenList() {
		return open;
	}
	
	public Node getOpen(int location) {
		return open.get(location);
	}
	
	public ArrayList<Node> getClosedList() {
		if(closed == null) {
			return closed;
		}
		
		return closed;
	}
	
	public ArrayList<Node> getPathList() {
		return path;
	}
	
	public long getRunTime() {
		return runTime;
	}
	
	public void reset() {
		while(open.size() > 0) {
			open.remove(0);
		}
		while(closed.size() > 0) {
			closed.remove(0);
		}
		while(path.size() > 0) {
			path.remove(0);
		}
		noPath = false;
		running = false;
		complete = false;
	}
	
	public Node getOpenNode(int x, int y) {
		for(int i = 0; i < open.size(); i++) {
			if(open.get(i).getX() == x && open.get(i).getY() == y) {
				return open.get(i);
			}
		}
		return null;
	}
	
	//these three methods were used to check whether the three different node lists would work
	public void printBorderList() {
		for(int i = 0; i <borders.size(); i++) {
			System.out.println(borders.get(i).getX() + ", " + borders.get(i).getY());
			System.out.println();
		}
		System.out.println("=================");
	}
	
	public void printOpenList() {
		for(int i = 0; i <open.size(); i++) {
			System.out.println(open.get(i).getX() + ", " + open.get(i).getY());
			System.out.println();
		}
		System.out.println("=================");	
	}
	
	public void printPathList() {
		for(int i = 0; i <path.size(); i++) {
			System.out.println(path.get(i).getX() + ", " + path.get(i).getY());
			System.out.println();
		}
		System.out.println("=================");		
	}
}
