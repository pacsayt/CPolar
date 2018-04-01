package cpolar;
// CProgInfoDlg.java hiba
// CProgInfoDlg : informaciokat jelenit meg a programrol
//          


import java.awt.*;
import java.awt.event.*;

class CDlgKilepListener extends WindowAdapter implements ActionListener
{
  public CDlgKilepListener( CProgInfoDlg cIniProgInfoDlg)
  {
    m_cProgInfoDlg = cIniProgInfoDlg ;
  }

  /**
   * @uml.property  name="m_cProgInfoDlg"
   * @uml.associationEnd  multiplicity="(1 1)" inverse="m_cDlgKilepListener:CProgInfoDlg"
   */
  protected CProgInfoDlg m_cProgInfoDlg = null ;

  public void actionPerformed( ActionEvent e)
  {
    m_cProgInfoDlg.dispose() ;
  }

  public void windowClosing(WindowEvent e)
  {
    m_cProgInfoDlg.dispose() ;
  }
}

class CProgInfoDlg extends Dialog
{
  public CProgInfoDlg( Frame parent, String sAblakCim)
  {
    super( parent, sAblakCim, false) ;

    setLayout( new GridLayout( 3, 1)) ;
  }

  /** Sajatkezoleg generalt Serialized Version UID */
  static final long serialVersionUID = 5261460716622152495L ;

  /**
   * @uml.property  name="m_sElsoSor"
   */
  protected String m_sElsoSor    = null ;
  /**
   * @uml.property  name="m_sMasodikSor"
   */
  protected String m_sMasodikSor = null ;
  
  /**
   * @uml.property  name="m_cOKButton"
   */
  protected Button m_cOKButton = new Button( "OK") ;
  
  // A dialogus ablakbol valo kilepesre hasznalt 'boolean handleEvent( Event evt)'
  // helyett a COnOK-val lep ki a rendszergombok hatasara
  /**
   * @uml.property  name="m_cDlgKilepListener"
   * @uml.associationEnd  multiplicity="(1 1)" inverse="m_cProgInfoDlg:CDlgKilepListener"
   */
  protected CDlgKilepListener  m_cDlgKilepListener = new CDlgKilepListener( this) ;
  
  public void SzovegBeallit(String sIniElsoSor, String sIniMasodikSor)
  {
    m_sElsoSor = sIniElsoSor ;
    
    if ( m_sElsoSor == null )
    {
      m_sElsoSor = "" ;
    }

    m_sMasodikSor = sIniMasodikSor ;
    
    if ( m_sMasodikSor == null )
    {
      m_sMasodikSor = "" ;
    }
  }
  
  public void Kirajzol()
  {
    add( new Label( m_sElsoSor, Label.CENTER)) ;
    add( new Label( m_sMasodikSor, Label.CENTER)) ;

    m_cOKButton.addActionListener( m_cDlgKilepListener) ;
    add( m_cOKButton) ;

    addWindowListener( m_cDlgKilepListener) ;

    setSize( 400, 150) ;
    
    show() ;
  }
}
