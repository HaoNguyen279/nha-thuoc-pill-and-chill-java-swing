package app.GUI;

import javax.swing.*;
import javax.swing.border.*;

import app.DAO.TaiKhoanDAO;
import app.Entity.NhanVien;
import app.Entity.TaiKhoan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaiKhoanPanel extends JPanel implements ActionListener{
	private CardLayout cardLayout;
	private JTextField txtMaNV;
    private JPasswordField txtMatKhauCu;
    private JPasswordField txtMatKhauMoi;
    private JButton btnDoiMatKhau;
    private NhanVien nhanVien;
	private TaiKhoanDAO tkDao  = new TaiKhoanDAO();;

	public TaiKhoanPanel(NhanVien nhanVien) {
		cardLayout = new CardLayout();
        setLayout(cardLayout);
        this.nhanVien = nhanVien;

        JPanel trangChu = taoTrangChu();
      
        add(trangChu, "TrangChu");

        cardLayout.show(this, "TrangChu");
	}

	private JPanel taoTrangChu() {
		JPanel pnlTrangChu = new JPanel(new GridBagLayout());
        pnlTrangChu.setBackground(new Color(245, 247, 250));
        
        // Panel chính chứa form - TĂNG KÍCH THƯỚC
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 223, 228), 1, true),
            BorderFactory.createEmptyBorder(50, 60, 50, 60) // Tăng padding
        ));
        
        // TĂNG KÍCH THƯỚC FORM
        mainPanel.setPreferredSize(new Dimension(1200, 700));
        mainPanel.setMaximumSize(new Dimension(1200, 700));

        // Icon và Tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        
        headerPanel.add(Box.createVerticalStrut(15));
        
        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        lblTitle.setForeground(new Color(32, 33, 36));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        
        headerPanel.add(Box.createVerticalStrut(8));
        
        JLabel lblSubtitle = new JLabel("Vui lòng nhập thông tin để đổi mật khẩu");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Tăng size
        lblSubtitle.setForeground(new Color(95, 99, 104));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblSubtitle);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(35));

        // Panel nhập thông tin với style đẹp hơn
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mã nhân viên
        formPanel.add(createInputField("Mã nhân viên", txtMaNV = createStyledTextField()));
        formPanel.add(Box.createVerticalStrut(20));

        // Mật khẩu cũ
        formPanel.add(createInputField("Mật khẩu cũ", txtMatKhauCu = createStyledPasswordField()));
        formPanel.add(Box.createVerticalStrut(20));

        // Mật khẩu mới
        formPanel.add(createInputField("Mật khẩu mới", txtMatKhauMoi = createStyledPasswordField()));

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(35));

        // Panel chứa nút - ĐẢM BẢO HIỂN THỊ ĐẦY ĐỦ
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(500, 50));
        
        btnDoiMatKhau = createStyledButton("Đổi mật khẩu", new Color(26, 115, 232), Color.WHITE);
        btnDoiMatKhau.setPreferredSize(new Dimension(160, 45)); // Tăng size nút
        btnDoiMatKhau.addActionListener(this);
       

        btnPanel.add(btnDoiMatKhau);
 

        mainPanel.add(btnPanel);
        
        // Thêm một khoảng trống nhỏ ở cuối để đảm bảo nút hiển thị
        mainPanel.add(Box.createVerticalStrut(10));
        
        pnlTrangChu.add(mainPanel);
        return pnlTrangChu;
    }
    
    // Tạo field với label và icon
    private JPanel createInputField(String labelText, JComponent inputField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(450, 80)); // Giới hạn width
        // Label
        JLabel label = new JLabel( labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Tăng size
        label.setForeground(new Color(60, 64, 67));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createVerticalStrut(10));
        
        // Input field
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(inputField);
        
        return panel;
    }
    
    // Tạo TextField với style đẹp - TĂNG SIZE
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textField.setPreferredSize(new Dimension(450, 45)); // Tăng size
        textField.setMaximumSize(new Dimension(450, 45));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15) // Tăng padding
        ));
        
        // Hiệu ứng focus
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(26, 115, 232), 2, true),
                    BorderFactory.createEmptyBorder(7, 14, 7, 14)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(218, 220, 224), 1, true),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        return textField;
    }
    
    // Tạo PasswordField với style đẹp - TĂNG SIZE
    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(450, 45)); // Tăng size
        passwordField.setMaximumSize(new Dimension(450, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15) // Tăng padding
        ));
        
        // Hiệu ứng focus
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(26, 115, 232), 2, true),
                    BorderFactory.createEmptyBorder(7, 14, 7, 14)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(218, 220, 224), 1, true),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        return passwordField;
    }
    
    // Tạo Button với style đẹp
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Tăng size
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // Tăng padding
        
        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (bgColor.equals(new Color(26, 115, 232))) {
                    button.setBackground(new Color(23, 103, 208));
                } else {
                    button.setBackground(new Color(232, 234, 237));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o==btnDoiMatKhau) {
			if(validateInput()) {
				String maNV = txtMaNV.getText();
				String mkMoi = txtMatKhauMoi.getText();
				TaiKhoan tk = new TaiKhoan(maNV, mkMoi, true);
				 
				if(tkDao.updateTaiKhoan(tk)) {
					JOptionPane.showMessageDialog(this, "Cập nhật mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					xoaTrang();
				}
				else {
					JOptionPane.showMessageDialog(this, "Cập nhật mật khẩu không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}	
		}
		
	}
	
	private void xoaTrang() {
		txtMaNV.setText("");
		txtMatKhauCu.setText("");
		txtMatKhauMoi.setText("");
		txtMaNV.requestFocus();
	}

	private boolean validateInput() {
	    // Kiểm tra các trường không được rỗng
	    if (txtMaNV.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMaNV.requestFocus();
	        return false;
	    }
	    if (txtMatKhauCu.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Mật khẩu cũ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMatKhauCu.requestFocus();
	        return false;
	    }
	    
	    if (txtMatKhauMoi.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Mật khẩu mới không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return false;
	    }
	    if(txtMatKhauCu.getText().trim().equals(txtMatKhauMoi.getText().trim())) {
	    	JOptionPane.showMessageDialog(this, "Mật khẩu cũ phải khác mật khẩu mới!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return false;
	    }
	    if (!(txtMaNV.getText().trim().equals(nhanVien.getMaNV()))) {
	        JOptionPane.showMessageDialog(this, "Mã nhân viên không đúng!! Phải nhập mã nhân viên của tài khoản hiện tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMaNV.requestFocus();
	        return false;
	    }
	    if(!(txtMatKhauCu.getText().equals(tkDao.getTaiKhoanById(txtMaNV.getText().trim()).getMatKhau()))) {
	    	JOptionPane.showMessageDialog(this, "Sai mật khẩu cũ!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        txtMaNV.requestFocus();
	        return false;
	    }

	    	    
	    
	    return true;
	}
}