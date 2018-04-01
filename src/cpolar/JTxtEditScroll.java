package cpolar;
// JTxtEditScroll.java hiba


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// A JTxtEditScroll JPanel uzeneteit kezeli
class CTxtEditScrollListener implements AdjustmentListener, IMFActionListener
{
  /**
   * @uml.property  name="m_jJTxtEditScroll"
   * @uml.associationEnd  multiplicity="(1 1)" inverse="m_jTxtEditScrollListener:JTxtEditScroll"
   */
  protected JTxtEditScroll m_jJTxtEditScroll = null ;
  
  /**
   * @uml.property  name="m_nElozoValue"
   */
  protected int m_nElozoValue = 0 ;
  
  public CTxtEditScrollListener( JTxtEditScroll iniTxtEditScroll)
  {
    m_jJTxtEditScroll = iniTxtEditScroll ;
  }

  /**
   * Invoked when the value of the adjustable has changed.
   */
  public void adjustmentValueChanged( AdjustmentEvent cAdjustmentEvent)
  {
    int nValue    = 0 ;
//    int nEltTav   = 0 ;
    int nEdzSzama = 0 ;

    int nOsszElt = 0 ; // <-
    
//    float   fEltTav = 0.0f  ;
    boolean bRC     = false ;
    
System.out.println( "CEgyListener::adjustmentValueChanged() AdjustmentEvent.getValue()=" + cAdjustmentEvent.getValue()) ;
System.out.println( "CEgyListener::adjustmentValueChanged() AdjustmentEvent.paramString()=" + cAdjustmentEvent.paramString()) ;
    if ( m_jJTxtEditScroll != null )
    {
      // Mivel a <, >-nal nincs a gomboknak allapota, celszeru lesz itt is ezt
      // kovetni : getValue() -> getAdjustmentType()
      // AdjustmentEvent.getAdjustmentType() is hasznalhatatlan, mert mindig AdjustmentEvent.TRACK jon ...
      // JScrollBar.java / ModelListener there's no way to determine the proper type of the AdjustmentEvent
      nValue = cAdjustmentEvent.getValue() ;
      
//      m_cPolar.m_cJDiagram.m_cSurface.validate() ; ???
      nEdzSzama = m_jJTxtEditScroll.GetEdzSzama() ;

      // Az eltolas a megfelelo HRMFile-ba valo beirasa
      if ( IMFActionListener.m_cPolar != null &&
           IMFActionListener.m_cPolar.m_aHRMFile != null &&
           IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama] != null &&
           IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama].GetKijelzendo() == true )
      {
        if ( m_nElozoValue - nValue > 0 )
        {
          nOsszElt = IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama].GetEltTav() - (int)(36000.0f*(JTxtEditScroll.m_fKmPerLepes)) ;
          System.out.println( "CTxtEditScrollListener::adjustmentValueChanged(int) + nOsszElt=" + nOsszElt) ;

          bRC = IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama].EltTavValt( nOsszElt) ;
        }
        else
        {
          if ( m_nElozoValue - nValue < 0 )
          {
            nOsszElt = IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama].GetEltTav() + (int)(36000.0f*(JTxtEditScroll.m_fKmPerLepes)) ;
            System.out.println( "CTxtEditScrollListener::adjustmentValueChanged(int) - nOsszElt=" + nOsszElt) ;

            bRC = IMFActionListener.m_cPolar.m_aHRMFile[nEdzSzama].EltTavValt( nOsszElt) ;
          }
        }
	
        // Ha az eltolas meg a 0..maxtav hatarok koze esik ...
        if ( bRC == true )
        {
// nAdjustValue = -1 az elso nyomas utan, felulirja a >-val bevitt erteket *********************************
//persze, mert minden ^ nyomasra 1,2,3,4,... szamlal, fuggetlenul az edit resz tartalmatol,
//mert az Ad.Evnt.::SetValue() nincs utanaallitva 
          m_jJTxtEditScroll.SetElt( nOsszElt/36000.0f) ;
          
          m_nElozoValue = nValue ;
        }
      }
      else
      {
        // Ha nincs ilyen edzes (mert pl. be lett csukva) biztonsag kedveert nullazom.
        m_jJTxtEditScroll.SetElt( 0) ;
      }

      m_cPolar.m_cJDiagram.m_cSurface.repaint() ;
    }
  }
  
  public void actionPerformed( ActionEvent e)
  {
  }
}

/**
 * A betoltott edzesek egymashoz kepesti elcsusztatasat
 */
public class JTxtEditScroll extends Box
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152503L ;

  // Az elcsusztatas 10 m-enkent tortenjen, negativ, mert a felfele nyilra csokkenti
  // a ScrollBar az erteket
  public static final float m_fKmPerLepes = -0.01f ;
  
  /**
   * @uml.property  name="m_nMinScrBarLepes"
   */
  private final int  m_nMinScrBarLepes = -10000 ;
  /**
   * @uml.property  name="m_nMaxScrBarLepes"
   */
  private final int  m_nMaxScrBarLepes =  10000 ;
  
  // A diagram alatti gombok a diagram gorditesere ill. nyuzsoritasara
  /**
   * @uml.property  name="m_jEdzSzamaLabel"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JLabel     m_jEdzSzamaLabel = null ;
  /**
   * @uml.property  name="m_jEdzEltTxtFld"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JTextField m_jEdzEltTxtFld  = null ;
  /**
   * @uml.property  name="m_jEdzEltScrBar"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JScrollBar m_jEdzEltScrBar  = null ;
//    public JScrollBar(int orientation, int value, int extent, int min, int max)
  
  // A listenert el kell menteni, mert az esemenykezelo agban allitani akarom az
  // erteket, mely valtozasa kivaltja az esemenyt : a listenert kinullazom
  /**
   * @uml.property  name="m_jTxtEditScrollListener"
   * @uml.associationEnd  multiplicity="(1 1)" inverse="m_jJTxtEditScroll:CTxtEditScrollListener"
   */
  protected CTxtEditScrollListener m_jTxtEditScrollListener = null ;

  /**
   * @uml.property  name="m_nEdzSzama"
   */
  protected int   m_nEdzSzama = 0 ;
  /**
   * @uml.property  name="m_fEdzEltKm"
   */
  protected float m_fEdzEltKm = 0.0f ;
//  protected float m_fEltLepes = 0.0f ;
  
  public JTxtEditScroll( int nIniEdzSzama, float fIniEdzEltKm/*, float fIniEltLepes*/)
  {
    super( BoxLayout.X_AXIS) ;
    
    m_nEdzSzama = nIniEdzSzama ;
    m_jEdzSzamaLabel = new JLabel( "  " + Integer.toString( m_nEdzSzama) + " :", JLabel.LEFT) ;
    
    m_fEdzEltKm = fIniEdzEltKm ;
    m_jEdzEltTxtFld = new JTextField( Float.toString( m_fEdzEltKm)) ;
    
//    m_fEltLepes     = fIniEltLepes ;
    
    // The "extent" is the size of the viewable area. It is also known as the "visible amount".
    m_jEdzEltScrBar = new JScrollBar( JScrollBar.VERTICAL,              // orientation
                                      (int)(m_fEdzEltKm/m_fKmPerLepes), // value
                                      1,                                // extent
                                      m_nMinScrBarLepes,                // min
                                      m_nMaxScrBarLepes) ;              // max
    
    m_jEdzSzamaLabel.setMaximumSize(   new Dimension( 40, 20)) ;
    m_jEdzSzamaLabel.setMinimumSize(   new Dimension( 10, 20)) ;
    m_jEdzSzamaLabel.setPreferredSize( new Dimension( 30, 20)) ;
    add( m_jEdzSzamaLabel) ;

    m_jEdzEltTxtFld.setMaximumSize(   new Dimension( 100, 20)) ;
    m_jEdzEltTxtFld.setMinimumSize(   new Dimension(  20, 20)) ;
    m_jEdzEltTxtFld.setPreferredSize( new Dimension(  50, 20)) ;
    add( m_jEdzEltTxtFld) ;

    m_jEdzEltScrBar.setMaximumSize(   new Dimension( 25, 20)) ;
    m_jEdzEltScrBar.setMinimumSize(   new Dimension( 10, 20)) ;
    m_jEdzEltScrBar.setPreferredSize( new Dimension( 20, 20)) ;
    
    m_jTxtEditScrollListener = new CTxtEditScrollListener( this) ;
    
    m_jEdzEltScrBar.addAdjustmentListener( m_jTxtEditScrollListener) ;
    
    add( m_jEdzEltScrBar) ;
  }

  public void SetElt( int nIniEdzElt)
  {
System.out.println( "JTxtEditScroll::SetElt(int) AdjustmentEvent.paramString()=" + nIniEdzElt) ;
System.out.println( "JTxtEditScroll::SetElt(int) m_fKmPerLepes=" + m_fKmPerLepes) ;
    m_fEdzEltKm = m_fKmPerLepes*nIniEdzElt ;
System.out.println( "JTxtEditScroll::SetElt(int) m_fEdzEltKm=" + m_fEdzEltKm) ;
    m_jEdzEltTxtFld.setText( Float.toString( m_fEdzEltKm)) ;
  }

  public void SetElt( float fIniEdzElt)
  {
System.out.println( "JTxtEditScroll::SetElt(float) fIniEdzElt=" + fIniEdzElt) ;
System.out.println( "JTxtEditScroll::SetElt(float) elotte : m_fEdzEltKm=" + m_fEdzEltKm) ;

    m_fEdzEltKm = fIniEdzElt ;

    m_jEdzEltTxtFld.setText( Float.toString( m_fEdzEltKm)) ;

// m_jEdzEltScrBar.setValueIsAdjusting( true) ; ez nem gatolja meg a setValue() altal generalt vegtelen ciklust !
    m_jEdzEltScrBar.removeAdjustmentListener( m_jTxtEditScrollListener) ;
    
System.out.println( "JTxtEditScroll::SetElt(float) (int)(m_fEdzEltKm/m_fKmPerLepes)=" + (int)(m_fEdzEltKm/m_fKmPerLepes)) ;
    m_jEdzEltScrBar.setValue( (int)(m_fEdzEltKm/m_fKmPerLepes)) ;

    m_jEdzEltScrBar.addAdjustmentListener( m_jTxtEditScrollListener) ;

//  m_jEdzEltScrBar.setValueIsAdjusting( false) ;
  }
  
  public float GetElt()
  {
    return m_fEdzEltKm ;
  }
  
  public int GetEdzSzama()
  {
    return m_nEdzSzama ;
  }
  
/*
  protected void MeretBeallit( Dimension cIniMinMeret, Dimension cIniMaxMeret, Dimension cIniPrefMeret)
  {
    if ( cIniMinMeret == null || cIniMaxMeret == null || cIniPrefMeret == null )
    {
      return ;
    }

    
  }
*/

//----------------------------------------------------------------------------
  public static void main(String s[])
  {
    final JTxtEditScroll JTxtEditScrollTeszt = new JTxtEditScroll( 1, 0.0f/*, 0.1f*/) ;

    WindowListener cWindowListener = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {System.exit(0);}
//      public void windowDeiconified(WindowEvent e) { demo.surf.start(); }
//      public void windowIconified(WindowEvent e) { demo.surf.stop(); }
    };
    
    JFrame f = new JFrame("A JDiagram osztalyt (a diagramot) gorgeto/nagyito-kicsinyito gombok");

    f.addWindowListener( cWindowListener) ;
    f.getContentPane().add("Center", JTxtEditScrollTeszt) ;
    f.pack() ;
    f.setSize(new Dimension(200,200)) ;
    f.setVisible(true)  ;
  }
}