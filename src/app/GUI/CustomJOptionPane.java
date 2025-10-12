package app.GUI;
import javax.swing.*;
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

        JLabel lblTextDisplay = new JLabel(textDisplay);
        lblTextDisplay.setFont(new Font("Roboto", Font.BOLD, 17));
        JPanel pnlTextDisplay = new JPanel(new BorderLayout());
        pnlTextDisplay.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        pnlTextDisplay.add(lblTextDisplay, BorderLayout.CENTER);
        
        
        if(isYesNoOption) {
            pane = new JOptionPane(
            	pnlTextDisplay,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION
            );
        }
        else {
            pane = new JOptionPane(
            		"<html>" +
                            "<body style='font-family: Roboto; font-size: 12px;'>" +
                            textDisplay +
                            "</body>" +
                            "</html>",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION
            );
        }
        
        
        JButton btnXacNhan = new JButton("Xác nhận");
        JButton btnKhong = new JButton("Không");
        
        // Style nút 1
        btnXacNhan.setPreferredSize(new Dimension(100, 35));
        btnXacNhan.setFont(new Font("Arial", Font.PLAIN, 13));
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
        
        dialog.setVisible(true);
        
        return result[0];
    }
    
}