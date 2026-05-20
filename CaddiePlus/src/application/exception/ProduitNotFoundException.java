package application.exception;

public class ProduitNotFoundException extends Exception {
	private String s;
	public ProduitNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucun produit n'a été trouvé : "+s;
	}
}
