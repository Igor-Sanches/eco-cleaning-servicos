package app.birdsoft.ecocleaningservicos.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Servico implements Serializable {
    private int position;
    private String uid;
    private int hora, minuto;
    private boolean favorito;
    private String name;
    private String descricao;
    private String imageUrl;
    private int tipoServico;
    private double valor;
    private boolean disponivel;
    private ArrayList<BlocoPublicar> servicos;
    private long data;

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getPosition() {
        return position;
    }

    public void setServicos(ArrayList<BlocoPublicar> servicos) {
        this.servicos = servicos;
    }

    public ArrayList<BlocoPublicar> getServicos() {
        return servicos;
    }

    public void setData(long data) {
        this.data = data;
    }

    public long getData() {
        return data;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(int tipoServico) {
        this.tipoServico = tipoServico;
    }

}
