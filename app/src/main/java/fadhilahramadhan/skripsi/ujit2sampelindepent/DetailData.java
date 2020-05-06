package fadhilahramadhan.skripsi.ujit2sampelindepent;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.Data;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel1;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel2;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Database.Database;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment.DataFragment;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class DetailData extends AppCompatActivity {

    TextView Nvar1, rataVar1, variansiVar1, standarDeviasiVar1, Nvar2, rataVar2, variansiVar2, standarDeviasiVar2, Thitung, Ttabel, df, alpha_, Selisihrata, nama_data, hipotesis, perbandingan, kesimpulan, kesimpulan_hasil,h0,h1,kesimpulan2;

    private List<Data> mDataList;
    private List<DataVariabel1> mDataListVariabel1;
    private List<DataVariabel2> mDataListVariabel2;
    private Database mDBHelper;
    int id_data, df_;
    double rata_rata1, rata_rata2, variansi1, variansi2, standar_deviasi1, standar_deviasi2, t_hitung;

    ImageButton data1, data2, tHitungDialog,SelisihRataRata, btnvariasni1, btnvariasni2, btnRataRata1,btnRataRata2, btndf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_data);

        Nvar1               = findViewById(R.id.txtdetail_Nvar1);
        Nvar2               = findViewById(R.id.txtdetail_Nvar2);
        rataVar1            = findViewById(R.id.txtdetail_rataVar1);
        rataVar2            = findViewById(R.id.txtdetail_rataVar2);
        variansiVar1        = findViewById(R.id.txtdetail_variansiVar1);
        variansiVar2        = findViewById(R.id.txtdetail_variansiVar2);
        standarDeviasiVar1  = findViewById(R.id.txtdetail_standarDeviasivar1);
        standarDeviasiVar2  = findViewById(R.id.txtdetail_standarDeviasivar2);
        Thitung             = findViewById(R.id.txtdetail_Thitung);
        Ttabel              = findViewById(R.id.txtdetail_Ttabel);
        df                  = findViewById(R.id.txtdetail_df);
        alpha_               = findViewById(R.id.txtdetail_alpha);
        Selisihrata         = findViewById(R.id.txtdetail_Selisihrata);
        nama_data           = findViewById(R.id.txtdetail_namaData);
        hipotesis           = findViewById(R.id.txtdetail_hipotesis);
        perbandingan        = findViewById(R.id.txtdetail_perbandingan);
        kesimpulan          = findViewById(R.id.txtdetail_kesimpulan);
        kesimpulan_hasil    = findViewById(R.id.txtdetail_kesimpulan_hasil);
        data1               = findViewById(R.id.btnDetail_data1);
        data2               = findViewById(R.id.btnDetail_data2);
        SelisihRataRata     = findViewById(R.id.btnDetail_selisihRataRata);
        tHitungDialog       = findViewById(R.id.btnDetail_tHitung);
        btnvariasni1        = findViewById(R.id.btnDetail_variasi1);
        btnvariasni2        = findViewById(R.id.btnDetail_variasi2);
        btnRataRata1        = findViewById(R.id.btnDetail_RataRata1);
        btnRataRata2        = findViewById(R.id.btnDetail_RataRata2);
        btndf               = findViewById(R.id.btnDetail_df);
        h0                  = findViewById(R.id.txtdetail_h0);
        h1                  = findViewById(R.id.txtdetail_h1);
        kesimpulan2         = findViewById(R.id.txtdetail_kesimpulan_hasil2);

        Intent i     = getIntent();
        id_data      = i.getIntExtra("id_data",0);

        mDBHelper          = new Database(this);
        mDataList          = mDBHelper.getListDataById(id_data);
        mDataListVariabel1 = mDBHelper.getListDataVariabel1(id_data);
        mDataListVariabel2 = mDBHelper.getListDataVariabel2(id_data);

        File database = getDatabasePath(Database.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)) {
            }
        }

        TabHost tabs = (TabHost) findViewById(R.id.tabHostVar);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec(mDataList.get(0).getVariabel_1());
        spec.setContent(R.id.var1);
        spec.setIndicator(mDataList.get(0).getVariabel_1());
        tabs.addTab(spec);
        spec = tabs.newTabSpec(mDataList.get(0).getVariabel_2());
        spec.setContent(R.id.var2);
        spec.setIndicator(mDataList.get(0).getVariabel_2());
        tabs.addTab(spec);

        rata_rata1 = Double.parseDouble(mDataList.get(0).getRata_rata_sampel_2());
        rata_rata2 = Double.parseDouble(mDataList.get(0).getRata_rata_sampel_2());
        variansi1 = Double.parseDouble(mDataList.get(0).getVariansi_sampel_1());
        variansi2 = Double.parseDouble(mDataList.get(0).getVariansi_sampel_2());
        hitung_standar_deviasi_variabel(1);
        hitung_standar_deviasi_variabel(2);
        t_hitung = Double.parseDouble(mDataList.get(0).getT_hitung());


        df_ = (mDataListVariabel1.size()+mDataListVariabel2.size())-2;
        double alpha = mDataList.get(0).getAlpha();

        double t_tabel = mDBHelper.getT_distribusi(df_,alpha);

        nama_data.setText(mDataList.get(0).getNama());
        hipotesis.setText(mDataList.get(0).getDeskripsi());
        Nvar1.setText(String.valueOf(mDataList.get(0).getN1()));
        Nvar2.setText(String.valueOf(mDataList.get(0).getN2()));
        rataVar1.setText(String.format("%.2f",rata_rata1));
        rataVar2.setText(String.format("%.2f", rata_rata2));
        variansiVar1.setText(String.format("%.2f", variansi1));
        variansiVar2.setText(String.format("%.2f", variansi2));
        standarDeviasiVar1.setText(String.format("%.2f", standar_deviasi1));
        standarDeviasiVar2.setText(String.format("%.2f", standar_deviasi2));
        h0.setText("H0 : "+mDataList.get(0).getH0());
        h1.setText("H1 : "+mDataList.get(0).getH1());


        if(Math.abs( t_hitung)> t_tabel){
            perbandingan.setText("T hitung ("+String.format("%.2f", Math.abs( t_hitung))+") > T tabel ("+String.format("%.2f", t_tabel)+")");
            kesimpulan.setText("H0 ditolak dan H1 diterima");
            kesimpulan_hasil.setText(mDataList.get(0).getH1() + " antara " + mDataList.get(0).getVariabel_1() + " dan  "+ mDataList.get(0).getVariabel_2());
            if(rata_rata1 > rata_rata2){
                kesimpulan2.setText("Rata-rata "+ mDataList.get(0).getVariabel_1()+ " Lebih besar dari pada rata-rata "+ mDataList.get(0).getVariabel_2());
            }else{
                kesimpulan2.setText("Rata-rata "+ mDataList.get(0).getVariabel_2()+ " Lebih besar dari pada rata-rata "+ mDataList.get(0).getVariabel_1());
            }
        }else{
            perbandingan.setText("T hitung ("+String.format("%.2f", Math.abs( t_hitung))+") < T tabel ("+String.format("%.2f", t_tabel)+")");
            kesimpulan.setText("H0 diterima dan H1 ditolak" );
            kesimpulan_hasil.setText(mDataList.get(0).getH0() + " antara " + mDataList.get(0).getVariabel_1() + " dan "+ mDataList.get(0).getVariabel_2());

        }

        if(t_hitung < 0){
            Thitung.setText("|"+String.format("%.2f", t_hitung)+"| -> " + String.format("%.2f", Math.abs( t_hitung)));
        }else{
            Thitung.setText(String.format("%.2f", t_hitung));
        }
        Ttabel.setText(String.format("%.2f", t_tabel));

        alpha_.setText(String.valueOf(mDataList.get(0).getAlpha()));
        df.setText(String.valueOf(df_));
        Selisihrata.setText(String.format("%.2f", rata_rata1-rata_rata2));


        mDataListVariabel1 = mDBHelper.getListDataVariabel1(id_data);
        mDataListVariabel2 = mDBHelper.getListDataVariabel2(id_data);

        SelisihRataRata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("selisih_rataRata");
            }
        });

        tHitungDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("t_hitung");
            }
        });
        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("data1");
            }
        });
        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("data2");
            }
        });
        btnvariasni1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("variansi1");
            }
        });
        btnvariasni2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("variansi2");
            }
        });
        btnRataRata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("RataRata1");
            }
        });
        btnRataRata2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("RataRata2");
            }
        });
        btndf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("df");
            }
        });

    }

    public void openDialog(String dari) {
        Bundle data = new Bundle();
        if(dari == "selisih_rataRata" ) {
            data.putString("aksi", "selisih_rataRata");
            data.putDouble("x1", rata_rata1);
            data.putDouble("x2", rata_rata2);
        }else if(dari == "t_hitung" ) {
            data.putString("aksi", "t_hitung");
            data.putDouble("x1", rata_rata1);
            data.putDouble("x2", rata_rata2);
            data.putDouble("v1", variansi1);
            data.putDouble("v2", variansi2);
            data.putInt("n1", mDataListVariabel1.size());
            data.putInt("n2", mDataListVariabel2.size());
            data.putDouble("tHitung", t_hitung);
        }else if(dari == "variansi1"){
            data.putString("aksi", "variansi1");

            String p1 = "$${ = {";
            double jumlah = 0;
            for(int x = 0; x < mDataListVariabel1.size(); x++){
                p1 += "("+mDataListVariabel1.get(x).getNilai().toString()+" - "+String.format("%.2f",rata_rata1)+")^2";
                if((mDataListVariabel1.size() - 1) != x ){
                    p1 += "+";
                }else{
                    p1 += "}/{"+mDataListVariabel1.size()+"-1}}$$";
                }
                jumlah = jumlah + Math.pow(Double.parseDouble(mDataListVariabel1.get(x).getNilai()) - rata_rata1, 2);
            }
            String p2 = "$${ = {" +String.valueOf(jumlah)+"}/{"+String.valueOf(mDataListVariabel1.size()-1)+"}}$$";
            data.putString("p1", p1);
            data.putString("p2", p2);
            data.putString("variansiHasil", "$${ = " + String.valueOf(variansi1)+ "}$$");

        }else if(dari == "variansi2"){
            data.putString("aksi", "variansi2");

            String p1 = "$${ = {";
            double jumlah = 0;
            for(int x = 0; x < mDataListVariabel2.size(); x++){
                p1 += "("+mDataListVariabel2.get(x).getNilai().toString()+" - "+String.format("%.2f",rata_rata2)+")^2";
                if((mDataListVariabel2.size() - 1) != x ){
                    p1 += "+";
                }else{
                    p1 += "}/{"+mDataListVariabel2.size()+"-1}}$$";
                }
                jumlah = jumlah + Math.pow(Double.parseDouble(mDataListVariabel2.get(x).getNilai()) - rata_rata2, 2);
            }
            String p2 = "$${ = {" +String.valueOf(jumlah)+"}/{"+String.valueOf(mDataListVariabel2.size()-1)+"}}$$";
            data.putString("p1", p1);
            data.putString("p2", p2);
            data.putString("variansiHasil", "$${ = " + String.valueOf(variansi2)+ "}$$");

        }else if(dari == "RataRata1"){
            data.putString("aksi", "RataRata1");
            String p1 = "$${ = {";
            double jumlah = 0;
            for(int x = 0; x < mDataListVariabel1.size(); x++){
                p1 += mDataListVariabel1.get(x).getNilai().toString();
                if((mDataListVariabel1.size() - 1) != x ){
                    p1 += "+";
                }else{
                    p1 += "}/{"+mDataListVariabel1.size()+"}}$$";
                }
                jumlah = jumlah + Double.parseDouble(mDataListVariabel1.get(x).getNilai());
            }
            String p2 = "$${ = {" +String.valueOf(jumlah)+"}/{"+String.valueOf(mDataListVariabel1.size())+"}}$$";
            data.putString("p1", p1);
            data.putString("p2", p2);
            data.putString("RataRataHasil", "$${ = " + String.valueOf(rata_rata1)+ "}$$");
        }else if(dari == "RataRata2"){
            data.putString("aksi", "RataRata2");
            String p1 = "$${ = {";
            double jumlah = 0;
            for(int x = 0; x < mDataListVariabel2.size(); x++){
                p1 += mDataListVariabel2.get(x).getNilai().toString();
                if((mDataListVariabel2.size() - 1) != x ){
                    p1 += "+";
                }else{
                    p1 += "}/{"+mDataListVariabel2.size()+"}}$$";
                }
                jumlah = jumlah + Double.parseDouble(mDataListVariabel2.get(x).getNilai());
            }
            String p2 = "$${ = {" +String.valueOf(jumlah)+"}/{"+String.valueOf(mDataListVariabel2.size())+"}}$$";
            data.putString("p1", p1);
            data.putString("p2", p2);
            data.putString("RataRataHasil", "$${ = " + String.valueOf(rata_rata2)+ "}$$");
        }else if(dari == "df"){
            data.putString("aksi", "df");
            data.putString("rumus", "$${ df = {(n1+n2)-2}}$$");
            data.putString("p1", "$${ = {("+mDataListVariabel1.size()+"+"+mDataListVariabel2.size()+")-2}}$$");
            data.putString("dfHasil", "$${ = "+String.valueOf(df_)+"}$$");
        }else if(dari == "data1"){
            String data_var = "";
            for(int x = 0; x<mDataListVariabel1.size();x++){
                data_var += String.valueOf(x+1) +". "+ mDataListVariabel1.get(x).getNilai() + "\n";
            }
            data.putString("aksi", "data");
            data.putString("data", data_var);
            data.putString("var", mDataList.get(0).getVariabel_1());
        }else if(dari == "data2"){
            String data_var = "";
            for(int x = 0; x<mDataListVariabel2.size();x++){
                data_var += String.valueOf(x+1) +". "+ mDataListVariabel2.get(x).getNilai() + "\n";
            }
            data.putString("aksi", "data");
            data.putString("data", data_var);
            data.putString("var", mDataList.get(0).getVariabel_2());
        }
        DetailProsesPerhitungan dialog= new DetailProsesPerhitungan();
        dialog.setArguments(data);
        dialog.show(getSupportFragmentManager(), "dialog");
    }



    public void hitung_standar_deviasi_variabel(int var){
        if(var == 1) {
            standar_deviasi1 = Math.sqrt(variansi1);
        }else if(var == 2){
            standar_deviasi2 = Math.sqrt(variansi2);
        }
    }


    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(Database.DBNAME);
            String outFileName = Database.DBLOCATION + Database.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}