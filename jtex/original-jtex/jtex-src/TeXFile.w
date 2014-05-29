
% Nb: The file TeXFile.w must be passed through cweave before LaTeX-ing.
%    cweave -j TeXFile.w
%    latex TeXFile
% To create the TeXFile Java class:
%    ctangle -j TeXFile.w

\documentclass[12pt,a4paper]{cweb}
% \usepackage[hypertex]{hyperref}
\usepackage{url}
\usepackage{amsmath}
\usepackage{amsfonts}

\def\defin{define} % As in webmac.tex
\def\C{\textsc{C}}
\def\Java{\textsc{Java}}
\def\D{\defin{define}} % macro definition

\def\CwebRankNoEject{1}

\title{TeXFile: A \Java\ class for locating files for \TeX-related programs}

\author{Timothy Murphy\\
\texttt{<tim@@maths.tcd.ie>}\\
School of Mathematics, Trinity College Dublin}

\date{\today}

\begin{document}

\maketitle

\tableofcontents

@* The package header.

Each of our output files
(corresponding to the various |public| classes we shall define)
contains the same header.

@<Package header@>=
package javaTeX;@/

import java.awt.*;@/
import java.io.*;@/
import java.util.*;@/
import javaTeX.*;@/

@ An extension of the File class.

@p @<Package header@>@;
@#
public class TeXFile extends File {

  @<Class data@>
  @<Instance data@>
  @<Constructors@>
  @<Class methods@>
}

@ @<Class data@>=
static String TEXMF = "/usr/local/lib/texmf";
static String TEXHASH = "tex.hash";

static String slash = "/";
static String colon = ":";
static Properties properties;
static File propFile;
static String tempName;

static Hashtable fileStore;

@ @<Instance data@>=
String fileName;
String fullName;
String realName;

@ @<Constructors@>=
public TeXFile( String texName ) {
  super( getFullName( texName ) );
  this.fullName = tempName;
}

@ @<Class methods@>=
private static String getFullName( String texName ) {
  String fileName;
  String prefix;
  int i;
  if ( ( i = texName.indexOf( ':' ) ) >= 0) {
    fileName = texName.substring( i + 1 );
    prefix = texName.substring( 0, i );
  } else {
    fileName = texName;
    prefix = "";
  }
  File file = new File( fileName );
  if ( file.exists() ) return fileName;
  if ( prefix.equals( "PSFONTS" ) ) {
    if ( PsFontFile.hashTable == null )
      PsFontFile.initHash();
    PsFontFile entry = (PsFontFile) PsFontFile.hashTable.get( fileName );
    if ( entry != null ) {
      fileName += ".tfm";
    } else
      return "";
  }
  initHash();
  String dirName = (String) fileStore.get( fileName );
  if ( dirName != null )
    tempName = dirName + slash + fileName;
  else
    tempName = "";
  return tempName;
}

@ @<Class methods@>=
static void initHash() {
  int lineNo = 0;
  if ( fileStore != null )
    return;
  @<Get properties@>
  fileStore = new Hashtable();
  int dirNo = 0;
  StringTokenizer strTok = new StringTokenizer( TEXMF, colon );
  while ( strTok.hasMoreTokens() ) {
    String texmf = strTok.nextToken();
    File dir = new File( texmf );
    if ( !dir.exists() ) {
      System.err.println( "Cannot find TEXMF=" + texmf );
      System.err.println( "Please edit your \"properties\" file" );
      System.exit( 1 );
    }
    if ( dirNo++ == 0 ) {
      String texHash = TEXHASH;
      File hashFile = new File( texHash );
      if ( !hashFile.exists() ) {
        texHash = TEXMF + slash + "tex.hash";
        hashFile = new File( texHash );
      }
      if ( hashFile.exists() ) {
        System.out.print( " (" + texHash );
        ObjectInputStream hash = null;
        try {
          hash = new ObjectInputStream( new BufferedInputStream(
            new FileInputStream( hashFile ) ) );
          fileStore = (Hashtable)( hash.readObject() );
          hash.close();
        }
        catch( IOException e ) {
          System.err.println("Problem reading \"tex.hash\": " 
            + e.getMessage() );
        }
        catch( ClassNotFoundException e ) {
          System.err.println("Problem reading \"tex.hash\": " 
            + e.getMessage() );
        }
        System.out.print( ") " );
        return;
      }
    }
    String listing = texmf + slash + "ls-R";
    File listFile = new File( listing );
    if ( !listFile.exists() ) {
      System.err.println( " Must have file \"$TEXMF/ls-R\" for javaTeX" );
      System.exit( 1 );
    }
    BufferedReader list = null;
    System.out.print( " (" + listing );
    try {
      list = new BufferedReader( new FileReader ( listing ) );
      String line = "";
      String dirName = "";
      while ( ( line = list.readLine() ) != null ) {
        if ( line.equals( "" ) ) continue;
        if ( line.endsWith( colon ) ) {
  	dirName = texmf + line.substring( 1, line.length() - 1 );
        } else {
          fileStore.put( line, dirName );
        }
      }
      list.close();
      System.out.print( ") " );
    }
    catch( IOException e ) {
      System.err.println( "Problem reading ls-R: " + e.getMessage() );
      System.exit(1);
    }
  }
}

@ @<Get properties@>=
String homeDir = System.getProperty( "user.home" );
String javaDir = System.getProperty( "java.home" );
slash = System.getProperty( "file.separator" );
colon = System.getProperty( "path.separator" );
String propName = "properties";
propFile = new File ( propName );
if ( !propFile.exists() ) {
  propName = homeDir + slash + ".javaTeX" + slash + "properties";
  propFile = new File ( propName );
  if ( !propFile.exists() ) {
    propName = javaDir + slash + "javaTeX.properties";
    propFile = new File ( propName );
  }
}
if ( propFile.exists() ) {
  properties = new Properties();
  try {
    DataInputStream propStream = new DataInputStream( 
      new FileInputStream( propFile ) );
    if ( propStream != null ) {
      properties.load( propStream );
    }
    TEXMF = properties.getProperty( "TEXMF", "/usr/local/lib/texmf" );
    TEXHASH = properties.getProperty( "TEXHASH", "tex.hash" );
  }
  catch( IOException e ) {
    System.err.println( "Problem reading properties file " + propName 
      + ": " + e.getMessage() );
  }
}

@* PostScript files.

@p
class PsFontFile {
  String name;
  String realName;
  String encoding;
  String source;
  String psCmd;
  int flags;
  static String mapName;
  static Hashtable hashTable;

  PsFontFile( String name ) {
    this.name = name;
  }

  static void initHash() {
    if ( mapName == null )
      mapName = TeXFile.TEXMF + TeXFile.slash + "dvips"
	+ TeXFile.slash + "psfonts.map";
    if ( hashTable != null )
      return;
    hashTable = new Hashtable();
    if ( TeXFile.propFile == null )
      TeXFile.initHash();
    try {
      @<Open map file@>
      @<Read map file into |hashTable|@>
    }
    catch( IOException e ) {
      System.err.println( "Problem reading " + mapName + ":"
	+ e.getMessage() );
    }
  }

}

@ @<Open map file@>=
File mapFile = new File( mapName );
if ( ! mapFile.exists() )
  throw new IOException( "cannot access map file " + mapName );
BufferedReader reader = new BufferedReader( new FileReader( mapFile ) );

@ @<Read map file into |hashTable|@>=
System.err.println( "Reading " + mapFile );
StreamTokenizer strTok = new StreamTokenizer( reader );
strTok.eolIsSignificant( true );
strTok.quoteChar( '"' );
strTok.commentChar( '%' );
strTok.commentChar( '#' );
strTok.ordinaryChars( '0', '9' );
strTok.wordChars( '0', '9' );
int tok;
int tokCount = 0;
PsFontFile entry = null;
String word = null;
while ( ( tok = strTok.nextToken() ) != strTok.TT_EOF ) {
  tokCount++;
  if ( tok == strTok.TT_EOL ) {
    if ( tokCount > 1 ) {
      hashTable.put( entry.name, entry );
  }
    tokCount = 0;
    continue;
  }
  if ( tokCount == 1 ) {
    entry = new PsFontFile( strTok.toString() );
    entry.name = strTok.sval;
  }
  else if ( tokCount == 2 )
    entry.realName = strTok.sval;
  else if ( tok == '"' )
    entry.psCmd = strTok.sval;
  else if ( tok == '<' ) {
    tok = strTok.nextToken();
    if ( strTok.sval.endsWith( ".enc" ) )
      entry.encoding = strTok.sval;
    else if ( strTok.sval.endsWith( ".pfa" ) 
      || strTok.sval.endsWith( ".pfb" ) )
      entry.source = strTok.sval;
    else
      throw new IOException( "<bad line " + strTok.lineno() 
	+ ": " + strTok.sval );
  } else {
    if ( strTok.sval.endsWith( ".enc" ) )
      entry.encoding = strTok.sval;
    else if ( strTok.sval.endsWith( ".pfa" ) 
      || strTok.sval.endsWith( ".pfb" ) )
      entry.source = strTok.sval;
    else
      try {
	entry.flags = Integer.parseInt( strTok.sval );
      }
      catch( NumberFormatException e ) {
        throw new IOException( "bad line " + strTok.lineno()
	  + ": " + strTok.sval );
      }
  }
}

@* Creating the hash file.

@ @<Class methods@>=
static void makeHash() {
  @<Get properties@>
  fileStore = new Hashtable();
  StringTokenizer strTok = new StringTokenizer( TEXMF, colon );
  while ( strTok.hasMoreTokens() ) {
    String texmf = strTok.nextToken();
    System.err.println();
    System.err.println( "(" + texmf + ")" );
    dirList( texmf );
  }
  System.err.println();
  saveHash();
}

@ @<Class methods@>=
public static void dirList( String dirName ) {
  File dir = new File( dirName );
  if ( !dir.exists() || !dir.isDirectory() ) return;
  String[] list = dir.list();
  for ( int i = 0; i < list.length; i++ ) {
    if ( list[i].startsWith( "." ) ) continue;
    File file = new File( dir, list[i] );
    if ( file.isFile() ) {
      fileStore.put( list[i], dirName );
    } else if ( file.isDirectory() ) {
      System.err.print(".");
      String subDir = dirName + slash + list[i];
      dirList( subDir );
    }
  }
}

@ @<Class methods@>=
public static void saveHash() {
  File saveFile = new File( "tex.hash" );
  try {
    ObjectOutputStream out = new ObjectOutputStream(
      new FileOutputStream( saveFile ) );
      out.writeObject( fileStore );
  }
  catch( IOException e ) {
    System.err.println( "Problem saving hash-table: " + e.getMessage() );
  }
}

@ @(texhash.java@>=
@<Package header@>@;

public class texhash {
  public static void main( String[] args ) {
    TeXFile.makeHash();
  }
}

@* Cweb and the `j' switch.

Knuth's original `|web|' format (\cite{knuth2}) was tied to \textsc{Pascal}.
Later Knuth and Levy developed `|cweb|' (\cite{knuth3}) to provide output in \C.
Since \Java\ is a dialect of \C,
|cweb| only requires minor modifications to output \Java.
These are contained in the change files
|ctang-java.ch|, |cweav-java.ch| and |comm-java.ch|,
all of which can be found at \url{ftp://ftp.maths.tcd.ie/pub/TeX/javaTeX}.
If |ctangle| and |cweave| are compiled with these change files
(as eg by modifying the |cweb| |Makefile|
by changing the line `|TCHANGES=|' to `|TCHANGES=ctang-java.ch|',
and similarly for |WCHANGES| and |CCHANGES|).
then the `|-j|' switch can be used to output \Java, eg
\begin{verbatim}
    % ctangle -j TeXFile.w
\end{verbatim}
produces the file |TeXFile.java| which can then be compiled
in the usual way
\begin{verbatim}
    % javac -O TeXFile.java
\end{verbatim}
and then run by
\begin{verbatim}
    % java TeXFile
\end{verbatim}

On the other hand, this document was produced by
\begin{verbatim}
    % cweave -j TeXFile.w
\end{verbatim}
producing the \LaTeX\ file |TeXFile.tex| which can be processed 
in the usual way
\begin{verbatim}
    % latex TeXFile
    % xdvi TeXFile
    % dvips TeXFile
\end{verbatim}

@* Administrivia.

The file |TeXFile.w| from which this document is derived
can be retrieved from
\url{ftp://ftp.maths.tcd.ie/pub/TeX/javaTeX/javaweb/examples/TeXFile.w}.

For simplicity this work is published subject to
the GNU GPL Licence.
Essentially this allows the work to be freely copied and used,
provided the original file |TeXFile.w| is made available.
Changes should preferably be made through a change file ---
perhaps |TeXFile.ch|.

\end{document}



