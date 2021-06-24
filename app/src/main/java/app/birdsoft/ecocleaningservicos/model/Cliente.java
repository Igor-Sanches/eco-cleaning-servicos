package app.birdsoft.ecocleaningservicos.model;

import com.google.firebase.firestore.GeoPoint;

public class Cliente {

    private String nomeRuaNumero, bairro, referencia, numeroCasa, tipo_lugar, complemento, bloco_n_ap;;
    private GeoPoint local;
    private boolean sn;
    private boolean block;
    private String msgBlock, cidadeLocal;

    public void setSn(boolean sn) {
        this.sn = sn;
    }

    public boolean isSn() {
        return sn;
    }

    public String getBloco_n_ap() {
        return bloco_n_ap;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getTipo_lugar() {
        return tipo_lugar;
    }

    public void setBloco_n_ap(String bloco_n_ap) {
        this.bloco_n_ap = bloco_n_ap;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setTipo_lugar(String tipo_lugar) {
        this.tipo_lugar = tipo_lugar;
    }

    public String getNomeRuaNumero() {
        return nomeRuaNumero;
    }

    public void setNomeRuaNumero(String nomeRuaNumero) {
        this.nomeRuaNumero = nomeRuaNumero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidadeLocal() {
        return cidadeLocal;
    }

    public void setCidadeLocal(String cidadeLocal) {
        this.cidadeLocal = cidadeLocal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void setMsgBlock(String msgBlock) {
        this.msgBlock = msgBlock;
    }

    public String getMsgBlock() {
        return msgBlock;
    }

    public GeoPoint getLocal() {
        return local;
    }

    public void setLocal(GeoPoint local) {
        this.local = local;
    }

    private String uuid, nome, email, telefone, endereco;
    private long data;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

}
