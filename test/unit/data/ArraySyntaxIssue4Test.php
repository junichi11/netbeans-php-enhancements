<?php
$query->selects=array("COUNT(DISTINCT $column)");
?>

// expected
<?php
$query->selects=["COUNT(DISTINCT $column)"];
?>