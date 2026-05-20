package application.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import application.Connexion;
import application.entites.Client;
import application.entites.VenteDetail;
import application.entites.Vente;
import application.exception.*;

public class VenteDAO {
	public static void afficherTableVente()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ventes");

		System.out.println("(vente_id, date_vente, client_id, cout_total)");
		while(rs.next()==true) {
			System.out.println(rs.getInt("vente_id")+", "
					+rs.getTimestamp("date_vente")+", "
					+rs.getInt("client_id")+", "
					+rs.getDouble("cout_total"));
		}
		System.out.println("Fin Table");
		stmt.close();
		conn.close();
		
	}
	public static void supVenteParId(int idVente) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ventes WHERE vente_id =?");
		pstmt.setInt(1, idVente);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean IsExistVenteOnTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT vente_id "
													  + "FROM ventes "
													  + "WHERE vente_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterVente (Vente v, VenteDetail[] vd) throws SQLException, ProduitNotFoundException, ClientNotFoundException, VenteNotFoundException, StockInvalidException, IdVenteDetailException, InsertionException  {
		Connection conn = Connexion.getConnection();
		if(ClientDAO.checkExistClientTableParId(v.getIdClientVente())==false || v.getIdClientVente()==0) {	
			ClientDAO.ajouterClient(new Client());
			v.setIdClientVente(ClientDAO.getLastInsertIdClient());
		}
		
		PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO ventes(date_vente,client_id) VALUES (?,?);");
		if(v.getDateVente()==null)pstmt1.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
		else pstmt1.setTimestamp(1,v.getDateVente());
		pstmt1.setInt(2,v.getIdClientVente());
		pstmt1.executeUpdate();
		v.setIdVente(getLastInsertIdVente());
		pstmt1.close();
		VenteDetail.setIdVenteVenteDetail(v.getIdVenteVente(), vd);
		vd=VenteDetailDAO.ajouterVenteDetail(vd);
		VenteDAO.modifCoutTotalVenteParId(VenteDetailDAO.calculerCoutTotalVente(v.getIdVenteVente(), vd), v.getIdVenteVente());
		
			
	}
	public static Vente getVenteParId(int id)throws SQLException, VenteNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ventes "
														+ "WHERE vente_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return new Vente(rs.getInt("vente_id"),
							   rs.getTimestamp("date_vente"),
							   rs.getInt("client_id"),
							   rs.getDouble("cout_total"));
		}else throw new VenteNotFoundException(""+id);
	}
	public static void afficherFactureVenteParId(int id)throws SQLException, VenteNotFoundException, ProduitNotFoundException, ClientNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ventes "
														+ "WHERE vente_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("Vente numéro "+rs.getInt("vente_id")
							  +", date de vente : "+rs.getDate("date_vente")
							  +", client numero "+rs.getInt("client_id")+ " "+ClientDAO.getNomClientParId(rs.getInt("client_id"))
							  +", cout total : "+rs.getDouble("cout_total")
							  +"\n\tDetail de la vente :");
			rs = VenteDetailDAO.ensembledetailsVenteParId(id);
			while(rs.next()!=false) {
				System.out.println("\t\t- produit : "+rs.getString("produit")+", "+rs.getString("marque")+" *"+rs.getInt("quantite")+", "+rs.getDouble("Prix_at_time")+"/u");
			}
			pstmt.close();
			conn.close();
		}else throw new VenteNotFoundException(""+id);
	}
//------------------datevente

	public static Timestamp getDateVenteParId (int id) throws SQLException, VenteNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT date_vente FROM ventes "
													  + "WHERE vente_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getTimestamp("date_vente");
		}else throw new VenteNotFoundException(""+id);
	}
//------------------client

	public static int getIdClientVenteParId (int id) throws SQLException, VenteNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT client_id FROM ventes "
													  + "WHERE vente_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("client_id");
		}else throw new VenteNotFoundException(""+id);
	}
//------------------cout total
	public static Double getCoutTotalVenteParId (int id) throws SQLException, VenteNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT cout_total FROM ventes "
													  + "WHERE vente_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getDouble("cout_total");
		}else throw new VenteNotFoundException(""+id);
	}
	public static void modifCoutTotalVenteParId (double newCoutTotal, int idVente) throws SQLException, VenteNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT cout_total FROM ventes "
				  									  + "WHERE vente_id =?");
		pstmt.setInt(1, idVente);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new VenteNotFoundException(""+idVente);
		pstmt = conn.prepareStatement("UPDATE ventes SET cout_total =? WHERE vente_id =?");
		pstmt.setInt(2, idVente);
		pstmt.setDouble(1, newCoutTotal);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
//---idvente
	public static int getLastInsertIdVente() throws SQLException, VenteNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT vente_id FROM ventes "
													  + "ORDER BY vente_id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("vente_id");
		}else throw new VenteNotFoundException("Aucun vente dans la table");
	}
}
/*
  private int venteId;
    private java.sql.Timestamp dateVente;
    private int clientId;
    private double coutTotal; */
