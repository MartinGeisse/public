module Test;

reg a,b;

initial begin
	$dumpfile("dump.vcd");
	$dumpvars(1);
	$dumpon;
	a = 0;
	t(7);
	#30 a = 1;
	t(3);
	#40 a = 0;
	t(5);
	#20 a = 1;
	#50 $finish;
end

task t;
	input [3:0] repeatCount;
	integer i;
	begin
		for (i=0; i<repeatCount; i=i+1) begin
			#5 b=1;
			#5 b=0;
		end
	end
endtask

endmodule
