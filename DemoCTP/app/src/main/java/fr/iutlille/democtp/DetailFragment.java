package fr.iutlille.democtp;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  FRAGMENT - Partie reutilisable d'interface                   ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Un Fragment = un "mini-ecran" qui vit DANS une Activity     ║
 * ║  Il a son propre layout XML et son propre cycle de vie       ║
 * ║                                                               ║
 * ║  Cycle de vie :                                              ║
 * ║  onAttach → onCreate → onCreateView → onViewCreated          ║
 * ║  → onStart → onResume → [ACTIF]                             ║
 * ║  → onPause → onStop → onDestroyView → onDestroy → onDetach  ║
 * ║                                                               ║
 * ║  COMMUNICATION Fragment → Activity :                          ║
 * ║  1. Definir une interface dans le Fragment                    ║
 * ║  2. L'Activity implemente cette interface                    ║
 * ║  3. Dans onAttach(), verifier que l'Activity l'implemente    ║
 * ║  4. Appeler callback.methode() quand on veut communiquer    ║
 * ║                                                               ║
 * ║  LIENS :                                                     ║
 * ║  - Charge dans DetailActivity via FragmentManager             ║
 * ║  - Layout : res/layout/fragment_detail.xml                   ║
 * ║  - Donnees viennent de DemoApplication + arguments Bundle    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import fr.iutlille.democtp.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment {

    private static final String TAG = "DemoCTP";

    // ════════════════════════════════════════════
    //  INTERFACE DE CALLBACK (communication → Activity)
    // ════════════════════════════════════════════
    /**
     * L'Activity qui contient ce Fragment DOIT implementer cette interface
     * Sinon → crash au demarrage avec un message clair
     */
    public interface OnFragmentActionListener {
        void onActionFermer();
        void onActionAppeler(String telephone);
    }

    // Reference vers l'Activity (qui implemente l'interface)
    private OnFragmentActionListener callback;

    // View Binding pour le layout du fragment
    private FragmentDetailBinding binding;

    // Le contact affiche
    private Contact contact;

    /**
     * CONSTRUCTEUR VIDE OBLIGATOIRE
     * Android en a besoin pour recreer le Fragment (rotation d'ecran, etc.)
     */
    public DetailFragment() {}

    /**
     * FACTORY METHOD (bonne pratique pour passer des arguments a un Fragment)
     * On ne passe JAMAIS de donnees via le constructeur d'un Fragment
     * On utilise setArguments(Bundle) a la place
     */
    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);  // ← les arguments survivent aux recreations
        return fragment;
    }

    /**
     * onAttach : le Fragment est attache a l'Activity
     * C'est ici qu'on verifie que l'Activity implemente notre interface
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentActionListener) {
            callback = (OnFragmentActionListener) context;
        } else {
            throw new RuntimeException(context + " doit implementer OnFragmentActionListener");
        }
    }

    /**
     * onCreateView : inflate le layout du Fragment
     * Equivalent de setContentView() pour une Activity
     *
     * IMPORTANT : on retourne la vue, on ne fait PAS setContentView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated : les vues sont creees, on peut les configurer
     * C'est l'equivalent de la partie "apres setContentView" de onCreate()
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ════════════════════════════════════════════
        //  RECUPERER LES ARGUMENTS (donnes par newInstance)
        // ════════════════════════════════════════════
        int position = 0;
        if (getArguments() != null) {
            position = getArguments().getInt("position", 0);
        }

        // Recuperer le contact depuis l'Application
        // requireActivity() = l'Activity qui contient ce Fragment
        DemoApplication app = (DemoApplication) requireActivity().getApplicationContext();
        List<Contact> liste = app.getListeContacts();

        if (position >= 0 && position < liste.size()) {
            contact = liste.get(position);

            // Remplir les vues
            binding.textViewDetailNom.setText(contact.getNom());
            binding.textViewDetailTelephone.setText(contact.getTelephone());
            binding.textViewDetailEmail.setText(contact.getEmail());
            binding.imageViewDetail.setImageResource(contact.getIdImage());
        }

        // ════════════════════════════════════════════
        //  CLICS DANS LE FRAGMENT (lambda)
        // ════════════════════════════════════════════

        // Bouton Fermer → communiquer avec l'Activity via le callback
        binding.btnFermer.setOnClickListener(v -> {
            callback.onActionFermer();  // ← appelle la methode de l'Activity
        });

        // Bouton Appeler → communiquer avec l'Activity
        binding.btnAppeler.setOnClickListener(v -> {
            if (contact != null) {
                callback.onActionAppeler(contact.getTelephone());
            }
        });
    }

    /**
     * onDestroyView : la vue est detruite (mais le Fragment peut survivre)
     * IMPORTANT : mettre binding a null pour eviter les fuites memoire
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(TAG, "DetailFragment → onDestroyView()");
    }
}
