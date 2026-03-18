package fr.iutlille.ctp2024;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

public class TacheViewHolder extends RecyclerView.ViewHolder {
    CheckBox checkBox;
    TextView tacheInfo;
    TextView category;
    TextView deadline;
    TextView priority;

    public TacheViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox);
        tacheInfo = itemView.findViewById(R.id.tache_info);
        category = itemView.findViewById(R.id.category);
        deadline = itemView.findViewById(R.id.deadline);
        priority = itemView.findViewById(R.id.priority);
    }

    public void bind(Tache tache) {
        tacheInfo.setText(tache.getNom());
        category.setText(tache.getCategorie());
        deadline.setText(DateFormat.getDateInstance().format(tache.getEcheance()));
        priority.setText(tache.getPriorite().toString());

        // Q7 : IMPORTANT - Retirer le listener AVANT de changer l'etat de la checkbox
        // Sinon quand le RecyclerView recycle une vue, setChecked() declenche
        // l'ancien listener sur la MAUVAISE tache (bug classique)
        checkBox.setOnCheckedChangeListener(null);

        // Cocher/decocher selon le status actuel
        checkBox.setChecked(tache.getStatus().equals(Status.FAIT));

        // Puis remettre le listener APRES
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tache.setStatus(Status.FAIT);
            } else {
                tache.setStatus(Status.AFAIRE);
            }
        });
    }
}
