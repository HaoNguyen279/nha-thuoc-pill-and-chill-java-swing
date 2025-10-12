package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class LapPhieuDoiThuoc extends JFrame implements ActionListener{

	private JSpinner soLuongThem;
	private JButton btnThem;
	private JButton btnXacNhan;
	private JSpinner soLuongGiam;
	private JButton btnXoa;
	private DefaultListModel danhSachModel;
	private JList danhSachDoiTra;
	private DefaultListModel thuocModel;
	private JList danhSachThuoc;

	public LapPhieuDoiThuoc() {
		createLapPhieuDoiThuoc();
	}

	private void createLapPhieuDoiThuoc() {
	    JMenuBar menuBar = new JMenuBar();

	    JLabel ten = new JLabel("Ten nv: Huynh Gia Man");
	    
	    menuBar.setPreferredSize(new Dimension(50,40));
	     
	    // Tạo menu chính
	    JMenu menuHeThong = new JMenu("Hệ thống");
	    JMenu menuDanhMuc = new JMenu("Danh mục");
	    JMenu menuCapNhat = new JMenu("Cập nhật");
	    JMenu menuTimKiem = new JMenu("Tìm kiếm");
	    JMenu menuXuLi = new JMenu("Xử lý");
	    JMenu menuThongKe = new JMenu("Thống kê");
	    
	  

	    menuHeThong.setHorizontalAlignment(SwingConstants.CENTER);
	    menuCapNhat.setHorizontalAlignment(SwingConstants.CENTER);
	    menuDanhMuc.setHorizontalAlignment(SwingConstants.CENTER);
	    menuThongKe.setHorizontalAlignment(SwingConstants.CENTER);
	    menuTimKiem.setHorizontalAlignment(SwingConstants.CENTER);
	    menuXuLi.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    
	    
	    JMenuItem openItem = new JMenuItem("Open");
	    JMenuItem exitItem = new JMenuItem("Exit");
	    JMenuItem testItem1 = new JMenuItem("Test menu");
	    JMenuItem testItem2 = new JMenuItem("Test menu");
	    JMenuItem testItem3 = new JMenuItem("Test menu");
	    JMenuItem testItem4 = new JMenuItem("Test menu");
	    
	    
	    openItem.setPreferredSize(new Dimension(60,20));
	    exitItem.setPreferredSize(new Dimension(60,20));
	    testItem1.setPreferredSize(new Dimension(60,20));
	    testItem2.setPreferredSize(new Dimension(60,20));
	    testItem3.setPreferredSize(new Dimension(60,20));
	    testItem4.setPreferredSize(new Dimension(60,20));
	    
	    testItem1.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Demo menu chức năng"));
	    testItem2.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Demo menu chức năng"));
	    testItem3.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Demo menu chức năng"));
	    testItem4.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Demo menu chức năng"));
	    
	    
	    exitItem.addActionListener(e -> System.exit(0));

	    menuHeThong.add(openItem);
	    menuHeThong.add(exitItem);
	    menuCapNhat.add(testItem1);
	    menuThongKe.add(testItem2);
	    menuTimKiem.add(testItem3);
	    menuXuLi.add(testItem4);
	    
	    JMenuItem aboutItem = new JMenuItem("About");
	    aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), "Demo menu chức năng"));
	    menuDanhMuc.add(aboutItem);
	    menuXuLi.add(openItem);
	    
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
	    
        
	    menuBar.add(menuHeThong);
	    menuBar.add(menuDanhMuc);
	    menuBar.add(menuCapNhat);
	    menuBar.add(menuTimKiem);
	    menuBar.add(menuXuLi);
	    menuBar.add(menuThongKe);
	    menuBar.add(Box.createHorizontalGlue());
	    menuBar.add(ten);
	    menuBar.add(Box.createHorizontalStrut(10));
	   
	    setTitle("LẬP PHIẾU ĐỔI TRẢ THUỐC");
	    setJMenuBar(menuBar);
	    setSize(1028, 720);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setVisible(true);
	    setLocationRelativeTo(null);
	    
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








