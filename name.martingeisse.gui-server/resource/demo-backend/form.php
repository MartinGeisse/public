<?php 

header('Content-Type: application/json');
if (empty($_POST['foo'])) {
	echo '{}';
} else {
	echo json_encode(array('redirectPage' => '/'));
}
