This is a change file for the Knuth/Levy ctangle.w, version 3.4c,
which can be retrieved from the CTANs, eg
ftp://ftp.tex.ac.uk/tex-archive/web/c_cpp/cweb .
The latest version of this change file can be found at
ftp://ftp.maths.tcd.ie/pub/TeX/javaTeX .

This change file allows ctangle to output Java code
when used with the "-j" switch.
If this switch is not given,
ctangle should run exactly as before.

The effect of the changes is to restore
the macro facilities provided in Knuth's original tangle.
These were removed from ctangle 
because their role could be played by the C preprocessor.
This option is not available with Java,
since it has no preprocessor. 

This change file must be used in conjunction with
the (very small) change file comm-java.ch .
There is also a corresponding (small) change file cweav-java.ch .

The cweb Makefile allows these change files to be used very easily,
by changing the line "TCHANGES=" to "TCHANGES=ctang-java.ch",
and similarly for "CCHANGES=" and "WCHANGES=".

The document JavaLitProg.tex describes more fully
how these change files operate.

Timothy Murphy <tim@maths.tcd.ie> 11 Sep 1996

@x [line 59]
@d banner "This is CTANGLE (Version 3.4)\n"
@y
@d banner "This is CTANGLE (Version 3.4j1)\n"
@d gt_gt_gt 022 
@z

@x [line 172]
@d equiv equiv_or_xref /* info corresponding to names */
@y
@d equiv equiv_or_xref /* info corresponding to names */
@d ilk dummy.Ilk
@d normal 0
@d numeric 1
@d simple 2
@d parametric 3
@d param 0
@d java flags['j']
@z

@x [line 362]
  stack_ptr--; /* go down to the previous level */
@y
  if (cur_name->ilk == parametric)
    @<Remove a parameter from the parameter stack@>@;
  stack_ptr--; /* go down to the previous level */
@z

@x [line 403]
  else if (a<0200) out_char(a); /* one-byte token */
@y
  else if (a<0200) {
    if (a==param)
      @<Start scanning current macro parameter, |goto restart|@>@;
    else
      out_char(a); /* one-byte token */
  }
@z

@x [line 407]
      case 0: cur_val=a; out_char(identifier); break;
@y
      case 0: if (java) {
          @<Expand macro |a|, |goto restart| if no output found@>;
        } else {
          cur_val=a; out_char(identifier);
        }
        break;
@z

@x [line 612]
      C_printf("%s","#define ");
      out_state=normal;
      protect=1; /* newlines should be preceded by |'\\'| */
@y
      if (java)
        C_printf("%s","public static final int ");
      else {
        C_printf("%s","#define ");
        protect=1; /* newlines should be preceded by |'\\'| */
      }
      out_state=normal;
@z

@x
      flush_buffer();
@y
      if (java) C_putc(';');
      flush_buffer();
@z

@x [line 681]
case gt_gt: C_putc('>'); C_putc('>'); out_state=normal; break;
@y
case gt_gt: C_putc('>'); C_putc('>'); out_state=normal; break;
case gt_gt_gt: C_putc('>'); C_putc('>'); C_putc('>');
  out_state=normal; break;
@z

@x [line 740]
    C_printf("\n#line %d \"",a);
@y
    if (java)
      C_printf("\n//line %d \"",a);
    else
      C_printf("\n#line %d \"",a);
@z

@x [line 956]
    else if (*loc=='>') compress(gt_gt); break;
@y
    else if (*loc=='>') 
      {if (*(loc+1)=='>') {loc++; compress(gt_gt_gt);}
       else compress(gt_gt);}
      break;
@z

@x [line 1196]
void
scan_repl(t) /* creates a replacement text */
@y
@<Define |scan_numeric| function@>@;
void
scan_repl(t) /* creates a replacement text */
@z

@x [line 1200] Removing newlines at end of replacement text.
  sixteen_bits a; /* the current token */
@y
  sixteen_bits a; /* the current token */
  eight_bits* first_nl; /* start of present sequence of newlines */
  eight_bits* last_nl; /* end of present sequence of newlines */
  last_nl=tok_mem;
@z

@x [line 1207]
      case ')': app_repl(a);
        if (t==macro) app_repl(' ');
        break;
@y
      case ')': app_repl(a);
        if (t==macro && !java) app_repl(' ');
        break;
      case '#': if (t==parametric) app_repl(param)
        else app_repl('#'); break;
      case '\n': if (last_nl!=tok_ptr) first_nl=tok_ptr;
        last_nl=tok_ptr+1; /* drop through */
@z
@x [line 1214]
  cur_text=text_ptr; (++text_ptr)->tok_start=tok_ptr;
@y
  if (java && last_nl==tok_ptr) tok_ptr=first_nl;
    /* ignore final newlines */
  cur_text=text_ptr; (++text_ptr)->tok_start=tok_ptr;
@z

@x [line 1221]
@<Insert the line...@>=
@y
@<Insert the line...@>=
if (!java) {
@z
@x [line 1229]
  app_repl(a % 0400);}
@y
  app_repl(a % 0400);}
}
@z

@x [line 1401]
  app_repl(((a=id_lookup(id_first,id_loc,0)-name_dir) / 0400)+0200);
        /* append the lhs */
  app_repl(a % 0400);
  if (*loc!='(') { /* identifier must be separated from replacement text */
    app_repl(string); app_repl(' '); app_repl(string);
  }
  scan_repl(macro);
  cur_text->text_link=0; /* |text_link==0| characterizes a macro */
@y
p=id_lookup(id_first,id_loc,0);
a=p-name_dir;
if (java) {
  next_control=get_next(); /* get token after the identifier */
  if (next_control=='=') {
    p->ilk=numeric;
    scan_numeric(p);
  } else if (next_control==eq_eq) {
    p->ilk=simple;
    scan_repl(simple);
    p->equiv=(char*)cur_text;
    cur_text->text_link=section_flag;
  } else if (next_control=='(') {
    next_control=get_next();
    if (next_control=='#') {
      next_control=get_next();
      if (next_control==')') {
        next_control=get_next();
        if (next_control=='=') {
          err_print("! Use == for macros");
          next_control=eq_eq;
        }
        if (next_control==eq_eq) {
          p->ilk=parametric;
          scan_repl(parametric);
          p->equiv=(char*)cur_text;
          cur_text->text_link=section_flag;
        }
      }
    }
  } else err_print("! Definition flushed since it starts badly");
@.Definition flushed...@>
} else {
  app_repl((a/0400)+0200);
  app_repl(a % 0400);
        /* append the lhs */
  if (*loc!='(') { /* identifier must be separated from replacement text */
    app_repl(string); app_repl(' '); app_repl(string);
  }
  scan_repl(macro);
  cur_text->text_link=0; /* |text_link==0| characterizes a macro */
}
@z

@x
@** Index.
@y
@* Some Java-related addenda.

@*1 Enabling macros.

@ @<Expand macro...@>= 
switch ((a+name_dir)->ilk) {
  case normal: cur_val=a; out_char(identifier); break;
  case numeric: cur_val=(int)(a+name_dir)->equiv-0100000; 
    if(out_state==num_or_id) C_putc(' ');
    C_printf("%d",cur_val); goto restart;
  case simple: if ((a+name_dir)->equiv!=(char *)text_info) 
      push_level(a+name_dir); goto restart;
  case parametric: @<Put a parameter on the parameter stack@>@; 
      push_level(a+name_dir); goto restart;
  default: confusion("Output");
}

@ @<Put a parameter on the parameter stack@>=
while (cur_byte==cur_end && stack_ptr>stack) pop_level(1);
if (stack_ptr==stack || *cur_byte!='(') {
  printf("\n! No parameter given for macro \"");
  print_id(a+name_dir); err_print("\"");
  goto restart;}
@<Copy the parameter into |tok_mem|@>@;
name_ptr->equiv=(char *)text_ptr; 
name_ptr->ilk=simple;
if (name_ptr++>=name_dir_end) overflow("name");
name_ptr->byte_start=byte_ptr;
if (text_ptr>text_info_end) overflow("text");
text_ptr->text_link=section_flag;
(++text_ptr)->tok_start=tok_ptr;

@ @<Remove a parameter from the parameter stack@>=
{
  name_ptr--; text_ptr--;
  tok_ptr=text_ptr->tok_start;
}

@ @<Start scanning current macro parameter...@>=
{
  push_level(name_ptr-1); goto restart;
}

@ @<Copy the parameter into |tok_mem|@>=
{
  sixteen_bits bal=1;
  sixteen_bits b;
  cur_byte++; /* skip the opening '(' */
  while (1) {
    b=*cur_byte++;
    if (b==param)
      store_two_bytes((name_ptr-name_dir)+077777);
    else {
      if (b>=0200) {
        app_repl(b); b=*cur_byte++;
      } else switch(b) {
        case '(': bal++; break;
        case ')': if (--bal==0) goto done; break;
        case '"': 
          do {app_repl(b); b=*cur_byte++;} while (b!='"');
          break;
        default:
      }
      app_repl(b);
    }
  }
  done:
}

@ @<Define |scan_numeric| function@>=
void 
scan_numeric(name_pointer p)
{
  int accumulator; /* accumulates sums */
  int next_sign; /* +1 or -1 */
  name_pointer q; /* points to identifiers being evaluated */
  int val; /* constants being evaluated */

  @<Set |accumulator| to the value of the right-hand side@>@;
  if (abs(accumulator) >= 0100000) {
    err_print("! Value too big"); accumulator = 0;
  }
  p->equiv = (char *)accumulator + 0100000;
}

@ @d add_in(val) accumulator+=next_sign*val; next_sign=1;

@<Set |accumulator| to the value of the right-hand side@>=
accumulator=0; next_sign=+1;
reswitch: switch(next_control=get_next()) {
  case constant:
    @<Set |val| to value of constant@>@;
    add_in(val);
    goto reswitch;
  case identifier: goto reswitch;
  case '+': goto reswitch;
  case '-': next_sign=-next_sign; goto reswitch;
  case '\n': goto reswitch;
  case format_code:
  case definition:
  case begin_C:
  case section_name:
  case new_section:
    break;
  case ';':
    err_print("! Omit semicolon in numeric definition");
    break;
  default: 
    @<Signal error, flush rest of definition@>@;
    break;
}

@ @<Signal error, flush rest of definition@>=
err_print("! Improper numeric definition will be flushed");
@<Skip ahead until |next_control| ...@>@;

@ @<Set |val| to value of constant@>=
if  (*id_first=='0') {
} else {
  sscanf(id_first,"%d",&val);
}

@** Index.
@z

