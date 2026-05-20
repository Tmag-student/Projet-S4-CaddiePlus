package application.entites;

public class HistoriquePrix {
    private int histPrixId;
    private java.sql.Timestamp dateChangement;
    private double prixHisto;
    private int produitId;

    public HistoriquePrix(int histPrixId, java.sql.Timestamp dateChangement,double prixHisto, int produitId) {
        this.histPrixId=histPrixId;
        this.dateChangement=dateChangement;
        this.prixHisto=prixHisto;
        this.produitId=produitId;
    }
    public HistoriquePrix(int produitId,double prixHisto) {
    	this(0,null,prixHisto, produitId);
    }
    public int getIdProduitHistPrix () {
    	return produitId;
    }
    public java.sql.Timestamp getDateChangeHistPrix(){
    	return dateChangement;
    }
    public int getIdHistPrix() {
    	return histPrixId;
    }
    public double getPrixHistoHistPrix() {
    	return prixHisto;
    }
}

