# Sistem Manajemen Restoran - Java Swing

Tugas Praktik 3 - Mata Kuliah STSI4201 Pemrograman Berbasis Desktop  
Universitas Terbuka  
Nama: SATRIA RANGGA JATI | NIM: 056283196 | Prodi: Sistem Informasi

## Deskripsi

Aplikasi desktop sistem manajemen restoran berbasis Java Swing murni (tanpa library eksternal).  
Aplikasi terdiri dari 3 tab utama: **Manajemen Menu**, **Pesanan**, dan **Struk**.  
Semua konsep pemrograman berorientasi objek diterapkan: abstraction, inheritance, encapsulation, dan polymorphism.

## Fitur Utama

| Modul | Fitur |
|-------|-------|
| Manajemen Menu | Tambah, hapus, simpan ke `menu.txt`, dan muat dari `menu.txt` |
| Pesanan | Pilih menu via `JComboBox`, input qty, tambah/hapus item, hitung total |
| Struk | Cetak struk lengkap, simpan ke `struk.txt`, dan muat dari `struk.txt` |

## Konsep OOP yang Diterapkan

| Konsep | Implementasi |
|--------|--------------|
| Abstraction | Class abstract `MenuItem` dengan method `tampilMenu()` |
| Inheritance | `Makanan`, `Minuman`, `Diskon` `extends MenuItem` |
| Encapsulation | Seluruh atribut `private`, diakses via getter dan setter |
| Polymorphism | `ArrayList<MenuItem>` menyimpan objek subclass, pemanggilan `tampilMenu()` sesuai tipe asli |

## Struktur Keputusan & Pengulangan

- `if`, `if else`, `if else if` (logika diskon 10% / 15%)
- `nested if` (status pesanan besar)
- `switch case` (baca file menu menurut jenis)
- `for`, `foreach`, `while`, `do while` (tabel, ArrayList, validasi)

## Exception Handling

`try-catch-finally` menangani:
- Input kosong
- Harga / qty bukan angka (`NumberFormatException`)
- File tidak ditemukan / gagal dibaca (`IOException`)
- Index menu tidak ditemukan
- Data pesanan kosong

Pesan error ditampilkan via `JOptionPane`.

## File I/O

`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`:
- `menu.txt` — daftar menu restoran
- `struk.txt` — isi struk pesanan

## Komponen Swing

`JFrame`, `JPanel`, `JTable`, `JScrollPane`, `JTextArea`, `JTextField`, `JComboBox`, `JButton`, `JMenuBar`, `JOptionPane`, `JSplitPane`, `JTabbedPane`.

## Syarat Sistem

- Java JDK 8 atau lebih baru
- NetBeans IDE (opsional, untuk menjalankan via GUI project)
- Tidak membutuhkan library eksternal

## Cara Menjalankan

### Via NetBeans
1. Buka NetBeans → `File → Open Project`, pilih folder `AppJavaRestaurant`.
2. Set `App.java` sebagai Main Class.
3. Run (`F6`).

### Via Command Line
```bash
cd src
javac restaurant/*.java
cd ..
java -cp src restaurant.App
```

## Struktur Folder

```
AppJavaRestaurant/
├── src/
│   └── restaurant/
│       ├── App.java                 # Entry point
│       ├── MenuItem.java            # Abstract class
│       ├── Makanan.java             # Inheritance
│       ├── Minuman.java             # Inheritance
│       ├── Diskon.java              # Inheritance
│       ├── MenuManager.java         # ArrayList<MenuItem>
│       ├── PesananManager.java      # ArrayList<MenuItem> + hitungDiskon()
│       ├── FileManager.java         # File I/O (menu.txt, struk.txt)
│       └── MainFrame.java           # Swing GUI (3 tab)
├── .gitignore
└── README.md
```

## Contoh Isi File

**menu.txt**
```
MAKANAN|Nasi Goreng|15000|Makanan Berat|Goreng
MINUMAN|Es Teh|5000|Minuman Dingin|Teh
MINUMAN|Jus Alpukat|14000|Minuman Dingin|Alpukat
DISKON|Promo Combo|50000|Paket|10
```

**struk.txt**
```
===== STRUK PESANAN =====
1. Nasi Goreng - Rp15000
2. Es Teh - Rp5000
----------------------------
Total Harga   : Rp20000
Diskon        : Rp0
Pajak 10%     : Rp2000
Biaya Layanan : Rp20000
----------------------------
Grand Total   : Rp42000
===========================
Status: PESANAN BIASA
Tier Diskon: 0%
```

## Logika Diskon

| Total Harga | Diskon |
|-------------|--------|
| > Rp200.000 | 15% |
| > Rp100.000 | 10% |
| ≤ Rp100.000 | 0% |

## Author

SATRIA RANGGA JATI — Sistem Informasi, Universitas Terbuka  
NIM: 056283196

---

**Tugas Praktik 3 — STSI4201 Pemrograman Berbasis Desktop**
