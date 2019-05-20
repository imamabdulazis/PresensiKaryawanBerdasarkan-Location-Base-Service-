package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataPresensiCek {

	@SerializedName("status_presensi")
	private String statusPresensi;

	@SerializedName("time_in")
	private String timeIn;

	@SerializedName("id_presensi")
	private String idPresensi;

	@SerializedName("time_out")
	private String timeOut;

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

	public void setIdPresensi(String idPresensi){
		this.idPresensi = idPresensi;
	}

	public String getIdPresensi(){
		return idPresensi;
	}

	public void setTimeOut(String timeOut){
		this.timeOut = timeOut;
	}

	public String getTimeOut(){
		return timeOut;
	}
}