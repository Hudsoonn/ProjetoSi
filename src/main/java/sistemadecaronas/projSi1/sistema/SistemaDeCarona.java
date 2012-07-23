package sistemadecaronas.projSi1.sistema;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import sistemadecaronas.projSi1.auxiliar.MergeSort;
import sistemadecaronas.projSi1.auxiliar.TrataDatas;
import sistemadecaronas.projSi1.auxiliar.TrataEmail;
import sistemadecaronas.projSi1.persistencia.SalvaDados;
import sistemadecaronas.projSi1.persistencia.Serializador;

public class SistemaDeCarona {

	/**
	 * @param args
	 */
	public List<Usuario> listaDeUsuarios = new  ArrayList<Usuario>();
	public List<Carona> listaDeCaronas = new ArrayList<Carona>();
	public List<Carona> listaDeCaronasInterMunicipais = new ArrayList<Carona>();
	public List<Carona> listaDeCaronasMunicipais = new ArrayList<Carona>();
	public List<Carona> listaDeCaronasRelampago = new ArrayList<Carona>();
	public List<Sessao> listaDeSessoesAbertas = new ArrayList<Sessao>();
	public List<Interesse> listaDeInteresses = new ArrayList<Interesse>();
	private static SistemaDeCarona sistemaDeCarona = null;

	private SistemaDeCarona(){
	}
    
	public static SistemaDeCarona getInstanceOf(){
		if (sistemaDeCarona == null) {
			sistemaDeCarona = new SistemaDeCarona();
		}
		return sistemaDeCarona;
	}

	
	/**
	 * metodo para criar uma usuario
	 * @param login
	 * @param senha
	 * @param nome
	 * @param endereco
	 * @param email
	 * @throws Exception
	 */
	public void criarUsuario(String login, String senha, String nome,
			String endereco, String email) throws Exception {

		excecaoCriarUsuario(login, senha, nome, endereco, email);

		Usuario novoUsuario = new Usuario(login, senha, nome, endereco, email);
		listaDeUsuarios.add(novoUsuario);
		
		salvaUsuarios();
		
	}
	
	

	public void salvaUsuarios(){
      Serializador<Collection> ser = Serializador.getInstanceOf();	
		ser.salvar("Usuarios", listaDeUsuarios);
	}
	public void salvaCaronas(){
		Serializador<Collection> ser = Serializador.getInstanceOf();	
		ser.salvar("Caronas", listaDeCaronas);
		
	}
	
	public void salvaInteresses(){
		Serializador<Collection> ser = Serializador.getInstanceOf();	
		ser.salvar("Interesses", listaDeInteresses);
	}
	public void encerrarSistema() {
        
		salvaUsuarios();
		salvaCaronas();
		salvaInteresses();


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
			throw new SessaoInvalidaException("Sessão inválida");
		}

		if (!isSessaoAberta(idDaSessao)) {
			throw new SessaoInvalidaException("Sessão inexistente");
		}
		if (origem == null || origem.equals("")) {
			throw new OrigemInvalidaException("Origem inválida");
		}

		if (destino == null || destino.equals("")) {
			throw new DestinoInvalidoException("Destino inválido");
		}
		
		if (hora == null || hora.equals("") || !TrataDatas.horaValida(hora)) {
			throw new HoraInvalidaException("Hora inválida");
		}

		if (data == null || data.equals("") || !TrataDatas.isDataValida(data,hora)) {
			throw new DataInvalidaException("Data inválida");
		}


		if (vagas == null || vagas.equals("")) {
			throw new VagaInvalidaException("Número de vagas inválido");
		} else {

			try {
				int vagasInt  = Integer.parseInt(vagas);
				if (vagasInt <= 0) {
					throw new Exception();
				}
			} catch (Exception e) {
		
				throw new VagaInvalidaException("Número de vagas inválido");
			}
		}
		
	 Sessao sessao = buscarSessaoId(idDaSessao);
     Usuario usuario = buscaUsuario(sessao.getLogin());
     
	 if (temCaronaNoHorario(idDaSessao, data, hora, usuario.getListaDeCaronasDoUsuario())) {
		throw new Exception("você já tem uma carona em um horario proximo");
	  }
	 
	 if (participaDeCaronaNoHorario(idDaSessao, data, hora, listaDeCaronas)) {
		 throw new Exception("você já participa de uma carona em um horario proximo");
	}


	}
	
	public void excecaoCriacaoCaronaRelampago(String nMinimoCaroneiros,String vagas) throws Exception{
		
		int nMinVagas = 0;
		
		if (nMinimoCaroneiros == null || nMinimoCaroneiros.equals("")) {
			
			throw new VagaInvalidaException("Número minimo de vagas inválido");
		}else{
			
			try {
			   nMinVagas = Integer.parseInt(nMinimoCaroneiros);
			   if (nMinVagas <= 0) {
				throw new Exception();
			  }
			} catch (Exception e) {
				
				throw new VagaInvalidaException("Número minimo de vagas inválido");
			}
			
		}	
		
		int nVagas = Integer.parseInt(vagas);
	    if (nVagas < nMinVagas) {
			throw new VagaInvalidaException("O número de vagas é menor que o número minimo de vagas");
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
			throw new LoginInvalidoException("Login inválido");
		}
		if (nome == null || nome.equals("")) {
			throw new NomeInvalidoException("Nome inválido");
		}
		
		if (senha.equals("") || senha == null) {
			throw new SenhaInvalidaException("Senha inválida");
		}

		if (email == null || email.equals("") || !TrataEmail.emailValido(email)) {
			throw new EmailInvalidoException("Email inválido");
		}

		for (int i = 0; i < listaDeUsuarios.size(); i++) {
			if (listaDeUsuarios.get(i).getLogin().equals(login)) {
				throw new LoginInvalidoException("Já existe um usuário com este login");
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
	public String cadastrarCarona(String idSessao, String origem,String destino, String data, String hora, String vagas)throws Exception {
		excecaoDeCriacaoDeCarona(idSessao, origem, destino, data, hora, vagas);
		
		Sessao sessao = buscarSessaoId(idSessao);
	
		Carona novaCarona = new CaronaIntermunicipal(origem, destino, data,hora, Integer.parseInt(vagas));
		novaCarona.setDonoDaCarona(buscaUsuario(sessao.getLogin()));
		listaDeCaronas.add(novaCarona);//
		listaDeCaronasInterMunicipais.add((CaronaIntermunicipal) novaCarona);

		Usuario usuario = buscaUsuario(sessao.getLogin());
		usuario.addCarona(novaCarona);
		

		salvaCaronas();
		
			//Verificar se existe interesse
			verificaInteresse(idSessao, origem, destino, data, hora);
		
		return novaCarona.getIdDaCarona();

	}
	
	public String cadastrarCaronaRelampago(String idSessao, String origem, String destino, String data, String hora,String vagas, String nMinimoCaroneiros) throws Exception{
		       
        excecaoDeCriacaoDeCarona(idSessao, origem, destino, data, hora, vagas);
		excecaoCriacaoCaronaRelampago(nMinimoCaroneiros, vagas);
        
		Sessao sessao = buscarSessaoId(idSessao);
		
		Carona novaCarona = new CaronaRelampago(origem, destino, data,hora,Integer.parseInt(vagas), Integer.parseInt(nMinimoCaroneiros));
		novaCarona.setDonoDaCarona(buscaUsuario(sessao.getLogin()));
		listaDeCaronas.add(novaCarona);//
		listaDeCaronasRelampago.add((CaronaRelampago) novaCarona);

		Usuario usuario = buscaUsuario(sessao.getLogin());
		usuario.addCarona(novaCarona);
		

		salvaCaronas();
		
			//Verificar se existe interesse
			verificaInteresse(idSessao, origem, destino, data, hora);
		
		return novaCarona.getIdDaCarona();

	
	}
	
	private void enviaEmailCaronaRelampago(Carona carona){
		
		String mensagem = "a carona não deu certo";
		
		for (Usuario usuario : carona.getListaDeParticipantes()) {		
			enviaEmail(carona.getDonoDaCarona().getId(), usuario.getEmail(), mensagem);
			
		}
	}
	
	public void atualizaStatusCaronaRelampago(CaronaRelampago carona) throws Exception{
		
		final int TEMPO_MAXIMO_CARONA_RELAMPAGO = 48;
		
		if (passouNumeroDeHoras(carona.getData(), carona.getHora(), TEMPO_MAXIMO_CARONA_RELAMPAGO) && carona.getVagas() != 0) {
			
			carona.setCaronaExpired();
		//	enviaEmailCaronaRelampago(carona);
			cancelarCarona(carona.getDonoDaCarona().getId(), carona.getIdDaCarona());
			
		}
		
	}
	
	public boolean passouNumeroDeHoras(String dataCarona,String horaCarona, int nHorasParaAumentar) throws ParseException{
		
		boolean passouNumeroDeHoras = false;
		
		GregorianCalendar calendar = new GregorianCalendar();
		GregorianCalendar calendarDepois = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		date = sdf.parse(dataCarona);
		calendarDepois.setTime(date);
		calendarDepois.set(calendarDepois.HOUR_OF_DAY, Integer.parseInt(horaCarona.split(":")[0]));
		calendarDepois.set(calendarDepois.MINUTE,  Integer.parseInt(horaCarona.split(":")[1]));
		calendarDepois.add(calendarDepois.HOUR, nHorasParaAumentar);
		
		if (calendar.after(calendarDepois)) {
			passouNumeroDeHoras = true;
		}
		
		return passouNumeroDeHoras;
		
	}
	/**
	 * verifica se o usuario tem um carona num horario proximo
	 * @param idSessao
	 * @param data
	 * @param hora
	 * @param listaDeCaronas
	 * @return true se tem uma carona no horario proximo
	 * @throws Exception
	 */
	public boolean temCaronaNoHorario(String idSessao, String data, String hora, List<Carona> listaDeCaronas) throws Exception{
        
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		
		
		return verificaCaronaNoIntervalo(idSessao, data, hora, usuario.getListaDeCaronasDoUsuario());
	}
	
	
	/**
	 * verifica se o usuario participa de alguma carona em um horario proximo
	 * @param idSessao
	 * @param data
	 * @param hora
	 * @param listaDeCaronas
	 * @return true se participa de uma carona no horario proximo
	 * @throws Exception
	 */
	public boolean participaDeCaronaNoHorario(String idSessao, String data, String hora, List<Carona> listaDeCaronas) throws Exception{
         
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		
		
		return verificaCaronaNoIntervalo(idSessao, data, hora, usuario.getListaDeCaronasQueParticipa());
	}
	
	/**
	 * verifica se o usuario esta oferencendo ou incluso em alguma carona em um intervalo de tempo
	 * @param idSessao
	 * @param data
	 * @param hora
	 * @param listaDeCaronas
	 * @return true se esta oferencendo uma carona ou incluso no intervalo de tempo
	 * @throws ParseException
	 */
	public boolean verificaCaronaNoIntervalo(String idSessao, String data, String hora, List<Carona> listaDeCaronas) throws ParseException{
		
		boolean participaNoHorario = false;
		
		GregorianCalendar calendar = new GregorianCalendar();
		GregorianCalendar calendarAntes = new GregorianCalendar();
		GregorianCalendar calendarDepois = new GregorianCalendar();
		GregorianCalendar calendarExigido = new GregorianCalendar();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		for (Carona carona : listaDeCaronas) {		  
		    
			date = sdf.parse(carona.getData());
			calendar.setTime(date);
			
			String[] horaStr = carona.getHora().split(":");
			String[] horaStr2 = hora.split(":");
			calendar.add(calendar.HOUR, Integer.parseInt(horaStr[0]));
			calendar.add(calendar.MINUTE, Integer.parseInt(horaStr[1]));
			
			calendarAntes.setTime(calendar.getTime());
			calendarDepois.setTime(calendar.getTime());
			
			calendarAntes.add(calendarAntes.HOUR, -1);
			calendarDepois.add(calendarDepois.HOUR, 1);
			
			date = sdf.parse(data);
			calendarExigido.setTime(date);
			calendarExigido.add(calendarExigido.HOUR, Integer.parseInt(horaStr2[0]));
			calendarExigido.add(calendarExigido.MINUTE, Integer.parseInt(horaStr2[1]));
			
			if (carona.tipoDeCarona().equals("Municipal")) {				         

				if ((calendarExigido.after(calendarAntes) || calendarExigido.equals(calendarAntes)) && (calendarExigido.before(calendarDepois) || calendarExigido.equals(calendarDepois))) {
					participaNoHorario = true;
				}
			}else{
				
				calendarAntes.add(calendarAntes.HOUR, -1);
				calendarDepois.add(calendarDepois.HOUR, 1);
				 
				  if ((calendarExigido.after(calendarAntes) || calendarExigido.equals(calendarAntes)) && (calendarExigido.before(calendarDepois) || calendarExigido.equals(calendarDepois))) {
					  
					  participaNoHorario = true;
					}
			}
		}
		return participaNoHorario;
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
		excecaoCidade(cidade);
		Sessao sessao = buscarSessaoId(idDaSessao);
		int vagasInt = Integer.parseInt(vagas);
		Carona novaCarona = new CaronaMunicipal(origem, destino, cidade,
				data, hora, vagasInt);
		novaCarona.setDonoDaCarona(buscaUsuario(sessao.getLogin()));
		listaDeCaronas.add(novaCarona);//
		listaDeCaronasMunicipais.add((CaronaMunicipal) novaCarona);


		Usuario usuario = buscaUsuario(sessao.getLogin());
		usuario.addCarona(novaCarona);
		
		salvaCaronas();
		
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
		
		Carona carona = new CaronaIntermunicipal(origem, destino, data, hora, 0);
		for(Interesse interesse : listaDeInteresses)
		{
			if(caronaEhDeInteresse(carona, interesse))
			{   
				String loginDonoCarona = buscarSessaoId(idDaSessao).getLogin();
				System.out.println(interesse.getIdSessao());
				String loginCaroneiro = buscaUsuarioId(interesse.getIdSessao()).getLogin();
				Conteudo conteudo =  new ConteudoTexto(String.format(mensagensDoSistema.MENSAGEM_INTERESSE.getMensagem(),data,hora,buscaUsuario(loginDonoCarona).getEmail()));
				Mensagem mensagem = new Mensagem(loginDonoCarona, loginCaroneiro, conteudo);
				
				enviaMensagem(mensagem);
			}
		}
		
	}
   /**
     * metodo que verifica se um horario esta entre outros dois
     * @param h
     * @param horaInicial
     * @param horaFim
     * @return true se o hora esta entre as outras duas
     * @throws ParseException
     */
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
	
		Usuario destinatario = buscaUsuario(mensagem.getDestinatario());
		destinatario.addMensagem(mensagem);
	}
	
	/**
	 * metodo para enviar email
	 * @param idSessao
	 * @param emailDestinatario
	 * @param mensagem
	 */
	public void enviaEmail(String idSessao, String emailDestinatario, String mensagem) {
		
		if (isSessaoAberta(idSessao)) {	
	  	MailJava eP = new MailJava();
        //configuracoes de envio
        eP.setSmtpHostMail("smtp.gmail.com");
        eP.setSmtpPortMail("587");
        eP.setSmtpAuth("true");
        eP.setSmtpStarttls("true");
        eP.setUserMail("sistemadecaronas@gmail.com");
        eP.setFromNameMail("Sistema de carona");
        eP.setPassMail("12345654321ab");
        eP.setCharsetMail("ISO-8859-1");
        eP.setSubjectMail("Notificação");
        eP.setBodyMail(mensagem);
        eP.setTypeTextMail(MailJava.TYPE_TEXT_HTML);
        
   	 String[] Arrayservidor = emailDestinatario.split("@");
     String servidorAux = Arrayservidor[1];
     servidorAux = servidorAux.replace(".", " ");
     String[] Arrayservidor2 = servidorAux.split(" ");
     String servidor = Arrayservidor2[0];

        
        //sete quantos destinatarios desejar
        Map<String, String> map = new HashMap<String, String>();
        map.put(emailDestinatario, "email "+servidor);

        eP.setToMailsUsers(map);
        
        List<String> files = new ArrayList<String>();
        
        eP.setFileMails(files);
        
        MailJavaSender m = new MailJavaSender();

			try {
				m.senderMail(eP);
			} catch (UnsupportedEncodingException e) {
	     		e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
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

			
			
			if (!origem.equals("") && !destino.equals("")) { // todas as caronas de um destino a uma origem 

				if (carona.getDestino().equals(destino)
						&& carona.getOrigem().equals(origem)) {

					caronasEncontradas .add(carona);

				}
			}

			if (!origem.equals("") && destino.equals("")) { // todas as carona a partir da origem
				if (carona.getOrigem().equals(origem)) {

					caronasEncontradas.add(carona);

				}
			}

			if (origem.equals("") && !destino.equals("")) { // todas as caronas com aquele destino
				if (carona.getDestino().equals(destino)) {

					caronasEncontradas.add(carona);

				}
			}

			if (origem.equals("") && destino.equals("")) { // todas as caronas

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
	public List<Carona> localizarCaronaInterMunicipal(String idSessao, String origem, String destino)
			throws Exception {

		excecaoLocalizarCarona(idSessao, origem, destino);
		List<Carona> caronasEncontradas = new ArrayList<Carona>();

		if (isSessaoAberta(idSessao)) {
			caronasEncontradas = listaCaronas(listaDeCaronasInterMunicipais, origem, destino);
		}


		return caronasEncontradas;
	}
	
	/**
	 * localiza uma carona ou mais caronas
	 * @param listaDeCaronas
	 * @param idSessao
	 * @param origem
	 * @param destino
	 * @return lista com as caronas
	 * @throws Exception
	 */
	public List<Carona> localizarCarona(List<Carona> listaDeCaronas,String idSessao, String origem, String destino)
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
		
				if(((CaronaMunicipal) carona).getCidade().equals(cidade))
				{
					caronaMunicipal.add(carona);
				
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
	public List<Carona> localizarCaronaMunicipal(String idSessao,String origem, String destino, String cidade) throws Exception {
		excecaoCidade(cidade);
		
		List<Carona> caronasEncontradas = localizarCarona(listaDeCaronasMunicipais,idSessao, origem, destino);
		
		return localizarPorCidade(cidade, caronasEncontradas);
		
	}
	/**
	 * adiciona as caronas nas lista de Municipais e intermunicipais para facilitar a busca
	 */
	public void addCaronaNaListaDeCaronaEspecifica(){
		for (Carona carona : listaDeCaronas) {
			if (carona.tipoDeCarona().equals("Municipal")) {
				listaDeCaronasMunicipais.add((CaronaMunicipal) carona);
			}else{
				listaDeCaronasInterMunicipais.add((CaronaIntermunicipal) carona);
			}
		}
	}

    
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
			throw new SessaoInvalidaException("Sessão inválida");
		}
		if (idDaSessao.equals("") || !isSessaoAberta(idDaSessao)) {

			throw new SessaoInvalidaException("Sessão inexistente");
		}

		if (origem == null) {
			throw new OrigemInvalidaException("Origem Inexistente");
		}

		if (destino == null) {
			throw new DestinoInvalidoException("Destino Inexistente");
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

		Sessao sessao = buscarSessaoId(idSessao);
		Carona carona = buscaCaronaID(idCarona);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		excecaoResponderPontoDeEncontro(pontos);
        
		Sugestao sugest = buscaSugestao(idSugestao, idCarona);
		Usuario usuarioQueSugeriu = buscaUsuario(buscarSessaoId(sugest.getIdSessao()).getLogin());
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {  // verifica se eh o dono da carona

			for (Sugestao sugestao : carona.getSugestoes()) {
				if (sugestao.getIdSugestao().equals(idSugestao)) { // se a sugestao eh a que eu procuro
					
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
		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());
		Carona carona = buscaCaronaID(idCarona);
		Solicitacao solicitacao = new Solicitacao(idSessao, idCarona, ponto);
		excecaoSolicitacaoPontoEncontro(ponto, carona);
        
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
        	
        	if (carona.ehPreferencial()) { // se for uma carona preferencial
        		if (usuarioTemPreferenciaNaCarona(usuario.getLogin(), carona)) { // se o usuario tem preferencia
					
        		}else{
        			throw new Exception("Usuário não está na lista preferencial da carona");
        		}
        	}
        	if (!usuario.getListaDeCaronasQueParticipa().contains(carona)) {
        		if (!temCaronaNoHorario(idSessao, carona.getData(), carona.getHora(), usuario.getListaDeCaronasDoUsuario()) && !participaDeCaronaNoHorario(idSessao, carona.getData(), carona.getHora(), usuario.getListaDeCaronasQueParticipa())) {			
	        	    if (carona.getVagas() > 0) {
	        		   carona.addSolicitacao(solicitacao);
				}else{
					throw new Exception("não há mais vagas na carona");
				}
	        	
        		}else{
            	throw new Exception("o usuário já tem ou paticipa de carona num horario proximo");
        	   } 
			}else{
				throw new Exception("o usuário já esta na carona");
			}      	
        }else{
        	throw new Exception("o usuário é o dono da carona");
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
    
	/**
	 * busca através do id uma solicitacao na lista 
	 * @param idSolicitacao
	 * @return solicitacao encontrada
	 * @throws Exception
	 */
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
			if (carona.getVagas() > 0 ) {
			carona.setVagas(carona.getVagas() - 1); // diminui vaga na carona
			carona.removeSolicitacao(solicitacao); // remove a solicitacao pq ja foi aceita
			usuarioQueSolicitou.addCaronaQueParticipa(carona);
			carona.addParticipante(usuarioQueSolicitou);
			

		//	enviaEmail(idSessao, usuarioQueSolicitou.getEmail(), "sua solicitacao na carona: "+getInformacoesCarona(carona.getIdDaCarona())+" foi aceita!.");

			}else{		
	
	    //    	enviaEmail(idSessao, usuarioQueSolicitou.getEmail(), "sua solicitacao na carona: "+getInformacoesCarona(carona.getIdDaCarona())+" foi rejeitada por falta de vagas.");
				throw new Exception("solicitacao rejeitada por falta de vagas");		
			}
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
			if (carona.getVagas() > 0 ) {
				
			
			    carona.setVagas(carona.getVagas()-1);
				carona.removeSolicitacao(solicitacao); // remove a solicitacao pq ja foi aceita
				usuarioQueSolicitou.addCaronaQueParticipa(carona);
				carona.addParticipante(usuarioQueSolicitou);
               	
			//    enviaEmail(idSessao, usuarioQueSolicitou.getEmail(), "sua solicitacao na carona: "+getInformacoesCarona(carona.getIdDaCarona())+" foi aceita!.");
			}else{
	
			//	enviaEmail(idSessao, usuarioQueSolicitou.getEmail(), "sua solicitacao na carona: "+getInformacoesCarona(carona.getIdDaCarona())+" foi rejeitada por falta de vagas.");
				throw new Exception("solicitacao rejeitada por falta de vagas");
			}
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

    /**
     * lanca excecao no metodo responde ponto de encontro
     * @param pontos
     * @throws Exception
     */
	public void excecaoResponderPontoDeEncontro(String pontos) throws Exception {
		if (pontos.equals("") || pontos == null) {
			throw new PontoInvalidoException("Ponto Inválido");
		}

	}
    
	/**
	 * lanca excecao na solicitacao de vaga em um ponto de encontro
	 * @param ponto
	 * @param carona
	 * @throws Exception
	 */
	public void excecaoSolicitacaoPontoEncontro(String ponto, Carona carona)
			throws Exception {
		boolean pontoValido = false;
		for (PontoDeEncontro ponto2 : carona.getPontoDeEncontro()) {
          	if (ponto.equals(ponto2.getPonto())) {
					pontoValido = true;
					break;
				}
		}
		if (!pontoValido) {
			throw new PontoInvalidoException("Ponto Inválido");
		}
	}
    
	/**
	 * 
	 * @param idSessao
	 * @param login
	 * @return string com as informações do usuario
	 * @throws Exception
	 */
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

	/**
	 * metodo para reiniciar o sistema,povoa as listas com as infomacoes salvas nos arquivos
	 * @throws Exception
	 */
	public void reiniciarSistema() throws Exception {

	    Serializador<Collection> ser = Serializador.getInstanceOf();
	    
	    this.listaDeCaronas = (List<Carona>) ser.recuperar("Caronas");
	    this.listaDeUsuarios  = (List<Usuario>) ser.recuperar("Usuarios");
	    this.listaDeInteresses  = (List<Interesse>) ser.recuperar("Interesses");
	    
	    addCaronaNaListaDeCaronaEspecifica();

     
	}
    


	/**
	 * 
	 * @param idSessao
	 * @return lista de caronas de um usuario
	 */
	public List<Carona> getCaronasUsuario(String idSessao) {

		Sessao sessao = buscarSessaoId(idSessao);
		Usuario usuario = buscaUsuario(sessao.getLogin());


		return usuario.getListaDeCaronasDoUsuario();
	}
    
	/**
	 * 
	 * @param idSessao
	 * @param idCarona
	 * @return lista de solicitacoes confirmadas
	 */
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
    
	/**
	 * 
	 * @param idCarona
	 * @return lista de solicitacoes pendentes de uma carona
	 */
	public List<String> getSolicitacoesPendentes(String idCarona) {
		Carona carona = buscaCaronaID(idCarona);
		List<String> solicitacoesPendentes = new ArrayList<String>();

		for (Solicitacao solicitacao : carona.getListaDeSolicitacao()) {
			solicitacoesPendentes.add(solicitacao.getIdSolicitacao());
		}
		return solicitacoesPendentes;
	}
    /**
     * 
     * @param idSessao
     * @param idCarona
     * @return lista de pontos sugeridos de uma carona
     */
	public List<String> getPontosSugeridos(String idSessao, String idCarona) {
		Carona carona = buscaCaronaID(idCarona);
		Sessao sessao = buscarSessaoId(idSessao);
		List<String> pontosSugeridos = new ArrayList<String>();
		if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) { // se sessao eh do dono da carona
			
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
		
		 if (!tempoPraReviewJaAcabou(idCarona)) { //se ainda pode da review

			if (sessao.getLogin().equals(carona.getDonoDaCarona().getLogin())) {// se eh o dono da carona
				if (participaDaCarona(loginCaroneiro, idCarona)) {// Se esta na carona
					
		        if (!donoJaDeuReview(loginCaroneiro, carona)) {
					
				if (!tempoMinimoPraReviewPassou(idCarona)) {
					throw new Exception("você ainda não pode da review");
				}																
				if (review.equals("faltou")) {
					Review rv = new Review(caroneiro, "faltou");
					carona.addReviewVagaCarona(rv);
					caroneiro.addFaltasEmCaronas();
					caroneiro.getListaDeCaronasQueParticipa().remove(carona);
				} else if (review.equals("não faltou")) {
					Review rv = new Review(caroneiro, "não faltou");
	                caroneiro.addQuemDeuReviewPositivo(carona.getDonoDaCarona());
					carona.addReviewVagaCarona(rv);
					caroneiro.addPresencaEmCaronas();
					addhistoricoVagasEmCaronas(caroneiro.getLogin(), idCarona);
					caroneiro.getListaDeCaronasQueParticipa().remove(carona);
				}
				else throw new Exception("Opção inválida.");
				
			}else throw new Exception("você ja deu review nesse caroneiro");
		   } 
		} else throw new Exception("Usuário não possui vaga na carona.");
		 
		 }else throw new Exception("você não pode mais da review");
	
		

	}
	
	/**
	 * metodo para ver se ja pode da review. eh necessario um tempo minimo após a carona pra liberar o review
	 * @param idCarona
	 * @return true se ja pode da review
	 * @throws ParseException
	 */
	public boolean tempoMinimoPraReviewPassou(String idCarona) throws ParseException{
		
		final int TEMPO_MINIMO_CARONA_MUNICIPAL = 1;
		final int TEMPO_MINIMO_CARONA_INTERMUNICIPAL = 2;
		
		boolean tempoMinimoPassou = false;
		Carona carona = buscaCaronaID(idCarona);
		
		
		GregorianCalendar calendar = new GregorianCalendar();
    	GregorianCalendar calendar2 = new GregorianCalendar();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = sdf.parse(carona.getData());
		calendar.setTime(date);
		calendar.add(calendar.HOUR_OF_DAY, Integer.parseInt((carona.getHora().split(":"))[0]));
		calendar.add(calendar.MINUTE, Integer.parseInt((carona.getHora().split(":"))[1]));
		
		if (carona.tipoDeCarona().equals("Municipal")) {		
			calendar.add(calendar.HOUR_OF_DAY, TEMPO_MINIMO_CARONA_MUNICIPAL);
		}else{
			calendar.add(calendar.HOUR_OF_DAY, TEMPO_MINIMO_CARONA_INTERMUNICIPAL);
		}


		if (calendar2.after(calendar)) {
			
			tempoMinimoPassou = true;
		}
		return tempoMinimoPassou;
		
	}
	
	/**
	 * metodo para verificar se um review eh valido, para um review ser valido
	 * eh preciso que pelo menos 65% dos caroneiros tenham dado o mesmo review
	 * @param carona
	 * @return true se eh valido
	 * @throws ParseException
	 */
	public boolean reviewValido(Carona carona) throws ParseException{
		
		double numeroReviewBom  = 0 ;
		double numeroReviewRuim = 0 ; 
		boolean reviewValido = false;
        if (carona.getListaDeReview().size() == carona.getListaDeParticipantes().size() || tempoPraReviewJaAcabou(carona.getIdDaCarona())) {
			
		for (Review review : carona.getListaDeReview()) {
			
			if (review.getTIPO_REVIEW().equals("BOM")) {
				numeroReviewBom++;
			}else{
				numeroReviewRuim++;
			}
		}
		if ((numeroReviewBom == 0 && numeroReviewRuim != 0) || (numeroReviewBom != 0 && numeroReviewRuim == 0)) {
			reviewValido = true;
			
		}else{ 
			
		 if (numeroReviewBom/numeroReviewRuim >= 1.65 || numeroReviewRuim/numeroReviewBom >= 1.65) {
			reviewValido = true;
		   }		 
		}
        }
		
		
		
		return reviewValido;
	}
	
	/**
	 * metodo para verificar se ainda eh possivel da review. o tempo maximo defino foi 7 dias
	 * @param idCarona
	 * @return true se o não eh possivel da review
	 * @throws ParseException
	 */
	public boolean tempoPraReviewJaAcabou(String idCarona) throws ParseException{
		
        Carona carona = buscaCaronaID(idCarona);
        boolean tempoEsgotado = false;
        final int TEMPO_MAXIMO_REVIEW = 7;
		
		GregorianCalendar calendar = new GregorianCalendar();
    	GregorianCalendar calendar2 = new GregorianCalendar();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = sdf.parse(carona.getData());
		calendar.setTime(date);
		calendar.add(calendar.HOUR_OF_DAY, Integer.parseInt((carona.getHora().split(":"))[0]));
		calendar.add(calendar.MINUTE, Integer.parseInt((carona.getHora().split(":"))[1]));
		
		calendar.add(calendar.DAY_OF_MONTH, TEMPO_MAXIMO_REVIEW);
	
		if (calendar2.after(calendar)) {
			tempoEsgotado = true;
		}
		
		return tempoEsgotado;

		
	}
	
	/**
	 * metodo para verificar se o dono da carona ja deu review naquele caroneiro
	 * @param loginCaroneiro
	 * @param carona
	 * @return true se o dono ja deu review no caroneiro
	 */
	public boolean donoJaDeuReview(String loginCaroneiro, Carona carona){
		boolean donoDeuReview = false;
		for (Review review : carona.getListaDeReviewVagaCarona()) {
		   if (review.getUsuario().getLogin().equals(loginCaroneiro)) {
			donoDeuReview = true;
			break;
		}
		   
		}
		return donoDeuReview;
	}
	/**
	 * metodo que verifica se o caroneiro ja deu review naquela carona
	 * @param caroneiro
	 * @param carona
	 * @return true se ja deu review
	 */
	public boolean jaDeuReview(Usuario caroneiro, Carona carona){
		boolean deuReview = false;
		for (Review review : carona.getListaDeReview()) {
			if (review.getUsuario().getLogin().equals(caroneiro.getLogin())) {
				deuReview = true;
				break;
			}   
		}
		
		return deuReview;
	}
	
	/**
	 * metodo para o caroneiro relatar como foi a carona
	 * @param idSessao
	 * @param idCarona
	 * @param review
	 * @throws Exception
	 */
	public void reviewCarona(String idSessao, String idCarona, String review) throws Exception{
		
		final String TIPO_REVIEW_BOM = "BOM";
		final String TIPO_REVIEW_RUIM = "RUIM";
		
		
	 	Carona carona = buscaCaronaID(idCarona);
    	Usuario donoDaCarona = carona.getDonoDaCarona();
    	Usuario caroneiro = buscaUsuario(buscarSessaoId(idSessao).getLogin());
    	

    	if (participaDaCarona(caroneiro.getLogin(), idCarona)) { //se o caroneiro participa da carona
    	
			  if (tempoMinimoPraReviewPassou(idCarona) && !tempoPraReviewJaAcabou(idCarona)) { //se ja pode da review
				if (!jaDeuReview(caroneiro, carona)) {
			
			   if(review.equals("segura e tranquila")){
				  Review  rv = new Review(caroneiro, TIPO_REVIEW_BOM);
				  carona.addReview(rv);
				  donoDaCarona.addQuemDeuReviewPositivo(caroneiro);
				  if (reviewValido(carona)) {
				    donoDaCarona.addCaronasSeguras();	   
			        addCaronaNoHistorico(donoDaCarona.getLogin(), idCarona);
				  }
			   }
			    else if(review.equals("não funcionou")){
			    	Review  rv = new Review(caroneiro, TIPO_REVIEW_RUIM);
				    carona.addReview(rv);
				    donoDaCarona.diminuiPontuacao();
				    if (reviewValido(carona)) {
				    	donoDaCarona.addCaronasNaoFuncionaram();
					}
			    }else throw new Exception("Opção inválida.");
			      
			}else throw new Exception("você ja deu review");
			
			}else throw new Exception("você ainda não pode da review");
			  
			}else throw new Exception("Usuário não possui vaga na carona.");
		}
	
	
	public void difinirCaronaPreferencial(Carona carona){
		
		carona.setCaronaPreferencial(true);
	}
	
	public void atualizaCaronaPreferencial(Carona carona) throws ParseException{
		if (passouNumeroDeHoras(carona.getData(), carona.getHora(), 24)) {
			carona.setCaronaPreferencial(false);
		}
	}
	
	public boolean usuarioTemPreferenciaNaCarona(String login,Carona carona){
		
		boolean temPreferencia = false;
		for (Usuario usuario : carona.getListaDeUsuariosPreferencias()) {
			
			if (usuario.getLogin().equals(login)) {
				temPreferencia = true;
				break;
			}
		}		
		
		return temPreferencia;
	}
		
	
	/**
	 * meotodo para um usuario cadastrar interesse em uma carona
	 * @param idSessao
	 * @param origem
	 * @param destino
	 * @param data
	 * @param horaInicial
	 * @param horaFim
	 * @return id do interesse criado
	 * @throws Exception
	 */
	public String cadastrarInteresse(String idSessao, String origem, String destino, String data, String horaInicial, String horaFim) throws Exception
	{
		excecaoCadastrarInteresse(idSessao, origem, destino, data);
		Interesse interesse = new Interesse(idSessao, origem, destino, data, horaInicial, horaFim);
		listaDeInteresses.add(interesse);
		
		salvaInteresses();
		return interesse.getIdInteresse();
	}
	
	public List<Usuario> rankCrescente(){
		
	   
	List<Usuario> listaOrdemCrescente = new ArrayList<Usuario>();	
	List<Usuario> listaOrdemCrescenteAux = MergeSort.mergeSort(listaDeUsuarios);
	
	for (Usuario usuario : listaOrdemCrescenteAux) {
		listaOrdemCrescente.add(0, usuario);
	}
	
	
				
		return listaOrdemCrescente;
		
		
	}
	
	
	public List<Usuario> rankDecrescente(){
		
		   
		  List<Usuario> listaOrdemCrescente = MergeSort.mergeSort(listaDeUsuarios);
		
			
			return listaOrdemCrescente;
			
		}
	
	/**
	 * metodo que lanca excecoes ao cadastrar interesse
	 * @param idSessao
	 * @param origem
	 * @param destino
	 * @param data
	 * @throws Exception
	 */
	public void excecaoCadastrarInteresse(String idSessao, String origem, String destino,
			String data) throws Exception {
		//excecao se as horas forem null???
		if (origem == null) {
			throw new OrigemInvalidaException("Origem inválida");
		}
		
		if (destino == null) {
			throw new DestinoInvalidoException("Destino inválido");
		}
		
		if ( (data == null || data.equals("")) && ( origem.contains("!") || origem.contains("-") || (destino.contains("!") || (destino.contains("-")) ))) {
			throw new DataInvalidaException("Data inválida");
		}
		
		if (idSessao == null || idSessao.equals("")) {
			throw new SessaoInvalidaException("IdSessao inválido");
		}
		
		else if(origem.equals("-") || origem.equals("!"))
		{
			throw new OrigemInvalidaException("Origem inválida");

		}

		
		else if(destino.contains("-") || destino.contains("!"))
		{
			throw new DestinoInvalidoException("Destino inválido");
		}


	}
	
	
    /**
     * zera o sistema incluindo os arquivos,todos os dados são apagados
     */
	public void zerarSistema() {
		
     
		listaDeCaronas.clear();
		listaDeSessoesAbertas.clear();
		listaDeUsuarios.clear();
		listaDeCaronasInterMunicipais.clear();
		listaDeCaronasMunicipais.clear();
		listaDeInteresses.clear();
		listaDeInteresses.clear();
	    //encerrarSistema();

	}

	public static void main(String[] args) throws Exception {
		
		SistemaDeCarona s = SistemaDeCarona.getInstanceOf();
		
		
		s.reiniciarSistema();
		for (Usuario usuario : s.listaDeUsuarios) {
			s.abrirSessao(usuario.getLogin(), usuario.getSenha());
		}
		
		s.salvaUsuarios();
		Usuario usuario = s.buscaUsuario("mark");
		Usuario usuario2 = s.buscaUsuario("bill");
		s.listaDeInteresses.clear();
		s.salvaInteresses();
		s.cadastrarInteresse(usuario.getId(), "", "", "", "","");
		
		String idCarona = s.cadastrarCarona(usuario2.getId(), "campina", "caruaru", "24/07/2012", "13:00", "3");
		String idSolicitacao = s.solicitarVaga(usuario.getId(), idCarona);
		s.aceitarSolicitacao(usuario2.getId(), idSolicitacao);
		System.out.println(usuario.getListaDeMensagens().get(0).getConteudo().getConteudo());
		
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.set(gc.DAY_OF_YEAR, gc.get(gc.DAY_OF_YEAR)-3);
		
		String data = gc.get(gc.DAY_OF_MONTH)+"/"+((gc.get(gc.MONTH))+1)+"/"+gc.get(gc.YEAR);
		System.out.println(data);
		Carona carona = s.buscaCaronaID(idCarona);
		carona.setData(data);
		
		System.out.println(MergeSort.mergeSort(s.listaDeUsuarios).get(0).getListaReviewPositivos().size());
		s.reviewCarona(usuario.getId(), idCarona, "segura e tranquila");
		s.reviewVagaEmCarona(usuario2.getId(), idCarona, "bill", "não faltou");
		System.out.println(s.rankCrescente().get(1).getLogin());
		
		
		
		
	


	//	System.out.println(MergeSort.mergeSort(s.listaDeUsuarios).get(0).getNome());
		
	//	Usuario usuario2 = s.buscaUsuario("mark");
	//	s.abrirSessao("mark", "m@rk");
		
	//	Usuario usuario = s.buscaUsuario("bill");
	//	s.abrirSessao("bill", "severino");
	//	s.cadastrarCarona(usuario.getId(), "campina", "caruaru", "21/08/2012", "14:00", "1");
		
		
		/*System.out.println(s.passouNumeroDeHoras("24/06/2012", "00:41", 48));
		
		s.criarUsuario("Hud", "123", "sfs", "ed", "a@g.com");
		s.criarUsuario("Hud2", "123", "sfs", "ed", "ab@g.com");
		String id = s.abrirSessao("Hud", "123");
		String id2 = s.abrirSessao("Hud2", "123");
		
		String idCarona = s.cadastrarCaronaRelampago(id, "s", "d", "24/06/2012", "00:40","3","2");
		
		String idSolicitacao = s.solicitarVaga(id2, idCarona);
		s.aceitarSolicitacao(id, idSolicitacao);
		
		System.out.println(s.listaDeCaronas.size());
		
		s.atualizaStatusCaronaRelampago((CaronaRelampago) s.buscaCaronaID(idCarona));
		
		System.out.println(s.listaDeCaronas.size());*/
		
		
		
		

	
			
		
	}


}
