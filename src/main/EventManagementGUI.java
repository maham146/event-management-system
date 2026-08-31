package main;

import dao.UserDAO;
import dao.EventDAO;
import dao.BookingDAO;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

// ─────────────────────────────────────────────────────────────────────────────
//  COLOUR PALETTE & FONTS
// ─────────────────────────────────────────────────────────────────────────────
class Theme {
    static final Color BG_DARK      = new Color(10, 12, 20);
    static final Color BG_CARD      = new Color(18, 22, 36);
    static final Color BG_FIELD     = new Color(24, 30, 48);
    static final Color BG_HOVER     = new Color(30, 38, 60);
    static final Color ACCENT       = new Color(99, 102, 241);
    static final Color ACCENT_GLOW  = new Color(99, 102, 241, 60);
    static final Color ACCENT2      = new Color(16, 185, 129);
    static final Color DANGER       = new Color(239, 68, 68);
    static final Color WARNING      = new Color(245, 158, 11);
    static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    static final Color TEXT_MUTED   = new Color(100, 116, 139);
    static final Color BORDER       = new Color(30, 41, 59);

    static Font font(int style, float size) { return new Font("Segoe UI", style, (int) size); }
    static Font mono(float size)            { return new Font("Consolas", Font.PLAIN, (int) size); }
}

// ─────────────────────────────────────────────────────────────────────────────
//  REUSABLE UI COMPONENTS
// ─────────────────────────────────────────────────────────────────────────────
class GlassPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Color bgColor;
    public GlassPanel(Color bg) {
        this.bgColor = bg;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
        g2.dispose();
        super.paintComponent(g);
    }
}

class AccentButton extends JButton {

    private static final long serialVersionUID = 1L;

    private final Color base, hover;
    private boolean hovered;

    public AccentButton(String text, Color base) {
        super(text);
        this.base = base;
        this.hover = base.brighter();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(Theme.font(Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 44));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color c = hovered ? hover : base;
        g2.setPaint(new GradientPaint(0, 0, c, getWidth(), getHeight(), c.darker()));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        if (hovered) { g2.setColor(new Color(255,255,255,30)); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10); }
        g2.dispose();
        super.paintComponent(g);
    }
}

class StyledField extends JTextField {
    private static final long serialVersionUID = 1L;
    public StyledField(int cols) {
        super(cols);
        setOpaque(false);
        setBackground(Theme.BG_FIELD);
        setForeground(Theme.TEXT_PRIMARY);
        setCaretColor(Theme.ACCENT);
        setFont(Theme.font(Font.PLAIN, 14));
        setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.BG_FIELD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.dispose();
        super.paintComponent(g);
    }
}

class StyledPasswordField extends JPasswordField {
    private static final long serialVersionUID = 1L;
    public StyledPasswordField(int cols) {
        super(cols);
        setOpaque(false);
        setBackground(Theme.BG_FIELD);
        setForeground(Theme.TEXT_PRIMARY);
        setCaretColor(Theme.ACCENT);
        setFont(Theme.font(Font.PLAIN, 14));
        setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.BG_FIELD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.dispose();
        super.paintComponent(g);
    }
}

class StyledCombo extends JComboBox<String> {
    private static final long serialVersionUID = 1L;
    public StyledCombo(String[] items) {
        super(items);
        setOpaque(false);
        setBackground(Theme.BG_FIELD);
        setForeground(Theme.TEXT_PRIMARY);
        setFont(Theme.font(Font.PLAIN, 14));
        setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? Theme.ACCENT : Theme.BG_CARD);
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(6, 12, 6, 12));
                return this;
            }
        });
    }
}

class DarkTable extends JTable {
    private static final long serialVersionUID = 1L;
    public DarkTable(DefaultTableModel model) {
        super(model);
        setBackground(Theme.BG_CARD);
        setForeground(Theme.TEXT_PRIMARY);
        setFont(Theme.font(Font.PLAIN, 13));
        setRowHeight(38);
        setGridColor(Theme.BORDER);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setSelectionBackground(Theme.ACCENT_GLOW);
        setSelectionForeground(Theme.TEXT_PRIMARY);
        getTableHeader().setBackground(Theme.BG_DARK);
        getTableHeader().setForeground(Theme.TEXT_MUTED);
        getTableHeader().setFont(Theme.font(Font.BOLD, 12));
        getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
        setIntercellSpacing(new Dimension(0, 0));
        setOpaque(true);
    }
}

// FIX: Removed duplicate top-level NavButton class that conflicted with the
//      inner class NavButton inside EventManagementGUI. Only the inner class
//      (with 3-argument constructor) is kept, as it is the one actually used.

class Badge extends JLabel {
    private static final long serialVersionUID = 1L;
    public Badge(String text, Color color) {
        super(" " + text + " ");
        setOpaque(false);
        setForeground(color);
        setFont(Theme.font(Font.BOLD, 11));
        setBorder(new CompoundBorder(
                new LineBorder(color, 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
    }
}

class Toast extends JWindow {
    private static final long serialVersionUID = 1L;
    public Toast(Window parent, String msg, Color color) {
        super(parent);
        JLabel lbl = new JLabel("  " + msg + "  ");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(Theme.font(Font.BOLD, 13));
        lbl.setOpaque(true);
        lbl.setBackground(color);
        lbl.setBorder(new CompoundBorder(
                new LineBorder(color.brighter(), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        add(lbl);
        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width - getWidth() - 30,
                    screen.height - getHeight() - 60);
        setVisible(true);
        Timer t = new Timer(2800, e -> dispose());
        t.setRepeats(false);
        t.start();
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  MAIN APPLICATION FRAME
// ─────────────────────────────────────────────────────────────────────────────
public class EventManagementGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static model.User currentUser;
    private JPanel contentArea;
    private CardLayout cardLayout;
    // FIX: List type is now the inner NavButton (3-arg), not the removed outer one
    private java.util.List<NavButton> navButtons = new ArrayList<>();
    private final UserDAO    userDAO    = new UserDAO();
    private final EventDAO   eventDAO   = new EventDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    public EventManagementGUI() {
        super("EventFlow — Event Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        showAuthScreen();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  AUTH SCREEN
    // ─────────────────────────────────────────────────────────────────────────
    private void showAuthScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0,0,new Color(10,12,30),getWidth(),getHeight(),new Color(20,15,40)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(99,102,241,15)); g2.fillOval(-100,-100,400,400);
                g2.setColor(new Color(16,185,129,10));  g2.fillOval(getWidth()-200,getHeight()-200,400,400);
                g2.dispose();
            }
        };
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        GlassPanel card = new GlassPanel(Theme.BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 640)); // FIX: was 560, too short to show register button
        card.setBorder(new EmptyBorder(40,40,40,40));

        JLabel logo    = new JLabel("⚡ EventFlow");
        logo.setFont(Theme.font(Font.BOLD, 26)); logo.setForeground(Theme.ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Professional Event Management");
        tagline.setFont(Theme.font(Font.PLAIN, 13)); tagline.setForeground(Theme.TEXT_MUTED);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tabs = new JPanel(new GridLayout(1,2,0,0));
        tabs.setOpaque(false); tabs.setMaximumSize(new Dimension(340,40));
        tabs.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginTab = tabBtn("Sign In", true);
        JButton regTab   = tabBtn("Register", false);
        tabs.add(loginTab); tabs.add(regTab);

        CardLayout cl = new CardLayout();
        JPanel forms = new JPanel(cl);
        forms.setOpaque(false); forms.setMaximumSize(new Dimension(340,400)); // FIX: was 260, too short for register form (4 fields + button)
        forms.setAlignmentX(Component.CENTER_ALIGNMENT);
        forms.add(buildLoginForm(), "login");
        forms.add(buildRegisterForm(), "register");

        loginTab.addActionListener(e -> { cl.show(forms,"login");    setTabActive(loginTab,regTab); });
        regTab.addActionListener(e  -> { cl.show(forms,"register"); setTabActive(regTab,loginTab); });

        card.add(logo); card.add(Box.createVerticalStrut(4));
        card.add(tagline); card.add(Box.createVerticalStrut(28));
        card.add(tabs); card.add(Box.createVerticalStrut(24));
        card.add(forms);

        bg.add(card);
        revalidate(); repaint();
    }

    private JButton tabBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(Theme.font(Font.BOLD, 13)); b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(active ? Theme.ACCENT : Theme.BG_FIELD);
        b.setForeground(active ? Color.WHITE : Theme.TEXT_MUTED);
        return b;
    }
    private void setTabActive(JButton active, JButton inactive) {
        active.setBackground(Theme.ACCENT);    active.setForeground(Color.WHITE);
        inactive.setBackground(Theme.BG_FIELD); inactive.setForeground(Theme.TEXT_MUTED);
    }

    private JPanel buildLoginForm() {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        StyledField emailF = new StyledField(20);
        StyledPasswordField passF = new StyledPasswordField(20);
        emailF.setMaximumSize(new Dimension(340,42));
        passF.setMaximumSize(new Dimension(340,42));

        AccentButton btn = new AccentButton("Sign In →", Theme.ACCENT);
        btn.setMaximumSize(new Dimension(340,44)); btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(lbl("Email Address")); p.add(emailF); p.add(Box.createVerticalStrut(12));
        p.add(lbl("Password"));     p.add(passF);  p.add(Box.createVerticalStrut(20));
        p.add(btn);

        btn.addActionListener(e -> {
            String email = emailF.getText().trim();
            String pass  = new String(passF.getPassword());
            if (email.isEmpty() || pass.isEmpty()) { toast("Please fill in all fields", Theme.DANGER); return; }
            model.User u = userDAO.login(email, pass);
            if (u != null) {
                currentUser = u;
                toast("Welcome back, " + u.getName() + "!", Theme.ACCENT2);
                if (u.getRole().equalsIgnoreCase("Admin")) showAdminDashboard();
                else showUserDashboard();
            } else {
                toast("Invalid email or password", Theme.DANGER);
            }
        });
        return p;
    }

    private JPanel buildRegisterForm() {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        StyledField nameF  = new StyledField(20);
        StyledField emailF = new StyledField(20);
        StyledPasswordField passF = new StyledPasswordField(20);
        StyledCombo roleC = new StyledCombo(new String[]{"User","Admin"});
        nameF.setMaximumSize(new Dimension(340,42)); emailF.setMaximumSize(new Dimension(340,42));
        passF.setMaximumSize(new Dimension(340,42)); roleC.setMaximumSize(new Dimension(340,42));

        AccentButton btn = new AccentButton("Create Account →", Theme.ACCENT2);
        btn.setMaximumSize(new Dimension(340,44)); btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(lbl("Full Name")); p.add(nameF); p.add(Box.createVerticalStrut(8));
        p.add(lbl("Email"));    p.add(emailF); p.add(Box.createVerticalStrut(8));
        p.add(lbl("Password")); p.add(passF);  p.add(Box.createVerticalStrut(8));
        p.add(lbl("Role"));     p.add(roleC);  p.add(Box.createVerticalStrut(16));
        p.add(btn);

        btn.addActionListener(e -> {
            String name  = nameF.getText().trim();
            String email = emailF.getText().trim();
            String pass  = new String(passF.getPassword());
            String role  = (String) roleC.getSelectedItem();
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) { toast("Please fill all fields", Theme.DANGER); return; }
            // FIX: Broadened email validation to accept more common providers
            if (!email.contains("@") || !email.contains(".")) {
                toast("Invalid email format", Theme.DANGER); return;
            }
            if (role.equalsIgnoreCase("Admin") && userDAO.checkrole("Admin")) {
                toast("Admin role already exists!", Theme.WARNING); return;
            }
            userDAO.insertUser(new model.User(name, email, pass, role));
            toast("Account created! You can now sign in.", Theme.ACCENT2);
        });
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADMIN DASHBOARD
    // ─────────────────────────────────────────────────────────────────────────
    private void showAdminDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout(0,0));

        JPanel sidebar = buildSidebar("Admin", new String[][]{
            {"◈","Dashboard"}, {"◉","Events"}, {"⊕","Add Event"}, {"◆","Bookings"}, {"◎","Users"}, {"▦","Reports"}, {"⌂","Logout"}
        });

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_DARK);
        { JPanel _p = buildAdminHome();          _p.setName("Dashboard");    contentArea.add(_p, "Dashboard"); }
        { JPanel _p = buildEventsPanel(true);    _p.setName("Events");       contentArea.add(_p, "Events"); }
        { JPanel _p = buildAddEventPanel();      _p.setName("Add Event");    contentArea.add(_p, "Add Event"); }
        { JPanel _p = buildAdminBookingsPanel(); _p.setName("Bookings");     contentArea.add(_p, "Bookings"); }
        { JPanel _p = buildUsersPanel();         _p.setName("Users");        contentArea.add(_p, "Users"); }
        { JPanel _p = buildReportsPanel();       _p.setName("Reports");      contentArea.add(_p, "Reports"); }

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
        revalidate(); repaint();
        navigate("Dashboard");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  USER DASHBOARD
    // ─────────────────────────────────────────────────────────────────────────
    private void showUserDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout(0,0));

        JPanel sidebar = buildSidebar("User", new String[][]{
            {"◈","Dashboard"}, {"◉","Browse Events"}, {"◆","My Bookings"}, {"◎","Profile"}, {"⌂","Logout"}
        });

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_DARK);
        { JPanel _p = buildUserHome();         _p.setName("Dashboard");     contentArea.add(_p, "Dashboard"); }
        { JPanel _p = buildEventsPanel(false); _p.setName("Browse Events"); contentArea.add(_p, "Browse Events"); }
        { JPanel _p = buildMyBookingsPanel();  _p.setName("My Bookings");   contentArea.add(_p, "My Bookings"); }
        { JPanel _p = buildProfilePanel();     _p.setName("Profile");       contentArea.add(_p, "Profile"); }

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
        revalidate(); repaint();
        navigate("Dashboard");
    }

    private JPanel buildSidebar(String roleLabel, String[][] items) {
        navButtons.clear();

        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_CARD);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(24, 16, 16, 16));

        JLabel logo = new JLabel("⚡ EventFlow");
        logo.setFont(Theme.font(Font.BOLD, 20));
        logo.setForeground(Theme.ACCENT);

        JLabel role = new JLabel("● " + currentUser.getName());
        role.setFont(Theme.font(Font.PLAIN, 12));
        role.setForeground(Theme.TEXT_MUTED);

        Badge badge = new Badge(roleLabel,
                roleLabel.equals("Admin") ? Theme.WARNING : Theme.ACCENT2);

        top.add(logo);
        top.add(Box.createVerticalStrut(4));
        top.add(role);
        top.add(Box.createVerticalStrut(4));
        top.add(badge);
        top.add(Box.createVerticalStrut(28));

        for (String[] item : items) {
            if (item[1].equals("Logout")) {
                AccentButton logoutBtn = new AccentButton("⌂  Sign Out", Theme.DANGER);
                logoutBtn.addActionListener(e -> {
                    currentUser = null;
                    showAuthScreen();
                });
                top.add(Box.createVerticalGlue());
                top.add(logoutBtn);
                continue;
            }

            NavButton nb = new NavButton(item[0], item[1], item[1]);
            nb.addActionListener(e -> navigate(item[1]));

            top.add(nb);
            top.add(Box.createVerticalStrut(4));
            navButtons.add(nb);
        }

        sidebar.add(top, BorderLayout.NORTH);

        JLabel ver = new JLabel("v1.0 • EventFlow");
        ver.setFont(Theme.mono(10));
        ver.setForeground(Theme.TEXT_MUTED);
        ver.setBorder(new EmptyBorder(8, 16, 12, 0));

        sidebar.add(ver, BorderLayout.SOUTH);
        return sidebar;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  INNER NAV BUTTON  (FIX: only this version exists — outer duplicate removed)
    // ─────────────────────────────────────────────────────────────────────────
    class NavButton extends JButton {
        private static final long serialVersionUID = 1L;
        private boolean active  = false;
        private boolean hovered = false;
        private final String pageName;

        public NavButton(String icon, String label, String pageName) {
            super(icon + " " + label);
            this.pageName = pageName;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setForeground(Theme.TEXT_MUTED);
            setFont(Theme.font(Font.PLAIN, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(200, 44));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }

        public String getPageName() { return pageName; }

        public void setActive(boolean a) {
            this.active = a;
            setForeground(a ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED);
            setFont(Theme.font(a ? Font.BOLD : Font.PLAIN, 14));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (active) {
                g2.setColor(Theme.ACCENT_GLOW); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(Theme.ACCENT);      g2.fillRoundRect(0,8,3,getHeight()-16,3,3);
            } else if (hovered) {
                g2.setColor(Theme.BG_HOVER); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void rebuildPage(String page) {
        boolean isAdmin = currentUser != null && currentUser.getRole().equalsIgnoreCase("Admin");
        JPanel fresh;
        switch (page) {
            case "Dashboard"     -> fresh = isAdmin ? buildAdminHome()      : buildUserHome();
            case "Events"        -> fresh = buildEventsPanel(true);
            case "Browse Events" -> fresh = buildEventsPanel(false);
            case "Bookings"      -> fresh = buildAdminBookingsPanel();
            case "Users"         -> fresh = buildUsersPanel();
            case "Reports"       -> fresh = buildReportsPanel();
            case "My Bookings"   -> fresh = buildMyBookingsPanel();
            default              -> { return; }
        }
        for (int i = contentArea.getComponentCount() - 1; i >= 0; i--) {
            java.awt.Component c = contentArea.getComponent(i);
            if (page.equals(c.getName())) { contentArea.remove(i); break; }
        }
        fresh.setName(page);
        contentArea.add(fresh, page);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADMIN HOME (DASHBOARD)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildAdminHome() {
        JPanel p = page();
        p.add(pageTitle("Dashboard"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("Welcome back, " + currentUser.getName() + ". Here's what's happening."));
        p.add(Box.createVerticalStrut(24));

        JLabel evCountLbl = new JLabel(String.valueOf(eventDAO.getTotalEventCount()));
        JLabel bkCountLbl = new JLabel(String.valueOf(bookingDAO.getTotalBookingCount()));
        JLabel usCountLbl = new JLabel(String.valueOf(userDAO.getTotalUserCount()));
        evCountLbl.setForeground(Theme.ACCENT);  evCountLbl.setFont(Theme.font(Font.BOLD,28));
        bkCountLbl.setForeground(Theme.ACCENT2); bkCountLbl.setFont(Theme.font(Font.BOLD,28));
        usCountLbl.setForeground(Theme.WARNING); usCountLbl.setFont(Theme.font(Font.BOLD,28));

        JPanel stats = new JPanel(new GridLayout(1,3,16,0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(makeStatCard("Total Events",     evCountLbl, Theme.ACCENT));
        stats.add(makeStatCard("Total Bookings",   bkCountLbl, Theme.ACCENT2));
        stats.add(makeStatCard("Registered Users", usCountLbl, Theme.WARNING));

        String[] cols = {"ID","Name","Location","Event Date","Reg. Deadline","Category","Capacity","Price (PKR)","Special"};
        DefaultTableModel dashModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable dashTable = new DarkTable(dashModel);
        loadEventsIntoTable(dashModel, true);

        AccentButton dashRefresh = new AccentButton("  Refresh", Theme.ACCENT);
        dashRefresh.setPreferredSize(new Dimension(130,36));
        dashRefresh.addActionListener(e -> {
            evCountLbl.setText(String.valueOf(eventDAO.getTotalEventCount()));
            bkCountLbl.setText(String.valueOf(bookingDAO.getTotalBookingCount()));
            usCountLbl.setText(String.valueOf(userDAO.getTotalUserCount()));
            dashModel.setRowCount(0);
            loadEventsIntoTable(dashModel, true);
            toast("Dashboard refreshed!", Theme.ACCENT2);
        });

        JScrollPane dashSp = new JScrollPane(dashTable);
        dashSp.setBackground(Theme.BG_CARD); dashSp.getViewport().setBackground(Theme.BG_CARD);
        dashSp.setBorder(new LineBorder(Theme.BORDER, 1));

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4)); bot.setOpaque(false);
        JLabel hint = new JLabel(" Live overview — click Refresh to update");
        hint.setFont(Theme.font(Font.ITALIC,11)); hint.setForeground(Theme.TEXT_MUTED);
        bot.add(hint); bot.add(Box.createHorizontalStrut(16)); bot.add(dashRefresh);
        JPanel tableWrap = new JPanel(new BorderLayout()); tableWrap.setOpaque(false);
        tableWrap.add(dashSp, BorderLayout.CENTER); tableWrap.add(bot, BorderLayout.SOUTH);

        p.add(stats); p.add(Box.createVerticalStrut(28));
        p.add(sectionTitle("All Events")); p.add(Box.createVerticalStrut(12));
        p.add(tableWrap);
        return p;
    }

    private JPanel makeStatCard(String title, JLabel valueLabel, Color accent) {
        GlassPanel card = new GlassPanel(Theme.BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST; gc.gridx = 0;
        JLabel t = new JLabel(title); t.setForeground(Theme.TEXT_MUTED); t.setFont(Theme.font(Font.PLAIN,12));
        gc.gridy = 0; card.add(t, gc);
        gc.gridy = 1; gc.insets = new Insets(4,0,0,0); card.add(valueLabel, gc);
        return card;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  USER HOME
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildUserHome() {
        JPanel p = page();
        p.add(pageTitle("Welcome, " + currentUser.getName()));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("Discover and book upcoming events below. Double-click any row to book."));
        p.add(Box.createVerticalStrut(28));
        p.add(sectionTitle("Upcoming Events")); p.add(Box.createVerticalStrut(12));

        String[] cols = {"ID","Name","Location","Event Date","Reg. Deadline","Category","Capacity","Price (PKR)","Special"};
        DefaultTableModel uhModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable uhTable = new DarkTable(uhModel);
        loadEventsIntoTable(uhModel, false);

        uhTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = uhTable.getSelectedRow();
                    if (row >= 0) showBookingDialog((int) uhModel.getValueAt(row, 0));
                }
            }
        });

        AccentButton uhRefresh = new AccentButton("  Refresh", Theme.ACCENT);
        uhRefresh.setPreferredSize(new Dimension(120,36));
        uhRefresh.addActionListener(e -> { uhModel.setRowCount(0); loadEventsIntoTable(uhModel, false); toast("Refreshed!", Theme.ACCENT2); });

        JScrollPane uhSp = new JScrollPane(uhTable);
        uhSp.setBackground(Theme.BG_CARD); uhSp.getViewport().setBackground(Theme.BG_CARD);
        uhSp.setBorder(new LineBorder(Theme.BORDER,1));

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT,0,4)); bot.setOpaque(false);
        JLabel hint = new JLabel(" Double-click a row to book");
        hint.setFont(Theme.font(Font.ITALIC,11)); hint.setForeground(Theme.TEXT_MUTED);
        bot.add(hint); bot.add(Box.createHorizontalStrut(16)); bot.add(uhRefresh);
        JPanel wrap = new JPanel(new BorderLayout()); wrap.setOpaque(false);
        wrap.add(uhSp, BorderLayout.CENTER); wrap.add(bot, BorderLayout.SOUTH);
        p.add(wrap);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  EVENTS PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildEventsPanel(boolean isAdmin) {
        JPanel p = page();
        p.add(pageTitle(isAdmin ? "Manage Events" : "Browse Events"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub(isAdmin ? "Select a row, then Delete or Update." : "Double-click any event to book it."));
        p.add(Box.createVerticalStrut(16));

        String[] cols = {"ID","Name","Location","Event Date","Reg. Deadline","Category","Capacity","Price (PKR)","Special"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable table = new DarkTable(tableModel);
        loadEventsIntoTable(tableModel, isAdmin);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        if (isAdmin) {
            AccentButton del = new AccentButton("  Delete Event", Theme.DANGER);
            del.setPreferredSize(new Dimension(150,40));
            AccentButton upd = new AccentButton("  Update Event", Theme.WARNING);
            upd.setPreferredSize(new Dimension(150,40));
            toolbar.add(del); toolbar.add(Box.createHorizontalStrut(8)); toolbar.add(upd);

            del.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) { toast("Select an event row first", Theme.WARNING); return; }
                int eid = (int) tableModel.getValueAt(row, 0);
                String evName = (String) tableModel.getValueAt(row, 1);
                int bookingCount = bookingDAO.getBookings(eid);
                String msg = bookingCount > 0
                    ? "Event \"" + evName + "\" has " + bookingCount + " booking(s).\nDeleting it will also cancel all those bookings.\nContinue?"
                    : "Delete event \"" + evName + "\"? This cannot be undone.";
                int confirm = JOptionPane.showConfirmDialog(this, msg,
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    java.sql.Connection con = null;
                    try {
                        con = db.DBConnect.getConnection();
                        con.setAutoCommit(false);
                        java.sql.PreparedStatement ps1 = con.prepareStatement("DELETE FROM bookings WHERE event_id=?");
                        ps1.setInt(1, eid); ps1.executeUpdate(); ps1.close();
                        java.sql.PreparedStatement ps2 = con.prepareStatement("DELETE FROM events WHERE event_id=?");
                        ps2.setInt(1, eid);
                        int deleted = ps2.executeUpdate(); ps2.close();
                        con.commit(); con.close();
                        if (deleted > 0) {
                            tableModel.removeRow(row);
                            toast("Event \"" + evName + "\" deleted successfully", Theme.ACCENT2);
                            rebuildPage("Dashboard");
                        } else {
                            toast("Delete failed — event ID not found in DB", Theme.DANGER);
                        }
                    } catch (Exception ex) {
                        if (con != null) { try { con.rollback(); con.close(); } catch (Exception ignored) {} }
                        toast("Delete error: " + ex.getMessage(), Theme.DANGER);
                    }
                }
            });

            upd.addActionListener(e -> showUpdateEventDialog(tableModel, table));
        }

        if (!isAdmin) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.getSelectedRow();
                        if (row >= 0) showBookingDialog((int) tableModel.getValueAt(row, 0));
                    }
                }
            });
        }

        AccentButton refresh = new AccentButton("  Refresh", Theme.ACCENT);
        refresh.setPreferredSize(new Dimension(120,36));
        refresh.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadEventsIntoTable(tableModel, isAdmin);
            toast("Events refreshed!", Theme.ACCENT2);
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.BG_CARD); sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER, 1));

        JPanel hint = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4)); hint.setOpaque(false);
        JLabel hintLbl = new JLabel(isAdmin ? " Select a row to delete or update"
                                            : " Double-click a row to book the event");
        hintLbl.setFont(Theme.font(Font.ITALIC, 11)); hintLbl.setForeground(Theme.TEXT_MUTED);
        hint.add(hintLbl); hint.add(Box.createHorizontalStrut(16)); hint.add(refresh);

        JPanel wrap = new JPanel(new BorderLayout()); wrap.setOpaque(false);
        wrap.add(sp, BorderLayout.CENTER); wrap.add(hint, BorderLayout.SOUTH);

        p.add(toolbar); p.add(Box.createVerticalStrut(12)); p.add(wrap);
        return p;
    }

    private void loadEventsIntoTable(DefaultTableModel model, boolean isAdmin) {
        List<Object[]> events = isAdmin ? eventDAO.getAllEvents() : eventDAO.getUpcomingEvents();
        for (Object[] row : events) model.addRow(row);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADD EVENT PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildAddEventPanel() {
        JPanel p = page();
        p.add(pageTitle("Create New Event"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("Fill in the details below to publish a new event."));
        p.add(Box.createVerticalStrut(24));

        GlassPanel form = new GlassPanel(Theme.BG_CARD);
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(28,28,28,28));
        form.setMaximumSize(new Dimension(760, 600));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; gc.insets = new Insets(6,8,6,8);

        StyledField nameF    = field(); StyledField locF   = field();
        StyledField capF     = field(); StyledField priceF = field();
        StyledField evDateF  = field(); evDateF.setText("YYYY-MM-DD");
        StyledField regDateF = field(); regDateF.setText("YYYY-MM-DD");
        StyledCombo catC     = new StyledCombo(new String[]{"Workshop","Seminar","SportsEvent"});
        StyledField specialF = field();

        catC.addActionListener(e -> {
            String cat = (String) catC.getSelectedItem();
            specialF.setToolTipText("Workshop".equals(cat) ? "Trainer Name" : "Seminar".equals(cat) ? "Speaker Name" : "Team Name");
        });

        Object[][] rows = {
            {"Event Name",nameF,    "Location",       locF},
            {"Capacity",  capF,     "Price (PKR)",    priceF},
            {"Event Date",evDateF,  "Reg. Last Date", regDateF},
            {"Category",  catC,     "Special Name",   specialF}
        };
        for (int r = 0; r < rows.length; r++) {
            for (int c = 0; c < 4; c += 2) {
                gc.gridy = r*2; gc.gridx = c/2*2; gc.weightx = 0;
                JLabel l = new JLabel((String)rows[r][c]);
                l.setForeground(Theme.TEXT_MUTED); l.setFont(Theme.font(Font.BOLD,11));
                form.add(l, gc);
                gc.gridy = r*2+1; gc.weightx = 1;
                form.add((Component)rows[r][c+1], gc);
            }
        }

        AccentButton submit = new AccentButton("  Publish Event", Theme.ACCENT2);
        submit.setPreferredSize(new Dimension(200,44));
        gc.gridy = 9; gc.gridx = 0; gc.gridwidth = 4; gc.insets = new Insets(20,8,0,8);
        form.add(submit, gc);

        submit.addActionListener(e -> {
            try {
                String name    = nameF.getText().trim();
                String loc     = locF.getText().trim();
                int    cap     = Integer.parseInt(capF.getText().trim());
                double price   = Double.parseDouble(priceF.getText().trim());
                Date   evDate  = Date.valueOf(evDateF.getText().trim());
                Date   regDate = Date.valueOf(regDateF.getText().trim());
                String cat     = (String) catC.getSelectedItem();
                String special = specialF.getText().trim();

                if (!LocalDate.now().isBefore(evDate.toLocalDate())) {
                    toast("Event date must be in the future!", Theme.DANGER); return;
                }
                if (!evDate.toLocalDate().isAfter(regDate.toLocalDate())) {
                    toast("Event date must be AFTER registration deadline!", Theme.DANGER); return;
                }
                if (name.isEmpty() || loc.isEmpty() || special.isEmpty()) {
                    toast("Please fill all fields!", Theme.DANGER); return;
                }

                model.Event ev;
                if ("Workshop".equals(cat))     ev = new Workshop(name,loc,cap,evDate,regDate,cat,price,special);
                else if ("Seminar".equals(cat)) ev = new Seminar(name,loc,cap,evDate,regDate,cat,price,special);
                else                            ev = new SportsEvent(name,loc,cap,evDate,regDate,cat,price,special);

                eventDAO.createEvent(ev);
                ev.notifyusers();
                toast("Event published successfully!", Theme.ACCENT2);
                clearFields(nameF, locF, capF, priceF, specialF);
                evDateF.setText("YYYY-MM-DD"); regDateF.setText("YYYY-MM-DD");
            } catch (Exception ex) {
                toast("Please check your input: " + ex.getMessage(), Theme.DANGER);
            }
        });

        p.add(form);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  USERS PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildUsersPanel() {
        JPanel p = page();
        p.add(pageTitle("Registered Users"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("View all users registered in the system."));
        p.add(Box.createVerticalStrut(20));

        String[] cols = {"ID","Name","Email","Role"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable table = new DarkTable(model);
        for (Object[] row : userDAO.getAllUsers()) model.addRow(row);

        AccentButton refresh = new AccentButton("  Refresh", Theme.ACCENT);
        refresh.setPreferredSize(new Dimension(120,36));
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            for (Object[] row : userDAO.getAllUsers()) model.addRow(row);
            toast("Users refreshed!", Theme.ACCENT2);
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.BG_CARD); sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER, 1));

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4)); bot.setOpaque(false);
        bot.add(refresh);
        JPanel wrap = new JPanel(new BorderLayout()); wrap.setOpaque(false);
        wrap.add(sp, BorderLayout.CENTER); wrap.add(bot, BorderLayout.SOUTH);
        p.add(wrap);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  REPORTS PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildReportsPanel() {
        JPanel p = page();
        p.add(pageTitle("Reports"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("Analyze bookings, events, and user activity."));
        p.add(Box.createVerticalStrut(24));

        String[][] reports = {
            {"◉","All Bookings",    "View every booking in the system"},
            {"⊕","Upcoming Events", "Events scheduled in the future"},
            {"◈","Full Capacity",   "Events that have reached max capacity"},
        };
        Color[] accents = {Theme.ACCENT2, Theme.ACCENT, Theme.DANGER};

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 16));
        cards.setOpaque(false); cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        for (int i = 0; i < reports.length; i++) {
            final int idx = i;
            GlassPanel card = new GlassPanel(Theme.BG_CARD);
            card.setLayout(new BorderLayout());
            card.setBorder(new EmptyBorder(20,20,20,20));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel ic    = new JLabel(reports[i][0]); ic.setFont(Theme.font(Font.PLAIN,26)); ic.setForeground(accents[i]);
            JLabel title = new JLabel(reports[i][1]); title.setFont(Theme.font(Font.BOLD,14)); title.setForeground(Theme.TEXT_PRIMARY);
            JLabel desc  = new JLabel("<html>"+reports[i][2]+"</html>"); desc.setFont(Theme.font(Font.PLAIN,12)); desc.setForeground(Theme.TEXT_MUTED);

            JPanel inner = new JPanel(); inner.setOpaque(false);
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.add(ic); inner.add(Box.createVerticalStrut(8)); inner.add(title); inner.add(Box.createVerticalStrut(4)); inner.add(desc);
            card.add(inner, BorderLayout.CENTER);
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { showReportDialog(idx); }
            });
            cards.add(card);
        }

        p.add(cards);
        return p;
    }

    private void showReportDialog(int idx) {
        String[] titles = {"All Bookings","Upcoming Events","Full Capacity Events"};
        JDialog dlg = new JDialog(this, titles[idx], true);
        dlg.setSize(900, 500); dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_DARK);
        dlg.setLayout(new BorderLayout());

        DefaultTableModel model;
        DarkTable table;

        if (idx == 0) {
            String[] cols = {"Booking ID","Event ID","User ID","Date","Status","Payment Method","Payment Status"};
            model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
            table = new DarkTable(model);
            for (Object[] row : bookingDAO.getAllBookings()) model.addRow(row);
        } else if (idx == 1) {
            String[] cols = {"ID","Name","Location","Event Date","Reg. Deadline","Category","Capacity","Price","Special"};
            model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
            table = new DarkTable(model);
            for (Object[] row : eventDAO.getUpcomingEvents()) model.addRow(row);
        } else {
            String[] cols = {"ID","Name","Capacity","Bookings","Status"};
            model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
            table = new DarkTable(model);
            for (Object[] ev : eventDAO.getAllEvents()) {
                int eid = (int) ev[0];
                int cap = (int) ev[6];
                int bkd = bookingDAO.getBookings(eid);
                if (bkd >= cap) model.addRow(new Object[]{eid, ev[1], cap, bkd, "FULL"});
            }
        }

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER,1));
        dlg.add(sp, BorderLayout.CENTER);
        dlg.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  MY BOOKINGS PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildMyBookingsPanel() {
        JPanel p = page();
        p.add(pageTitle("My Bookings"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("View and manage your event bookings."));
        p.add(Box.createVerticalStrut(20));

        String[] cols = {"Booking ID","Event ID","Date","Status","Payment Method","Payment Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable table = new DarkTable(model);
        for (Object[] row : bookingDAO.getBookingsForUser(currentUser.getId())) model.addRow(row);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolbar.setOpaque(false); toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        AccentButton cancelBtn  = new AccentButton("  Cancel Booking", Theme.DANGER);
        cancelBtn.setPreferredSize(new Dimension(180, 40));
        AccentButton refreshBtn = new AccentButton("  Refresh", Theme.ACCENT);
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        toolbar.add(cancelBtn); toolbar.add(Box.createHorizontalStrut(8)); toolbar.add(refreshBtn);

        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { toast("Select a booking first", Theme.WARNING); return; }
            int bid = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking #"+bid+"?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookingDAO.cancelBooking(bid);
                model.setValueAt("Cancelled", row, 3);
                toast("Booking cancelled", Theme.ACCENT2);
            }
        });

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Object[] row : bookingDAO.getBookingsForUser(currentUser.getId())) model.addRow(row);
            toast("Bookings refreshed!", Theme.ACCENT2);
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.BG_CARD); sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER, 1));

        p.add(toolbar); p.add(Box.createVerticalStrut(12)); p.add(sp);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADMIN BOOKINGS PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildAdminBookingsPanel() {
        JPanel p = page();
        p.add(pageTitle("All Bookings"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("View and manage every booking in the system."));
        p.add(Box.createVerticalStrut(20));

        String[] cols = {"Booking ID","Event ID","User ID","Booking Date","Status","Payment Method","Payment Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        DarkTable table = new DarkTable(model);
        for (Object[] row : bookingDAO.getAllBookings()) model.addRow(row);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolbar.setOpaque(false); toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        AccentButton cancelBtn  = new AccentButton("  Cancel Booking", Theme.DANGER);
        cancelBtn.setPreferredSize(new Dimension(180, 40));
        AccentButton refreshBtn = new AccentButton("  Refresh", Theme.ACCENT);
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        toolbar.add(cancelBtn); toolbar.add(Box.createHorizontalStrut(8)); toolbar.add(refreshBtn);

        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { toast("Select a booking first", Theme.WARNING); return; }
            int bid = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking #"+bid+"?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookingDAO.cancelBooking(bid);
                model.setValueAt("Cancelled", row, 4);
                toast("Booking cancelled", Theme.ACCENT2);
            }
        });

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Object[] row : bookingDAO.getAllBookings()) model.addRow(row);
            toast("Bookings refreshed!", Theme.ACCENT2);
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.BG_CARD); sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER, 1));

        p.add(toolbar); p.add(Box.createVerticalStrut(12)); p.add(sp);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  PROFILE PAGE
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildProfilePanel() {
        JPanel p = page();
        p.add(pageTitle("My Profile"));
        p.add(Box.createVerticalStrut(4));
        p.add(pageSub("Update your personal information."));
        p.add(Box.createVerticalStrut(24));

        GlassPanel form = new GlassPanel(Theme.BG_CARD);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(28,32,28,32));
        form.setMaximumSize(new Dimension(500,480));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel av = new JLabel(String.valueOf(currentUser.getName().charAt(0)));
        av.setFont(Theme.font(Font.BOLD,32)); av.setForeground(Color.WHITE);
        av.setOpaque(true); av.setBackground(Theme.ACCENT);
        av.setHorizontalAlignment(SwingConstants.CENTER);
        av.setPreferredSize(new Dimension(64,64));
        av.setBorder(new EmptyBorder(14,20,14,20));
        av.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(av); form.add(Box.createVerticalStrut(20));

        StyledField nameF  = field(); nameF.setText(currentUser.getName());
        StyledField emailF = field(); emailF.setText(currentUser.getEmail());
        StyledPasswordField passF = new StyledPasswordField(20);
        passF.setMaximumSize(new Dimension(440,42));

        addFormRow(form,"Full Name", nameF);
        addFormRow(form,"Email Address", emailF);
        addFormRow(form,"New Password (leave blank to keep)", passF);
        form.add(Box.createVerticalStrut(20));

        AccentButton save = new AccentButton("  Save Changes", Theme.ACCENT);
        save.setMaximumSize(new Dimension(440,44)); save.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(save);

        save.addActionListener(e -> {
            int id = currentUser.getId();
            String newName  = nameF.getText().trim();
            String newEmail = emailF.getText().trim();
            String newPass  = new String(passF.getPassword());
            if (!newName.equals(currentUser.getName()))   userDAO.updateName(id, newName);
            if (!newEmail.equals(currentUser.getEmail())) userDAO.updateEmail(id, newEmail);
            if (!newPass.isEmpty())                       userDAO.updatePassword(id, newPass);
            toast("Profile updated successfully!", Theme.ACCENT2);
        });

        p.add(form);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  BOOKING DIALOG  (FIX: Payment subclasses now actually instantiated & called)
    // ─────────────────────────────────────────────────────────────────────────
    private void showBookingDialog(int eventId) {
        JDialog dlg = new JDialog(this, "Book Event #" + eventId, true);
        dlg.setSize(460, 540); dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_CARD);

        JPanel inner = new JPanel(); inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(28,32,28,32));

        JLabel title = new JLabel("Book Event #" + eventId);
        title.setFont(Theme.font(Font.BOLD,20)); title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledField dateF = field(); dateF.setText(LocalDate.now().toString());
        StyledCombo payC  = new StyledCombo(new String[]{"Card Payment","JazzCash Payment","SimpleCash Payment"});
        dateF.setMaximumSize(new Dimension(400,42));

        StyledField cardNumF = field(); StyledField cvvF   = field();
        StyledField expF     = field(); StyledField phoneF = field();
        StyledField otpF     = field();

        JPanel payDetails = new JPanel(); payDetails.setOpaque(false);
        payDetails.setLayout(new BoxLayout(payDetails, BoxLayout.Y_AXIS));

        Runnable refreshPay = () -> {
            payDetails.removeAll();
            String sel = (String) payC.getSelectedItem();
            if ("Card Payment".equals(sel)) {
                addFormRow(payDetails,"Card Number", cardNumF);
                addFormRow(payDetails,"CVV", cvvF);
                addFormRow(payDetails,"Expiry Date (YYYY-MM-DD)", expF);
            } else if ("JazzCash Payment".equals(sel)) {
                addFormRow(payDetails,"Phone Number", phoneF);
                addFormRow(payDetails,"OTP", otpF);
            }
            payDetails.revalidate(); payDetails.repaint();
        };
        payC.addActionListener(e -> refreshPay.run());
        refreshPay.run();

        AccentButton confirm = new AccentButton("  Confirm Booking", Theme.ACCENT2);
        confirm.setMaximumSize(new Dimension(400,44)); confirm.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirm.addActionListener(e -> {
            try {
                Date bookDate = Date.valueOf(dateF.getText().trim());
                Date regDate  = eventDAO.ValidRegistration_Date(eventId);
                if (bookDate.toLocalDate().isAfter(regDate.toLocalDate())) {
                    toast("Booking date is after the registration deadline!", Theme.DANGER); return;
                }
                if (bookingDAO.viewDuplicate(eventId, currentUser.getId())) {
                    toast("You already booked this event!", Theme.WARNING); return;
                }
                if (bookingDAO.getBookings(eventId) >= eventDAO.getCapacity(eventId)) {
                    toast("No seats remaining for this event!", Theme.DANGER); return;
                }

                double amount = eventDAO.geteventPrice(eventId);
                String sel    = (String) payC.getSelectedItem();
                model.Payment pay;
                String payName;

                // FIX: Actually instantiate the correct Payment subclass and call payment()
                if ("Card Payment".equals(sel)) {
                    pay     = new CardPayment(amount, cardNumF.getText().trim(),
                                              cvvF.getText().trim(),
                                              new java.util.Date(Date.valueOf(expF.getText().trim()).getTime()));
                    payName = "Card Payment";
                } else if ("JazzCash Payment".equals(sel)) {
                    pay     = new JazzCash_Payment(amount, phoneF.getText().trim(),
                                                   Integer.parseInt(otpF.getText().trim()));
                    payName = "JazzCash Payment";
                } else {
                    pay     = new SimpleCash_Payment(amount);
                    payName = "SimpleCash Payment";
                }

                boolean ok = pay.payment();
                if (ok) {
                    Booking b = new Booking(eventId, currentUser.getId(),
                                            bookDate.toLocalDate(), "Confirmed", payName, "Paid");
                    bookingDAO.insertBooking(b);
                    // FIX: Don't rely on insertBooking's boolean return value — Oracle's
                    // con.commit() can throw after a successful insert, landing in the catch
                    // block and returning false even though the row was saved.
                    // Verify by querying the DB directly instead.
                    if (bookingDAO.viewDuplicate(eventId, currentUser.getId())) {
                        toast("Booking confirmed!", Theme.ACCENT2);
                        dlg.dispose();
                    } else {
                        toast("Booking failed, try again", Theme.DANGER);
                    }
                }
            } catch (Exception ex) {
                toast("Invalid input: " + ex.getMessage(), Theme.DANGER);
            }
        });

        inner.add(title); inner.add(Box.createVerticalStrut(20));
        addFormRow(inner,"Booking Date (YYYY-MM-DD)", dateF);
        addFormRow(inner,"Payment Method", payC);
        inner.add(payDetails); inner.add(Box.createVerticalStrut(20));
        inner.add(confirm);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null); scroll.getViewport().setBackground(Theme.BG_CARD);
        dlg.add(scroll);
        dlg.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  UPDATE EVENT DIALOG
    // ─────────────────────────────────────────────────────────────────────────
    private void showUpdateEventDialog(DefaultTableModel tableModel, DarkTable table) {
        JDialog dlg = new JDialog(this, "Update Event", true);
        dlg.setSize(400,420); dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_CARD);

        JPanel inner = new JPanel(); inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(24,28,24,28));

        JLabel title = new JLabel("Update Event");
        title.setFont(Theme.font(Font.BOLD,18)); title.setForeground(Theme.TEXT_PRIMARY);

        StyledField valF = field(); valF.setMaximumSize(new Dimension(340,42));
        String[] fields = {"Event Name","Location","Capacity","Event Date (YYYY-MM-DD)",
                           "Reg. Last Date (YYYY-MM-DD)","Category","Price"};
        StyledCombo fieldC = new StyledCombo(fields);

        AccentButton upd = new AccentButton("  Apply Update", Theme.WARNING);
        upd.setMaximumSize(new Dimension(340,44)); upd.setAlignmentX(Component.CENTER_ALIGNMENT);

        upd.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if (row < 0) { toast("Select an event row first", Theme.WARNING); return; }
                int evId = (int) tableModel.getValueAt(row, 0);
                String val = valF.getText().trim();
                int fi = fieldC.getSelectedIndex();
                switch (fi) {
                    case 0 -> { eventDAO.updateName(evId, val);                       tableModel.setValueAt(val, row, 1); }
                    case 1 -> { eventDAO.updateLocation(evId, val);                   tableModel.setValueAt(val, row, 2); }
                    case 2 -> { eventDAO.updateCapacity(evId, Integer.parseInt(val)); tableModel.setValueAt(Integer.parseInt(val), row, 6); }
                    case 3 -> { eventDAO.updateEventDate(evId, Date.valueOf(val));     tableModel.setValueAt(Date.valueOf(val), row, 3); }
                    case 4 -> { eventDAO.updateRegLastDate(evId, Date.valueOf(val));   tableModel.setValueAt(Date.valueOf(val), row, 4); }
                    case 5 -> { eventDAO.updateCategory(evId, val);                   tableModel.setValueAt(val, row, 5); }
                    case 6 -> { eventDAO.updatePrice(evId, Double.parseDouble(val));  tableModel.setValueAt(Double.parseDouble(val), row, 7); }
                }
                toast("Event updated!", Theme.ACCENT2);
                dlg.dispose();
            } catch (Exception ex) {
                toast("Invalid input: " + ex.getMessage(), Theme.DANGER);
            }
        });

        JLabel hint = new JLabel("Updating selected row in table");
        hint.setFont(Theme.font(Font.ITALIC, 11)); hint.setForeground(Theme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(title); inner.add(Box.createVerticalStrut(4)); inner.add(hint);
        inner.add(Box.createVerticalStrut(16));
        addFormRow(inner,"Field to Update", fieldC);
        addFormRow(inner,"New Value", valF);
        inner.add(Box.createVerticalStrut(20)); inner.add(upd);
        dlg.add(inner); dlg.setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel page() {
        JPanel p = new JPanel();
        p.setBackground(Theme.BG_DARK);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(32,36,32,36));
        return p;
    }
    private JLabel pageTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(Font.BOLD,26)); l.setForeground(Theme.TEXT_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JLabel pageSub(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(Font.PLAIN,14)); l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(Font.BOLD,16)); l.setForeground(Theme.TEXT_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(Font.BOLD,11)); l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); return l;
    }
    private StyledField field() {
        StyledField f = new StyledField(20);
        f.setMaximumSize(new Dimension(440,42)); f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }
    private void addFormRow(JPanel parent, String label, Component comp) {
        JLabel l = new JLabel(label);
        l.setFont(Theme.font(Font.BOLD,11)); l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(l); parent.add(Box.createVerticalStrut(4));
        if (comp instanceof JComponent) {
            ((JComponent)comp).setMaximumSize(new Dimension(440,42));
            ((JComponent)comp).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        parent.add(comp); parent.add(Box.createVerticalStrut(12));
    }
    private void clearFields(StyledField... fields) { for (StyledField f : fields) f.setText(""); }
    private void toast(String msg, Color color) {
        SwingUtilities.invokeLater(() -> new Toast(this, msg, color));
    }
    private void navigate(String page) {
        rebuildPage(page);
        cardLayout.show(contentArea, page);
        for (NavButton nb : navButtons) nb.setActive(nb.getPageName().equals(page));
    }
}
