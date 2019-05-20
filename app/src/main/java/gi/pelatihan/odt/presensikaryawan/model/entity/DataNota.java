package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataNota {

	@SerializedName("tgl_pengajuan")
	private String tglPengajuan;

	@SerializedName("id_klaim_bbm_detail")
	private String idKlaimBbmDetail;

	@SerializedName("jumlah_klaim")
	private String jumlahKlaim;

	@SerializedName("foto")
	private String foto;

	@SerializedName("id_admin")
	private String idAdmin;

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("status_klaim")
	private String statusKlaim;

	@SerializedName("id_klaim_bbm")
	private String idKlaimBbm;

	@SerializedName("jumlah_approv")
	private String jumlahApprov;

	@SerializedName("tgl_approv")
	private String tglApprov;

	public void setTglPengajuan(String tglPengajuan){
		this.tglPengajuan = tglPengajuan;
	}

	public String getTglPengajuan(){
		return tglPengajuan;
	}

	public void setIdKlaimBbmDetail(String idKlaimBbmDetail){
		this.idKlaimBbmDetail = idKlaimBbmDetail;
	}

	public String getIdKlaimBbmDetail(){
		return idKlaimBbmDetail;
	}

	public void setJumlahKlaim(String jumlahKlaim){
		this.jumlahKlaim = jumlahKlaim;
	}

	public String getJumlahKlaim(){
		return jumlahKlaim;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setIdAdmin(String idAdmin){
		this.idAdmin = idAdmin;
	}

	public String getIdAdmin(){
		return idAdmin;
	}

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setStatusKlaim(String statusKlaim){
		this.statusKlaim = statusKlaim;
	}

	public String getStatusKlaim(){
		return statusKlaim;
	}

	public void setIdKlaimBbm(String idKlaimBbm){
		this.idKlaimBbm = idKlaimBbm;
	}

	public String getIdKlaimBbm(){
		return idKlaimBbm;
	}

	public void setJumlahApprov(String jumlahApprov){
		this.jumlahApprov = jumlahApprov;
	}

	public String getJumlahApprov(){
		return jumlahApprov;
	}

	public void setTglApprov(String tglApprov){
		this.tglApprov = tglApprov;
	}

	public String getTglApprov(){
		return tglApprov;
	}
}