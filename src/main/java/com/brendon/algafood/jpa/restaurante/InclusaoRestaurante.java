package com.brendon.algafood.jpa.restaurante;

import java.math.BigDecimal;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.brendon.algafood.AlgafoodApiApplication;
import com.brendon.algafood.domain.model.Restaurante;
import com.brendon.algafood.domain.repository.RestauranteRepository;

public class InclusaoRestaurante {
	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
	    RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);
	    
	    Restaurante restaurante = new Restaurante();
	    restaurante.setNome("Brendon");
	    restaurante.setTaxaFrete(new BigDecimal(10));
	    
	    restauranteRepository.salvar(restaurante);
	}
}
