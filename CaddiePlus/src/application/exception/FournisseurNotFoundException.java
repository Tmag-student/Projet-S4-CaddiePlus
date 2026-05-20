package application.exception;

public class FournisseurNotFoundException extends Exception {
	private String s;
	public FournisseurNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucun Fournisseur n'a été trouvé : "+s;
	}
	
}
