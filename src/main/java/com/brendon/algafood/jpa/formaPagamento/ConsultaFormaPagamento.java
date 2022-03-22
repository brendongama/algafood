package com.brendon.algafood.jpa.formaPagamento;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.brendon.algafood.AlgafoodApiApplication;
import com.brendon.algafood.domain.model.FormaPagamento;
import com.brendon.algafood.domain.repository.FormaPagamentoRepository;

public class ConsultaFormaPagamento {

	  public static void main(String[] args) {
          ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                  .web(WebApplicationType.NONE)
                  .run(args);
          
          FormaPagamentoRepository formaPagamentoRepository = applicationContext.getBean(FormaPagamentoRepository.class);
          
          List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.listar();
          
          for (FormaPagamento formaPagamento : todasFormasPagamentos) {
              System.out.println(formaPagamento.getDescricao());
          }
      }
}
