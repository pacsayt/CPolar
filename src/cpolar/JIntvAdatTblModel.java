package cpolar;
// JIntvAdatTblModel.java 

//import javax.swing.* ;
//import javax.swing.event.*;
//import javax.swing.text.*;
//import javax.swing.DefaultCellEditor;
import java.util.Set;
import javax.swing.table.* ;

// A sugo szerint :
// public abstract class AbstractTableModel
// extends Object
// implements TableModel, Serializable

// CPolar->JDiagram->JDiagramParam->JIntTimesTable->JIntvAdatTblModel
public class JIntvAdatTblModel extends AbstractTableModel
{
  /** Sajatkezuleg generalt Serialized Version UID */
  protected static final long serialVersionUID = 6548695296135465462L ;
  
  int m_nMaxIntervallum = 0 ;
  
  // Referencia a kijelzendo parametereket kivalaszto check boxra :
  // itt mindig aktualis a kijelzendo parameterek listaja
  protected JParamValPanel  m_jParamValPanel = null ;
  
//  CHRMFile m_aHRMFile[] = null ; nincs hol inicializalni
  
  public JIntvAdatTblModel( JParamValPanel  jIniParamValPanel)
  {
    super() ;
//System.out.println( "JIntvAdatTblModel::JIntvAdatTblModel()") ;

    // Nem megy, mert a CPolar letrehozasi lancaban van benne, mely folyaman IMFActionListener.m_cPolar = null
    // CPolar->JDiagram->JDiagramParam->JIntTimesTable->JIntvAdatTblModel
    // m_aHRMFile = IMFActionListener.m_cPolar.m_aHRMFile ;
    
    m_jParamValPanel = jIniParamValPanel ;
  }
  
  public int getColumnCount()
  {
    int nIdx = 0 ;
//    int nKijEdzSzama = 0 ;
    
//    CHRMFile aHRMFile[] = null ;
    nIdx = CPolar.m_fnMaxHrmFileSzam ;
//System.out.println( "JIntvAdatTblModel::getColumnCount() -> nIdx=" + nIdx) ;
    
    // Mert valamiert ez is meghivodik a letrehozasi lancban
/* Mert kezdetben 0 edzes van, es a tabla boviteset nem t. megoldani
    if ( IMFActionListener.m_cPolar != null )
    {
      nIdx = IMFActionListener.m_cPolar.m_fnMaxHrmFileSzam ;
      aHRMFile = IMFActionListener.m_cPolar.m_aHRMFile ;
    
      if ( aHRMFile != null )
      {
        // Megszamlaljuk, hany edzes adatait kell megjeleniteni
        for ( nIdx = 0 ; nIdx < aHRMFile.length ; nIdx++ )
        {
          if ( aHRMFile[nIdx] != null && aHRMFile[nIdx].GetKijelzendo() == true )  
          {
            nKijEdzSzama++ ;
          }
        }
      }
    }
*/
    
System.out.println( "JIntvAdatTblModel::getColumnCount() -> " + nIdx) ;
    return nIdx ;
  }
  
  // Gyorsnak kell(ene) lennie
  public int getRowCount()
  {
    int nIdx = 0 ;
    int nIntSzam    = 0 ;
    int nMaxIntSzam = 0 ;
    
    CHRMFile aHRMFile[] = null ;
    
    if ( IMFActionListener.m_cPolar != null && IMFActionListener.m_cPolar.m_aHRMFile != null )
    {
      aHRMFile = IMFActionListener.m_cPolar.m_aHRMFile ;
    
      // Elvileg minden tombnek egy edzesen belu azonos meretunek kell lennie
      for ( nIdx = 0 ; nIdx < aHRMFile.length ; nIdx++ )
      {
        
if ( aHRMFile[nIdx] != null )
{
System.out.println( "JIntvAdatTblModel::getRowCount() m_cPolar.m_aHRMFile[" + nIdx + "] != null") ;
}
else
{
System.out.println( "JIntvAdatTblModel::getRowCount() m_cPolar.m_aHRMFile[" + nIdx + "] == null") ;
}
        
        if ( aHRMFile[nIdx] != null && aHRMFile[nIdx].GetKijelzendo() == true )  
        {
          nIntSzam = aHRMFile[nIdx].GetIntSzama() ;
        
          if ( nIntSzam > nMaxIntSzam )
          {
            nMaxIntSzam = nIntSzam ;
          }
        }
      }
    }
    
System.out.println( "JIntvAdatTblModel::getRowCount() -> " + nMaxIntSzam) ;
    
    return nMaxIntSzam ;
  }

  public Object getValueAt(int nSor, int nOszlop)
  {
    JIntvTimes jLokIntvTimes  = null ;
    String    sEgyBejegyzes = "" ;

    CHRMFile aHRMFile[] = null ;
    Set<EIntTimesMezok> sKijelzendoParameterek = null ;
  
    if ( IMFActionListener.m_cPolar != null && IMFActionListener.m_cPolar.m_aHRMFile != null )
    {
      aHRMFile = IMFActionListener.m_cPolar.m_aHRMFile ;

      if ( aHRMFile[nOszlop] != null )
      {
        sKijelzendoParameterek = m_jParamValPanel.GetKijelzendoParameterek() ;
        jLokIntvTimes = aHRMFile[nOszlop].GetIntTimes() ;
        sEgyBejegyzes = jLokIntvTimes.Sztringge( nSor, '|', sKijelzendoParameterek) ;
System.out.println( "JIntvAdatTblModel::getValueAt() m_cPolar.m_aHRMFile[" + nOszlop + "] != null") ;
      }
      else
      {
System.out.println( "JIntvAdatTblModel::getValueAt() m_cPolar.m_aHRMFile[" + nOszlop + "] == null") ;
      }
    }
    
System.out.println( "JIntvAdatTblModel::getValueAt() -> sEgyBejegyzes(" + nSor +  ", " + nOszlop + ") = " + sEgyBejegyzes) ;
    return sEgyBejegyzes ;
  }
  
  public String getColumnName(int nOszlop)
  {
System.out.println( "JIntvAdatTblModel::getColumnName() -> nOszlop=" + nOszlop) ;
    return Integer.toString( nOszlop) ;
  }
  
  public Class getColumnClass( int c)
  {
System.out.println( "JIntvAdatTblModel::getColumnClass()") ;
    return getValueAt( 0, c).getClass() ;
  }
  
  public boolean isCellEditable(int row, int col)
  {
    // Ha ez a fv. falset-t ad vissza, TableColumn::setCellEditor( new DefaultCellEditor( jComboBox)) ;
    // altal beallitott objektek nem lathatok !
    return false ;
  }

  public void setValueAt(Object aValue, int row, int column)
  {
  }
}