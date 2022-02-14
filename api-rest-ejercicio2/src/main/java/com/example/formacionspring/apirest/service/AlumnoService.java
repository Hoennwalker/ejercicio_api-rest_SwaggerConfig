package com.example.formacionspring.apirest.service;

import java.util.List;

import com.example.formacionspring.apirest.entity.Alumno;
import com.example.formacionspring.apirest.entity.Comunidad;

public interface AlumnoService 
{
	public List <Alumno> findAll(); 
	public Alumno findById(Long id);
	public Alumno save(Alumno alumno);
	public void delete (Long id);
	public List<Comunidad> finAllComunidades();
}
