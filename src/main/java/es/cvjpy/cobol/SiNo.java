package es.cvjpy.cobol;

public class SiNo extends Campo {

    private Boolean misino;

    public SiNo(Registro registro, String nombre, int inicio, int longi) {
        super(registro, nombre, inicio, longi);
    }

    void loadFromRegistro() {
        String f = new String(getRegistro().saca(getInicio(), getLongitud()));
        if (f.equals("")) {
            misino = null;
        } else {
            misino = f.trim().equals("S");
        }
    }

    void storeToRegistro() {
        String s;
        if (misino == null) {
            s = " ";
        } else {
            s = "N";
            if (misino) {
                s = "S";
            }
        }
        getRegistro().mete(s.toCharArray(), getInicio());
    }

    public Boolean get() {
        loadFromRegistro();
        return misino;
    }

    public void set(Boolean misino) {
        this.misino = misino;
        storeToRegistro();
    }
}
