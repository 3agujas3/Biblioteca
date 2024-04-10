package com.miapp.biblioteca.ui;

import com.miapp.biblioteca.Libro;
import com.miapp.biblioteca.Usuario;
import com.miapp.biblioteca.db.SQLiteConexion;
import com.miapp.biblioteca.service.LibroServicio;
import com.miapp.biblioteca.service.UsuarioServicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;


public class MainGUI extends JFrame implements ActionListener {
    private JButton crearLibroButton, actualizarLibroButton, buscarPorISBNButton, buscarPorTituloButton,
            listarLibrosButton, eliminarLibroButton, crearUsuarioButton, eliminarUsuarioButton,
            prestamosButton, salirButton,buscarUsuarioPorIdButton,devolverLibroButton,verLibrosPrestadosButton,verLibrosPrestadosGetButton;
    private JTextArea outputArea;
    private LibroServicio libroServicio;
    private UsuarioServicio usuarioServicio;
    private Connection connection; // 
    private JButton limpiarButton; // 

    public MainGUI() {
        try {
            connection = SQLiteConexion.getConnection();
            libroServicio = new LibroServicio(connection);
            usuarioServicio = new UsuarioServicio(connection, libroServicio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
            


        // Configuración de la ventana
        setTitle("Biblioteca Virtual");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear y configurar los componentes
        crearLibroButton = new JButton("Crear Libro");
        actualizarLibroButton = new JButton("Actualizar Libro");
        buscarPorISBNButton = new JButton("Buscar por ISBN");
        buscarPorTituloButton = new JButton("Buscar por Título");
        listarLibrosButton = new JButton("Listar Libros");
        eliminarLibroButton = new JButton("Eliminar Libro");
        crearUsuarioButton = new JButton("Crear Usuario");
        eliminarUsuarioButton = new JButton("Eliminar Usuario");
        prestamosButton = new JButton("Prestamos");
        salirButton = new JButton("Salir");
        limpiarButton = new JButton("Limpiar"); // Inicialización del botón
        buscarUsuarioPorIdButton = new JButton("Buscar Usuario por ID");
        prestamosButton = new JButton("Prestar Libro");
        devolverLibroButton = new JButton("Devolver Libro");
        verLibrosPrestadosButton = new JButton("Ver Libros Prestados");
        verLibrosPrestadosGetButton = new JButton("Ver Libros Prestados Get");

        JPanel panel = new JPanel(new GridLayout(10, 1));
        panel.add(crearLibroButton);
        panel.add(actualizarLibroButton);
        panel.add(buscarPorISBNButton);
        panel.add(buscarPorTituloButton);
        panel.add(listarLibrosButton);
        panel.add(eliminarLibroButton);
        panel.add(crearUsuarioButton);
        panel.add(buscarUsuarioPorIdButton);
        panel.add(eliminarUsuarioButton);
        panel.add(limpiarButton); // Agregar el botón al panel
        panel.add(prestamosButton);
        panel.add(devolverLibroButton);
        panel.add(verLibrosPrestadosButton);
        panel.add(salirButton);

        //panel.add(verLibrosPrestadosGetButton);
        
        

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Organizar los componentes en la ventana
        Container contentPane = getContentPane();
        contentPane.add(panel, BorderLayout.WEST);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Asignar manejadores de eventos
        crearLibroButton.addActionListener(this);
        actualizarLibroButton.addActionListener(this);
        buscarPorISBNButton.addActionListener(this);
        buscarPorTituloButton.addActionListener(this);
        listarLibrosButton.addActionListener(this);
        eliminarLibroButton.addActionListener(this);
        crearUsuarioButton.addActionListener(this);
        buscarUsuarioPorIdButton.addActionListener(this);
        eliminarUsuarioButton.addActionListener(this);
        prestamosButton.addActionListener(this);
        salirButton.addActionListener(this);
        limpiarButton.addActionListener(this); // Asignar el manejador de eventos
        devolverLibroButton.addActionListener(this);
        verLibrosPrestadosButton.addActionListener(this);
        verLibrosPrestadosGetButton.addActionListener(this);
        

        // Mostrar la ventana
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    	try {
        if (e.getSource() == crearLibroButton) {
            String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro:");
            String autor = JOptionPane.showInputDialog(this, "Ingrese el autor del libro:");
            String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro:");
            String genero = JOptionPane.showInputDialog(this, "Ingrese el género del libro:");

            if (titulo != null && autor != null && isbn != null && genero != null) {
            	libroServicio.crearLibro(isbn, titulo, autor, genero);
                outputArea.append("Libro creado: " + titulo + "\n");
            }
        
        
        } else if (e.getSource() == actualizarLibroButton) {
            String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro a actualizar:");
            if (isbn != null) {
                String nuevoTitulo = JOptionPane.showInputDialog(this, "Ingrese el nuevo título:");
                String nuevoAutor = JOptionPane.showInputDialog(this, "Ingrese el nuevo autor:");
                String nuevoGenero = JOptionPane.showInputDialog(this, "Ingrese el nuevo género:");

                if (nuevoTitulo != null && nuevoAutor != null && nuevoGenero != null) {
                	libroServicio.actualizarLibro(isbn, nuevoTitulo, nuevoAutor, nuevoGenero);
                    outputArea.append("Libro actualizado: " + isbn + "\n");
                }
            }
        
        
        }else if (e.getSource() == verLibrosPrestadosGetButton) {
            // Coloca aquí el código relacionado con el botón verLibrosPrestadosButton
        	int idUsuario = Integer.parseInt(JOptionPane.showInputDialog(MainGUI.this, "Ingrese el ID del usuario para ver sus libros prestados:"));
            Usuario usuario = usuarioServicio.buscarUsuarioPorId(idUsuario);
            if (usuario != null) {
                ArrayList<Libro> librosPrestados = usuario.getLibrosPrestados();
                if (!librosPrestados.isEmpty()) {
                    outputArea.append("Libros prestados al usuario " + usuario.getNombre() + ":\n");
                    for (Libro libro : librosPrestados) {
                        outputArea.append(libro.getTitulo() + " (" + libro.getISBN() + ")\n");
                    }
                } else {
                    outputArea.append("El usuario " + usuario.getNombre() + " no tiene libros prestados.\n");
                }
            } else {
                outputArea.append("No se encontró ningún usuario con ese ID.\n");
            }
            
            
        }else if (e.getSource() == limpiarButton) { // Verificar si se hizo clic en el botón "Limpiar"
            outputArea.setText(""); // Limpiar el contenido del área de salida
        
        }else if (e.getSource() == buscarPorISBNButton) {
            String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro a buscar:");
            if (isbn != null) {
                Libro libro = libroServicio.buscarLibroPorISBN(isbn);
                if (libro != null) {
                    outputArea.append("Libro encontrado: " + libro.getTitulo() + "\n");
                } else {
                    outputArea.append("No se encontró el libro.\n");
                }
            }
        
        
        } else if (e.getSource() == buscarPorTituloButton) {
            String titulo = JOptionPane.showInputDialog(this, "Ingrese el título del libro a buscar:");
            if (titulo != null) {
                ArrayList<Libro> librosPorTitulo = libroServicio.buscarLibrosPorTitulo(titulo);
                if (!librosPorTitulo.isEmpty()) {
                    outputArea.append("Libros encontrados:\n");
                    for (Libro libro : librosPorTitulo) {
                        outputArea.append(libro.getTitulo() + "\n");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron libros con ese título.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
           
        } else if (e.getSource() == listarLibrosButton) {
            ArrayList<Libro> listaLibros = libroServicio.obtenerTodosLosLibros();
            outputArea.append("Listado de libros:\n");
            for (Libro libro : listaLibros) {
                outputArea.append(libro.getTitulo() + " (" + libro.getISBN() + ")\n");
            }
        
        
        } else if (e.getSource() == eliminarLibroButton) {
            String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro a eliminar:");
            if (isbn != null) {
            	libroServicio.eliminarLibro(isbn);
                outputArea.append("Libro eliminado: " + isbn + "\n");
            }
        

        }else if (e.getSource() == prestamosButton) {
            String idUsuarioStr = JOptionPane.showInputDialog(this, "Ingrese el ID del usuario:");
            if (idUsuarioStr != null && !idUsuarioStr.isEmpty()) {
                int idUsuario = Integer.parseInt(idUsuarioStr);
                Usuario usuarioPrestamo = usuarioServicio.buscarUsuarioPorId(idUsuario);
                if (usuarioPrestamo != null) {
                    String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro:");
                    if (isbn != null && !isbn.isEmpty()) {
                        Libro libroPrestamo = libroServicio.buscarLibroPorISBN(isbn);
                        if (libroPrestamo != null) {
                            if (libroPrestamo.isDisponible()) {
                                usuarioServicio.prestarLibro(usuarioPrestamo, libroPrestamo);
                                outputArea.append("Libro prestado a " + usuarioPrestamo.getNombre() + " (ID: " + usuarioPrestamo.getId() + "): " + libroPrestamo.getTitulo() + " (ISBN: " + libroPrestamo.getISBN() + ")\n");
                            } else {
                                JOptionPane.showMessageDialog(this, "El libro no está disponible para préstamo.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No se encontró el libro con ese ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el usuario con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        
        
       
            
        }else if (e.getSource() == devolverLibroButton) {
            String isbn = JOptionPane.showInputDialog(this, "Ingrese el ISBN del libro a devolver:");
            if (isbn != null) {
                try {
                    // Obtener el usuario actual por su ID
                    int idUsuario = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el ID del usuario actual:"));
                    Usuario usuarioDev = usuarioServicio.buscarUsuarioPorId(idUsuario);

                    if (usuarioDev != null) {
                    	
                        // Buscar el libro por su ISBN
                        Libro libroDev = libroServicio.buscarLibroPorISBN(isbn);

                        if (libroDev != null) {
                        	System.out.println("Entro en if1");
                            // Realizar la devolución del libro si está prestado por el usuario actual
                            if (usuarioServicio.verificarPrestamo(usuarioDev, libroDev)) {
                            	
                                usuarioServicio.devolverLibro(usuarioDev, libroDev);
                                outputArea.append("Libro devuelto: " + libroDev.getTitulo() + "\n");
                            } else {
                                JOptionPane.showMessageDialog(this, "Este libro no fue prestado a este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No existe el libro (ISBN).", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró el usuario (IdUsuario).", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese un ID de usuario válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al devolver el libro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } else if (e.getSource() == crearUsuarioButton) {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del usuario:");
            if (nombre != null && !nombre.isEmpty()) {
                int id = usuarioServicio.crearUsuario(nombre);
                if (id != -1) {
                    outputArea.append("Usuario creado: " + nombre + " (ID: " + id + ")\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El nombre del usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        
        
        } else if (e.getSource() == eliminarUsuarioButton) {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el ID del usuario a eliminar:"));
            usuarioServicio.eliminarUsuario(id);
            outputArea.append("Usuario eliminado: ID " + id + "\n");
            
            
        }else if (e.getSource() == buscarUsuarioPorIdButton) {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el ID del usuario a buscar:"));
                Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
                if (usuario != null) {
                    outputArea.append("Usuario encontrado: " + usuario.getNombre() + " (ID: " + usuario.getId() + ")\n");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
        }  else if (e.getSource() == verLibrosPrestadosButton) {
            // Obtener el ID del usuario
            int idUsuario = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el ID del usuario para ver sus libros prestados:"));
            Usuario usuario = usuarioServicio.buscarUsuarioPorId(idUsuario);
            if (usuario != null) {
                ArrayList<Libro> librosPrestados = usuarioServicio.obtenerLibrosPrestados(usuario);
                if (!librosPrestados.isEmpty()) {
                    outputArea.append("Libros prestados al usuario " + usuario.getNombre() + ":\n");
                    for (Libro libro : librosPrestados) {
                        outputArea.append(libro.getTitulo() + " (" + libro.getISBN() + ")\n");
                    }
                } else {
                    outputArea.append("El usuario " + usuario.getNombre() + " no tiene libros prestados.\n");
                }
            } else {
                outputArea.append("No se encontró ningún usuario con ese ID.\n");
            }
            
            
        }else if (e.getSource() == salirButton) {
            dispose();
        }
    	} catch (Exception ex) {
            // Manejar cualquier excepción no controlada
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI();
            }
        });
    }
    // Cerrar la conexión al salir de la aplicación
    @Override
    public void dispose() {
        super.dispose();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
        	JOptionPane.showMessageDialog(this, "Error al cerrar la conexión con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}