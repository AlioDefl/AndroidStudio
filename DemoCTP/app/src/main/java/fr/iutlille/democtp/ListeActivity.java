package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  LISTE ACTIVITY - Ecran principal avec RecyclerView           ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Concepts demontres :                                         ║
 * ║  - View Binding (methode moderne, pas de findViewById)       ║
 * ║  - RecyclerView + Adapter + LayoutManager                    ║
 * ║  - Recevoir des extras d'un Intent                           ║
 * ║  - implements OnClickListener (gerer plusieurs boutons)      ║
 * ║  - implements OnItemClickListener (clics sur la liste)       ║
 * ║  - Menu options (barre d'action)                             ║
 * ║  - AlertDialog (confirmation + saisie)                       ║
 * ║  - Toast + notify... (mise a jour de la liste)               ║
 * ║  - Lancer une autre Activity avec des donnees                ║
 * ║                                                               ║
 * ║  LIENS :                                                     ║
 * ║  - Lancee par LoginActivity via Intent explicite             ║
 * ║  - Layout : res/layout/activity_liste.xml                    ║
 * ║  - Menu : res/menu/main_menu.xml                             ║
 * ║  - Utilise ContactAdapter + ContactViewHolder                ║
 * ║  - Donnees viennent de DemoApplication                       ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fr.iutlille.democtp.databinding.ActivityListeBinding;

//                                         ↓ Gere les clics sur les boutons     ↓ Gere les clics sur les items de la liste
public class ListeActivity extends AppCompatActivity implements View.OnClickListener, ContactAdapter.OnItemClickListener {

    private static final String TAG = "DemoCTP";

    // View Binding : remplace tous les findViewById
    // Le nom de la classe = nom du layout en PascalCase + "Binding"
    // activity_liste.xml → ActivityListeBinding
    private ActivityListeBinding binding;

    private List<Contact> liste;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ListeActivity → onCreate()");

        // ════════════════════════════════════════════
        //  VIEW BINDING : l'alternative moderne a findViewById
        // ════════════════════════════════════════════
        // Etape 1 : inflate le binding
        binding = ActivityListeBinding.inflate(getLayoutInflater());
        // Etape 2 : setContentView avec la racine du binding (PAS R.layout.xxx)
        setContentView(binding.getRoot());
        // Maintenant : binding.textViewBienvenue, binding.recyclerContacts, etc.
        // Les IDs du XML deviennent des champs en camelCase

        // Configurer la Toolbar comme barre d'action (pour les menus)
        setSupportActionBar(binding.toolbar);

        // ════════════════════════════════════════════
        //  RECUPERER LES EXTRAS DE L'INTENT
        // ════════════════════════════════════════════
        // L'intent qui a lance cette Activity contient des donnees
        Intent intent = getIntent();
        String login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN);
        // ↑ Utilise la constante de LoginActivity pour la cle

        // On peut aussi recuperer depuis l'Application
        // DemoApplication app = (DemoApplication) getApplicationContext();
        // String login = app.getLoginUtilisateur();

        // Afficher le message de bienvenue
        binding.textViewBienvenue.setText("Bienvenue " + login + " !");

        // ════════════════════════════════════════════
        //  RECYCLERVIEW : les 3 etapes obligatoires
        // ════════════════════════════════════════════

        // Etape 1 : Recuperer les donnees depuis l'Application (DAO)
        DemoApplication app = (DemoApplication) getApplicationContext();
        liste = app.getListeContacts();

        // Etape 2 : Creer l'adapter et lui donner la liste + le listener
        adapter = new ContactAdapter(liste);
        adapter.setOnItemClickListener(this);  // this = cette Activity implemente OnItemClickListener

        // Etape 3 : Configurer le RecyclerView
        binding.recyclerContacts.setAdapter(adapter);           // brancher l'adapter
        binding.recyclerContacts.setHasFixedSize(true);         // optimisation
        binding.recyclerContacts.setLayoutManager(              // OBLIGATOIRE sinon rien ne s'affiche !
                new LinearLayoutManager(this));                 // liste verticale

        // ALTERNATIVES de LayoutManager :
        // new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)  → horizontal
        // new GridLayoutManager(this, 2)                                         → grille 2 colonnes

        // ════════════════════════════════════════════
        //  CLICS SUR LES BOUTONS (implements OnClickListener)
        // ════════════════════════════════════════════
        binding.btnDeconnexion.setOnClickListener(this);
        // ↑ "this" car l'Activity implemente View.OnClickListener
    }

    /**
     * Gestion des clics sur les boutons (Facon 2 : implements OnClickListener)
     * On compare l'ID de la vue cliquee pour savoir quel bouton c'est
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDeconnexion) {
            Log.i(TAG, "Clic sur Deconnexion");
            // finish() ferme cette Activity et revient a la precedente (LoginActivity)
            finish();
        }
    }

    /**
     * Clic sur un item de la liste (vient du ViewHolder via l'Adapter)
     * On recoit la POSITION de l'item clique dans la liste
     */
    @Override
    public void onItemClick(int position) {
        Contact contact = liste.get(position);
        Log.i(TAG, "Clic sur : " + contact.getNom());

        // Naviguer vers l'ecran de detail
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }

    // ════════════════════════════════════════════
    //  MENU OPTIONS (barre d'action)
    // ════════════════════════════════════════════

    /**
     * Gonfler (inflate) le menu : rend les items du menu visibles
     * LIEN : res/menu/main_menu.xml
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true; // true = afficher le menu
    }

    /**
     * Reagir quand un item du menu est clique
     * Comparer item.getItemId() avec les IDs definis dans le XML du menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_ajouter) {
            // ════════════════════════════════════════════
            //  ALERTDIALOG avec champ de saisie
            // ════════════════════════════════════════════
            afficherDialogAjout();
            return true;

        } else if (item.getItemId() == R.id.action_reinitialiser) {
            // ════════════════════════════════════════════
            //  ALERTDIALOG de confirmation
            // ════════════════════════════════════════════
            new AlertDialog.Builder(this)
                    .setTitle("Reinitialiser")
                    .setMessage("Remettre la liste d'origine ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Reinitialiser via l'Application
                        DemoApplication app = (DemoApplication) getApplicationContext();
                        app.initListe();
                        // NOTIFY : TOUT a change → notifyDataSetChanged
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Liste reinitalisee", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Non", null) // null = fermer sans rien faire
                    .show();
            return true;

        } else if (item.getItemId() == R.id.action_grille) {
            // Changer le LayoutManager en grille
            binding.recyclerContacts.setLayoutManager(new GridLayoutManager(this, 2));
            return true;

        } else if (item.getItemId() == R.id.action_liste) {
            // Revenir en mode liste
            binding.recyclerContacts.setLayoutManager(new LinearLayoutManager(this));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * AlertDialog avec un layout custom (plusieurs champs de saisie)
     */
    private void afficherDialogAjout() {
        // Creer les champs de saisie programmatiquement
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        EditText editNom = new EditText(this);
        editNom.setHint("Nom");
        layout.addView(editNom);

        EditText editTel = new EditText(this);
        editTel.setHint("Telephone");
        layout.addView(editTel);

        EditText editEmail = new EditText(this);
        editEmail.setHint("Email");
        layout.addView(editEmail);

        new AlertDialog.Builder(this)
                .setTitle("Ajouter un contact")
                .setView(layout)  // layout custom au lieu d'un simple message
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String nom = editNom.getText().toString();
                    String tel = editTel.getText().toString();
                    String email = editEmail.getText().toString();

                    if (!nom.isEmpty()) {
                        // Ajouter a la liste
                        Contact nouveau = new Contact(nom, tel, email,
                                android.R.drawable.ic_menu_myplaces);
                        liste.add(nouveau);

                        // NOTIFY : un item a ete ajoute a la fin
                        adapter.notifyItemInserted(liste.size() - 1);

                        // Scroller vers le nouvel element
                        binding.recyclerContacts.scrollToPosition(liste.size() - 1);

                        Toast.makeText(this, nom + " ajoute !", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
