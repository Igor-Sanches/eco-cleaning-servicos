package app.birdsoft.ecocleaningservicos.model;

import java.io.Serializable;

public class HorariosFuncionamento implements Serializable {
    private String semane, sigla, horarioAberto, horarioFechado;
    private boolean aberto;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getSemane() {
        return semane;
    }

    public void setSemane(String semane) {
        this.semane = semane;
    }

    public String getHorarioAberto() {
        return horarioAberto;
    }

    public void setHorarioAberto(String horarioAberto) {
        this.horarioAberto = horarioAberto;
    }

    public String getHorarioFechado() {
        return horarioFechado;
    }

    public void setHorarioFechado(String horarioFechado) {
        this.horarioFechado = horarioFechado;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }
}
