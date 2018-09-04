package es.cvjpy.cobol;

public class Texto extends Campo {

    private String texto = "";

    public Texto(Registro registro, String nombre, int inicio, int longitud) {
        super(registro, nombre, inicio, longitud);
    }

    void loadFromRegistro() {
        texto = new String(getRegistro().saca(getInicio(), getLongitud()));
    }

    void storeToRegistro() {
        if (texto == null) {
            texto = "";
        }
        getRegistro().mete(relleno(texto).toCharArray(), getInicio());
    }

    public String get() {
        loadFromRegistro();
        return texto;
    }

    public void set(String texto) {
        this.texto = texto;
        storeToRegistro();
    }

}
