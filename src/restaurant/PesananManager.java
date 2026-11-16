package restaurant;

import java.util.ArrayList;

/**
 * PesananManager mengelola daftar pesanan menggunakan ArrayList<MenuItem>.
 * Fungsi: tambahPesanan, hitungTotal, hitungDiskon, buatStruk.
 * Menggunakan if else if untuk logika diskon, nested if, dan switch case.
 */
public class PesananManager {
    private ArrayList<MenuItem> daftarPesanan;

    public PesananManager() {
        daftarPesanan = new ArrayList<>();
    }

    public void tambahPesanan(MenuItem item) {
        daftarPesanan.add(item);
    }

    public void hapusPesanan(int index) {
        if (index >= 0 && index < daftarPesanan.size()) {
            daftarPesanan.remove(index);
        }
    }

    public double hitungTotal() {
        double total = 0;
        // foreach loop
        for (MenuItem item : daftarPesanan) {
            total += item.getHarga();
        }
        return total;
    }

    public double hitungDiskon(double total) {
        double diskon = 0;
        // if else if (struktur keputusan)
        if (total > 200000) {
            diskon = 0.15; // 15%
        } else if (total > 100000) {
            diskon = 0.10; // 10%
        } else {
            diskon = 0;
        }
        return total * diskon;
    }

    public String buatStruk() {
        if (daftarPesanan.isEmpty()) {
            return "Data pesanan kosong.";
        }

        StringBuilder struk = new StringBuilder();
        struk.append("===== STRUK PESANAN =====\n");

        // for loop untuk mencetak daftar pesanan
        for (int i = 0; i < daftarPesanan.size(); i++) {
            MenuItem item = daftarPesanan.get(i);
            struk.append((i + 1)).append(". ").append(item.getNama())
                    .append(" - Rp").append(item.getHarga()).append("\n");
        }

        double total = hitungTotal();
        double diskon = hitungDiskon(total);
        double pajak = total * 0.10; // pajak 10%
        double biayaLayanan = 20000;
        double grandTotal = total - diskon + pajak + biayaLayanan;

        struk.append("----------------------------\n");
        struk.append("Total Harga   : Rp").append(total).append("\n");
        struk.append("Diskon        : Rp").append(diskon).append("\n");
        struk.append("Pajak 10%     : Rp").append(pajak).append("\n");
        struk.append("Biaya Layanan : Rp").append(biayaLayanan).append("\n");
        struk.append("----------------------------\n");
        struk.append("Grand Total   : Rp").append(grandTotal).append("\n");
        struk.append("===========================\n");

        // nested if untuk keterangan
        if (grandTotal > 500000) {
            struk.append("Status: PESANAN BESAR\n");
            if (diskon > 0) {
                struk.append("Anda mendapatkan diskon besar!\n");
            }
        } else {
            struk.append("Status: PESANAN BIASA\n");
        }

        // switch case untuk kategori diskon
        int diskonTier;
        if (total > 200000) {
            diskonTier = 2;
        } else if (total > 100000) {
            diskonTier = 1;
        } else {
            diskonTier = 0;
        }

        switch (diskonTier) {
            case 2:
                struk.append("Tier Diskon: 15%\n");
                break;
            case 1:
                struk.append("Tier Diskon: 10%\n");
                break;
            default:
                struk.append("Tier Diskon: 0%\n");
                break;
        }

        return struk.toString();
    }

    public int getJumlahPesanan() {
        return daftarPesanan.size();
    }

    public ArrayList<MenuItem> getDaftarPesanan() {
        return daftarPesanan;
    }

    public void kosongkanPesanan() {
        daftarPesanan.clear();
    }

    // do while loop contoh untuk validasi sederhana
    public boolean adaPesanan() {
        boolean result = false;
        int i = 0;
        do {
            if (i < daftarPesanan.size()) {
                result = true;
            }
            i++;
        } while (i < 1 && !result);
        return result;
    }
}
