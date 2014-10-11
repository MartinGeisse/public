<?php

$v = 'blupp';

function f($x) {
	global $a;
	echo 'a: '; var_dump($a);
	echo 'x: '; var_dump($x);
	return $x;
}

function g($x, $info) {
	$y = print_r($x, true);
	echo "$info: $y\n";
	return $x;
}

function& h() {
	global $a, $v;
	echo 'a: '; var_dump($a);
	return $v;
}

//
// ------------------------------------------------------------
//

echo "basic evaluation order\n";
g(array(), 'array')[g('foo', 'key')] = g('bar', 'value');

echo "-------------------------------\n";

echo "check for pre-allocated array cell in value assignment\n";
$a = array();
$a[g('foo', 'key')] = f('bar');
var_dump($a);

echo "-------------------------------\n";

echo "check for colliding pre-allocated array cell in value assignment\n";
$a = array('foo' => 'previous');
$a[g('foo', 'key')] = f('bar');
var_dump($a);

echo "-------------------------------\n";

echo "check for pre-allocated array cell in reference assignment\n";
$a = array();
$a[g('foo', 'key')] =& h();
var_dump($a);

echo "-------------------------------\n";

echo "check for colliding pre-allocated array cell in reference assignment\n";
$a = array('foo' => 'previous');
$a[g('foo', 'key')] =& h();
var_dump($a);

echo "-------------------------------\n";

echo "check if the LHS is resolved to a variable or just an assignment target before evaluating the RHS (1)\n";
// It *does* only resolve to an assignment target -- if that is a reference, the actual variable
// is only determined when actually performing the assignment. If the reference is changed to another
// variable in the meantime, that new variable will be assigned to.
// If the operator takes the LHS value as input, then the

$a = array(
	'first' => 1,
	'second' => 2,
);
$a['foo'] =& $a['first'];
function q() {
	global $a;
	$a['foo'] =& $a['second'];
	return 99;
}
$a['foo'] = q();
print_r($a);

echo "-------------------------------\n";

echo "check if the LHS is resolved to a variable or just an assignment target before evaluating the RHS (2)\n";
// It *does* only resolve to an assignment target -- if that is a reference, the actual variable
// is only determined when actually performing the assignment. If the reference is changed to another
// variable in the meantime, that new variable will be assigned to.
// If the operator takes the LHS value as input, then that value is totally computed from the LHS
// assignment target at the time of assignment, both with respect to following references and
// to reading the value for a computed-assignment operator. These latter two steps happen
// in unspecified order because the computation of an operator cannot re-bind any LHS reference.

$a = array(
	'first' => 10,
	'second' => 20,
);
$a['foo'] =& $a['first'];
function r() {
	global $a;
	$a['second']++;
	$a['foo'] =& $a['second'];
	return 99;
}
$a['foo'] .= r();
print_r($a);

echo "-------------------------------\n";

echo "check the order of resolving the LHS assignment target or variable with respect to appending\n";

$dummy = 'dummy';

function s() {
	global $a;
	var_dump($a);
	$a[] = '---';
	return 99;
}
function& t() {
	global $a, $dummy;
	var_dump($a);
	$a[] = '---';
	return $dummy;
}

$a = array();
$a[] = s();
$a[] =& t();
var_dump($a);

echo "-------------------------------\n";

echo "check the order of resolving the LHS assignment target if it is a string cell\n";
// the assignment target is resolved, not knowing yet whether it will refer
// to an array element or string cell

function u() {
	global $a;
	$a = 'foo';
	return 'x';
}

$a = array(10, 20, 30);
$a[1] = u();
var_dump($a);

echo "-------------------------------\n";

echo "check the order of resolving the LHS assignment target in case the array gets re-bound\n";
// the array itself it fetched from its assignment target only upon assignment,
// after evaluating the RHS.

function v() {
	global $a;
	$a['foo'] =& $a['two'];
	return 10;
}

$a = array(
	'one' => array(1, 2, 3),
	'two' => array(4, 5, 6),
);
$a['foo'] =& $a['one'];
$a['foo'][1] += v();
var_dump($a);
