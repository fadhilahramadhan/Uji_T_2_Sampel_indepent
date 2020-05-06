package fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import fadhilahramadhan.skripsi.ujit2sampelindepent.DataEditTambah;
import fadhilahramadhan.skripsi.ujit2sampelindepent.Database.Database;
import fadhilahramadhan.skripsi.ujit2sampelindepent.DetailData;
import fadhilahramadhan.skripsi.ujit2sampelindepent.MainActivity;
import fadhilahramadhan.skripsi.ujit2sampelindepent.R;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;


public class BerandaFragment extends Fragment {

    ImageButton btn_data, btn_analisis, btn_petunjuk, btn_tentang;
    private Database dbHelper;
    int id_data_ = 1;
    boolean berhasil_import = false;
    boolean masih_ada_kosong = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        getActivity().setTitle("Beranda");

        btn_data        = view.findViewById(R.id.btnMenu_data);
        btn_analisis    = view.findViewById(R.id.btnMenu_analisisData);
        btn_petunjuk    = view.findViewById(R.id.btnMenu_petunjuk);
        btn_tentang     = view.findViewById(R.id.btnMenu_tentang);

        btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new DataFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        btn_analisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            startActivityForResult(intent, 1);
                        }
                    }

                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btn_petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PanduanFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        btn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new TentangFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable Intent resultData) {
        if (requestCode == requestCode) {
            if (resultData == null)
                return;
            switch (requestCode) {
                case 1:
                    Uri filepath = resultData.getData();
                    try {
                        if (resultCode == RESULT_OK) {
                            try {


                                dbHelper = new Database(getContext());
                                dbHelper.TambahDataPertama();
                                id_data_ = dbHelper.getId_DataTerakhir();

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
                                        dbHelper.updateDataHalaman1(id_data_, str[2], str[3], str[4], str[5], String.valueOf(readCSV(filepath).size()-1),String.valueOf(readCSV(filepath).size()-1),str[6], str[7], "0.05");
                                    }

                                    dbHelper.ImportDataVar1(id_data_, data_sampel1);
                                    dbHelper.ImportDataVar2(id_data_, data_sampel2);

                                }

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

}
