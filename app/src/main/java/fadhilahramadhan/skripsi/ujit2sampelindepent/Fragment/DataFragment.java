package fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.Data;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel1;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel2;
import fadhilahramadhan.skripsi.ujit2sampelindepent.DataEditTambah;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Database.Database;
import fadhilahramadhan.skripsi.ujit2sampelindepent.DetailData;
import fadhilahramadhan.skripsi.ujit2sampelindepent.R;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;


public class DataFragment extends Fragment {

    ListView lv_data;
    FloatingActionButton btn_dataTambah;
    LinearLayout kosong;
    private List<Data> mDataList;
    private Database mDBHelper;
    private ListAdapter adapter;
    public static final int requestcode = 1;
    int id_data_ = 0;
    boolean berhasil_import = false;
    boolean masih_ada_kosong = true;

    private Database dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        getActivity().setTitle("Daftar data penelitian");


        lv_data        = view.findViewById(R.id.lv_data);
        btn_dataTambah = view.findViewById(R.id.btn_dataTambah);
        kosong         = view.findViewById(R.id.kosong);

        mDBHelper      = new Database(view.getContext());

        //Check database
        File database = view.getContext().getDatabasePath(Database.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if(copyDatabase(view.getContext())) {
                // Toast.makeText(getContext(), "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getContext(), "Copy data error", Toast.LENGTH_SHORT).show();
                return view.getRootView();
            }
        }
        //Ambil data dari tabel
        mDataList = mDBHelper.getListData();
        adapter = new ListAdapter(view.getContext(), mDataList);
        lv_data.setAdapter(adapter);

        registerForContextMenu(lv_data);


        btn_dataTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Pilih Opsi");

                    final String[] opsi = {"Input Manual", "Import file CSV"};
                    builder.setItems(opsi, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                if ("Input Manual".equals(opsi[which])) {
                                    Intent i = new Intent(getActivity(), DataEditTambah.class);
                                    i.putExtra("EditOrTambah", "Tambah");
                                    startActivity(i);
                                }else if ("Import file CSV".equals(opsi[which])) {
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory("android.intent.category.OPENABLE");
                                    intent.setType("text/*");
                                    startActivityForResult(intent, requestcode);
                                }
                            }

                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

            }
        });

        if(mDataList.size() < 1){
            kosong.setVisibility(View.VISIBLE);
        }else{
            kosong.setVisibility(View.GONE);
        }


        return view;

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (requestCode == requestCode) {

            switch (requestCode) {
                case requestcode:
                    Uri filepath = resultData.getData();
                    try {
                        if (resultCode == RESULT_OK) {
                            try {
                                dbHelper = new Database(getContext());
                                dbHelper.TambahDataPertama();
                                id_data_ = dbHelper.getId_DataTerakhir();

                                int n1 = 0;
                                int n2 = 0;
                                for(int x = 1;x < readCSV(filepath).size(); x++) {

                                    String[] str = String.valueOf(readCSV(filepath).get(x)).split(";", 8);
                                    String data_sampel1 = str[0].toString();
                                    String data_sampel2 = str[1].toString();

                                    if(x == 1){
                                        if(TextUtils.isEmpty(str[2]) || TextUtils.isEmpty(str[3]) || TextUtils.isEmpty(str[4]) ||TextUtils.isEmpty(str[5]) ||TextUtils.isEmpty(str[6]) ||TextUtils.isEmpty(str[7])){
                                            masih_ada_kosong = true;
                                        }else{
                                            masih_ada_kosong = false;
                                        }
                                        dbHelper.updateDataHalaman1(id_data_, str[2], str[3], str[4], str[5], "","",str[6], str[7], "0.05");
                                    }
                                    if(TextUtils.isEmpty(data_sampel1) == false){
                                        dbHelper.ImportDataVar1(id_data_, data_sampel1);
                                        n1 = n1+1;
                                    }

                                    if(TextUtils.isEmpty(data_sampel2) == false){
                                        dbHelper.ImportDataVar2(id_data_, data_sampel2);
                                        n2 = n2+1;
                                    }
                                }
                                dbHelper.updateNData(id_data_, String.valueOf(n1), String.valueOf(n2));
                                berhasil_import = true;

                            } catch (IOException e) {
                                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getContext(), "Pastikan file CSV yang diimport", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();

                    }
            }

            if (berhasil_import) {

                if(masih_ada_kosong){
                    Intent i = new Intent(getActivity(), DataEditTambah.class);
                    i.putExtra("EditOrTambah", "Edit");
                    i.putExtra("id_data", id_data_);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getActivity(), DataEditTambah.class);
                    i.putExtra("EditOrTambah", "Import");
                    i.putExtra("id_data", id_data_);
                    startActivity(i);
                }

            }
        }
    }
    @NotNull
    public final List readCSV(@NotNull Uri uri) throws IOException {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        InputStream csvFile = getActivity().getContentResolver().openInputStream(uri);
        InputStreamReader isr = new InputStreamReader(csvFile);
        return TextStreamsKt.readLines((Reader)(new BufferedReader((Reader)isr)));
    }
    @Override
    public void onResume() {
        super.onResume();
        mDataList = mDBHelper.getListData();
        //Init adapter
        adapter.updateList(mDataList);

        mDataList.clear();
        mDataList = mDBHelper.getListData();
        adapter = new ListAdapter(getContext(), mDataList);
        lv_data.setAdapter(adapter);

        if(mDataList.size() < 1){
            kosong.setVisibility(View.VISIBLE);
        }else{
            kosong.setVisibility(View.GONE);
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

    public class ListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Data> mDatatList;
        private List<DataVariabel1> mDataListVariabel1;
        private List<DataVariabel2> mDataListVariabel2;

        public ListAdapter(Context mContext, List<Data> mDatatList) {
            this.mContext = mContext;
            this.mDatatList = mDatatList;
        }

        @Override
        public int getCount() {
            return mDatatList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDatatList.get(position).getId_data();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = View.inflate(mContext, R.layout.listrow_data, null);
            final int id_data = mDatatList.get(position).getId_data();

            final TextView txt_dataNama        = v.findViewById(R.id.txt_dataNama);
            final TextView txt_dataDeskrispi   = v.findViewById(R.id.txt_dataDeskripsi);
            final ImageButton btn_aksi         = v.findViewById(R.id.btn_dataAksi);
            final CardView cv_data             = v.findViewById(R.id.cv_data);

            txt_dataNama.setText(mDatatList.get(position).getNama());
            txt_dataDeskrispi.setText(String.valueOf(mDatatList.get(position).getDeskripsi()));



            btn_aksi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context wrapper = new ContextThemeWrapper(getActivity(),  R.style.MyPopupStyle);

                    PopupMenu popup = new PopupMenu(wrapper, view);

                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                        try {
                            Field[] fields = popup.getClass().getDeclaredFields();
                            for (Field field : fields) {
                                if ("mPopup".equals(field.getName())) {
                                    field.setAccessible(true);
                                    Object menuPopupHelper = field.get(popup);
                                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                    setForceIcons.invoke(menuPopupHelper, true);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menuPopup_edit:
                                    Intent i = new Intent(getActivity(), DataEditTambah.class);
                                    i.putExtra("EditOrTambah", "Edit");
                                    i.putExtra("id_data", id_data);
                                    startActivity(i);
                                    break;
                                case R.id.menuPopup_hapus:
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    if( mDBHelper.deleteDataById("Data",id_data)){
                                                        Toast.makeText(getActivity(),"Behasil dihapus", Toast.LENGTH_SHORT).show();
                                                        mDataList.clear();
                                                        mDataList = mDBHelper.getListData();
                                                        adapter = new ListAdapter(getContext(), mDataList);
                                                        lv_data.setAdapter(adapter);
                                                    } else {
                                                        Toast.makeText(getActivity(),"Gagal Menghapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:

                                                    break;
                                            }
                                        }

                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Anda yakin ingin menghapus ?").setPositiveButton("Ya", dialogClickListener)
                                            .setNegativeButton("Tidak", dialogClickListener).show();
                                    break;
                                case R.id.menuPopup_detailData:
                                    Intent intent = new Intent(getActivity(), DetailData.class);
                                    intent.putExtra("id_data", id_data);
                                    startActivity(intent);
                                    break;
                                case R.id.menuPopup_report:
                                    mDataListVariabel1 = mDBHelper.getListDataVariabel1(id_data);
                                    mDataListVariabel2 = mDBHelper.getListDataVariabel2(id_data);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                                    String currentDateandTime = sdf.format(new Date());

                                    String Fnamexls="Report_"  +currentDateandTime+".xls";

                                    File sdCard = Environment.getExternalStorageDirectory();

                                    File directory = new File (sdCard.getAbsolutePath() + "/Uji T/Report");
                                    directory.mkdirs();

                                    File file = new File(directory, Fnamexls);

                                    WorkbookSettings wbSettings = new WorkbookSettings();
                                    wbSettings.setLocale(new Locale("en", "EN"));

                                    WritableWorkbook workbook;
                                    try {
                                        int a = 1;
                                        workbook = Workbook.createWorkbook(file, wbSettings);
                                        WritableSheet sheet = workbook.createSheet("First Sheet", 0);

                                        try {

                                            WritableCellFormat boldYELLOW = new WritableCellFormat();
                                            boldYELLOW.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                                            boldYELLOW.setBackground(Colour.YELLOW);
                                            boldYELLOW.setVerticalAlignment(VerticalAlignment.TOP);

                                            Label label1 = new Label(0,0,mDatatList.get(position).getVariabel_1(),new WritableCellFormat(boldYELLOW));
                                            Label label2 = new Label(1,0,mDatatList.get(position).getVariabel_2(),new WritableCellFormat(boldYELLOW));
                                            Label label3 = new Label(2,0,"Nama Obyek",new WritableCellFormat(boldYELLOW));
                                            Label label4 = new Label(2,1,"Dekskripsi",new WritableCellFormat(boldYELLOW));
                                            Label label5 = new Label(2,2,"H0",new WritableCellFormat(boldYELLOW));
                                            Label label6 = new Label(2,3,"H1",new WritableCellFormat(boldYELLOW));
                                            Label label7 = new Label(2,4,"Rata-rata Sampel 1",new WritableCellFormat(boldYELLOW));
                                            Label label8 = new Label(2,5,"Rata-rata Sampel 2",new WritableCellFormat(boldYELLOW));
                                            Label label9 = new Label(2,6,"Variansi Sampel 1",new WritableCellFormat(boldYELLOW));
                                            Label label10 = new Label(2,7,"Variansi Sampel 2",new WritableCellFormat(boldYELLOW));
                                            Label label11 = new Label(2,8,"T hitung",new WritableCellFormat(boldYELLOW));
                                            Label label12 = new Label(2,9,"T tabel",new WritableCellFormat(boldYELLOW));
                                            Label label13 = new Label(2,10,"Alpha",new WritableCellFormat(boldYELLOW));
                                            Label label14 = new Label(2,11,"Kesimpulan",new WritableCellFormat(boldYELLOW));
                                            Label label15 = new Label(2,12,"",new WritableCellFormat(boldYELLOW));
                                            Label label16 = new Label(2,13,"",new WritableCellFormat(boldYELLOW));
                                            sheet.mergeCells(2, 11, 2, 13);

                                            sheet.addCell(label1);
                                            sheet.addCell(label2);
                                            sheet.addCell(label3);
                                            sheet.addCell(label4);
                                            sheet.addCell(label5);
                                            sheet.addCell(label6);
                                            sheet.addCell(label7);
                                            sheet.addCell(label8);
                                            sheet.addCell(label9);
                                            sheet.addCell(label10);
                                            sheet.addCell(label11);
                                            sheet.addCell(label12);
                                            sheet.addCell(label13);
                                            sheet.addCell(label14);
                                            sheet.addCell(label15);
                                            sheet.addCell(label16);
                                            sheet.addCell(new Label(3, 0,mDataList.get(position).getNama()));
                                            sheet.addCell(new Label(3, 1,mDataList.get(position).getDeskripsi()));
                                            sheet.addCell(new Label(3, 2,mDataList.get(position).getH0()));
                                            sheet.addCell(new Label(3, 3,mDataList.get(position).getH1()));
                                            sheet.addCell(new Label(3, 4,String.format("%.3f",Double.parseDouble(mDataList.get(position).getRata_rata_sampel_1()))));
                                            sheet.addCell(new Label(3, 5,String.format("%.3f",Double.parseDouble(mDataList.get(position).getRata_rata_sampel_2()))));
                                            sheet.addCell(new Label(3, 6,String.format("%.3f",Double.parseDouble(mDataList.get(position).getVariansi_sampel_1()))));
                                            sheet.addCell(new Label(3, 7,String.format("%.3f",Double.parseDouble(mDataList.get(position).getVariansi_sampel_2()))));
                                            sheet.addCell(new Label(3, 8,String.format("%.3f",Double.parseDouble(mDataList.get(position).getT_hitung()))));
                                            double t_tabel = mDBHelper.getT_distribusi((mDataListVariabel1.size()+mDataListVariabel2.size())-2,mDataList.get(position).getAlpha());
                                            sheet.addCell(new Label(3, 9,String.valueOf(t_tabel)));
                                            sheet.addCell(new Label(3, 10,String.valueOf(mDataList.get(position).getAlpha())));
                                            String kesimpulan1,kesimpulan2,kesimpulan3;
                                            if(Double.parseDouble(mDataList.get(position).getT_hitung()) < t_tabel){
                                                kesimpulan1 = "T hitung("+String.format("%.3f",Double.parseDouble(mDataList.get(position).getT_hitung()))+") < T tabel("+String.valueOf(t_tabel)+")";
                                                kesimpulan2 = "H0 diterima dan H1 ditolak";
                                                kesimpulan3 = mDataList.get(position).getH0() + " antara "+ mDataList.get(position).getVariabel_1() + " dan " +mDataList.get(position).getVariabel_1();
                                            }else{
                                                kesimpulan1 = "T hitung("+String.format("%.3f",Double.parseDouble(mDataList.get(position).getT_hitung()))+") > T tabel("+String.valueOf(t_tabel)+")";
                                                kesimpulan2 = "H0 ditolak dan H1 diterima";
                                                kesimpulan3 = mDataList.get(position).getH1() + " antara "+ mDataList.get(position).getVariabel_1() + " dan " +mDataList.get(position).getVariabel_1();
                                            }
                                            sheet.addCell(new Label(3, 11,kesimpulan1));
                                            sheet.addCell(new Label(3, 12,kesimpulan2));
                                            sheet.addCell(new Label(3, 13,kesimpulan3));

                                            for(int x = 1; x < mDataListVariabel1.size();x++){
                                                sheet.addCell(new Label(0, x,mDataListVariabel1.get(x).getNilai()));
                                            }
                                            for(int x = 1; x < mDataListVariabel2.size();x++){
                                                sheet.addCell(new Label(1, x,mDataListVariabel2.get(x).getNilai()));
                                            }



                                            CellView cell;
                                            cell =sheet.getColumnView(2);
                                            cell.setAutosize(true);
                                            sheet.setColumnView(2, cell);

                                        } catch (RowsExceededException e) {
                                            Toast.makeText(getActivity(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        } catch (WriteException e) {
                                            Toast.makeText(getActivity(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        workbook.write();
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                                        dlgAlert.setMessage("Data report berhasil di simpan pada folder 'Uji T' dengan nama file 'Report_"+currentDateandTime+".xls'");
                                        dlgAlert.setTitle("Report data");
                                        dlgAlert.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //dismiss the dialog
                                                    }
                                                });
                                        dlgAlert.create().show();

                                        try {
                                            workbook.close();
                                        } catch (WriteException e) {
                                            dlgAlert.setMessage("Aplikasi ini belum diizinkan untuk mengakses penyimpanan, izinkan aplikasi ini untuk mengakses penyimpanan pada settingan perizinan aplikasi");
                                            dlgAlert.setTitle("Gagal report data");
                                            dlgAlert.setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //dismiss the dialog
                                                        }
                                                    });
                                            dlgAlert.create().show();
                                        }

                                    } catch (IOException e) {
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                                        dlgAlert.setMessage("Aplikasi ini belum diizinkan untuk mengakses penyimpanan, izinkan aplikasi ini untuk mengakses penyimpanan pada setting perizinan aplikasi");
                                        dlgAlert.setTitle("Gagal report data");
                                        dlgAlert.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //dismiss the dialog
                                                    }
                                                });
                                        dlgAlert.create().show();
                                    }
                                    break;
                                case R.id.menuPopup_export:
                                    File folder = new File(Environment.getExternalStorageDirectory()
                                            + "/Uji T/Export");

                                    boolean var = false;
                                    if (!folder.exists())
                                        var = folder.mkdir();

                                    System.out.println("" + var);
                                    SimpleDateFormat sdf_ = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                                    final String currentDateandTime_ = sdf_.format(new Date());

                                    final String filename = folder.toString() + "/" + "Export_"+currentDateandTime_+".csv";
                                    final boolean berhasil = false;


                                            try {
                                                mDataListVariabel1 = mDBHelper.getListDataVariabel1(id_data);
                                                mDataListVariabel2 = mDBHelper.getListDataVariabel2(id_data);

                                                FileWriter fw = new FileWriter(filename);


                                                fw.append("Data Sampel 1");
                                                fw.append(';');

                                                fw.append("Data Sampel 2");
                                                fw.append(';');

                                                fw.append("Nama Obyek");
                                                fw.append(';');

                                                fw.append("Dekripsi");
                                                fw.append(';');

                                                fw.append("Nama Variabel Sampel 1");
                                                fw.append(';');

                                                fw.append("Nama Variabel Sampel 2");
                                                fw.append(';');

                                                fw.append("H0");
                                                fw.append(';');

                                                fw.append("H1");

                                                fw.append('\n');

                                                for(int x = 1; x <= Math.max(mDataListVariabel1.size(),mDataListVariabel2.size());x++){
                                                    fw.append(mDataListVariabel1.get(x-1).getNilai());
                                                    fw.append(';');
                                                    fw.append(mDataListVariabel2.get(x-1).getNilai());
                                                    fw.append(';');

                                                    if(x == 1){
                                                        fw.append(mDataList.get(position).getNama());
                                                        fw.append(';');
                                                        fw.append(mDataList.get(position).getDeskripsi());
                                                        fw.append(';');
                                                        fw.append(mDataList.get(position).getVariabel_1());
                                                        fw.append(';');
                                                        fw.append(mDataList.get(position).getVariabel_2());
                                                        fw.append(';');
                                                        fw.append(mDataList.get(position).getH0());
                                                        fw.append(';');
                                                        fw.append(mDataList.get(position).getH1());
                                                        fw.append(';');
                                                    }else{
                                                        fw.append(';');
                                                        fw.append(';');
                                                        fw.append(';');
                                                        fw.append(';');
                                                        fw.append(';');
                                                        fw.append(';');
                                                    }
                                                    fw.append('\n');
                                                }
                                                // fw.flush();
                                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                                                dlgAlert.setMessage("Data berhasil di Export pada folder 'Uji T' dengan nama file 'Export_"+currentDateandTime_+".csv'");
                                                dlgAlert.setTitle("Report data");
                                                dlgAlert.setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //dismiss the dialog
                                                            }
                                                        });
                                                dlgAlert.create().show();
                                                fw.close();
                                            }catch (IOException e) {
                                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                                                dlgAlert.setMessage("Aplikasi ini belum diizinkan untuk mengakses penyimpanan, izinkan aplikasi ini untuk mengakses penyimpanan pada setting perizinan aplikasi");
                                                dlgAlert.setTitle("Gagal export data");
                                                dlgAlert.setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //dismiss the dialog
                                                            }
                                                        });
                                                dlgAlert.create().show();
                                            }

                                    break;
                            }
                            return true;
                        }

                    });
                    popup.show();
                }


            });



            cv_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailData.class);
                    intent.putExtra("id_data", id_data);
                    startActivity(intent);
                }
            });
            return v;
        }


        public void updateList(List<Data> lstItem) {
            mDataList.clear();
            mDataList.addAll(lstItem);
            this.notifyDataSetChanged();
        }
    }


}
