package cpolar;
// JFileValasztDlg.java hiba
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

/* Ugy tunik nincs hasznalva ... <T> hozzaadva (warning kulonben)
class CFilenevOh<T> implements Comparator<T>
{
  public int compare(Object cObject1, Object cObject2)
  {
//System.out.println( "CFilenevOh.compare() ") ;

    // Csak sztringeket akarunk osszehasonlitani
    if ( !(cObject1 instanceof String) || !(cObject2 instanceof String) )
    {
      return -1 ;
    }

    if ( cObject1 == cObject2 )
    {
      return 0 ;
    }
    
    return ((String)cObject1).compareTo( (String)cObject2) ;
  }

  public boolean equals(Object cObject)
  {
    return false ;
  }
}
*/

// CProgInfoDlg.java : CDlgKilepListener ???
class CKvtNevValtLsnr implements KeyListener // KeyAdapter
{
  public CKvtNevValtLsnr( JFileValasztDlg iniFileValasztDlg, JTextField iniKonyvtarTxtFld)
  {
    m_jFileValasztDlg = iniFileValasztDlg ;
    m_jKonyvtarTxtFld = iniKonyvtarTxtFld ;
  }

  JFileValasztDlg m_jFileValasztDlg = null ;
  JTextField      m_jKonyvtarTxtFld = null ;

  @Override
  public void keyPressed( KeyEvent cIniKeyEvent)
  {
//    String sKonyvtar = null ;

//System.out.println( "CKvtNevValtLsnr.keyPressed() " + cIniKeyEvent.paramString()) ;
//      sKonyvtar = m_jKonyvtarTxtFld.getText() ;

//System.out.println( "CKvtNevValtLsnr.keyPressed() " + sKonyvtar) ;
//System.out.println("CKvtNevValtLsnr.keyPressed()sKonyvtar[sKonyvtar.length()-1] = " + sKonyvtar.charAt(sKonyvtar.length()-1)) ;
  }
  
  @Override
  public void keyReleased( KeyEvent cIniKeyEvent)
  {
    String sKonyvtar = null ;

//    int nID = 0 ;
//    char cBillentyu = '\0' ;

//System.out.println( "CKvtNevValtLsnr.keyReleased() " + cIniKeyEvent.paramString()) ;

//    nID = cIniKeyEvent.getID() ;
    
//    if ( nID == KeyEvent.KEY_TYPED )
//    {
//      cBillentyu = cIniKeyEvent.getKeyChar() ;
//    }

    if ( m_jKonyvtarTxtFld != null )
    {
      sKonyvtar = m_jKonyvtarTxtFld.getText() ;

//System.out.println( "CKvtNevValtLsnr.keyReleased() " + sKonyvtar) ;
//System.out.println("CKvtNevValtLsnr.keyReleased()sKonyvtar[sKonyvtar.length()-1] = " + sKonyvtar.charAt(sKonyvtar.length()-1)) ;

      if ( sKonyvtar.charAt(sKonyvtar.length()-1) == '/' ||
           sKonyvtar.charAt(sKonyvtar.length()-1) == '\\' )
      {
        m_jFileValasztDlg.KvtTartListFrissit( sKonyvtar) ;
      }
    }

//System.out.println( "CKvtNevValtLsnr.keyReleased() " + sKonyvtar) ;
//System.out.println("CKvtNevValtLsnr.keyReleased()sKonyvtar[sKonyvtar.length()-1] = " + sKonyvtar.charAt(sKonyvtar.length()-1)) ;
  }
  
  @Override
  public void keyTyped( KeyEvent cIniKeyEvent)
  {
/* Athelyezve a keyReleased()-be
    String sKonyvtar = null ;

    int nID = 0 ;
    char cBillentyu = '\0' ;
    
    nID = cIniKeyEvent.getID() ;
    
    if ( nID == KeyEvent.KEY_TYPED )
    {
      cBillentyu = cIniKeyEvent.getKeyChar() ;
    }

    if ( m_jKonyvtarTxtFld != null )
    {
      sKonyvtar = m_jKonyvtarTxtFld.getText() ;

System.out.println( "CKvtNevValtLsnr.keyTyped() " + sKonyvtar) ;
System.out.println("CKvtNevValtLsnr.keyTyped()sKonyvtar[sKonyvtar.length()-1] = " + sKonyvtar.charAt(sKonyvtar.length()-1)) ;

      if ( sKonyvtar.charAt(sKonyvtar.length()-1) == '/' ||
           sKonyvtar.charAt(sKonyvtar.length()-1) == '\\' )
      {
        m_jFileValasztDlg.KvtTartListFrissit( sKonyvtar) ;
      }
    }
*/
  }
}

class CKvtTartSzelLsnr implements ListSelectionListener
{
  public CKvtTartSzelLsnr( JFileValasztDlg iniFileValasztDlg)
  {
    m_jFileValasztDlg  = iniFileValasztDlg ;
    m_jKvtTartList     = m_jFileValasztDlg.m_jKvtTartList ;
    m_jFileTartTxtArea = m_jFileValasztDlg.m_jFileTartTxtArea ;
  }

  JFileValasztDlg m_jFileValasztDlg  = null ;
  JList           m_jKvtTartList     = null ;
  JTextArea       m_jFileTartTxtArea = null ;
  CHRMFile        m_cHRMFile         = new CHRMFile() ;
  
  protected void FileTartKiir()
  {
//    String sSzoveg = new String() ;
    String sSzoveg = null ;
    
    sSzoveg = m_cHRMFile.GetDatum() + "\n" ;
    sSzoveg = sSzoveg + m_cHRMFile.GetMegjegyzes() + "\n" ;
    
    sSzoveg = sSzoveg + IKonstansok.sVanSpeedAdat     + m_cHRMFile.VanSpeedAdat()     + "\n" ;
    sSzoveg = sSzoveg + IKonstansok.sVanCadenceAdat   + m_cHRMFile.VanCadenceAdat()   + "\n" ;
    sSzoveg = sSzoveg + IKonstansok.sVanHeartRateAdat + m_cHRMFile.VanHeartRateAdat() + "\n" ;
    sSzoveg = sSzoveg + IKonstansok.sVanAltitudeAdat  + m_cHRMFile.VanAltitudeAdat()  + "\n" ;
    sSzoveg = sSzoveg + IKonstansok.sGetInterval      + m_cHRMFile.GetInterval()             ;
    
//    m_jFileTartTxtArea.append( sSzoveg) ;
    m_jFileTartTxtArea.setText( sSzoveg) ;
  }
  
  protected void KonyvtarTartKiir( File cAktKvt)
  {
    if ( cAktKvt != null )
    {
      m_jFileValasztDlg.KonyvtarTartKiir( cAktKvt) ;
    }
  }
  
  @Override
  public void valueChanged( ListSelectionEvent cListSelectionEvent)
  {
    File    cAktKvt = null ;
    Object  aKivElemek[] ;
    
    boolean bRC = false ;
//System.out.println( "JFileValasztDlg.valueChanged()") ;

    try
    {
      aKivElemek = m_jKvtTartList.getSelectedValues() ;

      if ( aKivElemek.length == 1 )
      {
        cAktKvt = new File( m_jFileValasztDlg.GetAktKonyvtar() + System.getProperty( "file.separator") + aKivElemek[0].toString()) ;
        
        if ( cAktKvt != null && cAktKvt.isDirectory() == true )
        {
          KonyvtarTartKiir( cAktKvt) ;
        }
        else
        {
          m_cHRMFile.FileMegnyit( m_jFileValasztDlg.GetAktKonyvtar(), aKivElemek[0].toString()) ;
      
          bRC = m_cHRMFile.KonfiguracioBeolv() ;
    
          if ( bRC == true )
          {
            FileTartKiir() ;
	  
            if ( m_jFileValasztDlg != null )
            {
              m_jFileValasztDlg.SetAktHRMFile( aKivElemek[0].toString()) ;
            }
          }
        }
/*
for(int i = 0; i < aKivElemek.length; i++)
{
System.out.println( "CKvtTartSzelLsnr.valueChanged() " + i + aKivElemek[i].toString()) ;
}
*/
      }
    }
    catch ( IOException eIOException)
    {
    
    }
    
    cAktKvt = null ;
  }
}  

// A listaba (m_jKvtTartList) valo kettos klikkeles (beleugras a konyvtarba) lekezelese
class KvtTartList2KlikkListener extends MouseAdapter
{
  public KvtTartList2KlikkListener( JFileValasztDlg jIniFileValasztDlg)
  {
    m_jFileValasztDlg = jIniFileValasztDlg ;
    m_jKvtTartList    = m_jFileValasztDlg.m_jKvtTartList ;
  }

  JFileValasztDlg m_jFileValasztDlg = null ;
  JList           m_jKvtTartList    = null ;

  @Override
  public void mouseClicked(MouseEvent cMouseEvent) 
  {
//int nIdx = 0 ;
    
    if ( m_jKvtTartList != null && cMouseEvent.getClickCount() == 2 ) 
    {
//nIdx = m_jKvtTartList.locationToIndex( cMouseEvent.getPoint()) ;
//System.out.println("Double clicked on Item " + nIdx);

      m_jFileValasztDlg.KivKvtbaValt() ;
    }
  }
}

class COnOKListener implements IMFActionListener
{
  public COnOKListener( JFileValasztDlg jIniFileValasztDlg)
  {
    m_jFileValasztDlg = jIniFileValasztDlg ;
  }

  JFileValasztDlg m_jFileValasztDlg = null ;

  @Override
  public void actionPerformed( ActionEvent e) // throws IOException
  {
    String cEleresiUtvonal  = null ;
    String cMegnyitandoFile = null ;

    cEleresiUtvonal  = m_jFileValasztDlg.GetAktKonyvtar() ;
    cMegnyitandoFile = m_jFileValasztDlg.GetAktHRMFile()  ;

    m_jFileValasztDlg.dispose() ;

    m_cPolar.UjHRMFileHozzaad( cEleresiUtvonal, cMegnyitandoFile) ;
    
    m_cPolar.m_sAktKvt = cEleresiUtvonal ;
  }
}

class COnCancelListener implements ActionListener
{
  public COnCancelListener( JFileValasztDlg jIniFileValasztDlg)
  {
    m_jFileValasztDlg = jIniFileValasztDlg ;
  }

  JFileValasztDlg m_jFileValasztDlg = null ;

  @Override
  public void actionPerformed( ActionEvent e)
  {
    m_jFileValasztDlg.dispose() ;
  }
}

//---------------------------------------------------------------------
//    Panel cLblPnl = new Panel() ;
//    cLblPnl.setLayout( new GridLayout( 3, 1)) ;

class JFileValasztDlg extends JDialog
{
  public JFileValasztDlg( Frame parent)
  {
    super( parent, IKonstansok.sHRMFileokMegny, false) ;

    JPanel      jAktKvtPanel = null ;
    JPanel      jGombPanel   = null ;
    Container   jContentPane = null ;
    JScrollPane jScrollPane  = null ;
    Font        cFont        = null ;
    
    File jFile = null ;
    
    // Neha az ablak ugy indul, hogy egy kontrol sem latszik - most is ...
    setSize( new Dimension( 920, 780)) ;  
    
    // The default layout for the contentPane is BorderLayout.
    jContentPane = (JComponent) getContentPane() ;

    // Az aktualis konyvtar : cimke es a JTextField
    m_jAktKvtLabel.setForeground( Color.black)     ;
    m_jAktKvtLabel.setBackground( Color.lightGray) ;

    // A font lemasolasa egy masik kontrolrol, mert a text fielde elter
    cFont = m_jKonyvtarTxtFld.getFont() ;
    m_jAktKvtLabel.setFont( cFont) ;

    m_jKonyvtarTxtFld.setHorizontalAlignment( JTextField.LEFT) ;
    jAktKvtPanel = new JPanel() ;

    // ---- Kulon panel az aktualis konyvtar cimkenek es TextBoxnak.
    // Majd valahogy meg kell oldani, hogy megfeleloen ossza be a helyet a cimke es a TextBoxnak kozott
    jAktKvtPanel.setLayout( new BoxLayout( jAktKvtPanel, BoxLayout.X_AXIS)) ;
    jAktKvtPanel.setBorder(new EmptyBorder( 10, 10, 10, 10)) ;
    jAktKvtPanel.add( m_jAktKvtLabel) ;
    // Hogy a lebel es a szovegmezo kozt konstans meretu hely legyen
    jAktKvtPanel.add( Box.createHorizontalStrut( 10)) ;
    jAktKvtPanel.add( m_jKonyvtarTxtFld) ;

    jContentPane.add( jAktKvtPanel, BorderLayout.NORTH) ;

    // ---- Az aktualis konyvtar tartalma : 
    m_jKvtTartList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION) ;
    m_jKvtTartList.setBackground( getBackground()) ;

    jScrollPane = new JScrollPane( m_jKvtTartList) ;
    jScrollPane.setBorder(new EmptyBorder( 10, 10, 10, 10)) ;

    jContentPane.add( jScrollPane, BorderLayout.WEST) ;

    // ---- Az aktualis file (konyvtar) tartalma : 
    m_jFileTartTxtArea.setLineWrap( true) ;
    m_jFileTartTxtArea.setFont( cFont) ;
    m_jFileTartTxtArea.setForeground( Color.black) ;
    m_jFileTartTxtArea.setBackground( Color.white) ;
    m_jFileTartTxtArea.setEditable( false) ;
    m_jFileTartTxtArea.setBorder(new EmptyBorder( 10, 10, 10, 10)) ;

    jContentPane.add( m_jFileTartTxtArea, BorderLayout.CENTER) ;

    // ---- Az OK es Cancel gombok
    jGombPanel = new JPanel( new FlowLayout()) ;
    jGombPanel.setBorder(new EmptyBorder( 10, 10, 10, 10)) ;
    jGombPanel.add( m_jOKButton) ;
    jGombPanel.add( m_jCancelButton) ;
    jContentPane.add( jGombPanel, BorderLayout.SOUTH) ;

    // ---- Az esemenykezelok hozzadasa
    m_jKonyvtarTxtFld.addKeyListener( new CKvtNevValtLsnr( this, m_jKonyvtarTxtFld)) ;
    m_jKvtTartList.addListSelectionListener( new CKvtTartSzelLsnr( this)) ;
    m_jKvtTartList.addMouseListener( new KvtTartList2KlikkListener( this)) ;

    m_jOKButton.addActionListener(     new COnOKListener(     this)) ;
    m_jCancelButton.addActionListener( new COnCancelListener( this)) ;

    jFile = new File( ((CPolar)parent).m_sAktKvt) ;
    
    if ( jFile != null )
    {
      // jFile.getAbsolutePath()
      KvtTartListFrissit( IKonstansok.sKezdoKonyvtar) ;
    }
    else
    {
      KvtTartListFrissit( ((CPolar)parent).m_sAktKvt) ;
    }
  }

  public Object[] HRMFileSzuro( File cKonyvtar)
  {
    int    nIdx = 0 ;
    int    nExtLen = 0 ;
    int    nKiterjPoz = 0 ;
    File   cTmpFile /*= null*/ ;
//    SecurityManager cSecurityManager = null ;
    String aKvtTartalma[]   /*= null*/ ;
    ArrayList<String> cKvtrakFileok /*= null*/ ;
    ArrayList<String> cFileok       /*= null*/ ;
//    Iterator  cIterator    = null ;
    Object aObject[]        = (Object[])null ;
    
//System.out.println( "HRMFileSzuro() BEGIN") ;

    cKvtrakFileok = new ArrayList<String>() ;
    cFileok       = new ArrayList<String>() ;

    nExtLen = m_sFileKiterj.length() ;
//System.out.println( "HRMFileSzuro() nExtLen = m_sFileKiterj.length() utan") ;
    
//    cSecurityManager = System.getSecurityManager() ;
   
    aKvtTartalma = cKonyvtar.list() ;
//System.out.println( "HRMFileSzuro() aKvtTartalma = cKonyvtar.list() utan") ;

//    Arrays.sort( aKvtTartalma, new CFilenevOh()) ;
    // A szulo konyvtar hozzaadasa
    cKvtrakFileok.add( "..") ;
    
    if ( aKvtTartalma != null )
    {
//System.out.println( "HRMFileSzuro() : aKvtTartalma.length=" + aKvtTartalma.length) ;
      for ( nIdx = 0 ; nIdx < aKvtTartalma.length ; nIdx++)
      {
//        {
          // El kell donteni, konyvtar, vagy file
//System.out.println( "HRMFileSzuro() : aKvtTartalma[nIdx]=" + aKvtTartalma[nIdx]) ;
          try
          {
          // -> SecurityException, NullPointerException
//          cSecurityManager.checkRead( aKvtTartalma[nIdx]) ; // -> NullPointerException
	          // a path itt mindig csak a file neve 
            cTmpFile = new File( cKonyvtar + System.getProperty( "file.separator") + aKvtTartalma[nIdx]) ;

//System.out.println( "HRMFileSzuro() : new File() utan") ;

            // Kulon a konyvtarakat, kulon a file-okat, mert elobb a kvtrakat irja ki
            if ( cTmpFile.isDirectory() )
            {
//System.out.println( "HRMFileSzuro() : kvt : " + aKvtTartalma[nIdx]) ;
              cKvtrakFileok.add( aKvtTartalma[nIdx]) ;
            }
            else
            {
//System.out.println( "HRMFileSzuro() : file : " + aKvtTartalma[nIdx]) ;
              nKiterjPoz = aKvtTartalma[nIdx].indexOf( IKonstansok.sOlvFileKiterj, aKvtTartalma[nIdx].length()-nExtLen-1) ;

//              System.out.println( "HRMFileSzuro() : aKvtTartalma[nIdx], nKiterjPoz=" + aKvtTartalma[nIdx] + nKiterjPoz) ;
              	
              if ( nKiterjPoz != -1 )
              {
                cFileok.add( aKvtTartalma[nIdx]) ;
              }
            }
          }
          catch( SecurityException cSecurityException)
          {
//System.out.println( "HRMFileSzuro() : SecurityException") ;
          }
          catch( NullPointerException cNullPointerException)
          {
//System.out.println( "HRMFileSzuro() : NullPointerException") ;
            ExceptionTrace( (Exception) cNullPointerException) ;
          }      
//        }
      } // for

      // A konyvtarak mar benne vannak, most a file-ok is ...
      cKvtrakFileok.addAll( cFileok) ;

//System.out.println( "HRMFileSzuro() : cKvtrakFileok.size() = " + cKvtrakFileok.size()) ;

      aObject = cKvtrakFileok.toArray() ;
/*
System.out.println( "HRMFileSzuro() : aObject.length = " + aObject.length) ;

for ( int i=0 ; i < aObject.length ; i++ )
{
System.out.println( "HRMFileSzuro() : cKvtrakFileok[" + i + "]=" + aObject[i].toString() ) ;
}
*/
    }
    
    return aObject ;
//Iterator i = cKvtrakFileok.iterator() ;
//while (cKvtrakFileok.hasNext())
//  cKvtrakFileok.getNext() ;
  }

  public void KvtTartListFrissit( String sKonyvtar)
  {
//    int    nIdx = 0 ;
    File   cAktKvt        = null ;
    Object aKvtTartalma[] = null ;

//System.out.println( "JFileValasztDlg.KvtTartListFrissit( " + sKonyvtar + ")") ;

//    cAktKvt = new File( ".") ;
//    cAktKvt = new File( "/home/tamas/java/Polar/hrmfiles") ;
    cAktKvt = new File( sKonyvtar) ;

//System.out.println( "JFileValasztDlg.KvtTartListFrissit cAktKvt = new File() utan") ;

    // Az aktualis konyvtar teljes utvonalanak beirasa
// Itt nyeli le a vegerol a \-t vagy /-t :
//    m_jKonyvtarTxtFld.setText( cAktKvt.getAbsolutePath()) ; // getAbsolutePath()
    m_jKonyvtarTxtFld.setText( sKonyvtar) ;

//System.out.println( "JFileValasztDlg.KvtTartListFrissit m_jKonyvtarTxtFld.setText( cAktKvt.getAbsolutePath()) utan") ;
    if ( cAktKvt != null && cAktKvt.isDirectory() == true )
    {
      aKvtTartalma = HRMFileSzuro( cAktKvt) ;
//System.out.println( "JFileValasztDlg.KvtTartListFrissit() : HRMFileSzuro() utan") ;

      if ( aKvtTartalma != null )
      {
/*
for ( int i=0 ; i < aKvtTartalma.length ; i++ )
{
System.out.println( "JFileValasztDlg.KvtTartListFrissit() : cKvtrkFileok[" + i + "]=" + aKvtTartalma[i].toString()) ;
}
*/
        m_jKvtTartList.setListData( aKvtTartalma) ;
//System.out.println( "JFileValasztDlg.KvtTartListFrissit() : .setListData() utan") ;

        // Itt mar valoszinu, hogy a ListBox a konyvtar file-jait tartalmazza +++
        SetAktKonyvtar( sKonyvtar) ;

        m_jKvtTartList.setSelectedIndex( 0) ;
        
//System.out.println( "JFileValasztDlg.KvtTartListFrissit() : SetAktKonyvtar() utan") ;
//System.out.println( "JFileValasztDlg.KvtTartListFrissit() : setListData") ;
/*
    for ( nIdx = 0 ; nIdx < aKvtTartalma.length ; nIdx++ )
    {
      m_jKvtTartList.setListData( aKvtTartalma) ;
    }
*/
      }
    }
  }

  protected void KonyvtarTartKiir( File cAktKvt)
  {
    int    nIdx = 0 ;
    Object aKivElemek[] ;
    String sSzoveg = null ;
    
    if ( cAktKvt != null )
    {
      aKivElemek = HRMFileSzuro( cAktKvt) ;
      
      sSzoveg = "" ;
      
      for ( nIdx = 0 ; nIdx < aKivElemek.length ; nIdx++)
      {
        sSzoveg = sSzoveg + aKivElemek[nIdx].toString() + "\n" ;
      }
      
      m_jFileTartTxtArea.setText( sSzoveg) ;
    }
  }

  // Az eppen kivalasztott (m_jKvtTartList) konyvtarba valo bevaltas, ha eppen
  // konyvtar van m_jKvtTartList-ben kivalasztva 
  public void KivKvtbaValt()
  {
//    int    nIdx       = 0 ;
//    int    nExtLen    = 0 ;
//    int    nKiterjPoz = 0 ;
    File   cAktKvt    = null ;
    Object aKivElemek[] ;
    
//    boolean bRC = false ;
//System.out.println( "JFileValasztDlg::KivKvtbaValt()") ;

    try
    {
      aKivElemek = m_jKvtTartList.getSelectedValues() ;

      // Ha netan tobb lenne kivalasztva (bas single selection lett beallitva) ...
      if ( aKivElemek.length > 0 )
      {
        cAktKvt = new File( m_sAktKonyvtar /*GetAktKonyvtar()*/ + System.getProperty( IKonstansok.sFileSeparator) + aKivElemek[0].toString()) ;
        
        if ( cAktKvt != null && cAktKvt.isDirectory() == true )
        {
          KvtTartListFrissit( cAktKvt.getCanonicalPath()) ;
//          KonyvtarTartKiir( cAktKvt) ; ez csak pont a listaablakot nem frissiti !
          
          // A felso edit kontorlba valo beiras hianyzik !
// E:\TAMAS\PROG\JAVA\ecl_wrkspc\CPolar\2003 v. E:\TAMAS\PROG
//System.out.println( "JFileValasztDlg::KivKvtbaValt() cAktKvt.getCanonicalPath() : " + cAktKvt.getCanonicalPath()) ;
// E:\TAMAS\PROG\JAVA\ecl_wrkspc\CPolar\.\2003 v. E:\TAMAS\PROG\JAVA\..
//System.out.println( "JFileValasztDlg::KivKvtbaValt() cAktKvt.getAbsolutePath() : " + cAktKvt.getAbsolutePath()) ;          
//m_jKonyvtarTxtFld.setText( cAktKvt.getCanonicalPath()) ; ??? <-> KvtTartListFrissit()
        }
      }
    }
    catch( IOException cIOException)
    {
//System.out.println( "JFileValasztDlg::KivKvtbaValt() : cIOException") ;
      ExceptionTrace( (Exception) cIOException) ;
    }
    catch( SecurityException cSecurityException)
    {
//System.out.println( "JFileValasztDlg::KivKvtbaValt() : SecurityException") ;
      ExceptionTrace( (Exception) cSecurityException) ;
    }
    catch( NullPointerException cNullPointerException)
    {
//System.out.println( "JFileValasztDlg::KivKvtbaValt() : NullPointerException") ;
      ExceptionTrace( (Exception) cNullPointerException) ;
    }      
  }

  public void ExceptionTrace( Exception eException)
  {
    System.out.println( "Caught Exception") ;
    System.out.println( "getMessage(): " + eException.getMessage()) ;
    System.out.println( "toString(): " + eException.toString()) ;
    System.out.println( "printStackTrace():") ;
    eException.printStackTrace() ;
  }

  public String GetAktKonyvtar()
  {
    return m_sAktKonyvtar ;
  }

  public void SetAktKonyvtar( String iniKonyvtar)
  {
    m_sAktKonyvtar = iniKonyvtar ;
  }

  public String GetAktHRMFile()
  {
    return m_sAktHRMFile ;
  }

  public void SetAktHRMFile( String iniHRMFile)
  {
    m_sAktHRMFile = iniHRMFile ;
  }

  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152502L ;

  // A m_jFileTartTxtArea szamara. Ebbol szamitodik a "preferred size"
  final int m_nFileTartSorok = 30 ;
  final int m_nFileTartOszlp = 80 ;
  
  public final String m_sFileKiterj  = IKonstansok.sOlvFileKiterj ;

  // ~/java/demo/jfc/SwingSet2/src/DemoModule.java :
  public static Dimension HGAP2  = new Dimension(2,1)  ;
  public static Dimension VGAP2  = new Dimension(1,2)  ;
  public static Dimension HGAP5  = new Dimension(5,1)  ;
  public static Dimension VGAP5  = new Dimension(1,5)  ;
  public static Dimension HGAP10 = new Dimension(10,1) ;
  public static Dimension VGAP10 = new Dimension(1,10) ;
  public static Dimension HGAP15 = new Dimension(15,1) ;
  public static Dimension VGAP15 = new Dimension(1,15) ;
  public static Dimension HGAP20 = new Dimension(20,1) ;
  public static Dimension VGAP20 = new Dimension(1,20) ;
  public static Dimension HGAP25 = new Dimension(25,1) ;
  public static Dimension VGAP25 = new Dimension(1,25) ;
  public static Dimension HGAP30 = new Dimension(30,1) ;
  public static Dimension VGAP30 = new Dimension(1,30) ;

  // A kontrolok fuggoleges elhelyezeset biztosito layout manager
  Box m_cBoxLayout = new Box( BoxLayout.Y_AXIS) ;

  // 
  JLabel     m_jAktKvtLabel     = new JLabel( IKonstansok.sAktKvt, JLabel.LEFT) ;

  // A beolvasando adatokat tartalmazo TextField-ek
  JTextField m_jKonyvtarTxtFld  = new JTextField( ) ;
  public JList m_jKvtTartList   = new JList( )      ;
  JTextArea  m_jFileTartTxtArea = new JTextArea( m_nFileTartSorok, m_nFileTartOszlp) ;

  JButton m_jOKButton     = new JButton( IKonstansok.sOK)     ;
  JButton m_jCancelButton = new JButton( IKonstansok.sCancel) ;
  
  // Hogy biztos lehessek benne, hogy amikor raklikkelnek egy file-ra az LB-ban, az
  // aktualis 
  protected String  m_sAktKonyvtar = new String() ;
  // Az Ok gomb megnyomasara ezt a filenevet es az m_cAktKonyvtar-t adja vissza
  protected String  m_sAktHRMFile  = new String() ;
  
//  CKnvIndtDlgKlpListener  m_cKnvIndtDlgKilepListener = new CKnvIndtDlgKlpListener( this) ;
}


/*
    static private FileSystem fs = FileSystem.getFileSystem();
...
    public boolean isDirectory()
    {
        SecurityManager security = System.getSecurityManager();
        if (security != null)
	{
            security.checkRead(path); -> SecurityException, NullPointerException
        }
        return ((fs.getBooleanAttributes(this) & FileSystem.BA_DIRECTORY) != 0);
    }
    public File[] listFiles(FileFilter filter)
    {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList v = new ArrayList();
        for (int i = 0 ; i < ss.length ; i++)
	{
            File f = new File(this.path, ss[i]);
            if ((filter == null) || filter.accept(f))
	    {
                v.add(f);
            }
        }
        return (File[])(v.toArray(new File[0]));
    }
*/
