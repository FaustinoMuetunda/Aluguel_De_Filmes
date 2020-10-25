package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
		service=new LocacaoService();
	}
		
	@Rule
	public ErrorCollector error=new ErrorCollector();
	@Rule
	public ExpectedException exception= ExpectedException.none();
	
	
	@Test
	public void deveAlugarFilme()throws Exception {
		//Cenario
		
		Usuario usuario=new Usuario("Usuario 1");
		List<Filme> filmes=Arrays.asList(new Filme("Filme 1",2,5.0));
		
		//Accao
		Locacao locacao=service.alugarFilme(usuario, filmes);
		
		//Para a leitura ter ficado facilitada, fiz o import import org.hamcrest.CoreMatchers;
		//imports 
	
		error.checkThat( locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
		
	}
	
	//Forma elegante
	
	@Test(expected = FilmeSemEstoqueExceptions.class)//informar ao teste que se esta esperar por uma excepcao
	public void naoDeveAlugarFilmeSemExtoque() throws FilmeSemEstoqueExceptions, LocadoraException{
		
		//Cenario
		
				Usuario usuario=new Usuario("Usuario 1");
				List<Filme> filmes=Arrays.asList(new Filme("Filme 1",0,4.0));
				
				//Accao
			 service.alugarFilme(usuario, filmes);
				
	}
	
	//checagem do usuario-Usando a forma vazia
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueExceptions {
		
		//cenario
		List<Filme> filmes=Arrays.asList(new Filme("Filme 1",2,4.0));
				
		//accao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		
	}
	//Checagem com o filme vazio
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueExceptions, LocadoraException {
		//Cenario
		
		Usuario usuario=new Usuario("Usuario 1");
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		service.alugarFilme(usuario, null);
	
		
	}
	
	@Test
	public void devePagar75PcNo3Filme() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cenario
		Usuario usuario=new Usuario("Usuario 1");
		List<Filme>filmes=Arrays.asList(new Filme("Filme 1", 2, 4.0),new Filme("Filme 2", 2, 4.0),new Filme("Filme 3", 2, 4.0));
		//acao
		Locacao resultado= service.alugarFilme(usuario, filmes);
		//verificacao
		//4+4+3(desconto de 25% no terceiro filme)=11
		
		assertThat(resultado.getValor(),is(11.0));
		
	}
	
	@Test
	public void devePagar50PcNo4Filme() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cenario
		Usuario usuario=new Usuario("Usuario 1");
		List<Filme>filmes=Arrays.asList(new Filme("Filme 1", 2, 4.0),new Filme("Filme 2", 2, 4.0),new Filme("Filme 3", 2, 4.0),new Filme("Filme 4", 2, 4.0));
		//acao
		Locacao resultado= service.alugarFilme(usuario, filmes);
		//verificacao
		//4+4+3+2(desconto de 50% no terceiro filme)=13
		
		assertThat(resultado.getValor(),is(13.0));
		
	}
	
	@Test
	public void devePagar25PcNo5Filme() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cenario
		Usuario usuario=new Usuario("Usuario 1");
		List<Filme>filmes=Arrays.asList(new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0)
				);
		//acao
		Locacao resultado= service.alugarFilme(usuario, filmes);
		//verificacao
		//4+4+3+2+1(desconto de 50% no terceiro filme)=13
		
		assertThat(resultado.getValor(),is(14.0));
		
	}
	@Test
	public void devePagar0PcNo6Filme() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cenario
		Usuario usuario=new Usuario("Usuario 1");
		List<Filme>filmes=Arrays.asList(new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0)
				);
		//acao
		Locacao resultado= service.alugarFilme(usuario, filmes);
		//verificacao
		//4+4+3+2+1+0(desconto de 50% no terceiro filme)=13
		
		assertThat(resultado.getValor(),is(14.0));
		
	}

}
