package com.brendon.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.brendon.algafood.AlgafoodApiApplication;
import com.brendon.algafood.domain.model.Cozinha;
import com.brendon.algafood.domain.repository.CozinhaRepository;

public class InclusaoCozinhaMain {
  
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Roberto");
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Thalita");
		
		cozinhaRepository.salvar(cozinha1);
		cozinhaRepository.salvar(cozinha2);
		
  }
}
