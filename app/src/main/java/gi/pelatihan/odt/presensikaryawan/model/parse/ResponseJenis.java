package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataJenis;

public class ResponseJenis{

	@SerializedName("data")
	private List<DataJenis> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataJenis> data){
		this.data = data;
	}

	public List<DataJenis> getData(){
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