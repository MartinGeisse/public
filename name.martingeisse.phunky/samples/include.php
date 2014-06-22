<?php

// echoes "aa" -> include() affects include_once()
include('included/a.php');
include('included/a.php');
include_once('included/a.php');
include_once('included/a.php');
echo "\n";

// echoes "bb" -> require() affects require_once()
require('included/b.php');
require('included/b.php');
require_once('included/b.php');
require_once('included/b.php');
echo "\n";

// echoes "c" -> require() affects include_once()
require('included/c.php');
include_once('included/c.php');
echo "\n";

// echoes "d" -> include() affects require_once()
include('included/d.php');
require_once('included/d.php');
echo "\n";

// Conclusion: there is only a single set of already-included files. All four functions contribute
// to that set. The _once functions read from it.

