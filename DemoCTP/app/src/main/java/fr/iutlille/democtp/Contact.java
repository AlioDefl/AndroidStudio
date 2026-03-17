package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  MODELE (POJO) - Represente UNE donnee de l'app              ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  C'est le "M" de MVC (Modele-Vue-Controleur)                ║
 * ║                                                               ║
 * ║  Regle : que des champs prives + getters/setters             ║
 * ║  Pas de logique Android ici, juste les donnees               ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import androidx.annotation.DrawableRes;

public class Contact {

    // --- Champs prives ---
    private String nom;
    private String telephone;
    private String email;
    private @DrawableRes int idImage;
    // ↑ @DrawableRes = annotation qui dit "cet int est un ID de drawable"
    //   aide Android Studio a verifier qu'on passe bien une image

    // --- Constructeur par defaut (utile pour creer un contact vide) ---
    public Contact() {
        this.nom = "Inconnu";
        this.telephone = "";
        this.email = "";
        this.idImage = android.R.drawable.ic_menu_myplaces; // icone par defaut Android
    }

    // --- Constructeur complet ---
    public Contact(String nom, String telephone, String email, @DrawableRes int idImage) {
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.idImage = idImage;
    }

    // --- Getters ---
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    @DrawableRes
    public int getIdImage() { return idImage; }

    // --- Setters ---
    public void setNom(String nom) { this.nom = nom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setIdImage(@DrawableRes int idImage) { this.idImage = idImage; }

    // --- toString : utile pour le debug (Log.i) ---
    @Override
    public String toString() {
        return nom + " (" + telephone + ")";
    }
}
