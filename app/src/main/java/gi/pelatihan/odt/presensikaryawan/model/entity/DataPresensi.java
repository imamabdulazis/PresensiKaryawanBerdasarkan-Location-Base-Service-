package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;


public class DataPresensi {

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("status_presensi")
	private String statusPresensi;

	@SerializedName("time_in")
	private String timeIn;

	@SerializedName("time_out")
	private String timeOut;

	@SerializedName("tgl_presensi")
	private String tglPresensi;

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setStatusPresensi(String statusPresensi){
		this.statusPresensi = statusPresensi;
	}

	public String getStatusPresensi(){
		return statusPresensi;
	}

	public void setTimeIn(String timeIn){
		this.timeIn = timeIn;
	}

	public String getTimeIn(){
		return timeIn;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public void setTglPresensi(String tglPresensi){
		this.tglPresensi = tglPresensi;
	}

	public String getTglPresensi(){
		return tglPresensi;
	}
}