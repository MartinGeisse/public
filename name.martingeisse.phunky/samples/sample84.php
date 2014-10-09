
<?php
$large_number = 9223372036854775807;
var_dump($large_number);                     // int(9223372036854775807)
echo $large_number, "\n";

$large_number = 9223372036854775808;
var_dump($large_number);                     // float(9.2233720368548E+18)
echo $large_number, "\n";

$million = 1000000;
$large_number =  50000000000000 * $million;
var_dump($large_number);                     // float(5.0E+19)
echo $large_number, "\n";

?>
