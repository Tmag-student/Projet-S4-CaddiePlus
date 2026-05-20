package application.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Connexion;
import application.entites.Fournisseur;
import application.exception.FournisseurNotFoundException;

public class FournisseurDAO {
	public static void afficherTableFournisseur()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("""
				SELECT f.nom, SUM(c.cout_total) as ArgentDepense, SUM(cd.quantite) as nbProduit FROM fournisseurs f
				LEFT JOIN commandes c On f.fournisseur_id =c.fournisseur_id
				LEFT JOIN  commande_detail cd ON cd.commande_id=c.commande_id
				GROUP BY f.fournisseur_id;
				"""
				);
		System.out.println("---------");
		System.out.println("Recap Fournisseur (nom,ValeurMarchandisesAchetées,quantiteBiensAchetes)");
		while(rs.next()==true) {
			System.out.println(rs.getString("nom")+" | "
					+rs.getDouble("ArgentDepense")+" | "+ rs.getInt("nbProduit"));
		}
		System.out.println("---------");
		stmt.close();
		conn.close();
		
	}
	public static void supFournisseurParId(int id) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM fournisseurs WHERE fournisseur_id =?");
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean isExistFournisseurOnTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT fournisseur_id "
													  + "FROM fournisseurs "
													  + "WHERE fournisseur_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static boolean isExistFournisseurOnTableParNom(String nom) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT nom "
													  + "FROM fournisseurs "
													  + "WHERE nom=?");
		pstmt.setString(1, nom);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterFournisseur (Fournisseur f) throws SQLException, FournisseurNotFoundException  {
		Connection conn = Connexion.getConnection();
		if(isExistFournisseurOnTableParNom(f.getNomFournisseur())==true)f.seIdFournisseur(getIdFournisseurByNom(f.getNomFournisseur()));
		else {
			if(f.getIdFournisseur()==0 || isExistFournisseurOnTableParId(f.getIdFournisseur())==false) {
				PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO Fournisseurs(nom) VALUES (?);");
				pstmt1.setString(1,f.getNomFournisseur());
				pstmt1.executeUpdate();
				pstmt1.close();
				f.seIdFournisseur(getLastInsertIdFournisseur());
			}
		}
	}
	public static Fournisseur getFournisseurParId(int id)throws SQLException, FournisseurNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM fournisseurs "
														+ "WHERE fournisseur_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return new Fournisseur(rs.getInt("fournisseur_id"),rs.getString("nom"));
		}else throw new FournisseurNotFoundException(""+id);
	}
	public static void afficherFournisseurParId(int id)throws SQLException, FournisseurNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM fournisseurs "
														+ "WHERE fournisseur_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("client numéro : "+rs.getInt("fournisseur_id")
							  +", nom : "+rs.getString("nom"));
			pstmt.close();
			conn.close();
		}else throw new FournisseurNotFoundException(""+id);
	}
//------------------NOM
	public static void modifNomFournisseurParId (int id, String nom) throws SQLException, FournisseurNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM fournisseurs "
				  									  + "WHERE fournisseur_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new FournisseurNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE fournisseurs SET nom =? WHERE fournisseur_id =?");
		pstmt.setInt(2, id);
		pstmt.setString(1, nom);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static String getNomFournisseurParId (int id) throws SQLException, FournisseurNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT nom FROM fournisseurs "
													  + "WHERE fournisseur_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getString("nom");
		}else throw new FournisseurNotFoundException(""+id);
	}
//----id
	public static int getLastInsertIdFournisseur() throws SQLException, FournisseurNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT fournisseur_id FROM fournisseurs "
													  + "ORDER BY fournisseur_id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("fournisseur_id");
		}else throw new FournisseurNotFoundException("Aucun fournisseur dans la table");
	}
	public static int getIdFournisseurByNom(String nom) throws SQLException,FournisseurNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM fournisseurs "
													  + "WHERE nom = ?;");
		pstmt.setString(1,nom);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("fournisseur_id");
		}else throw new FournisseurNotFoundException("Aucun fournisseur trouvé dans la table");
	}
// STAT SUR FOURNISSEUR :
	public static void getStatFournisseur() {
		
	}
}


/*
private int fournisseurId;
private String nom; */
