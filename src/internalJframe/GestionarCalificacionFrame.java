package internalJframe;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import conexionConBase.ConexionDB;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GestionarCalificacionFrame extends JInternalFrame {
	
	private JPanel panel;
	private JTable table;
	
	private JTextField textCodigoCalificacion;
	private JTextField textNombre;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	
	private JComboBox<String> comboAsignatura;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionarCalificacionFrame frame = new GestionarCalificacionFrame();
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
	public GestionarCalificacionFrame() {
		setClosable(true);
		setTitle("Gestionar Calificaciones");
		setBounds(0, 0, 763, 559);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		comboAsignatura = new JComboBox<String>();
		comboAsignatura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select Nombre from Calificaciones order by id");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboAsignatura.addItem(rs.getString("Nombre"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"No se ha podido obtener el nombre");	
				}
			}
		});
		comboAsignatura.addMouseListener(new MouseAdapter() {
			
		});
		comboAsignatura.setBounds(317, 32, 121, 22);
		panel.add(comboAsignatura);
		
		comboAsignatura.addItem(" ");
		
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
					ps = con.prepareStatement("Select * from Calificaciones where IDcalificacion=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigoCalificacion.setText(String.valueOf("IDcalificacion"));
						textNombre.setText(rs.getString("Nombre "));
						comboAsignatura.getModel().setSelectedItem((rs.getString("Asignatura")));
						
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}
			}
		});
		scrollPane.setViewportView(table);
	}
	
	private void labels() {
		
		JLabel lblCodigoCalificacion = new JLabel("Codigo");
		lblCodigoCalificacion.setBounds(10, 11, 106, 20);
		lblCodigoCalificacion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblCodigoCalificacion);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(126, 11, 86, 20);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNombre);
		
		JLabel lblAsignatura = new JLabel("Asignatura");
		 lblAsignatura.setFont(new Font("Tahoma", Font.PLAIN, 16));
		 lblAsignatura.setBounds(317, 11, 86, 20);
		panel.add( lblAsignatura);
		
	}
	
	private void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Nombre =textNombre.getText();
				String Asignatura = comboAsignatura.getSelectedItem().toString();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Calificaciones(Nombre,Asignatura) values(?,?) ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Asignatura);
					
					
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro insertado");
					cargartabla();
					
					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No insertado");	
				}
			}
		});
		btnInsertar.setBounds(10, 71, 150, 30);
		btnInsertar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnInsertar);
		
		btnActualizar = new JButton("Actualizar");
		btnActualizar.setEnabled(false);
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int id  = Integer.parseInt(textCodigoCalificacion.getText());
				String Nombre =textNombre.getText();
				String Asignatura = comboAsignatura.getSelectedItem().toString();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Calificaciones set Nombre=?,Asignatura=? where IDcalificacion=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Asignatura);
					ps.setInt(3, id);
					
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro insertado");
					cargartabla();
					habilitarbotones();
					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No insertado");	
				}
			}
		});
		btnActualizar.setBounds(170, 71, 150, 30);
		btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnActualizar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(textCodigoCalificacion.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("delete from Calificaciones where IDcalificacion=?");
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
		btnEliminar.setBounds(330, 71, 150, 30);
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnEliminar);
		
	}
	
	private void textFields() {
		
		textCodigoCalificacion = new JTextField();
		textCodigoCalificacion.setEditable(false);
		textCodigoCalificacion.setBounds(10, 33, 105, 20);
		panel.add(textCodigoCalificacion);
		textCodigoCalificacion.setColumns(10);
		
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
					ps = con.prepareStatement("Select * from Calificaciones where Nombre like '%"+nombre+ "%'");
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
	

	private void cargartabla(){
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		PreparedStatement ps;
		ResultSet rs;
		ResultSetMetaData rmd;
		int column;
		
		int [] ancho= {10,50,20,100};
		for(int i = 0; i<table.getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(ancho[i]);;
		}
		try {
			Connection con = ConexionDB.getConnection();
			ps = con.prepareStatement("Select * from Calificaciones");
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

	
	private void mostrarDatos(){
	
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Codigo");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Asignatura");
		
		
		table.setModel(modeloTabla);
	}
	
}
