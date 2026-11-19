package ui;

import dao.ResultDAO;
import model.Result;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import static ui.LoginFrame.*;

public class ViewResultsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private ResultDAO resultDAO = new ResultDAO();
    private int studentId;

    public ViewResultsFrame(JFrame parent, int studentId) {
        this.studentId = studentId;
        setTitle("View Results");
        setSize(920, 520);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        initComponents();
        loadResults();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAVY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel lbl = new JLabel("  All Student Results");
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(WHITE);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(255,255,255,60));
        btnRefresh.setForeground(WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 11));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255,255,255,80), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadResults());
        topBar.add(lbl, BorderLayout.WEST);
        topBar.add(btnRefresh, BorderLayout.EAST);

        // Table
        String[] cols = {"#", "Student Name", "Roll No", "Subject", "Marks", "Max", "Percentage", "Grade", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setGridColor(new Color(237, 242, 247));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(232, 240, 251));
        table.setSelectionForeground(NAVY);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(NAVY);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(NAVY);
                setForeground(WHITE);
                setFont(new Font("Arial", Font.BOLD, 12));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);

        // Grade renderer
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                super.getTableCellRendererComponent(t, v, s, f, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                if (!s) {
                    String g = String.valueOf(v);
                    switch (g) {
                        case "A+": setForeground(new Color(22, 101, 52)); break;
                        case "A":  setForeground(new Color(30, 126, 69)); break;
                        case "B":  setForeground(new Color(26, 60, 110)); break;
                        case "C":  setForeground(new Color(192, 122, 0)); break;
                        default:   setForeground(new Color(197, 48, 48));
                    }
                }
                return this;
            }
        });

        // Status renderer
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                JLabel lbl = new JLabel(String.valueOf(v));
                lbl.setHorizontalAlignment(CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 11));
                lbl.setOpaque(true);
                if (!s) {
                    boolean pass = "PASS".equals(v);
                    lbl.setBackground(pass ? new Color(198, 246, 213) : new Color(254, 215, 215));
                    lbl.setForeground(pass ? new Color(22, 101, 52) : new Color(155, 44, 44));
                } else {
                    lbl.setBackground(table.getSelectionBackground());
                    lbl.setForeground(table.getSelectionForeground());
                }
                lbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return lbl;
            }
        });

        // Zebra striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? WHITE : new Color(247, 250, 252));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
        // Re-apply grade & status renderers after zebra
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int row, int col) {
                super.getTableCellRendererComponent(t, v, s, f, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                if (!s) {
                    setBackground(row % 2 == 0 ? WHITE : new Color(247, 250, 252));
                    String g = String.valueOf(v);
                    switch (g) {
                        case "A+": setForeground(new Color(22, 101, 52)); break;
                        case "A":  setForeground(new Color(30, 126, 69)); break;
                        case "B":  setForeground(NAVY); break;
                        case "C":  setForeground(new Color(192, 122, 0)); break;
                        default:   setForeground(new Color(197, 48, 48));
                    }
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);

        add(topBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void loadResults() {
        tableModel.setRowCount(0);
        List<Result> results = studentId == -1
                ? resultDAO.getAllResults()
                : resultDAO.getResultsByStudentId(studentId);
        int i = 1;
        for (Result r : results) {
            tableModel.addRow(new Object[]{
                i++, r.getStudentName(), r.getRollNo(), r.getSubjectName(),
                r.getMarksObtained(), r.getMaxMarks(),
                String.format("%.1f%%", r.getPercentage()),
                r.getGrade(), r.getStatus()
            });
        }
    }
}
