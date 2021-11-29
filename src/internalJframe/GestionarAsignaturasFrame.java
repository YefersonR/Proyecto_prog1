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

public class GestionarAsignaturasFrame extends JInternalFrame {
	
	private JPanel panel;
	private JTextField textCodigoMateria;
	private JTextField textNombre;
	private JTextField textProfesor;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	
	private JComboBox<String> comboCurso;
	
	private JTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionarAsignaturasFrame frame = new GestionarAsignaturasFrame();
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
	public GestionarAsignaturasFrame() {
		setClosable(true);
		setTitle("Gestionar Asignaturas");
		setBounds(0, 0, 763, 559);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		/// ComboBox
		comboCurso = new JComboBox<String>();
		comboCurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select Nombre from Curso");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboCurso.addItem(rs.getString("Nombre"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No");	
				}
			}
		});
		comboCurso.addMouseListener(new MouseAdapter() {
		});
		comboCurso.setBounds(496, 32, 59, 22);
		panel.add(comboCurso);
			
		comboCurso.addItem(" ");
		/// ComboBox
		
		
		labels();
		textFields();
		table();
		buttons();
		mostrarDatos();
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
					ps = con.prepareStatement("Select * from Asignaturas where IDasignatura=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigoMateria.setText(String.valueOf("IDcalificacion"));
						textNombre.setText(rs.getString("Nombre"));
						textProfesor.setText(rs.getString("Profesor"));
						comboCurso.getModel().setSelectedItem((rs.getString("Curso")));
						
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
		
		JLabel lblCodigoMateria = new JLabel("Codigo");
		lblCodigoMateria.setBounds(10, 11, 106, 20);
		lblCodigoMateria.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblCodigoMateria);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(126, 11, 86, 20);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNombre);
		
		JLabel lblProfesor = new JLabel("Profesor");
		lblProfesor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblProfesor.setBounds(317, 11, 86, 20);
		panel.add(lblProfesor);
		
		JLabel lblCursos = new JLabel("Cursos");
		lblCursos.setBounds(496, 11, 59, 20);
		lblCursos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblCursos);
		
	}

	private void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Nombre =textNombre.getText();
				String Profesor =textProfesor.getText();
				String Cursos = comboCurso.getSelectedItem().toString();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Asignaturas(Nombre,IDcursos,IDprofesor) values(?,?,?) ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Profesor);
					ps.setString(3, Cursos);					
					
					
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
				int id  = Integer.parseInt(textCodigoMateria.getText());
				String Nombre =textNombre.getText();
				String Profesor =textProfesor.getText();
				String Cursos = comboCurso.getSelectedItem().toString();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Asignaturas set Nombre=?,IDcursos=?,IDprofesor=? where IDasignatura=?");
					
					ps.setString(1, Nombre);
					ps.setString(2, Profesor);
					ps.setString(3, Cursos);					
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
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int id = Integer.parseInt(textCodigoMateria.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("delete from Asignaturas where IDasignatura=?");
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
		
		textCodigoMateria = new JTextField();
		textCodigoMateria.setEditable(false);
		textCodigoMateria.setBounds(10, 33, 105, 20);
		panel.add(textCodigoMateria);
		textCodigoMateria.setColumns(10);
		
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
					ps = con.prepareStatement("Select * from Asignaturas where Nombre like '%"+nombre+ "%'");
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
		
		textProfesor = new JTextField();
		textProfesor.setBounds(317, 33, 169, 20);
		textProfesor.setColumns(10);
		panel.add(textProfesor);
		
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
			ps = con.prepareStatement("Select * from Asignaturas");
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
		modeloTabla.addColumn("Profesor");
		modeloTabla.addColumn("Curso");
		
		
		table.setModel(modeloTabla);
		
	}
	
	
	
}
