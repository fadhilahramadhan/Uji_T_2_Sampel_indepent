package fadhilahramadhan.skripsi.ujit2sampelindepent.Panduan;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fadhilahramadhan.skripsi.ujit2sampelindepent.Data.DataTTabel;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Database.Database;
import fadhilahramadhan.skripsi.ujit2sampelindepent.R;

public class tTabel extends AppCompatActivity {

    private Database mDBHelper;

    EditText df;
    Spinner alpha;
    TextView tTabel_hasil;
    Button cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panduan_t_tabel);

        df = findViewById(R.id.txttTabel_df);
        alpha = findViewById(R.id.txttTabel_spalpha);
        tTabel_hasil = findViewById(R.id.txttTabel_hasil);
        cari = findViewById(R.id.btntTabel_cari);

        isiSpiner();

        mDBHelper      = new Database(this);


        File database = getDatabasePath(Database.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)) {
                // Toast.makeText(getContext(), "Copy database succes", Toast.LENGTH_SHORT).show();
            }
        }

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(df.getText().toString())){
                    df.setError("Tidak boleh kosong");
                }else{
                    double t_tabel = mDBHelper.getT_distribusi(Integer.parseInt(df.getText().toString()), Double.parseDouble(alpha.getSelectedItem().toString()));
                    tTabel_hasil.setText("T tabel: "+ String.valueOf(t_tabel));
                    tTabel_hasil.setVisibility(View.VISIBLE);
                }
            }
        });

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

    public void isiSpiner(){

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

}
