package application.entites;

public class Produit {
    private int produitId;
    private String nom;
    private int stock;
    private double prixVenteCourant;
    private int fournisseurId;

    public Produit(int produitId, String nom,int stock,double prixVenteCourant, int fournisseurId) {
        this.produitId=produitId;
        this.nom= nom;
        this.stock=stock;
        this.prixVenteCourant=prixVenteCourant;
        this.fournisseurId= fournisseurId;
    }
    public Produit(String nom, int stock, double prixVenteCourant,int fournisseurId) {
    	this(0,nom,stock,prixVenteCourant,fournisseurId);
    }
    public Produit(String nom,int stock,double prixVenteCourant) {
        this(nom, stock, prixVenteCourant, 0);
    }
    public Produit(String nom, double prix) {
    	this(nom,0,prix);
    }
    public String getNomProduit() {
    	return nom;
    }
    public int getStockProduit() {
    	return stock;
    }
    public double getPrixCourantProduit() {
    	return prixVenteCourant;
    }
    public int getIdFournProduit() {
    	return fournisseurId;
    }
    public int getIdProduit() {
    	return produitId;
    }
    public void setIdProduit(int produitId) {
    	this.produitId=produitId;
    }
    public void setIdFourn(int fournisseurId) {
    	this.fournisseurId=fournisseurId;
    }
   
    

}

