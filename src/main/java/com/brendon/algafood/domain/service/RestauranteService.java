package com.brendon.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.brendon.algafood.domain.model.Restaurante;
import com.brendon.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	public List<Restaurante> listar() {
		return restauranteRepository.listar();
	}

	public Restaurante buscar(Long restauranteId) {
		try {
			return restauranteRepository.buscar(restauranteId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de restaurante com código %d", restauranteId));
		}
	}

}
