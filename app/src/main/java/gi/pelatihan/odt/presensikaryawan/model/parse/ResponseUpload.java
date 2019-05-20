package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

public class ResponseUpload {

    private boolean berhasil;
    private String pesan;

    @SerializedName("id_karyawan")
    private String idkaryawan;

    @SerializedName("idklaimbbm")
    private String idklaimbbm;

    @SerializedName("jumlah_klaim")
    private String jumlah;

    @SerializedName("jenis")
    private String jenis;

    @SerializedName("success")
    private int success;

    @SerializedName("path")
    private String path;

    @SerializedName("message")
    private String message;

    public String getIdkaryawan() {
        return idkaryawan;
    }

    public void setIdkaryawan(String idkaryawan) {
        this.idkaryawan = idkaryawan;
    }

    public String getIdklaimbbm() {
        return idklaimbbm;
    }

    public void setIdklaimbbm(String idklaimbbm) {
        this.idklaimbbm = idklaimbbm;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getSuccess() {
        return success;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBerhasil() {
        return berhasil;
    }

    public void setBerhasil(boolean berhasil) {
        this.berhasil = berhasil;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
