package restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * MainFrame adalah jendela utama aplikasi (Java Swing GUI).
 * Tampilan diadaptasi dari MainGUI (Aktivitas 8): JSplitPane dengan
 * 3 panel (daftar menu | pesanan | struk + total), JTabbedPane,
 * border bertajam, font monospaced untuk area struk, label total hijau.
 *
 * Terdapat 3 tab sesuai Tugas Praktik 3:
 *   1. Manajemen Menu   : tambah/hapus/simpan/muat menu (tabel + form input)
 *   2. Pesanan          : pilih menu, qty, tambah/hitung total/cetak struk
 *   3. Struk            : tampilan struk, simpan/muat struk
 *
 * Semua atribut private (encapsulation). Menggunakan ArrayList<MenuItem>
 * (polymorphism), try-catch-finally (exception handling), dan
 * BufferedReader/BufferedWriter/FileReader/FileWriter (File I/O).
 */
public class MainFrame extends JFrame {

    private MenuManager menuManager;
    private PesananManager pesananManager;
    private FileManager fileManager;

    // ================= KOMPONEN TAB 1: MANAJEMEN MENU =================
    private JTextField txtNama, txtHarga, txtKategori, txtJenis;
    private JComboBox<String> cbJenisItem;
    private JTable tblMenu;
    private DefaultTableModel modelMenu;

    // Tombol Tab 1
    private JButton btnTambahMenu, btnHapusMenu, btnSimpanMenu, btnMuatMenu;

    // ================= KOMPONEN TAB 2: PESANAN =================
    private JComboBox<String> cbPilihMenu;
    private JTextField txtQty;
    private JTable tblPesanan;
    private DefaultTableModel modelPesanan;

    // Tombol Tab 2
    private JButton btnTambahPesanan, btnHapusPesanan, btnHitungTotal, btnCetakStruk, btnResetPesanan;

    // ================= KOMPONEN TAB 3: STRUK =================
    private JTextArea txtStrukPesanan;   // area struk di tab Pesanan
    private JTextArea txtStruk;          // area struk di tab Struk
    private JLabel lblGrandTotal;

    // Tombol Tab 3
    private JButton btnSimpanStruk, btnMuatStruk;

    private JTabbedPane tabbedPane;

    // Warna aksen (mirip Aktivitas 8: hijau untuk total)
    private static final Color WARNA_HIJAU = new Color(0, 100, 0);

    public MainFrame() {
        menuManager = new MenuManager();
        pesananManager = new PesananManager();
        fileManager = new FileManager();

        setTitle("Sistem Manajemen Restoran - STSI4201 Pemrograman Berbasis Desktop");
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        JMenuItem miKeluar = new JMenuItem("Keluar");
        miKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mFile.add(miKeluar);
        menuBar.add(mFile);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manajemen Menu", createPanelManajemenMenu());
        tabbedPane.addTab("Pesanan", createPanelPesanan());
        tabbedPane.addTab("Struk", createPanelStruk());
        add(tabbedPane, BorderLayout.CENTER);
    }

    // ================================================================
    //  TAB 1: MANAJEMEN MENU
    //  Mirip panel "Kelola Menu (Pemilik)" di Aktivitas 8, ditambah
    //  form input untuk menambah menu baru.
    // ================================================================
    private JPanel createPanelManajemenMenu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- Form input di bagian atas ----------
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Menu Baru"));

        cbJenisItem = new JComboBox<>(new String[]{"Makanan", "Minuman", "Diskon"});
        txtNama = new JTextField();
        txtHarga = new JTextField();
        txtKategori = new JTextField();
        txtJenis = new JTextField();

        formPanel.add(new JLabel("Jenis Item:"));
        formPanel.add(cbJenisItem);
        formPanel.add(new JLabel("Nama Menu:"));
        formPanel.add(txtNama);
        formPanel.add(new JLabel("Harga:"));
        formPanel.add(txtHarga);
        formPanel.add(new JLabel("Kategori:"));
        formPanel.add(txtKategori);
        formPanel.add(new JLabel("Jenis Detail:"));
        formPanel.add(txtJenis);

        // ---------- Tabel menu di tengah ----------
        String[] colMenu = {"No", "Nama", "Harga", "Kategori", "Jenis Detail"};
        modelMenu = new DefaultTableModel(colMenu, 0);
        tblMenu = new JTable(modelMenu);
        tblMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadTableMenu();

        JScrollPane scrollMenu = new JScrollPane(tblMenu);
        scrollMenu.setPreferredSize(new Dimension(350, 300));

        // ---------- Tombol di bawah ----------
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnTambahMenu = new JButton("Tambah Menu");
        btnHapusMenu = new JButton("Hapus Menu Terpilih");
        btnSimpanMenu = new JButton("Simpan Menu");
        btnMuatMenu = new JButton("Muat Menu");
        panelTombol.add(btnTambahMenu);
        panelTombol.add(btnHapusMenu);
        panelTombol.add(btnSimpanMenu);
        panelTombol.add(btnMuatMenu);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollMenu, BorderLayout.CENTER);
        panel.add(panelTombol, BorderLayout.SOUTH);

        // --- Listener Tambah Menu ---
        btnTambahMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String jenis = (String) cbJenisItem.getSelectedItem();
                    String nama = txtNama.getText().trim();
                    String hargaText = txtHarga.getText().trim();
                    String kategori = txtKategori.getText().trim();
                    String jenisDetail = txtJenis.getText().trim();

                    // Exception handling: input kosong
                    if (nama.isEmpty() || hargaText.isEmpty() || kategori.isEmpty() || jenisDetail.isEmpty()) {
                        throw new Exception("Input tidak boleh kosong.");
                    }

                    // Exception handling: harga bukan angka
                    double harga;
                    try {
                        harga = Double.parseDouble(hargaText);
                    } catch (NumberFormatException ex) {
                        throw new Exception("Harga harus berupa angka.");
                    }

                    MenuItem item;
                    if (jenis.equals("Makanan")) {
                        item = new Makanan(nama, harga, kategori, jenisDetail);
                    } else if (jenis.equals("Minuman")) {
                        item = new Minuman(nama, harga, kategori, jenisDetail);
                    } else {
                        double persentase;
                        try {
                            persentase = Double.parseDouble(jenisDetail);
                        } catch (NumberFormatException ex) {
                            throw new Exception("Persentase diskon harus angka.");
                        }
                        item = new Diskon(nama, harga, kategori, persentase);
                    }

                    menuManager.tambahMenu(item);
                    loadTableMenu();
                    refreshComboMenu();
                    clearFormMenu();
                    JOptionPane.showMessageDialog(MainFrame.this, "Menu berhasil ditambahkan.",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error: " + ex.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Listener Hapus Menu ---
        btnHapusMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tblMenu.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Pilih menu pada tabel terlebih dahulu.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int konfirmasi = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Yakin ingin menghapus menu ini?",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    boolean sukses = menuManager.hapusMenu(row);
                    if (sukses) {
                        loadTableMenu();
                        refreshComboMenu();
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Menu berhasil dihapus.", "Sukses",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Index menu tidak ditemukan.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // --- Listener Simpan Menu ---
        btnSimpanMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fileManager.simpanMenu(menuManager.getDaftarMenu());
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Menu disimpan ke menu.txt.", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Gagal menyimpan menu: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Listener Muat Menu ---
        btnMuatMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<MenuItem> daftar = fileManager.bacaMenu();
                    menuManager = new MenuManager();
                    for (MenuItem item : daftar) {
                        menuManager.tambahMenu(item);
                    }
                    loadTableMenu();
                    refreshComboMenu();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Menu dimuat dari menu.txt.", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Gagal memuat menu: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void loadTableMenu() {
        modelMenu.setRowCount(0);
        int i = 1;
        // for loop (struktur pengulangan)
        for (MenuItem item : menuManager.getDaftarMenu()) {
            String jenisDetail = "";
            if (item instanceof Makanan) {
                jenisDetail = ((Makanan) item).getJenisMakanan();
            } else if (item instanceof Minuman) {
                jenisDetail = ((Minuman) item).getJenisMinuman();
            } else if (item instanceof Diskon) {
                jenisDetail = String.valueOf(((Diskon) item).getPersentaseDiskon());
            }
            modelMenu.addRow(new Object[]{i++, item.getNama(), item.getHarga(),
                    item.getKategori(), jenisDetail});
        }
    }

    private void clearFormMenu() {
        txtNama.setText("");
        txtHarga.setText("");
        txtKategori.setText("");
        txtJenis.setText("");
    }

    private void refreshComboMenu() {
        cbPilihMenu.removeAllItems();
        // foreach loop (struktur pengulangan)
        for (MenuItem item : menuManager.getDaftarMenu()) {
            cbPilihMenu.addItem(item.getNama());
        }
    }

    // ================================================================
    //  TAB 2: PESANAN
    //  Mirip panel "Menu Pelanggan" di Aktivitas 8:
    //  JSplitPane dengan daftar menu | pesanan | struk.
    // ================================================================
    private JPanel createPanelPesanan() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- Kiri: Daftar Menu ----------
        JPanel panelKiri = new JPanel(new BorderLayout());
        panelKiri.setBorder(BorderFactory.createTitledBorder("Daftar Menu Restoran"));

        String[] colMenu = {"No", "Nama", "Harga", "Kategori", "Jenis Detail"};
        DefaultTableModel modelMenuPilih = new DefaultTableModel(colMenu, 0);
        JTable tblMenuPilih = new JTable(modelMenuPilih);
        tblMenuPilih.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        muatTabelMenuPilih(modelMenuPilih);

        JScrollPane scrollMenu = new JScrollPane(tblMenuPilih);
        scrollMenu.setPreferredSize(new Dimension(330, 400));
        panelKiri.add(scrollMenu, BorderLayout.CENTER);

        // ---------- Tengah: Pesanan ----------
        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBorder(BorderFactory.createTitledBorder("Pesanan Pelanggan"));

        String[] colPesanan = {"No", "Nama Menu", "Harga", "Qty", "Subtotal"};
        modelPesanan = new DefaultTableModel(colPesanan, 0);
        tblPesanan = new JTable(modelPesanan);
        tblPesanan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPesanan = new JScrollPane(tblPesanan);
        panelTengah.add(scrollPesanan, BorderLayout.CENTER);

        // Form input pesanan (di atas tombol)
        JPanel formPesanan = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        formPesanan.add(new JLabel("Pilih Menu:"));
        cbPilihMenu = new JComboBox<>();
        refreshComboMenu();
        cbPilihMenu.setPreferredSize(new Dimension(200, 25));
        formPesanan.add(cbPilihMenu);
        formPesanan.add(new JLabel("Qty:"));
        txtQty = new JTextField(5);
        formPesanan.add(txtQty);
        panelTengah.add(formPesanan, BorderLayout.NORTH);

        // Tombol di bawah tabel pesanan
        JPanel panelTombolTengah = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnTambahPesanan = new JButton("Tambah Pesanan");
        btnHapusPesanan = new JButton("Hapus Item Terpilih");
        btnHitungTotal = new JButton("Hitung Total");
        btnCetakStruk = new JButton("Cetak Struk");
        btnResetPesanan = new JButton("Reset Pesanan");
        panelTombolTengah.add(btnTambahPesanan);
        panelTombolTengah.add(btnHapusPesanan);
        panelTombolTengah.add(btnHitungTotal);
        panelTombolTengah.add(btnCetakStruk);
        panelTombolTengah.add(btnResetPesanan);
        panelTengah.add(panelTombolTengah, BorderLayout.SOUTH);

        // ---------- Kanan: Struk + Total ----------
        JPanel panelKanan = new JPanel(new BorderLayout());
        panelKanan.setBorder(BorderFactory.createTitledBorder("Struk Pesanan"));

        txtStrukPesanan = new JTextArea(20, 28);
        txtStrukPesanan.setEditable(false);
        txtStrukPesanan.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtStrukPesanan.setText("Belum ada struk.\n\n1. Pilih menu dari daftar di kiri.\n"
                + "2. Isi jumlah, klik 'Tambah Pesanan'.\n"
                + "3. Klik 'Hitung Total'.\n4. Klik 'Cetak Struk'.");

        JScrollPane scrollStruk = new JScrollPane(txtStrukPesanan);
        panelKanan.add(scrollStruk, BorderLayout.CENTER);

        JPanel panelTotal = new JPanel(new BorderLayout());
        lblGrandTotal = new JLabel("TOTAL BAYAR: Rp0", SwingConstants.CENTER);
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGrandTotal.setForeground(WARNA_HIJAU);
        panelTotal.add(lblGrandTotal, BorderLayout.CENTER);
        panelKanan.add(panelTotal, BorderLayout.SOUTH);

        // Gabungkan kiri + tengah + kanan dengan JSplitPane (mirip Aktivitas 8)
        JSplitPane splitKiriTengah = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelKiri, panelTengah);
        splitKiriTengah.setResizeWeight(0.35);
        splitKiriTengah.setOneTouchExpandable(true);

        JSplitPane splitUtama = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitKiriTengah, panelKanan);
        splitUtama.setResizeWeight(0.70);
        splitUtama.setOneTouchExpandable(true);

        panel.add(splitUtama, BorderLayout.CENTER);

        // ================= EVENT LISTENER =================
        btnTambahPesanan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int indexMenu = cbPilihMenu.getSelectedIndex();
                    if (indexMenu < 0) {
                        throw new Exception("Pilih menu terlebih dahulu.");
                    }
                    String qtyText = txtQty.getText().trim();
                    if (qtyText.isEmpty()) {
                        throw new Exception("Qty tidak boleh kosong.");
                    }
                    int qty;
                    try {
                        qty = Integer.parseInt(qtyText);
                    } catch (NumberFormatException ex) {
                        throw new Exception("Qty harus berupa angka.");
                    }
                    if (qty <= 0) {
                        throw new Exception("Qty harus lebih dari 0.");
                    }

                    MenuItem item = menuManager.getMenu(indexMenu);
                    if (item == null) {
                        throw new Exception("Menu tidak ditemukan.");
                    }

                    // Menambahkan item sesuai qty (struktur pengulangan for)
                    for (int i = 0; i < qty; i++) {
                        pesananManager.tambahPesanan(item);
                    }
                    loadTablePesanan();
                    txtQty.setText("");
                    resetGrandTotal();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Pesanan ditambahkan: " + item.getNama() + " x" + qty,
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error: " + ex.getMessage(), "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnHapusPesanan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tblPesanan.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Pilih item pada tabel Pesanan terlebih dahulu.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int konfirmasi = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Yakin ingin menghapus item ini dari pesanan?",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    pesananManager.hapusPesanan(row);
                    loadTablePesanan();
                    resetGrandTotal();
                }
            }
        });

        btnHitungTotal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pesananManager.getJumlahPesanan() == 0) {
                        throw new Exception("Data pesanan kosong.");
                    }
                    double total = pesananManager.hitungTotal();
                    double diskon = pesananManager.hitungDiskon(total);
                    double pajak = total * 0.10;
                    double biayaLayanan = 20000;
                    double grandTotal = total - diskon + pajak + biayaLayanan;

                    lblGrandTotal.setText("TOTAL BAYAR: Rp" + (long) grandTotal);

                    String msg = "Total Harga   : Rp" + (long) total + "\n"
                            + "Diskon        : Rp" + (long) diskon + "\n"
                            + "Pajak 10%     : Rp" + (long) pajak + "\n"
                            + "Biaya Layanan : Rp" + biayaLayanan + "\n"
                            + "Grand Total   : Rp" + (long) grandTotal;
                    JOptionPane.showMessageDialog(MainFrame.this, msg,
                            "Ringkasan Pesanan", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCetakStruk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pesananManager.getJumlahPesanan() == 0) {
                        throw new Exception("Data pesanan kosong.");
                    }
                    String struk = pesananManager.buatStruk();
                    txtStrukPesanan.setText(struk);
                    txtStruk.setText(struk);
                    double grandTotal = hitungGrandTotal();
                    lblGrandTotal.setText("TOTAL BAYAR: Rp" + (long) grandTotal);
                    // Pindah ke tab Struk
                    tabbedPane.setSelectedIndex(2);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnResetPesanan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int konfirmasi = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Yakin ingin mengosongkan seluruh pesanan?",
                        "Konfirmasi Reset", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    pesananManager.kosongkanPesanan();
                    loadTablePesanan();
                    txtStrukPesanan.setText("Belum ada struk.\n\n1. Pilih menu dari daftar di kiri.\n"
                            + "2. Isi jumlah, klik 'Tambah Pesanan'.\n"
                            + "3. Klik 'Hitung Total'.\n4. Klik 'Cetak Struk'.");
                    resetGrandTotal();
                }
            }
        });

        // Double-click pada tabel menu langsung menambah pesanan (qty default 1)
        tblMenuPilih.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblMenuPilih.getSelectedRow();
                    if (row >= 0) {
                        cbPilihMenu.setSelectedIndex(row);
                        txtQty.setText("1");
                        try {
                            int indexMenu = cbPilihMenu.getSelectedIndex();
                            MenuItem item = menuManager.getMenu(indexMenu);
                            if (item != null) {
                                pesananManager.tambahPesanan(item);
                                loadTablePesanan();
                                txtQty.setText("");
                                resetGrandTotal();
                            }
                        } catch (Exception ex) {
                            // abaikan
                        }
                    }
                }
            }
        });

        return panel;
    }

    private void muatTabelMenuPilih(DefaultTableModel model) {
        model.setRowCount(0);
        int i = 1;
        // for loop
        for (MenuItem item : menuManager.getDaftarMenu()) {
            String jenisDetail = "";
            if (item instanceof Makanan) {
                jenisDetail = ((Makanan) item).getJenisMakanan();
            } else if (item instanceof Minuman) {
                jenisDetail = ((Minuman) item).getJenisMinuman();
            } else if (item instanceof Diskon) {
                jenisDetail = String.valueOf(((Diskon) item).getPersentaseDiskon());
            }
            model.addRow(new Object[]{i++, item.getNama(), item.getHarga(),
                    item.getKategori(), jenisDetail});
        }
    }

    private void loadTablePesanan() {
        modelPesanan.setRowCount(0);
        int i = 1;
        // for loop
        for (MenuItem item : pesananManager.getDaftarPesanan()) {
            modelPesanan.addRow(new Object[]{i++, item.getNama(), item.getHarga(),
                    1, item.getHarga()});
        }
    }

    private void resetGrandTotal() {
        lblGrandTotal.setText("TOTAL BAYAR: Rp0");
    }

    private double hitungGrandTotal() {
        double total = pesananManager.hitungTotal();
        double diskon = pesananManager.hitungDiskon(total);
        double pajak = total * 0.10;
        double biayaLayanan = 20000;
        return total - diskon + pajak + biayaLayanan;
    }

    // ================================================================
    //  TAB 3: STRUK
    //  Tampilan struk lengkap + tombol simpan/muat struk.
    // ================================================================
    private JPanel createPanelStruk() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("<html><b>Struk Pesanan</b><br>"
                + "Tampilkan, simpan, atau muat ulang struk pesanan.</html>");
        panel.add(lblInfo, BorderLayout.NORTH);

        txtStruk = new JTextArea(24, 40);
        txtStruk.setEditable(false);
        txtStruk.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollStruk = new JScrollPane(txtStruk);
        panel.add(scrollStruk, BorderLayout.CENTER);
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSimpanStruk = new JButton("Simpan Struk");
        btnMuatStruk = new JButton("Muat Struk");
        panelTombol.add(btnSimpanStruk);
        panelTombol.add(btnMuatStruk);
        panel.add(panelTombol, BorderLayout.SOUTH);

        btnSimpanStruk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String isi = txtStruk.getText().trim();
                if (isi.isEmpty() || isi.startsWith("Belum ada struk")) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Struk kosong, tidak ada yang disimpan.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    fileManager.simpanStruk(isi);
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Struk disimpan ke struk.txt.", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Gagal menyimpan struk: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnMuatStruk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String isi = fileManager.bacaStruk();
                    txtStruk.setText(isi);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Gagal memuat struk: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}
