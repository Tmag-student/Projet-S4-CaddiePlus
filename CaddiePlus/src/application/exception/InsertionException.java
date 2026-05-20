package application.exception;

public class InsertionException extends Exception {
	private String s;
	public InsertionException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return s;
	}
}
