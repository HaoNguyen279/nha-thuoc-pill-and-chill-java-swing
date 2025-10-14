package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// --- CÁC IMPORT CẦN THIẾT ---
import app.ConnectDB.ConnectDB;
import app.DAO.TaiKhoanDAO;
import app.Entity.TaiKhoan;

public class DangNhap extends JFrame implements ActionListener {
	private JLabel lblUsername, lblPassword, lblTitleLogin;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JFrame loginFrame;
	
	// --- THÊM KHAI BÁO CHO DAO ---
	private TaiKhoanDAO taiKhoanDAO;

	public DangNhap() {
		// --- KHỞI TẠO ĐỐI TƯỢNG DAO ---
		taiKhoanDAO = new TaiKhoanDAO();
		
		createLoginWindow();
	}

	private void createLoginWindow() {
		loginFrame = new JFrame("Đăng nhập");
		Font fntLogin = new Font("Roboto", Font.BOLD, 30);
		Font fntUserAndPassword = new Font("Roboto", Font.PLAIN, 18);
		lblUsername = new JLabel("Username");
		lblUsername.setFont(fntUserAndPassword);
		lblPassword = new JLabel("Password");
		lblPassword.setFont(fntUserAndPassword);
		lblTitleLogin = new JLabel("Login");
		lblTitleLogin.setFont(fntLogin);

		txtUsername = new JTextField(20);
		txtUsername.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		txtUsername.setFont(new Font("Roboto", Font.PLAIN, 16));
		txtPassword = new JPasswordField(20);
		txtPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		txtPassword.setFont(new Font("Roboto", Font.PLAIN, 16));

		btnLogin = new JButton("Đăng nhập");
		btnLogin.setBorder(BorderFactory.createEmptyBorder());
		btnLogin.setSize(90, 30);
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setFont(new Font("Roboto", Font.PLAIN, 18));
		btnLogin.setBackground(new Color(124, 241, 144));
		btnLogin.setFocusPainted(false);

		LeftSubPanel pnlLeftPanel = new LeftSubPanel();
		JPanel pnlRight = new JPanel(null);

		JPanel test = new JPanel();
		test.add(lblTitleLogin);
		test.setBounds(0, 60, 400, 100);

		JPanel test2 = new JPanel(new BorderLayout());
		test2.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		test2.add(lblUsername, BorderLayout.NORTH);
		test2.add(txtUsername);
		test2.setBounds(0, 160, 400, 60);

		JPanel test4 = new JPanel(new BorderLayout());
		test4.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		test4.add(lblPassword, BorderLayout.NORTH);
		test4.add(txtPassword);
		test4.setBounds(0, 240, 400, 60);

		JPanel test6 = new JPanel(new BorderLayout());
		test6.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
		test6.add(btnLogin);
		test6.setBounds(0, 340, 400, 60);

		pnlRight.add(test);
		pnlRight.add(test2);
		pnlRight.add(test4);
		pnlRight.add(test6);

		btnLogin.addActionListener(this);
		loginFrame.setLayout(new GridLayout(1, 2));
		loginFrame.add(pnlLeftPanel);
		loginFrame.add(pnlRight);
		loginFrame.setSize(800, 600);
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		loginFrame.getRootPane().setDefaultButton(btnLogin);
		loginFrame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				ConnectDB.getInstance().connect();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.", "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
				return; 
			}
			new DangNhap();
		});
	}

	class LeftSubPanel extends JPanel {
		public LeftSubPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			ImageIcon icon = new ImageIcon(getClass().getResource("/resources/image/hinh-anh-nha-thuoc.jpg"));
			JLabel background = new JLabel(icon);
			background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
			background.add(Box.createVerticalStrut(50));
			setBackground(new Color(248, 248, 248));
			add(background);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnLogin) {
			validateLogin();
		}
	}

	/**
	 * --- PHƯƠNG THỨC XÁC THỰC ĐĂNG NHẬP (ĐÃ CẬP NHẬT) ---
	 * Lấy thông tin từ GUI, gọi DAO để kiểm tra với CSDL.
	 */
	private void validateLogin() {
		String user = txtUsername.getText().trim();
		String pwd = new String(txtPassword.getPassword());

		// 1. Kiểm tra xem người dùng đã nhập liệu chưa
		if (user.isEmpty() || pwd.isEmpty()) {
			JOptionPane.showMessageDialog(loginFrame, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông Báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// 2. Gọi phương thức kiemTraDangNhap từ DAO
		TaiKhoan taiKhoan = taiKhoanDAO.kiemTraDangNhap(user, pwd);

		// 3. Xử lý kết quả trả về
		if (taiKhoan != null) {
			JOptionPane.showMessageDialog(loginFrame, "Đăng nhập thành công! Xin chào " + taiKhoan.getMaNV());
			loginFrame.dispose(); 
			new Menu(taiKhoan.getMaNV());
		} else {
			// Nếu taiKhoan là null -> Sai thông tin hoặc tài khoản bị khóa
			JOptionPane.showMessageDialog(loginFrame, "Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản của bạn đã bị khóa.", "Đăng Nhập Thất Bại", JOptionPane.ERROR_MESSAGE);
			txtPassword.setText(""); // Xóa trống ô mật khẩu
			txtUsername.requestFocus(); // Focus lại vào ô username
		}
	}
}