package im.zcjl.zapm;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class AdvancedPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String KEY_ADVANCED_CHECKBOX_PREFERENCE = "advanced_checkbox_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advanced_preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.id.remote_host))) {
            Toast.makeText(this, "Thanks! You increased my count to " + sharedPreferences.getString(key, ""),
                           Toast.LENGTH_LONG).show();
        }
    }

}
