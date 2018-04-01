package cpolar;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Created on 2006.07.30.
 *
 *  Hatha mar nemcsak a CHRMFile-ban lesz hasznalva
 * 
 */

/**
 * @author tamas_2
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class COutFile extends BufferedWriter
{
  public COutFile( String filename) throws IOException
  {
    super( new BufferedWriter( new FileWriter(filename))) ;
  }

  public COutFile( File file) throws IOException
  {
    this( file.getPath()) ;
  }
}

