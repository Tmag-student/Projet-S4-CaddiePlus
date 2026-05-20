package application.entites;

import java.time.LocalDate;
import java.sql.Date;

public class Client {
    private int clientId;
    private String nom;
    private double pointsFidelite;
    private Date dateCreation;
    private Date anniversaire;

    public Client(int clientId, String nom, double pointsFidelite,Date dateCreation,Date anniversaire) {
        this.clientId= clientId;
        this.nom= nom;
        this.pointsFidelite= pointsFidelite;
        this.dateCreation=dateCreation;
        this.anniversaire=anniversaire;
    }
    public Client(String nom, double pointsFidelite,Date dateCreation,Date anniversaire) {
    	this(0,nom,pointsFidelite,dateCreation,anniversaire);
    }
    public Client(int clientId, String nom, double pointsFidelite,Date anniversaire){
    	this(clientId,nom,pointsFidelite,Date.valueOf(LocalDate.now()),anniversaire); //met la date actuelle
    }
    public Client(String nom,Date anniversaire){
    	this(0,nom,0,anniversaire); //le plus simple, celui utilisé nromalement 0 indique nouveau compte dans id
    }
    public Client() {
    	this("Anonyme",null);
    }
    public String getNomClient() {
        return nom;
    }

    public double getPtFidelClient() {
        return pointsFidelite;
    }

    public Date getDateCreaClient() {
        return dateCreation;
    }

    public Date getAnivClient() {
        return anniversaire;
    }
    public int getIdClient() {
        return clientId;
    }
    public void setIdClient(int id ) {
    	clientId = id;
    }

}
