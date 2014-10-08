<?php

$a = array(1, 2, 3);
$b = 5;
$a[] = $b;
$b = 99;
print_r($a);
echo "\n";

$a = array(1, 2, 3);
$b = 5;
$a[] =& $b;
$b = 99;
print_r($a);
echo "\n";

$x = 77;
$y[] =& $x;
$x = 99;
print_r($y);
