package index;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import conexionConBase.ConexionDB;
import internalJframe.GestionarAsignaturasFrame;
import internalJframe.GestionarCalificacionFrame;
import internalJframe.GestionarCursosFrame;
import internalJframe.GestionarEstudiantesFrame;
import internalJframe.GestionarProfesoresFrame;
import internalJframe.Informacion;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;


//import conexionConBase.Conexion;

public class APP {

	private JFrame frmSistemaDeEscuela;
	private GestionarEstudiantesFrame Estudiantes;
	private GestionarProfesoresFrame Profesores;
	private GestionarAsignaturasFrame Asignaturas;
	private GestionarCursosFrame Cursos;
	private GestionarCalificacionFrame Calificacion;
	private JDesktopPane desktopPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {	
			public void run() {
				try {
					APP window = new APP();
					window.frmSistemaDeEscuela.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public APP() {
		BaseDeDatos();
		initialize();
		DesktopPane();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSistemaDeEscuela = new JFrame();
		frmSistemaDeEscuela.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\eclipse-workspace\\Proyecto_Final_Programacion_1\\Icono_del_programa.png"));
		frmSistemaDeEscuela.setBackground(Color.LIGHT_GRAY);
		frmSistemaDeEscuela.setTitle("Sistema de Escuela");
		frmSistemaDeEscuela.setResizable(false);
		frmSistemaDeEscuela.setBounds(100, 100, 779, 625);
		frmSistemaDeEscuela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSistemaDeEscuela.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(224,194,248));
		menuBar.setBounds(0, 0, 773, 28);
		frmSistemaDeEscuela.getContentPane().add(menuBar);
		
		JMenu mnEstudiantes = new JMenu("Estudiantes");
		menuBar.add(mnEstudiantes);
		
		JMenuItem mntmGestionarEstudiantes = new JMenuItem("Gestionar Estudiantes");
		mntmGestionarEstudiantes.setBackground(new Color(196, 155, 229));
		mntmGestionarEstudiantes.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Estudiantes = new GestionarEstudiantesFrame();
				desktopPane.add(Estudiantes);
				Estudiantes.setVisible(true);
				
			}
			
		});
		
		mnEstudiantes.add(mntmGestionarEstudiantes);
		
		JMenu mnProfesores = new JMenu("Profesores");
		menuBar.add(mnProfesores);
		
		JMenuItem mntmGestionarProfesores = new JMenuItem("Gestionar Profesores");
        mntmGestionarProfesores.setBackground(new Color(196, 155, 229));
		mntmGestionarProfesores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Profesores = new GestionarProfesoresFrame();
				desktopPane.add(Profesores);
				Profesores.setVisible(true);
				
			}
		});
		mnProfesores.add(mntmGestionarProfesores);
		
		JMenu mnAsignaturas = new JMenu("Asignaturas");
		menuBar.add(mnAsignaturas);
		
		JMenuItem mntmGestionarAsignaturas = new JMenuItem("Gestionar Asignaturas");
        mntmGestionarAsignaturas.setBackground(new Color(196, 155, 229));
		mntmGestionarAsignaturas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Asignaturas = new GestionarAsignaturasFrame();
				desktopPane.add(Asignaturas);
				Asignaturas.setVisible(true);
				
			}
		});
		mnAsignaturas.add(mntmGestionarAsignaturas);
		
		JMenu mnCursos = new JMenu("Cursos");
		menuBar.add(mnCursos);
		
		JMenuItem mntmGestionarCursos = new JMenuItem("Gestionar Cursos");
		mntmGestionarCursos.setBackground(new Color(196, 155, 229));
		mntmGestionarCursos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Cursos = new GestionarCursosFrame();
				desktopPane.add(Cursos);
				Cursos.setVisible(true);
				
			}
		});
		mnCursos.add(mntmGestionarCursos);
		
		JMenu mnCalificaciones = new JMenu("Calificaciones");
		menuBar.add(mnCalificaciones);
		
		JMenuItem mntmGestionarcali = new JMenuItem("Gestionar calificacion ");
		mntmGestionarcali.setBackground(new Color(196, 155, 229));
		mntmGestionarcali.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			Calificacion = new GestionarCalificacionFrame();
			desktopPane.add(Calificacion);
			Calificacion.setVisible(true);
				
			}
		});
		mnCalificaciones.add(mntmGestionarcali);
		

		
	}
	
	private void DesktopPane() {
		
		desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 27, 773, 569);
		frmSistemaDeEscuela.getContentPane().add(desktopPane);
		JLabel ImagenPrincipal = new JLabel("");
        ImagenPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        ImagenPrincipal.setIcon(new ImageIcon("C:\\Users\\user\\eclipse-workspace\\Proyecto_Final_Programacion_1\\Icono_del_programa.png"));
        ImagenPrincipal.setBounds(10, 0, 753, 558);
        ImagenPrincipal.setBackground(new Color(204, 164, 237));
        desktopPane.add(ImagenPrincipal);
        desktopPane.setBackground(new Color(238,221,251));
    
	}
	private void BaseDeDatos() {
		
		Connection conexion = ConexionDB.getConnection();
		PreparedStatement Declaracion;
		
	}
}
