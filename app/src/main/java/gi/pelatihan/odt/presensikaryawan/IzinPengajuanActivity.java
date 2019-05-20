package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseIzin;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseUpload;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import gi.pelatihan.odt.presensikaryawan.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IzinPengajuanActivity extends AppCompatActivity {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    Uri uri;
    ImageView ivizin;
    EditText etketizin;
    Button btambilfotoizin;
    Button btsimpanizin;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_izin_pengajuan);
        getSupportActionBar().setTitle("Pengajuan Izin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ivizin = findViewById(R.id.ivIzin);
        btambilfotoizin = findViewById(R.id.btambilgambarizin);
        btsimpanizin = findViewById(R.id.btsimpanizin);

        btambilfotoizin.setOnClickListener(new View.OnClickListener() {
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

        btsimpanizin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(activity, null, "Harap Tunggu...", true, false);
                if (uri != null) {
                    File file = FileUtils.getFile(activity, uri);
                    doUploadfoto(file);
                    pop();
                }
            }
        });
    }

    //ambil dari galeri
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                ivizin.setImageURI(uri);
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

    private void doUploadfoto(File file) {
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("foto",
                file.getName(), photoBody);
        //String idKaryawan="1";
        String jenis = "izin";
        HashMap<String, RequestBody> params = new HashMap<>();
        //params.put("id_karyawan",createPartFromString(idKaryawan));
        params.put("jenis", createPartFromString(jenis));

        Call<ResponseUpload> call = ApiData.ApiService(url_data).getDataUploadFoto(params, photoPart);
        call.enqueue(new Callback<ResponseUpload>() {
            @Override
            public void onResponse(Call<ResponseUpload> call, Response<ResponseUpload> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
                String ssukses = response.body().getSuccess() + "";
                String sidkaryawan = response.body().getIdkaryawan();
                String sjenis = response.body().getJenis();
                String spesan = response.body().getMessage();
                String spath = response.body().getPath();
                Log.e(tag, "ssukses : " + ssukses);
                Log.e(tag, "sidkaryawan : " + sidkaryawan);
                Log.e(tag, "sjenis : " + sjenis);
                Log.e(tag, "spesan : " + spesan);
                Log.e(tag, "spath : " + spath);
                if (response.isSuccessful()) {
                    loading.dismiss();
                    Toast.makeText(activity, "BERHASIL UPLOAD FOTO BUKTI", Toast.LENGTH_SHORT).show();
                } else {
                    loading.dismiss();
                    Toast.makeText(activity, "GAGAL UPLOAD FOTO BUKTI", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseUpload> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                Log.e(tag, t.toString());
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

    //popup izin
    public void pop() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.pop_up_izin, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        final EditText etjmlizin = promptView.findViewById(R.id.etjmlizin);

        Button btnBBM = promptView.findViewById(R.id.btnBBM);
        btnBBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base_url = UrlAkses.BASE_URL;
                String url_data = base_url + UrlAkses.URL_API;
                String idkar = "1";
                String keterangan = etjmlizin.getText().toString();

                HashMap<String, String> params = new HashMap<>();
                params.put("id_karyawan", idkar);
                params.put("keterangan_izin", keterangan);

                Log.e(tag, "param url_data       : " + url_data);
                Log.e(tag, "param id karyawan  : " + idkar);
                Log.e(tag, "param keterangan  : " + keterangan);

                Call<ResponseIzin> call = ApiData.ApiService(url_data).getDetailIzin(params);
                call.enqueue(new Callback<ResponseIzin>() {
                    @Override
                    public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
                        Log.e(tag, "url         : " + response.raw().request().url());
                        Log.e(tag, "response    : " + new Gson().toJson(response.body()));

                        if (response.isSuccessful()) {
                            loading.dismiss();
                            Toast.makeText(activity, "BERHASIL", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.dismiss();
                            Toast.makeText(activity, "GAGAL", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseIzin> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(activity, "Terjadi Kesalahan #2", Toast.LENGTH_SHORT).show();
                        // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                        Log.e(tag, t.toString());
                    }
                });

            }
        });

        alertD.setView(promptView);
        alertD.show();
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