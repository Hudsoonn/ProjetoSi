package sistemadecaronas.projSi1.sistema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sistemadecaronas.projSi1.auxiliar.TrataDatas;
import sistemadecaronas.projSi1.persistencia.InterfaceXML;

public class SistemaDeCarona {

	/**
	 * @param args
	 */
	public List<Usuario> listaDeUsuarios = new ArrayList<Usuario>();
	public List<Carona> listaDeCaronas = new ArrayList<Carona>();
	public List<CaronaIntermunicipal> listaDeCaronasInterMunicipais = new ArrayList<CaronaIntermunicipal>();
	public List<CaronaMunicipal> listaDeCaronasDentroDaCidade = new ArrayList<CaronaMunicipal>();
	public List<Sessao> listaDeSessoesAbertas = new ArrayList<Sessao>();
	public List<Interesse> listaDeInteresses = new ArrayList<Interesse>();
	private boolean desistirSolicitacao;

	public SistemaDeCarona() {
	}

	// criarUsuario login="mark" senha="m@rk" nome="Mark Zuckerberg"
	// endereco="Palo Alto, California" email="mark@facebook.com"

	public void criarUsuario(String login, String senha, String nome,
			String endereco, String email) throws Exception {

		excecaoCriarUsuario(login, senha, nome, endereco, email);

		Usuario novoUsuario = new Usuario(login, senha, nome, endereco, email);
		listaDeUsuarios.add(novoUsuario);

	}

	public void encerrarSistema() {

		/*CriarXML cXML = new CriarXML();
		cXML.criaXMLUsuarios(listaDeUsuarios);*/
		
		InterfaceXML interXML;
		interXML = new InterfaceXML("Usuarios", listaDeUsuarios);
		interXML.saveData();
		interXML = new InterfaceXML("Caronas", listaDeCaronas);
		interXML.saveData();

		System.out.println("Sistema Encerrado");
	}

	/**
	 * metodo usado para o usuario fazer login
	 * @param login
	 * @param senha
	 * @return retorna o id da sessao
	 * @throws Exception
	 */
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
	/*public String getAtributoUsuario(String login, String atributo)
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

	}*/
	
    /**
     * metodo usado para lancar excecao quando um atributo eh invalido
     * @param login
     * @param atributo
     * @throws Exception
     */
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
    
	/*public String getAtributoCarona(String idDaCarona, String atributo)
			throws Exception {
		String saida = "";
		excecaoDeAtributosCaronaInvalidos(idDaCarona, atributo);

		Carona carona = buscaCaronaID(idDaCarona);

		if (atributo.equals("origem")) {
			saida = carona.getOrigem();
		}
		if (atributo.equals("destino")) {
			saida = carona.getDestino();
		}
		if (atributo.equals("data")) {
			saida = carona.getData();
		}

		if (atributo.equals("vagas")) {
			saida = Integer.toString(carona.getVagas());
		}

		if (atributo.equals("Ponto de Encontro")) {
			if (!carona.getPontoDeEncontro().isEmpty()) {
				saida = carona.getPontoDeEncontro().toString();
				saida = saida.replace("[", "");
				saida = saida.replace("]", "");

			}

		}

		// aceita o primeiro que achar e depois sai.
		return saida;
	}*/
    
	 // metodo nao precisa existir ja que o getAtributosCarona so serve para o easyAccept 
	
	
	/*public void excecaoDeAtributosCaronaInvalidos(String idDaCarona,
			String atributo) throws Exception {

		if (idDaCarona == null || idDaCarona.equals("")) {

			throw new Exception("Identificador do carona é inválido");
		}
		if (buscaCaronaID(idDaCarona) == null) {
			throw new Exception("Item inexistente");
		} else if (atributo == null || atributo.equals("")) {
			throw new Exception("Atributo inválido");
		} else if (!atributo.equals("origem") && !atributo.equals("destino")
				&& !atributo.equals("data") && !atributo.equals("vagas")
				&& !atributo.equals("Ponto de Encontro")) {
			throw new Exception("Atributo inexistente");
		}
	}*/
   
	/**
	 * metodo que lanca excecao durante a criacao de uma carona
	 * @param idDaSessao
	 * @param origem
	 * @param destino
	 * @param data
	 * @param hora
	 * @param vagas
	 * @throws Exception
	 */
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
		// !auxiliar.TrataDatas.isDataValida(data)
		if (data == null || data.equals("") || !TrataDatas.isDataValida(data)) {
			throw new Exception("Data inválida");
		}
		// !auxiliar.TrataDatas.horaValida(hora)
		if (hora == null || hora.equals("") || !TrataDatas.horaValida(hora)) {
			throw new Exception("Hora inválida");
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
    
	/**
	 * metodo que busca uma carona atraves do id
	 * @param idCarona
	 * @return carona encontrada
	 */
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
    
	/**
	 * metodo que lanca excecao durante a criacao e um usuario
	 * @param login
	 * @param senha
	 * @param nome
	 * @param endereco
	 * @param email
	 * @throws Exception
	 */
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
    
	/**
	 * 
	 * @return um lista contendo todos os usuarios do sistema
	 */
	public List<Usuario> getUsuarios() {
		return listaDeUsuarios;
	}

	/**
	 * encontra um usuario a partir do login
	 * @param login
	 * @return usuario encontrado
	 */
	public Usuario buscaUsuario(String login) {
		for (int i = 0; i < listaDeUsuarios.size(); i++) {
			if (listaDeUsuarios.get(i).getLogin().equals(login)) {
				return listaDeUsuarios.get(i);
			}
		}
		return null;
	}
    
	/**
	 * 
	 * @param id
	 * @return true se o usuario com aquele id esta logado
	 */
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
    
	/**
	 * metodo que cadastra uma carona e retorna o id criado pela carona
	 * @param idDaSessao
	 * @param origem
	 * @param destino
	 * @param data
	 * @param hora
	 * @param vagas
	 * @return id da carona
	 * @throws Exception
	 */
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
	
	/**
	 * cadastra uma carona municipal e retorna o seu id
	 * @param idDaSessao
	 * @param origem
	 * @param destino
	 * @param cidade
	 * @param data
	 * @param hora
	 * @param vagas
	 * @return id da carona
	 * @throws Exception
	 */
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
		listaDeCaronasDentroDaCidade.add((CaronaMunicipal) novaCarona);

		String login = sessao.getLogin();
		String idCarona = novaCarona.getIdDaCarona();
		addCaronaNoHistorico(login, idCarona);

		Usuario usuario = buscaUsuario(sessao.getLogin());
		usuario.addCarona(novaCarona);
		
		verificaInteresse(idDaSessao, origem, destino, data, hora);

		return novaCarona.getIdDaCarona();

	}

    /**
     * varifica se alguem esta interessado em alguma carona existente que do sistema
     * @param idDaSessao
     * @param origem
     * @param destino
     * @param data
     * @param hora
     * @throws Exception
     */
	private void verificaInteresse(String idDaSessao, String origem,
			String destino, String data, String hora) throws Exception {
		//Sessao sessao = buscarSessaoId(idDaSessao);
		
		for(Interesse interesse : listaDeInteresses)
		{
			if(origem.equals(interesse.getOrigem()) && destino.equals(interesse.getDestino()) && data.equals(interesse.getData()) && comparaHoras(hora, interesse.getHoraInicial(), interesse.getHoraFim()))
			{
				enviaMensagem(idDaSessao, buscarSessaoId(interesse.getIdSessao()).getLogin(), data, hora);
				break;//só envia uma mensagem???
			}
		}
		
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

   /**
    * metodo que envia mensagem quando uma carona desejada foi encontrada
    * @param idSessao
    * @param loginDestino
    * @param data
    * @param hora
    * @throws Exception
    */
	private void enviaMensagem(String idSessao, String loginDestino, String data, String hora) throws Exception {
		//expect "[Carona cadastrada no dia 23/06/2012, Ã s 16:00 de acordo com os seus interesses registrados. Entrar em contato com jucaPeroba@gmail.com]" verificarMensagensPerfil idSessao=${sessaoZezito}
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		
		String mensagem = "Carona cadastrada no dia " + data + ", Às " + hora + " de acordo com os seus interesses registrados." +
							" Entrar em contato com " + usuario.getEmail();
		
		Usuario destinatario = buscaUsuario(loginDestino);
		destinatario.addMensagem(mensagem);
	}
	
	public String verificarMensagensPerfil(String idSessao)
	{
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		return usuario.getMensagens();
	}
	

	
	/*
	 * public String localizarCarona(String idDaSessao, String origem,String
	 * destino) throws Exception {
	 * 
	 * excecaoLocalizarCarona(idDaSessao, origem, destino); List<String>
	 * caronasEncontradas = new ArrayList<String>(); String strCaronas = null;
	 * 
	 * for (String chave : mapaDeCaronas.keySet()) {
	 * 
	 * for (Carona carona : mapaDeCaronas.get(chave)) {
	 * 
	 * 
	 * if (!origem.equals("") && !destino.equals("")) { // lista todas as
	 * caronas de uma determinada origem até um destino
	 * 
	 * 
	 * if (carona.getDestino().equals(destino) &&
	 * carona.getOrigem().equals(origem)) {
	 * 
	 * caronasEncontradas.add(carona.getIdDaCarona());
	 * 
	 * } }
	 * 
	 * if (!origem.equals("") && destino.equals("")) { //lista todas as caronas
	 * daquela origem if (carona.getOrigem().equals(origem)) {
	 * 
	 * caronasEncontradas.add(carona.getIdDaCarona());
	 * 
	 * } }
	 * 
	 * if (origem.equals("") && !destino.equals("")) { // lista todas as caronas
	 * para aquele destino if (carona.getDestino().equals(destino)) {
	 * 
	 * caronasEncontradas.add(carona.getIdDaCarona());
	 * 
	 * } }
	 * 
	 * if (origem.equals("") && destino.equals("")) { // lista todas as caronas
	 * 
	 * caronasEncontradas.add(carona.getIdDaCarona());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * strCaronas = caronasEncontradas.toString(); strCaronas =
	 * strCaronas.replace("[", "{"); strCaronas = strCaronas.replace("]", "}");
	 * strCaronas = strCaronas.replace(" ", "");
	 * 
	 * return strCaronas; }
	 */
	
	/**
	 * lista todas as carona de uma determinada origem a um destino
	 * @param listaDeCaronas
	 * @param origem
	 * @param destino
	 * @return uma lista com as caronas encontradas
	 */
	public List<Carona> listaCaronas(List<Carona> listaDeCaronas,String origem,String destino){
		
		List<Carona> caronasEncontradas = new ArrayList<Carona>();
		
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

					caronasEncontradas .add(carona);

				}
			}

			if (!origem.equals("") && destino.equals("")) { // lista todas
															// as caronas
															// daquela
															// origem
				if (carona.getOrigem().equals(origem)) {

					caronasEncontradas.add(carona);

				}
			}

			if (origem.equals("") && !destino.equals("")) { // lista todas
															// as caronas
															// para aquele
															// destino
				if (carona.getDestino().equals(destino)) {

					caronasEncontradas.add(carona);

				}
			}

			if (origem.equals("") && destino.equals("")) { // lista todas as
															// caronas

				caronasEncontradas.add(carona);

			}

		}

		return caronasEncontradas;
	}
	 
	 
    /**
     * localiza caronas com aquela origem e destino
     * @param idSessao
     * @param origem
     * @param destino
     * @return lista de caronas encontradas
     * @throws Exception
     */
	public List<Carona> localizarCarona(String idSessao, String origem, String destino)
			throws Exception {

		excecaoLocalizarCarona(idSessao, origem, destino);
		List<Carona> caronasEncontradas = new ArrayList<Carona>();

		if (isSessaoAberta(idSessao)) {
			caronasEncontradas = listaCaronas(listaDeCaronas, origem, destino);
		}


		return caronasEncontradas;
	}
	
	
	
	/**
	 * localiza carona de uma determinada cidade
	 * @param cidade
	 * @param caronasEncontradas
	 * @return lista de caronas encontradas daquela cidade
	 */
	public List<Carona> localizarPorCidade(String cidade, List<Carona> caronasEncontradas)
	{
		List<Carona> caronaMunicipal = new ArrayList<Carona>();
		//=-=-=-=-=-=-=Padrao Iterator=-=-=-=-=-=
		Iterator<Carona> itCaronasEncontradas = caronasEncontradas.iterator();
		while(itCaronasEncontradas.hasNext())
		{
			Carona carona = (Carona) itCaronasEncontradas.next();
			if(carona.tipoDeCarona().equals("Municipal"))
			{
				if(((CaronaMunicipal) carona).getCidade().equals(cidade))
				{
					caronaMunicipal.add(carona);
				}
			}
		}
		
		return caronaMunicipal;
	}
	
    /**
     * localiza carona municipais
     * @param idSessao
     * @param cidade
     * @param origem
     * @param destino
     * @return lista de carona encontradas
     * @throws Exception
     */
	public List<Carona> localizarCaronaMunicipal(String idSessao, String cidade,
			String origem, String destino) throws Exception {
		excecaoCidade(cidade);
		
		List<Carona> caronasEncontradas = localizarCarona(idSessao, origem, destino);
		
		return localizarPorCidade(cidade, caronasEncontradas);
		
	}
	/*
	// fazer excecao cidade
	public String localizarCaronaMunicipal(String idSessao, String cidade)
			throws Exception {
		excecaoCidade(cidade);
		
		
		
		List<String> caronasEncontradas = new ArrayList<String>();
		String strCaronas = null;

		if (isSessaoAberta(idSessao)) {

			for (CaronaDentroDaCidade carona : listaDeCaronasDentroDaCidade) {

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
	}*/
    
	/**
	 * lanca exececao se ocorrer problema ao localizar uma carona
	 * @param idDaSessao
	 * @param origem
	 * @param destino
	 * @throws Exception
	 */
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
	
	/**
	 * lanca uma excecao para cidade invalida
	 * @param cidade
	 * @throws Exception
	 */
	public void excecaoCidade(String cidade) throws Exception
	{
		if(cidade == null || cidade.equals(""))
			throw new Exception("Cidade inexistente");
	}
    
	/**
	 * acha a sessao a partir do id
	 * @param idSessao
	 * @return uma sessao
	 */
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
    /**
     * imprime o trajeto de uma determinada carona
     * @param idDaCarona
     * @return
     * @throws Exception
     */
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
    /**
     * cria uma string com infomacoes da carona
     * @param idDaCarona
     * @return string com informacoes da carona
     * @throws Exception
     */
	public String getInformacoesCarona(String idDaCarona) throws Exception {
		excecaoGetCarona(idDaCarona);
		Carona carona = buscaCaronaID(idDaCarona);
		// João Pessoa para Campina Grande, no dia 25/11/2026, as 06:59
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
    /**
     * medo que desloga um usuario
     * @param login
     */
	public void encerrarSessao(String login) {
		for (Sessao sessao1 : listaDeSessoesAbertas) {
			if (sessao1.getLogin().equals(login)) {
				listaDeSessoesAbertas.remove(sessao1);
				break;
			}

		}

	}
    
	/**
	 * metodo que serve pra sugerir um ponto de encontro em uma carona
	 * @param idSessao
	 * @param idCarona
	 * @param pontos
	 * @return id da sugestao
	 * @throws Exception
	 */
	public String sugerirPontoEncontro(String idSessao, String idCarona,
			String pontos) throws Exception {
		excecaoDesistirSolicitacao(desistirSolicitacao);
		Carona carona = buscaCaronaID(idCarona);
		Sugestao sugestao = new Sugestao(pontos, idSessao);
		carona.addSugestao(sugestao); // adiciona a sugestao na lista de
										// sugestoes da carona

		return sugestao.getIdSugestao();
	}
     
	/**
	 * metodo que serve pra responder a sugestao feita
	 * @param idSessao
	 * @param idCarona
	 * @param idSugestao
	 * @param pontos
	 * @return id da resposta
	 * @throws Exception
	 */
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
			carona.addPontoDeEncontro(solicitacao.getPonto()); // adiciona o
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
		// Quem aceita nao era para ser o Dono da carona?!?!?!?!?!?!?!?
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		// Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		carona.addSolicitacaoAceita(solicitacao);
		carona.removeSolicitacao(solicitacao);
		carona.setVagas(carona.getVagas() - 1);
		addhistoricoVagasEmCaronas(buscarSessaoId(solicitacao.getIdSessao())
				.getLogin(), carona.getIdDaCarona());

		List<String> pontoDeEncontro = new ArrayList<String>();
		if (solicitacao.getPonto() == null
				&& carona.getPontoDeEncontro().isEmpty()) {
			for (Sugestao sugestao : carona.getSugestoes()) {
				pontoDeEncontro.add(sugestao.getPontos());

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
			// remover as coisas
			carona.removePontoDeEncontro(sugestao.getPontos());
			// sugestao.mapaDeResposta.remove(sugestao);
			carona.removeSugestao(sugestao);
			// carona.listaDeSolicitacao.remove(o);
			// carona.removeSolicitacao(sugestao.);
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
		String retorno = "";
		if (isSessaoAberta(idSessao)) {
			Usuario usuario = buscaUsuario(login);
			retorno = "Nome: "+usuario.getNome() + "\n" + "Email: "+usuario.getEmail() + "\n" + "Presencas em caronas: "+usuario.getPresencaEmCaronas() + "\n" + "Faltas em caronas: "+usuario.getFaltasEmCaronas()
					 + "\n" + "Caronas seguras: "+usuario.getCaronasSeguras() + "\n" + "Caronas que nao funcionaram: "+usuario.getCaronasNaoFuncionaram();
		} 
		return retorno;
	}

	public void addCaronaNoHistorico(String login, String idCarona) {
		Usuario usuario = buscaUsuario(login);
		usuario.addHistoricoCaronas(buscaCaronaID(idCarona));
	}

	public void addhistoricoVagasEmCaronas(String login, String idCarona) {
		Usuario usuario = buscaUsuario(login);
		usuario.addHistoricoVagasEmCaronas(buscaCaronaID(idCarona));
	}

	
	public void reiniciarSistema() throws Exception {
		// CriarXML cXml = new CriarXML();
		// cXml.lerXMLUsuarios(listaDeUsuarios, listaDeCaronas);

		InterfaceXML interXML;
		interXML = new InterfaceXML("Usuarios", listaDeUsuarios);
		interXML.loadData();
		interXML = new InterfaceXML("Caronas", listaDeCaronas);
		interXML.loadData();

	}

	public Carona getCaronaUsuario(String idSessao, int indexCarona) {
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Carona carona = usuario.getListaDeCaronasDoUsuario().get(indexCarona);

		return carona;
	}

	
	// OBS: esse metodo poe ser implementado usando o mapaDeCaronas existente só
	// que é bom mudar o mapaDeCaronas pra <String,String> para isso tem que
	// alterar alguns metodos,vou fazer isso depois.
	public List<Carona> getCaronasUsuario(String idSessao) {

		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());


		return usuario.getListaDeCaronasDoUsuario();
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

			pontosEncontro = carona.getPontoDeEncontro();

		}
		return pontosEncontro;
	}

	public void reviewVagaEmCarona(String idSessao,String idCarona,String loginCaroneiro,String review) throws Exception {
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
				} else if (review.equals("não faltou")) {
					caroneiro.addPresencaEmCaronas();
				}
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
		//excecao se as horas forem null???
		if (origem == null) {
			throw new EntradasInvalidas("Origem inválida");
		}
		
		if (destino == null) {
			throw new EntradasInvalidas("Destino inválido");
		}
		
		if ( (data == null || data.equals("")) && ( origem.contains("!") || origem.contains("-") || (destino.contains("!") || (destino.contains("-")) ))) {
			throw new EntradasInvalidas("Data inválida");
		}
		
		if (idSessao == null || idSessao.equals("")) {
			throw new EntradasInvalidas("IdSessao inválido");
		}
		
		else if(origem.equals("-") || origem.equals("!"))
		{
			throw new EntradasInvalidas("Origem inválida");

		}

		
		else if(destino.contains("-") || destino.contains("!"))
		{
			throw new EntradasInvalidas("Destino inválido");
		}


	}
	
	

	public void zerarSistema() {
		listaDeCaronas.clear();
		listaDeSessoesAbertas.clear();
		listaDeUsuarios.clear();
		listaDeCaronasInterMunicipais.clear();
		listaDeCaronasDentroDaCidade.clear();
		listaDeInteresses.clear();
		// encerrarSistema();

	}

	public static void main(String[] args) throws Exception {
		
		
		
		
	}

}
