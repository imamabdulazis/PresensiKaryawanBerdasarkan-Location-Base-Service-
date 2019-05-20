package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataLogin{

	@SerializedName("foto_url")
	private String fotoUrl;

	@SerializedName("foto")
	private String foto;

	@SerializedName("no_karyawan")
	private String noKaryawan;

	@SerializedName("nama_karyawan")
	private String namaKaryawan;

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("nohp")
	private String nohp;

	@SerializedName("status_karyawan")
	private String statusKaryawan;

	@SerializedName("jenis_kelamin")
	private String jenisKelamin;

	@SerializedName("tgl_lahir")
	private String tglLahir;

	@SerializedName("email")
	private String email;

	@SerializedName("alamat")
	private String alamat;

	public void setFotoUrl(String fotoUrl){
		this.fotoUrl = fotoUrl;
	}

	public String getFotoUrl(){
		return fotoUrl;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setNoKaryawan(String noKaryawan){
		this.noKaryawan = noKaryawan;
	}

	public String getNoKaryawan(){
		return noKaryawan;
	}

	public void setNamaKaryawan(String namaKaryawan){
		this.namaKaryawan = namaKaryawan;
	}

	public String getNamaKaryawan(){
		return namaKaryawan;
	}

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setNohp(String nohp){
		this.nohp = nohp;
	}

	public String getNohp(){
		return nohp;
	}

	public void setStatusKaryawan(String statusKaryawan){
		this.statusKaryawan = statusKaryawan;
	}

	public String getStatusKaryawan(){
		return statusKaryawan;
	}

	public void setJenisKelamin(String jenisKelamin){
		this.jenisKelamin = jenisKelamin;
	}

	public String getJenisKelamin(){
		return jenisKelamin;
	}

	public void setTglLahir(String tglLahir){
		this.tglLahir = tglLahir;
	}

	public String getTglLahir(){
		return tglLahir;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}
}