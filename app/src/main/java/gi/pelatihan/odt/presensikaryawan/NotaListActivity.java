package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.adapter.NotaItemAdapter;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataBulanNota;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataJenis;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanNota;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseBulanNota;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseLaporanNota;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotaListActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private Activity activity = this;
    ProgressDialog loading;
    ApiData apiData;
    Spinner splapjenisnota, splapbulannota;
    String idkarywan = "1";
    String jenis = "-";
    String tahun = "-";
    String bulan = "-";
    private ArrayList<DataLaporanNota> data_notaArrayList = new ArrayList<>();
    private ArrayList<DataBulanNota> dataBulanNotaArrayList = new ArrayList<>();
    private ArrayList<DataJenis> dataJenisArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    NotaItemAdapter eAdapter;
    LinearLayout llnothing;
    TextView tvnothing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nota_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvlapnota);
        llnothing = findViewById(R.id.llnothing);
        tvnothing = findViewById(R.id.tvnothing);
        splapjenisnota = findViewById(R.id.splapjenisnota);
        splapbulannota = findViewById(R.id.splapbulannota);

        loading = new ProgressDialog(activity);
        loading.setMessage("Mohon Tunggu......");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();

        doLoadSpiner();
        doShowSpinerJenis();
    }

    private void getSemuaItem() {
        if (!loading.isShowing()) {
            loading.show();
        }
        data_notaArrayList = new ArrayList<>();
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);
        params.put("jenis", jenis);
        params.put("bulan", bulan);
        params.put("tahun", tahun);

        Log.e(tag, "param url_data     : " + url_data);
        Log.e(tag, "param Id Karyawan       : " + idkarywan);
        Log.e(tag, "param Jenis       : " + jenis);
        Log.e(tag, "param Bulan       : " + bulan);
        Log.e(tag, "param Tahun       : " + tahun);

        Call<ResponseLaporanNota> call = apiData.ApiService(url_data).getSemuaLaporanBBM(params);
        call.enqueue(new Callback<ResponseLaporanNota>() {
            @Override
            public void onResponse(Call<ResponseLaporanNota> call, Response<ResponseLaporanNota> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                String message = "";
                try {
                    data_notaArrayList = new ArrayList<>();
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        data_notaArrayList.addAll(response.body().getData());
                    } else {
                        //Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        message = response.body().getMessage();
                        Log.e(tag, response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Terjadi kesalahan #1";
                    //Toast.makeText(activity, "Terjadi Kesalahan #1", Toast.LENGTH_SHORT).show();
                }
                doShowData(message);
            }

            @Override
            public void onFailure(Call<ResponseLaporanNota> call, Throwable t) {
                //Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                Log.e(tag, t.toString());
                doShowData("Terjadi Kesalahan");
            }
        });
    }

    private void doShowData(String message) {
        if (loading.isShowing() || loading == null) {
            loading.dismiss();
        }
        eAdapter = new NotaItemAdapter(activity, data_notaArrayList);
        LinearLayoutManager eLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(eLayoutManager);
        recyclerView.setAdapter(eAdapter);

        int size = data_notaArrayList.size();
        if (size > 0) {
            llnothing.setVisibility(View.GONE);
        } else {
            tvnothing.setText(message);
            llnothing.setVisibility(View.VISIBLE);
            //Toast.makeText(activity, "Data tidak ada !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doShowSpinerJenis() {
        final String[] jenisArray = {"Transport", "Supplies", "Perawatan"};
        final ArrayList<String> jenis_array = new ArrayList<>();
        jenis_array.add("Pilih Jenis");
        for (int i = 0; i < jenisArray.length; i++) {
            String sp = jenisArray[i];
            jenis_array.add(sp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, jenis_array);
        splapjenisnota.setAdapter(adapter);
        splapjenisnota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    jenis = jenisArray[position - 1].toLowerCase();
                    Log.e(tag, "Jenis yang di pilih: " + jenis);
                } else {
                    jenis = "-";
                }
                getSemuaItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doLoadSpiner() {
        dataBulanNotaArrayList = new ArrayList<>();

        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", idkarywan);
        params.put("bulan", bulan);
        params.put("tahun", tahun);

        Log.e(tag, "id Karyawan: " + idkarywan);
        Log.e(tag, "bulan   : " + bulan);
        Log.e(tag, "tahun: " + tahun);
        Call<ResponseBulanNota> call = apiData.ApiService(url_data).getSemuaBulanNota(params);
        call.enqueue(new Callback<ResponseBulanNota>() {
            @Override
            public void onResponse(Call<ResponseBulanNota> call, Response<ResponseBulanNota> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                dataBulanNotaArrayList = new ArrayList<>();
                try {
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        dataBulanNotaArrayList.addAll(response.body().getData());
                    } else {
                        loading.dismiss();
                        Toast.makeText(NotaListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                doShowSpinerBulan();
            }

            @Override
            public void onFailure(Call<ResponseBulanNota> call, Throwable t) {
                doShowSpinerBulan();
            }
        });
    }

    private void doShowSpinerBulan() {
        ArrayList<String> bulan_array = new ArrayList<>();
        bulan_array.add("Pilih Bulan");
        for (int i = 0; i < dataBulanNotaArrayList.size(); i++) {
            String sp = dataBulanNotaArrayList.get(i).getNamabulan() + " " + dataBulanNotaArrayList.get(i).getTahun();
            bulan_array.add(sp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, bulan_array);
        splapbulannota.setAdapter(adapter);
        splapbulannota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    bulan = dataBulanNotaArrayList.get(position - 1).getBulan();
                    tahun = dataBulanNotaArrayList.get(position - 1).getTahun();
                    Log.e(tag, "onItemSelected : " + bulan + " " + tahun);
                } else {
                    bulan = "-";
                    tahun = "-";
                    //Toast.makeText(activity, "Silahkan Pilih Bulan ..", Toast.LENGTH_SHORT).show();
                }
                getSemuaItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
