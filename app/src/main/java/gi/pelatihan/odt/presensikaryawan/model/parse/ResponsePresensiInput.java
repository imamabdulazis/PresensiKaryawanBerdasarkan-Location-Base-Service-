package gi.pelatihan.odt.presensikaryawan.model.parse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensiInput;

public class ResponsePresensiInput{

	@SerializedName("data")
	private List<DataPresensiInput> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataPresensiInput> data){
		this.data = data;
	}

	public List<DataPresensiInput> getData(){
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