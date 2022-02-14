package com.example.formacionspring.apirest.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.formacionspring.apirest.dao.AlumnoDao;
import com.example.formacionspring.apirest.entity.Alumno;
import com.example.formacionspring.apirest.entity.Comunidad;


@Service
public class AlumnoServiceImplements implements AlumnoService
{
	@Autowired
	private AlumnoDao alumnoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List <Alumno> findAll() 
	{
		return (List<Alumno>) alumnoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Alumno findById(Long id) 
	{
		return alumnoDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Alumno save(Alumno cliente)
	{
		return alumnoDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) 
	{
		alumnoDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comunidad> finAllComunidades() {
		return alumnoDao.finAllComunidades();
	}

	
}
