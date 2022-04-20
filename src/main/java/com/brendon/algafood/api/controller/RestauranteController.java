package com.brendon.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
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
		if(!restauranteList.isEmpty()) {
			return ResponseEntity.ok(restauranteList);
		}				
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {		
		Optional<Restaurante> restaurante = restauranteService.buscar(restauranteId);		
		if(restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());			
		}		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = restauranteService.salvar(restaurante);		
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> alterar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			Optional<Restaurante> restauranteBuscar = restauranteService.buscar(restauranteId);
			if(restauranteBuscar.isPresent()) {
				restaurante.setFormasPagamentos(restauranteBuscar.get().getFormasPagamentos());
				restaurante.setEndereco(restauranteBuscar.get().getEndereco());
				restaurante.setDataCadastro(restauranteBuscar.get().getDataCadastro());
				restaurante = restauranteService.alterar(restauranteId, restaurante); 
				return ResponseEntity.ok(restaurante);
			}
			return ResponseEntity.notFound().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
}
