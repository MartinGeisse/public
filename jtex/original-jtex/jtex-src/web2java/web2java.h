/* web2c.h: general includes for the `web2c' program itself.  */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BYTE_MIN -128
#define BYTE_MAX 127
#define SHORT_MIN -32000
#define SHORT_MAX 32000
#define STREQ(s1,s2) (!strcmp(s1,s2))

typedef int boolean;
typedef char* String;
typedef char* const_String;

#define true -1
#define false 0

#define ex_32 2
#define ex_real 3
#define max(a,b) ((a>b)?a:b)

extern int indent;
extern int line_pos;
extern int last_brace;
extern int block_level;
extern int ii;
extern int last_tok;

extern char safe_string[80];
extern char var_list[200];
extern char field_list[200];
extern char last_id[80];

extern long last_i_num;
extern int ii, l_s;
extern long lower_bound, upper_bound;
extern FILE *std;
extern int pf_count;

/* A symbol table entry.  */
struct sym_entry {
  char *id;	/* points to the identifier */
  int typ;	/* token type */
  int next;	/* next symbol entry */
  long val;	/* constant : value
		   subrange type : lower bound */
  long upper; 	/* subrange type : upper bound
		   variable, function, type or field : type length */
  int val_sym, upper_sym;	/* Sym table entries of symbols for lower
				   and upper bounds
				 */
  char *offset;	/* points to array_offset */
};

extern char strings[];
extern int hash_list[];
extern short global;
extern struct sym_entry sym_table[];
extern int next_sym_free, next_string_free;
extern int mark_sym_free, mark_string_free;

/* configure tries to figure out lex's convention for yytext.  */
#ifdef YYTEXT_ARRAY
extern char yytext[];
#else
extern char *yytext;
#endif

extern void normal(), new_line(), indent_line(), my_output();
extern void semicolon(), remove_locals(), mark(), initialize();
extern int add_to_table(), search_table(), yyerror(), yylex(), yyparse();

