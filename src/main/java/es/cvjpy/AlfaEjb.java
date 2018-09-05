
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import es.cvjpy.aplicacion.ConfiguracionPro;
import es.cvjpy.aplicacion.ConfiguracionAlfaEjb;
import es.cvjpy.aplicacion.ConfiguracionAlfaPro;
import es.cvjpy.aplicacion.PreferenciasPro;
import es.cvjpy.aplicacion.PreferenciasAlfaEjb;
import es.cvjpy.aplicacion.PreferenciasAlfaPro;
import es.cvjpy.aplicacion.SalirPro;
import es.cvjpy.aplicacion.SalirAlfaEjb;
import es.cvjpy.aplicacion.SalirAlfaPro;
import es.cvjpy.cobol.utilidades.ReconstruirIdxAlfaEjb;
import es.cvjpy.cobol.utilidades.ReconstruirIdxAlfaPro;
import es.cvjpy.cobol.utilidades.ReconstruirIdxGenAlfaEjb;
import es.cvjpy.cobol.utilidades.ReconstruirIdxGenAlfaPro;
import es.cvjpy.cobol.utilidades.ReconstruirIdxGenPro;
import es.cvjpy.cobol.utilidades.ReconstruirIdxPro;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author carlos
 */
@Stateless
public class AlfaEjb extends PrincipalCBLAbs implements AlfaPro, PrincipalCBLPro {

    @EJB
    private SalirAlfaPro salirAlfaEjb;
    @EJB
    private PreferenciasAlfaPro preferenciasAlfaEjb;
    @EJB
    private ConfiguracionAlfaPro configuracionAlfaEjb;
    @EJB
    private TextoPlanoAlfaPro textoPlanoAlfaEjb;
    @EJB
    private ReconstruirIdxAlfaPro reconstruirIdxAlfaEjb;
    @EJB
    private ReconstruirIdxGenAlfaPro reconstruirIdxGenAlfaEjb;

    @Override
    protected String dondeEstaElExe() {
        String toExternalForm = null;
        URL resource = getClass().getResource("/exe/srv/SRU.int");
        if (resource != null) {
            toExternalForm = resource.getPath();
            Logger.getLogger(ConfiguracionPro.class.getName()).log(Level.INFO, "URL de SRU.int pero desde el EJB:{0}", toExternalForm);
            int pos = toExternalForm.indexOf("/exe");
            toExternalForm = toExternalForm.substring(0, pos) + "/exe";
        }
        return toExternalForm;
    }

    @Override
    public Object getProgramaServidor(Class cachoPro) throws Exception {
        Object ret = null;

        if (cachoPro.equals(ConfiguracionPro.class)) {
            ret = getConfiguracionPro();
            return ret;
        }
        if (cachoPro.equals(PreferenciasPro.class)) {
            ret = getPreferenciasPro();
            return ret;
        }
        if (cachoPro.equals(SalirPro.class)) {
            ret = getSalirPro();
            return ret;
        }
        if (cachoPro.equals(TextoPlanoPro.class)) {
            ret = getTextoPlanoPro();
            return ret;
        }

        if (cachoPro.equals(ReconstruirIdxPro.class)) {
            ret = getReconstruirIdxPro();
            return ret;
        }
        if (cachoPro.equals(ReconstruirIdxGenPro.class)) {
            ret = getReconstruirIdxGenPro();
            return ret;
        }

        if (ret == null) {
            throw new Exception("No está en el servidor el EJB para:" + cachoPro);
        }
        return ret;
    }

    @Override
    public ConfiguracionPro getConfiguracionPro() {
        if (configuracionAlfaEjb == null) {
            configuracionAlfaEjb = new ConfiguracionAlfaEjb(this);
        }
        return configuracionAlfaEjb;
    }

    @Override
    public PreferenciasPro getPreferenciasPro() {
        if (preferenciasAlfaEjb == null) {
            preferenciasAlfaEjb = new PreferenciasAlfaEjb(this);
        }
        return preferenciasAlfaEjb;
    }

    @Override
    public SalirPro getSalirPro() {
        if (salirAlfaEjb == null) {
            salirAlfaEjb = new SalirAlfaEjb(this);
        }
        return salirAlfaEjb;
    }

    @Override
    public TextoPlanoPro getTextoPlanoPro() {
        if (textoPlanoAlfaEjb == null) {
            textoPlanoAlfaEjb = new TextoPlanoAlfaEjb(this);
        }
        return textoPlanoAlfaEjb;
    }

    @Override
    public ReconstruirIdxPro getReconstruirIdxPro() {
        if (reconstruirIdxAlfaEjb == null) {
            reconstruirIdxAlfaEjb = new ReconstruirIdxAlfaEjb(this);
        }
        return reconstruirIdxAlfaEjb;
    }

    @Override
    public ReconstruirIdxGenPro getReconstruirIdxGenPro() {
        if (reconstruirIdxGenAlfaEjb == null) {
            reconstruirIdxGenAlfaEjb = new ReconstruirIdxGenAlfaEjb(this);
        }
        return reconstruirIdxGenAlfaEjb;
    }

}
