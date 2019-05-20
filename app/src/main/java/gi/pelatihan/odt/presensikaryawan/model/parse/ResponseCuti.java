package gi.pelatihan.odt.presensikaryawan.model.parse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataCuti;

public class ResponseCuti{

	@SerializedName("data")
	private List<DataCuti> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataCuti> data){
		this.data = data;
	}

	public List<DataCuti> getData(){
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