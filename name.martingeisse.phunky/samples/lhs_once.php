<?php

$a = array(1, 2, 3, 4);
$x = 1;
$a[$x++] += 97;

var_dump($a);
var_dump($x);
