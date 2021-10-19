package htpt.ui;

import com.google.gson.Gson;
import htpt.Main;
import htpt.connection.Connector;
import htpt.models.Student;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DetailDialog extends JDialog {
    private static final int MODE_ADD = 1;      // Chế độ Thêm sinh viên
    private static final int MODE_UPDATE = 2;   // Chế độ Cập nhật sinh viên
    private JPanel contentPane;
    private JButton btnAction;
    private JButton btnDelete;
    private JTextField tfMSV;
    private JTextField tfHo;
    private JTextField tfTen;
    private JTextField tfNgaySinh;
    private JRadioButton radioNam;
    private JRadioButton radioNu;
    private JTextField tfQueQuan;
    private int mode = MODE_ADD;

    private Gson gson;          // chuyển object -> json
    private Connector conn;     // Chứa các request

    private MainFrame mFrame;

    public DetailDialog(MainFrame mFrame, Student s) {
        setContentPane(contentPane);
        setModal(true);

        this.mFrame = mFrame;
        init();

        if (s != null) {
            setTitle("Thông tin sinh viên");

            String msv = s.getMsv();

            s = getStudentInfo(msv);
            if (s != null) {
                tfMSV.setText(s.getMsv());
                tfMSV.setEnabled(false);
                tfHo.setText(s.getHo());
                tfTen.setText(s.getTen());
                tfNgaySinh.setText(s.getNgaysinh());
                if (s.getGioitinh() == 0) {
                    radioNam.setSelected(true);
                } else {
                    radioNu.setSelected(true);
                }
                tfQueQuan.setText(s.getQuequan());

                btnAction.setText("CẬP NHẬT");
                mode = MODE_UPDATE;
            } else {
                this.setVisible(false);
                return;
            }

        } else {
            setTitle("Thêm sinh viên");
            btnAction.setText("THÊM");
            btnDelete.setVisible(false);
        }

        btnAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mode == MODE_ADD) {
                    Student s = getInputStudent();
                    postStudent(s);
                } else if (mode == MODE_UPDATE) {
                    Student s = getInputStudent();
                    putStudent(s);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mode == MODE_UPDATE) {
                    int choose = JOptionPane.showConfirmDialog(DetailDialog.this, "Xác nhận", "Bạn thực sự muốn xóa sinh viên này", JOptionPane.YES_NO_OPTION);
                    if (choose == JOptionPane.OK_OPTION) {
                        deleteStudent(tfMSV.getText());
                    }
                }
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocation(500, 200);
        setSize(700, 500);
        setVisible(true);
    }

    private void init() {
        conn = new Connector();
        gson = new Gson();

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(radioNam);
        btnGroup.add(radioNu);
    }

    /**
     * Lấy thông tin sinh viên theo mã sinh viên
     *
     * @param msv Mã sinh viên cần tìm
     * @return Sinh viên được tìm thấy, nếu không thì trả về null
     */
    private Student getStudentInfo(String msv) {
        Object[] response = conn.getListStudents(Main.URL + "/student/" + msv);
        int status = (Integer) response[0];

        String body = (String) response[1];

        JSONObject obj = new JSONObject(body);

        // 200: Lấy thông tin sinh viên thành công
        if (status == 200) {
            return gson.fromJson(obj.getJSONObject("data").toString(), Student.class);
        } else {
            String mess = obj.getString("message");
            JOptionPane.showMessageDialog(this, mess);
        }
        return null;
    }

    /**
     * Chuyển các dữ liệu trang nhập liệu trên màn hình sang đối tượng Student
     *
     * @return
     */
    private Student getInputStudent() {
        String msv = tfMSV.getText().trim();
        String ho = tfHo.getText().trim();
        String ten = tfTen.getText().trim();
        String ngaysinh = tfNgaySinh.getText().trim();
        int gioitinh = radioNam.isSelected() ? 0 : 1;
        String quequan = tfQueQuan.getText().trim();

        Student s = new Student(msv, ho, ten, ngaysinh, gioitinh, quequan);
        System.out.println(s.getNgaysinh());
        return s;
    }

    /**
     * Thêm sinh viên
     *
     * @param s Sinh viên cần thêm
     */
    private void postStudent(Student s) {
        String json = gson.toJson(s);
        Object[] response = conn.postStudent(Main.URL + "/student/", json);
        int status = (Integer) response[0];
        String body = (String) response[1];

        JSONObject obj = new JSONObject(body);
        String mess = obj.getString("message");

        // 201: Thêm sinh viên thành công
        if (status == 201) {
            JOptionPane.showMessageDialog(this, mess);
            mFrame.getListStudent();
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, mess);
        }
    }

    /**
     * Cập nhật thông tin sinh viên
     *
     * @param s Sinh viên cần cập nhật
     */
    private void putStudent(Student s) {
        String json = gson.toJson(s);
        System.out.println("-------------json----------");
        System.out.println(json);
        System.out.println("-----------------");
        Object[] response = conn.putStudent(Main.URL + "/student/" + s.getMsv(), json);
        int status = (Integer) response[0];
        String body = (String) response[1];

        System.out.println(body);

        JSONObject obj = new JSONObject(body);
        String mess = obj.getString("message");

        if (status == 200) {
            JOptionPane.showMessageDialog(this, mess);
            mFrame.getListStudent();
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, mess);
        }
    }

    /**
     * Xóa sinh viên theo mã sinh viên
     *
     * @param msv
     */
    private void deleteStudent(String msv) {
        Object[] response = conn.deleteStudent(Main.URL + "/student/" + msv);
        int status = (Integer) response[0];
        String body = (String) response[1];

        System.out.println(body);

        JSONObject obj = new JSONObject(body);
        String mess = obj.getString("message");

        if (status == 200) {
            JOptionPane.showMessageDialog(this, mess);
            mFrame.getListStudent();
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, mess);
        }
    }
}
