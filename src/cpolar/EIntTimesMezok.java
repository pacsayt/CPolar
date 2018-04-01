/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpolar;

/**
 *
 * @author tamas
 */
public enum EIntTimesMezok
{
  KORIDO( 0), // ( 0) - lehet, nem kell az ordinal() miatt
  HR( 1),
  HRMIN( 2),
  HRAVG( 3),
  HRMAX( 4),
  FLAGS( 5),
  RCVTIME( 6),
  RCVHR( 7),
  SPD( 8),
  CAD( 9),
  ALT( 10),
  SOR3( 11),
  LAPTYPE( 12),
  DST( 13),
  POWER(14),
  TEMP( 15),
  PHASE( 16),
  AIRPR( 17),
  SOR5( 18) ;
  
  // A kijelezheto parameterek neve l. JIntTimes / JParamValPanel
  protected static final String m_aMezoNevek[] = { "Korido", "HR", "HRmin", "HRavg", "HRmax",
                                                   "Flags", "RecTime", "RecHR", "SPD", "CAD", "ALT",
                                                   "Sor3",
                                                   "LapTy", "DST", "Power", "10*TEMP", "Phase", "AirPr",
                                                   "Sor5"} ;
  
  protected final int m_nErtek ; // ( 0) - lehet, nem kell az ordinal() miatt
  
  EIntTimesMezok( int nTipus)
  {
    m_nErtek = nTipus ;
  }
  
  public String MezoNeve()
  {
    return m_aMezoNevek[m_nErtek] ;
  }
  
  public static EIntTimesMezok IntBolEnum( int nOrdinal)
  {
    EIntTimesMezok eIntTimesMezok = null ;
    
    switch ( nOrdinal)
    {
      case 0 :
        eIntTimesMezok = KORIDO ;
        break ;
      case 1 :
        eIntTimesMezok = HR ;
        break ;
      case 2 :
        eIntTimesMezok = HRMIN ;
        break ;
      case 3 :
        eIntTimesMezok = HRAVG ;
        break ;
      case 4 :
        eIntTimesMezok = HRMAX ;
        break ;
      case 5 :
        eIntTimesMezok = FLAGS ;
        break ;
      case 6 :
        eIntTimesMezok = RCVTIME ;
        break ;
      case 7 :
        eIntTimesMezok = RCVHR ;
        break ;
      case 8 :
        eIntTimesMezok = SPD ;
        break ;
      case 9 :
        eIntTimesMezok = CAD ;
        break ;
      case 10 :
        eIntTimesMezok = ALT ;
        break ;
      case 11 :
        eIntTimesMezok = SOR3 ;
        break ;
      case 12 :
        eIntTimesMezok = LAPTYPE ;
        break ;
      case 13 :
        eIntTimesMezok = DST ;
        break ;
      case 14 :
        eIntTimesMezok = POWER ;
        break ;
      case 15 :
        eIntTimesMezok = TEMP ;
        break ;
      case 16 :
        eIntTimesMezok = PHASE ;
        break ;
      case 17 :
        eIntTimesMezok = AIRPR ;
        break ;
      case 18 :
        eIntTimesMezok = SOR5 ;
        break ;
      default :
        eIntTimesMezok = KORIDO ;
        break ;
    }
    
    return eIntTimesMezok ;
  }
  
  public static void main( String [] args)
  {
    EIntTimesMezok eKorido = EIntTimesMezok.KORIDO ;
    
    // Note: The constructor for an enum type must be package-private or private access.
    // It automatically creates the constants that are defined at the beginning of the enum body.
    // You cannot invoke an enum constructor yourself. 
//    EIntTimesMezok eEnumEgyMasik = new EIntTimesMezok( 1) ;
    EIntTimesMezok eHR = EIntTimesMezok.HR ;
    
    // Pelda, mit lehet csinalni
    switch ( eKorido )
    {
      case KORIDO :
        System.err.println( "KORIDO") ;
        break ;
      case HR :
        System.err.println( "HR") ;
        break ;
      case HRMIN :
        System.err.println( "HRMIN") ;
        break ;
      default :
        System.err.println( "nem az elso 3 ertek") ;
    }
    
    if ( eKorido == eHR )
    {
      System.err.println( "eKorido == eHR") ;
    }
    else
    {
      System.err.println( "eKorido != eHR") ;
    }
    
    // OrderSubscriberAdaptor-ban valami hasonlo van (de nem azert, mert ordinal() nelkul nem megy ;-)) :
    if ( eKorido.ordinal() == eHR.ordinal() )
    {
      System.err.println( "eKorido.ordinal() == eHR.ordinal()") ;
    }
    else
    {
      System.err.println( "eKorido.ordinal() != eHR.ordinal()") ;
    }
  }
}