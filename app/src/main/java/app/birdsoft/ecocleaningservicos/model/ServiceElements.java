package app.birdsoft.ecocleaningservicos.model;

import java.util.List;

public class ServiceElements {
    private int vazioVisibility, layoutConexaoVisibility, listaVisibility, progressVisibility, LayoutWifiOffline;
    public List<Servico> servicos;

    public int getLayoutConexaoVisibility() {
        return layoutConexaoVisibility;
    }

    public void setLayoutConexaoVisibility(int layoutConexaoVisibility) {
        this.layoutConexaoVisibility = layoutConexaoVisibility;
    }

    public int getLayoutWifiOffline() {
        return LayoutWifiOffline;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        LayoutWifiOffline = layoutWifiOffline;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public int getVazioVisibility() {
        return vazioVisibility;
    }

    public void setVazioVisibility(int vazioVisibility) {
        this.vazioVisibility = vazioVisibility;
    }

    public int getListaVisibility() {
        return listaVisibility;
    }

    public void setListaVisibility(int listaVisibility) {
        this.listaVisibility = listaVisibility;
    }

    public int getProgressVisibility() {
        return progressVisibility;
    }

    public void setProgressVisibility(int progressVisibility) {
        this.progressVisibility = progressVisibility;
    }

}
