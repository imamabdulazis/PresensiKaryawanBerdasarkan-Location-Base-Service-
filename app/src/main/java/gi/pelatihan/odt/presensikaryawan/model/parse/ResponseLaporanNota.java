package gi.pelatihan.odt.presensikaryawan.model.parse;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanNota;


public class ResponseLaporanNota {

	@SerializedName("data")
	private List<DataLaporanNota> data;

	@SerializedName("success")
	private int success;

	@SerializedName("message")
	private String message;

	public void setData(List<DataLaporanNota> data){
		this.data = data;
	}

	public Collection<? extends DataLaporanNota> getData(){
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

	@Override
 	public String toString(){
		return 
			"ResponseLaporanBBM{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}