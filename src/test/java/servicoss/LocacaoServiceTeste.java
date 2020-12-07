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
import org.mockito.Mockito;

import buildermaster.BuilderMaster;
import builders.FilmeBuilder;
import builders.LocacaoBuilder;
import builders.UsuarioBuilder;
import daos.LocacaoDAO;
import daos.LocacaoFake;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueExceptions;
import exceptions.LocadoraException;
import matcherers.DiasSemanaMatcher;
import matcherers.MatchersProprios;
import servicos.EmailService;
import servicos.LocacaoService;
import servicos.SPCService;
import utils.DataUtils;

public class LocacaoServiceTeste {
	private LocacaoService service;
	private SPCService spc;
	private LocacaoDAO dao;
	private EmailService email;

	@Before
	public void setup() {
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDao(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
		email=Mockito.mock(EmailService.class);
		service.setEmailService(email);
		
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
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora()); // o builder facilita na leitura
																						// do codigo. Que apenas pela
																						// sua criação, basta ver para
																						// saber que é um filme sem
																						// stock

		// Accao
		service.alugarFilme(usuario, filmes);

	}

	// checagem do usuario-Usando a forma vazia
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueExceptions {

		// cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

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
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// accao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		// assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}

	@Test
	public void naoDeveAlugarFilmesParaNegativadoSPC()throws FilmeSemEstoqueExceptions{
		// cenario
		Usuario usuario = umUsuario().agora();
		//para questao de teste de erro
		//Usuario usuario2= umUsuario().comNome("Usuario2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			// verificacao
			Assert.fail();//o fail, garante que não exista um falso positivo
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		
		
	
		Mockito.verify(spc).possuiNegativacao(usuario);
	}
	@Test
	public void deveEnviarEmailParaLocacaoAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		//Para verificar se o sistema esta a funcionar em condicoes, irei criar um contexto de erro para o segundo usuario
		//Usuario usuario2=umUsuario().comNome("Usuario 2").agora();
		List<Locacao>locacoes=
				Arrays.asList(LocacaoBuilder.umLocacao()
						.comUsuario(usuario)
						.comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
						.agora());
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//accao
		
		service.notificarAtrasos();
		
		//verificacao
		Mockito.verify(email)
		.notificarAtraso(usuario);
		
		
	}

}
