package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces; //matriz de peças
	
	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 column and 1 row");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	//retornar uma Piece após ser indicada uma row e uma column
	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	//metodo que vai colocar uma peça em uma posição do tabuleiro
	public void placePiece(Piece piece, Position position) {
		//verificando se já existe alguma peça nessa posição
		if (thereIsAPiece(position)){
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		//é preciso declarar que essa piece não está mais na posição nula (e sim na position passada no parametro do método):
		piece.position = position;
	}
	
	//metodo que vai remove ruma peça de detrminada posição do tabuleiro
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		//priemiro, testa se a peça do Board nessa positio é null (se for null é porque não há nenhuma peça nessa posição)
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null; //a position de aux passa a ser null (ela foi retirada do Board)
		pieces[position.getRow()][position.getColumn()] = null; //na matriz de peças, a position que foi passada de parametro desse metodo, passa a ser null
		return aux;
	}
	
	//pega uma posição e retorna um booleano para dizer se essa posição existe ou não
	//PRIMEIRO, VAMOS FAZER UMA FUNÇÃO AUXILIAR:
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//pega uma posição e retorna um booleano para dizer se nessa posição tem uma peça
	public boolean thereIsAPiece(Position position) {
		//vendo se essa posição existe
		if (!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
