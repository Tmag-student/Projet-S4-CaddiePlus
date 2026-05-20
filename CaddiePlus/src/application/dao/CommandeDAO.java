package application.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import application.Connexion;
import application.entites.*;
import application.exception.*;
import java.sql.Date;

public class CommandeDAO {
	public static void afficherTableCommande()throws SQLException {
		Connection conn = Connexion.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM commandes");

		System.out.println("(commande_id, date_commande, fournisseur_id, cout_total)");
		while(rs.next()==true) {
			System.out.println(rs.getInt("commande_id")+", "
					+rs.getDate("date_commande")+", "
					+rs.getInt("fournisseur_id")+", "
					+rs.getDouble("cout_total"));
		}
		System.out.println("Fin Table");
		stmt.close();
		conn.close();
		
	}
	public static void supCommandeParId(int idCommande) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM commandes WHERE commande_id =?");
		pstmt.setInt(1, idCommande);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public static boolean IsExistCommandeOnTableParId (int id) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT commande_id "
													  + "FROM commandes "
													  + "WHERE commande_id=?");
		pstmt.setInt(1, id);
		ResultSet rs =pstmt.executeQuery();
		return rs.next();
	}
	public static void ajouterCommande (Commande c, CommandeDetail[] cd) throws SQLException, ProduitNotFoundException, FournisseurNotFoundException, CommandeNotFoundException, StockInvalidException, InsertionException  {
		Connection conn = Connexion.getConnection();
		if(FournisseurDAO.isExistFournisseurOnTableParId(c.getIdFournisseurCommande())==false || c.getIdFournisseurCommande()==0) {	
			FournisseurDAO.ajouterFournisseur(new Fournisseur());
			c.setIdFournisseurCommande(FournisseurDAO.getLastInsertIdFournisseur());
		}
		
		PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO commandes(date_commande,fournisseur_id) VALUES (?,?);");
		if(c.getDateCommande()==null)pstmt1.setDate(1,new Date(System.currentTimeMillis()));
		else pstmt1.setDate(1,c.getDateCommande());
		pstmt1.setInt(2,c.getIdFournisseurCommande());
		pstmt1.executeUpdate();
		c.setIdCommande(getLastInsertIdCommande());
		//System.out.println(getLastInsertIdCommande());
		//System.out.println(c.getIdCommandeCommande()+"LELELEL");
		pstmt1.close();
		CommandeDetail.setIdCommandeCommandeDetail(c.getIdCommandeCommande(), cd);
		
		//System.out.println(cd[0].getIdCommandeCommandeDetail()+"lalalla");
		cd=CommandeDetailDAO.ajouterCommandeDetail(cd);

		modifCoutTotalCommandeParId(CommandeDetailDAO.calculerCoutTotalCommande(c.getIdCommandeCommande(), cd), c.getIdCommandeCommande());
		
	}
	public static Commande getCommandeParId(int id)throws SQLException, CommandeNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM commandes "
														+ "WHERE commande_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return new Commande(rs.getInt("commande_id"),
							   rs.getDate("date_commande"),
							   rs.getInt("fournisseur_id"),
							   rs.getDouble("cout_total"));
		}else throw new CommandeNotFoundException(""+id);
	}
	public static void afficherFactureCommandeParId(int id)throws SQLException, CommandeNotFoundException, ProduitNotFoundException, FournisseurNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM commandes "
														+ "WHERE commande_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			System.out.println("Commande numéro "+rs.getInt("commande_id")
							  +", date de commande : "+rs.getDate("date_commande")
							  +", Fournisseur numero "+rs.getInt("fournisseur_id")+ " "+FournisseurDAO.getNomFournisseurParId(rs.getInt("fournisseur_id"))
							  +", cout total : "+rs.getDouble("cout_total")
							  +"\n\tDetail de la commande :");
			rs = CommandeDetailDAO.ensembledetailsCommandeParId(id);
			while(rs.next()!=false) {
				System.out.println("\t\t- produit : "+rs.getString("produit")+", "+rs.getString("marque")+" *"+rs.getInt("quantite")+", "+rs.getDouble("Prix_at_time")+"/u");
			}
			pstmt.close();
			conn.close();
		}else throw new CommandeNotFoundException(""+id);
	}
//------------------datevente

	public static Date getDateCommandeParId (int id) throws SQLException, CommandeNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT date_commande FROM commandes "
													  + "WHERE commande_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getDate("date_commande");
		}else throw new CommandeNotFoundException(""+id);
	}
//------------------client

	public static int getIdFournisseurCommandeParId (int id) throws SQLException, CommandeNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT fournisseur_id FROM commandes "
													  + "WHERE commande_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("commande_id");
		}else throw new CommandeNotFoundException(""+id);
	}
//------------------cout total
	public static Double getCoutTotalCommandeParId (int id) throws SQLException, CommandeNotFoundException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT cout_total FROM commandes "
													  + "WHERE commande_id =?");
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getDouble("cout_total");
		}else throw new CommandeNotFoundException(""+id);
	}
	public static void modifCoutTotalCommandeParId (double newCoutTotal, int idCommande) throws SQLException, CommandeNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT cout_total FROM commandes "
				  									  + "WHERE commande_id =?");
		pstmt.setInt(1, idCommande);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==false) throw new CommandeNotFoundException(""+idCommande);
		pstmt = conn.prepareStatement("UPDATE commandes SET cout_total =? WHERE commande_id =?");
		pstmt.setInt(2, idCommande);
		pstmt.setDouble(1, newCoutTotal);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
//---idvente
	public static int getLastInsertIdCommande() throws SQLException, CommandeNotFoundException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT commande_id FROM commandes "
													  + "ORDER BY commande_id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("commande_id");
		}else throw new CommandeNotFoundException("Aucun commande dans la table");
	}
}
/*
  private int venteId;
    private java.sql.Timestamp dateVente;
    private int clientId;
    private double coutTotal; */
