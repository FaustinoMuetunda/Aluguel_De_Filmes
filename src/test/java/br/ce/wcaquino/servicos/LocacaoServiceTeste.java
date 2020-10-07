package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	
	@Rule
	public ErrorCollector error=new ErrorCollector();
	@Test
	public void testeLocacao() {
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
	
		error.checkThat( locacao.getValor(), is(equalTo(6.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(false));
		
		
		
		
		
	}

}
