/*8:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class wordout extends DataOutputStream{
public int filebuf;
public boolean eof= false;
public String name;
wordout(OutputStream out){
super(out);
}
wordout(String name)throws FileNotFoundException,IOException{
super(new DataOutputStream(new BufferedOutputStream
(new FileOutputStream(name))));
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
}/*:8*/
