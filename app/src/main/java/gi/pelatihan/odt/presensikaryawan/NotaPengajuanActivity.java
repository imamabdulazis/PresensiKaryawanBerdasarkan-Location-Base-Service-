package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gi.pelatihan.odt.presensikaryawan.model.parse.ApiData;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseNota;
import gi.pelatihan.odt.presensikaryawan.model.parse.ResponseUpload;
import gi.pelatihan.odt.presensikaryawan.settings.UrlAkses;
import gi.pelatihan.odt.presensikaryawan.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotaPengajuanActivity extends AppCompatActivity {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    Uri uri;
    ApiData apiData;
    ImageView ivBBM;
    ProgressDialog loading;
    Dialog mDialog;
    Spinner spjenisklaimnota;
    String jenis = "-";
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nota_pengajuan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivBBM = findViewById(R.id.imgBBM);
        mDialog = new Dialog(this);
        spjenisklaimnota = findViewById(R.id.spjenisklaimnota);

        Button btnAmbil = findViewById(R.id.btambilgambarbbm);
        btnAmbil.setOnClickListener(new View.OnClickListener() {
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

        final Button btnSubmit = findViewById(R.id.btsubmitbbm);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
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
        doShowSpiner();
    }


    //ambil dari galeri
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                ivBBM.setImageURI(uri);
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


    public void pop() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.pop_up_klaimnota, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        final EditText etjmlklaimnota = promptView.findViewById(R.id.etjmlklaimnota);
        Button btnNota = promptView.findViewById(R.id.btnNota);
        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base_url = UrlAkses.BASE_URL;
                String url_data = base_url + UrlAkses.URL_API;
                String idkarywan = "1";
                String jmlnota = etjmlklaimnota.getText().toString();

                HashMap<String, String> params = new HashMap<>();
                params.put("id_karyawan", idkarywan);
                params.put("jumlah_klaim", jmlnota);

                Log.e(tag, "param url_data       : " + url_data);
                Log.e(tag, "param Id Karyawan       : " + idkarywan);
                Log.e(tag, "param jumlah klaim   : " + jmlnota);

                Call<ResponseNota> call = apiData.ApiService(url_data).getDetailKlaimBBM(params);
                call.enqueue(new Callback<ResponseNota>() {
                    @Override
                    public void onResponse(Call<ResponseNota> call, Response<ResponseNota> response) {
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
                    public void onFailure(Call<ResponseNota> call, Throwable t) {
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

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

    private void doUploadfoto(File file) {
        String base_url = UrlAkses.BASE_URL;
        String url_data = base_url + UrlAkses.URL_API;

        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("foto",
                file.getName(), photoBody);

        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("jenis", createPartFromString(jenis));

        Call<ResponseUpload> call = ApiData.ApiService(url_data).getDataUploadNota(params, photoPart);
        call.enqueue(new Callback<ResponseUpload>() {
            @Override
            public void onResponse(Call<ResponseUpload> call, Response<ResponseUpload> response) {
                Log.e(tag, "url         : " + response.raw().request().url());
                Log.e(tag, "response    : " + new Gson().toJson(response.body()));
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
                Toast.makeText(activity, "Informasi\",\"Mohon maaf, terjadi masalah dengan koneksi.", Toast.LENGTH_SHORT).show();
                // pop_up.pop_error("Informasi","Mohon maaf, terjadi masalah dengan koneksi.","-","OK");
                Log.e(tag, t.toString());
            }
        });
    }

    private void doShowSpiner() {
        final String[] jenisArray = {"Transport", "Supplies", "Perawatan"};
        final ArrayList<String> jenis_array = new ArrayList<>();
        jenis_array.add("Pilih Jenis");
        for (int i = 0; i < jenisArray.length; i++) {
            String sp = jenisArray[i];
            jenis_array.add(sp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, jenis_array);
        spjenisklaimnota.setAdapter(adapter);
        spjenisklaimnota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    jenis = jenisArray[position - 1].toLowerCase();
                    Log.e(tag, "Jenis yang di pilih: " + jenis);
                } else {
                    jenis = "-";
                }
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
