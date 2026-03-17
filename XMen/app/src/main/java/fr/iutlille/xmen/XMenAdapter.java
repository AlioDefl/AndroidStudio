package fr.iutlille.xmen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.iutlille.xmen.databinding.XMenBinding;

public class XMenAdapter extends RecyclerView.Adapter<XMenViewHolder> {

    // Interface pour gérer les clics sur les items
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final List<XMen> liste;
    private OnItemClickListener onItemClickListener;

    public XMenAdapter(List<XMen> liste) {
        this.liste = liste;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public XMenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        XMenBinding binding = XMenBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new XMenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull XMenViewHolder holder, int position) {
        XMen xmen = liste.get(position);
        // Affecter les données au ViewHolder
        holder.setXMen(xmen);
        // Transmettre l'écouteur au ViewHolder
        holder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }
}
