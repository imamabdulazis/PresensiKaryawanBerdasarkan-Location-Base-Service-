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
import gi.pelatihan.odt.presensikaryawan.model.entity.DataPresensi;

public class PresensiItemAdapter extends RecyclerView.Adapter<PresensiItemAdapter.ViewHolder> {
    private Activity activity;

    private List<DataPresensi> list_data;
    private ArrayList<DataPresensi> array_data;

    public PresensiItemAdapter(Activity activity, ArrayList<DataPresensi> array_data) {
        this.activity = activity;
        this.list_data = array_data;
        this.array_data = new ArrayList<>();
        this.array_data.addAll(list_data);

        Log.e("PresensiItemAdapter", "PresensiItemAdapter : " + list_data.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_presensi, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String tanggal = list_data.get(position).getTglPresensi();
        String statusPresensi = list_data.get(position).getStatusPresensi();
        String time_in = list_data.get(position).getTimeIn();
        String time_out = list_data.get(position).getTimeOut();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date newDate = sdf.parse(tanggal);
            String pengajuan = sdf2.format(newDate);
            viewHolder.tvtanggal.setText(pengajuan);
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.tvtanggal.setText(tanggal);
        }

        if(statusPresensi.equalsIgnoreCase("hadir")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_dark));
        }else if(statusPresensi.equalsIgnoreCase("izin")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        }else if(statusPresensi.equalsIgnoreCase("cuti")){
            viewHolder.llright.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_dark));
        }

        viewHolder.tvstatus.setText(statusPresensi.toUpperCase());
        viewHolder.time_in.setText(time_in);
        viewHolder.time_out.setText(time_out);

        //klik
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String idkar = list_data.get(position).getIdKaryawan();
                String tanggal = list_data.get(position).getTglPresensi();
                String statusPresensi = list_data.get(position).getStatusPresensi();
                String time_in = list_data.get(position).getTimeIn();
                String time_out = list_data.get(position).getTimeOut();
                //Toast.makeText(activity, "klik : " + statusPresensi + " - " + tanggal, Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                final View promptView = layoutInflater.inflate(R.layout.pop_detail_presensi, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                final TextView tvdetprestgl = promptView.findViewById(R.id.tvdetprestgl);
                final TextView tvdetpesid = promptView.findViewById(R.id.tvdetpresid);
                final TextView tvdetpresstatus = promptView.findViewById(R.id.tvdetpresstatus);
                final TextView datetimein = promptView.findViewById(R.id.dettime_in);
                final TextView datetimeout = promptView.findViewById(R.id.dettime_out);

                tvdetpesid.setText(idkar);
                tvdetpresstatus.setText(statusPresensi);
                tvdetprestgl.setText(tanggal);
                datetimein.setText(time_in);
                datetimeout.setText(time_out);

                alertDialog.setView(promptView);
                alertDialog.show();
                */

            }
        });
    }

    @Override
    public int getItemCount() {
        return (list_data != null) ? list_data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idkar, tvtanggal, tvstatus, time_in, time_out;

        public LinearLayout llright;

        public ViewHolder(View itemView) {
            super(itemView);
            tvtanggal = itemView.findViewById(R.id.tvtglpres);
            tvstatus = itemView.findViewById(R.id.tvstatus);
            time_in = itemView.findViewById(R.id.time_in);
            time_out = itemView.findViewById(R.id.time_out);
            llright = itemView.findViewById(R.id.llright);
        }
    }

}
