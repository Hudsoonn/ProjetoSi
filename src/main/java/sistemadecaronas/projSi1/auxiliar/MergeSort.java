package sistemadecaronas.projSi1.auxiliar;

import java.util.LinkedList;
import java.util.List;

import sistemadecaronas.projSi1.sistema.Usuario;


	public class MergeSort{
	//MERGESORT
	
	public static List<Usuario> divide(List<Usuario> lista, int inicio, int fim){
		List<Usuario> retorno = new LinkedList<Usuario>();

        for (int i = inicio ; i <= fim; i++){
            retorno.add(lista.get(i));
          }
        return retorno;

	}
	
	public static List<Usuario> mergeSort(List<Usuario> lista){
		int n = lista.size();
		if (n == 1){
			return lista;
		}
		
		List<Usuario> esquerda = mergeSort(divide(lista, 0,(lista.size()-1)/2));
		List<Usuario> direita = mergeSort(divide(lista, (lista.size()-1)/2 + 1, lista.size()-1));
		int indiceEsq = 0;
		int indiceDir = 0;
		List<Usuario> listaRetorno = new LinkedList<Usuario>();

		while(indiceEsq < esquerda.size() && indiceDir < direita.size()){
			if(esquerda.get(indiceEsq).getCaronasSeguras() < direita.get(indiceDir).getCaronasSeguras()){
				listaRetorno.add(indiceEsq + indiceDir, esquerda.get(indiceEsq));
				indiceEsq ++;
				continue;
			}
			listaRetorno.add(indiceEsq + indiceDir,direita.get(indiceDir));
            indiceDir ++;
		}
		if (indiceEsq < esquerda.size())
            for (int i = indiceEsq; i < esquerda.size(); i++) {
                listaRetorno.add(direita.size() + indiceEsq, esquerda.get(i));
                indiceEsq ++;
            }
        else
            for (int i = indiceDir; i < direita.size(); i++) {
                listaRetorno.add(esquerda.size() + indiceDir,direita.get(i));
                indiceDir ++;
            }
       
		return listaRetorno;
    
	}
	
	
}
	
