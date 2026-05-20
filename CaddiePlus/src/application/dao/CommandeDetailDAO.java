package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Connexion;
import application.entites.CommandeDetail;

import application.exception.IdCommandeDetailException;
import application.exception.ProduitNotFoundException;
import application.exception.CommandeNotFoundException;

public class CommandeDetailDAO {
	public static void ajouterCommandeDetail (CommandeDetail cd) throws SQLException, ProduitNotFoundException, CommandeNotFoundException,IdCommandeDetailException {
		Connection conn = Connexion.getConnection();

		if(ProduitDAO.IsExistProduitOnTableParId(cd.getIdProduitCommandeDetail())==false) {
			throw new ProduitNotFoundException(""+cd.getIdProduitCommandeDetail());
		}
		if(CommandeDAO.IsExistCommandeOnTableParId(cd.getIdCommandeCommandeDetail())==false) {
			throw new CommandeNotFoundException(""+cd.getIdCommandeCommandeDetail());
			
		}
		if(cd.getIdCommandeDetail()!=0)throw new IdCommandeDetailException(""+cd.getIdCommandeDetail());
		ProduitDAO.augmenterStockProduitParId(cd.getIdProduitCommandeDetail(), cd.getQuantiteCommandeDetail());
		PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO commande_detail (commande_id,produit_id,quantite) VALUES (?,?,?);");
		pstmt1.setInt(1,cd.getIdCommandeCommandeDetail());
		pstmt1.setInt(2,cd.getIdProduitCommandeDetail());
		pstmt1.setInt(3,cd.getQuantiteCommandeDetail());
		pstmt1.executeUpdate();
		pstmt1.close();
		try {
			cd.setIdCommandeDetail(getLastInsertIdCommandeDetail());
		}catch (IdCommandeDetailException icde) {
			System.out.println(icde);
		}
	}
	public static CommandeDetail[] ajouterCommandeDetail (CommandeDetail[] cd) throws SQLException, CommandeNotFoundException {
		CommandeDetail[] cdReturn =new CommandeDetail[cd.length];
		for(int i =0; i<cd.length;i++) {
			try {
				ajouterCommandeDetail(cd[i]);
				cdReturn[i]=cd[i];
			}catch(ProduitNotFoundException pnfe){
				System.out.println(pnfe);
				cdReturn[i]=null;
			}catch(IdCommandeDetailException ivde) {
				System.out.println(ivde);
				cdReturn[i]=null;
			}
		}
		return cdReturn;
	}
	public static int getLastInsertIdCommandeDetail() throws SQLException, IdCommandeDetailException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt;
		
		pstmt = conn.prepareStatement("SELECT id FROM commande_detail "
													  + "ORDER BY id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("id");
		}else throw new IdCommandeDetailException("il n'y a rien dans la table");
	}
	public static double calculerCoutTotalCommande(int commandeId, CommandeDetail[] cd ) throws SQLException, ProduitNotFoundException  {
		double ct = 0;
		for(int i =0; i<cd.length;i++) {
			if (cd[i]!=null)
			ct+=cd[i].getQuantiteCommandeDetail()*ProduitDAO.getPrixCourantProduitParId(cd[i].getIdProduitCommandeDetail());
		}
		return ct;
		
	}
	public static ResultSet ensembledetailsCommandeParId(int idCommande) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("""
														SELECT 
														 p.nom as produit, 
														 f.nom as marque, 
														 cd.quantite, 
														 (SELECT 					
															 histo_prix 
															 FROM historique_prix 
															 WHERE cast(date_changement as DATE)<=c.date_commande 
																 AND p.produit_id = produit_id 
															 ORDER BY hist_prix_id DESC LIMIT 1) 
															 as Prix_at_time 
															 FROM commande_detail cd  
														 INNER JOIN produits p ON p.produit_id=cd.produit_id 
														 INNER JOIN fournisseurs f ON f.fournisseur_id=p.fournisseur_id 
														 INNER JOIN commandes c ON c.commande_id = cd.commande_id 
														 WHERE c.commande_id =?; 
													"""	);
		pstmt.setInt(1, idCommande);
		return pstmt.executeQuery();
	}
}
