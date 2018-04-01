package cpolar;

import java.util.Arrays;

/*
 * Created on 2006.07.30.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author tamas_2
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JTomb <T>
{
  protected final int m_nFoglBlkMer = 5 ;

  protected int m_nElemekSzama  = 0 ;
  protected int m_nFoglaltMeret = m_nFoglBlkMer ;
  
  protected Object m_aTomb[] = new Object[m_nFoglBlkMer] ;

  public JTomb()
  {
    super();
    
    m_nElemekSzama = 0 ;
  }
  
  public int Meret()
  {
    return m_nElemekSzama ;
  }
  
  public T Visszaad( int nIdx)throws ArrayIndexOutOfBoundsException
  {
    if ( nIdx < m_nElemekSzama )
    {
      return (T) m_aTomb[nIdx] ;
    }

    throw new ArrayIndexOutOfBoundsException() ;    
  }

  public void Beallit( int nIdx, T jUjElem)throws ArrayIndexOutOfBoundsException
  {
    if ( nIdx < m_nElemekSzama )
    {
      m_aTomb[nIdx] = jUjElem ;
    }

    throw new ArrayIndexOutOfBoundsException() ;    
  }
  
  public void Hozzaad( T jUjElem)
  {
    int nIdx = 0 ;
    Object aIdglTomb[] = null ;
    
    // Kell-e az uj elem miatt ujrafoglalni az m_pNagydijak tombot
    if ( m_nElemekSzama+1 > m_nFoglaltMeret )
    {
      aIdglTomb = new Object[m_nFoglaltMeret+m_nFoglBlkMer] ;

      if ( aIdglTomb == null )
      {
        return  ; // ???
      }

      // Mivel ez mostmar teny :
      m_nFoglaltMeret = m_nFoglaltMeret + m_nFoglBlkMer ;

      for ( nIdx = 0 ; nIdx < m_nElemekSzama ; nIdx++ )
      {
        aIdglTomb[nIdx] = m_aTomb[nIdx] ;
      }

      // Ez 1:1-ben atmasolja a regit az ujba : meg a meretet is ! Eztan az eredeti, kisebb meret szamit !
      // aIdglTomb = Arrays.copyOf( m_aTomb, m_nElemekSzama, Object[].class) ;
      
      // A megnovelt tabla cimenek atmasolasa az eredeti helyere
      m_aTomb = aIdglTomb ;
    }
    
    m_aTomb[m_nElemekSzama] = jUjElem ;

    m_nElemekSzama = m_nElemekSzama + 1 ;
  }
  
  public void Kiurit()
  {
    int nIdx = 0 ;
    
    for ( nIdx = 0 ; nIdx < m_nElemekSzama ; nIdx++ )
    {
      m_aTomb[nIdx] = null ;
    }
  
    m_nElemekSzama =  0 ;
  }

  public void Felszabadit()
  {
    Kiurit() ;
    
    m_nFoglaltMeret = 0 ;
    
    m_aTomb = null ;
  }
  
  public static void main(String[] args)
  {
    Integer nI0 = new Integer( 0) ;
    Integer nI1 = new Integer( 1) ;
    Integer nI2 = new Integer( 2) ;
    Integer nI3 = new Integer( 3) ;
    Integer nI4 = new Integer( 4) ;
    Integer nI5 = new Integer( 5) ;
    Integer nI6 = new Integer( 6) ;
    Integer nI7 = new Integer( 7) ;
    Integer nI8 = new Integer( 8) ;
    Integer nI9 = new Integer( 9) ;
    
    JTomb aTomb = new JTomb() ;

    aTomb.Hozzaad( nI0) ;
    aTomb.Hozzaad( nI1) ;
    aTomb.Hozzaad( nI2) ;
    aTomb.Hozzaad( nI3) ;
    aTomb.Hozzaad( nI4) ;
    aTomb.Hozzaad( nI5) ;
    aTomb.Hozzaad( nI6) ;
    aTomb.Hozzaad( nI7) ;
    aTomb.Hozzaad( nI8) ;
    aTomb.Hozzaad( nI9) ;

    for ( int nIdx = 0 ; nIdx < aTomb.Meret() ; nIdx++ )
    {
      System.out.println( "[" + nIdx + "]=" + aTomb.Visszaad(nIdx)) ;
    }
  }
}
