package fadhilahramadhan.skripsi.ujit2sampelindepent.Data;

public class DataVariabel2 {

    private int id_variabel_2;
    private int id_data;
    private String nilai;

    public DataVariabel2(int id_variabel_2, int id_data, String nilai) {
        this.id_variabel_2  = id_variabel_2;
        this.id_data        = id_data;
        this.nilai          = nilai;
    }

    public int getId_variabel_2() {
        return id_variabel_2;
    }

    public void setId_variabel_2(int id_variabel_2) {
        this.id_variabel_2 = id_variabel_2;
    }

    public int getId_data() {
        return id_data;
    }

    public void setId_data(int id_data) {
        this.id_data = id_data;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }
}
