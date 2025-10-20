package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import app.ConnectDB.ConnectDB;
import app.DAO.TaiKhoanDAO;
import app.Entity.TaiKhoan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.Flow;

public class DangNhapFrame extends JFrame {
    
    private JTextField txtMaNhanVien;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JLabel lblTieuDe;
    private JLabel lblMaNhanVien;
    private JLabel lblMatKhau;
    private JLabel lblHinhAnh;
	private TaiKhoanDAO taiKhoanDAO;
	
    public DangNhapFrame() {
		taiKhoanDAO = new TaiKhoanDAO();
		
        setTitle("Đăng Nhập - Pill & Chill");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel pnlMain = new JPanel(new GridLayout(1, 2));
        
        JPanel pnlLeft = taoTrangTrai();
        JPanel pnlRight = taoTrangPhai();
        
        pnlMain.add(pnlLeft);
        pnlMain.add(pnlRight);
        
        add(pnlMain);
        setVisible(true);
    }
    
    private JPanel taoTrangTrai() {
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(new Color(240, 245, 240));
        pnlLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        
        ImageIcon logo = new ImageIcon(getClass().getResource("/resources/image/logo.png"));
        Image img = logo.getImage();
        // Anti aliasing khử răng cưa 
        Image scaledImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        
        ImageIcon scaleLogo = new ImageIcon(scaledImg);
        
        lblHinhAnh = new JLabel(scaleLogo, SwingConstants.CENTER);
        lblHinhAnh.setForeground(Color.WHITE);
        lblHinhAnh.setBackground(Color.WHITE);
        lblHinhAnh.setPreferredSize(new Dimension(500, 500));
        
        pnlLeft.add(lblHinhAnh);
        
        return pnlLeft;
    }
    
    private JPanel taoTrangPhai() {
    	// Dung gridbag, all components đc auto center align
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(new Color(240, 250, 240));
        pnlRight.setBorder(new EmptyBorder(50, 60, 50, 60));
        
        lblTieuDe =  new JLabel("ĐĂNG NHẬP");
        lblTieuDe.setFont(new Font("Roboto", Font.BOLD, 30));
        lblTieuDe.setForeground((new Color(45, 152, 42)));
        
        lblMaNhanVien = new JLabel("Mã nhân viên:");
        lblMaNhanVien.setFont(new Font("Roboto", Font.BOLD, 16));
        txtMaNhanVien = taoTextField();
        
        lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Roboto", Font.BOLD, 16));
        lblMatKhau.setPreferredSize(lblMaNhanVien.getPreferredSize());
        txtMatKhau = taoPasswordField();
        btnDangNhap = taoNutDangNhap();
        
        JPanel pnlMainPhai = new JPanel();
        pnlMainPhai.setBackground(new Color(240,250,240));
        pnlMainPhai.setLayout(new GridLayout(4, 1, 5, 10));
        
        // 1 la center, 0 la left, 2 la right
        JPanel r1 = rowPanel(1);
        r1.add(lblTieuDe);
        pnlMainPhai.add(r1);
        
        JPanel r2 = rowPanel(0);
        r2.add(lblMaNhanVien);
        r2.add(txtMaNhanVien);
        pnlMainPhai.add(r2);
        
        JPanel r3 = rowPanel(0);
        r3.add(lblMatKhau);
        r3.add(txtMatKhau);
        pnlMainPhai.add(r3);
        
        JPanel r4 = rowPanel(1);
        r4.add(btnDangNhap);
        pnlMainPhai.add(r4);
        handleHotKey();
        pnlRight.add(pnlMainPhai, new GridBagConstraints());

        return pnlRight;
    }
    
    private JTextField taoTextField() {
        txtMaNhanVien = new JTextField();
        txtMaNhanVien.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMaNhanVien.setBackground(new Color(240,250,240));
        txtMaNhanVien.setPreferredSize(new Dimension(300, 40));
        txtMaNhanVien.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtMaNhanVien.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtMatKhau.requestFocus();
			}
		});
        return txtMaNhanVien;
    }
    
    private JPasswordField taoPasswordField() {
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMatKhau.setBackground(new Color(240,250,240));
        txtMatKhau.setPreferredSize(new Dimension(300, 40));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        return txtMatKhau;
    }
    
    private JButton taoNutDangNhap() {
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 15));
        btnDangNhap.setPreferredSize(new Dimension(300, 45));
        btnDangNhap.setBackground(new Color(45, 152, 42));

        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setOpaque(true);
        btnDangNhap.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Shape shape = new RoundRectangle2D.Float(0, 0, btnDangNhap.getWidth(), btnDangNhap.getHeight(), 30, 30);
                btnDangNhap.setMixingCutoutShape(shape);
        }});
        btnDangNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDangNhap.setBackground(new Color(0, 155, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDangNhap.setBackground(new Color(34, 139, 34));
            }
        });
        btnDangNhap.addActionListener( e-> {
        	validateLogin();
        });
        
        return btnDangNhap;
    }
    
    private JPanel rowPanel(int layout) {
    	JPanel a = new JPanel(new FlowLayout(layout));
    	a.setBackground(new Color(240,250,240));
    	a.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    	return a;
    }
    
    private void handleHotKey() {
    	JRootPane rootPane = getRootPane();
    	rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    			.put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
    	rootPane.getActionMap().put("enterPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validateLogin();
				
			}
		});
    }
    
	private void validateLogin() {
		ConnectDB.getInstance().connect();
		String user = txtMaNhanVien.getText().trim();
		String pwd = new String(txtMatKhau.getPassword());

		if (user.isEmpty() || pwd.isEmpty()) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", false);
			a.show();
			return;
		}
		TaiKhoan taiKhoan = taiKhoanDAO.kiemTraDangNhap(user, pwd);
		if (taiKhoan != null) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Đăng nhập thành công! Xin chào "+ taiKhoan.getMaNV(), false);
			a.show();
			this.dispose(); 
			new MainFrame(taiKhoan.getMaNV());
		} else {
			CustomJOptionPane a = new CustomJOptionPane(this,  "Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản của bạn đã bị khóa.", false);
			a.show();
			txtMatKhau.setText("");
			txtMaNhanVien.requestFocus();
		}
	}
}