package restaurant;

/**
 * MenuItem adalah abstract class (abstraction).
 * Menentukan struktur dasar setiap item menu yang ada di restoran.
 * Semua atribut private (encapsulation).
 */
public abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    /**
     * Method abstract yang harus diimplementasi oleh subclass.
     * Polymorphism: pemanggilan melalui referensi MenuItem akan
     * menjalankan implementasi asli dari object sebenarnya.
     */
    public abstract void tampilMenu();
}
