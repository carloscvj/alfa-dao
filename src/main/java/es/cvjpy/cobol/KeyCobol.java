
package es.cvjpy.cobol;

import java.util.Iterator;

public interface KeyCobol extends CobolTransferible {
  
  public String getNombre();
  
  public CampoCobol getCampo(String nombre);
  
  public Iterator getCamposIterator();
  
}
