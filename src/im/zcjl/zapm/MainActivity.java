package im.zcjl.zapm;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private class EfficientAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public EfficientAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return DATA.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_apm, null);

                holder = new ViewHolder();
                holder.host = (TextView) convertView.findViewById(R.id.host);
                holder.account = (TextView) convertView.findViewById(R.id.account);
                holder.passwd = (TextView) convertView.findViewById(R.id.passwd);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.host.setText(DATA[position][0]);
            holder.account.setText(DATA[position][1]);
            holder.passwd.setText(PasswdGenerator.encrypted);

            return convertView;
        }

        class ViewHolder {

            TextView host;
            TextView account;
            TextView passwd;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileDS fileDS = new FileDS(this);
        try {
            this.DATA = fileDS.getApmData(fileDS.readFromLocal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListAdapter(new EfficientAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.zapm_menu, menu); 
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Toast.makeText(this, sharedPref.getString(getString(R.id.remote_host), "nothing"), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.sync:
                    fileDS.sync();
                    this.DATA = fileDS.getApmData(fileDS.readFromLocal());
                    getListView().invalidateViews();
                    return true;
                case R.id.reset:
                    Toast.makeText(this, getString(R.string.settings) + fileDS.readFromLocal(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < getListView().getChildCount(); i++) {
                        EfficientAdapter.ViewHolder holder = (EfficientAdapter.ViewHolder) getListView().getChildAt(i).getTag();
                        holder.passwd.setText(PasswdGenerator.encrypted);
                    }
                    return true;
                case R.id.settings:
                    Intent launchPreferencesIntent = new Intent().setClass(this, AdvancedPreferences.class);
                    startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
                    return true;
                default:
                    // Don't toast text when a submenu is clicked
                    if (!item.hasSubMenu()) {
                        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Errors..." + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return false;
    }

    private static final int REQUEST_CODE_PREFERENCES = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        

        // The preferences returned if the request code is what we had given
        // earlier in startSubActivity
        if (requestCode == REQUEST_CODE_PREFERENCES) {
            // Read a sample value they have set
            // updateCounterText();
        }
    }

    // private void updateCounterText() {
    // // Since we're in the same package, we can use this context to get
    // // the default shared preferences
    // SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    // final int counter = sharedPref.getInt(AdvancedPreferences.KEY_MY_PREFERENCE, 0);
    // mCounterText.setText(getString(R.string.counter_value_is) + " " + counter);
    // }

    @Override
    public void onListItemClick(ListView lv, View v, int i, long l) {
        EfficientAdapter.ViewHolder holder = (EfficientAdapter.ViewHolder) v.getTag();
        if (holder.passwd.getText().equals(PasswdGenerator.encrypted)) {
            holder.passwd.setText(PasswdGenerator.getInstance().getPasswd(holder.host.getText(),
                                                                          holder.account.getText()));
        } else {
            holder.passwd.setText(PasswdGenerator.encrypted);
        }
    }

    private String[][]    DATA          = null;
    private FileDS        fileDS        = new FileDS(this);
//    private SettingHelper settingHelper = new SettingHelper(this);

}
