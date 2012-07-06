package sistemadecaronas.projSi1.sistema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.print.attribute.HashAttributeSet;

public class CaronaMunicipal implements Carona{

	private final String TIPO_CARONA = "Municipal";
	private Usuario donoDaCarona;
	private  List<Solicitacao> listaDeSolicitacaoAceitas = new ArrayList<Solicitacao>();
	private List<Sugestao> listaDeSugestoes = new ArrayList<Sugestao>();
	private List<Usuario> listaDeParticipantes = new ArrayList<Usuario>();
	private  List<Solicitacao> listaDeSolicitacao = new ArrayList<Solicitacao>();
	private String origem, destino, data, hora, idCarona, cidade;
	private int vagas;
	private List<PontoDeEncontro> listaDePontoDeEncontro = new ArrayList<PontoDeEncontro>();
	private boolean caronaPreferencial = false;
	private List<Review>  listaDeReview = new ArrayList<Review>();
	private List<Review>  listaDeReviewVagaCarona = new ArrayList<Review>();
	
	
	public CaronaMunicipal(String origem, String destino, String cidade, String data, String hora, int vagas){
		   this.origem = origem;
		   this.destino = destino;
		   this.data = data;
		   this.cidade = cidade;
		   this.hora = hora;
		   this.vagas = vagas;
		   idCarona = UUID.randomUUID().toString();	
	}
	
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
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
		return vagas;
	}
	public void setVagas(int vagas) {
		this.vagas = vagas;
	}
	

	public void addSolicitacao(Solicitacao solicitacao) {
		this.listaDeSolicitacao.add(solicitacao);
		
	}

	public void addSolicitacaoAceita(Solicitacao solicitacao) {
		this.listaDeSolicitacaoAceitas.add(solicitacao);
		
	}

	public void addPontoDeEncontro(PontoDeEncontro pontos) {
		this.listaDePontoDeEncontro.add(pontos);
		
	}


	public void removeSugestao(Sugestao sugestao) {
		  listaDeSugestoes.remove(sugestao);
		
		
	}
	
	public void removeSolicitacao(Solicitacao solicitacao)
	{
		listaDeSolicitacao.remove(solicitacao);
	}
	
	public void removeSolicitacaoAceita(Solicitacao solicitacao)
	{
		listaDeSolicitacaoAceitas.remove(solicitacao);
	}
	
	public void removePontoDeEncontro(PontoDeEncontro ponto)
	{
		for (PontoDeEncontro pontoEncontro : listaDePontoDeEncontro) {
			if (pontoEncontro.getUsuario().getLogin().equals(ponto.getUsuario().getLogin() ) && pontoEncontro.getPonto().equals(ponto.getPonto())) {
				listaDePontoDeEncontro.remove(ponto);	
				break;
			}
		}

	}

	public String tipoDeCarona() {
		return TIPO_CARONA;
	}
	
   public void addParticipante(Usuario usuario){
	    	listaDeParticipantes.add(usuario);
	   }
	    
   public void removeParticipante(Usuario usuario){
	    	
	    listaDeParticipantes.remove(usuario);
	  }

	public List<Usuario> getListaDeParticipantes() {
		
		return listaDeParticipantes;
	}
	
	public void addReview(Review review){
		
		listaDeReview.add(review);
		
	}

	public List<Review> getListaDeReview() {

		return listaDeReview;
	}
	
public void addReviewVagaCarona(Review review){
		
		listaDeReviewVagaCarona.add(review);
		
	}

	public List<Review> getListaDeReviewVagaCarona() {

		return listaDeReviewVagaCarona;
	}

	public boolean ehPreferencial() {

		return caronaPreferencial;
	}

	public void setCaronaPreferencial(boolean caronaPreferencial) {
		
		this.caronaPreferencial = caronaPreferencial;
		
	}
	


	
	

}
