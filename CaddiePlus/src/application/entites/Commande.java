package application.entites;

public class Commande {
    private int commandeId;
    private java.sql.Date dateCommande;
    private int fournisseurId;
    private double coutTotal;

    public Commande(int commandeId, java.sql.Date dateCommande, int fournisseurId,double coutTotal) {
        this.commandeId=commandeId;
        this.dateCommande= dateCommande;
        this.fournisseurId= fournisseurId;
        this.coutTotal= coutTotal;
    }
    public Commande(int fournisseurId) {
    	this(0,null,fournisseurId,0);
    }
    // ID FOURNISSEUR
    public int getIdFournisseurCommande() {
    	return fournisseurId;
    }
    public void setIdFournisseurCommande(int fournisseurId) {
    	this.fournisseurId=fournisseurId;
    }
    //DATE COMMANDE
    public java.sql.Date getDateCommande(){
    	return dateCommande;
    }
    //COUT TOT
    public double getCoutTotalCommande() {
    	return coutTotal;
    }
    // ID COMMANDe
    public void setIdCommande(int commandeId) {
    	this.commandeId=commandeId;
    }
    public int getIdCommandeCommande() {
    	return commandeId;
    }

}

