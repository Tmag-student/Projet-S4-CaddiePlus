package application.entites;

public class Fournisseur {
    private int fournisseurId;
    private String nom;

    public Fournisseur(int fournisseurId,String nom) {
        this.fournisseurId= fournisseurId;
        this.nom = nom;
    }
    public Fournisseur(String nom) {
    	this(0,nom);
    }
    public Fournisseur() {
    	this(0,"Non renseigné");
    }
    public int getIdFournisseur() {
    	return fournisseurId;
    }
    public String getNomFournisseur() {
    	return nom;
    }
    public void seIdFournisseur(int fournisseurId) {
    	this.fournisseurId=fournisseurId;
    }

}

