package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CasculadoraTest {
	private Calculadora calc;

	@Before

	public void setup() {
		calc = new Calculadora();

	}

	@Test
	public void deveSomarDoisNumeros() {
		// cenario
		int a = 5;
		int b = 3;

		// acao
		int result = calc.somar(a, b);
		// verificacao
		Assert.assertEquals(8, result);

	}

	@Test
	public void deveSubitrairDoisValores() {
		// cenario
		int a = 8;
		int b = 5;

		// acao
		int result = calc.subtrair(a, b);

		// verificacao
		Assert.assertEquals(3, result);

	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 6;
		int b = 3;

		// accao
		int result = calc.dividir(a, b);

		// verificacao
		Assert.assertEquals(2, result);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcepitonDividirPorZero() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 10;
		int b = 0;

		calc.dividir(a, b);
		// accao

		// verificacao

	}

}
