This is a change file for the Knuth/Levy cweave.w, version 3.4.
For information on its use see the introduction to ctang-java.ch .

Timothy Murphy <tim@maths.tcd.ie> 11 Sep 1996

@x [line 65]
@d banner "This is CWEAVE (Version 3.43)\n"
@y
@d banner "This is CWEAVE (Version 3.43j1)\n"
@d gt_gt_gt 022 
@d java flags['j']
@z

@x [line 414]
id_lookup("error",NULL,if_like);
@y
if (!java) id_lookup("error",NULL,if_like);
@z

@x [line 430]
id_lookup("line",NULL,if_like);
@y
if (!java) id_lookup("line",NULL,if_like);
@z

@x [line 467]
id_lookup("while",NULL,for_like);
@y
id_lookup("while",NULL,for_like);
if (java) {
  id_lookup("abstract",NULL,public_like);
  id_lookup("boolean",NULL,raw_int);
  id_lookup("byte",NULL,raw_int);
  id_lookup("extends",NULL,base);
  id_lookup("final",NULL,int_like);
  id_lookup("finally",NULL,do_like);
  id_lookup("implements",NULL,base);
  id_lookup("import",NULL,define_like);
  id_lookup("instanceof",NULL,sizeof_like);
  id_lookup("interface",NULL,struct_like);
  id_lookup("native",NULL,int_like);
  id_lookup("package",NULL,define_like);
  id_lookup("synchronized",NULL,if_like);
  id_lookup("throws",NULL,base);
}
@z

@x [line 674]
    if (c=='#' && loc==buffer+1) @<Raise preprocessor flag@>;
@y
    if (c=='#' && loc==buffer+1 && !java) @<Raise preprocessor flag@>;
@z

@x [line 750]
    else if (*loc=='>') compress(gt_gt); break;
@y
    else if (*loc=='>') 
      {if (java && *(loc+1)=='>') {loc++; compress(gt_gt_gt);}
       else compress(gt_gt);}
      break;
@z

@x [line 1978]
@i prod.w
@y
@i jprod.w
@z

@x [line 2295]
    case raw_int: @<Cases for |raw_int|@>; @+break;
@y
    case raw_int: @<Cases for |raw_int|@>; @+break;
    case define_like: if (java) {
      if (cat1==decl_head || cat1==stmt)
        squash(pp,2,define_like,0,115);
      else if (cat1==exp && cat2==binop && cat3==semi)
        squash(pp,4,stmt,0,116);
      @+break;
    }
@z

@x [line 2456]
    big_app1(pp); big_app(' '); big_app1(pp+1); big_app(' '); big_app1(pp+2);
@y
    if (java) big_app(' '); 
    big_app1(pp); big_app(' '); big_app1(pp+1); big_app(' '); big_app1(pp+2);
@z

@x [line 2520]
else if (cat1==exp || cat1==unorbinop || cat1==semi) {
@y
else if (java && cat1==binop && cat2==exp)
  squash(pp,3,exp,-2,112);
else if (cat1==exp || cat1==unorbinop || cat1==semi) {
@z

@x [line 2556]
else if (cat1==semi) squash(pp,2,decl,-1,39);
@y
else if (cat1==semi) squash(pp,2,decl,-1,39);
else if (java && cat1==base && (cat2==exp || cat2==int_like))
  squash(pp,3,decl_head,0,113);
@z

@x [line 2592]
  else if (cat2!=base) {
    big_app1(pp); big_app(' '); big_app1(pp+1); reduce(pp,2,int_like,-2,48);
  }
@y
  else if (cat2!=base) {
    big_app1(pp); big_app(' '); big_app1(pp+1); reduce(pp,2,int_like,-2,48);
  } else if (java && cat3 == exp || cat3 == int_like) {
    make_underlined(pp+1); make_reserved(pp+1);
    squash(pp+1,3,cat1,0,114);
  }
@z

@x [line 3117]
  case '#': app_str("\\#"); app_scrap(unorbinop,yes_math);@+break;
@y
  case '#': app_str("\\#"); app_scrap(java?exp:unorbinop,yes_math);@+break;
@z

@x [line 3189]
case gt_gt: app_str("\\GG");@+app_scrap(binop,yes_math);@+break;
@.\\GG@>
@y
case gt_gt: app_str("\\GG");@+app_scrap(binop,yes_math);@+break;
@.\\GG@>
case gt_gt_gt: app_str("\\ggg");@+app_scrap(binop,yes_math);@+break;
@.\\ggg@>
@z

@x [line 3979]
      case ')': app(next_control); next_control=get_next(); break;
      default: err_print("! Improper macro definition"); break;
    }
    else next_control=get_next();
@y
      case ')': app(next_control); next_control=get_next(); break;
      case '#': if (java) {app_str("\\#"); goto reswitch;}
      default: err_print("! Improper macro definition"); break;
    }
    else next_control=get_next();
    if (java) {
      if (next_control == eq_eq) {
        app_str("\\E"); 
        next_control=get_next();
      } else err_print("! Improper macro definition");
    }
@z

@x [line 3984]
    app_scrap(dead,no_math); /* scrap won't take part in the parsing */
@y
    app_scrap(java?define_like:dead,no_math);
      /* scrap won't take part in the parsing */
@z

