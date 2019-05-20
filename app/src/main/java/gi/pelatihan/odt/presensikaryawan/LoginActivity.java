package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataLogin;
import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseLogin;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    private TextInputEditText etlogemail;
    private TextInputEditText etlogpassword;
    private Button btloglogin;
    private Button btlogregister;
    private ProgressDialog loading;

    private ArrayList<DataLogin> data_loginArrayList = new ArrayList<>();

    private SharedPreferences spLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        getSupportActionBar().hide();

        spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        String id_karyawan = spLogin.getString(Data_Pref.id_karyawan,"-");

        if(!id_karyawan.equalsIgnoreCase("-")){
            Intent intent = new Intent(this,BerandaActivity.class);
            startActivity(intent);
            finish();
        }

        etlogemail = findViewById(R.id.etlogemail);
        etlogpassword = findViewById(R.id.etlogpassword);
        btloglogin = findViewById(R.id.btloglogin);
        btlogregister = findViewById(R.id.btlogregis);

        btloglogin.setOnClickListener(this);
        btlogregister.setOnClickListener(this);
    }

    private void doGetDataLogin(String suser, String spass) {

        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        HashMap<String, String> params = new HashMap<>();
        params.put("username", suser);
        params.put("password", spass);

        Log.e(tag, "param url_data     : " + url_data);
        Log.e(tag, "param username     : " + suser);
        Log.e(tag, "param password     : " + spass);

        Call<ResponseLogin> call = ApiData.ApiService(url_data).getDataLogin(params);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                try {
                    data_loginArrayList = new ArrayList<>();
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        loading.dismiss();
                        data_loginArrayList.addAll(response.body().getData());

                        doShowDataLogin();
                    } else {
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //pop_up.pop_error("Informasi",response.body().getMessage(),"-","OK");
                        Log.e(tag, response.body().getMessage());
                        loading.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(activity, "Terjadi Kesalahan #1", Toast.LENGTH_SHORT).show();
                    //pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                Log.e(tag, t.toString());
            }
        });
    }

    private void doShowDataLogin() {
        SharedPreferences.Editor editorLogin = spLogin.edit();
        editorLogin.putString(Data_Pref.no_karyawan,data_loginArrayList.get(0).getNoKaryawan());
        editorLogin.putString(Data_Pref.tgl_lahir,data_loginArrayList.get(0).getTglLahir());
        editorLogin.putString(Data_Pref.alamat,data_loginArrayList.get(0).getAlamat());
        editorLogin.putString(Data_Pref.nama_karyawan,data_loginArrayList.get(0).getNamaKaryawan());
        editorLogin.putString(Data_Pref.id_karyawan,data_loginArrayList.get(0).getIdKaryawan());
        editorLogin.putString(Data_Pref.nohp,data_loginArrayList.get(0).getNohp());
        editorLogin.putString(Data_Pref.status_karyawan,data_loginArrayList.get(0).getStatusKaryawan());
        editorLogin.putString(Data_Pref.foto,data_loginArrayList.get(0).getFoto());
        editorLogin.putString(Data_Pref.foto_url,data_loginArrayList.get(0).getFotoUrl());
        editorLogin.putString(Data_Pref.jenis_kelamin,data_loginArrayList.get(0).getJenisKelamin());
        editorLogin.putString(Data_Pref.email,data_loginArrayList.get(0).getEmail());
        editorLogin.apply();

        Intent intent = new Intent(this,BerandaActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btloglogin:{
                loading = ProgressDialog.show(activity, null, "Harap Tunggu...", true, false);

                String user = etlogemail.getText().toString();
                String pass = etlogpassword.getText().toString();
                doGetDataLogin(user, pass);
                //startActivity(new Intent(activity,BerandaActivity.class));
                break;
            }case R.id.btlogregis:{
                etlogemail.setText("");
                etlogpassword.setText("");

                startActivity(new Intent(activity, RegisterActivity.class));
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(activity, "Minimize", Toast.LENGTH_SHORT).show();
        moveTaskToBack(true);
    }
}
