SHELL = /bin/sh

jT = javaTeX

all: $(jT)/virtex.class $(jT)/initex.class $(jT)/texhash.class

top_srcdir = .
srcdir = .
tangledir = /usr/local/bin

TeXLibsrc = alphafile.java alphaout.java bytefile.java \
	byteout.java wordfile.java wordout.java \
	memoryword.java

TeXLib = $(jT)/alphafile.class $(jT)/alphaout.class $(jT)/bytefile.class \
	$(jT)/byteout.class $(jT)/wordfile.class $(jT)/wordout.class \
	$(jT)/memoryword.class

web2java-src = web2java/web2java.[hly] web2java/main.c web2java/convert web2java/Makefile

cweb-src = cweb/comm-java.ch cweb/ctang-java.ch cweb/cweav-java.ch cweb/jprod.w cweb/Makefile.jtex

jtex-src = tex.jch TeXLib.w TeXFile.w virtoini.sed Makefile \
	web2java/common.defines $(web2java-src) $(cweb-src) \
	properties

jtex-java = virtex.java initex.java memoryword.java \
	alphafile.java alphaout.java bytefile.java byteout.java \
	wordfile.java wordout.java TeXFile.java texhash.java \
	Makefile tex.pool properties

jtex-classes = $(jT)/initex.class $(jT)/virtex.class \
	$(TeXLib) $(jT)/TeXFile.class $(jT)/texhash.class \
	$(jT)/instaterecord.class $(jT)/liststaterecord.class \
	$(jT)/twohalves.class $(jT)/fourquarters.class 

jtex-extras = tex.pool properties Unix/*

#javac = javac
javac = jikes
#javac = guavac
JFLAGS = -d .

tangle = tangle
ctangle = $(tangledir)/ctangle +j
cweave = $(tangledir)/cweave +j
weave = weave

CC = gcc
CCLD = gcc
CFLAGS = -g -O $(XCFLAGS)

.SUFFIXES: .w .web .jch .p .java .class 

# programs = $(jT)/TeXFont.class $(jT)/DVI.class $(jT)/TeXFile.class \
# 	$(jT)/PDF.class $(jT)/DviReader.class $(jT)/DviDevice.class \
# 	$(jT)/DviScanner.class $(jT)/DviPdf.class

tools = tangle weave

# The sh scripts that do the conversions:
web2java = srcdir=$(srcdir) $(SHELL) $(srcdir)/web2java/convert

.w.java:
	$(ctangle) $*
$(jT)/%.class : %.java
	$(javac) $(JFLAGS) $<
%.java : %.p web2java/common.defines
	$(web2java) $*
%.p : %.web %.jch
	$(tangle) $*.web $*.jch
.w.tex:
	$(cweave) $*
.tex.dvi:
	latex $<

$(TeXLib): TeXLib.w
	$(ctangle) TeXLib.w
	$(javac) $(JFLAGS) $(TeXLibsrc)

$(jT)/tangle.class: tangle.java $(jT)/CmdLine.class

$(jT)/virtex.class: virtex.java $(TeXLib) $(jT)/TeXFile.class
	$(javac) $(JFLAGS) virtex.java

virtex.p: tex.web tex.jch
	$(tangle) tex.web tex.jch
	mv tex.p virtex.p

$(jT)/initex.class: initex.java $(TeXLib) $(jT)/TeXFile.class
	$(javac) $(JFLAGS) initex.java

initex.java: initex.p

initex.p: tex.web tex.jch
	sed -f virtoini.sed tex.jch > initex.jch
	$(tangle) tex.web initex.jch
	mv tex.p initex.p

texhash.java: TeXFile.w
	$(ctangle) TeXFile.w

memoryword.java: TeXLib.w
	$(ctangle) TeXLib.w

jtex-java: $(jtex-java)

$(jT)/texhash.class: texhash.java

# $(jT)/tangle.class: tangle.java

# $(jT)/DVI.class: DVI.java TeXFont.java

# $(jT)/TeXFont.class: DVI.java TeXFont.java

clean:
	rm -f *.p *.java virtex.* initex.* core

distclean:	clean
	$(jT)/*.class

jar:	$(jtex-classes)
	jar cf jtex.jar $(jtex-classes) $(jtex-extras)

src-tar:	$(jtex-src)
	tar czf jtex-src.tar.gz $(jtex-src) $(jtex-extras)

java-tar:	$(jtex-java)
	tar czf jtex-java.tar.gz $(jtex-java) $(jtex-extras)

