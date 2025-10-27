package app.GUI;

import java.awt.FlowLayout;

import javax.swing.*;
public class ThongKeTheoNhanVienPanel extends JPanel{
	    public static void main(String[] args) {
	        JFrame frame = new JFrame("Radio Button Group Demo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(300, 200);
	        frame.setLayout(new FlowLayout());

	        // Tạo 3 nút radio
	        JRadioButton r1 = new JRadioButton("Option 1");
	        JRadioButton r2 = new JRadioButton("Option 2");
	        JRadioButton r3 = new JRadioButton("Option 3");

	        // Tạo ButtonGroup -> nhóm 3 nút lại
	        ButtonGroup group = new ButtonGroup();
	        group.add(r1);
	        group.add(r2);
	        group.add(r3);

	        // Chọn mặc định
	        r1.setSelected(true);

	        // Thêm vào frame
	        frame.add(r1);
	        frame.add(r2);
	        frame.add(r3);

	        frame.setVisible(true);
	    
	}


	

}
