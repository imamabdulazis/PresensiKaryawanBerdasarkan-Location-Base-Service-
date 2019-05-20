package gi.pelatihan.odt.presensikaryawan.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gi.pelatihan.odt.presensikaryawan.R;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataCuti;

public class CutiItemAdapter extends RecyclerView.Adapter<CutiItemAdapter.ViewHolder> {
    private String TAG = getClass().getCanonicalName();
    private Activity activity;
    private List<DataCuti> listData;
    private ArrayList<DataCuti> arrayData;

    public CutiItemAdapter(Activity activity, ArrayList<DataCuti> arrayData) {
        this.activity = activity;
        this.listData = arrayData;
        this.arrayData = new ArrayList<>();
        this.arrayData.addAll(listData);


        Log.e("PresensiItemAdapter", "PresensiItemAdapter : " + listData.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cuti, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String tglPengajuan = listData.get(position).getTglPengajuan();
        String jumlahCuti = listData.get(position).getJumlahCuti();
        String keterangan = listData.get(position).getKeterangan();
        String statusPengajuan = listData.get(position).getStatusPengajuan().toUpperCase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

        try {
            Date newDate = sdf.parse(tglPengajuan);
            String pengajuan = sdf2.format(newDate);
            viewHolder.tvitemtanggal.setText(pengajuan);
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.tvitemtanggal.setText(tglPengajuan);
        }

        if(statusPengajuan.equalsIgnoreCase("tunggu")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_dark));
        }else if(statusPengajuan.equalsIgnoreCase("tolak")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        }else if(statusPengajuan.equalsIgnoreCase("terima")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_dark));
        }


        viewHolder.tvitemjumlah.setText(jumlahCuti);
        viewHolder.tvitemketerangan.setText(keterangan);
        viewHolder.tvitemstatus.setText(statusPengajuan);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String idkar = listData.get(position).getIdKaryawan();
                String tglIzin = listData.get(position).getTglPengajuan();
                String ketIzin = listData.get(position).getKeteranganIzin();
                String sttsIzin = listData.get(position).getStatus();
                String gbr = listData.get(position).getPhotoIzin();

                Toast.makeText(activity, "Tanggal :" + listData.get(position).getTglPengajuan(), Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                final View promptView = layoutInflater.inflate(R.layout.pop_detail_laporan_izin, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                final TextView sidkar = promptView.findViewById(R.id.tvdetidkarizin);
                final TextView stvtglizin = promptView.findViewById(R.id.tvdettglizin);
                final TextView stvketzin = promptView.findViewById(R.id.tvdetketizin);
                final TextView stvstatus = promptView.findViewById(R.id.tvdetizinstatus);
                final TextView sgbrizin = promptView.findViewById(R.id.tvgbrizin);

                sidkar.setText(idkar);
                stvtglizin.setText(tglIzin);
                stvketzin.setText(ketIzin);
                stvstatus.setText(sttsIzin);
                sgbrizin.setText(gbr);

                alertDialog.setView(promptView);
                alertDialog.show();
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listData != null) ? listData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvitemtanggal;
        public TextView tvitemjumlah;
        public TextView tvitemketerangan;
        public TextView tvitemstatus;
        public LinearLayout llright;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvitemtanggal = itemView.findViewById(R.id.tvitemtanggal);
            tvitemjumlah = itemView.findViewById(R.id.tvitemjumlah);
            tvitemketerangan = itemView.findViewById(R.id.tvitemketerangan);
            tvitemstatus = itemView.findViewById(R.id.tvitemstatus);
            llright = itemView.findViewById(R.id.llright);
        }
    }
}
