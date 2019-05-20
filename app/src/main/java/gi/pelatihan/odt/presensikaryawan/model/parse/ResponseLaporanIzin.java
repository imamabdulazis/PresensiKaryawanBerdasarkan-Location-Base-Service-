package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanIzin;

public class ResponseLaporanIzin{

	@SerializedName("data")
	private List<DataLaporanIzin> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataLaporanIzin> data){
		this.data = data;
	}

	public List<DataLaporanIzin> getData(){
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
}