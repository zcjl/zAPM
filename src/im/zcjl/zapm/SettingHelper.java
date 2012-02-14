package im.zcjl.zapm;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SettingHelper {

    private static final String SETTING_NAME   = "zAMP";
    private static final String KEY_REMOTE_URL = "remoteURL";

    private ContextWrapper      cw;
    private SharedPreferences   settings;

    public SettingHelper(ContextWrapper cw) {
        this.cw = cw;
        settings = this.cw.getSharedPreferences(SETTING_NAME, ContextWrapper.MODE_PRIVATE);
    }

    public void setRemoteURL(String remoteURL) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_REMOTE_URL, remoteURL);
        editor.commit();
    }

    public String getRemoteURL() {
        return settings.getString(KEY_REMOTE_URL, "");
    }

}
