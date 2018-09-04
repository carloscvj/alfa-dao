
package es.cvjpy.cobol;

public interface CampoCobol extends CobolTransferible {
  
  public String getNombre();
  
  public int getLongitud();
  
  public boolean isNumerico();
  
  public int getEnteros();
  
  public int getDecimales();
  
}
