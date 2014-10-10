<?php

// when assigning to a string cell, only a single character gets replaced
// by another single character (the first character of the RHS string).
// Computed assignment operators such as .= cannot be used on string cells
// (triggers an error).

$a = 'abcde';
$a[1] = 'foo';
echo $a; // afcde'
