package app.GUI;

import java.awt.BorderLayout;
import java.awt. CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt. Dimension;
import java.awt. FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax. swing.JButton;
import javax.swing.JFrame;
import javax.swing. JLabel;
import javax.swing.JPanel;
import javax. swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing. SwingConstants;
import javax. swing.table.DefaultTableCellRenderer;
import javax.swing. table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app. DAO.HoaDonDAO;
import app.DTO. HoaDonKemGiaDTO;
import app.Entity. ThongKeThuoc;

public class XemChiTietThongKeThuoc extends JFrame implements MouseListener{

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);
    
    private JTable tblHoaDon;
    private DefaultTableModel dtm;
    private ArrayList<HoaDonKemGiaDTO> dsHoaDon;
    private ArrayList<ThongKeThuoc> dsThongKe;
    private DecimalFormat df = new DecimalFormat("#,###.##");
    
    private int ngay = 0;
    private int thang = 0;
    private int nam = 0;
    
    // CardLayout cho JFrame
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    public XemChiTietThongKeThuoc(int ngay, int thang, int nam, ArrayList<ThongKeThuoc> dsThongKe) {
        this.dsThongKe = dsThongKe;
        this.ngay = ngay;
        this.thang = thang;
        this. nam = nam;
        initFrame("DANH SÁCH THUỐC BÁN CHẠY " + ngay + " THÁNG " + thang + " NĂM " + nam);
    }
    
    public XemChiTietThongKeThuoc(int thang, int nam, ArrayList<ThongKeThuoc> dsThongKe) {
        this.dsThongKe = dsThongKe;
        this.thang = thang;
        this.nam = nam;
        initFrame("DANH SÁCH THUỐC BÁN CHẠY THÁNG " + thang + " NĂM " + nam);
    }
    
    public XemChiTietThongKeThuoc(int nam, ArrayList<ThongKeThuoc> dsThongKe) {
        this.dsThongKe = dsThongKe;
        this.nam = nam;
        initFrame("DANH SÁCH THUỐC BÁN CHẠY NĂM " + nam);
    }
    
    private void initFrame(String tieuDe) {
        FlatLightLaf.setup();
        
        setTitle("Danh Sách Thuốc");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);
        
        // Tạo CardLayout và mainContainer
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        // Panel danh sách (ban đầu)
        JPanel danhSachPanel = taoDanhSachPanel(tieuDe);
        mainContainer.add(danhSachPanel, "DanhSach");
        
        // Add mainContainer vào JFrame
        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);
        
        cardLayout.show(mainContainer, "DanhSach");
        loadDummyData();
        setVisible(true);
    }
    
    private JPanel taoDanhSachPanel(String tieuDe) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        
        // Tiêu đề
        JLabel lblTieuDe = new JLabel(tieuDe, SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        panel.add(lblTieuDe, BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        
        return panel;
    }
       
    public JScrollPane createTablePanel() {
        String[] cols = {"Mã thuốc", "Tên thuốc", "Số lượng bán", "Doanh thu"};
        
        dtm = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblHoaDon = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (! isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblHoaDon.setRowHeight(35);
        tblHoaDon.setFont(new Font("Segoe UI", Font. PLAIN, 14));
        tblHoaDon.setFillsViewportHeight(true);
        tblHoaDon.setShowGrid(true);
        tblHoaDon.setGridColor(new Color(224, 224, 224));
        tblHoaDon. setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setSelectionBackground(new Color(178, 223, 219));
        tblHoaDon.setSelectionForeground(Color. BLACK);
        tblHoaDon.addMouseListener(this);
        
        JTableHeader header = tblHoaDon.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả cells
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            tblHoaDon.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }
        
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }

    private void loadDummyData() {
        dtm.setRowCount(0);
        for (ThongKeThuoc tk : dsThongKe) {
            Object[] rowData = {
                tk.getMaThuoc(),
                tk.getTenThuoc(),
                tk.getSoLuongBan(),
                df.format(tk.getDoanhThu())
            };
            dtm.addRow(rowData);
        }
    }
    
    private void loadDummyData(int nam, String maNV) {
        HoaDonDAO hdDAO = new HoaDonDAO();
        dsHoaDon = hdDAO.getHoaDonTrongNamCuaNhanVien(nam, maNV);
        
        dtm.setRowCount(0);
        for (HoaDonKemGiaDTO hd :  dsHoaDon) {
            Object[] rowData = {
                hd.getMaHoaDon(),
                hd.getTenNV(),
                hd.getTenKH(),
                hd. getNgayBan(),
                hd.getGhiChu(),
                df.format(hd.getTongTien())
            };
            dtm.addRow(rowData);
        }
    }
    
    public void quayLaiDanhSach() {
        cardLayout.show(mainContainer, "DanhSach");
        loadDummyData();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if (o == tblHoaDon) {
            if (e.getClickCount() == 2) {
                int row = tblHoaDon. getSelectedRow();
                if (row >= 0) {
                    String maThuoc = tblHoaDon.getValueAt(row, 0).toString();
                    String tenThuoc = tblHoaDon.getValueAt(row, 1).toString();
                    
                    System.out.println("Double click vào:  " + maThuoc + " - " + tenThuoc);
                    
                    // Tạo panel chi tiết
                    JPanel chiTietPanel = new DanhMucHoaDon(this,maThuoc,thang,ngay,nam);
                    
                    try {
                        mainContainer.remove(mainContainer.getComponent(1));
                    } catch (Exception ex) {
                        // Không có panel chi tiết cũ
                    }
                    
                    mainContainer.add(chiTietPanel, "ChiTiet");
                    cardLayout.show(mainContainer, "ChiTiet");
                }
            }
        }
    }
    
    
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}