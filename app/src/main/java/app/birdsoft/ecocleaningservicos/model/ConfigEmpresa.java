package app.birdsoft.ecocleaningservicos.model;

import java.util.ArrayList;

public class ConfigEmpresa {
    private boolean aberto;
    private String whastapp, telefone, email;
    private ArrayList<HorariosFuncionamento> horariosFuncionamentos;
    private ArrayList<String> locaisFucionamentos;

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public String getWhastapp() {
        return whastapp;
    }

    public void setWhastapp(String whastapp) {
        this.whastapp = whastapp;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<HorariosFuncionamento> getHorariosFuncionamentos() {
        return horariosFuncionamentos;
    }

    public void setHorariosFuncionamentos(ArrayList<HorariosFuncionamento> horariosFuncionamentos) {
        this.horariosFuncionamentos = horariosFuncionamentos;
    }

    public ArrayList<String> getLocaisFucionamentos() {
        return locaisFucionamentos;
    }

    public void setLocaisFucionamentos(ArrayList<String> locaisFucionamentos) {
        this.locaisFucionamentos = locaisFucionamentos;
    }
}
