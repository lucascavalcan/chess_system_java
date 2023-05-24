package chess;

import boardgame.Board;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8,8); //tamanho de um tabuleiro
	}
	
	//metodo que retorna uma matriz de peças de xadrez correpondentes a essa chessMatch
	public ChessPiece[][] getPieces(){
		//criar variável do tipo matriz de ChessPiece 
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		//percorrer a matriz de peças do tabuleiro (board) e, para cada peça do tabuleiro, vai fazer um downcasting para ChessPiece
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j); //fazer um downcasting para que interprete que é uma peça de xadrez e não uma piece comum
			}
		}
		return mat;
	}
	
}
