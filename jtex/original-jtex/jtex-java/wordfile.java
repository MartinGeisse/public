/*7:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class wordfile extends DataInputStream{
public int filebuf;
public boolean eof= false;
public String name;
wordfile(InputStream in){
super(in);
}
wordfile(TeXFile texFile)throws FileNotFoundException{
super(new DataInputStream(new BufferedInputStream
(new FileInputStream(texFile))));
name= texFile.getName();
}
public void close(){
try{
super.close();
}
catch(IOException e){
System.err.println();
System.err.print("Problem closing "+name);
}
}
}/*:7*/
