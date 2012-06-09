package sistemadecaronas.projSi1.auxiliar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrataEmail {
	
	public static boolean emailValido(String email){
		boolean formatoValido = false;
		Pattern pt = Pattern.compile("[^\\W[.][-]]*[@][^\\d\\W]*[.][^\\d\\W[.]\\p{Punct}[.]]*[^\\d\\W\\p{Punct}]");
		Matcher mt = pt.matcher(email);
		if (mt.matches()) {
			formatoValido = true;
		}
		return formatoValido;

	}


}

