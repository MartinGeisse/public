/*4:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/
public class alphaout extends PrintWriter{
public int filebuf;
public boolean eof= false;
public String name;
alphaout(Writer out){
super(out);
}
alphaout(String name)throws FileNotFoundException,IOException{
super(new PrintWriter(new FileWriter(name)));
this.name= name;
}
alphaout(OutputStream out){
super(new PrintWriter(new OutputStreamWriter(out),true));
name= "System.out";
}
}/*:4*/
