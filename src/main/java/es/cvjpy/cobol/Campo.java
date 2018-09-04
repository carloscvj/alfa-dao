package es.cvjpy.cobol;

public class Campo implements CampoCobol {

    private final Registro registro;
    private final String nombre;
    private final int inicio;
    private final int longitud;

    public Campo(Registro registro, String nombre, int inicio, int longitud) {
        this.registro = registro;
        this.nombre = nombre;
        this.inicio = inicio;
        this.longitud = longitud;
    }

    public Registro getRegistro() {
        return this.registro;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    protected int getInicio() {
        return inicio;
    }

    @Override
    public int getLongitud() {
        return longitud;
    }

    @Override
    public String getCobolText() {
        String text = new String(registro.saca(getInicio(), getLongitud()));
        return text;
    }

    @Override
    public void setCobolText(String texto) {
        registro.mete(relleno(texto).toCharArray(), getInicio());
    }

    protected String relleno(String texto) {
        StringBuilder ret = new StringBuilder(texto);
        for (int i = 0; i < getLongitud(); i++) {
            ret.append(" ");
        }
        return ret.substring(0, getLongitud());
    }

    @Override
    public boolean isNumerico() {
        return false;
    }

    @Override
    public int getEnteros() {
        return 0;
    }

    @Override
    public int getDecimales() {
        return 0;
    }
}
