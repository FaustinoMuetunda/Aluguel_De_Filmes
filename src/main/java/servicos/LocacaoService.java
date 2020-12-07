package servicos;

import static utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import daos.LocacaoDAO;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemEstoqueExceptions;
import exceptions.LocadoraException;
import utils.DataUtils;

public class LocacaoService {
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emaiservices;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
			throws FilmeSemEstoqueExceptions, LocadoraException {

		if (usuario == null) {

			throw new LocadoraException("Usuario vazio");
		}
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueExceptions();

			}

		
		}
		
		if(spcService.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuario Negativado");
			
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch(i){
			case 2: valorFilme *= 0.75;break;
			case 3: valorFilme *= 0.50;break;
			case 4: valorFilme *= 0.25;break;
			case 5: valorFilme *= 0.d;break;
			
			}
			
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
			
		}
		
		
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}
	//o metodo vai notificar atrasos para clientes com pagamentos
	public void notificarAtrasos() {
		List<Locacao>locacoes=dao.obterLocacoesPendentes();
		for(Locacao locacao: locacoes) {
			emaiservices.notificarAtraso(locacao.getUsuario());
			
		}
		
	}
	
	
	public void setLocacaoDao(LocacaoDAO dao) {
		this.dao=dao;
		
	}
	
	public void setSPCService(SPCService spc) {
		spcService=spc;
	}
	
	public void setEmailService(EmailService email) {
		
		emaiservices=email;
	}

}