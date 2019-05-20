package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataPresensiInput {

	@SerializedName("id_presensi")
	private String idPresensi;

	@SerializedName("presensi")
	private String presensi;

	public String getPresensi() {
		return presensi;
	}

	public void setPresensi(String presensi) {
		this.presensi = presensi;
	}

	public void setIdPresensi(String idPresensi){
		this.idPresensi = idPresensi;
	}

	public String getIdPresensi(){
		return idPresensi;
	}
}