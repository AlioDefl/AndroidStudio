package fr.iutlille.xmen;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import fr.iutlille.xmen.databinding.XMenBinding;

public class XMenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final XMenBinding ui;
    private XMenAdapter.OnItemClickListener onItemClickListener;

    public XMenViewHolder(XMenBinding ui) {
        super(ui.getRoot());
        this.ui = ui;
        // Définir le ViewHolder comme écouteur de clics sur itemView
        itemView.setOnClickListener(this);
    }

    public void setOnItemClickListener(XMenAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        // Quand on clique, on prévient l'écouteur en lui donnant la position
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public void setXMen(XMen xmen) {
        ui.textViewNom.setText(xmen.getNom());

        ui.textViewAlias.setText(xmen.getAlias());

        ui.textViewConfrerie.setText(xmen.getConfrerie());

        ui.textViewDescription.setText(xmen.getDescription());

        ui.textViewPouvoirs.setText("Powers: " + xmen.getPouvoirs());

        ui.imageView.setImageResource(xmen.getIdImage());
    }
}
