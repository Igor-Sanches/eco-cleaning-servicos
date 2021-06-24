package app.birdsoft.ecocleaningservicos.model;

import java.util.List;

public class Pedido {
    private boolean editadoObservacao;
    private String observacao;
    private String uid;
    private String DisplayName;
    private Double valorTotal;
    private String endereco, cidade;
    private String pagamento;
    private long dataServico, dataConcluido;
    private String msgCancelamento;
    private String uid_client;
    private String telefone;
    private String clienteNome;
    private List<Carrinho> itensServicos;
    private String statusServico;
    private Double valorTroco;
    private boolean isTroco, contraProposta;
    private boolean cancelado;
    private String coordenadas;
    private Integer hora, minuto;
    private String dataAgendamento, horarioAgendamento, fimDoHorarioTotal;

    public boolean isContraProposta() {
        return contraProposta;
    }

    public void setContraProposta(boolean contraProposta) {
        this.contraProposta = contraProposta;
    }

    public boolean isEditadoObservacao() {
        return editadoObservacao;
    }

    public void setEditadoObservacao(boolean editadoObservacao) {
        this.editadoObservacao = editadoObservacao;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCidade() {
        return cidade;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public Integer getHora() {
        return hora;
    }

    public String getFimDoHorarioTotal() {
        return fimDoHorarioTotal;
    }

    public void setFimDoHorarioTotal(String fimDoHorarioTotal) {
        this.fimDoHorarioTotal = fimDoHorarioTotal;
    }

    public String getDataAgendamento() {
        return dataAgendamento;
    }

    public String getHorarioAgendamento() {
        return horarioAgendamento;
    }

    public void setDataAgendamento(String dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public void setHorarioAgendamento(String horarioAgendamento) {
        this.horarioAgendamento = horarioAgendamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    private boolean alteracao;
    private String msgAlteracao;
    private boolean atrasado;

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public boolean isAlteracao() {
        return alteracao;
    }

    public String getMsgAlteracao() {
        return msgAlteracao;
    }

    public void setAlteracao(boolean alteracao) {
        this.alteracao = alteracao;
    }

    public void setMsgAlteracao(String msgAlteracao) {
        this.msgAlteracao = msgAlteracao;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getStatusServico() {
        return statusServico;
    }

    public void setStatusServico(String statusServico) {
        this.statusServico = statusServico;
    }

    public Double getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(Double valorTroco) {
        this.valorTroco = valorTroco;
    }

    public boolean isTroco() {
        return isTroco;
    }

    public void setTroco(boolean troco) {
        isTroco = troco;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public long getDataServico() {
        return dataServico;
    }

    public void setDataServico(long dataServico) {
        this.dataServico = dataServico;
    }

    public long getDataConcluido() {
        return dataConcluido;
    }

    public void setDataConcluido(long dataConcluido) {
        this.dataConcluido = dataConcluido;
    }

    public String getMsgCancelamento() {
        return msgCancelamento;
    }

    public void setMsgCancelamento(String msgCancelamento) {
        this.msgCancelamento = msgCancelamento;
    }

    public String getUid_client() {
        return uid_client;
    }

    public void setUid_client(String uid_client) {
        this.uid_client = uid_client;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public List<Carrinho> getItensServicos() {
        return itensServicos;
    }

    public void setItensServicos(List<Carrinho> itensServicos) {
        this.itensServicos = itensServicos;
    }
}