/*5:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class bytefile extends DataInputStream{
public int filebuf;
public boolean eof= false;
public String name;
public TeXFile texFile;
bytefile(InputStream in){
super(in);
}
bytefile(TeXFile texFile)throws FileNotFoundException{
super(new DataInputStream(new BufferedInputStream
(new FileInputStream(texFile))));
try{
filebuf= super.read();
}
catch(IOException e){
eof= true;
}
this.texFile= texFile;
name= texFile.getName();
}
public int read(){
try{
int c= filebuf;
filebuf= super.read();
return c;
}
catch(EOFException e){
eof= true;
}
catch(IOException e){
eof= true;
}
return 0;
}
void get(){
try{
filebuf= super.read();
}
catch(IOException e){
eof= true;
}
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
}/*:5*/
