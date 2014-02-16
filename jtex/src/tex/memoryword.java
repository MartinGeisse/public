/*9:*/
/*2:*/
package tex;

import java.io.IOException;
import name.martingeisse.jtex.io.FileDataOutputStream;

public class memoryword {
	/*10:*/
	public final static int maxHalfword = 65535;

	int Int;

	public final void setInt(final int Int) {
		this.Int = Int;
	}

	public void copy(final memoryword m) {
		Int = m.Int;
	}

	public final void setlh(int lh) {
		lh &= 0xffff;
		Int &= 0x0000ffff;
		Int |= (lh << 16);
	}

	public final void setrh(int rh) {
		rh &= 0xffff;
		Int &= 0xffff0000;
		Int |= rh;
	}

	public final void setb0(int b) {
		b &= 0xff;
		Int &= 0x00ffffff;
		Int |= (b << 24);
	}

	public final void setb1(int b) {
		b &= 0xff;
		Int &= 0xff00ffff;
		Int |= (b << 16);
	}

	public final void setb2(int b) {
		b &= 0xff;
		Int &= 0xffff00ff;
		Int |= (b << 8);
	}

	public final void setb3(int b) {
		b &= 0xff;
		Int &= 0xffffff00;
		Int |= b;
	}

	public final void setglue(final double g) {
		Int = Float.floatToIntBits((float)g);
	}

	public final void sethh(final twohalves hh) {
		Int = (hh.lh << 16) | (hh.rh & 0xffff);
	}

	public final void setqqqq(final fourquarters qqqq) {
		Int = (qqqq.b0 << 24) | ((qqqq.b1 << 16) & 0x00ff0000) | ((qqqq.b2 << 8) & 0x0000ff00) | (qqqq.b3 & 0x000000ff);
	}

	public final int getInt() {
		return Int;
	}

	public final int getlh() {
		return (Int >>> 16);
	}

	public final int getrh() {
		return (Int & 0x0000ffff);
	}

	public final int getb0() {
		return (Int >>> 24);
	}

	public final int getb1() {
		return ((Int >>> 16) & 0xff);
	}

	public final int getb2() {
		return ((Int >>> 8) & 0xff);
	}

	public final int getb3() {
		return (Int & 0xff);
	}

	public final double getglue() {
		return Float.intBitsToFloat(Int);
	}

	public final twohalves hh() {
		final twohalves val = new twohalves();
		val.lh = Int >>> 16;
		val.rh = Int & 0xffff;
		return val;
	}

	public final fourquarters qqqq() {
		final fourquarters val = new fourquarters();
		val.b0 = Int >>> 24;
		val.b1 = (Int >>> 16) & 0xff;
		val.b2 = (Int >>> 8) & 0xff;
		val.b3 = Int & 0xff;
		return val;
	}

	public final void memdump(final FileDataOutputStream fmtfile) throws IOException {
		fmtfile.writeInt(Int);
	}

	public final void memundump(final wordfile fmtfile) throws IOException {
		this.Int = fmtfile.readInt();
	}/*:10*/

}/*:9*/
