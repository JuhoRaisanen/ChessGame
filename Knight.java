
public class Knight extends Piece {
	
	private int x;
	private int y;	
	private int color;
	private int pieceCode;
	
	private ChessCalculator chess;
	
	public Knight(int x, int y, int color, ChessCalculator chess) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.chess = chess;
		this.pieceCode = 3;
	}
		
	public void possibleMoves() {
		
		int xx = x;
		int yy = y;
		int i = 1;
		int k = 2;
		int index;
		
		for(index = 1; index <= 4; index++) {
			if(xx+i <= 7 && xx+i >= 0 && yy+k <= 7 && yy+k >= 0) {
				
				if(chess.getSquare(xx+i, yy+k) != color) {
					chess.submitMove(this, xx+i, yy+k);
				}
			}
			
			if(xx+k <= 7 && xx+k >= 0 && yy+i <= 7 && yy+i >= 0) {
				
				if(chess.getSquare(xx+k, yy+i) != color) {
					chess.submitMove(this, xx+k, yy+i);
				}
			}
			
			switch(index) {
				case 1:
				i *= -1;
				break;
				
				case 2:
				k *= -1;
				break;
				
				case 3:
				i *= -1;
				break;
			}
		}
	}
	
	public void getControl() {
		int xx = x;
		int yy = y;
		int i = 1;
		int k = 2;
		int index;
		
		for(index = 1; index <= 4; index++) {
			if(xx+i <= 7 && xx+i >= 0 && yy+k <= 7 && yy+k >= 0) {
				submitControl(xx+i, yy+k);
			}
			
			if(xx+k <= 7 && xx+k >= 0 && yy+i <= 7 && yy+i >= 0) {
				submitControl(xx+k, yy+i);
			}
			
			switch(index) {
				case 1:
				i *= -1;
				break;
				
				case 2:
				k *= -1;
				break;
				
				case 3:
				i *= -1;
				break;
			}
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