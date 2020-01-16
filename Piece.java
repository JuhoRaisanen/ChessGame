
public abstract class Piece {
	
	/*
	x: 0=left, 7=right
	y: 0=top, 7=down
	color: 0=white, 1=black
	
	getPieceCode: 1=Pawn, 2=Rook, 3=Knight, 4=Bishop, 5=Queen, 6=King
	*/
	private int x;
	private int y;	
	private int color;
	private int pieceCode;
	
	private ChessCalculator chess;
		
	public void possibleMoves() {};	
	
	public void getControl() {};
	
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