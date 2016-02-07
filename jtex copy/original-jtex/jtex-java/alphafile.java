/*3:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class alphafile extends BufferedReader{
public int filebuf;
public boolean eof= false;
public String name;
alphafile(Reader in){
super(in);
}
alphafile(TeXFile texFile)throws FileNotFoundException{
super(new BufferedReader(new FileReader(texFile)));
try{
filebuf= super.read();
}
catch(IOException e){
System.err.println("I/O error reading "+name);
eof= true;
}
name= texFile.getName();
}
alphafile(InputStream in){
super(new BufferedReader(new InputStreamReader(in)));
name= "System.in";
}
public int read(){
try{
int c= filebuf;
filebuf= super.read();
if(c==-1){
eof= true;
c= 0;
}
return c;
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
public boolean eoln(){
return(eof||filebuf=='\n'||filebuf=='\r');
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
}/*:3*/
