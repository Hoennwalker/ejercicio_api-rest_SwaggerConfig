package com.example.formacionspring.apirest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.formacionspring.apirest.entity.Alumno;
import com.example.formacionspring.apirest.entity.Comunidad;

@Repository
public interface AlumnoDao extends CrudRepository<Alumno, Long>
{
	@Query("from Comunidad")
	public List<Comunidad> finAllComunidades();
}
