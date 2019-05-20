package gi.pelatihan.odt.presensikaryawan.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gi.pelatihan.odt.presensikaryawan.R;
import gi.pelatihan.odt.presensikaryawan.model.entity.DataLaporanNota;
import gi.pelatihan.odt.presensikaryawan.utils.ConverterUtils;

public class NotaItemAdapter extends RecyclerView.Adapter<NotaItemAdapter.ViewHolder> {
    private Activity activity;
    private List<DataLaporanNota> list_data;
    private ArrayList<DataLaporanNota> array_data;

    public NotaItemAdapter(Activity activity, ArrayList<DataLaporanNota> array_data) {
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
                .inflate(R.layout.item_nota, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String tglnota = list_data.get(position).getTglPengajuan();
        String jmlnota = list_data.get(position).getJumlahKlaim();
        String jenisnota = list_data.get(position).getJenisKlaim().toUpperCase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

        try {
            Date newDate = sdf.parse(tglnota);
            String pengajuan = sdf2.format(newDate);
            viewHolder.tvitemtanggal.setText(pengajuan);
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.tvitemtanggal.setText(tglnota);
        }

        viewHolder.tvitemjenis.setText(jenisnota);
        viewHolder.tvitemklaim.setText(ConverterUtils.convertIDR(jmlnota));
        viewHolder.tvitemapprov.setText(ConverterUtils.convertIDR("0"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String idkar = list_data.get(position).getIdKaryawan();
                String tglnota = list_data.get(position).getTglPengajuan();
                String jmlnota = list_data.get(position).getJumlahKlaim();
                String jnsnota = list_data.get(position).getJenisKlaim();

                Toast.makeText(activity, "klik" + " " + jnsnota, Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                final View promptView = layoutInflater.inflate(R.layout.pop_detail_klaim_nota, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                final TextView tvdetnotatgl = promptView.findViewById(R.id.tvdettglnota);
                final TextView idkarnota = promptView.findViewById(R.id.tvdetidkarnota);
                final TextView jumlahnota = promptView.findViewById(R.id.tvdetjmlnota);
                final TextView jenisnota = promptView.findViewById(R.id.tvdetjenisnota);

                tvdetnotatgl.setText(tglnota);
                idkarnota.setText(idkar);
                jumlahnota.setText(jmlnota);
                jenisnota.setText(jnsnota);
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
        public TextView tvitemtanggal;
        public TextView tvitemjenis;
        public TextView tvitemklaim;
        public TextView tvitemapprov;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvitemtanggal = itemView.findViewById(R.id.tvitemtanggal);
            tvitemjenis = itemView.findViewById(R.id.tvitemjenis);
            tvitemklaim = itemView.findViewById(R.id.tvitemklaim);
            tvitemapprov = itemView.findViewById(R.id.tvitemapprov);
        }
    }
}
