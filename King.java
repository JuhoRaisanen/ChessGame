
public class King extends Piece {
	
	private int x;
	private int y;	
	private int color;
	private int pieceCode;
	
	private boolean castleShort;
	private boolean castleLong;
	
	private ChessCalculator chess;
	
	public King(int x, int y, int color, ChessCalculator chess) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.chess = chess;
		this.pieceCode = 6;
		
		castleShort = true;
		castleLong = true;
	}
		
	public void possibleMoves() {
		
		int xx = x;
		int yy = y;
		int i, k;
		for(i=-1; i<2; i++) {
			for(k=-1; k<2; k++) {
				if(xx+i <= 7 && xx+i >= 0 && yy+k <= 7 && yy+k >= 0) {
					if(i != 0 || k != 0) {
						
						if(color == 0) {
							if(chess.getSquare(xx+i, yy+k) != color) {
								chess.submitMove(this, xx+i, yy+k);
							}
						} else {
							if(chess.getSquare(xx+i, yy+k) != color) {
								chess.submitMove(this, xx+i, yy+k);
							}
						}
					}
				}
			}
		}
		
		if(color == 0) {
			chess.refreshBlackControl(null);
		} else {
			chess.refreshWhiteControl(null);
		}
		//Castling
		if(color == 0) {
			if(castleShort) {
				if(chess.getSquare(5, 7) == -1 && chess.getSquare(6, 7) == -1) {
					if(chess.getBControl(4, 7) == 0 && chess.getBControl(5, 7) == 0 && chess.getBControl(6, 7) == 0) {
						chess.submitCastleMove(xx, yy, 6, 7);
					}
				}
			}
			if(castleLong) {
				if(chess.getSquare(3, 7) == -1 && chess.getSquare(2, 7) == -1 && chess.getSquare(1, 7) == -1) {
					if(chess.getBControl(4, 7) == 0 && chess.getBControl(3, 7) == 0 && chess.getBControl(2, 7) == 0) {
						chess.submitCastleMove(xx, yy, 2, 7);
					}
				}
			}
		} else {
			if(castleShort) {
				if(chess.getSquare(5, 0) == -1 && chess.getSquare(6, 0) == -1) {
					if(chess.getWControl(4, 0) == 0 && chess.getWControl(5, 0) == 0 && chess.getWControl(6, 0) == 0) {
						chess.submitCastleMove(xx, yy, 6, 0);
					}
				}
			}
			if(castleLong) {
				if(chess.getSquare(3, 0) == -1 && chess.getSquare(2, 0) == -1 && chess.getSquare(1, 0) == -1) {
					if(chess.getWControl(4, 0) == 0 && chess.getWControl(3, 0) == 0 && chess.getWControl(2, 0) == 0) {
						chess.submitCastleMove(xx, yy, 2, 0);
					}
				}
			}
		}
	}
	
	public void getControl() {
		int xx = x;
		int yy = y;
		int i, k;
		for(i=-1; i<2; i++) {
			for(k=-1; k<2; k++) {
				if(xx+i <= 7 && xx+i >= 0 && yy+k <= 7 && yy+k >= 0) {
					if(i != 0 || k != 0) {
						submitControl(xx+i, yy+k);
					}
				}
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
	
	public void setCastleShort(boolean b) {
		castleShort = b;
	}
	
	public void setCastleLong(boolean b) {
		castleLong = b;
	}
}