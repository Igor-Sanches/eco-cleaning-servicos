package app.birdsoft.ecocleaningservicos.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogEndereco;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.DialogSair;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.model.BlocoPublicar;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.model.ItemServico;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.viewModel.ClienteViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ConfigEmpresaViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;

public class HelperManager {

    public static void exitApp(Activity activity) {
        DialogSair dialogSair = new DialogSair(activity);
        dialogSair.show();
    }


    public static String messageView(ItemServico itemCardapio, Context context) {
        String msg;
        String start;
        if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.escolha)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.escolar))){
            start = context.getString(R.string.e_nescessario_que);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.selecione)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.selaciona))){
            start = context.getString(R.string.e_nescessario_voce);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.bedida)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.bebidas))){
            start = context.getString(R.string.e_nescessario_voce_seleciona_uma);
        }else if(itemCardapio.getDispayTitulo().toLowerCase().startsWith(context.getString(R.string.sabo)) || itemCardapio.getDispayTitulo().toLowerCase().endsWith(context.getString(R.string.sabor))){
            start = context.getString(R.string.e_nescessario_voce_selecione_um);
        }else{
            start = context.getString(R.string.e_nescessario);
        }
        if(!itemCardapio.isMultiselect()){
            String f = itemCardapio.getSelectMax() == 1 ? context.getString(R.string.item) : context.getString(R.string.itens);

            msg = start + " " + itemCardapio.getDispayTitulo().toLowerCase() + " " + context.getString(R.string.para_continuar_vc_selecione_ate) + " " + itemCardapio.getSelectMax() + " " + f;
        }else{
            msg = start + " " + itemCardapio.getDispayTitulo().toLowerCase() + " " + context.getString(R.string.para_continuar);
        }

        return msg;
    }

    public static BlocoPublicar convert(ItemServico itemServico) {
        BlocoPublicar publicar = new BlocoPublicar();
        publicar.setContents(itemServico.getContents());
        publicar.setDispayTitulo(itemServico.getDispayTitulo());
        publicar.setItensAdicionais(itemServico.isItensAdicionais());
        publicar.setMaxItensAdicionais(itemServico.getMaxItensAdicionais());
        publicar.setMultiselect(itemServico.isMultiselect());
        publicar.setObgdSelect(itemServico.isObgdSelect());
        publicar.setSelectMax(itemServico.getSelectMax());
        publicar.setValores(itemServico.getValores());
        publicar.setText(itemServico.getText());
        publicar.setTextos(itemServico.getTextos());
        publicar.setValorMaior(itemServico.isValorMaior());
        return publicar;
    }

    public static ItemServico convert(BlocoPublicar publicar) {
        ItemServico itemServico = new ItemServico();
        itemServico.setContents(publicar.getContents());
        itemServico.setDispayTitulo(publicar.getDispayTitulo());
        itemServico.setText(publicar.getText());
        itemServico.setTextos(publicar.getTextos());
        itemServico.setItensAdicionais(publicar.isItensAdicionais());
        itemServico.setMaxItensAdicionais(publicar.getMaxItensAdicionais());
        itemServico.setMultiselect(publicar.isMultiselect());
        itemServico.setObgdSelect(publicar.isObgdSelect());
        itemServico.setSelectMax(publicar.getSelectMax());
        itemServico.setValores(publicar.getValores());
        itemServico.setValorMaior(publicar.isValorMaior());
        return itemServico;
    }


    public static List<ItemServico> convert(ArrayList<BlocoPublicar> cardapio) {
        List<ItemServico> cardapioList = new ArrayList<>();
        for(BlocoPublicar blocoPublicar : cardapio){
            cardapioList.add(convert(blocoPublicar));
        }
        return cardapioList;
    }

    public static long clieckTime = 0;
    public static boolean isClicked(){
        if(System.currentTimeMillis() - clieckTime > 4000){
            clieckTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public static String getTempoServico(int hora, int minuto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.add(Calendar.MINUTE, minuto);
        String result = "";
        int _hora = calendar.get(Calendar.HOUR_OF_DAY);
        int _minuto = calendar.get(Calendar.MINUTE);
        Date date = calendar.getTime();
        String dia = DateTime.toDateString("dd", date.getTime());
        int _dia = Integer.parseInt(dia) - 1;
        System.out.println(_dia);
        if(_dia > 0){
            result = String.format("%s %s", _dia, _dia == 1 ? "Dia" : "Dias");
        }
        if(_hora > 0){
            if(!result.equals(""))
                result += ", ";

            result += String.format("%s %s", _hora, _hora == 1 ? "Hora" : "Horas");
        }
        if(_minuto > 0){
            if(!result.equals(""))
                result += " e ";

            result += String.format("%s %s", _minuto, _minuto == 1 ? "Minuto" : "Minutos");
        }

        return result;

    }

    public static LiveData<Endereco> inputEnderecoCliente(Activity context){
        MutableLiveData<Endereco> data = new MutableLiveData<>();
        inputEnderecoCliente(context, null, data);
        return data;
    }

    public static LiveData<Endereco> inputEnderecoCliente(Activity context, Endereco endereco){
        MutableLiveData<Endereco> data = new MutableLiveData<>();
        inputEnderecoCliente(context, endereco, data);
        return data;
    }

    public static void inputEnderecoCliente(Activity context, Endereco _endereco, MutableLiveData<Endereco> data) {
        DialogEndereco dialogEndereco = new DialogEndereco(context, _endereco, data);
        dialogEndereco.show();
    }

    public static String getTempoServicoTotal(int hora, int minuto, String horario) {
        String hr = horario.split(":")[0];
        String min = horario.split(":")[1];
        int hora_ = Integer.parseInt(hr);
        int minuto_ = Integer.parseInt(min);
        hora += hora_;
        minuto += minuto_;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.add(Calendar.MINUTE, minuto);
        int _hora = calendar.get(Calendar.HOUR_OF_DAY);
        int _minuto = calendar.get(Calendar.MINUTE);

        return String.format("%s:%s", String.valueOf(_hora).length() == 1 ? "0" + _hora : _hora, String.valueOf(_minuto).length() == 1 ? "0" + _minuto : _minuto);
    }

    public static void onCallService(Activity context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.show();
        ConfigEmpresaViewModel viewModel =  new ViewModelProvider(viewModelStoreOwner).get(ConfigEmpresaViewModel.class);
        viewModel.init(context);
        viewModel.getMutableLiveData().observe(lifecycleOwner, (configEmpresaElements -> {
            if(configEmpresaElements.getConfigEmpresa() != null){
                String numeroTelefone = configEmpresaElements.getConfigEmpresa().getTelefone();
                if(numeroTelefone != null){
                    StringBuilder numero = new StringBuilder();
                    for(char c : numeroTelefone.toCharArray()){
                        if("0123456789".contains(String.valueOf(c))){
                            numero.append(c);
                        }
                    }

                    String zap = PhoneNumberUtils.formatNumber(numero.toString());
                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + zap)));
                }else{
                    messageView(R.string.nao_posivel_conectar, context);
                }
            }else{
                messageView(R.string.nao_posivel_conectar, context);
            }
            dialog.dismiss();
        }));
    }

    private static void messageView(@StringRes int res, Activity context){
        DialogMessage message =
                new DialogMessage(context, context.getString(res), false, "");
        message.show();
    }

    public static void onEmailService(Activity context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.show();
        ConfigEmpresaViewModel viewModel =  new ViewModelProvider(viewModelStoreOwner).get(ConfigEmpresaViewModel.class);
        viewModel.init(context);
        viewModel.getMutableLiveData().observe(lifecycleOwner, (configEmpresaElements -> {
            if(configEmpresaElements.getConfigEmpresa() != null){
                String email = configEmpresaElements.getConfigEmpresa().getEmail();
                if(email != null){
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                       context.startActivity(intent);
                    }else messageView(R.string.nao_posivel_conectar, context);
                }else{
                    messageView(R.string.nao_posivel_conectar, context);
                }
            }else{
                messageView(R.string.nao_posivel_conectar, context);
            }
            dialog.dismiss();
        }));
    }

    public static void onZapService(Activity context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner) {
        try{
            LoadingDialog dialog = new LoadingDialog(context);
            dialog.show();
            ConfigEmpresaViewModel viewModel =  new ViewModelProvider(viewModelStoreOwner).get(ConfigEmpresaViewModel.class);
            viewModel.init(context);
            viewModel.getMutableLiveData().observe(lifecycleOwner, (configEmpresaElements -> {
                if(configEmpresaElements.getConfigEmpresa() != null){
                    String numeroWhatsApp = configEmpresaElements.getConfigEmpresa().getWhastapp();
                    if(numeroWhatsApp != null){
                        StringBuilder numero = new StringBuilder();
                        for(char c : numeroWhatsApp.toCharArray()){
                            if("0123456789".contains(String.valueOf(c))){
                                numero.append(c);
                            }
                        }

                        String zap = PhoneNumberUtils.formatNumber(numero.toString());
                        if("###########".length() == zap.length() || "##########".length() == zap.length()){
                            zap = "+55" + zap;
                        }
                        String link = "https://api.whatsapp.com/send?phone=" + zap;
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    }else{
                        messageView(R.string.nao_posivel_conectar, context);
                    }
                }else{
                    messageView(R.string.nao_posivel_conectar, context);
                }
                dialog.dismiss();
            }));
        }catch (Exception x){
            System.out.println(x.getMessage());
        }

    }

    public static boolean isAtrasado(Pedido pedido) {
        String dataFormatada = String.format("%s %s", pedido.getDataAgendamento(),pedido.getHorarioAgendamento());
        return DateTime.isAtrasado(dataFormatada);
    }

    public static Boolean getTempoServicoParse(int hora, int minuto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(0));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.add(Calendar.MINUTE, minuto);
        int _hora = calendar.get(Calendar.HOUR_OF_DAY);
        Date date = calendar.getTime();
        String dia = DateTime.toDateString("dd", date.getTime());
        int _dia = Integer.parseInt(dia) - 1;

        return _hora <= 10 && _dia == 0;
    }
}
