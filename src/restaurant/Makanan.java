package restaurant;

/**
 * Makanan extends MenuItem (inheritance).
 * Menambahkan atribut jenisMakanan dan override tampilMenu().
 */
public class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String kategori, String jenisMakanan) {
        super(nama, harga, kategori);
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    public void setJenisMakanan(String jenisMakanan) {
        this.jenisMakanan = jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.println("[MAKANAN] " + getNama() + " | Rp" + getHarga()
                + " | Kategori: " + getKategori() + " | Jenis: " + jenisMakanan);
    }
}
