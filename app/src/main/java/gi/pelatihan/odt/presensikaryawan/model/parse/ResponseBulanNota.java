package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataBulanNota;

public class ResponseBulanNota{

	@SerializedName("data")
	private List<DataBulanNota> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataBulanNota> data){
		this.data = data;
	}

	public List<DataBulanNota> getData(){
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