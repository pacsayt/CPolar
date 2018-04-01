package cpolar;
// JDiagram.java
/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

//import CPolar.source.CHRMFile ;

/*
 *  Probalkozas, hatha a grafika is kepes kozvetlen parancsokat fogadni
 *  De nem.
class CKeyListener implements KeyListener
{
  // Invoked when a key has been typed.
  public void keyTyped( KeyEvent e)
  {
    System.out.println( "CKeyListener::keyTyped()") ;     
  }

  // Invoked when a key has been pressed.
  public void keyPressed( KeyEvent e)
  {
    System.out.println( "CKeyListener::keyPressed()") ;
  }

  // Invoked when a key has been released.
  public void keyReleased( KeyEvent e)
  {
    System.out.println( "CKeyListener::keyReleased()") ;
  }  
}

class CKeyAdapter extends KeyAdapter
{
  CKeyAdapter()
  {
    
  }
  
  // Invoked when a key has been typed.
  public void keyTyped( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyTyped()") ;     
  }

  // Invoked when a key has been pressed.
  public void keyPressed( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyPressed()") ;
  }

  // Invoked when a key has been released.
  public void keyReleased( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyReleased()") ;
  }  
}
*/
/**
 * Tracks Memory allocated & used, displayed in graph form.
 */
public class JDiagram extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152497L ;

  public final static Color m_cParamColor[][] = {
    { // m_cSpdColor 
       new Color(   0,   0, 255),
       new Color(  10,  10, 205),
       new Color(  15,  15, 155),
       new Color(  20,  20, 105),
       new Color(  25,  25,  55)
    },
    {  // m_cCadColor
       new Color(   0, 255,   0),
       new Color(  10, 205,  10),
       new Color(  15, 155,  15),
       new Color(  20, 105,  20),
       new Color(  25,  55,  25)
    },
    {  // m_cHRColor
       new Color( 255,   0,   0),
       new Color( 205,  10,  10),
       new Color( 155,  15,  15),
       new Color( 105,  20,  20),
       new Color(  55,  25,  25)
    },
    { // m_cAltColor 
       new Color( 255, 140, 140),
       new Color( 205, 130, 130),
       new Color( 155, 120, 120),
       new Color( 105, 110, 110),
       new Color(  55, 100, 100)
    },
/*                                                     { // m_cDstColor idokulonbsegre hasznalva
       new Color(  10, 200, 220),
       new Color(  30, 180, 200),
       new Color(  50, 160, 180),
       new Color(  70, 140, 160),
       new Color(  50, 120, 140)
    }
*/
    {
       new Color(   0,   0, 255),
       new Color(   0, 255,   0),
       new Color( 255,   0,   0),
       new Color(  70, 140, 160),
       new Color(  50, 120, 140)
    }
  } ;
//    static JCheckBox dateStampCB = new JCheckBox("Output Date Stamp");
  protected static final double m_dLOG10 = Math.log( 10.0) ;

  /**
   * @uml.property  name="m_jAlsoGombsorPanel"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  protected JPanel m_jAlsoGombsorPanel = new JPanel() ;

  public Surface m_cSurface ;
  protected JDiagramParam  m_jJDiagramParam = new JDiagramParam() ;
  protected boolean        m_bDiagramLathato = true ;
  // Hogy ne kelljen a lenti valtozokat minden atkapcsolasnal letrehozni
  protected BorderLayout   m_cBorderLayout ;
  protected TitledBorder   m_cTitledBorder ;
  protected EtchedBorder   m_cEtchedBorder ;

  protected JDiagrGorgNagy m_cJDiagrGorgNagy ;
  protected JEdzesEltolo   m_cJEdzesEltolo   ;
  
  // Ezzel lehet beallitani a tavolsagot, melyhez tartozo pillanatnyi
  // ertekeket irja ki a racs ala 
  protected JAktErtekCsuszka m_cAktErtekCsuszka = null ;
  // Ez a listener veszi a csuszka altal kuldott esemenyeket es tagvaltozojabol
  // ovassa ki aztan a az aktualis tavolsagot
  protected JAktErtCsuszListener m_jJAktErtCsuszListener = null ;
  
  public JDiagram()
  {
//    Dimension cCsuszkaPrefSize = null ;
    
    m_cBorderLayout = new BorderLayout() ;
    
    m_cEtchedBorder = new EtchedBorder() ;
    m_cTitledBorder = new TitledBorder( m_cEtchedBorder, IKonstansok.sHRMFileTavFvben) ;
    setLayout( m_cBorderLayout) ;
    setBorder( m_cTitledBorder) ;

    m_cSurface = new Surface() ;

    // NORTH eseten a CENTER helyen nagy ures helyet hagyna
    add( m_cSurface, BorderLayout.CENTER) ;
    
    m_jAlsoGombsorPanel.setLayout( new BoxLayout( m_jAlsoGombsorPanel, BoxLayout.Y_AXIS)) ;

    m_cAktErtekCsuszka = new JAktErtekCsuszka() ;

    // most csak ugy, nem tagvaltozokent ...
    m_jJAktErtCsuszListener = new JAktErtCsuszListener( m_cSurface) ;
    m_cAktErtekCsuszka.addChangeListener( m_jJAktErtCsuszListener) ;
    
    // A diagramot gorgeto ill. nyujto gombokat tartalmazo panel
    m_cJDiagrGorgNagy = new JDiagrGorgNagy() ;
    m_cJEdzesEltolo   = new JEdzesEltolo()   ;
    
    m_jAlsoGombsorPanel.add( m_cAktErtekCsuszka) ;
    
    m_jAlsoGombsorPanel.add( m_cJDiagrGorgNagy) ;
    m_jAlsoGombsorPanel.add( m_cJEdzesEltolo)   ;
    
    add( m_jAlsoGombsorPanel, BorderLayout.SOUTH) ; 

    addMouseListener(new MouseAdapter()
                         {
                           public void mouseClicked(MouseEvent e)
                           {
                             removeAll() ;

                             if ( m_bDiagramLathato == true )
                             {
                               add( m_cSurface,          BorderLayout.CENTER) ; // ???
                               add( m_jAlsoGombsorPanel, BorderLayout.SOUTH)  ;
                             }
                             else
                             {
                               // Itt kapcsol at a fent beallitott szovegmezokre
                               m_jJDiagramParam.TartalomFrissit() ;
                               add( m_jJDiagramParam, BorderLayout.CENTER) ;
                             }
  
                             m_bDiagramLathato = !m_bDiagramLathato ;
//System.out.println( "JDiagram::JDiagram() -> addMouseListener() elotte") ;
                             validate() ;
                             repaint()  ;
//System.out.println( "JDiagram::JDiagram() -> addMouseListener() utana") ;
                           }
                         }) ;
    
//    addKeyListener( new CKeyListener()) ;
//    addKeyListener( new CKeyAdapter()) ;
/*
    addKeyListener( new KeyAdapter()
        {
          // Invoked when a key has been typed.
          public void keyTyped( KeyEvent e)
          {
            System.out.println( "CKeyAdapter::keyTyped()") ;     
          }

          // Invoked when a key has been pressed.
          public void keyPressed( KeyEvent e)
          {
            System.out.println( "CKeyAdapter::keyPressed()") ;
          }

          // Invoked when a key has been released.
          public void keyReleased( KeyEvent e)
          {
            System.out.println( "CKeyAdapter::keyReleased()") ;
          }  
        }) ;
*/
  }

/*
  // Invoked when a key has been typed.
  public void keyTyped( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyTyped()") ;     
  }

  // Invoked when a key has been pressed.
  public void keyPressed( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyPressed()") ;
  }

  // Invoked when a key has been released.
  public void keyReleased( KeyEvent e)
  {
    System.out.println( "CKeyAdapter::keyReleased()") ;
  }  

  protected void processKeyEvent(KeyEvent e)
  {
System.out.println("JDiagram::processKeyEvent()") ;
    super.processKeyEvent( e) ;
  }
  
  protected void processComponentKeyEvent(KeyEvent e)
  {
System.out.println("JDiagram::processComponentKeyEvent()") ;
    super.processKeyEvent( e) ;
  }
*/
  // CPolar->JDiagram->JEdzesEltolo->JTxtEditScroll
  public void SetElt( int nEdzesIdx, float fIniEdzElt)
  {
    if ( 0 <= nEdzesIdx && nEdzesIdx < CPolar.m_fnMaxHrmFileSzam )
    {
      m_cJEdzesEltolo.SetElt( nEdzesIdx, fIniEdzElt) ;
    }
  }

  /**
   * @author  tamas_2
   */
  public class Surface extends JPanel
  {
    /** Sajatkezuleg generalt Serialized Version UID */
    static final long serialVersionUID = 5261460716622152498L ;

//    protected final Dimension m_cPreferredSize = new Dimension( 500, 250) ;

    protected int m_nDiagrSzel = 0 ; // w
    protected int m_nDiagrMag  = 0 ; // h

    // Azert ez az osztalyvaltozo, mert a fSkalafaktorX valtozik a HRM file
    // es a grafikonmeret fuggvenyeben
    protected float m_fNyuzsoritasX = 1.0f ;
//      private int   m_aEltIdx[] = new int[m_fnMaxHrmFileSzam] ;

    protected BufferedImage bimg       = null ;
    protected Graphics2D m_cGraphics2D = null ;
    protected Font       m_cFont       = new Font( IKonstansok.sTimesNewRoman, Font.PLAIN, 11) ;

    protected int m_nAscent  = 0 ;
    protected int m_nDescent = 0 ;

    protected Line2D m_cGraphLine = new Line2D.Float()      ;
    protected Color m_cGraphColor = new Color(45, 140, 85)  ;
 
    // A csuszkaval kijelolt pontban a pillanatnyi ertek megjelenitese
    protected JPillErtMegj m_jPillErtMegj = new JPillErtMegj() ;
    
/* CParamTip-be atvive
    public final static int eSpeed     = 0 ;
    public final static int eCadence   = 1 ;
    public final static int eHeartRate = 2 ;
    public final static int eAltitude  = 3 ;
    public final static int eDistance  = 4 ;
    public final static int eMax       = 5 ;
*/
    // A racs mindig itt kezdodik : a csuszka pozicionalasanal kell
    public final static int m_nGrafBalszele = 30 ;

    protected Color m_cKmColor    = new Color( 8, 195, 221) ;

    public Surface()
    {
      setBackground( Color.black) ;
/*
        addMouseListener(new MouseAdapter()
                             {
                               public void mouseClicked(MouseEvent e)
                               {
                               }
                              }
                         );
*/
    }
/*
    public Dimension getMinimumSize()
    {
System.out.println( "Surface::getMinimumSize()") ;
      return super.getPreferredSize();
    }

    public Dimension getMaximumSize()
    {
System.out.println( "Surface::getMinimumSize()") ;
      return super.getPreferredSize();
    }


    public Dimension getPreferredSize()
    {
System.out.println( "Surface::getMinimumSize()") ;
      return super.getPreferredSize() ; // ??? vegtelen ciklus
    }
*/
    public void ValtNyuzsoritasX( float fValtNyuzsoritas)
    {
//System.out.println( "fNyuzsoritas=" + Float.toString( fValtNyuzsoritas)) ;
//System.out.println( "elotte : m_fNyuzsoritasX=" + Float.toString( m_fNyuzsoritasX)) ;
      m_fNyuzsoritasX = fValtNyuzsoritas*m_fNyuzsoritasX ;
//System.out.println( "utana : m_fNyuzsoritasX=" + Float.toString( m_fNyuzsoritasX)) ;

      repaint() ;
    }
    
    public boolean ValtEltIdx( float fValtoztatas)
    {
      IMFActionListener.m_cPolar.EdzEltolas( (int)(36000.0f*fValtoztatas)) ;

      repaint() ;

      return true ;
    }

/*
    public boolean EgyEdzValtEltIdx( int nEdzSzama, float fValtoztatas)
    {
      IMFActionListener.m_cPolar.EgyEdzEltolas( nEdzSzama, fValtoztatas) ;

      repaint() ;

      return true ;
    }
*/
    protected void GrafikaTorol()
    {
      Dimension   cAblakMeret  ;
      FontMetrics cFontMetrics ;

      int nPillErtMegjMag = 0 ;
  
      cAblakMeret = getSize() ;

      m_jPillErtMegj.KezdoMeretBeallit( cAblakMeret.width, cAblakMeret.height) ;
      
      nPillErtMegjMag = m_jPillErtMegj.GetMagass() ;
      
//      bimg = (BufferedImage) createImage( cAblakMeret.width, nPillErtMegjMag) ;
      
      if ( cAblakMeret.width != m_nDiagrSzel || (cAblakMeret.height - nPillErtMegjMag) != m_nDiagrMag )
      {
        m_nDiagrSzel = cAblakMeret.width ;
        m_nDiagrMag  = cAblakMeret.height - nPillErtMegjMag ;
//        m_nDiagrMag  = cAblakMeret.height ;

        // Sajnos mindig letre kell hozni, mert a meret kulonben nem valtozik
        // Maskent meg nem lehet valtoztatni a meretet ...
        bimg = (BufferedImage) createImage( m_nDiagrSzel, cAblakMeret.height) ;

        m_cGraphics2D = bimg.createGraphics() ;
        
        m_cGraphics2D.setFont( m_cFont) ;
        cFontMetrics = m_cGraphics2D.getFontMetrics( m_cFont) ;
        m_nAscent  = (int) cFontMetrics.getAscent()  ;
        m_nDescent = (int) cFontMetrics.getDescent() ;
      }

      m_cGraphics2D.setBackground( Color.BLACK) ; //  getBackground()
      m_cGraphics2D.clearRect( 0, 0, cAblakMeret.width, cAblakMeret.height) ;

      // Az if moge hozva, hogy mindig legyen bimg
      m_jPillErtMegj.GrafikaLetrehoz( bimg) ;
    }

    // Minimalis erteknel 0 van feltetelezve 
    protected float Osztas( float fMaxErtek, int nOsztSzamaMin, int nOsztSzamaMax)
    {
      int nIdx = 0 ;
      int nNagysagrend = 0 ;
      int nOsztSzama   = 0 ;
      float fOsztasok[] = { 1.0f, 2.5f, 5.0f, 7.5f} ;
      float f ;
      double dLog10 = 1.0d ;

/*	
System.out.println( "Osztas(): fMaxErtek=" + fMaxErtek     +
                    " nOsztSzamaMin="      + nOsztSzamaMin +
		    " nOsztSzamaMax="      + nOsztSzamaMax) ;
*/
      dLog10 = Math.log( fMaxErtek) / m_dLOG10 ;
//System.out.println( "Osztas(): dLog10=" + Double.toString(dLog10)) ;

      nNagysagrend = (int) (dLog10 - 1.0d) ;
//System.out.println( "Osztas(): nNagysagrend=" + Integer.toString(nNagysagrend)) ;

      // 10 <= f <= 999
      f = (float)Math.pow( 10, nNagysagrend) ;
//System.out.println( "Osztas(): f=" + Float.toString(f)) ;

      nIdx = 0 ;
      nOsztSzama = (int)(fMaxErtek/(f*fOsztasok[nIdx])) ;
//System.out.println( "Osztas(): nOsztSzama=" + Integer.toString(nOsztSzama)) ;

      // 1.0:(10..999) 2.5:(4..39.2) 5.0:(2..19.8) 7.5:(1.3..13.2)
      while ( nIdx < nOsztSzamaMin && nOsztSzamaMax < nOsztSzama )
      {
        nIdx++ ;
        nOsztSzama = (int)(fMaxErtek/(f*fOsztasok[nIdx])) ;
//System.out.println( "Osztas(): nIdx" + Integer.toString(nIdx) + " nOsztSzama=" + Integer.toString(nOsztSzama)) ;
      }

      // Ha az osztasszam nem teljesul, legalabb ne dobjon exceptiont
// Ez ugyan mar nem fordulhat elo 20 < nOsztSzama miatt
      if ( nIdx == 4 )
      {
        nIdx = 3 ;
      }
//System.out.println( "Osztas(): fMaxErtek=" + Float.toString(fMaxErtek) + " osztas=" + Float.toString(f*fOsztasok[nIdx])) ;
      return f*fOsztasok[nIdx] ;
    }

    // Mivel a HRMFile-ban a mintavetelezestol fuggetlenul 1/36000 km-ben
    // vannak az adatok
    //                             int nGrafBalszele = 30;int graphY ;    int graphW      int graphH
    protected void GrafRacsKirajzol( int nBalFelsoX, int nBalFelsoY, int nSzelesseg, int nMagassag,
                                   float fSkalafaktorX, float fSkalafaktorY)
    {
      Rectangle cGraphOutlineRect = new Rectangle() ;
      float fBalFelsoKm  = 0.0f ;
      float fSzelessegKm = 0.0f ;
      float fKmOsztas    = 0.0f ;
//      float fOszlopTav   = 0.0f ;
      float fKm          = 0.0f ;

      CHRMFile cAktHRMFile ;

      int nHrOsztasY = 0 ;
      int nHr        = 0 ;
 
//      int nIdx       = 0 ;
      int nX         = 0 ;
      int nY         = 0 ;
      int nElsoRacsX = 0 ;
      int nSorTav    = 0 ; // graphRow = nMagassag/10 ;
      // Oszlopracsok kirajzolasa, columnInc kezdo eltolassal
      int nOszlopTav = 0 ; // graphColumn = nSzelesseg/15 ;
      int nMintakSzama = 0 ;

      cAktHRMFile = IMFActionListener.m_cPolar.AktivFile() ;

//System.out.println( "GrafRacsKirajzol(): nSzelesseg=" + Integer.toString( nSzelesseg) +
//                    "nMagassag=" + Integer.toString( nMagassag)) ;
//                    " fSkalafaktorX=" + Float.toString( fSkalafaktorX)) ;

//        nSorTav    = nMagassag/10  ;
//        nOszlopTav = nSzelesseg/15 ;

      // A diagram bekeretezese
      cGraphOutlineRect.setRect( nBalFelsoX, nBalFelsoY, nSzelesseg, nMagassag) ;
      m_cGraphics2D.draw( cGraphOutlineRect) ;
/*
fSkalafaktorX = m_fNyuzsoritasX*fSkalafaktorX ;
          // az eltolast itt veszem f.-be
          nGrafBalszele  = nGrafBalszele - (int)(fSkalafaktorX*cLocHRMFile.GetDistanceAt( m_nEltIdx)) ;
*/
      if ( cAktHRMFile != null && cAktHRMFile.VanHeartRateAdat() )
      {
        nY = cAktHRMFile.GetMaxHeartRate() ;
      }

      // Melkasi jelado nelkul lehet minden pulzusadat 0 !
      if ( nY == 0 )
      {
        nY = 200 ;
      }

      nHrOsztasY = (int)Osztas( (float)nY, 5, 10) ;
//System.out.println( "nHrOsztasY=" + Integer.toString( nHrOsztasY)) ;
//System.out.println( "nMagassag="  + Integer.toString( nMagassag)) ;
      nSorTav    = nMagassag*nHrOsztasY/nY ;
//System.out.println( "nSorTav=" + Integer.toString( nSorTav)) ;

      // Sorracsok kirajzolasa
      for ( nY=nBalFelsoY+nMagassag, nHr=0 ; nY > nBalFelsoY; nY-=nSorTav, nHr+=nHrOsztasY)
      {
        m_cGraphLine.setLine( nBalFelsoX, nY, nBalFelsoX+nSzelesseg, nY) ;
        m_cGraphics2D.draw( m_cGraphLine) ;

        // A pulzusosztas skalazasanak kiirasa
        m_cGraphics2D.setColor( Color.red) ;
        m_cGraphics2D.drawString( Integer.toString( nHr), 0, nY) ;

        m_cGraphics2D.setColor( m_cGraphColor) ;
      }

      // A diagram skalazasanak kitalalasa
      // Hany km fer el a diagramban
      if ( cAktHRMFile != null )
      {
        nMintakSzama = cAktHRMFile.GetMintakSzama() ;
      }

      if ( nMintakSzama > 0 )
      {
        try
        { 
          fBalFelsoKm  = cAktHRMFile.GetDistanceAt( cAktHRMFile.GetEltIdx()) ;
//System.out.println( "GrafRacsKirajzol() fBalFelsoKm=" + Float.toString( fBalFelsoKm)) ; 
//          fSzelessegKm = cLocHRMFile.GetDistanceAt(nMintakSzama-1)/36000.0f ;
          fSzelessegKm = (int)(nSzelesseg/fSkalafaktorX/36000) ;
//System.out.println( "GrafRacsKirajzol() fSzelessegKm=" + Float.toString( fSzelessegKm)) ; 

          //szelesseg, lathato, kilometer /* osztas csak a lathato tartomanyra */
          fKmOsztas  = Osztas( fSzelessegKm /*- fBalFelsoKm*/, 4, 20) ;
//System.out.println( "GrafRacsKirajzol() fKmOsztas=" + Float.toString( fKmOsztas)) ; 
//System.out.println( "GrafRacsKirajzol() fOszlopTav=" + Float.toString( fOszlopTav)) ; 
          nOszlopTav = (int)(36000.0f*fKmOsztas*fSkalafaktorX/*fOszlopTav*/) ; // /36000 egyszerusites
        }
        catch ( ArrayIndexOutOfBoundsException eArrayIndexOutOfBoundsException)
        {
          // Hibas *.hrm file, mert kevesebb adat van benne, mint az
          // idotartam/mintavetelezesbol szamitott
    
          cAktHRMFile.SetKijelzendo( false) ;
    
          IMFActionListener.m_cPolar.ElsoKijAktivizal() ;
    
          return ; // ??? +++
        }
      }
      else
      {
        // Ha nincsenek adatok, valamit kezdetnek...
        fSzelessegKm = (float)nSzelesseg ;
        fBalFelsoKm  = 0 ;

        //szelesseg, tenyl. kilometer \/ = szelesseg pontban
        fKmOsztas  = Osztas( fSzelessegKm, 4, 20) ;
        nOszlopTav = (int)(fKmOsztas*fSkalafaktorX) ;
      }

//System.out.println( "nOszlopTav=" + Integer.toString( nOszlopTav)) ; 
      nX = nBalFelsoX ;
      nY = m_nDiagrMag-m_nDescent ;
      fKm = fBalFelsoKm/36000.0f ;
//System.out.println( "fKm=" + Float.toString( fKm)) ;

      m_cGraphics2D.setColor( m_cKmColor) ;
      m_cGraphics2D.drawString( Integer.toString( (int)fKm), nX, nY) ;

      // Hova keruljon az elso osztas
      nElsoRacsX = nOszlopTav - (int)(fBalFelsoKm*fSkalafaktorX)%nOszlopTav ;
/*
System.out.println( "fBalFelsoKm*fSkalafaktorX=" + Float.toString( fBalFelsoKm*fSkalafaktorX)) ; 
System.out.println( "(int)(fBalFelsoKm*fSkalafaktorX)=" + Integer.toString( (int)(fBalFelsoKm*fSkalafaktorX))) ;
System.out.println( "(int)(fBalFelsoKm*fSkalafaktorX)%nOszlopTav=" + Integer.toString( (int)(fBalFelsoKm*fSkalafaktorX)%nOszlopTav)) ;
System.out.println( "nElsoRacsX=" + Integer.toString( nElsoRacsX)) ; 
System.out.println( " fSkalafaktorX*nElsoRacsX=" + Float.toString( fSkalafaktorX*nElsoRacsX)) ; 
*/
      if ( nMintakSzama > 0 )
      {
        fKm = fKmOsztas*((int)(fBalFelsoKm/fKmOsztas/36000.0f)+1) ;
      }
      else
      {
        fKm = fKm + nElsoRacsX/fSkalafaktorX ;
      }
//System.out.println( "fKm=" + Float.toString( fKm)) ; 

      // Oszlopracsok kirajzolasa
      for ( nX = nBalFelsoX+nElsoRacsX; nX < nSzelesseg+nBalFelsoX; nX+=nOszlopTav )
      {
        m_cGraphics2D.setColor( m_cGraphColor) ;

        m_cGraphLine.setLine( nX, nBalFelsoY, nX, nBalFelsoY+nMagassag);
        m_cGraphics2D.draw( m_cGraphLine) ;

        m_cGraphics2D.setColor( m_cKmColor) ;

        m_cGraphics2D.drawString( Integer.toString( (int)fKm), nX, nY) ;

//System.out.println( "GrafRacsKirajzol(): fKm=" + Float.toString( fKm)) ;

        fKm = fKm + fKmOsztas ;
      }
//System.out.println( "GrafRacsKirajzol(): Vege") ;
    }

    protected void SetMaxParamErt( int aMaxParamErt[])
    {
      int nIdx = 0 ;
//      int nParTipIdx = 0 ;
      CParamTip cParTipIdx = new CParamTip() ;

      // Jollehet, a legkisebb sebesseg 0 lehet csak ... (???)
    	int nMaxParamErt = Integer.MIN_VALUE ;

      CHRMFile cLocHRMFile = null ;

      if ( aMaxParamErt == null )
      {
        return ;
      }

      for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eMax ; cParTipIdx.m_nAktParamTip++ )
      {
        nMaxParamErt = Integer.MIN_VALUE ;
        
        for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
        {
          cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nIdx] ;

          if ( cLocHRMFile != null && nMaxParamErt < cLocHRMFile.GetMaxParamErt( cParTipIdx) )
          {
            nMaxParamErt = cLocHRMFile.GetMaxParamErt( cParTipIdx) ;
          }
        }

        aMaxParamErt[cParTipIdx.m_nAktParamTip] = nMaxParamErt ;
	    }
    }

    // A gyari program a magassagnal is 0-rol indit, ezt lehet aztan kezzel
    // modositani ???
    protected void SetMinParamErt( int aMinParamErt[])
    {
      int nIdx = 0 ;
//      int nParTipIdx = 0 ;
      CParamTip cParTipIdx = new CParamTip() ;

      int nMinParamErt = Integer.MAX_VALUE ;

      CHRMFile cLocHRMFile = null ;

      if ( aMinParamErt == null )
      {
       return ;
      }

      for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eMax ; cParTipIdx.m_nAktParamTip++ )
      {
        nMinParamErt = Integer.MAX_VALUE ;
        
        for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
        {
          cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nIdx] ;

          if ( cLocHRMFile != null && nMinParamErt > cLocHRMFile.GetMinParamErt( cParTipIdx) )
          {
            nMinParamErt = cLocHRMFile.GetMinParamErt( cParTipIdx) ;
          }
        }

        aMinParamErt[cParTipIdx.m_nAktParamTip] = nMinParamErt ;
      }
    }

    protected void SkalafaktorBeall( float fIniRemainingHeight, float[] aIniSkalafaktorY)
    {
      // ??? Ezeket a tomboket at kene tenni tagvaltozoba, es akkor frissiteni,
    	// ha file-t nyitok meg, vagy zarok le
      CParamTip cParTipIdx = new CParamTip() ;
    	
    	final int aMinParamErt[] = new int[CParamTip.eMax] ;
      final int aMaxParamErt[] = new int[CParamTip.eMax] ;

      SetMinParamErt( aMinParamErt) ;
      SetMaxParamErt( aMaxParamErt) ;
      
    	if ( fIniRemainingHeight == 0.0 || aIniSkalafaktorY == null )
      {
        return ;
      }

      for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eMax ; cParTipIdx.m_nAktParamTip++ )
      {
        // ??? Itt feltetelezem, hogy mindegyik parameter 0-rol indul !      \/ 
        aIniSkalafaktorY[cParTipIdx.m_nAktParamTip] = fIniRemainingHeight / aMaxParamErt[cParTipIdx.m_nAktParamTip] ;
      }        
    }

    // Az esetleges atmeretezes, nyuzsoritas es eltolas hatasait figyelembe v.
    void CsuszkaAktualizal( int nAktivEdzes, float fRemainingWidth, float fSkalafaktorX)
    {
      int nAktPozicio = 0 ;
      int nMintakSzama = 0 ;
      int nMinKm      = 0 ;
      int nMaxKm      = 0 ;
      int nMaxFileKm  = 0 ;

      CHRMFile cAktHRMFile = null ;

      if ( nAktivEdzes != -1 )
      {
        // A rovidites kedveert
        cAktHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nAktivEdzes] ;
        
        if ( cAktHRMFile.GetKijelzendo() == false )
        {
          return ;
        } 

        nAktPozicio = m_cAktErtekCsuszka.GetPozicio() ;

        nMinKm       = cAktHRMFile.GetDistanceAt( cAktHRMFile.GetEltIdx()) ;
//System.out.println( "JDiagram::CsuszkaAktualizal(): nMinKm=" + nMinKm) ;

        // Ennyi km lathato a kepernyon maximum
        nMaxKm = nMinKm + (int)(fRemainingWidth / fSkalafaktorX) ;
//System.out.println( "JDiagram::CsuszkaAktualizal(): nMaxKm=" + nMaxKm) ;

//      Ennyi km "van a file-ban" maximum

        // Hany kilometeres volt az edzes
        nMintakSzama = cAktHRMFile.GetMintakSzama() ;
 
        // Hogy a -1 bol ne legyen baj
        if ( nMintakSzama > 0 )
        {
          nMaxFileKm = cAktHRMFile.GetDistanceAt( nMintakSzama - 1) ;
        }
        
// Ez nem biztos, hogy jo, mert a csuszka maximuma a teljes szelessegre ertendo
/*
        if ( nMaxKm > nMaxFileKm )
        {
          nMaxKm = nMaxFileKm ;
        }
*/
        
        // Ha a csuszka kivul van az aktualis file-on
        if ( nAktPozicio < nMinKm )
        {
          m_cAktErtekCsuszka.SetPozicio( nMinKm) ;
        }
        else
        {
          if ( nMaxFileKm < nAktPozicio )
          {
            m_cAktErtekCsuszka.SetPozicio( nMaxFileKm) ;
          }
        }

        // A csuszka min / max ertekeinek szukseg szerinti modositasa
//System.out.println( "JDiagram::CsuszkaAktualizal(): GetMinimum()=" + m_cAktErtekCsuszka.GetMinimum()) ;
        if ( m_cAktErtekCsuszka.GetMinimum() != nMinKm )
        {
          m_cAktErtekCsuszka.SetMinimum( nMinKm) ;
        }
        
        // nMaxKm <-> nMaxFileKm               \/ ??? meddig megy a racs ?
//System.out.println( "JDiagram::CsuszkaAktualizal(): GetMaximum()=" + m_cAktErtekCsuszka.GetMaximum()) ;
        if ( m_cAktErtekCsuszka.GetMaximum() != nMaxKm )
        {
          m_cAktErtekCsuszka.SetMaximum( nMaxKm) ;
        }
      }
      else
      {
        m_cAktErtekCsuszka.Init( 0, (int)(fRemainingWidth / fSkalafaktorX), m_cAktErtekCsuszka.GetPozicio()) ;
      }
    }
    
    void ParTipKirajzol( int   nHRMFileIdx,
                         CParamTip cParamTip,
                         float fSkalafaktorX,
                         float [] afSkalafaktorY,
                         int   nGrafFelsSzele,
                         int   nGrafMagassaga,
                         float fRemainingWidth,
                         int   nGrafBalszele )
    {
//      int      nAktivEdzes   = 0    ;
      int      nIdxXIg       = 0    ;
      int      nIdxX         = 0    ;
      int      nMintakSzama  = 0    ;
      int      nElozoX       = 0    ;
      int      nAktualisX    = 0    ;
      int      nAktualisY    = 0    ;
      int      nEltKeperPont = 0    ;
      int      nElozoY       = 0    ;
      float    fSkalafaktorY = 0.0f ;
      CHRMFile cAktHRMFile   = null ;

      // altalanositva
      if ( nHRMFileIdx < 0 || nHRMFileIdx >= CPolar.m_fnMaxHrmFileSzam ||
           IMFActionListener.m_cPolar.m_aHRMFile[nHRMFileIdx] == null ||
           cParamTip == null )
      {
        return ;
      }

      // A rovidites kedveert
      cAktHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nHRMFileIdx] ;
      
      if ( cAktHRMFile.GetKijelzendo() == false ||
           cAktHRMFile.VanParamAdat( cParamTip) == false )
      {
        return ;
      } 

      fSkalafaktorY = afSkalafaktorY[cParamTip.m_nAktParamTip] ;
      m_cGraphics2D.setColor( m_cParamColor[cParamTip.m_nAktParamTip][nHRMFileIdx]) ;

      nMintakSzama  = cAktHRMFile.GetMintakSzama() ;
      // Az utolso kirajzolando ponthoz tartozo index
      nIdxXIg = cAktHRMFile.MeterBolIndex( (int)(fRemainingWidth/fSkalafaktorX + 
      /*    m_cAktHRMFile ??? ->  */       cAktHRMFile.GetDistanceAt( cAktHRMFile.GetEltIdx()))) ;

      // Hogyha nem erne el a gorbe a grafikon jobb szeleig ...
      if ( nIdxXIg == -1 )
      {
        nIdxXIg = cAktHRMFile.GetMintakSzama() ;
      }

// jobbra (!) valo eltolasnal a grafikon az x=0 tol indul, az elso y ertekkel igy egy hosszu egyenes lesz lathato
//      nElozoX = nGrafBalszele ;
        
      // az eltolast itt veszem f.-be
      nEltKeperPont = (int)(fSkalafaktorX*cAktHRMFile.GetEltTav()) ;

      nElozoX = nGrafBalszele ; 
      
      // Jobbra valo eltolasnal a kirajzolas ne x=0-tol induljon, hanem a kezdet is mozogjon jobbra
      if ( nEltKeperPont < 0 )
      {
        nElozoX = nElozoX - nEltKeperPont ;
      }

      nElozoY = cAktHRMFile.GetParamAt( cParamTip, cAktHRMFile.GetEltIdx()) ;
      nElozoY = (int) (nGrafFelsSzele + nGrafMagassaga - fSkalafaktorY*nElozoY) ;

      for ( nIdxX = cAktHRMFile.GetEltIdx() + 1 ; nIdxX < nMintakSzama && nIdxX < nIdxXIg ; nIdxX++ )
      {
        nAktualisX = nGrafBalszele + (int)(fSkalafaktorX*cAktHRMFile.GetDistanceAt( nIdxX)) - nEltKeperPont ;
            
        try
        {
          nAktualisY = cAktHRMFile.GetParamAt( cParamTip, nIdxX) ;
          nAktualisY = (int) (nGrafFelsSzele + nGrafMagassaga - fSkalafaktorY*nAktualisY) ;

          m_cGraphics2D.drawLine( nElozoX,
                                  nElozoY,
                                  nAktualisX,
                                  nAktualisY) ;

/*
          if ( nAktivEdzes != -1 && cAktHRMFile != null && cParamTip != null && cParamTip.m_nAktParamTip == cParamTip.eSpeed )
          {
System.out.println( "JDiagram::ParTipKirajzol(): nElozoX=" + nElozoX + " nElozoY=" + nElozoY + " nAktualisX=" + nAktualisX + " nAktualisY=" + nAktualisY) ;
          }
*/
          nElozoX = nAktualisX ;
          nElozoY = nAktualisY ;
        }
        catch ( ArrayIndexOutOfBoundsException eArrayIndexOutOfBoundsException)
        {
System.out.println( "JDiagram::ParTipKirajzol(): Caught ArrayIndexOutOfBoundsException") ;

          break ;
        }
      }
    }

//-----------------------------------------------------------------------------------------
    void IdoKulKirajzol( float fSkalafaktorX,
/*                         float [] afSkalafaktorY,  ??? */
                         int   nGrafFelsSzele,
                         int   nGrafMagassaga,
                         float fRemainingWidth,
                         int   nGrafBalszele )
    {
      int      nIdxXIg       = 0    ;
      int      nIdxX         = 0    ;
      int      nMintakSzama  = 0    ;
      int      nAktivKezdIdo = 0    ;
      int      nAktivEltTav  = 0    ;
      int      nMasikKezdIdo = 0    ;
      int      nMasikEltIdo  = 0    ;
      int      nMasikEltTav  = 0    ;
      int      nIdx          = 0    ;
      int      nAktDistance  = 0    ;
      int      nElozoX       = 0    ;
      int      nAktualisX    = 0    ;
      int      nAktualisY    = 0    ;
      int      nEltKeperPont = 0    ;
//      int      nEltTav       = 0    ;
      int      nElozoY       = 0    ;
      int      nGrafFeleMag  = 0    ;
      float    fSkalafaktorY = 0.5f ;
      CHRMFile cAktHRMFile   = null ;
      CHRMFile cMasikHRMFile = null ;

      // A rovidites kedveert
      cAktHRMFile = IMFActionListener.m_cPolar.AktivFile() ;

      if ( cAktHRMFile == null ||
//           cAktHRMFile.GetKijelzendo() == false || // Az aktiv file mindig kijelzendo
           cAktHRMFile.VanSpeedAdat()  == false    )
      {
        return ;
      } 

/*      fSkalafaktorY = afSkalafaktorY[cParamTip.m_nAktParamTip] ;*/

      nMintakSzama  = cAktHRMFile.GetMintakSzama() ;
      
      // Az utolso kirajzolando ponthoz tartozo index
      nIdxXIg = cAktHRMFile.MeterBolIndex( (int)(fRemainingWidth/fSkalafaktorX + 
                                           cAktHRMFile.GetDistanceAt( cAktHRMFile.GetEltIdx()))) ;

      // Hogyha nem erne el a gorbe a grafikon jobb szeleig ...
      if ( nIdxXIg == -1 )
      {
        nIdxXIg = cAktHRMFile.GetMintakSzama() ;
      }

      nElozoX = nGrafBalszele ;

      // az eltolast itt veszem f.-be
      nEltKeperPont = (int)(fSkalafaktorX*cAktHRMFile.GetEltTav()) ;

      // A diagram kozeperol kene inditani, hogy negativ ertekek is latsszanak
//    ??? = cAktHRMFile.GetParamAt( cParamTip, cAktHRMFile.GetEltIdx()) ;
      // Induljon a nulla a grafika kozeperol ...
      nGrafFeleMag = nGrafFelsSzele + nGrafMagassaga / 2 ;

      // cAktHRMFile.GetDistanceAt( cAktHRMFile.GetEltIdx()) : cAktHRMFile.GetEltTav()
      nAktivEltTav  = cAktHRMFile.GetEltTav() ;
      nAktivKezdIdo = cAktHRMFile.MeterbolEltIdo( nAktivEltTav) ;

      for ( nIdx = 0 ; nIdx < CPolar.m_fnMaxHrmFileSzam ; nIdx++ )
      {
//System.out.println( "JDiagram::IdoKulKirajzol(): nIdx=" + nIdx + " nGrafFeleMag=" + nGrafFeleMag) ;

        // Nem minden tombelem betoltott, es sajat magaval se kepezzen kulonbseget, es kijelzendo legyen
        if ( IMFActionListener.m_cPolar.m_aHRMFile != null &&
             IMFActionListener.m_cPolar.m_aHRMFile[nIdx] != null &&
             IMFActionListener.m_cPolar.m_aHRMFile[nIdx] != cAktHRMFile &&
             IMFActionListener.m_cPolar.m_aHRMFile[nIdx].GetKijelzendo() == true &&
             IMFActionListener.m_cPolar.m_aHRMFile[nIdx].VanSpeedAdat() == true )
        {
          m_cGraphics2D.setColor( m_cParamColor[CParamTip.eDistance][nIdx]) ;
              
          cMasikHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nIdx] ;          
            
          nMasikEltTav  = cMasikHRMFile.GetEltTav() ;
          nMasikKezdIdo = cMasikHRMFile.MeterbolEltIdo( nMasikEltTav) ;          
          
          // \/ csak az aktiv file-e kell l. lent
          nMintakSzama  = cAktHRMFile.GetMintakSzama() < cMasikHRMFile.GetMintakSzama() ? cAktHRMFile.GetMintakSzama() : cMasikHRMFile.GetMintakSzama() ;

          nElozoX = nGrafBalszele ;
          nElozoY = nGrafFeleMag ; 

          // Csak ameddig vannak adatok, vagy a diagram szeleig (folosleges tovabb)
          for ( nIdxX = cAktHRMFile.GetEltIdx() + 1 ; nIdxX < nMintakSzama && nIdxX < nIdxXIg ; nIdxX++ )
          {
            nAktDistance = cAktHRMFile.GetDistanceAt( nIdxX) ;
            nAktualisX = nGrafBalszele + (int)(fSkalafaktorX*nAktDistance) - nEltKeperPont ;

            try
            {
              // Ezt kellene lecsekkolni, ha egyszer hiba volt kiugrani a for ciklusbol, mert kul nem megy vegig
              nMasikEltIdo = cMasikHRMFile.MeterbolEltIdo( nAktDistance - nAktivEltTav + nMasikEltTav) ;
              
              if ( nMasikEltIdo == -1 )
              {
                break ;
              }
              
              nAktualisY = (cAktHRMFile.MeterbolEltIdo( nAktDistance) - nAktivKezdIdo) -
                           (nMasikEltIdo - nMasikKezdIdo) ;

//System.out.println( "JDiagram::IdoKulKirajzol(): nIdxX =" + nIdxX + " nAktualisX=" + nAktualisX + " nAktDistance=" + nAktDistance/36000.0f + " nAktualisY=" + nAktualisY) ;

              nAktualisY = (int) (nGrafFeleMag - fSkalafaktorY*nAktualisY) ;

              m_cGraphics2D.drawLine( nElozoX,
                                      nElozoY,
                                      nAktualisX,
                                      nAktualisY) ;

              nElozoX = nAktualisX ;
              nElozoY = nAktualisY ;
            }
            catch ( ArrayIndexOutOfBoundsException eArrayIndexOutOfBoundsException)
            {
              System.out.println( "IdoKulKirajzol(): Caught ArrayIndexOutOfBoundsException") ;

              break ;
            }
            
          }
//System.out.println( "JDiagram::IdoKulKirajzol(): nMintakSzama=" + nMintakSzama + " nIdxXIg=" + nIdxXIg) ;
        }
      }  
    }

    public void repaint()
    {
// System.out.println( "JDiagram.repaint() BEGIN") ;
     super.repaint() ;
      m_jJDiagramParam.repaint() ;  
    }
    
    public void paint( Graphics g)
    {
// System.out.println( "JDiagram::Surface::paint() BEGIN") ;
      int   nGrafFelsSzele =  0 ;
      int   nGrafMagassaga =  0 ;

//      int   nEltKeperPont = 0 ;

//      int   nIdxXTol     = 0 ;
//      int   nIdxXIg      = 0 ;

//      int   nIdxX        = 0 ;
      int   nFileSzam	   = 0 ;

//      int   nElozoX      = 0 ;
//      int   nAktualisX   = 0 ;
//      int   nEltolasX    = 0 ;

//      int   nElozoY      = 0 ;
//      int   nAktualisY   = 0 ;

      // A kulonbozo parameterfajtaknak mas-mas a min / max erteke - skalazasa
      float afSkalafaktorY[] = null ;
      float fSkalafaktorX    = 0.0f ;

      int nMinHeartRate = 0 ;
      int nMaxHeartRate = 0 ;
      int nMintakSzama  = 0 ;

      int nAktivEdzes   = 0 ;
      
      String    sSzoveg     = null ;
      CHRMFile  cLocHRMFile = null ;
      CParamTip cParTipIdx  = null ;
      
      afSkalafaktorY = new float[CParamTip.eMax] ;
      sSzoveg        = new String() ;
      cParTipIdx     = new CParamTip() ;
        
      // Az aktiv edzes szama, az idokulonbseg kepzesenel hasznalatos
      nAktivEdzes = IMFActionListener.m_cPolar.m_nAktivEdzes ;
      
      // A rovidites kedveert
      cLocHRMFile = IMFActionListener.m_cPolar.AktivFile() ;

      if ( cLocHRMFile != null )
      {
        nMinHeartRate = cLocHRMFile.GetMinHeartRate() ;
        nMaxHeartRate = cLocHRMFile.GetMaxHeartRate() ;
        nMintakSzama  = cLocHRMFile.GetMintakSzama()  ;
      }

      // m_cGraphics2D letrehozasa, inicializalasa
      GrafikaTorol() ;

      // Graphics2D m_cGraphics2D - tagvaltozo
      if ( m_cGraphics2D == null )
      {
//System.out.println( "JDiagram::Surface::paint(): m_cGraphics2D == null") ;
        return ;
      }

      // A maximalis es minimalis pulzus kiirasa
      // Alulrol es felulrol szamitva 
      sSzoveg = IKonstansok.sHRMinMax +
                String.valueOf( nMinHeartRate) + " / " +
                String.valueOf( nMaxHeartRate) ;

      m_cGraphics2D.setColor(Color.red) ;
      m_cGraphics2D.drawString( sSzoveg, 4.0f, (float)m_nAscent+0.5f) ;
/*
        m_cGraphics2D.drawString( "HR max : ",
                                  4,
                                  m_nDiagrMag-m_nDescent) ;
*/
      // Calculate remaining size
      float ssH = m_nAscent + m_nDescent ;
      float fRemainingHeight = (float) (m_nDiagrMag - (ssH*2) - 0.5f)  ;
      // L. GrafRacsKirajzol() 3. parameter
      float fRemainingWidth = (float) (m_nDiagrSzel - m_nGrafBalszele - JAktErtekCsuszka.m_nThumbWidthH) ;

      // .. Draw History Graph ..
      m_cGraphics2D.setColor( m_cGraphColor) ;

// A Heart Rate kirajzolasa
// ------- x koordinatak / tombindexek 1:1 megfeleltetese
      if ( nMintakSzama > 0 )
      {
//System.out.println( "JDiagram::Surface::paint():cLocHRMFile.GetMintakSzama() : " + Integer.toString( nMintakSzama) + " > 0 -> fSkalafaktorX beall.") ;
        fSkalafaktorX = fRemainingWidth / (float)cLocHRMFile.GetDistanceAt( nMintakSzama-1) ;
  
//System.out.println( "JDiagram::Surface::paint():fRemainingWidth=" + Float.toString( fRemainingWidth) +
//                    " cLocHRMFile.GetDistanceAt(max-1)=" + Integer.toString( cLocHRMFile.GetDistanceAt( cLocHRMFile.GetMintakSzama()-1))) ;
      }
      else
      {
//System.out.println( "JDiagram::Surface::paint():cLocHRMFile.GetMintakSzama() : " + Integer.toString( nMintakSzama) + " <= 0 -> fSkalafaktorX beall.(1)") ;
        fSkalafaktorX = 1 ;
      }

      // A fSkalafaktorX valtozik a HRM file es a grafikonmeret fuggvenyeben
//System.out.println( "JDiagram::Surface::paint():m_fNyuzsoritasX=" + Float.toString( m_fNyuzsoritasX) +
//                    " fSkalafaktorX=" + Float.toString( fSkalafaktorX)) ;
      fSkalafaktorX = m_fNyuzsoritasX*fSkalafaktorX ;
//System.out.println( "JDiagram::Surface::paint():m_fNyuzsoritasX=" + Float.toString( m_fNyuzsoritasX) +
//                    " fSkalafaktorX=" + Float.toString( fSkalafaktorX)) ;
//        fSkalafaktorY = fRemainingHeight / (float)nMaxHeartRate ;
      
      // A kulonfele parameterekhez tartozo skalafaktorok (nyuzsoritas) beallitasa
      SkalafaktorBeall( fRemainingHeight, afSkalafaktorY) ;
  
      nGrafFelsSzele = (int)ssH ;
      nGrafMagassaga = (int)fRemainingHeight ;

      // ??? Itt feltetelezem, hogy mindig var HR adat, helyesebb lenne, megkeresni
      // azt, amelyiknel van
      GrafRacsKirajzol( m_nGrafBalszele,
                        nGrafFelsSzele,
//                        m_nDiagrSzel - m_nGrafBalszele - 5,
                        m_nDiagrSzel - m_nGrafBalszele - JAktErtekCsuszka.m_nThumbWidthH,                        
                        nGrafMagassaga,
                        fSkalafaktorX,
                        afSkalafaktorY[CParamTip.eSpeed]) ;

      // Az esetleges atmeretezes, nyuzsoritas es eltolas hatasait figyelembe v.
      CsuszkaAktualizal( nAktivEdzes, fRemainingWidth, fSkalafaktorX) ; // ???

      for ( nFileSzam = 0 ; nFileSzam < CPolar.m_fnMaxHrmFileSzam ; nFileSzam++ )
      {
        cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nFileSzam] ;
  
        for ( cParTipIdx.m_nAktParamTip = CParamTip.eSpeed ; cParTipIdx.m_nAktParamTip < CParamTip.eDistance ; cParTipIdx.m_nAktParamTip++ )
//        for ( cParTipIdx.m_nAktParamTip = CParamTip.eAltitude ; cParTipIdx.m_nAktParamTip < CParamTip.eDistance ; cParTipIdx.m_nAktParamTip++ )
        {
          ParTipKirajzol( nFileSzam, cParTipIdx, fSkalafaktorX, afSkalafaktorY, nGrafFelsSzele, nGrafMagassaga, fRemainingWidth, m_nGrafBalszele) ;
        }       
      }

//System.out.println( "JDiagram::Surface::paint() IdoKulKirajzol() elott") ;

      IdoKulKirajzol( fSkalafaktorX, nGrafFelsSzele, nGrafMagassaga, fRemainingWidth, m_nGrafBalszele) ;

//System.out.println( "JDiagram::Surface::paint() IdoKulKirajzol() utan") ;

      // A pillanatnyi ertekek kiirasa a diagram alatti reszen
      m_jPillErtMegj.PillErtKiir( m_jJAktErtCsuszListener.GetCsuszTav()) ; // ???

//System.out.println( "JDiagram::Surface::paint() PillErtKiir() utan") ;

      // Ezt, ugy nez ki, mindenkeppen fel kell hivni a paint()-ben,
      // kulonben meinden egyeb kontrolt ennek a helyere frissit
      g.drawImage( bimg, 0, 0, this) ;
      
//System.out.println( "JDiagram::Surface::paint() drawImage() utan") ;

      // A pillanatnyi ertekeket mindig ki kell rajzolni
//      g.drawImage( m_jPillErtMegj.GetBufferedImage(), 0, m_nDiagrMag, this) ; // ???
    }
  }
/*
  protected void PaintDiagramParam( Graphics g)
  {
    super.paint() ; // Az aktualis rajzolo thread-en kivulrol tilos hivni !
  }
*/
/*
  public void paint( Graphics g)
  {
    super.paint( g) ;
System.out.println( "JDiagram::paint()") ;


    // Arra hivjuk meg a tenyleges 
    if ( m_bDiagramLathato == true )
    {
      PaintDiagram( g) ;
    }
    else
    {
      PaintDiagramParam( g) ;
    }
  }
*/

/*
  public Dimension getMinimumSize()
  {
System.out.println( "JDiagram::getMinimumSize()") ;
    return super.getPreferredSize();
  }

  public Dimension getMaximumSize()
  {
System.out.println( "JDiagram::getMaximumSize()") ;
    return super.getPreferredSize();
  }


  public Dimension getPreferredSize()
  {
System.out.println( "JDiagram::getPreferredSize()") ;
    return super.getPreferredSize() ;
  }
*/
  public static void main(String s[])
  {
    final JDiagram demo = new JDiagram();

    WindowListener l = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {System.exit(0);}
//      public void windowDeiconified(WindowEvent e) { demo.m_cSurface.start(); }
//      public void windowIconified(WindowEvent e) { demo.m_cSurface.stop(); }
    };
    
    JFrame f = new JFrame("Java2D Demo - JDiagram");

    f.addWindowListener(l);
    f.getContentPane().add("Center", demo);
    f.pack();
    f.setSize(new Dimension(600,400));
    f.setVisible(true);
  }
}
