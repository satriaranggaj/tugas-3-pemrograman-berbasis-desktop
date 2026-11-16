package restaurant;

import java.util.ArrayList;

/**
 * MenuManager mengelola daftar menu menggunakan ArrayList<MenuItem> (polymorphism).
 * Fungsi: tambahMenu, hapusMenu, tampilMenu, cariMenu.
 * Menggunakan struktur pengulangan (for, foreach, while) dan struktur keputusan (if, switch).
 */
public class MenuManager {
    private ArrayList<MenuItem> daftarMenu;

    public MenuManager() {
        daftarMenu = new ArrayList<>();
    }

    public void tambahMenu(MenuItem item) {
        daftarMenu.add(item);
    }

    public boolean hapusMenu(int index) {
        if (index >= 0 && index < daftarMenu.size()) {
            daftarMenu.remove(index);
            return true;
        }
        return false;
    }

    public void tampilMenu() {
        // for loop
        for (int i = 0; i < daftarMenu.size(); i++) {
            MenuItem item = daftarMenu.get(i);
            System.out.print((i + 1) + ". ");
            item.tampilMenu(); // polymorphism
        }
    }

    public MenuItem cariMenu(String nama) {
        // foreach loop
        for (MenuItem item : daftarMenu) {
            if (item.getNama().equalsIgnoreCase(nama)) {
                return item;
            }
        }
        return null;
    }

    public int getJumlahMenu() {
        return daftarMenu.size();
    }

    public MenuItem getMenu(int index) {
        if (index >= 0 && index < daftarMenu.size()) {
            return daftarMenu.get(index);
        }
        return null;
    }

    public ArrayList<MenuItem> getDaftarMenu() {
        return daftarMenu;
    }

    // while loop contoh untuk menampilkan menu berdasarkan kategori
    public void tampilMenuPerKategori(String kategori) {
        int i = 0;
        while (i < daftarMenu.size()) {
            MenuItem item = daftarMenu.get(i);
            if (item.getKategori().equalsIgnoreCase(kategori)) {
                item.tampilMenu();
            }
            i++;
        }
    }
}
