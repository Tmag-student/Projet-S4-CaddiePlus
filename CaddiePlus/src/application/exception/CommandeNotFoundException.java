package application.exception;

public class CommandeNotFoundException extends Exception {
	private String s;
	public CommandeNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucune commande n'a été trouvé : "+s;
	}
	
}
