package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataCuti {

	@SerializedName("tgl_pengajuan")
	private String tglPengajuan;

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("waktu_cuti")
	private String waktuCuti;

	@SerializedName("id_cuti")
	private String idCuti;

	@SerializedName("status_pengajuan")
	private String statusPengajuan;

	@SerializedName("jumlah_cuti")
	private String jumlahCuti;

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("tgl_approv")
	private String tglApprov;

	public void setTglPengajuan(String tglPengajuan){
		this.tglPengajuan = tglPengajuan;
	}

	public String getTglPengajuan(){
		return tglPengajuan;
	}

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setWaktuCuti(String waktuCuti){
		this.waktuCuti = waktuCuti;
	}

	public String getWaktuCuti(){
		return waktuCuti;
	}

	public void setIdCuti(String idCuti){
		this.idCuti = idCuti;
	}

	public String getIdCuti(){
		return idCuti;
	}

	public void setStatusPengajuan(String statusPengajuan){
		this.statusPengajuan = statusPengajuan;
	}

	public String getStatusPengajuan(){
		return statusPengajuan;
	}

	public void setJumlahCuti(String jumlahCuti){
		this.jumlahCuti = jumlahCuti;
	}

	public String getJumlahCuti(){
		return jumlahCuti;
	}

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setTglApprov(String tglApprov){
		this.tglApprov = tglApprov;
	}

	public String getTglApprov(){
		return tglApprov;
	}
}