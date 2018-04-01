package cpolar;
/*
 * JIntvTimes.java
 * Created on 2006.07.02.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// import java.io.* ;
import java.io.FileNotFoundException ;
import java.io.IOException;
import java.util.Set;

/**
 * @author tamas_2
 *
 * Az IntTimes szekcio adatainak beolvasasara es tarolasara
 * [IntTimes]
 * 1. sor : 2:03:22.9	157	117	133	160 |LAP|HR |HRmin|HRavg  |HRmax|
 * 2. sor : 0	0	0	116	73	172     |   |SPD|CAD  |ALT    |     |
 * 3. sor : 0	0	0	0	0           |   |   |     |       |     |
 * 4. sor : 4	3058	0	240	0	0   |STP|DST|     |10*TEMP|     |
 * 5. sor : 0	0	0	0	0	0       |   |   |     |       |     |
 * ... = tovabbi IntTime, ossz n db
 * Mindegyik bejegyzes ugyanannyi szamot tartalmaz (a hianyzo adatok 0-val potolva)
 * 
 * [IntNotes]
 * 1\tIntNote
 * ...
 * n\tIntNote
 */

public class JIntvTimes extends Object
{
  static final String m_sIntTimes = "[IntTimes]" ;

  // 1. sor
  protected JTomb<JKorIdo> m_aKorIdok = new JTomb<JKorIdo>() ;
                                                                               
  // Object (Integer)-t tartalmazo din. tombok
  protected JTomb<Integer> m_aHR      = new JTomb<Integer>() ; // Integer
  protected JTomb<Integer> m_aHRmin   = new JTomb<Integer>() ; // Integer
  protected JTomb<Integer> m_aHRavg   = new JTomb<Integer>() ; // Integer
  protected JTomb<Integer> m_aHRmax   = new JTomb<Integer>() ; // Integer

  // 2. sor
  protected JTomb<String>  m_aFlags   = new JTomb<String>() ; // String
  protected JTomb<String>  m_aRcvTime = new JTomb<String>() ; // String Recovery time (seconds)
  protected JTomb<String>  m_aRcvHR   = new JTomb<String>() ; // String Recovery HR   (bpm)
  protected JTomb<Integer> m_aSPD     = new JTomb<Integer>() ; // Integer
  protected JTomb<Integer> m_aCAD     = new JTomb<Integer>() ; // Integer
  protected JTomb<Integer> m_aALT     = new JTomb<Integer>() ; // Integer

  // 3. sor : 1 db String eleg, mert csak kiirjuk az adatokat (100%-ban 0-kat)
  protected JTomb<String>  m_a3sor    = new JTomb<String>() ;
  
  // 4. sor
  protected JTomb<String>  m_aLapType = new JTomb<String>() ; // String         x m_aSTP m_aLapTyp
  protected JTomb<Integer> m_aDST     = new JTomb<Integer>() ; // Integer Lap Dist Manually given lap distance in meters / yards alapbol 0
  protected JTomb<String>  m_aPower   = new JTomb<String>() ; // String
  protected JTomb<Integer> m_aTEMP    = new JTomb<Integer>() ; // Integer
  protected JTomb<String>  m_aPhase   = new JTomb<String>() ; // String
  protected JTomb<String>  m_aAirPr   = new JTomb<String>() ; // String

  // 5. sor : 1 db String eleg, mert csak kiirjuk az adatokat (100%-ban 0-kat)
  protected JTomb<String>  m_a5sor    = new JTomb<String>() ; // String

/*  
  A fenti szakajto mennyisegu tomb helyett
  sajnos, nem sokat segit, mert pl. amikor ki akarok venni egy erteket, tudnom kell, az Objectet mire kasztoljam
  hacsak erre nem vezetek be kulon intezkedest pl. cast es hiba -> masik cast
  egyaltalan kellenek ezek az adatok numerikus formaban ???
  protected JTomb m_aAdatok[] = new JTomb[EIntTimesMezok.values().length] ;
*/
  
  // Kezdetben minden parameter legyen kijelzendo
 // int m_nKijelzendoParameterek = -1 ; // ezt ki lehe majd venni a Set<>  veszi at a helyet
  
//  protected Set<EIntTimesMezok> m_sKijelzendoParameterek = null ; nem tagvaltozo (nem ide valo, GUI adat), bemeno parameter a Sztrigge()-ben
  
  public JIntvTimes()
  {
    super();
    
//    m_sKijelzendoParameterek = EnumSet.noneOf( EIntTimesMezok.class) ;
//    m_sKijelzendoParameterek.addAll( IMFActionListener.m_sKijelzendoParameterek) ; // = EnumSet.of( IMFActionListener.m_sKijelzendoParameterek) ;  

//    A CHRMFile tagvaltozo inicializalasakor hivodik
//    kerdes ennek az inicializalasa letrehozaskor, ez a fv. vszleg csak ha a check boxokat nyomkodjak hivodik meg ???
//    az IF-bol meg kivettem a m_sKijelzendoParameterek=t mivel a CHRMFile-ban is benne van
  }

  public int GetIntSzama()
  {
    if ( m_aKorIdok != null )
    {
      return m_aKorIdok.Meret() ;
    }
    
    return 0 ;
  }
  
  // TODO : ezt meg csak helyfoglalo ...
  public int GetDST( int nIdx)
  {
    return 0;
  }
  
  // Az atadott file-nak meg kell mar nyitva lennie
  public boolean Beolvas( CInFile fInputFile)
  {
    int  nIdx    = 0 ;
//    int  nIgIdx  = 0 ;
//    int  nTolIdx = 0 ;
    
//    int nIdoadatVegeIdx  = 0 ;
    String sEgyInSor = null ;

    boolean bRC = false ;
    
//    String  sTeszt = new String() ;
//    Integer jTeszt = null ;

    try
    {
      if ( fInputFile != null )
      {
        sEgyInSor = fInputFile.readLine() ;

        // [IntTimes]
        while( sEgyInSor != null && !sEgyInSor.startsWith( m_sIntTimes) )
        {
          sEgyInSor = fInputFile.readLine() ;
        }

//  System.out.println( "[IntTimes]" + sEgyInSor) ;
        // Ennek a sornak igy kell kineznie :
        // 0:05:00.5  159 97  151 167 |LAP|HR |HRmin|HRavg  |HRmax|
        while ( sEgyInSor != null )
        {
          // [IntTimes] atlepese
          sEgyInSor = fInputFile.readLine() ;

          // Ures sor jelzi a szekcio veget
          if ( sEgyInSor.equals( "") == true )
          {
            return true ;
          }
              
          bRC = ElsoSorSzetszed( sEgyInSor) ;

          if ( bRC == false )
          {
            return false ;
          }
          
          // Masodik sor : 0  0 0 170 0 245           |   |   |     |SPD    |CAD  |ALT
          sEgyInSor = fInputFile.readLine() ;

          bRC = MasodikSorSzetszed( sEgyInSor) ;

          if ( bRC == false )
          {
            return false ;
          }

          // Harmadik sor: 0  0 0 0 0                 |   |   |     |       |     |
          // Ismeretlen tartalom : atugor
          sEgyInSor = fInputFile.readLine() ;
            
          if ( sEgyInSor == null )
          {
//System.out.println( "Beolvas() : 11" + sEgyInSor + ", nIdx=" + nIdx) ;
            return false ;
          }

          bRC = HarmadikSorSzetszed( sEgyInSor) ;
          
          if ( bRC == false )
          {
            return false ;
          }

          // Negyedik sor: 0  0 0 260 0 0             |STP|DST|     |10*TEMP|     |
          sEgyInSor = fInputFile.readLine() ;

          if ( sEgyInSor == null )
          {
//System.out.println( "Beolvas() : 12" + sEgyInSor + ", nIdx=" + nIdx) ;
            return false ;
          }

          bRC = NegyedikSorSzetszed( sEgyInSor) ;

          if ( bRC == false )
          {
            return false ;
          }

          // Otodik sor  : 0  0 0 0 0 0               |   |   |     |       |     |
          // Ismeretlen tartalom : atugor
          sEgyInSor = fInputFile.readLine() ;
           
          if ( sEgyInSor == null )
          {
//System.out.println( "Beolvas() : 16" + sEgyInSor + ", nIdx=" + nIdx) ;
            return false ;
          }
          
          bRC = OtodikSorSzetszed( sEgyInSor) ;

          if ( bRC == false )
          {
            return false ;
          }

          nIdx++ ;
        } // while ( sEgyInSor != null )
      } // if ( fInputFile != null )
    }
    catch ( IOException cIOException)
    {
//System.out.println( "Beolvas() : 17" + sEgyInSor + ", nIdx=" + nIdx) ;
      return false ;
    }

//System.out.println( "Beolvas() : m_aHR    : " + m_aHR.Meret()) ;
//System.out.println( "Beolvas() : m_aHRmin : " + m_aHRmin.Meret()) ;
//System.out.println( "Beolvas() : m_aHRavg : " + m_aHRavg.Meret()) ;
//System.out.println( "Beolvas() : m_aHRmax : " + m_aHRmax.Meret()) ;
//System.out.println( "Beolvas() : m_aSPD   : " + m_aSPD.Meret()) ;
//System.out.println( "Beolvas() : m_aCAD   : " + m_aCAD.Meret()) ;
//System.out.println( "Beolvas() : m_aALT   : " + m_aALT.Meret()) ;
//System.out.println( "Beolvas() : m_aSTP   : " + m_aSTP.Meret()) ;
//System.out.println( "Beolvas() : m_aDST   : " + m_aDST.Meret()) ;
//System.out.println( "Beolvas() : m_aTEMP  : " + m_aTEMP.Meret()) ;

    return true ;
  }

  private boolean ElsoSorSzetszed( String sElsoSor)
  {
//    int  nIdx    = 0 ;
    int     nIgIdx  = 0 ;
    int     nTolIdx = 0 ;
    JKorIdo jKorIdo = null ;
  
//	    int nIdoadatVegeIdx  = 0 ;

    boolean bRC = false ;
	    
//    String  sTeszt = new String() ;
//    Integer jTeszt = null ;

    // Az olcso megoldas
    if ( sElsoSor == null )
    {
//System.out.println( "ElsoSorSzetszed() : 1" + sElsoSor + ", nIdx=" + nIdx) ;
      return false ;
    }
          
    // Az idoadat vege
    nTolIdx = 0 ;
    nIgIdx  = sElsoSor.indexOf( '\t') ;
        
    if ( nIgIdx == -1 )
    {
//System.out.println( "ElsoSorSzetszed() : 2" + sElsoSor + ", nIdx=" + nIdx) ;
    }

    jKorIdo = new JKorIdo() ;
    if ( jKorIdo == null )
    {
//System.out.println( "ElsoSorSzetszed() : 3" + sElsoSor) ;
      return false ;
    }
      
    bRC = jKorIdo.Init( sElsoSor.substring( nTolIdx, nIgIdx)) ;
      
    if ( bRC == false )
    {
//System.out.println( "ElsoSorSzetszed() : 4" + sElsoSor + ", nIdx=" + nIdx) ;
      return false ;
    }
      
    m_aKorIdok.Hozzaad( jKorIdo) ;
      
    nTolIdx = nIgIdx + 1 ; // + 1
    nIgIdx  = sElsoSor.indexOf( '\t', nTolIdx) ;
      
    if ( nIgIdx == -1 )
    {
//System.out.println( "ElsoSorSzetszed() : 5" + sElsoSor + ", nIdx=" + nIdx) ;
      return false ;
    }
      
    // throws NumberFormatException
//sTeszt = sElsoSor.substring( nTolIdx, nIgIdx) ;
    m_aHR.Hozzaad( Integer.valueOf( sElsoSor.substring( nTolIdx, nIgIdx))) ;
        
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sElsoSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "ElsoSorSzetszed() : 6" + sElsoSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aHRmin.Hozzaad( Integer.valueOf( sElsoSor.substring( nTolIdx, nIgIdx))) ;

    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sElsoSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "ElsoSorSzetszed() : 7" + sElsoSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aHRavg.Hozzaad( Integer.valueOf( sElsoSor.substring( nTolIdx, nIgIdx))) ;

    nTolIdx = nIgIdx + 1 ;

    // Ez az utolso mezo a sorban, nincs tobb \t
    m_aHRmax.Hozzaad( Integer.valueOf( sElsoSor.substring( nTolIdx))) ;
    return true ;
  }
  
  private boolean MasodikSorSzetszed( String sMasodikSor)
  {
    int  nIgIdx  = 0 ;
    int  nTolIdx = 0 ;
		
//		    int nIdoadatVegeIdx  = 0 ;
//    String SElsoSor= null ;
//    String  sTeszt = new String() ;
//    Integer jTeszt = null ;
  
    if ( sMasodikSor == null )
    {
//System.out.println( "MasodikSorSzetszed() : 8" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }

    nTolIdx = 0 ;
    nIgIdx  = sMasodikSor.indexOf( '\t', nIgIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "MasodikSorSzetszed() : 10" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    
    m_aFlags.Hozzaad( sMasodikSor.substring( nTolIdx, nIgIdx)) ;
//sTeszt = sMasodikSor.substring( nTolIdx, nIgIdx) ;
    
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sMasodikSor.indexOf( '\t', nTolIdx) ;
    
    if ( nIgIdx == -1 )
    {
//System.out.println( "MasodikSorSzetszed() : 10" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }

    m_aRcvTime.Hozzaad( sMasodikSor.substring( nTolIdx, nIgIdx)) ;
    
//sTeszt = sMasodikSor.substring( nTolIdx, nIgIdx) ;
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sMasodikSor.indexOf( '\t', nTolIdx) ;
    
    if ( nIgIdx == -1 )
    {
//System.out.println( "MasodikSorSzetszed() : 10" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }

    m_aRcvHR.Hozzaad( sMasodikSor.substring( nTolIdx, nIgIdx));
//sTeszt = sMasodikSor.substring( nTolIdx, nIgIdx) ;
    
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sMasodikSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "MasodikSorSzetszed() : 9" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
//sTeszt = sMasodikSor.substring( nTolIdx, nIgIdx) ;
    m_aSPD.Hozzaad( Integer.valueOf( sMasodikSor.substring( nTolIdx, nIgIdx))) ;
        
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sMasodikSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "MasodikSorSzetszed() : 10" + sMasodikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
//sTeszt = sMasodikSor.substring( nTolIdx, nIgIdx) ;
    m_aCAD.Hozzaad( Integer.valueOf( sMasodikSor.substring( nTolIdx, nIgIdx))) ;
        
    nTolIdx = nIgIdx + 1 ;

    // Ez az utolso mezo a sorban, nincs tobb \t
//sTeszt = sMasodikSor.substring( nTolIdx) ;
    m_aALT.Hozzaad( Integer.valueOf( sMasodikSor.substring( nTolIdx))) ;

    return true ;
  }
  
  private boolean HarmadikSorSzetszed( String sHarmadikSor)
  {
    m_a3sor.Hozzaad( sHarmadikSor);
    
    return true ;
  }
  
  private boolean NegyedikSorSzetszed( String sNegyedikSor)
  {
//    int  nIdx    = 0 ;
    int  nIgIdx  = 0 ;
    int  nTolIdx = 0 ;
			    
//    String  sTeszt = new String() ;
//    Integer jTeszt = null ;
	  
    if ( sNegyedikSor == null )
    {
//System.out.println( "NegyedikSorSzetszed() : 12" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }

    nTolIdx = 0 ;
    nIgIdx  = sNegyedikSor.indexOf( '\t', nTolIdx) ;
        
    if ( nIgIdx == -1 )
    {
//System.out.println( "NegyedikSorSzetszed() : 13" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aLapType.Hozzaad( sNegyedikSor.substring( nTolIdx, nIgIdx)) ;
        
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sNegyedikSor.indexOf( '\t', nTolIdx) ;
      
    if ( nIgIdx == -1 )
    {
//System.out.println( "NegyedikSorSzetszed() : 14" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aDST.Hozzaad( Integer.valueOf( sNegyedikSor.substring( nTolIdx, nIgIdx))) ;
      
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sNegyedikSor.indexOf( '\t', nTolIdx) ;
      
    if ( nIgIdx == -1 )
    {
//System.out.println( "NegyedikSorSzetszed() : 14" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aPower.Hozzaad( sNegyedikSor.substring( nTolIdx, nIgIdx)) ;
      
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sNegyedikSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "NegyedikSorSzetszed() : 15" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aTEMP.Hozzaad( Integer.valueOf( sNegyedikSor.substring( nTolIdx, nIgIdx))) ;
      
    nTolIdx = nIgIdx + 1 ;
    nIgIdx  = sNegyedikSor.indexOf( '\t', nTolIdx) ;

    if ( nIgIdx == -1 )
    {
//System.out.println( "NegyedikSorSzetszed() : 15" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aPhase.Hozzaad( sNegyedikSor.substring( nTolIdx, nIgIdx)) ;

    nTolIdx = nIgIdx + 1 ;

    if ( nTolIdx >= sNegyedikSor.length() )
    {
//System.out.println( "NegyedikSorSzetszed() : 15" + sNegyedikSor + ", nIdx=" + nIdx) ;
      return false ;
    }
    m_aAirPr.Hozzaad( sNegyedikSor.substring( nTolIdx, sNegyedikSor.length())) ;

    return true ;
  }
  
  private boolean OtodikSorSzetszed( String sOtodikSor)
  {
	  m_a5sor.Hozzaad( sOtodikSor) ;
    
    return true ;
  }

  public void Kiurit()
  {
    // JKorIdo m_jKorIdo -> csak segedosztaly
    m_aKorIdok.Kiurit() ;
    
    // Object (Integer/String)-t tartalmazo din. tombok
    m_aHR.Kiurit()    ;
    m_aHRmin.Kiurit() ;
    m_aHRavg.Kiurit() ;
    m_aHRmax.Kiurit() ;
    
    m_aFlags.Kiurit() ;
    m_aRcvTime.Kiurit() ;
    m_aRcvHR.Kiurit() ;
    m_aSPD.Kiurit()   ;
    m_aCAD.Kiurit()   ;
    m_aALT.Kiurit()   ;
    
    m_a3sor.Kiurit()  ;
    
    m_aLapType.Kiurit() ;
    m_aDST.Kiurit()   ;
    m_aPower.Kiurit() ;
    m_aTEMP.Kiurit()  ;
    m_aPhase.Kiurit() ;
    m_aAirPr.Kiurit() ;
    
    m_a5sor.Kiurit()  ;
  }

/*
  public void KijelzendoParamBeall( Set<EIntTimesMezok> sMegjelenitendoParameterek)
  {
    if ( sMegjelenitendoParameterek != null )
    {
      m_sKijelzendoParameterek.addAll( sMegjelenitendoParameterek) ;
    }  
  }
  
  // Nem lehet kivulrol meghivni (public) mert az enum kivulrol nem lathato

  public void KijelzendoParamBeall( EIntTimesMezok... aIniKijelzendoParameterekTomb)
  {
    m_nKijelzendoParameterek = 0 ;
      
    for ( EIntTimesMezok eAktIntTimesMezok : aIniKijelzendoParameterekTomb )
    {
      // A bemeneti tomb tartalmazhat kevesebb elemet, mint a hossza -> null -> break
      if ( eAktIntTimesMezok != null )
      {
        m_nKijelzendoParameterek = m_nKijelzendoParameterek | (1 << eAktIntTimesMezok.ordinal()) ;
      }
      else
      {
        break ;
      }
    }
  }


  public void KijelzendoParamTorol( Set<EIntTimesMezok> sElrejendoParameterek)
  {
    if ( sElrejendoParameterek != null )
    {
      m_sKijelzendoParameterek.removeAll( sElrejendoParameterek) ;
    }  
  }


  public void KijelzendoParamTorol( EIntTimesMezok... aIniKijelzendoParameterekTomb)
  {
    for ( EIntTimesMezok eAktIntTimesMezok : aIniKijelzendoParameterekTomb )
    {
      m_nKijelzendoParameterek = m_nKijelzendoParameterek & ~(1 << eAktIntTimesMezok.ordinal());
    }
  }
*/
  
  // toString() helyett, mert nem ugyanazt csinalja
  // nIdx    - az [IntTimes]=en belul hanyadik rekordot
  // cElvKar - 
  public String Sztringge( int nIdx, char cElvKar, Set<EIntTimesMezok> sKijelzendoParameterek)
  {
    String sVisszatErt = new String() ;

    if ( nIdx < m_aKorIdok.Meret() )
    {
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.KORIDO) == true )
      {
        sVisszatErt = m_aKorIdok.Visszaad( nIdx).toString() + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.HR) == true )
      {
        sVisszatErt = sVisszatErt + m_aHR.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.HRMIN) == true )
      {
        sVisszatErt = sVisszatErt + m_aHRmin.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.HRAVG) == true )
      {
        sVisszatErt = sVisszatErt + m_aHRavg.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.HRMAX) == true )
      {
        sVisszatErt = sVisszatErt + m_aHRmax.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.FLAGS) == true )
      {
        sVisszatErt = sVisszatErt + m_aFlags.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.RCVTIME) == true )
      {
        sVisszatErt = sVisszatErt + m_aRcvTime.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.RCVHR) == true )
      {
        sVisszatErt = sVisszatErt + m_aRcvHR.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.SPD) == true )
      {
        sVisszatErt = sVisszatErt + m_aSPD.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.CAD) == true )
      {
        sVisszatErt = sVisszatErt + m_aCAD.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.ALT) == true )
      {
        sVisszatErt = sVisszatErt + m_aALT.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.SOR3) == true )
      {
        sVisszatErt = sVisszatErt + m_a3sor.Visszaad( nIdx) + cElvKar ;
      }
      
      if ( sKijelzendoParameterek.contains( EIntTimesMezok.LAPTYPE) == true )
      {
        sVisszatErt = sVisszatErt + m_aLapType.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.DST) == true )
      {
        sVisszatErt = sVisszatErt + m_aDST.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.POWER) == true )
      {
        sVisszatErt = sVisszatErt + m_aPower.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.TEMP) == true )
      {
        sVisszatErt = sVisszatErt + m_aTEMP.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.PHASE) == true )
      {
        sVisszatErt = sVisszatErt + m_aPhase.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.AIRPR) == true )
      {
        sVisszatErt = sVisszatErt + m_aAirPr.Visszaad( nIdx) + cElvKar ;
      }

      if ( sKijelzendoParameterek.contains( EIntTimesMezok.SOR5) == true )
      {
        sVisszatErt = sVisszatErt + m_a5sor.Visszaad( nIdx) ;
      }
    }
    
    return sVisszatErt ;
  }

  public String Sztringge( char cElvKar, Set<EIntTimesMezok> sKijelzendoParameterek)
  {
    int nIdx = 0 ;
    StringBuilder sVisszatErt = new StringBuilder() ;
	
    if ( sKijelzendoParameterek != null )
    {
      for ( nIdx = 0 ; nIdx < m_aKorIdok.Meret() ; nIdx++ )
      {
        sVisszatErt.append( Sztringge( nIdx, cElvKar, sKijelzendoParameterek)) ;
      }
    }
    
    return sVisszatErt.toString() ;
  }
  
  public void Kiir( Set<EIntTimesMezok> sKijelzendoParameterek)
  {
  	System.out.println( Sztringge( '\t', sKijelzendoParameterek)) ;
  }

  public static void main(String[] args)
  {
    CInFile cTesztFile = null ;
    JIntvTimes jTesztIntTimes = null ;
    
    jTesztIntTimes = new JIntvTimes() ;
    
    try
    {
      cTesztFile = new CInFile( "G:\\TAMAS\\PROG\\CPolar_adatok\\2011\\11040201.hrm" ) ;
    }
    catch ( FileNotFoundException eFileNotFoundException)
    {
      System.out.println( "JIntTimes::main()" + eFileNotFoundException.toString()) ;
    }
    
    if ( jTesztIntTimes.Beolvas( cTesztFile) == false )
    {
      System.out.println( "JIntTimes::main() jTesztIntTimes.Beolvas( CInFile fInputFile) -> false") ;
    }
    
    System.out.println( "JIntTimes::main() jTesztIntTimes.jTesztIntTimes() -> \n" + jTesztIntTimes.toString()) ;
  }
}