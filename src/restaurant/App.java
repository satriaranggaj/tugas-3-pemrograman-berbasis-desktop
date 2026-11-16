package restaurant;

/**
 * App adalah titik masuk aplikasi.
 * Membuat instance MainFrame dan menampilkannya.
 */
public class App {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
