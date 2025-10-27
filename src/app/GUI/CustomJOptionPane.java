package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class CustomJOptionPane {
	private Component parent;
	private String textDisplay;
	private boolean isYesNoOption;
	public CustomJOptionPane(Component parent, String textDisplay, boolean isYesNoOption) {
		this.parent = parent;
		this.textDisplay = textDisplay;
		this.isYesNoOption = isYesNoOption;
		
	}
    public int show() {
        // Tạo JOptionPane
        JOptionPane pane;

        JTextArea txtTextDisplay = new JTextArea(textDisplay);
        txtTextDisplay.setFont(new Font("Roboto", Font.BOLD, 15));
        txtTextDisplay.setEditable(false);
        txtTextDisplay.setOpaque(false);
        txtTextDisplay.setFocusable(false);
        txtTextDisplay.setLineWrap(true);
        txtTextDisplay.setWrapStyleWord(true);
//        txtTextDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtTextDisplay.setBackground(new Color(0, 0, 0, 0)); // Trong suốt
        
        
        int maxWidth = 350;
        txtTextDisplay.setSize(maxWidth, Short.MAX_VALUE);
        Dimension preferredSize = txtTextDisplay.getPreferredSize();
        txtTextDisplay.setPreferredSize(new Dimension(maxWidth, preferredSize.height));
        
        JPanel pnlText = new JPanel(new BorderLayout());
        pnlText.add(txtTextDisplay, BorderLayout.CENTER);
        pnlText.setBorder(new EmptyBorder(10, 10, 10, 10));

        if(isYesNoOption) {
            pane = new JOptionPane(
            		pnlText,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION
            );
        }
        else {
            pane = new JOptionPane(
            		pnlText,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION
            );
        }

        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnKhong = new JButton("Không");
        
        // Style nút 1
        btnXacNhan.setPreferredSize(new Dimension(100, 35));
        btnXacNhan.setFont(new Font("Arial", Font.PLAIN, 12));
        btnXacNhan.setBackground(new Color(230,255,230));
        btnXacNhan.setForeground(Color.BLACK);
        btnXacNhan.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Style nút 2
        btnKhong.setPreferredSize(new Dimension(100, 35));
        btnKhong.setFont(new Font("Arial", Font.PLAIN, 13));
        btnKhong.setBackground(new Color(242, 242, 242));
        btnKhong.setForeground(Color.BLACK);
        btnKhong.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnKhong.setFocusPainted(false);
        btnKhong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set các nút vào OptionPane
        if(isYesNoOption)
        	pane.setOptions(new Object[]{btnXacNhan, btnKhong});
        else
        	pane.setOptions(new Object[]{btnXacNhan});
        
        pane.setInitialValue(btnXacNhan);
        // Tạo dialog
        JDialog dialog = pane.createDialog(parent, "Thông báo");
        
        // Xử lý click
        final int[] result = {-1};
        
        btnXacNhan.addActionListener(e -> {
            result[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });
        
        btnKhong.addActionListener(e -> {
            result[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });
        dialog.pack();
        dialog.setVisible(true);
        
        return result[0];
    }
    
}