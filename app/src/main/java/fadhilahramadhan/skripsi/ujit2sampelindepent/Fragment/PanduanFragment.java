package fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import fadhilahramadhan.skripsi.ujit2sampelindepent.Panduan.AnalisisData;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Panduan.KriteriaPengujian;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Panduan.Rumus;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Panduan.tTabel;
import fadhilahramadhan.skripsi.ujit2sampelindepent.R;


public class PanduanFragment extends Fragment {

    CardView analisis_data, rumus, kriteria_pengujian, t_tabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panduan, container, false);
        getActivity().setTitle("Panduan");

        analisis_data = view.findViewById(R.id.cvPetunjuk_analisisData);
        rumus = view.findViewById(R.id.cvPetunjuk_rumus);
        kriteria_pengujian = view.findViewById(R.id.cvPetunjuk_kriteriaPengujian);
        t_tabel = view.findViewById(R.id.cvPetunjuk_tTabel);

        analisis_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AnalisisData.class);
                startActivity(i);
            }
        });

        rumus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Rumus.class);
                startActivity(i);
            }
        });

        kriteria_pengujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), KriteriaPengujian.class);
                startActivity(i);
            }
        });

        t_tabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), tTabel.class);
                startActivity(i);
            }
        });

        return view;
    }
}
