package sistemadecaronas.projSi1.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sun.mail.imap.protocol.UID;
import com.sun.mail.imap.protocol.UIDSet;


public class CaronaRelampago implements Carona{
	
	private String origem, destino, data, hora, idCarona;
	private int nMinimoCaroneiros;
	private final String TIPO_CARONA = "Relampago";	
	private Usuario donoDaCarona;
	private List<Solicitacao> listaDeSolicitacaoAceitas = new ArrayList<Solicitacao>();
	private List<Sugestao> listaDeSugestoes = new ArrayList<Sugestao>();
	private List<Solicitacao> listaDeSolicitacao = new ArrayList<Solicitacao>();
	private List<Usuario> listaDeParticipantes = new ArrayList<Usuario>();
	private boolean caronaPreferencial = false;
	private List<PontoDeEncontro> listaDePontoDeEncontro = new ArrayList<PontoDeEncontro>();
	private List<Review> listaDeReview = new ArrayList<Review>();
	private List<Review> listaDeReviewVagaCarona = new ArrayList<Review>();
	private boolean expired = false;
	
	
	
	
	public CaronaRelampago(String origem, String destino, String data, String hora, int nMinimoCaroneiros){
		this.origem = origem;
		this.destino = destino;
		this.data = data;
		this.hora = hora;
		this.nMinimoCaroneiros = nMinimoCaroneiros;
		idCarona = UUID.randomUUID().toString();
	
	
	}

	public String tipoDeCarona() {

		return TIPO_CARONA;
	}

	public List<Solicitacao> getListaDeSolicitacao() {
		return listaDeSolicitacao;
	}

	public List<PontoDeEncontro> getPontoDeEncontro() {
		
		return listaDePontoDeEncontro;
	}

	public List<Solicitacao> getListaDeSolicitacaoAceitas() {
		
		return listaDeSolicitacaoAceitas;
	}

	public void setPontoDeEncontro(List<PontoDeEncontro> listaDePontoDeEncontro) {		
		this.listaDePontoDeEncontro = listaDePontoDeEncontro;		
	}

	public String getIdDaCarona() {
		
		return idCarona;
	}

	public List<Sugestao> getSugestoes() {

		return listaDeSugestoes;
	}

	public void addSugestao(Sugestao sugestao) {
		listaDeSugestoes.add(sugestao);
		
	}

	public void setIdDaCarona(String idCarona) {
		
		this.idCarona = idCarona;
		
	}

	public Usuario getDonoDaCarona() {
		
		return donoDaCarona;
	}

	public void setDonoDaCarona(Usuario donoDaCarona) {
		
		this.donoDaCarona = donoDaCarona;
		
	}

	public String getOrigem() {
		
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {

		return destino;
	}

	public void setDestino(String destino) {

          this.destino = destino;
		
	}

	public String getData() {

		return data;
	}

	public void setData(String data) {
		
		this.data = data;
		
	}

	public String getHora() {
	
		return hora;
	}

	public void setHora(String hora) {
		
		this.hora = hora;
		
	}

	public int getVagas() {

		return nMinimoCaroneiros;
	}

	public void setVagas(int vagas) {
		
		this.nMinimoCaroneiros = vagas;
		
	}

	public void addSolicitacao(Solicitacao solicitacao) {
		
		listaDeSolicitacao.add(solicitacao);
		
	}

	public void addSolicitacaoAceita(Solicitacao solicitacao) {
		
		listaDeSolicitacaoAceitas.add(solicitacao);
		
	}

	public void addPontoDeEncontro(PontoDeEncontro ponto) {
		
		listaDePontoDeEncontro.add(ponto);
		
	}

	public void removeSugestao(Sugestao sugestao) {
		
		listaDeSugestoes.remove(sugestao);
		
	}

	public void removeSolicitacao(Solicitacao solicitacao) {
		
		listaDeSolicitacao.remove(solicitacao);
		
	}

	public void removeSolicitacaoAceita(Solicitacao solicitacao) {
		
		listaDeSolicitacaoAceitas.remove(solicitacao);
		
	}

	public void removePontoDeEncontro(PontoDeEncontro ponto) {
		
		listaDePontoDeEncontro.remove(ponto);
		
	}

	public void addParticipante(Usuario usuario) {
		
		listaDeParticipantes.add(usuario);
		
	}

	public void removeParticipante(Usuario usuario) {
		
		listaDeParticipantes.remove(usuario);
		
	}

	public List<Usuario> getListaDeParticipantes() {
	
		return listaDeParticipantes;
	}

	public List<Review> getListaDeReview() {
	
		return listaDeReview;
	}

	public void addReview(Review review) {
	 
		listaDeReview.add(review);
		
	}

	public List<Review> getListaDeReviewVagaCarona() {
		
		return listaDeReviewVagaCarona;
	}

	public void addReviewVagaCarona(Review review) {
		
		listaDeReviewVagaCarona.add(review);
		
	}
	
	public void setCaronaExpired(){
		
		expired = true;
	}
	
	public boolean isExpired(){
		
		return expired;
	}

	public boolean ehPreferencial() {

		return caronaPreferencial;
	}

	public void setCaronaPreferencial(boolean caronaPreferencial) {
		
		this.caronaPreferencial = caronaPreferencial;
		
	}
	


}
