package internalJframe;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import conexionConBase.ConexionDB;
import index.Interface;

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
import java.awt.Color;

public class GestionarCursosFrame extends JInternalFrame implements Interface{
	
	private JPanel panel;
	private JTextField textCodigoCurso;
	private JTextField textNombre;
	private JTextField textSeccion;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private JButton btnInsertar;

	private JTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionarCursosFrame frame = new GestionarCursosFrame();
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
	public GestionarCursosFrame() {
		setBackground(new Color(255, 255, 255));
        setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 164, 237)));
        setClosable(true);
        setTitle("Gestionar Cursos");
        setBounds(0, 0, 763, 559);
        getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		labels();
		buttons();
		textFields();
		table();
		mostrarDatos();
		cargartabla();
	}
	
	public void labels() {
		
		JLabel lblCodigoCurso = new JLabel("Codigo");
        lblCodigoCurso.setBounds(217, 11, 46, 20);
        lblCodigoCurso.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblCodigoCurso);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(71, 11, 60, 20);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNombre);

        JLabel lblSeccion = new JLabel("Seccion");
        lblSeccion.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblSeccion.setBounds(340, 11, 69, 20);
        panel.add( lblSeccion);
	}
	
	public void buttons() {

		btnInsertar = new JButton("Insertar");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Nombre =textNombre.getText();
				String Seccion = textSeccion.getText();
				
				try {
					Connection con = ConexionDB.getConnection();
					
					PreparedStatement ps = con.prepareStatement("Insert into Curso(Nombre,Seccion) values(?,?) ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Seccion);
					
					
					
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
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id  = Integer.parseInt(textCodigoCurso.getText());
				String Nombre =textNombre.getText();
				String Seccion = textSeccion.getText();
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Curso set Nombre=?,Seccion=? where IDcurso=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Seccion);
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
				int id = Integer.parseInt(textCodigoCurso.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("delete from Curso where IDcurso=?");
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
	
	public void textFields() {
		
		textCodigoCurso = new JTextField();
		textCodigoCurso.setEditable(false);
        textCodigoCurso.setHorizontalAlignment(SwingConstants.CENTER);
        textCodigoCurso.setBounds(201, 33, 79, 20);
        panel.add(textCodigoCurso);
        textCodigoCurso.setColumns(10);
        
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
		 textNombre.setHorizontalAlignment(SwingConstants.CENTER);
	        textNombre.setBounds(10, 33, 182, 20);
	        textNombre.setColumns(10);
	        panel.add(textNombre);

		
	        textSeccion = new JTextField();
	        textSeccion.setBounds(290, 33, 169, 20);
	        textSeccion.setColumns(10);
	        panel.add(textSeccion);
		
		
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
					ps = con.prepareStatement("Select * from Curso where IDcurso=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigoCurso.setText(rs.getString(String.valueOf("IDcurso")));
						textNombre.setText(rs.getString("Nombre"));
						textSeccion.setText(rs.getString("Seccion"));
						
						}
					habilitarbotones();

				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}
			}
		});
		scrollPane.setViewportView(table);
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
		textCodigoCurso.setText("");
		textNombre.setText(" ");
		textSeccion.setText(" ");	
	}
	
	public void cargartabla(){
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
			ps = con.prepareStatement("Select * from Curso");
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
		modeloTabla.addColumn("Seccion");
		
		table.setModel(modeloTabla);

	}
}
