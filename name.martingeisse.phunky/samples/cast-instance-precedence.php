<?php

$x = new Stdclass();
$x->a = 5;
$b = (string)$x->a;
var_dump($b);

