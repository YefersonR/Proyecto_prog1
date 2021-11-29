package internalJframe;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import conexionConBase.ConexionDB;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GestionarProfesoresFrame extends JInternalFrame {
	
	private JPanel panel;
	private JTextField textCodigo;
	private JTextField textNombre;
	private JTextField textTelefono;
	private JTextField textDireccion;
	private JTextField textEmail;
	private JTable table;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	private JComboBox<String> comboSexo;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionarProfesoresFrame frame = new GestionarProfesoresFrame();
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
	public GestionarProfesoresFrame() {
		setClosable(true);
		setTitle("Gestionar Profesores");
		setBounds(0, 0, 763, 559);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
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
						JOptionPane.showMessageDialog(null,"Registro No actualizado");	
					}
				}
			});
			comboSexo.setBounds(345, 84, 47, 22);
			panel.add(comboSexo);

		
		
		
		labels();
		buttons();
		textFields();
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
					ps = con.prepareStatement("Select * from Profesores where Codigo=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigo.setText(String.valueOf("Codigo"));
						textNombre.setText(rs.getString("Nombre "));
						comboSexo.getModel().setSelectedItem((rs.getString("Sexo")));
						textTelefono.setText(rs.getString("Telefono"));
						textEmail.setText(rs.getString("Email"));
						textDireccion.setText(rs.getString("Direccion"));
						
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
					PreparedStatement ps = con.prepareStatement("Insert into Profesores(Nombre,idsexo,Telefono,Email,Direccion) values(?,?,?,?,?) ");
					
					ps.setString(1, Nombre);
					ps.setInt(2, sexo);
					ps.setString(3, Telefono);
					ps.setString(4, Email);
					ps.setString(5, Direccion);
					
					
					
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
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int Codigo  = Integer.parseInt(textCodigo.getText());
				String Nombre =textNombre.getText();
				String Sexo = comboSexo.getSelectedItem().toString();
				String Telefono = textTelefono.getText();
				String Email =textEmail.getText();
				String Direccion = textDireccion.getText();
				 
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Profesores set Nombre=?,Sexo=?,Telefono=? Email=?,Direccion=? where Codigo=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
					ps.setString(3, Telefono);
					ps.setString(4, Email);
					ps.setString(5, Direccion);
					ps.setInt(7, Codigo);
					
					
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
		btnActualizar.setBounds(553, 60, 184, 30);
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnActualizar);
		
		btnEliminar =  	new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int id = Integer.parseInt(textCodigo.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("delete from Profesores where Codigo=?");
					ps.setInt(1, id);
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
	
	private void labels() {
		
		JLabel lblCodigoProfesor = new JLabel("Codigo");
		lblCodigoProfesor.setBounds(10, 11, 106, 20);
		lblCodigoProfesor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblCodigoProfesor);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(126, 11, 86, 20);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNombre);
		
		JLabel lblSexo = new JLabel("Sexo");
		lblSexo.setBounds(345, 65, 47, 20);
		lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblSexo);
		
		JLabel lblTelefono = new JLabel("Tel\u00E9fono");
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTelefono.setBounds(10, 65, 86, 20);
		panel.add(lblTelefono);
		
		JLabel lblDireccion = new JLabel("Direcci\u00F3n");
		lblDireccion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDireccion.setBounds(155, 65, 86, 20);
		panel.add(lblDireccion);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBounds(317, 11, 86, 20);
		panel.add(lblEmail);
		
	}
	
	private void textFields() {
		
		textCodigo = new JTextField();
		textCodigo.setEditable(false);
		textCodigo.setBounds(10, 33, 105, 20);
		panel.add(textCodigo);
		textCodigo.setColumns(10);
		
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
					ps = con.prepareStatement("Select * from Profesores where Nombre like '%"+nombre+ "%'");
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
		textNombre.addMouseListener(new MouseAdapter() {

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
		textCodigo.setText("");
		textNombre.setText("");
		comboSexo.getModel().setSelectedItem("");
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
			ps = con.prepareStatement("Select * from Profesores");
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

		
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Codigo");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Sexo");
		modeloTabla.addColumn("E-mail");
		modeloTabla.addColumn("Telefono");
		modeloTabla.addColumn("Direccion");
		
		
		table.setModel(modeloTabla);

	}
}
