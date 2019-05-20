package gi.pelatihan.odt.presensikaryawan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import gi.pelatihan.odt.presensikaryawan.adapter.CutiItemAdapter;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataBulan;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataCuti;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseBulan;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseCuti;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CutiListActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private Activity activity = this;

    private Spinner spcuti;
    private RecyclerView rvcuti;
    private FloatingActionButton fabcuti;
    private LinearLayout llnothing;
    private TextView tvnothing;

    private ProgressDialog loading;
    private SharedPreferences spLogin;

    private ArrayList<DataBulan> dataBulanArrayList = new ArrayList<>();
    private ArrayList<DataCuti> dataCutiArrayList = new ArrayList<>();

    private String idkarywan = "";
    private String statusPengajuan = "-";
    private String tahun = "-";
    private String bulan = "-";
    private String title = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cuti_list);

        try{
            Intent dataIntent = getIntent();
            statusPengajuan = dataIntent.getStringExtra("status");
        }catch (Exception e){
            e.printStackTrace();
            statusPengajuan = "tunggu";
        }


        spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        idkarywan = spLogin.getString(Data_Pref.id_karyawan,"-");

        spcuti = findViewById(R.id.spcuti);
        rvcuti = findViewById(R.id.rvcuti);
        llnothing = findViewById(R.id.llnothing);
        tvnothing = findViewById(R.id.tvnothing);
        fabcuti = findViewById(R.id.fabcuti);

        fabcuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,CutiPengajuanActivity.class);
                startActivity(intent);
            }
        });

        if(statusPengajuan.equalsIgnoreCase("tunggu")){
            title = "Pengajuan Cuti";
            fabcuti.setVisibility(View.VISIBLE);
        }else{
            title = "Laporan Pengajuan Cuti";
            fabcuti.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

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
        params.put("status_pengajuan", statusPengajuan);

        Log.e(tag, "param url_data         : " + url_data);
        Log.e(tag, "param id_karyawan      : " + idkarywan);
        Log.e(tag, "param status_pengajuan : " + statusPengajuan);

        Call<ResponseBulan> call = ApiData.ApiService(url_data).getDataBulanCuti(params);
        call.enqueue(new Callback<ResponseBulan>() {
            @Override
            public void onResponse(Call<ResponseBulan> call, Response<ResponseBulan> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));

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

                doShowDataBulan(message,success);
            }

            @Override
            public void onFailure(Call<ResponseBulan> call, Throwable t) {
                t.printStackTrace();

                String message = "";
                int success = 0;
                doShowDataCuti(message,success);
            }
        });
    }

    private void doShowDataBulan(final String message, int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        ArrayList<String> bulan_array = new ArrayList<>();
        bulan_array.add("Pilih Bulan");
        for (int i = 0; i < dataBulanArrayList.size(); i++) {
            String sp = dataBulanArrayList.get(i).getNamabulan() + " " + dataBulanArrayList.get(i).getTahun();
            bulan_array.add(sp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, bulan_array);
        spcuti.setAdapter(adapter);
        spcuti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    bulan = dataBulanArrayList.get(position - 1).getBulan();
                    tahun = dataBulanArrayList.get(position - 1).getTahun();
                    Log.e(tag, "onItemSelected : " + bulan + " " + tahun);

                    doGetDataCuti();
                } else {
                    bulan = "-";
                    tahun = "-";
                    //Toast.makeText(activity, "Silahkan Pilih Bulan ..", Toast.LENGTH_SHORT).show();

                    dataCutiArrayList = new ArrayList<>();
                    doShowDataCuti("Silakan Pilih Bulan...",0);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doGetDataCuti() {
        if (!loading.isShowing()) {
            loading.show();
        }

        dataCutiArrayList = new ArrayList<>();
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;


        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);
        params.put("status_pengajuan", statusPengajuan);
        params.put("bulan", bulan);
        params.put("tahun", tahun);

        Log.e(tag, "param url_data         : " + url_data);
        Log.e(tag, "param id_karyawan      : " + idkarywan);
        Log.e(tag, "param status_pengajuan : " + statusPengajuan);
        Log.e(tag, "param bulan            : " + bulan);
        Log.e(tag, "param tahun            : " + tahun);

        Call<ResponseCuti> call = ApiData.ApiService(url_data).getDataCuti(params);
        call.enqueue(new Callback<ResponseCuti>() {
            @Override
            public void onResponse(Call<ResponseCuti> call, Response<ResponseCuti> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));

                String message = "";
                int success = 0;

                dataCutiArrayList = new ArrayList<>();
                try{
                    success = response.body().getSuccess();
                    message = response.body().getMessage();

                    if (response.isSuccessful() && success == 1) {
                        dataCutiArrayList.addAll(response.body().getData());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    message = "Terjadi Kesalahan #1";
                }

                doShowDataCuti(message,success);
            }

            @Override
            public void onFailure(Call<ResponseCuti> call, Throwable t) {
                t.printStackTrace();

                String message = "Terjadi Kesalahan #2";
                int success = 0;
                doShowDataCuti(message,success);
            }
        });
    }

    private void doShowDataCuti(final String message, int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        CutiItemAdapter adapter = new CutiItemAdapter(activity, dataCutiArrayList);
        LinearLayoutManager eLayoutManager = new LinearLayoutManager(activity);
        rvcuti.setHasFixedSize(true);
        rvcuti.setLayoutManager(eLayoutManager);
        rvcuti.setAdapter(adapter);

        int size = dataCutiArrayList.size();
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
