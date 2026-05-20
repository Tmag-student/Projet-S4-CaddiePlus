package application.exception;

public class StockInvalidException extends Exception {
	private String s;
	private int quantite;
	private int stock;
	public StockInvalidException (String s) {
		super(s);
		this.s = s;
	}
	public StockInvalidException(int quantite,int stock,String s) {
		super(s);
		this.s=s;
		this.quantite =quantite;
		this.stock=stock;
	}
	public String toString() {
		return "Le Stock n'est pas suffisant : quantite demandée : "+quantite+" quantite en stock : "+stock+" "+s;
	}
}
