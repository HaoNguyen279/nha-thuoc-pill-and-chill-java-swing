package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.DAO.HoaDonDAO;
import app.DTO.HoaDonKemGiaDTO;
import app.Entity.HoaDon;

public class XemChiTietDoanhThuTheoThangFrame extends JFrame implements MouseListener{

    private final Color TABLE_HEADER_COLOR = new Color(144, 238, 144); 
    private final Color TABLE_HEADER_TEXT_COLOR = Color.BLACK;
//    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color SELECTION_COLOR = new Color(178, 223, 219);

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);JTable tblHoaDon;
    private DefaultTableModel dtm;
    private ArrayList<HoaDonKemGiaDTO> dsHoaDon;

    public XemChiTietDoanhThuTheoThangFrame(int thang, int nam) {
        setTitle("Danh Sách Hóa Đơn");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);

        // Tiêu đề frame
        JLabel lblTieuDe = new JLabel("DANH SÁCH HÓA ĐƠN THÁNG " + thang + " NĂM "+ nam, SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(new Color(0, 150, 136));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTieuDe, BorderLayout.NORTH);


        add(createTablePanel(), BorderLayout.CENTER);
        
        setVisible(true);
        loadDummyData(thang, nam);
    }
    public XemChiTietDoanhThuTheoThangFrame(int nam) {
        setTitle("Danh Sách Hóa Đơn");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);

        // Tiêu đề frame
        JLabel lblTieuDe = new JLabel("DANH SÁCH HÓA ĐƠN NĂM "+ nam, SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(new Color(0, 150, 136));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTieuDe, BorderLayout.NORTH);


        add(createTablePanel(), BorderLayout.CENTER);
        
        setVisible(true);
        loadDummyData(nam);
    }

       
    public JScrollPane createTablePanel() {
        String[] cols = {"Mã hóa đơn", "Tên NV", "Tên KH", "Ngày lập", "Ghi chú", "Text"};
        
        dtm = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        tblHoaDon = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblHoaDon.setRowHeight(35);
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblHoaDon.setFillsViewportHeight(true);
        tblHoaDon.setShowGrid(true);
        tblHoaDon.setGridColor(new Color(224, 224, 224));
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setSelectionBackground(new Color(178, 223, 219));
        tblHoaDon.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblHoaDon.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblHoaDon.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private void loadDummyData(int thang, int nam) {
    	HoaDonDAO hdDAO = new HoaDonDAO();
    	dsHoaDon = hdDAO.getHoaDonTrongThang(thang, nam);
//        Object[][] data = {
//            {"HD00048", "Ngô Phương Thảo", "Lưu Thị Ngọc", "22/09/2025", "Khách hàng mua thuốc điều trị đau khớp"},
//        };
        for (HoaDonKemGiaDTO hd : dsHoaDon) {
        	Object[] rowData = {
        			hd.getMaHoaDon(),
        			hd.getTenKH(),
        			hd.getTenKH(),
        			hd.getNgayBan(),
        			hd.getGhiChu(),
        			hd.getTongTien()
        	};
            dtm.addRow(rowData);
        }
    }
    
    private void loadDummyData(int nam) {
    	HoaDonDAO hdDAO = new HoaDonDAO();

    	dsHoaDon = hdDAO.getHoaDonTrongNam(nam);
    	
    	
        for (HoaDonKemGiaDTO hd : dsHoaDon) {
        	Object[] rowData = {
        			hd.getMaHoaDon(),
        			hd.getTenKH(),
        			hd.getTenKH(),
        			hd.getNgayBan(),
        			hd.getGhiChu(),
        			hd.getTongTien()
        	};
            dtm.addRow(rowData);
        }
    }


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}