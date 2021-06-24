package app.birdsoft.ecocleaningservicos.manager;

import java.util.regex.Pattern;

public class Email {
    public static boolean validar(String email) {
        String codigo = email;
        if (codigo.startsWith("mailto:") || codigo.startsWith("MAILTO:") && isEmailValido(codigo)) {
            if (codigo.contains("@") && !codigo.contains("/") || codigo.contains("@") && !codigo.contains(" ")) {
                String[] aar = codigo.split("@");
                String number = aar[0];
                if (codigo.substring(number.length() + 1).contains("@") || codigo.substring(number.length() + 1).contains(",")) {
                    return false;
                }

                if (aar[1].contains(".")) {
                    if (aar[1].endsWith(".") || aar[1].startsWith(".") || aar[1].contains(","))
                        return false;
                    else return true;
                } else return false;
            }
            return false;
        } else if (codigo.contains("@") && !codigo.contains(":") && isEmailValido(codigo)) {
            String[] aar = codigo.split("@");
            String number = aar[0];
            if (codigo.substring(number.length() + 1).contains("@") || codigo.substring(number.length() + 1).contains(",")) {
                return false;
            }

            if (aar[1].contains(".")) {
                if (aar[1].endsWith(".") || aar[1].startsWith(".") || aar[1].contains(","))
                    return false;
                else return true;
            } else return false;
        } else return false;
    }


    private static Pattern EMAIL_VALIDO = Pattern.compile("[a-zA-Z0-9@.!#$&%'*+\\-/?^_`{|}~]+");
    private static boolean isEmailValido(String codigo) {
        return EMAIL_VALIDO.matcher(codigo).matches();
    }
}