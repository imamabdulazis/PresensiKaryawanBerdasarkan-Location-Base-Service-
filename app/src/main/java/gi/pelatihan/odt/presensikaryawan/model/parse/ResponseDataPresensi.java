package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensi;


public class ResponseDataPresensi {

	@SerializedName("data")
	private List<DataPresensi> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	@SerializedName("semuapresensi")
	private List<DataPresensi> semuapresensi;

	public void setData(List<DataPresensi> data){
		this.data = data;
	}

	public List<DataPresensi> getData(){
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

	public void setSemuapresensi(List<DataPresensi> semuapresensi){
		this.semuapresensi = semuapresensi;
	}

	public List<DataPresensi> getSemuapresensi(){
		return semuapresensi;
	}
}