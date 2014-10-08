<?php

/* This sample is needed because the English and German PHP documentation tell
 * radically different stories about operator precedence and associativity. This
 * test is used to find out how it actually works.
 */


// Precedence of the expression (!$a++): The variable will always be incremented
// first, whether ! and ++ have the same precedence (as stated in the German doc)
// or ++ has higher precedence (as stated in the English doc), because they are
// right-associative.

// Precedence of ! vs. instanceof: instanceof is evaluated first
$x = new Stdclass();
$a = (!$x instanceof FooClass); // true
var_dump($a);
$b = (!($x instanceof FooClass)); // true
var_dump($b);
$c = ((!$x) instanceof Stdclass); // false
var_dump($c);
