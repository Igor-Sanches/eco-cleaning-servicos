package app.birdsoft.ecocleaningservicos.model;

public class NotificationData {
     private String titulo, cliente, mensagem, uidCliente, uidServico, type, responder, statusServico;

    public String getStatusServico() {
        return statusServico;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setStatusServico(String statusServico) {
        this.statusServico = statusServico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getUidCliente() {
        return uidCliente;
    }

    public void setUidCliente(String uidCliente) {
        this.uidCliente = uidCliente;
    }

    public String getUidServico() {
        return uidServico;
    }

    public void setUidServico(String uidServico) {
        this.uidServico = uidServico;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public NotificationData(){}
    public NotificationData(String titulo, String mensagem, String uidCliente, String uidServico, String type, String responder, String statusServico) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.statusServico = statusServico;
        this.uidCliente = uidCliente;
        this.uidServico = uidServico;
        this.type = type;
        this.responder = responder;
    }
}
