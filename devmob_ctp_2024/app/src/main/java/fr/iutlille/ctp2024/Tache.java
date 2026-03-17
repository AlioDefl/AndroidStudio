package fr.iutlille.ctp2024;

import java.util.Date;

public class Tache {
    private String nom;
    private Date echeance;
    private Status status = Status.AFAIRE;
    private Priorite priorite = Priorite.FAIBLE;
    private String categorie;
    private String description;

    public Tache(String nom, Date echeance, Priorite priorite, String categorie, String description) {
        this.nom = nom;
        this.echeance = echeance;
        this.priorite = priorite;
        this.categorie = categorie;
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getNom() {
        return nom;
    }

    public Date getEcheance() {
        return echeance;
    }

    public Status getStatus() {
        return status;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public String getDescription() {
        return description;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return "----------\nNom: " + nom + "\nÉchéance: " + echeance + "\nPriorité : " + priorite + "\nStatus : " + status + "\nCatégorie : " + categorie + "\nDescription : " + description + "\n";
    }
}
