package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB. ConnectDB;
import app.  DAO.ChiTietLoThuocDAO;
import app.DAO.ThuocDAO;
import app. Entity.ChiTietLoThuoc;
import app.Entity. Thuoc;

import java. awt.*;
import java.awt. event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java. text.SimpleDateFormat;
import java.util. ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimKiemChiTietLoThuocPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private JLabel lblTieuDe;
    private JLabel lblMaThuoc;
    private JLabel lblMaLo;
    private JLabel lblSoLuong;
    private JLabel lblGiaNhap;

    private JTextField txtMaThuoc;
    private JTextField txtMaLo;
    private JTextField txtSoLuong;
    private JTextField txtGiaNhap;

    private JButton btnXoaTrang;
    private JButton btnTimKiem;
    
    private DefaultTableModel dtmTable;
    private ArrayList<ChiTietLoThuoc> dsCTLT;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");
    private ChiTietLoThuocDAO ctltDAO = new ChiTietLoThuocDAO();
    private ThuocDAO thuocDAO = new ThuocDAO();
    
    public TimKiemChiTietLoThuocPanel() {
        FlatLightLaf.setup();
        setLayout(new BorderLayout());
        
        ConnectDB. getInstance().connect();
        
        dsCTLT = ctltDAO.getAllActiveChiTietLoThuoc();
        JPanel topPanel = createTopPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = taoCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout. NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("ENTER"), "clickSearch");
        getActionMap().put("clickSearch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTimKiem. doClick();
            }
        });
        
        setVisible(true);
    }
    
    public JPanel createTopPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        JPanel pnlTopOfMain = new JPanel();
        JPanel pnlCenterOfMain = new JPanel(new GridLayout(2, 2, 10, 5));
        JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        lblTieuDe = new JLabel("TÌM KIẾM CHI TIẾT LÔ THUỐC", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        pnlTopOfMain. add(lblTieuDe);
        
        lblMaThuoc = new JLabel("Mã thuốc:");
        lblMaThuoc. setOpaque(true);
        lblMaLo = new JLabel("Mã lô:");
        lblSoLuong = new JLabel("Số lượng tối thiểu:");
        lblGiaNhap = new JLabel("Giá nhập tối thiểu:");
        
        txtMaThuoc = new JTextField(15);
        txtMaLo = new JTextField(15);
        txtSoLuong = new JTextField(15);
        txtGiaNhap = new JTextField(15);
        
        btnXoaTrang = new JButton("Xóa trắng");
        btnTimKiem = new JButton("Tìm kiếm");
        
        pnlMain.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        JPanel pnlr1 = new JPanel(new BorderLayout());
        pnlr1.add(lblMaThuoc, BorderLayout.WEST);
        pnlr1.add(txtMaThuoc);
        pnlr1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel pnlr2 = new JPanel(new BorderLayout());
        pnlr2.add(lblMaLo, BorderLayout.WEST);
        pnlr2.add(txtMaLo);
        pnlr2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel pnlr3 = new JPanel(new BorderLayout());
        pnlr3.add(lblSoLuong, BorderLayout.WEST);
        pnlr3.add(txtSoLuong);
        pnlr3.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel pnlr4 = new JPanel(new BorderLayout());
        pnlr4.add(lblGiaNhap, BorderLayout.WEST);
        pnlr4.add(txtGiaNhap);
        pnlr4.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        pnlCenterOfMain.add(pnlr1);
        pnlCenterOfMain.add(pnlr2);
        pnlCenterOfMain. add(pnlr3);
        pnlCenterOfMain.add(pnlr4);
        
        // JButton, JLabel, JTextField customization
        JButton[] btnList = {btnXoaTrang, btnTimKiem};
        for (JButton item : btnList) {
            item.setFont(new Font("Segoe UI", Font.BOLD, 16));
            item.setForeground(Color.WHITE);
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color. LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 45, 15, 45)
            ));
            item.setFocusPainted(false);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            item.addActionListener(this);
        }
        btnXoaTrang.setForeground(Color.BLACK);
        btnTimKiem.setForeground(Color.BLACK);
        
        JLabel[] lblItems = {
            lblMaThuoc, lblMaLo, lblSoLuong, lblGiaNhap
        };
        for (JLabel item : lblItems) {
            item.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            item.setPreferredSize(new Dimension(150, 0));
        }
        
        JTextField[] txtItems = {
            txtMaThuoc, txtMaLo, txtSoLuong, txtGiaNhap
        };
        for (JTextField item :  txtItems) {
            item. setFont(new Font("Segoe UI", Font.PLAIN, 14));
            item.setBackground(new Color(245, 245, 245));
            item.setPreferredSize(new Dimension(200, 30));
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color. LIGHT_GRAY),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
            ));
        }
        
        pnlBottomOfMain.add(btnXoaTrang);
        pnlBottomOfMain.add(Box.createHorizontalStrut(10));
        pnlBottomOfMain.add(btnTimKiem);
        pnlBottomOfMain.add(Box.createHorizontalStrut(10));
        
        pnlMain.add(pnlTopOfMain, BorderLayout. NORTH);
        pnlCenterOfMain.setPreferredSize(new Dimension(0, 80));
        pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
        pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMain.add(pnlBottomOfMain, BorderLayout.SOUTH);
        
        return pnlMain;
    }

    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Table Panel
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"Mã lô", "Mã thuốc", "Tên thuốc", "Số lượng", 
                         "Giá nhập", "Ngày SX", "Hạn SD"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData_CTLT(dsCTLT);
        
        table = new JTable(dtmTable) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (! isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };
        
        // Style table
        table. setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        table.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane. setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsCTLT.size());
        lblTongSoBanGhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongSoBanGhi. setBorder(new EmptyBorder(15, 0, 0, 0));
        
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout.SOUTH);
        return centerPanel;
    }
    
    public void loadData_CTLT(ArrayList<ChiTietLoThuoc> dsCTLT) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dtmTable.setRowCount(0);
        Map<String, String> mapThuoc = new HashMap<>();
        for (Thuoc t : thuocDAO.getAllThuoc()) {
            mapThuoc.put(t.getMaThuoc(), t.getTenThuoc());
        }
        
        for (ChiTietLoThuoc ctlt : dsCTLT) { 
        	String tenThuoc = mapThuoc.getOrDefault(ctlt.getMaThuoc(), "N/A");
            Object[] rowData = {
                ctlt. getMaLo(),
                ctlt.getMaThuoc(),
                tenThuoc,
                ctlt.getSoLuong(),
                String.format("%,.0f", ctlt.getGiaNhap()),
                sdf.format(ctlt. getNgaySanXuat()),
                sdf. format(ctlt.getHanSuDung())
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi. setText("Tổng số bản ghi: " + dsCTLT.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o == btnXoaTrang) {
            txtMaThuoc.setText("");
            txtMaLo.setText("");
            txtSoLuong.setText("");
            txtGiaNhap.setText("");
            loadData_CTLT(dsCTLT);
        } 
        else if (o == btnTimKiem) {
            String maThuocTim = txtMaThuoc. getText().trim().toLowerCase();
            String maLoTim = txtMaLo.getText().trim().toLowerCase();
            String soLuongStr = txtSoLuong. getText().trim();
            String giaNhapStr = txtGiaNhap.getText().trim();

            Integer soLuongTim = null;
            Double giaNhapTim = null;
        
            if (! soLuongStr.isEmpty()) {
                try {
                    soLuongTim = Integer.parseInt(soLuongStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Số lượng phải là số nguyên! ",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!giaNhapStr.isEmpty()) {
                try {
                    giaNhapTim = Double.parseDouble(giaNhapStr);
                } catch (NumberFormatException ex) {
                    JOptionPane. showMessageDialog(this,
                            "Giá nhập phải là số! ",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            ArrayList<ChiTietLoThuoc> ketQua = new ArrayList<>();

            for (ChiTietLoThuoc ct : dsCTLT) {
                boolean match = true;

                if (! maThuocTim.isEmpty() &&
                        ! ct.getMaThuoc().toLowerCase().contains(maThuocTim)) {
                    match = false;
                }

                if (match && !maLoTim. isEmpty() &&
                        !ct.getMaLo().toLowerCase().contains(maLoTim)) {
                    match = false;
                }

                if (match && soLuongTim != null &&
                        ct.getSoLuong() < soLuongTim) {
                    match = false;
                }

                if (match && giaNhapTim != null &&
                        ct.getGiaNhap() < giaNhapTim) {
                    match = false;
                }

                if (match) {
                    ketQua.add(ct);
                }
            }

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả phù hợp!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadData_CTLT(dsCTLT);
                return;
            }

            loadData_CTLT(ketQua);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}