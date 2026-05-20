package application.entites;

public class VenteDetail {

    private int venteDetailId;
    private int venteId;
    private int produitId;
    private int quantite;

    public VenteDetail(int venteDetailId, int venteId, int produitid, int quantite) {
        this.venteDetailId=venteDetailId;
        this.venteId= venteId;
        this.produitId= produitid;
        this.quantite= quantite;
    }
    public VenteDetail(int produitId,int quantite) {
    	this(0,0,produitId,quantite);
    }
    public int getIdVenteVenteDetail() {
    	return venteId;
    }
    public int getQuantiteVenteDetail() {
    	return quantite;
    }
    public void setIdVenteVenteDetail(int venteId) {
    	this.venteId = venteId;
    }
    public static void setIdVenteVenteDetail(int idVente, VenteDetail[] vd) {
    	for(int i =0;i<vd.length;i++) {
    		vd[i].setIdVenteVenteDetail(idVente);
    	}
    }
    public int getIdProduitVenteDetail() {
    	return produitId;
    }
    public int getIdVenteDetail() {
    	return venteDetailId;
    }
    public void setIdVenteDetail(int venteDetailId) {
    	this.venteDetailId = venteDetailId;
    }
}
