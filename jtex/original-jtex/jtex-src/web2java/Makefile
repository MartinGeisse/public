srcdir = .
web2cdir = ../..

programs = web2java
all: $(programs)

sources = web2java.l web2java.y web2java.h main.c \
	Makefile common.defines convert

CC = gcc
LEX = flex
LEXLIB = -lfl
LEX_OUTPUT_ROOT = lex.yy
YACC = bison -y
CFLAGS = -I$(web2cdir) -DCHECKING
#CFLAGS = -I$(web2cdir)
LDFLAGS = -L/usr/local/lib -s
LINK = $(CC) -o $@ $(LDFLAGS)

web2java_objects = main.o y.tab.o $(LEX_OUTPUT_ROOT).o
web2java: $(web2java_objects)
	$(LINK) $(web2java_objects) $(LEXLIB) $(LOADLIBES) 

main.o: main.c web2java.h y.tab.h
y.tab.o: y.tab.c web2java.h
y.tab.c y.tab.h: web2java.y
	@echo Expect 3 shift/reduce conflicts.
	$(YACC) -d -v $(srcdir)/web2java.y
$(LEX_OUTPUT_ROOT).o: y.tab.h web2java.h
$(LEX_OUTPUT_ROOT).c: web2java.l 
	$(LEX) $(srcdir)/web2java.l

# misc.make -- cleaning, etc.
mostlyclean::
	rm -f *.o $(programs)

clean:: mostlyclean
	rm -f y.tab.c y.tab.h y.output yacc.* $(LEX_OUTPUT_ROOT).*

distclean:: clean

tar:	$(sources)
	tar czf web2java.tar.gz $(sources)

save:	$(sources)
	cp $(sources) backup

web2java.tar.gz:	$(sources)
	tar czvf web2java.tar.gz $(sources)

uue:	web2java.tar.gz
	uuencode web2java.tar.gz web2java.tar.gz > web2java.uue

