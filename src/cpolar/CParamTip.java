package cpolar;
/*
 * Created on 2005.03.12.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author tamas_2
 *
 * "enum"-ok a parametertipusokra
 *
 */
//"enum"-ok a parametertipusokra

public class CParamTip
{
  public final static int eSpeed      = 0 ;
  public final static int eCadence    = 1 ;
  public final static int eHeartRate  = 2 ;
  public final static int eAltitude   = 3 ;
  public final static int eDistance   = 4 ;
  public final static int eMax        = 5 ;

  /**
   * @uml.property  name="m_nAktParamTip"
   */
  public int m_nAktParamTip = eSpeed ;
  
	/**
	 * 
	 */
	public CParamTip()
	{
		super();
		// TODO Auto-generated constructor stub
	}

}
