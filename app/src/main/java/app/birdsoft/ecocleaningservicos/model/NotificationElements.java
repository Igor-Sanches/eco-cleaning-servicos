package app.birdsoft.ecocleaningservicos.model;

import java.util.List;

public class NotificationElements {
    private int layoutWifiVisibility, layoutConnectionVisibility, layoutProgressVisibility, layoutVazioVisibility, layoutVisibility;
    private List<Notification> notifications;
    private Integer numberNaoLida;

    public Integer getNumberNaoLida() {
        return numberNaoLida == null ? 0 : numberNaoLida;
    }

    public void setNumberNaoLida(Integer numberNaoLida) {
        this.numberNaoLida = numberNaoLida;
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

    public int getLayoutProgressVisibility() {
        return layoutProgressVisibility;
    }

    public void setLayoutProgressVisibility(int layoutProgressVisibility) {
        this.layoutProgressVisibility = layoutProgressVisibility;
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
