package cpolar;
// JEdzesEltolo.java
// A JTxtEditScroll (szovegmezo-szerkesztoablak-valtoztato nyilak) osztalyt
// tartalmazo sor, m_fnMaxHrmFileSzam db-ot tartalmaz beloluk.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A betoltott edzesek egymashoz kepesti elcsusztatasat
 */
public class JEdzesEltolo extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152501L ;

  // A diagram alatti gombok a diagram gorditesere ill. nyuzsoritasara
  protected JTxtEditScroll m_aTxtEditScroll[]  = null ;
  
  // Az adott edzest melyik intervallumtol (megjegyzett pont egy edzesen belul)
  // kezdodjon
  protected JComboBox      m_aIntervallCombo[] = null ;

  // CPolar() -> JDiagram() -> JEdzesEltolo() a hivasi sorrend
  public JEdzesEltolo()
  {
    int   i            = 0    ;
    int   nIntvSzama   = 0    ;
    int   nIntvIdx     = 0    ;
    float fIniEdzEltKm = 0.0f ;
    
    CHRMFile cLocHRMFile = null ;

//System.out.println( "JEdzesEltolo::JEdzesEltolo() BEGIN") ;

    // A GridLayout nem veszi figyelembe a "preferred size"-t
    setLayout( new BoxLayout( this, BoxLayout.X_AXIS)) ;
//System.out.println( "JEdzesEltolo::JEdzesEltolo() setLayout() utan") ;

//CPolar.java:  public static final int m_fnMaxHrmFileSzam = 5 ;
    m_aTxtEditScroll  = new JTxtEditScroll[CPolar.m_fnMaxHrmFileSzam] ;
    m_aIntervallCombo = new JComboBox[CPolar.m_fnMaxHrmFileSzam] ;
//System.out.println( "JEdzesEltolo::JEdzesEltolo() m_aTxtEditScroll = new ... utan") ;

    for ( i = 0 ; i < CPolar.m_fnMaxHrmFileSzam ; i++ )
    {
      nIntvSzama = 0 ;
      
      // A file-ok itt mindig uresek lesznek a hivasi sorrend miatt
      if ( IMFActionListener.m_cPolar != null &&
           IMFActionListener.m_cPolar.m_aHRMFile != null &&
           IMFActionListener.m_cPolar.m_aHRMFile[i] != null )
      {
        cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[i] ;
        fIniEdzEltKm = (cLocHRMFile.GetEltTav()*cLocHRMFile.GetInterval())/3600 ;
        
        nIntvSzama = cLocHRMFile.GetIntSzama() ;
      }
      else
      {
        fIniEdzEltKm = 0.0f ;
      }
      
      m_aTxtEditScroll[i] = new JTxtEditScroll( i, fIniEdzEltKm) ;

      if ( m_aTxtEditScroll[i] != null )
      {
        add( m_aTxtEditScroll[i])  ;
      }
      
      m_aIntervallCombo[i] = new JComboBox() ;

      if ( m_aIntervallCombo[i] != null )
      {
        for ( nIntvIdx = 0 ; nIntvIdx < nIntvSzama ; nIntvIdx++ )
        {
          m_aIntervallCombo[i].addItem( Integer.toString( nIntvIdx)) ;
        }
        
        add( m_aIntervallCombo[i])  ;
      }      
    }
  }
  
  public void paint( Graphics g)
  {
    int nFileIdx   = 0 ;
    int nIntvSzama = 0 ;
    int nIntvIdx   = 0 ;

    CHRMFile cLocHRMFile = null ;

    super.paint( g) ;
    
//System.out.print( "JEdzesEltolo::paint()\n") ;
    for ( nFileIdx = 0 ; nFileIdx < CPolar.m_fnMaxHrmFileSzam ; nFileIdx++ )
    {
      // A file-ok itt mindig uresek lesznek a hivasi sorrend miatt
      if ( IMFActionListener.m_cPolar != null &&
           IMFActionListener.m_cPolar.m_aHRMFile != null &&
           IMFActionListener.m_cPolar.m_aHRMFile[nFileIdx] != null )
      {
        cLocHRMFile = IMFActionListener.m_cPolar.m_aHRMFile[nFileIdx] ;
        nIntvSzama = cLocHRMFile.GetIntSzama() ;
        
        if ( m_aIntervallCombo[nFileIdx] != null )
        {
          if ( cLocHRMFile.GetKijelzendo() == true )
          {
            // A CB-nak uresnek kell lennie, kulonben mar aktualis, mert egy file 
            // bezarasanal kiuritem
            if ( m_aIntervallCombo[nFileIdx].getItemCount() == 0 )
            {
              for ( nIntvIdx = 0 ; nIntvIdx < nIntvSzama ; nIntvIdx++ )
              {
                m_aIntervallCombo[nFileIdx].addItem( Integer.toString( nIntvIdx)) ; // TODO =====================================================
              }
            }
          }
          else
          {
            // A ComboBox tartalmanak kiuritese
            m_aIntervallCombo[nFileIdx].removeAllItems() ;
          }
        }
      }
      else
      {
        if ( m_aIntervallCombo[nFileIdx] != null )
        {
          // A ComboBox tartalmanak kiuritese
          m_aIntervallCombo[nFileIdx].removeAllItems() ;
        }
      }
    }
  }
  
  // CPolar->JDiagram->JEdzesEltolo->JTxtEditScroll
  public void SetElt( int nEdzesIdx, float fIniEdzElt)
  {
    if ( nEdzesIdx < CPolar.m_fnMaxHrmFileSzam )
    {
      m_aTxtEditScroll[nEdzesIdx].SetElt( fIniEdzElt) ; // magat a kontrollt nem allitja !
    }
  }
  
  public static void main(String s[])
  {
    final JEdzesEltolo JEdzesEltoloTeszt = new JEdzesEltolo() ;

    WindowListener cWindowListener = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {System.exit(0);}
//      public void windowDeiconified(WindowEvent e) { demo.surf.start(); }
//      public void windowIconified(WindowEvent e) { demo.surf.stop(); }
    };
    
    JFrame f = new JFrame("Az edzes file-ok eltolasat beallito edit mezok + gombok");

    f.addWindowListener( cWindowListener) ;
    f.getContentPane().add("Center", JEdzesEltoloTeszt) ;
    f.pack() ;
    f.setSize(new Dimension(200,200)) ;
    f.setVisible(true)  ;
  }
}