package com.brendon.algafood.jpa.cozinha;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.brendon.algafood.AlgafoodApiApplication;
import com.brendon.algafood.domain.model.Cozinha;
import com.brendon.algafood.domain.repository.CozinhaRepository;

public class AlteracaoCozinhaMain {
  
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(1L);
		cozinha.setNome("lUCAS");
		
		cozinhaRepository.salvar(cozinha);
  }
}