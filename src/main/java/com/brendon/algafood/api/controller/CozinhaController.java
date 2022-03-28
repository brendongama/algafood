package com.brendon.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brendon.algafood.domain.exception.EntidadeEmUsoException;
import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.brendon.algafood.domain.model.Cozinha;
import com.brendon.algafood.domain.repository.CozinhaRepository;
import com.brendon.algafood.domain.service.CozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar() {
		return cozinhaRepository.listar();
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha =  cozinhaRepository.buscar(cozinhaId);		
		if(cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}		
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cozinha salvar(@RequestBody Cozinha cozinha) {		
		return  cozinhaService.salvar(cozinha);				
	}
	
	@PutMapping("{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha){
		Cozinha cozinhaAtual = cozinhaRepository.buscar(cozinhaId);
		if(cozinhaAtual != null) {
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");			
			cozinhaService.salvar(cozinhaAtual);
			return ResponseEntity.ok().body(cozinhaAtual);
		}
		return ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("{cozinhaId}")
	public ResponseEntity<Cozinha> deletar(@PathVariable Long cozinhaId){
		try {			
			cozinhaService.excluir(cozinhaId);
			return ResponseEntity.noContent().build();
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}		
	}
}
