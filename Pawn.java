
public class Pawn extends Piece {
	
	private int x;
	private int y;	
	private int color;
	private int pieceCode;
	
	private ChessCalculator chess;
	
	public Pawn(int x, int y, int color, ChessCalculator chess) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.chess = chess;
		this.pieceCode = 1;
	}		
	
	public void possibleMoves() {
		int xx = x;
		int yy = y;
		int add = -1;
		int firstRow = 7;
		if(color == 1) {
			add = 1;
			firstRow = 0;
		}
		
		if(yy == firstRow + add) {
			if(chess.getSquare(xx, yy+add*2) == -1 && chess.getSquare(xx, yy+add) == -1) {
				chess.submitMove(this, xx, yy+add*2);
			}
		}
		
		if(chess.getSquare(xx, yy+add) == -1) {
			chess.submitMove(this, xx, yy+add);
		}
		
		if(xx > 0) {
			int s = chess.getSquare(xx-1, yy+add);
			if(s > -1 && s != color) {
				chess.submitMove(this, xx-1, yy+add);
			} 
		}
		
		if(xx < 7) {
			int s = chess.getSquare(xx+1, yy+add);
			if(s > -1 && s != color) {
				chess.submitMove(this, xx+1, yy+add);
			} 
		}
		
		//un passant -move
		int up = chess.getUnPassant(xx, yy);
		if(up != 0) {
			chess.submitMove(this, xx-up, yy+add);
		}
	}
	
	
	public void getControl() {
		int xx = x;
		int yy = y;	
		int add = -1;
		if(color == 1) {
			add = 1;
		}
		
		if(xx > 0) {
			submitControl(xx-1, yy+add);
		}
		
		if(xx < 7) {
			submitControl(xx+1, yy+add);
		}
	}
	
	private void submitControl(int toX, int toY) {
		if(color == 0) {
			chess.setWControl(toX, toY);
		} else {
			chess.setBControl(toX, toY);
		}
	}
	
	public int getPieceCode() {
		return this.pieceCode;
	}
	
	public int getColor() {
		return this.color;
	}
		
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}