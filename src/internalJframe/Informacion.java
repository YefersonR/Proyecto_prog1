package internalJframe;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;

public class Informacion extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Informacion frame = new Informacion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Informacion() {
		setBounds(100, 100, 450, 300);

	}

}
