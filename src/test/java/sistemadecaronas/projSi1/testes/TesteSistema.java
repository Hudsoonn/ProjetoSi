package sistemadecaronas.projSi1.testes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


import org.junit.*;

import sistemadecaronas.projSi1.sistema.Carona;
import sistemadecaronas.projSi1.sistema.CaronaRelampago;
import sistemadecaronas.projSi1.sistema.ConteudoTexto;
import sistemadecaronas.projSi1.sistema.Interesse;
import sistemadecaronas.projSi1.sistema.Mensagem;
import sistemadecaronas.projSi1.sistema.PontoDeEncontro;
import sistemadecaronas.projSi1.sistema.SistemaDeCarona;

import sistemadecaronas.projSi1.sistema.Solicitacao;
import sistemadecaronas.projSi1.sistema.Sugestao;
import sistemadecaronas.projSi1.sistema.Usuario;

public class TesteSistema {

	SistemaDeCarona sistema;
	String sessaoMark;
	String sessaoSteve;
	String sessaoBill;
	String idCarona4;
	String idCarona5;
	@Before
	public void antes() throws Exception {
		sistema = SistemaDeCarona.getInstanceOf();
		sistema.criarUsuario("mark", "m@rk", "Mark Zuckerberg",
				"Palo Alto, California", "mark@facebook.com");
		sistema.criarUsuario("steve", "5t3v3", "Steven Paul Jobs",
				"Palo Alto, California", "jobs@apple.com");
		sistema.criarUsuario("bill", "severino", "William Henry Gates III",
				"Medina, Washington", "bil@apple.com");
		try {
			sistema.criarUsuario("", "sda", "fasdf", "Pfa", "xcv@apple.com");
		} catch (Exception e) {
			assertEquals("Login inválido", e.getMessage());

		}
		try {
			sistema.criarUsuario(null, "fas", "afa", "Palo dsia",
					"asd@apple.com");
		} catch (Exception e) {
			assertEquals("Login inválido", e.getMessage());
		}
		sessaoMark = sistema.abrirSessao("mark", "m@rk");
		sessaoSteve = sistema.abrirSessao("steve", "5t3v3");
		sessaoBill = sistema.abrirSessao("bill", "severino");

		idCarona4 = sistema.cadastrarCarona(sessaoMark, "Campina Grande",
				"Joao Pessoa", "20/08/2012", "14:00", "3");

		idCarona5 = sistema.cadastrarCarona(sessaoMark, "Lagoa Seca", "Recife",
				"12/07/2013", "23:00", "2");

	}

	@After
	public void depois() throws Exception {
		sistema.zerarSistema();
	}

	@Test
	public void testeLista() throws Exception {
		// adicionou os 3 mark , steave e bill
		assertEquals(3, sistema.getUsuarios().size());
        
	}

	@Test
	public void testeAbrirSessao() throws Exception {
		sistema.abrirSessao("mark", "m@rk");
		try {
			sistema.abrirSessao("", "m@rk");
		} catch (Exception e) {
			assertEquals("Login inválido", e.getMessage());
		}
		try {
			sistema.abrirSessao("mark", "");
		} catch (Exception e) {
			assertEquals("Senha inválida", e.getMessage());
		}

		try {
			sistema.abrirSessao("asd", "123");
		} catch (Exception e) {
			assertEquals("Usuário inexistente", e.getMessage());
		}
		try {
			sistema.abrirSessao("mark", "123");
		} catch (Exception e) {
			assertEquals("Login inválido", e.getMessage());
		}
	}

	@Test
	public void testaGetAtributoUsuario() throws Exception {
		assertEquals("Mark Zuckerberg",sistema.buscaUsuario("mark").getNome());
		assertEquals("Palo Alto, California",sistema.buscaUsuario("mark").getEndereco());
		assertEquals("mark@facebook.com",sistema.buscaUsuario("mark").getEmail());


	}

	@Test
	public void testaCadastrarCarona() throws Exception {
		try {
			sistema.cadastrarCarona("123", "Campina Grande", "Joao Pessoa",
					"12/06/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("Sessão inexistente", e.getMessage());
		}
		try {
			sistema.cadastrarCarona("", "Campina Grande", "Joao Pessoa",
					"12/06/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("Sessão inválida", e.getMessage());
		}
		try {
			sistema.cadastrarCarona(sessaoMark, "", "Joao Pessoa",
					"12/06/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			sistema.cadastrarCarona(sessaoMark, "Campina Grande", "",
					"12/06/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("Destino inválido", e.getMessage());
		}
		try {
			sistema.cadastrarCarona(sessaoMark, "Campina Grande",
					"Joao Pessoa", "30/25/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("Data inválida", e.getMessage());
		}
		try {
			sistema.cadastrarCarona(sessaoMark, "Campina Grande",
					"Joao Pessoa", "12/07/2013", "32:00", "3");
		} catch (Exception e) {
			assertEquals("Hora inválida", e.getMessage());
		}
		try {
			sistema.cadastrarCarona(sessaoMark, "Campina Grande",
					"Joao Pessoa", "20/07/2013", "14:00", "");
		} catch (Exception e) {
			assertEquals("Vaga inválida", e.getMessage());
		}
		sistema.cadastrarCarona(sessaoMark, "origemM", "destino", "12/07/2013",
				"14:00", "3");
	}

	@Test
	public void testaGetAtributoCarona() throws Exception {
		assertEquals("Campina Grande",
				sistema.buscaCaronaID(idCarona4).getOrigem());
		assertEquals("Joao Pessoa",
				sistema.buscaCaronaID(idCarona4).getDestino());
		assertEquals(3, sistema.buscaCaronaID(idCarona4).getVagas());
	
		 assertEquals(new ArrayList<String>(), sistema.buscaCaronaID(idCarona4).getPontoDeEncontro());
		 
		 Carona carona = sistema.buscaCaronaID(idCarona4);
		 carona.addPontoDeEncontro(new PontoDeEncontro(carona.getDonoDaCarona(),"parque da crianca"));
		 
		 List<String> respEsperada = new ArrayList<String>();
		 respEsperada.add("parque da crianca");
		 
		 
		 assertEquals(1, carona.getPontoDeEncontro().size());
		 carona.addPontoDeEncontro(new PontoDeEncontro(carona.getDonoDaCarona(), "acude"));
		 respEsperada.add("acude");
		 assertEquals(2, carona.getPontoDeEncontro().size());
		 
		 List<String> resposta = new LinkedList<String>();
		 for (PontoDeEncontro objetoPonto : carona.getPontoDeEncontro()) {
			 
			 resposta.add(objetoPonto.getPonto());			 
			
		}
		 
		 assertEquals(respEsperada, resposta);
		 
	}

	@Test
	public void testaLocalizarCarona() throws Exception {
		List<Carona> resp = new ArrayList<Carona>();
		assertEquals(resp, sistema.localizarCaronaInterMunicipal(sessaoMark, "São Francisco",
				"Palo Alto"));
	}

	@Test
	public void testaGetTrajeto() throws Exception {
		assertEquals("Campina Grande - Joao Pessoa",
				sistema.getTrajeto(idCarona4));
		try {
			sistema.getTrajeto(null);
		} catch (Exception e) {
			assertEquals("Trajeto Inválido", e.getMessage());
		}
		try {
			sistema.getTrajeto("");
		} catch (Exception e) {
			assertEquals("Trajeto Inexistente", e.getMessage());
		}
		try {
			sistema.getTrajeto("dcasdas");
		} catch (Exception e) {
			assertEquals("Trajeto Inexistente", e.getMessage());
		}

	}

	@Test
	public void testaGetCarona() throws Exception {
		assertEquals(
				"Campina Grande para Joao Pessoa, no dia 20/08/2012, as 14:00",
				sistema.getInformacoesCarona(idCarona4));
		try {
			sistema.getInformacoesCarona(null);
		} catch (Exception e) {
			assertEquals("Carona Inválida", e.getMessage());
		}
		try {
			sistema.getInformacoesCarona("");
		} catch (Exception e) {
			assertEquals("Carona Inexistente", e.getMessage());
		}
	}

	@Test
	public void testaSugerirPontoEncontro() throws Exception {
		Carona carona = sistema.buscaCaronaID(idCarona4);
		assertEquals(0, carona.getSugestoes().size());
		
		try {
			sistema.sugerirPontoEncontro(sessaoBill, idCarona4, "acude");
		} catch (Exception e) {
			assertEquals("Usuário não participa da carona", e.getMessage());
		}
		
		String idSolitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolitacao);
		sistema.sugerirPontoEncontro(sessaoBill, idCarona4, "acude");
		assertEquals(1, carona.getSugestoes().size());
	}

	@Test
	public void testaResponderSugestaoPontoEncontro() throws Exception {
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		String idSugestao = sistema.sugerirPontoEncontro(sessaoBill, idCarona4,
				"acude");	
		Sugestao sugestao = sistema.buscaSugestao(idSugestao, idCarona4);
		assertEquals(0, sugestao.getlistaDeResposta().size());
		sistema.responderSugestaoPontoEncontro(sessaoMark, idCarona4,
				idSugestao, "Parque Crianca");
		
		assertEquals(1,sistema.buscaCaronaID(idCarona4).getPontoDeEncontro().size());
		assertEquals("Parque Crianca",sistema.buscaCaronaID(idCarona4).getPontoDeEncontro().get(0).getPonto() );
	
	}

	@Test
	public void testaSolicitarVagaPontoEncontro() throws Exception {
		
		Carona carona = sistema.buscaCaronaID(idCarona4);
		
		try {
			sistema.solicitarVagaPontoEncontro(sessaoBill, idCarona4, "centro");
		} catch (Exception e) {
			assertEquals("Ponto Inválido", e.getMessage());
		}
		carona.addPontoDeEncontro(new PontoDeEncontro(sistema.buscaUsuario("mark"), "centro"));
		
		sistema.solicitarVagaPontoEncontro(sessaoBill, idCarona4, "centro");
		assertEquals("centro", carona.getPontoDeEncontro().get(0).getPonto());
		
		
	}

	@Test
	public void testaSolicitarVaga() throws Exception {
		sistema.solicitarVaga(sessaoBill, idCarona4);
		Carona carona = sistema.buscaCaronaID(idCarona4);
		int numeroDeVagas = carona.getVagas();
	
		assertEquals(1, carona.getListaDeSolicitacao().size());
		assertEquals(carona.getVagas(), numeroDeVagas);
		
	}
	
	@Test
	public void testaAceitarSolicitacao() throws Exception{
		
		Carona carona = sistema.buscaCaronaID(idCarona5);
		int numeroDeVagas = carona.getVagas();
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona5);
		
		assertEquals(carona.getVagas(), numeroDeVagas);
		
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		assertEquals(carona.getVagas(), numeroDeVagas-1);
		
		carona.setVagas(0);
		
		try {
			sistema.solicitarVaga(sessaoBill, idCarona5);
		} catch (Exception e) {
			assertEquals("o usuário já esta na carona", e.getMessage());
		}
		
		try {
			sistema.solicitarVaga(sessaoSteve, idCarona5);
		} catch (Exception e) {
			assertEquals("não há mais vagas na carona", e.getMessage());
		}
		
	}

	@Test
	public void testaGetAtributoSolicitacao() throws Exception {
		// origem, destino, Dono da carona, Dono da solicitacao, Ponto de
		// Encontro
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		assertEquals("Campina Grande",
				sistema.buscaCaronaID(sistema.buscaSolicitacao(idSolicitacao).getIdCarona()).getOrigem());
		assertEquals("Joao Pessoa",
				sistema.getAtributoSolicitacao(idSolicitacao, "destino"));
		assertEquals("Mark Zuckerberg",
				sistema.getAtributoSolicitacao(idSolicitacao, "Dono da carona"));
		assertEquals("William Henry Gates III", sistema.getAtributoSolicitacao(
				idSolicitacao, "Dono da solicitacao"));

		Solicitacao solicitacao = sistema.buscaSolicitacao(idSolicitacao);
		
		assertEquals(null, sistema.getAtributoSolicitacao(idSolicitacao,
				"Ponto de Encontro"));
		
		solicitacao.setPonto("Acude");
		
		assertEquals("Acude", solicitacao.getPonto());
		
		assertEquals(null, sistema.getAtributoSolicitacao(idSolicitacao,
				"Acude"));
		

	}

	@Test
	public void testaAceitarSolicitacaoPontoEncontro() throws Exception {
	    
		
		Carona carona = sistema.buscaCaronaID(idCarona4);
        int numeroDeVagas = carona.getVagas();
		carona.addPontoDeEncontro(new PontoDeEncontro(sistema.buscaUsuario("mark"),"acude"));

		String idSolicitacao = sistema.solicitarVagaPontoEncontro(sessaoBill, idCarona4,"acude");
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		assertEquals(carona.getVagas(), numeroDeVagas-1);
		
	}
	
	@Test
	public void testaCancelarCarona() throws Exception{
		int numeroDeCaronas = sistema.listaDeCaronas.size();
		Usuario usuario = sistema.buscaUsuario("mark");
		int caronasDoUsuario = usuario.getListaDeCaronasDoUsuario().size();
		
		sistema.cancelarCarona(sessaoMark, idCarona4);
		
		assertEquals(sistema.listaDeCaronas.size(), numeroDeCaronas-1);
		assertEquals(usuario.getListaDeCaronasDoUsuario().size(),caronasDoUsuario-1);
	}
	
	@Test
	public void testaRejeitarSolicitacao() throws Exception{
		
		String idSolicitacao = sistema.solicitarVaga(sessaoSteve, idCarona5);
		Carona carona = sistema.buscaCaronaID(idCarona5);
		assertEquals(1,carona.getListaDeSolicitacao().size());
		sistema.rejeitarSolicitacao(sessaoMark, idSolicitacao);
		assertEquals(0,carona.getListaDeSolicitacao().size());
		
	}
	
	@Test
	public void testaDesistirRequisicao() throws Exception{
		
        Usuario caroneiro = sistema.buscaUsuario("bill");
		Carona carona = sistema.buscaCaronaID(idCarona5);
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona5);
		
		assertEquals(1, carona.getListaDeSolicitacao().size());
		
		int numVagasCarona = carona.getVagas();
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		assertEquals(carona.getVagas(), numVagasCarona-1);
		assertEquals(1, caroneiro.getListaDeCaronasQueParticipa().size());
		assertEquals(0, carona.getListaDeSolicitacao().size());
		
		sistema.desistirRequisicao(sessaoBill, idCarona5);

		assertEquals(0, caroneiro.getListaDeCaronasQueParticipa().size());
		assertEquals(carona.getVagas(), numVagasCarona);
		
		
	}
	
	@Test
	public void testaDeletarPontoDeEncontro() throws Exception{
		
		Carona carona = sistema.buscaCaronaID(idCarona4);
		Usuario donoCarona = sistema.buscaUsuario("mark");
		
		PontoDeEncontro ponto = new PontoDeEncontro(donoCarona, "acude");
		carona.addPontoDeEncontro(ponto);
		
		assertEquals(1, carona.getPontoDeEncontro().size());
		
		sistema.deletarPontoDeEncontro(sessaoMark, idCarona4, ponto);
		
		assertEquals(0, carona.getPontoDeEncontro().size());
	
	}
	
	@Test
	public void TestaReviewCarona() throws Exception{
		
		GregorianCalendar calendar = new GregorianCalendar();
		
		Carona carona = sistema.buscaCaronaID(idCarona4);
		
		Usuario caroneiro = sistema.buscaUsuario("bill");
		Usuario donoDaCarona = sistema.buscaUsuario("mark");
		Usuario caroneiro2 = sistema.buscaUsuario("steve");
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		String idSolicitacao2 = sistema.solicitarVaga(sessaoSteve, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao2);
		
		
		assertEquals(2, carona.getListaDeParticipantes().size());
		assertEquals(2, sistema.listaDeCaronas.size());
		assertEquals(0, caroneiro.getFaltasEmCaronas());
		assertEquals(0, caroneiro.getHistoricoCaronas().size());
		assertEquals(0, caroneiro.getHistoricoVagasEmCaronas().size());
		assertEquals(0, caroneiro.getPresencaEmCaronas());
		
		assertEquals(0, caroneiro2.getFaltasEmCaronas());
		assertEquals(0, caroneiro2.getHistoricoCaronas().size());
		assertEquals(0, caroneiro2.getHistoricoVagasEmCaronas().size());
		assertEquals(0, caroneiro2.getPresencaEmCaronas());
		
		assertEquals(0, donoDaCarona.getCaronasNaoFuncionaram());
		assertEquals(0, donoDaCarona.getCaronasSeguras());
		assertEquals(0, donoDaCarona.getHistoricoCaronas().size());
		assertEquals(0, donoDaCarona.getHistoricoVagasEmCaronas().size());
		
		// so pode da review depois de um tempo minimo após a hora marcada para a carona
        calendar.add(calendar.HOUR_OF_DAY, -4);
        String hora = calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE);
        String data = calendar.get(calendar.DATE)+"/"+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR);
        carona.setData(data);
        carona.setHora(hora);
		sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
		
		// so adiciona o review se ele for seguro,se 65% dos caroneiros da carona derem o mesmo review
		assertEquals(0, donoDaCarona.getCaronasNaoFuncionaram());
		assertEquals(0, donoDaCarona.getCaronasSeguras());
		assertEquals(0, donoDaCarona.getHistoricoCaronas().size());
		assertEquals(0, donoDaCarona.getHistoricoVagasEmCaronas().size());
		
		sistema.reviewCarona(sessaoSteve, idCarona4, "segura e tranquila");
		
		assertEquals(0, donoDaCarona.getCaronasNaoFuncionaram());
		assertEquals(1, donoDaCarona.getCaronasSeguras());
		assertEquals(1, donoDaCarona.getHistoricoCaronas().size());
		assertEquals(0, donoDaCarona.getHistoricoVagasEmCaronas().size());
		
	    sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "bill", "não faltou");
	   
		assertEquals(0, caroneiro.getFaltasEmCaronas());
		assertEquals(0, caroneiro.getHistoricoCaronas().size());
		assertEquals(1, caroneiro.getHistoricoVagasEmCaronas().size());
		assertEquals(1, caroneiro.getPresencaEmCaronas());
		
		
		sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "steve", "faltou");
		
		assertEquals(1, caroneiro2.getFaltasEmCaronas());
		assertEquals(0, caroneiro2.getHistoricoCaronas().size());
		assertEquals(0, caroneiro2.getHistoricoVagasEmCaronas().size());
		assertEquals(0, caroneiro2.getPresencaEmCaronas());
		
		
		
	}
	
	@Test
	public void testaCadastrarInteresse() throws Exception{
		
		Usuario caroneiro = sistema.buscaUsuario("bill");
		
		sistema.cadastrarInteresse(sessaoBill, "", "", "", "", "");
		
		assertEquals(0, caroneiro.getListaDeMensagens().size());
		
		sistema.cadastrarCarona(sessaoMark, "Campina Grande", "João Pessoa", "15/06/2020", "15:00", "4");
		
		assertEquals(1, caroneiro.getListaDeMensagens().size());
		
		sistema.cadastrarInteresse(sessaoBill, "Campina Grande", "", "", "", "");
		
		sistema.cadastrarCarona(sessaoMark, "Campina Grande", "João Pessoa", "17/06/2020", "15:00", "3");
		
		assertEquals(3, caroneiro.getListaDeMensagens().size());
		
		sistema.cadastrarInteresse(sessaoBill, "", "", "", "15:00", "");
		
		sistema.cadastrarCarona(sessaoMark, "Campina Grande", "João Pessoa", "19/06/2020", "15:00", "3");
		
		assertEquals(6, caroneiro.getListaDeMensagens().size());
		
		sistema.cadastrarInteresse(sessaoBill, "", "", "", "16:00", ""); // fora do horario
		
		sistema.cadastrarCarona(sessaoMark, "Campina Grande", "João Pessoa", "20/06/2020", "15:00", "3");

		assertEquals(9, caroneiro.getListaDeMensagens().size());
	
	}
	
	@Test
	public void testaEnviaMensagem() throws Exception{
		
		Usuario usuario = sistema.buscaUsuario("bill");
		
		sistema.cadastrarInteresse(sessaoBill, "", "", "", "", "");
		
		assertEquals(0, usuario.getListaDeMensagens().size());
		
		sistema.cadastrarCarona(sessaoMark, "Campina", "Patos", "14/07/2013", "11:00", "3");

		assertEquals(1, usuario.getListaDeMensagens().size());
		
		String mensagem = "Carona cadastrada no dia 14/07/2013, Às 11:00 de acordo com os seus interesses registrados. Entrar em contato com mark@facebook.com";
	
		assertEquals(mensagem, usuario.getListaDeMensagens().get(0).getConteudo().getConteudo());
		
		Mensagem mensagem2 = new Mensagem("mark", "bill", new ConteudoTexto("mensagem de teste"));
		sistema.enviaMensagem(mensagem2);
		
		assertEquals(2, usuario.getListaDeMensagens().size());
		
		assertEquals("mensagem de teste", usuario.getListaDeMensagens().get(1).getConteudo().getConteudo());
		
		sistema.deletarMensagem(usuario, mensagem2);
		
		assertEquals(1,usuario.getListaDeMensagens().size());
		
	}
	
	@Test
	public void testaExcluiCaroneiroDaCarona() throws Exception{
		
	    Carona carona = sistema.buscaCaronaID(idCarona4);
		int numeroDeVagas = carona.getVagas();
		Usuario caroneiro = sistema.buscaUsuario("bill");
		
		int caronasQueParticipa = caroneiro.getListaDeCaronasQueParticipa().size();
		
		assertEquals(0, caronasQueParticipa);
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		assertEquals(1, carona.getListaDeParticipantes().size());
		assertEquals(carona.getVagas(), numeroDeVagas-1);
		assertEquals(1, caronasQueParticipa+1);
		
		sistema.excluirCaroneiroDaCarona(sessaoMark, idCarona4, "bill");
		
		assertEquals(0, carona.getListaDeParticipantes().size());
		assertEquals(carona.getVagas(), numeroDeVagas);
		assertEquals(0, caronasQueParticipa);
		
	}
	
	@Test
	public void testaCancelarInteresse() throws Exception{
		
		sistema.cadastrarInteresse(sessaoBill, "", "", "", "", "");
		
		assertEquals(1, sistema.listaDeInteresses.size());
		
        sistema.cadastrarCarona(sessaoSteve, "campina","joão pessoa", "15/12/2012", "14:00", "1");
        
        assertEquals(1, sistema.buscaUsuario("bill").getListaDeMensagens().size());
        
        try {
        	sistema.cancelarInteresse(sessaoMark, new Interesse(sessaoBill, "", "", "", "", ""));
		} catch (Exception e) {
			assertEquals("você não é o dono do interesse", e.getMessage());
		}
        
        sistema.cancelarInteresse(sessaoBill, new Interesse(sessaoBill, "", "", "", "", ""));
        
        sistema.cadastrarCarona(sessaoSteve, "campina","joão pessoa", "15/12/2012", "20:00", "1");
         
        assertEquals(1, sistema.buscaUsuario("bill").getListaDeMensagens().size());
        assertEquals(0, sistema.listaDeInteresses.size());
        
        
        
		
	}
	
	@Test
	public void testaParticipaDeCaronaNoHorario() throws Exception{
	  String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
	  sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
	  
	  Usuario usuario = sistema.buscaUsuario("bill");
	  assertEquals(false,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "11:59",usuario.getListaDeCaronasQueParticipa()));
	  
	  assertEquals(true,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "12:00",usuario.getListaDeCaronasQueParticipa()));
	  assertEquals(true,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "13:00",usuario.getListaDeCaronasQueParticipa()));
	  
	  assertEquals(true,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "14:00",usuario.getListaDeCaronasQueParticipa()));
	  assertEquals(true,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "15:00",usuario.getListaDeCaronasQueParticipa()));
	  assertEquals(true,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "16:00",usuario.getListaDeCaronasQueParticipa()));
	  
	  assertEquals(false,sistema.participaDeCaronaNoHorario(sessaoBill, "20/08/2012", "16:01",usuario.getListaDeCaronasQueParticipa()));
		
	}
	
	@Test
	public void testaTemCaronaNoHorario() throws Exception{
		
	  Usuario usuario = sistema.buscaUsuario("mark");
	  
      assertEquals(false,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "11:59",usuario.getListaDeCaronasDoUsuario()));
	  
	  assertEquals(true,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "12:00",usuario.getListaDeCaronasDoUsuario()));
	  assertEquals(true,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "13:00",usuario.getListaDeCaronasDoUsuario()));
	  
	  assertEquals(true,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "14:00",usuario.getListaDeCaronasDoUsuario()));
	  assertEquals(true,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "15:00",usuario.getListaDeCaronasDoUsuario()));
	  assertEquals(true,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "16:00",usuario.getListaDeCaronasDoUsuario()));
	  
	  assertEquals(false,sistema.temCaronaNoHorario(sessaoMark, "20/08/2012", "16:01",usuario.getListaDeCaronasDoUsuario()));
	  
	  
	}
	
	@Test
	
	public void testaCadastrarCaronaHorarioExistente() throws Exception{
		
		try {
			sistema.cadastrarCarona(sessaoMark, "campina", "joão pessoa","20/08/2012", "14:00", "3");
		} catch (Exception e) {
			 assertEquals("você já tem uma carona em um horario proximo", e.getMessage());
		}
		
		try {
			sistema.cadastrarCarona(sessaoMark, "campina", "joão pessoa","20/08/2012", "12:00", "3");
		} catch (Exception e) {
			 assertEquals("você já tem uma carona em um horario proximo", e.getMessage());
		}
		
		try {
			sistema.cadastrarCarona(sessaoMark, "campina", "joão pessoa","20/08/2012", "15:00", "3");
		} catch (Exception e) {
			 assertEquals("você já tem uma carona em um horario proximo", e.getMessage());
		}
		
		try {
			sistema.cadastrarCarona(sessaoMark, "campina", "joão pessoa","20/08/2012", "16:00", "3");
		} catch (Exception e) {
			 assertEquals("você já tem uma carona em um horario proximo", e.getMessage());
		}
		
       assertEquals(2, sistema.buscaUsuario("mark").getListaDeCaronasDoUsuario().size());
	   sistema.cadastrarCarona(sessaoMark, "campina", "joão pessoa","20/08/2012", "16:01", "3");
	   assertEquals(3, sistema.buscaUsuario("mark").getListaDeCaronasDoUsuario().size());




	}
	
	@Test
	public void TestaVagasEmCarona() throws Exception{
		
	
		 String idCarona = sistema.cadastrarCarona(sessaoMark, "campina", "jp", "15/07/2013", "14:00", "1");
		 String idSolicitacao1 = sistema.solicitarVaga(sessaoBill, idCarona);
		 String idSolicitacao2 = sistema.solicitarVaga(sessaoSteve, idCarona);
		 
		 assertEquals(1, sistema.buscaCaronaID(idCarona).getVagas());
		 sistema.aceitarSolicitacao(sessaoMark, idSolicitacao1);
		 assertEquals(0, sistema.buscaCaronaID(idCarona).getVagas());
		 try {
			 sistema.aceitarSolicitacao(sessaoMark, idSolicitacao2);
		} catch (Exception e) {
			assertEquals("solicitacao rejeitada por falta de vagas", e.getMessage());
		}
		 assertEquals(0, sistema.buscaCaronaID(idCarona).getVagas()); 

		 
		 
	}
	
	@Test
	public void testaSolicitacao() throws Exception{
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		String idCarona = sistema.cadastrarCarona(sessaoSteve, "Campina", "Caruaru", "12/06/2020", "15:00", "2");
		try {
			sistema.solicitarVaga(sessaoBill, idCarona);
		} catch (Exception e) {
			assertEquals("o usuário já tem ou paticipa de carona num horario proximo", e.getMessage());
		}
		
		Carona carona = sistema.buscaCaronaID(idCarona);
		carona.setHora("20:00");
		String idSolicitacao2 = sistema.solicitarVaga(sessaoBill, idCarona);
		sistema.aceitarSolicitacao(sessaoSteve, idSolicitacao2);
		
		assertEquals(2, sistema.buscaUsuario("bill").getListaDeCaronasQueParticipa().size());		

		
	}
	
	@Test	
	public void testaLocalizarCaronas() throws Exception{
		
		assertEquals(1, sistema.localizarCaronaInterMunicipal(sessaoMark, "Campina Grande", "Joao Pessoa").size());
		assertEquals(1, sistema.localizarCarona(sistema.listaDeCaronasInterMunicipais, sessaoMark, "Campina Grande", "Joao Pessoa").size());
		assertEquals(0, sistema.localizarCarona(sistema.listaDeCaronasMunicipais, sessaoMark, "Campina Grande", "Joao Pessoa").size());
		
		sistema.cadastrarCaronaMunicipal(sessaoMark, "bodocongo", "centro", "campina grande", "15/07/2013", "14:00", "3");
		
		assertEquals(0, sistema.localizarCarona(sistema.listaDeCaronasInterMunicipais, sessaoMark, "bodocongo", "centro").size());
		assertEquals(1, sistema.localizarCarona(sistema.listaDeCaronasMunicipais, sessaoMark, "bodocongo", "centro").size());
		assertEquals(1, sistema.localizarCaronaMunicipal(sessaoMark, "bodocongo", "centro", "campina grande").size());
	}
	
	@Test
	
	public void testaReview() throws Exception{
		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
		Carona carona = sistema.buscaCaronaID(sistema.buscaSolicitacao(idSolicitacao).getIdCarona());

		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		String idSolicitacao2 = sistema.solicitarVaga(sessaoSteve, idCarona4);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao2);
		

		
		GregorianCalendar calendar = new GregorianCalendar();
		// tenta da review quando a carona não ocorreu 
		try {
			sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
		} catch (Exception e) {
			assertEquals("você ainda não pode da review", e.getMessage());
		}
		
		try {
			sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "bill", "faltou");
		} catch (Exception e) {
			assertEquals("você ainda não pode da review", e.getMessage());
		}
		
		calendar.add(calendar.HOUR_OF_DAY, -4);
        String hora = calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE);
        String data = calendar.get(calendar.DATE)+"/"+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR);
        carona.setData(data);
        carona.setHora(hora);
        
        sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
        sistema.reviewCarona(sessaoSteve, idCarona4, "não funcionou");
        
        // review não seguro
        assertEquals(0, carona.getDonoDaCarona().getCaronasSeguras());
        assertEquals(0, carona.getDonoDaCarona().getCaronasNaoFuncionaram());
        
        carona.getListaDeReview().clear();
        
        sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
        sistema.reviewCarona(sessaoSteve, idCarona4, "segura e tranquila");
        
        // review seguro
        assertEquals(1, carona.getDonoDaCarona().getCaronasSeguras());
        assertEquals(0, carona.getDonoDaCarona().getCaronasNaoFuncionaram());
        
        
        try {
        	sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
		} catch (Exception e) {
			assertEquals("você ja deu review", e.getMessage());
		}
        
        sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "bill", "faltou");
        
        try {
        	sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "bill", "faltou");
		} catch (Exception e) {
			assertEquals("você ja deu review nesse caroneiro", e.getMessage());
		}
                
        
	}
	
	@Test
	public void testaCadastrarCaronaHoraExistente() throws Exception{
		
		try {
			sistema.cadastrarCarona(sessaoMark, "campina", "joao pessoa", "20/08/2012", "14:00", "3");
		} catch (Exception e) {
			assertEquals("você já tem uma carona em um horario proximo", e.getMessage());
		}
		 
       

	}
	
	@Test	
	public void testaCaronaRelampago() throws Exception{
		
		
		assertEquals(2, sistema.buscaUsuario("mark").getListaDeCaronasDoUsuario().size());
		
		// cadastra com 2 vagas e so uma eh preenchida
		String idCarona = sistema.cadastrarCaronaRelampago(sessaoMark, "campina", "natal", "20/10/2013", "14:00", "2");

		
		String idSolicitacao = sistema.solicitarVaga(sessaoBill,idCarona);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		
		Usuario caroneiro = sistema.buscaUsuario("bill");
		
		assertEquals(1, sistema.buscaUsuario("bill").getListaDeCaronasQueParticipa().size());
		assertEquals(3, sistema.buscaUsuario("mark").getListaDeCaronasDoUsuario().size());
		
		GregorianCalendar calendar  =  new GregorianCalendar();
		calendar.set(calendar.DAY_OF_YEAR, calendar.get(calendar.DAY_OF_YEAR)-2);
		calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY)-1);
		int diaPassado = calendar.get(calendar.DAY_OF_MONTH);
		int ano = calendar.get(calendar.YEAR);
		int mes = calendar.get(calendar.MONTH)+1;
		
		String dataPassada = ""+diaPassado+"/"+0+mes+"/"+ano;
		
	
		
		Carona carona = sistema.buscaCaronaID(idCarona);
		
		carona.setData(dataPassada);
		
     	assertEquals(3, sistema.listaDeCaronas.size());
		sistema.atualizaStatusCaronaRelampago((CaronaRelampago) carona);
		
		assertEquals(2, sistema.listaDeCaronas.size());
		assertEquals(0, caroneiro.getListaDeCaronasQueParticipa().size());
		
		
		// cadastra com 1 vagas e ela eh preenchida
        String idCarona2 = sistema.cadastrarCaronaRelampago(sessaoMark, "campina", "natal", "20/10/2013", "14:00", "1");

		String idSolicitacao2 = sistema.solicitarVaga(sessaoBill,idCarona2);
		sistema.aceitarSolicitacao(sessaoMark, idSolicitacao2);
		
		Usuario caroneiro2 = sistema.buscaUsuario("bill");
		
		assertEquals(1, sistema.buscaUsuario("bill").getListaDeCaronasQueParticipa().size());
		assertEquals(3, sistema.buscaUsuario("mark").getListaDeCaronasDoUsuario().size());
		
        Carona carona2 = sistema.buscaCaronaID(idCarona2);
        carona2.setData(dataPassada);
        
        assertEquals(3, sistema.listaDeCaronas.size());
		assertEquals(1, caroneiro.getListaDeCaronasQueParticipa().size());
		
		
		
			
	}
	
	@Test
    public void testaReviewPositivos() throws Exception{
		
		GregorianCalendar calendar = new GregorianCalendar();
		
		
	    String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona4);
	    sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
	    
	    Usuario caroneiro = sistema.buscaUsuario("bill");
	    Usuario donoDaCarona = sistema.buscaUsuario("mark");
	    Carona carona = sistema.buscaCaronaID(idCarona4);
	    try {
	    	
	    	sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
			
		} catch (Exception e) {
			assertEquals("você ainda não pode da review",e.getMessage());
			assertEquals(0,donoDaCarona.getListaReviewPositivos().size());
		    
		}
	    calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY)-3);
	    String dataAtual = calendar.get(calendar.DAY_OF_MONTH)+"/"+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR);
      
	    carona.setData(dataAtual);
	    
	    sistema.reviewCarona(sessaoBill, idCarona4, "segura e tranquila");
	    
	    assertEquals(1, donoDaCarona.getListaReviewPositivos().size());
	    
	    sistema.reviewVagaEmCarona(sessaoMark, idCarona4, "bill", "faltou");
	    
	    assertEquals(0, caroneiro.getListaReviewPositivos().size());
	  	
			
	}
	
	@Test
	
	public void testaCaronaPreferencial() throws Exception{
		
		GregorianCalendar calendar = new GregorianCalendar();
		
		 
		 String idSolicitacao = sistema.solicitarVaga(sessaoBill, idCarona5);
		 String idSolicitacao2 = sistema.solicitarVaga(sessaoSteve, idCarona5);
		 sistema.aceitarSolicitacao(sessaoMark, idSolicitacao);
		 sistema.aceitarSolicitacao(sessaoMark, idSolicitacao2);
		 
		 
		 Usuario caroneiro = sistema.buscaUsuario("bill");
		 Usuario caroneiro2 = sistema.buscaUsuario("steve");
		 Usuario donoDaCarona = sistema.buscaUsuario("mark");
		 Carona carona = sistema.buscaCaronaID(idCarona5);
		 
		 calendar.set(calendar.DAY_OF_YEAR, calendar.get(calendar.DAY_OF_YEAR)-2);
		 String dataAtual = ""+calendar.get(calendar.DAY_OF_MONTH)+"/"+0+""+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR);
	     carona.setData(dataAtual);

	     System.out.println(calendar.getTime());
	     
	     assertEquals(0, carona.getListaDeUsuariosPreferencias().size());
	     assertEquals(0, donoDaCarona.getListaReviewPositivos().size());
	     
	     sistema.reviewCarona(sessaoBill, idCarona5, "segura e tranquila");
	     
	     
	     assertEquals(0, carona.getListaDeUsuariosPreferencias().size());
	     assertEquals(1, donoDaCarona.getListaReviewPositivos().size());
         
	     sistema.reviewCarona(sessaoSteve, idCarona5, "não funcionou");
	     assertEquals(1, donoDaCarona.getListaReviewPositivos().size());
	     
	     Carona caronaPreferencial = sistema.buscaCaronaID(idCarona4);
	     sistema.difinirCaronaPreferencial(caronaPreferencial);
	     
	     assertEquals(1, caronaPreferencial.getListaDeUsuariosPreferencias().size());
	     
	     assertEquals(true, sistema.usuarioTemPreferenciaNaCarona("bill", caronaPreferencial));
	     
	     assertFalse(sistema.usuarioTemPreferenciaNaCarona("usuario qualquer", caronaPreferencial));
	     
	     try {
			sistema.solicitarVaga(sessaoSteve, idCarona4);
		} catch (Exception e) {
			assertEquals("Usuário não está na lista preferencial da carona", e.getMessage());
		}
	     
	     Carona carona2 = sistema.buscaCaronaID(idCarona4);
	     calendar = new GregorianCalendar();
	     calendar.set(calendar.DAY_OF_YEAR, calendar.get(calendar.DAY_OF_YEAR)-1);
		 String dataAtual2 = ""+calendar.get(calendar.DAY_OF_MONTH)+"/"+0+""+(calendar.get(calendar.MONTH)+1)+"/"+calendar.get(calendar.YEAR);
	     carona2.setData(dataAtual2);
	     
	     sistema.atualizaCaronaPreferencial(carona2);
	     sistema.solicitarVaga(sessaoSteve, idCarona4);
	     assertEquals(1, carona2.getListaDeSolicitacao().size());
	          
		
	}
	
	

	
	
	
	
	
	
	

}
