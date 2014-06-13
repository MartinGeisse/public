<?php

/* The docs say that => is an operator with higher precedence than "and", but
 * this example shows that "and" has higher precedence
 */
$a = array(
	5 => true and 9
);
var_dump($a);

// ... and this doesn't work at all, which suggests that => isn't an operator at all
// $y = (5 => 8);