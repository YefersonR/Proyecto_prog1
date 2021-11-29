package conexionConBase;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConexionDB {
	
	private static Connection cn;
	
	public static Connection getConnection() {
		JFrame exception = new JFrame();;
		String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=ESCUELADB;user=sa;password=1234";

		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			cn = DriverManager.getConnection(connectionUrl);
			
			//JOptionPane.showMessageDialog(exception, "Conexion Saticfactora");
		} 
		catch (Exception ex){
			
			JFrame exception1 = new JFrame();;
			JOptionPane.showMessageDialog(exception1, ex.toString());
			
			cn = null;
		}
		
		return cn;
	}
}
