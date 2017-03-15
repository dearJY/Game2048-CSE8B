//------------------------------------------------------------------//
// Gui2048.java                                                     //
//                                                                  //
// game purpose:Game Driver for 2048                                //
//                                                                  //
// Author: Jingyi Ouyang
//                                                                  //
// Date:    05/25/16                                                //
//------------------------------------------------------------------//


import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
	private String outputBoard; // The filename for where to save the Board
	private Board board; // The 2048 Game Board

	private static final int TILE_WIDTH = 106;

	private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
	private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
	//(128, 256, 512)
	private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
	//(1024, 2048, Higher)

	// Fill colors for each of the Tile values
	private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
	private static final Color COLOR_2 = Color.rgb(238, 228, 218);
	private static final Color COLOR_4 = Color.rgb(237, 224, 200);
	private static final Color COLOR_8 = Color.rgb(242, 177, 121);
	private static final Color COLOR_16 = Color.rgb(245, 149, 99);
	private static final Color COLOR_32 = Color.rgb(246, 124, 95);
	private static final Color COLOR_64 = Color.rgb(246, 94, 59);
	private static final Color COLOR_128 = Color.rgb(237, 207, 114);
	private static final Color COLOR_256 = Color.rgb(237, 204, 97);
	private static final Color COLOR_512 = Color.rgb(237, 200, 80);
	private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
	private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
	private static final Color COLOR_OTHER = Color.BLACK;
	private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

	private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
	// For tiles >= 8

	private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
	// For tiles < 8

	private GridPane pane;
	private Text gameTile = new Text("2048");
	private Text gameScore;
	private int tileNumber;
	private int count = 0;

	//store each Tile
	ArrayList<Tile> tile = new ArrayList<Tile>();
	private int sceneWidth;
	private int sceneHeight;
	private StackPane pane1 = new StackPane();
	private int countOver = 0;
	private int sp1 = 0;
	private int sp2 = 0;
	/** Add your own Instance Variables here */




	@Override
	public void start(Stage primaryStage) {
		// Process Arguments and Initialize the Game Board
		processArgs(getParameters().getRaw().toArray(new String[0]));

		// Create the pane that will hold all of the visual objects
		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
		// Set the spacing between the Tiles
		pane.setHgap(15);
		pane.setVgap(15);

		//odd or even board has different text span to ensure
		//interface looking good
		if( board.GRID_SIZE%2 == 0 ) {
			sp1 = board.GRID_SIZE/2;
			sp2 = board.GRID_SIZE/2;
		}else {
			sp1 = board.GRID_SIZE/2+1;
			sp2 = board.GRID_SIZE/2;
		}

		//set game name "2048" on the left corner
		pane.add(gameTile, 0, 0,sp2,1);
		gameTile.setFont(Font.font("Calibri", FontWeight.BOLD, 35));

		//set game score on the right corner
		gameScore = new Text("Score: " + board.getScore());
		pane.add(gameScore,sp2,0,sp1,1);
		gameScore.setFont(Font.font("Calibri", FontWeight.BOLD, 35));
		GridPane.setHalignment( gameScore,HPos.RIGHT );



		//set each tile properties
		for (int i = 1; i <= board.GRID_SIZE; i++) {
			for (int j = 0; j < board.GRID_SIZE; j++) {

				//create Tile class containing a rectangle and
				//a tilenumber
				Tile singleTile = new Tile();
				tile.add(singleTile);//store Tile into arraylist
				tileNumber = board.getGrid()[i - 1][j];

				//set width,height,color,arc of the rectangle
				singleTile.rTile.setWidth(TILE_WIDTH - change(TILE_WIDTH) );
				singleTile.rTile.setHeight(TILE_WIDTH - change(TILE_WIDTH) );
				singleTile.rTile.setFill(setTileColor(tileNumber));
				singleTile.rTile.setStyle("-fx-arc-height:20;-fx-arc-width:20");

				//for extra credit Part1 B
				//bind rectangle width and height to the pane's width and height
				singleTile.rTile.widthProperty().bind
					(pane1.widthProperty().divide(board.GRID_SIZE+2));
				singleTile.rTile.heightProperty().bind
					(pane1.heightProperty().divide(board.GRID_SIZE+2));

				//set font,size,color of tile number
				//when tilenumber is 0, nothing presents on the screen
				if( tileNumber == 0 ) {
					singleTile.tileText.setText("");
				}else singleTile.tileText.setText( "" + tileNumber );
				singleTile.tileText.setFont( Font.font("Calibri", FontWeight.BOLD,
						       	setTextSize(tileNumber)-change(setTextSize(tileNumber))) );
				singleTile.tileText.setFill(setTextColor(tileNumber));

				pane.add(singleTile.rTile, j, i);
				pane.add(singleTile.tileText, j, i);
				GridPane.setHalignment( singleTile.tileText,HPos.CENTER );
				GridPane.setHalignment( singleTile.rTile,HPos.CENTER );

			}
		}

		//add gridpane(pane) to Stackpane(pane1)
		pane1.getChildren().add(pane);
		Scene scene = new Scene( pane1 );
		sceneHeight = (int)scene.getHeight();
		sceneWidth = (int)scene.getWidth();
		scene.setOnKeyPressed(new myKeyHandler());
		primaryStage.setTitle( "Gui2048" );
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	//purpose: define what will be executed after press specific key
	private class myKeyHandler implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			//press arrows, r, or s for moving, rotating or saving boards
			switch(e.getCode()) {
				case DOWN:
					if(board.canMove(Direction.DOWN)) {
						board.move(Direction.DOWN);
						board.addRandomTile();
						updateBoard();
						System.out.println("Moving Down");
						//update score
						gameScore.setText("Score: " + board.getScore());
					}
					break;
				case UP:
					if(board.canMove(Direction.UP)){
						board.move( Direction.UP );
						board.addRandomTile();
						updateBoard();
						System.out.println( "Moving Up" );
						gameScore.setText( "Score: " + board.getScore() );
					}
					break;
				case LEFT:
					if(board.canMove(Direction.LEFT)) {
						board.move( Direction.LEFT );
						board.addRandomTile();
						updateBoard();
						System.out.println( "Moving Left" );
						gameScore.setText( "Score: " + board.getScore() );
					}
					break;
				case RIGHT:
					if(board.canMove(Direction.RIGHT)) {
						board.move( Direction.RIGHT );
						board.addRandomTile();
						updateBoard();
						System.out.println( "Moving Right" );
						gameScore.setText( "Score: " + board.getScore() );
					}
					break;
				case S:
					try {
						board.saveBoard(outputBoard);
						System.out.println("Saving board to" + "<" + outputBoard + ">");
					}catch( IOException e1) {
						System.out.println( "Save Board throws an Exception" );
					}
					break;
				case R:
					board.rotate(true);
					updateBoard();
					System.out.println("rotate");
					break;
				default:break;
			}
			//set interface when game is over
			if( board.isGameOver() && countOver == 0) {
				//background
				Rectangle back = new Rectangle();
				back.setWidth(sceneWidth);
				back.setHeight(sceneHeight);
				back.widthProperty().bind(pane.widthProperty());
				back.heightProperty().bind(pane.heightProperty());
				back.setFill(COLOR_GAME_OVER);

				//reminder:"game over!"
				Text gameOver = new Text("GAME OVER!");
				gameOver.setFont(Font.font("Calibri", FontWeight.BOLD, 60));
				gameOver.setFill(COLOR_VALUE_DARK);
				gameOver.setX(sceneWidth/2);
				gameOver.setY(sceneHeight/2);

				//add to the stack pane to overlap the board
				pane1.getChildren().add(back);
				pane1.getChildren().add(gameOver);
				//change countOver to non zero number then
				//there is no change on interface when press
				//any keys
				countOver++;
			}
		}
	}

	//Name: updateBoard
	//purpose: update board to the screen after moving, rotating.
	private  void updateBoard() {

		//unbind each tile to ensure that they can be set properties later
		//remove former tile to provide space for later tile to be added
		//into pane.
		for(int i=0;i<board.GRID_SIZE*board.GRID_SIZE;i++) {
			tile.get(i).rTile.widthProperty().unbind();
			tile.get(i).rTile.heightProperty().unbind();
			pane.getChildren().remove(tile.get(i).rTile);
			pane.getChildren().remove(tile.get(i).tileText);
		}

		//change each tile properties after changing the board
		for (int i = 1; i <= board.GRID_SIZE; i++) {
			for (int j = 0; j < board.GRID_SIZE; j++) {

				//get tile in the arraylist
				Tile currentTile = tile.get(count);

				//get new tile numbers
				tileNumber = board.getGrid()[i - 1][j];

				//same as setting properties of initial board before
				currentTile.rTile.setWidth(TILE_WIDTH - change(TILE_WIDTH) );
				currentTile.rTile.setHeight(TILE_WIDTH - change(TILE_WIDTH) );
				currentTile.rTile.setFill(setTileColor(tileNumber));
				currentTile.rTile.setStyle("-fx-arc-height:20;-fx-arc-width:20");
				currentTile.rTile.widthProperty().bind
					(pane1.widthProperty().divide(board.GRID_SIZE+2));
				currentTile.rTile.heightProperty().bind
					(pane1.heightProperty().divide(board.GRID_SIZE+2));


				if( tileNumber == 0 ) {
					currentTile.tileText.setText("");
				}else currentTile.tileText.setText( "" + tileNumber );
				currentTile.tileText.setFont( Font.font("Calibri", FontWeight.BOLD,
						       	setTextSize(tileNumber)-change(setTextSize(tileNumber))) );
				currentTile.tileText.setFill(setTextColor(tileNumber));


				pane.add(currentTile.rTile, j, i);
				pane.add(currentTile.tileText, j, i);
				GridPane.setHalignment( currentTile.tileText,HPos.CENTER );
				GridPane.setHalignment( currentTile.rTile,HPos.CENTER );

				//get to the next tile in the arraylist
				count++;
			}
		}
		//back to the first tile in the arraylist
		count = 0;
	}

	//creat Tile class containing rectangle and text for convenient using
	private class Tile {
		public Rectangle rTile;
		public Text tileText;

		public Tile() {
			rTile = new Rectangle();
			tileText = new Text("");
		}
	}

	//Name: setTileColor
	//return:Color
	//parameter:int tileNumber
	//purpose:set rectangle color for different tileNumber
	private Color setTileColor(int tileNumber) {
		switch (tileNumber) {
			case 0:
				return COLOR_EMPTY;
			case 2:
				return COLOR_2;
			case 4:
				return COLOR_4;
			case 8:
				return COLOR_8;
			case 16:
				return COLOR_16;
			case 32:
				return COLOR_32;
			case 64:
				return COLOR_64;
			case 128:
				return COLOR_128;
			case 256:
				return COLOR_256;
			case 512:
				return COLOR_512;
			case 1024:
				return COLOR_1024;
			case 2048:
				return COLOR_2048;
			default:
				return COLOR_OTHER;
		}
	}

	//Name:setTextColor
	//return:Color
	//parameter: int tileNumber
	//purpose: set text color for different tileNumber
	private Color setTextColor(int tileNumber) {

		if ( tileNumber<8 ) {
			return COLOR_VALUE_DARK;
		}else return COLOR_VALUE_LIGHT;
	}

	//Name:setTextSize
	//return: int
	//parameter:int tileNumber
	//purpose:set Text size for different tileNumber
	private int setTextSize( int tileNumber ) {
		if ( tileNumber<=8 ) {
			return  TEXT_SIZE_LOW;
		}else if( tileNumber>8 && tileNumber<=512 ) {
			return TEXT_SIZE_MID;
		}else return TEXT_SIZE_HIGH;
	}

	//For extra credit part1 A
	//change of rectangle and text size when the grid size
	// is smaller or larger than 4
	private int change( int size  ) {
		int change = (( board.GRID_SIZE-4)*size)/board.GRID_SIZE;
		return change;
	}

	/** DO NOT EDIT BELOW */

	// The method used to process the command line arguments
	private void processArgs(String[] args)
	{
		String inputBoard = null;   // The filename for where to load the Board
		int boardSize = 0;          // The Size of the Board

		// Arguments must come in pairs
		if((args.length % 2) != 0)
		{
			printUsage();
			System.exit(-1);
		}

		// Process all the arguments 
		for(int i = 0; i < args.length; i += 2)
		{
			if(args[i].equals("-i"))
			{   // We are processing the argument that specifies
				// the input file to be used to set the board
				inputBoard = args[i + 1];
			}
			else if(args[i].equals("-o"))
			{   // We are processing the argument that specifies
				// the output file to be used to save the board
				outputBoard = args[i + 1];
			}
			else if(args[i].equals("-s"))
			{   // We are processing the argument that specifies
				// the size of the Board
				boardSize = Integer.parseInt(args[i + 1]);
			}
			else
			{   // Incorrect Argument 
				printUsage();
				System.exit(-1);
			}
		}

		// Set the default output file if none specified
		if(outputBoard == null)
			outputBoard = "2048.board";
		// Set the default Board size if none specified or less than 2
		if(boardSize < 2)
			boardSize = 4;

		// Initialize the Game Board
		try{
			if(inputBoard != null)
				board = new Board(inputBoard, new Random());
			else
				board = new Board(boardSize, new Random());
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + 
					" was thrown while creating a " +
					"Board from file " + inputBoard);
			System.out.println("Either your Board(String, Random) " +
					"Constructor is broken or the file isn't " +
					"formated correctly");
			System.exit(-1);
		}
	}

	// Print the Usage Message 
	private static void printUsage()
	{
		System.out.println("Gui2048");
		System.out.println("Usage:  Gui2048 [-i|o file ...]");
		System.out.println();
		System.out.println("  Command line arguments come in pairs of the "+ 
				"form: <command> <argument>");
		System.out.println();
		System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
				"should be loaded");
		System.out.println();
		System.out.println("  -o [file]  -> Specifies a file that should be " + 
				"used to save the 2048 board");
		System.out.println("                If none specified then the " + 
				"default \"2048.board\" file will be used");  
		System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
				"board if an input file hasn't been"); 
		System.out.println("                specified.  If both -s and -i" + 
				"are used, then the size of the board"); 
		System.out.println("                will be determined by the input" +
				" file. The default size is 4.");
	}
}
