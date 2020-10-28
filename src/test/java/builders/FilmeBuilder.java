package builders;

import entidades.Filme;

public class FilmeBuilder {
	private Filme filme;
	private FilmeBuilder() {}
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder=new FilmeBuilder();
		builder.filme=new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(4.0);//defini como valor padr�o por ser o que da menos resultados de falha
		return builder;
	}
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder=new FilmeBuilder();
		builder.filme=new Filme();
		builder.filme.setEstoque(0);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(4.0);//defini como valor padr�o por ser o que da menos resultados de falha
		return builder;
	}
	
	public FilmeBuilder semEstoque() {
		
		filme.setEstoque(0);
		return this; //por estar no proprio metodo de instancia
	}
	
	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
		
	}
	
	public Filme agora() {
		return filme;
		
	}

}
