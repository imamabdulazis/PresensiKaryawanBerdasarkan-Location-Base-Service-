package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePresensi{

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	@SerializedName("id_presensi")
	private String idpresensi;

	@SerializedName("id_kantor")
	private String idkantor;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("imei")
	private String imai;

	@SerializedName("merk_hp")
	private String merkhp;

	public void setData(List<Object> data){
		this.data = data;
	}

	public List<Object> getData(){
		return data;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public String getIdpresensi() {
		return idpresensi;
	}

	public void setIdpresensi(String idpresensi) {
		this.idpresensi = idpresensi;
	}

	public String getIdkantor() {
		return idkantor;
	}

	public void setIdkantor(String idkantor) {
		this.idkantor = idkantor;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getImai() {
		return imai;
	}

	public void setImai(String imai) {
		this.imai = imai;
	}

	public String getMerkhp() {
		return merkhp;
	}

	public void setMerkhp(String merkhp) {
		this.merkhp = merkhp;
	}
}