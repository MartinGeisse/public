This is a change file for the Knuth/Levy common.w, version 3.3.
For information on its use see the introduction to ctang-java.ch .

Timothy Murphy <tim@maths.tcd.ie> 11 Sep 1996

@x [line 124]
@d gt_gt 021 /* `\.{>>}'\,;  corresponds to MIT's {\tentex\char'21} */
@y
@d gt_gt 021 /* `\.{>>}'\,;  corresponds to MIT's {\tentex\char'21} */
@d gt_gt_gt 022 
@d java flags['j']
@z

@x [line 1299]
  sprintf(C_file_name,"%s.c",name_pos);
@y
  if (java)
    sprintf(C_file_name,"%s.java",name_pos);
  else
    sprintf(C_file_name,"%s.c",name_pos);
@z

