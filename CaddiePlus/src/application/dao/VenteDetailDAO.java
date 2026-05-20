package application.dao;
import application.Connexion;

import application.entites.VenteDetail;

import application.exception.ProduitNotFoundException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import application.exception.VenteNotFoundException;
import application.exception.IdVenteDetailException;
import application.exception.StockInvalidException;
public class VenteDetailDAO {
	public static double calculerCoutTotalVente(int venteId, VenteDetail[] vd ) throws SQLException, ProduitNotFoundException  {
		double ct = 0;
		for(int i =0; i<vd.length;i++) {
			if (vd[i]!=null)
			ct+=vd[i].getQuantiteVenteDetail()*ProduitDAO.getPrixCourantProduitParId(vd[i].getIdProduitVenteDetail());
		}
		return ct;
		
	}
	public static ResultSet ensembledetailsVenteParId(int idVente) throws SQLException {
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(""" 
														SELECT 
														 p.nom as produit, 
														 f.nom as marque, 
														 vd.quantite, 
														 (SELECT 					
															 histo_prix 
															 FROM historique_prix 
															 WHERE cast(date_changement as DATE)<=v.date_vente 
																 AND p.produit_id = produit_id 
															 ORDER BY hist_prix_id DESC LIMIT 1) 
															 as Prix_at_time 
															 FROM vente_detail vd  
														 INNER JOIN produits p ON p.produit_id=vd.produit_id 
														 INNER JOIN fournisseurs f ON f.fournisseur_id=p.fournisseur_id 
														 INNER JOIN ventes v ON v.vente_id = vd.vente_id 
														 WHERE v.vente_id =?;	
														 """);
		pstmt.setInt(1, idVente);
		return pstmt.executeQuery();
	}
	public static void ajouterVenteDetail (VenteDetail vd) throws SQLException, ProduitNotFoundException, VenteNotFoundException,IdVenteDetailException, StockInvalidException {
		Connection conn = Connexion.getConnection();

		if(ProduitDAO.IsExistProduitOnTableParId(vd.getIdProduitVenteDetail())==false) {
			throw new ProduitNotFoundException(""+vd.getIdProduitVenteDetail());
		}
		if(VenteDAO.IsExistVenteOnTableParId(vd.getIdVenteVenteDetail())==false) {
			throw new VenteNotFoundException(""+vd.getIdVenteVenteDetail());
			
		}
		if(vd.getIdVenteDetail()!=0)throw new IdVenteDetailException(""+vd.getIdVenteDetail());
		if (vd.getQuantiteVenteDetail()>ProduitDAO.getStockProduitParId(vd.getIdProduitVenteDetail())) {
			throw new StockInvalidException (vd.getQuantiteVenteDetail(),ProduitDAO.getStockProduitParId(vd.getIdProduitVenteDetail()),ProduitDAO.getNomProduitParId(vd.getIdProduitVenteDetail()));
		}else {
			ProduitDAO.diminuerStockProduitParId(vd.getIdProduitVenteDetail(), vd.getQuantiteVenteDetail());
		}	
		PreparedStatement pstmt1 =conn.prepareStatement("INSERT INTO vente_detail (vente_id,produit_id,quantite) VALUES (?,?,?);");
		pstmt1.setInt(1,vd.getIdVenteVenteDetail());
		pstmt1.setInt(2,vd.getIdProduitVenteDetail());
		pstmt1.setInt(3,vd.getQuantiteVenteDetail());
		pstmt1.executeUpdate();
		pstmt1.close();
		try {
			vd.setIdVenteDetail(getLastInsertIdVenteDetail());
		}catch (IdVenteDetailException ivde) {
			System.out.println(ivde);
		}
	}
	public static VenteDetail[] ajouterVenteDetail (VenteDetail[] vd) throws SQLException, VenteNotFoundException {
		VenteDetail[] vdReturn =new VenteDetail[vd.length];
		for(int i =0; i<vd.length;i++) {
			try {
				ajouterVenteDetail(vd[i]);
				vdReturn[i]=vd[i];
			}catch(StockInvalidException sie){
				System.out.println(sie);
				vdReturn[i]=null;
			}catch(ProduitNotFoundException pnfe){
				System.out.println(pnfe);
				vdReturn[i]=null;
			}catch(IdVenteDetailException ivde) {
				System.out.println(ivde);
				vdReturn[i]=null;
			}
		}
		return vdReturn;
	}
	public static int getLastInsertIdVenteDetail() throws SQLException, IdVenteDetailException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt;
		
		pstmt = conn.prepareStatement("SELECT id FROM vente_detail "
													  + "ORDER BY id DESC LIMIT 1;");
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("id");
		}else throw new IdVenteDetailException("il n'y a rien dans la table");
	}
}
