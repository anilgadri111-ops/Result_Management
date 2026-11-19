package ui;

import dao.StudentDAO;
import model.Student;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static ui.LoginFrame.*;

public class StudentForm extends JFrame {

    private JTextField txtName, txtRollNo, txtDepartment;
    private JComboBox<Integer> cmbSemester;
    private JTable table;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO = new StudentDAO();
    private int selectedStudentId = -1;

    public StudentForm(JFrame parent) {
        setTitle("Manage Students");
        setSize(860, 560);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAVY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel lbl = new JLabel("  Manage Students");
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(WHITE);
        topBar.add(lbl, BorderLayout.WEST);

        // Main content
        JPanel content = new JPanel(new BorderLayout(14, 0));
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ── Form panel (LEFT) ────────────────────────────────
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        formCard.setPreferredSize(new Dimension(240, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(0, 0, 4, 0);

        JLabel formTitle = new JLabel("Student Details");
        formTitle.setFont(new Font("Arial", Font.BOLD, 13));
        formTitle.setForeground(NAVY);
        g.gridx = 0; g.gridy = 0;
        formCard.add(formTitle, g);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_CLR);
        g.gridy = 1; g.insets = new Insets(4, 0, 14, 0);
        formCard.add(sep, g);

        g.insets = new Insets(0, 0, 4, 0);
        String[] labels = {"Full Name", "Roll Number", "Department"};
        JTextField[] fields = new JTextField[3];
        for (int i = 0; i < labels.length; i++) {
            g.gridy = 2 + i * 2;
            formCard.add(fieldLabel(labels[i]), g);
            g.gridy = 3 + i * 2;
            g.insets = new Insets(0, 0, 12, 0);
            fields[i] = styledField();
            formCard.add(fields[i], g);
            g.insets = new Insets(0, 0, 4, 0);
        }
        txtName = fields[0]; txtRollNo = fields[1]; txtDepartment = fields[2];

        g.gridy = 8;
        formCard.add(fieldLabel("Semester"), g);
        g.gridy = 9; g.insets = new Insets(0, 0, 18, 0);
        cmbSemester = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8});
        cmbSemester.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbSemester.setBackground(new Color(247, 250, 252));
        formCard.add(cmbSemester, g);

        // Buttons
        g.gridy = 10; g.insets = new Insets(0, 0, 8, 0);
        formCard.add(actionBtn("Add Student", new Color(30, 126, 69),
            e -> addStudent()), g);
        g.gridy = 11;
        formCard.add(actionBtn("Update Student", NAVY,
            e -> updateStudent()), g);
        g.gridy = 12;
        formCard.add(actionBtn("Delete Student", new Color(197, 48, 48),
            e -> deleteStudent()), g);
        g.gridy = 13; g.insets = new Insets(0, 0, 0, 0);
        formCard.add(actionBtn("Clear Form", new Color(113, 128, 150),
            e -> clearForm()), g);

        // ── Table panel (RIGHT) ──────────────────────────────
        String[] cols = {"ID", "Name", "Roll No", "Department", "Semester"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = buildTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scroll.getViewport().setBackground(WHITE);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(WHITE);
        tableCard.setBorder(new LineBorder(BORDER_CLR, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(NAVY);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel tblTitle = new JLabel("All Students");
        tblTitle.setFont(new Font("Arial", Font.BOLD, 13));
        tblTitle.setForeground(WHITE);
        tableHeader.add(tblTitle);
        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(formCard, BorderLayout.WEST);
        content.add(tableCard, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                selectedStudentId = (int) tableModel.getValueAt(row, 0);
                txtName.setText((String) tableModel.getValueAt(row, 1));
                txtRollNo.setText((String) tableModel.getValueAt(row, 2));
                txtDepartment.setText((String) tableModel.getValueAt(row, 3));
                cmbSemester.setSelectedItem(tableModel.getValueAt(row, 4));
            }
        });
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Arial", Font.BOLD, 10));
        l.setForeground(new Color(113, 128, 150));
        return l;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setForeground(TEXT_DARK);
        f.setBackground(new Color(247, 250, 252));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    private JButton actionBtn(String text, Color color, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 34));
        btn.addActionListener(al);
        return btn;
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(30);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setGridColor(new Color(237, 242, 247));
        t.setSelectionBackground(new Color(232, 240, 251));
        t.setSelectionForeground(NAVY);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(247, 250, 252));
        t.getTableHeader().setForeground(new Color(74, 85, 104));
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    private void addStudent() {
        if (!validateForm()) return;
        Student s = new Student(txtName.getText().trim(), txtRollNo.getText().trim(),
                txtDepartment.getText().trim(), (int) cmbSemester.getSelectedItem());
        if (studentDAO.addStudent(s)) {
            showSuccess("Student added successfully!");
            clearForm(); loadStudents();
        } else {
            showError("Failed — Roll No may already exist.");
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) { showError("Select a student first."); return; }
        if (!validateForm()) return;
        Student s = new Student(selectedStudentId, txtName.getText().trim(),
                txtRollNo.getText().trim(), txtDepartment.getText().trim(),
                (int) cmbSemester.getSelectedItem());
        if (studentDAO.updateStudent(s)) {
            showSuccess("Student updated!"); clearForm(); loadStudents();
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) { showError("Select a student first."); return; }
        int c = JOptionPane.showConfirmDialog(this,
                "Delete this student and all their results?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION && studentDAO.deleteStudent(selectedStudentId)) {
            showSuccess("Student deleted."); clearForm(); loadStudents();
        }
    }

    private void clearForm() {
        txtName.setText(""); txtRollNo.setText(""); txtDepartment.setText("");
        cmbSemester.setSelectedIndex(0);
        selectedStudentId = -1;
        table.clearSelection();
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty() || txtRollNo.getText().trim().isEmpty()
                || txtDepartment.getText().trim().isEmpty()) {
            showError("Please fill all fields.");
            return false;
        }
        return true;
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        for (Student s : studentDAO.getAllStudents())
            tableModel.addRow(new Object[]{s.getStudentId(), s.getName(),
                    s.getRollNo(), s.getDepartment(), s.getSemester()});
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
