package cpolar;
// CHRMFile.java hiba
// CHRMFile : beolvassa a *.hrm file-t

// A HRM file formatuma : http://fi.polar.fi/files/Polar_HRM_file%20format.pdf

// A kovetkezoket ertekeli ki a *.hrm file-bol :
// [Params]
// ...   1.     8.
// SMode=00100010 
// 1. - speed
// 2. - cadence
// 3. - heart rate
// 4. - ???
// 5. - ???
// 6. - ???
// 7. - altitude
// 8. - ???

// Date=
// Length=
// Interval=

// [Note]

//[IntTimes]
// 0:05:00.5  159 97  151 167 |LAP|HR |HRmin|HRavg  |HRmax|
// 0  0 0 170 0 245           |   |   |     |SPD    |CAD  |ALT
// 0  0 0 0 0                 |   |   |     |       |     |
// 0  0 0 260 0 0             |STP|   |     |10*TEMP|     |
// 0  0 0 0 0 0               |   |   |     |       |     |

//[IntNotes]
// 1  Vas Gereben
// 2  Term.ved ter. tabla

//
// [HRData]
// heart rate; 10*speed; cadence; altitude

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

/*
 * BufferedReader in
 *   = new BufferedReader(new FileReader("foo.in"));
*/

public class CHRMFile
{
  final String m_sFileSeparator = System.getProperty( "file.separator") ;

  // A HRM file kiertekelt szekcioinak neve
  static final String m_sParams   = "[Params]"   ;
  static final String m_sNote     = "[Note]"     ;
  static final String m_sHRData   = "[HRData]"   ;
  static final String m_sIntTimes = "[IntTimes]" ;
  static final String m_sIntNotes = "[IntNotes]" ;

  // A HRM file kiertekelt bejegyzesei a fenti szekciokon belul
  // [Params] SMode=
  static final String m_sSMode  = "SMode" ;

  // [Params] Date=
  static final String m_sDate   = "Date" ;

  // Milyen hosszu a file o:p:mp.tmp (2:35:25.0)
  // [Params] Length=
  static final String m_sLength  = "Length" ;

  // A mintavetelezes gyakorisaga 5,15,60[s]
  // [Params] Interval=
  static final String m_sInterval  = "Interval" ;

  // Az SMode egyes pozicioinak jelentese
  static final int m_nSpeed     = 1 ;
  static final int m_nCadence   = 2 ;
  static final int m_nHeartRate = 3 ;
  static final int m_nAltitude  = 7 ;

  // A m_aDistance[]-ben tarolt tavolsagok atvaltasahoz km-be
  public static final int m_nDistBolKm = 36000 ;
  
  // Buffered input file
  // FileMegnyit nyitja meg, Konvertal zarja le oket
  // Konvertal olvassa
  protected CInFile  m_fInputFile = null ;
  // Kiir irja
  protected COutFile m_fOutFile   = null ;

  // Mivel nem tudom, hogy lehetne fv-nek ugy String-et atadni,
  // hogy a valtoztatas vissza is jojjon
  protected String m_sEgyInSor  = new String() ;
//  String m_sEgyOutSor = new String() ;

  // Tobb file egyideju megnyitasa eseten, ha valamelyiket nem akarom latni
  // Ezt aztan be lehet ismet kapcsolni (?) vagy ujrafelhasznalni (?)
  protected boolean m_bKijelzendo = false ;

  // Milyen informaciot tartalmaz a file a [Params] SMode= alapjan
  protected boolean m_bSpeed     = false ;
  protected boolean m_bCadence   = false ;
  protected boolean m_bHeartRate = false ;
  protected boolean m_bAltitude  = false ;
  
  // Az m_sDate altal tartalmazott parameter erteke
  protected String m_sDatum = new String() ;
  
  // m_nMintakSzama = [Params]/Length osztva [Params]/Interval
  protected int m_nMintakSzama ;
  
  // Az eltolas a grafikon kirajzolasakor file specifikus Surface::m_nEltIdx ->
  protected int m_nEltIdx = 0 ;
  
  // Plusz valtozo, hogy az esetleges kis lepesekben tett valtozas is hasson :
  // ezt a valtozot novelve elobb utobb eleri a kovetkezo indexhez tartozo erteket,
  // mig, ha csak m_nEltTav=m_aDistance[m_nEltIdx]-t vennem, sosem lehetne
  // m_nEltIdx+1-re ugrani.
  // m_aDistance[m_nEltIdx]<=m_nEltTav<=m_aDistance[m_nEltIdx+1]
  protected int m_nEltTav = 0 ;
  
  // A mintavetelezes gyakorisaga [s]
  protected int m_nInterval ;

  // Az m_sNote ([Note]) szekcio alatti megjegyzes
  protected String m_sMegjegyzes = new String() ;

  // A tombok, melyek a mintavett adatokat taroljak 0..m_nMintakSzama-1
  protected int m_aSpeed[]     ;
  protected int m_aCadence[]   ;
  protected int m_aHeartRate[] ;
  protected int m_aAltitude[]  ;
  // Az adott indexhez adja meg az edzes kezdetetol ertendo tavolsagot
  // Szarmaztatott ertek, ha m_bSpeed == true (VanSpeedAdat())
  // int : 2^31/(5*10716{14h53m}) -> 400080 -> 4000,8 km/h atlagsebesseg
  // A metekegyseg 1/7200,2400,600 km a mintavetelezes fv-eben 5,15,60s ???
  // A metekegyseg 1/36000 (m_nDistBolKm) km a mintavetelezestol fuggetlenul, 
  // mert a mintaveteli frekvenciaval be van szorozva
  protected int m_aDistance[]  ;

  protected int m_aParTombok[][] = null  ;

  // A megjeleniteshez szukseges max / min ertekek
  protected int m_nMinSpeed = 0 ;
  protected int m_nMaxSpeed = 0 ;

  protected int m_nMinCadence = 0 ;
  protected int m_nMaxCadence = 0 ;

  protected int m_nMinHeartRate = 0 ;
  protected int m_nMaxHeartRate = 0 ;

  protected int m_nMinAltitude = 0 ;
  protected int m_nMaxAltitude = 0 ;

  protected JIntvTimes m_jIntTimes = new JIntvTimes() ;
  
  // A fenti valtozokhoz lekerdezo fv-ek
  public boolean GetKijelzendo() { return m_bKijelzendo ; }

  // Ez a fv. kapcsolja ki a file megjeleniteset
  // Egy sor parameter visszaallitasa, mert pl. ha ujat nyitok, az eltolas hamis lesz
  public void SetKijelzendo( boolean bKijelzendo)
  {
    m_bKijelzendo = bKijelzendo ;

    if ( m_bKijelzendo == false )
    {
      m_nEltIdx = 0 ;
      m_nEltTav = 0 ;

      m_nMinSpeed = 0 ;
      m_nMaxSpeed = 0 ;

      m_nMinCadence = 0 ;
      m_nMaxCadence = 0 ;

      m_nMinHeartRate = 0 ;
      m_nMaxHeartRate = 0 ;

      m_nMinAltitude = 0 ;
      m_nMaxAltitude = 0 ;
      
      m_jIntTimes.Kiurit() ;
    }
  }
  
  public boolean VanSpeedAdat()     { return m_bSpeed     ; }
  public boolean VanCadenceAdat()   { return m_bCadence   ; }
  public boolean VanHeartRateAdat() { return m_bHeartRate ; }
  public boolean VanAltitudeAdat()  { return m_bAltitude  ; }
  
  // A ciklusba rendezhetoseg erdekeben
  public boolean VanParamAdat( CParamTip cParamTip)
  {
    boolean bRC = false ;

    if ( cParamTip != null )
    {
      switch ( cParamTip.m_nAktParamTip )
      {
        case CParamTip.eAltitude :
          bRC = m_bAltitude ;
          break ;

        case CParamTip.eCadence :
          bRC = m_bCadence ;
          break ;

        case CParamTip.eDistance :
        case CParamTip.eSpeed    :
          bRC = m_bSpeed ;
          break ;

        case CParamTip.eHeartRate :
          bRC = m_bHeartRate ;
          break ;
      }
    }
    
    return bRC  ;
  }

  public String GetDatum()
  {
    return m_sDatum ;
  }

  public int GetInterval()
  {
    return m_nInterval ;
  }

  public String GetMegjegyzes()
  {
    return m_sMegjegyzes ;
  }
    
  public int GetSpeedAt( int nIdx) throws ArrayIndexOutOfBoundsException
  {
    if ( m_bSpeed == false || m_aSpeed == null || nIdx < 0 || nIdx >= m_nMintakSzama )
    {
      throw new ArrayIndexOutOfBoundsException( nIdx) ;
    }
    
    return m_aSpeed[nIdx] ;
  }

  public int GetCadenceAt( int nIdx) throws ArrayIndexOutOfBoundsException
  {
    if ( m_bCadence == false || m_aCadence == null || nIdx < 0 || nIdx >= m_nMintakSzama )
    {
      throw new ArrayIndexOutOfBoundsException( nIdx) ;
    }
    
    return m_aCadence[nIdx] ;
  }

  public int GetHeartRateAt( int nIdx) throws ArrayIndexOutOfBoundsException
  {
    if ( m_bHeartRate == false || m_aHeartRate == null || nIdx < 0 || nIdx >= m_nMintakSzama )
    {
      throw new ArrayIndexOutOfBoundsException( nIdx) ;
    }
    
    return m_aHeartRate[nIdx] ;
  }

  public int GetAltitudeAt( int nIdx) throws ArrayIndexOutOfBoundsException
  {
    if ( m_bAltitude == false || m_aAltitude == null || nIdx < 0 || nIdx >= m_nMintakSzama )
    {
      throw new ArrayIndexOutOfBoundsException( nIdx) ;
    }
    
    return m_aAltitude[nIdx] ;
  }

  public int GetDistanceAt( int nIdx) throws ArrayIndexOutOfBoundsException
  {
//System.out.println( "CHRMFile::GetDistanceAt() m_bSpeed=" + m_bSpeed + " m_aDistance" + m_aDistance + " m_nMintakSzama=" + m_nMintakSzama + " nIdx=" + nIdx) ;
    if ( m_bSpeed == false || m_aDistance == null || nIdx < 0 || nIdx >= m_nMintakSzama )
    {
//System.out.println( "CHRMFile::GetDistanceAt(): m_nMintakSzama=" + m_nMintakSzama + " nIdx=" + nIdx) ;
      
      throw new ArrayIndexOutOfBoundsException( nIdx) ;
    }
    
    return m_aDistance[nIdx] ;
  }

  public int GetParamAt( CParamTip cParameterTipus, int nIdx) throws ArrayIndexOutOfBoundsException
  {
    int nParamErt = -1 ;
    
    switch ( cParameterTipus.m_nAktParamTip )
    {
      case CParamTip.eSpeed     :
        nParamErt = GetSpeedAt( nIdx) ;
        break ;
        
      case CParamTip.eCadence   :
        nParamErt = GetCadenceAt( nIdx) ;
        break ;

      case CParamTip.eHeartRate :
        nParamErt = GetHeartRateAt( nIdx) ;
        break ;

      case CParamTip.eAltitude  :
        nParamErt = GetAltitudeAt( nIdx) ;
        break ;

      case CParamTip.eDistance  :
        nParamErt = GetDistanceAt( nIdx) ;
        break ;
        
      default :
    }

    return nParamErt ;
  }

  public JIntvTimes GetIntTimes()
  {
    return m_jIntTimes ;
  }
  
  public int GetIntSzama()
  {
    if ( m_jIntTimes != null )
    {
      return m_jIntTimes.GetIntSzama() ; // TODO ================================
    }
    
    return 0 ;
  }
  
  public int GetDST( int nIdx)
  {
    if ( m_jIntTimes != null )
    {// TODO itt persze meg mindig 0 lesz, mert a m_aDST tombnek nincsenek ertekek szamolva ...
      return m_jIntTimes.GetDST( nIdx) ; // TODO ================================
    }
    
    return 0 ; // vagy new Integer( 0) ;
  }

  
  
/* Allitolag az enum parameter nem lathato kivulrol, igy nem hivhato meg ez a fv */
/*
  public void KijelzendoParamBeall( EIntTimesMezok... aIniKijelzendoParameterekTomb)
  {
    if ( m_jIntTimes != null )
    {
      m_jIntTimes.KijelzendoParamBeall( aIniKijelzendoParameterekTomb) ;
    }
  }
  
  public void KijelzendoParamBeall( Set<EIntTimesMezok> sKijelzendoParameterek) ide sem kell
  {
    if ( m_jIntTimes != null )
    {
      m_jIntTimes.KijelzendoParamBeall( sKijelzendoParameterek) ; nem tagvaltozo ,Sztringge( , , Set<EIntTimesMezok> sKijelzendoParameterek) 
    }
  }
*/  
  
  
  // nMeter - az edzes kezdetetol megtett tavolsag 1/36 meterben
  // visszateresi ertek - az utolso index, mely az adott tavolsagnal
  // kisebb
  public int MeterBolIndex( int nMeter)
  {
    int nKisebbIdx  ;
    int nNagyobbIdx ;
    int nProbaIdx   ;
    
//System.out.println( "MeterBolIndex() nMeter=" + Integer.toString( nMeter)) ;
    // Tul nagy tavolsag eseten ez jelzi a hibat
    nKisebbIdx = -1 ;

    if ( m_nMintakSzama > 0 && nMeter <= m_aDistance[m_nMintakSzama-1] )
    {
      nKisebbIdx  = 0 ;
      nNagyobbIdx = m_nMintakSzama ;
    
      while ( nKisebbIdx < nNagyobbIdx - 1 )
      {
        nProbaIdx = (nKisebbIdx+nNagyobbIdx)/2 ;
      
        if ( nMeter < m_aDistance[nProbaIdx] )
        {
          nNagyobbIdx = nProbaIdx ;
        }
        else
        {
          nKisebbIdx = nProbaIdx ; 
        }
/*
System.out.println( "MeterBolIndex() nKisebbIdx=" + Integer.toString( nKisebbIdx) +
                    " nNagyobbIdx=" + Integer.toString( nNagyobbIdx) +
		    " m_aDistance[" + Integer.toString( nProbaIdx) + "]=" +
		    Integer.toString( m_aDistance[nProbaIdx])) ;
*/
      }
    }

//System.out.println( "MeterBolIndex() nKisebbIdx=" + Integer.toString( nKisebbIdx)) ;
    
    return nKisebbIdx ;
  }
  
  // A parameterkent megadott tavolsag (1/36 meterben) megtetelehez szukseges
  // idot adja vissza (masodpercben)
  // -1, ha a tavolsag kivul esik az edzes tartomanyan
  public int MeterbolEltIdo( int nMeter)
  {
    int nKisebbIdx  ;
//    int nNagyobbIdx ;
    int nElteltIdo  = -1 ;
    
    // Az utolso index, mely az adott tavolsagnal kisebb
    nKisebbIdx = MeterBolIndex( nMeter) ;
    
    // Ha nincs ilyen tavolsag a edzesben
    if ( nKisebbIdx == -1 )
    {
    	return nElteltIdo ;
    }

    if ( nKisebbIdx + 1 < m_nMintakSzama )
    {
      nElteltIdo = nKisebbIdx*m_nInterval +
                   m_nInterval*(nMeter - m_aDistance[nKisebbIdx]) /
                   (m_aDistance[nKisebbIdx + 1] - m_aDistance[nKisebbIdx]) ;
    }
    else
    {
    	nElteltIdo = m_nMintakSzama*m_nInterval ;
    }
      
    return nElteltIdo ;  
  }
  
  public int GetMinSpeed() { return m_nMinSpeed ; }
  public int GetMaxSpeed() { return m_nMaxSpeed ; }

  public int GetMinCadence() { return m_nMinCadence ; }
  public int GetMaxCadence() { return m_nMaxCadence ; }

  public int GetMinHeartRate() { return m_nMinHeartRate ; }
  public int GetMaxHeartRate() { return m_nMaxHeartRate ; }

  public int GetMinAltitude() { return m_nMinAltitude ; }
  public int GetMaxAltitude() { return m_nMaxAltitude ; }

  // L., mint fent csak a ciklusba rendezhetoseg erdekeben parameter a tipus
  public int GetMaxParamErt( CParamTip cParameterTipus)
  {
  	int nMaxParamErt = -1 ;
  	
    switch ( cParameterTipus.m_nAktParamTip )
    {
      case CParamTip.eSpeed     :
      	nMaxParamErt = m_nMaxSpeed ;
      	break ;
      	
      case CParamTip.eCadence   :
      	nMaxParamErt = m_nMaxCadence ;
      	break ;

      case CParamTip.eHeartRate :
      	nMaxParamErt = m_nMaxHeartRate ;
      	break ;

      case CParamTip.eAltitude  :
      	nMaxParamErt = m_nMaxAltitude ;
      	break ;

      case CParamTip.eDistance  :
        if ( m_bSpeed == true && m_aDistance != null )
        {	
          nMaxParamErt = m_aDistance[m_nMintakSzama-1] ;
        }
        
      	break ;
      	
      default :
  	}

    return nMaxParamErt ;
  }

  public int GetMinParamErt( CParamTip cParameterTipus)
  {
  	int nMinParamErt = -1 ;
  	
    switch ( cParameterTipus.m_nAktParamTip )
    {
      case CParamTip.eSpeed     :
      	nMinParamErt = m_nMinSpeed ;
      	break ;
      	
      case CParamTip.eCadence   :
      	nMinParamErt = m_nMinCadence ;
      	break ;

      case CParamTip.eHeartRate :
      	nMinParamErt = m_nMinHeartRate ;
      	break ;

      case CParamTip.eAltitude  :
      	nMinParamErt = m_nMinAltitude ;
      	break ;

      case CParamTip.eDistance  :
        	nMinParamErt = 0 ;
        
      	break ;
      	
      default :
  	}

    return nMinParamErt ;
  }

  public int GetMintakSzama() { return m_nMintakSzama ; }

  public int GetEltIdx() { return m_nEltIdx ; }

  public void SetEltIdx( int nIniEltIdx)
  {
    if ( 0 < nIniEltIdx && nIniEltIdx < m_nMintakSzama )
    {
      m_nEltIdx = nIniEltIdx ;
      m_nEltTav = m_aDistance[m_nEltIdx] ;
    }
  }
 
  // Az edzesek egymashoz kepesti es egyuttes eltologatasa ahogy a feluletrol
  // jon kilometerben (hozzaadodik), abszolut
  // fIniElt - az edzesek egyuttes eltologatasa + az edzesek egymashoz kepesti eltolasa  
  public boolean EltTavValt( int nIniElt)
  {
    int nEltIdx = 0 ;
//    int nEltTav = 0 ;
    
    boolean bRC = false ;
    
//System.out.println( "CHRMFile::EltTavValt() nIniElt=" + nIniElt) ;
    
//    nEltTav = (int)((fIniElt)*3600.0f*m_nInterval) ; // <<< 36000
    
//System.out.println( "CHRMFile::EltTavValt() nEltTav=" + nEltTav) ;

    nEltIdx = MeterBolIndex( nIniElt) ;
//System.out.println( "CHRMFile::EltTavValt() nEltIdx=" + nEltIdx)  ;

    // Hibas ertek eseten -1 jon vissza
    if ( nEltIdx != -1 )
    {
      m_nEltIdx = nEltIdx ;
      m_nEltTav = nIniElt ;

      bRC = true ;
    }
    
//System.out.println( "CHRMFile::EltTavValt() bRC=" + bRC) ;
    return bRC ;
  }
  
  public int GetEltTav() { return m_nEltTav ; }

  protected void MinMaxTorol()
  {
    m_nMinSpeed = 0 ;
    m_nMaxSpeed = 0 ;

    m_nMinCadence = 0 ;
    m_nMaxCadence = 0 ;

    m_nMinHeartRate = 0 ;
    m_nMaxHeartRate = 0 ;

    m_nMinAltitude = 0 ;
    m_nMaxAltitude = 0 ;
  }

  public void MinMaxKeres()
  {
    int nIdx = 0 ;

//System.out.println( "MinMaxKeres() m_nMintakSzama=" + Integer.toString( m_nMintakSzama)) ;

    if ( m_bSpeed == true && m_aSpeed != null )
    {
      m_nMinSpeed = m_aSpeed[0] ;
      m_nMaxSpeed = m_aSpeed[0] ;

      for ( nIdx = 1 ; nIdx < m_nMintakSzama ; nIdx++ )
      {
        if ( m_aSpeed[nIdx] > m_nMaxSpeed )
        {
          m_nMaxSpeed = m_aSpeed[nIdx] ;
        }
        else
        {
          if ( m_aSpeed[nIdx] < m_nMinSpeed )
          {
            m_nMinSpeed = m_aSpeed[nIdx] ;
          }
        }
      }
    }

    if ( m_bCadence == true && m_aCadence != null )
    {
      m_nMinCadence = m_aCadence[0] ;
      m_nMaxCadence = m_aCadence[0] ;

      for ( nIdx = 1 ; nIdx < m_nMintakSzama ; nIdx++ )
      {
        if ( m_aCadence[nIdx] > m_nMaxCadence )
        {
          m_nMaxCadence = m_aCadence[nIdx] ;
        }
        else
        {
          if ( m_aCadence[nIdx] < m_nMinCadence )
          {
            m_nMinCadence = m_aCadence[nIdx] ;
          }
        }
      }
    }

    if ( m_bHeartRate == true && m_aHeartRate != null )
    {
      m_nMinHeartRate = m_aHeartRate[0] ;
      m_nMaxHeartRate = m_aHeartRate[0] ;
//System.out.println( "m_nMinHeartRate=" + Integer.toString( m_nMinHeartRate)) ;
//System.out.println( "m_nMaxHeartRate=" + Integer.toString( m_nMaxHeartRate)) ;

      for ( nIdx = 1 ; nIdx < m_nMintakSzama ; nIdx++ )
      {
//System.out.println( "m_aHeartRate[" + Integer.toString( nIdx) + "]=" + Integer.toString( m_aHeartRate[nIdx])) ;
        if ( m_aHeartRate[nIdx] > m_nMaxHeartRate )
        {
          m_nMaxHeartRate = m_aHeartRate[nIdx] ;
//System.out.println( "m_nMaxHeartRate=" + Integer.toString( m_nMaxHeartRate)) ;
        }
        else
        {
          if ( m_aHeartRate[nIdx] < m_nMinHeartRate )
          {
            m_nMinHeartRate = m_aHeartRate[nIdx] ;
//System.out.println( "m_nMinHeartRate=" + Integer.toString( m_nMinHeartRate)) ;
          }
        }
      }
    }

    if ( m_bAltitude == true && m_aAltitude != null )
    {
      m_nMinAltitude = m_aAltitude[0] ;
      m_nMaxAltitude = m_aAltitude[0] ;

      for ( nIdx = 1 ; nIdx < m_nMintakSzama ; nIdx++ )
      {
        if ( m_aAltitude[nIdx] > m_nMaxAltitude )
        {
          m_nMaxAltitude = m_aAltitude[nIdx] ;
        }
        else
        {
          if ( m_aAltitude[nIdx] < m_nMinAltitude )
          {
            m_nMinAltitude = m_aAltitude[nIdx] ;
          }
        }
      }
    }
  }

  public void FileMegnyit( String sKonyvtar, String sFilenev) throws IOException
  {
    String sTeljFilenev ;

//    sTeljFilenev = new String() ;

//System.out.println( "sKonyvtar : " + sKonyvtar) ;
//System.out.println( "m_sFileSeparator : " + m_sFileSeparator) ;
//System.out.println( "sFilenev : " + sFilenev) ;

    sTeljFilenev = sKonyvtar + m_sFileSeparator + sFilenev ;

    m_fInputFile = new CInFile( sTeljFilenev) ;
//System.out.println( "sTeljFilenev : " + sTeljFilenev) ;
  }

  public void FileLezar() throws IOException
  {
    try 
    {
      if ( m_fInputFile != null )
      {
        m_fInputFile.close() ;
      }
    }
    catch( IOException eIOException)
    {
      System.out.println( "m_fInputFile.close() nem sikerult") ;
    }
  }

  int OraPercMpBolMp( String sOraPercMasodperc)
  {
    int nOra       = 0 ;
    int nPerc      = 0 ;
    int nMasodperc = -1 ;
    
    int nOraVege       = 0 ;
    int nPercVege      = 0 ;
    int nMasodPercVege = 0 ;
    
//System.out.println( "OraPercMpBolMp(" + sOraPercMasodperc + ")") ;

    nOraVege = sOraPercMasodperc.indexOf( ':') ;
//System.out.println( "nOraVege = " + Integer.toString( nOraVege)) ;
    
    if ( nOraVege != -1 )
    {
      nPercVege = sOraPercMasodperc.indexOf( ':', nOraVege+1) ;
//System.out.println( "nPercVege = " + Integer.toString( nPercVege)) ;
      
      if ( nPercVege != -1 )
      {
        nMasodPercVege = sOraPercMasodperc.indexOf( '.', nPercVege+1) ;
//System.out.println( "nMasodPercVege = " + Integer.toString( nMasodPercVege)) ;
	
       if ( nMasodPercVege != -1 )
       {
          try
          {   
//System.out.println( "nOra <- " + sOraPercMasodperc.substring( 0, nOraVege)) ;
            nOra =  Integer.parseInt( sOraPercMasodperc.substring( 0, nOraVege), 10) ;
	    
//System.out.println( "nPerc <- " + sOraPercMasodperc.substring( nOraVege+1, nPercVege)) ;
            nPerc = Integer.parseInt( sOraPercMasodperc.substring( nOraVege+1, nPercVege), 10) ;
	    
//System.out.println( "nMasodperc <- " + sOraPercMasodperc.substring( nPercVege+1, nMasodPercVege)) ;
            nMasodperc = Integer.parseInt( sOraPercMasodperc.substring( nPercVege+1, nMasodPercVege), 10) ;
	    
            // A kapott numerikus ertekek atszamitasa masodpercre
            if ( nPerc >= 0 && nPerc < 60 && nMasodperc >= 0 && nMasodperc < 60 )
            {
              nMasodperc = 3600*nOra + 60*nPerc + nMasodperc ;
            }
            else
            {
              nMasodperc = -1 ;
            }
          }
          catch( NumberFormatException eNumberFormatException)
          {
            nMasodperc = -1 ;
          }
        }
      }
    }
    
    return nMasodperc ;
  }

  public boolean KonfiguracioBeolv() throws IOException
  {
    int    nEgyenloIdx      = -1 ;
    int    nLengthMasodperc =  0 ;

    String sEgyInSor = null  ;
    
//    boolean bRC = false ;
    
//    sEgyInSor = new String() ; ???
    m_bSpeed     = false ;
    m_bCadence   = false ;
    m_bHeartRate = false ;
    m_bAltitude  = false ;

    m_nEltIdx    = 0     ;
    
    // [Params]
    sEgyInSor = SzekcioigElmegy( m_sParams) ;

    // Az olcso megoldas
    if ( sEgyInSor == null )
    {
//System.out.println( "[Params]" + sEgyInSor) ;
      return false ;
    }

    // [Params] ... != '[' - nehogy mas szekciobol olvasson
    sEgyInSor = SzekcioigElmegy( m_sSMode) ;
    
    // Az olcso megoldas
    if ( sEgyInSor == null )
    {
//System.out.println( "[Params]" + sEgyInSor) ;
      return false ;
    }

//System.out.println( "SMode=..." + sEgyInSor) ;
    
    // SMode=... kiertekelese
    // Mely parametereket tartalmazza ez a *.hrm file 
    nEgyenloIdx = sEgyInSor.indexOf( '=') ;
//System.out.println( "nEgyenloIdx=" + Integer.toString(nEgyenloIdx)) ;
      
    if ( nEgyenloIdx == -1 )
    {
      return false ;
    }
    
    m_bSpeed     = sEgyInSor.charAt( nEgyenloIdx + m_nSpeed)     == '1' ;
//System.out.println( "m_bSpeed=" + sEgyInSor.charAt( nEgyenloIdx + m_nSpeed)) ;
    m_bCadence   = sEgyInSor.charAt( nEgyenloIdx + m_nCadence)   == '1' ;
//System.out.println( "m_bCadence=" + sEgyInSor.charAt( nEgyenloIdx + m_nCadence)) ;
    m_bHeartRate = sEgyInSor.charAt( nEgyenloIdx + m_nHeartRate) == '1' ;
//System.out.println( "m_bHeartRate=" + sEgyInSor.charAt( nEgyenloIdx + m_nHeartRate)) ;
    m_bAltitude  = sEgyInSor.charAt( nEgyenloIdx + m_nAltitude)  == '1' ;
//System.out.println( "m_bAltitude=" + sEgyInSor.charAt( nEgyenloIdx + m_nAltitude)) ;
    
    // [Params] Date=
    sEgyInSor = SzekcioigElmegy( m_sDate) ;
//System.out.println( "[Params] Date elott") ;

    // Az olcso megoldas
    if ( sEgyInSor == null )
    {
//System.out.println( "[Params]" + sEgyInSor) ;
      return false ;
    }

    nEgyenloIdx = sEgyInSor.indexOf( '=') ;
      
    if ( nEgyenloIdx == -1 )
    {
      return false ;
    }

    m_sDatum = sEgyInSor.substring( nEgyenloIdx+1, sEgyInSor.length()) ;
//  System.out.println( "[Params] Date=" + m_sDatum) ;
    
    // [Params] Length
    // if ( bRC == true )
    sEgyInSor = SzekcioigElmegy( m_sLength) ;
//System.out.println( "Length " + sEgyInSor) ;

    if ( sEgyInSor == null )
    {
      return false ;
    }

    // Length=... kiertekelese
    // Milyen hosszu ez a *.hrm file 
    nEgyenloIdx = sEgyInSor.indexOf( '=') ;
      
    if ( nEgyenloIdx == -1 )
    {
      return false ;
    }

//System.out.println( "OraPercMpBolMp() elott : " + sEgyInSor.substring( nEgyenloIdx+1)) ;
    nLengthMasodperc = OraPercMpBolMp( sEgyInSor.substring( nEgyenloIdx+1)) ;

//System.out.println( "nLengthMasodperc " + Integer.toString(nLengthMasodperc)) ;
    if ( nLengthMasodperc == -1 )
    {
      return false ;
    }

    // Ha a Length parametert sikerult masodpercben megkapni, a mintavetel
    // gyakorisaganak segitsegevel meg lehet allapitani a mintak szamat
    // [Params] Interval - azaz a mintavetel gyakorisaga
    sEgyInSor = SzekcioigElmegy( m_sInterval) ;

//System.out.println( "Interval " + sEgyInSor) ;

    // Az olcso megoldas
    if ( sEgyInSor == null || sEgyInSor.startsWith( "[") )
    {
      return false ;
    }
    
    nEgyenloIdx = sEgyInSor.indexOf( '=') ;
      
    if ( nEgyenloIdx == -1 )
    {
      return false ;
    }
    
    try
    {
      m_nInterval =  Integer.parseInt( sEgyInSor.substring( nEgyenloIdx+1), 10) ;
                 
      // m_nMintakSzama = [Params]/Length osztva [Params]/Interval
      m_nMintakSzama = nLengthMasodperc / m_nInterval ;

//System.out.println( "KonfiguracioBeolv() nLengthMasodperc=" + Integer.toString( nLengthMasodperc)) ;
//System.out.println( "KonfiguracioBeolv() m_nInterval=" + Integer.toString( m_nInterval)) ;
//System.out.println( "KonfiguracioBeolv() m_nMintakSzama=" + Integer.toString( m_nMintakSzama)) ;
    }
    catch ( Exception eException)
    {
      m_nMintakSzama = 0 ;

      return false ;               
    }

    sEgyInSor = SzekcioigElmegy( m_sNote) ;

    if ( sEgyInSor == null )
    {
      return false ;
    }
    
    sEgyInSor = m_fInputFile.readLine() ;	  
    m_sMegjegyzes = sEgyInSor ;
//System.out.println( "sEgyInSor : " + sEgyInSor) ;

    return true ;
  }

  /* Megkeresi az atadott nevu szekciot, es az elso azutanit adja vissza
   * tovabbi feldolgozasra.
   */
  protected String SzekcioigElmegy( String sSzekcioNeve) throws IOException
  {
    String sEgyInSor = null ;
    
    if ( sSzekcioNeve != null && m_fInputFile != null )
    {
      sEgyInSor = m_fInputFile.readLine() ;
//    System.out.println( "[Note] elott") ;

      // [Note]
      // Itt a megjegyzes helye ...
      while( sEgyInSor != null &&
             !sEgyInSor.startsWith( sSzekcioNeve) )
      {
        sEgyInSor = m_fInputFile.readLine() ;   
      }
    }
    
    return sEgyInSor ;
  }
  
  boolean TombokLetrehozasa()
  {
    boolean bRC = false ;
    
//System.out.println( "TombokLetrehozasa() BEGIN") ;
    MinMaxTorol() ;
//System.out.println( "TombokLetrehozasa() m_nMintakSzama=" + m_nMintakSzama) ;

    if ( m_nMintakSzama > 0 )
    {
//System.out.println( "TombokLetrehozasa() m_nMintakSzama > 0 - true") ;
      try
      {
        if ( m_bSpeed == true )
        {
          if ( m_aSpeed != null )
          {
            if ( m_aSpeed.length < m_nMintakSzama )
            {
//System.out.println( "TombokLetrehozasa() nagyobb kell new : m_aSpeed, m_aDistance") ;
              m_aSpeed    = new int[m_nMintakSzama] ;
              m_aDistance = new int[m_nMintakSzama] ;
            }
          }
          else
          {
//System.out.println( "TombokLetrehozasa() uj kell new : m_aSpeed, m_aDistance") ;
            m_aSpeed    = new int[m_nMintakSzama] ;
            m_aDistance = new int[m_nMintakSzama] ;
          }
        }
        else
        {
          m_aSpeed    = null ;
          m_aDistance = null ;
        }
//System.out.println( "TombokLetrehozasa() if ( speed ...) utan") ;

        if ( m_bCadence == true )
        {
          if ( m_aCadence != null )
          {
            if ( m_aCadence.length < m_nMintakSzama )
            {
//System.out.println( "TombokLetrehozasa() nagyobb kell : new m_aCadence") ;
              m_aCadence = new int[m_nMintakSzama] ;
            }
          }
          else
          {
//System.out.println( "TombokLetrehozasa() uj kell new : m_aCadence") ;
            m_aCadence = new int[m_nMintakSzama] ;
          }
        }
        else
        {
          m_aCadence = null ;
        }

//System.out.println( "TombokLetrehozasa() if ( cadence ...) utan") ;

//System.out.println( "TombokLetrehozasa() m_bHeartRate:" + m_bHeartRate) ; 
//System.out.println( "TombokLetrehozasa() m_aHeartRate:" + m_aHeartRate) ;
////System.out.println( "TombokLetrehozasa() m_aHeartRate.length" + m_aHeartRate.length) ;
////System.out.println( "TombokLetrehozasa() m_nMintakSzama:" + m_nMintakSzama) ;

        if ( m_bHeartRate == true )
        {
          if ( m_aHeartRate != null )
          {
            if (  m_aHeartRate.length < m_nMintakSzama )
            {
//System.out.println( "TombokLetrehozasa() nagyobb kell : new m_aHeartRate") ;
              m_aHeartRate = new int[m_nMintakSzama] ;
            }
          }
          else
          {
//System.out.println( "TombokLetrehozasa() uj kell : new m_aHeartRate") ;
            m_aHeartRate = new int[m_nMintakSzama] ;
          }  
        }
        else
        {
//System.out.println( "TombokLetrehozasa() m_aHeartRate = null") ;
          m_aHeartRate = null ;
        }

        if ( m_bAltitude == true )
        {
          if ( m_aAltitude != null )
          {
            if ( m_aAltitude.length < m_nMintakSzama )
            {
//System.out.println( "TombokLetrehozasa() nagyobb kell : new m_aAltitude") ;
              m_aAltitude = new int[m_nMintakSzama] ;
            }
          }
          else
          {
//System.out.println( "TombokLetrehozasa() uj kell : new m_aAltitude") ;
            m_aAltitude = new int[m_nMintakSzama] ;
          }  
        }
        else
        {
          m_aAltitude = null ;
        }
        
        bRC = true ;
      }
      catch ( Exception eException)
      {
        ExceptionTrace( eException) ;
	
        m_aSpeed     = null ;
        m_aDistance  = null ;
        m_aCadence   = null ;
        m_aHeartRate = null ;
        m_aAltitude  = null ;
        
        m_bKijelzendo = false ;

        bRC = false ;
      }
    }

    // Ciklusok szervezesenel, ill. fuggvenyhivasoknal
    m_aParTombok = new int[CParamTip.eMax][] ;
    
    m_aParTombok[CParamTip.eSpeed]     = m_aSpeed     ;
    m_aParTombok[CParamTip.eCadence]   = m_aCadence   ;
    m_aParTombok[CParamTip.eHeartRate] = m_aHeartRate ;
    m_aParTombok[CParamTip.eAltitude]  = m_aAltitude  ;
    m_aParTombok[CParamTip.eDistance]  = m_aDistance  ;
    
//System.out.println( "TombokLetrehozasa() END") ;
    return bRC ;
  }
  
  boolean SorSzetszed( String sHrSpdCadAlt, int nIdx)
  {
//    int nSpeedVege     = -1 ;
//    int nCadenceVege   = -1 ;
//    int nHeartRateVege = -1 ;
//    int nAltitudeVege  = -1 ;
    
    int nElozoVege     = 0 ;
    int nAktVege       = 0 ;
 
    boolean bRC = true ;

//System.out.println( "SorSzetszed( " + sHrSpdCadAlt + "), nIdx:" + nIdx) ;

    if ( sHrSpdCadAlt != null && sHrSpdCadAlt.length() > 0 &&
         0 <= nIdx && nIdx < m_nMintakSzama )
    {
      try
      {
//System.out.println( "SorSzetszed() try...") ;
        // heart rate; 10*speed; cadence; altitude
        if ( m_bHeartRate == true )
        {
          nAktVege = sHrSpdCadAlt.indexOf( '\t') ;
//System.out.println( "nHeartRateVege:" + Integer.toString( nHeartRateVege)) ;
          if ( nAktVege != -1 )
          {
//System.out.println( "m_aHeartRate1=" + sHrSpdCadAlt.substring( 0, nHeartRateVege)) ;
            m_aHeartRate[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( 0, nAktVege), 10) ;
          }
          else
          {
            // Hatha a HeartRate az utolso es nincs utana tabulator : vegeig
//System.out.println( "m_aHeartRate2=" + sHrSpdCadAlt) ;
            m_aHeartRate[nIdx] = Integer.parseInt( sHrSpdCadAlt, 10) ;

            bRC = false ;
          }
        }

//System.out.println( "SorSzetszed() if ( m_bHeartRate == true ) utan") ;
        nElozoVege = nAktVege ;
        
        // Ha mar HeartRate sem volt, akkor Speed sem lesz
        if ( m_bSpeed == true && nElozoVege != -1 )
        {
          nAktVege = sHrSpdCadAlt.indexOf( '\t', nElozoVege+1) ;
//System.out.println( "nSpeedVege:" + Integer.toString( nSpeedVege)) ;
        
          if ( nAktVege != -1 )
          {
//System.out.println( "m_aSpeed1=" + sHrSpdCadAlt.substring( nHeartRateVege+1, nSpeedVege)) ;
            m_aSpeed[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1, nAktVege), 10) ;
          }
          else
          {
            // Hatha a Speed az utolso es nincs utana tabulator : vegeig
//System.out.println( "m_aSpeed2=" + sHrSpdCadAlt.substring( nHeartRateVege+1)) ;
            m_aSpeed[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1), 10) ;

            bRC = false ;
          }
	  
          if ( nIdx > 0 )
          {
            // A sebessegnel az aktualis es megelozo sebesseg atlagaval szamolok
            m_aDistance[nIdx] = m_aDistance[nIdx-1] + m_nInterval*(m_aSpeed[nIdx] + m_aSpeed[nIdx-1])/2 ;
          }
        }

        nElozoVege = nAktVege ;

        // Ha mar Speed sem volt, akkor Cadence sem lesz
        if ( m_bCadence == true && nElozoVege != -1 )
        {
          nAktVege = sHrSpdCadAlt.indexOf( '\t', nElozoVege+1) ;
//System.out.println( "nCadenceVege:" + Integer.toString( nCadenceVege)) ;
        
          if ( nAktVege != -1 )
          {
//System.out.println( "m_aCadence1=" + sHrSpdCadAlt.substring( nSpeedVege+1, nCadenceVege)) ;
            m_aCadence[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1, nAktVege), 10) ;
          }
          else
          {
            // Hatha a Cadence az utolso es nincs utana tabulator : vegeig
//System.out.println( "m_aCadence2=" + sHrSpdCadAlt.substring( nSpeedVege+1)) ;
            m_aCadence[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1), 10) ;

            bRC = false ;
          }
        }
        
        nElozoVege = nAktVege ;

        // Ha mar Cadence sem volt, akkor Altitude sem lesz
        if ( m_bAltitude == true && nElozoVege != -1 )
        {
          nAktVege = sHrSpdCadAlt.indexOf( '\t', nElozoVege+1) ;
//System.out.println( "nAltitudeVege:" + Integer.toString( nAltitudeVege)) ;

          if ( nAktVege != -1 )
          {
//System.out.println( "m_aAltitude1=" + sHrSpdCadAlt.substring( nCadenceVege+1, nAltitudeVege)) ;
            m_aAltitude[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1, nAktVege), 10) ;
          }
          else
          {
            // Hatha az Altitude az utolso es nincs utana tabulator : vegeig
//System.out.println( "m_aAltitude2=" + sHrSpdCadAlt.substring( nCadenceVege+1)) ;
            m_aAltitude[nIdx] = Integer.parseInt( sHrSpdCadAlt.substring( nElozoVege+1), 10) ;
          }
        }
      }
      catch( NumberFormatException eNumberFormatException)
      {
        bRC = false ;
      }
    }
    else
    {
      bRC = false ;
    }
    
    return bRC ;
  }

  // ELofordul, hogy a file-ban kevesebb adat van, mint amennyit kiszamit
  // (m_nMintakSzama = [Params]/Length osztva [Params]/Interval),
  // viszont ha az utolso elem tavolsagadata 0, a racsakirajzolas elszall
  protected void HianyzoTavolsagBeir( int nUtolsoIndex)
  {
    int nIdx = 0 ;

    nIdx = nUtolsoIndex ;
    
    // Csak, ha volt a file-ban legelabb egy minta ...
    if ( nIdx > 0 )
    {
      while ( nIdx < m_nMintakSzama )
      {
//System.out.println( "HianyzoTavolsagBeir() hianyzo adat : m_aDistance[" + nIdx + "]") ;
        // A hianyzo tavolsag beirasa
        m_aDistance[nIdx] = m_aDistance[nUtolsoIndex-1] ;
  
        nIdx++ ;
      }
    }
  }
  
  // A KonfiguracioBeolv() altal meghatarozott adattipusokat beolvassa
  // a m_aSpeed/m_aCadence/m_aHeartRate/m_aAltitude tombokbe
  public boolean AdatokBeolv() throws IOException
  {
    String sEgySor ;

    int nBeolvAdatokIdx = 0 ;

    boolean bRC = false ;

    m_nEltIdx = 0 ;

    // [IntTimes] 
    if ( m_jIntTimes != null )
    {
      if ( m_jIntTimes.Beolvas( m_fInputFile) == false )
      {
        return false ;
      }
// m_jIntTimes.Kiir() ; teszt
    }

    // [HRData]
    // 90	0	0	189
    // 92	231	0	189
    // 92	242	0	187
    // ... 
    // [HRData] megkeresese
    sEgySor = SzekcioigElmegy( m_sHRData) ; // ??? fel kellene keszulni arra is, hogy a szekciok tetszoleges sorrendben vannak (a speckoban mas sorrend van)

    // Az olcso megoldas
    if ( sEgySor == null )
    {
      m_bKijelzendo = false ;

      return false ;
    }

//System.out.println( "AdatokBeolv() sEgySor" + sEgySor) ;

    // Innentol mar csak adatok vannak a file vegeig MinMaxTorol() !
    bRC = TombokLetrehozasa() ;
    
    if ( bRC == true )
    {
      // A mintavetelezett adatok beolvasasa
      sEgySor = m_fInputFile.readLine() ;

      while( sEgySor != null && nBeolvAdatokIdx < m_nMintakSzama && bRC == true )
      {
        bRC = SorSzetszed( sEgySor, nBeolvAdatokIdx) ;
        sEgySor = m_fInputFile.readLine() ;
        nBeolvAdatokIdx++ ;
      }

      if ( bRC == true )
      {
        MinMaxKeres() ;

        m_bKijelzendo = false ;

        // ELofordul, hogy a file-ban kevesebb adat van, mint amennyit kiszamit,
        // viszont ha az utolso elem tavolsagadata 0, a racsakirajzolas elszall
        HianyzoTavolsagBeir( nBeolvAdatokIdx) ;
      }
    }
    
//System.out.println( "AdatokBeolv() : bRC=" + bRC) ;

    return bRC ;
  }

  void Kiir() throws IOException
  {
    String sEgySor ;

    int nMintakIdx = 0 ;

System.out.println( "CHRMFile::Kiir BEGIN") ;
System.out.println( "m_nMintakSzama : " + Integer.toString( m_nMintakSzama)) ;
System.out.println( "m_nInterval    : " + Integer.toString( m_nInterval))    ;

System.out.println( "n. minta PLS SPD (DST [1/36m] = DST/36 [m]) CAD ALT") ;

    for ( nMintakIdx = 0 ; nMintakIdx < m_nMintakSzama ; nMintakIdx++ )
//    for ( nMintakIdx = 0 ; nMintakIdx < 10 ; nMintakIdx++ )
//nMintakIdx = m_nMintakSzama - 1 ;
    {
      sEgySor = Integer.toString( nMintakIdx) + ". minta " ;
      
      if ( m_bHeartRate == true )
      {
        sEgySor = sEgySor + Integer.toString( m_aHeartRate[nMintakIdx]) + " " ;
      }

      if ( m_bSpeed == true )
      {
        sEgySor = sEgySor + Integer.toString( m_aSpeed[nMintakIdx]) + " (" ;
        sEgySor = sEgySor + Integer.toString( m_aDistance[nMintakIdx]) + " [1/36m] = " + m_aDistance[nMintakIdx]/36  + " m) " ;
      }

      if ( m_bCadence == true )
      {
        sEgySor = sEgySor + Integer.toString( m_aCadence[nMintakIdx]) + " " ;
      }

      if ( m_bAltitude == true )
      {
        sEgySor = sEgySor + Integer.toString( m_aAltitude[nMintakIdx]) ;
      }
      
//System.out.println( sEgySor) ;
    }
    
    m_jIntTimes.Kiir( EnumSet.allOf( EIntTimesMezok.class)) ;
  }
  
  public void ExceptionTrace( Exception eException)
  {
    System.out.println( "CHRMFile Caught Exception") ;
    System.out.println( "CHRMFile getMessage(): " + eException.getMessage()) ;
    System.out.println( "CHRMFile toString(): " + eException.toString()) ;
    System.out.println( "CHRMFile printStackTrace():") ;
    eException.printStackTrace() ; 
  }

  // A main() for the application:
  public static void main( String[] args)
  {
    int nIdx ;
    int nAktualisY = 0 ;
    CParamTip cParamTip = new CParamTip() ;
    
    CHRMFile cHRMFile ;

    cHRMFile = new CHRMFile() ;
    
    // E:\TAMAS\PROG\JAVA\ecl_wrkspc\CPolar\2008 08051701.hrm 4430 adat, 2 117 796 "meter"
    if( args.length == 2 )
    {
      try
      {  
        cHRMFile.FileMegnyit( args[0], args[1]) ;
//System.out.println( "cHRMFile.FileMegnyit() utan") ;
        cHRMFile.KonfiguracioBeolv() ;
//System.out.println( "cHRMFile.KonfiguracioBeolv() utan") ;
        cHRMFile.AdatokBeolv() ;
//System.out.println( "cHRMFile.AdatokBeolv() utan") ;
//        cHRMFile.Kiir() ;
//System.out.println( "cHRMFile.Kiir() utan") ;

        for ( nIdx = 0 ; nIdx < 4430 ; nIdx++ )
        {
          for ( cParamTip.m_nAktParamTip = CParamTip.eSpeed ; cParamTip.m_nAktParamTip < CParamTip.eMax ; cParamTip.m_nAktParamTip++ )
          {
            nAktualisY = cHRMFile.GetParamAt( cParamTip, nIdx) ;
/*            
            switch ( cParamTip.m_nAktParamTip )
            {
              case CParamTip.eSpeed     :
                nAktualisY = cHRMFile.GetSpeedAt( nIdx) ;
                break ;
                
              case CParamTip.eCadence   :
                nAktualisY = cHRMFile.GetCadenceAt( nIdx) ;
                break ;

              case CParamTip.eHeartRate :
                nAktualisY = cHRMFile.GetHeartRateAt( nIdx) ;
                break ;

              case CParamTip.eAltitude  :
                nAktualisY = cHRMFile.GetAltitudeAt( nIdx) ;
                break ;

              case CParamTip.eDistance  :
                nAktualisY = cHRMFile.GetDistanceAt( nIdx) ;
                break ;
                
              default :
            }
*/
          }
        }
        
        // [1]=312
        nIdx = cHRMFile.MeterbolEltIdo( 500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 3500) : " + Integer.toString(nIdx)) ;
        // [100]=66891    
        nIdx = cHRMFile.MeterbolEltIdo( 1000) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 1000) : " + Integer.toString(nIdx)) ;
        // [500]=582303
        nIdx = cHRMFile.MeterbolEltIdo( 2000) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 2000) : " + Integer.toString(nIdx)) ;
        // [1000]=1414652
        nIdx = cHRMFile.MeterbolEltIdo( 3000) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 3000) : " + Integer.toString(nIdx)) ;
        // [1864]=2556144
        nIdx = cHRMFile.MeterbolEltIdo( 4000) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 4000) : " + Integer.toString(nIdx)) ;

        // [0]=0
        nIdx = cHRMFile.MeterbolEltIdo( 0) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 0) : " + Integer.toString(nIdx)) ;

        // [0]=0, [1]=312
        nIdx = cHRMFile.MeterbolEltIdo( 8500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 8500) : " + Integer.toString(nIdx)) ;

        // [utolso]=2556144
        nIdx = cHRMFile.MeterbolEltIdo( 39500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 39500) : " + Integer.toString(nIdx)) ;

        nIdx = cHRMFile.MeterbolEltIdo( 42500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 42500) : " + Integer.toString(nIdx)) ;

        nIdx = cHRMFile.MeterbolEltIdo( 68500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 68500) : " + Integer.toString(nIdx)) ;

        nIdx = cHRMFile.MeterbolEltIdo( 70500) ;
//System.out.println( "cHRMFile.MeterbolEltIdo( 70500) : " + Integer.toString(nIdx)) ;

        cHRMFile.FileLezar() ;
//System.out.println( "cHRMFile.FileLezar() utan") ;
      }
      catch ( IOException eIOException)
      {
        try
        { 
          cHRMFile.FileLezar() ;
        }
        catch ( IOException eIOException2)
        {
        }
      }
    }
    else
    {
      System.out.println( "CHRMFile hasznalat : CHRMFile konyvtar *.hrm file") ;
    }
  }
}
