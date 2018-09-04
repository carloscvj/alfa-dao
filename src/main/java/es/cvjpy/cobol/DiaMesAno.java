package es.cvjpy.cobol;

import java.text.*;
import java.util.*;

public class DiaMesAno extends Campo {

    private Date midate = new Date();

    public DiaMesAno(Registro registro, String nombre, int inicio) {
        super(registro, nombre, inicio, 8);
    }

    void loadFromRegistro() {
        String f = new String(getRegistro().saca(getInicio(), getLongitud()));
        Date mifecha = new Date();
        mifecha.setTime(0L);
        if (f.equals("") || f.equals("00000000")) {
            mifecha = null;
        } else {
            try {
                mifecha = new SimpleDateFormat("ddMMyyyy").parse(f);
            } catch (ParseException ex) {
                mifecha.setTime(-999999999999L);
            }
        }
        midate = mifecha;
    }

    void storeToRegistro() {
        String s;
        if (midate == null) {
            s = "00000000";
        } else {
            s = new SimpleDateFormat("ddMMyyyy").format(midate);
        }
        getRegistro().mete(s.toCharArray(), getInicio());
    }

    public Date get() {
        loadFromRegistro();
        return midate;
    }

    public void set(Date midate) {
        this.midate = midate;
        storeToRegistro();
    }
}
