package sistemadecaronas.projSi1.sistema;

import java.util.ArrayList;
import java.util.List;


//throw new Exception("");

public class Usuario {

	private String login;
	private String senha;
	private String nome;
	private String endereco;
	private String email;
	private String id = null;
	private List<Carona> listaDeCaronasQueParticipa = null;
	private List<Carona> listaDeCaronasDoUsuario = null;
	private List<Mensagem> listaDeMensagens;
	private int presencaEmCaronas;
	private int faltasEmCaronas;
	private int caronasNaoFuncionaram;
	private int caronasSeguras;
	private ArrayList<Carona> historicoVagasEmCaronas;
	private ArrayList<Carona> historicoCaronas;
	private ArrayList<Usuario> listaReviewPositivos;

	public Usuario(String login, String senha, String nome, String endereco,
			String email) throws Exception {
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.endereco = endereco;
		this.email = email;
		listaDeCaronasQueParticipa = new ArrayList<Carona>();
		listaDeCaronasDoUsuario = new ArrayList<Carona>();
		listaDeMensagens = new ArrayList<Mensagem>();
		listaReviewPositivos = new ArrayList<Usuario>();
		
		this.historicoCaronas = new ArrayList<Carona>();
		this.historicoVagasEmCaronas = new ArrayList<Carona>();
		this.caronasSeguras = 0;
		this.caronasNaoFuncionaram = 0;
		this.faltasEmCaronas = 0;
		this.presencaEmCaronas = 0;
		

	}
	
/*	public String getMensagens()
	{
		String saida = "";
		for(String s : listaDeMensagens)
		{
			saida = saida + "\n" + s;
		}
		return saida;
	}*/

	public List<Mensagem> getListaDeMensagens() {
		return listaDeMensagens;
	}

	
	public void addPresencaEmCaronas() {
		this.presencaEmCaronas ++;
	}

	public void addFaltasEmCaronas() {
		this.faltasEmCaronas++;
	}

	public void addCaronasNaoFuncionaram() {
		this.caronasNaoFuncionaram++;
	}

	public void addCaronasSeguras() {
		this.caronasSeguras++;
	}

	public void addHistoricoVagasEmCaronas(Carona historicoVagasEmCaronas) {
		this.historicoVagasEmCaronas.add(historicoVagasEmCaronas);
	}

	public void addHistoricoCaronas(Carona carona) {
		this.historicoCaronas.add(carona);
	}

	public int getPresencaEmCaronas() {
		return presencaEmCaronas;
	}

	public int getFaltasEmCaronas() {
		return faltasEmCaronas;
	}

	public int getCaronasNaoFuncionaram() {
		return caronasNaoFuncionaram;
	}

	public int getCaronasSeguras() {
		return caronasSeguras;
	}

	public ArrayList<Carona> getHistoricoVagasEmCaronas() {
		return historicoVagasEmCaronas;
	}

	public ArrayList<Carona> getHistoricoCaronas() {
		return historicoCaronas;
	}

	public List<Carona> getListaDeCaronasDoUsuario() {
		return listaDeCaronasDoUsuario;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) throws Exception {
		if ((login == null) || (login.equals(""))) {
			throw new Exception("Login inválido");
		} else {
			this.login = login;
		}
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
	public List<Carona> getListaDeCaronasQueParticipa(){
		return listaDeCaronasQueParticipa;
	}
	
    public void addCaronaQueParticipa(Carona carona){
    	listaDeCaronasQueParticipa.add(carona);
    }
	public void addCarona(Carona carona) {
		listaDeCaronasDoUsuario.add(carona);	
	}

	public void removeCaronaQueParticipa(Carona carona){
		listaDeCaronasQueParticipa.remove(carona);
	}
	
	public void removeCarona(Carona carona){
		listaDeCaronasDoUsuario.remove(carona);
	}
	public void addMensagem(Mensagem mensagem) {
		listaDeMensagens.add(mensagem);
		
	}
	
	public void removeMensagem(Mensagem mensagem){
		listaDeMensagens.remove(mensagem);
	}
    
	public void addQuemDeuReviewPositivo(Usuario usuario){
		
		listaReviewPositivos.add(usuario);
	}
	public ArrayList<Usuario> getListaReviewPositivos() {
		return listaReviewPositivos;
	}

	
	

	

}