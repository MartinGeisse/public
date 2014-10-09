<?php

// prints a b c
$a1 = array('a', 'b', 'c');
foreach ($a1 as $x) {
	echo $x."\n";
	if ($x == 'a') {
		$a1[2] = 99;
	}
}

// prints a b 99, because the "$b =& $a2;" turns a2 into a reference variable
// TODO: not so for Phunky! -- define exactly what should be tested
/*
$a2 = array('a', 'b', 'c');
$b =& $a2;
foreach ($a2 as $x) {
	echo $x."\n";
	if ($x == 'a') {
		$a2[2] = 99;
	}
}
*/

// prints a b 99, because $a2 is still a reference variable
// TODO: not so for Phunky! -- define exactly what should be tested
/*
$a2 = array('a', 'b', 'c');
foreach ($a2 as $x) {
	echo $x."\n";
	if ($x == 'a') {
		$a2[2] = 99;
	}
}
*/
