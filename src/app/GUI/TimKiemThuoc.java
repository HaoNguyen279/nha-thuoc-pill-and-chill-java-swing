package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.ConnectDB.ConnectDB;
import app.DAO.ThuocDAO;
import app.Entity.Thuoc;

import java.awt.*;
import java.util.ArrayList;
public class TimKiemThuoc extends JFrame{
	    private DefaultTableModel dtmTable;
	    private ArrayList<Thuoc> dsThuoc;


		public TimKiemThuoc() {
	        setTitle("Tìm kiếm thuốc");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(1200, 800);
	        setLocationRelativeTo(null);
	        ConnectDB.getInstance().connect();
	        
	        JPanel mainPanel = new JPanel(new BorderLayout());
	        mainPanel.setBackground(Color.WHITE);
	        
	    
	        
	        
	        JPanel centerPanel = taoCenterPanel();
	        centerPanel.setPreferredSize(new Dimension(1000,1000));
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
	        JLabel titleLabel = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
	        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	        titlePanel.add(titleLabel, BorderLayout.CENTER);
	        
	        
	        JPanel searchPanel = taoSearchPanel();
	        titlePanel.add(searchPanel, BorderLayout.SOUTH);
	        
	        centerPanel.add(titlePanel, BorderLayout.NORTH);
	        
	        
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        // Bảng dữ liệu
	        String[] cols = {"Mã thuốc" , "Mã lô" , "Tên thuốc", "Số lượng tồn", "Giá bán","Đơn vị","Số lượng tối thiểu","Mã nsx"};
	        dtmTable = new DefaultTableModel(cols,0);
	        loadData_Thuoc();
	        
	        JTable table = new JTable(dtmTable);
	        
	        table.setBackground(new Color(240, 240, 245));
	        table.setGridColor(Color.LIGHT_GRAY);
	        table.setFont(new Font("Arial", Font.PLAIN, 12));
	        table.setRowHeight(50);
	        JScrollPane scrollPane = new JScrollPane(table);
//	        scrollPane.setPreferredSize(new Dimension(0, 300));
	        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	        
	        
	        
	        panel.add(scrollPane, BorderLayout.CENTER);
	        
	        JLabel tongSoThuoc = new JLabel("Tổng số bản ghi:"+Integer.toString(dsThuoc.size()));
	        
	        centerPanel.add(panel, BorderLayout.CENTER);
	        centerPanel.add(tongSoThuoc,BorderLayout.SOUTH);
	        return centerPanel;
	    }
	    
	    private JPanel taoSearchPanel() {
	        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
	        searchPanel.setBackground(Color.WHITE);
	        
	        JTextField searchField = new JTextField(25);
	        searchField.setText("");
	        searchField.setForeground(Color.GRAY);
	        searchField.setFont(new Font("Arial", Font.ITALIC, 13));
	        searchField.setBackground(new Color(245, 245, 245));
	        searchField.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        
	        JButton searchButton = new JButton("Tìm");
	        searchButton.setFont(new Font("Arial", Font.PLAIN, 13));
	        searchButton.setPreferredSize(new Dimension(80, 30));
	        searchButton.setBackground(Color.WHITE);
	        searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        searchButton.setFocusPainted(false);
	        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        
	        searchPanel.add(searchField);
	        searchPanel.add(searchButton);
	        
	        return searchPanel;
	    }
	    
	    private JPanel taoTablePanel(String title, String buttonText) {
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);
	        
	        
	        
	        
	        // Bảng dữ liệu
	        String[] cols = {"Mã thuốc" , "Mã lô" , "Tên thuốc", "Số lượng tồn", "Giá bán","Đơn vị","Số lượng tối thiểu","Mã nsx"};
	        dtmTable = new DefaultTableModel(cols,0);
	        loadData_Thuoc();
	        
	        JTable table = new JTable(dtmTable);
	        
	        table.setBackground(new Color(240, 240, 245));
	        table.setGridColor(Color.LIGHT_GRAY);
	        table.setFont(new Font("Arial", Font.PLAIN, 12));
	        
	        JScrollPane scrollPane = new JScrollPane(table);
//	        scrollPane.setPreferredSize(new Dimension(0, 300));
	        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	        
	        
	        
	        panel.add(scrollPane, BorderLayout.CENTER);

	        
	        return panel;
	    }
	    
	    public void loadData_Thuoc() {
	    	dtmTable.setRowCount(0);
	    	ThuocDAO thuocdao = new ThuocDAO();
	    	dsThuoc = thuocdao.getListThuoc();
	    	for(Thuoc thuoc : dsThuoc) {
	    		Object[] rowData =  {
	    				thuoc.getMaThuoc(),
	    				thuoc.getMaLo(),
	    				thuoc.getTenThuoc(),
	    				thuoc.getSoLuongTon() == 0 ?"Hết hàng":thuoc.getSoLuongTon(),
	    				thuoc.getGiaBan(),
	    				thuoc.getDonVi(),
	    				thuoc.getSoLuongToiThieu(),
	    				thuoc.getMaNSX()
	    		};
	    		dtmTable.addRow(rowData);
	    	}
	    }
	    
	    
	    
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new TimKiemThuoc());
	    }
	}


