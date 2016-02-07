%{
/* web2java.l -- lexical analysis for Tangle output.  */

#include "web2java.h"
#include "y.tab.h"

/* For some reason flex wants to do a system call, so we must lose the
   definition of the Pascal read that is in `pascal.h'.  */
#undef read

char conditional[20], negbuf[2], temp[20];
extern boolean doing_statements;
%}
DIGIT		[0-9]
ALPHA		[a-zA-Z]
ALPHANUM	({DIGIT}|{ALPHA})
IDENTIFIER	({ALPHA}{ALPHANUM}*)
NUMBER		({DIGIT}+)
SIGN		("+"|"-")
SIGNED		({SIGN}?{NUMBER})
WHITE		[ \n\t]+
REAL		({NUMBER}"."{NUMBER}("e"{SIGNED})?)|({NUMBER}"e"{SIGNED})
COMMENT		(("{"[^}]*"}")|("(*"([^*]|"*"[^)])*"*)"))
W		({WHITE}|"packed ")+
WW		({WHITE}|{COMMENT}|"packed ")*

%%
{W}				;
"{"		{ while (input() != '}'); }

"#"		{
		    register int c;
		    putc('#', std);
		    while ((c = input()) && c != ';')
			putc(c, std);
		    putc('\n', std);
		}

"procedure "[a-z]+";"[ \n\t]*"forward;"	;

"function "[(),:a-z]+";"[ \n\t]*"forward;"	;

"@define"	return last_tok=define_tok;
"and"		return last_tok=and_tok;
"array"		return last_tok=array_tok;
"arraylength"	return last_tok=arraylength_tok;
"begin"		return last_tok=begin_tok;
"break"		return last_tok=break_tok;
"case"		return last_tok=case_tok;
"cast"		return last_tok=cast_tok;
"catch"		return last_tok=catch_tok;
"class"		return last_tok=class_tok;
"const"		return last_tok=const_tok;
"continue"	return last_tok=continue_tok;
"div"		return last_tok=div_tok;
"do"		return last_tok=do_tok;
"downto"	return last_tok=downto_tok;
"dontmake"	return last_tok=dontmake_tok;
"else"		return last_tok=else_tok;
"end"		return last_tok=end_tok;
"file"		return last_tok=file_tok;
"final"		return last_tok=final_tok;
"finally"	return last_tok=finally_tok;
"for"		return last_tok=for_tok;
"function"	return last_tok=function_tok;
"goto"		return last_tok=goto_tok;
"if"		return last_tok=if_tok;
"label"		return last_tok=label_tok;
"memfield"	return last_tok=mem_tok;
"mod"		return last_tok=mod_tok;
"new"		return last_tok=new_tok;
"not"		return last_tok=not_tok;
"object"	return last_tok=object_tok;
"of"		return last_tok=of_tok;
"or"		return last_tok=or_tok;
"printproc"	return last_tok=printproc_tok;
"private"	return last_tok=private_tok;
"procedure"	return last_tok=procedure_tok;
"program"	return last_tok=program_tok;
"public"	return last_tok=public_tok;
"read"		return last_tok=read_tok;
"record"	return last_tok=record_tok;
"repeat"	return last_tok=repeat_tok;
"rest"		return last_tok=rest_tok;
"shl"		return last_tok=shl_tok;
"shr"		return last_tok=shr_tok;
"static"	return last_tok=static_tok;
"then"		return last_tok=then_tok;
"throw"		return last_tok=throw_tok;
"throws"	return last_tok=throws_tok;
"to"		return last_tok=to_tok;
"try"		return last_tok=try_tok;
"type"		return last_tok=type_tok;
"until"		return last_tok=until_tok;
"var"		return last_tok=var_tok;
"while"		return last_tok=while_tok;
"others"	return last_tok=others_tok;

{REAL}		{		
		  sprintf (temp, "%s%s", negbuf, yytext);
		  negbuf[0] = '\0';
		  return last_tok=r_num_tok;
		}

{NUMBER}	{
		  sprintf (temp, "%s%s", negbuf, yytext);
		  negbuf[0] = '\0';
		  return last_tok=i_num_tok;
		}

("'"(("\\"[^'])|[^']|"''")"'")	return last_tok=single_char_tok;

("'"([^']|"''")*"'")		return last_tok=string_literal_tok;

"+"		{ if ((last_tok>=undef_id_tok &&
		      last_tok<=type_id_tok) ||
		      last_tok==i_num_tok ||
		      last_tok==r_num_tok ||
		      last_tok==')' ||
		      last_tok==']')
		   return last_tok='+';
		else return last_tok=unary_plus_tok; }

"-"		{ if ((last_tok>=undef_id_tok &&
		      last_tok<=type_id_tok) ||
		      last_tok==i_num_tok ||
		      last_tok==r_num_tok ||
		      last_tok==')' ||
		      last_tok==']')
		   return last_tok='-';
		else {
		  int c;
		  while ((c = input()) == ' ' || c == '\t')
                    ;
  		  unput(c);
		  if (c < '0' || c > '9') {
			return last_tok = unary_minus_tok;
		  }
		  negbuf[0] = '-';
		}}

"*"		return last_tok='*';
"/"		return last_tok='/';
"="		return last_tok='=';
"<>"		return last_tok=not_eq_tok;
"<"		return last_tok='<';
">"		return last_tok='>';
"<="		return last_tok=less_eq_tok;
">="		return last_tok=great_eq_tok;
"("		return last_tok='(';
")"		return last_tok=')';
"["		return last_tok='[';
"]"		return last_tok=']';
":="		return last_tok=assign_tok;
".."		return last_tok=two_dots_tok;
"."		return last_tok='.';
","		return last_tok=',';
";"		return last_tok=';';
":"		return last_tok=':';
"^"		return last_tok='^';

{IDENTIFIER}	{ strcpy (last_id, yytext);
		  l_s = search_table (last_id);
		  return
                    last_tok = (l_s == -1 ? undef_id_tok : sym_table[l_s].typ);
		}


.		{ /* Any bizarre token will do.  */
		  return last_tok = two_dots_tok; }
%%
