package fr.iutlille.xmen;

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

public class XMenApplication extends Application {

    private final List<XMen> liste = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        intiListe();
    }

    public void intiListe(){
        liste.clear();
        Resources res = getResources();

        String[] noms = res.getStringArray(R.array.noms);
        String[] alias = res.getStringArray(R.array.alias);
        String[] confreries = res.getStringArray(R.array.confrerie);
        String[] description = res.getStringArray(R.array.descriptions);
        String[] pouvoirs = res.getStringArray(R.array.pouvoirs);
        TypedArray images = res.obtainTypedArray(R.array.idimages);

        for (int i = 0; i < noms.length; i++){
            int idImage = images.getResourceId(i, R.drawable.ic_launcher_foreground);
            XMen xmen = new XMen(noms[i], alias[i], confreries[i], description[i], pouvoirs[i], idImage);

            liste.add(xmen);
        }
        images.recycle();
    }

    public List<XMen> getListe() {
        return liste;
    }
}
