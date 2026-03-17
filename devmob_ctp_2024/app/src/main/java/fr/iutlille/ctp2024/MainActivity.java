package fr.iutlille.ctp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import fr.iutlille.ctp2024.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonQuit.setOnClickListener(v -> finish());
        reloadData();
        binding.button.setOnClickListener(v -> {
            startActivity(new Intent(this, TacheActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData(){
        List<Tache> taches = ((TacheApplication) getApplication()).getTaches();
        int todo = 0;
        int urgent = 0;
        for(Tache tache : taches) {
            if(tache.getStatus().equals(Status.AFAIRE)){
                todo++;
            }
            if(tache.getPriorite().equals(Priorite.HAUTE)){
                urgent++;
            }
        }
        binding.textViewTodo.setText("Tâches à faire: "+ todo);
        binding.textViewUrgent.setText("Tâches urgentes: "+ urgent);
    }
}