package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner (System.in);
		ChessMatch chessMatch = new ChessMatch();
		//função para imprimir as peças da partida instanciada acima:
		
		while (true) {
			try {
				UI.clearScreen(); //limpar a tela a cada vez que voltar no while
				//esse método vai receber a matriz de peças da partida
				UI.printBoard(chessMatch.getPieces());
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); //fazer o programa aguardar que o usuário aperte enter
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine(); //fazer o programa aguardar que o usuário aperte enter
			}
		}
	}

}
