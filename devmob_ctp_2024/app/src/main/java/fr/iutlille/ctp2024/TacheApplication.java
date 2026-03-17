package fr.iutlille.ctp2024;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TacheApplication extends Application {
    public static final String LOG_TAG = "TASKS";
    private final List<Tache> lesTaches = new ArrayList<Tache>();

    private final Set<String> lesCategories = new HashSet<String>();

    @Override public void onCreate() {
        super.onCreate();
        try {
            initTaches();
            Log.i(LOG_TAG, getTaches().toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Erreur de lecture du fichier des taches...");
            e.printStackTrace();
        }
    }

    public List<Tache> getTaches() {
        return lesTaches;
    }

    public Set<String> getLesCategories() {
        return lesCategories;
    }

    public void initTaches() throws JSONException {
        StringBuilder text = getConfig() ;
        JSONObject json = new JSONObject(text.toString());
        JSONArray array = json.getJSONArray("tasks");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0 ; i < array.length(); i++) {
            String category = array.getJSONObject(i).getString("category");
            Priorite priority = Priorite.valueOf(array.getJSONObject(i).getString("priority"));
            String description = array.getJSONObject(i).getString("description");
            String short_description = array.getJSONObject(i).getString("short_description");
            Date date;
            try {
                date = formatter.parse(array.getJSONObject(i).getString("due_date"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (!lesCategories.contains(category)) lesCategories.add(category);
            lesTaches.add(new Tache(short_description,date,priority,category,description));
        }
    }

    private StringBuilder getConfig() {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.des_taches)));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
