package com.brendon.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brendon.algafood.domain.model.Restaurante;
import com.brendon.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteService restauranteService;
	
	@GetMapping
	public ResponseEntity <List<Restaurante>> listar() {		
		List<Restaurante> restauranteList =  restauranteService.listar();		
		if(restauranteList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}		
		return ResponseEntity.ok(restauranteList);
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {		
		Restaurante restaurante = restauranteService.buscar(restauranteId);		
		if(restaurante != null) {
			return ResponseEntity.ok(restaurante);			
		}		
		return ResponseEntity.notFound().build();
	}
}
