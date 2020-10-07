package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	
	@Rule
	public ErrorCollector error=new ErrorCollector();
	@Rule
	public ExpectedException exception= ExpectedException.none();
	
	
	@Test
	public void testeLocacao()throws Exception {
		//Cenario
		
		LocacaoService service=new LocacaoService();
		Usuario usuario=new Usuario("Usuario 1");
		Filme filme=new Filme("Filme 1",2,5.0);
		
		//Accao
		Locacao locacao=service.alugarFilme(usuario, filme);
		
		//Verificacao
		
		System.out.println(locacao.getValor());
		System.out.println(locacao.getDataLocacao());
		System.out.println(locacao.getDataRetorno());
		
		//Verificar se os atributos estao conforme o esperado
		System.out.println();
		
		//Para a leitura ter ficado facilitada, fiz o import import org.hamcrest.CoreMatchers;
		//imports 
	
		error.checkThat( locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
		
	}
	
	//Forma elegante
	
	@Test(expected = Exception.class)//informar ao teste que se esta esperar por uma excepcao
	public void testLocacao_filmeSemEstoque() throws Exception {
		
		//Cenario
		
				LocacaoService service=new LocacaoService();
				Usuario usuario=new Usuario("Usuario 1");
				Filme filme=new Filme("Filme 1",0,5.0);
				
				//Accao
			service.alugarFilme(usuario, filme);
				
	}
	
	//Forma robusta
	
	@Test
	public void testLocacao_filmeSemEstoque2() {
		
		//Cenario
		
				LocacaoService service=new LocacaoService();
				Usuario usuario=new Usuario("Usuario 1");
				Filme filme=new Filme("Filme 1",0,5.0);
				
				//Accao
			try {
				service.alugarFilme(usuario, filme);
				Assert.fail("Deveria ter lancado um excecao");
			} catch (Exception e) {
				assertThat(e.getMessage(), is("Filme sem stoque"));
			}
				
	}
	
	//Forma nova
	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		
		//Cenario
		
				LocacaoService service=new LocacaoService();
				Usuario usuario=new Usuario("Usuario 1");
				Filme filme=new Filme("Filme 1",0,5.0);
				exception.expect(Exception.class);
				exception.expectMessage("Filme sem stoque");
				
				//Accao
			service.alugarFilme(usuario, filme);
			
			
				
	}
	
	

}
