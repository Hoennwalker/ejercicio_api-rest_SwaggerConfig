package com.example.formacionspring.apirest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name="alumnos")
public class Alumno implements Serializable
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String apellido;
	@Column(nullable = false, unique = true)
	private String dni;
	@Column(nullable = false)
	private String direccion;
	@Column(nullable = false)
	private int codigo_postal;
	@Column(nullable = false)
	private int telefono;
	
	@Column
	private String avatar_img;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="comunidad_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Comunidad comunidad;
	
	public Comunidad getComunidad() {
		return comunidad;
	}


	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}


	public String getImagen() {
		return avatar_img;
	}


	public void setImagen(String avatar_img) {
		this.avatar_img = avatar_img;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellido;
	}

	public void setApellidos(String apellido) {
		this.apellido = apellido;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
