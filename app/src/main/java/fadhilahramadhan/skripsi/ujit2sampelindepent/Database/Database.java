package fadhilahramadhan.skripsi.ujit2sampelindepent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.Data;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel1;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataVariabel2;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataTTabel;


public class Database extends SQLiteOpenHelper {
    public static final String DBNAME = "database.sqlite";
    public static final String DBLOCATION = "/data/data/fadhilahramadhan.skripsi.ujit2sampelindepent/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Database(Context context) {
        super(context, DBNAME, null, 7);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<Data> getListData() {
        Data data = null;
        List<Data> dataList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM data", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = new Data(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3) ,cursor.getString(4),cursor.getDouble(  5),cursor.getInt(6),cursor.getInt(7), cursor.getString(8), cursor.getString(9),cursor.getString(  10),cursor.getString(11),cursor.getString(12), cursor.getString(13), cursor.getString(14));
            dataList.add(data);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dataList;
    }

    public List<Data> getListDataById(int id_data) {
        Data data = null;
        List<Data> dataList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM data WHERE id_data="+id_data, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = new Data(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3) ,cursor.getString(4),cursor.getDouble(  5),cursor.getInt(6),cursor.getInt(7), cursor.getString(8), cursor.getString(9),cursor.getString(  10),cursor.getString(11),cursor.getString(12), cursor.getString(13), cursor.getString(14));
            dataList.add(data);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dataList;
    }

    public List<DataVariabel1> getListDataVariabel1(int id_data) {
        DataVariabel1 dataVariabel1 = null;
        List<DataVariabel1> dataList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM variabel_1 WHERE id_data="+id_data, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dataVariabel1 = new DataVariabel1(cursor.getInt(0),cursor.getInt(1), cursor.getString(2));
            dataList.add(dataVariabel1);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dataList;
    }

    public List<DataVariabel2> getListDataVariabel2(int id_data) {
        DataVariabel2 dataVariabel2 = null;
        List<DataVariabel2> dataList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM variabel_2 WHERE id_data="+id_data, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dataVariabel2 = new DataVariabel2(cursor.getInt(0),cursor.getInt(1), cursor.getString(2));
            dataList.add(dataVariabel2);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dataList;
    }

    public long TambahDataPertama() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama_obyek", "");
        openDatabase();
        long returnValue = mDatabase.insert("data", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public long TambahDataVar1(int id_data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", "");
        openDatabase();
        long returnValue = mDatabase.insert("variabel_1", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public long ImportDataVar1(int id_data, String nilai) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", nilai);
        openDatabase();
        long returnValue = mDatabase.insert("variabel_1", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public long TambahDataVar2(int id_data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", "");
        openDatabase();
        long returnValue = mDatabase.insert("variabel_2", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public long ImportDataVar2(int id_data, String nilai) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", nilai);
        openDatabase();
        long returnValue = mDatabase.insert("variabel_2", null, contentValues);
        closeDatabase();
        return returnValue;
    }

    public long updateDataHalaman1(int id_data, String nama, String deskripsi, String namaVar1, String namaVar2, String  n1, String n2, String h0, String h1, String alpha) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama_obyek", nama);
        contentValues.put("deskripsi", deskripsi);
        contentValues.put("variabel_sampel_1", namaVar1);
        contentValues.put("variabel_sampel_2", namaVar2);
        contentValues.put("h0", h0);
        contentValues.put("h1", h1);
        contentValues.put("n1", n1);
        contentValues.put("n2", n2);
        contentValues.put("alpha", alpha);
        String[] whereArgs = {Integer.toString(id_data)};
        openDatabase();
        long returnValue = mDatabase.update("data",contentValues, "id_data=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long updatePerhitungan(int id_data, double rata_rata_sampel_1, double rata_rata_sampel_2, double variansi_sampel_1, double variansi_sampel_2, double  t_hitung) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("rata_rata_sampel_1", rata_rata_sampel_1);
        contentValues.put("rata_rata_sampel_2", rata_rata_sampel_2);
        contentValues.put("variansi_sampel_1", variansi_sampel_1);
        contentValues.put("variansi_sampel_2", variansi_sampel_2);
        contentValues.put("t_hitung", t_hitung);
        String[] whereArgs = {Integer.toString(id_data)};
        openDatabase();
        long returnValue = mDatabase.update("data",contentValues, "id_data=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long updateNData(int id_data, String  n1, String n2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("n1", n1);
        contentValues.put("n2", n2);
        String[] whereArgs = {Integer.toString(id_data)};
        openDatabase();
        long returnValue = mDatabase.update("data",contentValues, "id_data=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long updateDataHalaman2_var1(int id_data, String nilai, int ke) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", nilai);
        String[] whereArgs = {Integer.toString(id_data)};
        openDatabase();
        long returnValue = mDatabase.update("variabel_1",contentValues, "id_data=? AND id_variabel_1 in (select id_variabel_1 from variabel_1 WHERE id_data = "+id_data+" ORDER BY id_variabel_1 LIMIT "+ke+",1) ", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long updateDataHalaman2_var2(int id_data, String nilai, int ke) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_data", id_data);
        contentValues.put("nilai", nilai);
        String[] whereArgs = {Integer.toString(id_data)};
        openDatabase();
        long returnValue = mDatabase.update("variabel_2",contentValues, "id_data=? AND id_variabel_2 in (select id_variabel_2 from variabel_2 WHERE id_data = "+id_data+" ORDER BY id_variabel_2 LIMIT "+ke+",1) ", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public int getId_DataTerakhir() {
        int id_data = 0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT MAX(id_data) FROM data", null);
        if(cursor.getCount() >0)

        {
            while (cursor.moveToNext()) {
                int id_data_ = cursor.getInt(0);
                id_data = id_data_;
            }
        }
        cursor.close();
        closeDatabase();
        return id_data;
    }

    public ArrayList<String> getListvar(int id_data) {
        ArrayList<String> listvar =new ArrayList<String>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT variabel_sampel_1,variabel_sampel_2 FROM data WHERE id_data="+id_data, null);
        if(cursor.getCount() >0)

        {
            while (cursor.moveToNext()) {
                String var1 = cursor.getString(0);
                String var2 = cursor.getString(1);
                listvar.add(var1);
                listvar.add(var2);
            }
        }
        cursor.close();
        closeDatabase();
        return listvar;
    }

    public double getData_rata_rata_variabel(int variabel, int id_data){
        Double rata_rata = 0.0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT AVG(nilai) FROM variabel_"+variabel+" WHERE id_data="+id_data, null);
        if(cursor.getCount() >0)

        {
            while (cursor.moveToNext()) {
                rata_rata = cursor.getDouble(0);
            }
        }
        cursor.close();
        closeDatabase();
        return rata_rata;
    }

    public boolean deleteDataById(String tabel,int id) {
        openDatabase();
        int result = mDatabase.delete(tabel,  "id_"+tabel+" =?", new String[]{String.valueOf(id)});
        closeDatabase();
        return result !=0;
    }

    public boolean deleteDatavar(String tabel,int id_data, int selisih) {
        openDatabase();
        int result = mDatabase.delete(tabel,  "id_data =?  AND id_"+tabel+" in (select id_"+tabel+" from "+tabel+" WHERE id_data = "+id_data+" ORDER BY id_"+tabel+" DESC LIMIT 0,"+selisih+") ", new String[]{String.valueOf(id_data)});
        closeDatabase();
        return result !=0;
    }

    public Double getT_distribusi(int df, double alpha) {
        double nilai = 0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT `"+alpha+"`  FROM t_distribusi WHERE df = "+ df , null);
        if(cursor.getCount() >0)

        {
            while (cursor.moveToNext()) {
                nilai = cursor.getDouble(0);
            }
        }
        cursor.close();
        closeDatabase();
        return nilai;
    }

    public List<DataTTabel> getListDatatTabel() {
        DataTTabel data = null;
        List<DataTTabel> dataList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM t_distribusi", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = new DataTTabel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3) ,cursor.getString(4),cursor.getString(  5),cursor.getString(6),cursor.getString(7), cursor.getString(8), cursor.getString(9));
            dataList.add(data);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dataList;
    }

}