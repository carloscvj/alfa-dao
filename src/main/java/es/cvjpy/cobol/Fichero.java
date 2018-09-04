package es.cvjpy.cobol;

public class Fichero implements Ficherable {

    private boolean iniciado = false;
    private boolean abierto = false;
    private boolean invalidkey = false;
    private boolean bloquear = false;
    private ProcesoCobolPro enlace;
    private Registro registro;
    private String nombreExterno;
    private final String nombreInterno;
    private final String programaServidor;
    private String orden = "";
    //private String mensajecargado;
    private boolean trazable = false;

    public Fichero(String nombreInterno, String nombreExterno, String programaServidor) {
        this.nombreInterno = nombreInterno;
        this.nombreExterno = nombreExterno;
        this.programaServidor = programaServidor;
    }

    private ProcesoCobolPro getEnlace() {
        return enlace;
    }

    private void inicia() throws Exception {
        iniciado = true;
    }

    private synchronized void open() throws Exception {
        if (bloquear) {
            open_i_o();
        } else {
            open_input();
        }
        abierto = true;
    }

    private synchronized void open_i_o() throws Exception {
        orden = "OIO";
        enviar(orden + ProcesoCobolPro.separar);
        controlerror(getErrores());
    }

    private synchronized void open_input() throws Exception {
        orden = "OIN";
        enviar(orden + ProcesoCobolPro.separar);
        controlerror(getErrores());
    }

    private synchronized void open_output() throws Exception {
        orden = "OOU";
        enviar(orden + ProcesoCobolPro.separar);
        controlerror(getErrores());
    }

    @Override
    public synchronized void OPEN_I_O() throws Exception {
        if (abierto) {
            this.CLOSE();
        }
        this.open_i_o();
        this.abierto = true;
    }

    @Override
    public synchronized void OPEN_OUTPUT() throws Exception {
        if (abierto) {
            this.CLOSE();
        }
        this.open_output();
        this.abierto = true;
    }

    private synchronized void enviar(String fun) throws Exception {
        if (!iniciado) {
            inicia();
        }
        getEnlace().enviar("LLA" + ProcesoCobolPro.separar + programaServidor + ProcesoCobolPro.separar);
        getEnlace().enviar(fun);
    }

    private synchronized void trazar(String traza) throws Exception {
        if (!iniciado) {
            inicia();
        }
    }

    private synchronized String recibir() throws Exception {
        return getEnlace().recibir();
    }

    private synchronized void controlerror(String er) throws Exception {
        if (er.length() > 0) {
            if (!(er.substring(0, 1).equals("0"))) {
                throw new CobolException(er, nombreInterno, nombreExterno, orden);
            }
        }
    }

    private synchronized boolean stob(String s) throws Exception {
        boolean b = false;
        if (s.equalsIgnoreCase("2")) {
            controlerror(getErrores());
        }
        if (s.equalsIgnoreCase("1")) {
            b = true;
        }
        return b;
    }

    protected synchronized void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @Override
    public synchronized Registrable getRegistro() {
        return registro;
    }

    @Override
    public synchronized void setEnlace(ProcesoCobolPro enlace) {
        this.enlace = enlace;

    }

    @Override
    public synchronized String getProgramaServidor() {
        return programaServidor;
    }

    private void setInvalidkey(boolean sino) {
        this.invalidkey = sino;
    }

    @Override
    public synchronized boolean isInvalidkey() {
        return invalidkey;
    }

    @Override
    public synchronized String getNombreInterno() {
        return this.nombreInterno;
    }

    @Override
    public synchronized void setNombreExterno(String nombreexterno) throws Exception {
        if (abierto) {
            this.CLOSE();
        }
        this.nombreExterno = nombreexterno;
        orden = "SNO";
        enviar(orden + ProcesoCobolPro.separar + nombreExterno);
        controlerror(getErrores());

    }

    @Override
    public synchronized String getNombreExterno() {
        return nombreExterno;
    }

    @Override
    public synchronized String getErrores() throws Exception {
        enviar("GER" + ProcesoCobolPro.separar);
        return (recibir());
    }

    @Override
    public synchronized void setBloquear(boolean sino) throws Exception {
        if (abierto) {
            this.CLOSE();
        }
        this.bloquear = sino;
        if (isBloquear()) {
            enviar("SIL" + ProcesoCobolPro.separar + "0");
        } else {
            enviar("SIL" + ProcesoCobolPro.separar + "1");
        }
    }

    @Override
    public synchronized boolean isBloquear() {
        return bloquear;
    }

    @Override
    public synchronized boolean READ() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "REA";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar);
            String rec = recibir();
            registro.pararecibir(rec);
        }
        return isInvalidkey();
    }

    @Override
    public synchronized boolean READ_KEY_IS(KeyCobol key) throws Exception {
        if (!abierto) {
            this.open();
        }
        String par = key.getNombre().toUpperCase();
        orden = "REAK" + par;
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar);
            registro.pararecibir(recibir());
        }
        return isInvalidkey();
    }

    @Override
    public synchronized boolean WRITE() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "WRI";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        boolean ret = isInvalidkey();
        if (!ret) {
            trazar("ALTA:" + registro.paraenviar());
        }
        return ret;
    }

    @Override
    public synchronized boolean REWRITE() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "REW";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        boolean ret = isInvalidkey();
        if (!ret) {
            trazar("MODI:" + registro.paraenviar());
        }
        return ret;
    }

    @Override
    public synchronized boolean DELETE() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "DEL";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        boolean ret = isInvalidkey();
        if (!ret) {
            trazar("BAJA:" + registro.paraenviar());
        }
        return ret;
    }

    @Override
    public synchronized boolean START_MENOR() throws Exception {
        if (!abierto) {
            this.open();
        }

        orden = "ST<";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_MENOR_KEY_IS(KeyCobol key) throws Exception {
        if (!abierto) {
            this.open();
        }
        String par = key.getNombre().toUpperCase();
        orden = "ST<K" + par;
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_MAYOR() throws Exception {
        if (!abierto) {
            this.open();
        }

        orden = "ST>";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_MAYOR_KEY_IS(KeyCobol key) throws Exception {
        if (!abierto) {
            this.open();
        }
        String par = key.getNombre().toUpperCase();
        orden = "ST>K" + par;
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_NO_MENOR() throws Exception {
        if (!abierto) {
            this.open();
        }

        orden = "STN<";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_NO_MENOR_KEY_IS(KeyCobol key) throws Exception {
        if (!abierto) {
            this.open();
        }
        String par = key.getNombre().toUpperCase();
        orden = "STN<K" + par;
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_NO_MAYOR() throws Exception {
        if (!abierto) {
            this.open();
        }

        orden = "STN>";
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean START_NO_MAYOR_KEY_IS(KeyCobol key) throws Exception {
        if (!abierto) {
            this.open();
        }
        String par = key.getNombre().toUpperCase();
        orden = "STN>K" + par;
        enviar("SETALL" + ProcesoCobolPro.separar + registro.paraenviar() + ProcesoCobolPro.separar);
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        return isInvalidkey();
    }

    @Override
    public synchronized boolean NEXT() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "NEX";
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar);
            registro.pararecibir(recibir());
        }
        return isInvalidkey();
    }

    @Override
    public synchronized boolean NEXT(String condicion) throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "NEXC";
        enviar(orden + ProcesoCobolPro.separar + condicion + "|BUS|" + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar);
            registro.pararecibir(recibir());
        }
        return isInvalidkey();
    }

    @Override
    public synchronized boolean PREVIOUS() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "PRE";
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar);
            registro.pararecibir(recibir());
        }
        return isInvalidkey();
    }

    @Override
    public synchronized boolean PREVIOUS(String condicion) throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "PREC";
        enviar(orden + ProcesoCobolPro.separar);
        setInvalidkey(stob(recibir()));
        if (!isInvalidkey()) {
            enviar("GETALL" + ProcesoCobolPro.separar + condicion + "|BUS|" + ProcesoCobolPro.separar);
            registro.pararecibir(recibir());
        }
        return isInvalidkey();
    }

    @Override
    public synchronized void CLOSE() throws Exception {
        if (!abierto) {
            this.open();
        }
        orden = "CLO";
        enviar(orden + ProcesoCobolPro.separar);
        controlerror(getErrores());
        abierto = false;
    }

    public boolean isTrazable() {
        return trazable;
    }

    public void setTrazable(boolean trazable) {
        this.trazable = trazable;
    }
}
