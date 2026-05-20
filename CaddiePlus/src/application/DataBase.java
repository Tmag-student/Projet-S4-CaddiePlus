package application;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class DataBase {
	public static void initialisation(Connection conn) throws SQLException {
	    Statement stmt = conn.createStatement();
	    stmt.execute("USE CaddiePlusDataBase");

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS fournisseurs (
	            fournisseur_id INT PRIMARY KEY AUTO_INCREMENT,
	            nom VARCHAR(255)
	    		);"""); //Fournisseur

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS clients (
	            client_id INT PRIMARY KEY AUTO_INCREMENT,
	            nom VARCHAR(255),
	            points_fidelite DOUBLE,
	            date_creation DATE,
	            anniversaire DATE
	    		);"""); //Client

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS produits (
	            produit_id INT PRIMARY KEY AUTO_INCREMENT,
	            nom VARCHAR(255),
	            stock INT,
	            prix_vente_courant DOUBLE,
	            fournisseur_id INT NOT NULL,
	            FOREIGN KEY (fournisseur_id)
	            REFERENCES fournisseurs(fournisseur_id)
	            ON DELETE CASCADE
	    		);"""); //produit

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS commandes (
	            commande_id INT PRIMARY KEY AUTO_INCREMENT,
	            date_commande DATE,
	            fournisseur_id INT NOT NULL,
	            cout_total DOUBLE,
	            FOREIGN KEY (fournisseur_id)
	            REFERENCES fournisseurs(fournisseur_id)
	            ON DELETE CASCADE
	    		);"""); // commande

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS ventes (
	            vente_id INT PRIMARY KEY AUTO_INCREMENT,
	            date_vente DATE,
	            client_id INT NOT NULL,
	            cout_total DOUBLE,
	            FOREIGN KEY (client_id)
	            REFERENCES clients(client_id)
	            ON DELETE CASCADE);"""); // vente

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS vente_detail (
	            id INT PRIMARY KEY AUTO_INCREMENT,
	            vente_id INT NOT NULL,
	            produit_id INT NOT NULL,
	            quantite INT NOT NULL,
	            FOREIGN KEY (vente_id)
	            REFERENCES ventes(vente_id)
	            ON DELETE CASCADE,
	            FOREIGN KEY (produit_id)
	            REFERENCES produits(produit_id)
	            ON DELETE CASCADE);"""); // vente detail

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS commande_detail (
	            id INT PRIMARY KEY AUTO_INCREMENT,
	            commande_id INT NOT NULL,
	            produit_id INT NOT NULL,
	            quantite INT NOT NULL,
	            FOREIGN KEY (commande_id)
	            REFERENCES commandes(commande_id)
	            ON DELETE CASCADE,
	            FOREIGN KEY (produit_id)
	            REFERENCES produits(produit_id)
	            ON DELETE CASCADE);"""); // commande detail 

	    stmt.executeUpdate("""
	        CREATE TABLE IF NOT EXISTS historique_prix (
	            hist_prix_id INT PRIMARY KEY AUTO_INCREMENT,
	            date_changement TIMESTAMP,
	            histo_prix DOUBLE,
	            produit_id INT,
	            FOREIGN KEY (produit_id)
	            REFERENCES produits(produit_id)
	            ON DELETE CASCADE);"""); //historique prix

	    stmt.close();
	}

}
