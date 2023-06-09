package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	//metodo para saber se há uma peça adversário no board (pois se é adversária, pode se mover para essa posição)
	//esse metodo fica nessa classe pois ela vai ser aproveitada por todas as ChessPieces
	protected boolean isThereOponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p.getColor() != color; //testar se a color da peça é diferente da peça que vai se mover
	}
}
