package app.birdsoft.ecocleaningservicos.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.Status;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.tools.Pagamento;

public class AdaptadorContratos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Pedido> contratos;
    private Context context;
    private int animation = 2, lastPosition = -1;
    public boolean on_atach = true;
    private OnEditarObservacaoAction onEditarObservacaoAction;
    private OnConfirmarCancelamentoAction onConfirmarCancelamentoAction;
    private OnVerObersavacaoAction onVerObersavacaoAction;
    private OnMenuOptionsAction onMenuOptionsAction;
    private OnCallServicoAction onCallServicoAction;
    private OnZapServicoAction onZapServicoAction;
    private OnAceitarAlteracaoAction onAceitarAlteracaoAction;
    private OnContraProposta onContraProposta;
    private OnRejeitarAlteracaoAction onRejeitarAlteracaoAction;
    private OnMotivoCancelamentoAction onMotivoCancelamentoAction;

    public void setOnContraProposta(OnContraProposta onContraProposta) {
        this.onContraProposta = onContraProposta;
    }

    public void setOnRejeitarAlteracaoAction(OnRejeitarAlteracaoAction onRejeitarAlteracaoAction) {
        this.onRejeitarAlteracaoAction = onRejeitarAlteracaoAction;
    }

    public void setOnAceitarAlteracaoAction(OnAceitarAlteracaoAction onAceitarAlteracaoAction) {
        this.onAceitarAlteracaoAction = onAceitarAlteracaoAction;
    }

    public void setOnConfirmarCancelamentoAction(OnConfirmarCancelamentoAction onConfirmarCancelamentoAction) {
        this.onConfirmarCancelamentoAction = onConfirmarCancelamentoAction;
    }

    public void setOnMotivoCancelamentoAction(OnMotivoCancelamentoAction onMotivoCancelamentoAction) {
        this.onMotivoCancelamentoAction = onMotivoCancelamentoAction;
    }

    public void setOnCallServicoAction(OnCallServicoAction onCallServicoAction) {
        this.onCallServicoAction = onCallServicoAction;
    }

    public void setOnZapServicoAction(OnZapServicoAction onZapServicoAction) {
        this.onZapServicoAction = onZapServicoAction;
    }

    public void setOnMenuOptionsAction(OnMenuOptionsAction onMenuOptionsAction) {
        this.onMenuOptionsAction = onMenuOptionsAction;
    }

    public void setOnEditarObservacaoAction(OnEditarObservacaoAction onEditarObservacaoAction) {
        this.onEditarObservacaoAction = onEditarObservacaoAction;
    }

    public void setOnVerObersavacaoAction(OnVerObersavacaoAction onVerObersavacaoAction) {
        this.onVerObersavacaoAction = onVerObersavacaoAction;
    }

    public void remover(int position) {
        contratos.remove(position);
        notifyDataSetChanged();
    }

    public interface OnContraProposta{
        void onProposta(Pedido pedido);
    }

    public interface OnAceitarAlteracaoAction{
        void onAceitar(Pedido pedido);
    }

    public interface OnRejeitarAlteracaoAction{
        void onRejeitar(Pedido pedido);
    }

    public interface OnConfirmarCancelamentoAction{
        void onConfirmar(Pedido pedido, int position);
    }

    public interface OnMotivoCancelamentoAction{
        void onMotivo(String motivo);
    }

    public interface OnCallServicoAction{
        void onCall();
    }

    public interface OnZapServicoAction{
        void onZap();
    }

    public interface OnMenuOptionsAction{
        void onMenu(Pedido pedido);
    }

    public interface OnVerObersavacaoAction{
        void onVisualizar(String observacao);
    }

    public interface OnEditarObservacaoAction{
        void onEditar(Pedido pedido);
    }

    public AdaptadorContratos(List<Pedido> contratos, Context context) {
        this.contratos = contratos;
        this.context =context;
    }

    public void inserir(List<Pedido> cidades) {
        this.contratos = cidades;
        notifyDataSetChanged();
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView situacao, pendente_analise, pagamento, novo_agendamento, endereco, numeros_servico, valor_troco, valor_total, horario_final, data_horario_servico, tempo_servico, nome_servicos, nome_display;
        public View layout;
        public Button contra_proposta, rejeitar_alteracao, aceitar_alteracao, comfirmar_cancelamento, editar_obs, ver_obs, menu_options, btn_ver_msg_cancelamento_pedido;
        public ImageButton call_option, zap_option;
        public LinearLayout layout_alteracao_agenda, layout_troco, layout_observacao;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            ver_obs = root.findViewById(R.id.ver_obs);
            btn_ver_msg_cancelamento_pedido = root.findViewById(R.id.btn_ver_msg_cancelamento_pedido);
            valor_total = root.findViewById(R.id.valor_total);
            contra_proposta = root.findViewById(R.id.contra_proposta);
            pendente_analise = root.findViewById(R.id.pendente_analise);
            situacao = root.findViewById(R.id.situacao);
            rejeitar_alteracao = root.findViewById(R.id.rejeitar_alteracao);
            aceitar_alteracao = root.findViewById(R.id.aceitar_alteracao);
            layout_alteracao_agenda = root.findViewById(R.id.layout_alteracao_agenda);
            zap_option = root.findViewById(R.id.zap_option);
            novo_agendamento = root.findViewById(R.id.novo_agendamento);
            comfirmar_cancelamento = root.findViewById(R.id.comfirmar_cancelamento);
            call_option = root.findViewById(R.id.call_option);
            menu_options = root.findViewById(R.id.menu_options);
            pagamento  =root.findViewById(R.id.pagamento);
            editar_obs = root.findViewById(R.id.editar_obs);
            layout_observacao = root.findViewById(R.id.layout_observacao);
            layout_troco = root.findViewById(R.id.layout_troco);
            endereco = root.findViewById(R.id.endereco);
            numeros_servico = root.findViewById(R.id.numeros_servico);
            valor_troco = root.findViewById(R.id.valor_troco);
            horario_final = root.findViewById(R.id.horario_final);
            data_horario_servico = root.findViewById(R.id.data_horario_servico);
            tempo_servico = root.findViewById(R.id.tempo_servico);
            nome_servicos = root.findViewById(R.id.nome_servicos);
            nome_display = root.findViewById(R.id.nome_display);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contrato, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
            Pedido pedido = contratos.get(position);
            viewHolder.endereco.setText(pedido.getEndereco());
            viewHolder.valor_total.setText(Mask.formatarValor(pedido.getValorTotal()));
            viewHolder.data_horario_servico.setText(String.format("%s as %s", pedido.getDataAgendamento(), pedido.getHorarioAgendamento()));
            viewHolder.pagamento.setText(getPagamento(pedido.getPagamento()));
            viewHolder.numeros_servico.setText(String.format("%s %s", pedido.getItensServicos().size(), pedido.getItensServicos().size() > 1 ? "Serviços" : "Serviço"));
            viewHolder.horario_final.setText(String.format("%s", pedido.getFimDoHorarioTotal()));
            viewHolder.nome_display.setText(pedido.getDisplayName());
            String result = pedido.getItensServicos().size() > 1 ? " e mais " + pedido.getItensServicos().size() : "";
            viewHolder.nome_servicos.setText(String.format("%s %s", pedido.getItensServicos().get(0).getDisplayName(), result));

            if(pedido.getHora() != null){
                viewHolder.tempo_servico.setText(String.format("%s", HelperManager.getTempoServico(pedido.getHora(), pedido.getMinuto())));
            }
            if(pedido.isTroco()){
                viewHolder.valor_troco.setText(Mask.formatarValor(pedido.getValorTroco()));
                viewHolder.layout_troco.setVisibility(View.VISIBLE);
            }
            if(!pedido.getObservacao().equals("") && !pedido.isCancelado()){
                viewHolder.layout_observacao.setVisibility(View.VISIBLE);
            }
            viewHolder.pendente_analise.setVisibility(pedido.getStatusServico().equals(Status.pendente.toString()) ? View.VISIBLE : View.GONE);
            viewHolder.layout_alteracao_agenda.setVisibility(pedido.getStatusServico().equals(Status.mudancaAgendamento.toString()) ? View.VISIBLE : View.GONE);
            if(pedido.getStatusServico().equals(Status.mudancaAgendamento.toString())){
                viewHolder.contra_proposta.setOnClickListener((v) -> onContraProposta.onProposta(pedido));
                viewHolder.rejeitar_alteracao.setOnClickListener((v) -> onRejeitarAlteracaoAction.onRejeitar(pedido));
                viewHolder.aceitar_alteracao.setOnClickListener((v) -> onAceitarAlteracaoAction.onAceitar(pedido));
                viewHolder.contra_proposta.setVisibility(pedido.isContraProposta() ? View.VISIBLE : View.GONE);
                viewHolder.novo_agendamento.setText(String.format("%s %s as %s", context.getString(R.string.novo_agenda), pedido.getDataAgendamento(), pedido.getHorarioAgendamento()));
            }
            viewHolder.comfirmar_cancelamento.setVisibility(pedido.getStatusServico().equals(Status.recusado.toString()) || pedido.getStatusServico().equals(Status.cancelado.toString()) ? View.VISIBLE : View.GONE);
            viewHolder.comfirmar_cancelamento.setOnClickListener((v) -> onConfirmarCancelamentoAction.onConfirmar(pedido, position));
            viewHolder.ver_obs.setOnClickListener((v) -> onVerObersavacaoAction.onVisualizar(pedido.getObservacao()));
            viewHolder.editar_obs.setOnClickListener((v) -> onEditarObservacaoAction.onEditar(pedido));
            viewHolder.call_option.setOnClickListener((v) -> onCallServicoAction.onCall());
            viewHolder.zap_option.setOnClickListener((v) -> onZapServicoAction.onZap());
            viewHolder.menu_options.setOnClickListener((v) -> onMenuOptionsAction.onMenu(pedido));
            viewHolder.btn_ver_msg_cancelamento_pedido.setVisibility(pedido.isCancelado() ? View.VISIBLE : View.GONE);
            viewHolder.menu_options.setVisibility(pedido.isCancelado() || pedido.getStatusServico().equals(Status.recusado.toString()) || pedido.getStatusServico().equals(Status.pendente.toString()) || pedido.getStatusServico().equals(Status.contraProposta.toString()) || pedido.getStatusServico().equals(Status.mudancaAgendamento.toString()) ? View.GONE : View.VISIBLE);
            inputSituacao(viewHolder.situacao, pedido);
            viewHolder.btn_ver_msg_cancelamento_pedido.setOnClickListener((v)->onMotivoCancelamentoAction.onMotivo(pedido.getMsgCancelamento()));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void inputSituacao(TextView situacao, Pedido pedido) {
        if(pedido.isCancelado() && !pedido.getStatusServico().equals(Status.cancelado.toString())){
            situacao.setTextColor(R.color.acriligo_tint_vermelho);
            situacao.setBackgroundResource(R.drawable.btn_acriligo_vermelho);
            situacao.setText(context.getString(R.string.aguardando_cancelamento));
            situacao.setOnClickListener((v) -> new DialogMessage(context, context.getString(R.string.msg_cancel), false, "").show());
        }else{
            if(pedido.getStatusServico().equals(Status.recusado.toString())){
                situacao.setOnClickListener((v) -> new DialogMessage(context, String.format("%s", context.getString(R.string.msg_view_recusado)), false, "").show());
            }
            if(pedido.getStatusServico().equals(Status.pendente.toString())){
                situacao.setOnClickListener((v) -> new DialogMessage(context, String.format("%s %s as %s", context.getString(R.string.msg_view_pendente), pedido.getDataAgendamento(), pedido.getHorarioAgendamento()), false, "").show());
            }
            if(pedido.getStatusServico().equals(Status.aceito.toString())){
                situacao.setOnClickListener((v) -> new DialogMessage(context, String.format("%s %s as %s", context.getString(R.string.msg_aceito_agenda), pedido.getDataAgendamento(), pedido.getHorarioAgendamento()), false, "").show());
            }
            if(pedido.getStatusServico().equals(Status.contraProposta.toString())){
                situacao.setOnClickListener((v) -> new DialogMessage(context, String.format("%s", context.getString(R.string.msg_contra_proposta_agenda)), false, "").show());
            }
            if(pedido.getStatusServico().equals(Status.mudancaAgendamento.toString())){
                situacao.setOnClickListener((v) -> new DialogMessage(context, String.format("%s", context.getString(R.string.msg_mudancaAgendamento_agenda)), false, "").show());
            }
            situacao.setTextColor(getSituacaoColorTint(pedido.getStatusServico()));
            situacao.setBackgroundResource(getSituacaoDrawable(pedido.getStatusServico()));
            situacao.setText(context.getString(getSituacao(pedido.getStatusServico())));
        }
    }

    private @ColorRes
    int getSituacaoColorTint(String statusServico) {
        int res = 0;
        switch (Status.valueOf(statusServico)){
            case aceito:
            case contraProposta:
                res = R.color.acriligo_tint_verde; break;
            case recusado:
            case concluido:
            case cancelado:
                res = R.color.acriligo_tint_vermelho; break;
            case pendente: res = R.color.acriligo_tint_laranja; break;
            case mudancaAgendamento: res = R.color.acriligo_tint_lilais; break;
        }

        return res;
    }

    private int getPagamento(String pagamento) {
        if(Pagamento.valueOf(pagamento) == Pagamento.cartao_credito)
            return R.string.cartao_credito;
        else if(Pagamento.valueOf(pagamento) == Pagamento.cartao_debido)
            return R.string.cartao_debido;
        else return R.string.dinheiro;
    }

    private @StringRes
    int getSituacao(String statusServico) {
        int res = 0;
        switch (Status.valueOf(statusServico)){
            case aceito: res = R.string.aceito; break;
            case recusado: res = R.string.recusado; break;
            case pendente: res = R.string.pendente; break;
            case cancelado: res = R.string.cancelado; break;
            case concluido: res = R.string.concluido; break;
            case contraProposta: res = R.string.contraProposta; break;
            case mudancaAgendamento: res = R.string.mudancaAgendamento; break;
        }

        return res;
    }

    @SuppressLint("SupportAnnotationUsage")
    private @DrawableRes
    int getSituacaoDrawable(String statusServico) {
        int res = 0;
        switch (Status.valueOf(statusServico)){
            case aceito:
            case contraProposta:
                res = R.drawable.btn_acriligo_verde; break;
            case recusado:
            case concluido:
            case cancelado:
                res = R.drawable.btn_acriligo_vermelho; break;
            case pendente: res = R.drawable.btn_acriligo_laranja; break;
            case mudancaAgendamento: res = R.drawable.btn_acriligo_lilais; break;
        }

        return res;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_atach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return contratos.size();
    }
}



