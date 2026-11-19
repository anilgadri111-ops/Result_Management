package ui;

import dao.ResultDAO;
import dao.StudentDAO;
import model.Result;
import model.Student;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static ui.LoginFrame.*;

public class ResultForm extends JFrame {

    private JComboBox<Student> cmbStudent;
    private JComboBox<String> cmbSubject;
    private JTextField txtMarks, txtMaxMarks;
    private JLabel lblGrade, lblStatus, lblPercentage;
    private ResultDAO resultDAO = new ResultDAO();
    private StudentDAO studentDAO = new StudentDAO();

    private static final String[] SUBJECTS = {
        "Mathematics", "Physics", "Chemistry", "Computer Science",
        "English", "Data Structures", "DBMS", "Operating Systems",
        "Software Engineering", "Computer Networks"
    };

    public ResultForm(JFrame parent) {
        setTitle("Enter Student Marks");
        setSize(500, 560);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAVY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel lbl = new JLabel("  Enter Student Marks");
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(WHITE);
        topBar.add(lbl, BorderLayout.WEST);

        // Card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        // Student
        g.gridx = 0; g.gridy = 0; g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Select Student"), g);
        g.gridy = 1; g.insets = new Insets(0, 0, 14, 0);
        cmbStudent = new JComboBox<>();
        styleCombo(cmbStudent);
        card.add(cmbStudent, g);

        // Subject
        g.gridy = 2; g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Subject"), g);
        g.gridy = 3; g.insets = new Insets(0, 0, 14, 0);
        cmbSubject = new JComboBox<>(SUBJECTS);
        styleCombo(cmbSubject);
        card.add(cmbSubject, g);

        // Max marks
        g.gridy = 4; g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Maximum Marks"), g);
        g.gridy = 5; g.insets = new Insets(0, 0, 14, 0);
        txtMaxMarks = styledField("100");
        card.add(txtMaxMarks, g);

        // Marks obtained
        g.gridy = 6; g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Marks Obtained"), g);
        g.gridy = 7; g.insets = new Insets(0, 0, 18, 0);
        txtMarks = styledField("");
        card.add(txtMarks, g);

        // Live preview panel
        g.gridy = 8; g.insets = new Insets(0, 0, 18, 0);
        JPanel preview = new JPanel(new GridLayout(1, 3, 10, 0));
        preview.setBackground(new Color(247, 250, 252));
        preview.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        lblGrade = previewLabel("-", "GRADE");
        lblStatus = previewLabel("-", "STATUS");
        lblPercentage = previewLabel("-", "PERCENTAGE");
        preview.add(lblGrade.getParent());
        preview.add(lblStatus.getParent());
        preview.add(lblPercentage.getParent());
        card.add(preview, g);

        // Save button
        g.gridy = 9; g.insets = new Insets(0, 0, 10, 0);
        JButton btnSave = new JButton("SAVE RESULT");
        btnSave.setBackground(new Color(30, 126, 69));
        btnSave.setForeground(WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(0, 42));
        btnSave.addActionListener(e -> saveResult());
        card.add(btnSave, g);

        // View results button
        g.gridy = 10; g.insets = new Insets(0, 0, 0, 0);
        JButton btnView = new JButton("View All Results");
        btnView.setBackground(new Color(107, 63, 160));
        btnView.setForeground(WHITE);
        btnView.setFont(new Font("Arial", Font.BOLD, 13));
        btnView.setFocusPainted(false);
        btnView.setBorderPainted(false);
        btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnView.setPreferredSize(new Dimension(0, 36));
        btnView.addActionListener(e -> new ViewResultsFrame(this, -1).setVisible(true));
        card.add(btnView, g);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        wrapper.add(card, new GridBagConstraints());

        add(topBar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);

        txtMarks.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updatePreview(); }
        });
    }

    private JLabel previewLabel(String value, String caption) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel cap = new JLabel(caption);
        cap.setFont(new Font("Arial", Font.BOLD, 9));
        cap.setForeground(new Color(113, 128, 150));
        cap.setAlignmentX(CENTER_ALIGNMENT);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.BOLD, 18));
        val.setForeground(NAVY);
        val.setAlignmentX(CENTER_ALIGNMENT);
        p.add(cap);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return val;
    }

    private void updatePreview() {
        try {
            int marks = Integer.parseInt(txtMarks.getText().trim());
            int max   = Integer.parseInt(txtMaxMarks.getText().trim());
            if (marks >= 0 && marks <= max) {
                String grade  = Result.calculateGrade(marks, max);
                String status = Result.calculateStatus(marks, max);
                double pct    = (marks * 100.0) / max;
                lblGrade.setText(grade);
                lblStatus.setText(status);
                lblStatus.setForeground(status.equals("PASS")
                    ? new Color(30, 126, 69) : new Color(197, 48, 48));
                lblPercentage.setText(String.format("%.1f%%", pct));
                lblGrade.setForeground(NAVY);
            }
        } catch (NumberFormatException ex) {
            lblGrade.setText("-"); lblStatus.setText("-"); lblPercentage.setText("-");
        }
    }

    private void saveResult() {
        Student student = (Student) cmbStudent.getSelectedItem();
        if (student == null) { JOptionPane.showMessageDialog(this, "No student selected."); return; }
        try {
            int marks = Integer.parseInt(txtMarks.getText().trim());
            int max   = Integer.parseInt(txtMaxMarks.getText().trim());
            if (marks < 0 || marks > max) { JOptionPane.showMessageDialog(this, "Invalid marks."); return; }
            String subject = (String) cmbSubject.getSelectedItem();
            if (resultDAO.resultExists(student.getStudentId(), subject)) {
                JOptionPane.showMessageDialog(this, "Result for this subject already exists!",
                        "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Result r = new Result(student.getStudentId(), subject, marks, max);
            if (resultDAO.addResult(r)) {
                JOptionPane.showMessageDialog(this, "Result saved successfully!", "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
                txtMarks.setText("");
                lblGrade.setText("-"); lblStatus.setText("-"); lblPercentage.setText("-");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric marks.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudents() {
        for (Student s : studentDAO.getAllStudents()) cmbStudent.addItem(s);
    }

    private JTextField styledField(String def) {
        JTextField f = new JTextField(def);
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setForeground(TEXT_DARK);
        f.setBackground(new Color(247, 250, 252));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setBackground(new Color(247, 250, 252));
        c.setPreferredSize(new Dimension(0, 38));
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Arial", Font.BOLD, 10));
        l.setForeground(new Color(113, 128, 150));
        return l;
    }
}
