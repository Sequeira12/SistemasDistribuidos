package pt.uc.sd.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Resultado implements Serializable {
    private String Titulo;
    private String Citacao;
    private String Url;

    private ArrayList<String> Urls;

    public Resultado(String titulo, String citação, String url, ArrayList<String> urls) {
        Titulo = titulo;
        Citacao = citação;
        Url = url;
        Urls = urls;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getCitacao() {
        return Citacao;
    }

    public void setCitacao(String citacao) {
        Citacao = citacao;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public ArrayList<String> getUrls() {
        return Urls;
    }

    public void setUrls(ArrayList<String> urls) {
        Urls = urls;
    }
}
