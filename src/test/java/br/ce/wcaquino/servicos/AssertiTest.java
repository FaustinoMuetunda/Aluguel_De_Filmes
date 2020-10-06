package br.ce.wcaquino.servicos;

import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

import org.junit.Assert;


public class AssertiTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		//é possivel adicionar a mensagem de ocorrencia
		
		Assert.assertEquals("Erro de compacao",1, 1);
		
		Assert.assertEquals(1, 1);
		
		//o 0.01, é um padrão de margem de erros que o equal, pode lidar-se
		
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		//Usando o equas, testando no nivel de um obj e um tipo primitivo
		
		int i=5;
		Integer i2=5;
		
		Assert.assertEquals(Integer.valueOf(i), i2); //passei o tipo primitivo para um objeto
		Assert.assertEquals(i, i2.intValue()); //passei o objeto para tipo primitico
		
		//Assertivas em String
		
		Assert.assertEquals("bola", "bola");
		
		//Para verificar se dois objet
		Assert.assertNotEquals("bola", "casa");
		
		//verificar obj, não considerando se for maiuscula ou nao
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		
		Usuario u1= new Usuario("Fausto Kwamme");
		Usuario u2= new Usuario("Fausto Kwamme");
		Assert.assertEquals(u1, u2);
		Usuario u3=null;
		
		
		//Para verificar se dois obj sao da mesma instancia
		//usamos o assertSema
		
		Assert.assertSame(u2, u2);
		
		Assert.assertNotSame(u1, u2);
		
		//Verificar se o obj esta nulo/vazio
		
		Assert.assertNull(u3);
		
		Assert.assertNotNull(u2);
		
	
		
	}

}
