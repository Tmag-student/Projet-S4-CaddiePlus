package application.entites;
import java.sql.Timestamp;
public class Vente {
    private int venteId;
    private java.sql.Timestamp dateVente;
    private int clientId;
    private double coutTotal;

    public Vente(int venteId, java.sql.Timestamp dateVente,int clientId, double coutTotal) {
        this.venteId= venteId;
        this.dateVente = dateVente;
        this.clientId= clientId;
        this.coutTotal= coutTotal;
    }
    public Vente(int clientId) {
    	this(0,null,clientId,0);
    }
    public int getIdVenteVente() {
    	return venteId;
    }
    public Timestamp getDateVente() {
    	return dateVente;
    }
    public int getIdClientVente() {
    	return clientId;
    }
    public void setIdClientVente(int clientId) {
    	this.clientId=clientId;
    }
    public double getCoutTotalVente() {
    	return coutTotal;
    }
    public void setIdVente(int venteId) {
    	this.venteId = venteId;
    }

}
