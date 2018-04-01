package cpolar;
// CPolar.java hiba
//: CPolar.java
// CPolar Java proba program Polar Precision Performance *.hrm file-ok
//        megjelenitesere
 
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
// import java.util.EnumSet;
// import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
// import javax.swing.RepaintManager;

// Minden valtozo implicit static final
interface IMFActionListener extends ActionListener
{
  // A kijelezheto parameterek neve l. JIntTimes / JParamValPanel
  // Ha ez kesobb van, mint az m_cPolar letrehozasa, null pointer az eredmeny CPolar inicializalasakor
//  String m_aParamNevek[] = { "LAP", "HR", "HRmin", "HRavg", "HRmax", "SPD", "CAD", "ALT", "STP", "DST", "10*TEMP"} ;
  
  // A fenti parameterek kijelzeset ki/be kapcsolo "bitmezo" Kezdetben minden "be"
  // JParValCBListener::actionPerformed( ActionEvent e)-ben allitodik, JParamValPanel::JCheckBox[] alapjan
  // Uj edzes megnyitasanal ezt adom meg CPolar::UjHRMFileHozzaad()-ban
  // Tomb, mert kul. nem lehetne allitani

/*
kar volt ezt innen az egyes hrm file-ok ba tenni mert uj file betoltesenel
annak minden parametere megjelenik a tablazatban, es csak ki/beixelesnel frissul
*/

//  int m_aKijelzendoParameterek[] = { -1} ;
  // http://www.javapractices.com/topic/TopicAction.do?Id=1 -alapjan : l. meg : JDiagramParam - ennyi parameter kozul lehet valasztani a GUI-n
  // Ez ebben az esetben egy RegularEnumSet<E extends Enum<E>> extends EnumSet<E> tipusu osztalyt hoz letre (<= 64 elem)
  // Kezdetben mindegyik benne lesz
// A CHRMFile-okba kerult (multiplikalva) JParValCBListener::actionPerformed
//  Set<EIntTimesMezok> m_sKijelzendoParameterek = EnumSet.of( EIntTimesMezok.KORIDO, EIntTimesMezok.HR, EIntTimesMezok.HRMIN, EIntTimesMezok.HRAVG, EIntTimesMezok.HRMAX,
//                                                             EIntTimesMezok.SPD, EIntTimesMezok.CAD, EIntTimesMezok.ALT, EIntTimesMezok.LAPTYPE, EIntTimesMezok.DST, EIntTimesMezok.TEMP) ;
  
  // A JDiagrGorgNagy.java : CBalra/JobbraListener leptetese: +,- -> Surface::ValtEltIdx( int nValtoztatas)
//  int   m_nIdxValtLepese = 1 ;
  // 0.01 [Km] a CHRMFile::m_aDistance-ban [10km]*Fs(5,15,60) mertekegysegben van a tavolsag.
  // Ehhez Fs*3600-zal kell szorozni m_fEltTavValtLepese-t, hogy osszhangban legyenek
  float m_fEltTavValtLepese = 0.01f ;
  
  // A JDiagrGorgNagy.java : 
  float m_fNyuzsFaktor   = 1.1f ;

  // This final variable must be initialized: CPolar m_cPolar miatt :
  // A sok ebbol az interface-bol leszarmaztatott es lettrehozott Listener
  // ellenere csak egy CPolar objektum lesz
  CPolar m_cPolar = new CPolar() ;
}

// Szuroosztaly a FileDialog szamara, hogy csak a *.hrm fileok legyenek lathatok
class CHRMFilter implements FilenameFilter
{
  // OK-t ad minden file-ra, mely .hrm-re vegzodik
  final String m_sKiterjesztes = IKonstansok.sOlvFileKiterj ;
  
  // Ez az a fv., melyet a FileDialog felhiv, hogy eldontse, hogy
  // az adott file *.hrm file-e vagy sem : csak a name lesz kiertekelve
  public boolean accept( File dir, String sFileName)
  {
    return sFileName.endsWith( m_sKiterjesztes) ;
  }
}

// A file megnyitas rendszer dialogus ablak probaja
class CMegnyitListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
//    String cEleresiUtvonal  = null ;
//    String cMegnyitandoFile = null ;
    // Hivatott megszurni a kivalaszthato file-okat
//    CHRMFilter cHRMFilter ; 
    JFileValasztDlg jFileValasztDlg = null ;

//    int     nIdx = 0      ;
//    boolean bRC  = false  ;

    jFileValasztDlg = new JFileValasztDlg( m_cPolar) ;

    // Ez a fv (ellentetben a HivValt-ban tapasztaltakkal) rogton visszater...
    jFileValasztDlg.show() ;
  }

  public void ExceptionTrace( Exception eException)
  {
    System.out.println( "Caught Exception") ;
    System.out.println( "getMessage(): " + eException.getMessage()) ;
    System.out.println( "toString(): " + eException.toString()) ;
    System.out.println( "printStackTrace():") ;
    eException.printStackTrace() ; 
  }
}

// Program inf� / Program inform�ci�
class CProgramInfoListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {                                              // IMFActionListener.m_cPolar
    CProgInfoDlg cProgInfoDlg = new CProgInfoDlg( m_cPolar, IKonstansok.sProgramNev) ;
    
    cProgInfoDlg.SzovegBeallit( IKonstansok.sProgInfoSzoveg1, IKonstansok.sProgInfoSzoveg2) ;

    cProgInfoDlg.Kirajzol() ;
  }
}

// F�jl / Kil�p
// Kilepes a rendszergombokra
class CKilepListener extends WindowAdapter implements ActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    System.exit( 0) ;
  }

  public void windowClosing( WindowEvent e)
  {
    System.exit( 0) ;
  }
}

class CPolarKeyListener implements KeyListener
{
  // Invoked when a key has been typed.
  public void keyTyped( KeyEvent e)
  {
    System.out.println( "CBillentyuLeutes::keyTyped()") ;     
  }

  // Invoked when a key has been pressed.
  public void keyPressed( KeyEvent e)
  {
    System.out.println( "CBillentyuLeutes::keyPressed()") ;
  }

  // Invoked when a key has been released.
  public void keyReleased( KeyEvent e)
  {
    System.out.println( "CBillentyuLeutes::keyReleased()") ;
  }  
}

public class CPolar extends JFrame
{
  /**
   * Ezt hianyolta, mert az osztaly "serializable" <code>serialVersionUID</code>
   */

  private static final long serialVersionUID = 1L;
  // T A G V A L T O Z O K
  public static final int m_fnMaxHrmFileSzam = 5 ;
  
  JMenuBar m_cMenuBar = new JMenuBar() ;

  JMenu m_cFajlMenu        = new JMenu( IKonstansok.sFajlMenu) ;
  JMenu m_cProgramInfoMenu = new JMenu( IKonstansok.sProgramInfoMenu) ;

  JMenuItem m_cFajlMenuPont[] = {
//                                  new JMenuItem( "Konvertálás indít"),
                                  new JMenuItem( IKonstansok.sFajlMegnyitMenu),
                                  new JMenuItem( IKonstansok.sFajlKilepMenu)
                                } ;

  JMenuItem m_cProgramInfoMenuPont[] = {
                                         new JMenuItem( IKonstansok.sProgramMenuPont)
                                       } ;

  // A HRM-file adatait tartalmazo osztlay
  CHRMFile m_aHRMFile[] = null ;

  // A teljes ablakot kitolto diagram
  JDiagram m_cJDiagram = null ;
  
  // Az aktiv edzes file indexe a fenti tombben
  int m_nAktivEdzes = -1 ;
  
  // Eltolas km-ben minden edzes/HRMFile-ra ervenyesen, az osztalyok csak ennek
  // es az adott file-ra ervenyes eltolas osszeget indexet tartjak szamon
  private float m_fFoEltTav  = 0.0f ;
  
  // A JFileValaszt dialogusban ezt kinalja fel. Kilepeskor aktualizalodik.
  public String m_sAktKvt = "." ;

  public CPolar()
  {
    IMFActionListener cTempMFActionListener ;
    int i ;
    int nHossz ;

// System.out.println( "CPolar()") ;
    // A HRM-file adatait tartalmazo osztaly
    m_aHRMFile = new CHRMFile[m_fnMaxHrmFileSzam] ;

    // A teljes ablakot kitolto diagram
    m_cJDiagram = new JDiagram() ;

    // ???
    setTitle( IKonstansok.sProgramNev) ;

    // -------------------------------------------------

    // Fajl / Megnyit
    cTempMFActionListener = new CMegnyitListener() ;
    m_cFajlMenuPont[0].addActionListener( cTempMFActionListener) ;

    // Program infó / Program információ
    cTempMFActionListener = new CProgramInfoListener() ;
    m_cProgramInfoMenuPont[0].addActionListener( cTempMFActionListener) ;

    // A menupontok menukhoz valo hozzaadasa
    nHossz = m_cFajlMenuPont.length ;
    for( i = 0 ; i < nHossz ; i++)
    {
      m_cFajlMenu.add( m_cFajlMenuPont[i]) ;
    }

    nHossz = m_cProgramInfoMenuPont.length ;
    for( i = 0 ; i < nHossz ; i++)
    {
      m_cProgramInfoMenu.add( m_cProgramInfoMenuPont[i]) ;
    }

    m_cMenuBar.add( m_cFajlMenu) ;
    m_cMenuBar.add( m_cProgramInfoMenu) ;

    getContentPane().add( "Center", m_cJDiagram) ;
    
//getContentPane().add( "South", new Button( "South")) ; athelyezve a JDiagramba

    setJMenuBar( m_cMenuBar) ;
    
//    addKeyListener( new CPolarKeyListener()) ;
  }
  
  protected void processKeyEvent(KeyEvent e)
  {
System.out.println("CPolar::processKeyEvent()") ;
    super.processKeyEvent( e) ;
  }

  protected void processComponentKeyEvent(KeyEvent e)
  {
System.out.println("CPolar::processComponentKeyEvent()") ;
    super.processKeyEvent( e) ;
  }

  // A JFileValasztDlg.java COnOKListener.actionPerformed()-bol hivodik
  public boolean UjHRMFileHozzaad( String cEleresiUtvonal, String cMegnyitandoFile)
  {
    // Hivatott megszurni a kivalaszthato file-okat
//    CHRMFilter cHRMFilter ; 
//    JFileValasztDlg jFileValasztDlg = null ;

    int     nIdx = 0      ;
    boolean bRC  = false  ;

    if( cEleresiUtvonal != null && cMegnyitandoFile != null )
    {
      try
      {
        // Ures hely keresese - meg az ujrafelhasznalas (letrehozott file, ami mar
        // nincs megjelenitve)
        while ( nIdx < CPolar.m_fnMaxHrmFileSzam  &&
                m_aHRMFile[nIdx] != null &&
                m_aHRMFile[nIdx].GetKijelzendo() == true )
        {
System.out.println( "CPolar::UjHRMFileHozzaad() m_cPolar.m_aHRMFile[" + nIdx + "] != null") ;

          nIdx++ ;
        }

        if ( nIdx < CPolar.m_fnMaxHrmFileSzam && m_aHRMFile[nIdx] == null )
        {
System.out.println( "CPolar::UjHRMFileHozzaad() m_cPolar.m_aHRMFile[" + nIdx + "] == null") ;
          m_aHRMFile[nIdx] = new CHRMFile() ;
        }

/*
        m_aHRMFile[nIdx].KijelzendoParamBeall( EIntTimesMezok.KORIDO, EIntTimesMezok.HR, EIntTimesMezok.HRMIN, EIntTimesMezok.HRAVG, EIntTimesMezok.HRMAX,
                                               EIntTimesMezok.SPD, EIntTimesMezok.CAD, EIntTimesMezok.ALT, EIntTimesMezok.LAPTYPE, EIntTimesMezok.DST,
                                               EIntTimesMezok.TEMP) ;
*/

        m_aHRMFile[nIdx].FileMegnyit( cEleresiUtvonal, cMegnyitandoFile) ;

        bRC = m_aHRMFile[nIdx].KonfiguracioBeolv() ;
//System.out.println( "UjHRMFileHozzaad CHRMFile.KonfiguracioBeolv() -> " + bRC) ;

        if ( bRC == false )
        {
          m_aHRMFile[nIdx].FileLezar() ;
          
          return false ;
        }

        bRC = m_aHRMFile[nIdx].AdatokBeolv() ;
//System.out.println( "UjHRMFileHozzaad CHRMFile.AdatokBeolv() -> " + bRC) ;

        m_aHRMFile[nIdx].FileLezar() ;

        if ( bRC == false )
        {
          return false ;
        }

        if ( m_aHRMFile[nIdx].VanSpeedAdat() == true )
        {
//System.out.println( "UjHRMFileHozzaad m_aHRMFile[" + nIdx + "].VanSpeedAdat() == TRUE") ;
	
// m_aHRMFile[nIdx].Kiir() ;

          m_aHRMFile[nIdx].SetKijelzendo( true) ;

          // Hogy a karakteres kijelzesi mod is frissuljon
          m_cJDiagram.m_jJDiagramParam.TartalomFrissit() ;
//System.out.println( "CPolar::UjHRMFileHozzaad()") ;

          // Mindenkeppen a SetKijelzendo() utan, hogy kirajzolja
          // Kell, abban az esetben, ha az eltuno file megnyito ablak nem general
          // frissitesi kerelmet (mert nem fedte a foablakot)
          repaint() ;
        }
        else
        {
//          JDialog createDialog(Component parentComponent, String title) ;
//System.out.println( "UjHRMFileHozzaad m_aHRMFile[" + nIdx + "].VanSpeedAdat() == FALSE") ;

          JOptionPane.showMessageDialog( this, "Nincs sebesseg adat !\nA file nem jeleníthető meg.") ;
  
          m_aHRMFile[nIdx].SetKijelzendo( false) ;
        }
      }
      catch ( IOException eIOException)
      {
        ExceptionTrace( eIOException) ;
//System.out.println( "CHRMFile.AdatokBeolv() ExceptionTrace( eIOException) utan") ;
        m_aHRMFile[nIdx].SetKijelzendo( false) ;
//System.out.println( "CHRMFile.AdatokBeolv() m_aHRMFile[nIdx].SetKijelzendo( false) utan") ;

        try
        {
          if ( m_aHRMFile != null && m_aHRMFile[nIdx] != null )
          {
            m_aHRMFile[nIdx].FileLezar() ;
          }
//System.out.println( "CHRMFile.AdatokBeolv() m_aHRMFile[nIdx].FileLezar() utan") ;
        }
        catch ( IOException eIOException2)
        {
          ExceptionTrace( eIOException2) ;
//System.out.println( "CHRMFile.AdatokBeolv() ExceptionTrace( eIOException2) utan") ;
        }
	
        bRC = false ;
      }
    }
    
    return bRC ;
  }

  public CHRMFile AktivFile()
  {
    // A rovidites kedveert
    CHRMFile cLocHRMFile = null ;

    int nIdx = 0 ;
	
    nIdx = m_nAktivEdzes ;
//System.out.println( "AktivFile() nIdx = " + nIdx) ;

    if ( 0 <= nIdx && nIdx < CPolar.m_fnMaxHrmFileSzam ) 
    {
      cLocHRMFile = m_aHRMFile[nIdx] ;
    }
    else
    {
//System.out.println( "AktivFile() Most csak megkeresi az elso nem ureset, az lesz az aktiv...") ;
      nIdx = 0 ;

      // Most csak megkeresi az elso nem ureset, az lesz az aktiv...
      while ( nIdx < CPolar.m_fnMaxHrmFileSzam &&
              (m_aHRMFile[nIdx] == null ||
              (m_aHRMFile[nIdx] != null &&
               m_aHRMFile[nIdx].GetKijelzendo() == false)) )
      {
        nIdx++ ;
      }

      if ( nIdx < CPolar.m_fnMaxHrmFileSzam )
      {
        m_nAktivEdzes = nIdx ;
        cLocHRMFile = m_aHRMFile[nIdx] ;	
      }
    }

//System.out.println( "AktivFile() nIdx = " + nIdx) ;
    return cLocHRMFile ;
  }

  // Ha egy *.hrm file-t becsuknak (v. kiderul betoltes utan, hogy korrupt),
  // ez a file allit be uj aktiv file-t -> visszateresi ertek
  public int ElsoKijAktivizal()
  {
    int nIdx = 0 ;

    // Most csak megkeresi az elso nem ures kijelzendot, az lesz az aktiv...
    while ( nIdx < CPolar.m_fnMaxHrmFileSzam &&
            (IMFActionListener.m_cPolar.m_aHRMFile[nIdx] == null ||
            (IMFActionListener.m_cPolar.m_aHRMFile[nIdx] != null &&
             IMFActionListener.m_cPolar.m_aHRMFile[nIdx].GetKijelzendo() == false)) )
    {
      nIdx++ ;
    }

    if ( nIdx < CPolar.m_fnMaxHrmFileSzam )
    {
      m_nAktivEdzes = nIdx ;
    }
    else
    {
      // Ha egyik sem tartalmaz kijelzendo edzest...
      m_nAktivEdzes = -1 ;
    }

//System.out.println( "ElsoKijAktivizal() : m_nAktivEdzes = " + m_nAktivEdzes) ;

    return m_nAktivEdzes ;
  }
  
  public float GetFoEltTav()
  {
    return m_fFoEltTav ;
  }

  public void EdzEltolas( int nValtoztatas)
  {
    int nIdx = 0 ;
    int nOsszElt = 0 ;
    boolean bEltTavValt  = false ;
//    boolean bVoltEltolas = false ;

    // A rovidites kedveert
    CHRMFile cLocHRMFile = null ;

//System.out.println( "CPolar::ValtEltIdx() : BEGIN nValtoztatas=" + nValtoztatas) ;

    for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
    {
//System.out.println( "CPolar::ValtEltIdx() : nIdx=" + nIdx) ;
      cLocHRMFile = m_aHRMFile[nIdx] ;
  
      if ( cLocHRMFile != null )
      {
        // Ez a tavolsagot is allitja az osztalyban, es az eltolasi indexet
        // Csak fo eltolas file-onkenti eltolast ez a fv. nem csinal
        nOsszElt = cLocHRMFile.GetEltTav()+ nValtoztatas ; // int + float !!! 650299 + 0.01
//System.out.println( "CPolar::ValtEltIdx() : nOsszElt=" + nOsszElt) ;
        bEltTavValt = cLocHRMFile.EltTavValt( nOsszElt) ;
        
        // Be kell allitani az adott file-hoz tartozo JTxtEditScroll-ban az eltolasi tavolsagot
        if ( bEltTavValt == true )
        {
          // CPolar->JDiagram->JEdzesEltolo->JTxtEditScroll
          m_cJDiagram.SetElt( nIdx, nOsszElt/36000.0f) ;
        }
      }
    }
//System.out.println( "CPolar::ValtEltIdx() : END") ;
  }

  // Surface::EgyEdzValtEltIdx()-bol hivva. Az viszont sehol sincs hivva. 
  public boolean EgyEdzEltolas( int nEdzSzama, float fValtoztatas)
  {
    boolean bRC = false ;

    if ( 0 <= nEdzSzama &&  nEdzSzama < m_fnMaxHrmFileSzam )
    {
      if ( m_aHRMFile[nEdzSzama] != null )
      {
//        m_aHRMFile[nEdzSzama].EltTavValt( fValtoztatas) ; fv. param. valtoztatas : ugysincs hasznalva ...
        m_aHRMFile[nEdzSzama].EltTavValt( (int)(36000.0f*fValtoztatas)) ;        
        bRC = true ;
      }
    }
    
    return bRC ;
  }

  
  public void ExceptionTrace( Exception eException)
  {
    System.out.println( "Caught Exception") ;
    System.out.println( "getMessage(): " + eException.getMessage()) ;
    System.out.println( "toString(): " + eException.toString()) ;
    System.out.println( "printStackTrace():") ;
    eException.printStackTrace() ;
  }

/*
  public Dimension getMaximumSize()
  {
System.out.println( "CPolar::getMaximumSize()") ;
    return super.getMaximumSize() ;    
  }
  
  public Dimension getMinimumSize()
  {
System.out.println( "CPolar::getMinimumSize()") ;
    return super.getMinimumSize() ;    
  }
  
  public Dimension getPreferredSize()
  {
System.out.println( "CPolar::getPreferredSize()") ;
    return super.getPreferredSize() ;    
  }
*/  
  // A main() for the application:
  public static void main( String[] args)
  {
//System.out.println( "CPolar::main() : setDoubleBufferingEnabled(false)") ;
//RepaintManager.currentManager(null).setDoubleBufferingEnabled(false) ;

    CKilepListener cKilepListener ;

    cKilepListener = new CKilepListener() ;

    // A system kilepes gomb
    IMFActionListener.m_cPolar.addWindowListener( cKilepListener) ; 

    // Kil�p
    IMFActionListener.m_cPolar.m_cFajlMenuPont[1].addActionListener( cKilepListener) ;

    IMFActionListener.m_cPolar.pack() ;

    IMFActionListener.m_cPolar.setSize( new Dimension( 900, 550)) ;
    IMFActionListener.m_cPolar.setVisible(true) ;
  }
}
