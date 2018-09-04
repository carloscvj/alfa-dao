package es.cvjpy.cobol;

public class CobolException extends Exception {

    private final String er;
    private final String fichero;
    private final String exterior;
    private final String orden;

    public CobolException(String er, String fichero, String exterior, String orden) {
        super("Error:" + er + " en el fichero:" + fichero + " llamado:" + exterior + " al hacer:" + orden);
        this.er = er;
        this.fichero = fichero;
        this.exterior = exterior;
        this.orden = orden;
    }

    public CobolException(String men) {
        super(men);
        this.er = "";
        this.fichero = "";
        this.exterior = "";
        this.orden = "";
    }

    public String getEr() {
        return er;
    }

    public String getExterior() {
        return exterior;
    }

    public String getFichero() {
        return fichero;
    }

    public String getOrden() {
        return orden;
    }
}
