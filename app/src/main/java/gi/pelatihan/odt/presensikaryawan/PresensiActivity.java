package gi.pelatihan.odt.presensikaryawan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.adapter.PresensiItemAdapter;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataBulan;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensi;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseBulan;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseDataPresensi;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresensiActivity extends AppCompatActivity {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    private RecyclerView recyclerView;
    private Spinner splappres;
    private ProgressDialog loading;
    private LinearLayout llnothing;
    private TextView tvnothing;

    private ArrayList<DataBulan> dataBulanArrayList = new ArrayList<>();
    private ArrayList<DataPresensi> data_presensiArrayList = new ArrayList<>();

    private SharedPreferences spLogin;

    private String idkarywan = "1";
    private String tahun = "-";
    private String bulan = "-";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_presensi_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        idkarywan = spLogin.getString(Data_Pref.id_karyawan,"-");

        llnothing = findViewById(R.id.llnothing);
        tvnothing = findViewById(R.id.tvnothing);
        recyclerView = findViewById(R.id.recycler_view);
        splappres = findViewById(R.id.splappres);

        loading = new ProgressDialog(activity);
        loading.setMessage("Mohon Tunggu......");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();

        doGetDataBulan();
    }

    private void doGetDataBulan() {
        if (!loading.isShowing()) {
            loading.show();
        }

        dataBulanArrayList = new ArrayList<>();
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);

        Log.e(tag, "param url_data         : " + url_data);
        Log.e(tag, "param id_karyawan      : " + idkarywan);

        Call<ResponseBulan> call = ApiData.ApiService(url_data).getSemuaBulan(params);
        call.enqueue(new Callback<ResponseBulan>() {
            @Override
            public void onResponse(Call<ResponseBulan> call, Response<ResponseBulan> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                dataBulanArrayList = new ArrayList<>();

                String message = "";
                int success = 0;

                dataBulanArrayList = new ArrayList<>();
                try{
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    
                    if (response.isSuccessful() && success == 1) {
                        dataBulanArrayList.addAll(response.body().getData());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    message = "Terjadi Kesalahan #1";
                }
                doShowDataPresensiBulan(message,success);
            }

            @Override
            public void onFailure(Call<ResponseBulan> call, Throwable t) {
                String message = "";
                int success = 0;
                doShowDataPresensiBulan(message,success);
            }
        });
    }

    private void doShowDataPresensiBulan(final String message, int success) {
        if (loading.isShowing()) {
            loading.cancel();
        }
        ArrayList<String> bulan_array = new ArrayList<>();
        bulan_array.add("Pilih Bulan");
        for (int i = 0; i < dataBulanArrayList.size(); i++) {
            String sp = dataBulanArrayList.get(i).getNamabulan() + " " + dataBulanArrayList.get(i).getTahun();
            bulan_array.add(sp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, bulan_array);
        splappres.setAdapter(adapter);
        splappres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(tag, "onItemSelected : " + position);
                if (position > 0) {
                    bulan = dataBulanArrayList.get(position - 1).getBulan();
                    tahun = dataBulanArrayList.get(position - 1).getTahun();
                    Log.e(tag, "onItemSelected : " + bulan + " " + tahun);

                    doGetDataPresensi();
                } else {
                    bulan = "-";
                    tahun = "-";
                    //Toast.makeText(activity, "Silahkan Pilih Bulan ..", Toast.LENGTH_SHORT).show();

                    doShowDataPresensi("Silakan Pilih Bulan...",0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doGetDataPresensi() {
        if (!loading.isShowing()) {
            loading.show();
        }

        data_presensiArrayList = new ArrayList<>();

        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        String idkarywan = "1";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);
        params.put("bulan", bulan);
        params.put("tahun", tahun);

        Log.e(tag, "param url_data     : " + url_data);
        Log.e(tag, "param Id Karyawan       : " + idkarywan);
        Log.e(tag, "param Bulan       : " + bulan);
        Log.e(tag, "param Tahun       : " + tahun);

        Call<ResponseDataPresensi> call = ApiData.ApiService(url_data).getSemuaLaporanPresensi(params);
        call.enqueue(new Callback<ResponseDataPresensi>() {
            @Override
            public void onResponse(Call<ResponseDataPresensi> call, Response<ResponseDataPresensi> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                String message = "";
                int success = 0;

                data_presensiArrayList = new ArrayList<>();
                try{
                    success = response.body().getSuccess();
                    message = response.body().getMessage();

                    if (response.isSuccessful() && success == 1) {
                        data_presensiArrayList.addAll(response.body().getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Terjadi Kesalahan #1";
                }

                doShowDataPresensi(message,success);
            }

            @Override
            public void onFailure(Call<ResponseDataPresensi> call, Throwable t) {
                t.printStackTrace();

                String message = "";
                int success = 0;
                doShowDataPresensi(message,success);
            }
        });
    }

    private void doShowDataPresensi(final String message, int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        PresensiItemAdapter adapter = new PresensiItemAdapter(activity, data_presensiArrayList);
        recyclerView.setAdapter(adapter);

        int size = data_presensiArrayList.size();
        Log.e(tag, "size : " + size);
        if (size > 0) {
            showNothing(false,message);
        } else {
            showNothing(true,message);
        }
    }

    private void showNothing(boolean isShow,String message){
        if(isShow){
            llnothing.setVisibility(View.VISIBLE);
            tvnothing.setText(message);
        }else{
            llnothing.setVisibility(View.GONE);
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

