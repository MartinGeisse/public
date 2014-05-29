/* web2java.y -- parse some of Pascal, and output C, sort of.

   This grammar has one shift/reduce conflict, from the
   if-then[-else] rules, which is unresolvable.  */

/* The order of some of the tokens here is significant.  See the rules
   for - and + in web2java.l.  */
%token	array_tok begin_tok case_tok const_tok do_tok downto_tok else_tok
	end_tok file_tok for_tok function_tok goto_tok if_tok label_tok
	of_tok procedure_tok program_tok record_tok repeat_tok then_tok
	to_tok type_tok until_tok var_tok while_tok 
	others_tok r_num_tok i_num_tok string_literal_tok single_char_tok
	assign_tok two_dots_tok undef_id_tok var_id_tok
	proc_id_tok proc_param_tok fun_id_tok fun_param_tok const_id_tok
	class_tok object_tok class_id_tok object_id_tok dontmake_tok
	program_id_tok obj_fun_id_tok obj_fun_param_tok
	type_id_tok define_tok
	throw_tok catch_tok try_tok finally_tok throws_tok
	break_tok continue_tok rest_tok
	cast_tok new_tok public_tok private_tok static_tok final_tok
	printproc_tok print_id_tok read_tok
	shl_tok shr_tok
	mem_tok mem_id_tok
	arraylength_tok

%nonassoc '=' not_eq_tok '<' '>' less_eq_tok great_eq_tok
%left '.' '+' '-' or_tok
%right unary_plus_tok unary_minus_tok 
%left '*' '/' div_tok mod_tok and_tok shl_tok shr_tok
%right not_tok

%{
#include "web2java.h"

#define YYDEBUG 1

#define	symbol(x)	sym_table[x].id
#define	MAX_ARGS	50

static char fn_return_type[50], for_stack[300], control_var[50];
static char arg_type[MAX_ARGS][30];
static int ids_typed;
char routine[100];	/* Name of routine being parsed, if any */
static char array_bounds[80], array_offset[80];
static int lower_sym, upper_sym;
static FILE *orig_std;
boolean doing_statements = false;
static int param_id_list[MAX_ARGS], ids_paramed=0;

int array_dim;
static boolean global_var = false;
static boolean local_var = false;
boolean broken[16];
boolean halfbroken;
boolean mem_assign = false;

extern char conditional[], temp[], negbuf[];
static char Program_name[32];
static char Program_param[8][32];
static int Program_param_count;
extern boolean debug;

static level = 0;
static new_argc;

extern int stat_no;
boolean no_exit = false;
boolean make_object = false;
boolean make_new = true;
boolean classified = false;
int class_id;
static int id_list[16];
static int id_ctr;
boolean typing = false;
#define MAX_BRA_LEVEL 8
static int loop_level = 0;
int bra_level = 0;
boolean need_offset[MAX_BRA_LEVEL];

#define MAX_LAB 127
#define LAB_SIZE MAX_LAB+1
int lab[LAB_SIZE];
int lab_no;
int lab_list[32];
int lab_count = 0;
boolean cont[LAB_SIZE];
extern int last_brace;
#define warning(str) fprintf(stderr, str)

static void check_loop_start(boolean);
static void check_loop_end(boolean);
static void check_lab_stat(int);
static void check_goto(int);
static void check_proc_start();
static void check_proc_end();

static void compute_array_bounds (void);
static void fixup_var_list (void);
static void gen_function_head (void);
static void gen_copy_routine(void);
static void set_offset();
%}

%start PROGRAM

%%
PROGRAM:
	DEFS
		{ 
			printf ("package javaTeX;\n\n");
			printf ("import java.awt.* ;\n");
			printf ("import java.io.* ;\n");
			printf ("import java.util.* ;\n");
			printf ("import javaTeX.* ;\n");
			block_level++;
			typing = true;
		}
	TYPE_DEC_PART
		{
			typing = false;
		}
	PROGRAM_HEAD LABEL_DEC_PART CONST_DEC_PART 
		{
			global_var = true;
		}
	VAR_DEC_PART
		{
			global_var = false;
		}
	P_F_DEC_PART BODY
		{
			YYACCEPT;
		}
	;

DEFS:
	/* empty */
	| DEFS DEF
	;

DEF:
	define_tok function_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = fun_id_tok;
		}
	| define_tok const_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = const_id_tok;
		}
	| define_tok function_tok undef_id_tok '(' ')' ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = fun_param_tok;
		}
	| define_tok procedure_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = proc_id_tok;
		}
	| define_tok procedure_tok undef_id_tok '(' ')' ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = proc_param_tok;
		}
	| define_tok object_tok function_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = obj_fun_id_tok;
		}
	| define_tok object_tok function_tok undef_id_tok '(' ')' ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = obj_fun_param_tok;
		}
	| define_tok class_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = class_id_tok;
		}
	| define_tok object_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = object_id_tok;
		}
	| define_tok type_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = type_id_tok;
		}
	| define_tok type_tok undef_id_tok '=' SUBRANGE_TYPE ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = type_id_tok;
			sym_table[ii].val = lower_bound;
			sym_table[ii].val_sym = lower_sym;
			sym_table[ii].upper = upper_bound;
			sym_table[ii].upper_sym = upper_sym;
		}
	| define_tok var_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = var_id_tok;
		}
	| define_tok continue_tok i_num_tok ';'
		{
			sscanf (temp, "%d", &lab_no);
			if (lab_no <= MAX_LAB)
				cont[lab_no] = true;
			else
				yyerror("label no too large");
		}
	| define_tok printproc_tok undef_id_tok '(' ')' ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = print_id_tok;
		}
	| define_tok mem_tok undef_id_tok ';'
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = mem_id_tok;
		}
	;

PROGRAM_HEAD:
	program_tok undef_id_tok 
		{
			ii = add_to_table (last_id); 
			sym_table[ii].typ = program_id_tok;
			strcpy (Program_name, last_id);
			sprintf (safe_string, 
				"\npublic class %s extends Thread {\n",
				Program_name);
			my_output (safe_string);
		}
	PARAM ';'
		{
			int i;
			for (i=0; i<ids_paramed; i++)
				sprintf (Program_param[i], "%s %s", 
					arg_type[i], symbol (param_id_list[i]));
			Program_param_count = ids_paramed;
		}
	;

BLOCK:
		{
			if (block_level > 0) my_output("{\n ");
			indent++;
			block_level++;
			local_var = true;
		}
	LABEL_DEC_PART CONST_DEC_PART TYPE_DEC_PART
		{
			if (block_level == 2) {
				if (strcmp(fn_return_type, "void")) {
					my_output(fn_return_type);
					my_output("Result;");
					new_line();
				}
			}
		}
	VAR_DEC_PART
		{
			doing_statements = true;
		}
	STAT_PART
		{
			if (block_level == 2) {
				if (no_exit)
					no_exit = false;
				else if (strcmp(fn_return_type,"void")) {
					my_output("return Result");
					broken[level] = true;
					semicolon();
				}
				routine[0] = '\0';
			}
			indent--;
			block_level--;
			my_output("}");
			new_line();
			doing_statements = false;
			local_var = false;
		}
	;

LABEL_DEC_PART:
	/* empty */
	| label_tok 
		{
			my_output("/*");
		}
	LABEL_LIST ';'
		{
			my_output("*/");
		}
	;

LABEL_LIST:
	LABEL
	| LABEL_LIST ',' LABEL
	;

LABEL:
	i_num_tok 
		{
			sscanf (temp, "%ld", &lab_no);
			lab_list[lab_count++] = lab_no;
			my_output (temp);
		}
	;

CONST_DEC_PART:		
	/* empty */
	| const_tok CONST_DEC_LIST
		{
			new_line();
		}
	;

CONST_DEC_LIST:
	CONST_DEC
	| CONST_DEC_LIST CONST_DEC
	;

CONST_DEC:
		{ 
			unsigned save = indent;
			new_line ();
			indent = 0;
			if (block_level <= 1)
			my_output ("static final int");
			else
			my_output ("int");
			indent = save;
		}
	undef_id_tok
		{
			ii = add_to_table (last_id);
			sym_table[ii].typ = const_id_tok;
			my_output (last_id);
		}
	'='
		{
			my_output ("=");
		}
	CONSTANT_EXPRESS
		{
			my_output (";");
		}
	';'
		{
			sym_table[ii].val = last_i_num;
			new_line();
		}
	;

CONSTANT:
	i_num_tok
		{
			sscanf (temp, "%ld", &last_i_num);
			my_output (temp);
			$$ = ex_32;
		}
	| r_num_tok
		{
			my_output(temp);
			$$ = ex_real;
		}
	| STRING
		{
			$$ = 0;
		}
	| CLASSIFIER CONSTANT_ID
		{
			$$ = ex_32;
		}
	;

CONSTANT_EXPRESS:
	UNARY_OP CONSTANT_EXPRESS %prec '*'
		{
			$$ = $2;
		}
	| CONSTANT_EXPRESS '+'
		{
			my_output ("+");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '-'
		{
			my_output ("-");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '*'
		{
			my_output ("*");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS div_tok
		{
			my_output ("/");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '='
		{
			my_output ("==");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS not_eq_tok
		{
			my_output ("!=");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS mod_tok
		{
			my_output ("%");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS shl_tok
		{
			my_output ("<<");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS shr_tok
		{
			my_output (">>");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '<'
		{
			my_output ("<");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '>'
		{
			my_output (">");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS less_eq_tok
		{
			my_output ("<=");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS great_eq_tok
		{
			my_output (">=");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS and_tok
		{
			my_output ("&&");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS or_tok
		{
			my_output ("||");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
		}
	| CONSTANT_EXPRESS '/'
		{
			my_output ("/ ((double)");
		}
	CONSTANT_EXPRESS
		{
			$$ = max ($1, $4);
			my_output (")");
		}
	| CONST_FACTOR
		{
			$$ = $1;
		}
	;

CONST_FACTOR:
	'('
		{
			my_output ("(");
		}
	CONSTANT_EXPRESS ')'
		{
			my_output (")");
			$$ = $1;
		}
	| CONSTANT
	;

STRING:
	string_literal_tok
		{

			int i, j; char s[132];
			j = 1;
			s[0] = '"';
			for (i=1; yytext[i-1] != 0; i++) {
				if (yytext[i] == '\\' || yytext[i] == '"')
					s[j++] = '\\';
				else if (yytext[i] == '\'')
					i++;
				s[j++] = yytext[i];
			}
			s[j-1] = '"';
			s[j] = 0;
			my_output (s);
		}
	| single_char_tok
		{

			char s[5];
			s[0]='\'';
			if (strlen(yytext) == 4 && yytext[1] == '\\') {
				s[1] = yytext[1];
				s[2] = yytext[2];
				s[3] = '\'';
				s[4] = 0;
			} else if (yytext[1] == '\\' || yytext[1] == '\'') {
				s[1] = '\\';
				s[2] = yytext[1];
				s[3] = '\'';
				s[4] = 0;
			} else {
				s[1] = yytext[1];
				s[2] = '\'';
				s[3] = 0;
			}
			my_output (s);
		}
	;

CONSTANT_ID:
	const_id_tok
		{
			my_output (last_id);
		}
	;

TYPE_DEC_PART:
	/* empty */
	| type_tok TYPE_DEF_LIST
	;

TYPE_DEF_LIST:
	TYPE_DEF
	| TYPE_DEF_LIST TYPE_DEF
	;

TYPE_DEF: 
		{
			my_output ("class"); id_ctr = 0;
		}
	undef_id_tok 
		{

			ii = add_to_table(last_id);
			class_id = ii;
			sym_table[ii].typ = class_id_tok;
			my_output (last_id);
		}
	'=' 
		{
			my_output ("{");
			array_bounds[0] = 0;
			array_offset[0] = 0;
		}
	RECORD_TYPE ';'
		{
			if (*array_offset) {
				yyerror ("Cannot typedef arrays with offsets");
			}
			gen_copy_routine();
			my_output ("}");
			new_line();
		}
	;

CLASSIFIER:
	/* empty */
	| CLASSIFIER class_id_tok '.'
		{
			my_output(last_id);
			my_output(".");
		}
	| CLASSIFIER SIMPLE_OBJECT '.'
		{
			my_output(".");
		}
	;

SIMPLE_OBJECT:
	object_id_tok
		{
			my_output (last_id);
		}
	VAR_DESIG_LIST
	| object_id_tok
		{
			my_output (last_id);
		}
	;

OBJECT:
	CLASSIFIER SIMPLE_OBJECT

TYPE:
	SIMPLE_TYPE
		{
			my_output (safe_string);
		}
	| STRUCTURED_TYPE
	;

SIMPLE_TYPE:
	SUBRANGE_TYPE
		{
			/* If the bounds on an integral type 
			are known at translation time, 
			we would like to select 
			the smallest Java type which can represent it.  
			At present we just use int's in all cases. */

			if (lower_sym == -1 && upper_sym == -1) {
				if (BYTE_MIN <= lower_bound && 
					upper_bound <= BYTE_MAX) {
				/*
					strcpy (safe_string, "byte");
				*/
					strcpy (safe_string, "int");
				} else if (SHORT_MIN <= lower_bound && 
					upper_bound <= SHORT_MAX) {
				/* 
					strcpy (safe_string, "short");
				*/
					strcpy (safe_string, "int");
				} else {
					strcpy (safe_string, "int");
				}
			} else {
				strcpy (safe_string, "int");
			}
		}
	| TYPE_ID
	| CLASS_ID
	;

SUBRANGE_TYPE:
	SUBRANGE_CONSTANT two_dots_tok SUBRANGE_CONSTANT
	;

POSSIBLE_PLUS:
	/* empty */
	| unary_plus_tok
	;

SUBRANGE_CONSTANT:
	POSSIBLE_PLUS i_num_tok
		{
			lower_bound = upper_bound;
			lower_sym = upper_sym;
			sscanf (temp, "%ld", &upper_bound);
			upper_sym = -1; /* no sym table entry */
		}
	| const_id_tok
		{
			lower_bound = upper_bound;
			lower_sym = upper_sym;
			upper_bound = sym_table[l_s].val;
			upper_sym = l_s;
		}
	| var_id_tok
		{
			lower_bound = upper_bound;
			lower_sym = upper_sym;
			upper_bound = 0;
			upper_sym = l_s;
		}
	| class_id_tok '.' 
		{
			classified = true;
			class_id = ii;
		}
	var_id_tok
		{
			lower_bound = upper_bound;
			lower_sym = upper_sym;
			upper_bound = 0;
			upper_sym = l_s;
		}
	| undef_id_tok
		{
			lower_bound = upper_bound;
			lower_sym = upper_sym;
			upper_bound = 0;
			upper_sym = l_s;
		}
	;

CLASS_ID:
	class_id_tok
		{
			strcpy(safe_string, last_id);
			make_object = true;
		}
	| program_id_tok
		{
			strcpy(safe_string, last_id);
			make_object = true;
		}
	;

TYPE_ID:
	type_id_tok
		{
			strcpy(safe_string, last_id);
		}
	;

STRUCTURED_TYPE:
	ARRAY_TYPE
	| RECORD_TYPE
	| FILE_TYPE
	| POINTER_TYPE
	;

POINTER_TYPE:
	'^' type_id_tok
		{
			my_output (last_id);
			my_output ("[]");
		}
	;

ARRAY_TYPE:
	array_tok '[' INDEX_TYPE ']' of_tok COMPONENT_TYPE
		{
			array_dim = 1;
		}
	| array_tok '[' INDEX_TYPE ',' INDEX_TYPE ']' of_tok COMPONENT_TYPE
		{
			array_dim = 2;
		}
	;

INDEX_TYPE:
	SUBRANGE_TYPE
		{
			compute_array_bounds();
		}
	| type_id_tok
		{
			lower_bound = sym_table[l_s].val;
			lower_sym = sym_table[l_s].val_sym;
			upper_bound = sym_table[l_s].upper;
			upper_sym = sym_table[l_s].upper_sym;
			compute_array_bounds();
		}
	;

COMPONENT_TYPE:
	TYPE
	;

RECORD_TYPE:
	record_tok
		{
			new_line();
			indent++;
		}
	VAR_DEC_LIST end_tok
		{
			indent--;
		}
	;

FILE_TYPE:
	file_tok of_tok 
		{
			my_output ("text /* of ");
		}
	TYPE
		{
			my_output ("*/");
		}
	;

VAR_DEC_PART:
	/* empty */
	| var_tok VAR_DEC_LIST
	;

VAR_DEC_LIST:
	VAR_DEC
	| VAR_DEC_LIST VAR_DEC
	;

VAR_DEC: 
		{
			var_list[0] = 0;
			array_bounds[0] = 0;
			array_offset[0] = 0;
			ids_paramed = 0;
		}
	VAR_ID_DEC_LIST ':'
		{
			array_bounds[0] = 0;	
			array_offset[0] = 0;
		}
	MODIFIER TYPE ';'
		{
			fixup_var_list();
		}
	;

VAR_ID_DEC_LIST:
	VAR_ID
	| VAR_ID_DEC_LIST ',' VAR_ID
	;

VAR_ID:
	undef_id_tok
		{
			int i=0, j=0;
			ii = add_to_table(last_id);
			if (typing) id_list[id_ctr++] = ii;
			sym_table[ii].typ = var_id_tok;
			param_id_list[ids_paramed++] = ii;
			while (var_list[i] == '!')
			while(var_list[i++]);
			var_list[i++] = '!';
			while (last_id[j])
			var_list[i++] = last_id[j++];
			var_list[i++] = 0;
			var_list[i++] = 0;
		}
	| var_id_tok
		{
			int i=0, j=0;
			ii = add_to_table(last_id);
			if (typing) id_list[id_ctr++] = ii;
			sym_table[ii].typ = var_id_tok;
			param_id_list[ids_paramed++] = ii;
			while (var_list[i] == '!')
			while (var_list[i++]);
			var_list[i++] = '!';
			while (last_id[j])
			var_list[i++] = last_id[j++];
			var_list[i++] = 0;
			var_list[i++] = 0;
		}
	| object_id_tok
		{
			int i=0, j=0;
			ii = add_to_table(last_id);
			if (typing) id_list[id_ctr++] = ii;
			sym_table[ii].typ = object_id_tok;
			param_id_list[ids_paramed++] = ii;
			while (var_list[i] == '!')
			while (var_list[i++]);
			var_list[i++] = '!';
			while (last_id[j])
			var_list[i++] = last_id[j++];
			var_list[i++] = 0;
			var_list[i++] = 0;
		}
	;

BODY:
	/* empty */
	| begin_tok 
		{
			int i;
			my_output ("public void run (");
			for ( i = 0; i < Program_param_count; i++ ) {
				strcpy (safe_string, Program_param[i]);
				if (i < Program_param_count - 1)
					strcat (safe_string, ",");
				my_output(safe_string);
			}
			my_output(") {"); 
			indent++;
			new_line ();
		}
	STAT_LIST end_tok '.'
		{
			indent--;
			my_output ("}");
			new_line ();
			my_output ("}");
			new_line ();
		}
	;

P_F_DEC_PART:
	P_F_DEC 
	| P_F_DEC_PART P_F_DEC
	;

P_F_DEC:
	PROCEDURE_DEC ';'
		{
			new_line();
			remove_locals();
		}
	| FUNCTION_DEC ';'
		{
			new_line();
			remove_locals();
		}
	;

PROCEDURE_DEC:
	MODIFIER PROCEDURE_HEAD BLOCK 
		{
			check_proc_end();
		}
	;

PROCEDURE_HEAD:
	procedure_tok undef_id_tok
		{
			my_output ("void");
			ii = add_to_table(last_id);
			if (debug)
				fprintf(stderr, "%3d Procedure %s\n", 
					pf_count++, last_id);
			sym_table[ii].typ = proc_id_tok;
			strcpy(routine, last_id);
			new_line ();
			orig_std = std;
			std = 0;
			check_proc_start();
		}
	PARAM
		{
			strcpy(fn_return_type, "void");
			gen_function_head();
		}
	THROWS_CLAUSE ';'
	| procedure_tok DECLARED_PROC
		{
			ii = l_s; 
			if (debug)
				fprintf(stderr, "%3d Procedure %s\n",
					pf_count++, last_id);
			strcpy(routine, last_id);
			my_output ("public void");
			new_line ();
		}
	PARAM
		{
			strcpy(fn_return_type, "void");
			gen_function_head();
		}
	THROWS_CLAUSE ';'
	| procedure_tok program_id_tok
		{
			ii = l_s; 
			if (debug)
				fprintf(stderr, "%3d Constructor %s\n",
					pf_count++, last_id);
			strcpy(routine, Program_name);
			new_line ();
			check_proc_start();
		}
	PARAM
		{
			strcpy(fn_return_type, "void");
			gen_function_head();
		}
	THROWS_CLAUSE ';'
	;

THROWS_CLAUSE: /* empty */
	| throws_tok
		{
			my_output ("throws");
		}
	EXCEPTION_LIST

EXCEPTION_LIST:
	EXCEPTION
	| EXCEPTION_LIST ',' 
		{
			my_output (",");
		}
	EXCEPTION

EXCEPTION:
			type_id_tok { my_output(last_id);
		}
	| class_id_tok
		{
			my_output(last_id);
		}

PARAM:
	/* empty */
		{
			mark ();
			ids_paramed = 0;
		}
	| '('
		{
			ids_paramed = 0;
			if (sym_table[ii].typ == proc_id_tok)
			sym_table[ii].typ = proc_param_tok;
			else if (sym_table[ii].typ == fun_id_tok)
			sym_table[ii].typ = fun_param_tok;
			mark();
		}
	FORM_PAR_SEC_L ')'
	;

FORM_PAR_SEC_L:
	FORM_PAR_SEC
	| FORM_PAR_SEC_L ';' FORM_PAR_SEC
	;

FORM_PAR_SEC1:
		{
			ids_typed = ids_paramed;
		}
	VAR_ID_DEC_LIST ':' JAVA_TYPE
		{
			int i;
			for (i=ids_typed; i<ids_paramed; i++) {
				strcpy(arg_type[i], safe_string);
			}
			if (make_object) {
				for (i=0; i<ids_paramed; i++)
					sym_table[param_id_list[i]].typ = 
						object_id_tok;
				make_object = false;
			}
		}
	;

FORM_PAR_SEC:
	FORM_PAR_SEC1
	| var_tok FORM_PAR_SEC1
	;

JAVA_TYPE:
	SIMPLE_TYPE
	| '^' SIMPLE_TYPE
		{
			strcat (safe_string, "[]");
		}

DECLARED_PROC:
	proc_id_tok
	| proc_param_tok
	;

FUNCTION_DEC:
	MODIFIER FUNCTION_HEAD BLOCK
		{
			check_proc_end();
		}
	;

MODIFIER:
	/* empty */
		{
			if (global_var)
				my_output("private"); 
			else if (!local_var)
				my_output("public");
		}
	| MOD_LIST

MOD_LIST: MOD
	| MOD_LIST MOD

MOD:	public_tok
		{
			my_output("public");
		}
	| static_tok
		{
			my_output("static");
		}
	| final_tok
		{
			my_output("final");
		}
	| dontmake_tok
		{
			make_new = false;
		}

FUNCTION_HEAD:
	function_tok undef_id_tok 
		{
			orig_std = std;
			std = 0;
			ii = add_to_table(last_id);
			if (debug)
				fprintf(stderr, "%3d Function %s\n", 
					pf_count++, last_id);
			sym_table[ii].typ = fun_id_tok;
			strcpy (routine, last_id);
			check_proc_start();
		}
	PARAM ':'
		{
			normal();
			array_bounds[0] = 0;
			array_offset[0] = 0;
		}
	SIMPLE_TYPE
		{
			strcpy (fn_return_type, safe_string);
			my_output (fn_return_type);
			gen_function_head();
		}
	THROWS_CLAUSE ';'
	| function_tok DECLARED_FUN 
		{
			my_output ("public");
			orig_std = std;
			std = 0;
			ii = l_s;
			if (debug)
				fprintf(stderr, "%3d Function %s\n", 
					pf_count++, last_id);
			strcpy(routine, last_id);
		}
	PARAM ':'
		{
			normal();
			array_bounds[0] = 0;
			array_offset[0] = 0;
		}
	SIMPLE_TYPE
		{
			strcpy (fn_return_type, safe_string);
			my_output (fn_return_type);
			gen_function_head();
		}
	THROWS_CLAUSE ';'
	;

DECLARED_FUN:
	fun_id_tok
	| fun_param_tok
	;

STAT_PART:
	begin_tok STAT_LIST end_tok
	;

COMPOUND_STAT:
	begin_tok 
		{
			my_output ("{");
			indent++;
			new_line();
		}
	STAT_LIST end_tok
		{
			indent--;
			my_output ("}");
			new_line();
		}
	;

STAT_LIST:
	STATEMENT
	| STAT_LIST ';' STATEMENT
	;

STATEMENT:
	UNLAB_STAT
	| S_LABEL ':'
	STATEMENT
	;

S_LABEL:
	i_num_tok
		{
			sscanf(temp, "%ld", &lab_no);
			check_lab_stat(lab_no);
		}
	;

UNLAB_STAT:
	SIMPLE_STAT
		{
			semicolon();
		}
	| STRUCT_STAT
		{
			semicolon();
		}
	;

SIMPLE_STAT:
	ASSIGN_STAT
	| CONSTRUCT_STAT
	| OBJECT_COPY_STAT
	| PROC_STAT
	| GO_TO_STAT
	| BREAK_STAT
	| CONTINUE_STAT
	| THROW_STAT
	| TRY_STAT
	| CATCH_STAT
	| FINALLY_STAT
	| PRINT_STAT
	| READ_STAT
	| EMPTY_STAT
	| REST_STAT
	;

REST_STAT:
	rest_tok
	;

CONSTRUCT_STAT:
	OBJECT assign_tok new_tok
		{
			my_output("=");
			my_output("new");
		}
	CLASS_ID
		{
			my_output(safe_string);
			make_object = false;
		}
	NEW_PARAM_LIST
	;

OBJECT_COPY_STAT:
	OBJECT assign_tok
		{
			my_output("=");
		}
	CONSTANT_ID
	| OBJECT assign_tok dontmake_tok
		{
			my_output("=");
		}
	ANY_OBJECT
	| OBJECT assign_tok 
		{
			my_output(". copy (");
		}
	ANY_OBJECT
		{
			my_output(")");
		}
	| MEM_OBJECT assign_tok 
		{
			sprintf(safe_string, "set%s (", last_id);
			my_output(safe_string);
		}
	ANY_OBJECT
		{
			my_output(")");
		}
	;

ANY_OBJECT:
	OBJECT
	| MEM_OBJECT
		{
			my_output(last_id);
			my_output("()");
		}
	| CLASSIFIER obj_fun_id_tok
		{
			my_output(last_id);
			my_output("()");
		}
	| CLASSIFIER obj_fun_param_tok
		{
			my_output(last_id);
		}
	PARAM_LIST
	| cast_tok '(' 
		{
			my_output ("(");
		}
	CLASS_ID ','
		{
			my_output (safe_string);
			my_output (")");
		}
	ANY_OBJECT ')'
	;

BREAK_STAT:
	break_tok 
		{
			my_output ("break");
			broken[level] = true;
		}
	| break_tok i_num_tok
		{
			sscanf(temp, "%ld", &lab_no);
			sprintf(safe_string, "break lab%d", lab_no);
			my_output (safe_string);
			broken[level] = true;
		}
	;

CONTINUE_STAT:
	continue_tok 
		{
			my_output ("continue");
			broken[level] = true;
		}
	| continue_tok i_num_tok
		{
			sscanf(temp, "%ld", &lab_no);
			sprintf(safe_string, "continue lab%d", lab_no);
			my_output (safe_string);
			broken[level] = true;
		}
	;

THROW_STAT:
	throw_tok
		{
			my_output ("throw");
		}
	new_tok
		{
			my_output ("new");
		}
	class_id_tok '(' 
		{
			my_output (last_id);
			my_output ("(");
		}
	STRING ')'
		{
			my_output (")");
			broken[level] = true;
		}
	;

CATCH_STAT:
	catch_tok '('
		{
			my_output ("catch");
			my_output ("(");
		}
	object_id_tok ':'
		{
			strcpy(safe_string, last_id);
		}
	class_id_tok ')'
		{
			my_output (last_id);
			my_output (safe_string);
			my_output (")");
		}
	STATEMENT
	;

TRY_STAT:
	try_tok
		{
			my_output ("try");
		}
	STATEMENT
	;

FINALLY_STAT:
	finally_tok
		{
			my_output ("finally");
		}
	STATEMENT
	;

PRINT_STAT:
	print_id_tok 
		{
			strcpy(safe_string, last_id);
		}
	'(' OBJECT
		{
			my_output ("."); 
			my_output (safe_string);
			my_output ("(");
		}
	PRINT_PARAMS ')'
		{
			my_output (")");
		}
	;

PRINT_PARAMS:
	/* empty */
	| ',' PRINT_PARAM_L

PRINT_PARAM_L:
	ACTUAL_PARAM
	| PRINT_PARAM_L ','
		{
			my_output ("+");
		}
	ACTUAL_PARAM
	;

READ_STAT:
	read_tok '(' object_id_tok
		{
			strcpy (safe_string, last_id);
		}
	',' VARIABLE ')'
		{
			my_output ("=");
			my_output (safe_string);
			my_output (".");
			my_output ("read ()");
		}
	;

ASSIGN_STAT:		
	CLASSIFIER VARIABLE assign_tok 
		{
			if (!mem_assign) my_output ("=");
		}
	EXPRESS
		{
			if (mem_assign) my_output(")");
			mem_assign = false;
		}
	| FUNC_ID_AS assign_tok
		{
			my_output ("Result =");
		}
	EXPRESS
	| MEM_VAR assign_tok
		{
			sprintf(safe_string, "set%s (", last_id);
			my_output(safe_string);
		}
	EXPRESS
		{
			my_output(")");
		}
	;

VARIABLE:
	CLASSIFIER SIMPLE_VARIABLE
	;

SIMPLE_VARIABLE:
	var_id_tok
		{
			my_output (last_id);
			$$ = ex_32;
		}
	VAR_DESIG_LIST
	| var_id_tok
		{
			my_output (last_id); $$ = ex_32;
		}
	| var_id_tok '.' arraylength_tok
		{
			my_output (last_id);
			my_output (".");
			my_output ("length");
			$$ = ex_32;
		}
	;

MEM_VAR:
	CLASSIFIER mem_id_tok '.' var_id_tok
	;

MEM_OBJECT:
	CLASSIFIER mem_id_tok
	;

FUNC_ID_AS:
	fun_id_tok
		{
			$$ = ex_32;
		}
	| fun_param_tok
		{
			$$ = ex_32;
		}
	;

VAR_DESIG_LIST:
	VAR_DESIG
	| VAR_DESIG_LIST VAR_DESIG
	;

VAR_DESIG:
	'['
		{
			bra_level++;
			my_output ("[");
			if (sym_table[l_s].offset) {
				strcpy(array_offset, sym_table[l_s].offset);
				need_offset[bra_level] = true;
			}
		}
	EXPRESS VAR_DESIG1
		{
			if (need_offset[bra_level]) {
				my_output(array_offset);
				need_offset[bra_level] = false;
			}
			my_output ("]");
			bra_level--;
		}
	;

	VAR_DESIG1:
	']'
	| ','
		{
			my_output ("][");
		}
	EXPRESS	']'
	;

EXPRESS:
	UNARY_OP
	EXPRESS
	%prec '*'
		{
			$$ = $2;
		}
	| EXPRESS '+'
		{
			my_output ("+");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '-'
		{
			my_output ("-");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '*'
		{
			my_output ("*");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS div_tok
		{
			my_output ("/");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '='
		{
			my_output ("==");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS not_eq_tok
		{
			my_output ("!=");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS mod_tok
		{
			my_output ("%");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS shl_tok
		{
			my_output ("<<");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS shr_tok
		{
			my_output (">>");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '<'
		{
			my_output ("<");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '>'
		{
			my_output (">");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS less_eq_tok
		{
			my_output ("<=");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS great_eq_tok
		{
			my_output (">=");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS and_tok
		{
			my_output ("&&");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS or_tok
		{
			my_output ("||");
		}
	EXPRESS
		{
			$$ = max ($1, $4);
		}
	| EXPRESS '/'
		{
			my_output ("/ ((double)");
		}
	EXPRESS
		{
			$$ = max ($1, $4); my_output (")");
		}
	| FACTOR
		{
			$$ = $1;
		}
	;

UNARY_OP:
	unary_plus_tok
	| unary_minus_tok
		{
			my_output ("- (int)");
		}
	| not_tok
		{
			my_output ("!");
		}
	;

FACTOR:
	'('
		{
			my_output ("(");
		}
	EXPRESS ')'
		{
			my_output (")"); $$ = $1;
		}
	| VARIABLE
	| MEM_VAR
		{
			sprintf(safe_string, "get%s ()", last_id);
			my_output(safe_string);
		}
	| CONSTANT
	| FUNCTION
	| cast_tok '(' 
		{
			my_output ("(");
		}
	TYPE ','
		{
			my_output (")");
		}
	EXPRESS ')'
	| OBJECT '='
		{
			my_output("==");
		}
	CONSTANT_ID
	| OBJECT not_eq_tok
		{
			my_output("!=");
		}
	CONSTANT_ID
	;

NEW_PARAM_LIST:
	/* empty */
		{
			my_output ("()");
		}
	| '('
		{
			my_output ("(");
		}
	ACTUAL_PARAM_L ')'
		{
			my_output (")");
		}
	| '['
		{
			my_output ("[");
		}
	EXPRESS ']'
		{
			my_output ("]");
		}

FUNCTION: 
	CLASSIFIER fun_id_tok
		{
			my_output (last_id);
			my_output ("()");
		}
	| CLASSIFIER fun_param_tok
		{
			my_output (last_id);
		}
	PARAM_LIST
	| CLASSIFIER read_tok
		{
			my_output(". read ()");
		}
	'(' VARIABLE ')'
	;

PARAM_LIST:
	'('
		{
			my_output ("(");
		}
	ACTUAL_PARAM_L ')'
		{
			my_output (")");
		}
	;

ACTUAL_PARAM_L:
	ACTUAL_PARAM
	| ACTUAL_PARAM_L ','
		{
			my_output (",");
		}
	ACTUAL_PARAM
	;

ACTUAL_PARAM:
	EXPRESS WIDTH_FIELD
	| OBJECT
	; 

WIDTH_FIELD:
	':' i_num_tok ':' i_num_tok
	| ':' i_num_tok
	| /* empty */
	;

PROC_STAT:
	CLASSIFIER SIMPLE_PROC_STAT
	;

SIMPLE_PROC_STAT:
	proc_id_tok
		{
			my_output (last_id);
			my_output ("()");
		}
	| undef_id_tok
		{
			my_output (last_id);
			ii = add_to_table(last_id);
			sym_table[ii].typ = proc_id_tok;
			my_output ("()");
		}
	| proc_param_tok
		{
			my_output (last_id);
		}
	PARAM_LIST
	;

GO_TO_STAT:
	goto_tok i_num_tok
		{
			sscanf(temp, "%ld", &lab_no);
			check_goto(lab_no);
			broken[level] = true;
		}
	;

EMPTY_STAT:
	/* empty */
	;

STRUCT_STAT:
	COMPOUND_STAT
	| CONDIT_STAT
	| REPETIT_STAT
	;

CONDIT_STAT:
	IF_STATEMENT
	| CASE_STATEMENT
	;

IF_STATEMENT:
	BEGIN_IF_STAT
	| BEGIN_IF_STAT ELSE_STAT
	;

BEGIN_IF_STAT:
	if_tok 
		{
			my_output ("if"); my_output ("(");
		}
	EXPRESS 
		{
			my_output (")"); new_line ();
		}
	then_tok
		{
			level++;
		}
	STATEMENT
		{
			level--;
		}
	;

ELSE_STAT:
	else_tok
		{
			my_output ("else");
			if (broken[++level]) {
				halfbroken = true;
				broken[level] = false;
			} else
				halfbroken = false;
		}
	STATEMENT
		{
			if (broken[level--] && halfbroken)
				broken[level] = true;
		}
	;

CASE_STATEMENT:
	case_tok
		{
			my_output ("switch"); my_output ("("); 
			check_loop_start(true);
		}
	EXPRESS of_tok 
		{
			my_output (")"); new_line();
			my_output ("{"); indent++;
			level++;
		}
	CASE_EL_LIST END_CASE
		{
			indent--;
			my_output ("}");
			new_line();
			level--;
			check_loop_end(true);
		}
	;

CASE_EL_LIST:
	CASE_ELEMENT 
	| CASE_EL_LIST ';' CASE_ELEMENT
	;

CASE_ELEMENT:
	CASE_LAB_LIST ':'
		{
			broken[level] = false;
		}
	UNLAB_STAT
		{
			if (no_exit)
				no_exit = false;
			else if (!broken[level]) {
				my_output ("break");
				semicolon();
			}
		}
	;

CASE_LAB_LIST:
	CASE_LAB
	| CASE_LAB_LIST ',' CASE_LAB
	;

CASE_LAB:
	i_num_tok
		{
			my_output ("case"); 
			my_output (temp);
			my_output (":"); new_line();
		}
	| others_tok
		{
			my_output ("default:"); new_line();
		}
	;

END_CASE:
	end_tok
	| ';' end_tok
	;

REPETIT_STAT:
	WHILE_STATEMENT
	| REP_STATEMENT
	| FOR_STATEMENT
	;

WHILE_STATEMENT:
	while_tok 
		{
			my_output ("while");
			my_output ("(");
			level++;
			check_loop_start(false);
		}
	EXPRESS 
		{
			my_output (")");
		}
	do_tok STATEMENT
		{
			level--; 
			check_loop_end(false); 
		}
	| rest_tok while_tok 
		{
			my_output ("while");
			my_output ("(");
			level++;
			check_loop_start(false);
		}
	EXPRESS 
		{
			my_output (")");
		}
	do_tok STATEMENT
		{
			level--; 
			check_loop_end(false); 
		}
	;

REP_STATEMENT:
	repeat_tok 
		{
			my_output ("do");
			my_output ("{");
			indent++;
			level++;
			check_loop_start(false);
		}
	STAT_LIST until_tok 
		{
			indent--; my_output ("}");
			level--;
			my_output ("while"); my_output ("( ! (");
		}
	EXPRESS
		{
			my_output (") )"); 
			check_loop_end(false); 
		}
	;

FOR_STATEMENT:
	for_tok 
		{
			my_output ("for");
			my_output ("(");
			level++;
			check_loop_start(false);
		}
	CONTROL_VAR assign_tok 
		{
			my_output (control_var);
			my_output ("=");
		}
	FOR_LIST do_tok
		{
			indent++; 
			new_line();
		}
	STATEMENT
		{
			indent--;
			new_line();
			level--;
			check_loop_end(false); 
		}
	;

CONTROL_VAR:
	var_id_tok
		{
			strcpy(control_var, last_id);
		}
	;

FOR_LIST:
	EXPRESS 
		{
			my_output (";");
		}
	to_tok 
		{

			my_output (control_var);
			my_output ("<=");
		}
	EXPRESS
		{

			my_output (";");
			my_output (control_var);
			my_output ("++");
			my_output (")");
		}
	| EXPRESS 
		{
			my_output (";");
		}
	downto_tok 
		{
			my_output (control_var);
			my_output (">=");
		}
	EXPRESS
		{
			my_output (";");
			my_output (control_var);
			my_output ("--");
			my_output (")");
		}
	;
%%

static void
compute_array_bounds (void)
{
  long lb;
  char tmp[200];

  if (lower_sym == 0) {
    yyerror ("Cannot handle variable lower bound in array declaration");
  }
  else if (lower_sym == -1) {	/* lower is a constant */
    lb = lower_bound - 1;
    if (lb==0) lb = -1;	/* Treat lower_bound==1 as if lower_bound==0 */
    if (upper_sym == -1)	/* both constants */
      sprintf(tmp, "[%ld]", upper_bound - lb);
    else {			/* upper a symbol, lower constant */
/*
      if (classified) {
        if (lb < 0)
          sprintf(tmp, "[%s.%s + %ld]",
            symbol(class_id), symbol(upper_sym), (-lb));
        else
          sprintf(tmp, "[%s.%s - %ld]",
            symbol(class_id), symbol(upper_sym), lb);
      } else {
*/
        if (lb < 0)
          sprintf(tmp, "[%s + %ld]",
            symbol(upper_sym), (-lb));
        else
          sprintf(tmp, "[%s - %ld]",
            symbol(upper_sym), lb);
/*
      }
*/
    }
    if (lower_bound < 0 || lower_bound > 1) {
      if (*array_bounds) {
        yyerror ("Cannot handle offset in second dimension");
      }
      if (lower_bound < 0) {
        sprintf(array_offset, "+%ld", -lower_bound);
      } else {
        sprintf(array_offset, "-%ld", lower_bound);
      }
      set_offset();
    }
    strcat(array_bounds, tmp);
  } else {			/* lower is a symbol */
    if (upper_sym != -1)	/* both are symbols */
      sprintf(tmp, "[%s - %s + 1]", symbol(upper_sym),
        symbol(lower_sym));
    else {			/* upper constant, lower symbol */
      sprintf(tmp, "[%ld - %s]", upper_bound + 1,
        symbol(lower_sym));
    }
    if (*array_bounds) {
      yyerror ("Cannot handle symbolic offset in second dimension");
    }
    sprintf(array_offset, "- (int)(%s)", symbol(lower_sym));
    set_offset();
    strcat(array_bounds, tmp);
  }
  classified = false;
}

/* Dealing with negative lower array bounds.  */

static void
fixup_var_list ()
{
  int i, j;
  char output_string[100], real_symbol[100];

  if (make_object)
    for (i=0; i<ids_paramed; i++)
      sym_table[param_id_list[i]].typ = object_id_tok;

  for (i = 0; var_list[i++] == '!'; ) {
    for (j = 0; (real_symbol[j++] = var_list[i++]); );
    if (*array_bounds) {
      if (make_new)
        sprintf (output_string, "%s%s = new %s%s%c", real_symbol, 
            array_dim==1? "[]": "[][]",
            safe_string, array_bounds, var_list[i] == '!' ? ',' : ' ');
      else
        sprintf (output_string, "%s%s", real_symbol, 
            array_dim==1? "[]": "[][]");
    } else if(make_object && make_new)
      sprintf (output_string, "%s%s = new %s()%c", real_symbol, 
        array_bounds, last_id,
        var_list[i] == '!' ? ',' : ' ');
    else
      sprintf (output_string, "%s%s%c", real_symbol, 
        array_bounds, var_list[i] == '!' ? ',' : ' ');
    my_output (output_string);
  }
  make_object = false;
  make_new = true;
  semicolon ();
}

static void
gen_function_head (void)
{
  int i;

  std = orig_std;
  new_line ();
  my_output (routine);
  my_output ("(");
  for (i=0; i<ids_paramed; i++) {
    if (i > 0) my_output (",");
    sprintf (safe_string, "%s %s", arg_type[i], symbol (param_id_list[i]));
    my_output (safe_string);
  }
  my_output (")");
  new_line ();
}

static void
gen_copy_routine(void)
{
  int i, j;
  indent++;
  sprintf(safe_string, "void copy(%s that) {", symbol(class_id));
  my_output(safe_string);
  new_line();
  indent++;
  for (i=0; i<id_ctr; i++) {
    j = id_list[i];
    if (sym_table[j].typ == object_id_tok)
      sprintf(safe_string, "this . %s . copy ( that . %s ) ;", 
        symbol(j), symbol(j));
    else
      sprintf(safe_string, "this . %s = that . %s ;", symbol(j), symbol(j));
    my_output(safe_string);
    new_line();
  }
  indent--;
  my_output("}");
  new_line();
  indent--;
}

void set_offset() {
  struct sym_entry *s = sym_table + ii;
  s->offset = malloc(strlen(array_offset)+1);
  strcpy(s->offset, array_offset);
}

static void check_loop_start(boolean case_statement) {
  int i, j;
  for (i = 0; j = lab_list[i]; i++)
    if (j <= MAX_LAB && lab[j] > 0) {
      if (lab[j] > loop_level && j != 10)
	warning("!!");
    }
  loop_level++;
}

static void check_loop_end(boolean case_statement) {
  loop_level--;
  stat_no = 0;
}

static void check_lab_stat(int lab_no) {
  int last_last_brace = last_brace;
  if (debug)
    fprintf(stderr,"(%d)",lab_no);
  if (!lab_no) {
    no_exit = true;
    return;
  }
  if (debug && lab_no <= MAX_LAB && lab[lab_no] > 0) { /* post-label */
    if (stat_no > 0 && lab_no != 10 && !cont[lab_no])
      fprintf(stderr, "<%d,%d;%s>", lab_no, stat_no, routine);
  }
  if (lab_no != 10) {
    if (lab_no <= MAX_LAB && lab[lab_no]) {
      sprintf(safe_string, "/* lab%d: */", lab_no);
      if (lab[lab_no] > 0) {
	if (lab[lab_no] != (loop_level+1))
	  warning("!");
      }
      lab[lab_no] = 0;
    } else {
      sprintf(safe_string, "lab%d:", lab_no);
      last_last_brace = 0;
      if (lab_no <= MAX_LAB)
        lab[lab_no] = -loop_level;
    }
    my_output(safe_string);
    last_brace = last_last_brace;
  }
}

static void check_goto(int lab_no) {
  if (debug)
    fprintf(stderr,"[%d]",lab_no);
  if (lab_no == 10) { /* return */
    if (strcmp(fn_return_type, "void")) 
      my_output ("return Result"); 
    else
      my_output ("return");
  } else if (lab_no <= MAX_LAB && cont[lab_no]) { /* continue */
    my_output("continue");
  } else {
    my_output("break");
  }
  if (lab_no > MAX_LAB || lab[lab_no] < 0) {
    sprintf(safe_string, "lab%d", lab_no);
  } else {
    sprintf(safe_string, "/* lab%d */", lab_no);
    if (lab[lab_no] == 0)
      lab[lab_no] = loop_level;
    else if (lab[lab_no] != loop_level && lab_no != 10)
      warning("*");
  }
  my_output(safe_string);
}

static void check_proc_start() {
  int i; 
  memset(lab, 0, LAB_SIZE * sizeof(int));
  for (i = 0; lab_list[i]; i++) 
    if (lab_list[i] <= MAX_LAB)
      lab[lab_list[i]] = 0;
  lab_count = 0;
  stat_no = 0;
  loop_level++;
  for (i = 0; i < MAX_BRA_LEVEL; i++)
    need_offset[i] = false;
  fprintf(stderr, "%s()", routine);
}

static void check_proc_end() {
  int i;
  if (debug) {
    for (i = 0; lab_list[i]; i++)
      fprintf(stderr, "%c%d", i? ' ': '<', lab_list[i]);
    if (i) fprintf(stderr,">");
  }
  lab_list[lab_count] = 0;
  loop_level--;
  fprintf(stderr, "\n");
}

