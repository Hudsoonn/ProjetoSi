package sistemadecaronas.projSi1.sistema;

import java.util.LinkedList;
import java.util.List;

public class Mensagem {
	
	private String remetente, destinatario;
	private Conteudo conteudo;
    private List<Conteudo> ConteudoDaMensagem = new LinkedList<Conteudo>();
   
	public Mensagem(String remetente, String destinatario,Conteudo conteudo){
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.conteudo = conteudo;
		
	}
	
	
	public String getRemetente() {
		return remetente;
	}


	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}


	public String getDestinatario() {
		return destinatario;
	}


	public void setDestinatatio(String destinatario) {
		this.destinatario = destinatario;
	}


	public Conteudo getConteudo() {
		return conteudo;
	}


	public void setConteudo(Conteudo conteudo) {
		this.conteudo = conteudo;
	}
	
	public void addConteudo(Conteudo conteudo){
		ConteudoDaMensagem.add(conteudo);
	}
	
	public List<Conteudo> getConteudosDaMensagem(){
		
		return ConteudoDaMensagem;
	}



}
