package application.entites;

public class CommandeDetail {

    private int id;
    private int commandeId;
    private int produitId;
    private int quantite;

    public CommandeDetail(int id,int commandeId,int produitId,int quantite) {
        this.id=id;
        this.commandeId=commandeId;
        this.produitId=produitId;
        this.quantite =quantite;
    }
    public CommandeDetail(int produitId,int quantite) {
    	this(0,0,produitId,quantite);
    }
    //ID COMMANDE
    public void setIdCommandeCommandeDetail(int commandeId) {
    	this.commandeId=commandeId;
    }
    public static void setIdCommandeCommandeDetail(int idCommande, CommandeDetail[] cd) {
    	for(int i =0;i<cd.length;i++) {
    		cd[i].setIdCommandeCommandeDetail(idCommande);
    	}
    }
    public int getIdCommandeCommandeDetail() {
    	return commandeId;
    }
    // ID PRODUIT
    public int getIdProduitCommandeDetail() {
    	return produitId;
    }
    //ID COMMANDE DETAIL
    public int getIdCommandeDetail() {
    	return id;
    }
    public void setIdCommandeDetail(int id) {
    	this.id=id;
    }
    // QUANTITE
    public int getQuantiteCommandeDetail() {
    	return quantite;
    }
}

