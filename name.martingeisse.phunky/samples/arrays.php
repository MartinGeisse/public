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

// ----------------------------------------------------------------
// creating arrays in uninitialized variables through element access
// ----------------------------------------------------------------

echo "\n\n\n";

// create array by assigning to a specific element
echo 'creating arrays in uninitialized variables(1):'."\n";
$newArray1[5] = 9;
var_dump($newArray1);

// create array by appending
echo 'creating arrays in uninitialized variables(2):'."\n";
$newArray2[] = 9;
var_dump($newArray2);

// DOESN'T WORK: create array by reading an element
// echo 'creating arrays in uninitialized variables(3):'."\n";
// $x = $newArray3[5];
// var_dump($newArray3);

// creating an array also works if the variable exists and is null
echo 'creating arrays in uninitialized variables(4):'."\n";
$newArray4 = null;
$newArray4[1] = 9;
var_dump($newArray4);

// DOESN'T WORK: if the variable has the value 0
// echo 'creating arrays in uninitialized variables(5):'."\n";
// $newArray5 = 0;
// $newArray5[1] = 9;
// var_dump($newArray5);

// creating an array also works if the variable has the value false
echo 'creating arrays in uninitialized variables(6):'."\n";
$newArray6 = false;
$newArray6[1] = 9;
var_dump($newArray6);

// DOESN'T WORK: if the variable has the value true
// echo 'creating arrays in uninitialized variables(7):'."\n";
// $newArray7 = true;
// $newArray7[1] = 9;
// var_dump($newArray7);

