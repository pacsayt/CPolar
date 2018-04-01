package cpolar;
// JDiagrGorgNagy.java hiba


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import CPolar ;

// A JDiagrGorgNagy JPanel uzeneteit kezeli
class CBalraListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    m_cPolar.m_cJDiagram.m_cSurface.ValtEltIdx( -m_fEltTavValtLepese) ;
  }
}

class CJobbraListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    m_cPolar.m_cJDiagram.m_cSurface.ValtEltIdx( m_fEltTavValtLepese) ;
  }
}

class CNagyitListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    m_cPolar.m_cJDiagram.m_cSurface.ValtNyuzsoritasX( m_fNyuzsFaktor) ;
  }
}

class CKicsinyitListener implements IMFActionListener
{
  public void actionPerformed( ActionEvent e)
  {
    m_cPolar.m_cJDiagram.m_cSurface.ValtNyuzsoritasX( 1.0f/m_fNyuzsFaktor) ;
  }
}

/**
 * A JDiagram osztalyt (a diagramot) gorgeto/nagyito-kicsinyito gombok
 */
public class JDiagrGorgNagy extends JPanel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152500L ;

  // A diagram alatti gombok a diagram gorditesere ill. nyuzsoritasara
  /**
   * @uml.property  name="m_jBalra"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  public JButton m_jBalra     = new JButton( IKonstansok.sBalra)  ;
  /**
   * @uml.property  name="m_jJobbra"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  public JButton m_jJobbra    = new JButton( IKonstansok.sJobbra)  ;
  /**
   * @uml.property  name="m_jNagyit"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  public JButton m_jNagyit    = new JButton( IKonstansok.sNagyit) ;
  /**
   * @uml.property  name="m_jKicsinyit"
   * @uml.associationEnd  multiplicity="(1 1)"
   */
  public JButton m_jKicsinyit = new JButton( IKonstansok.sKicsinyit) ;

  public JDiagrGorgNagy()
  {
    //                         y, x
    setLayout( new GridLayout( 0, 4)) ;
    
    m_jBalra.addActionListener( new CBalraListener()) ;
    m_jKicsinyit.addActionListener( new CKicsinyitListener()) ;
    m_jNagyit.addActionListener( new CNagyitListener()) ;
    m_jJobbra.addActionListener( new CJobbraListener()) ;

    add( m_jBalra) ;
    add( m_jKicsinyit) ;
    add( m_jNagyit) ;
    add( m_jJobbra) ;
  }
  
//----------------------------------------------------------------------------
  public static void main(String s[])
  {
    final JDiagrGorgNagy jDiagrGorgNagyTeszt = new JDiagrGorgNagy() ;

    WindowListener cWindowListener = new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {System.exit(0);}
//      public void windowDeiconified(WindowEvent e) { demo.surf.start(); }
//      public void windowIconified(WindowEvent e) { demo.surf.stop(); }
    };
    
    JFrame f = new JFrame("A JDiagram osztalyt (a diagramot) gorgeto/nagyito-kicsinyito gombok");

    f.addWindowListener( cWindowListener) ;
    f.getContentPane().add("Center", jDiagrGorgNagyTeszt) ;
    f.pack() ;
    f.setSize(new Dimension(200,200)) ;
    f.setVisible(true)  ;
  }
}