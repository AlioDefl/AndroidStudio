/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║          SNIPPETS CTP - COPIER/COLLER DANS ANDROID STUDIO       ║
 * ║          Ctrl+F pour chercher un snippet rapidement              ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * SOMMAIRE :
 * - [MODELE]        Classe POJO (ligne ~30)
 * - [APPLICATION]   Classe Application / DAO (ligne ~90)
 * - [ACTIVITY]      Activity de base + cycle de vie (ligne ~150)
 * - [VIEWBINDING]   Activity avec View Binding (ligne ~220)
 * - [INTENT]        Navigation / Intent explicite + implicite (ligne ~270)
 * - [INTENT_RETOUR] Intent avec retour ActivityResultLauncher (ligne ~320)
 * - [ONCLICK]       3 facons de gerer les clics (ligne ~370)
 * - [RECYCLERVIEW]  Adapter + ViewHolder + Activity (ligne ~420)
 * - [MENU]          Menu options + menu contextuel (ligne ~580)
 * - [DIALOG]        AlertDialog (confirmation, saisie, liste) (ligne ~660)
 * - [TOAST]         Toast + Snackbar (ligne ~730)
 * - [FRAGMENT]      Fragment + communication (ligne ~760)
 * - [MANIFEST]      AndroidManifest.xml complet (ligne ~900)
 * - [LAYOUTS]       Layouts XML prets a coller (ligne ~950)
 * - [PERMISSIONS]   Demande de permissions runtime (ligne ~1100)
 */

// ╔══════════════════════════════════════╗
// ║  [MODELE] - CLASSE POJO             ║
// ╚══════════════════════════════════════╝

package fr.iutlille.monapp;

import androidx.annotation.DrawableRes;

public class Item {
    private String nom;
    private String description;
    private @DrawableRes int idImage;

    // Constructeur par defaut
    public Item() {
        this.nom = "inconnu";
        this.description = "";
        this.idImage = R.drawable.ic_launcher_foreground;
    }

    // Constructeur complet
    public Item(String nom, String description, @DrawableRes int idImage) {
        this.nom = nom;
        this.description = description;
        this.idImage = idImage;
    }

    // Getters
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    @DrawableRes public int getIdImage() { return idImage; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setIdImage(@DrawableRes int idImage) { this.idImage = idImage; }

    @Override
    public String toString() { return nom; }
}


// ╔══════════════════════════════════════╗
// ║  [APPLICATION] - CLASSE APPLICATION  ║
// ║  (DAO / stockage de donnees)         ║
// ╚══════════════════════════════════════╝
// MANIFEST : <application android:name=".MonApplication" ...>

package fr.iutlille.monapp;

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;
import java.util.ArrayList;
import java.util.List;

public class MonApplication extends Application {
    private final List<Item> liste = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initListe();
    }

    public void initListe() {
        liste.clear();
        Resources res = getResources();

        String[] noms = res.getStringArray(R.array.noms);
        String[] descriptions = res.getStringArray(R.array.descriptions);
        TypedArray images = res.obtainTypedArray(R.array.idimages);

        for (int i = 0; i < noms.length; i++) {
            int idImage = images.getResourceId(i, R.drawable.ic_launcher_foreground);
            liste.add(new Item(noms[i], descriptions[i], idImage));
        }
        images.recycle(); // IMPORTANT : toujours liberer le TypedArray
    }

    public List<Item> getListe() {
        return liste;
    }

    // --- Version simple (sans ressources XML) ---
    // Si les donnees sont codees en dur :
    public void initListeSimple() {
        liste.clear();
        liste.add(new Item("Alice", "Premiere", R.drawable.img1));
        liste.add(new Item("Bob", "Deuxieme", R.drawable.img2));
    }

    // --- Stocker une valeur simple (ex: login) ---
    private String login;
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}


// ╔══════════════════════════════════════╗
// ║  [ACTIVITY] - ACTIVITY DE BASE      ║
// ║  avec cycle de vie complet           ║
// ╚══════════════════════════════════════╝

package fr.iutlille.monapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MonApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gestion des barres systeme (copier tel quel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Acces aux vues avec findViewById ---
        TextView label = findViewById(R.id.textViewLabel);
        EditText input = findViewById(R.id.editTextSaisie);
        Button btn = findViewById(R.id.btnValider);

        // Lire/ecrire
        String texte = input.getText().toString();
        label.setText("Bonjour " + texte);
    }

    // Methode appelee par android:onClick="onClickBouton" dans le XML
    public void onClickBouton(View view) {
        Log.i(TAG, "Clic sur le bouton");
    }

    // --- Cycle de vie complet ---
    @Override protected void onStart()   { super.onStart();   Log.i(TAG, "onStart()"); }
    @Override protected void onResume()  { super.onResume();  Log.i(TAG, "onResume()"); }
    @Override protected void onPause()   { super.onPause();   Log.i(TAG, "onPause()"); }
    @Override protected void onStop()    { super.onStop();    Log.i(TAG, "onStop()"); }
    @Override protected void onDestroy() { super.onDestroy(); Log.i(TAG, "onDestroy()"); }
    @Override protected void onRestart() { super.onRestart(); Log.i(TAG, "onRestart()"); }

    // --- Sauvegarde d'etat (rotation ecran) ---
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("maClef", "maValeur");
        outState.putInt("monScore", 42);
    }
    // Restauration : dans onCreate(), verifier if (savedInstanceState != null)
}


// ╔══════════════════════════════════════╗
// ║  [VIEWBINDING] - ACTIVITY AVEC      ║
// ║  VIEW BINDING                        ║
// ╚══════════════════════════════════════╝
// build.gradle.kts : android { buildFeatures { viewBinding = true } }
// activity_main.xml → ActivityMainBinding

package fr.iutlille.monapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fr.iutlille.monapp.databinding.ActivityMainBinding;

public class MainActivityBinding extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate le binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Acces direct aux vues (id dans le XML → camelCase)
        // android:id="@+id/text_view_nom" → binding.textViewNom
        // android:id="@+id/editTextLogin" → binding.editTextLogin
        // android:id="@+id/btnValider" → binding.btnValider

        binding.textViewNom.setText("Jean");
        String saisie = binding.editTextLogin.getText().toString();

        binding.btnValider.setOnClickListener(v -> {
            // Action au clic
        });
    }
}


// ╔══════════════════════════════════════╗
// ║  [INTENT] - NAVIGATION              ║
// ╚══════════════════════════════════════╝

package fr.iutlille.monapp;

import android.content.Intent;
import android.net.Uri;

// --- Dans l'activite source ---

// INTENT EXPLICITE : lancer une activite precise
public void lancerDetails() {
    Intent intent = new Intent(this, DetailsActivity.class);

    // Passer des donnees
    intent.putExtra("CLE_NOM", "Alice");
    intent.putExtra("CLE_AGE", 25);
    intent.putExtra("CLE_ACTIF", true);

    startActivity(intent);
}

// --- Dans l'activite cible ---
// Recuperer les donnees dans onCreate()
public void recupererExtras() {
    Intent intent = getIntent();
    String nom = intent.getStringExtra("CLE_NOM");          // null si absent
    int age = intent.getIntExtra("CLE_AGE", 0);             // 0 = defaut
    boolean actif = intent.getBooleanExtra("CLE_ACTIF", false);
}

// INTENT IMPLICITE : demander une action au systeme
public void ouvrirWeb() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
    startActivity(intent);
}

public void envoyerEmail() {
    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:test@mail.com"));
    intent.putExtra(Intent.EXTRA_SUBJECT, "Sujet du mail");
    startActivity(intent);
}

public void appeler() {
    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0601020304"));
    startActivity(intent);
}

// FERMER l'activite courante
public void fermer() {
    finish(); // Revient a l'activite precedente dans la pile
}


// ╔══════════════════════════════════════╗
// ║  [INTENT_RETOUR] - AVEC RESULTAT    ║
// ╚══════════════════════════════════════╝

package fr.iutlille.monapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// --- ACTIVITE APPELANTE ---
public class AppelantActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> monLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appelant);

        // 1. Enregistrer le launcher
        monLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // 3. Traiter le resultat au retour
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String retour = result.getData().getStringExtra("CLE_RETOUR");
                    // Utiliser le resultat
                }
            }
        );
    }

    public void lancerPourResultat() {
        // 2. Lancer l'activite
        Intent intent = new Intent(this, AppeleeActivity.class);
        monLauncher.launch(intent);
    }
}

// --- ACTIVITE APPELEE ---
public class AppeleeActivity extends AppCompatActivity {

    public void renvoyerResultat() {
        Intent retour = new Intent();
        retour.putExtra("CLE_RETOUR", "valeur de retour");
        setResult(RESULT_OK, retour);
        finish(); // IMPORTANT : fermer pour renvoyer le resultat
    }

    public void annuler() {
        setResult(RESULT_CANCELED);
        finish();
    }
}


// ╔══════════════════════════════════════╗
// ║  [ONCLICK] - 3 FACONS DE CLIQUER    ║
// ╚══════════════════════════════════════╝

// === FACON 1 : android:onClick dans le XML ===
// XML : <Button android:onClick="onClickBouton" />
public void onClickBouton(View view) {
    // Signature EXACTE : public void xxx(View view)
}

// === FACON 2 : implements View.OnClickListener ===
public class MonActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn1) {
            // Action 1
        } else if (id == R.id.btn2) {
            // Action 2
        }
    }
}

// === FACON 3 : Lambda / Classe anonyme ===
// Lambda (court)
binding.btn.setOnClickListener(v -> {
    // Action
});

// Classe anonyme (long)
binding.btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Action
    }
});


// ╔══════════════════════════════════════╗
// ║  [RECYCLERVIEW] - ADAPTER +         ║
// ║  VIEWHOLDER + ACTIVITY              ║
// ╚══════════════════════════════════════╝

// === 1. VIEWHOLDER ===
package fr.iutlille.monapp;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import fr.iutlille.monapp.databinding.ItemLayoutBinding;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ItemLayoutBinding ui;
    private ItemAdapter.OnItemClickListener listener;

    public ItemViewHolder(ItemLayoutBinding ui) {
        super(ui.getRoot());
        this.ui = ui;
        itemView.setOnClickListener(this);
    }

    public void setItem(Item item) {
        ui.textViewNom.setText(item.getNom());
        ui.textViewDescription.setText(item.getDescription());
        ui.imageView.setImageResource(item.getIdImage());
    }

    public void setOnItemClickListener(ItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}

// === 2. ADAPTER ===
package fr.iutlille.monapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import fr.iutlille.monapp.databinding.ItemLayoutBinding;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    // Interface pour les clics sur les items
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final List<Item> liste;
    private OnItemClickListener listener;

    public ItemAdapter(List<Item> liste) {
        this.liste = liste;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout d'un item
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Remplir le ViewHolder avec les donnees
        Item item = liste.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }
}

// === 3. ACTIVITY AVEC RECYCLERVIEW ===
package fr.iutlille.monapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import java.util.List;
import fr.iutlille.monapp.databinding.ActivityMainBinding;

public class ListeActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private ActivityMainBinding binding;
    private List<Item> liste;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Recuperer les donnees
        MonApplication app = (MonApplication) getApplicationContext();
        liste = app.getListe();

        // 2. Creer l'adapter
        adapter = new ItemAdapter(liste);
        adapter.setOnItemClickListener(this);

        // 3. Configurer le RecyclerView
        binding.recycler.setAdapter(adapter);
        binding.recycler.setHasFixedSize(true);

        // 4. Choisir le LayoutManager
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        // OU grille : new GridLayoutManager(this, 2)
        // OU horizontal : new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    @Override
    public void onItemClick(int position) {
        Item item = liste.get(position);
        // --- Exemples d'actions ---

        // Modifier l'item
        item.setNom("Modifie");
        adapter.notifyItemChanged(position);

        // Supprimer l'item
        // liste.remove(position);
        // adapter.notifyItemRemoved(position);

        // Naviguer vers un detail
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra("POSITION", position);
        // startActivity(intent);
    }

    // Ajouter un item
    public void ajouterItem(String nom, String desc) {
        Item nouveau = new Item(nom, desc, R.drawable.ic_launcher_foreground);
        liste.add(nouveau);
        adapter.notifyItemInserted(liste.size() - 1);
        binding.recycler.scrollToPosition(liste.size() - 1);
    }

    // Supprimer un item
    public void supprimerItem(int position) {
        liste.remove(position);
        adapter.notifyItemRemoved(position);
    }

    // Reinitialiser toute la liste
    public void reinitialiserListe() {
        MonApplication app = (MonApplication) getApplicationContext();
        app.initListe();
        adapter.notifyDataSetChanged();
    }
}


// ╔══════════════════════════════════════╗
// ║  [MENU] - BARRE D'ACTION + MENUS    ║
// ╚══════════════════════════════════════╝

// Dans n'importe quelle Activity :

import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;

// 1. Gonfler le menu (le rendre visible)
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
}

// 2. Reagir aux clics sur les items du menu
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_ajouter) {
        // Ajouter un element
        return true;
    } else if (item.getItemId() == R.id.action_reinit) {
        // Reinitialiser
        MonApplication app = (MonApplication) getApplicationContext();
        app.initListe();
        adapter.notifyDataSetChanged();
        return true;
    } else if (item.getItemId() == R.id.action_settings) {
        // Parametres
        return true;
    }
    return super.onOptionsItemSelected(item);
}

// === MENU CONTEXTUEL (appui long) ===

// Dans onCreate() :
registerForContextMenu(maVue); // ou binding.recycler

@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    getMenuInflater().inflate(R.menu.context_menu, menu);
    menu.setHeaderTitle("Actions");
}

@Override
public boolean onContextItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_supprimer) {
        // Supprimer
        return true;
    } else if (item.getItemId() == R.id.action_editer) {
        // Editer
        return true;
    }
    return super.onContextItemSelected(item);
}

// === POPUP MENU ===
PopupMenu popup = new PopupMenu(this, anchorView);
popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
popup.setOnMenuItemClickListener(menuItem -> {
    if (menuItem.getItemId() == R.id.action_copier) {
        return true;
    }
    return false;
});
popup.show();


// ╔══════════════════════════════════════╗
// ║  [DIALOG] - ALERTDIALOG             ║
// ╚══════════════════════════════════════╝

import android.app.AlertDialog;
import android.widget.EditText;

// === Dialog de confirmation ===
new AlertDialog.Builder(this)
    .setTitle("Confirmation")
    .setMessage("Voulez-vous supprimer ?")
    .setIcon(android.R.drawable.ic_dialog_alert)
    .setPositiveButton("Oui", (dialog, which) -> {
        // Confirmer : supprimer
        liste.remove(position);
        adapter.notifyItemRemoved(position);
    })
    .setNegativeButton("Non", (dialog, which) -> {
        dialog.dismiss();
    })
    .show();

// === Dialog avec champ de saisie ===
EditText input = new EditText(this);
input.setHint("Nom du personnage");

new AlertDialog.Builder(this)
    .setTitle("Ajouter un element")
    .setView(input)
    .setPositiveButton("Ajouter", (dialog, which) -> {
        String nom = input.getText().toString();
        if (!nom.isEmpty()) {
            liste.add(new Item(nom, "Nouveau", R.drawable.ic_launcher_foreground));
            adapter.notifyItemInserted(liste.size() - 1);
        }
    })
    .setNegativeButton("Annuler", null)
    .show();

// === Dialog avec layout custom (plusieurs champs) ===
View dialogView = getLayoutInflater().inflate(R.layout.dialog_ajout, null);
EditText editNom = dialogView.findViewById(R.id.editNom);
EditText editDesc = dialogView.findViewById(R.id.editDesc);

new AlertDialog.Builder(this)
    .setTitle("Ajouter")
    .setView(dialogView)
    .setPositiveButton("OK", (dialog, which) -> {
        String nom = editNom.getText().toString();
        String desc = editDesc.getText().toString();
        // Ajouter
    })
    .setNegativeButton("Annuler", null)
    .show();

// === Dialog avec liste de choix ===
String[] options = {"Supprimer", "Editer", "Partager"};
new AlertDialog.Builder(this)
    .setTitle("Action")
    .setItems(options, (dialog, which) -> {
        // which = index (0=Supprimer, 1=Editer, 2=Partager)
    })
    .show();


// ╔══════════════════════════════════════╗
// ║  [TOAST] - TOAST + SNACKBAR         ║
// ╚══════════════════════════════════════╝

import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

// Toast court
Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();

// Toast long
Toast.makeText(this, "Message long", Toast.LENGTH_LONG).show();

// Snackbar avec action
Snackbar.make(binding.getRoot(), "Element supprime", Snackbar.LENGTH_LONG)
    .setAction("ANNULER", v -> {
        // Annuler
    })
    .show();


// ╔══════════════════════════════════════╗
// ║  [FRAGMENT] - FRAGMENT +             ║
// ║  COMMUNICATION                       ║
// ╚══════════════════════════════════════╝

// === LE FRAGMENT ===
package fr.iutlille.monapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import fr.iutlille.monapp.databinding.FragmentListeBinding;

public class ListeFragment extends Fragment {

    // Interface de callback pour communiquer avec l'Activity
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private FragmentListeBinding binding;
    private OnItemSelectedListener callback;

    // Constructeur vide OBLIGATOIRE
    public ListeFragment() {}

    // Recuperer le callback quand le fragment est attache a l'Activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            callback = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context + " doit implementer OnItemSelectedListener");
        }
    }

    // Inflate le layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Configurer les vues (equivalent de onCreate pour une Activity)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Exemple : configurer un RecyclerView dans le fragment
        // MonApplication app = (MonApplication) requireActivity().getApplicationContext();
        // List<Item> liste = app.getListe();
        // ...

        // Exemple : notifier l'Activity quand un item est clique
        // callback.onItemSelected(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Eviter les fuites memoire
    }
}

// === FRAGMENT DETAIL (recoit des arguments) ===
public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;

    public DetailFragment() {}

    // Factory method pour creer avec des arguments
    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperer les arguments
        if (getArguments() != null) {
            int position = getArguments().getInt("position");
            // Charger et afficher les details
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

// === ACTIVITY QUI GERE LES FRAGMENTS ===
public class MainActivity extends AppCompatActivity
        implements ListeFragment.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le premier fragment (seulement a la creation)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new ListeFragment())
                .commit();
        }
    }

    // Callback quand un item est selectionne dans ListeFragment
    @Override
    public void onItemSelected(int position) {
        // Remplacer par le fragment detail
        DetailFragment detail = DetailFragment.newInstance(position);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, detail)
            .addToBackStack(null)   // Permet de revenir en arriere
            .commit();
    }
}


// ╔══════════════════════════════════════╗
// ║  [MANIFEST] - ANDROIDMANIFEST.XML   ║
// ╚══════════════════════════════════════╝

/*
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- PERMISSIONS -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".MonApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MonApp">

        <!-- Activite principale (LAUNCHER) -->
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Autres activites -->
        <activity android:name=".DetailsActivity" android:exported="false" />
        <activity android:name=".ListeActivity" android:exported="false" />
    </application>
</manifest>
*/


// ╔══════════════════════════════════════╗
// ║  [LAYOUTS] - XML PRETS A COLLER     ║
// ╚══════════════════════════════════════╝

// === activity_main.xml (avec RecyclerView) ===
/*
<?xml version="1.0" encoding="utf-8"?>
<androidx.recyclerview.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/recycler"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
*/

// === activity_login.xml ===
/*
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="16dp">

    <TextView
        android:id="@+id/textViewTitre"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Connexion"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginTop="32dp" />

    <EditText
        android:id="@+id/editTextLogin"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Login"
        android:inputType="text"
        android:layout_marginTop="16dp"
        android:layout_marginStart="32dp"
        android:layout_marginEnd="32dp" />

    <EditText
        android:id="@+id/editTextPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Mot de passe"
        android:inputType="textPassword"
        android:layout_marginTop="8dp"
        android:layout_marginStart="32dp"
        android:layout_marginEnd="32dp" />

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Se connecter"
        android:layout_marginTop="16dp" />
</LinearLayout>
*/

// === item_layout.xml (pour un item du RecyclerView) ===
/*
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="12dp"
    android:background="?android:attr/selectableItemBackground">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:scaleType="centerCrop"
        android:contentDescription="Image" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginStart="12dp">

        <TextView
            android:id="@+id/textViewNom"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textSize="18sp"
            android:textColor="#000000"
            android:text="Nom" />

        <TextView
            android:id="@+id/textViewDescription"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="13sp"
            android:maxLines="2"
            android:ellipsize="end"
            android:layout_marginTop="4dp"
            android:text="Description" />
    </LinearLayout>
</LinearLayout>
*/

// === Layout pour Activity avec fragment container ===
/*
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragmentContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
*/

// === Menu XML (res/menu/main_menu.xml) ===
/*
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item android:id="@+id/action_ajouter"
        android:icon="@drawable/ic_action_new"
        android:title="Ajouter"
        app:showAsAction="always" />

    <item android:id="@+id/action_reinit"
        android:icon="@drawable/ic_action_select_all"
        android:title="Reinitialiser"
        app:showAsAction="ifRoom" />

    <item android:id="@+id/action_settings"
        android:title="Parametres"
        app:showAsAction="never" />
</menu>
*/

// === Menu contextuel (res/menu/context_menu.xml) ===
/*
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/action_supprimer"
        android:title="Supprimer" />
    <item android:id="@+id/action_editer"
        android:title="Editer" />
</menu>
*/

// === arrays.xml (res/values/arrays.xml) ===
/*
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="noms">
        <item>Alice</item>
        <item>Bob</item>
        <item>Charlie</item>
    </string-array>
    <string-array name="descriptions">
        <item>Premier personnage</item>
        <item>Deuxieme personnage</item>
        <item>Troisieme personnage</item>
    </string-array>
    <array name="idimages">
        <item>@drawable/img1</item>
        <item>@drawable/img2</item>
        <item>@drawable/img3</item>
    </array>
</resources>
*/


// ╔══════════════════════════════════════╗
// ║  [PERMISSIONS] - RUNTIME            ║
// ╚══════════════════════════════════════╝

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

private static final int REQUEST_CAMERA = 100;

// Verifier et demander une permission
public void demanderPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        // Permission pas encore accordee → demander
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    } else {
        // Permission deja accordee → utiliser la camera
        ouvrirCamera();
    }
}

// Traiter la reponse de l'utilisateur
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_CAMERA) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission ACCORDEE
            ouvrirCamera();
        } else {
            // Permission REFUSEE
            Toast.makeText(this, "Permission refusee", Toast.LENGTH_SHORT).show();
        }
    }
}


// ╔══════════════════════════════════════╗
// ║  [BUILD_GRADLE] - build.gradle.kts  ║
// ╚══════════════════════════════════════╝

/*
android {
    // ...
    buildFeatures {
        viewBinding = true    // Activer View Binding
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.11.0")
    // ... autres dependances
}
*/
