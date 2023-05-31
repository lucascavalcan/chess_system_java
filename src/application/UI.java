package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	// códigos especiais das cores para se imprimir no console
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	//cores do text
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	//cores do background
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	//metodo qu evai limpara tela
	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void clearScreen() {
		 System.out.print("\033[H\033[2J");
		 System.out.flush();
	} 
	
	//metodo que vai ler a posição de um usuário
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
		String s = sc.nextLine();
			char column = s.charAt(0); // pois a coluna da chess position é o primeiro caractere do string (ex: position a1)
			int row = Integer.parseInt(s.substring(1)); //recortar o string a partir da posição 1 e converter o resultado para Integer
			return new ChessPosition(column, row);
		}
		catch (RuntimeException e) { //qualquer exceção que ocorrer
			throw new InputMismatchException("Valid values are from a1 to h8"); //erro de entrada de dados
		}
		
	}
	
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
	private static void printPiece(ChessPiece piece) {
    	if (piece == null) { //se essa peça for = nulo, significa que não tinha peça nessa posição do tabuleiro
            System.out.print("-");
        }
        else { //caso contrário, vai imprimir a peça que estava naquela posição
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET); //o ANSI_RESET serve para resetar a cor
            }
            else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" "); //imprimir um espaço em branco para que as peças não fiquem grudadas
	}
}
