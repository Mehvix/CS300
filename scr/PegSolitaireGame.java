import java.util.Arrays;
import java.util.Scanner;

public class PegSolitaireGame 
{
	/**
	 * This method is responsible for everything from displaying the opening 
	 * welcome message to printing out the final thank you.  It will clearly be
	 * helpful to call several of the following methods from here, and from the
	 * methods called from here.  See the Sample Runs below for a more complete
	 * idea of everything this method is responsible for.
	 * 
	 * @param args - any command line arguments may be ignored by this method.
	 */

	public static void main(String[] args)
	{
		Scanner reader = new Scanner(System.in);
		char[][] board = createBoard(readValidInt(reader,
				"readValidInt(reader,\n" +
						"Board Style Menu\n" +
						"\t1) Cross\n" +
						"\t2) Circle\n" +
						"\t3) Triangle\n" +
						"\t4) Simple T\n" +
						"Choose a board style: ", 1, 4));
		displayBoard(board);

		while (countPegsRemaining(board) > 1 && countMovesAvailable(board) != 0){
			int[] arr = readValidMove(reader, board);  // get user input
			int col = arr[0]; int row = arr[1]; int dir = arr[2];
			performMove(board, row, col, dir);  // move pieces
			displayBoard(board);  // display updated board
		}

		if (countPegsRemaining(board) == 1){  // if there is only 1 piece remaining
			System.out.println("Congrats, you won!");
		} else {  // if there are multiple pieces
			System.out.println("It looks like there are no more legal moves.  Please try again.");
		}
		System.out.println("==========================================\n" +
				           "THANK YOU FOR PLAYING CS300 PEG SOLITAIRE!");
	}
		
	/**
	 * This method is used to read in all inputs from the user.  After printing
	 * the specified prompt, it will check whether the user’s input is in fact
	 * an integer within the specified range.  If the user’s input does not 
	 * represent an integer or does not fall within the required range, print
	 * an error message asking for a value within that range before giving the
	 * user another chance to enter valid input.  The user should be given as
	 * many chances as they need to enter a valid integer within the specified
	 * range.  See the Sample Runs to see how these error messages should be 
	 * phrased, and to see how the prompts are repeated when multiple invalid 
	 * inputs are entered by the user.
	 * 
	 * @param in - user input from standard in is ready through this.
	 * @param prompt - message describing what the user is expected to enter.
	 * @param min - the smallest valid integer that the user may enter.
	 * @param max - the largest valid integer that the user may enter.
	 * @return - the valid integer between min and max entered by the user.
	 */
	public static int readValidInt(Scanner in, String prompt, int min, int max)
	{
		System.out.println(prompt);
		int value = 0;
		if(in.hasNextInt()) {  // if user inputs an int
			value = in.nextInt();
			if (value > max || value < min) {  // if input falls outside range of min-max
				return readValidInt(new Scanner(System.in), "Please enter your choice as an integer between " + min + " and " + max + ":", min, max);
			}
			return value;
		} else {  // if user DOESN'T input an int
			return readValidInt(new Scanner(System.in), "Please enter your choice as an integer between " + min + " and " + max + ":", min, max);
		}

	}

	/**
	 * This method creates, initializes, and then returns a rectangular two
	 * dimensional array of characters according to the specified boardType.
	 * Initial configurations for each of the possible board types are depicted
	 * below.  Note that pegs are displayed as @s, empty holes are displayed as
	 * -s, and extra blank positions that are neither pegs nor holes within 
	 * each rectangular array are displayed as #s.
	 * 
	 * @param boardType - 1-4 indicating one of the following initial patterns:
	 *   1) Cross
	 *     ###@@@###
	 *     ###@@@###
	 *     @@@@@@@@@
	 *     @@@@-@@@@
	 *     @@@@@@@@@
	 *     ###@@@###
	 *     ###@@@###
	 *     
	 *   2) Circle
	 *     #-@@-#
	 *     -@@@@-
	 *     @@@@@@
	 *     @@@@@@
	 *     -@@@@-
	 *     #-@@-#
	 *     
	 *   3) Triangle
	 *     ###-@-###
	 *     ##-@@@-##
	 *     #-@@-@@-#
	 *     -@@@@@@@-
	 *     
	 *   4) Simple T
	 *     -----
	 *     -@@@-
	 *     --@--
	 *     --@--
	 *     -----
	 *     
	 * @return - the fully initialized two dimensional array.
	 */
	public static char[][] createBoard(int boardType)
	{
		char[][] board = new char[0][];
		switch (boardType) {
			case 1: {  // cross
				return new char[][]{
						"###@@@###".toCharArray(),
						"###@@@###".toCharArray(),
						"@@@@@@@@@".toCharArray(),
						"@@@@-@@@@".toCharArray(),
						"@@@@@@@@@".toCharArray(),
						"###@@@###".toCharArray(),
						"###@@@###".toCharArray()};
			}
			case 2: {  // circle
				return new char[][]{
						"#-@@-#".toCharArray(),
						"-@@@@-".toCharArray(),
						"@@@@@@".toCharArray(),
						"@@@@@@".toCharArray(),
						"-@@@@-".toCharArray(),
						"#-@@-#".toCharArray()};
			}
			case 3: {  // triangle
				return new char[][]{
						"###-@-###".toCharArray(),
						"##-@@@-##".toCharArray(),
						"#-@@-@@-#".toCharArray(),
						"-@@@@@@@-".toCharArray()};
			}
			case 4: {  // simple T
				return new char[][]{
						"-----".toCharArray(),
						"-@@@-".toCharArray(),
						"--@--".toCharArray(),
						"--@--".toCharArray(),
						"-----".toCharArray()};
			}
		}
		return board;
	}
	
	/**
	 * This method prints out the contents of the specified board using @s to 
	 * represent pegs, -s to represent empty hole, and #s to represent empty 
	 * positions that are neither pegs nor holes.  In addition to this, the 
	 * columns and rows of this board should be labelled with numbers starting 
	 * at one and increasing from left to right (for column labels) and from 
	 * top to bottom (for row labels).  See the Sample Runs for examples of how
	 * these labels appear next to boards with different dimensions.
	 * 
	 * @param board - the current state of the board being drawn.
	 */
	public static void displayBoard(char[][] board)
	{
		System.out.print("  ");  // spacing for column nums

		for (int i = 1; i < board[0].length + 1; i ++){   // column nums
			System.out.print(i + " ");
		}
		System.out.print("\n");

		int count = 1;  // for counting row number
		for (char[] i : board) {  // each row
			System.out.print(count+ " ");
			for (char j : i) {  // each item in each row
				System.out.print(j + " ");  // add a space for legibility
			}
			count ++;
			System.out.print("\n");
		}
	}

	/**
	 * This method is used to read in and validate each part of a user’s move 
	 * choice: the row and column that they wish to move a peg from, and the 
	 * direction that they would like to move/jump that peg in.  When the 
	 * player’s row, column, and direction selection does not represent a valid
	 * move, your program should report that their choice does not constitute a
	 * legal move before giving them another chance to enter a different move.  
	 * They should be given as many chances as necessary to enter a legal move.
	 * The array of three integers that this method returns will contain: the 
	 * user’s choice of column as the first integer, their choice of row as the
	 * second integer, and their choice of direction as the third.
	 * 
	 * @param in - user input from standard in is ready through this.
	 * @param board - the state of the board that moves must be legal on.
	 * @return - the user's choice of column, row, and direction representing
	 *   a valid move and store in that order with an array.
	 */
	public static int[] readValidMove(Scanner in, char[][] board)
	{
		// The row and column that they wish to move a peg from
		int col = readValidInt(in, "Choose the COLUMN of a peg you'd like to move: ", 1, board[0].length);  // max is equal to size of col
		int row = readValidInt(in, "Choose the ROW of a peg you'd like to move: ", 1, board.length);  // max is equal to size of row

		// Direction that they would like to move/jump that peg in
		int dir = readValidInt(in, "Choose a DIRECTION to move that peg 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT: ", 1, 4);

		/* When the player’s row, column, and direction selection does not represent a
		 * valid move, your program should report that their choice does not constitute
		 * a legal move before giving them another chance to enter a different move. */
		String[] direction = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};
		while(!isValidMove(board, row, col, dir)){  // if the move isn't valid, reask the user
			if (countMovesAvailable(board) > 0) {
				System.out.println("Moving a peg from row " + row + " and column " + col + " " + direction[dir - 1] + " is not currently a legal move.");
				col = readValidInt(in, "Choose the COLUMN of a peg you'd like to move: ", 1, board[0].length);
				row = readValidInt(in, "Choose the ROW of a peg you'd like to move: ", 1, board.length);
				dir = readValidInt(in, "Choose a DIRECTION to move that peg 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT: ", 1, 4);
			} else {  // (in theory) this shouldn't be called ever, but better safe than sorry!
				System.out.println("It looks like there are no more legal moves.  Please try again.");
				System.out.println("==========================================\n" +
								   "THANK YOU FOR PLAYING CS300 PEG SOLITAIRE!");
				System.exit(0);
			}
		}

		/* The array of three integers that this method returns will contain: the
		 * user’s choice of column as the first integer, their choice of row as the
		 * second integer, and their choice of direction as the third. */
		return new int[]{col, row, dir};
	}
	
	/**
	 * This method checks whether a specific move (column + row + direction) is
	 * valid within a specific board configuration.  In order for a move to be
	 * a valid:
		 * 1) there must be a peg at position row, column within the board,
		 * 2) there must be another peg neighboring that first one in the specified
		 *    direction, and
		 * 3) there must be an empty hole on the other side of that
		 *    neighboring peg (further in the specified direction).
	 * This method only returns true when all three of these conditions are met.
	 * If any of the three positions being checked happen to fall beyond the bounds
	 * of your board array, then this method should return false.  Note that the
	 * row and column parameters here begin with one, and may need to be 
	 * adjusted if your programming language utilizes arrays with zero-based 
	 * indexing.
	 * 
	 * @param board - the state of the board that moves must be legal on.
	 * @param row - the vertical position of the peg proposed to be moved.
	 * @param column - the horizontal position of the peg proposed to be moved.
	 * @param direction - the direction proposed to move/jump that peg in.
	 * @return - true when the proposed move is legal, otherwise false.
	 */
	public static boolean isValidMove(char[][] board, int row, int column, int direction)
	{
		row --;  // because start counting at 0, while the game starts counting at 1 (1 greater than zero)
		column --;
		if (board[row][column] == '@') {    // 1) there must be a peg at position row, column within the board,
			// Position is a Peg
			boolean validNeighbor = false;  // 2) there must be another peg neighboring that first one in the specified direction, and
			try {
				switch (direction) {  // 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT:
					case 1:
						if (board[row - 1][column] == '@') validNeighbor = true;
						break;
					case 2:
						if (board[row + 1][column] == '@') validNeighbor = true;
						break;
					case 3:
						if (board[row][column - 1] == '@') validNeighbor = true;
						break;
					case 4:
						if (board[row][column + 1] == '@') validNeighbor = true;
						break;
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				// Direction places endpoint outside
				return false;
			}
			if (validNeighbor) {  // 3) there must be an empty hole on the other side of that
				// Neighbor is valid
				boolean emptyLocation = false;
				try {
					switch (direction) {  // 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT:
						case 1:
							if (board[row - 2][column] == '-') emptyLocation = true;
							break;
						case 2:
							if (board[row + 2][column] == '-') emptyLocation = true;
							break;
						case 3:
							if (board[row][column - 2] == '-') emptyLocation = true;
							break;
						case 4:
							if (board[row][column + 2] == '-') emptyLocation = true;
							break;
					}
					if (emptyLocation){
						// Location is empty
						return true;
					} else {
						// Location overlaps (non-empty)
						return false;
					}
				} catch(ArrayIndexOutOfBoundsException e) {
					// Direction places end point outside
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * The parameters of this method are the same as those of the isValidMove()
	 * method.  However this method changes the board state according to this
	 * move parameter (column + row + direction), instead of validating whether
	 * the move is valid.  If the move specification that is passed into this
	 * method does not represent a legal move, then do not modify the board.
	 * 
	 * @param board - the state of the board will be changed by this move.
	 * @param row - the vertical position that a peg will be moved from.
	 * @param column - the horizontal position that a peg will be moved from.
	 * @param direction - the direction of the neighbor to jump this peg over.
	 * @return - the updated board state after the specified move is taken.
	 */
	public static char[][] performMove(char[][] board, int row, int column, int direction)
	{
		row --;
		column --;

		board[row][column] = '-';  // set 'old' location to blank
		switch (direction) {  // 1) UP, 2) DOWN, 3) LEFT, or 4) RIGHT:
			case 1:
				board[row - 1][column] = '-';  // remove piece being jumped over
				board[row - 2][column] = '@';  // update new location
				break;
			case 2:
				board[row + 1][column] = '-';
				board[row + 2][column] = '@';
				break;
			case 3:
				board[row][column - 1] = '-';
				board[row][column - 2] = '@';
				break;
			case 4:
				board[row][column + 1] = '-';
				board[row][column + 2] = '@';
				break;
		}
		return board;
	}
	
	/**
	 * This method counts up the number of pegs left within a particular board 
	 * configuration, and returns that number.
	 * 
	 * @param board - the board that pegs are counted from.
	 * @return - the number of pegs found in that board.
	 */
	public static int countPegsRemaining(char[][] board)
	{
		int count = 0;
		for (char[] i : board) {  // for each row
			for (char j : i) {  // for each item in each row
				if (j == '@') count ++;
			}
		}
		return count;
	}
	
	/**
	 * This method counts up the number of legal moves that are available to be
	 * performed in a given board configuration.
	 * 
	 * HINT: Would it be possible to call the isValidMove() method for every
	 * direction and from every position within your board?  Counting up the
	 * number of these calls that return true should yield the total number of
	 * moves available within a specific board.
	 * 
	 * @param board - the board that possible moves are counted from.
	 * @return - the number of legal moves found in that board.
	 */
	public static int countMovesAvailable(char[][] board)
	{
		int count = 0;  // keep track  of valid moves
		for(int i = 1; i <= board.length; i++){  // each row
			for(int j = 1; j <= board[0].length; j++){  // each item in each row
				for(int k = 1; k < 5; k++){   // each possible move (4; up, down, left, right)
					if(isValidMove(board, i, j, k)){
						count++;
					}
				}
			}
		}
		return count;
	}	

}
