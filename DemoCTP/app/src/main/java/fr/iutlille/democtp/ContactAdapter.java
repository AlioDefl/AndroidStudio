package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  ADAPTER - Le pont entre les DONNEES et le RECYCLERVIEW       ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  C'est le "C" de MVC (Controleur) avec l'Activity            ║
 * ║                                                               ║
 * ║  3 METHODES OBLIGATOIRES :                                   ║
 * ║  1. onCreateViewHolder → cree un ViewHolder (inflate XML)    ║
 * ║  2. onBindViewHolder   → remplit un ViewHolder avec donnees  ║
 * ║  3. getItemCount       → dit combien d'items il y a         ║
 * ║                                                               ║
 * ║  LIENS :                                                     ║
 * ║  - Utilise ContactViewHolder pour afficher chaque item       ║
 * ║  - Recoit la List<Contact> depuis l'Activity                 ║
 * ║  - L'Activity implemente OnItemClickListener                 ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.iutlille.democtp.databinding.ItemContactBinding;

//                                    ↓ type generique = notre ViewHolder
public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    /**
     * INTERFACE de callback pour les clics sur les items
     * L'Activity implemente cette interface pour reagir aux clics
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // La liste de donnees (reference vers la meme liste que l'Application)
    private final List<Contact> liste;

    // Le listener de clics (sera l'Activity)
    private OnItemClickListener listener;

    // Constructeur : recoit la liste
    public ContactAdapter(List<Contact> liste) {
        this.liste = liste;
    }

    // Setter pour le listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ════════════════════════════════════════════
    //  LES 3 METHODES OBLIGATOIRES
    // ════════════════════════════════════════════

    /**
     * METHODE 1 : Cree un nouveau ViewHolder
     * Appelee quand le RecyclerView a besoin d'un NOUVEAU conteneur
     * (pas a chaque item, seulement quand il manque de ViewHolders)
     *
     * On "inflate" le layout XML d'un item → on cree le ViewHolder avec
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate = transformer le XML en objets Java
        // parent = le RecyclerView
        // false = ne pas attacher au parent maintenant (le RecyclerView le fera)
        ItemContactBinding binding = ItemContactBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ContactViewHolder(binding);
    }

    /**
     * METHODE 2 : Remplit un ViewHolder existant avec les donnees a la position donnee
     * Appelee a CHAQUE fois qu'un item doit etre affiche (scroll, etc.)
     *
     * holder = le ViewHolder a remplir
     * position = l'index dans la liste (0, 1, 2, ...)
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = liste.get(position);
        holder.setContact(contact);           // remplir les vues
        holder.setOnItemClickListener(listener); // transmettre le listener
    }

    /**
     * METHODE 3 : Retourne le nombre total d'items
     * Le RecyclerView en a besoin pour savoir combien de lignes afficher
     */
    @Override
    public int getItemCount() {
        return liste.size();
    }
}
