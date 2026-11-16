package restaurant;

/**
 * Diskon extends MenuItem (inheritance).
 * Menambahkan atribut persentaseDiskon dan override tampilMenu().
 */
public class Diskon extends MenuItem {
    private double persentaseDiskon;

    public Diskon(String nama, double harga, String kategori, double persentaseDiskon) {
        super(nama, harga, kategori);
        this.persentaseDiskon = persentaseDiskon;
    }

    public double getPersentaseDiskon() {
        return persentaseDiskon;
    }

    public void setPersentaseDiskon(double persentaseDiskon) {
        this.persentaseDiskon = persentaseDiskon;
    }

    @Override
    public void tampilMenu() {
        System.out.println("[DISKON] " + getNama() + " | Rp" + getHarga()
                + " | Kategori: " + getKategori() + " | Diskon: " + persentaseDiskon + "%");
    }
}
