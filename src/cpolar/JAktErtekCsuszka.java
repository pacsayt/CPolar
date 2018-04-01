package cpolar;
// JAktErtekCsuszka.java
// Az edzesfile-ok pillanatnyi ertekenek megjelenitesekor ezzel a csuszkaval
// lehet a tavolsagot definialni, csuszkalni a file-on belul.

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
* Az edzesfile-ok pillanatnyi ertekenek megjelenitesekor ezzel a csuszkaval
* lehet a tavolsagot definialni, csuszkalni a file-on belul.
*/
public class JAktErtekCsuszka extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152496L ;

  // Ezt a kontrolt tudja a nyuzer jobbra-balra tologatni, aszerint, hogy hol
  // kivancsi az edzesfajlok parametereinek pillanatnyi ertekere
  /**
   * @uml.property  name="m_cCsuszka"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JSlider m_cCsuszka = null ;
  
  // A vizszintes csuszka szelessege bedrotozva a BasicSliderUI::getThumbSize()-ba
  public final static int m_nThumbWidthH = 11 ;

  public JAktErtekCsuszka()
  {
//System.out.println( "JAktErtekCsuszka::JAktErtekCsuszka() BEGIN") ;

     // A GridLayout nem veszi figyelembe a "preferred size"-t
     setLayout( new BoxLayout( this, BoxLayout.X_AXIS)) ;
     
     // Hogy a csuszka csucsa pont a racs bal szeletol menjen
     // A jobb szelehez valo illeszteshez a racsot kell beljebb hozni
     add( Box.createRigidArea( new Dimension( JDiagram.Surface.m_nGrafBalszele - m_nThumbWidthH/2, 20))) ;
     
//     add( Box.createHorizontalGlue()) ;

     m_cCsuszka = new JSlider() ;
     
     if ( m_cCsuszka != null )
     {
       m_cCsuszka.setValue( 0) ;
       
       add( m_cCsuszka) ;
     }
  }

  public void addChangeListener( ChangeListener cChangeListener)
  {
    if ( m_cCsuszka != null )
    {
      m_cCsuszka.addChangeListener( cChangeListener) ;
    }
  }

  public void Init( int nIniMinimum, int nIniMaximum, int nIniValue)
  {
//System.out.println( "JAktErtekCsuszka::Init() nIniMinimum=" + nIniMinimum + " nIniMaximum=" + nIniMaximum + " nIniValue=" + nIniValue) ;
    
    if ( m_cCsuszka != null )
    {
      m_cCsuszka.setMinimum( nIniMinimum) ;
      m_cCsuszka.setMaximum( nIniMaximum) ;
      m_cCsuszka.setValue( nIniValue) ;
    }
  }
  
  public int GetPozicio()
  {
    int nPozicio = 0 ;
    
    if ( m_cCsuszka != null )
    {
      nPozicio = m_cCsuszka.getValue() ;
    }
 
    return nPozicio ;
  }
  
  public void SetPozicio( int nIniPozicio)
  {
//System.out.println( "JAktErtekCsuszka::SetPozicio() nIniPozicio=" + nIniPozicio) ;

    if ( m_cCsuszka != null )
    {
      m_cCsuszka.setValue( nIniPozicio) ;
    }    
  }
  
  public int GetMinimum()
  {
    int nMinimum = 0 ;
    
    if ( m_cCsuszka != null )
    {
      nMinimum = m_cCsuszka.getMinimum() ;
    }
 
    return nMinimum ;
  }

  public void SetMinimum( int nIniMinimum)
  {
//System.out.println( "JAktErtekCsuszka::SetMinimum() nIniMinimum=" + nIniMinimum) ;

    if ( m_cCsuszka != null )
    {
      m_cCsuszka.setMinimum( nIniMinimum) ;
    }    
  }

  public int GetMaximum()
  {
    int nMaximum = 0 ;
    
    if ( m_cCsuszka != null )
    {
      nMaximum = m_cCsuszka.getMaximum() ;
    }
 
    return nMaximum ;
  }

  public void SetMaximum( int nIniMaximum)
  {
//System.out.println( "JAktErtekCsuszka::SetMaximum() nIniMaximum=" + nIniMaximum) ;

    if ( m_cCsuszka != null )
    {
      m_cCsuszka.setMaximum( nIniMaximum) ;
    }    
  }
}
