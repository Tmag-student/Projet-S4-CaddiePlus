package application.exception;

public class DetailNotInVenteException extends Exception {
	private int idDetailVente;
	private int idVente;
	public DetailNotInVenteException (int idDetailVente, int idVente) {
		super("");
		this.idDetailVente= idDetailVente;
		this.idVente=idVente;
	}
	public String toString() {
		return "Le detail numero "+idDetailVente+" n'est pas relié à la vente numero "+idVente;
	}
	
}