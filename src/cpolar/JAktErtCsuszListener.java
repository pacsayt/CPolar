package cpolar;
// JAktErtCsuszListener.java

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ChangeListener;

class JAktErtCsuszListener implements ChangeListener, IMFActionListener
{
  /**
   * @uml.property  name="m_nAktCsuszTav"
   */
  protected int m_nAktCsuszTav = 0 ;

  // Ez a valtozo mutatja meg, hogy a csuszka elmozditasa utan a valtoztatas a
  // kepernyon ki lett-e rajzolva
  /**
   * @uml.property  name="m_bPillErtKirajz"
   */
  protected boolean m_bPillErtKirajz = true ;

  /**
   * @uml.property  name="m_jSurface"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JDiagram.Surface m_jSurface = null ;
  
  public JAktErtCsuszListener( JDiagram.Surface jSurface)
  {
    m_jSurface = jSurface ;
  }
  
  public void stateChanged( ChangeEvent cChangeEvent)
  {
//    m_cPolar.m_nAktivEdzes
    JSlider cCsuszka = null ;
    
    cCsuszka = (JSlider)cChangeEvent.getSource() ;
    // java.lang.ClassCastException \/ 
    // cAktErtekCsuszka = (JAktErtekCsuszka)cChangeEvent.getSource() ;
    
    if ( cCsuszka != null )
    {
//System.out.println( "JAktErtCsuszListener::stateChanged() : cCsuszka.getValue()=" + cCsuszka.getValue()) ;
      m_nAktCsuszTav = cCsuszka.getValue() ;
      
      // Jelzi, hogy a csuszka tologatasa miatt kell a kepernyot frissiteni
      m_bPillErtKirajz = false ;
      
      // Igy frissiti a kepernyot !      
      m_jSurface.repaint() ;
//System.out.println( "JAktErtCsuszListener::stateChanged() : " + cChangeEvent.getClass().getName()) ;
    }
  }
  
  // Nem hasznalom, de kell az IMFActionListener miatt
  public void actionPerformed( ActionEvent e)
  {
  }
  
  public int GetCsuszTav()
  {
    return m_nAktCsuszTav ;
  }
  
  public boolean PillErtKirajz()
  {
    return m_bPillErtKirajz ;
  }
}