/*9:*/
/*2:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:2*/

public class memoryword{
/*10:*/
final static int maxHalfword= 65535;

int Int;

final void setInt(int Int){
this.Int= Int;
}

void copy(memoryword m){
Int= m.Int;
}

final void setlh(int lh){
lh&= 0xffff;
Int&= 0x0000ffff;
Int|= (lh<<16);
}

final void setrh(int rh){
rh&= 0xffff;
Int&= 0xffff0000;
Int|= rh;
}

final void setb0(int b){
b&= 0xff;
Int&= 0x00ffffff;
Int|= (b<<24);
}

final void setb1(int b){
b&= 0xff;
Int&= 0xff00ffff;
Int|= (b<<16);
}

final void setb2(int b){
b&= 0xff;
Int&= 0xffff00ff;
Int|= (b<<8);
}

final void setb3(int b){
b&= 0xff;
Int&= 0xffffff00;
Int|= b;
}

final void setglue(double g){
Int= Float.floatToIntBits((float)g);
}

final void sethh(twohalves hh){
Int= (hh.lh<<16)|(hh.rh&0xffff);
}

final void setqqqq(fourquarters qqqq){
Int= (qqqq.b0<<24)|((qqqq.b1<<16)&0x00ff0000)
|((qqqq.b2<<8)&0x0000ff00)|(qqqq.b3&0x000000ff);
}

final int getInt(){
return Int;
}

final int getlh(){
return(Int>>>16);
}

final int getrh(){
return(Int&0x0000ffff);
}

final int getb0(){
return(Int>>>24);
}

final int getb1(){
return((Int>>>16)&0xff);
}

final int getb2(){
return((Int>>>8)&0xff);
}

final int getb3(){
return(Int&0xff);
}

final double getglue(){
return(double)Float.intBitsToFloat(Int);
}

final twohalves hh(){
twohalves val= new twohalves();
val.lh= Int>>>16;
val.rh= Int&0xffff;
return val;
}

final fourquarters qqqq(){
fourquarters val= new fourquarters();
val.b0= Int>>>24;
val.b1= (Int>>>16)&0xff;
val.b2= (Int>>>8)&0xff;
val.b3= Int&0xff;
return val;
}

final void memdump(wordout fmtfile)throws IOException{
fmtfile.writeInt(Int);
}

final void memundump(wordfile fmtfile)throws IOException{
this.Int= fmtfile.readInt();
}/*:10*/



}/*:9*/
