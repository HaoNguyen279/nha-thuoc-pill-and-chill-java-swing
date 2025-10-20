package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LapHoaDonPanel extends JPanel {

    public LapHoaDonPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel pnlCenter = createCenterPanel();
        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = createBottomPanel();
        add(pnlBottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createCenterPanel() {
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 20));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);

        pnlCenter.add(titlePanel, BorderLayout.NORTH);

        // Panel giữa chứa 2 bảng
        JPanel pnlTable = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlTable.setBackground(Color.WHITE);

        // Bảng trên - Lập hóa đơn
        JPanel topTablePanel = createTablePanel("", "Thêm vào giỏ hàng");
        pnlTable.add(topTablePanel);

        // Bảng dưới - Giỏ hàng
        JPanel bottomTablePanel = createTablePanel("GIỎ HÀNG", "Xóa khỏi giỏ hàng");
        pnlTable.add(bottomTablePanel);

        pnlCenter.add(pnlTable, BorderLayout.CENTER);

        return pnlCenter;
    }

    private JPanel createSearchPanel() {
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        pnlSearch.setBackground(Color.WHITE);

        JTextField txtSearch = new JTextField(25);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font("Arial", Font.ITALIC, 13));
        txtSearch.setBackground(new Color(245, 245, 245));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSearch.setPreferredSize(new Dimension(80, 30));
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        return pnlSearch;
    }

    private JPanel createTablePanel(String title, String buttonText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Bảng dữ liệu
        String[] columns = {"Mã thuốc", "Tên thuốc", "Số lượng", "Giá", "Thành tiền"};
        Object[][] data = {};

        JTable tblThuoc = new JTable(data, columns);
        tblThuoc.setRowHeight(30);
        tblThuoc.setBackground(new Color(240, 250, 240));
        tblThuoc.setGridColor(Color.LIGHT_GRAY);
        tblThuoc.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel điều khiển dưới bảng
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlControl.setBackground(Color.WHITE);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(60, 25));

        JButton btnAction = new JButton(buttonText);
        btnAction.setFont(new Font("Arial", Font.PLAIN, 13));
        btnAction.setPreferredSize(new Dimension(160, 35));
        btnAction.setBackground(Color.WHITE);
        btnAction.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlControl.add(quantityLabel);
        pnlControl.add(quantitySpinner);
        pnlControl.add(btnAction);

        panel.add(pnlControl, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 14));
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(Color.WHITE);
        btnConfirm.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlBottom.add(btnConfirm);

        return pnlBottom;
    }
}
