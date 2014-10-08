<?php

// represents an instance variable of an object
$objectVariable = null;

// represents a method of that object
function firstMethod($a) {
	global $objectVariable;
	$objectVariable = $a;
}

// represents another method of that object
function secondMethod() {
	global $objectVariable;
	var_dump($objectVariable);
}

// ------------------------------
// main code
// ------------------------------

// load model
$data = array(
	'Shop' => array(
		'id' => 1,
		'name' => 'testshop',
	),
	'User' => array(
		'id' => 42,
		'name' => 'testuser',
	),
);

// manipulate data (sometimes easier using references)
$ref =& $data['Shop'];
$ref['name'] = 'liveshop';

// store in object
firstMethod($data);

// do something else
$data['Shop']['name'] = 'foobar';

// use the object again
secondMethod();

// echoes "foobar" in PHP (unexpected), "liveshop" in Phunky
