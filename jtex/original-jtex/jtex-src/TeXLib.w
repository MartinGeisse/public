% $DocId:$

% Nb: The file TeXlib.w must be passed through cweave before LaTeX-ing.
%    cweave +j TeXlib.w
%    latex TeXlib
% To create the TeXlib Java class:
%    ctangle +j TeXlib.w

\documentclass[12pt,a4paper]{cweb}
%\documentclass[12pt,a4paper]{article}
%\input{cwebbase}
% \usepackage[hypertex]{hyperref}
\usepackage{rcs}
\usepackage{url}
\usepackage{amsmath}
\usepackage{amsfonts}
\usepackage{doc}
\MakeShortVerb{\|}

\def\defin{define} % As in webmac.tex
\def\C{\textsc{C}}
\def\Java{\textsc{Java}}
\def\D{\defin{define}} % macro definition

\def\CwebRankNoEject{1}

\title{TeXlib: A \Java\ library for \TeX\ and friends}

\author{Timothy Murphy\\
\texttt{<tim@@maths.tcd.ie>}\\
School of Mathematics, Trinity College Dublin}

\date{\today}

\begin{document}

\maketitle

\tableofcontents

\begin{abstract}
This file contains the auxiliary functions required
by |virtex.java|, |virmf.java| and related programs.
Since we wish to define a number of |public| classes,
the output is necessarily sent to several files
(each carrying the name of the class).
\end{abstract}

@* Introduction.

This document defines various classes making up |TeXlib|.

\TeX\ makes use of various file types,
|alpha_file|, etc.
(Java draws a sharp distinction between
files that are read and those that are written to.
Consequently we have to define two types,
|alpha_file| for input files,
and |alpha_out| for output files;
and similarly for |byte_file|s and |word_file|s.

We have 'hived off' the definiton of these file types
(or rather, classes) into |TeXlib|.

We also define |memory_word| here,
to facilitate the rather complicated `word-partitioning' involved.

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

@* File classes.

@(alphafile.java@>=
@<Package header@>@;
public class alphafile extends BufferedReader {
  public int filebuf;
  public boolean eof = false;
  public String name;
  alphafile(Reader in) {
    super(in);
  }
  alphafile(TeXFile texFile) throws FileNotFoundException {
    super(new BufferedReader(new FileReader(texFile)));
    try {
      filebuf = super.read();
    }
    catch(IOException e) {
      System.err.println("I/O error reading " + name);
      eof = true;
    }
    name = texFile.getName();
  }
  alphafile(InputStream in) {
    super(new BufferedReader(new InputStreamReader(in)));
    name = "System.in";
  }
  public int read() {
    try {
      int c = filebuf;
      filebuf = super.read();
      if (c == -1) {
        eof = true;
        c = 0;
      }
      return c;
    }
    catch(IOException e) {
      eof = true;
    }
    return 0;
  }
  void get() {
    try {
      filebuf = super.read();
    }
    catch(IOException e) {
      eof = true;
    }
  }
  public boolean eoln() {
    return (eof || filebuf == '\n' || filebuf == '\r');
  }
  public void close() {
    try {
      super.close();
    }
    catch(IOException e) {
      System.err.println();
      System.err.print("Problem closing " + name);
    }
  }
}
@#
@ @(alphaout.java@>=
@<Package header@>@;
public class alphaout extends PrintWriter {
  public int filebuf;
  public boolean eof = false;
  public String name;
  alphaout(Writer out) {
    super(out);
  }
  alphaout(String name) throws FileNotFoundException, IOException {
    super(new PrintWriter(new FileWriter(name)));
    this.name = name;
  }
  alphaout(OutputStream out) {
    super(new PrintWriter(new OutputStreamWriter(out), true));
    name = "System.out";
  }
}
@#
@ @(bytefile.java@>=
@<Package header@>@;
public class bytefile extends DataInputStream {
  public int filebuf;
  public boolean eof = false;
  public String name;
  public TeXFile texFile;
  bytefile(InputStream in) {
    super(in);
  }
  bytefile(TeXFile texFile) throws FileNotFoundException {
    super(new DataInputStream(new BufferedInputStream
      (new FileInputStream(texFile))));
    try {
      filebuf = super.read();
    }
    catch(IOException e) {
      eof = true;
    }
    this.texFile = texFile;
    name = texFile.getName();
  }
  public int read() {
    try {
      int c = filebuf;
      filebuf = super.read();
      return c;
    }
    catch(EOFException e) {
      eof = true;
    }
    catch(IOException e) {
      eof = true;
    }
    return 0;
  }
  void get() {
    try {
      filebuf = super.read();
    }
    catch(IOException e) {
      eof = true;
    }
  }
  public void close() {
    try {
      super.close();
    }
    catch(IOException e) {
      System.err.println();
      System.err.print("Problem closing " + name);
    }
  }
}
@#
@ @(byteout.java@>=
@<Package header@>@;
public class byteout extends DataOutputStream {
  public int filebuf;
  public boolean eof = false;
  public String name;
  byteout(OutputStream out) {
    super(out);
  }
  byteout(String name) throws FileNotFoundException, IOException {
    super(new DataOutputStream(new BufferedOutputStream
      (new FileOutputStream(name))));
    this.name = name;
  }
  public void close() {
    try {
      super.close();
    }
    catch(IOException e) {
      System.err.println();
      System.err.print("Problem closing " + name);
    }
  }
}
@#
@ @(wordfile.java@>=
@<Package header@>@;
public class wordfile extends DataInputStream {
  public int filebuf;
  public boolean eof = false;
  public String name;
  wordfile(InputStream in) {
    super(in);
  }
  wordfile(TeXFile texFile) throws FileNotFoundException {
    super(new DataInputStream(new BufferedInputStream
      (new FileInputStream(texFile))));
    name = texFile.getName();
  }
  public void close() {
    try {
      super.close();
    }
    catch(IOException e) {
      System.err.println();
      System.err.print("Problem closing " + name);
    }
  }
}
@#
@ @(wordout.java@>=
@<Package header@>@;
public class wordout extends DataOutputStream {
  public int filebuf;
  public boolean eof = false;
  public String name;
  wordout(OutputStream out) {
    super(out);
  }
  wordout(String name) throws FileNotFoundException, IOException {
    super(new DataOutputStream(new BufferedOutputStream
      (new FileOutputStream(name))));
  }
  public void close() {
    try {
      super.close();
    }
    catch(IOException e) {
      System.err.println();
      System.err.print("Problem closing " + name);
    }
  }
}

@* Memory\_word.

We provide two versions of this class,
one for `small' \TeX\ or \MF,
with a memory\_word occupying 4 bytes,
and one for `large' \TeX\ or \MF,
with a memory\_word occupying 8 bytes,

@(memoryword.java@>=
@<Package header@>@;

public class memoryword {
@<Small memoryword model@>@;
/*
@<Big memoryword model@>@;
*/
}

@ @<Small memoryword model@>=
final static int maxHalfword = 65535;

int Int;

final void setInt(int Int) {
  this.Int = Int;
}

void copy(memoryword m) {
  Int = m.Int;
}

final void setlh(int lh) {
  lh &= 0xffff;
  Int &= 0x0000ffff;
  Int |= (lh << 16);
}

final void setrh(int rh) {
  rh &= 0xffff;
  Int &= 0xffff0000;
  Int |= rh;
}

final void setb0(int b) {
  b &= 0xff;
  Int &= 0x00ffffff;
  Int |= (b << 24);
}

final void setb1(int b) {
  b &= 0xff;
  Int &= 0xff00ffff;
  Int |= (b << 16);
}

final void setb2(int b) {
  b &= 0xff;
  Int &= 0xffff00ff;
  Int |= (b << 8);
}

final void setb3(int b) {
  b &= 0xff;
  Int &= 0xffffff00;
  Int |= b;
}

final void setglue(double g) {
  Int = Float.floatToIntBits((float) g);
}

final void sethh(twohalves hh) {
  Int = (hh.lh << 16) | (hh.rh & 0xffff);
}

final void setqqqq(fourquarters qqqq) {
  Int = (qqqq.b0 << 24) | ((qqqq.b1 << 16) & 0x00ff0000) 
  | ((qqqq.b2 << 8) & 0x0000ff00) | (qqqq.b3 & 0x000000ff);
}

final int getInt() {
  return Int;
}

final int getlh() {
  return (Int >>> 16);
}

final int getrh() {
  return (Int & 0x0000ffff);
}

final int getb0() {
  return (Int >>> 24);
}

final int getb1() {
  return ((Int >>> 16) & 0xff);
}

final int getb2() {
  return ((Int >>> 8) & 0xff);
}

final int getb3() {
  return (Int & 0xff);
}

final double getglue() {
  return (double)Float.intBitsToFloat(Int);
}

final twohalves hh() {
  twohalves val = new twohalves();
  val.lh = Int >>> 16;
  val.rh = Int & 0xffff;
  return val;
}

final fourquarters qqqq() {
  fourquarters val = new fourquarters();
  val.b0 = Int >>> 24;
  val.b1 = (Int >>> 16) & 0xff;
  val.b2 = (Int >>> 8) & 0xff;
  val.b3 = Int & 0xff;
  return val;
}

final void memdump(wordout fmtfile) throws IOException {
  fmtfile.writeInt(Int);
}

final void memundump(wordfile fmtfile) throws IOException {
  this.Int = fmtfile.readInt();
}

@ @<Big memoryword model@>=
final static int maxHalfword = 262143;

long Long;

void copy(memoryword m) {
  Long = m.Long;
}

final void setInt(int Int) {
  Long = (long)Int;
}

final void setlh(int lh) {
  Long &= 0x00000000ffffffffL;
  Long |= ((long)lh << 32);
}

final void setrh(int rh) {
  Long &= 0xffffffff00000000L;
  Long |= rh;
}

final void setb0(int b) {
  b &= 0xffff;
  Long &= 0x0000ffffffffffffL;
  Long |= ((long)b << 48);
}

final void setb1(int b) {
  b &= 0xffff;
  Long &= 0xffff0000ffffffffL;
  Long |= ((long)b << 32);
}

final void setb2(int b) {
  b &= 0xffff;
  Long &= 0xffffffff0000ffffL;
  Long |= ((long)b << 16);
}

final void setb3(int b) {
  b &= 0xffff;
  Long &= 0xffffffffffff0000L;
  Long |= b;
}

final void setglue(double g) {
  Long = (long)Float.floatToIntBits((float)g);
}

final void sethh(twohalves hh) {
  Long = ((long)hh.lh << 32) | (hh.rh & 0xffffffff);
}

final void setqqqq(fourquarters qqqq) {
  Long = ((long)qqqq.b0 << 48) 
  | (((long)qqqq.b1 << 32) & 0x0000ffff00000000L) 
  | (((long)qqqq.b2 << 16) & 0x00000000ffff0000L)
  | ((long)qqqq.b3 & 0x000000000000ffffL);
}

final int getInt() {
  return (int)Long;
}

final int getlh() {
  return (int)(Long >>> 32);
}

final int getrh() {
  return (int)(Long & 0x00000000ffffffffL);
}

final int getb0() {
  return (int)(Long >>> 48);
}

final int getb1() {
  return (int)((Long >>> 32) & 0xffff);
}

final int getb2() {
  return (int)((Long >>> 16) & 0xffff);
}

final int getb3() {
  return (int)(Long & 0xffff);
}

final double getglue() {
  return (double)Float.intBitsToFloat((int)Long);
}

final twohalves hh() {
  twohalves val = new twohalves();
  val.lh = (int)(Long >>> 32);
  val.rh = (int)(Long & 0xffffffff);
  return val;
}

final fourquarters qqqq() {
  fourquarters val = new fourquarters();
  val.b0 = (int)(Long >>> 48);
  val.b1 = (int)((Long >>> 32) & 0xffff);
  val.b2 = (int)((Long >>> 16) & 0xffff);
  val.b3 = (int)(Long & 0xffff);
  return val;
}

final void memdump(wordout fmtfile) throws IOException {
  fmtfile.writeLong(Long);
}

final void memundump(wordfile fmtfile) throws IOException {
  this.Long = fmtfile.readLong();
}

@* The |tex| class.

This is the `user interface' for running |jtex| or |jlatex|.

@(jtex.java@>=
import java.io.*;@/
import java.util.*;@/
import javaTeX.*;@/

public class jtex {
  public static void main(String args[]) {
    StringBuffer strBuf = new StringBuffer("&plain");
    for (int i = 0; i < args.length; i++) {
      strBuf.append(" ");
      strBuf.append(args[i]);
    }
    javaTeX.tex T = new javaTeX.tex(strBuf.toString());
    T.start();
  }
}

@ 
@(jlatex.java@>=
import java.io.*;@/
import java.util.*;@/
import javaTeX.*;@/

public class jlatex {
  public static void main(String args[]) {
    StringBuffer strBuf = new StringBuffer("&latex");
    for (int i = 0; i < args.length; i++) {
      strBuf.append(" ");
      strBuf.append(args[i]);
    }
    javaTeX.tex T = new javaTeX.tex(strBuf.toString());
    T.start();
  }
}

@ And a class for running |initex|.

@(jinitex.java@>=
import java.io.*;@/
import java.util.*;@/
import javaTeX.*;@/

public class jinitex {
  public static void main(String args[]) {
    StringBuffer strBuf = new StringBuffer();
    for (int i = 0; i < args.length; i++) {
      if (i > 0) strBuf.append(" ");
      strBuf.append(args[i]);
    }
    javaTeX.initex T = new javaTeX.initex(strBuf.toString());
    T.start();
  }
}

@* Cweb and the `j' switch.

Knuth's original `|web|' format was tied to \textsc{Pascal}.
Later Knuth and Levy developed `|cweb|' to provide output in \C.
Since \Java\ is a dialect of \C,
|cweb| only requires minor modifications to output \Java.
These are contained in the change files
|ctang-java.ch|, |cweav-java.ch| and |comm-java.ch|,
all of which can be found at \url{ftp://ftp.maths.tcd.ie/pub/TeX/javaTeX}.
If |ctangle| and |cweave| are compiled with these change files
(as eg by modifying the |cweb| |Makefile|
by changing the line `|TCHANGES=|' to `|TCHANGES=ctang-java.ch|',
and similarly for |WCHANGES| and |CCHANGES|).
then the `|+j|' switch can be used to output \Java, eg
\begin{verbatim}
    % ctangle +j TeXlib.w
\end{verbatim}
produces the file |TeXlib.java| which can then be compiled
in the usual way
\begin{verbatim}
    % javac -O TeXlib.java
\end{verbatim}
and then run by
\begin{verbatim}
    % java TeXlib
\end{verbatim}

On the other hand, this document was produced by
\begin{verbatim}
    % cweave +j TeXlib.w
\end{verbatim}
producing the \LaTeX\ file |TeXlib.tex| which can be processed 
in the usual way
\begin{verbatim}
    % latex TeXlib
    % xdvi TeXlib
    % dvips TeXlib
\end{verbatim}

@* Administrivia.

The file |TeXlib.w| from which this document is derived
can be retrieved from
\url{ftp://ftp.maths.tcd.ie/pub/TeX/javaTeX/javaweb/examples/TeXlib.w}.

For simplicity this work is published subject to
the GNU GPL Licence.
Essentially this allows the work to be freely copied and used,
provided the original file |TeXlib.w| is made available.
Changes should preferably be made through a change file ---
perhaps |TeXlib.ch|.

\end{document}



