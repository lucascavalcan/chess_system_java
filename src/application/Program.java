package application;

import chess.ChessMatch;

public class Program {

	public static void main(String[] args) {
		
		ChessMatch chessMatch = new ChessMatch();
		//função para imprimir as peças da partida instanciada acima:
		
		//esse método vai receber a matriz de peças da partida
		UI.printBoard(chessMatch.getPieces());

	}

}
