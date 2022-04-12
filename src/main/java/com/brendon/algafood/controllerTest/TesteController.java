package com.brendon.algafood.controllerTest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brendon.algafood.domain.model.Restaurante;
import com.brendon.algafood.domain.repository.RestauranteRepository;
import com.brendon.algafood.infrastructure.repository.spec.RestauranteComFreteGratisEpec;
import com.brendon.algafood.infrastructure.repository.spec.RestauranteComNomeSemelhanteSpec;

@RestController
@RequestMapping("/teste")
public class TesteController {

	
	@Autowired 
	private RestauranteRepository restauranteRepository;
	
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
		
		var comFreteGratis = new RestauranteComFreteGratisEpec(); 
		var comNomeSemelhante = new RestauranteComNomeSemelhanteSpec(nome);
		return restauranteRepository.findAll(comFreteGratis.and(comNomeSemelhante));
	}
	

}
