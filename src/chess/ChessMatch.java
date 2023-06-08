package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn; //rodada atual
	private Color currentPlayer;
	private Board board;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8,8); //tamanho de um tabuleiro
		turn = 1; // a partida começa na jogada 1
		currentPlayer = Color.WHITE;
		initialSetup(); //na hora que inicia a partida, chama-se essa função
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
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
	
	// operação de movimentos possíveis dada uma posição (vai retornar uma matriz de booleano contendo quais os movimentos possíveis para que a aplicação possa colorir o fundo de cada aplicação)
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		//converte a chess positiopn para uma position de matriz normal
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) { //posição de origem e de destino
		//primeiro, converte-se as duas positions do parametro para posições da matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		//validar se na posição de origem (source) realmente existia uma peça
		validateSourcePosition(source);
		//validar a posição de destino
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target); //ja passa os parametros no formato de matriz
		nextTurn(); // troca a jogada
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		//primeiro se retira a peça que está na posição source
		Piece p = board.removePiece(source);
		//remover a possível peça que esteja na posição de destino
		Piece capturedPiece = board.removePiece(target);
		//colcoa a peça que estava na source na posição target
		board.placePiece(p, target);
		
		if (capturedPiece != null) { //significa que capturou uma peça
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
		
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) { //se não houver peça, vai dar uma exceção
			throw new ChessException("There is no Piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { //significa que está tentando mover uma peça do adversário
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible move for chosen position");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	//metodo que passa para a proxima jogada
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//metodo que vai receber as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		//colcoar essa peça dentro da lista de peças no tabuleiro:
		piecesOnTheBoard.add(piece);
	}
	
	//metodo responsavel por iniciar a partida de xadrez
	public void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
