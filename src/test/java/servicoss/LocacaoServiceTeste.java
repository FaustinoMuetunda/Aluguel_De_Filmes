package servicoss;

import static builders.UsuarioBuilder.umUsuario;
import static matcherers.MatchersProprios.caiEm;
import static matcherers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static utils.DataUtils.isMesmaData;

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

import buildermaster.BuilderMaster;
import builders.FilmeBuilder;
import builders.UsuarioBuilder;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueExceptions;
import exceptions.LocadoraException;
import matcherers.DiasSemanaMatcher;
import matcherers.MatchersProprios;
import servicos.LocacaoService;
import utils.DataUtils;

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

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());

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

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora()); //o builder facilita na leitura do codigo. Que apenas pela sua criação, basta ver para saber que é um filme sem stock

		// Accao
		service.alugarFilme(usuario, filmes);

	}

	// checagem do usuario-Usando a forma vazia
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueExceptions {

		// cenario
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilme().agora());

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

		Usuario usuario = umUsuario().agora();
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		// acao
		service.alugarFilme(usuario, null);

	}

	@Test
	public void deveDevolverNaSegundaSeAlugarNoSabado() throws FilmeSemEstoqueExceptions, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =  Arrays.asList(FilmeBuilder.umFilme().agora());

		// accao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}
	
	public static void main(String[] args) {
		
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
		
	}

}
