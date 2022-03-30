package com.brendon.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.brendon.algafood.domain.exception.EntidadeEmUsoException;
import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.brendon.algafood.domain.model.Estado;
import com.brendon.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;


	public List<Estado> listar() {
		List<Estado> estadoLista = estadoRepository.listar();		
		if(estadoLista.isEmpty()) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um ou mais estados cadastrados"));
		}		
		return estadoLista;
	}


	public Estado buscar(Long id) {
		Estado estado = estadoRepository.buscar(id);
		if(estado == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um estados cadastrados com o id %d", id));
		}
		return estado;
	}


	public Estado salvar(Estado estado) {
		return estadoRepository.salvar(estado);
	}


	public void remover(Long id) {			
		try {
			estadoRepository.remover(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Estado de código %d não pode ser removida, pois esta em uso", id));
		} catch (EmptyResultDataAccessException e) {
                throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de estado com código %d", id));
		}
		
	}
	
	public Estado alterar(Long id, Estado estadoAlterado) {	
		try {
			estadoAlterado.setId(id);			
			return estadoRepository.salvar(estadoAlterado);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Algum campo no Json esta incorreto :  %s", e.getMostSpecificCause()));
		}
		
	}

}
