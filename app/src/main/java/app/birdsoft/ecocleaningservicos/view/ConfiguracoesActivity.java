package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class ConfiguracoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        //Fragmentos da configurações do app executando o res/XML
        private ListPreference formato;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference notificationSettings = findPreference("CONFIGURATION_PUSH");
            notificationSettings.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getActivity().getPackageName());
                intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
                intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
                startActivity(intent);
                return false;
            });
            findPreference("BIRDSOFT_KEY").setOnPreferenceClickListener((click -> {
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.birdsoft.com.br")));
                return false;
            }));
            formato = findPreference("FORMATO_DATA");
             Preference btn_exit = findPreference("PROFILE_EXIT");

            btn_exit.setOnPreferenceClickListener(preference -> {
                HelperManager.exitApp(getActivity());
                return false;
            });
        }

        public void onPause() {
            super.onPause();
            this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onResume() {
            super.onResume();
            this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("FORMATO_DATA")) {
                Settings.setFormateDate(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(key, "dd/MM/yyyy"), getActivity());
                //iniciarVisualizacaoData();
            } else {
                Settings.put(key, (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(key, true)), getActivity());
            }
        }

    }
}