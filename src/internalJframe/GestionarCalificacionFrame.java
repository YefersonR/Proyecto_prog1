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

public class GestionarCalificacionFrame extends JInternalFrame implements Interface{
	
	private JPanel panel;
	private JTable table;
	
	private JTextField textCodigoCalificacion;
	private JButton btnInsertar,btnActualizar,btnEliminar;
	
	private JComboBox<String> comboAsignatura;
	private JTextField txtCalificacion;
	private JLabel lblCalificacion;
	private JComboBox<String> textNombre;
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
		setBackground(new Color(255, 255, 255));
        setBorder(new MatteBorder(5, 5, 5, 5, (Color) new Color(204, 164, 237)));
        setClosable(true);
        setTitle("Gestionar Calificaión");
        setBounds(0, 0, 763, 559);
        getContentPane().setLayout(null);
        
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 747, 529);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		comboAsignatura = new JComboBox<String>();
		comboAsignatura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select IDasignatura from Asignaturas");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						comboAsignatura	.addItem(rs.getString("IDasignatura"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No encontrado");	
				}
			}
		});
		comboAsignatura.addMouseListener(new MouseAdapter() {
			
		});
		comboAsignatura.setBounds(199, 32, 121, 22);
		panel.add(comboAsignatura);
		
		comboAsignatura.addItem(" ");
		
		labels();
		buttons();
		textFields();
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
					ps = con.prepareStatement("Select * from Calificaciones where IDcalificacion=?");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					while(rs.next()) {
						textCodigoCalificacion.setText(rs.getString(String.valueOf("IDcalificacion")));
						textNombre.getModel().setSelectedItem((rs.getString("Matricula")));
						comboAsignatura.getModel().setSelectedItem((rs.getString("IDasignatura")));
						txtCalificacion.setText(rs.getString("Calificacion"));
						}
					habilitarbotones();
					
				}catch(SQLException a) {
					JOptionPane.showMessageDialog(null, a.toString());
				}
			}
		});
		scrollPane.setViewportView(table);
		
		txtCalificacion = new JTextField();
		txtCalificacion.setHorizontalAlignment(SwingConstants.CENTER);
		txtCalificacion.setColumns(10);
		txtCalificacion.setBounds(341, 33, 130, 20);
		panel.add(txtCalificacion);
		
		lblCalificacion = new JLabel("Calificacion");
		lblCalificacion.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblCalificacion.setBounds(358, 11, 93, 20);
		panel.add(lblCalificacion);
		
		textNombre = new JComboBox<String>();
		textNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Select IDcurso as Curso from Curso");
					ResultSet rs = ps.executeQuery();

					while(rs.next()) {
						textNombre.addItem(rs.getString("Curso"));
						}

					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No encontrado");	
				}
			}
		});
		textNombre.setBounds(39, 32, 150, 22);
		panel.add(textNombre);
		textNombre.addItem(" ");
	}
	
	public void labels() {
		
        JLabel lblNombre = new JLabel("Matricula Alumno");
        lblNombre.setBounds(47, 11, 121, 20);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(lblNombre);
		
        JLabel lblAsignatura = new JLabel("Asignatura");
        lblAsignatura.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblAsignatura.setBounds(216, 11, 69, 20);
       panel.add( lblAsignatura);	
	}
	
	public void buttons() {
		
		btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int matricula= Integer.parseInt(comboAsignatura.getSelectedItem().toString());
				String Asignatura = comboAsignatura.getSelectedItem().toString();
				int Calificacion =Integer.parseInt(txtCalificacion.getText());
				
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("Insert into Calificaciones(IDasignatura,Matricula,Calificacion) values(?,?,?) ");
					
					ps.setInt(1, matricula);
					ps.setString(2, Asignatura);
					ps.setInt(3, Calificacion);
					
					
					ps.executeUpdate();
					JOptionPane.showMessageDialog(null,"Registro insertado");
					cargartabla();
					
					
				}catch(SQLException e1) 
				{
					JOptionPane.showMessageDialog(null,"Registro No insertado");	
				}
			}
		});
		btnInsertar.setBounds(39, 71, 150, 30);
		btnInsertar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnInsertar);
		
		btnActualizar = new JButton("Actualizar");
		btnActualizar.setEnabled(false);
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int id  = Integer.parseInt(textCodigoCalificacion.getText());
				int matricula= Integer.parseInt(textNombre.getSelectedItem().toString());
				String Asignatura = comboAsignatura.getSelectedItem().toString();
				String Calificacion =txtCalificacion.getText();
				try {
					Connection con = ConexionDB.getConnection();
					PreparedStatement ps = con.prepareStatement("update Calificaciones set IDasignatura=?,Matricula=?,Calificacion=? where IDcalificacion=? ");
					
					ps.setString(1, Asignatura);
					ps.setInt(2, matricula);
					ps.setString(3, Calificacion);
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
		btnActualizar.setBounds(197, 71, 150, 30);
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
		btnEliminar.setBounds(357, 71, 150, 30);
		btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnEliminar);
		
	}
	
	public void textFields() {
		
		textCodigoCalificacion = new JTextField();
		textCodigoCalificacion.setHorizontalAlignment(SwingConstants.CENTER);
	    textCodigoCalificacion.setBounds(499, 33, 79, 20);
	    panel.add(textCodigoCalificacion);
	    textCodigoCalificacion.setColumns(10);
	    textCodigoCalificacion.setVisible(false);
		
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
		textNombre.getModel().setSelectedItem("");
		comboAsignatura.getModel().setSelectedItem("");
		txtCalificacion.setText("");
				
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
			limpiar();
		}catch(SQLException e2) {
			JOptionPane.showMessageDialog(null, e2.toString());
		}
	}

	
	public void mostrarDatos(){
	
		DefaultTableModel modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("IDcalificacion");
		modeloTabla.addColumn("Asignatura");
		modeloTabla.addColumn("Matricula");
		modeloTabla.addColumn("Calificacion");
		
		table.setModel(modeloTabla);
	}
	
}
