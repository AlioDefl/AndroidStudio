# FICHE CTP - ANDROID MOBILE (R4A11)
> Ctrl+F pour chercher rapidement un sujet

---

# TABLE DES MATIERES
1. [COURS 1 - Logcat, Commandes, Systeme de fichiers](#1---logcat-commandes-systeme-de-fichiers)
2. [COURS 2 - Composition d'une app, Layouts, Composants UI](#2---composition-dune-app-layouts-composants-ui)
3. [COURS 3 - Intents, Autorisations, Cycle de vie des Activites](#3---intents-autorisations-cycle-de-vie)
4. [COURS 4 - RecyclerView, MVC, Adapter, ViewHolder](#4---recyclerview-mvc-adapter-viewholder)
5. [COURS 5 - Menus, Dialogues, Fragments](#5---menus-dialogues-fragments)
6. [EXERCICES TYPE CTP](#exercices-type-ctp)

---

# 1 - LOGCAT, COMMANDES, SYSTEME DE FICHIERS

## 1.1 Logcat - Messages de debug

Logcat = console de debug Android. Affiche les messages de l'app en temps reel.

### Niveaux de log (du moins au plus critique)
| Methode | Niveau | Usage |
|---------|--------|-------|
| `Log.v(TAG, msg)` | Verbose | Tout detail (dev uniquement) |
| `Log.d(TAG, msg)` | Debug | Info de debug |
| `Log.i(TAG, msg)` | Info | Info generale |
| `Log.w(TAG, msg)` | Warn | Avertissement |
| `Log.e(TAG, msg)` | Error | Erreur |
| `Log.wtf(TAG, msg)` | Assert | Erreur fatale |

### Utilisation dans le code
```java
private static final String TAG = "MonApp";
Log.i(TAG, "onCreate() de l'activite " + getLocalClassName());
Log.e(TAG, "Erreur: " + e.getMessage());
```

### Commandes ADB (Android Debug Bridge)
```bash
# Voir les logs en temps reel
adb logcat

# Filtrer par TAG
adb logcat -s MonApp

# Filtrer par niveau (I=Info, W=Warn, E=Error)
adb logcat *:E

# Effacer les logs
adb logcat -c

# Installer un APK
adb install app.apk

# Lister les appareils connectes
adb devices

# Shell interactif sur l'appareil
adb shell

# Copier un fichier vers l'appareil
adb push fichier_local /sdcard/
# Copier depuis l'appareil
adb pull /sdcard/fichier .
```

## 1.2 Systeme de fichiers Android

```
/ (root)
├── /data/data/<package>/        # Donnees privees de l'app
│   ├── databases/               # Bases SQLite
│   ├── shared_prefs/            # Preferences (XML)
│   ├── files/                   # Fichiers internes
│   └── cache/                   # Cache
├── /sdcard/ (ou /storage/emulated/0/)  # Stockage externe
│   ├── Download/
│   ├── DCIM/
│   └── Documents/
├── /system/                     # Fichiers systeme (lecture seule)
└── /mnt/                        # Points de montage
```

### Stockage interne vs externe
| | Interne | Externe |
|--|---------|---------|
| Acces | Prive a l'app | Accessible par tous |
| Permission | Aucune requise | `READ/WRITE_EXTERNAL_STORAGE` |
| Suppression | Quand app desintallee | Persiste |
| Methode | `getFilesDir()` | `getExternalFilesDir()` |

---

# 2 - COMPOSITION D'UNE APP, LAYOUTS, COMPOSANTS UI

## 2.1 Structure d'un projet Android Studio

```
MonProjet/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml      # Declaration de l'app
│   │   ├── java/fr/iutlille/monapp/ # Code Java
│   │   │   ├── MainActivity.java
│   │   │   └── MonApplication.java
│   │   └── res/                     # Ressources
│   │       ├── layout/              # Fichiers XML d'interface
│   │       │   └── activity_main.xml
│   │       ├── values/
│   │       │   ├── strings.xml      # Chaines de texte
│   │       │   ├── colors.xml       # Couleurs
│   │       │   └── themes.xml       # Themes
│   │       ├── drawable/            # Images, icones
│   │       ├── menu/                # Menus XML
│   │       └── mipmap-*/            # Icones de l'app
│   └── build.gradle.kts            # Dependances
├── gradle/
│   └── libs.versions.toml          # Versions centralisees
└── settings.gradle.kts
```

## 2.2 Composants cles

### AndroidManifest.xml - Declaration de l'app
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:name=".MonApplication"           <!-- Classe Application custom -->
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.MonApp">

        <!-- Activite principale (LAUNCHER = point d'entree) -->
        <activity android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Autres activites -->
        <activity android:name=".DetailsActivity"
            android:exported="false" />
    </application>
</manifest>
```

### La classe R (auto-generee)
- Generee automatiquement par le build
- Contient des ID entiers pour chaque ressource
- `R.layout.activity_main` → pointe vers `res/layout/activity_main.xml`
- `R.id.btnLogin` → pointe vers un widget avec `android:id="@+id/btnLogin"`
- `R.string.app_name` → pointe vers une string dans `strings.xml`
- `R.drawable.img1` → pointe vers une image dans `drawable/`
- `R.array.noms` → pointe vers un tableau dans `arrays.xml`
- `R.menu.main_menu` → pointe vers un fichier menu XML
- **NE JAMAIS MODIFIER MANUELLEMENT**

### Fichiers XML - Ressources
```xml
<!-- res/values/strings.xml -->
<resources>
    <string name="app_name">Mon App</string>
    <string name="welcome">Bienvenue %s</string>  <!-- %s = placeholder -->
</resources>

<!-- res/values/colors.xml -->
<resources>
    <color name="rouge">#FF0000</color>
</resources>

<!-- res/values/arrays.xml (tableaux de donnees) -->
<resources>
    <string-array name="noms">
        <item>Alice</item>
        <item>Bob</item>
    </string-array>
    <array name="images">                      <!-- tableau type (images) -->
        <item>@drawable/img1</item>
        <item>@drawable/img2</item>
    </array>
</resources>
```

## 2.3 Mise en page - Layouts

### Les layouts principaux

#### LinearLayout (le plus simple)
Aligne les elements en ligne (horizontal) ou en colonne (vertical).
```xml
<LinearLayout
    android:layout_width="match_parent"     <!-- OBLIGATOIRE -->
    android:layout_height="match_parent"    <!-- OBLIGATOIRE -->
    android:orientation="vertical"          <!-- vertical ou horizontal -->
    android:gravity="center_horizontal"     <!-- alignement du contenu -->
    android:padding="16dp">
    <!-- enfants empiles verticalement -->
</LinearLayout>
```

#### RelativeLayout
Positionne les elements les uns par rapport aux autres.
```xml
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView android:id="@+id/titre"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true" />

    <Button android:id="@+id/btn"
        android:layout_below="@id/titre"          <!-- en dessous du titre -->
        android:layout_alignParentEnd="true" />    <!-- colle a droite -->

    <EditText
        android:layout_below="@id/titre"
        android:layout_toStartOf="@id/btn" />      <!-- a gauche du bouton -->
</RelativeLayout>
```

#### TableLayout
Disposition en tableau (lignes/colonnes).
```xml
<TableLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:stretchColumns="1">              <!-- colonne 1 etirable -->

    <TableRow>
        <TextView android:text="Nom:" />
        <EditText android:hint="Entrez votre nom" />
    </TableRow>
    <TableRow>
        <TextView android:text="Email:" />
        <EditText android:hint="Entrez votre email" />
    </TableRow>
</TableLayout>
```

#### ConstraintLayout (le plus flexible, Android moderne)
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button android:id="@+id/btn"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

#### FrameLayout
Empile les elements (utile pour les fragments).
```xml
<FrameLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Parametres OBLIGATOIRES pour tout widget
```xml
android:layout_width="..."    <!-- match_parent | wrap_content | 100dp -->
android:layout_height="..."   <!-- match_parent | wrap_content | 100dp -->
```

### Parametres de position courants
| Parametre | Effet |
|-----------|-------|
| `android:layout_margin="16dp"` | Marge exterieure |
| `android:layout_marginTop="8dp"` | Marge en haut |
| `android:padding="12dp"` | Espacement interieur |
| `android:gravity="center"` | Alignement du contenu |
| `android:layout_gravity="center"` | Position dans le parent |
| `android:layout_weight="1"` | Poids (LinearLayout) |

### Unites
- `dp` = density-independent pixels (tailles, marges)
- `sp` = scale-independent pixels (textes uniquement)
- `px` = pixels (a eviter)

## 2.4 Composants d'interface (Widgets)

```xml
<!-- Texte affiche -->
<TextView android:id="@+id/label"
    android:text="Bonjour"
    android:textSize="18sp"
    android:textStyle="bold"
    android:textColor="#000000"
    android:maxLines="2"
    android:ellipsize="end" />

<!-- Champ de saisie -->
<EditText android:id="@+id/input"
    android:hint="Entrez ici"
    android:inputType="text" />         <!-- text|number|textPassword|textEmailAddress -->

<!-- Bouton -->
<Button android:id="@+id/btn"
    android:text="Valider"
    android:onClick="onClickValider" />    <!-- methode dans l'Activity -->

<!-- Image -->
<ImageView android:id="@+id/img"
    android:src="@drawable/photo"
    android:scaleType="centerCrop"         <!-- centerCrop|fitCenter|centerInside -->
    android:contentDescription="Photo" />

<!-- Case a cocher -->
<CheckBox android:id="@+id/cb"
    android:text="J'accepte" />

<!-- Bouton radio (dans un RadioGroup) -->
<RadioGroup android:id="@+id/rg">
    <RadioButton android:id="@+id/rb1" android:text="Option 1" />
    <RadioButton android:id="@+id/rb2" android:text="Option 2" />
</RadioGroup>

<!-- Switch -->
<Switch android:id="@+id/sw"
    android:text="Activer" />

<!-- Spinner (liste deroulante) -->
<Spinner android:id="@+id/spinner"
    android:entries="@array/options" />

<!-- ScrollView (pour contenu defilant) -->
<ScrollView android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout android:orientation="vertical">
        <!-- contenu long -->
    </LinearLayout>
</ScrollView>
```

---

# 3 - INTENTS, AUTORISATIONS, CYCLE DE VIE

## 3.1 Intents - Navigation entre ecrans

### Intent explicite (on sait QUELLE activite lancer)
```java
// Lancer une activite
Intent intent = new Intent(ActivityActuelle.this, ActiviteCible.class);
startActivity(intent);

// Avec des donnees (extras)
Intent intent = new Intent(this, DetailsActivity.class);
intent.putExtra("CLE_LOGIN", login);          // String
intent.putExtra("CLE_AGE", 25);               // int
intent.putExtra("CLE_ACTIF", true);           // boolean
startActivity(intent);

// Recuperer les extras dans l'activite cible
Intent intent = getIntent();
String login = intent.getStringExtra("CLE_LOGIN");
int age = intent.getIntExtra("CLE_AGE", 0);          // 0 = valeur par defaut
boolean actif = intent.getBooleanExtra("CLE_ACTIF", false);
```

### Bonne pratique : constante pour les cles
```java
// Dans l'activite source
public static final String EXTRA_LOGIN = "fr.iutlille.monapp.LOGIN";

// Utilisation
intent.putExtra(EXTRA_LOGIN, login);

// Recuperation dans la cible
String login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN);
```

### Intent implicite (on sait QUELLE ACTION faire, pas quelle app)
```java
// Ouvrir une page web
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
startActivity(intent);

// Envoyer un email
Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:test@mail.com"));
intent.putExtra(Intent.EXTRA_SUBJECT, "Sujet");
startActivity(intent);

// Passer un appel
Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0601020304"));
startActivity(intent);

// Partager du texte
Intent intent = new Intent(Intent.ACTION_SEND);
intent.setType("text/plain");
intent.putExtra(Intent.EXTRA_TEXT, "Regarde ca !");
startActivity(Intent.createChooser(intent, "Partager via"));
```

### Intent avec retour (startActivityForResult / ActivityResultLauncher)

#### Methode moderne (ActivityResultLauncher) - RECOMMANDEE
```java
// 1. Declarer le launcher dans l'activite appelante
private ActivityResultLauncher<Intent> monLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // ...

    // 2. Enregistrer le launcher
    monLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String valeur = result.getData().getStringExtra("CLE_RETOUR");
                // Traiter le resultat
            }
        }
    );
}

// 3. Lancer l'activite
void lancerActivite() {
    Intent intent = new Intent(this, AutreActivity.class);
    monLauncher.launch(intent);
}
```

```java
// Dans l'activite appelee : renvoyer un resultat
Intent retour = new Intent();
retour.putExtra("CLE_RETOUR", "valeur");
setResult(RESULT_OK, retour);
finish();  // Fermer et retourner
```

#### Methode ancienne (deprecated mais peut etre demandee)
```java
// Lancer avec un code de requete
private static final int REQUEST_CODE = 1;
startActivityForResult(intent, REQUEST_CODE);

// Recevoir le resultat
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
        String resultat = data.getStringExtra("CLE");
    }
}
```

### Fin d'activite
```java
finish();  // Ferme l'activite courante et revient a la precedente
```

## 3.2 Autorisations (Permissions)

### Declaration dans AndroidManifest.xml
```xml
<!-- Permissions courantes -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.CALL_PHONE" />
```

### Permissions normales vs dangereuses
| Type | Exemples | Demande a l'utilisateur ? |
|------|----------|---------------------------|
| Normale | `INTERNET`, `BLUETOOTH` | Non, auto-accordee |
| Dangereuse | `CAMERA`, `LOCATION`, `READ_CONTACTS` | Oui, popup a l'execution |

### Demande de permission a l'execution (API 23+)
```java
// Verifier si la permission est accordee
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
    // Demander la permission
    ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
}

// Traiter la reponse
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if (requestCode == REQUEST_CAMERA && results.length > 0
            && results[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission accordee !
    } else {
        // Permission refusee
    }
}
```

## 3.3 Cycle de vie des activites

### Schema du cycle de vie
```
                    ┌──────────────────────┐
                    │    onCreate()         │  ← Activite creee
                    └──────────┬───────────┘
                               ▼
                    ┌──────────────────────┐
                    │    onStart()          │  ← Visible
                    └──────────┬───────────┘
                               ▼
                    ┌──────────────────────┐
           ┌──────>│    onResume()         │  ← Au premier plan (interactive)
           │       └──────────┬───────────┘
           │                  ▼
           │       ┌──────────────────────┐
           │       │    ACTIVITE ACTIVE    │  ← L'utilisateur interagit
           │       └──────────┬───────────┘
           │                  ▼
           │       ┌──────────────────────┐
           │       │    onPause()          │  ← Partiellement masquee
           │       └──────────┬───────────┘
           │                  ▼
           │       ┌──────────────────────┐
           ├───────│    onStop()           │  ← Plus visible
           │       └──────────┬───────────┘
           │                  ▼
  onRestart()      ┌──────────────────────┐
                   │    onDestroy()        │  ← Activite detruite
                   └──────────────────────┘
```

### Quand chaque methode est appelee
| Methode | Quand | Utilisation typique |
|---------|-------|---------------------|
| `onCreate()` | Creation de l'activite | Init UI, bind views, charger donnees |
| `onStart()` | Devient visible | Enregistrer listeners |
| `onResume()` | Au premier plan | Reprendre animations, capteurs |
| `onPause()` | Partiellement cachee | Sauvegarder donnees critiques |
| `onStop()` | Plus visible du tout | Liberer ressources lourdes |
| `onDestroy()` | Destruction | Nettoyage final |
| `onRestart()` | Retour apres onStop | Rafraichir donnees |

### Code complet du cycle de vie
```java
public class MonActivity extends AppCompatActivity {
    private static final String TAG = "MonApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      // TOUJOURS appeler super en premier
        setContentView(R.layout.activity_mon);
        Log.i(TAG, "onCreate()");
    }

    @Override protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }
}
```

### Sauvegarde d'etat (rotation d'ecran, etc.)
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("cle", valeur);
    outState.putInt("score", 42);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mon);
    if (savedInstanceState != null) {
        String valeur = savedInstanceState.getString("cle");
        int score = savedInstanceState.getInt("score");
    }
}
```

## 3.4 Vues & Activites - Acces aux widgets

### Methode 1 : findViewById (classique)
```java
// Dans onCreate()
TextView label = findViewById(R.id.label);
EditText input = findViewById(R.id.editTextNom);
Button btn = findViewById(R.id.btnValider);
ImageView img = findViewById(R.id.imageView);

// Lire/ecrire des valeurs
String texte = input.getText().toString();
label.setText("Bonjour " + texte);
img.setImageResource(R.drawable.photo);
```

### Methode 2 : View Binding (moderne, recommandee)

#### Etape 1 : Activer dans build.gradle.kts (module app)
```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

#### Etape 2 : Utiliser dans l'activite
```java
// Le nom de la classe = nom du layout en PascalCase + "Binding"
// activity_main.xml → ActivityMainBinding
// activity_details.xml → ActivityDetailsBinding

private ActivityMainBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());   // ← binding.getRoot() au lieu de R.layout.xxx

    // Acces direct aux vues (pas de cast, pas de null)
    binding.textViewNom.setText("Jean");
    binding.editTextLogin.getText().toString();
    binding.btnValider.setOnClickListener(v -> { /* ... */ });
}
```

## 3.5 Gestion des clics (3 facons)

### Facon 1 : android:onClick dans le XML
```xml
<Button android:onClick="onClickBouton" />
```
```java
public void onClickBouton(View view) {
    // Action
}
```

### Facon 2 : implements OnClickListener
```java
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
            // Action bouton 1
        } else if (id == R.id.btn2) {
            // Action bouton 2
        }
    }
}
```

### Facon 3 : Classe anonyme / Lambda
```java
// Classe anonyme
binding.btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Action
    }
});

// Lambda (plus court)
binding.btn.setOnClickListener(v -> {
    // Action
});
```

---

# 4 - RECYCLERVIEW, MVC, ADAPTER, VIEWHOLDER

## 4.1 Schema MVC (Modele-Vue-Controleur)

```
┌─────────────┐     notifie      ┌─────────────┐
│   MODELE    │──────────────────>│     VUE     │
│ (donnees)   │<──────────────────│ (affichage) │
│ List<Item>  │     demande       │ RecyclerView│
└──────┬──────┘     donnees       └──────┬──────┘
       │                                  │
       │         ┌─────────────┐          │
       └────────>│ CONTROLEUR  │<─────────┘
    modifie      │ (Activity)  │    actions
    donnees      │  Adapter    │  utilisateur
                 └─────────────┘
```

### En Android :
- **Modele** = classe POJO (ex: `XMen.java`) + liste dans Application
- **Vue** = `RecyclerView` + layouts XML
- **Controleur** = `Activity` + `Adapter`

## 4.2 Le Modele (POJO / Data class)

```java
public class Item {
    private String nom;
    private String description;
    private @DrawableRes int idImage;   // annotation pour les ressources drawable

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

    // Getters & Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @DrawableRes
    public int getIdImage() { return idImage; }
    public void setIdImage(@DrawableRes int idImage) { this.idImage = idImage; }

    @Override
    public String toString() { return nom; }
}
```

## 4.3 Le DAO (Data Access Object) - Classe Application

Stocke et fournit les donnees. Fait office de DAO simple.

```java
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
        images.recycle();  // IMPORTANT : liberer le TypedArray
    }

    public List<Item> getListe() { return liste; }
}
```

**Ne pas oublier** de declarer dans le Manifest :
```xml
<application android:name=".MonApplication" ...>
```

## 4.4 Le ViewHolder

Contient les references aux vues d'UN item de la liste. Evite les appels repetes a `findViewById`.

```java
public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemBinding ui;   // View Binding pour le layout d'un item
    private XMenAdapter.OnItemClickListener listener;

    public ItemViewHolder(ItemBinding ui) {
        super(ui.getRoot());
        this.ui = ui;
        itemView.setOnClickListener(this);  // ecoute les clics
    }

    // Remplir les vues avec les donnees d'un item
    public void setItem(Item item) {
        ui.textViewNom.setText(item.getNom());
        ui.textViewDescription.setText(item.getDescription());
        ui.imageView.setImageResource(item.getIdImage());
    }

    public void setOnItemClickListener(XMenAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
```

## 4.5 L'Adapter

Fait le lien entre les donnees et le RecyclerView. 3 methodes OBLIGATOIRES.

```java
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    // Interface pour les clics
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

    // 1. CREER un ViewHolder (inflate le layout d'un item)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding binding = ItemBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false    // ne PAS attacher au parent maintenant
        );
        return new ItemViewHolder(binding);
    }

    // 2. REMPLIR un ViewHolder avec les donnees a la position donnee
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = liste.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    // 3. Retourner le NOMBRE total d'items
    @Override
    public int getItemCount() {
        return liste.size();
    }
}
```

## 4.6 L'Activity (tout assembler)

```java
public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
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
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        Item item = liste.get(position);
        // Faire quelque chose avec l'item clique
    }
}
```

**Layout XML du RecyclerView :**
```xml
<!-- activity_main.xml -->
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recycler"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

## 4.7 LayoutManagers

```java
// Liste verticale (par defaut)
binding.recycler.setLayoutManager(new LinearLayoutManager(this));

// Liste horizontale
binding.recycler.setLayoutManager(
    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

// Grille (2 colonnes)
binding.recycler.setLayoutManager(new GridLayoutManager(this, 2));

// Grille decalee (type Pinterest)
binding.recycler.setLayoutManager(
    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
```

## 4.8 Notifications de changement (adapter.notify...)

**CRUCIAL : toujours notifier l'adapter apres modification des donnees**

```java
// Un seul item a change (position connue)
adapter.notifyItemChanged(position);

// Un item a ete ajoute
liste.add(nouvelItem);
adapter.notifyItemInserted(liste.size() - 1);

// Un item a ete supprime
liste.remove(position);
adapter.notifyItemRemoved(position);

// Un item a ete deplace
adapter.notifyItemMoved(fromPosition, toPosition);

// Plage d'items modifies
adapter.notifyItemRangeChanged(startPosition, itemCount);
adapter.notifyItemRangeInserted(startPosition, itemCount);
adapter.notifyItemRangeRemoved(startPosition, itemCount);

// TOUT a change (a eviter si possible, moins performant)
adapter.notifyDataSetChanged();
```

### Exemple : ajouter un item
```java
void ajouterItem() {
    Item nouveau = new Item("Nouveau", "Description", R.drawable.img1);
    liste.add(nouveau);
    adapter.notifyItemInserted(liste.size() - 1);
    binding.recycler.scrollToPosition(liste.size() - 1);  // scroll vers le nouveau
}
```

### Exemple : supprimer un item
```java
void supprimerItem(int position) {
    liste.remove(position);
    adapter.notifyItemRemoved(position);
    // Si les positions changent apres suppression :
    adapter.notifyItemRangeChanged(position, liste.size() - position);
}
```

---

# 5 - MENUS, DIALOGUES, FRAGMENTS

## 5.1 Barre d'action - Menu Options

### Creer le fichier menu XML (`res/menu/main_menu.xml`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <!-- Toujours visible dans la barre -->
    <item android:id="@+id/action_ajouter"
        android:icon="@drawable/ic_action_new"
        android:title="Ajouter"
        app:showAsAction="always" />

    <!-- Visible si la place le permet -->
    <item android:id="@+id/action_reinit"
        android:icon="@drawable/ic_action_select_all"
        android:title="Reinitialiser"
        app:showAsAction="ifRoom" />

    <!-- Toujours dans le menu overflow (3 points) -->
    <item android:id="@+id/action_settings"
        android:title="Parametres"
        app:showAsAction="never" />
</menu>
```

### showAsAction valeurs
| Valeur | Comportement |
|--------|-------------|
| `always` | Toujours visible dans la barre |
| `ifRoom` | Visible si la place, sinon overflow |
| `never` | Toujours dans le menu overflow (3 points) |
| `withText` | Affiche le titre a cote de l'icone |

### Charger et ecouter le menu dans l'Activity
```java
// 1. Gonfler (inflate) le menu
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
}

// 2. Reagir aux clics
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_ajouter) {
        // Action ajouter
        return true;
    } else if (item.getItemId() == R.id.action_reinit) {
        // Action reinitialiser
        return true;
    } else if (item.getItemId() == R.id.action_settings) {
        // Action parametres
        return true;
    }
    return super.onOptionsItemSelected(item);
}
```

## 5.2 Menu contextuel (appui long sur un element)

### Menu contextuel flottant
```java
// 1. Enregistrer la vue pour le menu contextuel (dans onCreate)
registerForContextMenu(binding.recycler);  // ou n'importe quelle vue

// 2. Creer le menu
@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    getMenuInflater().inflate(R.menu.context_menu, menu);
    menu.setHeaderTitle("Actions");
}

// 3. Reagir aux clics
@Override
public boolean onContextItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_supprimer) {
        // Supprimer l'element
        return true;
    }
    return super.onContextItemSelected(item);
}
```

### Menu contextuel ActionMode (barre d'action temporaire)
```java
// Demarrer l'ActionMode (typiquement sur un appui long)
ActionMode actionMode = startActionMode(new ActionMode.Callback() {
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.context_menu, menu);
        mode.setTitle("1 selectionne");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_supprimer) {
            // Supprimer
            mode.finish();  // Fermer l'ActionMode
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Nettoyage
    }
});
```

## 5.3 Popup Menu
```java
// Afficher un menu popup ancre a une vue
PopupMenu popup = new PopupMenu(this, anchorView);
popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
popup.setOnMenuItemClickListener(item -> {
    if (item.getItemId() == R.id.action_editer) {
        // Action
        return true;
    }
    return false;
});
popup.show();
```

## 5.4 Toast (message bref)
```java
Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
Toast.makeText(getApplicationContext(), "Message long", Toast.LENGTH_LONG).show();
```

## 5.5 Snackbar (message avec action)
```java
Snackbar.make(binding.getRoot(), "Element supprime", Snackbar.LENGTH_LONG)
    .setAction("ANNULER", v -> {
        // Annuler la suppression
    })
    .show();
```

## 5.6 AlertDialog (dialogue modal)
```java
new AlertDialog.Builder(this)
    .setTitle("Confirmation")
    .setMessage("Voulez-vous supprimer cet element ?")
    .setIcon(android.R.drawable.ic_dialog_alert)
    .setPositiveButton("Oui", (dialog, which) -> {
        // Action confirmer
    })
    .setNegativeButton("Non", (dialog, which) -> {
        dialog.dismiss();
    })
    .setNeutralButton("Annuler", null)
    .setCancelable(false)    // empeche de fermer en cliquant a cote
    .show();
```

### AlertDialog avec champ de saisie
```java
EditText input = new EditText(this);
input.setHint("Entrez un nom");

new AlertDialog.Builder(this)
    .setTitle("Ajouter un element")
    .setView(input)
    .setPositiveButton("Ajouter", (dialog, which) -> {
        String nom = input.getText().toString();
        // Ajouter avec ce nom
    })
    .setNegativeButton("Annuler", null)
    .show();
```

### AlertDialog avec liste de choix
```java
String[] options = {"Option A", "Option B", "Option C"};

new AlertDialog.Builder(this)
    .setTitle("Choisir")
    .setItems(options, (dialog, which) -> {
        // which = index du choix (0, 1, 2)
        String choix = options[which];
    })
    .show();
```

## 5.7 Fragments

### Schema Fragment dans une Activity
```
┌──────────────── Activity ────────────────────┐
│  ┌──────────────────────────────────────┐    │
│  │      FragmentContainerView           │    │
│  │  ┌──────────────────────────────┐    │    │
│  │  │        Fragment A            │    │    │
│  │  │  (propre layout, logique)    │    │    │
│  │  └──────────────────────────────┘    │    │
│  └──────────────────────────────────────┘    │
│  ┌──────────────────────────────────────┐    │
│  │      FragmentContainerView           │    │
│  │  ┌──────────────────────────────┐    │    │
│  │  │        Fragment B            │    │    │
│  │  └──────────────────────────────┘    │    │
│  └──────────────────────────────────────┘    │
└──────────────────────────────────────────────┘
```

### Cas tablette (multi-panneaux) vs telephone (un seul)
```
TABLETTE :                          TELEPHONE :
┌──────────┬───────────────┐        ┌──────────────┐
│ Fragment │  Fragment      │        │  Fragment     │
│  Liste   │  Detail        │        │  Liste        │
│          │               │        │              │
└──────────┴───────────────┘        └──────────────┘
                                     (navigation vers Detail)
```

### Creer un Fragment

#### 1. Layout du fragment (`res/layout/fragment_liste.xml`)
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView android:id="@+id/textViewTitre"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Mon Fragment" />

    <RecyclerView android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

#### 2. Classe du Fragment
```java
public class ListeFragment extends Fragment {

    private FragmentListeBinding binding;

    // Constructeur vide OBLIGATOIRE
    public ListeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configurer les vues ici (equivalent du onCreate de l'Activity)
        binding.textViewTitre.setText("Liste des elements");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Eviter les fuites memoire
    }
}
```

#### 3. Placer le fragment dans l'Activity

**En XML (statique) :**
```xml
<!-- Dans le layout de l'Activity -->
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/fragmentContainer"
    android:name="fr.iutlille.monapp.ListeFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

**En Java (dynamique) :**
```java
// Dans onCreate() de l'Activity
if (savedInstanceState == null) {   // eviter de recreer au changement d'orientation
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragmentContainer, new ListeFragment())
        .commit();
}
```

#### 4. Remplacer un fragment (navigation)
```java
getSupportFragmentManager()
    .beginTransaction()
    .replace(R.id.fragmentContainer, new DetailFragment())
    .addToBackStack(null)    // permet de revenir en arriere avec le bouton retour
    .commit();
```

### Communication entre Fragments

#### Via l'Activity (pattern interface callback)

```java
// 1. Interface dans le fragment source
public class ListeFragment extends Fragment {

    // Interface que l'Activity doit implementer
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private OnItemSelectedListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verifier que l'Activity implemente l'interface
        if (context instanceof OnItemSelectedListener) {
            callback = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context + " doit implementer OnItemSelectedListener");
        }
    }

    // Quand un item est clique
    void onItemClique(int position) {
        callback.onItemSelected(position);  // Prevenir l'Activity
    }
}
```

```java
// 2. L'Activity implemente l'interface
public class MainActivity extends AppCompatActivity
        implements ListeFragment.OnItemSelectedListener {

    @Override
    public void onItemSelected(int position) {
        // Mettre a jour le fragment detail
        DetailFragment detail = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        detail.setArguments(args);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, detail)
            .addToBackStack(null)
            .commit();
    }
}
```

```java
// 3. Le fragment detail recupere les arguments
public class DetailFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int position = getArguments().getInt("position");
            // Afficher les details
        }
    }
}
```

#### Via ViewModel partage (methode moderne)
```java
// ViewModel partage entre fragments
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedPosition = new MutableLiveData<>();

    public void selectPosition(int position) {
        selectedPosition.setValue(position);
    }

    public LiveData<Integer> getSelectedPosition() {
        return selectedPosition;
    }
}

// Dans le fragment liste
SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
model.selectPosition(position);

// Dans le fragment detail
SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
model.getSelectedPosition().observe(getViewLifecycleOwner(), position -> {
    // Afficher les details pour cette position
});
```

### Cycle de vie d'un Fragment
```
onAttach() → onCreate() → onCreateView() → onViewCreated()
→ onStart() → onResume() → [ACTIF]
→ onPause() → onStop() → onDestroyView() → onDestroy() → onDetach()
```

---

# EXERCICES TYPE CTP

## Exercice 1 : App de gestion d'une liste (type XMen)
**Enonce imagine :** Creer une app qui affiche une liste de personnages avec RecyclerView.
Chaque personnage a : nom, description, image.
- Ajouter via un menu "+" dans la barre d'action
- Supprimer via un clic sur l'element (avec confirmation AlertDialog)
- Bouton "reinitialiser" dans le menu overflow

**Fichiers a creer :**
1. `Personnage.java` (modele POJO)
2. `MonApplication.java` (extends Application, init la liste)
3. `PersonnageAdapter.java` (extends RecyclerView.Adapter)
4. `PersonnageViewHolder.java` (extends RecyclerView.ViewHolder)
5. `MainActivity.java` (RecyclerView + menu)
6. Layouts : `activity_main.xml`, `item_personnage.xml`
7. Menu : `res/menu/main_menu.xml`
8. Manifest : declarer `android:name=".MonApplication"`

## Exercice 2 : Navigation multi-ecrans
**Enonce imagine :** App avec ecran login → ecran liste → ecran detail.
- LoginActivity : saisie nom + bouton → passe le nom a ListeActivity
- ListeActivity : affiche "Bonjour [nom]" + RecyclerView
- Clic sur un item → DetailsActivity avec les infos de l'item
- Menu avec "Deconnexion" (retour au login avec finish())

**Points cles :**
- Intent explicite avec putExtra/getStringExtra
- Application class pour stocker les donnees partagees
- View Binding dans toutes les activites
- finish() pour fermer une activite

## Exercice 3 : Fragments
**Enonce imagine :** App avec 2 fragments : ListeFragment et DetailFragment.
- En mode portrait : affiche ListeFragment, clic → remplace par DetailFragment
- Communication via interface callback dans l'Activity
- BackStack pour pouvoir revenir en arriere

**Pattern a suivre :**
1. ListeFragment avec interface OnItemSelectedListener
2. Activity implemente l'interface
3. Activity remplace le fragment avec addToBackStack
4. DetailFragment recoit les donnees via Bundle/getArguments()

---

# AIDE-MEMOIRE RAPIDE

## Imports les plus courants
```java
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;          // TextView, EditText, Button, ImageView, Toast
import android.app.AlertDialog;
import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
```

## Checklist avant de rendre
- [ ] AndroidManifest.xml : toutes les activites declarees ?
- [ ] AndroidManifest.xml : `android:name=".MonApplication"` si Application custom ?
- [ ] AndroidManifest.xml : permissions ajoutees si necessaire ?
- [ ] build.gradle : `viewBinding = true` si utilise ?
- [ ] Adapter : les 3 methodes implementees (onCreateViewHolder, onBindViewHolder, getItemCount) ?
- [ ] notify... appele apres chaque modification de la liste ?
- [ ] LayoutManager defini sur le RecyclerView ?
- [ ] `images.recycle()` apres obtainTypedArray ?
- [ ] `super.onXxx()` appele en premier dans chaque methode du cycle de vie ?
