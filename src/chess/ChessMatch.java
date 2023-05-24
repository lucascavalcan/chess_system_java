package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8,8); //tamanho de um tabuleiro
		initialSetup(); //na hora que inicia a partida, chama-se essa função
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
	
	//metodo responsavel por iniciar a partida de xadrez
	public void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}
}
