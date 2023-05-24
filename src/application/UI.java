package application;

import chess.ChessPiece;

public class UI {

	//metodo que vai imprimir o tabuleiro
	public static void printBoard(ChessPiece[][] pieces) {
		for (int i=0; i<pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j=0; j<pieces.length; j++) {
				printPiece(pieces[i][j]);
			}
			System.out.println(); //quebra de linha para ir para a proxima linha
		}
		System.out.println("  a b c d e f g h");
	}
	
	//metodo que vai imprimir uma unica peça:
	public static void printPiece(ChessPiece piece) {
		if (piece == null) { //se essa peça for = nulo, significa que não tinha peça nessa posição do tabuleiro
			System.out.print("-");
		} 
		else { //caso contrário, vai imprimir a peça que estava naquela posição
			System.out.print(piece);
		}	
		System.out.print(" "); //imprimir um espaço em branco para que as peças não fiquem grudadas
	}
}
