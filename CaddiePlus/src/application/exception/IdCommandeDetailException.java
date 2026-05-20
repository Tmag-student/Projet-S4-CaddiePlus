package application.exception;

public class IdCommandeDetailException extends Exception{
	private String s;
	public IdCommandeDetailException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "le numero de commande detail n'est pas correcte "+s;
	}
}
