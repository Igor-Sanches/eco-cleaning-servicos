package app.birdsoft.ecocleaningservicos.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.viewModel.CarrinhoViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ClienteViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.FavoritosViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.SettingsViewModel;

public class UpdateAllViewModel extends AsyncTask<ViewModelStoreOwner, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    public UpdateAllViewModel(Context context){
        this.context = context;
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(ViewModelStoreOwner... owner) {
        new ViewModelProvider(owner[0]).get(CarrinhoViewModel.class).init(context);
        new ViewModelProvider(owner[0]).get(ClienteViewModel.class).init(context);
        new ViewModelProvider(owner[0]).get(FavoritosViewModel.class).init(context);
        new ViewModelProvider(owner[0]).get(ServicosViewModel.class).init(context);
        new ViewModelProvider(owner[0]).get(ServicosViewModel.class).initContratos(context);
        new ViewModelProvider(owner[0]).get(SettingsViewModel.class).init(context);
        return null;
    }
}
