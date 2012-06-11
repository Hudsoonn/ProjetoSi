package sistemadecaronas.projSi1.sistema;

public class Review {
	private Usuario usuario;
	private final String TIPO_REVIEW;
	
	public Review(Usuario usuario,String TIPO_REVIEW){
		this.usuario = usuario;
		this.TIPO_REVIEW = TIPO_REVIEW;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getTIPO_REVIEW() {
		return TIPO_REVIEW;
	}
	
	

}
