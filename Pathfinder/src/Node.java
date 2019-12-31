
//The Node class contains the blueprint for every node on the grid
//for the required pathfinding. 

public class Node {
	private int x, y, gCost, hCost, fCost;
	private Node parent;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
 	public int getGCost() {
		return this.gCost;
	}
	
	public void setGCost(int gCost) {
		this.gCost = gCost;
	}
	
	public int getHCost() {
		return this.hCost;
	}
	
	public void setHCost(int hCost) {
		this.hCost = hCost;
	}
	
	public int getFCost() {
		return this.fCost;
	}
	
	public void setFCost(int fCost) {
		this.fCost = fCost;
	}
	
	public Node getNode() {
		return this.parent;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public static boolean isEqual(Node start, Node end) {
		if(start.getX() == end.getX() && start.getY() == end.getY()) {
			return true;
		}
		
		return false;
	}
}
