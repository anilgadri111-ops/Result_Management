package ui;

import dao.UserDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    static final Color NAVY       = new Color(26, 60, 110);
    static final Color NAVY_LIGHT = new Color(37, 82, 148);
    static final Color BG         = new Color(240, 244, 248);
    static final Color WHITE      = Color.WHITE;
    static final Color TEXT_DARK  = new Color(45, 55, 72);
    static final Color TEXT_MUTED = new Color(113, 128, 150);
    static final Color BORDER_CLR = new Color(203, 213, 224);

    public LoginFrame() {
        setTitle("Student Result Management System");
        setSize(420, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Main card panel
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            BorderFactory.createEmptyBorder(0, 0, 20, 0)
        ));
        card.setPreferredSize(new Dimension(360, 420));

        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(NAVY);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(28, 20, 22, 20));

        // Icon circle
        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(NAVY);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "SRS";
                g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        iconCircle.setPreferredSize(new Dimension(54, 54));
        iconCircle.setMaximumSize(new Dimension(54, 54));
        iconCircle.setOpaque(false);
        iconCircle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("Student Result Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(WHITE);
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Please sign in to continue");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(new Color(168, 196, 224));
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        header.add(iconCircle);
        header.add(Box.createVerticalStrut(10));
        header.add(lblTitle);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSub);

        // ── Form body ────────────────────────────────────────
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 28, 10, 28));
        GridBagConstraints f = new GridBagConstraints();
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;

        // Username label
        f.gridx = 0; f.gridy = 0; f.insets = new Insets(0, 0, 4, 0);
        JLabel lblU = new JLabel("USERNAME");
        lblU.setFont(new Font("Arial", Font.BOLD, 10));
        lblU.setForeground(TEXT_MUTED);
        body.add(lblU, f);

        // Username field
        f.gridy = 1; f.insets = new Insets(0, 0, 14, 0);
        txtUsername = styledField(false);
        body.add(txtUsername, f);

        // Password label
        f.gridy = 2; f.insets = new Insets(0, 0, 4, 0);
        JLabel lblP = new JLabel("PASSWORD");
        lblP.setFont(new Font("Arial", Font.BOLD, 10));
        lblP.setForeground(TEXT_MUTED);
        body.add(lblP, f);

        // Password field
        f.gridy = 3; f.insets = new Insets(0, 0, 18, 0);
        txtPassword = (JPasswordField) styledField(true);
        body.add(txtPassword, f);

        // Login button
        f.gridy = 4; f.insets = new Insets(0, 0, 10, 0);
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(NAVY);
        btnLogin.setForeground(WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(0, 40));
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(NAVY_LIGHT); }
            public void mouseExited(MouseEvent e)  { btnLogin.setBackground(NAVY); }
        });
        body.add(btnLogin, f);

        // Status
        f.gridy = 5; f.insets = new Insets(0, 0, 6, 0);
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(197, 48, 48));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        body.add(lblStatus, f);

        // Hint
        f.gridy = 6;
        JLabel hint = new JLabel("Default: admin / admin  |  student / student");
        hint.setFont(new Font("Arial", Font.ITALIC, 10));
        hint.setForeground(new Color(160, 174, 192));
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        body.add(hint, f);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        gbc.insets = new Insets(20, 20, 20, 20);
        add(card, gbc);

        btnLogin.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());
    }

    private JTextField styledField(boolean isPassword) {
        JTextField f = isPassword ? new JPasswordField() : new JTextField();
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

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter username and password.");
            return;
        }
        UserDAO dao = new UserDAO();
        String role = dao.login(username, password);
        if (role != null) {
            dispose();
            new DashboardFrame(role, username).setVisible(true);
        } else {
            lblStatus.setText("Invalid username or password.");
            txtPassword.setText("");
        }
    }
}
