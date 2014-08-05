 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelo.PerioPM;

public class DataUtil {
	
	private static SimpleDateFormat sdf_data_simples = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdf_hora_simples = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat sdf_hora_minuto_simples = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdf_data_hora = new SimpleDateFormat("HH");
    private static SimpleDateFormat sdf_data_minuto = new SimpleDateFormat("mm");
    private static SimpleDateFormat sdf_data_segundo = new SimpleDateFormat("ss");
    private static SimpleDateFormat sdf_data_dia = new SimpleDateFormat("dd");
    private static SimpleDateFormat sdf_data_mes_extenso = new SimpleDateFormat("MMMMM", new Locale("pt", "BR"));
    private static SimpleDateFormat sdf_data_mes = new SimpleDateFormat("MM");
    private static SimpleDateFormat sdf_data_ano = new SimpleDateFormat("yyyy");

    public DataUtil() {
    }

    public static String formatarDataCompleta(Calendar data) {
        Date dataDate = data.getTime();
        return sdf_data_dia.format(dataDate) + " de " + sdf_data_mes_extenso.format(dataDate) + " de " + sdf_data_ano.format(dataDate) + " às " + sdf_hora_simples.format(dataDate);
    }
    
    public static String formatarDataParaPadraoPerioPM(PerioPM perioPM){
    	
    	Date dateInicial = perioPM.getDataInicial().getTime();
    	Date dateFinal = perioPM.getDataFinal().getTime();
    	
    	return sdf_data_simples.format(dateInicial) + " a " + sdf_data_simples.format(dateFinal);
    }

    public static String formatarHorario(Date data) {
        return sdf_hora_minuto_simples.format(data);
    }

    public static Date formatarParaDate(String dataStr, String formato) throws java.text.ParseException {
        Pattern pattern = Pattern.compile(formato);
        Matcher matcher = pattern.matcher(dataStr);
        if (matcher.matches()) {
            return new SimpleDateFormat(formato).parse(dataStr);
        }
        return null;
    }

    /**
     * <b>Autor:</b> Walanem
     * <br/>
     * Retorna o número de semanas entre uma data e outra
     *
     * @param Calendar data1
     * @param Calendar data2
     * @return boolean
     */
    public static int numeroSemanas(Calendar data1, Calendar data2) {

        data1 = new GregorianCalendar(data1.get(Calendar.YEAR), data1.get(Calendar.MONTH), data1.get(Calendar.DAY_OF_MONTH));
        data2 = new GregorianCalendar(data2.get(Calendar.YEAR), data2.get(Calendar.MONTH), data2.get(Calendar.DAY_OF_MONTH));

        long ms;

        //subtrai a menor data da maior
        if (data1.after(data2)) {
            ms = data1.getTimeInMillis() - data2.getTimeInMillis();

        } else {
            ms = data2.getTimeInMillis() - data1.getTimeInMillis();

        }

        //calcula o numero de semanas
        return (int) (ms / (1000 * 3600 * 24 * 7));
    }

    public static int numeroSemanas(Date data1, Date data2) {
        Calendar c1 = new GregorianCalendar();
        c1.setTime(data1);

        Calendar c2 = new GregorianCalendar();
        c2.setTime(data2);

        return numeroSemanas(c1, c2);
    }

    /**
     * <b>Autor:</b> Walanem
     * <br/>
     * Retorna true caso as datas tenham o mesmo dia, mes e ano
     *
     * @param Calendar data1
     * @param Calendar data2
     * @return boolean
     */
    public static boolean datasIguais(Calendar data1, Calendar data2) {
        if (data1.get(Calendar.DAY_OF_MONTH) != data2.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        if (data1.get(Calendar.MONTH) != data2.get(Calendar.MONTH)) {
            return false;
        }
        if (data1.get(Calendar.YEAR) != data2.get(Calendar.YEAR)) {
            return false;
        }
        return true;
    }
    
    /**
     * <b>Autor:</b> Walanem
     * <br/>
     * Método que valida se uma dada Data final eh posterior a uma data Inicial fornecida.
     * Em caso de sucesso, o metodo retorna 'true'. Caso contrario, retorna 'false'.
     *
     * @param Date dataInicial
     * @param Date dataFinal
     * @return boolean
     */
    public static boolean validacaoDatasInicialEFinal(Date dataInicial, Date dataFinal) {
    	return dataFinal.after(dataInicial);
    }

    public static boolean datasIguais(Date data1, Date data2) {
        Calendar calendar1 = dateToCalendar(data1);
        Calendar calendar2 = dateToCalendar(data2);
        return datasIguais(calendar1, calendar2);
    }


    public static Calendar dateToCalendar(Date dt) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);
        return gc;
    }
}
