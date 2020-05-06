package fadhilahramadhan.skripsi.ujit2sampelindepent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.Data;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel1;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel2;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Database.Database;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment.DataFragment;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment.PanduanFragment;

public class DataEditTambah extends AppCompatActivity {

    Button btn_lanjut,btn_kembali,btn_selesai;
    LinearLayout halaman1,halaman2;
    EditText nama,deskripsi,namaVar1, namaVar2, n1, n2, h0,h1;
    TextView var1, var2,loading;
    ListView lv_var1, lv_var2;
    Spinner alpha;
    private ProgressDialog mProgressDialog ;
    private int mProgressStatus = 0;
    private ProgressBarAsync mProgressbarAsync;
    private ProgressHitung mProgressHitung_;
    private int asynchCounter = 0;
    int halaman = 1;
    int id_data;
    double rata_rata1, rata_rata2, variansi1, variansi2, standar_deviasi1, standar_deviasi2, t_hitung;

    private Database dbHelper;
    private List<Data> mDataList;
    private List<DataVariabel1> mDataListVariabel1;
    private List<DataVariabel2> mDataListVariabel2;
    private ListAdaptervar1 adaptervar1;
    private ListAdaptervar2 adaptervar2;

    ArrayList<String> dataTextVar1 = new ArrayList<String>();
    ArrayList<String> dataTextVar2 = new ArrayList<String>();

    String EditOrTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_data);

        Intent i     = getIntent();
        EditOrTambah = i.getStringExtra("EditOrTambah");

        btn_lanjut  = findViewById(R.id.btn_lanjut);
        btn_kembali = findViewById(R.id.btn_kemabli);
        btn_selesai = findViewById(R.id.btn_selesai);

        halaman1    = findViewById(R.id.halaman1);
        halaman2    = findViewById(R.id.halaman2);

        nama        = findViewById(R.id.txtdataEditTambah_etNama);
        deskripsi   = findViewById(R.id.txtdataEditTambah_etDeskripsi);
        namaVar1    = findViewById(R.id.txtdataEditTambah_etVar1);
        namaVar2    = findViewById(R.id.txtdataEditTambah_etVar2);
        n1          = findViewById(R.id.txtdataEditTambah_etN1);
        n2          = findViewById(R.id.txtdataEditTambah_etN2);
        h0          = findViewById(R.id.txtdataEditTambah_etH0);
        h1          = findViewById(R.id.txtdataEditTambah_etH1);
        alpha       = findViewById(R.id.txtdataEditTambah_spalpha);

        var1        = findViewById(R.id.txtdataEditTambah_namaVar1);
        var2        = findViewById(R.id.txtdataEditTambah_namaVar2);

        lv_var1     = findViewById(R.id.txtdataEditTambah_lvVar1);
        lv_var2     = findViewById(R.id.txtdataEditTambah_lvVar2);

        loading     = findViewById(R.id.loading);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Sedang memproses ...");

        dbHelper = new Database(getApplicationContext());

        File database = getBaseContext().getDatabasePath(Database.DBNAME);
        if(false == database.exists()) {
            dbHelper.getReadableDatabase();
            if(copyDatabase(getApplication())) {
            }
        }

        isiSpiner();
        if("Tambah".equals(EditOrTambah)){
            dbHelper.TambahDataPertama();
            id_data = dbHelper.getId_DataTerakhir();
            setSpinTerpilih(alpha, "0.05");
        }else if("Import".equals(EditOrTambah)){
            id_data = i.getIntExtra("id_data",0);
            mDataList = dbHelper.getListDataById(id_data);
            mDataListVariabel1 = dbHelper.getListDataVariabel1(id_data);
            mDataListVariabel2 = dbHelper.getListDataVariabel2(id_data);
            mProgressDialog.show();

            mProgressHitung_ = new ProgressHitung();

            mProgressHitung_.execute();
        }else{
            id_data = i.getIntExtra("id_data",0);
            mDataList = dbHelper.getListDataById(id_data);

            nama.setText(mDataList.get(0).getNama());
            deskripsi.setText(mDataList.get(0).getDeskripsi());
            namaVar1.setText(mDataList.get(0).getVariabel_1());
            namaVar2.setText(mDataList.get(0).getVariabel_2());
            n1.setText(String.valueOf(mDataList.get(0).getN1()));
            n2.setText(String.valueOf(mDataList.get(0).getN2()));
            h0.setText(String.valueOf(mDataList.get(0).getH0()));
            h1.setText(String.valueOf(mDataList.get(0).getH1()));
            if(mDataList.get(0).getAlpha().toString() != null || mDataList.get(0).getAlpha().toString() != "" ) {
                setSpinTerpilih(alpha, String.valueOf(mDataList.get(0).getAlpha()));
            }
            mDataListVariabel1 = dbHelper.getListDataVariabel1(id_data);
            mDataListVariabel2 = dbHelper.getListDataVariabel2(id_data);
        }



        btn_kembali.setVisibility(View.INVISIBLE);
        btn_selesai.setVisibility(View.GONE);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (halaman == 1) {
                    if (TextUtils.isEmpty(nama.getText().toString()) || TextUtils.isEmpty(deskripsi.getText().toString()) || TextUtils.isEmpty(namaVar1.getText().toString()) || TextUtils.isEmpty(namaVar1.getText().toString()) ||
                            TextUtils.isEmpty(n1.getText().toString()) || TextUtils.isEmpty(n2.getText().toString())) {
                        peringatan_ada_kosong();
                    }else{
                        mProgressDialog.show();

                        mProgressbarAsync = new ProgressBarAsync();

                        mProgressbarAsync.execute();

                        halaman1.setVisibility(View.GONE);
                        halaman2.setVisibility(View.VISIBLE);
                        btn_kembali.setVisibility(View.VISIBLE);
                        btn_selesai.setVisibility(View.VISIBLE);
                        btn_lanjut.setVisibility(View.GONE);
                        halaman++;

                    }

                }


            }
        });

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(halaman == 2){
                     for(int x = 0; x < dataTextVar1.size(); x++){
                         dbHelper.updateDataHalaman2_var1(id_data, dataTextVar1.get(x), x);
                     }
                     for(int x = 0; x < dataTextVar2.size(); x++){
                         dbHelper.updateDataHalaman2_var2(id_data, dataTextVar2.get(x), x);
                     }
                     halaman1.setVisibility(View.VISIBLE);
                     halaman2.setVisibility(View.GONE);
                     btn_lanjut.setVisibility(View.VISIBLE);
                     btn_selesai.setVisibility(View.GONE);
                     btn_kembali.setVisibility(View.INVISIBLE);
                     halaman--;
                }

            }
        });

        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ada_kosong = false;

                for(int i = 0; i < dataTextVar1.size(); i++){
                    if(TextUtils.isEmpty(dataTextVar1.get(i).toString())){
                        ada_kosong = true;
                    }
                }

                for(int i = 0; i < dataTextVar2.size(); i++){
                    if(TextUtils.isEmpty(dataTextVar2.get(i).toString())){
                        ada_kosong = true;
                    }
                }

                if(ada_kosong){
                    peringatan_ada_kosong();
                }else{
                    for(int x = 0; x < dataTextVar1.size(); x++){
                        dbHelper.updateDataHalaman2_var1(id_data, dataTextVar1.get(x), x);
                    }
                    for(int x = 0; x < dataTextVar2.size(); x++){
                        dbHelper.updateDataHalaman2_var2(id_data, dataTextVar2.get(x), x);
                    }
                    mProgressDialog.show();

                    mProgressHitung_ = new ProgressHitung();

                    mProgressHitung_.execute();

                }



            }
        });

    }

    public void hitung_rata_rata_variabel(int var){

        Double jumlah = 0.0;

        if(var == 1) {
            for (int x = 0; x < mDataListVariabel1.size(); x++) {
                jumlah = jumlah + Double.parseDouble(mDataListVariabel1.get(x).getNilai());
            }
            rata_rata1 = jumlah / mDataListVariabel1.size();
        }else if(var == 2){
            for (int x = 0; x < mDataListVariabel2.size(); x++) {
                jumlah = jumlah + Double.parseDouble(mDataListVariabel2.get(x).getNilai());
            }
            rata_rata2 = jumlah / mDataListVariabel2.size();
        }
    }

    public void hitung_variansi_variabel(int var){

        double jumlah = 0;

        if(var == 1) {
            for (int x = 0; x < mDataListVariabel1.size(); x++) {
                jumlah = jumlah + Math.pow(Double.parseDouble(mDataListVariabel1.get(x).getNilai()) - rata_rata1, 2);
            }
            variansi1= jumlah / (mDataListVariabel1.size() - 1 );
        }else if(var == 2){
            for (int x = 0; x < mDataListVariabel2.size(); x++) {
                jumlah = jumlah + Math.pow(Double.parseDouble(mDataListVariabel2.get(x).getNilai()) - rata_rata2, 2);
            }
            variansi2 = jumlah / (mDataListVariabel2.size()- 1);
        }
    }

    public void hitung_T_hitung(){
        t_hitung = (rata_rata1 - rata_rata2)/Math.sqrt((variansi1/mDataListVariabel1.size()) + (variansi2/mDataListVariabel2.size()));
    }

    private class ProgressHitung extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (mProgressStatus < 100) {

                    hitung_rata_rata_variabel(1);
                    hitung_rata_rata_variabel(2);
                    mProgressStatus = 50;
                    hitung_variansi_variabel(1);
                    hitung_variansi_variabel(2);
                    mProgressStatus = 70;
                    hitung_T_hitung();
                    mProgressStatus = 80;
                    dbHelper.updatePerhitungan(id_data,rata_rata1,rata_rata2,variansi1,variansi2,t_hitung);
                    mProgressStatus = 100;

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(mProgressStatus);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Intent intent = new Intent(DataEditTambah.this, DetailData.class);
            intent.putExtra("id_data", id_data);
            finish();
            startActivity(intent);
        }
    }

    private class ProgressBarAsync extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            while (mProgressStatus < 100) {
                try {
                    dbHelper.updateDataHalaman1(id_data, nama.getText().toString(), deskripsi.getText().toString(), namaVar1.getText().toString(), namaVar2.getText().toString(), n1.getText().toString(), n2.getText().toString(), h0.getText().toString(), h1.getText().toString(), alpha.getSelectedItem().toString());
                    mProgressStatus = 100;
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressDialog.setProgress(mProgressStatus);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            isiListVar();
            mProgressDialog.dismiss();
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

    public void isiListVar(){
        mDataListVariabel1 = dbHelper.getListDataVariabel1(id_data);
        mDataListVariabel2 = dbHelper.getListDataVariabel2(id_data);


        if( dbHelper.getListDataVariabel1(id_data).size() < Integer.parseInt(n1.getText().toString())){
            int tambahan = Integer.parseInt(n1.getText().toString()) - dbHelper.getListDataVariabel1(id_data).size();
            for(int x = 0;  x < tambahan; x++){
                dbHelper.TambahDataVar1(id_data);
                dataTextVar1.add("");
            }
        }else if( dbHelper.getListDataVariabel1(id_data).size() > Integer.parseInt(n1.getText().toString())){
            int selisih = dbHelper.getListDataVariabel1(id_data).size() - Integer.parseInt(n1.getText().toString());
            dbHelper.deleteDatavar("variabel_1",id_data,selisih);

            for(int x = 0;  x < selisih; x++){
                dataTextVar1.remove(dataTextVar1.size() - 1);
            }
        }else{
            for(int x = 0;  x < dbHelper.getListDataVariabel1(id_data).size(); x++){
                dataTextVar1.add(mDataListVariabel1.get(x).getNilai());
            }
        }

        if( dbHelper.getListDataVariabel2(id_data).size() < Integer.parseInt(n2.getText().toString())){
            int tambahan = Integer.parseInt(n2.getText().toString()) - dbHelper.getListDataVariabel2(id_data).size();

            for(int x = 0;  x < tambahan; x++){
                dbHelper.TambahDataVar2(id_data);
                dataTextVar2.add("");
            }
        }else if( dbHelper.getListDataVariabel2(id_data).size() > Integer.parseInt(n2.getText().toString())){
            int selisih = dbHelper.getListDataVariabel2(id_data).size() - Integer.parseInt(n2.getText().toString());
            dbHelper.deleteDatavar("variabel_2", id_data,selisih);
            for(int x = 0;  x < selisih; x++){
                dataTextVar1.remove(dataTextVar1.size() - 1);
            }
        }else{
            for(int x = 0;  x < dbHelper.getListDataVariabel2(id_data).size(); x++){
                dataTextVar2.add(mDataListVariabel2.get(x).getNilai());
            }
        }
        mDataListVariabel1 = dbHelper.getListDataVariabel1(id_data);
        mDataListVariabel2 = dbHelper.getListDataVariabel2(id_data);


        adaptervar1 = new ListAdaptervar1(DataEditTambah.this, mDataListVariabel1);
        lv_var1.setAdapter(adaptervar1);

        adaptervar2 = new ListAdaptervar2(DataEditTambah.this, mDataListVariabel2);
        lv_var2.setAdapter(adaptervar2);

        var1.setText(namaVar1.getText().toString());
        var2.setText(namaVar2.getText().toString());

        loading.setVisibility(View.GONE);
    }

    public void isiSpiner(){
        ArrayList<String> var = dbHelper.getListvar(id_data);

        String[] arrayH = new String[]{
                "0.1",
                "0.05",
                "0.02",
                "0.025",
                "0.01"

        };

        final ArrayAdapter<String> spinnerArrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,android.R.id.text1,arrayH)
        {
            @Override
            public View getDropDownView(final int position,final View convertView,final ViewGroup parent)
            {
                final View v=super.getDropDownView(position,convertView,parent);
                v.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((TextView)v.findViewById(android.R.id.text1)).setSingleLine(false);
                    }
                });
                return v;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alpha.setAdapter(spinnerArrayAdapter);

    }

    public class ListAdaptervar1 extends BaseAdapter {
        private Context mContext;
        private List<DataVariabel1> mDataListVariabel1;

        public ListAdaptervar1(Context mContext, List<DataVariabel1> mDataListVariabel1) {
            this.mContext = mContext;
            this.mDataListVariabel1 = mDataListVariabel1;
        }

        @Override
        public int getCount() {
            return mDataListVariabel1.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataListVariabel1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDataListVariabel1.get(position).getId_variabel_1();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = View.inflate(mContext, R.layout.listrow_var1, null);

            final int id_variabel_1 = mDataListVariabel1.get(position).getId_variabel_1();

            final EditText nilai = v.findViewById(R.id.lisrow_etvar1);

            nilai.setHint("Nilai "+ String.valueOf(position+1));


            nilai.setText(dataTextVar1.get(position));

            nilai.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    dataTextVar1.set(position, nilai.getText().toString());
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    dataTextVar1.set(position, nilai.getText().toString());
                }
            });

            return v;
        }
    }

    public class ListAdaptervar2 extends BaseAdapter {
        private Context mContext;
        private List<DataVariabel2> mDataListVariabel2;

        public ListAdaptervar2(Context mContext, List<DataVariabel2> mDataListVariabel2) {
            this.mContext = mContext;
            this.mDataListVariabel2 = mDataListVariabel2;
        }

        @Override
        public int getCount() {
            return mDataListVariabel2.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataListVariabel2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDataListVariabel2.get(position).getId_variabel_2();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = View.inflate(mContext, R.layout.listrow_var2, null);

            final int id_variabel_2 = mDataListVariabel2.get(position).getId_variabel_2();

            final EditText nilai = v.findViewById(R.id.lisrow_etvar2);

            nilai.setHint("Nilai "+ String.valueOf(position+1));


            nilai.setText(dataTextVar2.get(position));

            nilai.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    dataTextVar2.set(position, nilai.getText().toString());
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    dataTextVar2.set(position, nilai.getText().toString());
                }
            });
            return v;
        }

    }

    public void setSpinTerpilih(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(halaman == 1){
            if(TextUtils.isEmpty(nama.getText().toString()) || TextUtils.isEmpty(deskripsi.getText().toString()) || TextUtils.isEmpty(namaVar1.getText().toString()) || TextUtils.isEmpty(namaVar1.getText().toString())||
                    TextUtils.isEmpty(n1.getText().toString())|| TextUtils.isEmpty(n2.getText().toString())){
                peringatan_batal();
            }else{
                if("Tambah".equals(EditOrTambah)) {
                    peringatan_batal();
                }else{
                    finish();
                }
            }
        }else{
            boolean ada_kosong = false;

            for(int i = 0; i < dataTextVar1.size(); i++){
                if(TextUtils.isEmpty(dataTextVar1.get(i).toString())){
                    ada_kosong = true;
                }
            }

            for(int i = 0; i < dataTextVar2.size(); i++){
                if(TextUtils.isEmpty(dataTextVar2.get(i).toString())){
                    ada_kosong = true;
                }
            }

            if(ada_kosong){
                if("Tambah".equals(EditOrTambah)) {
                    peringatan_batal();
                }else{
                    peringatan_ada_kosong();
                }
            }else{
                if("Tambah".equals(EditOrTambah)) {
                    peringatan_batal();
                }else{
                    finish();
                }
            }
        }
    }

    public void peringatan_batal(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if("Tambah".equals(EditOrTambah)) {
                            dbHelper.deleteDataById("data",id_data);
                            finish();
                        }else{
                            finish();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin membatalkan analisis data ?").setPositiveButton("Ya", dialogClickListener).setNegativeButton("Tidak", dialogClickListener).show();
    }

    public void peringatan_ada_kosong(){
        Toast.makeText(this, "Masih ada data yang kosong",
                Toast.LENGTH_LONG).show();
    }

}
