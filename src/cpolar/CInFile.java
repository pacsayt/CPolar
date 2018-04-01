package cpolar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
/*
 * Created on 2006.07.30.
 *
 * Mivel mar nemcsak a CHRMFile-ban hasznalva : JIntTimes-ben is
 * 
 */

/**
 * @author tamas_2
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CInFile extends BufferedReader
{
  public CInFile( String sFilename) throws FileNotFoundException
  {
    super( new FileReader( sFilename)) ;
  }

  public CInFile( File fFile) throws FileNotFoundException
  {
    this( fFile.getPath()) ;
  }
}