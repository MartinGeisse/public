<?php

var_dump(1 << 32); // works as expected since we're in 64-bit land
var_dump(1 << 63); // gives a negative value, so the result is integer-only
var_dump(1 << 65); // right-hand operand wraps around, just as in Java
var_dump(1 << 63); // negative value again
var_dump(2 << 63); // 0 since the bit gets shifted out
var_dump(1.9 << 1); // left operand gets int-cast first, so this yields 2

// Conclusion: bit-shifting works on integer operands and integer results only,
// and just like in Java. Any float operand gets int-cast first.
