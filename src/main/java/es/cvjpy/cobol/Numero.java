package es.cvjpy.cobol;

import java.math.BigDecimal;

public class Numero extends Campo {

    private BigDecimal numero;
    private final int enteros;
    private final int decimales;
    private final boolean consigno;

    public Numero(Registro registro, String nombre, int inicio, boolean consigno, int enteros, int decimales) {
        super(registro, nombre, inicio, enteros + decimales + (consigno ? 1 : 0));
        this.numero = new BigDecimal(0);
        this.numero.setScale(decimales, BigDecimal.ROUND_HALF_UP);
        this.enteros = enteros;
        this.decimales = decimales;
        this.consigno = consigno;
    }

    private BigDecimal getNumero() {
        if (numero == null) {
            numero = new BigDecimal(0);
        }
        return numero;
    }

    void loadFromRegistro() {
        String texto = new String(getRegistro().saca(getInicio(), getLongitud()));
        StringBuilder tex = new StringBuilder(texto);
        int pos = enteros;
        if (consigno) {
            pos++;
        }
        if (decimales > 0) {
            tex.insert(pos, '.');
        }
        numero = new BigDecimal(0);
        try {
            numero = new BigDecimal(tex.toString());
        } catch (Exception ex) {
            numero = new BigDecimal(0);
        }
        getNumero().setScale(decimales, BigDecimal.ROUND_HALF_UP);
    }

    void storeToRegistro() {
        getRegistro().mete(relleno().toCharArray(), getInicio());
    }

    public BigDecimal get() {
        loadFromRegistro();
        return getNumero();
    }

    public void set(BigDecimal numero) {
        this.numero = numero;
        storeToRegistro();
    }

    public void set(Short numero) {
        this.numero = null;
        if (numero != null) {
            this.set(new BigDecimal(numero));
        }
    }

    public void set(Integer numero) {
        this.numero = null;
        if (numero != null) {
            this.set(new BigDecimal(numero));
        }
    }

    private String relleno() {
        StringBuilder cadena = new StringBuilder(getNumero().toString());
        if (decimales > 0) {
            if (cadena.indexOf(".") < 0) {
                cadena.append(".");
            }
        }
        cadena.append(repetir('0', decimales));
        StringBuffer ret;
        StringBuffer ent = repetir('0', enteros);
        StringBuffer dec = new StringBuffer("");
        StringBuffer signo = new StringBuffer("+");
        char c;
        for (int i = 0; i < cadena.length(); i++) {
            c = cadena.charAt(i);
            if (c == '.') {
                break;
            } else {
                if (c == '+' || c == '-') {
                    signo = new StringBuffer("" + c);
                } else {
                    ent.append(c);
                }
            }
        }
        for (int i = cadena.length() - 1; i > -1; i--) {
            c = cadena.charAt(i);
            if (c == ',' || c == '.') {
                break;
            } else {
                dec.insert(0, c);
            }
        }
        dec.append(repetir('0', decimales));
        ent = new StringBuffer(ent.substring(ent.length() - enteros));
        dec = new StringBuffer(dec.substring(0, decimales));
        ret = ent.append(dec);
        if (consigno) {
            ret.insert(0, signo);
        }
        return ret.toString();
    }

    private StringBuffer repetir(char caracter, int veces) {
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; i < veces; i++) {
            ret.append(caracter);
        }
        return ret;
    }

    @Override
    public boolean isNumerico() {
        return true;
    }

    @Override
    public int getEnteros() {
        return enteros;
    }

    @Override
    public int getDecimales() {
        return decimales;
    }

    @Override
    public void setCobolText(String texto) {
        String x = texto.trim().replace(',', '.');
        numero = new BigDecimal(0);
        try {
            numero = new BigDecimal(Double.parseDouble(x));
        } catch (NumberFormatException exc) {
        }
        storeToRegistro();
    }

}
