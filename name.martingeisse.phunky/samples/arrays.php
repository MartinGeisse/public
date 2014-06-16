<?php

// Appending: the code sequence "$a[] = ...;" determines the key by adding 1 to
// the highest numeric index ever used, even if that index is not used anymore.

// appending (uses 26 as the key)
$a = array(
	'a' => 'value1',
	25 => 'value2',
	'c' => 'value3',
	3 => 'value4',
	'd' => 'value5'
);
$a[] = 'valueX';
var_dump($a);

// appending (still uses 26 as the key, even though 25 isn't used anymore)
$a = array(
	'a' => 'value1',
	25 => 'value2',
	'c' => 'value3',
	3 => 'value4',
	'd' => 'value5'
);
unset($a[25]);
$a[] = 'valueX';
var_dump($a);

// Iteration support:
// - nested foreach on the same array *variable* works
// - iteration pointer functions do not affect foreach
// - first() returns the first element, next() the *others*. That is,
//   next() only returns all elements *except* the first one.
// 
// Note:
// http://stackoverflow.com/questions/10057671/how-foreach-actually-works
// - PHP makes a copy so nested foreach loops work. It's a "PHP copy"
//   though, sharing a value if possible. If the array gets modified in
//   the loop body, *then* the array being iterated and the array being
//   modified (and visible to the outside) get separated by COW.
// - Unclear: Nested foreach on an array that is a reference should break
//   then, but doesn't...!?
//
// Most interesting "first question" is if the iteration pointer is
// needed at all. Seems like everybody just uses foreach. Even if it
// *is* used (like an explicit iterator in Java), it's probably not mixed
// with foreach iteration, especially since almost nobody would get it
// right what would actually happen.
// ->
// Seems like I should protect against "mixed" usage, then implement the
// whole thing in a sane way.
//
$a = array('foo', 'bar', 'fupp', 'blubber');
$b =& $a;
// reset($a);

foreach ($a as $x) {
// 	echo '1                '.next($a)."\n";
// 	echo '1                '.next($a)."\n";
// 	echo '1                '.next($a)."\n";
	foreach ($a as $y) {
		echo "* $x / $y \n";
//		echo '#                '.reset($a)."\n";
// 		echo '2                '.next($a)."\n";
// 		echo '2                '.next($a)."\n";
// 		echo '2                '.next($a)."\n";
	}
// 	echo '3                '.next($a)."\n";
// 	echo '3                '.next($a)."\n";
// 	echo '3                '.next($a)."\n";
}

// order vs. setting entries
$a = array(0 => 'a', 1 => 'b', 5 => 'c');
$a[1] = 111;
$a[2] = 222;
$a[3] = 333;
var_dump($a);
