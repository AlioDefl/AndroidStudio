package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  LOGIN ACTIVITY - Ecran de connexion                          ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Concepts demontres :                                         ║
 * ║  - Cycle de vie complet (onCreate → onDestroy)               ║
 * ║  - findViewById (methode classique)                          ║
 * ║  - android:onClick dans le XML                               ║
 * ║  - Intent EXPLICITE avec putExtra (passer des donnees)       ║
 * ║  - Intent IMPLICITE (ouvrir un lien web)                     ║
 * ║  - Toast (message bref)                                      ║
 * ║  - Stockage dans Application                                ║
 * ║  - Log (Logcat)                                              ║
 * ║                                                               ║
 * ║  LIEN : declaree dans AndroidManifest.xml comme LAUNCHER     ║
 * ║  LAYOUT : res/layout/activity_login.xml                      ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // TAG pour Logcat : permet de filtrer les messages dans la console
    // Commande : adb logcat -s DemoCTP
    private static final String TAG = "DemoCTP";

    // Constante pour la cle de l'extra (bonne pratique)
    // Convention : package.NOMCLE
    public static final String EXTRA_LOGIN = "fr.iutlille.democtp.LOGIN";

    // ════════════════════════════════════════════
    //  CYCLE DE VIE - Chaque methode est appelee
    //  automatiquement par Android a un moment precis
    // ════════════════════════════════════════════

    /**
     * onCreate = PREMIER appel. On fait TOUT ici :
     * - Lier le layout XML (setContentView)
     * - Recuperer les vues (findViewById ou ViewBinding)
     * - Initialiser les donnees
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // TOUJOURS appeler super en premier
        Log.i(TAG, "LoginActivity → onCreate()");

        // Lie cette Activity au fichier XML res/layout/activity_login.xml
        // R.layout.activity_login → genere automatiquement par Android (classe R)
        setContentView(R.layout.activity_login);

        // --- Sauvegarde d'etat : restauration apres rotation d'ecran ---
        if (savedInstanceState != null) {
            String loginSauve = savedInstanceState.getString("login_sauve");
            if (loginSauve != null) {
                EditText editLogin = findViewById(R.id.editTextLogin);
                editLogin.setText(loginSauve);
                Log.i(TAG, "Etat restaure : " + loginSauve);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "LoginActivity → onStart() : l'activite devient VISIBLE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "LoginActivity → onResume() : l'activite est AU PREMIER PLAN");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "LoginActivity → onPause() : l'activite est PARTIELLEMENT cachee");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "LoginActivity → onStop() : l'activite n'est PLUS VISIBLE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LoginActivity → onDestroy() : l'activite est DETRUITE");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "LoginActivity → onRestart() : retour apres onStop");
    }

    /**
     * Sauvegarde d'etat : appele AVANT que l'activite soit detruite
     * (ex: rotation d'ecran). On sauve les donnees importantes.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editLogin = findViewById(R.id.editTextLogin);
        outState.putString("login_sauve", editLogin.getText().toString());
        Log.i(TAG, "Etat sauvegarde");
    }

    // ════════════════════════════════════════════
    //  GESTION DES CLICS
    // ════════════════════════════════════════════

    /**
     * Appele quand le bouton "Se connecter" est clique.
     * LIEN : dans activity_login.xml → android:onClick="onClickLogin"
     * Signature OBLIGATOIRE : public void nomMethode(View view)
     */
    public void onClickLogin(View view) {
        Log.i(TAG, "Clic sur le bouton Login");

        // --- findViewById : recuperer un widget du layout par son ID ---
        // R.id.editTextLogin → correspond a android:id="@+id/editTextLogin" dans le XML
        EditText editLogin = findViewById(R.id.editTextLogin);
        String login = editLogin.getText().toString();

        // Verification simple
        if (login.isEmpty()) {
            // TOAST : petit message temporaire en bas de l'ecran
            Toast.makeText(this, "Entrez un login !", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Stocker le login dans l'Application (accessible partout) ---
        DemoApplication app = (DemoApplication) getApplicationContext();
        app.setLoginUtilisateur(login);

        // --- INTENT EXPLICITE : on sait EXACTEMENT quelle Activity lancer ---
        // Intent(contexte, ClasseCible.class)
        Intent intent = new Intent(LoginActivity.this, ListeActivity.class);

        // putExtra : attacher des donnees a l'intent (cle, valeur)
        intent.putExtra(EXTRA_LOGIN, login);

        // Lancer l'activite cible
        startActivity(intent);
    }

    /**
     * Ouvre le site de l'IUT dans le navigateur.
     * LIEN : dans activity_login.xml → android:onClick="onClickSiteWeb"
     */
    public void onClickSiteWeb(View view) {
        Log.i(TAG, "Clic sur le lien Site Web");

        // --- INTENT IMPLICITE : on sait quelle ACTION faire, pas quelle app ---
        // ACTION_VIEW = "affiche-moi ca" → Android choisit l'app (navigateur)
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iut.univ-lille.fr"));
        startActivity(intent);
    }
}
