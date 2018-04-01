package cpolar;
// JParamValPanel.java
// A JDiagramParam hoz hozzaadva
// Az egyes intervallum adatok megjeleniteset szabalyozza (mert a JIntTimeTable atlathatatlanna valna)

//--- Kikommentezve, mert "... is never used"
//import java.lang.*;

import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.*;

// Csak mutatoba
class JParValCBListener implements IMFActionListener
{
  protected JParamValPanel m_jJParamValPanel = null ;
  
  public JParValCBListener( JParamValPanel aIniKijelzendo)
  {
    if ( aIniKijelzendo != null )
    {
      m_jJParamValPanel = aIniKijelzendo ;
    }
  }
  
  public void actionPerformed( ActionEvent e)
  {
    Set<EIntTimesMezok> sKijelzendoParameterek = null ;
    CHRMFile cCHRMFile = null ;
    
System.out.println( "JParValCBListener::actionPerformed()") ;

    // Ugyan a CPolar::CPolar()
//    if ( m_cPolar.m_aHRMFile != null && m_jJParamValPanel != null )
//    {
//      sKijelzendoParameterek = m_jJParamValPanel.GetKijelzendoParameterek() ;

//      for ( int nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
//      {
//        cCHRMFile = m_cPolar.m_aHRMFile[nIdx] ;

//        if ( cCHRMFile != null )
//        {
//          cCHRMFile.KijelzendoParamBeall( sKijelzendoParameterek) ;
//System.out.println( "JParValCBListener::actionPerformed() m_cPolar.m_aHRMFile[" + nIdx + "] == null") ;
//        }
//        else
//        {
//System.out.println( "JParValCBListener::actionPerformed() m_cPolar.m_aHRMFile[" + nIdx + "] != null") ;
//        }
//      }
//    }      
    
//    m_cPolar.m_cJDiagram.m_jJDiagramParam.revalidate() ; ettol fuggetlenul is rogton megjelenik a valtozas, ha a CheckBoxok allapota valtozik
    m_cPolar.m_cJDiagram.m_jJDiagramParam.repaint() ; // Ez kell, hogy azonnal lassuk a valtozast
  }
}

public class JParamValPanel extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 3443685643513647952L ;

  // A CheckBoxokat tartalmazo tomb, a merete megegyezik az m_aKijelzendoParameterek[]-evel,
  // mely segitsegevel a CB-okat es Enumokat mappelem
  public          JCheckBox      m_aParamNevekCB[]          = null ;

  protected static final EIntTimesMezok m_aKijelzendoParameterek[] = { EIntTimesMezok.KORIDO, EIntTimesMezok.HR, EIntTimesMezok.HRMIN, EIntTimesMezok.HRAVG, EIntTimesMezok.HRMAX,
                                                                       EIntTimesMezok.SPD, EIntTimesMezok.CAD, EIntTimesMezok.ALT, EIntTimesMezok.LAPTYPE, EIntTimesMezok.DST, EIntTimesMezok.TEMP} ;

  protected JParValCBListener m_jJParValCBListener = new JParValCBListener( this) ;
  
  public JParamValPanel( /*EIntTimesMezok... aValhatoParam*/)
  {
    super() ;
    
    int nIdx = 0 ;

//setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION /*| DebugGraphics.FLASH_OPTION | DebugGraphics.LOG_OPTION*/);    
//System.out.println( "JParamValPanel::setDebugGraphicsOptions()") ;

    setLayout( new FlowLayout( FlowLayout.LEADING)) ;
    
    m_aParamNevekCB = new JCheckBox[m_aKijelzendoParameterek.length] ;
    
    for ( nIdx = 0 ; nIdx < m_aKijelzendoParameterek.length ; nIdx++ )
    {
      m_aParamNevekCB[nIdx] = new JCheckBox( m_aKijelzendoParameterek[nIdx].MezoNeve(), true) ;
      
      m_aParamNevekCB[nIdx].addActionListener( m_jJParValCBListener) ;
      
      add( m_aParamNevekCB[nIdx]) ;
    }
    
//setDebugGraphicsOptions( DebugGraphics.LOG_OPTION | DebugGraphics.FLASH_OPTION);
  }

  // A visszateresi tomb eleje kitoltve, a folos hely default null-okat tartalmaz
  public Set<EIntTimesMezok> GetKijelzendoParameterek()
  {
    int nIdx = 0 ;
    Set<EIntTimesMezok> sKijelzendoParameterek = null ;

    sKijelzendoParameterek = EnumSet.noneOf( EIntTimesMezok.class) ;
    
    if ( m_aParamNevekCB != null )
    {
      for ( nIdx = 0 ; nIdx < m_aParamNevekCB.length ; nIdx++ )
      {
        if ( m_aParamNevekCB[nIdx] != null && m_aParamNevekCB[nIdx].isSelected() == true )
        {
          sKijelzendoParameterek.add( m_aKijelzendoParameterek[nIdx]);
        }
      }
    }
    
    return sKijelzendoParameterek ; 
  }

  void TartalomFrissit()
  {
System.out.println( "JParamValPanel::TartalomFrissit() : ures fv") ;
  }

  //----------------------------------------------------------------------------
  public static void main(String s[])
  {
    // { "LAP", "HR", "HRmin", "HRavg", "HRmax", "SPD", "CAD", "ALT", "STP", "DST", "10*TEMP"} ;
    final JParamValPanel jParamValPanelTeszt = new JParamValPanel( /*EIntTimesMezok.KORIDO, EIntTimesMezok.HR, EIntTimesMezok.HRMIN, EIntTimesMezok.HRAVG, EIntTimesMezok.HRMAX,
                                                                   EIntTimesMezok.SPD, EIntTimesMezok.CAD, EIntTimesMezok.ALT, EIntTimesMezok.LAPTYPE, EIntTimesMezok.DST,
                                                                   EIntTimesMezok.TEMP*/) ;

    WindowListener cWindowListener = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {System.exit(0);}
//      public void windowDeiconified(WindowEvent e) { demo.surf.start(); }
//      public void windowIconified(WindowEvent e) { demo.surf.stop(); }
    };
    
    JFrame f = new JFrame("Az egyes intervallum adatok megjeleniteset szabalyozza");

    f.addWindowListener( cWindowListener) ;
    f.getContentPane().add("Center", jParamValPanelTeszt) ;
    f.pack() ;
    f.setSize(new Dimension(200,200)) ;
    f.setVisible(true)  ;
  }
} // public class JParamValPanel ...
