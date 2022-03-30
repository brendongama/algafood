package com.brendon.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brendon.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.brendon.algafood.domain.model.Cidade;
import com.brendon.algafood.domain.repository.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;

	
	public List<Cidade> listar() {
		List<Cidade> listaCidade =  cidadeRepository.listar();
		if(listaCidade.isEmpty()) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um ou mais cidades cadastradas"));
		}
		return listaCidade;
	}

	
	public Cidade buscar(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Cidade salvar(Cidade cidade) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void remover(Cidade cidade) {
		// TODO Auto-generated method stub
		
	}
	

}
