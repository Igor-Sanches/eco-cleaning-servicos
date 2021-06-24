package app.birdsoft.ecocleaningservicos.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private String titulo, cliente, mensagem, uidCliente, uidServico, type, uid;
    private boolean lida, recebida, responder;
    private long data;
    private long id;

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCliente() {
        return cliente;
    }

    public String getType() {
        return type;
    }

    public void setResponder(boolean responder) {
        this.responder = responder;
    }

    public boolean isResponder() {
        return responder;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUidCliente(String uidCliente) {
        this.uidCliente = uidCliente;
    }

    public void setUidServico(String uidServico) {
        this.uidServico = uidServico;
    }

    public String getUidCliente() {
        return uidCliente;
    }

    public String getUidServico() {
        return uidServico;
    }

    public boolean isRecebida() {
        return recebida;
    }

    public void setRecebida(boolean recebida) {
        this.recebida = recebida;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
