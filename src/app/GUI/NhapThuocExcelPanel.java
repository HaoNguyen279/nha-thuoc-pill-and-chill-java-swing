package app.GUI;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.ConnectDB.ConnectDB;
import app.DAO.ChiTietPhieuNhapDAO;
import app.DAO.PhieuNhapThuocDAO;
import app.DAO.ThuocDAO;
import app.Entity.ChiTietLoThuoc;
import app.Entity.ChiTietPhieuNhap;
import app.Entity.PhieuNhapThuoc;
import app.Entity.Thuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
public class NhapThuocExcelPanel extends JPanel implements ActionListener{ 
	    private DefaultTableModel dtmTable;
	    private ArrayList<Thuoc> dsThuoc;
	    private ArrayList<ChiTietLoThuoc> dsCTLT;
		private JButton btnTim;
		private JTextField txtTim;
		private String tieuChi = "Mã thuốc";
		private JComboBox<String> cboTieuChi;
		private JLabel tongSoThuoc = new JLabel("");
		private JButton btnXoaThuoc;
		private JTable table;
		private JButton btnNhapThuoc;

		
		public NhapThuocExcelPanel(ArrayList<Thuoc> dsThuoc1,ArrayList<ChiTietLoThuoc> dsCTLT1) {
	       
	       
	        
	        ConnectDB.connect();
	        
	        dsThuoc =  dsThuoc1;
	        dsCTLT = dsCTLT1;
	        JPanel mainPanel = new JPanel(new BorderLayout());
	        mainPanel.setBackground(Color.WHITE);
	        JPanel centerPanel = taoCenterPanel();
	        centerPanel.setPreferredSize(new Dimension(1400,700));
	        mainPanel.add(centerPanel, BorderLayout.CENTER);
	        add(mainPanel);
	        
	        setVisible(true);
	    }
	    
	   
	    
	    private JPanel taoCenterPanel() {
	        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
	        centerPanel.setBackground(Color.WHITE);
	        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
	        
	        
	        JPanel titlePanel = new JPanel(new BorderLayout());
	        titlePanel.setBackground(Color.WHITE);
	        JLabel titleLabel = new JLabel("NHẬP THUỐC", SwingConstants.CENTER);
	        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	        titlePanel.add(titleLabel, BorderLayout.CENTER);
	        
	        
	        JPanel searchPanel = taoSearchPanel();
	        titlePanel.add(searchPanel, BorderLayout.SOUTH);
	        
	        centerPanel.add(titlePanel, BorderLayout.NORTH);
	        
	        
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        // Bảng dữ liệu
	        String[] cols = {"Mã thuốc" , "Mã lô" , "Tên thuốc", "Số lượng", "Giá nhập","Đơn vị","Số lượng tối thiểu","Mã nsx","Ngày sản xuất","Hạn sử dụng"};
	        
	        dtmTable = new DefaultTableModel(cols, 0) {

	            @Override
	            public boolean isCellEditable(int row, int column) {
	               //all cells false
	               return false;
	            }
	        };
	    	loadData_Thuoc(dsThuoc);
	        
	        table = new JTable(dtmTable);
	        
	        table.setBackground(new Color(240, 240, 245));
	        table.setGridColor(Color.LIGHT_GRAY);
	        table.setFont(new Font("Arial", Font.PLAIN, 12));
	        table.setRowHeight(50);
	        JScrollPane scrollPane = new JScrollPane(table);
//	        scrollPane.setPreferredSize(new Dimension(0, 300));
	        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	        
	        
	        
	        panel.add(scrollPane, BorderLayout.CENTER);
	        
	        tongSoThuoc = new JLabel("Tổng số bản ghi:"+Integer.toString(dsThuoc.size()));
	        
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
	        
	        String[] tieuChiTim = {"Mã thuốc","Mã lô","Tên thuốc"};
	        cboTieuChi = new JComboBox<String>(tieuChiTim);
	        
	        cboTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        cboTieuChi.setBackground(Color.WHITE);
	        cboTieuChi.setForeground(Color.BLACK);
	        cboTieuChi.setFocusable(false);
	        cboTieuChi.setBorder(BorderFactory.createLineBorder(Color.gray));
	        
	        cboTieuChi.addActionListener(this);
	        
	        
	        btnXoaThuoc = new JButton("Xóa thuốc");
	        btnXoaThuoc.setFont(new Font("Arial", Font.PLAIN, 13));
	        btnXoaThuoc.setPreferredSize(new Dimension(80, 30));
	        btnXoaThuoc.setBackground(Color.WHITE);
	        btnXoaThuoc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        btnXoaThuoc.setFocusPainted(false);
	        btnXoaThuoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        btnXoaThuoc.addActionListener(this);
	        
	        
	        btnNhapThuoc = new JButton("Lưu vào CSDL");
	        btnNhapThuoc.setFont(new Font("Arial", Font.PLAIN, 13));
	        btnNhapThuoc.setPreferredSize(new Dimension(100, 30));
	        btnNhapThuoc.setBackground(Color.WHITE);
	        btnNhapThuoc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        btnNhapThuoc.setFocusPainted(false);
	        btnNhapThuoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        btnNhapThuoc.addActionListener(this);
	        
	        searchPanel.add(txtTim);
	        searchPanel.add(btnTim);
	        searchPanel.add(cboTieuChi);
	        searchPanel.add(btnXoaThuoc);
	        searchPanel.add(btnNhapThuoc);
	        return searchPanel;
	    }
	   
	    
	    public void loadData_Thuoc(ArrayList<Thuoc> dsThuoc) {
	    	dtmTable.setRowCount(0);
//	    	for(Thuoc thuoc : dsThuoc) {
//	    		Object[] rowData =  {
//	    				thuoc.getMaThuoc(),
//	    				thuoc.getMaLo(),
//	    				thuoc.getTenThuoc(),
//	    				thuoc.getSoLuongTon() == 0 ?"Hết hàng":thuoc.getSoLuongTon(),
//	    				thuoc.getGiaBan(),
//	    				thuoc.getDonVi(),
//	    				thuoc.getSoLuongToiThieu(),
//	    				thuoc.getMaNSX()
//	    		};
//	    		dtmTable.addRow(rowData);
//	    		tongSoThuoc.setText("Tổng số bản ghi:"+Integer.toString(dsThuoc.size()));
//	    	}
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	for(int i = 0 ; i < dsThuoc.size();i++) {
	    		Object[] rowData =  {
	    				dsThuoc.get(i).getMaThuoc(),
	    				dsThuoc.get(i).getMaLo(),
	    				dsThuoc.get(i).getTenThuoc(),
	    				dsThuoc.get(i).getSoLuongTon() == 0 ?"Hết hàng":dsThuoc.get(i).getSoLuongTon(),
	    				dsThuoc.get(i).getGiaBan(),
	    				dsThuoc.get(i).getDonVi(),
	    				dsThuoc.get(i).getSoLuongToiThieu(),
	    				dsThuoc.get(i).getMaNSX(),
	    				sdf.format(dsCTLT.get(i).getNgaySanXuat()),
	    				sdf.format(dsCTLT.get(i).getHanSuDung()),
	    		};
	    		dtmTable.addRow(rowData);
	    		tongSoThuoc.setText("Tổng số bản ghi:"+Integer.toString(dsThuoc.size()));
	    	}
	    }
	    
	   
	    
	    
	    
	    



		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if(o==btnTim) {
				ArrayList<Thuoc> ketQuaTim = new ArrayList<Thuoc>();
				String timString = txtTim.getText().toLowerCase().trim();
				if(timString.isBlank()) {
					 loadData_Thuoc(dsThuoc);
				}
				else {
					if(tieuChi.equals("Mã thuốc")) {
						for(Thuoc thuoc: dsThuoc) {
							if(thuoc.getMaThuoc().toLowerCase().matches("^"+timString+".*")) {
								ketQuaTim.add(thuoc);
							}
						}
						loadData_Thuoc(ketQuaTim);
					}
					else if(tieuChi.equals("Mã lô")) {
						for(Thuoc thuoc: dsThuoc) {
							if(thuoc.getMaLo().toLowerCase().matches("^"+timString+".*")) {
								ketQuaTim.add(thuoc);
							}
						}
						loadData_Thuoc(ketQuaTim);
					}
					else {
						for(Thuoc thuoc: dsThuoc) {
							if(thuoc.getTenThuoc().toLowerCase().matches("^"+timString+".*")) {
								ketQuaTim.add(thuoc);
							}
						}
						loadData_Thuoc(ketQuaTim);
					}
					
				}
				if(ketQuaTim.isEmpty()) {
					JOptionPane.showMessageDialog(this,
		                    "Không tìm thấy!!",
		                    "Lỗi", JOptionPane.ERROR_MESSAGE);
					loadData_Thuoc(dsThuoc);
					txtTim.setText("");
					txtTim.requestFocus();
				}


			}
			else if(o == cboTieuChi) {
				tieuChi = cboTieuChi.getSelectedItem().toString();

			}
			else if(o == btnXoaThuoc) {
				int row = table.getSelectedRow();

				dtmTable.removeRow(row);
				dsThuoc.remove(row);
				dsCTLT.remove(row);
				tongSoThuoc.setText("Tổng số bản ghi:"+Integer.toString(dsThuoc.size()));

			}
			else if (o == btnNhapThuoc) {
				int result = JOptionPane.showConfirmDialog(
                        this,
                        "Xác nhận nhập đơn hàng!!",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );
                if(result == JOptionPane.YES_OPTION) {
                	if(nhapThuoc()&&themPhieuNhap()) {
    					JOptionPane.showMessageDialog(this,
    		                    "Nhập thuốc vào CSDL thành công!!",
    		                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
    					dtmTable.setRowCount(0);
//    					SwingUtilities.dispose();
    				}
                	else {
                		JOptionPane.showMessageDialog(this,
    		                    "Hãy kiểm tra lại thông tin đơn hàng",
    		                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                	}
                }
                else {
                    JOptionPane.showMessageDialog(this,
                            "Đã Hủy!",
                            "Hủy", JOptionPane.INFORMATION_MESSAGE);
                }
				
			}
		}



		private boolean nhapThuoc() {
			ThuocDAO daoThuoc = new ThuocDAO();
			return daoThuoc.addDsThuoc(dsThuoc,dsCTLT);
		}
		
		private boolean themPhieuNhap() {
			PhieuNhapThuocDAO phieuDAO = new PhieuNhapThuocDAO();
			ChiTietPhieuNhapDAO ctpnDao = new ChiTietPhieuNhapDAO();
			PhieuNhapThuoc phieu = new PhieuNhapThuoc();
			phieu.setMaPhieuNhapThuoc(phieuDAO.taoMaTuDong());
			phieu.setMaNV("NV001");
			phieu.setNgayNhap(new Date());
			phieu.setIsActive(true);
			phieuDAO.addPhieuNhapThuoc(phieu);
			
			for (int i = 0; i < dsThuoc.size(); i++) {
			    ChiTietLoThuoc ctlt = dsCTLT.get(i);		    
			    ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
			    ctpn.setMaPhieuNhapThuoc(phieu.getMaPhieuNhapThuoc());
			    ctpn.setMaLo(ctlt.getMaLo());
			    ctpn.setSoLuong(ctlt.getSoLuong());
			    ctpn.setDonGia((float)ctlt.getGiaNhap());
			    ctpn.setIsActive(true);
			    ctpnDao.addChiTietPhieuNhap(ctpn);
			}
			return true;
		}
	}



