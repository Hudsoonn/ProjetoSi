package sistemadecaronas.projSi1.sistema;

import java.util.Collection;

import sistemadecaronas.projSi1.persistencia.Serializador;

public class ThreadForSave<T> extends Thread{
	
	private Collection<T> colecao;
	private String nomeArquivo;
	
	public ThreadForSave(String nomeArquivo, Collection colecao){
		this.colecao = colecao;
		this.nomeArquivo = nomeArquivo;
	}
	
	
	
	public Collection<T> getColecao() {
		return colecao;
	}



	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public synchronized void salva(String nomeArquivo, Collection colecao){
		Serializador.getInstanceOf().salvar(nomeArquivo, (Collection) colecao);
	}



    
	public synchronized void run(){
		
		salva(this.getNomeArquivo(), this.getColecao());
		
		System.out.println("informacoes salvas em: "+getNomeArquivo());

			
	}

}
