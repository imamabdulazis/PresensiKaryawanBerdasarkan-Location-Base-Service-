package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataBulan {

	@SerializedName("tahun")
	private String tahun;

	@SerializedName("id_karyawan")
	private String idKaryawan;

	@SerializedName("bulan")
	private String bulan;

	@SerializedName("namabulan")
	private String namabulan;

	public void setTahun(String tahun){
		this.tahun = tahun;
	}

	public String getTahun(){
		return tahun;
	}

	public void setIdKaryawan(String idKaryawan){
		this.idKaryawan = idKaryawan;
	}

	public String getIdKaryawan(){
		return idKaryawan;
	}

	public void setBulan(String bulan){
		this.bulan = bulan;
	}

	public String getBulan(){
		return bulan;
	}

	public void setNamabulan(String namabulan){
		this.namabulan = namabulan;
	}

	public String getNamabulan(){
		return namabulan;
	}
}