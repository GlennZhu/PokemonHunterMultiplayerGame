package zz23_jj26.popupWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class View extends JFrame {
	private static final long serialVersionUID = -6202920466708147835L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public View() {
		start();
	}

	private void start(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
	}
	
	public void addComponent(Component component){
		add(component);
	}
}
