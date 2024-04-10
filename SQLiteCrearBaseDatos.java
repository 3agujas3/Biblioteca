package com.miapp.biblioteca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class SQLiteCrearBaseDatos {
    
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        
        try {
            // Establecer la conexión con la base de datos SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
            
            // Crear un objeto Statement para ejecutar las sentencias SQL
            statement = connection.createStatement();
            
            // Sentencia SQL para crear la tabla de libros
            String createLibrosTableSQL = "CREATE TABLE IF NOT EXISTS Libros (" +
                                          "ISBN TEXT PRIMARY KEY," +
                                          "Titulo TEXT," +
                                          "Autor TEXT," +
                                          "Genero TEXT," +
                                          "Disponible INTEGER)";
            
            // Ejecutar la sentencia SQL para crear la tabla de libros
            statement.execute(createLibrosTableSQL);
            
            // Sentencia SQL para crear la tabla de usuarios
            String createUsuariosTableSQL = "CREATE TABLE IF NOT EXISTS Usuarios (" +
                                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            "Nombre TEXT)";
            
            // Ejecutar la sentencia SQL para crear la tabla de usuarios
            statement.execute(createUsuariosTableSQL);
            
            // Sentencia SQL para crear la tabla de préstamos
            String createPrestamosTableSQL = "CREATE TABLE IF NOT EXISTS Prestamos (" +
                                              "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                              "UsuarioID INTEGER," +
                                              "LibroISBN TEXT," +
                                              "FechaPrestamo TEXT," +
                                              "FechaDevolucion TEXT," +
                                              "Estado INTEGER,"+
                                              "FOREIGN KEY(UsuarioID) REFERENCES Usuarios(ID)," +
                                              "FOREIGN KEY(LibroISBN) REFERENCES Libros(ISBN))";
            
            // Ejecutar la sentencia SQL para crear la tabla de préstamos
            statement.execute(createPrestamosTableSQL);
            
            System.out.println("Base de datos y tablas creadas con éxito.");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al crear la base de datos: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}