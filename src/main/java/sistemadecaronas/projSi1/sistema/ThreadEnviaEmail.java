package sistemadecaronas.projSi1.sistema;

public class ThreadEnviaEmail extends Thread{
	
	private String idSessao, emailDestinatario, mensagem;
	
	public ThreadEnviaEmail(String idSessao, String emailDestinatario, String mensagem){
		this.idSessao = idSessao;
		this.emailDestinatario = emailDestinatario;
		this.mensagem = mensagem;
		
		
	}
	
	
	public String getIdSessao() {
		return idSessao;
	}


	public String getEmailDestinatario() {
		return emailDestinatario;
	}


	public String getMensagem() {
		return mensagem;
	}


	public void run(){
		SistemaDeCarona s = SistemaDeCarona.getInstanceOf();
		s.enviaEmail(this.getIdSessao(), this.getEmailDestinatario(), this.getMensagem());
	}
	
	

}
