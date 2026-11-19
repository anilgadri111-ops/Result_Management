package ui;

import dao.ResultDAO;
import dao.StudentDAO;
import model.Result;
import model.Student;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static ui.LoginFrame.*;

public class SearchFrame extends JFrame {

    private JTextField txtRollNo;
    private JLabel lblName, lblDept, lblSem, lblPercentage;
    private JLabel lblOverallResult, lblOverallPct;
    private JTable table;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO = new StudentDAO();
    private ResultDAO resultDAO   = new ResultDAO();

    public SearchFrame(JFrame parent) {
        setTitle("Search Student Result");
        setSize(800, 580);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAVY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel lbl = new JLabel("  Search Student Result");
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(WHITE);
        topBar.add(lbl, BorderLayout.WEST);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        searchPanel.setBackground(WHITE);
        searchPanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        JLabel searchLbl = new JLabel("Roll Number:");
        searchLbl.setFont(new Font("Arial", Font.BOLD, 12));
        searchLbl.setForeground(TEXT_DARK);
        txtRollNo = new JTextField(18);
        txtRollNo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtRollNo.setBackground(new Color(247, 250, 252));
        txtRollNo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        txtRollNo.setPreferredSize(new Dimension(200, 36));
        JButton btnSearch = new JButton("  Search  ");
        btnSearch.setBackground(NAVY);
        btnSearch.setForeground(WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setPreferredSize(new Dimension(100, 36));
        btnSearch.addActionListener(e -> searchStudent());
        txtRollNo.addActionListener(e -> searchStudent());
        searchPanel.add(searchLbl);
        searchPanel.add(txtRollNo);
        searchPanel.add(btnSearch);

        // Content area
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // Info cards (LEFT)
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        infoPanel.setOpaque(false);
        infoPanel.setPreferredSize(new Dimension(190, 0));

        lblName       = new JLabel("-");
        lblDept       = new JLabel("-");
        lblSem        = new JLabel("-");
        lblPercentage = new JLabel("-");

        infoPanel.add(infoCard("STUDENT NAME", lblName, NAVY));
        infoPanel.add(infoCard("DEPARTMENT", lblDept, NAVY));
        infoPanel.add(infoCard("SEMESTER", lblSem, NAVY));
        infoPanel.add(infoCard("OVERALL %", lblPercentage, new Color(30, 126, 69)));

        // Results table (RIGHT)
        String[] cols = {"Subject", "Marks", "Max", "Percentage", "Grade", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setGridColor(new Color(237, 242, 247));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(232, 240, 251));

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Arial", Font.BOLD, 12));
        th.setBackground(NAVY);
        th.setForeground(WHITE);
        th.setPreferredSize(new Dimension(0, 34));

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerR);
        table.getColumnModel().getColumn(2).setCellRenderer(centerR);
        table.getColumnModel().getColumn(3).setCellRenderer(centerR);

        // Grade column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                super.getTableCellRendererComponent(t, v, s, f, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                if (!s) switch (String.valueOf(v)) {
                    case "A+": setForeground(new Color(22, 101, 52)); break;
                    case "A":  setForeground(new Color(30, 126, 69)); break;
                    case "B":  setForeground(NAVY); break;
                    case "C":  setForeground(new Color(192, 122, 0)); break;
                    default:   setForeground(new Color(197, 48, 48));
                }
                return this;
            }
        });

        // Status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                JLabel l = new JLabel(String.valueOf(v));
                l.setOpaque(true);
                l.setHorizontalAlignment(CENTER);
                l.setFont(new Font("Arial", Font.BOLD, 11));
                boolean pass = "PASS".equals(v);
                l.setBackground(s ? t.getSelectionBackground()
                        : (pass ? new Color(198, 246, 213) : new Color(254, 215, 215)));
                l.setForeground(s ? t.getSelectionForeground()
                        : (pass ? new Color(22, 101, 52) : new Color(155, 44, 44)));
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER_CLR, 1, true));
        scroll.getViewport().setBackground(WHITE);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(WHITE);
        tableWrapper.setBorder(new LineBorder(BORDER_CLR, 1, true));
        JPanel tHeader = new JPanel(new BorderLayout());
        tHeader.setBackground(NAVY);
        tHeader.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        JLabel tLbl = new JLabel("Subject-wise Results");
        tLbl.setFont(new Font("Arial", Font.BOLD, 13));
        tLbl.setForeground(WHITE);
        tHeader.add(tLbl);
        tableWrapper.add(tHeader, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);

        content.add(infoPanel, BorderLayout.WEST);
        content.add(tableWrapper, BorderLayout.CENTER);

        // Result banner (BOTTOM)
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(new Color(30, 126, 69));
        banner.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        lblOverallResult = new JLabel("Search a student to view their result");
        lblOverallResult.setFont(new Font("Arial", Font.BOLD, 14));
        lblOverallResult.setForeground(new Color(198, 246, 213));
        lblOverallPct = new JLabel("");
        lblOverallPct.setFont(new Font("Arial", Font.BOLD, 20));
        lblOverallPct.setForeground(WHITE);
        banner.add(lblOverallResult, BorderLayout.WEST);
        banner.add(lblOverallPct, BorderLayout.EAST);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(topBar, BorderLayout.NORTH);
        topSection.add(searchPanel, BorderLayout.SOUTH);

        add(topSection, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(banner, BorderLayout.SOUTH);
    }

    private JPanel infoCard(String caption, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 3, 0, 0, accent),
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        ));
        JLabel cap = new JLabel(caption);
        cap.setFont(new Font("Arial", Font.BOLD, 9));
        cap.setForeground(new Color(113, 128, 150));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setForeground(TEXT_DARK);
        card.add(cap, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void searchStudent() {
        String rollNo = txtRollNo.getText().trim();
        if (rollNo.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a roll number."); return; }

        Student student = studentDAO.getStudentByRollNo(rollNo);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with roll number: " + rollNo,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        lblName.setText(student.getName());
        lblDept.setText(student.getDepartment());
        lblSem.setText("Semester " + student.getSemester());

        List<Result> results = resultDAO.getResultsByStudentId(student.getStudentId());
        tableModel.setRowCount(0);

        if (results.isEmpty()) {
            lblPercentage.setText("-");
            lblOverallResult.setText("No results found for this student.");
            return;
        }

        double totalMarks = 0, totalMax = 0;
        boolean hasFail = false;
        for (Result r : results) {
            totalMarks += r.getMarksObtained();
            totalMax   += r.getMaxMarks();
            if ("FAIL".equals(r.getStatus())) hasFail = true;
            tableModel.addRow(new Object[]{
                r.getSubjectName(), r.getMarksObtained(), r.getMaxMarks(),
                String.format("%.1f%%", r.getPercentage()),
                r.getGrade(), r.getStatus()
            });
        }

        double pct = (totalMarks / totalMax) * 100;
        lblPercentage.setText(String.format("%.2f%%", pct));
        lblPercentage.setForeground(hasFail ? new Color(197, 48, 48) : new Color(30, 126, 69));

        JPanel banner = (JPanel) lblOverallResult.getParent();
        if (hasFail) {
            banner.setBackground(new Color(197, 48, 48));
            lblOverallResult.setForeground(new Color(254, 215, 215));
            lblOverallResult.setText("Overall Result: FAIL  —  " + student.getName());
        } else {
            banner.setBackground(new Color(30, 126, 69));
            lblOverallResult.setForeground(new Color(198, 246, 213));
            lblOverallResult.setText("Overall Result: PASS  —  " + student.getName());
        }
        lblOverallPct.setText(String.format("%.2f%%", pct));
    }
}
