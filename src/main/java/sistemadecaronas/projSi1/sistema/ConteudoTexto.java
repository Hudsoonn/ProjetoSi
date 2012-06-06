package sistemadecaronas.projSi1.sistema;


public class ConteudoTexto implements Conteudo{
	
	private String conteudoTexto;
	
	public ConteudoTexto(String conteudoTexto){		
		this.conteudoTexto = conteudoTexto;
	}

    
    public String getConteudo(){

    	return conteudoTexto;
    }
    
    
    public void setConteudo(String conteudoTexto){
    	this.conteudoTexto = conteudoTexto;
    }
    

}
