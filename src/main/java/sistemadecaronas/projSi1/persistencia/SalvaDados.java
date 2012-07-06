package sistemadecaronas.projSi1.persistencia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sistemadecaronas.projSi1.sistema.Usuario;

public class SalvaDados<T> {

	public void salvaUsuarios(String nomeArquivo,List<Usuario> listaDeUsuarios){
		
		Element usuarios = new Element("Usuários");
		Element usuario = new Element("Usuário");
		Element nome = new Element("Nome");
		Element login = new Element("Login");
		Element senha = new Element("Senha");
		Element endereco = new Element("Endereço");
		Element email = new Element("Email");
		Element id = new Element("ID");
		Element listaDeCaronaQueParticipa = new Element("ListaDeCaronasQuePaticipa");
		Element listaDeCaronasDoUsuario = new Element("ListaDeCaronasDoUsuario");
		
		Element listaDeMensagens = new Element("ListaDeMensagens");
		Element presencaEmCarona = new Element("PresencaEmCaronas");
		Element faltasEmcaronas = new Element("FaltasEmCaronas");
		Element caronaNaoFuncionaram = new Element("CaronasQueNaoFuncionaram");
		Element caronasSeguras = new Element("CaronasSeguras");
		Element historicoVagasEmCaronas = new Element("HistoricoDeVagasEmCaronas");
		Element historicoCaronas = new Element("HistoricoCaronas");
		
		for (Usuario user : listaDeUsuarios) {
			
			nome.setText(user.getNome());
			login.setText(user.getLogin());
			senha.setText(user.getSenha());
			endereco.setText(user.getEndereco());
			email.setText(user.getEmail());
			id.setText(user.getId());
			listaDeCaronaQueParticipa.setContent(user.getListaDeCaronasQueParticipa());
			listaDeCaronasDoUsuario.setContent(user.getListaDeCaronasDoUsuario());
			listaDeMensagens.setContent(user.getListaDeMensagens());
			presencaEmCarona.setText(String.valueOf(user.getPresencaEmCaronas()));
			faltasEmcaronas.setText(String.valueOf(user.getFaltasEmCaronas()));	
			caronaNaoFuncionaram.setText(String.valueOf(user.getCaronasNaoFuncionaram()));
			caronasSeguras.setText(String.valueOf(user.getCaronasSeguras()));
			historicoVagasEmCaronas.setContent(user.getHistoricoVagasEmCaronas());
			historicoCaronas.setContent(user.getHistoricoCaronas());
			
			
		}
		
		usuario.addContent(login);
		usuario.addContent(senha);
		usuario.addContent(nome);
		usuario.addContent(endereco);
		usuario.addContent(email);
		usuario.addContent(id);
		usuario.addContent(listaDeCaronaQueParticipa);
		usuario.addContent(listaDeCaronasDoUsuario);
		usuario.addContent(listaDeMensagens);
		usuario.addContent(presencaEmCarona);
		usuario.addContent(faltasEmcaronas);
		usuario.addContent(caronaNaoFuncionaram);
		usuario.addContent(caronasSeguras);
		usuario.addContent(historicoVagasEmCaronas);
		usuario.addContent(historicoCaronas);
		
		usuarios.addContent(usuario);
		
		Document users = new Document(usuarios);
		
		XMLOutputter xout = new XMLOutputter();
		
		  try {
		        FileWriter arquivo = new FileWriter(new File(nomeArquivo+".xml"));
		        Format formatXML = Format.getPrettyFormat();  
		        formatXML.setEncoding("ISO-8859-1");  
		        xout.setFormat(formatXML);
		        xout.output(users,arquivo);

		        } catch (IOException e) {
		         e.printStackTrace();
		        }
		
		
		
		
		
		
		
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
