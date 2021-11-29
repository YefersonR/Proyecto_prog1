package internalJframe;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import conexionConBase.ConexionDB;

import javax.swing.JComboBox;
import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;


import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GestionarEstudiantesFrame extends JInternalFrame {
	
	private JTextField textMatricula;
	private JTextField textNombre;
	private JTextField textTelefono;
	private JTextField textDireccion;
	private JTextField textEmail;
	private JLabel lblDireccion;
	private JLabel lblEmail;
	private JTable table;
	private JPanel panel;
	private JDateChooser fecha;
	private JComboBox<String> comboSexo;
	private JButton btnInsertar,btnActualizar,btnEliminar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionarEstudiantesFrame frame = new GestionarEstudiantesFrame();
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
	public GestionarEstudiantesFrame() {
		setClosable(true);
		setTitle("Gestionar Estudiantes");
		setBounds(0, 0, 763, 559);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		fecha = new JDateChooser();
		fecha.setBounds(347, 85, 169, 20);
		panel.add(fecha);
		
	
		comboSexo = new JComboBox<String>();
		comboSexo.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select Sexo from Sexo order by id");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboSexo.addItem(rs.getString("Sexo"));
						}
						
				}catch(SQLException e1) 
					{
						JOptionPane.showMessageDialog(null,"Registro No Sexo");	
					}
				}
			});
			comboSexo.setBounds(345, 84, 47, 22);
			panel.add(comboSexo);
		comboSexo.setBounds(496, 32, 47, 22);
		panel.add(comboSexo);
		
		comboSexo.addItem(" ");

		Labels();
		textFields();
		buttons();
		table();
		mostrarDatos();
		cargartabla();

		

	}
	private void table() {
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(10, 138, 727, 380);
		panel.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int fila = table.getSelectedRow();
					int id =Integer.parseInt(table.getValueAt(fila, 0).toString());
					
					PreparedStatement ps;
					ResultSet rs;
					
					Connection con = ConexionDB.getConnection();
					ps = con.prepareStatement("Select * from Alumno where Matricula=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textMatricula.setText(String.valueOf("Matricula"));
						textNombre.setText(rs.getString("Nombre "));
						comboSexo.getModel().setSelectedItem((rs.getString("Sexo")));
						fecha.setToolTipText(rs.getString("Fecha_Nacimiento "));
						textTelefono.setText(rs.getString("Telefono  "));
						textEmail.setText(rs.getString("Email  "));
						textDireccion.setText(rs.getString("Direccion  "));
						
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}

			}
		});
		scrollPane.setViewportView(table);
	}
	private void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Nombre =textNombre.getText();
				String Sexo = comboSexo.getSelectedItem().toString();
				int anio =fecha.getCalendar().get(Calendar.YEAR);
				int mes =fecha.getCalendar().get(Calendar.MARCH);
				int dia =fecha.getCalendar().get(Calendar.DAY_OF_MONTH);
				String Fecha_Nacimiento =(anio+"-"+mes+"-"+dia);
				String Telefono = textTelefono.getText();
				String Email =textEmail.getText();
				String Direccion = textDireccion.getText();
				int sexo;
				if(Sexo=="F") {
					 sexo= 1;
				}
				else {
					sexo=2;
				}
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Alumnos(Nombre,idsexo,Fecha_Nacimiento,Telefono,Email,Direccion) values(?,?,?,?,?,?) ");
					
					ps.setString(1, Nombre);
					ps.setInt(2, sexo);
					ps.setString(3, Fecha_Nacimiento);
					ps.setString(4, Telefono);
					ps.setString(5, Email);
					ps.setString(6, Direccion);
					
					
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro insertado");
					cargartabla();
					
					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No insertado");	
				}

			}
		});
		btnInsertar.setBounds(553, 25, 184, 30);
		btnInsertar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnInsertar);
		
		btnActualizar = new JButton("Actualizar");
		btnActualizar.setEnabled(false);
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int Matricula  = Integer.parseInt(textMatricula.getText());
				String Nombre =textNombre.getText();
				String Sexo = comboSexo.getSelectedItem().toString();
				int anio =fecha.getCalendar().get(Calendar.YEAR);
				int mes =fecha.getCalendar().get(Calendar.MARCH);
				int dia =fecha.getCalendar().get(Calendar.DAY_OF_MONTH);
				String Fecha_Nacimiento =(anio+"-"+mes+"-"+dia);
				String Telefono = textTelefono.getText();
				String Email =textEmail.getText();
				String Direccion = textDireccion.getText();
				 
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Alumno set Nombre=?,Sexo=?,Fecha_Nacimiento=?,Telefono=? Email=?,Direccion=? where Matricula=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
					ps.setString(3, Fecha_Nacimiento);
					ps.setString(4, Telefono);
					ps.setString(5, Email);
					ps.setString(6, Direccion);
					ps.setInt(7, Matricula);
					
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro Actualizado");
					cargartabla();
					habilitarbotones();
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No actualizado");	
				}
			}
		});
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});

		btnActualizar.setBounds(553, 60, 184, 30);
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnActualizar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int Matricula = Integer.parseInt(textMatricula.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("delete from Alumnos where Matricula=?");
					ps.setInt(1, Matricula);
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro Eliminado");
					cargartabla();
					habilitarbotones();
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No eliminado");	
				}
			}

		});
		btnEliminar.setBounds(553, 95, 184, 32);
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnEliminar);
		
	}
	private void textFields() {
		
		textMatricula = new JTextField();
		textMatricula.setEditable(false);
		textMatricula.setBounds(10, 33, 105, 20);
		panel.add(textMatricula);
		textMatricula.setColumns(10);
		
		textNombre = new JTextField();
		textNombre.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				DefaultTableModel modelo = (DefaultTableModel) table.getModel();
				modelo.setRowCount(0);
				PreparedStatement ps;
				ResultSet rs;
				ResultSetMetaData rmd;
				int column;
				String nombre = textNombre.getText().trim();
				
				int [] ancho= {10,50,20,100};
				for(int i = 0; i<table.getColumnCount();i++) {
					table.getColumnModel().getColumn(i).setPreferredWidth(ancho[i]);;
				}
				try {
					Connection con = ConexionDB.getConnection();
					ps = con.prepareStatement("Select * from Curso where Nombre like '%"+nombre+ "%'");
					rs = ps.executeQuery();
					rmd = rs.getMetaData();
					column = rmd.getColumnCount();
					while(rs.next()){
						Object[] fila = new Object[column];
						for(int indice=0; indice<column;indice++) {
							fila[indice] = rs.getObject(indice+1);
						}
						modelo.addRow(fila);
					}
					
				}catch(SQLException e2) {
					JOptionPane.showMessageDialog(null, e2.toString());
				}
			}
		});
		textNombre.setBounds(125, 33, 182, 20);
		textNombre.setColumns(10);
		panel.add(textNombre);
		
		textTelefono = new JTextField();
		textTelefono.setBounds(10, 85, 135, 20);
		textTelefono.setColumns(10);
		panel.add(textTelefono);
		
		textDireccion = new JTextField();
		textDireccion.setBounds(155, 85, 182, 20);
		textDireccion.setColumns(10);
		panel.add(textDireccion);
		
		textEmail = new JTextField();
		textEmail.setBounds(317, 33, 169, 20);
		textEmail.setColumns(10);
		panel.add(textEmail);
		
	}
	private void Labels() {
		
		JLabel lblNewLabel = new JLabel("Matricula");
		lblNewLabel.setBounds(10, 11, 86, 20);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNewLabel);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(125, 11, 86, 20);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNombre);
		
		JLabel lblFecha = new JLabel("Fecha de nacimiento");
		lblFecha.setBounds(347, 65, 169, 20);
		lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblFecha);
		
		JLabel lblSexo = new JLabel("Sexo");
		lblSexo.setBounds(496, 11, 47, 20);
		lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblSexo);
		
		JLabel lbltelefono = new JLabel("Telefono ");
		lbltelefono.setBounds(10, 65, 86, 20);
		lbltelefono.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lbltelefono);
		
		lblDireccion = new JLabel("Direccion ");
		lblDireccion.setBounds(155, 65, 86, 20);
		lblDireccion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblDireccion);
		
		lblEmail = new JLabel("E-mail");
		lblEmail.setBounds(317, 11, 86, 20);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblEmail);
		
		
	}
	private void habilitarbotones(){
		
		if(table.getSelectedRow() != -1) {
			btnActualizar.setEnabled(true);
			btnEliminar.setEnabled(true);
			btnInsertar.setEnabled(false);
		}
		else {
			btnActualizar.setEnabled(false);
			btnEliminar.setEnabled(false);
			btnInsertar.setEnabled(true);
			
		}
	}
	
	private void limpiar() {
		textMatricula.setText("");
		textNombre.setText("");
		comboSexo.getModel().setSelectedItem(" ");
		fecha.setToolTipText("");
		textTelefono.setText("");
		textEmail.setText("");
		textDireccion.setText("");				
}

	private void cargartabla(){
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		PreparedStatement ps;
		ResultSet rs;
		ResultSetMetaData rmd;
		int column;
	/*	
		int [] ancho= {10,50,20,100};
		for(int i = 0; i<table.getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(ancho[i]);;
		}
		*/
		try {
			Connection con = ConexionDB.getConnection();
			ps = con.prepareStatement("Select * from Alumnos");
			rs = ps.executeQuery();
			rmd = rs.getMetaData();
			column = rmd.getColumnCount();
			while(rs.next()){
				Object[] fila = new Object[column];
				for(int indice=0; indice<column;indice++) {
					fila[indice] = rs.getObject(indice+1);
				}
				modelo.addRow(fila);
			}
			limpiar();
		}catch(SQLException e2) {
			JOptionPane.showMessageDialog(null, e2.toString());
		}
	}
	private void mostrarDatos(){
		//BasedeDatos = Conexion.getConnection();
		
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Matricula");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Sexo");
		modeloTabla.addColumn("E-mail");
		modeloTabla.addColumn("Telefono");
		modeloTabla.addColumn("Fecha de nacimiento");
		modeloTabla.addColumn("Direccion");
		
		
		table.setModel(modeloTabla);
		
		//String Datos[] = new String[3];
		
		//try {
			//Statement leer = //BasedeDatos.createStatement();
			//ResultSet resultado = leer.executeQuery("SELECT * FROM Eventos_Agenda");
			
			//while (resultado.next()) {
					//Datos[0] = String.valueOf(resultado.getString(1));
					//Datos[1] = resultado.getString(2);
					//Datos[2] = resultado.getString(3);
					//modeloTabla.addRow(Datos);
			//}
			//table.setModel(modeloTabla);
			
			
		//}//catch (Exception e) {
			//JOptionPane.showMessageDialog(null, e.toString());	
		//}
	}
}
