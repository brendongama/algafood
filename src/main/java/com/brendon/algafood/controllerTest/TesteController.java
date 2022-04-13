package com.brendon.algafood.controllerTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brendon.algafood.domain.model.Cozinha;
import com.brendon.algafood.domain.model.Restaurante;
import com.brendon.algafood.domain.repository.CozinhaRepository;
import com.brendon.algafood.domain.repository.RestauranteRepository;
import com.brendon.algafood.infrastructure.repository.spec.RestauranteSpecs;

@RestController
@RequestMapping("/teste")
public class TesteController {

	
	@Autowired 
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@GetMapping("/restaurante/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxaFretes(BigDecimal taxaInicial, BigDecimal taxaFinal){
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}
	
	@GetMapping("/restaurante/por-nome")
	public List<Restaurante> restaurantesPorNome(String nome, Long cozinhaId){
		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}
	
	@GetMapping("/restaurante/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome){
		return restauranteRepository.findAll(
				RestauranteSpecs.comFreteGratis().and(
						RestauranteSpecs.comNomeSemelhante(nome)));
	}
	
	@GetMapping("/cozinha/primeira")
	public Optional<Cozinha> cozinhaPrimeira(){
		return cozinhaRepository.buscarPrimeiro();
	}
	
	@GetMapping("/restaurante/primeiro")
	public Optional<Restaurante> restaurantePrimeiro(){
		return restauranteRepository.buscarPrimeiro();
	}
	

}
