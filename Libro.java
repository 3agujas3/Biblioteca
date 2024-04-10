package com.miapp.biblioteca;

public class Libro {
	
	//Atributos
	
	private String titulo;
	private String autor;
	private String ISBN;
	private String genero;
	private boolean Disponible;
	
	//Constructor
	
	public Libro(String titulo,String autor,String ISBN,String genero) {
			
		this.titulo=titulo;
		this.autor=autor;
		this.ISBN=ISBN;
		this.genero=genero;
		this.Disponible=true;
			
	}
	
	//Constructor
	
	public Libro() {
		
	}
	
	//Getters Setters
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public boolean isDisponible() {
		return Disponible;
	}

	public void setDisponible(boolean disponible) {
		Disponible = disponible;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Libro[titulo=" + titulo + ", autor=" + autor
				+", ISBN=" + ISBN +", genero=" + genero + ", disponible="
				+ Disponible + "]";
	}


	
	
	
	
	

}
