package application.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import application.Connexion;
import application.entites.Client;
import application.exception.*;

import java.sql.ResultSet;

public class ClientDAO {
	/* 
	 Les points d'intérogation dans les requêtes sont des marqueurs 
	 pour placer les valeurs par la suite : setTYPE
	 */
//--------Client	
	public static void afficherTableClient()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("""
				SELECT c.nom, SUM(v.cout_total) as ArgentDepense, SUM(vd.quantite) as nbProduit FROM clients c
					LEFT JOIN ventes v On c.client_id =v.client_id
					LEFT JOIN  vente_detail vd ON vd.vente_id=v.vente_id
					GROUP BY c.client_id;
			"""	);
		System.out.println("-----------");
		System.out.println("Recap Client (Nom,DepenseTotal,NbArticle)");
		while(rs.next()==true) {
			System.out.println(rs.getString("nom")+" | "+rs.getDouble("ArgentDepense")+" | "
					+rs.getInt("nbProduit"));
		}
		System.out.println("-----------");
		stmt.close();
		conn.close();
		
	}
	public static void supClientParId(int id) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM clients WHERE client_id =?");
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean checkExistClientTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT client_id "
													  + "FROM clients "
													  + "WHERE client_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static boolean isExistClientOnTableParNom(String nom) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT nom FROM clients WHERE nom=?");
		pstmt.setString(1, nom);
		ResultSet rs=pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterClient (Client c) throws SQLException, ClientNotFoundException  {
		Connection conn = Connexion.getConnection();
		if(isExistClientOnTableParNom(c.getNomClient())==true )c.setIdClient(getIdClientByNom(c.getNomClient()));
		else {
			if(c.getIdClient()==0 || checkExistClientTableParId(c.getIdClient())==false) {
				PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO clients(nom,points_fidelite, date_creation, anniversaire) VALUES (?,?,?,?);");
				pstmt1.setString(1,c.getNomClient());
				pstmt1.setDouble(2,c.getPtFidelClient());
				pstmt1.setDate(3,c.getDateCreaClient());
				pstmt1.setDate(4,c.getAnivClient());
				pstmt1.executeUpdate();
				pstmt1.close();
				c.setIdClient(getLastInsertIdClient());
			}else {
				System.out.println("Client déjà dans la base de donnée");
			}
		}
	}
	public static Client getClientParId(int id)throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clients "
														+ "WHERE client_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("client numéro : "+rs.getInt("client_id")
							  +", nom : "+rs.getString("nom")
							  +", fidelite : "+rs.getDouble("points_fidelite")
							  +", inscrit le : "+rs.getDate("date_creation")
							  +", né le : "+rs.getDate("anniversaire"));
			return new Client(rs.getInt("client_id"),rs.getString("nom"),
							  rs.getDouble("points_fidelite"),rs.getDate("date_creation"),
							  rs.getDate("anniversaire"));
		}else throw new ClientNotFoundException(""+id);
	}
	public static void afficherClientParId(int id)throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clients "
														+ "WHERE client_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("client numéro : "+rs.getInt("client_id")
							  +", nom : "+rs.getString("nom")
							  +", fidelite : "+rs.getDouble("points_fidelite")
							  +", inscrit le : "+rs.getDate("date_creation")
							  +", né le : "+rs.getDate("anniversaire"));
			pstmt.close();
			conn.close();
		}else throw new ClientNotFoundException(""+id);
	}
	//-----Anniversaire
	public static void modifAnnivClientParId(int id, Date newanniv) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("UPDATE clients SET anniversaire = ? WHERE client_id =?");
		pstmt.setDate(1, newanniv);
		pstmt.setInt(2,id);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		
	}
	//------Points de fidelite
	public static double pFclientParId(int id)throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clients WHERE client_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			double tmp =rs.getDouble("points_fidelite");
			pstmt.close();
			conn.close();
			return tmp;
		}else throw new ClientNotFoundException(""+id);
	}
	public static void modifPtFidClientParId (double quantite, int id)throws SQLException { // faire un fonction qui change juste, puis d'autre pour ajout ou retirer
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("UPDATE clients SET points_fidelite =? WHERE client_id =?");
		pstmt.setInt(2, id);
		pstmt.setDouble(1, quantite);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
//------------------NOM
	public static void modifNomClientParId (int id, String nom) throws SQLException, ClientNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clients "
				  									  + "WHERE client_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new ClientNotFoundException(""+id);
		pstmt = conn.prepareStatement("UPDATE clients SET nom =? WHERE client_id =?");
		pstmt.setInt(2, id);
		pstmt.setString(1, nom);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static String getNomClientParId (int id) throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT nom FROM clients "
													  + "WHERE client_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getString("nom");
		}else throw new ClientNotFoundException(""+id);
	}
//---id
	public static int getLastInsertIdClient() throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT client_id FROM clients "
													  + "ORDER BY client_id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("client_id");
		}else throw new ClientNotFoundException("Aucun Client dans la table");
	}
	public static int getIdClientByNom(String nom) throws SQLException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT client_id, nom FROM clients "
													  + "WHERE nom=?;");
		pstmt.setString(1, nom);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("client_id");
		}else throw new ClientNotFoundException("Aucun Client trouvé dans la table");
	}
}






/*
private int clientId;
private String nom;
private double pointsFidelite;
private java.sql.Timestamp dateCreation;
private java.sql.Timestamp anniversaire;
*/