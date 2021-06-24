package app.birdsoft.ecocleaningservicos.dialogo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.model.HorariosFuncionamento;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class DialogAgendamento extends AlertDialog {

    private final Activity activity;
    private final LifecycleOwner owner;
    private String dataFormatada = "";
    private final ViewModelStoreOwner modelStoreOwner;
    private OnDataSelectedListener onDataSelectedListener;
    public interface OnDataSelectedListener{
        void onData(String data, String horario);
    }

    public void setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
        this.onDataSelectedListener = onDataSelectedListener;
    }

    public DialogAgendamento(@NonNull Activity activity, LifecycleOwner owner, ViewModelStoreOwner modelStoreOwner) {
        super(activity);
        this.owner = owner;
        this.modelStoreOwner = modelStoreOwner;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void show() {
        if(!isShowing()) {
            super.show();
            DatePicker data_selector = findViewById(R.id.data_selector);
            TextView semana_view = findViewById(R.id.semana_view);
            TimePicker timer_selector = findViewById(R.id.timer_selector);
            assert timer_selector != null;
            assert data_selector != null;
            assert semana_view != null;
            data_selector.setMinDate(DateTime.getTime());
            timer_selector.setIs24HourView(true);
            dataFormatada = String.format("%s/%s/%s", String.valueOf(data_selector.getDayOfMonth()).length() == 1 ? "0" + data_selector.getDayOfMonth() : data_selector.getDayOfMonth(), String.valueOf(data_selector.getMonth()).length() == 1 ? "0" + data_selector.getMonth() : data_selector.getMonth(), data_selector.getYear());
            semana_view.setText(DateTime.toStringFromDateSemanaNoSigleSelected(dataFormatada));
            data_selector.setOnDateChangedListener(((view, year, monthOfYear, dayOfMonth) ->
            {
                dataFormatada = String.format("%s/%s/%s", String.valueOf(data_selector.getDayOfMonth()).length() == 1 ? "0" + data_selector.getDayOfMonth() : data_selector.getDayOfMonth(), String.valueOf(data_selector.getMonth()).length() == 1 ? "0" + data_selector.getMonth() : data_selector.getMonth(), data_selector.getYear());
                semana_view.setText(DateTime.toStringFromDateSemanaNoSigleSelected(dataFormatada));
            }
            ));
            Objects.requireNonNull((View) findViewById(R.id.confirmar)).setOnClickListener((v) -> {
                if(Conexao.isConnected(activity)){
                    LoadingDialog dialog = new LoadingDialog(activity);
                    dialog.show();
                    new ViewModelProvider(modelStoreOwner).get(ServicosViewModel.class)
                            .getHorarios().observe(owner, (horariosFuncionamentos -> {
                        if(horariosFuncionamentos != null){
                            dataFormatada = String.format("%s/%s/%s", String.valueOf(data_selector.getDayOfMonth()).length() == 1 ? "0" + data_selector.getDayOfMonth() : data_selector.getDayOfMonth(), String.valueOf(data_selector.getMonth()).length() == 1 ? "0" + data_selector.getMonth() : data_selector.getMonth(), data_selector.getYear());

                            String semana = DateTime.toStringFromDateSemanaSelected(dataFormatada);
                            HorariosFuncionamento data = null;
                            for(HorariosFuncionamento hr : horariosFuncionamentos){
                                if(hr.getSigla().toUpperCase().equals(semana.toUpperCase())){
                                    data = hr;
                                    break;
                                }
                            }
                            if(data != null){
                                if(data.isAberto()){
                                    String hora = String.valueOf(timer_selector.getHour()).length() == 1 ? "0" + timer_selector.getHour() : timer_selector.getHour()+"";
                                    String minuto = String.valueOf(timer_selector.getMinute()).length() == 1 ? "0" + timer_selector.getMinute() : timer_selector.getMinute()+"";
                                    String horaFormatada = String.format("%s:%s", hora, minuto);
                                    if(DateTime.isAbertoServico(horaFormatada, data.getHorarioAberto(), dataFormatada)){
                                        if(DateTime.isFechadoServico(horaFormatada, data.getHorarioFechado(), dataFormatada)){
                                            dismiss();
                                            onDataSelectedListener.onData(dataFormatada, horaFormatada);
                                        }else{
                                            messageErro(String.format("%s %s", data.getHorarioFechado(), activity.getString(R.string.erro_dia_hr_servico_fechamento)));
                                        }
                                    }else {
                                        messageErro(String.format("%s %s", data.getHorarioAberto(), activity.getString(R.string.erro_dia_hr_servico_aberto)));
                                    }

                                }else{
                                    messageErro(R.string.erro_dia_indisponivel);
                                }
                            }else{
                                messageErro(R.string.erro_dia_indisponivel);
                            }

                        }else{
                            MyToast.makeText(activity, R.string.os_horarios_nulos, ModoColor._falha).show();
                        }
                        dialog.dismiss();
                    }));
                }else{
                    DialogMessage
                            message = new DialogMessage(activity, activity.getString(R.string.sem_conexao), false, "");
                    message.show();
                }
            });
            Objects.requireNonNull((View) findViewById(R.id.cancel)).setOnClickListener((v) -> dismiss());

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    private void messageErro(@StringRes int res){
        DialogMessage
                message = new DialogMessage(activity, activity.getString(R.string.erro_dia_indisponivel), false, "");
        message.show();
    }

    private void messageErro(String msg){
        DialogMessage
                message = new DialogMessage(activity, msg, false, "");
        message.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_agendamento);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
