package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import static ui.LoginFrame.*;

public class DashboardFrame extends JFrame {

    private String role, username;

    public DashboardFrame(String role, String username) {
        this.role = role;
        this.username = username;
        setTitle("Student Result Management System - Dashboard");
        setSize(680, 460);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        // ── Top bar ──────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAVY);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel("Student Result Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(WHITE);

        JPanel userBadge = new JPanel();
        userBadge.setBackground(new Color(255, 255, 255, 40));
        userBadge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        JLabel lblUser = new JLabel("Logged in: " + username + "  (" + role.toUpperCase() + ")");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 11));
        lblUser.setForeground(new Color(168, 196, 224));
        userBadge.add(lblUser);

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(userBadge, BorderLayout.EAST);

        // ── Center grid ──────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);
        center.setBorder(BorderFactory.createEmptyBorder(28, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 30; gbc.ipady = 20;

        if (role.equals("admin")) {
            addCard(center, gbc, "👨‍🎓", "Manage Students", "Add, edit, delete students",
                    new Color(232, 240, 251), new Color(26, 60, 110), 0, 0,
                    e -> new StudentForm(this).setVisible(true));
            addCard(center, gbc, "📝", "Enter Marks", "Subject-wise marks entry",
                    new Color(232, 248, 239), new Color(30, 126, 69), 1, 0,
                    e -> new ResultForm(this).setVisible(true));
            addCard(center, gbc, "📊", "View All Results", "Color-coded results table",
                    new Color(240, 235, 252), new Color(107, 63, 160), 0, 1,
                    e -> new ViewResultsFrame(this, -1).setVisible(true));
            addCard(center, gbc, "🔍", "Search by Roll No", "Instant result lookup",
                    new Color(254, 243, 226), new Color(192, 122, 0), 1, 1,
                    e -> new SearchFrame(this).setVisible(true));
        } else {
            addCard(center, gbc, "🔍", "Search My Result", "Search by roll number",
                    new Color(232, 240, 251), new Color(26, 60, 110), 0, 0,
                    e -> new SearchFrame(this).setVisible(true));
        }

        // ── Bottom bar ───────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottom.setBackground(BG);
        bottom.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));
        JButton btnLogout = new JButton("  Logout  ");
        btnLogout.setBackground(new Color(197, 48, 48));
        btnLogout.setForeground(WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        bottom.add(btnLogout);

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void addCard(JPanel panel, GridBagConstraints gbc,
                         String icon, String title, String sub,
                         Color bg, Color accent,
                         int gridx, int gridy,
                         ActionListener listener) {
        gbc.gridx = gridx; gbc.gridy = gridy;

        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(accent);
                g2.fillRect(0, 0, 4, getHeight());
            }
        };
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 1, true),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitle.setForeground(accent);

        JLabel lblSub = new JLabel(sub);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 10));
        lblSub.setForeground(accent);

        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(lblSub);

        card.add(lblIcon, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { listener.actionPerformed(null); }
            public void mouseEntered(MouseEvent e) { card.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { card.setBackground(bg); }
        });

        panel.add(card, gbc);
    }
}
