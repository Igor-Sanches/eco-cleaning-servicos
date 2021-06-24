package app.birdsoft.ecocleaningservicos.model;

public class ConfigEmpresaElements {
    private int layoutPrincipal, layoutProgress, layoutConexao, layoutVazio, LayoutWifiOffline;
    private ConfigEmpresa configEmpresa;
    private boolean aberto;

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public int getLayoutConexao() {
        return layoutConexao;
    }

    public void setLayoutConexao(int layoutConexao) {
        this.layoutConexao = layoutConexao;
    }

    public int getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(int layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }

    public int getLayoutProgress() {
        return layoutProgress;
    }

    public void setLayoutProgress(int layoutProgress) {
        this.layoutProgress = layoutProgress;
    }

    public int getLayoutVazio() {
        return layoutVazio;
    }

    public void setLayoutVazio(int layoutVazio) {
        this.layoutVazio = layoutVazio;
    }

    public int getLayoutWifiOffline() {
        return LayoutWifiOffline;
    }

    public void setLayoutWifiOffline(int layoutWifiOffline) {
        LayoutWifiOffline = layoutWifiOffline;
    }

    public ConfigEmpresa getConfigEmpresa() {
        return configEmpresa;
    }

    public void setConfigEmpresa(ConfigEmpresa configEmpresa) {
        this.configEmpresa = configEmpresa;
    }
}
