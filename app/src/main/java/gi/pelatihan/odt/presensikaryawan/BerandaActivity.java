package gi.pelatihan.odt.presensikaryawan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensiCek;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensiInput;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponsePresensiCek;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponsePresensiInput;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    private Calendar fromTime;
    private Calendar toTime;
    private Calendar currentTime;

    private Button btbrndpresensi;
    private Vibrator vibrator;
    private NavigationView navigationView;

    private static final int REQUEST_LOCATION = 1;

    private LocationManager locationManager;
    

    private ProgressDialog loading;
    private SharedPreferences spLogin;

    private ArrayList<DataPresensiCek> dataPresensiCekArrayList = new ArrayList<>();
    private ArrayList<DataPresensiInput> dataPresensiInputArrayList = new ArrayList<>();

    private String id_karywan="";
    private String lattitude;
    private String longitude;
    private String imei = "123";
    private String merk_hp = "somai";
    private String presensi = "masuk";
    private String id_presensi = "";
    private String keterangan = "";

    private boolean isMasuk = true;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_beranda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        id_karywan = spLogin.getString(Data_Pref.id_karyawan,"-");

        btbrndpresensi = findViewById(R.id.btbrndpresensi);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        /*
        Log.e(tag, "onCreate: " + cekWaktu(getCallingPackage()));
        if (cekWaktu("07:30-12:30")) {
            btbrndpresensi.setEnabled(true);
            Toast.makeText(activity, "SILAHKAN KLIK TOMBOL MASUK", Toast.LENGTH_LONG).show();
        } else {
            btbrndpresensi.setEnabled(false);
            Toast toast = Toast.makeText(activity, "WAKTU PRESENSI MASUK SUDAH DI TUTUP", Toast.LENGTH_LONG);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.RED);
            toast.show();
        }
        */

        btbrndpresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 26) {
                    //vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //vibrator.vibrate(200);
                }

                Log.e(tag,"isMasuk : "+isMasuk);
                if(isMasuk){
                    doPostDataPresensi();
                }else{
                    popupInfo();
                }

            }
        });

        loading = new ProgressDialog(activity);
        loading.setMessage("Mohon Tunggu......");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        //loading.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNav();
        doGetDataPresensiCek();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void setNav(){
        View header = navigationView.getHeaderView(0);
        LinearLayout llnavheader = header.findViewById(R.id.llnavheader);
        ImageView ivnavfoto = header.findViewById(R.id.ivnavfoto);
        TextView tvnavnama = header.findViewById(R.id.tvnavnama);

        String nama_karyawan = spLogin.getString(Data_Pref.nama_karyawan,"-");
        String foto_url = spLogin.getString(Data_Pref.foto_url,"-");

        Picasso.get()
                .load(foto_url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivnavfoto);

        tvnavnama.setText(nama_karyawan);

        llnavheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ProfilActivity.class));
            }
        });

    }

    private void doGetDataPresensiCek() {
        if (!loading.isShowing()) {
            loading.show();
        }

        dataPresensiCekArrayList = new ArrayList<>();
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan",id_karywan);

        Log.e(tag, "param url_data         : " + url_data);
        Log.e(tag, "param id_karyawan      : " + id_karywan);

        Call<ResponsePresensiCek> call = ApiData.ApiService(url_data).getDataPresensiCek(params);
        call.enqueue(new Callback<ResponsePresensiCek>() {
            @Override
            public void onResponse(Call<ResponsePresensiCek> call, Response<ResponsePresensiCek> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));

                dataPresensiCekArrayList = new ArrayList<>();
                int success = 0;
                try {
                    success = response.body().getSuccess();
                    if(success==1){
                        dataPresensiCekArrayList.addAll(response.body().getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(tag, "Terjadi Kesalahan #1");
                }

                doShowDataPresensiCek(success);
            }

            @Override
            public void onFailure(Call<ResponsePresensiCek> call, Throwable t) {
                t.printStackTrace();
                Log.e(tag, "Terjadi Kesalahan #2");
                doShowDataPresensiCek(0);
            }
        });
    }

    private void doShowDataPresensiCek(int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        if(success==1){
            btbrndpresensi.setBackgroundResource(R.drawable.btncircularpulang);
            btbrndpresensi.setText("PULANG");
            isMasuk=false;
            presensi="pulang";
            id_presensi = dataPresensiCekArrayList.get(0).getIdPresensi();
        }else{
            btbrndpresensi.setBackgroundResource(R.drawable.btncircularmasuk);
            btbrndpresensi.setText("MASUK");
            isMasuk=true;
            presensi="masuk";
            id_presensi = "";
            keterangan = "";
        }
    }

    private void doPostDataPresensi() {
        if (!loading.isShowing()) {
            loading.show();
        }

        dataPresensiInputArrayList = new ArrayList<>();

        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan",id_karywan);
        params.put("latitude", lattitude);
        params.put("longitude", longitude);
        params.put("imei", imei);
        params.put("merk_hp", merk_hp);
        params.put("presensi", presensi);
        params.put("id_presensi", id_presensi);
        params.put("keterangan", keterangan);

        Call<ResponsePresensiInput> call = ApiData.ApiService(url_data).postDataPresensi(params);
        call.enqueue(new Callback<ResponsePresensiInput>() {
            @Override
            public void onResponse(Call<ResponsePresensiInput> call, Response<ResponsePresensiInput> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));

                dataPresensiInputArrayList = new ArrayList<>();

                String message = "";
                int success = 0;
                try {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();

                    if(success==1){
                        dataPresensiInputArrayList.addAll(response.body().getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    success = 0;
                    message = "Terjadi Kesalahan #1";
                }

                doShowDataPresensi(message,success);
            }

            @Override
            public void onFailure(Call<ResponsePresensiInput> call, Throwable t) {
                t.printStackTrace();

                String message = "Terjadi Kesalahan #1";
                int success = 0;

                doShowDataPresensi(message,success);
            }
        });
    }

    private void doShowDataPresensi(String message, int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        if(success==1){
            String sid_presensi = dataPresensiInputArrayList.get(0).getPresensi();
            String spresensi = dataPresensiInputArrayList.get(0).getPresensi();



            Log.e(tag,"spresensi : "+spresensi);
            if(spresensi.equalsIgnoreCase("pulang")){
                btbrndpresensi.setBackgroundResource(R.drawable.btncircularmasuk);
                btbrndpresensi.setText("MASUK");
                presensi="masuk";
                isMasuk=true;

                id_presensi = "";
                keterangan = "";

            }else if(spresensi.equalsIgnoreCase("masuk")){
                btbrndpresensi.setBackgroundResource(R.drawable.btncircularpulang);
                btbrndpresensi.setText("PULANG");
                isMasuk=false;
                presensi="pulang";

                id_presensi = sid_presensi;
                keterangan = "";
            }
        }else{
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(activity, "Minimize", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        }
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


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pres_lap) {
            startActivity(new Intent(this, PresensiActivity.class));
        } else if (id == R.id.nav_nota) {
            startActivity(new Intent(this, NotaPengajuanActivity.class));
        } else if (id == R.id.nav_nota_lap) {
            startActivity(new Intent(this, NotaListActivity.class));
        } else if (id == R.id.nav_izin) {
            startActivity(new Intent(this, IzinPengajuanActivity.class));
        } else if (id == R.id.nav_izin_lap) {
            startActivity(new Intent(this, IzinListActivity.class));
        }else if (id == R.id.nav_cuti) {
            Intent intent = new Intent(this,CutiListActivity.class);
            intent.putExtra("status","tunggu");
            startActivity(intent);
        } else if (id == R.id.nav_cuti_lap) {
            Intent intent = new Intent(this,CutiListActivity.class);
            intent.putExtra("status","-");
            startActivity(intent);
        } else if (id == R.id.nav_ttg) {
            startActivity(new Intent(this, TentangActivity.class));
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editorLogin = spLogin.edit();
            editorLogin.clear();
            editorLogin.apply();

            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Lokasi
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(BerandaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (BerandaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(BerandaActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e(tag,"------- Lokasi Saat Ini -------");
                Log.e(tag,"latitude     : "+lattitude);
                Log.e(tag,"longitude    : "+longitude);
            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e(tag,"------- Lokasi Saat Ini -------");
                Log.e(tag,"latitude     : "+lattitude);
                Log.e(tag,"longitude    : "+longitude);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e(tag,"------- Lokasi Saat Ini -------");
                Log.e(tag,"latitude     : "+lattitude);
                Log.e(tag,"longitude    : "+longitude);

            } else {

                Toast.makeText(this, "Tidak dapat Presensi", Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Silahkan Hidupkan GPS Anda")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //PULANG
    private void popupInfo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin akan PULANG ??");
        builder.setCancelable(false);

        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                popupKeterangan();
                if (cekWaktu("07.30-12.30")) {
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean cekWaktu(String waktu) {
        try {
            String[] swaktu = waktu.split("-");
            String[] from = swaktu[0].split(":");
            String[] until = swaktu[1].split(":");

            fromTime = Calendar.getInstance();
            fromTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(from[0]));
            fromTime.set(Calendar.MINUTE, Integer.valueOf(from[1]));

            toTime = Calendar.getInstance();
            toTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(until[0]));
            toTime.set(Calendar.MINUTE, Integer.valueOf(until[1]));

            currentTime = Calendar.getInstance();
            currentTime.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY);
            currentTime.set(Calendar.MINUTE, Calendar.MINUTE);
            if (currentTime.after(fromTime) && currentTime.before(toTime)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void popupKeterangan() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.pop_pulang_awal, null);
        final AlertDialog alertD = new AlertDialog.Builder(activity).create();

        final EditText etKetPulang = promptView.findViewById(R.id.etpulangawal);
        Button btn_simpanPulang = promptView.findViewById(R.id.btpulangawal);

        btn_simpanPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();

                doPostDataPresensi();
            }
        });

        alertD.setView(promptView);
        alertD.show();
    }


}
