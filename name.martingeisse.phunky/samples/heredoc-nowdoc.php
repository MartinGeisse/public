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


// ------------------

// beginning and end markers each cannot have content on their line,
// and they cannot be on the same line. This means that each is separated
// from the actual content by a newline character. The two newline
// characters will collapse into a single one for an empty literal.
$x = <<<FOO

foo

FOO;

for ($i=0; $i < 10; $i = $i + 1) {
	echo ord($x[$i]), "\n";
}
echo strlen($x);

// ---

$y = <<<FOO
FOO;

for ($i=0; $i < 10; $i = $i + 1) {
	echo ord($y[$i]), "\n";
}
echo strlen($y);
