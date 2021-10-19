package htpt.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import htpt.Main;
import htpt.connection.Connector;
import htpt.models.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainFrame extends JFrame {
    Connector conn;
    Gson gson;
    List<Student> list = new ArrayList<Student>();
    DefaultTableModel model;
    private JPanel mainPanel;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JTable table;

    public MainFrame() {
        setTitle("Danh sách sinh viên");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Cambria", Font.BOLD, 18));

        model = new DefaultTableModel();
        model.addColumn("Mã sinh viên");
        model.addColumn("Họ");
        model.addColumn("Tên");
        model.addColumn("Ngày sinh");
        model.addColumn("Giới tính");
        model.addColumn("Quê quán");
        table.setModel(model);

        conn = new Connector();
        gson = new Gson();

        getListStudent();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetailDialog(null);
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getListStudent();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                System.out.println(selectedRow + " row");
                showDetailDialog(list.get(selectedRow));
            }
        });

        add(mainPanel);
        Main.centerScreen(this);
    }

    private void showDetailDialog(Student s) {
        DetailDialog dialog = new DetailDialog(this, s);
    }

    /**
     * Request lấy danh sách sinh viên và đổ dữ liệu vào Table
     */
    public void getListStudent() {
        Object[] response = conn.getListStudents(Main.URL + "/student/");

        // statusCode - mã trạng thái của response
        int status = (Integer) response[0];
        String body = (String) response[1];

        JSONObject obj = new JSONObject(body);
        // message của response
        String mess = obj.getString("message");

        System.out.println(mess);

        // Lấy mảng dữ liệu
        JSONArray jarr = obj.getJSONArray("data");

        // Chuyển dữ liệu từ json array -> ArrayList
        list = gson.fromJson(jarr.toString(), new TypeToken<ArrayList<Student>>() {
        }.getType());

        // Xóa hết dữ liệu trong Table
        model.setRowCount(0);

        for (Student s : list) {
            Vector row = new Vector();

            row.add(s.getMsv());
            row.add(s.getHo());
            row.add(s.getTen());
            row.add(s.getNgaysinh());
            row.add(s.getGioitinh() == 0 ? "Nam" : "Nữ");
            row.add(s.getQuequan());

            model.addRow(row);
        }

        System.out.println(list.size());
    }
}
