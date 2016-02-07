/*6:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class byteout extends DataOutputStream{
public int filebuf;
public boolean eof= false;
public String name;
byteout(OutputStream out){
super(out);
}
byteout(String name)throws FileNotFoundException,IOException{
super(new DataOutputStream(new BufferedOutputStream
(new FileOutputStream(name))));
this.name= name;
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
}/*:6*/
