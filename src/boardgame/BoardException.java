package boardgame;

public class BoardException extends RuntimeException{ //para ser uma exceção de tratamento opcional
	private static final long serialVersionUID = 1L; 
	
	public BoardException(String msg) {
		super(msg);
	}
}