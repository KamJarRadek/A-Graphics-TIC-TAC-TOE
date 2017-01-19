package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TTTGraphics2P extends JFrame {

	public static final int ROWS = 3;
	public static final int COLS = 3;

	public static final int CELL_SIZE = 100;
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	public static final int GRID_WIDTH = 8;
	public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

	public static final int CELL_PADDING = CELL_SIZE / 6;
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	public static final int SYMBOL_STROKE_WIDTH = 8;

	private static GameState currentState;
	private static Seed currentPlayer;

	private static Seed[][] board;

	public static Seed[][] getBoard() {
		return board;
	}

	public static Seed getCurrentPlayer() {
		return currentPlayer;
	}

	public static void setCurrentPlayer(Seed currentPlayer) {
		TTTGraphics2P.currentPlayer = currentPlayer;
	}

	public static GameState getCurrentState() {
		return currentState;
	}

	public void setBoard(Seed[][] board) {
		this.board = board;
	}

	private DrawCanvas canvas;
	private static JLabel statusBar;

	// ------------------------------------------------------------------------
	public TTTGraphics2P() {
		canvas = new DrawCanvas(); // Construct a drawing canvas (a JPanel)
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		// The canvas (JPanel) fires a MouseEvent upon mouse-click
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { // mouse-clicked handler
				int mouseX = e.getX();
				int mouseY = e.getY();
				// Get the row and column clicked
				int rowSelected = mouseY / CELL_SIZE;
				int colSelected = mouseX / CELL_SIZE;

				if (currentState == GameState.PLAYING) {
					if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS
							&& board[rowSelected][colSelected] == Seed.EMPTY) {
						board[rowSelected][colSelected] = currentPlayer; // Make
																			// a
																			// move
						updateGame(currentPlayer, rowSelected, colSelected); // update
																				// state
						// Switch player
						currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
					}
				} else { // game over
					initGame(); // restart the game
				}
				// Refresh the drawing canvas
				repaint(); // Call-back paintComponent().
			}
		});

		// Setup the status bar (JLabel) to display status message
		statusBar = new JLabel("  ");
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack all the components in this JFrame
		setTitle("Tic Tac Toe");
		setVisible(true); // show this JFrame

		board = new Seed[ROWS][COLS]; // allocate array
		initGame(); // initialize the game board contents and game variables
	}

	public static JLabel getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(JLabel statusBar) {
		this.statusBar = statusBar;
	}

	/** Initialize the game-board contents and the status */
	public void initGame() {
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				board[row][col] = Seed.EMPTY; // all cells empty
			}
		}
		currentState = GameState.PLAYING; // ready to play
		currentPlayer = Seed.CROSS; // cross plays first
	}

	/**
	 * Update the currentState after the player with "theSeed" has placed on
	 * (rowSelected, colSelected).
	 */
	public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
		if (hasWon(theSeed, rowSelected, colSelected)) { // check for win
			currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
		} else if (isDraw()) { // check for draw
			currentState = GameState.DRAW;
		}
		// Otherwise, no change to current state (still GameState.PLAYING).
	}

	/** Return true if it is a draw (i.e., no more empty cell) */
	public boolean isDraw() {
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				if (board[row][col] == Seed.EMPTY) {
					return false; // an empty cell found, not draw, exit
				}
			}
		}
		return true; // no more empty cell, it's a draw
	}

	/**
	 * Return true if the player with "theSeed" has won after placing at
	 * (rowSelected, colSelected)
	 */
	public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
		return (board[rowSelected][0] == theSeed // 3-in-the-row
				&& board[rowSelected][1] == theSeed && board[rowSelected][2] == theSeed
				|| board[0][colSelected] == theSeed // 3-in-the-column
						&& board[1][colSelected] == theSeed && board[2][colSelected] == theSeed
				|| rowSelected == colSelected // 3-in-the-diagonal
						&& board[0][0] == theSeed && board[1][1] == theSeed && board[2][2] == theSeed
				|| rowSelected + colSelected == 2 // 3-in-the-opposite-diagonal
						&& board[0][2] == theSeed && board[1][1] == theSeed && board[2][0] == theSeed);
	}
	// ===================================================================

	public static void main(String[] args) {
		// Run GUI codes in the Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TTTGraphics2P(); // Let the constructor do the job
			}
		});
	}

}
