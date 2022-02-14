package com.example.formacionspring.apirest.controller;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.formacionspring.apirest.entity.Alumno;
import com.example.formacionspring.apirest.entity.Comunidad;
import com.example.formacionspring.apirest.service.AlumnoService;

@RestController
@RequestMapping("/api")
public class AlumnoController
{
	@Autowired
	private AlumnoService servicio;
	
	@GetMapping("/alumnos")
	public List <Alumno> alumno()
	{//método get del postman
		return servicio.findAll();
	}

	@DeleteMapping("/alumnos/{id}")
	public ResponseEntity<?> deleteAlumno (@PathVariable Long id)
	{//método delete
		Alumno AlumnoBorrar = servicio.findById(id);
		Map<String, Object> response = new HashMap<>();
		
		if(AlumnoBorrar == null) 
		{
			response.put("mensaje", "El alumno ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		else
		{
			try 
			{
				servicio.delete(id);
				
				String nombrefotoAnterior = AlumnoBorrar.getImagen();
				
				if(nombrefotoAnterior!=null && nombrefotoAnterior.length()>0)
				{
					Path rutaFotoAnterior = Paths.get("uploads").resolve(nombrefotoAnterior).toAbsolutePath();
					File ArchivoFotoAnterior = rutaFotoAnterior.toFile();
					
					if(ArchivoFotoAnterior.exists() && ArchivoFotoAnterior.canRead())
						ArchivoFotoAnterior.delete();
				}
			} 
			catch (DataAccessException e) 
			{
				response.put("mensaje", "error al borrar en la base de datos");
				response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "el alumno ha sido borrado con éxito.");
		response.put("alumno", AlumnoBorrar);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	
	@PutMapping("/alumnos/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateCliente (@RequestBody Alumno alumno, @PathVariable Long id)
	{//update es método put en el postman
		Alumno AlumnoActual = servicio.findById(id);
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			AlumnoActual.setNombre(alumno.getNombre());
			AlumnoActual.setApellidos(alumno.getApellidos());
			AlumnoActual.setTelefono(alumno.getTelefono());
			AlumnoActual.setComunidad(alumno.getComunidad());
			
			servicio.save(AlumnoActual);
		} 
		catch (DataAccessException e) 
		{
			response.put("mensaje", "error al insertar en la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		response.put("mensaje", "el alumno ha sido actualizado con éxito.");
		response.put("alumno", AlumnoActual);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/alumnos/{id}")
	public ResponseEntity<?> ClienteShow(@PathVariable Long id)
	{
		Alumno alumno = null;
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			alumno = servicio.findById(id);
		} 
		catch (DataAccessException e) 
		{
			response.put("mensaje", "error al consultar la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(alumno == null) {
			response.put("mensaje", "El alumno ID: ".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
	}
	
	@PostMapping("/alumnos")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> clienteSave(@RequestBody Alumno alumno)
	{//método post del postman
		Alumno AlumnoNew = null;
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			AlumnoNew = servicio.save(alumno);
		} 
		catch (DataAccessException e) 
		{
			response.put("mensaje", "error al insertar en la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "el alumno ha sido creado con éxito.");
		response.put("alumno", AlumnoNew);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PostMapping("/alumnos/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id)
	{
		Map<String, Object> response = new HashMap<>();
		Alumno alumno = servicio.findById(id);
		
		if(!archivo.isEmpty())
		{
			String nombreArchivo= UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ","");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			try 
			{
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} 
			catch (IOException e) 
			{
				response.put("mensaje", "error al insertar en la base de datos");
				response.put("error", e.getMessage().concat("_ ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombrefotoAnterior = alumno.getImagen();
			
			if(nombrefotoAnterior!=null && nombrefotoAnterior.length()>0)
			{
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombrefotoAnterior).toAbsolutePath();
				File ArchivoFotoAnterior = rutaFotoAnterior.toFile();
				
				if(ArchivoFotoAnterior.exists() && ArchivoFotoAnterior.canRead())
					ArchivoFotoAnterior.delete();
			}
			
			alumno.setImagen(nombreArchivo);
			servicio.save(alumno);
			response.put("alumno", alumno);
			response.put("mensaje", "subida correcta de imagen"+nombreArchivo);
		}
		else
			response.put("Archivo", "Archivo vacío");
			
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("alumnos/upload/img/{nombreImagen:.+}")
	public ResponseEntity<Resource> verImage(@PathVariable String nombreImagen)
	{
		Path ruta_archivo = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
		Resource recurso = null;
		
		try 
		{
			recurso = new UrlResource(ruta_archivo.toUri());
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable())
		{
			throw new RuntimeException("error, no se puede cargar la imagen "+nombreImagen);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename\""+recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	@GetMapping("/alumnos/comunidad")
	public List<Comunidad> listaRegiones()
	{
		return null;
	}
	
}
