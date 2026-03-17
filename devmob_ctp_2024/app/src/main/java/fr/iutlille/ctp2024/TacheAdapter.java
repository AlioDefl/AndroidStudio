package fr.iutlille.ctp2024;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TacheAdapter extends RecyclerView.Adapter<TacheViewHolder> {
    private List<Tache> taches;

    public TacheAdapter(List<Tache> taches) {
        this.taches = taches;
    }

    @Override
    public TacheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tache, parent, false);
        return new TacheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TacheViewHolder holder, int position) {
        Tache tache = taches.get(position);
        holder.bind(tache);
    }

    @Override
    public int getItemCount() {
        return taches.size();
    }
}
