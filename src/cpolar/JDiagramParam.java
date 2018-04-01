package cpolar;
//JDiagramParam.java hiba

//--- Kikommentezve, mert "... is never used"
//import java.lang.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// JDiagramParam : az egyes betoltott file-okhoz tartozo RadioButtonok lenyomasara
class CElsoRBListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    if ( m_cPolar.m_aHRMFile.length > 0 )
    {
      if ( m_cPolar.m_aHRMFile[0] != null &&
           m_cPolar.m_aHRMFile[0].GetKijelzendo() == true )
      {
        m_cPolar.m_nAktivEdzes = 0 ;
      }
      else
      {
        // Mert m_cPolar.m_aHRMFile az osztaly letrehozasanal foglalodik,
        // es -1 az aktiv edzes 
        if ( m_cPolar.m_nAktivEdzes >= 0 )
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[m_cPolar.m_nAktivEdzes].setSelected(true) ;
        }
        else
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected(true) ;
        }
      }
    }
  }
}

class CMasodikRBListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    if ( m_cPolar.m_aHRMFile.length > 0 )
    {
      if ( m_cPolar.m_aHRMFile[1] != null &&
           m_cPolar.m_aHRMFile[1].GetKijelzendo() == true )
      {
        m_cPolar.m_nAktivEdzes = 1 ; 
      }
      else
      {
        // Mert m_cPolar.m_aHRMFile az osztaly letrehozasanal foglalodik,
        // es -1 az aktiv edzes 
        if ( m_cPolar.m_nAktivEdzes >= 0 )
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[m_cPolar.m_nAktivEdzes].setSelected(true) ;
        }
        else
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected(true) ;
        }
      }
    }
    else
    {
      if ( m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek != null &&
           m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0] != null )
      {
        // Ha nincs file betoltve, a vegen mindig a 0. legyen az aktiv
        m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected( true) ;
      }
    }
  }
}

class CHarmadikRBListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    if ( m_cPolar.m_aHRMFile.length > 0 )
    {
      if ( m_cPolar.m_aHRMFile[2] != null &&
           m_cPolar.m_aHRMFile[2].GetKijelzendo() == true )
      {
        m_cPolar.m_nAktivEdzes = 2 ;
      }
      else
      {
        // Mert m_cPolar.m_aHRMFile az osztaly letrehozasanal foglalodik,
        // es -1 az aktiv edzes 
        if ( m_cPolar.m_nAktivEdzes >= 0 )
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[m_cPolar.m_nAktivEdzes].setSelected(true) ;
        }
        else
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected(true) ;
        }
      }
    }
    else
    {
      if ( m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek != null &&
           m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0] != null )
      {
        // Ha nincs file betoltve, a vegen mindig a 0. legyen az aktiv
        m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected( true) ;
      }
    }
  }
}

class CNegyedikRBListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    if ( m_cPolar.m_aHRMFile.length > 0 )
    {
      if ( m_cPolar.m_aHRMFile[3] != null &&
           m_cPolar.m_aHRMFile[3].GetKijelzendo() == true )
      {
        m_cPolar.m_nAktivEdzes = 3 ;
      }
      else
      {
        // Mert m_cPolar.m_aHRMFile az osztaly letrehozasanal foglalodik,
        // es -1 az aktiv edzes 
        if ( m_cPolar.m_nAktivEdzes >= 0 )
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[m_cPolar.m_nAktivEdzes].setSelected(true) ;
        }
        else
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected(true) ;
        }
      }
    }
    else
    {
      if ( m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek != null &&
           m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0] != null )
      {
        // Ha nincs file betoltve, a vegen mindig a 0. legyen az aktiv
        m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected( true) ;
      }
    }
  }
}

class COtodikRBListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    if ( m_cPolar.m_aHRMFile.length > 0 )
    {
      if ( m_cPolar.m_aHRMFile[4] != null &&
           m_cPolar.m_aHRMFile[4].GetKijelzendo() == true )
      {
        m_cPolar.m_nAktivEdzes = 4 ;
      }
      else
      {
        // Mert m_cPolar.m_aHRMFile az osztaly letrehozasanal foglalodik,
        // es -1 az aktiv edzes 
        if ( m_cPolar.m_nAktivEdzes >= 0 )
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[m_cPolar.m_nAktivEdzes].setSelected(true) ;
        }
        else
        {
          m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected(true) ;
        }
      }
    }
    else
    {
      if ( m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek != null &&
           m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0] != null )
      {
        // Ha nincs file betoltve, a vegen mindig a 0. legyen az aktiv
        m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[0].setSelected( true) ;
      }
    }
  }
}

class CEdzesBezarListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    int nIdx = 0 ;

//System.out.println( "CEdzesBezarListener::actionPerformed() BEGIN") ;
//System.out.println( "CEdzesBezarListener::actionPerformed() m_cPolar.m_nAktivEdzes=" + m_cPolar.m_nAktivEdzes) ;

    nIdx = m_cPolar.m_nAktivEdzes ;

    if ( nIdx >= 0 )
    {
      m_cPolar.m_aHRMFile[nIdx].SetKijelzendo( false) ;

      // A becsukott file szoveget letorli
      m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[nIdx].setText( Integer.toString( nIdx) + ". ") ;
 
      nIdx = m_cPolar.ElsoKijAktivizal() ;
//System.out.println( "CEdzesBezarListener::actionPerformed() ElsoKijAktivizal()=" + nIdx) ;
    
      // A radiogomb bekapcsolasa
      if ( nIdx == -1 )
      {
        nIdx = 0 ;
      }
    
      m_cPolar.m_cJDiagram.m_jJDiagramParam.m_aBetoltottEdzesek[nIdx].setSelected(true) ;
      
      m_cPolar.m_cJDiagram.m_jJDiagramParam.repaint() ;
    }
    
//System.out.println( "CEdzesBezarListener::actionPerformed() END") ;
    // TartalomFrissit() ; ???
  }
}

public class JDiagramParam extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152499L ;

  // A HRM file-ok kijelzendo szoveges parametereit tartalmazo layout
//  Box m_cBoxLayout = new Box( BoxLayout.Y_AXIS) ;// Box.createVerticalBox() ;

  // Az 5 megjegyzest ide rakom bele
  protected JRadioButton m_aBetoltottEdzesek[] = new JRadioButton[5] ; // A bejelolt az aktiv
  protected ButtonGroup  m_cButtonGroup        = new ButtonGroup() ;

  protected JButton m_jEdzesBezar = new JButton( IKonstansok.sAktEdzBezar) ;

  protected Font m_cSerifF = new Font( "serif", Font.PLAIN, 10)  ;

  protected JParamValPanel m_jParamValPanel = null ;
  
  protected JIntvAdatTblModel m_jIntAdatTblModel = null ;
  protected JTable           m_jIntAdatTbl      = null ;

  public JDiagramParam()
  {
    super() ;
    
    JScrollPane jTablazatScrollPane = null ;
    
    int nIdx = 0 ;
    boolean bAktiv = true ;
    
//    setPreferredSize( new Dimension( 1035, 800)) ; nincs hatasa

    setLayout( new BoxLayout( this, BoxLayout.Y_AXIS)) ;
    
    for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
    {
      // Az elso lesz legeloszor aktiv
      m_aBetoltottEdzesek[nIdx] = new JRadioButton( Integer.toString( nIdx) + ". ", bAktiv) ;
      bAktiv = false ;
        
      m_aBetoltottEdzesek[nIdx].setFont( m_cSerifF) ;
      m_aBetoltottEdzesek[nIdx].setForeground( Color.black) ;
      
      // Ez igazitja balra a szoveget - hatastalan
//      m_aBetoltottEdzesek[nIdx].setHorizontalAlignment( JRadioButton.LEFT) ;
//m_cSerifF.getLineMetrics( "Wy", Graphics2D.getFontRenderContext()).getHeight() ; 
//m_aBetoltottEdzesek[nIdx].setPreferredSize( new Dimension( 500, 20)) ;

//      m_cBoxLayout.add( m_aBetoltottEdzesek[nIdx]) ;
      add( m_aBetoltottEdzesek[nIdx]) ;
      
      m_cButtonGroup.add( m_aBetoltottEdzesek[nIdx]) ;
      
//m_aBetoltottEdzesek[nIdx].setDebugGraphicsOptions( DebugGraphics.LOG_OPTION | DebugGraphics.FLASH_OPTION);
    }
    
    m_aBetoltottEdzesek[0].addActionListener( new CElsoRBListener())     ;
    m_aBetoltottEdzesek[1].addActionListener( new CMasodikRBListener())  ;
    m_aBetoltottEdzesek[2].addActionListener( new CHarmadikRBListener()) ;
    m_aBetoltottEdzesek[3].addActionListener( new CNegyedikRBListener()) ;
    m_aBetoltottEdzesek[4].addActionListener( new COtodikRBListener())   ;
//m_jEdzesBezar.setPreferredSize( new Dimension( 160, 26)) ;

//    m_cBoxLayout.add( m_jEdzesBezar) ;
    add( m_jEdzesBezar) ;
    
//System.out.println( "JDiagramParam::JDiagramParam() : m_jEdzesBezar.getPreferredSize()" + m_jEdzesBezar.getPreferredSize()) ;

    m_jEdzesBezar.addActionListener( new CEdzesBezarListener()) ;
//m_jEdzesBezar.setDebugGraphicsOptions( DebugGraphics.BUFFERED_OPTION | DebugGraphics.FLASH_OPTION /* | DebugGraphics.LOG_OPTION  */) ;

// innen atvinni a kijelzendo parametereket a JParamValPanel-be    
    m_jParamValPanel = new JParamValPanel( /*EIntTimesMezok.KORIDO, EIntTimesMezok.HR, EIntTimesMezok.HRMIN, EIntTimesMezok.HRAVG, EIntTimesMezok.HRMAX,
                                           EIntTimesMezok.SPD, EIntTimesMezok.CAD, EIntTimesMezok.ALT, EIntTimesMezok.LAPTYPE, EIntTimesMezok.DST, EIntTimesMezok.TEMP*/) ;

    add( m_jParamValPanel) ;
    
    // Az edzes adatokat tartalmazo tablazat hozzaadasa
    m_jIntAdatTblModel = new JIntvAdatTblModel( m_jParamValPanel) ;
    m_jIntAdatTbl      = new JTable( m_jIntAdatTblModel) ;
    
    jTablazatScrollPane = new JScrollPane( m_jIntAdatTbl) ;
//m_jIntAdatTbl.setDebugGraphicsOptions( DebugGraphics.BUFFERED_OPTION | DebugGraphics.FLASH_OPTION /* | DebugGraphics.LOG_OPTION  */) ;

    add( jTablazatScrollPane) ;
    
//jTablazatScrollPane.setDebugGraphicsOptions( DebugGraphics.BUFFERED_OPTION | DebugGraphics.FLASH_OPTION /* | DebugGraphics.LOG_OPTION  */) ;
//-------------------------------------------------------------------
//    JIntvAdatTblModel jIntAdatTblModel = new JIntvAdatTblModel() ;
//    JTable jTable = new JTable( jIntAdatTblModel) ;
//    JScrollPane jScrollPane = new JScrollPane() ;
    
//    jScrollPane.add( jTable) ; 
//    m_cBoxLayout.add( jScrollPane) ;
    
//    jScrollPane.doLayout() ;
//  -------------------------------------------------------------------
    
//    add( m_cBoxLayout) ;
  }
  
  void TartalomFrissit()
  {
    int      nIdx = 0 ;
    CHRMFile cLocHRMFile = null ;
//    String   sSzoveg = null ;
    
//System.out.println( "JDiagramParam::TartalomFrissit(): BEGIN") ;
       
//    sSzoveg = new String() ;

    for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
    {
      // A rovidites kedveert...
      cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nIdx] ;
      
      if ( cLocHRMFile != null && cLocHRMFile.GetKijelzendo() == true  )
      {
/*
sSzoveg = Integer.toString( nIdx) +
          ". " + cLocHRMFile.GetDatum() + " " +
              cLocHRMFile.GetMegjegyzes() ;
	  
System.out.println( "JDiagramParam::TartalomFrissit(): " + sSzoveg) ;
*/
        m_aBetoltottEdzesek[nIdx].setText( Integer.toString( nIdx) + ". " +
                                           cLocHRMFile.GetDatum() + 
                                           " " + cLocHRMFile.GetMegjegyzes()) ;
      }
      else
      {
        m_aBetoltottEdzesek[nIdx].setText( Integer.toString( nIdx) + ". ") ;
      }
    }
    
//System.out.println( "JDiagramParam::TartalomFrissit(): END") ;
  }
  
  public void revalidate()
  {
    super.revalidate() ;
//System.out.println( "JDiagramParam::revalidate()") ;
  }
  
  public void repaint()
  {
    super.repaint() ;
//System.out.println( "JDiagramParam::repaint()") ;
  }

  
/*
  public Dimension getMaximumSize()
  {
System.out.println( "JDiagramParam::getMaximumSize()") ;
    return super.getMaximumSize() ;    
  }
  
  public Dimension getMinimumSize()
  {
    System.out.println( "JDiagramParam::getMinimumSize()") ;
    return super.getMinimumSize() ;    
  }
  
  public Dimension getPreferredSize()
  {
System.out.println( "JDiagramParam::getPreferredSize()") ;
    return super.getPreferredSize() ;    
  }
*/  
} // public class JDiagramParam ...