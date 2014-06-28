<?php

// prints "ed" -->
// - early default case does not override later matching cases
// for multiple default cases, the last one is used
foreach (array(3, 4) as $x) {
	switch ($x) {
			
	case 2:
		echo 'a';
		break;
		
	default:
		echo 'b';
		break;
		
	case 5:
		echo 'c';
		break;
		
	default:
		echo 'd';
		break;
		
	case 3:
		echo 'e';
		break;
		
	}
}
