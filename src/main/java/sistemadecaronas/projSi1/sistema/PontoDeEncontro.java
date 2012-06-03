package sistemadecaronas.projSi1.sistema;

public class PontoDeEncontro {
	
	private Usuario usuario;
	private String ponto;
	
	public PontoDeEncontro(Usuario usuario, String ponto){
		this.usuario = usuario;
		this.ponto = ponto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getPonto() {
		return ponto;
	}

	public void setPonto(String ponto) {
		this.ponto = ponto;
	}
	
	

}
