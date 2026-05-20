package application;
import java.sql.Connection;

import java.sql.SQLException;

import java.sql.Date;
import application.dao.*;
import application.exception.*;
import application.entites.*;
public class Main {

	public static void main (String[] args){
		try {
		Connection conn = Connexion.getConnection();
		DataBase.initialisation(conn);
		
		Fournisseur lotus= new Fournisseur("Lotus");
		Fournisseur nestle = new Fournisseur("Nestle");
		Fournisseur AgriculteurNanterre= new Fournisseur("NanterreAgri");
		FournisseurDAO.ajouterFournisseur(lotus);
		FournisseurDAO.ajouterFournisseur(nestle);
		FournisseurDAO.ajouterFournisseur(AgriculteurNanterre);
		
		Produit p1= new Produit("Papier toilette",10,3.49);
		Produit p2= new Produit("Mouchoirs",25,1.99);
		Produit p3= new Produit("Speculoos",3,2.32);
		Produit p4= new Produit("Tablette de Chocolat",50,1.79);
		Produit p5= new Produit("Eau",12,4.50);
		Produit p6= new Produit("Tomate",5.2);
		Produit p7= new Produit("Pomme de terre",20,0.99);

		ProduitDAO.ajouterProduit(p1,lotus);
		ProduitDAO.ajouterProduit(p2,lotus);
		ProduitDAO.ajouterProduit(p3,nestle);
		ProduitDAO.ajouterProduit(p4,nestle);
		ProduitDAO.ajouterProduit(p5,nestle);
		ProduitDAO.ajouterProduit(p6,AgriculteurNanterre);
		p7.setIdFourn(AgriculteurNanterre.getIdFournisseur());
		ProduitDAO.ajouterProduit(p7);
		ProduitDAO.modifPrixCourantProduitParId(ProduitDAO.getIdProduitByNom("Tomate"), 3.23);
		ProduitDAO.modifPrixCourantProduitParId(p1.getIdProduit(), 5.50);
		ProduitDAO.modifPrixCourantProduitParId(p4.getIdProduit(), 1.95);

		Client lina= new Client("Lina", Date.valueOf("2015-03-22"));
		Client enzo= new Client(3, "Enzo", 4000, Date.valueOf("2011-01-01"), Date.valueOf("1990-05-11"));
		Client maya= new Client("Maya", Date.valueOf("2015-03-22"));
		Client thomas= new Client("Thomas", Date.valueOf("2006-11-07"));
		ClientDAO.ajouterClient(thomas);
		ClientDAO.ajouterClient(maya);
		ClientDAO.ajouterClient(enzo);
		ClientDAO.ajouterClient(lina);

		VenteDetail[] vd1 = new VenteDetail[3];
		vd1[0]= new VenteDetail(p6.getIdProduit(),2);
		vd1[1]= new VenteDetail(p3.getIdProduit(),1);
		vd1[2]= new VenteDetail(p1.getIdProduit(),3);
		Vente v1 = new Vente(thomas.getIdClient());
		VenteDAO.ajouterVente(v1, vd1);
		VenteDAO.afficherFactureVenteParId(v1.getIdVenteVente());

		VenteDetail[] vd2 = new VenteDetail[2];
		vd2[0]= new VenteDetail(p4.getIdProduit(),10);
		vd2[1]= new VenteDetail(p5.getIdProduit(),5);
		Vente v2 = new Vente(maya.getIdClient());		
		VenteDAO.ajouterVente(v2, vd2);
		VenteDAO.afficherFactureVenteParId(v2.getIdVenteVente());

		CommandeDetail[] cd1 = new CommandeDetail[2];
		cd1[0]= new CommandeDetail(p4.getIdProduit(),30);
		cd1[1]= new CommandeDetail(p3.getIdProduit(),15);
		Commande c1=new Commande(nestle.getIdFournisseur());
		CommandeDAO.ajouterCommande(c1, cd1);
		CommandeDAO.afficherFactureCommandeParId(c1.getIdCommandeCommande());

		CommandeDetail[] cd2 = new CommandeDetail[2];
		cd2[0]= new CommandeDetail(p6.getIdProduit(),50);
		cd2[1]= new CommandeDetail(p7.getIdProduit(),100);
		Commande c2=new Commande(AgriculteurNanterre.getIdFournisseur());
		CommandeDAO.ajouterCommande(c2, cd2);
		CommandeDAO.afficherFactureCommandeParId(c2.getIdCommandeCommande());
		
		
		
		HistoriquePrixDAO.afficherTableHistoriquePrix();
		
		FournisseurDAO.afficherTableFournisseur();
		
		ClientDAO.afficherTableClient();
	
		}catch(SQLException se) {
			System.out.println(se);
		}catch(FournisseurNotFoundException fnfe) {
			System.out.println(fnfe);
		}catch(ProduitNotFoundException pnfe) {
			System.out.println(pnfe);
		}catch(HistoriquePrixNotFoundException hpnofe) {
			System.out.println(hpnofe);
		}catch(InsertionException ie) {
			System.out.println(ie);
		}
		catch(ClientNotFoundException cnfe) {
			System.out.println(cnfe);
		}catch(VenteNotFoundException vnfe) {
			System.out.println(vnfe);
		}catch(StockInvalidException sie) {
			System.out.println(sie);
		}catch(IdVenteDetailException ivde){
			System.out.println(ivde);
		}catch(CommandeNotFoundException cnfe) {
			System.out.println(cnfe);
		}
		System.out.println("ok");
		
	}
}
