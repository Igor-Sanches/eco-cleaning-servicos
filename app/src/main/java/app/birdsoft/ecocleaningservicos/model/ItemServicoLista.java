package app.birdsoft.ecocleaningservicos.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemServicoLista implements Serializable {
    private String displayName = "";
    private ArrayList<String> contents = new ArrayList<>();
    private ArrayList<Double> valores = new ArrayList<>();
    private ArrayList<Integer> quantidate = new ArrayList<>();

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public ArrayList<Double> getValores() {
        return valores;
    }

    public void setValores(ArrayList<Double> valores) {
        this.valores = valores;
    }

    public ArrayList<Integer> getQuantidate() {
        return quantidate;
    }

    public void setQuantidate(ArrayList<Integer> quantidate) {
        this.quantidate = quantidate;
    }
}
