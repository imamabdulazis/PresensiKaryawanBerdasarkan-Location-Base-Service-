package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseNota {

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	@SerializedName("id_karyawan")
	private String idkaryawan;

	@SerializedName("jumlah_klaim")
	private String jumlah;


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

	public String getIdkaryawan() {
		return idkaryawan;
	}

	public void setIdkaryawan(String idkaryawan) {
		this.idkaryawan = idkaryawan;
	}

	public String getJumlah() {
		return jumlah;
	}

	public void setJumlah(String jumlah) {
		this.jumlah = jumlah;
	}
}