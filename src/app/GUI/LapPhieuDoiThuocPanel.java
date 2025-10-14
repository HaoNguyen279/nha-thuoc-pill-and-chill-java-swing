package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class LapPhieuDoiThuocPanel extends JPanel implements ActionListener{

	private JSpinner soLuongThem;
	private JButton btnThem;
	private JButton btnXacNhan;
	private JSpinner soLuongGiam;
	private JButton btnXoa;
	private DefaultListModel<String> danhSachModel;
	private JList<String> danhSachDoiTra;
	private DefaultListModel<String> thuocModel;
	private JList<String> danhSachThuoc;

	public LapPhieuDoiThuocPanel() {
		createLapPhieuDoiThuoc();
	}

	private void createLapPhieuDoiThuoc() {
	    setLayout(new BorderLayout());
	    
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    //Panel Lập Phiếu Đổi Trả
	    
	    JLabel lblLapPhieuDoiTra = new JLabel("LẬP PHIẾU ĐỔI TRẢ THUỐC", JLabel.CENTER);
	    lblLapPhieuDoiTra.setFont(new Font("Arial", Font.BOLD, 16));
	    mainPanel.add(lblLapPhieuDoiTra, BorderLayout.NORTH);
	    
	    JPanel pnlLapPhieuDoiTra = new JPanel(new BorderLayout(5, 5));
	    pnlLapPhieuDoiTra.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    mainPanel.add(pnlLapPhieuDoiTra);
	    
	    thuocModel = new DefaultListModel<>();
	    danhSachThuoc = new JList<>(thuocModel);
	    JScrollPane scrollThuoc = new JScrollPane(danhSachThuoc);
	    scrollThuoc.setPreferredSize(new Dimension(500, 150));
	    pnlLapPhieuDoiTra.add(scrollThuoc);
	    
	    
	    JPanel pnlTopInput = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pnlTopInput.add(new JLabel("Số lượng: "));
	    soLuongThem = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
	    pnlTopInput.add(soLuongThem);
	    
	    btnThem = new JButton("Thêm vào danh sách đổi trả");
	    pnlTopInput.add(btnThem);
	    pnlLapPhieuDoiTra.add(pnlTopInput, BorderLayout.SOUTH);
	    
	    
	    //Panel Danh Sách Đổi Trả
	    
	    JLabel lblDanhSachDoiTra = new JLabel("DANH SÁCH THUỐC ĐỔI TRẢ", JLabel.CENTER);
	    lblDanhSachDoiTra.setFont(new Font("Arial", Font.BOLD, 16));
	    mainPanel.add(lblDanhSachDoiTra, BorderLayout.NORTH);
	
	    JPanel pnlDanhSachDoiTra = new JPanel(new BorderLayout(5, 5));
	    pnlDanhSachDoiTra.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    mainPanel.add(pnlDanhSachDoiTra);
	    
	    danhSachModel = new DefaultListModel<>();
	    danhSachDoiTra = new JList<>(danhSachModel);
	    JScrollPane scrollDanhSach = new JScrollPane(danhSachDoiTra);
	    scrollDanhSach.setPreferredSize(new Dimension(500, 150));
	    pnlDanhSachDoiTra.add(scrollDanhSach);
	    
	    JPanel pnlBotInput = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pnlBotInput.add(new JLabel("Số lượng: "));
	    soLuongGiam = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
	    pnlBotInput.add(soLuongGiam);
	    
	    btnXoa = new JButton("Xóa khỏi danh sách đổi trả");
	    pnlBotInput.add(btnXoa);
	    pnlDanhSachDoiTra.add(pnlBotInput, BorderLayout.SOUTH);

	    
	    //Panel Xác Nhận
	    JPanel pnlXacNhan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    btnXacNhan = new JButton("Xác nhận");
	    pnlXacNhan.add(btnXacNhan);
	    mainPanel.add(pnlXacNhan);
	    
	    
	    add(mainPanel, BorderLayout.CENTER);
	    
	    btnThem.addActionListener(this);
	    btnXoa.addActionListener(this);
	    btnXacNhan.addActionListener(this);
	    
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnThem) {
			JOptionPane.showMessageDialog(this, "Chưa có làm ạ!");
			return;
		} else if (o == btnXoa) {
			JOptionPane.showMessageDialog(this, "Chưa có làm ạ!");
			return;
		} else if (o == btnXacNhan) {
			JOptionPane.showMessageDialog(this, "Chưa có làm ạ!");
			return;
		}
		
	}
	
}








