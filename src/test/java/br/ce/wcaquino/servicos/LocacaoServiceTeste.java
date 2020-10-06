package br.ce.wcaquino.servicos;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {
	@Test
	public void teste() {
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
		
		
		Assert.assertEquals( 5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		
		
		
		
	}

}
