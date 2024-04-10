package com.miapp.biblioteca;
import java.util.ArrayList; // import the ArrayList class

public class Usuario {
	
	//Atributos
	
	private String nombre;
	private int id;
	private ArrayList<Libro> librosPrestados;
	
	//Constructor
	
	public Usuario(String nombre, int id){
		
		this.nombre=nombre;
		this.id=id;
		this.librosPrestados=new ArrayList<>();
	}
	
	//Constructor
	
	public Usuario() {
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Libro> getLibrosPrestados() {
		return librosPrestados;
	}
	
	public void setLibrosprestados(ArrayList<Libro> librosprestados) {
		this.librosPrestados = librosprestados;
	}
	
	@Override
	public String toString() {
		return "Usuario [nombre=" + ", id=" + id +"]";
	}


	


}
