package fr.iutlille.tp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TP3";
    public static final String EXTRA_LOGIN = "fr.iutlille.tp3.LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() de l'activité " + getLocalClassName());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        setTitle(getLocalClassName());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

    public void onClickLogin(View view) {
        Log.i(TAG, "Clic sur le bouton login");

        EditText editTextLogin = findViewById(R.id.editTextLogin);
        String login = editTextLogin.getText().toString();

        // Stocker le login dans le contexte d'application
        TP3Application app = (TP3Application) getApplicationContext();
        app.setLogin(login);

        Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        startActivity(intent);
    }
}