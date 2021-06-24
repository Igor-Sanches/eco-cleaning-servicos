package app.birdsoft.ecocleaningservicos.model;

import java.util.List;

public class PedidosElements {
    private int layoutWifiVisibility, layoutConnectionVisibility, layoutProgressVisibility, layoutVazioVisibility, layoutVisibility;
    private List<Pedido> pedidos;

    public int getLayoutProgressVisibility() {
        return layoutProgressVisibility;
    }

    public void setLayoutProgressVisibility(int layoutProgressVisibility) {
        this.layoutProgressVisibility = layoutProgressVisibility;
    }

    public int getLayoutWifiVisibility() {
        return layoutWifiVisibility;
    }

    public void setLayoutWifiVisibility(int layoutWifiVisibility) {
        this.layoutWifiVisibility = layoutWifiVisibility;
    }

    public int getLayoutConnectionVisibility() {
        return layoutConnectionVisibility;
    }

    public void setLayoutConnectionVisibility(int layoutConnectionVisibility) {
        this.layoutConnectionVisibility = layoutConnectionVisibility;
    }

    public int getLayoutVazioVisibility() {
        return layoutVazioVisibility;
    }

    public void setLayoutVazioVisibility(int layoutVazioVisibility) {
        this.layoutVazioVisibility = layoutVazioVisibility;
    }

    public int getLayoutVisibility() {
        return layoutVisibility;
    }

    public void setLayoutVisibility(int layoutVisibility) {
        this.layoutVisibility = layoutVisibility;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
