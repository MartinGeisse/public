<?php

/*
 * DOESN'T WORK YET, because PHP will not only insist on its
 * zval magic for references, but even print out the reference
 * flag in var_dump. So the output differs by that flag marker.
 */

echo "\n";

function f(&$x) {
	$x = 42;
}

function g(&$x) {
}

function h(&$x) {
	global $a;
	var_dump($a);
}

// insertion into array
$a = array();
$a[] = 1;
var_dump($a);
echo "\n";

// reference parameter
$y = 23;
f($y);
var_dump($y);
echo "\n";

// insertion as reference parameter
f($a[]);
var_dump($a);
echo "\n";

// if not assigned, null gets inserted
g($a[]);
var_dump($a);
echo "\n";

// Insertion of null occurs before the call. In f(), that null gets overwritten later.
// While the function is running, the element being inserted is a reference cell;
// later it becomes a value cell.
h($a[]);
var_dump($a);

// reading a slot being inserted is impossible in PHP
// $b = $a[];
// var_dump($b);
