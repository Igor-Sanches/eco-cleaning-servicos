package app.birdsoft.ecocleaningservicos.tools;
 
import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTime {
    @SuppressLint("ConstantLocale")
    private static final Locale locale = Locale.getDefault();
    
    /**
     *
     * Retorna uma String com a data e hora atual do aparelho
     * @return
     */
    public static String toDateString(){
       SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
       return date.format(new Date());
    }

    /**
     *
     * @param format = String onde voc� coloca o formato da data ex: dd/MM/yyyy
     * @return
     */
    public static String toDateString(String format){
       SimpleDateFormat date = new SimpleDateFormat(format, locale);
       return date.format(new Date());
    }

    /**
     *
     * @param format = String onde voc� coloca o formato da data ex: dd/MM/yyyy
    * @param time = Adicione o time que deseja para retorna a String com o formado desejado
     * @return
     */
    public static String toDateString(String format, long time){
       SimpleDateFormat date = new SimpleDateFormat(format, locale);
       return date.format(time);
    }

    /**
     *
     * Pegue o time da data e hora atual no formato dd/MM/yyyy HH:mm
     * @return
     */
    public static long getTime(){
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(date.format(new Date())).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static Date getDate(long date){
        return new Date(date);
    }

    public static boolean isAtrasado(String data) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(data).before(date2.parse(toDateString("dd/MM/yyyy HH:mm")));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValido(long dataValidade) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parse = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date horaPedidoChegaR = parse.parse(toDateString("dd/MM/yyyy", dataValidade));
            Date horaAtual = parse.parse(parse.format(new Date()));
            return horaPedidoChegaR.before(horaAtual);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long getTime(String format) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(format).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static String toStringFromDateSemanaSelected(String data) {
        return toDateStringParse(data);
    }

    private static String toDateStringParse(String data) {
        SimpleDateFormat date = new SimpleDateFormat("EEE", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy", locale);
        try {
             return date.format(Objects.requireNonNull(date2.parse(data)));
        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean isAbertoServico(String horaFormatada, String horarioAberto, String dataFormatada) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(dataFormatada + " " + horaFormatada).after(date2.parse(dataFormatada + " " + horarioAberto));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFechadoServico(String horaFormatada, String horarioAberto, String dataFormatada) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(dataFormatada + " " + horaFormatada).before(date2.parse(dataFormatada + " " + horarioAberto));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String toStringFromDateSemanaNoSigleSelected(String data) {
        SimpleDateFormat date = new SimpleDateFormat("EEEE", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy", locale);
        try {
            return date.format(Objects.requireNonNull(date2.parse(data)));
        } catch (ParseException ex) {
            Logger.getLogger(DateTime.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean isMaiorServicoHorario(String horarioAgendamento, String horarioAgendamento1, String dataAgendamento) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        SimpleDateFormat date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
        try {
            return date.parse(dataAgendamento + " " + horarioAgendamento).after(date2.parse(dataAgendamento + " " + horarioAgendamento1));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String add(String horario) {
        String hr = horario.split(":")[0];
        String min = horario.split(":")[1];
        int hora_ = Integer.parseInt(hr);
        int minuto_ = Integer.parseInt(min);
        int hora = hora_;
        int minuto = minuto_;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.add(Calendar.MINUTE, minuto + 30);
        int _hora = calendar.get(Calendar.HOUR_OF_DAY);
        int _minuto = calendar.get(Calendar.MINUTE);
        return String.format("%s:%s", String.valueOf(_hora).length() == 1 ? "0" + _hora : _hora, String.valueOf(_minuto).length() == 1 ? "0" + _minuto : _minuto);
    }
}
