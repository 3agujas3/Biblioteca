package com.miapp.biblioteca.service;
import com.miapp.biblioteca.Usuario;

import com.miapp.biblioteca.Libro;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class UsuarioServicio {
	
    private Connection connection;
    private LibroServicio libroServicio; // Agregar referencia al servicio de libros
    private ArrayList<Usuario> usuarios;

    public UsuarioServicio(Connection connection, LibroServicio libroServicio) {
        this.usuarios=new ArrayList<>();
    	this.connection = connection;
        this.libroServicio = libroServicio;
    }

    public int crearUsuario(String nombre) throws SQLException {
        String sql = "INSERT INTO Usuarios (Nombre) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nombre);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Usuario nuevoUsuario= new Usuario(nombre,generatedKeys.getInt(1));
                    usuarios.add(nuevoUsuario);
                    return generatedKeys.getInt(1); // Devuelve el ID del usuario recién creado
                }
            }

            
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        return -1; // Si no se puede crear el usuario, devuelve -1
    }

    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                int id = resultSet.getInt("ID");
                Usuario usuario = new Usuario(nombre, id);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                return new Usuario(nombre, id);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    public void eliminarUsuario(int id) {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
   
    
    public void prestarLibro(Usuario usuario, Libro libro) {
        try {
            if (libro.isDisponible()) {
                // Agregar el libro a la lista de libros prestados del usuario
                usuario.getLibrosPrestados().add(libro);
                libro.setDisponible(false);
                
                // Actualizar la disponibilidad del libro en la base de datos
                String updateQuery = "UPDATE Libros SET Disponible = 0 WHERE ISBN = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, libro.getISBN());
                    updateStatement.executeUpdate();
                }
                
                // Insertar el préstamo en la tabla de préstamos
                String insertQuery = "INSERT INTO Prestamos (UsuarioID, LibroISBN, FechaPrestamo, Estado) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, usuario.getId());
                    insertStatement.setString(2, libro.getISBN());
                    insertStatement.setDate(3, new Date(System.currentTimeMillis())); // Fecha actual
                    insertStatement.setInt(4, 1);
                    insertStatement.executeUpdate();
                }
                
                System.out.println("Libro prestado correctamente.");
            } else {
                System.out.println("El libro no está disponible para préstamo.");
            }
        } catch (SQLException e) {
            System.err.println("Error al procesar el préstamo: " + e.getMessage());
        }
    }
 
    
    
    
    public void devolverLibro(Usuario usuario, Libro libro) {
        
        	System.out.println("Entro en if");
            usuario.getLibrosPrestados().remove(libro);
            libro.setDisponible(true);
            try {
                // Actualizar la disponibilidad del libro en la base de datos
                String updateLibroSQL = "UPDATE Libros SET Disponible = 1 WHERE ISBN = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateLibroSQL)) {
                    statement.setString(1, libro.getISBN());
                    statement.executeUpdate();
                }
                // Actualizar la disponibilidad del libro en la base de datos
                String updateQuery = "UPDATE Prestamos SET Estado = 0 WHERE LibroISBN = ? AND UsuarioID = ? ";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, libro.getISBN());
                    updateStatement.setInt(2, usuario.getId());
                    updateStatement.executeUpdate();
                }
                System.out.println("Libro devuelto exitosamente.");
            } catch (SQLException e) {
                System.err.println("Error al devolver el libro: " + e.getMessage());
            }
 
    }
    
    public ArrayList<Libro> obtenerLibrosPrestados(Usuario usuario) {
        ArrayList<Libro> librosPrestados = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos WHERE UsuarioID = ? AND Estado = 1";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuario.getId());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String isbn = resultSet.getString("LibroISBN");
                    System.out.println("ISBN: " + isbn);
                    Libro libro = libroServicio.buscarLibroPorISBN(isbn);
                    if (libro != null) {
                        librosPrestados.add(libro);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros prestados: " + e.getMessage());
            // Decide si quieres propagar la excepción o manejarla aquí
        }
        
        return librosPrestados;
    }
    
    
    public Libro obtenerLibroPrestado(Usuario usuario, Libro libro) {
        String sql = "SELECT * FROM Prestamos WHERE Estado = 1 AND UsuarioID = ? AND LibroISBN = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuario.getId());
            statement.setString(2, libro.getISBN());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                
                    String isbn = resultSet.getString("LibroISBN");
                    System.out.println("ISBN: " + isbn);
                    if(isbn==libro.getISBN()) {
                    	return libro;
                    }
                    else {
                    	System.out.println("libro no encontrado");
                    }
                    
                    }
               
        } catch (SQLException e) {
            System.err.println("Error al obtener libro prestado: " + e.getMessage());
            // Decide si quieres propagar la excepción o manejarla aquí
        }
        
        libro=null;
        return libro;
        
    }
    
   
    
    public boolean verificarPrestamo(Usuario usuario, Libro libro) {
    	
        Libro libroPrestado = obtenerLibroPrestado(usuario,libro);   
        if(libroPrestado!=null) {
        	return true;
        }
        else {
        	return false;
        }
    }
}