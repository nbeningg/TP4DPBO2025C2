import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(480, 580);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel prodiLabel;
    private JComboBox prodiComboBox;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        // isi listMahasiswa
        populateList();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box jenis kelamin
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // atur isi combo box program studi
        String[] prodiData = {"", "Ilmu Komputer", "Pendidikan Ilmu Komputer"};
        prodiComboBox.setModel(new DefaultComboBoxModel(prodiData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex >= 0) {
                    // tambah confirmation dialog
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Apakah Anda yakin ingin menghapus data ini?",
                            "Konfirmasi Penghapusan",
                            JOptionPane.YES_NO_OPTION
                    );

                    // delete jika user konfirmasi iya
                    if (choice == JOptionPane.YES_OPTION) {
                        deleteData();
                    }
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // saat tombol
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedProdi = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                prodiComboBox.setSelectedItem(selectedProdi);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Program Studi"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        // isi tabel dengan listMahasiswa
        for (int i = 0; i < listMahasiswa.size(); i++){
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = listMahasiswa.get(i).getNim();
            row[2] = listMahasiswa.get(i).getNama();
            row[3] = listMahasiswa.get(i).getJenisKelamin();
            row[4] = listMahasiswa.get(i).getProdi();

            temp.addRow(row);
        }

        return temp;
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String prodi = prodiComboBox.getSelectedItem().toString();

        // tambahkan data ke dalam list
        listMahasiswa.add(new Mahasiswa(nim, nama, jenisKelamin, prodi));

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String prodi = prodiComboBox.getSelectedItem().toString();

        // ubah data mahasiswa di list
        listMahasiswa.get(selectedIndex).setNim(nim);
        listMahasiswa.get(selectedIndex).setNama(nama);
        listMahasiswa.get(selectedIndex).setJenisKelamin(jenisKelamin);
        listMahasiswa.get(selectedIndex).setProdi(prodi);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
    }

    public void deleteData() {
        // hapus data dari list
        listMahasiswa.remove(selectedIndex);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        prodiComboBox.setSelectedItem("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2303558", "Dewi Yanti", "Perempuan", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2311697", "Dinda Natania Sugara", "Perempuan", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2308372", "Muhammad Ridho Fajri", "Laki-laki", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2305056", "Syifa Nur Azizah Suhud", "Perempuan", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2305551", "Taufiq Nurrohman", "Laki-laki", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2300498", "Alvyn Hadrian Nugraha", "Laki-laki", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2309131", "Alya Nurul Hanifah", "Perempuan", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2300408", "Cynthia Hasna Mazaya", "Perempuan", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2300134", "Samsul Maarif Aripin", "Laki-laki", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2301333", "Zaenal Rifa'i Saepudin", "Laki-laki", "Pendidikan Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2308138", "Alifa Salsabila", "Perempuan", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2311399", "Faisal Nur Qolbi", "Laki-laki", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2304017", "Naeya Adeani Putri", "Perempuan", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2308355", "Raffi Adzril Alfaiz", "Laki-laki", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2309209", "Safira Aliyah Azmi", "Perempuan", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2300492", "Ahmad Izzuddin Azzam", "Laki-laki", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2300484", "Julian Dwi Satrio", "Laki-laki", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2312120", "Natasha Adinda Cantika", "Perempuan", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2305309", "Rasendriya Andhika", "Laki-laki", "Ilmu Komputer"));
        listMahasiswa.add(new Mahasiswa("2305274", "Zakiyah Hasanah", "Perempuan", "Ilmu Komputer"));
    }
}
