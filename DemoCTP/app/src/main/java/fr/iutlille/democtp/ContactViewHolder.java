package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  VIEWHOLDER - Tient les references des vues d'UN item        ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Role : eviter de refaire findViewById a chaque scroll        ║
 * ║  Chaque item visible dans la liste a SON ViewHolder          ║
 * ║                                                               ║
 * ║  LIENS :                                                     ║
 * ║  - Utilise par ContactAdapter (dans onCreateViewHolder)      ║
 * ║  - Layout : res/layout/item_contact.xml (via View Binding)   ║
 * ║  - Communique les clics via l'interface de l'Adapter         ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import fr.iutlille.democtp.databinding.ItemContactBinding;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // ui = View Binding pour item_contact.xml
    // Donne acces a : ui.textViewNom, ui.textViewTelephone, ui.imageView, etc.
    private final ItemContactBinding ui;

    // Reference vers le listener de clics (defini dans l'Adapter)
    private ContactAdapter.OnItemClickListener listener;

    /**
     * Constructeur : recoit le binding du layout d'un item
     * super(ui.getRoot()) → donne la vue racine au RecyclerView
     */
    public ContactViewHolder(ItemContactBinding ui) {
        super(ui.getRoot());
        this.ui = ui;

        // Quand on clique sur l'item entier → this.onClick() est appele
        itemView.setOnClickListener(this);
    }

    /**
     * Remplit les vues avec les donnees d'un Contact
     * Appele par l'Adapter dans onBindViewHolder()
     */
    public void setContact(Contact contact) {
        ui.textViewNom.setText(contact.getNom());
        ui.textViewTelephone.setText(contact.getTelephone());
        ui.textViewEmail.setText(contact.getEmail());
        ui.imageViewContact.setImageResource(contact.getIdImage());
    }

    /**
     * Recoit le listener depuis l'Adapter
     */
    public void setOnItemClickListener(ContactAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Quand l'utilisateur clique sur cet item :
     * On previent le listener en lui donnant la POSITION de l'item dans la liste
     * getAdapterPosition() → retourne l'index (0, 1, 2, ...)
     */
    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
