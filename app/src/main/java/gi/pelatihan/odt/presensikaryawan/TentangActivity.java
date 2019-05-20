package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import gi.pelatihan.odt.presensikaryawan.R;

public class TentangActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private Activity activity = this;

    private ImageView ivttggambar;
    private TextView tvttgnama;
    private TextView tvttgversi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tentang);
        getSupportActionBar().setTitle("Tentang Aplikasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ivttggambar = findViewById(R.id.ivttggambar);
        tvttgnama = findViewById(R.id.tvttgnama);
        tvttgversi = findViewById(R.id.tvttgversi);

        String nama = getString(R.string.app_name);
        String versi = "v"+BuildConfig.VERSION_NAME;

        tvttgnama.setText(nama);
        tvttgversi.setText(versi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
