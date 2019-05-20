package gi.pelatihan.odt.presensikaryawan.model.parse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public class ApiData {
    private static Iface ApiService;


    public static Iface ApiService(String url){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService = retrofit.create(Iface.class);
        return ApiService;
    }

    public interface Iface {
        @GET("login.php")
        Call<ResponseLogin> getDataLogin(@QueryMap HashMap<String, String> params);

        @GET("register.php")
        Call<ResponseRegister> getDataRegister(@QueryMap HashMap<String, String> params);

        @GET("pengajuancuti.php")
        Call<ResponsePengajuanCuti> postPengajuanCuti(@QueryMap HashMap<String, String> params);

        @Multipart
        @POST("uploadfoto.php")
        Call<ResponseUpload> getDataUploadFoto(@PartMap HashMap<String, RequestBody> params,
                                               @Part MultipartBody.Part foto);

        @Multipart
        @POST("uploadnota.php")
        Call<ResponseUpload> getDataUploadNota(@PartMap HashMap<String, RequestBody> params,
                                               @Part MultipartBody.Part foto);

        @GET("pengajuanizin.php")
        Call<ResponseIzin> getDetailIzin(@QueryMap HashMap<String, String> params);

        @GET("klaimnota.php")
        Call<ResponseNota> getDetailKlaimBBM(@QueryMap HashMap<String, String> params);

        @GET("presensimasuk.php")
        Call<ResponsePresensi> getDataPresensiMasuk(@QueryMap HashMap<String, String> params);

        @GET("informasipresensi.php")
        Call<ResponseDataPresensi> getSemuaLaporanPresensi(@QueryMap HashMap<String, String> params);

        @GET("informasinota.php")
        Call<ResponseLaporanNota> getSemuaLaporanBBM(@QueryMap HashMap<String, String> params);

        @GET("listbulan.php")
        Call<ResponseBulan> getSemuaBulan(@QueryMap HashMap<String, String> params);

        @GET("listjenisnota.php")
        Call<ResponseJenis> getSemuaJenis(@QueryMap HashMap<String, String> params);

        @GET("listbulannota.php")
        Call<ResponseBulanNota> getSemuaBulanNota(@QueryMap HashMap<String, String> params);

        @GET("informasiizin.php")
        Call<ResponseLaporanIzin> getSemuaDataIzin(@QueryMap HashMap<String, String> params);


        @GET("listbulancuti.php")
        Call<ResponseBulan> getDataBulanCuti(@QueryMap HashMap<String, String> params);

        @GET("listcuti.php")
        Call<ResponseCuti> getDataCuti(@QueryMap HashMap<String, String> params);

        @GET("presensicek.php")
        Call<ResponsePresensiCek> getDataPresensiCek(@QueryMap HashMap<String, String> params);

        @GET("presensiinput.php")
        Call<ResponsePresensiInput> postDataPresensi(@QueryMap HashMap<String, String> params);

        @GET("dataprofil.php")
        Call<ResponseProfil> getDataProfil(@QueryMap HashMap<String,String> params);

        @GET("editprofile.php")
        Call<ResponseEditProfil> getEditProfil(@QueryMap HashMap<String,String>  params);
    }

}
