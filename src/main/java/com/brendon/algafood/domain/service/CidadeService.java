package com.brendon.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.brendon.algafood.domain.model.Cidade;
import com.brendon.algafood.domain.model.Estado;
import com.brendon.algafood.domain.repository.CidadeRepository;
import com.brendon.algafood.domain.repository.EstadoRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;

	
	public List<Cidade> listar() {
		List<Cidade> listaCidade =  cidadeRepository.listar();
		if(listaCidade.isEmpty()) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um ou mais cidades cadastradas"));
		}
		return listaCidade;
	}

	
	public Cidade buscar(Long id) {
		Cidade cidade = cidadeRepository.buscar(id);
		if(cidade == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe uma cidade cadastrada com o id %d", id));
		}		
		return cidade;
	}

	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		
		Estado estado = estadoRepository.buscar(estadoId);
		if(estado == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um estado cadastrado com o id %d", estadoId));
		}		
		return cidadeRepository.salvar(cidade);
	}

	
	public void remover(Long id) {
		Cidade cidade = cidadeRepository.buscar(id);
		if(cidade == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe uma cidade cadastrada com o id %d", id));
		}			
		cidadeRepository.remover(id);		
	}
	
	public Cidade atualizar(Long id, Cidade cidade) {
		Cidade cidadeAntiga = cidadeRepository.buscar(id);
		if(cidadeAntiga == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe uma cidade cadastrada com o id %d", id));
		}		
		cidade.setId(id);
		return cidadeRepository.salvar(cidade);		
	}

}
