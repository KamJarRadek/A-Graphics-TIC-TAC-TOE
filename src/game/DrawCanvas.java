package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawCanvas extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.cyan);

		g.setColor(Color.ORANGE);
		int gridWidth = TTTGraphics2P.GRID_WIDTH;
		int cellSize = TTTGraphics2P.CELL_SIZE;
		int cellPadding = TTTGraphics2P.CELL_PADDING;
		int symbolSize = TTTGraphics2P.SYMBOL_SIZE;

		for (int row = 0; row < TTTGraphics2P.ROWS; row++) {
			g.fillRoundRect(0, cellSize * row - TTTGraphics2P.GRID_WIDTH_HALF, TTTGraphics2P.CANVAS_WIDTH - 1,
					gridWidth, gridWidth, gridWidth);
		}
		for (int col = 0; col < TTTGraphics2P.COLS; col++) {
			g.fillRoundRect(cellSize * col - TTTGraphics2P.GRID_WIDTH_HALF, 0, gridWidth,
					TTTGraphics2P.CANVAS_HEIGHT - 1, gridWidth, gridWidth);
		}
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.setStroke(
				new BasicStroke(TTTGraphics2P.SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		for (int row = 0; row < TTTGraphics2P.ROWS; row++) {
			for (int col = 0; col < TTTGraphics2P.COLS; col++) {
				int x1 = col * cellSize + cellPadding;
				int y1 = row * cellSize + cellPadding;
				Seed[][] board = TTTGraphics2P.getBoard();
				if (board[row][col] == Seed.CROSS) {
					graphics2d.setColor(Color.red);
					int x2 = (col + 1) * cellSize - cellPadding;
					int y2 = (row + 1) * cellSize - cellPadding;
					graphics2d.drawLine(x1, y1, x2, y2);
					graphics2d.drawLine(x2, y1, x1, y2);
				} else if (board[row][col] == Seed.NOUGHT) {
					graphics2d.setColor(Color.blue);
					graphics2d.drawOval(x1, y1, symbolSize, symbolSize);
				}
			}
		}
		JLabel statusBar = TTTGraphics2P.getStatusBar();

		GameState currentState = TTTGraphics2P.getCurrentState();
		if (currentState == GameState.PLAYING) {
			statusBar.setForeground(Color.BLACK);
			if (TTTGraphics2P.getCurrentPlayer()== Seed.CROSS) {
				statusBar.setText("X's Turn");
			} else {
				statusBar.setText("O's Turn");
			}
		} else if (currentState == GameState.DRAW) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("It's a Draw! Click to play again.");
		} else if (currentState == GameState.CROSS_WON) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'X' Won! Click to play again.");
		} else if (currentState == GameState.NOUGHT_WON) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'O' Won! Click to play again.");
		}

	}

}
