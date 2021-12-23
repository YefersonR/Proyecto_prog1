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
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import conexionConBase.ConexionDB;
import index.Interface;

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
import java.awt.Color;

public class GestionarAsignaturasFrame extends JInternalFrame implements Interface{
	
	private JPanel panel;
	private JTextField textCodigoMateria;
	private JTextField textNombre;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	
	private JComboBox<String> comboCurso;
	
	private JTable table;
	private JComboBox<String> comboprofe;
	
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
		setBackground(new Color(255, 255, 255));
        setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 164, 237)));
        setClosable(true);
        setTitle("Gestionar Asignaturas");
        setBounds(0, 0, 763, 559);
        getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		/// ComboBox
		comboCurso = new JComboBox<String>();
		comboCurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select IDcurso as Curso from Curso");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboCurso.addItem(rs.getString("Curso"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No encontrado");	
				}
			}
		});
		comboCurso.addMouseListener(new MouseAdapter() {
		});
		comboCurso.setBounds(446, 32, 59, 22);
		panel.add(comboCurso);
			
		comboCurso.addItem(" ");
		/// ComboBox
		
		
		labels();
		textFields();
		table();
		buttons();
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
					ps = con.prepareStatement("Select * from Asignaturas where IDasignatura=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigoMateria.setText(rs.getString(String.valueOf("IDasignatura")));
						textNombre.setText(rs.getString("Nombre"));
						comboCurso.getModel().setSelectedItem((rs.getString(String.valueOf("IDcurso"))));
						comboprofe.getModel().setSelectedItem(rs.getString(String.valueOf("IDprofesor")));
						
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}
			}
		});
		scrollPane.setViewportView(table);
	}
	
	
	public void labels() {
		
		JLabel lblCodigoMateria = new JLabel("Codigo");
		lblCodigoMateria.setBounds(217, 11, 46, 20);
		lblCodigoMateria.setFont(new Font("SansSerif", Font.PLAIN, 15));
	    panel.add(lblCodigoMateria);

	    JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(71, 11, 60, 20);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNombre);

        JLabel lblProfesor = new JLabel("Profesor");
        lblProfesor.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblProfesor.setBounds(340, 11, 69, 20);
        panel.add(lblProfesor);

        JLabel lblCursos = new JLabel("Cursos");
        lblCursos.setBounds(446, 11, 59, 20);
        lblCursos.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblCursos);
		
	}

	public void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Nombre =textNombre.getText();
				int Profesor = Integer.parseInt(comboprofe.getSelectedItem().toString());
				int Cursos = Integer.parseInt(comboCurso.getSelectedItem().toString());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Asignaturas(Nombre,IDcurso,IDprofesor) values(?,?,?) ");
					
					ps.setString(1, Nombre);
					ps.setInt(2, Cursos);					
					ps.setInt(3, Profesor);
					
					
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
				int Cursos = Integer.parseInt(comboCurso.getSelectedItem().toString());
				int Profesor = Integer.parseInt(comboprofe.getSelectedItem().toString());
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Asignaturas set Nombre=?,IDcurso=?,IDprofesor=? where IDasignatura=?");
					
					ps.setString(1, Nombre);
					ps.setInt(2, Cursos);					
					ps.setInt(3, Profesor);
					ps.setInt(4, id);	
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro Actualizado");
					cargartabla();
					habilitarbotones();
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No Actualizado");	
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
		
		comboprofe = new JComboBox<String>();
		comboprofe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select Codigo from Profesores");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboprofe.addItem(rs.getString("Codigo"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No encontrado");	
				}	
			
			}
		});
		comboprofe.setBounds(334, 32, 59, 22);
		panel.add(comboprofe);
		
		comboprofe.addItem(" ");
	}
	
	public void textFields() {
		
		textCodigoMateria = new JTextField();
		textCodigoMateria.setEditable(false);
		textCodigoMateria.setHorizontalAlignment(SwingConstants.CENTER);
        textCodigoMateria.setBounds(201, 33, 79, 20);
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
        textNombre.setHorizontalAlignment(SwingConstants.CENTER);
        textNombre.setBounds(10, 33, 182, 20);
        textNombre.setColumns(10);
        panel.add(textNombre);
		
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
		textCodigoMateria.setText("");
		textNombre.setText("");
		comboCurso.getModel().setSelectedItem("");
		comboprofe.getModel().setSelectedItem("");
		
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
			limpiar();
		}catch(SQLException e2) {
			JOptionPane.showMessageDialog(null, e2.toString());
		}
	}
	public void mostrarDatos(){
		
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("IDasignatura");
		modeloTabla.addColumn("Nombre");
		modeloTabla.addColumn("IDcurso");
		modeloTabla.addColumn("IDprofesor");
		
		
		table.setModel(modeloTabla);
		
	}
	
	
	
}
