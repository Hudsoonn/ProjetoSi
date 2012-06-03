package sistemadecaronas.projSi1.sistema;

import java.util.UUID;


public class Interesse {
	private String idSessao;
	private String origem;
	private String destino;
	private String data;
	private String horaInicial;
	private String horaFim;
	private String idInteresse;

	public Interesse(String idSessao, String origem, String destino, String data, String horaInicial, String horaFim) {
		this.idSessao = idSessao;
		this.origem = origem;
		this.destino = destino;
		this.data = data;
		this.horaInicial = horaInicial;
		this.horaFim = horaFim;
		idInteresse = UUID.randomUUID().toString();
		
	}

	public String getIdInteresse() {
		return idInteresse;
	}

	public void setIdInteresse(String idInteresse) {
		this.idInteresse = idInteresse;
	}

	public String getIdSessao() {
		return idSessao;
	}

	public void setIdSessao(String idSessao) {
		this.idSessao = idSessao;
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

	public String getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}
}
