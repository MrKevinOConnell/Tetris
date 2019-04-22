package tetris;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseListener;

public class Characters extends JPanel {
	/*
	 * create a method that initalizes the game 12 horizontal boxes, 23 vertical
	 * boxes (2d array) create the shapes by using the point class, 3d array that
	 * takes each combo of block and rotation once board and shapes are made make a
	 * method that brings shapes onto board, but they aren't permantantly attached
	 * randomize shapes by using java.util.Random or something of that sort (found
	 * java.util.collections which has more flexibility) make an array list that
	 * stores the possible pieces for the random shapes. for collisions, run a for
	 * loop for the array which checks if the territory they are going to be in is
	 * black if not black then it can't move make a thread which automatically moves
	 * the current shape down using a method after collisions keyboard movements are
	 * done, finally, take everything together and put it inside a JFrame
	 */

	// initalizations for game are created
	private final Color[] PieceColors = { Color.blue, Color.cyan, Color.gray, Color.yellow, Color.green, Color.pink,
			Color.WHITE };

	private Point ogPiece;
	private int currentPiece;
	private int rotation;
	private ArrayList<Integer> pieces = new ArrayList<Integer>();
	private Color[][] board;

	// Creates a border and initializes the game
	void start() {
		// creates a 2d array that colors are stored for the board (values 0-11,0-21
		board = new Color[12][22];
		// for every block on the board, make the color black as a default unless its a
		// border block
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 22; j++) {
				// if first column/first row /last row/ last column color = magenta or else it
				// is black
				if (j == 0 || i == 0 || i == 11 || j == 21) {
					board[i][j] = Color.MAGENTA;
				} else {
					board[i][j] = Color.BLACK;
				}
			}
		}
		// creates a newpiece to start the game 
		newPiece();
	}

	// Put a new, random piece into the dropping position
	public void newPiece() {
		// places the piece in the center of
		
		ogPiece = new Point(4, 2);
		rotation = 0;
		// if all of the pieces have been used
		// resets the list of blocks that could be
		// used and randomizes the combo
		if (pieces.isEmpty()) {
			Collections.addAll(pieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(pieces);
		}
		
		// grabs the first piece in the list and removes it from further consideration

		currentPiece = pieces.get(0);
		pieces.remove(0);
	}

	// Collision test for the dropping piece for the current position of block
	private boolean collision(int x, int y, int rotation) {
		// for the current piece and rotation spit out
		for (Point f : Points[currentPiece][rotation]) {
			// if the upcoming position is filled by block or border returned true
			if (board[f.x + x][f.y + y] != Color.BLACK) {
				return true;
			}
		} // if not its returned as returned as false
		return false;
	}

	// Rotate the pieces
	public void rotate(int i) {
		// 4 different rotations (0,1,2,3), for counterclockwise,
		// if the number goes below zero the rotation is reset to the 4th one
		int rotate = (rotation + i) % 4;
		if (rotate < 0) {
			rotate = 3;
		}
		// if the wanted rotation doesn't collide with anything it is made
		if (collision(ogPiece.x, ogPiece.y, rotate) == false) {
			rotation = rotate;
		}
		// used for testing purposes
		System.out.println(rotation);
		repaint();
	}

	// moves the piece left
	public void left(int i) {
		// if there is no collision with anything for the wanted movement it is done
		if (collision(ogPiece.x - i, ogPiece.y, rotation) == false) {
			ogPiece.x -= i;
			System.out.println(ogPiece.x);
		}
		repaint();
	}

//move the piece right
	public void right(int i) {
		// if there is no collision with anything for the wanted movement it is done
		if (collision(ogPiece.x + i, ogPiece.y, rotation) == false) {
			ogPiece.x += i;
			System.out.println(ogPiece.x);
		}
		repaint();
	}

	// Drops the piece one line or fixes it to the well if it can't drop
	public void bringDown() {
		// if there is no collision with anything for the dropdown it is done
		if (collision(ogPiece.x, ogPiece.y + 1, rotation) == false) {
			ogPiece.y += 1;
			// if there is collision the piece is added to the board
		} else {
			placePiece();
		}
		repaint();
	}

	// Make the dropping piece part of the board, so it is available for
	// collision detection.
	public void placePiece() {
		for (Point f : Points[currentPiece][rotation]) {
			// adds the piece to the board by taking piece coordinates and adding to equal rep board pieces
			board[ogPiece.x + f.x][ogPiece.y + f.y] = PieceColors[currentPiece];
		}
		clearRows();
		newPiece();
	}

//brings the rows down by 1
	public void ridOfRow(int row) {
		// checks every row above the one that is filled
		for (int j = row - 1; j > 0; j--) {
			// for every block in the row
			for (int i = 1; i < 11; i++) {
				// replaces the filled row with the row above
				board[i][j + 1] = board[i][j];
			}
		}
	}

	// clears rows if every block within is not black
	public void clearRows() {
		boolean opening;
		// checks every row
		for (int j = 20; j > 0; j--) {
			// Initialized to false
			opening = false;
			// for every block in row
			for (int i = 1; i < 11; i++) {
				// if any block is black then it is returned as true, row isnt cleared
				if (board[i][j] == Color.BLACK) {

					opening = true;
					break;
				}
			}
			// if no opening it gets rid of the row
			if (opening==false) {
				//calls method above if no blocks in row are black
				ridOfRow(j);
				// repeated for every row above until there is a black block
				j += 1;

			}
		}

	}
//3d array made to store each piece with its different rotations together
	private final Point[][][] Points = {

			// different versions of the I-Piece with the rotations included
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },

			// different versions of the J-Piece with the rotations included
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },

			// different versions of the L-Piece with the rotations included
			{ { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
					{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) } },

			// different versions of the O-Piece with the rotations included
			{ { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) } },

			// different versions of the S-Piece with the rotations included
			{ { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
					{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },

			// different versions of the T-Piece with the rotations included
			{ { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
					{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
					{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },

			// different versions of the Z-Piece with the rotations included
			{ { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
					{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
					{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } } };

	// Draw the falling piece
	private void pieceDrawn(Graphics g) {
		//gives the piece being drawn its color
		g.setColor(PieceColors[currentPiece]);
		//for every rotation in the current piece
		for (Point p : Points[currentPiece][rotation]) {
			//takes the rendered piece and updates it where the piece is going to be. 
			//values match the graphics, which is why its multiplied by 26, and the width and height = 25
			g.fillRect((p.x + ogPiece.x) * 26, (p.y + ogPiece.y) * 26, 25, 25);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		//initalizes the render
		super.paintComponent(g);
		
		//this is the background of the game
		g.fillRect(0, 0, 26*12, 26*23);
//every 
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 22; j++) {
				
				// sets the color based on current piece
				g.setColor(board[i][j]);
				// was thinking of doing g.copyArea to copy rectangles to rectangles but decided
				// instead on painting blocks

				// g.copyArea(26*i, 26*j, 25, 25, 20, 20);

				// creates the graphics for the game, starts at the origin and goes across the whole board
				g.fillRect(26 * i, 26 * j, 25, 25);
			}
		}

		// creates and updates the piece that is currently falling
		pieceDrawn(g);
	}

	public static void main(String[] args) {

		JFrame f = new JFrame("game");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//10x20 format
		f.setSize(315, 605);
		//allows for creation of game, user to see the graphics and the ability to play without the user 
		//adjusting the screen
		f.setVisible(true);
		f.setResizable(false);
		final Characters game = new Characters();
		game.start();

		f.add(game);

		// KeyListener to track keyboard actions, 3 required methods keyTyped,
		// keyPressed,keyReleased to give program idea of what to do when
		// action occurs
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

//when keyboard is pressed
			public void keyPressed(KeyEvent e) {
				// switches action depending on what letter is pressed on keyboard (movement rotation and dropping
				switch (e.getKeyCode()) {
				//calls the rotate method when the up arrow is pressed 
				case KeyEvent.VK_UP:
					game.rotate(1);
					break;
					//calls the rotate method when the up arrow is pressed 
				case KeyEvent.VK_DOWN:
					game.rotate(-1);
					break;
					//tells program to move left when left arrow is pressed
				case KeyEvent.VK_LEFT:
					game.left(1);
					break;
					//tells program to move right when right arrow is pressed
				case KeyEvent.VK_RIGHT:
					game.right(1);
					break;
					//calls bringDown method when Q is pressed
				case KeyEvent.VK_Q:
					game.bringDown();
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		// Make the falling piece drop every second
		new Thread() {
			@Override
			public void run() {
				//while the game is running the function occurs
				while (true) {
					try {
						// every 2000 milliseconds (2 seconds) a InterruptedException e is thrown and the game goes
						// down
						Thread.sleep(2000);
						game.bringDown();
					} catch (InterruptedException e) {
					}
				}
			}
			//begins the thread
		}.start();
	}
}