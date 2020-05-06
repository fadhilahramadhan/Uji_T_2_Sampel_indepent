package fadhilahramadhan.skripsi.ujit2sampelindepent.Data;

public class DataVariabel1 {

    private int id_variabel_1;
    private int id_data;
    private String nilai;

    public DataVariabel1(int id_variabel_1, int id_data, String nilai) {
        this.id_variabel_1  = id_variabel_1;
        this.id_data        = id_data;
        this.nilai          = nilai;
    }

    public int getId_variabel_1() {
        return id_variabel_1;
    }

    public void setId_variabel_1(int id_variabel_1) {
        this.id_variabel_1 = id_variabel_1;
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
