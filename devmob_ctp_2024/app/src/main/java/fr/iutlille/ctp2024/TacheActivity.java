package fr.iutlille.ctp2024;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.iutlille.ctp2024.databinding.ActivityTacheBinding;

public class TacheActivity extends AppCompatActivity {

        ActivityTacheBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityTacheBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            TacheApplication application = (TacheApplication) getApplication();
            List<Tache> taches = application.getTaches();
            TacheAdapter adapter = new TacheAdapter(taches);
            binding.recycler.setAdapter(adapter);
            binding.recycler.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            binding.recycler.setLayoutManager(lm);
        }
}
