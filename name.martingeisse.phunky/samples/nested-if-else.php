<?php

$tf = array(true, false);
foreach ($tf as $x) {
	foreach ($tf as $y) {
	
		echo 'values: '.$x.', '.$y."\n";
		
		if ($x) {
			if ($y) {
				echo 'T';
			} else {
				echo 'E';
			}
		}
		echo '.';

		if ($x) {
			if ($y) {
				echo 'T';
			}
		} else {
			echo 'E';
		}
		echo '.';

		if ($x)
			if ($y)
				echo 'T';
			else
				echo 'E';
		echo '.';
		
		echo "\n";
	
	}
}
