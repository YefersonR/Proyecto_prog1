package internalJframe;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import conexionConBase.ConexionDB;
import index.Interface;

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

public class GestionarProfesoresFrame extends JInternalFrame implements Interface{
	
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
			
		setBackground(new Color(255, 255, 255));
        setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 164, 237)));
        setClosable(true);
        setTitle("Gestionar Profesores");
        setBounds(0, 0, 763, 559);
        getContentPane().setLayout(null);
        
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
			comboSexo = new JComboBox<String>();
			comboSexo.setBounds(345, 84, 47, 22);
			panel.add(comboSexo);


			comboSexo.addItem("M");
			comboSexo.addItem("F");
		
		
		labels();
		buttons();
		textFields();
		//table();
		mostrarDatos();
		cargartabla();
	}
	
	public void table() {
		
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
						textCodigo.setText(rs.getString(String.valueOf("Codigo")));
						textNombre.setText(rs.getString("Nombre"));
						comboSexo.getModel().setSelectedItem((rs.getString(String.valueOf("Sexo"))));
						textTelefono.setText(rs.getString("Telefono"));
						textDireccion.setText(rs.getString("Direccion"));
						textEmail.setText(rs.getString("Email"));
						
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}

			}
		});
		scrollPane.setViewportView(table);
	}
	
	public void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Nombre =textNombre.getText();
				String Sexo = comboSexo.getSelectedItem().toString();
				String Telefono = textTelefono.getText();
				String Email =textEmail.getText();
				String Direccion = textDireccion.getText();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Profesores(Nombre,Sexo,Telefono,Email,Direccion) values(?,?,?,?,?) ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
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
					PreparedStatement ps = con.prepareStatement("update Profesores set Nombre=?,Sexo=?,Telefono=?,Direccion=?,Email=? where Codigo=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
					ps.setString(3, Telefono);
					ps.setString(4, Email);
					ps.setString(5, Direccion);
					ps.setInt(6, Codigo);
					
					
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
	
	public void labels() {
		
		JLabel lblCodigoProfesor = new JLabel("Codigo");
        lblCodigoProfesor.setBounds(210, 11, 60, 20);
        lblCodigoProfesor.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblCodigoProfesor);
		
        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(71, 11, 60, 20);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNombre);
        
        JLabel lblSexo = new JLabel("Sexo");
        lblSexo.setBounds(345, 64, 39, 20);
        lblSexo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblSexo);

        JLabel lbltelefono = new JLabel("Tel\u00E9fono ");
        lbltelefono.setBounds(209, 65, 60, 20);
        lbltelefono.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lbltelefono);

        JLabel lblDireccion = new JLabel("Direcci\u00F3n ");
        lblDireccion.setBounds(331, 10, 67, 20);
        lblDireccion.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblDireccion);
        
        JLabel lblEmail = new JLabel("E-mail");
        lblEmail.setBounds(78, 65, 47, 20);
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblEmail);
		
	}
	
	public void textFields() {
		
		textCodigo = new JTextField();
		textCodigo.setEditable(false);
		textCodigo.setHorizontalAlignment(SwingConstants.CENTER);
        textCodigo.setBounds(201, 33, 79, 20);
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
		
	      textNombre.setBounds(10, 33, 182, 20);
	      textNombre.setColumns(10);
	      panel.add(textNombre);
	        
	      	textTelefono = new JTextField();
	        textTelefono.setBounds(202, 84, 78, 20);
	        textTelefono.setColumns(10);
	        panel.add(textTelefono);

	        textDireccion = new JTextField();
	        textDireccion.setBounds(290, 32, 149, 20);
	        textDireccion.setColumns(10);
	        panel.add(textDireccion);

	        textEmail = new JTextField();
	        textEmail.setBounds(10, 84, 182, 20);
	        textEmail.setColumns(10);
	        panel.add(textEmail);
		
	}
	
	public void habilitarbotones(){
		
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
	public void limpiar() {
		textCodigo.setText("");
		textNombre.setText("");
		comboSexo.getModel().setSelectedItem("");
		textTelefono.setText("");
		textEmail.setText("");
		textDireccion.setText("");
				
}
	public void cargartabla(){
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		PreparedStatement ps;
		ResultSet rs;
		ResultSetMetaData rmd;
		int column;
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
	public void mostrarDatos(){

		
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Codigo");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Sexo");
		modeloTabla.addColumn("Telefono");
		modeloTabla.addColumn("Direccion");
		modeloTabla.addColumn("E-mail");
		
		
		table.setModel(modeloTabla);

	}

}
