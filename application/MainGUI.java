package application;

import java.time.LocalTime;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class MainGUI extends Application {
	/*Below are GUI data members */
	private int canvas_width = 640; //canvas width
	private int canvas_height = 480; //canvas height
	private Button btn_add = new Button("add"); //button for adding a node
	private TextField tf = new TextField(); //textfield to enter node value
	private Canvas canvas = new Canvas(canvas_width, canvas_height);
	private GraphicsContext gc = canvas.getGraphicsContext2D(); //define the canvas brush
	
	/*The tree object that will be drawn on the canvas*/
	Tree tree = new Tree(canvas, gc);
	
	@Override
	public void start(Stage primaryStage) {
		try {
			/* Define the layout */
			primaryStage.setTitle("Black and Red Tree");
			VBox vbox = new VBox();
			HBox hbox = new HBox();

			/* Make the button and textfield in the center */
			hbox.setPadding(new Insets(20, 20, 10, 20));
			hbox.setAlignment(Pos.CENTER);
			tf.setPrefWidth(150); //set text field width 
			

			/*
			 * Make sure only numbers can be entered and no more than two digits
			 */
			tf.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent t) {
					if (t.getCharacter().length() > 0) {
						char ar[] = t.getCharacter().toCharArray();
						char ch = ar[t.getCharacter().toCharArray().length - 1];

						if (!(ch >= '0' && ch <= '9')) {
							t.consume();
						}

						if (tf.getText().length() == 2) {
							t.consume();
						}

					}

				}
			});

			/* Add the input number into the tree by button click */
			btn_add.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(tf.getText().length() > 0)
						createNode();
				}

			});
			
			
			
			
			/* Add the input number into the tree by Enter Key */
			tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent ke) {
					if (ke.getCode().equals(KeyCode.ENTER)) {
						if(tf.getText().length() > 0)
							createNode();

						ke.consume();
					} 
				}
			});
			
			
			canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		           @Override
		           public void handle(MouseEvent e) {
		              	tree.checkNodeDragging(e.getX(), e.getY());
		           }
		       });
			
			canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		           @Override
		           public void handle(MouseEvent e) {
		              	if(e.getButton() == MouseButton.SECONDARY )
		              	{
		              		tree.setSelect_node_value(-1);
		        			clearCanvas();
		        			tree.showTree(false);
		              	}
		           }
		       });
			
			canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
		           @Override
		           public void handle(MouseEvent e) {
		              	tree.finishNodeDragging(e.getX(), e.getY());
		           }
		       });

			 canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
		           @Override
		           public void handle(MouseEvent e) {
		              	tree.doNodeDragging(e.getX(), e.getY());
		           }
		       });
			
			Group root = new Group();
			hbox.getChildren().addAll(tf, btn_add);
			vbox.getChildren().addAll(hbox, canvas);
			root.getChildren().add(vbox);
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Create a node
	private void createNode()
	{
		
		//Get the value from TextField
		int num_value = Integer.parseInt(tf.getText());
		
		//Clear the TextField
		tf.clear();
		
		//Create the node object by the value
		Node node = new Node(num_value, this.tree, this.canvas, this.gc);
		
		//Insert the node to the tree
		boolean insertion_occur = tree.insertNode(node);
		tree.setNew_node(node);
		
		if(insertion_occur)
		{
			//Consider the new node as updated
			tree.setSelect_node_value(num_value);
			
			// Clear the canvas for re-draw later
			clearCanvas();

			// Draw the tree on canvas
			tree.showTree(true);
		}
		
		
	}
	
	
	
	//Clear the canvas
	private void clearCanvas()
	{
		gc.clearRect(0, 0, canvas_width, canvas_height);
	}
	
	
	

	//Setters and Getters
	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	

	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}



}
