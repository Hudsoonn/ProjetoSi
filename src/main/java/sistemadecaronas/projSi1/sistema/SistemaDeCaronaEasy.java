package sistemadecaronas.projSi1.sistema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sistemadecaronas.projSi1.auxiliar.TrataDatas;
import sistemadecaronas.projSi1.persistencia.Serializador;


public class SistemaDeCaronaEasy {

	/**
	 * @param args
	 */
	public List<Usuario> listaDeUsuarios = new ArrayList<Usuario>();
	public List<Carona> listaDeCaronas = new ArrayList<Carona>();
	public List<CaronaIntermunicipal> listaDeCaronasInterMunicipais = new ArrayList<CaronaIntermunicipal>();
	public List<CaronaMunicipal> listaDeCaronasMunicipais = new ArrayList<CaronaMunicipal>();
	public List<Sessao> listaDeSessoesAbertas = new ArrayList<Sessao>();
	public List<Interesse> listaDeInteresses = new ArrayList<Interesse>();
	private boolean desistirSolicitacao;

	public SistemaDeCaronaEasy() {
	}


	public void criarUsuario(String login, String senha, String nome,
			String endereco, String email) throws Exception {

		excecaoCriarUsuario(login, senha, nome, endereco, email);

		Usuario novoUsuario = new Usuario(login, senha, nome, endereco, email);
		listaDeUsuarios.add(novoUsuario);

	}

	public void encerrarSistema() {

		Serializador<Collection> ser = Serializador.getInstanceOf();
		
		ser.salvar("Usuarios", this.listaDeUsuarios);
		ser.salvar("Caronas", this.listaDeCaronas);
		ser.salvar("Interesses", this.listaDeInteresses);
        
		System.out.println("Sistema Encerrado");
	}

	public String abrirSessao(String login, String senha) throws Exception {

		boolean sessaoAberta = false;
		String id = null;
		if (login == null || login.equals("")) {
			throw new Exception("Login inválido");
		}
		if (senha == null || senha.equals("")) {
			throw new Exception("Senha inválida");
		}

		if (buscaUsuario(login) != null) {
			if (buscaUsuario(login).getSenha().equals(senha)) {
				for (Sessao sessao : listaDeSessoesAbertas) {
					if (sessao.getLogin().equals(login)) {
						sessaoAberta = true;
						id = sessao.getId();
						break;
					}

				}
				if (!sessaoAberta) {
					if (buscaUsuario(login).getId() == null) {
						Sessao sessao = new Sessao(login, senha);
						listaDeSessoesAbertas.add(sessao);
						id = sessao.getId();
						buscaUsuario(login).setId(id);
					} else {
						Sessao sessao = new Sessao(login, senha);
						sessao.setID(buscaUsuario(login).getId());
						listaDeSessoesAbertas.add(sessao);
						id = sessao.getId();
					}

				}

			} else {

				throw new Exception("Login inválido");
			}

		} else {

			throw new Exception("Usuário inexistente");
		}

		return id;

	}

	// getAtributoUsuario
	public String getAtributoUsuario(String login, String atributo)
			throws Exception {

		excecaoDeAtributosInvalidos(login, atributo); // lança qualquer excecao
														// se o login ou o
														// atributo estiver
														// incorreto

		if (atributo.equals("nome")) {
			return buscaUsuario(login).getNome();
		}
		if (atributo.equals("endereco")) {
			return buscaUsuario(login).getEndereco();
		}

		throw new Exception("error");

	}

	public void excecaoDeAtributosInvalidos(String login, String atributo)
			throws Exception {

		if (login == null || login.equals("")) {
			throw new Exception("Login inválido");
		}
		if (atributo == null || atributo.equals("")) {
			throw new Exception("Atributo inválido");
		}

		if (!atributo.equals("nome") && !atributo.equals("endereco")
				&& !atributo.equals("login") && !atributo.equals("senha")
				&& !atributo.equals("email")) {
			throw new Exception("Atributo inexistente");
		}

		if (buscaUsuario(login) == null) {
			throw new Exception("Usuário inexistente");
		}
	}

	public String getAtributoCarona(String idDaCarona, String atributo)
			throws Exception {
		String saida = "";
		excecaoDeAtributosCaronaInvalidos(idDaCarona, atributo);

		Carona carona = buscaCaronaID(idDaCarona);

		if (atributo.equals("origem")) {
			saida = carona.getOrigem();
		}
		else if (atributo.equals("destino")) {
			saida = carona.getDestino();
		}
		else if (atributo.equals("data")) {
			saida = carona.getData();
		}

		else if (atributo.equals("vagas")) {
			saida = Integer.toString(carona.getVagas());
		}

		else if (atributo.equals("Ponto de Encontro")) {
			if (!carona.getPontoDeEncontro().isEmpty()) {
				saida = carona.getPontoDeEncontro().toString();
				saida = saida.replace("[", "");
				saida = saida.replace("]", "");

			}

		}
		else if (atributo.equals("ehMunicipal")) {
			saida = String.valueOf(carona.tipoDeCarona().equals("Municipal"));
		}

		return saida;
	}

	public void excecaoDeAtributosCaronaInvalidos(String idDaCarona,
			String atributo) throws Exception {

		if (idDaCarona == null || idDaCarona.equals("")) {
			throw new Exception("Identificador da carona é inválido");
		}
		if (buscaCaronaID(idDaCarona) == null) {
			throw new Exception("Item inexistente");
		} else if (atributo == null || atributo.equals("")) {
			throw new Exception("Atributo inválido");
		} else if (!atributo.equals("origem") && !atributo.equals("destino")
				&& !atributo.equals("data") && !atributo.equals("vagas")
				&& !atributo.equals("Ponto de Encontro") && !atributo.equals("ehMunicipal")) {
			throw new Exception("Atributo inexistente");
		}
	}

	public void excecaoDeCriacaoDeCarona(String idDaSessao, String origem,
			String destino, String data, String hora, String vagas)
			throws Exception {

		if (idDaSessao == null || idDaSessao.equals("")) {
			throw new Exception("Sessão inválida");
		}

		if (!isSessaoAberta(idDaSessao)) {
			throw new Exception("Sessão inexistente");
		}
		if (origem == null || origem.equals("")) {
			throw new Exception("Origem inválida");
		}

		if (destino == null || destino.equals("")) {
			throw new Exception("Destino inválido");
		}
		
		if (hora == null || hora.equals("") || !TrataDatas.horaValida(hora)) {
			throw new Exception("Hora inválida");
		}

		if (data == null || data.equals("") || !TrataDatas.isDataValida(data,hora)) {
			throw new Exception("Data inválida");
		}


		if (vagas == null || vagas.equals("")) {
			throw new Exception("Vaga inválida");
		} else {

			try {
				Integer.parseInt(vagas);
			} catch (Exception e) {
				throw new Exception("Vaga inválida");
			}
		}

	}

	public Carona buscaCaronaID(String idCarona) {

		Iterator<Carona> itListaDeCaronas = listaDeCaronas.iterator();
		Carona saida = null;

		while (itListaDeCaronas.hasNext()) {
			Carona carona = (Carona) itListaDeCaronas.next();
			if (carona.getIdDaCarona().equals(idCarona)) {
				saida = carona;
				break;

			}

		}

		return saida;

	}

	public void excecaoCriarUsuario(String login, String senha, String nome,
			String endereco, String email) throws Exception {
		if (login == null || login.equals("")) {
			throw new Exception("Login inválido");
		}
		if (nome == null || nome.equals("")) {
			throw new Exception("Nome inválido");
		}

		if (email == null || email.equals("")) {
			throw new Exception("Email inválido");
		}

		for (int i = 0; i < listaDeUsuarios.size(); i++) {
			if (listaDeUsuarios.get(i).getLogin().equals(login)) {
				throw new Exception("Já existe um usuário com este login");
			}
			if (listaDeUsuarios.get(i).getEmail().equals(email)) {
				throw new Exception("Já existe um usuário com este email");
			}
		}

	}

	public List<Usuario> getUsuarios() {
		return listaDeUsuarios;
	}

	// Busca Usuario e retorna um usuario a partir do login
	public Usuario buscaUsuario(String login) {
		for (int i = 0; i < listaDeUsuarios.size(); i++) {
			if (listaDeUsuarios.get(i).getLogin().equals(login)) {
				return listaDeUsuarios.get(i);
			}
		}
		return null;
	}
	
	public Usuario buscaUsuarioId(String id) {
		for (int i = 0; i < listaDeUsuarios.size(); i++) {
			
			if (id != null) {
			if (listaDeUsuarios.get(i).getId().equals(id)) {
				
				return listaDeUsuarios.get(i);
			}
		  }
		}
		return null;
	}

	public boolean isSessaoAberta(String id) {

		boolean existeSessaoAberta = false;

		for (Sessao sessao : listaDeSessoesAbertas) {
			if (sessao.getId().equals(id)) {

				existeSessaoAberta = true;
				break;
			}
		}

		return existeSessaoAberta;

	}

	public String cadastrarCarona(String idDaSessao, String origem,
			String destino, String data, String hora, String vagas)
			throws Exception {
		excecaoDeCriacaoDeCarona(idDaSessao, origem, destino, data, hora, vagas);
		Sessao sessao = buscarSessaoId(idDaSessao);
		int vagasInt = Integer.parseInt(vagas);
		Carona novaCarona = new CaronaIntermunicipal(origem, destino, data,
				hora, vagasInt);
		novaCarona.setDonoDaCarona(buscaUsuario(sessao.getLogin()));
		listaDeCaronas.add(novaCarona);//
		listaDeCaronasInterMunicipais.add((CaronaIntermunicipal) novaCarona);

		String login = sessao.getLogin();
		String idCarona = novaCarona.getIdDaCarona();
		addCaronaNoHistorico(login, idCarona);

		Usuario usuario = buscaUsuario(sessao.getLogin());
		usuario.addCarona(novaCarona);
		
		//Verificar se existe interesse
		verificaInteresse(idDaSessao, origem, destino, data, hora);
		
		return novaCarona.getIdDaCarona();

	}
	
	public String cadastrarCaronaMunicipal(String idDaSessao, String origem,
			String destino, String cidade, String data, String hora,
			String vagas) throws Exception {
		excecaoDeCriacaoDeCarona(idDaSessao, origem, destino, data, hora, vagas);
		Sessao sessao = buscarSessaoId(idDaSessao);
		int vagasInt = Integer.parseInt(vagas);
		Carona novaCarona = new CaronaMunicipal(origem, destino, cidade,
				data, hora, vagasInt);
		novaCarona.setDonoDaCarona(buscaUsuario(sessao.getLogin()));

		listaDeCaronas.add(novaCarona);//
		listaDeCaronasMunicipais.add((CaronaMunicipal) novaCarona);

		String login = sessao.getLogin();
		String idCarona = novaCarona.getIdDaCarona();
		addCaronaNoHistorico(login, idCarona);

		Usuario usuario = buscaUsuario(login);
		usuario.addCarona(novaCarona);
		
		verificaInteresse(idDaSessao, origem, destino, data, hora);
		

		return novaCarona.getIdDaCarona();

	}

	private void verificaInteresse(String idDaSessao, String origem,
			String destino, String data, String hora) throws Exception {
		Carona carona = new CaronaIntermunicipal(origem, destino, data, hora, 0);
		for(Interesse interesse : listaDeInteresses)
		{
			if(caronaEhDeInteresse(carona, interesse))
			{   
				String loginDonoCarona = buscarSessaoId(idDaSessao).getLogin();
				String loginCaroneiro = buscaUsuarioId((interesse.getIdSessao())).getLogin();
				Conteudo conteudo =  new ConteudoTexto(String.format(mensagensDoSistema.MENSAGEM_INTERESSE.getMensagem(),data,hora,buscaUsuario(loginDonoCarona).getEmail()));
				Mensagem mensagem = new Mensagem(loginDonoCarona, loginCaroneiro, conteudo);
				
				enviaMensagem(mensagem);
			}
		}
		
	}
	
	public boolean caronaEhDeInteresse(Carona carona,Interesse interesse) throws Exception{		
		boolean ehDeInteresse = false;
		
		if (interesse.getOrigem().equals("") || interesse.getOrigem().equals(carona.getOrigem())) {
			ehDeInteresse = true;
		}else if (!interesse.getOrigem().equals(carona.getOrigem())) {
			ehDeInteresse = false;
			
		}
		if (!interesse.getDestino().equals(carona.getDestino()) && !interesse.getDestino().equals("")) {
			ehDeInteresse = false;
		}
		
		if (!comparaHoras(carona.getHora(), interesse.getHoraInicial(), interesse.getHoraFim())) {
			ehDeInteresse = false;
		}
		
		if (!interesse.getData().equals(carona.getData()) && !interesse.getData().equals("")) {
			ehDeInteresse = false;
		}
			
		
		return ehDeInteresse;
		
	}

	public boolean comparaHoras(String hora, String horaInicial, String horaFim) throws ParseException {
		   SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");   
		   Date horaI = formatter.parse(horaInicial);
		   Date horaF = formatter.parse(horaFim); 
		   Date horaCarona = formatter.parse(hora);
		   boolean saida = false;
		   if (horaInicial.equals("") && horaFim.equals("")) {
			saida = true;
		   }else if (horaInicial.equals("") && !horaFim.equals("") && horaF.getTime() >= horaCarona.getTime()) {
			  saida = true;
		   }
		   else if (horaFim.equals("") && !horaInicial.equals("") && horaI.getTime() <= horaCarona.getTime()) {
			saida = true;
			
		} else if(horaCarona.getTime() >= horaI.getTime() && horaCarona.getTime() <= horaF.getTime()) {
			   saida = true;
		   }

				   
			return saida;
	}
	
	public enum mensagensDoSistema{
		MENSAGEM_INTERESSE("Carona cadastrada no dia %s, Às %s de acordo com os seus interesses registrados. Entrar em contato com %s");
		
		String mensagem;
	    mensagensDoSistema(String mensagem){
		 this.mensagem = mensagem;
		}
		
		public String getMensagem(){
			
   		   return mensagem;
		}
	}


	public void enviaMensagem(Mensagem mensagem) throws Exception {
		
		Usuario destinatario = buscaUsuario(mensagem.getDestinatario());
		destinatario.addMensagem(mensagem);
	}
	
	public String verificarMensagensPerfil(String idSessao)
	{
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		List<String> mensagens = new LinkedList<String>();
		for (Mensagem mensagem : usuario.getListaDeMensagens()) {
			mensagens.add((String) mensagem.getConteudo().getConteudo());
		}
		return mensagens.toString();
	}
	



	public String localizarCarona(String idSessao, String origem, String destino)
			throws Exception {

		excecaoLocalizarCarona(idSessao, origem, destino);
		List<String> caronasEncontradas = new ArrayList<String>();
		String strCaronas = null;

		if (isSessaoAberta(idSessao)) {

			for (Carona carona : listaDeCaronas) {

				if (!origem.equals("") && !destino.equals("")) { // lista todas
																	// as
																	// caronas
																	// de uma
																	// determinada
																	// origem
																	// até um
																	// destino

					if (carona.getDestino().equals(destino)
							&& carona.getOrigem().equals(origem)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (!origem.equals("") && destino.equals("")) { // lista todas
																// as caronas
																// daquela
																// origem
					if (carona.getOrigem().equals(origem)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (origem.equals("") && !destino.equals("")) { // lista todas
																// as caronas
																// para aquele
																// destino
					if (carona.getDestino().equals(destino)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (origem.equals("") && destino.equals("")) { // lista todas as
																// caronas

					caronasEncontradas.add(carona.getIdDaCarona());

				}

			}

		}

		strCaronas = caronasEncontradas.toString();
		strCaronas = strCaronas.replace("[", "{");
		strCaronas = strCaronas.replace("]", "}");
		strCaronas = strCaronas.replace(" ", "");

		return strCaronas;
	}

	public String localizarCaronaMunicipal(String idSessao, String cidade,
			String origem, String destino) throws Exception {
		excecaoCidade(cidade);
		excecaoLocalizarCarona(idSessao, origem, destino);// adicionar excecao
															// cidade
		List<String> caronasEncontradas = new ArrayList<String>();
		String strCaronas = null;

		if (isSessaoAberta(idSessao)) {

			for (CaronaMunicipal carona : listaDeCaronasMunicipais) {

				if (!origem.equals("") && !destino.equals("")
						&& !cidade.equals("")) { // lista todas
					// as
					// caronas
					// de uma
					// determinada
					// origem
					// até um
					// destino

					if (carona.getDestino().equals(destino)
							&& carona.getOrigem().equals(origem)
							&& carona.getCidade().equals(cidade)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (!origem.equals("") && destino.equals("")
						&& !cidade.equals("")) { // lista todas
					// as caronas
					// daquela
					// origem
					if (carona.getOrigem().equals(origem)
							&& carona.getCidade().equals(cidade)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (origem.equals("") && !destino.equals("")
						&& !cidade.equals("")) { // lista todas
												// as caronas
												// para aquele
												// destino
					if (carona.getDestino().equals(destino)
							&& carona.getCidade().equals(cidade)) {

						caronasEncontradas.add(carona.getIdDaCarona());

					}
				}

				if (origem.equals("") && destino.equals("") && !cidade.equals("")) { // lista todas as
					// caronas

					caronasEncontradas.add(carona.getIdDaCarona());

				}

			}

		}

		strCaronas = caronasEncontradas.toString();
		strCaronas = strCaronas.replace("[", "{");
		strCaronas = strCaronas.replace("]", "}");
		strCaronas = strCaronas.replace(" ", "");

		return strCaronas;
	}

	// fazer excecao cidade
	public String localizarCaronaMunicipal(String idSessao, String cidade)
			throws Exception {
		excecaoCidade(cidade);
		List<String> caronasEncontradas = new ArrayList<String>();
		String strCaronas = null;

		if (isSessaoAberta(idSessao)) {

			for (CaronaMunicipal carona : listaDeCaronasMunicipais) {

				if (carona.getCidade().equals(cidade)) { 

					caronasEncontradas.add(carona.getIdDaCarona());

					
				}

				
			}

		}

		strCaronas = caronasEncontradas.toString();
		strCaronas = strCaronas.replace("[", "{");
		strCaronas = strCaronas.replace("]", "}");
		strCaronas = strCaronas.replace(" ", "");

		return strCaronas;
	}

	public void excecaoLocalizarCarona(String idDaSessao, String origem,
			String destino) throws Exception {
		if (idDaSessao == null) {
			throw new Exception("Sessão inválida");
		}
		if (idDaSessao.equals("") || !isSessaoAberta(idDaSessao)) {

			throw new Exception("Sessão inexistente");
		}

		if (origem == null) {
			throw new Exception("Origem Inexistente");
		} else {

			for (char c : origem.toCharArray()) {
				if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
					throw new Exception("Origem inválida");
				}
			}
		}

		if (destino == null) {
			throw new Exception("Destino Inexistente");
		} else {
			for (char c : destino.toCharArray()) {
				if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
					throw new Exception("Destino inválido");
				}
			}
		}

	}
	
	public void excecaoCidade(String cidade) throws Exception
	{
		if(cidade == null || cidade.equals(""))
			throw new Exception("Cidade inexistente");
	}

	public Sessao buscarSessaoId(String idSessao) {
		Sessao sessao = null;
		for (Sessao sessao1 : listaDeSessoesAbertas) {
			if (sessao1.getId().equals(idSessao)) {
				sessao = sessao1;
				break;
			}
		}
		return sessao;
	}

	public String getTrajeto(String idDaCarona) throws Exception {
		excecaoGetTrajeto(idDaCarona);
		return buscaCaronaID(idDaCarona).getOrigem() + " - "
				+ buscaCaronaID(idDaCarona).getDestino();
	}

	public void excecaoGetTrajeto(String idDaCarona) throws Exception {

		if (idDaCarona == null) {
			throw new Exception("Trajeto Inválido");
		}
		if (idDaCarona.equals("")) {
			throw new Exception("Trajeto Inexistente");
		}

		if (buscaCaronaID(idDaCarona) == null) {

			throw new Exception("Trajeto Inexistente");
		}
	}

	public String getCarona(String idDaCarona) throws Exception {
		excecaoGetCarona(idDaCarona);
		Carona carona = buscaCaronaID(idDaCarona);
		return carona.getOrigem() + " para " + carona.getDestino()
				+ ", no dia " + carona.getData() + ", as " + carona.getHora();

	}

	public void excecaoGetCarona(String idDaCarona) throws Exception {
		if (idDaCarona == null) {
			throw new Exception("Carona Inválida");

		}
		if (idDaCarona.equals("") || buscaCaronaID(idDaCarona) == null) {

			throw new Exception("Carona Inexistente");

		}

	}

	public void encerrarSessao(String login) {
		for (Sessao sessao1 : listaDeSessoesAbertas) {
			if (sessao1.getLogin().equals(login)) {
				listaDeSessoesAbertas.remove(sessao1);
				break;
			}

		}

	}

	public String sugerirPontoEncontro(String idSessao, String idCarona,
			String pontos) throws Exception {
		excecaoDesistirSolicitacao(desistirSolicitacao);
		Carona carona = buscaCaronaID(idCarona);
		Sugestao sugestao = new Sugestao(pontos, idSessao);
		carona.addSugestao(sugestao); 

		return sugestao.getIdSugestao();
	}

	public String responderSugestaoPontoEncontro(String idSessao,
			String idCarona, String idSugestao, String pontos) throws Exception {

		String idResposta = null;
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		excecaoResponderPontoDeEncontro(pontos);

		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // verifica
																				// se
																				// o
																				// login
																				// de
																				// quem
																				// ta
																				// logado
																				// eh
																				// igual
																				// ao
																				// do
																				// dono
																				// da
																				// carona

			for (Sugestao sugestao : carona.getSugestoes()) {
				if (sugestao.getIdSugestao().equals(idSugestao)) { // verificase
																	// a
																	// sugestao
																	// eh igual
																	// a que eu
																	// procuro

					Resposta resp = new Resposta(pontos); // cria uma resposta
					idResposta = resp.getIdResposta();
					sugestao.addResposta(resp); // adiciona em uma lista a
												// resposta dessa sugestao

				}

			}
		}

		return idResposta;
	}

	public String solicitarVagaPontoEncontro(String idSessao, String idCarona,
			String ponto) throws Exception {
		/*
		 * 1- Colocar solicitacao em carona 2- Return ID
		 */
		excecaoDesistirSolicitacao(desistirSolicitacao);
		Carona carona = buscaCaronaID(idCarona);
		Solicitacao solicitacao = new Solicitacao(idSessao, idCarona, ponto);
		excecaoSolicitacao(ponto, carona);

		for (Sugestao sugestao : carona.getSugestoes()) {
			for (Resposta resposta : sugestao.getlistaDeResposta()) {
				if (resposta.getPontos().contains(ponto)) { // verifica no mapa
															// de sugestoes e
															// respostas se
															// alguma resposta
															// contem o ponto
															// desejado para a
															// carona
					carona.addSolicitacao(solicitacao); // se o ponto esta no
														// mapa das sugestoes e
														// resposta adiciona
														// essa solicitacao numa
														// lista;
				}

			}

		}

		return solicitacao.getIdSolicitacao();
	}

	public String solicitarVaga(String idSessao, String idCarona) {
		Carona carona = buscaCaronaID(idCarona);
		Solicitacao solicitacao = new Solicitacao(idSessao, idCarona);

		carona.addSolicitacao(solicitacao);

		return solicitacao.getIdSolicitacao();
	}

	public String getAtributoSolicitacao(String idSolicitacao, String atributo)
			throws Exception {

		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		String resposta = null;

		if (atributo.equals("origem")) {
			resposta = carona.getOrigem();
		} else if (atributo.equals("destino")) {
			resposta = carona.getDestino();
		} else if (atributo.equals("Dono da carona")) {
			resposta = carona.getDonoDaCarona().getNome();
		} else if (atributo.equals("Dono da solicitacao")) {
			Usuario usuario = buscaUsuario((buscarSessaoId(solicitacao
					.getIdSessao()).getLogin()));
			resposta = usuario.getNome();
		} else if (atributo.equals("Ponto de Encontro")) {
			for (Solicitacao sol : carona.getListaDeSolicitacao()) {
				if (sol.getIdSolicitacao().equals(idSolicitacao)) {
					resposta = sol.getPonto();
				}
			}
		}
		return resposta;
	}

	public Solicitacao buscaSolicitacao(String idSolicitacao) throws Exception {
		Solicitacao solicitacao = null;
		for (Carona carona1 : listaDeCaronas) {
			for (Solicitacao solicitacao1 : carona1.getListaDeSolicitacao()) {
				if (solicitacao1.getIdSolicitacao().equals(idSolicitacao)) {
					solicitacao = solicitacao1;
					break;
				}

			}
		}
		if (solicitacao == null) {
			throw new Exception("Solicitação inexistente");
		}
		return solicitacao;
	}

	public Sugestao buscaSugestao(String idSugestao, String idCarona) {
		Sugestao sugestaoEncontrada = null;
		Carona carona = buscaCaronaID(idCarona);
		for (Sugestao sugestao : carona.getSugestoes()) {
			if (sugestao.getIdSugestao().equals(idSugestao)) {
				sugestaoEncontrada = sugestao;
			}
		}
		return sugestaoEncontrada;
	}

	public void aceitarSolicitacaoPontoEncontro(String idSessao,
			String idSolicitacao) throws Exception {
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
        
		Sessao sessaoDeQuemSolicitou = buscarSessaoId(solicitacao.getIdSessao());
	    Usuario usuarioQueSolicitou = buscaUsuario(sessaoDeQuemSolicitou.getLogin());
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // se
																				// o
																				// dono
																				// da
																				// carona
																				// bate
																				// com
																				// o
																				// id
																				// logado

			carona.setVagas(carona.getVagas() - 1); // diminiu uma vaga na
													// carona

			carona.removeSolicitacao(solicitacao); // remove a solicitacao
													// porque ja foi aceita
			carona.addPontoDeEncontro(new PontoDeEncontro(usuarioQueSolicitou, solicitacao.getPonto())); // adiciona o
																// ponto de
																// encontro da
																// solicitacao
																// em uma lista
																// de pontos de
																// encontro para
																// a carona

			for (Sugestao sugest : carona.getSugestoes()) {
				if (sugest.getIdSessao().equals(solicitacao.getIdSessao())) { // se
																				// a
																				// sugestao
																				// e
																				// a
																				// solicitacao
																				// foram
																				// feitas
																				// pelo
																				// mesmo
																				// usuario

					carona.removeSugestao(sugest); // remove a sugestao da lista
													// de sugestoes porque ela
													// ja foi aceita
					break;
				}
			}
		}
	}

	public void aceitarSolicitacao(String idSessao, String idSolicitacao)
			throws Exception {
		
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Sessao sessaoDeQuemSolicitou = buscarSessaoId(solicitacao.getIdSessao());
	    Usuario usuarioQueSolicitou = buscaUsuario(sessaoDeQuemSolicitou.getLogin());

		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		carona.addSolicitacaoAceita(solicitacao);
		carona.removeSolicitacao(solicitacao);
		carona.setVagas(carona.getVagas() - 1);
		addhistoricoVagasEmCaronas(buscarSessaoId(solicitacao.getIdSessao())
				.getLogin(), carona.getIdDaCarona());

		List<PontoDeEncontro> pontoDeEncontro = new ArrayList<PontoDeEncontro>();
		if (solicitacao.getPonto() == null
				&& carona.getPontoDeEncontro().isEmpty()) {
			for (Sugestao sugestao : carona.getSugestoes()) {
				pontoDeEncontro.add(new PontoDeEncontro(buscaUsuario(buscarSessaoId(sugestao.getIdSessao()).getLogin()), sugestao.getPontos()));

			}
		}
		
		carona.setPontoDeEncontro(pontoDeEncontro);

	}

	public void rejeitarSolicitacao(String idSessao, String idSolicitacao)
			throws Exception {
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		carona.removeSolicitacao(solicitacao);

	}

	public void desistirRequisicao(String idSessao, String idCarona,
			String idSugestao) { // precisa implementar do jeito certo....
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Sugestao sugestao = buscaSugestao(idSugestao, idCarona);
        
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {
			desistirSolicitacao = true;
			if (sugestao != null) {
				carona.removeSugestao(sugestao);
			}

			carona.removePontoDeEncontro(new PontoDeEncontro(buscaUsuario(sessao.getLogin()), sugestao.getPontos()));

			carona.removeSugestao(sugestao);

		}

	}

	public void excecaoDesistirSolicitacao(boolean desistirSolicitacao)
			throws Exception {

		if (desistirSolicitacao == true) {
			throw new Exception("Ponto Inválido");
		}

	}

	public void excecaoResponderPontoDeEncontro(String pontos) throws Exception {
		if (pontos.equals("") || pontos == null) {
			throw new Exception("Ponto Inválido");
		}

	}

	public void excecaoSolicitacao(String ponto, Carona carona)
			throws Exception {
		boolean pontoValido = false;
		for (Sugestao sugest : carona.getSugestoes()) {
			for (Resposta resposta : sugest.getlistaDeResposta()) {

				if (resposta.getPontos().contains(ponto)) {
					pontoValido = true;
					break;

				}
			}
		}
		if (!pontoValido) {
			throw new Exception("Ponto Inválido");
		}
	}

	public String visualizarPerfil(String idSessao, String login)
			throws Exception {
		Sessao sessao = buscarSessaoId(idSessao);
		String retorno = "";
		if (isSessaoAberta(idSessao) && sessao.getLogin().equals(login)) {
			Usuario usuario = buscaUsuario(login);
			retorno = "Nome: "+usuario.getNome() + "\n" + "Email: "+usuario.getEmail() + "\n" + "Presencas em caronas: "+usuario.getPresencaEmCaronas() + "\n" + "Faltas em caronas: "+usuario.getFaltasEmCaronas()
					 + "\n" + "Caronas seguras: "+usuario.getCaronasSeguras() + "\n" + "Caronas que nao funcionaram: "+usuario.getCaronasNaoFuncionaram();
		} else{

			throw new Exception("Login inválido");
		}
		return retorno;
	}
	
	

	public void addCaronaNoHistorico(String login, String idCarona) {
		Usuario caroneiro = buscaUsuario(login);
		caroneiro.addHistoricoCaronas(buscaCaronaID(idCarona));

	}

	public void addhistoricoVagasEmCaronas(String login, String idCarona) {
		Usuario caroneiro = buscaUsuario(login);
		caroneiro.addHistoricoVagasEmCaronas(buscaCaronaID(idCarona));

	}


	public void reiniciarSistema() throws Exception {

	    Serializador<Collection> ser = Serializador.getInstanceOf();
	    
	    this.listaDeCaronas = (List<Carona>) ser.recuperar("Caronas");
	    this.listaDeUsuarios  = (List<Usuario>) ser.recuperar("Usuarios");
	    this.listaDeInteresses  = (List<Interesse>) ser.recuperar("Interesses");
	    
	    addCaronaNaListaDeCaronaEspecifica();

     
	}
	
	public void addCaronaNaListaDeCaronaEspecifica(){
		for (Carona carona : listaDeCaronas) {
			if (carona.tipoDeCarona().equals("Municipal")) {
				listaDeCaronasMunicipais.add((CaronaMunicipal) carona);
			}else{
				listaDeCaronasInterMunicipais.add((CaronaIntermunicipal) carona);
			}
		}
	}

	public String getCaronaUsuario(String idSessao, int indexCarona) {
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		String idCarona = usuario.getListaDeCaronasDoUsuario().get(indexCarona)
				.getIdDaCarona();

		return idCarona;
	}

	public String getAtributoPerfil(String login, String atributo)
			throws Exception {
        
		Usuario caroneiro = buscaUsuario(login);
		String resposta = null;

		if (atributo.equals("nome")) {
			resposta = caroneiro.getNome();
		} else if (atributo.equals("endereco")) {
			resposta = caroneiro.getEndereco();
		} else if (atributo.equals("email")) {
			resposta = caroneiro.getEmail();
		} else if (atributo.equals("historico de caronas")) {
			List<String> lista = new LinkedList<String>(); 
			for (Carona carona : caroneiro.getHistoricoCaronas()) {
				lista.add(carona.getIdDaCarona());
			}
			if (lista.isEmpty()) {
				resposta = "";
			}else{
				resposta = lista.toString();
				resposta = resposta.replace(" ", "");
			}
		} else if (atributo.equals("historico de vagas em caronas")) {
			List<String> lista = new LinkedList<String>();
			for (Carona carona : caroneiro.getHistoricoVagasEmCaronas()) {
				lista.add(carona.getIdDaCarona());
			}
			if (lista.isEmpty()) {
				resposta = "";
			}
			else{					
			resposta = lista.toString();
			resposta = resposta.replace(" ","");
			}
			
		} else if (atributo.equals("caronas seguras e tranquilas")) {
			resposta = String.valueOf(caroneiro.getCaronasSeguras());
		} else if (atributo.equals("caronas que não funcionaram")) {
			resposta = String.valueOf(caroneiro.getCaronasNaoFuncionaram());
		} else if (atributo.equals("faltas em vagas de caronas")) {
			resposta = String.valueOf(caroneiro.getFaltasEmCaronas());
		} else if (atributo.equals("presenças em vagas de caronas")) {
			resposta = String.valueOf(caroneiro.getPresencaEmCaronas());
		}
		return resposta;
	}


	public String getTodasCaronasUsuario(String idSessao) {

		List<String> todasAsCaronas = new ArrayList<String>();
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		String strCaronas = null;
		for (Carona carona : usuario.getListaDeCaronasDoUsuario()) {
			todasAsCaronas.add(carona.getIdDaCarona());
		}

		strCaronas = todasAsCaronas.toString();
		strCaronas = strCaronas.replace("[", "{");
		strCaronas = strCaronas.replace("]", "}");
		strCaronas = strCaronas.replace(" ", "");

		return strCaronas;
	}

	public List<String> getSolicitacoesConfirmadas(String idSessao,
			String idCarona) {

		Sessao sessao = buscarSessaoId(idSessao);
		List<String> resp = new ArrayList<String>();
		Carona carona = buscaCaronaID(idCarona);
		if (isSessaoAberta(sessao.getId())) {
			for (Solicitacao solicitacao : carona
					.getListaDeSolicitacaoAceitas()) {
				resp.add(solicitacao.getIdSolicitacao());
			}
		}

		return resp;
	}

	public List<String> getSolicitacoesPendentes(String idCarona) {
		Carona carona = buscaCaronaID(idCarona);
		List<String> solicitacoesPendentes = new ArrayList<String>();

		for (Solicitacao solicitacao : carona.getListaDeSolicitacao()) {
			solicitacoesPendentes.add(solicitacao.getIdSolicitacao());
		}
		return solicitacoesPendentes;
	}

	public List<String> getPontosSugeridos(String idSessao, String idCarona) {
		Carona carona = buscaCaronaID(idCarona);
		Sessao sessao = buscarSessaoId(idSessao);
		List<String> pontosSugeridos = new ArrayList<String>();
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // se
																				// a
																				// sessao
																				// é
																				// do
																				// dono
																				// da
																				// carona

			for (Sugestao sugestao : carona.getSugestoes()) {
				pontosSugeridos.add(sugestao.getPontos());
			}

		}
		return pontosSugeridos;
	}

	public List<String> getPontosEncontro(String idSessao, String idCarona) {

		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		List<String> pontosEncontro = new ArrayList<String>();
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {
            
			for (PontoDeEncontro pontosDeEncontro : carona.getPontoDeEncontro()) {
				 pontosEncontro.add(pontosDeEncontro.getPonto());
				 
			}
			

		}
		return pontosEncontro;
	}

	public void reviewVagaEmCarona(String idSessao, String idCarona, String loginCaroneiro, String review) throws Exception {
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Usuario caroneiro = buscaUsuario(loginCaroneiro);

		boolean taNaCarona = false; // Verifica se o caroneiro ta na carona
		for (Solicitacao solicitacao : carona.getListaDeSolicitacaoAceitas()) {
			if (buscarSessaoId(solicitacao.getIdSessao()).getLogin().equals(
					loginCaroneiro)) {
				taNaCarona = true;
			}
		}

		if (taNaCarona) {// Se tiver...

			if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {// Verifica
																				// se
																				// é
																				// o
																				// dono
																				// da
																				// carona
				if (review.equals("faltou")) { // Para poder da Review ou nao
					caroneiro.addFaltasEmCaronas();
				} else if (review.equals("não faltou")) 
					caroneiro.addPresencaEmCaronas();			
				else{
					throw new Exception("Opção inválida.");
				}
			}
		} else {
			throw new Exception("Usuário não possui vaga na carona.");
		}

	}
	
    public void reviewCarona(String idSessao, String idCarona, String review) throws Exception{
		
    	Carona carona = buscaCaronaID(idCarona);
    	Usuario donoDaCarona = carona.getDonoDaCarona();
    	
    	for (Solicitacao solicitacao : carona.getListaDeSolicitacaoAceitas()) {
			if (solicitacao.getIdSessao().equals(idSessao)) { // se o usuario estava naquela carona
			   if(review.equals("segura e tranquila"))
				  donoDaCarona.addCaronasSeguras();	   
			    else if(review.equals("não funcionou"))	
			    	donoDaCarona.addCaronasNaoFuncionaram();
			    else{
			    	throw new Exception("Opção inválida.");
			    }
			   break;
			}
			throw new Exception("Usuário não possui vaga na carona.");
						
		}
	}
	
	
	//retorna idInteresse
	public String cadastrarInteresse(String idSessao, String origem, String destino, String data, String horaInicial, String horaFim) throws Exception
	{
		excecaoCadastrarInteresse(idSessao, origem, destino, data);
		Interesse interesse = new Interesse(idSessao, origem, destino, data, horaInicial, horaFim);
		listaDeInteresses.add(interesse);
		return interesse.getIdInteresse();
	}
	
	public void excecaoCadastrarInteresse(String idSessao, String origem, String destino,
			String data) throws Exception {

		if (origem == null) {
			throw new Exception("Origem inválida");
		}
		
		if (destino == null) {
			throw new Exception("Destino inválido");
		}
		
		if ( (data == null || data.equals("")) && ( origem.contains("!") || origem.contains("-") || (destino.contains("!") || (destino.contains("-")) ))) {
			throw new Exception("Data inválida");
		}
		
		if (idSessao == null || idSessao.equals("")) {
			throw new Exception("IdSessao inválido");
		}
		
		else if(origem.equals("-") || origem.equals("!"))
		{
			throw new Exception("Origem inválida");

		}

		
		else if(destino.contains("-") || destino.contains("!"))
		{
			throw new Exception("Destino inválido");
		}


	}
	
	

	public void zerarSistema() {
		listaDeCaronas.clear();
		listaDeSessoesAbertas.clear();
		listaDeUsuarios.clear();
		listaDeCaronasInterMunicipais.clear();
		listaDeCaronasMunicipais.clear();
		listaDeInteresses.clear();
		//encerrarSistema();

	}



	public static void main(String[] args) throws Exception {
		
		
		
		
		
		
	
	}

}
