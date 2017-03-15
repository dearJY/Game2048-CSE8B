//File Namei Board.java
//Purpose: To create the properties of grid.  
//Name: Jingyi Ouyang
//Date: 04/19/2016

import java.util.*;
import java.io.*;

public class Board {

	public final int NUM_START_TILES = 2;
	public final int TWO_PROBABILITY = 90;
	public final int GRID_SIZE;

	private final Random random;
	private int[][] grid;
	private int score;

	//purpose:constructs a new board
	//parameter: boardSize, random
	public Board(int boardSize, Random random) {

		this.random = random;
		GRID_SIZE = boardSize;
		score = 0;
		grid = new int[boardSize][boardSize];

		//gives first two random value into new board
		for(int i=1;i<=NUM_START_TILES;i++) {
			addRandomTile();
		}
		System.out.println();
	}

	//purpose: Constructs a board based off of an input file
	//parameter: inputBoard, random
	public Board(String inputBoard, Random random) throws IOException {

		this.random = random;

		//read a board from an input file
		Scanner input  = new Scanner(new File(inputBoard));
		GRID_SIZE = input.nextInt();
		grid = new int[GRID_SIZE][GRID_SIZE];
		score = input.nextInt();
		for( int i=0;i<GRID_SIZE;i++ ) {
			for( int j=0;j<GRID_SIZE;j++ ) {
				grid[i][j] = input.nextInt();
			}
		}
		System.out.println();
	}

	// purpose:Saves the current board to a file
	// parameter:outputBoard
	// return type:void
	public void saveBoard(String outputBoard) throws IOException {

		//write the board into a file
		PrintWriter output = new PrintWriter( outputBoard );
		output.println( GRID_SIZE );
		output.println( score );
		for( int i=0;i<GRID_SIZE;i++ ) {
			for( int j=0;j<GRID_SIZE;j++) {
				output.print( grid[i][j] + " ");
			}
			output.println();
		}
		output.close();
	}

	// purpose:Adds a random tile (of value 2 or 4) to a
	//         random empty space on the board
	// parameter:none
	// return type:void
	public void addRandomTile() {

		//counts the number of empty grids
		int count = 0;
		for( int i=0;i<GRID_SIZE;i++ ) {
			for( int j=0;j<GRID_SIZE;j++ ) {
				if( grid[i][j] == 0 ) {
					count = count + 1;
				}
			}
		}
		if( count == 0 ) {
			System.exit(0);
		}

		//generates random location and random 2 or 4
		int location = random.nextInt(count);
		int value = 0;
		if( random.nextInt(100)<TWO_PROBABILITY) {
			value = 2;
		}else{
			value = 4;
		}
		int count1 = 0;

		//add random number to the random location
A:
		for( int i=0;i<GRID_SIZE;i++ ) {
			for( int j=0;j<GRID_SIZE;j++) {
				if( grid[i][j]==0 ) {
					count1 = count1+1;
					if( (count1-1) == location ) {
						grid[i][j] = value;
						break A;
					}
				}
			}
		}

	}


	// purpose:Rotates the board by 90 degrees clockwise 
	//         or 90 degrees counter-clockwise.
	// parameter: rotateClockwise
	// return type:void
	public void rotate(boolean rotateClockwise) {

		// If rotateClockwise == true, rotates the board clockwise , else rotates
		// the board counter-clockwise
		int[][] rotatearr = new int[GRID_SIZE][GRID_SIZE];
		if( rotateClockwise == true ) {
			for( int i=0;i<GRID_SIZE;i++ ) {
				for( int j=0;j<GRID_SIZE;j++ ) {
					rotatearr[j][GRID_SIZE-1-i] = grid[i][j];
				}
			}
			grid = rotatearr;
		}else {
			for( int i=0;i<GRID_SIZE;i++ ) {
				for( int j=0;j<GRID_SIZE;j++ ) {
					rotatearr[GRID_SIZE-1-j][i] = grid[i][j];
				}
			}
			grid = rotatearr;
		}
	}

	//Complete this method ONLY if you want to attempt at getting the extra credit
	//Returns true if the file to be read is in the correct format, else return
	//false
	public static boolean isInputFileCorrectFormat(String inputFile) {

		try {
			Scanner input  = new Scanner( new File(inputFile) );
			String size = input.nextLine();
			int a = Integer.parseInt( size );
			String score = input.nextLine();
			int b = Integer.parseInt( score );
			boolean c = true;
			for( int i=0;i<a;i++ ) {
				String gridRow = input.nextLine();
				String[] d = gridRow.split("\t");
				if (d.length == a) {
					for (int j = 0; j < a; j++) {
						int e = Integer.parseInt(d[j]);
						c = ((e & (e - 1)) == 0) && c;//whether it's power of 2
					}
				}else return false;
			};
			boolean f = !input.hasNext();
			boolean total = f&&c;
			return total;
		} catch (Exception e) {
			return false;
		}
	}

	// purpose: performs a move Operation
	// parameter: Direction direction
	// return type:boolean
	public boolean move(Direction direction) {
		if ( direction.equals( Direction.LEFT ) ) {
			return moveLeft();
		}else if ( direction.equals( Direction.RIGHT ) ) {
			return moveRight();
		}else if ( direction.equals( Direction.UP ) ) {
			return moveUp();
		}else return moveDown();
	}

	//purpose: performs a left movement
	//parameter: none
	//return type: boolean
	private boolean moveLeft() {
		
		//looks for two neighboring numbers with same values without 
		//considering zero between them and combining them
		//to the left side.
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++) {
				if ( grid[i][j] == 0 ) continue;
				for ( int k=j+1;k<GRID_SIZE;k++ ) {
					if ( grid[i][k] == 0 ) continue;
					else if ( grid[i][j] != grid[i][k] ) {
						j = k-1;
						break;
					}else {
						grid[i][j] += grid[i][j];
						grid[i][k] = 0;
						score += grid[i][j];
						j = k;//j=k+1 ?
						break;
					}
				}
			}
		}

		//moves all the numbers to the left side
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if ( grid[i][j] != 0 ) continue;
				for ( int k=j+1;k<GRID_SIZE;k++ ) {
					if ( grid[i][k] == 0 ) continue;
					else {
						grid[i][j] = grid[i][k];
						grid[i][k] = 0;
						break;
					}
				}
			}
		}
		return true;
	}

	//purpose: performs a right movement
	//parameter: none
	//return type: boolean
	//similar to moveLeft()
	private boolean moveRight() {

		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=GRID_SIZE-1;j>=0;j--) {
				if ( grid[i][j] == 0 ) continue;
				for ( int k=j-1;k>=0;k-- ) {
					if ( grid[i][k] == 0 ) continue;
					else if ( grid[i][j] != grid[i][k] ) {
						j = k+1;
						break;
					}else {
						grid[i][j] += grid[i][j];
						grid[i][k] = 0;
						score += grid[i][j];
						j = k;//j=k+1 ?
						break;
					}
				}
			}
		}

		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=GRID_SIZE-1;j>=0;j-- ) {
				if ( grid[i][j] != 0 ) continue;
				for ( int k=j-1;k>=0;k-- ) {
					if ( grid[i][k] == 0 ) continue;
					else {
						grid[i][j] = grid[i][k];
						grid[i][k] = 0;
						break;
					}
				}
			}
		}
		return true;
	}

	//purpose: performs a up movement
	//parameter: none
	//return type: boolean
	//similar to moveLeft()
	private boolean moveUp() {

		for ( int j=0;j<GRID_SIZE;j++ ) {
			for ( int i=0;i<GRID_SIZE;i++) {
				if ( grid[i][j] == 0 ) continue;
				for ( int k=i+1;k<GRID_SIZE;k++ ) {
					if ( grid[k][j] == 0 ) continue;
					else if ( grid[i][j] != grid[k][j] ) {
						i = k-1;
						break;
					}else {
						grid[i][j] += grid[i][j];
						grid[k][j] = 0;
						score += grid[i][j];
						i = k;//j=k+1 ?
						break;
					}
				}
			}
		}

		for ( int j=0;j<GRID_SIZE;j++ ) {
			for ( int i=0;i<GRID_SIZE;i++ ) {
				if ( grid[i][j] != 0 ) continue;
				for ( int k=i+1;k<GRID_SIZE;k++ ) {
					if ( grid[k][j] == 0 ) continue;
					else {
						grid[i][j] = grid[k][j];
						grid[k][j] = 0;
						break;
					}
				}
			}
		}
		return true;
	}

	//purpose: performs a down movement
	//parameter: none
	//return type: boolean
	//similar to moveLeft()
	private boolean moveDown() {

		for ( int j=0;j<GRID_SIZE;j++ ) {
			for ( int i=GRID_SIZE-1;i>=0;i--) {
				if ( grid[i][j] == 0 ) continue;
				for ( int k=i-1;k>=0;k-- ) {
					if ( grid[k][j] == 0 ) continue;
					else if ( grid[i][j] != grid[k][j] ) {
						i = k+1;
						break;
					}else {
						grid[i][j] += grid[i][j];
						grid[k][j] = 0;
						score += grid[i][j];
						i = k;//j=k+1 ?
						break;
					}
				}
			}
		}

		for ( int j=0;j<GRID_SIZE;j++ ) {
			for ( int i=GRID_SIZE-1;i>=0;i-- ) {
				if ( grid[i][j] != 0 ) continue;
				for ( int k=i-1;k>=0;k-- ) {
					if ( grid[k][j] == 0 ) continue;
					else {
						grid[i][j] = grid[k][j];
						grid[k][j] = 0;
						break;
					}
				}
			}
		}
		return true;
	}


	// purpose: check if the game is over
	// parameter: none
	// return type: boolean
	public boolean isGameOver() {
		
		// grid can not move in all 4 directions
		if ( canMoveLeft()||canMoveRight()||canMoveUp()||canMoveDown() ) return false;
		else return true;
	}

	// purpose: determine if we can move in a given direction
	// parameter: Direction direction
	// return type: boolean
	public boolean canMove(Direction direction) {

		if ( direction.equals( Direction.LEFT ) )  {
			return canMoveLeft();
		}else if ( direction.equals( Direction.RIGHT ) ) {
			return canMoveRight();
		}else if ( direction.equals( Direction.UP ) ) {
			return canMoveUp();
		}else return canMoveDown();

	}
	
	// purpose: determine if grid can be moved left
	// parameter: none
	// return type: boolean
	private boolean canMoveLeft() {

		// whether contains a blank cell that has a non-zero number 
		// on its right.
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if ( grid[i][j]==0 ) {
					for ( int k=j+1;k<GRID_SIZE;k++ ) {
						if ( grid[i][k]!=0 ) {
							return true;
						}
					}
				}
			}
		}
		
		//whether contains two neighboring number with same value without considering
		//zero cells between them
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE-1;j++ ) {
				if( grid[i][j] == grid[i][j+1] && grid[i][j]!=0 && grid[i][j+1]!=0 ) {
					return true;
				}
			}
		}

		return false;
	}

	// purpose: determine if grid can be moved right
	// parameter: none
	// return type: boolean
	// similar to canMoveLeft()
	private boolean canMoveRight() {
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=GRID_SIZE-1;j>=0;j-- ) {
				if ( grid[i][j]==0 ) {
					for ( int k=j-1;k>=0;k-- ) {
						if ( grid[i][k]!=0 ) {
							return true;
						}
					}
				}
			}
		}

		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE-1;j++ ) {
				if( grid[i][j] == grid[i][j+1] && grid[i][j]!=0 && grid[i][j+1]!=0 ) {
					return true;
				}
			}
		}

		return false;
	}

	// purpose: determine if grid can be moved up
	// parameter: none
	// return type: boolean
	// similar to canMoveLeft()
	private boolean canMoveUp() {
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if ( grid[i][j]==0 ) {
					for ( int k=i+1;k<GRID_SIZE;k++ ) {
						if ( grid[k][j]!=0 ) {
							return true;
						}
					}
				}
			}
		}

		for ( int i=0;i<GRID_SIZE-1;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if( grid[i][j] == grid[i+1][j] && grid[i][j]!=0 && grid[i+1][j]!=0 ) {
					return true;
				}
			}
		}

		return false;
	}

	// purpose: determine if grid can be moved down
	// parameter: none
	// return type: boolean
	// similar to canMoveLeft()
	private boolean canMoveDown() {
		for ( int i=0;i<GRID_SIZE;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if ( grid[i][j]==0 ) {
					for ( int k=i-1;k>=0;k-- ) {
						if ( grid[k][j]!=0 ) {
							return true;
						}
					}
				}
			}
		}

		for ( int i=0;i<GRID_SIZE-1;i++ ) {
			for ( int j=0;j<GRID_SIZE;j++ ) {
				if( grid[i][j] == grid[i+1][j] && grid[i][j]!=0 && grid[i+1][j]!=0 ) {
					return true;
				}
			}
		}

		return false;
	}

	// Return the reference to the 2048 Grid
	public int[][] getGrid() {
		return grid;
	}

	// Return the score
	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		outputString.append(String.format("Score: %d\n", score));
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int column = 0; column < GRID_SIZE; column++)
				outputString.append(grid[row][column] == 0 ? "    -" :
						String.format("%5d", grid[row][column]));

			outputString.append("\n");
		}
		return outputString.toString();
	}

	public void undo() {
		Scanner input  = new Scanner( "FormerGrid.txt" );
		System.out.println( input.nextLine() );
		for (int i = 0; i < GRID_SIZE; i++) {
			System.out.print("   ");
			System.out.print( input.nextLine() );
			System.out.println();
		}
		System.out.println();
	}
}
