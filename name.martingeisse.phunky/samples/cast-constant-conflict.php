<?php

// constant
define('foo', 'bar');
echo foo."\n";

// cast
$a = 5;
echo (int)$a;

// ?
echo (foo)."\n";

// ?
define('long', 'loooooong');
echo (long)."\n";
