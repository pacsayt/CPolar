package cpolar;
// JPillErtMegj.java


import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

// A BufferedImage generalasa miatt \/
public class JPillErtMegj extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152503L ;

  /**
   * @uml.property  name="m_aParamNevek" multiplicity="(0 -1)" dimension="1"
   */
  public final String  m_aParamNevek[]  = {
                                            IKonstansok.sSPD,
                                            IKonstansok.sCAD,
                                            IKonstansok.sHR,
                                            IKonstansok.sALT,
                                            IKonstansok.sDST,                          
//                                            "TIM",
                                            IKonstansok.sdT
                                          } ;

  // A parameterek kiirasanak indulopontjai tavolsaga
  /**
   * @uml.property  name="m_nParTav"
   */
  public final int m_nParTav = 40 ;
  
  /**
   * @uml.property  name="m_cBufferedImage"
   */
  protected BufferedImage m_cBufferedImage = null ;
  /**
   * @uml.property  name="m_cGraphics2D"
   */
  protected Graphics2D    m_cGraphics2D    = null ;
  /**
   * @uml.property  name="m_cFont"
   */
  protected Font          m_cFont          = null ;
  
  // A kovetkezo parametereket csak az image letrehozasa utan lehet megallapitani,
  // jollehet szuksegesek az image meretenek meghatarozasahoz :
  // az alanti kezdeti ertekeket az elso kirajzolas utan pontositom
  /**
   * @uml.property  name="m_nVonalFelett"
   */
  protected int m_nVonalFelett =  6 ;
  /**
   * @uml.property  name="m_nVonalalatt"
   */
  protected int m_nVonalalatt  =  3 ;
  /**
   * @uml.property  name="m_nSorkoz"
   */
  protected int m_nSorkoz      =  2 ;

  /**
   * @uml.property  name="m_nImageMagass"
   */
  protected int m_nImageMagass  = 0 ;
  /**
   * @uml.property  name="m_nImageSzeless"
   */
  protected int m_nImageSzeless = 0 ;
  // Fuggolegesen hol kezdodnek a pillanatnyi ertekek (= a grafikon alatt)
  /**
   * @uml.property  name="m_nImageY0"
   */
  protected int m_nImageY0      = 0 ;

  // Eloszor a maximalis fileszam szerint vesszuk a grafika meretet
//  protected int m_nFileo      = 2 ;

  public JPillErtMegj()
  {
    m_cFont = new Font( IKonstansok.sTimesNewRoman, Font.PLAIN, 11) ;
  }

  /*
   * A GrafikaLetrehoz( int nDiagrSzel)-bol kiveve, mert a BufferedImage-t
   * nem lehet a kijelzettekhez (nincs addolva) nem tartozo osztalybol letrehozni.
   * m_cBufferedImage = null esetben ujat kell letrehozni, mert valtozott a merete.
   */
  public void KezdoMeretBeallit( int nDiagrSzel, int nDiagrTeljMag)
  {
    m_nImageSzeless = nDiagrSzel ;
    
    // + 1 - a fejlecnek
    // Ujra szamolja, mert a GrafikaLetrehoz()-ban a fontmereteket aktualizalja (legeloszor)
    m_nImageMagass = (CPolar.m_fnMaxHrmFileSzam + 1)*(m_nVonalFelett + m_nVonalalatt + m_nSorkoz) ;
    
    // Jelzes, hogy uj BufferedImage-t kell letrehozni
    
    m_nImageY0 = nDiagrTeljMag - m_nImageMagass ;
  }
  
  /* 
   * Nem lehet egy nem csatlakoztatott JPanelbol BufferedImage-t letrehozni
   */
//  public void GrafikaLetrehoz( int nDiagrSzel)
  public void GrafikaLetrehoz( BufferedImage cBufferedImage) throws IllegalArgumentException
  {
    FontMetrics cFontMetrics = null ;
//System.out.println( "JPillErtMegj::GrafikaLetrehoz() BEGIN") ;
    
    if ( cBufferedImage == null )
    {
      throw new IllegalArgumentException() ;
    }
//    m_nImageSzeless = nDiagrSzel ;
//    m_nImageMagass  = CPolar.m_fnMaxHrmFileSzam*(m_nVonalFelett + m_nVonalalatt + m_nSorkoz) ;

    // Ez nem mukodik, ezert bemeno parameter 
//    m_cBufferedImage = (BufferedImage) createImage( m_nImageSzeless, m_nImageMagass) ;
    m_cBufferedImage = cBufferedImage ;

    m_cGraphics2D = m_cBufferedImage.createGraphics() ;
    
    m_cGraphics2D.setFont( m_cFont) ;
    
    cFontMetrics = m_cGraphics2D.getFontMetrics( m_cFont) ;
    
    // Utolag pontositva a kovetkezo kirajzolas szamara ...
    m_nVonalFelett = (int) cFontMetrics.getAscent()  ;
    m_nVonalalatt  = (int) cFontMetrics.getDescent() ;

    m_cGraphics2D.setBackground( Color.BLACK) ; //  getBackground()
    m_cGraphics2D.clearRect( 0, 0, m_nImageSzeless, m_nImageMagass) ;
    
//System.out.println( "JPillErtMegj::GrafikaLetrehoz() END") ;
  }

  public void PillErtKiir( int nTavolsag)
  {
//    final int m_nOszlSzel = 0 ;
    int       nIdokulonbseg = 0 ;

    int nIdx     = 0 ;
    int nFileIdx = 0 ;
    
    int nY    = 0 ;
//    int nAktY = 0 ;
    int nParamErt = 0 ;
    
    String sParamErt = null ;

    CParamTip cParTipIdx = null ;
    CHRMFile cLocHRMFile = null ;

//System.out.println( "JPillErtMegj::PillErtKiir() BEGIN ") ;

    // A soremeles nelkul beleir a kilometer osztasba
    nY = m_nImageY0 + m_nVonalFelett + m_nVonalalatt + m_nSorkoz ;
    
    cParTipIdx = new CParamTip() ;
    
    // A fejlec kiirasa : a parameterek nevei
    for ( nIdx = 0 ; nIdx < m_aParamNevek.length ; nIdx++ )
    {
      m_cGraphics2D.setColor( JDiagram.m_cParamColor[nIdx%CParamTip.eMax][0]) ;
      m_cGraphics2D.drawString( m_aParamNevek[nIdx], JDiagram.Surface.m_nGrafBalszele + m_nParTav*nIdx, nY) ;
    }
//System.out.println( "JPillErtMegj::PillErtKiir() A fejlec kiirasa utan") ; // ebbol a fv-bol nem bir kimaszni
    nY = m_nImageY0 + m_nVonalFelett + m_nVonalalatt + m_nSorkoz ; 

    // A tarolt parameterertekek kirajzolasa
//    for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eMax ; cParTipIdx.m_nAktParamTip++ )
    for ( nFileIdx = 0 ; nFileIdx < CPolar.m_fnMaxHrmFileSzam ; nFileIdx++ )
    {
//      nY = m_nImageY0 + m_nVonalFelett + m_nVonalalatt + m_nSorkoz ;
      nY = nY + m_nVonalFelett + m_nVonalalatt + m_nSorkoz ;

//System.out.println( "JPillErtMegj::PillErtKiir() cParTipIdx.m_nAktParamTip=" + Integer.toString( cParTipIdx.m_nAktParamTip)) ;

//      for ( nFileIdx = 0 ; nFileIdx < CPolar.m_fnMaxHrmFileSzam ; nFileIdx++ )
      for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eMax ; cParTipIdx.m_nAktParamTip++ )
      {
//System.out.println( "JPillErtMegj::PillErtKiir() HrmFileSzam=" + Integer.toString( nIdx)) ;
//        nY = nY + m_nVonalFelett + m_nVonalalatt + m_nSorkoz ;

        cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nFileIdx] ;

        if ( cLocHRMFile != null && cLocHRMFile.GetKijelzendo() == true && cLocHRMFile.VanParamAdat( cParTipIdx) )
        {
          //                                            \/ ???
          nIdx = cLocHRMFile.MeterBolIndex( nTavolsag /* + cLocHRMFile.GetEltTav()*/) ;
          
          if ( nIdx != -1 )
          {
            nParamErt = cLocHRMFile.GetParamAt( cParTipIdx, nIdx) ; // throws ArrayIndexOutOfBoundsException
            
            m_cGraphics2D.setColor( JDiagram.m_cParamColor[cParTipIdx.m_nAktParamTip%CParamTip.eMax][0]) ;

            // A tavolsagot le kell osztani, hogy km-ben kapjuk
            if ( cParTipIdx.m_nAktParamTip == CParamTip.eDistance )
            {
              sParamErt = FloatBolString( (float)nParamErt/CHRMFile.m_nDistBolKm) ;
              m_cGraphics2D.drawString( sParamErt, JDiagram.Surface.m_nGrafBalszele + m_nParTav*cParTipIdx.m_nAktParamTip, nY) ;
//              nParamErt = nParamErt/m_nDistBolKm ;
            }
            else
            {
              m_cGraphics2D.drawString( Integer.toString( nParamErt), JDiagram.Surface.m_nGrafBalszele + m_nParTav*cParTipIdx.m_nAktParamTip, nY) ;
            }
          }
        }
      }
      
      // A problemas eseteket lekezeli (=0)
      nIdokulonbseg = IdoKulKiszamol( nTavolsag, nFileIdx) ;
      
      sParamErt = Integer.toString( nIdokulonbseg) ;
      m_cGraphics2D.drawString( sParamErt, JDiagram.Surface.m_nGrafBalszele + m_nParTav*(CParamTip.eMax), nY) ;
    }
//System.out.println( "JPillErtMegj::PillErtKiir() END") ;
  }

//--------------------------------------------------------------------------------------
  protected int IdoKulKiszamol( int nAktDistance, int nFileIndex)
  {
    int      nAktivKezdIdo = 0    ;
    int      nAktivEltTav  = 0    ;
    int      nMasikKezdIdo = 0    ;
    int      nMasikEltIdo  = 0    ;
    int      nMasikEltTav  = 0    ;
    int      nIdokulonbseg = 0    ;
    CHRMFile cAktHRMFile   = null ;
    CHRMFile cMasikHRMFile = null ;

    // A rovidites kedveert
    cAktHRMFile = IMFActionListener.m_cPolar.AktivFile() ;

    if ( cAktHRMFile == null ||
    //               cAktHRMFile.GetKijelzendo() == false || // Az aktiv file mindig kijelzendo
         cAktHRMFile.VanSpeedAdat()  == false    )
    {
      return nIdokulonbseg ;
    } 

    nAktivEltTav  = cAktHRMFile.GetEltTav() ;
    nAktivKezdIdo = cAktHRMFile.MeterbolEltIdo( nAktivEltTav) ;

    // Nem minden tombelem betoltott, es sajat magaval se kepezzen kulonbseget, es kijelzendo legyen
    if ( IMFActionListener.m_cPolar.m_aHRMFile != null &&
         IMFActionListener.m_cPolar.m_aHRMFile[nFileIndex] != null &&
         IMFActionListener.m_cPolar.m_aHRMFile[nFileIndex] != cAktHRMFile &&
         IMFActionListener.m_cPolar.m_aHRMFile[nFileIndex].GetKijelzendo() == true )
    {
      cMasikHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nFileIndex] ;          

      nMasikEltTav  = cMasikHRMFile.GetEltTav() ;
      nMasikKezdIdo = cMasikHRMFile.MeterbolEltIdo( nMasikEltTav) ;          

      try
      {
        // Ezt kellene lecsekkolni, ha egyszer hiba volt kiugrani a for ciklusbol, mert kul nem megy vegig
        nMasikEltIdo = cMasikHRMFile.MeterbolEltIdo( nAktDistance - nAktivEltTav + nMasikEltTav) ; // ???

        if ( nMasikEltIdo == -1 )
        {
//System.out.println( "IdoKulKiszamol(): nIdokulonbseg=" + Integer.toString( nIdokulonbseg)) ;    
          return nIdokulonbseg ;
        }

        nIdokulonbseg = (cAktHRMFile.MeterbolEltIdo( nAktDistance) - nAktivKezdIdo) -
                        (nMasikEltIdo - nMasikKezdIdo) ;
      }
      catch ( ArrayIndexOutOfBoundsException eArrayIndexOutOfBoundsException)
      {
        System.out.println( "IdoKulKirajzol(): Caught ArrayIndexOutOfBoundsException") ;

        return nIdokulonbseg ;
      }
    }

//System.out.println( "IdoKulKiszamol(): nIdokulonbseg=" + Integer.toString( nIdokulonbseg)) ;    
    return nIdokulonbseg ;
  }
  
  int GetMagass()
  {
    return m_nImageMagass ;
  }
  
  public BufferedImage GetBufferedImage()
  {
    return m_cBufferedImage ;
  }
  
  String FloatBolString( float fSzam)
  {
    int    nPontPoz = 0 ;
    
    String sSzam        = null ;
    String sSzamKetTiz  = null ;
    
    sSzam = new String() ;
    sSzamKetTiz = new String() ;
    
    sSzam = Float.toString( fSzam) ;
//System.out.println( "JPillErtMegj::PillErtKiir() BEGIN : sSzam = " + sSzam) ;

    nPontPoz = sSzam.lastIndexOf( '.') ;
//System.out.println( "JPillErtMegj::PillErtKiir() nPontPoz = " + Integer.toString( nPontPoz)) ;
    
    // -1 ha a kerdeses sztringet nem talalta
    if ( nPontPoz != -1 )
    {
      nPontPoz = nPontPoz + 3 ;
      
      if ( nPontPoz > sSzam.length() )
      {
        nPontPoz = sSzam.length() ;
      }
      
      sSzamKetTiz = sSzam.substring( 0, nPontPoz) ;
    }
    else
    {
      sSzamKetTiz = sSzam ;
    }

//System.out.println( "JPillErtMegj::PillErtKiir() END : sSzamKetTiz = " + sSzamKetTiz) ;
    
    return sSzamKetTiz ;
  }
}