package tex;

public class liststaterecord {
	public int modefield;

	public int headfield, tailfield;

	public int pgfield, mlfield;

	public memoryword auxfield = new memoryword();

	public void copy(final liststaterecord that) {
		this.modefield = that.modefield;
		this.headfield = that.headfield;
		this.tailfield = that.tailfield;
		this.pgfield = that.pgfield;
		this.mlfield = that.mlfield;
		this.auxfield.copy(that.auxfield);
	}
}
