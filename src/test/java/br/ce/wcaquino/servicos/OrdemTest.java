package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

public class OrdemTest {
	public static int cont=0;

	public void inicia() {
		cont=1;
	}

	
	public void verificacao() {
		Assert.assertEquals(1, cont);
	}
	
	//O Junit não garante a ordem de ocorrencia dos testes. 
	//Para o caso, convem criar um teste geral que implemente esta ordem
	@Test
	public void testGeral() {
		inicia();
		verificacao();

	}

}
