package com.algaworks.algafood.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String MSG_GENERICA_USER_MESSAGE = "Ocorreu um erro interno inesperado no sistema. "
	+ "Tente novamente e se o problema persistir, entre em contato "
	+ "com o administrador do sistema.";
	private static final String ERRO_DE_SINTAXE = "O corpo da requisição está invalido. Verifique erro de sintaxe";

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException)rootCause, headers, status, request);
		}
		
		if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException)rootCause, headers, status, request);
		}
		
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, ERRO_DE_SINTAXE).userMessages(ex.getMessage()).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = String.format("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
		
		BindingResult bindingResult = ex.getBindingResult();
		
		List<Problem.Field> problemFields = bindingResult.getFieldErrors()
				.stream().map(fieldError -> Problem.Field.builder()
						.name(fieldError.getField())
						.userMessage(fieldError.getDefaultMessage())
						.build())
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessages(detail)
				.fields(problemFields)
				.build();
		return handleExceptionInternal(ex, problem, headers, status, request);		
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if(ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatchException(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		 ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		 String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
		            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
		            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		 Problem problem = createProblemBuilder(status, problemType, detail).userMessages(ex.getMessage()).build();
		 return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		
		String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é de um tipo invalido. Corrija e informe um valor compativel com o tipo '%s'", path, ex.getValue(), ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail).userMessages(ex.getMessage()).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		String detail = String.format("A propriedade '%s' não existe na entidade '%s'. Corrija ou remova essa propriedade e tente novamente", path, ex.getReferringClass().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessages(MSG_GENERICA_USER_MESSAGE).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;
		String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
	            ex.getRequestURL());
		Problem problem = createProblemBuilder(status, problemType, detail).userMessages(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
	    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
	    String detail = MSG_GENERICA_USER_MESSAGE;
	    ex.printStackTrace();	    
	    Problem problem = createProblemBuilder(status, problemType, detail).userMessages(detail).userMessages(detail).build();
	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
			WebRequest request) {	
		Problem problem = createProblemBuilder(HttpStatus.NOT_FOUND, ProblemType.RECURSO_NAO_ENCONTRADA, ex.getMessage()).userMessages(ex.getMessage()).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
		Problem problem = createProblemBuilder(HttpStatus.BAD_REQUEST, ProblemType.ERRO_NEGOCIO, ex.getMessage()).userMessages(ex.getMessage()).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
		Problem problem = createProblemBuilder(HttpStatus.CONFLICT, ProblemType.ENTIDADE_EM_USO, ex.getMessage())
				.userMessages(MSG_GENERICA_USER_MESSAGE).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = Problem.builder()
					.timestamp(LocalDateTime.now())
					.title(status.getReasonPhrase())
					.status(status.value())
					.userMessages(MSG_GENERICA_USER_MESSAGE)
					.build();
		}else if(body instanceof String){
			body = Problem.builder()
					.timestamp(LocalDateTime.now())
					.title((String) body)
					.status(status.value()) 
					.userMessages(MSG_GENERICA_USER_MESSAGE)
					.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder()
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);
	}
}
