package application.exception;

public class ClientNotFoundException extends Exception {
	private String s;
	public ClientNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucun client n'a été trouvé : "+s;
	}
}
