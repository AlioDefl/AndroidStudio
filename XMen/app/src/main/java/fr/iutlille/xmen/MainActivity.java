package fr.iutlille.xmen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import java.util.List;

import fr.iutlille.xmen.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements XMenAdapter.OnItemClickListener {

    private ActivityMainBinding binding;
    private List<XMen> liste;
    private XMenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        XMenApplication app = (XMenApplication) getApplicationContext();
        liste = app.getListe();

        adapter = new XMenAdapter(liste);

        // Fournir l'écouteur à l'adaptateur
        adapter.setOnItemClickListener(this);

        binding.recycler.setAdapter(adapter);

        binding.recycler.setHasFixedSize(true);


        binding.recycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onItemClick(int position) {
        // Récupérer le XMen cliqué
        XMen xmen = liste.get(position);

        // Modifier son image
        xmen.setIdImage(R.drawable.undef);

        // Notifier l'adaptateur du changement
        adapter.notifyItemChanged(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.reinit){
            XMenApplication app = (XMenApplication) getApplicationContext();
            app.intiListe();
            adapter.notifyDataSetChanged();
            return true;
        } else if(item.getItemId() == R.id.create){
            return true;
        }
    return super.onContextItemSelected(item);

    }
}
