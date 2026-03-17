package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  CLASSE APPLICATION - Le "DAO" / stockage global             ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Extends Application = existe TOUTE la vie de l'app          ║
 * ║  Sert a stocker des donnees partagees entre Activities       ║
 * ║                                                               ║
 * ║  LIEN : declare dans AndroidManifest.xml avec                ║
 * ║         android:name=".DemoApplication"                      ║
 * ║                                                               ║
 * ║  ACCES : depuis n'importe quelle Activity avec               ║
 * ║    DemoApplication app = (DemoApplication) getApplicationContext();  ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

public class DemoApplication extends Application {

    // --- Donnees globales ---
    private final List<Contact> listeContacts = new ArrayList<>();
    private String loginUtilisateur; // stocke le nom de l'utilisateur connecte

    /**
     * onCreate() de Application = appele UNE SEULE FOIS au demarrage de l'app
     * C'est ici qu'on initialise les donnees
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initListe();
    }

    /**
     * Initialise la liste depuis les ressources XML (res/values/arrays.xml)
     * C'est le role du DAO : charger et fournir les donnees
     */
    public void initListe() {
        listeContacts.clear();

        Resources res = getResources();

        // Lire les tableaux depuis arrays.xml via la classe R
        // R.array.noms → pointe vers <string-array name="noms"> dans arrays.xml
        String[] noms = res.getStringArray(R.array.contact_noms);
        String[] telephones = res.getStringArray(R.array.contact_telephones);
        String[] emails = res.getStringArray(R.array.contact_emails);

        // TypedArray = tableau special pour les references de ressources (images)
        TypedArray images = res.obtainTypedArray(R.array.contact_images);

        for (int i = 0; i < noms.length; i++) {
            // getResourceId(index, valeurParDefaut) → recupere l'ID drawable
            int idImage = images.getResourceId(i, android.R.drawable.ic_menu_myplaces);
            listeContacts.add(new Contact(noms[i], telephones[i], emails[i], idImage));
        }

        // IMPORTANT : toujours liberer un TypedArray apres utilisation
        images.recycle();
    }

    // --- Getters/Setters ---
    public List<Contact> getListeContacts() {
        return listeContacts;
    }

    public String getLoginUtilisateur() {
        return loginUtilisateur;
    }

    public void setLoginUtilisateur(String login) {
        this.loginUtilisateur = login;
    }
}
