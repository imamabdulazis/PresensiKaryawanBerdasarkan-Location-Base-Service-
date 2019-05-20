package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataLaporanIzin {

	@SerializedName("tgl_pengajuan")
	private String tglPengajuan;

	@SerializedName("tgl_approve")
	private Object tglApprove;

	@SerializedName("id_admin")
	private Object idAdmin;

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("id_pengajuan_izin")
	private String idPengajuanIzin;

	@SerializedName("photo_izin")
	private String photoIzin;

	@SerializedName("id_presensi")
	private String idPresensi;

	@SerializedName("ketrangan_izin")
	private String ketranganIzin;

	@SerializedName("status")
	private String status;

	public void setTglPengajuan(String tglPengajuan){
		this.tglPengajuan = tglPengajuan;
	}

	public String getTglPengajuan(){
		return tglPengajuan;
	}

	public void setTglApprove(Object tglApprove){
		this.tglApprove = tglApprove;
	}

	public Object getTglApprove(){
		return tglApprove;
	}

	public void setIdAdmin(Object idAdmin){
		this.idAdmin = idAdmin;
	}

	public Object getIdAdmin(){
		return idAdmin;
	}

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setIdPengajuanIzin(String idPengajuanIzin){
		this.idPengajuanIzin = idPengajuanIzin;
	}

	public String getIdPengajuanIzin(){
		return idPengajuanIzin;
	}

	public void setPhotoIzin(String photoIzin){
		this.photoIzin = photoIzin;
	}

	public String getPhotoIzin(){
		return photoIzin;
	}

	public void setIdPresensi(String idPresensi){
		this.idPresensi = idPresensi;
	}

	public String getIdPresensi(){
		return idPresensi;
	}

	public void setKeteranganIzin(String ketranganIzin){
		this.ketranganIzin = ketranganIzin;
	}

	public String getKeteranganIzin(){
		return ketranganIzin;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}