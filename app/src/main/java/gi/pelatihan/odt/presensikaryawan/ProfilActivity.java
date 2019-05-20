package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseEditProfil;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseProfil;
import gi.pelatihan.odt.presensikaryawan.model.shared.Data_Pref;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private  Uri uri;

    private ImageView ivproffoto;
    private TextView tvprofnama;
    private TextView tvprofemail;
    private TextView tvprofnohp;
    private TextView tvprofiljmlhadir;
    private TextView tvprofiljmlizin;
    private TextView tvprofiljmlcuti;
    private TextView tvprofiljmlnota;
    private TextView tvprofnokaryawan;
    private Button btneditprofil;
    private Button edtsimpan;

    private SharedPreferences spLogin;
    private String id_karyawan;

    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ivproffoto = findViewById(R.id.ivproffoto);
        tvprofnama = findViewById(R.id.tvprofnama);
        tvprofemail = findViewById(R.id.tvprofemail);
        tvprofnohp = findViewById(R.id.tvprofnohp);
        //jml
        tvprofiljmlhadir=findViewById(R.id.tvprofjmlhadir);
        tvprofiljmlizin=findViewById(R.id.tvprofjmlizin);
        tvprofiljmlcuti=findViewById(R.id.tvprofjmlcuti);
        tvprofiljmlnota=findViewById(R.id.tvprofklaimnota);
        //
        tvprofnokaryawan = findViewById(R.id.tvprofnokaryawan);
        btneditprofil= findViewById(R.id.btneditprofil);

        ivproffoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);

                } else {
                    openGallery();
                }
            }
        });

        btneditprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               popup();
            }
        });

        spLogin=getSharedPreferences(Data_Pref.LoginPref,Context.MODE_PRIVATE);
        id_karyawan=spLogin.getString(Data_Pref.id_karyawan,"-");
        getDataProfil();
    }

    private void popup() {
        LayoutInflater layoutInflater=LayoutInflater.from(activity);
        View promptView=layoutInflater.inflate(R.layout.pop_up_edit_profil,null);
        AlertDialog alertDialog=new AlertDialog.Builder(activity).create();
        final EditText edtinput_nama=promptView.findViewById(R.id.edtinput_nama);
        final EditText edtinput_nomor=promptView.findViewById(R.id.edtinput_nomor);
        final EditText edtinput_email=promptView.findViewById(R.id.edtinput_email);
        edtsimpan=promptView.findViewById(R.id.edtbtnsimpan);

        edtsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base_url=UrlAkses.BASE_URL;
                String data_url=base_url+UrlAkses.URL_API;

                String etnama=edtinput_nama.getText().toString();
                String ettelp=edtinput_nomor.getText().toString();
                String etemail=edtinput_email.getText().toString();
                HashMap<String, String> params=new HashMap<>();
                params.put("id_karyawan",id_karyawan);
                params.put("nama_karyawan",etnama);
                params.put("nohp",ettelp);
                params.put("email",etemail);

                Log.e(tag, "param url_data       : " + data_url);
                Log.e(tag, "param Id Karyawan       : " +id_karyawan );
                Log.e(tag, "param nama       : " +etnama );
                Log.e(tag, "param nomor telepon  : " + ettelp);
                Log.e(tag, "param email  : " + etemail);

                Call<ResponseEditProfil> call=ApiData.ApiService(data_url).getEditProfil(params);
                call.enqueue(new Callback<ResponseEditProfil>() {
                    @Override
                    public void onResponse(Call<ResponseEditProfil> call, Response<ResponseEditProfil> response) {
                        Log.e(tag, "url         : " + response.raw().request().url());
                        Log.d(tag, "Keterangan: "+response.body().getSuccess());
                            if (response.isSuccessful()){
                                Toast.makeText(activity, response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                                //loading=ProgressDialog.show(activity,null,"Harap Tunggu...",true,false);
                            }else{
                                Toast.makeText(activity, "GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        setProfil();
                    }

                    @Override
                    public void onFailure(Call<ResponseEditProfil> call, Throwable t) {

                    }
                });
            }
        });

        alertDialog.setView(promptView);
        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfil();
    }

    private void setProfil(){
        SharedPreferences spLogin = getSharedPreferences(Data_Pref.LoginPref, Context.MODE_PRIVATE);
        String nama_karyawan = spLogin.getString(Data_Pref.nama_karyawan,"-");
        String foto_url = spLogin.getString(Data_Pref.foto_url,"-");
        String email = spLogin.getString(Data_Pref.email,"-");
        String nohp = spLogin.getString(Data_Pref.nohp,"-");
        String no_karyawan = spLogin.getString(Data_Pref.no_karyawan,"-");

        Picasso.get()
                .load(foto_url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivproffoto);

        tvprofnama.setText(nama_karyawan);
        tvprofemail.setText(email);
        tvprofnohp.setText(nohp);
        tvprofnokaryawan.setText(no_karyawan);
    }

    //ambil dari galeri
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                ivproffoto.setImageURI(uri);
            }
        }
    }

    //buka galeri
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE);
    }

    public void getDataProfil(){
        String base_url= UrlAkses.BASE_URL;
        final String data_url=base_url+UrlAkses.URL_API;

        HashMap<String,String> params=new HashMap<>();
        params.put("id_karyawan",id_karyawan);

        Call<ResponseProfil> call= ApiData.ApiService(data_url).getDataProfil(params);
        call.enqueue(new Callback<ResponseProfil>() {
            @Override
            public void onResponse(Call<ResponseProfil> call, Response<ResponseProfil> response) {
                Log.e(tag, "url : "+response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                String ssukses = response.body().getMessage() + "";
                int jml_hadir=response.body().getData().get(0).getJumlahPresensi();
                int jml_izin=response.body().getData().get(0).getJumlahIzin();
                int jml_cuti=response.body().getData().get(0).getJumlahCuti();
                int jml_klaimnota=response.body().getData().get(0).getJumlahKlaim();
                Log.e(tag, "Keterangan: "+ssukses );
                try {
                    if (response.isSuccessful()){
                        tvprofiljmlhadir.setText(String.valueOf(jml_hadir));
                        tvprofiljmlizin.setText(String.valueOf(jml_izin));
                        tvprofiljmlcuti.setText(String.valueOf(jml_cuti));
                        tvprofiljmlnota.setText(String.valueOf(jml_klaimnota));
                    }else{
                        Toast.makeText(activity, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseProfil> call, Throwable t) {
                Log.e(tag, "onFailure: "+t.toString());
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
