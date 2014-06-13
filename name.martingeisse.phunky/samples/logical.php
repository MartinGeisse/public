<?php

$x = 0;
$y = (true || $x++);
var_dump($x);
var_dump($y);
echo "---------\n";

$x = 0;
$y = (false && $x++);
var_dump($x);
var_dump($y);
echo "---------\n";

$x = 0;
$y = (true | $x++);
var_dump($x);
var_dump($y);
echo "---------\n";

$x = 0;
$y = (false & $x++);
var_dump($x);
var_dump($y);
echo "---------\n";

$x = 0;
$y = (true or $x++);
var_dump($x);
var_dump($y);
echo "---------\n";

$x = 0;
$y = (false and $x++);
var_dump($x);
var_dump($y);
echo "---------\n";
