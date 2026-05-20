package application.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import application.Connexion;
import application.entites.Produit;
import application.exception.ProduitNotFoundException;

import application.exception.FournisseurNotFoundException;
import application.entites.HistoriquePrix;
import application.exception.*;
// voir dans modif prix chez produit pour relier avec historique
public class HistoriquePrixDAO {
	public static void afficherTableHistoriquePrix()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("""
				SELECT p.nom, hp.date_changement, hp.histo_prix FROM historique_prix hp
					INNER JOIN produits p ON p.produit_id = hp.produit_id
					ORDER BY p.nom ASC, hp.date_changement DESC;
				""");
		System.out.println("-----------");
		System.out.println("Recap Historique Prix (Produit,DateModification,Prix)");
		while(rs.next()!=false) {
			System.out.println(rs.getString("p.nom")+" | "+rs.getTimestamp("hp.date_changement")+" | "+rs.getDouble("hp.histo_prix"));
		}
		System.out.println("-----------");
		stmt.close();
		conn.close();
		
	}
	public static void supProduitParId(int id) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM historique_prix WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean IsExistHistPrixOnTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT hist_prix_id "
													  + "FROM historique_prix "
													  + "WHERE hist_prix_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterHistPrix (HistoriquePrix hp) throws SQLException, ProduitNotFoundException, InsertionException  {
		Connection conn = Connexion.getConnection();
		if(ProduitDAO.IsExistProduitOnTableParId(hp.getIdProduitHistPrix())==false) {
			throw new ProduitNotFoundException(""+hp.getIdProduitHistPrix());
		}
		if(hp.getIdHistPrix()==0 || IsExistHistPrixOnTableParId(hp.getIdHistPrix())==false) {
			
			PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO historique_prix(date_changement,histo_prix, produit_id) VALUES (?,?,?);");
			if(hp.getDateChangeHistPrix()==null) {
				pstmt1.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
			}else {
				pstmt1.setTimestamp(1,hp.getDateChangeHistPrix());
			}
			pstmt1.setDouble(2, hp.getPrixHistoHistPrix());
			pstmt1.setInt(3,hp.getIdProduitHistPrix());
			pstmt1.executeUpdate();
			pstmt1.close();
		}else {
			throw new InsertionException("l'ajout dans l'historique n'a pas foncionné");
		}
	}
	public static void ajouterHistPrix (HistoriquePrix hp, Produit p) throws SQLException, ProduitNotFoundException, HistoriquePrixNotFoundException, FournisseurNotFoundException, InsertionException  {
		Connection conn = Connexion.getConnection();
		ProduitDAO.ajouterProduit(p);
		int idProduit;
		if(p.getIdProduit()==0)idProduit = ProduitDAO.getLastInsertIdProduit();
		else idProduit = p.getIdProduit();
		p.setIdFourn(idProduit);
		
		if(hp.getIdHistPrix()==0 || IsExistHistPrixOnTableParId(hp.getIdHistPrix())==false) {
			
			PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO historique_prix(date_changement,produit_id) VALUES (?,?);");
			if(hp.getDateChangeHistPrix()==null) {
				pstmt1.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
			}else {
				pstmt1.setTimestamp(1,hp.getDateChangeHistPrix());
			}
			pstmt1.setDouble(2, hp.getPrixHistoHistPrix());
			pstmt1.setInt(2,hp.getIdProduitHistPrix());
			pstmt1.executeUpdate();
			pstmt1.close();
			
		}else {
			modifDateChangeHistPrixParId(hp.getIdHistPrix(),hp.getDateChangeHistPrix());
			modifIdProduitHistPrixParId(hp.getIdHistPrix(),hp.getIdProduitHistPrix());
			modifHistoPrixHistPrixParId(hp.getIdHistPrix(),hp.getPrixHistoHistPrix());
		}
	}
	public static HistoriquePrix getHistPrixParId(int id)throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM historique_prix "
														+ "WHERE hist_prix_id_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return new HistoriquePrix(rs.getInt("hist_prix_id"),
							   rs.getTimestamp("date_changement"),
							   rs.getDouble("histo_prix"),
							   rs.getInt("produit_id"));
		}else throw new HistoriquePrixNotFoundException(""+id);
	}
	public static void afficherHistPrixParId(int id)throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM historique_prix "
														+ "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("historique numéro : "+rs.getInt("hist_prix_id")
							  +", date de modification : "+rs.getTimestamp("date_changement")
							  +", ancien prix : "+rs.getDouble("histo_prix")
							  +", produit de reference numéro : "+rs.getInt("produit_id"));
			pstmt.close();
			conn.close();
		}else throw new HistoriquePrixNotFoundException(""+id);
	}
//------------------datechangmenrt
	public static void modifDateChangeHistPrixParId (int id,  java.sql.Timestamp date) throws SQLException, HistoriquePrixNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM historique_prix "
				  									  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new HistoriquePrixNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE historique_prix SET nom =? WHERE hist_prix_id =?");
		pstmt.setInt(2, id);
		pstmt.setTimestamp(1, date);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static java.sql.Timestamp getDateChangeProduitParId (int id) throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT date_changement FROM historique_prix "
													  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getTimestamp("date_changmement");
		}else throw new HistoriquePrixNotFoundException(""+id);
	}
//------------------IdFourn
	// en vrai ne paas utilisé car comme des log tu modifies pas les log
	public static void modifIdProduitHistPrixParId (int id, int idProduit) throws SQLException, HistoriquePrixNotFoundException, ProduitNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM historique_prix "
				  									  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new HistoriquePrixNotFoundException(""+id);
		if (ProduitDAO.IsExistProduitOnTableParId(idProduit)==false)throw new ProduitNotFoundException(""+idProduit);
		pstmt = conn.prepareStatement("UPDATE historique_prix SET Produit_id =? WHERE hist_prix_id =?");
		pstmt.setInt(2, id);
		pstmt.setInt(1, idProduit);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static int getIdProduitHistPrixParId (int id) throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT produit_id FROM historique_prix "
													  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("produit_id");
		}else throw new HistoriquePrixNotFoundException(""+id);
	}
//------histoPrix
	public static double getHistoPrixHistPrixParId(int id)throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT histo_prix FROM historique_prix "
													  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getDouble("histo_prix");
		}else throw new HistoriquePrixNotFoundException(""+id);
	}
	public static void modifHistoPrixHistPrixParId(int id,double histoPrix)throws SQLException, HistoriquePrixNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM historique_prix "
				  									  + "WHERE hist_prix_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new HistoriquePrixNotFoundException(""+id);

		pstmt = conn.prepareStatement("UPDATE historique_prix SET histo_prix =? WHERE hist_prix_id =?");
		pstmt.setInt(2, id);
		pstmt.setDouble(1, histoPrix);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
}
/*
 private int histPrixId;
    private java.sql.Timestamp dateChangement;
    private int produitId;
*/
