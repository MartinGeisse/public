<?php
$obj = new Stdclass();

// this is ok, even though the constant 'foo' isn't defined
$foo = 42;

// valid PHP, but will be disallowed in Phunky because it's likely an error
// $$bar = 3;

// this is ok, even though the constant 'foo' isn't defined
$obj->foo = 42;

// should this be supported in Phunky? Makes sense for scripting, but only
// if using objects (but that is useful, so yes)
$obj->$foo = 23;

// same here
$obj->$foo();
