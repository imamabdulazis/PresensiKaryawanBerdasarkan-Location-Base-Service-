package gi.pelatihan.odt.presensikaryawan;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.adapter.IzinItemAdapter;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanIzin;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseLaporanIzin;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IzinListActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private Activity activity = this;

    private RecyclerView rvlapIzin;
    private LinearLayout llnothing;
    private TextView tvnothing;

    private ProgressDialog loading;
    private SharedPreferences spLogin;

    private ArrayList<DataLaporanIzin> dataLaporanIzinArrayList = new ArrayList<>();

    private String idkarywan = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_izin_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        idkarywan = spLogin.getString(Data_Pref.id_karyawan,"-");

        rvlapIzin = findViewById(R.id.rvlapIzin);
        llnothing = findViewById(R.id.llnothing);
        tvnothing = findViewById(R.id.tvnothing);
        
        loading = new ProgressDialog(activity);
        loading.setMessage("Mohon Tunggu......");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();

        doGetDataIzin();
    }

    public void doGetDataIzin() {
        if (!loading.isShowing()) {
            loading.show();
        }

        dataLaporanIzinArrayList = new ArrayList<>();
        String url_base = UrlAkses.BASE_URL;
        String url_data = url_base + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);

        Log.e(tag, "param url_data         : " + url_data);
        Log.e(tag, "param id_karyawan      : " + idkarywan);

        Call<ResponseLaporanIzin> call = ApiData.ApiService(url_data).getSemuaDataIzin(params);
        call.enqueue(new Callback<ResponseLaporanIzin>() {
            @Override
            public void onResponse(Call<ResponseLaporanIzin> call, Response<ResponseLaporanIzin> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                
                String message = "";
                int success = 0;

                dataLaporanIzinArrayList = new ArrayList<>();
                try{
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    
                    if (response.isSuccessful() && success == 1) {
                        dataLaporanIzinArrayList.addAll(response.body().getData());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Terjadi Kesalahan #1";
                }

                doShowDataIzin(message,success);
            }

            @Override
            public void onFailure(Call<ResponseLaporanIzin> call, Throwable t) {
                t.printStackTrace();

                String message = "Terjadi Kesalahan #2";
                int success = 0;
                doShowDataIzin(message,success);
            }
        });

    }

    private void doShowDataIzin(final String message, int success) {
        if (loading.isShowing()) {
            loading.dismiss();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false);
        rvlapIzin.setHasFixedSize(true);
        rvlapIzin.setNestedScrollingEnabled(true);
        rvlapIzin.setLayoutManager(layoutManager);
        IzinItemAdapter adapter = new IzinItemAdapter(activity, dataLaporanIzinArrayList);
        rvlapIzin.setAdapter(adapter);

        int size = dataLaporanIzinArrayList.size();
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
