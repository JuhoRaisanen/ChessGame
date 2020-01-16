
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.*;
import java.util.ArrayList;

public class BoardGraphics extends JPanel {
	
	//Width and height must be divisible by 16
	private int width;
	private int height;
	private int square;
	private int mouseX = 0;
	private int mouseY = 0;
	private int chosenX = 0;
	private int chosenY = 0;
	
	private int drawMode = 0;
	
	private ChessCalculator chess;
	
	public BoardGraphics(int w, int h, ChessCalculator chess) {
		width = w;
		height = h;
		square = width/8;
		this.chess = chess;
	}
	
	@Override
	protected void paintComponent(Graphics g) { 
	
		if(drawMode == 0) {
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
				
			g.setColor(Color.LIGHT_GRAY);
			int i, j;
			int color = 0;
			
			for(i = 0; i < width; i += square) {
				for(j = 0; j < height; j += square) {
					
					g.fillRect(i, j, square, square);
					
					if(color == 1) {
						g.setColor(Color.LIGHT_GRAY);
						color = 0;
					} else {
						g.setColor(Color.DARK_GRAY);
						color = 1;
					}
				}
				
				if(color == 1) {
					g.setColor(Color.LIGHT_GRAY);
					color = 0;
				} else {
					g.setColor(Color.DARK_GRAY);
					color = 1;
				}
			}
					
			ArrayList<Piece> pieces = chess.getPieces();
			if(pieces != null) {
				for(Piece p : pieces) {
					int pc = p.getPieceCode();
					int c = p.getColor();
					int x = p.getX();
					int y = p.getY();
					
					switch(pc) {
						case 1:
						drawPawn(g, x*square, y*square, c);
						break;
						
						case 2:
						drawRook(g, x*square, y*square, c);
						break;
						
						case 3:
						drawKnight(g, x*square, y*square, c);
						break;
						
						case 4:
						drawBishop(g, x*square, y*square, c);
						break;
						
						case 5:
						drawQueen(g, x*square, y*square, c);
						break;
						
						case 6:
						drawKing(g, x*square, y*square, c);
						break;
					}
				}
			}
			
		} else {
			
			//Remove previous rectangle
			if((chosenX/square)%2 == 0) {
				if((chosenY/square)%2 == 0) {
					g.setColor(Color.LIGHT_GRAY);
				} else {
					g.setColor(Color.DARK_GRAY);
				}
			} else {
				if((chosenY/square)%2 == 0) {
					g.setColor(Color.DARK_GRAY);
				} else {
					g.setColor(Color.LIGHT_GRAY);
				}
			}
			g.drawRect((chosenX/square)*square, (chosenY/square)*square, square, square);
			
			//New Rectangle
			g.setColor(Color.ORANGE);	
			g.drawRect((mouseX/square)*square, (mouseY/square)*square, square, square);
			chosenX = mouseX;
			chosenY = mouseY;
			drawMode = 0;
		}
	}

	public void drawChose(int x, int y) {
		
		drawMode = 1;
		mouseX = x;
		mouseY = y;
		
		int a, b;
		a = x/square;
		b = y/square;
		if(a >= 0 && a < 8 && b >= 0 && b < 8) {
			chess.userInput(a, b);
		}
		
		repaint();
	}
	
	public void drawPieces() {
		drawMode = 0;
		repaint();
	}
	
	private void drawPawn(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[5];
		int [] yPoints = new int[5];
		
		xPoints[0] = x + square/4;
		xPoints[1] = x + (square/8)*3;
		xPoints[2] = x + square/2;
		xPoints[3] = x + (square/8)*5;
		xPoints[4] = x + (square/8)*6;
		
		yPoints[0] = y + (square/8)*6;
		yPoints[1] = y + (square/8)*3;
		yPoints[2] = y + square/4;
		yPoints[3] = y + (square/8)*3;
		yPoints[4] = y + (square/8)*6;
		
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 5);
	}
	
	private void drawRook(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[8];
		int [] yPoints = new int[8];
		
		xPoints[0] = x + square/4;
		xPoints[1] = x + square/4;
		xPoints[2] = x + (square/8)*3;
		xPoints[3] = x + (square/8)*3;
		xPoints[4] = x + (square/8)*5;
		xPoints[5] = x + (square/8)*5;
		xPoints[6] = x + (square/8)*6;
		xPoints[7] = x + (square/8)*6;
		
		yPoints[0] = y + (square/8)*6;
		yPoints[1] = y + square/4;
		yPoints[2] = y + square/4;
		yPoints[3] = y + (square/16)*5;
		yPoints[4] = y + (square/16)*5;
		yPoints[5] = y + square/4;
		yPoints[6] = y + square/4;
		yPoints[7] = y + (square/8)*6;
		
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 8);
	}
	
	private void drawKnight(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[10];
		int [] yPoints = new int[10];
		
		xPoints[0] = x + (square/8)*3;
		xPoints[1] = x + (square/16)*7;
		xPoints[2] = x + square/2;
		xPoints[3] = x + square/4;
		xPoints[4] = x + (square/16)*3;
		xPoints[5] = x + square/4;
		xPoints[6] = x + square/2;
		xPoints[7] = x + (square/8)*6;
		xPoints[8] = x + (square/16)*13;
		xPoints[9] = x + (square/8)*6;
		
		yPoints[0] = y + (square/8)*7;
		yPoints[1] = y + square/2;
		yPoints[2] = y + (square/16)*7;
		yPoints[3] = y + square/2;
		yPoints[4] = y + (square/8)*3;
		yPoints[5] = y + square/4;
		yPoints[6] = y + square/8;
		yPoints[7] = y + square/8;
		yPoints[8] = y + square/4;
		yPoints[9] = y + (square/8)*7;
		
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 10);
	}
	
	private void drawBishop(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[5];
		int [] yPoints = new int[5];
		
		xPoints[0] = x + square/4;
		xPoints[1] = x + (square/8)*3;
		xPoints[2] = x + square/2;
		xPoints[3] = x + (square/8)*5;
		xPoints[4] = x + (square/8)*6;
	
		yPoints[0] = y + (square/8)*7;
		yPoints[1] = y + (square/8)*3;
		yPoints[2] = y + square/8;
		yPoints[3] = y + (square/8)*3;
		yPoints[4] = y + (square/8)*7;
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 5);
	}
	
	private void drawQueen(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[9];
		int [] yPoints = new int[9];
		
		xPoints[0] = x + square/4;
		xPoints[1] = x + square/8;
		xPoints[2] = x + square/4;
		xPoints[3] = x + (square/8)*3;
		xPoints[4] = x + square/2;
		xPoints[5] = x + (square/8)*5;
		xPoints[6] = x + (square/8)*6;
		xPoints[7] = x + (square/8)*7;
		xPoints[8] = x + (square/8)*6;
		
		yPoints[0] = y + (square/8)*6;
		yPoints[1] = y + square/4;
		yPoints[2] = y + (square/8)*3;
		yPoints[3] = y + square/4;
		yPoints[4] = y + (square/8)*3;
		yPoints[5] = y + square/4;
		yPoints[6] = y + (square/8)*3;
		yPoints[7] = y + square/4;
		yPoints[8] = y + (square/8)*6;
		
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 9);
	}
	
	private void drawKing(Graphics g, int x, int y, int c) {
		
		int [] xPoints = new int[18];
		int [] yPoints = new int[18];
		
		xPoints[0] = x + square/4;
		xPoints[1] = x + square/4;
		xPoints[2] = x + (square/8)*3;
		xPoints[3] = x + (square/16)*7;
		xPoints[4] = x + (square/16)*7;
		xPoints[5] = x + (square/8)*3;
		xPoints[6] = x + (square/8)*3;
		xPoints[7] = x + (square/16)*7;
		xPoints[8] = x + (square/16)*7;
		xPoints[9] = x + (square/16)*9;
		xPoints[10] = x + (square/16)*9;
		xPoints[11] = x + (square/8)*5;
		xPoints[12] = x + (square/8)*5;
		xPoints[13] = x + (square/16)*9;
		xPoints[14] = x + (square/16)*9;
		xPoints[15] = x + (square/8)*5;
		xPoints[16] = x + (square/8)*6;
		xPoints[17] = x + (square/8)*6;
		
		yPoints[0] = y + (square/8)*6;
		yPoints[1] = y + square/2;
		yPoints[2] = y + (square/8)*3;
		yPoints[3] = y + (square/8)*3;
		yPoints[4] = y + square/4;
		yPoints[5] = y + square/4;
		yPoints[6] = y + (square/16)*3;
		yPoints[7] = y + (square/16)*3;
		yPoints[8] = y + square/8;
		yPoints[9] = y + square/8;
		yPoints[10] = y + (square/16)*3;
		yPoints[11] = y + (square/16)*3;
		yPoints[12] = y + square/4;
		yPoints[13] = y + square/4;
		yPoints[14] = y + (square/8)*3;
		yPoints[15] = y + (square/8)*3;
		yPoints[16] = y + square/2;
		yPoints[17] = y + (square/8)*6;
		
		
		if(c == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillPolygon(xPoints, yPoints, 18);
	}
}
