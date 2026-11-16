package restaurant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * FileManager menangani operasi File I/O untuk menu.txt dan struk.txt.
 * Menggunakan BufferedReader, BufferedWriter, FileReader, FileWriter.
 * Menggunakan try-catch-finally untuk exception handling.
 */
public class FileManager {
    private static final String FILE_MENU = "menu.txt";
    private static final String FILE_STRUK = "struk.txt";

    /**
     * Simpan daftar menu ke file menu.txt.
     * Format baris: jenis|nama|harga|kategori|jenisDetail
     */
    public void simpanMenu(ArrayList<MenuItem> daftarMenu) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_MENU));
            for (MenuItem item : daftarMenu) {
                if (item instanceof Makanan) {
                    Makanan m = (Makanan) item;
                    writer.write("MAKANAN|" + m.getNama() + "|" + m.getHarga()
                            + "|" + m.getKategori() + "|" + m.getJenisMakanan());
                    writer.newLine();
                } else if (item instanceof Minuman) {
                    Minuman m = (Minuman) item;
                    writer.write("MINUMAN|" + m.getNama() + "|" + m.getHarga()
                            + "|" + m.getKategori() + "|" + m.getJenisMinuman());
                    writer.newLine();
                } else if (item instanceof Diskon) {
                    Diskon d = (Diskon) item;
                    writer.write("DISKON|" + d.getNama() + "|" + d.getHarga()
                            + "|" + d.getKategori() + "|" + d.getPersentaseDiskon());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan menu: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Gagal menutup writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Baca daftar menu dari file menu.txt.
     */
    public ArrayList<MenuItem> bacaMenu() {
        ArrayList<MenuItem> daftarMenu = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_MENU));
            String baris;
            while ((baris = reader.readLine()) != null) {
                if (baris.trim().isEmpty()) {
                    continue;
                }
                String[] parts = baris.split("\\|");
                if (parts.length < 5) {
                    continue;
                }
                String jenis = parts[0];
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                String kategori = parts[3];
                String detail = parts[4];

                switch (jenis) {
                    case "MAKANAN":
                        daftarMenu.add(new Makanan(nama, harga, kategori, detail));
                        break;
                    case "MINUMAN":
                        daftarMenu.add(new Minuman(nama, harga, kategori, detail));
                        break;
                    case "DISKON":
                        daftarMenu.add(new Diskon(nama, harga, kategori, Double.parseDouble(detail)));
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("File menu tidak ditemukan atau gagal dibaca: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Format harga tidak valid dalam file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Gagal menutup reader: " + e.getMessage());
                }
            }
        }
        return daftarMenu;
    }

    /**
     * Simpan isi struk ke file struk.txt.
     */
    public void simpanStruk(String isiStruk) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_STRUK));
            writer.write(isiStruk);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan struk: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Gagal menutup writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Baca isi struk dari file struk.txt.
     */
    public String bacaStruk() {
        StringBuilder isi = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_STRUK));
            String baris;
            while ((baris = reader.readLine()) != null) {
                isi.append(baris).append("\n");
            }
        } catch (IOException e) {
            System.err.println("File struk tidak ditemukan atau gagal dibaca: " + e.getMessage());
            return "Struk tidak tersedia.";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Gagal menutup reader: " + e.getMessage());
                }
            }
        }
        return isi.toString();
    }
}
