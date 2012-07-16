package sistemadecaronas.projSi1.sistema;

import java.util.List;
import java.util.Map;

public interface Carona {
	
	public List<Usuario> getListaDeUsuariosPreferencias();
	
	public void addUsuarioPreferencial(Usuario usuario);
	
	public void removeUsuarioPreferencial(Usuario usuario);
	
	public boolean ehPreferencial();
	
	public void setCaronaPreferencial(boolean caronaPreferencial);
	
	public String tipoDeCarona();
	
	public List<Solicitacao> getListaDeSolicitacao();


	public List<PontoDeEncontro> getPontoDeEncontro() ;


	public List<Solicitacao> getListaDeSolicitacaoAceitas();


	public void setPontoDeEncontro(List<PontoDeEncontro> listaDePontoDeEncontro) ;

	public String getIdDaCarona() ;


	public List<Sugestao> getSugestoes() ;


	public void addSugestao(Sugestao sugestao) ;


	public void setIdDaCarona(String idDaCarona);

	
	public Usuario getDonoDaCarona() ;


	public void setDonoDaCarona(Usuario donoDaCarona) ;

	
	public String getOrigem() ;
	
	public void setOrigem(String origem) ;
	
	public String getDestino();
	
	
	public void setDestino(String destino) ;
	
	public String getData() ;
	
	public void setData(String data) ;
	
	public String getHora() ;
	
	public void setHora(String hora) ;
	
	public int getVagas() ;
	
	public void setVagas(int vagas);
	

	public void addSolicitacao(Solicitacao solicitacao) ;

	public void addSolicitacaoAceita(Solicitacao solicitacao);
	
	public void addPontoDeEncontro(PontoDeEncontro ponto) ;


	public void removeSugestao(Sugestao sugestao);
	
	public void removeSolicitacao(Solicitacao solicitacao);
	
	public void removeSolicitacaoAceita(Solicitacao solicitacao);
	
	public void removePontoDeEncontro(PontoDeEncontro ponto);
    
    public void addParticipante(Usuario usuario);  
	
    public void removeParticipante(Usuario usuario);
	    		
	public List<Usuario> getListaDeParticipantes();
	
	public List<Review> getListaDeReview();

	public void addReview(Review review);
	
	public List<Review> getListaDeReviewVagaCarona();

	public void addReviewVagaCarona(Review review);
	

	
	
}
