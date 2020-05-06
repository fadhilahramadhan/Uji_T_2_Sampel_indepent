package fadhilahramadhan.skripsi.ujit2sampelindepent.Data;

public class Data {

    private int id_data;
    private String nama;
    private String deskripsi;
    private String h0;
    private String h1;
    private Double alpha;
    private Integer n1;
    private Integer n2;
    private String variabel_1;
    private String variabel_2;
    private String rata_rata_sampel_1;
    private String rata_rata_sampel_2;
    private String variansi_sampel_1;
    private String variansi_sampel_2;
    private String t_hitung;

    public Data(int id_data, String nama, String deskripsi, String h0, String h1, Double alpha, Integer n1, Integer n2, String variabel_1, String variabel_2, String rata_rata_sampel_1, String rata_rata_sampel_2, String variansi_sampel_1, String variansi_sampel_2, String t_hitung) {

        this.id_data            = id_data;
        this.nama               = nama;
        this.deskripsi          = deskripsi;
        this.h0                 = h0;
        this.h1                 = h1;
        this.alpha              = alpha;
        this.n1                 = n1;
        this.n2                 = n2;
        this.variabel_1         = variabel_1;
        this.variabel_2         = variabel_2;
        this.rata_rata_sampel_1 = rata_rata_sampel_1;
        this.rata_rata_sampel_2 = rata_rata_sampel_2;
        this.variansi_sampel_1  = variansi_sampel_1;
        this.variansi_sampel_2  = variansi_sampel_2;
        this.t_hitung           = t_hitung;
    }

    public int getId_data() {
        return id_data;
    }

    public void setId_data(int id_data) {
        this.id_data = id_data;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getH0() {
        return h0;
    }

    public void setH0(String h0) {
        this.h0 = h0;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }

    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }

    public String getVariabel_1() {
        return variabel_1;
    }

    public void setVariabel_1(String variabel_1) {
        this.variabel_1 = variabel_1;
    }

    public String getVariabel_2() {
        return variabel_2;
    }

    public void setVariabel_2(String variabel_2) {
        this.variabel_2 = variabel_2;
    }

    public String getRata_rata_sampel_1() {
        return rata_rata_sampel_1;
    }

    public void setRata_rata_sampel_1(String rata_rata_sampel_1) {
        this.rata_rata_sampel_1 = rata_rata_sampel_1;
    }

    public String getRata_rata_sampel_2() {
        return rata_rata_sampel_2;
    }

    public void setRata_rata_sampel_2(String rata_rata_sampel_2) {
        this.rata_rata_sampel_2 = rata_rata_sampel_2;
    }

    public String getVariansi_sampel_1() {
        return variansi_sampel_1;
    }

    public void setVariansi_sampel_1(String variansi_sampel_1) {
        this.variansi_sampel_1 = variansi_sampel_1;
    }

    public String getVariansi_sampel_2() {
        return variansi_sampel_2;
    }

    public void setVariansi_sampel_2(String variansi_sampel_2) {
        this.variansi_sampel_1 = variansi_sampel_1;
    }

    public String getT_hitung() {
        return t_hitung;
    }

    public void setT_hitung(String t_hitung) {
        this.t_hitung = t_hitung;
    }

}
