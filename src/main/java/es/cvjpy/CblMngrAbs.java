/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import es.cvjpy.cobol.CampoCobol;
import es.cvjpy.cobol.Ficherable;
import es.cvjpy.cobol.KeyCobol;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 * @param <COSA>
 */
public abstract class CblMngrAbs<COSA> implements MngrPro<COSA> {

    protected int lineasPorPagina = 64;
    private Clasificado clasificado;
    private String buscar;
    private COSA voyPor;
    private List<COSA> lista;
    private boolean nuevo;

    private List<COSA> getLista() {
        if (lista == null) {
            lista = new ArrayList();
        }
        return lista;
    }

    private void setLista(List<COSA> lista) {
        this.lista = lista;
    }

    private int meteEnLista(int yavan) throws Exception {
        COSA cosa = (COSA) recuperaParaBuscar();
        if (yavan == 0) {
            setLista(null);
        }
        getLista().add(cosa);
        yavan++;
        return yavan;
    }

    protected abstract boolean filtrando() throws Exception;

    protected abstract Ficherable getNavegante() throws Exception;

    protected abstract Clasificado crearClasificado();

    protected abstract COSA recupera() throws Exception;

    protected abstract COSA recuperaParaBuscar() throws Exception;

    protected boolean seleccionando() throws Exception {
        return true;
    }

    protected boolean startPrimero() throws Exception {
        boolean ret;
        getNavegante().getRegistro().initAll();
        String nombreCampo = getClasificado().getCobolKey();
        CampoCobol campo = getNavegante().getRegistro().getCampo(nombreCampo);
        if (!getBuscar().equals("")) {
            campo.setCobolText(getBuscar());
        }
        KeyCobol alterKey = getNavegante().getRegistro().getAlterKey(nombreCampo);
        if (alterKey == null) {
            ret = !getNavegante().START_NO_MENOR();
        } else {
            ret = !getNavegante().START_NO_MENOR_KEY_IS(alterKey);
        }
        return ret;
    }

    protected boolean startUltimo() throws Exception {
        boolean ret;
        getNavegante().getRegistro().lastAll();
        String nombreCampo = getClasificado().getCobolKey();
        CampoCobol campo = getNavegante().getRegistro().getCampo(nombreCampo);
        if (!getBuscar().equals("")) {
            campo.setCobolText(getBuscar());
        }
        KeyCobol alterKey = getNavegante().getRegistro().getAlterKey(nombreCampo);
        if (alterKey == null) {
            ret = !getNavegante().START_NO_MAYOR();
        } else {
            ret = !getNavegante().START_NO_MAYOR_KEY_IS(alterKey);
        }
        return ret;
    }

    protected boolean next() throws Exception {
        boolean ret = false;
        if (!getNavegante().NEXT()) {
            ret = filtrando();
        }
        return ret;
    }

    protected boolean previous() throws Exception {
        boolean ret = false;
        if (!getNavegante().PREVIOUS()) {
            ret = filtrando();
        }
        return ret;
    }

    protected COSA getVoyPor() throws Exception {
        if (voyPor == null) {
            voyPor = nuevo();
        }
        return voyPor;
    }

    protected void setVoyPor(COSA voyPor) {
        this.voyPor = voyPor;
    }

    protected Clasificado getClasificado() {
        if (clasificado == null) {
            clasificado = crearClasificado();
        }
        return clasificado;
    }

    protected String getBuscar() {
        if (buscar == null) {
            buscar = "";
        }
        return buscar;
    }

    protected boolean existeId(Object id) throws Exception {
        boolean ret = true;
        if (getEntidad(id) == null) {
            ret = false;
        }
        return ret;
    }

    @Override
    public List<COSA> paginaPrimera() throws Exception {
        int yavan = 0;
        if (startPrimero()) {
            while (next()) {
                if (seleccionando()) {
                    yavan = meteEnLista(yavan);
                    if (yavan > lineasPorPagina) {
                        break;
                    }
                }
            }
        }
        return getLista();
    }

    @Override
    public List<COSA> paginaAnterior() throws Exception {
        int yavan = 0;
        while (previous()) {
            if (seleccionando()) {
                yavan++;
                if (yavan >= ((lineasPorPagina * 2) + 1)) {
                    break;
                }
            }
        }
        return paginaSiguiente();
    }

    @Override
    public List<COSA> paginaSiguiente() throws Exception {
        int yavan = 0;
        while (next()) {
            if (seleccionando()) {
                yavan = meteEnLista(yavan);
                if (yavan > lineasPorPagina) {
                    break;
                }
            }
        }
        return getLista();
    }

    @Override
    public List<COSA> paginaUltima() throws Exception {
        int yavan = 0;
        if (startUltimo()) {
            while (previous()) {
                if (seleccionando()) {
                    yavan++;
                    if (yavan >= lineasPorPagina) {
                        break;
                    }
                }
            }
        }
        return paginaSiguiente();
    }

    @Override
    public void relista(Clasificado clasificarPorSelected, String buscar) throws Exception {
        this.clasificado = clasificarPorSelected;
        this.buscar = buscar;
        this.lista = null;
    }

    @Override
    public void grabarEntidad(COSA cosa) throws Exception {
    }

    @Override
    public void borrarEntidad(COSA cosa) throws Exception {
        if (cosa != null) {
            COSA otra = recupera();
            if (otra != null) {
            }
        }
    }

    @Override
    public void confirmar() throws Exception {
        setNuevo(false);
    }

    @Override
    public COSA primero() throws Exception {
        setNuevo(false);
        Ficherable fichero = getNavegante();
        fichero.getRegistro().initAll();
        if (!fichero.START_NO_MENOR()) {
            while (!fichero.NEXT()) {
                if (seleccionando()) {
                    setVoyPor(recupera());
                    break;
                }
            }
        }
        return getVoyPor();
    }

    @Override
    public COSA anterior() throws Exception {
        setNuevo(false);
        while (!getNavegante().PREVIOUS()) {
            if (seleccionando()) {
                setVoyPor(recupera());
                break;
            }
        }
        return getVoyPor();
    }

    @Override
    public COSA siguiente() throws Exception {
        setNuevo(false);
        while (!getNavegante().NEXT()) {
            if (seleccionando()) {
                setVoyPor(recupera());
                break;
            }
        }
        return getVoyPor();
    }

    @Override
    public COSA ultimo() throws Exception {
        setNuevo(false);
        Ficherable fichero = getNavegante();
        fichero.getRegistro().lastAll();
        if (!fichero.START_NO_MAYOR()) {
            while (!fichero.PREVIOUS()) {
                if (seleccionando()) {
                    setVoyPor(recupera());
                    break;
                }
            }
        }
        return getVoyPor();
    }

    @Override
    public void grabarAmbos(COSA cosa) throws Exception {
        grabarEntidad(cosa);
        grabarEnCobol(cosa);
    }

    @Override
    public void borrarAmbos(COSA cosa) throws Exception {
        borrarEntidad(cosa);
        borrarEnCobol(cosa);
    }

    @Override
    public boolean isNuevo() {
        return nuevo;
    }

    @Override
    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }
}
