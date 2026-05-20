package application.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Connexion;
import application.entites.Produit;
import application.exception.ProduitNotFoundException;
import application.dao.ProduitDAO;
import application.entites.Fournisseur;
import application.exception.FournisseurNotFoundException;

import application.entites.HistoriquePrix;
import application.exception.*;

public class ProduitDAO {
	public static void afficherTableProduit()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM produits");

		System.out.println("(produit_id, nom, stock, Prix de vente actuel, fournisseur_id)");
		while(rs.next()==true) {
			System.out.println(rs.getInt("produit_id")+", "
					+rs.getString("nom")+", "
					+rs.getInt("stock")+", "
					+rs.getDouble("prix_vente_courant")+", "
					+rs.getInt("fournisseur_id"));
		}
		System.out.println("Fin Table");
		stmt.close();
		conn.close();
		
	}
	public static void supProduitParId(int id) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM produits WHERE produit_id =?");
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean IsExistProduitOnTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT produit_id "
													  + "FROM produits "
													  + "WHERE produit_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterProduit (Produit p) throws SQLException, ProduitNotFoundException, FournisseurNotFoundException, HistoriquePrixNotFoundException,InsertionException  {
		Connection conn = Connexion.getConnection();
		if(FournisseurDAO.isExistFournisseurOnTableParId(p.getIdFournProduit())==false) {
			FournisseurDAO.ajouterFournisseur(new Fournisseur());
			p.setIdFourn(FournisseurDAO.getLastInsertIdFournisseur());
		}
		if(p.getIdProduit()==0 || IsExistProduitOnTableParId(p.getIdProduit())==false) {
			
			PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO Produits(nom,stock,prix_vente_courant,fournisseur_id) VALUES (?,?,?,?);");
			pstmt1.setString(1,p.getNomProduit());
			pstmt1.setInt(2,p.getStockProduit());
			pstmt1.setDouble(3,p.getPrixCourantProduit());
			pstmt1.setInt(4,p.getIdFournProduit());
			pstmt1.executeUpdate();
			pstmt1.close();
			p.setIdProduit(ProduitDAO.getLastInsertIdProduit());
			try {
				HistoriquePrixDAO.ajouterHistPrix(new HistoriquePrix(p.getIdProduit(),p.getPrixCourantProduit()));
			}catch(InsertionException hnfe) {
				System.out.println(hnfe);
			}
		}else {
			throw new InsertionException("id produit existe deja");
		}
	}
	public static void ajouterProduit (Produit p, Fournisseur f) throws InsertionException, SQLException, ProduitNotFoundException, FournisseurNotFoundException, HistoriquePrixNotFoundException  {
		Connection conn = Connexion.getConnection();
		FournisseurDAO.ajouterFournisseur(f);
		int idFourn;
		if(f.getIdFournisseur()==0)idFourn = FournisseurDAO.getLastInsertIdFournisseur();
		else idFourn = f.getIdFournisseur();
		p.setIdFourn(idFourn);
		
		if(p.getIdProduit()==0 || IsExistProduitOnTableParId(p.getIdProduit())==false) {
			
			PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO produits(nom,stock,prix_vente_courant,fournisseur_id) VALUES (?,?,?,?);");
			pstmt1.setString(1,p.getNomProduit());
			pstmt1.setInt(2,p.getStockProduit());
			pstmt1.setDouble(3,p.getPrixCourantProduit());
			pstmt1.setInt(4,p.getIdFournProduit());
			pstmt1.executeUpdate();
			pstmt1.close();
			p.setIdProduit(ProduitDAO.getLastInsertIdProduit());
			HistoriquePrixDAO.ajouterHistPrix(new HistoriquePrix(p.getIdProduit(),p.getPrixCourantProduit()));
			
		}else {
			throw new InsertionException("insertion produit");
		}
	}
	public static Produit getProduitParId(int id)throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produits "
														+ "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return new Produit(rs.getInt("produit_id"),
							   rs.getString("nom"),
							   rs.getInt("Stock"),
							   rs.getDouble("prix_vente_courant"),
							   rs.getInt("fournisseur_id"));
		}else throw new ProduitNotFoundException(""+id);
	}
	public static void afficherProduitParId(int id)throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produits "
														+ "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("produit numéro : "+rs.getInt("produit_id")
							  +", nom : "+rs.getString("nom")
							  +", stock : "+rs.getInt("stock")
							  +", prix de vente : "+rs.getDouble("prix_vente_courant")
							  +",fournisseur numero : "+rs.getInt("fournisseur_id"));
			pstmt.close();
			conn.close();
		}else throw new ProduitNotFoundException(""+id);
	}
//------------------NOM
	public static void modifNomProduitParId (int id, String nom) throws SQLException, ProduitNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produits "
				  									  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new ProduitNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE produits SET nom =? WHERE produit_id =?");
		pstmt.setInt(2, id);
		pstmt.setString(1, nom);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static String getNomProduitParId (int id) throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT nom FROM produits "
													  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getString("nom");
		}else throw new ProduitNotFoundException(""+id);
	}
//------------------Stock
	public static void modifStockProduitParId (int id, int stock) throws SQLException, ProduitNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT stock FROM produits "
				  									  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new ProduitNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE produits SET stock =? WHERE produit_id =?");
		pstmt.setInt(2, id);
		pstmt.setInt(1, stock);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	 public static void diminuerStockProduitParId(int id, int retirer) throws SQLException, ProduitNotFoundException{
		int stockCourant = getStockProduitParId(id);
		modifStockProduitParId(id, stockCourant-retirer);
	 }
	 public static void augmenterStockProduitParId(int id, int ajouter) throws SQLException, ProduitNotFoundException{
		int stockCourant = getStockProduitParId(id);
		modifStockProduitParId(id, stockCourant+ajouter);
		 }
	public static int getStockProduitParId (int id) throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT stock FROM produits "
													  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("stock");
		}else throw new ProduitNotFoundException(""+id);
	}
//------------------PrixCourant
	public static void modifPrixCourantProduitParId (int id, double prixCour) throws SQLException, ProduitNotFoundException, HistoriquePrixNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT prix_vente_courant FROM produits "
				  									  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new ProduitNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE produits SET prix_vente_courant =? WHERE produit_id =?");
		pstmt.setInt(2, id);
		pstmt.setDouble(1, prixCour);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		try {
			HistoriquePrixDAO.ajouterHistPrix(new HistoriquePrix(id,prixCour));
		}catch(InsertionException ie){
			System.out.println(ie);
		}
	}
	public static Double getPrixCourantProduitParId (int id) throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT prix_vente_courant FROM produits "
													  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getDouble("prix_vente_courant");
		}else throw new ProduitNotFoundException(""+id);
	}
//------------------IdFourn
	public static void modifIdFournProduitParId (int id, int idFourn) throws SQLException, ProduitNotFoundException, FournisseurNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produits "
				  									  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new ProduitNotFoundException(""+id);
		if(FournisseurDAO.isExistFournisseurOnTableParId(idFourn)==false)throw new FournisseurNotFoundException(""+idFourn);
		pstmt = conn.prepareStatement("UPDATE produits SET fournisseur_id =? WHERE produit_id =?");
		pstmt.setInt(2, id);
		pstmt.setInt(1, idFourn);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static int getIdFournProduitParId (int id) throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT fournisseur_id FROM produits "
													  + "WHERE produit_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("fournisseur_id");
		}else throw new ProduitNotFoundException(""+id);
	}
//id prod
	public static int getLastInsertIdProduit() throws SQLException, ProduitNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt;
		
		pstmt = conn.prepareStatement("SELECT produit_id FROM produits "
													  + "ORDER BY produit_id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("produit_id");
		}else throw new ProduitNotFoundException("Aucun produit dans la table");
	}
	public static int getIdProduitByNom(String nom) throws SQLException, ProduitNotFoundException{
		Connection conn=Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT produit_id, nom FROM produits "
				  									  + "WHERE nom =?;");
		pstmt.setString(1, nom);
		ResultSet rs= pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("produit_id");
		}else throw new ProduitNotFoundException("Aucun produit trouvé dans la table");
	}
}
/*
private int produitId;
    private String nom;
    private int stock;
    private double prixVenteCourant;
    private int fournisseurId; */
