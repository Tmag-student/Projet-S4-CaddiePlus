package application.exception;

public class HistoriquePrixNotFoundException extends Exception {
	private String s;
	public HistoriquePrixNotFoundException (String s) {
		super(s);
		this.s = s;
	}
	public String toString() {
		return "Aucun historiquePrix n'a été trouvé : "+s;
	}
	
}