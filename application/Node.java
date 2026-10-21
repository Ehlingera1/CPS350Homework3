package application;


import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Node {
	private int value; //store the value of the node
	private int color=1; //The color of the node (for red-black tree, it can be either BLACK or RED, as defined in the 
	private Node left; //the left child of the node
	private Node right; //the right child of the node
	private Node parent; //the parent of the node
	private boolean left_child_of_parent; //an indicator if this node is the left child of its parent
										  //true - left
	  									  //false - right
	
	//Static members for color assignment. You just need to use them and do not need to add additional colors
	public static final int BLACK = 0;
	public static final int RED = 1;
	public static final int GREEN = 2; //this green is just for the demo purpose in the original framework. It is not needed for the red-black tree

   
	
	/*******************************	Implementation Here (Optional)  *****************************************/
	//Feel free to add any additional data or member functions here and the corresponding "setter" and "getter"
	//It is really not necessary to write any additional code for this Node.java class. But just in case, if you want to 
	//modify this class for your specific implementation, you can put your code here

		
     
        
	/*******************************	End of Implementation  *****************************************/
	
	
	
	
	
	
	
	/* Read-only: data members below are just for GUI uses */
	private Point2D position; //store the position of the node on canvas 
	private Canvas canvas; //the canvas where a node is drawn
	private GraphicsContext gc; //the brush used to draw on canvas
	private boolean high_light; //an indicator if the current node is the newly inserted node
							  //true - either (1) this node is a newly created node
							  //       or (2) this node is selected
							  //false - regular node
	

	private int idx; //record the access order from BFS
	private Tree tree; //the tree this node is from
	private int layer_idx; //the position of the node in its layer

	private int depth = 0; //the depth of current node in the tree
	

	public Node()
	{
	}
	
	//Create a node with value assigned
	public Node(int value)
	{
		this.value = value;
	}
	
	//Create a node with value and canvas and GraphicsContext assigned.
	public Node(int value,  Tree tree, Canvas c, GraphicsContext gc)
	{
		this.position = new Point2D(0, 0);
		this.value = value;
		this.canvas = c;
		this.gc = gc;
		this.tree = tree;
	}

	
	
	/* Draw this node onto canvas */
	void showNode()
	{
		gc.setFill((color == RED)?Color.RED:Color.BLACK);
		gc.fillOval(position.getX(), position.getY(), tree.getRadius(), tree.getRadius());
		
		gc.setFill(Color.WHITE);
		Font font = Font.font("serif", FontWeight.BOLD, tree.getRadius() / 1.5);
		gc.setFont(font);
		
		if(value <= 9)
			gc.fillText(Integer.toString(value), position.getX() + tree.getRadius() / 3, position.getY()  + tree.getRadius() / 1.4);
		else
			gc.fillText(Integer.toString(value), position.getX() + tree.getRadius() / 5.5, position.getY()  + tree.getRadius() / 1.4);
	}

	/*Similar to the function above, but this is for highlight purpose*/
	void showNode(int select_value)
	{

		if (value == select_value) {
			gc.setFill(Color.GOLD);
			gc.fillOval(position.getX() - 2, position.getY() - 2, tree.getRadius() + 4, tree.getRadius() + 4);
		}
		
		if(color == RED)
			gc.setFill(Color.RED);
		else if(color == BLACK)
			gc.setFill(Color.BLACK);
		else
			gc.setFill(Color.GREEN);
		gc.fillOval(position.getX(), position.getY(), tree.getRadius(), tree.getRadius());
		

		gc.setFill(Color.WHITE);
		Font font = Font.font("serif", FontWeight.BOLD, tree.getRadius() / 1.5);
		gc.setFont(font);

		if (value <= 9)
			gc.fillText(Integer.toString(value), position.getX() + tree.getRadius() / 3,
					position.getY() + tree.getRadius() / 1.4);
		else
			gc.fillText(Integer.toString(value), position.getX() + tree.getRadius() / 5.5,
					position.getY() + tree.getRadius() / 1.4);
	}
	
	
	//Below are setters and getters
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public GraphicsContext getGc() {
		return gc;
	}
	
	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}
	
	public boolean isHigh_light() {
		return high_light;
	}
	
	public void setHigh_light(boolean new_node) {
		this.high_light = high_light;
	}
	

	
	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}
	

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isLeft_child_of_parent() {
		return left_child_of_parent;
	}

	public void setLeft_child_of_parent(boolean left_child_of_parent) {
		this.left_child_of_parent = left_child_of_parent;
	}

	public int getDepth() {
            int result=0;
            Node parentNode=this.parent;
            while (parentNode!=null){
                parentNode=parentNode.parent;
                result++;
            }
            return result;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}


	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}


	public int getLayer_idx() {
		return layer_idx;
	}

	public void setLayer_idx(int layer_idx) {
		this.layer_idx = layer_idx;
	}

	 
    public int getColor(){
        return color;
    }
    
    public void setColor(int color){
        this.color=color;
    }
    
    public boolean isRed(){
        if(color == RED)
        	return true;
        else
        	return false;
    }
    
	
}
