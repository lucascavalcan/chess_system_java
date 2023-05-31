package boardgame;

public abstract class Piece {

	//a position do tipo Position é protected, pois esse tipo de Position não é ainda a Position do xadrez (é uma posição simples de matriz)
	//não é para essa Position ser visível na camada de xadrez, logo, ela vai ser colocada como protected:
	protected Position position;
	//é preciso ter uma associação da Piece com o Board onde ela está:
	private Board board;
	
	//no construtor, vai ser passada apenas o Board na hora de criar a Piece (pois a Position de uma Piece recem criada é null)
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	//apenas o get pois não vai permitir que o Board seja alterado
	protected Board getBoard() {  //somente acessivel para a camada d etabuleiro
		return board;
	}
	
	//operação abstrata que verifica os possíveis movimentos de uma peça (retorna uma matriz booleana com as casas que ela pode ir)
	public abstract boolean[][] possibleMoves();
	
	//metodo que recebe uma posição e retorna se é possível ou não uma peça se mover para essa posição
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()]; //método concreto que utiliza um método abstrato
	}
	
	//metodo que retorna se há pelo menos um movimento possível para aquela peça
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
