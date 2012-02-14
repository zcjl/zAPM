package im.zcjl.zapm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class FileDS {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String SPLIT_FLAG     = "\\|";
    private static final String APM_TXT        = "apm.txt";

    private ContextWrapper      cw;

    public FileDS(ContextWrapper cw) {
        this.cw = cw;
    }

    private String readFromNet() {
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(cw);
            String url = sharedPref.getString(cw.getString(R.id.remote_host), "nothing");
            Toast.makeText(cw, url, Toast.LENGTH_LONG).show();
            URL myurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String apmTxt = readApmTxt(br);
            br.close();
            conn.disconnect();

            return apmTxt;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String readFromLocal() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(cw.openFileInput(APM_TXT)));
            String apmTxt = readApmTxt(br);
            br.close();

            return apmTxt;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String readApmTxt(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private void writeToLocal(String apmTxt) {
        try {
            FileOutputStream os = cw.openFileOutput(APM_TXT, ContextWrapper.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(apmTxt);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sync() {
        writeToLocal(readFromNet());
    }

    public String[][] getApmData(String apmTxt) {
        String[] items = apmTxt.split(LINE_SEPARATOR);
        String[][] apmData = new String[items.length][2];
        for (int i = 0; i < items.length; i++) {
            apmData[i] = items[i].split(SPLIT_FLAG);
        }
        return apmData;
    }

}
