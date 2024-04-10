package com.miapp.biblioteca.service;

import com.miapp.biblioteca.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibroServicio {
    
    private Connection connection;
    
    public LibroServicio(Connection connection) {
        this.connection = connection;
    }
    
    // Método para crear un nuevo libro
    public void crearLibro(String isbn, String titulo, String autor, String genero) throws SQLException {
        String sql = "INSERT INTO Libros (ISBN, Titulo, Autor, Genero, Disponible) VALUES (?, ?, ?, ?, 1)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.setString(2, titulo);
            statement.setString(3, autor);
            statement.setString(4, genero);
            statement.executeUpdate();
        }
    }

    // Método para obtener todos los libros
    public ArrayList<Libro> obtenerTodosLosLibros() throws SQLException {
        ArrayList<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Libro libro = new Libro();
                libro.setISBN(resultSet.getString("ISBN"));
                libro.setTitulo(resultSet.getString("Titulo"));
                libro.setAutor(resultSet.getString("Autor"));
                libro.setGenero(resultSet.getString("Genero"));
                libro.setDisponible(resultSet.getBoolean("Disponible"));
                libros.add(libro);
            }
        }
        return libros;
    }


    // Método para actualizar un libro
    public void actualizarLibro(String isbn, String nuevoTitulo, String nuevoAutor, String nuevoGenero) throws SQLException {
        String sql = "UPDATE Libros SET Titulo = ?, Autor = ?, Genero = ? WHERE ISBN = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nuevoTitulo);
            statement.setString(2, nuevoAutor);
            statement.setString(3, nuevoGenero);
            statement.setString(4, isbn);
            statement.executeUpdate();
        }
    }

    // Método para eliminar un libro
    public void eliminarLibro(String isbn) throws SQLException {
        String sql = "DELETE FROM Libros WHERE ISBN = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        }
    }

    // Método para buscar un libro por su ISBN
    public Libro buscarLibroPorISBN(String isbn) throws SQLException {
        String sql = "SELECT * FROM Libros WHERE ISBN = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Libro libro = new Libro();
                    libro.setISBN(resultSet.getString("ISBN"));
                    libro.setTitulo(resultSet.getString("Titulo"));
                    libro.setAutor(resultSet.getString("Autor"));
                    libro.setGenero(resultSet.getString("Genero"));
                    libro.setDisponible(resultSet.getBoolean("Disponible"));
                    return libro;
                }
            }
        }
        System.out.println("isbn,no se encontró="+isbn);
        return null;
    }

    // Método para buscar libros por título
    public ArrayList<Libro> buscarLibrosPorTitulo(String titulo) throws SQLException {
        ArrayList<Libro> librosEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM Libros WHERE Titulo LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + titulo + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Libro libro = new Libro();
                    libro.setISBN(resultSet.getString("ISBN"));
                    libro.setTitulo(resultSet.getString("Titulo"));
                    libro.setAutor(resultSet.getString("Autor"));
                    libro.setGenero(resultSet.getString("Genero"));
                    libro.setDisponible(resultSet.getBoolean("Disponible"));
                    librosEncontrados.add(libro);
                }
            }
        }
        return librosEncontrados;
    }

    public boolean verificarDisponibilidad(Libro libro) {
        boolean disponibilidad = false; // Por defecto, el libro no está disponible

        String isbn = libro.getISBN();
        String sql = "SELECT Disponible FROM Libros WHERE ISBN = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int disponible = resultSet.getInt("Disponible");
                disponibilidad = (disponible == 1); // Convertir el valor entero a booleano
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad del libro: " + e.getMessage());
        }

        return disponibilidad;
    }
}
