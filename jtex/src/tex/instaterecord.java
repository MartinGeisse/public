package tex;

public class instaterecord {
	public int statefield, indexfield;

	public int startfield, locfield, limitfield, namefield;

	public void copy(final instaterecord that) {
		this.statefield = that.statefield;
		this.indexfield = that.indexfield;
		this.startfield = that.startfield;
		this.locfield = that.locfield;
		this.limitfield = that.limitfield;
		this.namefield = that.namefield;
	}
}
