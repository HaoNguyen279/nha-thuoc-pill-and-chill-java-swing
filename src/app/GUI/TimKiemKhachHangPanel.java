package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.ConnectDB.ConnectDB;
import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class TimKiemKhachHangPanel extends JPanel implements ActionListener{ 
	    private DefaultTableModel dtmTable;
	    private ArrayList<KhachHang> dsKhachHang;
		private JButton btnTim;
		private JTextField txtTim;
		private String tieuChi = "Số điện thoại";
		private JComboBox<String> cboTieuChi;
		public TimKiemKhachHangPanel(){
	    
	        
	        setPreferredSize(getPreferredSize());
	        ConnectDB.connect();
	        KhachHangDAO khachHangDao = new KhachHangDAO();
	    	dsKhachHang= khachHangDao.getAllKhachHang();
	        JPanel mainPanel = new JPanel(new BorderLayout());
	        mainPanel.setBackground(Color.WHITE);
	        
	    
	        
	        
	        JPanel centerPanel = taoCenterPanel();
	        centerPanel.setPreferredSize(new Dimension(1400,700));
	        mainPanel.add(centerPanel, BorderLayout.CENTER);
	        
	        
	        
	        add(mainPanel);
	        
	        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
	        .put(KeyStroke.getKeyStroke("ENTER"), "clickLogin");
	       getActionMap().put("clickLogin", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            btnTim.doClick(); // gọi hành động của nút
	        }
	    });
	        setVisible(true);
	    }
	    
	   
	    
	    private JPanel taoCenterPanel() {
	        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
	        centerPanel.setBackground(Color.WHITE);
	        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
	        
	        
	        JPanel titlePanel = new JPanel(new BorderLayout());
	        titlePanel.setBackground(Color.WHITE);
	        JLabel titleLabel = new JLabel("TÌM KIẾM KHÁCH HÀNG", SwingConstants.CENTER);
	        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	        titlePanel.add(titleLabel, BorderLayout.CENTER);
	        
	        
	        JPanel searchPanel = taoSearchPanel();
	        titlePanel.add(searchPanel, BorderLayout.SOUTH);
	        
	        centerPanel.add(titlePanel, BorderLayout.NORTH);
	        
	        
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        // Bảng dữ liệu
	        String[] cols = {"Mã khách hàng","Tên khách hàng","Số điện thoại","Điểm tích lũy"};
	        
	        dtmTable = new DefaultTableModel(cols, 0) {

	            @Override
	            public boolean isCellEditable(int row, int column) {
	               //all cells false
	               return false;
	            }
	        };
	    	load_dataKhachHang(dsKhachHang);
	        
	        JTable table = new JTable(dtmTable);
	        
	        table.setBackground(new Color(240, 240, 245));
	        table.setGridColor(Color.LIGHT_GRAY);
	        table.setFont(new Font("Arial", Font.PLAIN, 12));
	        table.setRowHeight(50);
	        JScrollPane scrollPane = new JScrollPane(table);
//	        scrollPane.setPreferredSize(new Dimension(0, 300));
	        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	        
	        
	        
	        panel.add(scrollPane, BorderLayout.CENTER);
	        
	        JLabel tongSoThuoc = new JLabel("Tổng số bản ghi:"+Integer.toString(dsKhachHang.size()));
	        
	        centerPanel.add(panel, BorderLayout.CENTER);
	        centerPanel.add(tongSoThuoc,BorderLayout.SOUTH);
	        return centerPanel;
	    }
	    
	    private JPanel taoSearchPanel() {
	        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
	        searchPanel.setBackground(Color.WHITE);
	        
	        txtTim = new JTextField(25);
	        txtTim.setText("");
	        txtTim.setForeground(Color.GRAY);
	        txtTim.setFont(new Font("Arial", Font.ITALIC, 13));
	        txtTim.setBackground(new Color(245, 245, 245));
	        txtTim.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        
	        btnTim = new JButton("Tìm");
	        btnTim.setFont(new Font("Arial", Font.PLAIN, 13));
	        btnTim.setPreferredSize(new Dimension(80, 30));
	        btnTim.setBackground(Color.WHITE);
	        btnTim.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        btnTim.setFocusPainted(false);
	        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        btnTim.addActionListener(this);
	        
	        String[] tieuChiTim = {"Số điện thoại","Mã khách hàng","Tên khách hàng"};
	        cboTieuChi = new JComboBox<String>(tieuChiTim);
	        
	        cboTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        cboTieuChi.setBackground(Color.WHITE);
	        cboTieuChi.setForeground(Color.BLACK);
	        cboTieuChi.setFocusable(false);
	        cboTieuChi.setBorder(BorderFactory.createLineBorder(Color.gray));
	        
	        cboTieuChi.addActionListener(this);
	        searchPanel.add(txtTim);
	        searchPanel.add(btnTim);
	        searchPanel.add(cboTieuChi);
	        return searchPanel;
	    }
	   
	    
	    public void load_dataKhachHang(ArrayList<KhachHang> dsKhachHang) {
	    	dtmTable.setRowCount(0);
	    	for(KhachHang khach : dsKhachHang) {
	    		Object[] rowData =  {
	    				khach.getMaKH(),
	    				khach.getTenKH(),
	    				khach.getSoDienThoai(),
	    				khach.getDiemTichLuy()
	    		};
	    		dtmTable.addRow(rowData);
	    	}
	    }
	    
	    
	    
	    
	  



		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if(o==btnTim) {
				ArrayList<KhachHang> ketQuaTim = new ArrayList<KhachHang>();
				String timString = txtTim.getText().toLowerCase().trim();
				if(timString.isBlank()) {
					 load_dataKhachHang(dsKhachHang);
				}
				else {
					if(tieuChi.equals("Mã khách hàng")) {
						for(KhachHang khachhang : dsKhachHang) {
							if(khachhang.getMaKH().toLowerCase().matches("^.*"+timString+".*")) {
								ketQuaTim.add(khachhang);
							}
						}
						load_dataKhachHang(ketQuaTim);
					}
					else if(tieuChi.equals("Tên nhân viên")) {
						for(KhachHang khachhang : dsKhachHang) {
							if(khachhang.getTenKH().toLowerCase().matches("^.*"+timString+".*")) {
								ketQuaTim.add(khachhang);
							}
						}
						load_dataKhachHang(ketQuaTim);
					}
					else {
						for(KhachHang khachhang : dsKhachHang) {
							if(khachhang.getSoDienThoai().toLowerCase().matches("^.*"+timString+".*")) {
								ketQuaTim.add(khachhang);
							}
						}
						load_dataKhachHang(ketQuaTim);
						
					}
					
				}
				if(ketQuaTim.isEmpty()) {
					JOptionPane.showMessageDialog(this,
		                    "Không tìm thấy!!",
		                    "Lỗi", JOptionPane.ERROR_MESSAGE);
					load_dataKhachHang(dsKhachHang);
					txtTim.setText("");
					txtTim.requestFocus();
				}


			}
			else if(o == cboTieuChi) {
				tieuChi = cboTieuChi.getSelectedItem().toString();

			}
		}
	}


