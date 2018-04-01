package cpolar;
/*
 * Created on 2006.07.02.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

//import java.util.* ;

/**
 * @author tamas_2
 *
 * 0:17:37.2 - oo:pp:mpmp.tmp adatok tarolasara es muveletek vegzesere
 *             sajnos igenytelen JAVA-ban nem lehet operatorokat definialni
 * 
 * Az -100 <= orat < 100-ra korlatozom. Egyebkent sem lehet nagyobb, mint oo:pp:mpmp.tmp, a file formatum leirasaban legalabbis ez szerepel
 *
 */
public class JKorIdo extends Object implements Comparable<JKorIdo>
{
  protected int m_nOra            = 0 ; // +, - ez hordozza a szam elojelet
  protected int m_nPerc           = 0 ; // +
  protected int m_nMasodperc      = 0 ; // +
  protected int m_nTizedmasodperc = 0 ; // +
  
  public JKorIdo()
  {
    super();
  }

  boolean Init( int nIniOra, int nIniPerc, int nIniMasodperc, int nIniTizedmasodperc)
  {
    boolean bRC = false ;

    if ( -100 <= nIniOra && nIniOra < 100 )
    {
      if ( 0 <= nIniPerc && nIniPerc < 60 )
      {
        if ( 0 <= nIniMasodperc && nIniMasodperc < 60 )
        {
          if ( 0 <= nIniTizedmasodperc && nIniTizedmasodperc <= 9 )
          {
            // Minden jo, ha a vege jo : at lehet venni az adatokat
            m_nOra            = nIniOra ;
            m_nPerc           = nIniPerc ;
            m_nMasodperc      = nIniMasodperc ;
            m_nTizedmasodperc = nIniTizedmasodperc ;

            bRC = true ;
          }
        }
      }
    }
    
    return bRC ;
  }
  
  // A sztringnek a oo:pp:mpmp.tmp formatumban kell tartalmazni a koridot (egy tizedes)
  boolean Init( String sIniOrPeMpTimp)
  {
    int nOraVegeIdx  = 0 ;
    int nPercVegeIdx = 0 ;
    int nMsdPcVegeIdx = 0 ;
    
    boolean bRC = false ;
    
    if ( sIniOrPeMpTimp != null && sIniOrPeMpTimp.length() != 0 )
    {
      // Ora
      nOraVegeIdx = sIniOrPeMpTimp.indexOf( ':') ;
      
      if ( nOraVegeIdx > 0 ) // ???
      {
        try
        {
          m_nOra = Integer.parseInt( sIniOrPeMpTimp.substring( 0, nOraVegeIdx), 10) ;
        
          if ( -100 <= m_nOra && m_nOra < 100 )
          {  
            // Perc
            nPercVegeIdx = sIniOrPeMpTimp.indexOf( ":", nOraVegeIdx+1) ;

            // Legalabb egy karakternek kell lennie a ket : kozott
            if ( nPercVegeIdx > nOraVegeIdx + 1 )
            {
              m_nPerc = Integer.parseInt( sIniOrPeMpTimp.substring( nOraVegeIdx+1, nPercVegeIdx), 10) ;

              if ( 0 <= m_nPerc && m_nPerc < 60 )
              {
                // Masodperc
                nMsdPcVegeIdx = sIniOrPeMpTimp.indexOf( ".", nPercVegeIdx+1) ;

                // Legalabb egy karakternek kell lennie a : es . kozott
                if ( nMsdPcVegeIdx > nPercVegeIdx + 1 )
                {
                  m_nMasodperc = Integer.parseInt( sIniOrPeMpTimp.substring( nPercVegeIdx+1, nMsdPcVegeIdx), 10) ;

                  if ( 0 <= m_nMasodperc && m_nMasodperc < 60 )
                  {
                    if ( nMsdPcVegeIdx + 1 < sIniOrPeMpTimp.length() )
                    {
                      m_nTizedmasodperc = Integer.parseInt( sIniOrPeMpTimp.substring( nMsdPcVegeIdx+1), 10) ;

                      if ( 0 <= m_nTizedmasodperc && m_nTizedmasodperc < 10 )
                      {
                        bRC = true ;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        catch ( NumberFormatException cNumberFormatException )
        {
          bRC = false ;
        }
      }
    }
 
    return bRC ;
  }

  // negativ idovel nem engedek inicializalni ... igy me megis kerulhet bele negativ ora
  public JKorIdo Kulonbseg( JKorIdo jKivonando) // throws NumberFormatException ???
  {
    int nOra            = 0 ;
    int nPerc           = 0 ;
    int nMasodperc      = 0 ;
    int nTizedmasodperc = 0 ;
    
    int nAtvitel = 0 ;
    
    JKorIdo jKorIdo = null ;
    
    if ( jKivonando != null )
    {
      nTizedmasodperc = m_nTizedmasodperc - jKivonando.m_nTizedmasodperc ;
      
      if ( nTizedmasodperc < 0 )
      {
        nTizedmasodperc = nTizedmasodperc + 10 ; // 3 - 4 = -1  + 10 kene (?)
        nAtvitel = 1 ;
      }
      
      nMasodperc = m_nMasodperc - jKivonando.m_nMasodperc - nAtvitel ;
      
      nAtvitel = 0 ;

      if ( nMasodperc < 0 )
      {
        nAtvitel = 1 ;
        
        nMasodperc = nMasodperc + 60 ;
      }
      
      nPerc = m_nPerc - jKivonando.m_nPerc - nAtvitel ;
      
      nAtvitel = 0 ;

      if ( nPerc < 0 )
      {
        nAtvitel = 1 ;

        nPerc = nPerc + 60 ;
      }
      
      nOra = m_nOra - jKivonando.m_nOra - nAtvitel ;
    }

    jKorIdo = new JKorIdo() ;
    jKorIdo.Init( nOra, nPerc, nMasodperc, nTizedmasodperc) ;

//    cProbaString = jKorIdo.toString() ;
    
    return jKorIdo ;
  }

  public int ToInt()
  {
    int nMasodperc = 0 ;
    
    nMasodperc = 3600*m_nOra + m_nPerc + m_nMasodperc ;
    
    return nMasodperc ;
  }
    
  @Override  
  public String toString()
  {
    String sPercElvalaszto  = ":" ;
    String sMasPcElvalaszto = ":" ;
    
    if ( m_nPerc < 10 )
    {
      sPercElvalaszto  = ":0" ;
    }
    
    if ( m_nMasodperc < 10 )
    {
      sMasPcElvalaszto  = ":0" ;
    }
    
    return Integer.toString( m_nOra) + sPercElvalaszto +
           Integer.toString( m_nPerc) + sMasPcElvalaszto +
           Integer.toString( m_nMasodperc) + "." +
           Integer.toString( m_nTizedmasodperc) ;
  }
  
  @Override
  public int compareTo( JKorIdo jMasikKorIdo)
  {
    int nMindkettoNegativ = 1 ;

    if ( m_nOra < 0 && jMasikKorIdo.m_nOra < 0 )
    {
      nMindkettoNegativ = -1 ;
    }
    
    if ( m_nOra < jMasikKorIdo.m_nOra )
    {
      return -1*nMindkettoNegativ ;
    }
    else
    {
      if ( m_nOra > jMasikKorIdo.m_nOra )
      {
        return 1*nMindkettoNegativ ;
      }
      else // m_nOra == 
      {
        if ( m_nPerc < jMasikKorIdo.m_nPerc )
        {
          return -1*nMindkettoNegativ ;
        }
        else
        {
          if ( m_nPerc > jMasikKorIdo.m_nPerc )
          {
            return 1*nMindkettoNegativ ;
          }
          else // m_nPerc == 
          {
            if ( m_nMasodperc < jMasikKorIdo.m_nMasodperc )
            {
              return -1*nMindkettoNegativ ;
            }
            else
            {
              if ( m_nMasodperc > jMasikKorIdo.m_nMasodperc )
              {
                return 1*nMindkettoNegativ ;
              }
              else // m_nMasodperc == 
              {
                if ( m_nTizedmasodperc < jMasikKorIdo.m_nTizedmasodperc )
                {
                  return -1*nMindkettoNegativ ;
                }
                else
                {
                  if ( m_nTizedmasodperc > jMasikKorIdo.m_nTizedmasodperc )
                  {
                    return 1*nMindkettoNegativ ;
                  }
                  else // m_nTizedmasodperc == 
                  {
                    return 0 ;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  public static void main(String[] args)
  {
    JKorIdo cProbaKorido   = new JKorIdo() ;
    JKorIdo cProbaKorido2  = new JKorIdo() ;
    JKorIdo cNegativKorido = new JKorIdo() ;

    JKorIdo cEredmeny     = null ;
    String  cEredmenyStr  = null ;
    int     nEredmeny     = 0 ;
//    cProbaKorido.Init( "0:00:00.0") ;
//    cProbaKorido.Init( "12:34:56.7") ;
//    cNegativKorido.Init( "-12:34:56.7") ;
    
//    cProbaKorido.Init( ":34:56.7") ;
//    cProbaKorido.Init( "12::56.7") ;
//    cProbaKorido.Init( "12:34:.7") ;
//    cProbaKorido.Init( "12:34:56.") ;

//    cProbaKorido.Init( "1234:56.7") ;
//   cProbaKorido.Init( "12:34:567") ;
//    cProbaKorido.Init( "1234567") ;
  
//    cProbaKorido.Init( "a:00:00.0") ;
//    cProbaKorido.Init( "0:bb:00.0") ;
//    cProbaKorido.Init( "0:00:cc.0") ;
//    cProbaKorido.Init( "0:00:00.d") ;
    
//    cProbaKorido.Init( "2:02:02.2") ;
//    cProbaKorido2.Init( "1:01:01.1") ;
//    cProbaKorido.Init( "2:02:02.2") ;
//  cProbaKorido2.Init( "1:01:01.3") ;
//    cProbaKorido2.Init( "3:03:03.3") ;

//    cEredmenyStr = cProbaKorido.toString()  ;
//    cEredmenyStr = cProbaKorido2.toString() ;
    
//    cEredmeny = cProbaKorido2.Kulonbseg( cProbaKorido) ;
//    cEredmenyStr = cEredmeny.toString() ;

//    System.out.println( cProbaKorido.toString() + " - " + cProbaKorido2 + " = " + cEredmenyStr) ;
    
    cProbaKorido2.Init( "3:03:03.3") ;
    cProbaKorido.Init(  "2:02:02.4") ;
    cEredmeny = cProbaKorido2.Kulonbseg( cProbaKorido) ;
    cEredmenyStr = cEredmeny.toString() ;
    System.out.println( cProbaKorido2.toString() + " - " + cProbaKorido + " = " + cEredmenyStr) ;

    cProbaKorido2.Init( "3:03:03.3") ;
    cProbaKorido.Init(  "2:02:04.2") ;
    cEredmeny = cProbaKorido2.Kulonbseg( cProbaKorido) ;
    cEredmenyStr = cEredmeny.toString() ;
    System.out.println( cProbaKorido2.toString() + " - " + cProbaKorido + " = " + cEredmenyStr) ;

    cProbaKorido2.Init( "3:03:03.3") ;
    cProbaKorido.Init(  "2:04:02.2") ;
    cEredmeny = cProbaKorido2.Kulonbseg( cProbaKorido) ;
    cEredmenyStr = cEredmeny.toString() ;
    System.out.println( cProbaKorido2.toString() + " - " + cProbaKorido + " = " + cEredmenyStr) ;

    cProbaKorido2.Init( "3:03:03.3") ;
    cProbaKorido.Init(  "2:03:03.4") ;
    cEredmeny = cProbaKorido2.Kulonbseg( cProbaKorido) ;
    cEredmenyStr = cEredmeny.toString() ;
    System.out.println( cProbaKorido2.toString() + " - " + cProbaKorido + " = " + cEredmenyStr) ;

    // Osszehasonlitas --------------------------------------------------------------------------
    cProbaKorido.Init(  "2:02:02.2") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido2.toString() + " == compareTo(" + cProbaKorido + ") = " + nEredmeny) ;

    cProbaKorido.Init(  "2:02:02.3") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;

    cProbaKorido.Init(  "2:02:03.2") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;
    
    cProbaKorido.Init(  "2:03:02.2") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;

    cProbaKorido.Init(  "3:02:02.3") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;
    
    cProbaKorido.Init(  "-2:02:02.2") ;
    cProbaKorido2.Init( "2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;

    cProbaKorido.Init(  "-2:02:02.3") ;
    cProbaKorido2.Init( "-2:02:02.2") ;
    nEredmeny = cProbaKorido.compareTo( cProbaKorido2) ;
    System.out.println( cProbaKorido.toString() + " == compareTo(" + cProbaKorido2 + ") = " + nEredmeny) ;
    
  }
}
