<?php

$a = 'abc';

$x = <<<FOO
This is a test: $a done.
FOO;
$y = <<<'FOO'
This is a test: $a done.
FOO;
$z = <<<"FOO"
This is a test: $a done.
FOO;

echo $x."\n"; // variable replaced
echo $y."\n"; // literal text preserved
echo $z."\n"; // variable replaced

?>
