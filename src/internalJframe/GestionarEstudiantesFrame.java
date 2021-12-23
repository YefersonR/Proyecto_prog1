package internalJframe;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
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
import index.Interface;

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

public class GestionarEstudiantesFrame extends JInternalFrame implements Interface {
	
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
	private JComboBox<String> comboSexo,combocurso;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	private JLabel lblCurso;

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
		setBackground(new Color(255, 255, 255));
        setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 164, 237)));
        setClosable(true);
        setTitle("Gestionar Estudiantes");
        setBounds(0, 0, 763, 559);
        getContentPane().setLayout(null);
        
        panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(0, 0, 754, 529);
        getContentPane().add(panel);
        panel.setLayout(null);
		
		fecha = new JDateChooser();
		fecha.setBounds(10, 85, 182, 20);
    	panel.add(fecha);
		
	
		comboSexo = new JComboBox<String>();
		comboSexo.setBounds(496, 32, 47, 22);
		panel.add(comboSexo);
		
		comboSexo.addItem(" ");
		comboSexo.addItem("M");
		comboSexo.addItem("F");

		Labels();
		textFields();
		buttons();
		table();
		mostrarDatos();
		//cargartabla();

		

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
					ps = con.prepareStatement("Select * from Alumnos where Matricula=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textMatricula.setText(rs.getString(String.valueOf("Matricula")));
						textNombre.setText(rs.getString("Nombre"));
						comboSexo.getModel().setSelectedItem((rs.getString("Sexo")));
						fecha.setToolTipText(rs.getString("Fecha_Nacimiento"));
						textTelefono.setText(rs.getString("Telefono"));
						textEmail.setText(rs.getString("Email"));
						textDireccion.setText(rs.getString("Direccion"));
						combocurso.getModel().setSelectedItem((rs.getString("IDcurso")));
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}

			}
		});
		scrollPane.setViewportView(table);
		
		lblCurso = new JLabel("Curso");
		lblCurso.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblCurso.setBounds(447, 85, 47, 20);
		panel.add(lblCurso);
		
		combocurso = new JComboBox<String>();
		combocurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select IDcurso as Curso from Curso");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						combocurso.addItem(rs.getString("Curso"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No encontrado");	
				}
			}
		});
		combocurso.setBounds(496, 85, 47, 22);
		panel.add(combocurso);
		combocurso.addItem(" ");
	}
	public void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
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
				int idcurso = Integer.parseInt(combocurso.getSelectedItem().toString());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Alumnos(Nombre,Sexo,Fecha_Nacimiento,Telefono,Email,Direccion,IDcurso) values(?,?,?,?,?,?,?)");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
					ps.setString(3, Fecha_Nacimiento);
					ps.setString(4, Telefono);
					ps.setString(5, Email);
					ps.setString(6, Direccion);
					ps.setInt(7, idcurso);
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro insertado");
					cargartabla();
					
					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No insertado");	
				}

			}
		});

        btnInsertar.setBounds(563, 24, 184, 30);
        btnInsertar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        panel.add(btnInsertar);

		
		btnActualizar = new JButton("Actualizar");
		btnActualizar.setEnabled(false);
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int Matricula  = Integer.parseInt(textMatricula.getText());
				String Nombre =textNombre.getText();
				String Sexo = comboSexo.getSelectedItem().toString();
				String Telefono = textTelefono.getText();
				String Email =textEmail.getText();
				String Direccion = textDireccion.getText();
				int idcurso = Integer.parseInt(combocurso.getSelectedItem().toString());
								 
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Alumnos set Nombre=?,Sexo=?,Telefono=?,Email=?,Direccion=?, IDcurso=? where Matricula=? ");
					
					ps.setString(1, Nombre);
					ps.setString(2, Sexo);
					ps.setString(3, Telefono);
					ps.setString(4, Email);
					ps.setString(5, Direccion);
					ps.setInt(6, idcurso);
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

	     btnActualizar.setBounds(563, 60, 184, 30);
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

        btnEliminar.setBounds(563, 95, 184, 32);
        btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        panel.add(btnEliminar);
		
	}
	public void textFields() {
		
		textMatricula = new JTextField();
		textMatricula.setEditable(false);
		textMatricula.setBounds(201, 33, 79, 20);
		panel.add(textMatricula);
		textMatricula.setColumns(10);
		textMatricula.setHorizontalAlignment(SwingConstants.CENTER);
	        
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
					ps = con.prepareStatement("Select * from Alumnos where Nombre like '%"+nombre+ "%'");
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
		
        textTelefono = new JTextField();
        textTelefono.setHorizontalAlignment(SwingConstants.CENTER);
        textTelefono.setBounds(202, 85, 78, 20);
        textTelefono.setColumns(10);
        panel.add(textTelefono);
        
        textDireccion = new JTextField();
        textDireccion.setHorizontalAlignment(SwingConstants.CENTER);
        textDireccion.setBounds(290, 32, 149, 20);
        textDireccion.setColumns(10);
        panel.add(textDireccion);

        textEmail = new JTextField();
        textEmail.setHorizontalAlignment(SwingConstants.CENTER);
        textEmail.setBounds(290, 85, 149, 20);
        textEmail.setColumns(10);
        panel.add(textEmail);
		
	}
	public void Labels() {
		JLabel lblNewLabel = new JLabel("Matr\u00EDcula");
        lblNewLabel.setBounds(210, 11, 60, 20);
        lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNewLabel);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(71, 11, 60, 20);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNombre);

        JLabel lblFecha = new JLabel("Fecha de nacimiento");
        lblFecha.setBounds(32, 65, 142, 20);
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblFecha);

        JLabel lblSexo = new JLabel("Sexo");
        lblSexo.setBounds(447, 31, 39, 20);
        lblSexo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblSexo);

        JLabel lbltelefono = new JLabel("Tel\u00E9fono ");
        lbltelefono.setBounds(210, 65, 60, 20);
        lbltelefono.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lbltelefono);

        lblDireccion = new JLabel("Direcci\u00F3n ");
        lblDireccion.setBounds(331, 10, 67, 20);
        lblDireccion.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblDireccion);

        lblEmail = new JLabel("E-mail");
        lblEmail.setBounds(341, 63, 47, 20);
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblEmail);
		
		
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
		textMatricula.setText("");
		textNombre.setText("");
		comboSexo.getModel().setSelectedItem(" ");
		fecha.setToolTipText("");
		textTelefono.setText("");
		textEmail.setText("");
		textDireccion.setText("");		
		combocurso.getModel().setSelectedItem(" ");
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
	public void mostrarDatos(){
		
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Matricula");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("Sexo");
		modeloTabla.addColumn("Fecha_Nacimiento");
		modeloTabla.addColumn("Direccion");
		modeloTabla.addColumn("Email");
		modeloTabla.addColumn("Telefono");
		modeloTabla.addColumn("IDcurso");
		
		table.setModel(modeloTabla);
		
	}

	@Override
	public void labels() {
		// TODO Auto-generated method stub
		
	}
}
