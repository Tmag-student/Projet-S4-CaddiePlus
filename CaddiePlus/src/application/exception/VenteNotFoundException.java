package application.exception;

public class VenteNotFoundException extends Exception {
	private String s;
	public VenteNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucune vente n'a été trouvé : "+s;
	}
	
}
