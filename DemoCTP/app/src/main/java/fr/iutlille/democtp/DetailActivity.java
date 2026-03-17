package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  DETAIL ACTIVITY - Affiche les details d'un contact           ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Concepts demontres :                                         ║
 * ║  - Fragment dans une Activity                                ║
 * ║  - Charger un Fragment dynamiquement (FragmentManager)       ║
 * ║  - Passer des donnees a un Fragment (Bundle / arguments)     ║
 * ║  - Communication Fragment → Activity (interface callback)    ║
 * ║  - View Binding dans une Activity                            ║
 * ║  - finish() pour revenir en arriere                          ║
 * ║                                                               ║
 * ║  LIENS :                                                     ║
 * ║  - Lancee par ListeActivity via Intent avec extra "POSITION" ║
 * ║  - Contient un DetailFragment dans un FrameLayout            ║
 * ║  - Layout : res/layout/activity_detail.xml                   ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.iutlille.democtp.databinding.ActivityDetailBinding;

//                                             ↓ Implemente l'interface du Fragment pour recevoir les callbacks
public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentActionListener {

    private static final String TAG = "DemoCTP";
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recuperer la position envoyee par ListeActivity
        int position = getIntent().getIntExtra("POSITION", 0);
        Log.i(TAG, "DetailActivity → position recue : " + position);

        // ════════════════════════════════════════════
        //  CHARGER UN FRAGMENT DYNAMIQUEMENT
        // ════════════════════════════════════════════
        // On ne charge que si c'est la premiere creation
        // (evite de recreer le fragment a chaque rotation d'ecran)
        if (savedInstanceState == null) {

            // Creer le fragment avec la factory method (bonne pratique)
            DetailFragment fragment = DetailFragment.newInstance(position);

            // Utiliser le FragmentManager pour l'inserer dans le conteneur
            getSupportFragmentManager()
                    .beginTransaction()                          // demarrer une transaction
                    .replace(R.id.fragmentContainer, fragment)   // mettre le fragment dans le FrameLayout
                    // .addToBackStack(null)                     // decommenter si on veut "retour" dans les fragments
                    .commit();                                   // executer la transaction
        }
    }

    /**
     * Callback du Fragment : appele quand l'utilisateur clique "Fermer" dans le fragment
     * C'est la COMMUNICATION Fragment → Activity via interface
     */
    @Override
    public void onActionFermer() {
        Log.i(TAG, "Fragment a demande de fermer");
        Toast.makeText(this, "Retour a la liste", Toast.LENGTH_SHORT).show();
        finish(); // Ferme cette Activity → retour a ListeActivity
    }

    /**
     * Callback du Fragment : appele quand l'utilisateur appelle dans le fragment
     */
    @Override
    public void onActionAppeler(String telephone) {
        Log.i(TAG, "Fragment demande d'appeler : " + telephone);
        // Ici on pourrait lancer un Intent implicite ACTION_DIAL
        Toast.makeText(this, "Appel vers " + telephone, Toast.LENGTH_SHORT).show();
    }
}
