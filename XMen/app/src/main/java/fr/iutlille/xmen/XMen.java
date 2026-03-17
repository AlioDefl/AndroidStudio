package fr.iutlille.xmen;

import androidx.annotation.DrawableRes;

public class XMen {
    private String nom;
    private String alias;
    private String confrerie;
    private String description;
    private String pouvoirs;
    private @DrawableRes int idImage;

    public XMen() {
        nom = "inconnu";
        alias = "inconnu";
        confrerie = "inconnu";
        description = "inconnu";
        pouvoirs = "inconnu";
        this.idImage = R.drawable.ic_launcher_foreground;
    }

    public XMen(String nom, String alias, String confrerie, String description, String pouvoirs, @DrawableRes int idImage) {
        this.nom = nom;
        this.alias = alias;
        this.confrerie = confrerie;
        this.description = description;
        this.pouvoirs = pouvoirs;
        this.idImage = idImage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConfrerie() {
        return confrerie;
    }

    public void setConfrerie(String confrerie) {
        this.confrerie = confrerie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPouvoirs() {
        return pouvoirs;
    }

    public void setPouvoirs(String pouvoirs) {
        this.pouvoirs = pouvoirs;
    }

    @DrawableRes
    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(@DrawableRes int idImage) {
        this.idImage = idImage;
    }

    @Override
    public String toString() {
        return nom + " (" + alias + ")";
    }
}
