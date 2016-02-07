/*2:*//*1:*/
package javaTeX;

import java.awt.*;
import java.io.*;
import java.util.*;
import javaTeX.*;/*:1*/

public class TeXFile extends File{

/*3:*/
static String TEXMF= "/usr/local/lib/texmf";
static String TEXHASH= "tex.hash";

static String slash= "/";
static String colon= ":";
static Properties properties;
static File propFile;
static String tempName;

static Hashtable fileStore;/*:3*/
/*4:*/
String fileName;
String fullName;
String realName;/*:4*/
/*5:*/
public TeXFile(String texName){
super(getFullName(texName));
this.fullName= tempName;
}/*:5*/
/*6:*/
private static String getFullName(String texName){
String fileName;
String prefix;
int i;
if((i= texName.indexOf(':'))>=0){
fileName= texName.substring(i+1);
prefix= texName.substring(0,i);
}else{
fileName= texName;
prefix= "";
}
File file= new File(fileName);
if(file.exists())return fileName;
if(prefix.equals("PSFONTS")){
if(PsFontFile.hashTable==null)
PsFontFile.initHash();
PsFontFile entry= (PsFontFile)PsFontFile.hashTable.get(fileName);
if(entry!=null){
fileName+= ".tfm";
}else
return"";
}
initHash();
String dirName= (String)fileStore.get(fileName);
if(dirName!=null)
tempName= dirName+slash+fileName;
else
tempName= "";
return tempName;
}/*:6*//*7:*/
static void initHash(){
int lineNo= 0;
if(fileStore!=null)
return;
/*8:*/
String homeDir= System.getProperty("user.home");
String javaDir= System.getProperty("java.home");
slash= System.getProperty("file.separator");
colon= System.getProperty("path.separator");
String propName= "properties";
propFile= new File(propName);
if(!propFile.exists()){
propName= homeDir+slash+".javaTeX"+slash+"properties";
propFile= new File(propName);
if(!propFile.exists()){
propName= javaDir+slash+"javaTeX.properties";
propFile= new File(propName);
}
}
if(propFile.exists()){
properties= new Properties();
try{
DataInputStream propStream= new DataInputStream(
new FileInputStream(propFile));
if(propStream!=null){
properties.load(propStream);
}
TEXMF= properties.getProperty("TEXMF","/usr/local/lib/texmf");
TEXHASH= properties.getProperty("TEXHASH","tex.hash");
}
catch(IOException e){
System.err.println("Problem reading properties file "+propName
+": "+e.getMessage());
}
}/*:8*/
fileStore= new Hashtable();
int dirNo= 0;
StringTokenizer strTok= new StringTokenizer(TEXMF,colon);
while(strTok.hasMoreTokens()){
String texmf= strTok.nextToken();
File dir= new File(texmf);
if(!dir.exists()){
System.err.println("Cannot find TEXMF="+texmf);
System.err.println("Please edit your \"properties\" file");
System.exit(1);
}
if(dirNo++==0){
String texHash= TEXHASH;
File hashFile= new File(texHash);
if(!hashFile.exists()){
texHash= TEXMF+slash+"tex.hash";
hashFile= new File(texHash);
}
if(hashFile.exists()){
System.out.print(" ("+texHash);
ObjectInputStream hash= null;
try{
hash= new ObjectInputStream(new BufferedInputStream(
new FileInputStream(hashFile)));
fileStore= (Hashtable)(hash.readObject());
hash.close();
}
catch(IOException e){
System.err.println("Problem reading \"tex.hash\": "
+e.getMessage());
}
catch(ClassNotFoundException e){
System.err.println("Problem reading \"tex.hash\": "
+e.getMessage());
}
System.out.print(") ");
return;
}
}
String listing= texmf+slash+"ls-R";
File listFile= new File(listing);
if(!listFile.exists()){
System.err.println(" Must have file \"$TEXMF/ls-R\" for javaTeX");
System.exit(1);
}
BufferedReader list= null;
System.out.print(" ("+listing);
try{
list= new BufferedReader(new FileReader(listing));
String line= "";
String dirName= "";
while((line= list.readLine())!=null){
if(line.equals(""))continue;
if(line.endsWith(colon)){
dirName= texmf+line.substring(1,line.length()-1);
}else{
fileStore.put(line,dirName);
}
}
list.close();
System.out.print(") ");
}
catch(IOException e){
System.err.println("Problem reading ls-R: "+e.getMessage());
System.exit(1);
}
}
}/*:7*//*13:*/
static void makeHash(){
/*8:*/
String homeDir= System.getProperty("user.home");
String javaDir= System.getProperty("java.home");
slash= System.getProperty("file.separator");
colon= System.getProperty("path.separator");
String propName= "properties";
propFile= new File(propName);
if(!propFile.exists()){
propName= homeDir+slash+".javaTeX"+slash+"properties";
propFile= new File(propName);
if(!propFile.exists()){
propName= javaDir+slash+"javaTeX.properties";
propFile= new File(propName);
}
}
if(propFile.exists()){
properties= new Properties();
try{
DataInputStream propStream= new DataInputStream(
new FileInputStream(propFile));
if(propStream!=null){
properties.load(propStream);
}
TEXMF= properties.getProperty("TEXMF","/usr/local/lib/texmf");
TEXHASH= properties.getProperty("TEXHASH","tex.hash");
}
catch(IOException e){
System.err.println("Problem reading properties file "+propName
+": "+e.getMessage());
}
}/*:8*/
fileStore= new Hashtable();
StringTokenizer strTok= new StringTokenizer(TEXMF,colon);
while(strTok.hasMoreTokens()){
String texmf= strTok.nextToken();
System.err.println();
System.err.println("("+texmf+")");
dirList(texmf);
}
System.err.println();
saveHash();
}/*:13*//*14:*/
public static void dirList(String dirName){
File dir= new File(dirName);
if(!dir.exists()||!dir.isDirectory())return;
String[]list= dir.list();
for(int i= 0;i<list.length;i++){
if(list[i].startsWith("."))continue;
File file= new File(dir,list[i]);
if(file.isFile()){
fileStore.put(list[i],dirName);
}else if(file.isDirectory()){
System.err.print(".");
String subDir= dirName+slash+list[i];
dirList(subDir);
}
}
}/*:14*//*15:*/
public static void saveHash(){
File saveFile= new File("tex.hash");
try{
ObjectOutputStream out= new ObjectOutputStream(
new FileOutputStream(saveFile));
out.writeObject(fileStore);
}
catch(IOException e){
System.err.println("Problem saving hash-table: "+e.getMessage());
}
}/*:15*/
}/*:2*//*9:*/
class PsFontFile{
String name;
String realName;
String encoding;
String source;
String psCmd;
int flags;
static String mapName;
static Hashtable hashTable;

PsFontFile(String name){
this.name= name;
}

static void initHash(){
if(mapName==null)
mapName= TeXFile.TEXMF+TeXFile.slash+"dvips"
+TeXFile.slash+"psfonts.map";
if(hashTable!=null)
return;
hashTable= new Hashtable();
if(TeXFile.propFile==null)
TeXFile.initHash();
try{
/*10:*/
File mapFile= new File(mapName);
if(!mapFile.exists())
throw new IOException("cannot access map file "+mapName);
BufferedReader reader= new BufferedReader(new FileReader(mapFile));/*:10*/
/*11:*/
System.err.println("Reading "+mapFile);
StreamTokenizer strTok= new StreamTokenizer(reader);
strTok.eolIsSignificant(true);
strTok.quoteChar('"');
strTok.commentChar('%');
strTok.commentChar('#');
strTok.ordinaryChars('0','9');
strTok.wordChars('0','9');
int tok;
int tokCount= 0;
PsFontFile entry= null;
String word= null;
while((tok= strTok.nextToken())!=strTok.TT_EOF){
tokCount++;
if(tok==strTok.TT_EOL){
if(tokCount>1){
hashTable.put(entry.name,entry);
}
tokCount= 0;
continue;
}
if(tokCount==1){
entry= new PsFontFile(strTok.toString());
entry.name= strTok.sval;
}
else if(tokCount==2)
entry.realName= strTok.sval;
else if(tok=='"')
entry.psCmd= strTok.sval;
else if(tok=='<'){
tok= strTok.nextToken();
if(strTok.sval.endsWith(".enc"))
entry.encoding= strTok.sval;
else if(strTok.sval.endsWith(".pfa")
||strTok.sval.endsWith(".pfb"))
entry.source= strTok.sval;
else
throw new IOException("<bad line "+strTok.lineno()
+": "+strTok.sval);
}else{
if(strTok.sval.endsWith(".enc"))
entry.encoding= strTok.sval;
else if(strTok.sval.endsWith(".pfa")
||strTok.sval.endsWith(".pfb"))
entry.source= strTok.sval;
else
try{
entry.flags= Integer.parseInt(strTok.sval);
}
catch(NumberFormatException e){
throw new IOException("bad line "+strTok.lineno()
+": "+strTok.sval);
}
}
}/*:11*/
}
catch(IOException e){
System.err.println("Problem reading "+mapName+":"
+e.getMessage());
}
}

}/*:9*/
