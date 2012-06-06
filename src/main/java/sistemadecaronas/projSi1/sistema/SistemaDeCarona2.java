package sistemadecaronas.projSi1.sistema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sistemadecaronas.projSi1.auxiliar.TrataDatas;
import sistemadecaronas.projSi1.persistencia.Serializador;

public class SistemaDeCarona2 {

	/**
	 * @param args
	 */
	public List<Usuario> listaDeUsuarios = new ArrayList<Usuario>();
	public List<Carona> listaDeCaronas = new ArrayList<Carona>();
	public List<CaronaIntermunicipal> listaDeCaronasInterMunicipais = new ArrayList<CaronaIntermunicipal>();
	public List<CaronaMunicipal> listaDeCaronasMunicipais = new ArrayList<CaronaMunicipal>();
	public List<Sessao> listaDeSessoesAbertas = new ArrayList<Sessao>();
	public List<Interesse> listaDeInteresses = new ArrayList<Interesse>();
	//private boolean desistirSolicitacao;
	private static SistemaDeCarona2 sistemaDeCarona = null;

	private SistemaDeCarona2() {
	}
    
	public static SistemaDeCarona2 getInstanceOf(){
		if (sistemaDeCarona == null) {
			sistemaDeCarona = new SistemaDeCarona2();
		}
		return sistemaDeCarona;
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

		Serializador<Collection> ser = new Serializador<Collection>();
		
		ser.salvar("Usuarios", this.listaDeUsuarios);
		ser.salvar("Caronas", this.listaDeCaronas);
		ser.salvar("Interesses", this.listaDeInteresses);
        
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
				int vagasInt  = Integer.parseInt(vagas);
				if (vagasInt <= 0) {
					throw new Exception();
				}
			} catch (Exception e) {
				throw new Exception("número de vagas inválido");
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

		/*String login = sessao.getLogin();
		String idCarona = novaCarona.getIdDaCarona();
		addCaronaNoHistorico(login, idCarona);*/

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
		listaDeCaronasMunicipais.add((CaronaMunicipal) novaCarona);

		/*String login = sessao.getLogin();
		String idCarona = novaCarona.getIdDaCarona();
		addCaronaNoHistorico(login, idCarona);*/

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
		Carona carona = new CaronaIntermunicipal(origem, destino, data, hora, 0);
		for(Interesse interesse : listaDeInteresses)
		{
			if(caronaEhDeInteresse(carona, interesse))
			{   
				String loginDonoCarona = buscarSessaoId(idDaSessao).getLogin();
				String loginCaroneiro = buscarSessaoId(interesse.getIdSessao()).getLogin();
				Conteudo conteudo =  new ConteudoTexto(String.format(mensagensDoSistema.MENSAGEM_INTERESSE.getMensagem(),data,hora,buscaUsuario(loginDonoCarona).getEmail()));
				Mensagem mensagem = new Mensagem(loginDonoCarona, loginCaroneiro, conteudo);
				
				enviaMensagem(mensagem);
			}
		}
		
	}
    
	public boolean comparaHoras(String hora, String horaInicial, String horaFim) throws ParseException {
		   SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");   
		   Date horaI; 
		   Date horaF;
		   Date horaCarona = formatter.parse(hora);
		   boolean saida = false;
		   if (horaInicial.equals("") && horaFim.equals("")) {
			saida = true;
		   }else   
			  if (horaInicial.equals("") && !horaFim.equals("")){
				  horaF  = formatter.parse(horaFim);
			    if(horaF.getTime() >= horaCarona.getTime()) {
			      saida = true;
			  }
			}else
		    	if (!horaInicial.equals("") && horaFim.equals("")){
		    		horaI = formatter.parse(horaInicial);	
		    	    if(horaI.getTime() <= horaCarona.getTime()) {
			        saida = true;
		       }
		   }else{
			horaI = formatter.parse(horaInicial);	
			horaF  = formatter.parse(horaFim);
			if(horaCarona.getTime() >= horaI.getTime() && horaCarona.getTime() <= horaF.getTime()) {
			   saida = true;
		   
			}
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

   /**
    * metodo que envia mensagem quando uma carona desejada foi encontrada
    * @param idSessao
    * @param loginDestino
    * @param data
    * @param hora
    * @throws Exception
    */
	public void enviaMensagem(Mensagem mensagem) throws Exception {
		//expect "[Carona cadastrada no dia 23/06/2012, Ã s 16:00 de acordo com os seus interesses registrados. Entrar em contato com jucaPeroba@gmail.com]" verificarMensagensPerfil idSessao=${sessaoZezito}
		/*Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		String mensagem = "Carona cadastrada no dia " + data + ", Às " + hora + " de acordo com os seus interesses registrados." +
							" Entrar em contato com " + usuario.getEmail();*/
		
		Usuario destinatario = buscaUsuario(mensagem.getDestinatario());
		destinatario.addMensagem(mensagem);
	}
	
	public void deletarMensagem(Usuario usuario, Mensagem mensagem){
		usuario.removeMensagem(mensagem);
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
	
	/*public String verificarMensagensPerfil(String idSessao)
	{
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		return usuario.getMensagens();
	}
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
	
	public void addCaronaNaListaDeCaronaEspecifica(){
		for (Carona carona : listaDeCaronas) {
			if (carona.tipoDeCarona().equals("Municipal")) {
				listaDeCaronasMunicipais.add((CaronaMunicipal) carona);
			}else{
				listaDeCaronasInterMunicipais.add((CaronaIntermunicipal) carona);
			}
		}
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
     * metodo que desloga um usuario
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
	 * verifica um ponto de encontro na carona foi sugerido por determinado usuario
	 * @param login
	 * @param idCarona
	 * @return true se a carona contem um ponto de encontro sugerido por esse usuario
	 */
	public boolean usuarioJaSugeriu(String login, String idCarona){
		Carona carona = buscaCaronaID(idCarona);
		boolean jaSugeriu = false;
		
		for (PontoDeEncontro ponto : carona.getPontoDeEncontro()) {
			if (ponto.getUsuario().getLogin().equals(login)) {
				jaSugeriu = true;
				break;
			}
		}
		return jaSugeriu;
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
		
		//excecaoDesistirSolicitacao(desistirSolicitacao);
		
		Carona caronaS = buscaCaronaID(idCarona);
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Sugestao sugestao = null;
		PontoDeEncontro pontoEncontro = new PontoDeEncontro(usuario, pontos);
		boolean participaDaCarona = false;
		boolean donoDaCarona = false;
		
		if (usuario.getLogin().equals(caronaS.getDonoDaCarona().getLogin())) {
			donoDaCarona = true;
		}
		
		if (!donoDaCarona) {
		 for (Carona carona : usuario.getListaDeCaronasQueParticipa()) { // so quem pode sugerir um ponto de encontro eh um usuario que participa da carona
	     	if (carona.getIdDaCarona().equals(idCarona)) {
				participaDaCarona = true;
				break;
			}
		}
	}
		
		if (donoDaCarona) { // quando quem sugere um ponto eh o dono ele logo eh adicionado aos ponto de encontro sem precisar de aceitacao
			sugestao = new Sugestao(pontos, idSessao);
			caronaS.addPontoDeEncontro(pontoEncontro);
		}
		else if(participaDaCarona){
				sugestao = new Sugestao(pontos, idSessao);
      			caronaS.addSugestao(sugestao);
		
		}else if (!donoDaCarona && !participaDaCarona) {
			throw new Exception("Usuário não participa da carona");
		}

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
	public void responderSugestaoPontoEncontro(String idSessao,
			String idCarona, String idSugestao, String pontos) throws Exception {

		// String idResposta = null;
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		excecaoResponderPontoDeEncontro(pontos);
        
		Sugestao sugest = buscaSugestao(idSugestao, idCarona);
		Usuario usuarioQueSugeriu = buscaUsuario(buscarSessaoId(sugest.getIdSessao()).getLogin());
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {  // verifica se eh o dono da carona

			for (Sugestao sugestao : carona.getSugestoes()) {
				if (sugestao.getIdSugestao().equals(idSugestao)) { // se a sugestao eh a que eu procuro
					
				//	Resposta resp = new Resposta(pontos); // cria uma resposta
				//	idResposta = resp.getIdResposta();
				//	sugestao.addResposta(resp); // adiciona em uma lista a resposta dessa sugestao
					PontoDeEncontro pontoEncontro;
					
					if (pontos.equals(sugestao.getPontos())) {
						
						pontoEncontro = new PontoDeEncontro(usuarioQueSugeriu, pontos);
					}else{
						pontoEncontro = new PontoDeEncontro(usuario, pontos);
					}

					
                    if (!usuarioJaSugeriu(usuarioQueSugeriu.getLogin(), idCarona)) { // se o usuario nao tem nenhum ponto de encontro na carona
    					
    					carona.addPontoDeEncontro(pontoEncontro); // a sugestao vira ponto de encontro	
					}else{
						for (PontoDeEncontro pontoDeEncontro : carona.getPontoDeEncontro()) {
							if (pontoDeEncontro.getUsuario().getLogin().equals(usuarioQueSugeriu.getLogin())) { // se o usuario ja tinha um ponto de encontro na carona subistitui
								
								carona.removePontoDeEncontro(pontoDeEncontro);
								carona.addPontoDeEncontro(pontoEncontro);
								
							}
						}
						
					}
				

				}

			}
		}

	//	return idResposta;
	}
    /**
     * metodo pra solicitar uma vaga em um determinado ponto de encontro
     * @param idSessao
     * @param idCarona
     * @param ponto
     * @return id da solicitacao
     * @throws Exception
     */
	public String solicitarVagaPontoEncontro(String idSessao, String idCarona,
			String ponto) throws Exception {
		/*
		 * 1- Colocar solicitacao em carona 2- Return ID
		 */
	//	excecaoDesistirSolicitacao(desistirSolicitacao);
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Carona carona = buscaCaronaID(idCarona);
		Solicitacao solicitacao = new Solicitacao(idSessao, idCarona, ponto);
		excecaoSolicitacao(ponto, carona);
        
		if (!(carona.getDonoDaCarona().getLogin().equals(usuario.getLogin()))) { // quem pede uma vaga nao pode ser o dono
			if (!usuario.getListaDeCaronasQueParticipa().contains(carona)) { // se o usuario nao esta na carona
			  
				if (carona.getVagas() > 0) {
					for (PontoDeEncontro pontoDeEncontro : carona.getPontoDeEncontro()) {
						if (pontoDeEncontro.getPonto().equals(ponto)) {
							carona.addSolicitacao(solicitacao);
							break;
						}
				  }
				}else{
					throw new Exception("não há mais vagas na carona");
		            }
			}else{
				throw new Exception("o usuário já esta na carona");
			}
		
		}else{
			throw new Exception("o usuario eh o dono da carona");
		}
	
		return solicitacao.getIdSolicitacao();
	}

	public String solicitarVaga(String idSessao, String idCarona) throws Exception {
		Carona carona = buscaCaronaID(idCarona);
		Solicitacao solicitacao = new Solicitacao(idSessao, idCarona);
        Sessao sessao = buscarSessaoId(idSessao);
        Usuario usuario = buscaUsuario(sessao.getLogin());
        
        if (!(usuario.getLogin().equals(carona.getDonoDaCarona().getLogin()))) { // se nao eh o dono que esta pedindo carona
        	if (!usuario.getListaDeCaronasQueParticipa().contains(carona)) {
	        	if (carona.getVagas() > 0) {
	        		carona.addSolicitacao(solicitacao);
				}else{
					throw new Exception("não há mais vagas na carona");
				}
	
			}else{
				throw new Exception("o usuário já esta na carona");
			}
	      
        }else{
        	throw new Exception("o usuário eh o dono da carona");
        }

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
    /**
     * metodo para aceitar a solicitacao de uma carona em um determinado ponto de encontro
     * @param idSessao
     * @param idSolicitacao
     * @throws Exception
     */
	public void aceitarSolicitacaoPontoEncontro(String idSessao,
			String idSolicitacao) throws Exception {
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		
		Sessao sessaoDeQuemSolicitou = buscarSessaoId(solicitacao.getIdSessao());
	    Usuario usuarioQueSolicitou = buscaUsuario(sessaoDeQuemSolicitou.getLogin());
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // se eh o dono da carona e ha vagas na carona
			carona.setVagas(carona.getVagas() - 1); // diminui vaga na carona
          //  addhistoricoVagasEmCaronas(usuarioQueSolicitou.getLogin(), carona.getIdDaCarona());
			carona.removeSolicitacao(solicitacao); // remove a solicitacao pq ja foi aceita
			usuarioQueSolicitou.addCaronaQueParticipa(carona);
			carona.addParticipante(usuarioQueSolicitou);
		//	carona.addPontoDeEncontro(solicitacao.getPonto()); //adiciona o ponto de encontro

		}
	}
    /**
     * aceita a solicitacao de um caroneiro por vaga,
     * diminui o numero de vagas na carona e adiciona o carona aos participantes
     * a carona eh adicionada a lista de caronas que o caroneiro participa
     * @param idSessao
     * @param idSolicitacao
     * @throws Exception
     */
	public void aceitarSolicitacao(String idSessao, String idSolicitacao)
			throws Exception {
		
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		carona.addSolicitacaoAceita(solicitacao);
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin()); 
		
		Sessao sessaoDeQuemSolicitou = buscarSessaoId(solicitacao.getIdSessao());
		Usuario usuarioQueSolicitou = buscaUsuario(sessaoDeQuemSolicitou.getLogin());
		
		if (carona.getDonoDaCarona().getLogin().equals(usuario.getLogin())) { //se eh o dono da carona
				carona.setVagas(carona.getVagas()-1);
			//	addhistoricoVagasEmCaronas(usuarioQueSolicitou.getLogin(), solicitacao.getIdCarona());
				carona.removeSolicitacao(solicitacao); // remove a solicitacao pq ja foi aceita
				usuarioQueSolicitou.addCaronaQueParticipa(carona);
				carona.addParticipante(usuarioQueSolicitou);
			
		}
			

	}
    /**
     * metodo para rejeitar a solicitacao de uma vaga em um carona
     * @param idSessao
     * @param idSolicitacao
     * @throws Exception
     */
	public void rejeitarSolicitacao(String idSessao, String idSolicitacao)
			throws Exception {
		Sessao sessao = buscarSessaoId(idSessao);
		Solicitacao solicitacao = buscaSolicitacao(idSolicitacao);
		Carona carona = buscaCaronaID(solicitacao.getIdCarona());
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // se eh o dono da carona
			carona.removeSolicitacao(solicitacao);
		}



	}
    /**
     * metodo usado para o usuario que esta na carona desistir dela
     * @param idSessao
     * @param idCarona
     * @param idSugestao
     * @throws Exception 
     */
	public void desistirRequisicao(String idSessao, String idCarona) throws Exception {
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
        Usuario usuario = buscaUsuario(sessao.getLogin());
        
        if (usuario.getListaDeCaronasQueParticipa().contains(carona)) { // se o usuario participa da carona
			usuario.getListaDeCaronasQueParticipa().remove(carona);
			carona.setVagas(carona.getVagas()+1);
		}else{
			throw new Exception("Usuário não faz parte da carona");
		}  

	}
	/**
	 * metodo para o dono da carona cancelar a carona
	 * @param idSessao
	 * @param idCarona
	 * @throws Exception 
	 */
	public void cancelarCarona(String idSessao, String idCarona) throws Exception{	
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		
		if (carona.getDonoDaCarona().getLogin().equals(usuario.getLogin())) { // se eh o dono da carona
			
			for (Usuario usuarioDaCarona : carona.getListaDeParticipantes()) {
		        usuarioDaCarona.removeCaronaQueParticipa(carona);
			}
		}else{
			throw new Exception("o Usuário não é o dono da carona");
		}
		usuario.removeCarona(carona);
		listaDeCaronas.remove(carona);
	}
	
	/**
	 * cancela o interesse de um usuario
	 * @param idSessao
	 * @param interesse
	 * @throws Exception 
	 */
	public void cancelarInteresse(String idSessao, Interesse interesse) throws Exception{
		
		if (interesse.getIdSessao().equals(idSessao)) {
			removeInteresse(interesse);
		}else{
			throw new Exception("você não é o dono do interesse");
		}
	}
	/**
	 * remove o interesse da lista
	 * @param interesse
	 */
	private void removeInteresse(Interesse interesse) {
		
		for (Interesse interesseFor : listaDeInteresses) {
			
			if (interesseFor.getData().equals(interesse.getData()) && interesseFor.getOrigem().equals(interesse.getOrigem()) && interesseFor.getDestino().equals(interesse.getDestino()) && interesseFor.getHoraInicial().equals(interesse.getHoraInicial()) && interesseFor.getHoraFim().equals(interesse.getHoraFim())) {
				listaDeInteresses.remove(interesseFor);
				break;
			}
			
		}
		
	}

	/**
	 * o dono da carona pode excluir um caroneiro
	 * @param idSessao
	 * @param idCarona
	 * @param loginCaroneiro
	 */
	public void excluirCaroneiroDaCarona(String idSessao, String idCarona, String loginCaroneiro){
		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Usuario caroneiro = buscaUsuario(loginCaroneiro);
		if (carona.getDonoDaCarona().getLogin().equals(sessao.getLogin())) { // se eh o dono da carona
			if (participaDaCarona(loginCaroneiro, idCarona)) {
				carona.removeParticipante(caroneiro);
				caroneiro.removeCaronaQueParticipa(carona);
				carona.setVagas(carona.getVagas()+1);
			}
		}
	}
	/**
	 * 
	 * @param login
	 * @param idCarona
	 * @return true se o usuario esta na carona procurada
	 */
	public boolean participaDaCarona(String login, String idCarona){
		Carona carona = buscaCaronaID(idCarona);
		boolean participa = false;
		for (Usuario usuario : carona.getListaDeParticipantes()) {
			if (usuario.getLogin().equals(login)) {
				participa = true;
				break;
			}
		}
		return participa;
	}
	
	/**
	 * metodo para deletar um ponto de encontro da carona,
	 * o dono deleta qualquer ponto e o participante o ponto que ele sugeriu
	 * @param idSessao
	 * @param idCarona
	 * @param pontoDeEncontro
	 * @throws Exception
	 */
	public void deletarPontoDeEncontro(String idSessao, String idCarona, PontoDeEncontro pontoDeEncontro) throws Exception{
		
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Carona carona = buscaCaronaID(idCarona);
		
		if (carona.getDonoDaCarona().getLogin().equals(usuario.getLogin())) { // se o usuario eh o dono da carona
			
			carona.removePontoDeEncontro(pontoDeEncontro);
			
		}else if (participaDaCarona(usuario.getLogin(), idCarona)) { // se participa da carona mas nao eh o dono
			for (PontoDeEncontro ponto : carona.getPontoDeEncontro()) {
				if (ponto.getUsuario().getLogin().equals(usuario.getLogin())) { //achou o ponto ja que um usuario so possui um unico ponto de encontro
					carona.removePontoDeEncontro(pontoDeEncontro);
					break;
				}
			}
		
		}else {
			throw new Exception("Usuário não faz parte da carona");
		}
			
		
	}
	
	/**
	 * procura em uma carona um ponto de encontro sugerido por um usuario
	 * @param login
	 * @param idCarona
	 * @return um objeto ponto de encontro
	 */
	public PontoDeEncontro buscaPontoPeloLogin(String login, String idCarona){
		Carona carona = buscaCaronaID(idCarona);
		PontoDeEncontro respPonto = null;
		for (PontoDeEncontro ponto : carona.getPontoDeEncontro()) {
			if (ponto.getUsuario().getLogin().equals(login)) {
				respPonto = ponto;
				break;
			}
		}
		
		return respPonto;
	}


	public void excecaoResponderPontoDeEncontro(String pontos) throws Exception {
		if (pontos.equals("") || pontos == null) {
			throw new Exception("Ponto Inválido");
		}

	}

	public void excecaoSolicitacao(String ponto, Carona carona)
			throws Exception {
		boolean pontoValido = false;
		for (PontoDeEncontro ponto2 : carona.getPontoDeEncontro()) {
          	if (ponto.equals(ponto2.getPonto())) {
					pontoValido = true;
					break;
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
    
	/**
	 * adiciona no no historico do usuario a carona que ele ofereceu
	 * @param login
	 * @param idCarona
	 */
	public void addCaronaNoHistorico(String login, String idCarona) {
		Usuario usuario = buscaUsuario(login);
		usuario.addHistoricoCaronas(buscaCaronaID(idCarona));
	}
    
	/**
	 * adiciona no historico do caroneiro a carona que ele participou
	 * @param login
	 * @param idCarona
	 */
	public void addhistoricoVagasEmCaronas(String login, String idCarona) {
		Usuario usuario = buscaUsuario(login);
		usuario.addHistoricoVagasEmCaronas(buscaCaronaID(idCarona));
	}

	
	public void reiniciarSistema() throws Exception {

	    Serializador<Collection> ser = new Serializador<Collection>();
	    
	    this.listaDeCaronas = (List<Carona>) ser.recuperar("Caronas");
	    this.listaDeUsuarios  = (List<Usuario>) ser.recuperar("Usuarios");
	    this.listaDeInteresses  = (List<Interesse>) ser.recuperar("Interesses");
	    
	    addCaronaNaListaDeCaronaEspecifica();

     
	}

	public Carona getCaronaUsuario(String idSessao, int indexCarona) {
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Carona carona = usuario.getListaDeCaronasDoUsuario().get(indexCarona);

		return carona;
	}

	
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
    /**
     * 
     * @param idSessao
     * @param idCarona
     * @return uma lista com objetos do tipo ponto de encontro que contem o poto sugerido e o usuario que sugeriu
     */
	public List<PontoDeEncontro> getPontosEncontro(String idSessao, String idCarona) {

		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		List<PontoDeEncontro> pontosEncontro = new ArrayList<PontoDeEncontro>();
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {

			pontosEncontro = carona.getPontoDeEncontro();

		}
		return pontosEncontro;
	}
    /**
     * após a carona o dono faz o review do que aconteceu com a carona
     * @param idSessao
     * @param idCarona
     * @param loginCaroneiro
     * @param review
     * @throws Exception
     */
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
					caroneiro.getListaDeCaronasQueParticipa().remove(carona);
				} else if (review.equals("não faltou")) {
					caroneiro.addPresencaEmCaronas();
					addhistoricoVagasEmCaronas(caroneiro.getLogin(), idCarona);
					caroneiro.getListaDeCaronasQueParticipa().remove(carona);
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
			   if(review.equals("segura e tranquila")){
				  donoDaCarona.addCaronasSeguras();	   
			      addCaronaNoHistorico(donoDaCarona.getLogin(), idCarona);
			   }
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
		listaDeCaronasMunicipais.clear();
		listaDeInteresses.clear();
	 //   encerrarSistema();

	}

	public static void main(String[] args) throws Exception {
		
		SistemaDeCarona2 s = getInstanceOf();
		s.criarUsuario("Hudson", "123", "Hudson Daniel", "edesio silva", "hudson@gmail.com");
		
		/*s.criarUsuario("Hudson", "123", "Hudson Daniel", "edesio silva", "hudson@gmail.com");
		s.criarUsuario("Hudson2", "123", "Hudson Daniel", "edesio silva", "hudson@gmaill.com");
		s.criarUsuario("Hudson3", "123", "Hudson Daniel", "edesio silva", "hudson@gmailll.com");
		String idSessaoHudson = s.abrirSessao("Hudson", "123");
		String idSessaoHudson2 = s.abrirSessao("Hudson2", "123");
		String idSessaoHudson3 = s.abrirSessao("Hudson3", "123");
		
		s.cadastrarInteresse(idSessaoHudson3, "campina", "Caruaru", "20/03/2013", "", "15:00");
		String idCarona = s.cadastrarCarona(idSessaoHudson, "campina", "Caruaru", "20/03/2013", "15:00", "2");
		
		Carona carona = s.buscaCaronaID(idCarona);
		carona.addPontoDeEncontro(new PontoDeEncontro(s.buscaUsuario("Hudson"), "centro"));
		String idSolicitacao = s.solicitarVagaPontoEncontro(idSessaoHudson2, idCarona, "centro");
		System.out.println(carona.getVagas());
		s.aceitarSolicitacaoPontoEncontro(idSessaoHudson, idSolicitacao);
		System.out.println(carona.getVagas());*/
		/*System.out.println(s.buscaUsuario("Hudson3").getMensagens());
		
		
		Carona carona = s.buscaCaronaID(idCarona);
		
		s.sugerirPontoEncontro(idSessaoHudson, idCarona, "centro");
		String idSolicitacao = s.solicitarVagaPontoEncontro(idSessaoHudson2, idCarona,"centro");
		s.aceitarSolicitacao(idSessaoHudson, idSolicitacao);
		
		String idSolicitacao2 = s.solicitarVagaPontoEncontro(idSessaoHudson3, idCarona,"centro");
		s.rejeitarSolicitacao(idSessaoHudson, idSolicitacao2);
		
	//	carona.addPontoDeEncontro(new PontoDeEncontro(s.buscaUsuario("Hudson2"),"centro"));
		
	//	String idSolicitacao = s.solicitarVagaPontoEncontro(idSessaoHudson2, idCarona,"centro");
		
	//	s.aceitarSolicitacaoPontoEncontro(idSessaoHudson, idSolicitacao);
		
		String idSugestao = s.sugerirPontoEncontro(idSessaoHudson2, idCarona, "alto branco");
		
		s.responderSugestaoPontoEncontro(idSessaoHudson, idCarona, idSugestao, "alto branco");
		
		String idSugestao2 = s.sugerirPontoEncontro(idSessaoHudson2, idCarona, "centro");
		
		s.responderSugestaoPontoEncontro(idSessaoHudson, idCarona, idSugestao2, "parque da crianca");
		
		String idSugestao3 = s.sugerirPontoEncontro(idSessaoHudson, idCarona, "centro");
        
		s.reviewVagaEmCarona(idSessaoHudson, idCarona, "Hudson2", "faltou");
		System.out.println("vagas na carona: "+carona.getVagas());
		System.out.println(carona.getListaDeParticipantes().size());
		System.out.println(s.buscaUsuario("Hudson3").getListaDeCaronasQueParticipa().size());
		
		s.encerrarSistema();
	//	s.reiniciarSistema();
		System.out.println(s.listaDeCaronas.size());
		*/
		
	}

}
