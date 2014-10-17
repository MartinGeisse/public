//
// toplevel.v -- ECO32 top-level description
//


module toplevel(
	clk_in,
	reset_in,
	vga_hsync,
	vga_vsync,
	vga_r,
	vga_g,
	vga_b
);
	// clock and reset
	input clk_in;
	input reset_in;
	
	// VGA display
	output vga_hsync;
	output vga_vsync;
	output vga_r;
	output vga_g;
	output vga_b;
	
	// ---------------------------------------------------------------------------

	// clock/reset signal handling
	wire clk;
	wire reset;
	
	// VGA display
	wire dsp_en;
	wire dsp_wr;
	wire [13:2] dsp_addr;
	wire [15:0] dsp_data_in;

	// clock/reset signal handling
	clk_rst clk_rst1(
		.clk_in(clk_in),
		.reset_in(reset_in),
		// .ddr_clk_0(ddr_clk_0),
		// .ddr_clk_90(ddr_clk_90),
		// .ddr_clk_180(ddr_clk_180),
		// .ddr_clk_270(ddr_clk_270),
		// .ddr_clk_ok(ddr_clk_ok),
		.clk(clk),
		.reset(reset)
	);

	// VGA display
	assign dsp_en = 0;
	assign dsp_wr = 0;
	assign dsp_addr = 0;
	assign dsp_data_in = 0;
	dsp dsp1(
		.clk(clk),
		.reset(reset),
		.en(dsp_en),
		.wr(dsp_wr),
		.addr(dsp_addr[13:2]),
		.data_in(dsp_data_in[15:0]),
		// .data_out(dsp_data_out[15:0]),
		// .wt(dsp_wt),
		.hsync(vga_hsync),
		.vsync(vga_vsync),
		.r(vga_r),
		.g(vga_g),
		.b(vga_b)
	);

endmodule
