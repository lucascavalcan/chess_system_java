package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn; //rodada atual
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
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
	
	public boolean getCheck() {
		return check;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
		
		//testando se o jogador não se auto colocou em check
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		//se a jpgada feita deixou em cheque mate, o jogo precisa acabar
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn(); // caso contrário (não teve cheque mate) vai apenas trocar a jogada
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		//primeiro se retira a peça que está na posição source
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
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
	
	//metodo para poder desfazer o movimento casa o jogador mover a peça e se colocar em cheque
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
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
	
	private Color opponent(Color color) { //metodo que retorna a cor do oponente
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//metodo para localizar o rei de uma determinada cor (para realizar a logica de check)
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) { //significa que encontrou o rei
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	//testar se o rei de determinada cor está em cheque (percorrer as peças adversárias para ver se há um movimento possível que caia na casa do rei)
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //significa que o rei está em cheque
				return true;
			}
		}
		return false;
	}
	
	//testar se o rei está em cheque mate
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) { //se não está em cheque, é pq também não está em cheque mate
			return false;
		}
		//vai verificar se não existe nenhum movimento das peças daquela cor que tire do cheque
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) { //vai mover essa Piece p para essa posição da matriz e verificar se esse movimento fez o rei sair do cheque
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) { //se não estava em cheque, significa que esse movimento tirou o rei do cheque
							return false; //não era cheque mate
						}
					}
				}
			}
		}
		return true; // se esgotou o for e não encontrou nenhum movimento possível
	}	

	
	//metodo que vai receber as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		//colcoar essa peça dentro da lista de peças no tabuleiro:
		piecesOnTheBoard.add(piece);
	}
	
	//metodo responsavel por iniciar a partida de xadrez
	public void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}
}
