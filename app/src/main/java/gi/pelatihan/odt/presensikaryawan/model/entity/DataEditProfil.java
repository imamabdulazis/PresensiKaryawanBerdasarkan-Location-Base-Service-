package gi.pelatihan.odt.presensikaryawan.model.entity;

import com.google.gson.annotations.SerializedName;

public class DataEditProfil {

	@SerializedName("jumlah_cuti")
	private int jumlahCuti;

	@SerializedName("jumlah_klaim")
	private int jumlahKlaim;

	@SerializedName("jumlah_izin")
	private int jumlahIzin;

	@SerializedName("jumlah_presensi")
	private int jumlahPresensi;

	public void setJumlahCuti(int jumlahCuti){
		this.jumlahCuti = jumlahCuti;
	}

	public int getJumlahCuti(){
		return jumlahCuti;
	}

	public void setJumlahKlaim(int jumlahKlaim){
		this.jumlahKlaim = jumlahKlaim;
	}

	public int getJumlahKlaim(){
		return jumlahKlaim;
	}

	public void setJumlahIzin(int jumlahIzin){
		this.jumlahIzin = jumlahIzin;
	}

	public int getJumlahIzin(){
		return jumlahIzin;
	}

	public void setJumlahPresensi(int jumlahPresensi){
		this.jumlahPresensi = jumlahPresensi;
	}

	public int getJumlahPresensi(){
		return jumlahPresensi;
	}
}