package servicoss;

import static builders.FilmeBuilder.umFilme;
import static builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.dnd.peer.DropTargetPeer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import builders.FilmeBuilder;
import daos.LocacaoDAO;
import daos.LocacaoFake;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueExceptions;
import exceptions.LocadoraException;
import servicos.LocacaoService;
import servicos.SPCService;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	private LocacaoService service;
	@Parameter
	public List<Filme> filmes;
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;

	
	@Before
	public void setup() {
		service = new LocacaoService();
		LocacaoDAO dao=Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDao(dao);
		SPCService spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
	} 

	// Criar a colecao que sera testada
	private static Filme filme1 =  umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
/*
 *para informar ao Junit que este metodo, sera a fonte de Dados, usamos o parametro 
 *@Parameters antes do metodo
 *Este metodo sera static pk é ele que vai definir a quantidade de execucao de cada metodo de teste
 */
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { 
			{ Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25 %"},
			{ Arrays.asList(filme1, filme2, filme3,filme4), 13.0 , "4 Filmes: 50 %"},
			{ Arrays.asList(filme1, filme2, filme3,filme4,filme5), 14.0, "5 Filmes: 75 %" },
			{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme5), 14.0, "6 Filmes: 100 %" } });

	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueExceptions, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(valorLocacao));

	}

}
