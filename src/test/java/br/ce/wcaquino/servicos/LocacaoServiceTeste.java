package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	private LocacaoService service;

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	@Rule
	public ErrorCollector error = new ErrorCollector();
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void deveAlugarFilme() throws Exception {
		// O assume, crie excepcoes para o teste

		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenario

		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// Accao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Para a leitura ter ficado facilitada, fiz o import import
		// org.hamcrest.CoreMatchers;
		// imports

		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

	}

	// Forma elegante

	@Test(expected = FilmeSemEstoqueExceptions.class) // informar ao teste que se esta esperar por uma excepcao
	public void naoDeveAlugarFilmeSemExtoque() throws FilmeSemEstoqueExceptions, LocadoraException {

		// Cenario

		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

		// Accao
		service.alugarFilme(usuario, filmes);

	}

	// checagem do usuario-Usando a forma vazia
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueExceptions {

		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

		// accao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	// Checagem com o filme vazio
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueExceptions, LocadoraException {
		// Cenario

		Usuario usuario = new Usuario("Usuario 1");
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		// acao
		service.alugarFilme(usuario, null);

	}

	@Test
	public void deveDevolverNaSegundaSeAlugarNoSabado() throws FilmeSemEstoqueExceptions, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		// accao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		boolean ehSegunada = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunada);

	}

}
