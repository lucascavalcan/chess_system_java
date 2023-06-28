package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner (System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>(); //lista de peças capturadas
		//função para imprimir as peças da partida instanciada acima:
		
		while (!chessMatch.getCheckMate()) { //enquanto a partida não estiver com cheque mate
			try {
				UI.clearScreen(); //limpar a tela a cada vez que voltar no while
				//esse método vai receber a matriz de peças da partida
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //na hora de imprimir novamente o board vai criar uma sobrecarga/nova versão na qual vai se passar tambem os PossibleMoves (pois é assim que vai coloris as possíveis posições p mover)
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if (capturedPiece != null) { //significa que alguma peça foi capturada
					captured.add(capturedPiece); //adiciona ela na lista de peças capturadas
				}
				
				if (chessMatch.getPromoted() != null) { //siginifica que uma peça foi promovida (o usuário precisa indicar qual peça vai ser a promoção)
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();
					while (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
						System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
						type = sc.nextLine().toUpperCase();
					}
					chessMatch.replacePromotedPiece(type);
				}
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
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}

}
