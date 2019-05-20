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
import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanIzin;

public class IzinItemAdapter extends RecyclerView.Adapter<IzinItemAdapter.ViewHolder> {
    private String tag = getClass().getCanonicalName();
    private Activity activity;
    private List<DataLaporanIzin> listData;
    private ArrayList<DataLaporanIzin> arrayData;

    public IzinItemAdapter(Activity activity, ArrayList<DataLaporanIzin> arrayData) {
        this.activity = activity;
        this.listData = arrayData;
        this.arrayData = new ArrayList<>();
        this.arrayData.addAll(listData);


        Log.e("PresensiItemAdapter", "PresensiItemAdapter : " + listData.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_izin, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String tglIzin = listData.get(position).getTglPengajuan();
        String ketIzin = listData.get(position).getKeteranganIzin();
        String sttsIzin = listData.get(position).getStatus().toUpperCase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

        try {
            Date newDate = sdf.parse(tglIzin);
            String pengajuan = sdf2.format(newDate);
            viewHolder.tvitemtanggal.setText(pengajuan);
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.tvitemtanggal.setText(tglIzin);
        }

        if(sttsIzin.equalsIgnoreCase("tunggu")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_dark));
        }else if(sttsIzin.equalsIgnoreCase("tolak")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        }else if(sttsIzin.equalsIgnoreCase("terima")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_dark));
        }

        viewHolder.tvitemstatus.setText(sttsIzin);
        viewHolder.tvitemketerangan.setText(ketIzin);

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
        TextView tvitemtanggal;
        TextView tvitemketerangan;
        TextView tvitemstatus;
        LinearLayout llright;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvitemtanggal = itemView.findViewById(R.id.tvitemtanggal);
            tvitemketerangan = itemView.findViewById(R.id.tvitemketerangan);
            tvitemstatus = itemView.findViewById(R.id.tvitemstatus);
            llright = itemView.findViewById(R.id.llright);
        }
    }
}
