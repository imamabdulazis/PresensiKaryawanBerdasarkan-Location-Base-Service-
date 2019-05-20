package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseRegister;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    private TextInputEditText etregnokar;
    private TextInputEditText etregnama;
    private TextInputEditText etregnohp;
    private TextInputEditText etregemail;
    private TextInputEditText etPassword;
    private Button btregregis;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        getSupportActionBar().hide();

        etregnokar = findViewById(R.id.input_nokar);
        etregnama = findViewById(R.id.input_nama);
        etregnohp = findViewById(R.id.input_nomor);
        etregemail = findViewById(R.id.input_email);
        etPassword = findViewById(R.id.input_password);
        btregregis = findViewById(R.id.btregregis);

        btregregis.setOnClickListener(this);
    }

    private void doGetDataRegister(String snokar, String snama_kar, String snohp, String semail, String spass) {
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("no_karyawan", snokar);
        params.put("nama_karyawan", snama_kar);
        params.put("nohp", snohp);
        params.put("email", semail);
        params.put("pass_word", spass);

        Log.e(tag, "param url_data       : " + url_data);
        Log.e(tag, "param nomor karyawan : " + snokar);
        Log.e(tag, "param nama karyawan  : " + snama_kar);
        Log.e(tag, "param nomor telepon  : " + snohp);
        Log.e(tag, "param email          : " + semail);
        Log.e(tag, "param pass_wWword       : " + snama_kar);


        Call<ResponseRegister> call = ApiData.ApiService(url_data).getDataRegister(params);
        call.enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                try {
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        loading.dismiss();
                        Toast.makeText(activity, "Berhasil Daftar Akun..", Toast.LENGTH_SHORT).show();

                        activity.finish();

                    } else {
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                        //pop_up.pop_error("Informasi",response.body().getMessage(),"-","OK");
                        Log.e(tag, response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(activity, "Terjadi Kesalahan #1", Toast.LENGTH_SHORT).show();
                    // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                }

            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                Log.e(tag, t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btregregis:{
                loading = ProgressDialog.show(activity, null, "Harap Tunggu...", true, false);
                String nokar = etregnokar.getText().toString();
                String nama = etregnama.getText().toString();
                String nohp = etregnohp.getText().toString();
                String email = etregemail.getText().toString();
                String pass = etPassword.getText().toString();


                doGetDataRegister(nokar, nama, nohp, email, pass);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
