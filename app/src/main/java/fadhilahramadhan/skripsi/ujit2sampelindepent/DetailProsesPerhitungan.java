package fadhilahramadhan.skripsi.ujit2sampelindepent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.jstarczewski.pc.mathview.src.MathView;
import com.jstarczewski.pc.mathview.src.TextAlign;

public class DetailProsesPerhitungan extends AppCompatDialogFragment {

    LinearLayout SelisihRataRata, tHitung,variansi, rata_rata,df, data_ly;
    TextView data_var;
    MathView RataRata_pengurangan, SelisihRataRata_hasil, tHitungperhitungan1,tHitungperhitungan2,tHitungperhitungan3, tHitungHasil, variansiPerhitungan1, variansiPerhitungan2, variansiHasil, RataRataPerhitungan1,RataRataPerhitungan2, RataRataHasil, dfRumus,dfPerhitungan1,dfHasil;
    private String judul;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.detail_proses_perhitungan, null);

        Bundle data = getArguments();

        SelisihRataRata = view.findViewById(R.id.detailPerhitungan_SelisihRataRata);
        RataRata_pengurangan = view.findViewById(R.id.detailPerhitungan_RataRataPengurangan);
        SelisihRataRata_hasil = view.findViewById(R.id.detailPerhitungan_SelisihRataRataHasil);
        variansi       = view.findViewById(R.id.detailPerhitungan_variansi);
        rata_rata      = view.findViewById(R.id.detailPerhitungan_RataRata);
        df             = view.findViewById(R.id.detailPerhitungan_df);

        tHitung     = view.findViewById(R.id.detailPerhitungan_Thitung);
        tHitungperhitungan1 = view.findViewById(R.id.detailPerhitungan_tHitungPerhitungan1);
        tHitungperhitungan2 = view.findViewById(R.id.detailPerhitungan_tHitungPerhitungan2);
        tHitungperhitungan3 = view.findViewById(R.id.detailPerhitungan_tHitungPerhitungan3);
        tHitungHasil = view.findViewById(R.id.detailPerhitungan_tHitungHasil);

        variansiPerhitungan1 = view.findViewById(R.id.detailPerhitungan_variasniPerhitungan1);
        variansiPerhitungan2 = view.findViewById(R.id.detailPerhitungan_variasniPerhitungan2);
        variansiHasil = view.findViewById(R.id.detailPerhitungan_variansiHasil);

        RataRataPerhitungan1 = view.findViewById(R.id.detailPerhitungan_RataRataPerhitungan1);
        RataRataPerhitungan2 = view.findViewById(R.id.detailPerhitungan_RataRataPerhitungan2);
        RataRataHasil = view.findViewById(R.id.detailPerhitungan_RataRataHasil);

        dfRumus = view.findViewById(R.id.detailPerhitungan_dfRumus);
        dfPerhitungan1 = view.findViewById(R.id.detailPerhitungan_dfPerhitungan1);
        dfHasil = view.findViewById(R.id.detailPerhitungan_dftaHasil);

        data_ly    = view.findViewById(R.id.detailData);
        data_var = view.findViewById(R.id.data);


        if(data.getString("aksi") == "selisih_rataRata"){
            judul = "Selisih Rata-rata";
            SelisihRataRata.setVisibility(View.VISIBLE);
            RataRata_pengurangan.setTextZoom(60);
            SelisihRataRata_hasil.setTextZoom(60);
            RataRata_pengurangan.setText("$${= "+String.format("%.2f",data.getDouble("x1"))+" - "+ String.format("%.2f",data.getDouble("x2"))+"}$$");
            SelisihRataRata_hasil.setText("$${= "+String.format("%.2f",data.getDouble("x1") - data.getDouble("x2"))+"}$$");

        }else if(data.getString("aksi") == "t_hitung"){
            judul = "T hitung";
            tHitung.setVisibility(View.VISIBLE);
            String x1 = String.format("%.2f",data.getDouble("x1"));
            String x2 = String.format("%.2f",data.getDouble("x2"));
            String v1 = String.format("%.2f",data.getDouble("v1"));
            String v2 = String.format("%.2f",data.getDouble("v2"));
            String n1 = String.valueOf(data.getInt("n1"));
            String n2 = String.valueOf(data.getInt("n2"));
            String tHitung = String.format("%.2f",data.getDouble("tHitung"));

            double tHitungP1 = data.getDouble("x1") - data.getDouble("x2");
            double tHitungP2 = (data.getDouble("v1")/data.getInt("n1") + (data.getDouble("v2")/data.getInt("n2")));
            double tHitungP3 = Math.sqrt(tHitungP2);

            tHitungperhitungan1.setTextZoom(60);
            tHitungperhitungan2.setTextZoom(60);
            tHitungperhitungan3.setTextZoom(60);
            tHitungHasil.setTextZoom(60);

            tHitungperhitungan1.setText("$${ = {"+x1+" - "+x2+"}/√{{{"+v1+"}/"+n1+"}+{{"+v2+"}/"+n2+"}}}$$");
            tHitungperhitungan2.setText("$${ = {"+String.format("%.2f",tHitungP1)+"}/√{"+String.format("%.2f",tHitungP2)+"}}$$");
            tHitungperhitungan3.setText("$${ = {"+String.format("%.2f",tHitungP1)+"}/{"+String.format("%.2f",tHitungP3)+"}}$$");

            tHitungHasil.setText("$$ = "+tHitung+"$$");

        }else if(data.getString("aksi") == "variansi1"){
            judul = "Variansi Sampel 1";
            variansi.setVisibility(View.VISIBLE);
            variansiPerhitungan1.setTextZoom(60);
            variansiPerhitungan1.setText(data.getString("p1"));
            variansiPerhitungan2.setTextZoom(60);
            variansiPerhitungan2.setText(data.getString("p2"));
            variansiHasil.setTextZoom(60);
            variansiHasil.setText(data.getString("variansiHasil"));
        }else if(data.getString("aksi") == "variansi2"){
            judul = "Variansi Sampel 2";
            variansi.setVisibility(View.VISIBLE);
            variansiPerhitungan1.setTextZoom(60);
            variansiPerhitungan1.setText(data.getString("p1"));
            variansiPerhitungan2.setTextZoom(60);
            variansiPerhitungan2.setText(data.getString("p2"));
            variansiHasil.setTextZoom(60);
            variansiHasil.setText(data.getString("variansiHasil"));
        }else if(data.getString("aksi") == "RataRata1"){
            judul = "Rata-rata Sampel 1";
            rata_rata.setVisibility(View.VISIBLE);
            RataRataPerhitungan1.setTextZoom(60);
            RataRataPerhitungan1.setText(data.getString("p1"));
            RataRataPerhitungan2.setTextZoom(60);
            RataRataPerhitungan2.setText(data.getString("p2"));
            RataRataHasil.setTextZoom(60);
            RataRataHasil.setText(data.getString("RataRataHasil"));
        }else if(data.getString("aksi") == "RataRata2"){
            judul = "Rata-rata Sampel 2";
            rata_rata.setVisibility(View.VISIBLE);
            RataRataPerhitungan1.setTextZoom(60);
            RataRataPerhitungan1.setText(data.getString("p1"));
            RataRataPerhitungan2.setTextZoom(60);
            RataRataPerhitungan2.setText(data.getString("p2"));
            RataRataHasil.setTextZoom(60);
            RataRataHasil.setText(data.getString("RataRataHasil"));
        }else if(data.getString("aksi") == "df") {
            judul = "df (degree of fredoom)";
            df.setVisibility(View.VISIBLE);
            dfRumus.setTextZoom(70);
            dfRumus.setText(data.getString("rumus"));
            dfPerhitungan1.setTextZoom(60);
            dfPerhitungan1.setText(data.getString("p1"));
            dfHasil.setTextZoom(60);
            dfHasil.setText(data.getString("dfHasil"));
        }else if(data.getString("aksi") == "data") {
            judul = "Data " + data.getString("var");
            data_ly.setVisibility(View.VISIBLE);
            data_var.setText(data.getString("data"));
        }


        builder.setView(view)
                .setTitle(judul)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        return builder.create();
    }
}
