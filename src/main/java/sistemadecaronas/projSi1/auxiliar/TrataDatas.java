package sistemadecaronas.projSi1.auxiliar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrataDatas {
	
public static boolean isDataValida(String dataRecebida,String hora) {
		
		boolean dataBoolean = true;
		Calendar calendar = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		
	   // calendar2.set(calendar2.YEAR, calendar2.MONTH, calendar2.DATE);
		String[] arrayDataRecebida = dataRecebida.split("/");
		
		Pattern pt = Pattern.compile("[0-9]{2}[/][0-9]{2}[/][0-9]{4}");
		Matcher mt = pt.matcher(dataRecebida);
		dataBoolean = mt.matches();

		
		if (dataBoolean) {
			
			int ano = Integer.parseInt(arrayDataRecebida[2]);
			int mes = Integer.parseInt(arrayDataRecebida[1])-1;
			int dia = Integer.parseInt(arrayDataRecebida[0]);
			
			int h = Integer.parseInt(hora.split(":")[0]);
			int m = Integer.parseInt(hora.split(":")[1]);
			
			calendar.set(ano,mes,dia);
			calendar.set(calendar.HOUR_OF_DAY, h);
			calendar.set(calendar.MINUTE, m);
	
           if (calendar.before(calendar2)) {
				dataBoolean = false;
			}
				
			if (calendar.get(calendar.DATE) != dia || calendar.get(calendar.MONTH) != mes || calendar.get(calendar.YEAR) != ano) {
					
				dataBoolean = false;
			}
		}	
		
		return dataBoolean;
	}


		public static boolean horaValida(String hora){
			
			String[] arrayHoraRecebida = hora.split(":");
			Pattern pt = Pattern.compile("[\\d]+[:][0-9]{2}");
		    Matcher m = pt.matcher(hora);
			boolean formatoCorreto = m.matches();
			boolean horaValida = false;
			
			if (formatoCorreto) {
				int campoHora = Integer.parseInt(arrayHoraRecebida[0]);
				int campoMinutos = Integer.parseInt(arrayHoraRecebida[1]);
				
				if (campoHora >=0 && campoHora <= 23 && campoMinutos >= 0 && campoMinutos <= 59) {
					
					horaValida = true;
								
				}
			}
				
		
		
		return horaValida;
		} 
		
		
		public static boolean ehBissexto(int ano) {
			Calendar calendarC = new GregorianCalendar();

			if (((GregorianCalendar) calendarC).isLeapYear(ano)) {

				return true;

			}
			return false;
		}


 public static void main(String[] args) {
}

}
