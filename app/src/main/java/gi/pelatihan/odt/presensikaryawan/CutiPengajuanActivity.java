package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponsePengajuanCuti;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CutiPengajuanActivity extends AppCompatActivity implements View.OnClickListener {
    private String tag = getClass().getSimpleName();
    private Activity activity = this;

    private TextInputEditText etcutijumlah;
    private TextInputEditText etcutiwaktu;
    private TextInputEditText etcutialasan;

    private Button btcutipilih;
    private Button btcutiajukan;

    private SharedPreferences sharedpreferences1;
    private SharedPreferences.Editor editor;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cuti_pengajuan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences1 = getSharedPreferences("PengajuanCuti", Context.MODE_PRIVATE);
        editor = sharedpreferences1.edit();

        etcutijumlah = findViewById(R.id.etcutijumlah);
        etcutiwaktu = findViewById(R.id.etcutiwaktu);
        etcutialasan = findViewById(R.id.etcutialasan);
        btcutipilih = findViewById(R.id.btcutipilih);
        btcutiajukan = findViewById(R.id.btcutiajukan);

        btcutipilih.setOnClickListener(this);
        btcutiajukan.setOnClickListener(this);

        cutiSekarang();
    }

    private void cutiSekarang(){
        String sumber1=sharedpreferences1.getString("Str",null);

        if(sumber1!=null) {
            String[] sumber2 = sumber1.split(",");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM yyyy");

            String waktu = "";

            String tanggal = "";
            String bulantahun = "";

            int bulanSebelumnya = 0;

            int size = sumber2.length;
            for (int i = 0; i < size; i++) {
                try {
                    Date tempDate = sdf.parse(sumber2[i]);
                    String cekDate2 = sdf2.format(tempDate);

                    int bulanSekarang = tempDate.getMonth();
                    int tanggalSekarang = tempDate.getDate();

                    Log.e(tag, "bulan : " + bulanSebelumnya + " | " + bulanSekarang);

                    if (bulanSekarang == bulanSebelumnya || i == 0) {
                        if (i > 0) {
                            tanggal = tanggal + "," + tanggalSekarang;
                        } else {
                            tanggal = tanggalSekarang + "";
                        }
                    } else {
                        tanggal = tanggal + " " + bulantahun + "\n" + tanggalSekarang;
                    }

                    if (i == size - 1) {
                        waktu = tanggal + " " + cekDate2;
                    }

                    bulanSebelumnya = bulanSekarang;
                    bulantahun = cekDate2;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            etcutijumlah.setText(size+"");
            etcutiwaktu.setText(waktu);
        }else{
            etcutijumlah.setText("0");
            etcutiwaktu.setText(" ");
        }
    }

    private void popupTanggalCuti() {
        final AlertDialog alertD = new AlertDialog.Builder(activity).create();

        View view = LayoutInflater.from(activity).inflate(R.layout.pop_cuti_tanggal, null);
        final CalendarPickerView cvpopcalender = view.findViewById(R.id.cvpopcalender);
        Button btpopsimpan = view.findViewById(R.id.btpopsimpan);

        alertD.setView(view);
        alertD.show();

        //calender

        String sumber1=sharedpreferences1.getString("Str",null);

        Calendar limitCuti = Calendar.getInstance();
        limitCuti.add(Calendar.MONTH, 6);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<Date> arrayList2 = new ArrayList<>();
        if(sumber1!=null){
            Log.e(tag,"sumber load : "+sumber1);

            String[] sumber2 = sumber1.split(",");
            int size = sumber2.length;
            for (int i = 0; i < size; i++) {
                try {
                    Date newDate = sdf.parse(sumber2[i]);
                    arrayList2.add(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        cvpopcalender.init(new Date(), limitCuti.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE) //
                .withSelectedDates(arrayList2);


        btpopsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM yyyy");

                String hasil=null;
                String waktu="";

                String tanggal="";
                String bulantahun = "";

                int bulanSebelumnya = 0;

                int size = cvpopcalender.getSelectedDates().size();
                for (int i = 0; i < size; i++) {
                    Date tempDate = cvpopcalender.getSelectedDates().get(i);
                    String cekDate = sdf.format(tempDate);
                    if(i>0){
                        hasil = hasil+","+cekDate;
                    }else{
                        hasil = cekDate;
                    }

                    String cekDate2 = sdf2.format(tempDate);


                    int bulanSekarang = tempDate.getMonth();
                    int tanggalSekarang = tempDate.getDate();

                    Log.e(tag,"bulan : "+bulanSebelumnya+" | "+bulanSekarang);

                    if(bulanSekarang == bulanSebelumnya || i == 0){
                        if(i>0){
                            tanggal = tanggal+","+tanggalSekarang;
                        }else{
                            tanggal = tanggalSekarang+"";
                        }
                    }else{
                        tanggal = tanggal+" "+bulantahun+"\n"+tanggalSekarang;
                    }

                    if(i == size-1){
                        waktu = tanggal+" "+cekDate2;
                    }

                    bulanSebelumnya = bulanSekarang;
                    bulantahun = cekDate2;
                }

                etcutijumlah.setText(size+"");
                etcutiwaktu.setText(waktu);

                Log.e(tag,"hasil save : "+hasil);
                editor.putString("Str", hasil);
                editor.clear();
                editor.apply();

                alertD.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btcutipilih:{
                popupTanggalCuti();
                break;
            }case R.id.btcutiajukan:{
                loading = ProgressDialog.show(activity, null, "Harap Tunggu...", true, false);

                String keterangan = etcutialasan.getText().toString();
                doPostDataCuti(keterangan);
                break;
            }
        }
    }

    private void doPostDataCuti(String keterangan) {
        String sumber1=sharedpreferences1.getString("Str",null);

        int jumlahCuti = 0;
        if(sumber1!=null) {
            String[] sumber2 = sumber1.split(",");
            jumlahCuti = sumber2.length;
        }

        Log.e(tag,"jumlah cuti : "+jumlahCuti+"");
        Log.e(tag,"waktu cuti  : "+sumber1);

        String id_karyawan = "10";

        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;


        HashMap<String, String> params = new HashMap<>();
        params.put("id_karyawan", id_karyawan);
        params.put("waktu_cuti", sumber1);
        params.put("keterangan", keterangan);

        Log.e(tag, "param url_data       : " + url_data);
        Log.e(tag, "param id_karyawan    : " + id_karyawan);
        Log.e(tag, "param waktu_cuti     : " + sumber1);
        Log.e(tag, "param keterangan     : " + keterangan);

        Call<ResponsePengajuanCuti> call = ApiData.ApiService(url_data).postPengajuanCuti(params);
        call.enqueue(new Callback<ResponsePengajuanCuti>() {
            @Override
            public void onResponse(Call<ResponsePengajuanCuti> call, Response<ResponsePengajuanCuti> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                try {
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        loading.dismiss();
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(tag, response.body().getMessage());

                        activity.finish();
                    } else {
                        loading.dismiss();
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(tag, response.body().getMessage());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(activity, "Terjadi Kesalahan #1", Toast.LENGTH_SHORT).show();
                    Log.e(tag, "Terjadi Kesalahan #1");
                }
            }

            @Override
            public void onFailure(Call<ResponsePengajuanCuti> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                Log.e(tag, t.toString());
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
