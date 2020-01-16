import java.util.ArrayList;
import java.util.Random;

public class ChessCalculator {
	
	private ArrayList<Piece> pieces;
	private int[][] bControl = new int[8][8];
	private int[][] wControl = new int[8][8];
	private int[] moves = new int[200];
	private int moveIndex = 0;
	private int [][] squares = new int[8][8];
	private int userChose;
	private BoardGraphics board;
	private Window window;
	
	private int[] unPassantSquares = new int[3];
	
	private boolean whiteTurn = true;
	private King whiteKing;
	private King blackKing;
	
	private boolean promote = false;
	private int promoteX;
	private int promoteY;
	
	private boolean computerWhite = false;
	private boolean computerBlack = true;
	private boolean playerTurn;
	private Random rand;
	
	
	public ChessCalculator() {
		rand = new Random();
		reset();
	}
	
	public void reset() {
		int i, j;
		for(i=0; i<8; i++) {
			for(j=0; j<8; j++) {
				bControl[i][j] = 0;
				wControl[i][j] = 0;	
				squares[i][j] = -1;
			}
		}
		
		unPassantSquares[0] = -1; //x0
		unPassantSquares[1] = -1; //x1
		unPassantSquares[2] = -1; //y
		
		userChose = 0;
		whiteTurn = true;
		
		pieces = new ArrayList<>(32);
		
		for(i=0; i<8; i++) {
			
			pieces.add(new Pawn(i, 1, 1, this));
			pieces.add(new Pawn(i, 6, 0, this));
			squares[i][0] = 1;
			squares[i][1] = 1;
			squares[i][6] = 0;
			squares[i][7] = 0;
			
		}
		
		pieces.add(new Rook(0, 0, 1, this));
		pieces.add(new Rook(7, 0, 1, this));
		pieces.add(new Rook(0, 7, 0, this));
		pieces.add(new Rook(7, 7, 0, this));
		
		pieces.add(new Knight(1, 0, 1, this));
		pieces.add(new Knight(6, 0, 1, this));
		pieces.add(new Knight(1, 7, 0, this));
		pieces.add(new Knight(6, 7, 0, this));
		
		pieces.add(new Bishop(2, 0, 1, this));
		pieces.add(new Bishop(5, 0, 1, this));
		pieces.add(new Bishop(2, 7, 0, this));
		pieces.add(new Bishop(5, 7, 0, this));
		
		pieces.add(new Queen(3, 0, 1, this));
		pieces.add(new Queen(3, 7, 0, this));
		
		blackKing = new King(4, 0, 1, this);
		whiteKing = new King(4, 7, 0, this);
		pieces.add(blackKing);
		pieces.add(whiteKing);
		
		calculateMoves();			
	}
	
	public void setBoard(BoardGraphics board) {
		this.board = board;
	}
	
	public void piecePromote(int pieceCode) {
		int col = 1;
		if(whiteTurn) {
			col = 0;
		}
		
		switch(pieceCode) {
			case 2:
			pieces.add(new Rook(promoteX, promoteY, col, this));
			break;
			case 3:
			pieces.add(new Knight(promoteX, promoteY, col, this));
			break;
			case 4:
			pieces.add(new Bishop(promoteX, promoteY, col, this));
			break;
			case 5:
			pieces.add(new Queen(promoteX, promoteY, col, this));
			break;
		}
		
		promote = false;
		
		if(whiteTurn) {
			whiteTurn = false;
		} else {
			whiteTurn = true;
		}
		
		board.drawPieces();
		calculateMoves();			
	}
	
	public void getWindow(Window window) {
		this.window = window;
	}
	
	public void calculateMoves() {
		moveIndex = 0;
		int i, j;
		
		for(i=0; i<200; i++) {
			moves[i] = 0;
		}
		
		int color;
		playerTurn = true;
		if(whiteTurn) {
			color = 0;
			if(computerWhite) {
				playerTurn = false;
			}
		} else {
			color = 1;
			if(computerBlack) {
				playerTurn = false;
			}
		}
		
		//Calculate possible moves
		for(Piece p : pieces) {
			if(p.getColor() == color) {
				p.possibleMoves();
			}
		}

		//If checkmate, game ends
		if(moveIndex == 0) {
			//no moves
			
			int w = 0;
			if(whiteTurn) {
				w = 1;
			}
			window.showGameEnd(w);
			return;
		}
		
		//Computer move
		if(!playerTurn) {
			if(whiteTurn) {
				doComputerMove(wControl, bControl);
			} else {
				doComputerMove(bControl, wControl);
			}			
		}
	}
	
	
	private void doComputerMove(int [][] ownControl, int [][] enemyControl) {
		
		int i;
		int move;
		int x, y, toX, toY;
		double [] evaluation = new double[moveIndex];
		int pieceCode;
		int enemyPieceCode;
		
		Piece p, p2;
		
		for(i=0; i<moveIndex; i++) {
			refreshBlackControl(null);
			refreshWhiteControl(null);
			
			evaluation[i] = 0;
			move = moves[i];
			
			x = move/1000;
			y = (move - x*1000)/100;
			toX = (move - x*1000 - y*100)/10;
			toY = move%10;	
			p = getPiece(x, y);
			pieceCode = 0;
			if(p != null) {
				pieceCode = p.getPieceCode();
			}

			//Check if piece is in danger
			if(enemyControl[x][y] > 0) {
				switch(pieceCode) {
					case 5:
					evaluation[i] += 5;
					break;
					
					case 4:
					if(enemyControl[x][y] > ownControl[x][y]) {
						evaluation[i] += 1.5;
					}
					break;
					
					case 3:
					if(enemyControl[x][y] > ownControl[x][y]) {
						evaluation[i] += 1.5;
					}
					break;
					
					case 2:
					evaluation[i] += 3;
					break;
				}
				
			}
			
			
			//Test move
			p2 = getPiece(toX, toY);
			enemyPieceCode = 0;
			p.setX(toX);
			p.setY(toY);
			squares[x][y] = -1;
			squares[toX][toY] = p.getColor();
			
			if(pieceCode != 1 || (toY > 0 && toY < 7)) { //don't test pawn promote
				refreshBlackControl(p2);
				refreshWhiteControl(p2);
			} else {
				//Pawn promote is good move
				evaluation[i] += 9;
			}

			
			if(p2 != null) {
				enemyPieceCode = p2.getPieceCode();
				
				switch (enemyPieceCode) {
					case 5:
					evaluation[i] += 9;
					break;
					
					case 4:
					evaluation[i] += 3;
					break;
					
					case 3:
					evaluation[i] += 3;
					break;
					
					case 2:
					evaluation[i] += 5;
					break;
					
					case 1:
					evaluation[i] += 1;
					break;
				}
			}
			
			if(enemyControl[toX][toY] > ownControl[toX][toY]) {
				switch (pieceCode) {
					case 5:
					evaluation[i] -= 8;
					break;
					
					case 4:
					evaluation[i] -= 2;
					break;
					
					case 3:
					evaluation[i] -= 2;
					break;
					
					case 2:
					evaluation[i] -= 4;
					break;				
				}
			}
			
			//Some good principles
			if(pieceCode == 3 || pieceCode == 4) {
				//if knight or bishop undeveloped
				if(y == 0 || y == 7) {
					evaluation[i] += 1;
				}
			}
			
			//Don't lose your queen
			if(pieceCode == 5 && enemyPieceCode != 5) {
				if(enemyControl[toX][toY] > ownControl[toX][toY]) {
					evaluation[i] -= 5;
				}
			}
			
			//Castling is good
			if(pieceCode == 6) {
				if(Math.abs(x - toX) == 2) {
					evaluation[i] += 2;
				}
			}
			
			if(ownControl[toX][toY] >= enemyControl[toX][toY]) {
				//Checking is good
				if(whiteTurn) {
					if(ownControl[blackKing.getX()][blackKing.getY()] > 0) {
						evaluation[i] += 2;
					}
				} else {
					if(ownControl[whiteKing.getX()][whiteKing.getY()] > 0) {
						evaluation[i] += 2;
					}					
				}
				
				if(pieceCode == 1) {
					//moving pawns forward is sometimes good
					evaluation[i] += 0.5;
					if((x == 3 || x == 4) && (y == 1 || y == 6)) {
						evaluation[i] += 0.5;
					}
				}
			}
			
			
			//set pieces again
			p.setX(x);
			p.setY(y);
			squares[x][y] = p.getColor();
			
			if(p2 != null) {
				squares[toX][toY] = p2.getColor();
			} else {
				squares[toX][toY] = -1;
			}			
			
		}
		
		
		
		//Choose best evaluation
		double maxEvaluation = evaluation[0];
		int amount = 1;
		
		for(i = 0; i < moveIndex; i++) {
			if(evaluation[i] > maxEvaluation) {
				maxEvaluation = evaluation[i];
				amount = 1;
			} else if(evaluation[i] == maxEvaluation) {
				amount++;
			}
		}
		
		int [] bestMoves = new int[amount];
		amount = 0;
		for(i = 0; i < moveIndex; i++) {
			
			if(evaluation[i] == maxEvaluation) {
				bestMoves[amount] = moves[i];
				amount++;
			}
		}
		
		int a = rand.nextInt(amount);
		move = bestMoves[a];		
		
		x = move/1000;
		y = (move - x*1000)/100;
		toX = (move - x*1000 - y*100)/10;
		toY = move%10;	
		doMove(x, y, toX, toY);		
	}
	
	public void submitMove(Piece p, int toX, int toY) {
		
		int xx = p.getX();
		int yy = p.getY();
		int col = p.getColor();
		boolean validMove = false;
		
		//Piece in target location
		Piece p2 = getPiece(toX, toY);

		//Try if the move is valid (king is not in check)
		p.setX(toX);
		p.setY(toY);
		squares[xx][yy] = -1;
		squares[toX][toY] = col;
		
		if(col == 0) {
			refreshBlackControl(p2);
			if(bControl[whiteKing.getX()][whiteKing.getY()] == 0) {
				validMove = true;
			}
		} else {
			refreshWhiteControl(p2);
			if(wControl[blackKing.getX()][blackKing.getY()] == 0) {
				validMove = true;
			}
		}
		
		p.setX(xx);
		p.setY(yy);
		squares[xx][yy] = col;
		if(p2 != null) {
			squares[toX][toY] = p2.getColor();
		} else {
			squares[toX][toY] = -1;
		}		
		
		if(validMove) {
			moves[moveIndex] = xx*1000 + yy*100 + toX*10 + toY*1;
			moveIndex++;
		}
	}
	
	public void submitCastleMove(int xx, int yy, int toX, int toY) {
			moves[moveIndex] = xx*1000 + yy*100 + toX*10 + toY*1;
			moveIndex++;		
	}
	
	public void refreshBlackControl(Piece excludeThis) {
		int i, j;
		
		for(i=0; i<8; i++) {
			for(j=0; j<8; j++) {
				bControl[i][j] = 0;
			}
		}
		for(Piece p : pieces) {
			if(p.getColor() == 1 && p != excludeThis) {
				p.getControl();
			}
		}		
	}
	
	public void refreshWhiteControl(Piece excludeThis) {
		int i, j;
		
		for(i=0; i<8; i++) {
			for(j=0; j<8; j++) {
				wControl[i][j] = 0;
			}
		}
		for(Piece p : pieces) {
			if(p.getColor() == 0 && p != excludeThis) {
				p.getControl();
			}
		}		
	}
	
	public int getSquare(int x, int y) {
		return squares[x][y];
	}
	
	public Piece getPiece(int x, int y) {
		for(Piece p : pieces) {
			if(p.getX() == x && p.getY() == y) {
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Piece> getPieces () {
		return pieces;
	}
	
	public int getWControl(int x, int y) {
		return wControl[x][y];
	}
	
	public int getBControl(int x, int y) {
		return bControl[x][y];
	}
	
	public void setBControl(int x, int y) {
		bControl[x][y] += 1;
	}
	
	public void setWControl(int x, int y) {
		wControl[x][y] += 1;
	}
	
	public int getUnPassant(int x, int y) {
		if(unPassantSquares[2] == y) {
			if(unPassantSquares[0] == x) {
				return -1;
			}
			
			if(unPassantSquares[1] == x) {
				return 1;
			}
		}
		
		return 0;
	}
	
	private void doMove(int x, int y, int toX, int toY) {

		Piece p, p2;
		
		//Find the piece that is moving
		p = getPiece(x, y);
		if(p != null) {
			
			//Find if there is piece in the target square and remove it
			p2 = getPiece(toX, toY);
			if(p2 != null) {
				pieces.remove(p2);
			}
			
			//Move the piece
			p.setX(toX);
			p.setY(toY);
			squares[x][y] = -1;
			squares[toX][toY] = p.getColor();
			
			//Special rules
				//unPassant
			unPassantSquares[0] = -1; //x0
			unPassantSquares[1] = -1; //x1
			unPassantSquares[2] = -1; //y
			if(p.getPieceCode() == 1) {
				if(Math.abs(y - toY) == 2) {
					unPassantSquares[0] = toX-1; //x0
					unPassantSquares[1] = toX+1; //x1
					unPassantSquares[2] = toY; //y
				}
				
				if(Math.abs(x - toX) == 1) {
					if(p2 == null) {
						//Remove unPassant enemy soldier
						int col = p.getColor();
						int add = -1;
						if(col == 0) {
							add = 1;
						}
						p2 = getPiece(toX, toY+add);
						if(p2 != null) {
							pieces.remove(p2);
						}
					}
				}
			}
				//Castling rights
			if(p.getPieceCode() == 2) {
				if(x == 0) {
					if(y == 0) {
						blackKing.setCastleLong(false);
					} else if (y == 7) {
						whiteKing.setCastleLong(false);
					}
				} else if (x == 7) {
					if(y == 0) {
						blackKing.setCastleShort(false);
					} else if (y == 7) {
						whiteKing.setCastleShort(false);
					}
				}
			}
				//Castling
			if(p.getPieceCode() == 6) {
				//Short castle
				if(Math.abs(x - toX) == 2 && x < toX) {
					Piece p3 = getPiece(7, toY);
					if(p3 != null) {
						if(p3.getPieceCode() == 2) {
							p3.setX(5);
							squares[7][toY] = -1;
							squares[5][toY] = p3.getColor();									
						}
					}
				}
				
				//Long castle
				if(Math.abs(x - toX) == 2  && x > toX) {
					Piece p3 = getPiece(0, toY);
					if(p3 != null) {
						if(p3.getPieceCode() == 2) {
							p3.setX(3);
							squares[0][toY] = -1;
							squares[3][toY] = p3.getColor();									
						}
					}							
				}
				
				if(whiteTurn) {
					whiteKing.setCastleLong(false);
					whiteKing.setCastleShort(false);					
				} else {
					blackKing.setCastleLong(false);
					blackKing.setCastleShort(false);
				}
			}
			
				//Pawn promote
			if(p.getPieceCode() == 1) {
				if(toY == 7 || toY == 0) {
					if(playerTurn) {
						window.createPromotionWindow();
						board.drawPieces();
						promote = true;
						promoteX = toX;
						promoteY = toY;
						
						pieces.remove(p);						
					} else {
						pieces.add(new Queen(toX, toY, p.getColor(), this));
						pieces.remove(p);
					}
				}
			}
			
			
			if(promote == false) {

				if(whiteTurn) {
					whiteTurn = false;
				} else {
					whiteTurn = true;
				}
				
				board.drawPieces();
				calculateMoves();							
			}
		}
	}
	
	public void userInput(int x, int y) {
		if(promote == false && playerTurn == true) {
			int newMove = userChose*100 + x*10 + y;
			int i;
			
			for(i = 0; i < moveIndex; i++) {
				if(newMove == moves[i]) {
					//doMove
					int pieceX = userChose/10;
					int pieceY = userChose%10;
					doMove(pieceX, pieceY, x, y);	
					
					//break
					i = moveIndex + 1;
				}
			}
			
			userChose = x*10 + y;
		}
	}
	
	
}