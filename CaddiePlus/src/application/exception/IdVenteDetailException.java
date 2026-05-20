package application.exception;

public class IdVenteDetailException extends Exception{
	private String s;
	public IdVenteDetailException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "le numero de vente detail n'est pas correcte "+s;
	}
}
