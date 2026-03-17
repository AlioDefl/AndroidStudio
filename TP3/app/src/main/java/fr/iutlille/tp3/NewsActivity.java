package fr.iutlille.tp3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TP3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() de l'activité " + getLocalClassName());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        setTitle(getLocalClassName());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Récupérer le login depuis l'intent
        Intent intent = getIntent();
        String login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN);

        TextView textViewLogin = findViewById(R.id.textViewLogin);
        if (login != null && !login.isEmpty()) {
            textViewLogin.setText("Bienvenue " + login);
        }

        Button btnCours = findViewById(R.id.btnCours);
        btnCours.setOnClickListener(this);

        Button btnAPropos = findViewById(R.id.btnAPropos);
        btnAPropos.setOnClickListener(this);

        Button btnDetails = findViewById(R.id.btnDetails);
        btnDetails.setOnClickListener(this);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() de l'activité " + getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() de l'activité " + getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause() de l'activité " + getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() de l'activité " + getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() de l'activité " + getLocalClassName());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCours) {
            Log.i(TAG, "Clic sur le bouton Cours");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iut.univ-lille.fr"));
            startActivity(intent);
        } else if (id == R.id.btnAPropos) {
            Log.i(TAG, "Clic sur le bouton A Propos");
            android.widget.Toast.makeText(getApplicationContext(), "À propos de cette appli...", android.widget.Toast.LENGTH_LONG).show();
        } else if (id == R.id.btnDetails) {
            Log.i(TAG, "Clic sur le bouton Détails");
            Intent intent = new Intent(NewsActivity.this, DetailsActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnLogout) {
            Log.i(TAG, "Clic sur le bouton Logout");
            Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}