package tex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import name.martingeisse.jtex.engine.DviWriter;
import name.martingeisse.jtex.engine.StringPool;
import name.martingeisse.jtex.error.ErrorReporter;
import name.martingeisse.jtex.error.ErrorReporter.Level;
import name.martingeisse.jtex.io.Input;
import name.martingeisse.jtex.io.TexFileDataInputStream;
import name.martingeisse.jtex.io.TexFileDataOutputStream;
import name.martingeisse.jtex.io.TexFilePrintWriter;
import name.martingeisse.jtex.parser.TexTokenizer;

/**
 * The complete TeX engine.
 */
@SuppressWarnings("javadoc")
public final class Tex {

	public static final int TOKENIZER_STATE_TOKEN_LIST = 0;

	public static final int TOKENIZER_STATE_NEW_LINE = 33;

	public static final int TOKENIZER_STATE_MID_LINE = 1;

	public static final int TOKENIZER_STATE_SKIP_BLANKS = 17;

	public static final int memmax = 65500;

	public static final int memtop = 65500;

	public static final int errorline = 72;

	public static final int halferrorline = 42;

	public static final int maxinopen = 6;

	public static final int fontmax = 75;

	public static final int fontmemsize = 50000;

	public static final int paramsize = 60;

	public static final int nestsize = 40;

	public static final int maxstrings = 6000;

	public static final int savesize = 1800;

	public static final int triesize = 24000;

	public static final int dvibufsize = 800;

	public static final int filenamesize = 80;

	// ------------------------------------------------------------

	/**
	 * the inputBuffer
	 */
	public final PrelimInputBuffer inputBuffer;

	public final boolean initex;

	public int bad;

	public String nameoffile;

	public File thisfile;

	public int namelength;

	public int buffer[] = new int[500000];

	public int first;

	public int last;

	public PrintWriter termout;

	public int strpool[] = new int[1024 * 1024];

	public int strstart[] = new int[maxstrings + 1];

	public int poolptr;

	public int strptr;

	public int initstrptr;

	public PrintWriter logfile;

	public ErrorReporter errorReporter;

	public ErrorLogic errorLogic;

	public int selector;

	public int dig[] = new int[23];

	public int tally;

	public int termoffset;

	public int fileoffset;

	public int trickbuf[] = new int[errorline + 1];

	public int trickcount;

	public int firstcount;

	public boolean setboxallowed;

	public int helpline[] = new int[6];

	public int helpptr;

	public boolean aritherror;

	public int remainder;

	public int tempptr;

	public memoryword mem[] = new memoryword[memmax + 1];

	public int lomemmax;

	public int himemmin;

	public int varused, dynused;

	public int avail;

	public int memend;

	public int rover;

	public boolean Free[] = new boolean[memmax + 1];

	public boolean wasfree[] = new boolean[memmax + 1];

	public int wasmemend, waslomax, washimin;

	public boolean panicking;

	public int fontinshortdisplay;

	public int depththreshold;

	public int breadthmax;

	public liststaterecord nest[] = new liststaterecord[nestsize + 1];

	public int nestptr;

	public int maxneststack;

	public liststaterecord curlist = new liststaterecord();

	public int shownmode;

	public int oldsetting;

	public memoryword eqtb[] = new memoryword[10407];

	public int xeqlevel[] = new int[844];

	public twohalves hash[] = new twohalves[6667];

	public int hashused;

	public boolean nonewcontrolsequence;

	public int cscount;

	public memoryword savestack[] = new memoryword[savesize + 1];

	public int saveptr;

	public int maxsavestack;

	public int curlevel;

	public int curgroup;

	public int curboundary;

	public int magset;

	public int curcmd;

	public int curchr;

	public int curcs;

	public int curtok;

	public TexTokenizer inputStackBackingArray[] = new TexTokenizer[InputStack.STACK_SIZE + 1];

	public int inputptr;

	public InputStack inputStack;

	public TexTokenizer curinput = new TexTokenizer();

	public int inopen;

	public int openparens;

	public Input inputfile[] = new Input[maxinopen + 1];

	public int line;

	public int linestack[] = new int[maxinopen + 1];

	public int scannerstatus;

	public int warningindex;

	public int defref;

	public int paramstack[] = new int[paramsize + 1];

	public int paramptr;

	public int maxparamstack;

	public int alignstate;

	public int baseptr;

	public int parloc;

	public int partoken;

	public boolean forceeof;

	public int curmark[] = new int[5];

	public int longstate;

	public int pstack[] = new int[9];

	public int curval;

	public int curvallevel;

	public int radix;

	public int curorder;

	public Input readfile[] = new Input[16];

	public int readopen[] = new int[17];

	public int condptr;

	public int iflimit;

	public int curif;

	public int ifline;

	public int skipline;

	public int curname;

	public int curarea;

	public int curext;

	public int areadelimiter;

	public int extdelimiter;

	public boolean nameinprogress;

	public int jobname;

	public TexFileDataOutputStream dvifile;

	public int outputfilename;

	public TexFileDataInputStream tfmfile;

	public memoryword fontinfo[] = new memoryword[fontmemsize + 1];

	public int fmemptr;

	public int fontptr;

	public fourquarters fontcheck[] = new fourquarters[fontmax + 1];

	public int fontsize[] = new int[fontmax + 1];

	public int fontdsize[] = new int[fontmax + 1];

	public int fontparams[] = new int[fontmax + 1];

	public int fontname[] = new int[fontmax + 1];

	public int fontarea[] = new int[fontmax + 1];

	public int fontbc[] = new int[fontmax + 1];

	public int fontec[] = new int[fontmax + 1];

	public int fontglue[] = new int[fontmax + 1];

	public boolean fontused[] = new boolean[fontmax + 1];

	public int hyphenchar[] = new int[fontmax + 1];

	public int skewchar[] = new int[fontmax + 1];

	public int bcharlabel[] = new int[fontmax + 1];

	public int fontbchar[] = new int[fontmax + 1];

	public int fontfalsebchar[] = new int[fontmax + 1];

	public int charbase[] = new int[fontmax + 1];

	public int widthbase[] = new int[fontmax + 1];

	public int heightbase[] = new int[fontmax + 1];

	public int depthbase[] = new int[fontmax + 1];

	public int italicbase[] = new int[fontmax + 1];

	public int ligkernbase[] = new int[fontmax + 1];

	public int kernbase[] = new int[fontmax + 1];

	public int extenbase[] = new int[fontmax + 1];

	public int parambase[] = new int[fontmax + 1];

	public fourquarters nullcharacter = new fourquarters();

	public int totalpages;

	public int maxv;

	public int maxh;

	public int maxpush;

	public int lastbop;

	public int deadcycles;

	public boolean doingleaders;

	public int f;

	public int ruleht, ruledp, rulewd;

	public int lq, lr;

	public int dvibuf[] = new int[dvibufsize + 1];

	public int halfbuf;

	public int dvilimit;

	public int dviptr;

	public int dvioffset;

	public int dvigone;

	public int downptr, rightptr;

	public int dvih, dviv;

	public int curh, curv;

	public int dvif;

	public int curs;

	public int totalstretch[] = new int[4], totalshrink[] = new int[4];

	public int lastbadness;

	public int adjusttail;

	public int packbeginline;

	public twohalves emptyfield = new twohalves();

	public fourquarters nulldelimiter = new fourquarters();

	public int curmlist;

	public int curstyle;

	public int cursize;

	public int curmu;

	public boolean mlistpenalties;

	public int curf;

	public int curc;

	public fourquarters curi = new fourquarters();

	public int magicoffset;

	public int curalign;

	public int curspan;

	public int curloop;

	public int alignptr;

	public int curhead, curtail;

	public int justbox;

	public int passive;

	public int activewidth[] = new int[7];

	public int curactivewidth[] = new int[7];

	public int background[] = new int[7];

	public int breakwidth[] = new int[7];

	public boolean noshrinkerroryet;

	public int curp;

	public boolean secondpass;

	public boolean finalpass;

	public int threshold;

	public int minimaldemerits[] = new int[4];

	public int minimumdemerits;

	public int bestplace[] = new int[4];

	public int bestplline[] = new int[4];

	public int discwidth;

	public int easyline;

	public int lastspecialline;

	public int firstwidth;

	public int secondwidth;

	public int firstindent;

	public int secondindent;

	public int bestbet;

	public int fewestdemerits;

	public int bestline;

	public int actuallooseness;

	public int linediff;

	public int hc[] = new int[66];

	public int hn;

	public int ha, hb;

	public int hf;

	public int hu[] = new int[64];

	public int hyfchar;

	public int curlang, initcurlang;

	public int lhyf, rhyf, initlhyf, initrhyf;

	public int hyfbchar;

	public int hyf[] = new int[65];

	public int initlist;

	public boolean initlig;

	public boolean initlft;

	public int hyphenpassed;

	public int curl, curr;

	public int curq;

	public int ligstack;

	public boolean ligaturepresent;

	public boolean lfthit, rthit;

	public twohalves trie[] = new twohalves[triesize + 1];

	public int hyfdistance[] = new int[752];

	public int hyfnum[] = new int[752];

	public int hyfnext[] = new int[752];

	public int opstart[] = new int[256];

	public int hyphword[] = new int[608];

	public int hyphlist[] = new int[608];

	public int hyphcount;

	public int trieophash[] = new int[1503];

	public int trieused[] = new int[256];

	public int trieoplang[] = new int[752];

	public int trieopval[] = new int[752];

	public int trieopptr;

	public int triec[] = new int[triesize + 1];

	public int trieo[] = new int[triesize + 1];

	public int triel[] = new int[triesize + 1];

	public int trier[] = new int[triesize + 1];

	public int trieptr;

	public int triehash[] = new int[triesize + 1];

	public boolean trietaken[] = new boolean[triesize + 1];

	public int triemin[] = new int[256];

	public int triemax;

	public boolean trienotready;

	public int bestheightplusdepth;

	public int pagetail;

	public int pagecontents;

	public int pagemaxdepth;

	public int bestpagebreak;

	public int leastpagecost;

	public int bestsize;

	public int pagesofar[] = new int[8];

	public int lastglue;

	public int lastpenalty;

	public int lastkern;

	public int insertpenalties;

	public boolean outputactive;

	public int mainf;

	public fourquarters maini = new fourquarters();

	public fourquarters mainj = new fourquarters();

	public int maink;

	public int mainp;

	public int mains;

	public int bchar;

	public int falsebchar;

	public boolean cancelboundary;

	public boolean insdisc;

	public int curbox;

	public int aftertoken;

	public boolean longhelpseen;

	public int formatident;

	public TexFileDataInputStream fmtfile;

	public PrintWriter writefile[] = new PrintWriter[16];

	public boolean writeopen[] = new boolean[18];

	public int writeloc;

	public StringBuffer cmdlinebuf = new StringBuffer();

	public int maxhalfword;

	public final StringPool stringPool = new StringPool(this);

	public final DviWriter dviWriter = new DviWriter(this);

	/**
	 * Constructor.
	 * @param initex whether this is INITEX
	 * @param args command-line arguments
	 */
	public Tex(final boolean initex, final String[] args) {
		this(initex);
		int i = 0;
		while (i < args.length) {
			if (i > 0) {
				cmdlinebuf.append(' ');
			}
			cmdlinebuf.append(args[i]);
			i = i + 1;
		}
	}

	/**
	 * Constructor.
	 * @param initex whether this is INITEX
	 */
	public Tex(final boolean initex) {
		this.initex = initex;
		this.inputBuffer = new PrelimInputBuffer(this);
		maxhalfword = memoryword.maxHalfword;
		for (int c = 0; c <= memmax; c++) {
			mem[c] = new memoryword();
		}
		for (int c = 1; c <= 10406; c++) {
			eqtb[c] = new memoryword();
		}
		for (int c = 0; c <= savesize; c++) {
			savestack[c] = new memoryword();
		}
		for (int c = 0; c <= fontmemsize; c++) {
			fontinfo[c] = new memoryword();
		}
		for (int c = 514; c <= 7180; c++) {
			hash[c - 514] = new twohalves();
		}
		for (int c = 0; c <= triesize; c++) {
			trie[c] = new twohalves();
		}
		for (int c = 0; c <= fontmax; c++) {
			fontcheck[c] = new fourquarters();
		}
		for (int c = 0; c <= nestsize; c++) {
			nest[c] = new liststaterecord();
		}
		for (int c = 0; c <= InputStack.STACK_SIZE; c++) {
			inputStackBackingArray[c] = new TexTokenizer();
		}
	}

	void initialize() {
		int k;
		int z;
		setboxallowed = true;
		helpptr = 0;
		wasmemend = 0;
		waslomax = 0;
		washimin = memmax;
		panicking = false;
		nestptr = 0;
		maxneststack = 0;
		curlist.modefield = 1;
		curlist.headfield = memtop - 1;
		curlist.tailfield = memtop - 1;
		curlist.auxfield.setInt(-65536000);
		curlist.mlfield = 0;
		curlist.pgfield = 0;
		shownmode = 0;
		pagecontents = 0;
		pagetail = memtop - 2;
		mem[memtop - 2].setrh(0);
		lastglue = maxhalfword;
		lastpenalty = 0;
		lastkern = 0;
		pagesofar[7] = 0;
		pagemaxdepth = 0;
		for (k = 9563; k <= 10406; k++) {
			xeqlevel[k - 9563] = 1;
		}
		nonewcontrolsequence = true;
		hash[514 - 514].lh = 0;
		hash[514 - 514].rh = 0;
		for (k = 515; k <= 7180; k++) {
			hash[k - 514].lh = hash[514 - 514].lh;
			hash[k - 514].rh = hash[514 - 514].rh;
		}
		saveptr = 0;
		curlevel = 1;
		curgroup = 0;
		curboundary = 0;
		maxsavestack = 0;
		magset = 0;
		curmark[0] = 0;
		curmark[1] = 0;
		curmark[2] = 0;
		curmark[3] = 0;
		curmark[4] = 0;
		curval = 0;
		curvallevel = 0;
		radix = 0;
		curorder = 0;
		for (k = 0; k <= 16; k++) {
			readopen[k] = 2;
		}
		condptr = 0;
		iflimit = 0;
		curif = 0;
		ifline = 0;
		for (k = 0; k <= fontmax; k++) {
			fontused[k] = false;
		}
		nullcharacter.b0 = 0;
		nullcharacter.b1 = 0;
		nullcharacter.b2 = 0;
		nullcharacter.b3 = 0;
		totalpages = 0;
		maxv = 0;
		maxh = 0;
		maxpush = 0;
		lastbop = -1;
		doingleaders = false;
		deadcycles = 0;
		curs = -1;
		halfbuf = dvibufsize / 2;
		dvilimit = dvibufsize;
		dviptr = 0;
		dvioffset = 0;
		dvigone = 0;
		downptr = 0;
		rightptr = 0;
		adjusttail = 0;
		lastbadness = 0;
		packbeginline = 0;
		emptyfield.rh = 0;
		emptyfield.lh = 0;
		nulldelimiter.b0 = 0;
		nulldelimiter.b1 = 0;
		nulldelimiter.b2 = 0;
		nulldelimiter.b3 = 0;
		alignptr = 0;
		curalign = 0;
		curspan = 0;
		curloop = 0;
		curhead = 0;
		curtail = 0;
		for (z = 0; z <= 607; z++) {
			hyphword[z] = 0;
			hyphlist[z] = 0;
		}
		hyphcount = 0;
		outputactive = false;
		insertpenalties = 0;
		ligaturepresent = false;
		cancelboundary = false;
		lfthit = false;
		rthit = false;
		insdisc = false;
		aftertoken = 0;
		longhelpseen = false;
		formatident = 0;
		for (k = 0; k <= 17; k++) {
			writeopen[k] = false;
		}
		if (initex) {
			for (k = 1; k <= 19; k++) {
				mem[k].setInt(0);
			}
			k = 0;
			while (k <= 19) {
				mem[k].setrh(1);
				mem[k].setb0(0);
				mem[k].setb1(0);
				k = k + 4;
			}
			mem[6].setInt(65536);
			mem[4].setb0(1);
			mem[10].setInt(65536);
			mem[8].setb0(2);
			mem[14].setInt(65536);
			mem[12].setb0(1);
			mem[15].setInt(65536);
			mem[12].setb1(1);
			mem[18].setInt(-65536);
			mem[16].setb0(1);
			rover = 20;
			mem[rover].setrh(maxhalfword);
			mem[rover].setlh(1000);
			mem[rover + 1].setlh(rover);
			mem[rover + 1].setrh(rover);
			lomemmax = rover + 1000;
			mem[lomemmax].setrh(0);
			mem[lomemmax].setlh(0);
			for (k = memtop - 13; k <= memtop; k++) {
				mem[k].setInt(mem[lomemmax].getInt());
			}
			mem[memtop - 10].setlh(11014);
			mem[memtop - 9].setrh(256);
			mem[memtop - 9].setlh(0);
			mem[memtop - 7].setb0(1);
			mem[memtop - 6].setlh(maxhalfword);
			mem[memtop - 7].setb1(0);
			mem[memtop].setb1(255);
			mem[memtop].setb0(1);
			mem[memtop].setrh(memtop);
			mem[memtop - 2].setb0(10);
			mem[memtop - 2].setb1(0);
			avail = 0;
			memend = memtop;
			himemmin = memtop - 13;
			varused = 20;
			dynused = 14;
			eqtb[7181].setb0(101);
			eqtb[7181].setrh(0);
			eqtb[7181].setb1(0);
			for (k = 1; k <= 7180; k++) {
				eqtb[k].copy(eqtb[7181]);
			}
			eqtb[7182].setrh(0);
			eqtb[7182].setb1(1);
			eqtb[7182].setb0(117);
			for (k = 7183; k <= 7711; k++) {
				eqtb[k].copy(eqtb[7182]);
			}
			mem[0].setrh(mem[0].getrh() + 530);
			eqtb[7712].setrh(0);
			eqtb[7712].setb0(118);
			eqtb[7712].setb1(1);
			for (k = 7713; k <= 7977; k++) {
				eqtb[k].copy(eqtb[7181]);
			}
			eqtb[7978].setrh(0);
			eqtb[7978].setb0(119);
			eqtb[7978].setb1(1);
			for (k = 7979; k <= 8233; k++) {
				eqtb[k].copy(eqtb[7978]);
			}
			eqtb[8234].setrh(0);
			eqtb[8234].setb0(120);
			eqtb[8234].setb1(1);
			for (k = 8235; k <= 8282; k++) {
				eqtb[k].copy(eqtb[8234]);
			}
			eqtb[8283].setrh(0);
			eqtb[8283].setb0(120);
			eqtb[8283].setb1(1);
			for (k = 8284; k <= 9562; k++) {
				eqtb[k].copy(eqtb[8283]);
			}
			for (k = 0; k <= 255; k++) {
				eqtb[8283 + k].setrh(12);
				eqtb[9307 + k].setrh(k);
				eqtb[9051 + k].setrh(1000);
			}
			eqtb[8296].setrh(5);
			eqtb[8315].setrh(10);
			eqtb[8375].setrh(0);
			eqtb[8320].setrh(14);
			eqtb[8410].setrh(15);
			eqtb[8283].setrh(9);
			for (k = 48; k <= 57; k++) {
				eqtb[9307 + k].setrh(k + 28672);
			}
			for (k = 65; k <= 90; k++) {
				eqtb[8283 + k].setrh(11);
				eqtb[8283 + k + 32].setrh(11);
				eqtb[9307 + k].setrh(k + 28928);
				eqtb[9307 + k + 32].setrh(k + 28960);
				eqtb[8539 + k].setrh(k + 32);
				eqtb[8539 + k + 32].setrh(k + 32);
				eqtb[8795 + k].setrh(k);
				eqtb[8795 + k + 32].setrh(k);
				eqtb[9051 + k].setrh(999);
			}
			for (k = 9563; k <= 9873; k++) {
				eqtb[k].setInt(0);
			}
			eqtb[9580].setInt(1000);
			eqtb[9564].setInt(10000);
			eqtb[9604].setInt(1);
			eqtb[9603].setInt(25);
			eqtb[9608].setInt(92);
			eqtb[9611].setInt(13);
			for (k = 0; k <= 255; k++) {
				eqtb[9874 + k].setInt(-1);
			}
			eqtb[9920].setInt(0);
			for (k = 10130; k <= 10406; k++) {
				eqtb[k].setInt(0);
			}
			hashused = 6914;
			cscount = 0;
			eqtb[6923].setb0(116);
			hash[6923 - 514].rh = 502;
			fontptr = 0;
			fmemptr = 7;
			fontname[0] = 801;
			fontarea[0] = 338;
			hyphenchar[0] = 45;
			skewchar[0] = -1;
			bcharlabel[0] = 0;
			fontbchar[0] = 256;
			fontfalsebchar[0] = 256;
			fontbc[0] = 1;
			fontec[0] = 0;
			fontsize[0] = 0;
			fontdsize[0] = 0;
			charbase[0] = 0;
			widthbase[0] = 0;
			heightbase[0] = 0;
			depthbase[0] = 0;
			italicbase[0] = 0;
			ligkernbase[0] = 0;
			kernbase[0] = 0;
			extenbase[0] = 0;
			fontglue[0] = 0;
			fontparams[0] = 7;
			parambase[0] = -1;
			for (k = 0; k <= 6; k++) {
				fontinfo[k].setInt(0);
			}
			for (k = -751; k <= 751; k++) {
				trieophash[k + 751] = 0;
			}
			for (k = 0; k <= 255; k++) {
				trieused[k] = 0;
			}
			trieopptr = 0;
			trienotready = true;
			triel[0] = 0;
			triec[0] = 0;
			trieptr = 0;
			hash[6914 - 514].rh = 1190;
			formatident = 1257;
			hash[6922 - 514].rh = 1296;
			eqtb[6922].setb1(1);
			eqtb[6922].setb0(113);
			eqtb[6922].setrh(0);
		}
	}

	boolean inputln(final Input f, final boolean bypasseoln) {
		if (bypasseoln) {
			f.readCharacter();
		}
		return inputBuffer.appendRightTrimmedLineFromFile(f);
	}

	boolean initterminal() {
		Input termin;
		inputBuffer.appendLine(cmdlinebuf);
		termin = Input.from("(System.in)", new InputStreamReader(System.in));
		if (!inputBuffer.isEmpty()) {
			curinput.setLoc(first);
			while ((curinput.getLoc() < last) && (buffer[curinput.getLoc()] == ' ')) {
				curinput.setLoc(curinput.getLoc() + 1);
			}
			if (curinput.getLoc() < last) {
				return true;
			}
		}
		while (true) {
			termout.print("**");
			termout.flush();
			if (!inputln(termin, true)) {
				termout.print('\n');
				termout.print("! End of file on the terminal... why?");
				return false;
			}
			curinput.setLoc(first);
			while ((curinput.getLoc() < last) && (buffer[curinput.getLoc()] == 32)) {
				curinput.setLoc(curinput.getLoc() + 1);
			}
			if (curinput.getLoc() < last) {
				return true;
			}
			termout.print("Please type the name of your input file." + '\n');
		}
	}

	boolean privileged() {
		final boolean ok = (curlist.modefield > 0);
		if (!ok) {
			errorLogic.reportillegalcase();
		}
		return ok;
	}

	/**
	 * Tests if the specified string is found in the buffer at the specified position.
	 *
	 * As a special rule, this function returns false if the string to look for is empty,
	 * even though the empty string can be found anywhere.
	 *
	 * @param stringId the ID of the string to look for
	 * @param bufferStart the bufer position to look at
	 * @return whether the string was found at that position
	 */
	boolean streqbuf(final int stringId, final int bufferStart) {
		final String s = stringPool.getString(stringId);
		if (s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != buffer[bufferStart + i]) {
				return false;
			}
		}
		return true;
	}

	void getstringsstarted() {
		stringPool.reset();
		stringPool.createCharacterStrings();
		nameoffile = "tex.pool";
		thisfile = new File(nameoffile);
		stringPool.loadPoolFile(thisfile);
	}

	void normalizeselector() {
		selector = 19;
	}

	int half(final int x) {
		int Result;
		if (((x) % 2 == 1)) {
			Result = (x + 1) / 2;
		} else {
			Result = x / 2;
		}
		return Result;
	}

	int rounddecimals(int k) {
		int Result;
		int a;
		a = 0;
		while (k > 0) {
			k = k - 1;
			a = (a + dig[k] * 131072) / 10;
		}
		Result = (a + 1) / 2;
		return Result;
	}

	int multandadd(int n, int x, final int y, final int maxanswer) {
		int Result;
		if (n < 0) {
			x = -x;
			n = -n;
		}
		if (n == 0) {
			Result = y;
		} else if (((x <= (maxanswer - y) / n) && (-x <= (maxanswer + y) / n))) {
			Result = n * x + y;
		} else {
			aritherror = true;
			Result = 0;
		}
		return Result;
	}

	int xovern(int x, int n) {
		int Result;
		boolean negative;
		negative = false;
		if (n == 0) {
			aritherror = true;
			Result = 0;
			remainder = x;
		} else {
			if (n < 0) {
				x = -x;
				n = -n;
				negative = true;
			}
			if (x >= 0) {
				Result = x / n;
				remainder = x % n;
			} else {
				Result = -((-x) / n);
				remainder = -((-x) % n);
			}
		}
		if (negative) {
			remainder = -remainder;
		}
		return Result;
	}

	int xnoverd(int x, final int n, final int d) {
		int Result;
		boolean positive;
		int t, u, v;
		if (x >= 0) {
			positive = true;
		} else {
			x = -x;
			positive = false;
		}
		t = (x % 32768) * n;
		u = (x / 32768) * n + (t / 32768);
		v = (u % d) * 32768 + (t % 32768);
		if (u / d >= 32768) {
			aritherror = true;
		} else {
			u = 32768 * (u / d) + (v / d);
		}
		if (positive) {
			Result = u;
			remainder = v % d;
		} else {
			Result = -u;
			remainder = -(v % d);
		}
		return Result;
	}

	int badness(final int t, final int s) {
		int Result;
		int r;
		if (t == 0) {
			Result = 0;
		} else if (s <= 0) {
			Result = 10000;
		} else {
			if (t <= 7230584) {
				r = (t * 297) / s;
			} else if (s >= 1663497) {
				r = t / (s / 297);
			} else {
				r = t;
			}
			if (r > 1290) {
				Result = 10000;
			} else {
				Result = (r * r * r + 131072) / 262144;
			}
		}
		return Result;
	}

	int allocateMemoryWord() {
		int Result;
		int p;
		p = avail;
		if (p != 0) {
			avail = mem[avail].getrh();
		} else if (memend < memmax) {
			memend = memend + 1;
			p = memend;
		} else {
			himemmin = himemmin - 1;
			p = himemmin;
			if (himemmin <= lomemmax) {
				runaway();
				errorLogic.overflow(300, memmax + 1);
			}
		}
		mem[p].setrh(0);
		Result = p;
		return Result;
	}

	void flushlist(final int p) {
		int q, r;
		if (p != 0) {
			r = p;
			do {
				q = r;
				r = mem[r].getrh();
			} while (!(r == 0));
			mem[q].setrh(avail);
			avail = p;
		}
	}

	int getnode(final int s) {
		/* 40 10 20 */int Result;
		int p;
		int q;
		int r;
		int t;
		lab20: while (true) {
			p = rover;
			lab40: while (true) {
				do {
					q = p + mem[p].getlh();
					while ((mem[q].getrh() == maxhalfword)) {
						t = mem[q + 1].getrh();
						if (q == rover) {
							rover = t;
						}
						mem[t + 1].setlh(mem[q + 1].getlh());
						mem[mem[q + 1].getlh() + 1].setrh(t);
						q = q + mem[q].getlh();
					}
					r = q - s;
					if (r > p + 1) {
						mem[p].setlh(r - p);
						rover = p;
						break lab40;
					}
					if (r == p) {
						if (mem[p + 1].getrh() != p) {
							rover = mem[p + 1].getrh();
							t = mem[p + 1].getlh();
							mem[rover + 1].setlh(t);
							mem[t + 1].setrh(rover);
							break lab40;
						}
					}
					mem[p].setlh(q - p);
					p = mem[p + 1].getrh();
				} while (!(p == rover));
				if (s == 1073741824) {
					Result = maxhalfword;
					return Result /* lab10 */;
				}
				if (lomemmax + 2 < himemmin) {
					if (lomemmax + 2 <= 0 + maxhalfword) {
						if (himemmin - lomemmax >= 1998) {
							t = lomemmax + 1000;
						} else {
							t = lomemmax + 1 + (himemmin - lomemmax) / 2;
						}
						p = mem[rover + 1].getlh();
						q = lomemmax;
						mem[p + 1].setrh(q);
						mem[rover + 1].setlh(q);
						if (t > 0 + maxhalfword) {
							t = 0 + maxhalfword;
						}
						mem[q + 1].setrh(rover);
						mem[q + 1].setlh(p);
						mem[q].setrh(maxhalfword);
						mem[q].setlh(t - lomemmax);
						lomemmax = t;
						mem[lomemmax].setrh(0);
						mem[lomemmax].setlh(0);
						rover = q;
						continue lab20;
					}
				}
				errorLogic.overflow(300, memmax + 1);
				break;
			}
			/* lab40: */mem[r].setrh(0);
			break;
		}
		Result = r;
		return Result;
	}

	void freenode(final int p, final int s) {
		int q;
		mem[p].setlh(s);
		mem[p].setrh(maxhalfword);
		q = mem[rover + 1].getlh();
		mem[p + 1].setlh(q);
		mem[p + 1].setrh(rover);
		mem[rover + 1].setlh(p);
		mem[q + 1].setrh(p);
	}

	void sortavail() {
		int p, q, r;
		int oldrover;
		p = getnode(1073741824);
		p = mem[rover + 1].getrh();
		mem[rover + 1].setrh(maxhalfword);
		oldrover = rover;
		while (p != oldrover) {
			if (p < rover) {
				q = p;
				p = mem[q + 1].getrh();
				mem[q + 1].setrh(rover);
				rover = q;
			} else {
				q = rover;
				while (mem[q + 1].getrh() < p) {
					q = mem[q + 1].getrh();
				}
				r = mem[p + 1].getrh();
				mem[p + 1].setrh(mem[q + 1].getrh());
				mem[q + 1].setrh(p);
				p = r;
			}
		}
		p = rover;
		while (mem[p + 1].getrh() != maxhalfword) {
			mem[mem[p + 1].getrh() + 1].setlh(p);
			p = mem[p + 1].getrh();
		}
		mem[p + 1].setrh(rover);
		mem[rover + 1].setlh(p);
	}

	int newnullbox() {
		int Result;
		int p;
		p = getnode(7);
		mem[p].setb0(0);
		mem[p].setb1(0);
		mem[p + 1].setInt(0);
		mem[p + 2].setInt(0);
		mem[p + 3].setInt(0);
		mem[p + 4].setInt(0);
		mem[p + 5].setrh(0);
		mem[p + 5].setb0(0);
		mem[p + 5].setb1(0);
		mem[p + 6].setglue(0.0);
		Result = p;
		return Result;
	}

	int newrule() {
		int Result;
		int p;
		p = getnode(4);
		mem[p].setb0(2);
		mem[p].setb1(0);
		mem[p + 1].setInt(-1073741824);
		mem[p + 2].setInt(-1073741824);
		mem[p + 3].setInt(-1073741824);
		Result = p;
		return Result;
	}

	int newligature(final int f, final int c, final int q) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(6);
		mem[p + 1].setb0(f);
		mem[p + 1].setb1(c);
		mem[p + 1].setrh(q);
		mem[p].setb1(0);
		Result = p;
		return Result;
	}

	int newligitem(final int c) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb1(c);
		mem[p + 1].setrh(0);
		Result = p;
		return Result;
	}

	int newdisc() {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(7);
		mem[p].setb1(0);
		mem[p + 1].setlh(0);
		mem[p + 1].setrh(0);
		Result = p;
		return Result;
	}

	int newmath(final int w, final int s) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(9);
		mem[p].setb1(s);
		mem[p + 1].setInt(w);
		Result = p;
		return Result;
	}

	int newspec(final int p) {
		int Result;
		int q;
		q = getnode(4);
		mem[q].setInt(mem[p].getInt());
		mem[q].setrh(0);
		mem[q + 1].setInt(mem[p + 1].getInt());
		mem[q + 2].setInt(mem[p + 2].getInt());
		mem[q + 3].setInt(mem[p + 3].getInt());
		Result = q;
		return Result;
	}

	int newparamglue(final int n) {
		int Result;
		int p;
		int q;
		p = getnode(2);
		mem[p].setb0(10);
		mem[p].setb1(n + 1);
		mem[p + 1].setrh(0);
		q = eqtb[7182 + n].getrh();
		mem[p + 1].setlh(q);
		mem[q].setrh(mem[q].getrh() + 1);
		Result = p;
		return Result;
	}

	int newglue(final int q) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(10);
		mem[p].setb1(0);
		mem[p + 1].setrh(0);
		mem[p + 1].setlh(q);
		mem[q].setrh(mem[q].getrh() + 1);
		Result = p;
		return Result;
	}

	int newskipparam(final int n) {
		int Result;
		int p;
		tempptr = newspec(eqtb[7182 + n].getrh());
		p = newglue(tempptr);
		mem[tempptr].setrh(0);
		mem[p].setb1(n + 1);
		Result = p;
		return Result;
	}

	int newkern(final int w) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(11);
		mem[p].setb1(0);
		mem[p + 1].setInt(w);
		Result = p;
		return Result;
	}

	int newpenalty(final int m) {
		int Result;
		int p;
		p = getnode(2);
		mem[p].setb0(12);
		mem[p].setb1(0);
		mem[p + 1].setInt(m);
		Result = p;
		return Result;
	}

	void checkmem(final boolean printlocs) {
		/* 31 32 */int p, q;
		boolean clobbered;
		for (p = 0; p <= lomemmax; p++) {
			Free[p] = false;
		}
		for (p = himemmin; p <= memend; p++) {
			Free[p] = false;
		}
		p = avail;
		q = 0;
		clobbered = false;
		while (p != 0) {
			if ((p > memend) || (p < himemmin)) {
				clobbered = true;
			} else if (Free[p]) {
				clobbered = true;
			}
			if (clobbered) {
				printnl(301);
				printInt(q);
				break /* lab31 */;
			}
			Free[p] = true;
			q = p;
			p = mem[q].getrh();
		}
		/* lab31: */p = rover;
		q = 0;
		clobbered = false;
		do {
			if ((p >= lomemmax) || (p < 0)) {
				clobbered = true;
			} else if ((mem[p + 1].getrh() >= lomemmax) || (mem[p + 1].getrh() < 0)) {
				clobbered = true;
			} else if (!((mem[p].getrh() == maxhalfword)) || (mem[p].getlh() < 2) || (p + mem[p].getlh() > lomemmax) || (mem[mem[p + 1].getrh() + 1].getlh() != p)) {
				clobbered = true;
			}
			if (clobbered) {
				printnl(302);
				printInt(q);
				break /* lab32 */;
			}
			for (q = p; q <= p + mem[p].getlh() - 1; q++) {
				if (Free[q]) {
					printnl(303);
					printInt(q);
					break /* lab32 */;
				}
				Free[q] = true;
			}
			q = p;
			p = mem[p + 1].getrh();
		} while (!(p == rover));
		/* lab32: */p = 0;
		while (p <= lomemmax) {
			if ((mem[p].getrh() == maxhalfword)) {
				printnl(304);
				printInt(p);
			}
			while ((p <= lomemmax) && !Free[p]) {
				p = p + 1;
			}
			while ((p <= lomemmax) && Free[p]) {
				p = p + 1;
			}
		}
		if (printlocs) {
			printnl(305);
			for (p = 0; p <= lomemmax; p++) {
				if (!Free[p] && ((p > waslomax) || wasfree[p])) {
					printchar(32);
					printInt(p);
				}
			}
			for (p = himemmin; p <= memend; p++) {
				if (!Free[p] && ((p < washimin) || (p > wasmemend) || wasfree[p])) {
					printchar(32);
					printInt(p);
				}
			}
		}
		for (p = 0; p <= lomemmax; p++) {
			wasfree[p] = Free[p];
		}
		for (p = himemmin; p <= memend; p++) {
			wasfree[p] = Free[p];
		}
		wasmemend = memend;
		waslomax = lomemmax;
		washimin = himemmin;
	}

	void shortdisplay(int p) {
		int n;
		while (p > 0) {
			if ((p >= himemmin)) {
				if (p <= memend) {
					if (mem[p].getb0() != fontinshortdisplay) {
						if ((mem[p].getb0() < 0) || (mem[p].getb0() > fontmax)) {
							printchar(42);
						} else {
							printEscapeSequence(hash[6924 + mem[p].getb0() - 514].rh);
						}
						printchar(32);
						fontinshortdisplay = mem[p].getb0();
					}
					print(mem[p].getb1());
				}
			} else {
				switch (mem[p].getb0()) {
					case 0:
					case 1:
					case 3:
					case 8:
					case 4:
					case 5:
					case 13:
						print(308);
						break;
					case 2:
						printchar(124);
						break;
					case 10:
						if (mem[p + 1].getlh() != 0) {
							printchar(32);
						}
						break;
					case 9:
						printchar(36);
						break;
					case 6:
						shortdisplay(mem[p + 1].getrh());
						break;
					case 7: {
						shortdisplay(mem[p + 1].getlh());
						shortdisplay(mem[p + 1].getrh());
						n = mem[p].getb1();
						while (n > 0) {
							if (mem[p].getrh() != 0) {
								p = mem[p].getrh();
							}
							n = n - 1;
						}
					}
						break;
					default:
						;
						break;
				}
			}
			p = mem[p].getrh();
		}
	}

	void deletetokenref(final int p) {
		if (mem[p].getlh() == 0) {
			flushlist(p);
		} else {
			mem[p].setlh(mem[p].getlh() - 1);
		}
	}

	void deleteglueref(final int p) {
		if (mem[p].getrh() == 0) {
			freenode(p, 4);
		} else {
			mem[p].setrh(mem[p].getrh() - 1);
		}
	}

	void flushnodelist(int p) {
		/* 30 */int q;
		while (p != 0) {
			q = mem[p].getrh();
			if ((p >= himemmin)) {
				mem[p].setrh(avail);
				avail = p;
			} else {
				lab30: while (true) {
					switch (mem[p].getb0()) {
						case 0:
						case 1:
						case 13: {
							flushnodelist(mem[p + 5].getrh());
							freenode(p, 7);
							break lab30;
						}
						case 2: {
							freenode(p, 4);
							break lab30;
						}
						case 3: {
							flushnodelist(mem[p + 4].getlh());
							deleteglueref(mem[p + 4].getrh());
							freenode(p, 5);
							break lab30;
						}
						case 8: {
							switch (mem[p].getb1()) {
								case 0:
									freenode(p, 3);
									break;
								case 1:
								case 3: {
									deletetokenref(mem[p + 1].getrh());
									freenode(p, 2);
									break lab30;
								}
								case 2:
								case 4:
									freenode(p, 2);
									break;
								default:
									errorLogic.confusion(1295);
									break;
							}
							break lab30;
						}
						case 10: {
							{
								if (mem[mem[p + 1].getlh()].getrh() == 0) {
									freenode(mem[p + 1].getlh(), 4);
								} else {
									mem[mem[p + 1].getlh()].setrh(mem[mem[p + 1].getlh()].getrh() - 1);
								}
							}
							if (mem[p + 1].getrh() != 0) {
								flushnodelist(mem[p + 1].getrh());
							}
						}
							break;
						case 11:
						case 9:
						case 12:
							;
							break;
						case 6:
							flushnodelist(mem[p + 1].getrh());
							break;
						case 4:
							deletetokenref(mem[p + 1].getInt());
							break;
						case 7: {
							flushnodelist(mem[p + 1].getlh());
							flushnodelist(mem[p + 1].getrh());
						}
							break;
						case 5:
							flushnodelist(mem[p + 1].getInt());
							break;
						case 14: {
							freenode(p, 3);
							break lab30;
						}
						case 15: {
							flushnodelist(mem[p + 1].getlh());
							flushnodelist(mem[p + 1].getrh());
							flushnodelist(mem[p + 2].getlh());
							flushnodelist(mem[p + 2].getrh());
							freenode(p, 3);
							break lab30;
						}
						case 16:
						case 17:
						case 18:
						case 19:
						case 20:
						case 21:
						case 22:
						case 23:
						case 24:
						case 27:
						case 26:
						case 29:
						case 28: {
							if (mem[p + 1].getrh() >= 2) {
								flushnodelist(mem[p + 1].getlh());
							}
							if (mem[p + 2].getrh() >= 2) {
								flushnodelist(mem[p + 2].getlh());
							}
							if (mem[p + 3].getrh() >= 2) {
								flushnodelist(mem[p + 3].getlh());
							}
							if (mem[p].getb0() == 24) {
								freenode(p, 5);
							} else if (mem[p].getb0() == 28) {
								freenode(p, 5);
							} else {
								freenode(p, 4);
							}
							break lab30;
						}
						case 30:
						case 31: {
							freenode(p, 4);
							break lab30;
						}
						case 25: {
							flushnodelist(mem[p + 2].getlh());
							flushnodelist(mem[p + 3].getlh());
							freenode(p, 6);
							break lab30;
						}
						default:
							errorLogic.confusion(353);
							break;
					}
					freenode(p, 2);
					break;
				}
				/* lab30: */}
			p = q;
		}
	}

	int copynodelist(int p) {
		int Result;
		int h;
		int q;
		int r;
		int words;
		r = 0;
		h = allocateMemoryWord();
		q = h;
		while (p != 0) {
			words = 1;
			if ((p >= himemmin)) {
				r = allocateMemoryWord();
			} else {
				switch (mem[p].getb0()) {
					case 0:
					case 1:
					case 13: {
						r = getnode(7);
						mem[r + 6].copy(mem[p + 6]);
						mem[r + 5].copy(mem[p + 5]);
						mem[r + 5].setrh(copynodelist(mem[p + 5].getrh()));
						words = 5;
					}
						break;
					case 2: {
						r = getnode(4);
						words = 4;
					}
						break;
					case 3: {
						r = getnode(5);
						mem[r + 4].copy(mem[p + 4]);
						mem[mem[p + 4].getrh()].setrh(mem[mem[p + 4].getrh()].getrh() + 1);
						mem[r + 4].setlh(copynodelist(mem[p + 4].getlh()));
						words = 4;
					}
						break;
					case 8:
						switch (mem[p].getb1()) {
							case 0: {
								r = getnode(3);
								words = 3;
							}
								break;
							case 1:
							case 3: {
								r = getnode(2);
								mem[mem[p + 1].getrh()].setlh(mem[mem[p + 1].getrh()].getlh() + 1);
								words = 2;
							}
								break;
							case 2:
							case 4: {
								r = getnode(2);
								words = 2;
							}
								break;
							default:
								errorLogic.confusion(1294);
								break;
						}
						break;
					case 10: {
						r = getnode(2);
						mem[mem[p + 1].getlh()].setrh(mem[mem[p + 1].getlh()].getrh() + 1);
						mem[r + 1].setlh(mem[p + 1].getlh());
						mem[r + 1].setrh(copynodelist(mem[p + 1].getrh()));
					}
						break;
					case 11:
					case 9:
					case 12: {
						r = getnode(2);
						words = 2;
					}
						break;
					case 6: {
						r = getnode(2);
						mem[r + 1].copy(mem[p + 1]);
						mem[r + 1].setrh(copynodelist(mem[p + 1].getrh()));
					}
						break;
					case 7: {
						r = getnode(2);
						mem[r + 1].setlh(copynodelist(mem[p + 1].getlh()));
						mem[r + 1].setrh(copynodelist(mem[p + 1].getrh()));
					}
						break;
					case 4: {
						r = getnode(2);
						mem[mem[p + 1].getInt()].setlh(mem[mem[p + 1].getInt()].getlh() + 1);
						words = 2;
					}
						break;
					case 5: {
						r = getnode(2);
						mem[r + 1].setInt(copynodelist(mem[p + 1].getInt()));
					}
						break;
					default:
						errorLogic.confusion(354);
						break;
				}
			}
			while (words > 0) {
				words = words - 1;
				mem[r + words].copy(mem[p + words]);
			}
			mem[q].setrh(r);
			q = r;
			p = mem[p].getrh();
		}
		mem[q].setrh(0);
		q = mem[h].getrh();
		{
			mem[h].setrh(avail);
			avail = h;
		}
		Result = q;
		return Result;
	}

	void pushnest() {
		if (nestptr > maxneststack) {
			maxneststack = nestptr;
			if (nestptr == nestsize) {
				errorLogic.overflow(362, nestsize);
			}
		}
		nest[nestptr].copy(curlist);
		nestptr = nestptr + 1;
		curlist.headfield = allocateMemoryWord();
		curlist.tailfield = curlist.headfield;
		curlist.pgfield = 0;
		curlist.mlfield = line;
	}

	void popnest() {
		mem[curlist.headfield].setrh(avail);
		avail = curlist.headfield;
		nestptr = nestptr - 1;
		curlist.copy(nest[nestptr]);
	}

	/**
	 * Sets a dummy date/time for initialization (July 4th, 1776).
	 */
	void setDummyDateTime() {
		eqtb[9583].setInt(12 * 60);
		eqtb[9584].setInt(4);
		eqtb[9585].setInt(7);
		eqtb[9586].setInt(1776);
	}

	int idlookup(final int j, final int l) {
		/* 40 */int Result;
		int h;
		int d;
		int p;
		int k;
		h = buffer[j];
		for (k = j + 1; k <= j + l - 1; k++) {
			h = h + h + buffer[k];
			while (h >= 5437) {
				h = h - 5437;
			}
		}
		p = h + 514;
		lab40: while (true) {
			if (hash[p - 514].rh > 0) {
				if ((strstart[hash[p - 514].rh + 1] - strstart[hash[p - 514].rh]) == l) {
					if (streqbuf(hash[p - 514].rh, j)) {
						break lab40;
					}
				}
			}
			if (hash[p - 514].lh == 0) {
				if (nonewcontrolsequence) {
					p = 7181;
				} else {
					if (hash[p - 514].rh > 0) {
						do {
							if ((hashused == 514)) {
								errorLogic.overflow(503, 6400);
							}
							hashused = hashused - 1;
						} while (!(hash[hashused - 514].rh == 0));
						hash[p - 514].lh = hashused;
						p = hashused;
					}

					/*
					 * This code is probably less weird than it looks. It just wants to
					 * build a string in the string pool but preserve whatever partially
					 * built string was already there.
					 */
					d = (poolptr - strstart[strptr]);
					while (poolptr > strstart[strptr]) {
						poolptr = poolptr - 1;
						strpool[poolptr + l] = strpool[poolptr];
					}
					for (k = j; k <= j + l - 1; k++) {
						strpool[poolptr] = buffer[k];
						poolptr = poolptr + 1;
					}
					hash[p - 514].rh = stringPool.makeString();
					poolptr = poolptr + d;

				}
				break lab40;
			}
			p = hash[p - 514].lh;
		}
		/* lab40: */Result = p;
		return Result;
	}

	void primitive(final int s, final int c, final int o) {
		if (s < 256) {
			curval = s + 257;
		} else {
			final String string = stringPool.getString(s);
			inputBuffer.copyToInternalBuffer(string, 0);
			curval = idlookup(0, string.length());

			// this looks weird. It removes the last-added string.
			// I should have a look if this is always (s) or can be different.
			strptr = strptr - 1;
			poolptr = strstart[strptr];

			hash[curval - 514].rh = s;
		}
		eqtb[curval].setb1(1);
		eqtb[curval].setb0(c);
		eqtb[curval].setrh(o);
	}

	void newsavelevel(final int c) {
		if (saveptr > maxsavestack) {
			maxsavestack = saveptr;
			if (maxsavestack > savesize - 6) {
				errorLogic.overflow(541, savesize);
			}
		}
		savestack[saveptr].setb0(3);
		savestack[saveptr].setb1(curgroup);
		savestack[saveptr].setrh(curboundary);
		if (curlevel == 255) {
			errorLogic.overflow(542, 255);
		}
		curboundary = saveptr;
		curlevel = curlevel + 1;
		saveptr = saveptr + 1;
		curgroup = c;
	}

	void eqdestroy(final memoryword w) {
		int q;
		switch (w.getb0()) {
			case 111:
			case 112:
			case 113:
			case 114:
				deletetokenref(w.getrh());
				break;
			case 117:
				deleteglueref(w.getrh());
				break;
			case 118: {
				q = w.getrh();
				if (q != 0) {
					freenode(q, mem[q].getlh() + mem[q].getlh() + 1);
				}
			}
				break;
			case 119:
				flushnodelist(w.getrh());
				break;
			default:
				;
				break;
		}
	}

	void eqsave(final int p, final int l) {
		if (saveptr > maxsavestack) {
			maxsavestack = saveptr;
			if (maxsavestack > savesize - 6) {
				errorLogic.overflow(541, savesize);
			}
		}
		if (l == 0) {
			savestack[saveptr].setb0(1);
		} else {
			savestack[saveptr].copy(eqtb[p]);
			saveptr = saveptr + 1;
			savestack[saveptr].setb0(0);
		}
		savestack[saveptr].setb1(l);
		savestack[saveptr].setrh(p);
		saveptr = saveptr + 1;
	}

	void eqdefine(final int p, final int t, final int e) {
		if (eqtb[p].getb1() == curlevel) {
			eqdestroy(eqtb[p]);
		} else if (curlevel > 1) {
			eqsave(p, eqtb[p].getb1());
		}
		eqtb[p].setb1(curlevel);
		eqtb[p].setb0(t);
		eqtb[p].setrh(e);
	}

	void eqworddefine(final int p, final int w) {
		if (xeqlevel[p - 9563] != curlevel) {
			eqsave(p, xeqlevel[p - 9563]);
			xeqlevel[p - 9563] = curlevel;
		}
		eqtb[p].setInt(w);
	}

	void geqdefine(final int p, final int t, final int e) {
		eqdestroy(eqtb[p]);
		eqtb[p].setb1(1);
		eqtb[p].setb0(t);
		eqtb[p].setrh(e);
	}

	void geqworddefine(final int p, final int w) {
		eqtb[p].setInt(w);
		xeqlevel[p - 9563] = 1;
	}

	void saveforafter(final int t) {
		if (curlevel > 1) {
			if (saveptr > maxsavestack) {
				maxsavestack = saveptr;
				if (maxsavestack > savesize - 6) {
					errorLogic.overflow(541, savesize);
				}
			}
			savestack[saveptr].setb0(2);
			savestack[saveptr].setb1(0);
			savestack[saveptr].setrh(t);
			saveptr = saveptr + 1;
		}
	}

	void unsave() {
		/* 30 */int p;
		int l;
		int t;
		l = 0;
		if (curlevel > 1) {
			curlevel = curlevel - 1;
			while (true) {
				saveptr = saveptr - 1;
				if (savestack[saveptr].getb0() == 3) {
					break /* lab30 */;
				}
				p = savestack[saveptr].getrh();
				if (savestack[saveptr].getb0() == 2) {
					t = curtok;
					curtok = p;
					unreadToken();
					curtok = t;
				} else {
					if (savestack[saveptr].getb0() == 0) {
						l = savestack[saveptr].getb1();
						saveptr = saveptr - 1;
					} else {
						savestack[saveptr].copy(eqtb[7181]);
					}
					if (p < 9563) {
						if (eqtb[p].getb1() == 1) {
							eqdestroy(savestack[saveptr]);
						} else {
							eqdestroy(eqtb[p]);
							eqtb[p].copy(savestack[saveptr]);
						}
					} else if (xeqlevel[p - 9563] != 1) {
						eqtb[p].copy(savestack[saveptr]);
						xeqlevel[p - 9563] = l;
					} else {
						;
					}
				}
			}
			/* lab30: */curgroup = savestack[saveptr].getb1();
			curboundary = savestack[saveptr].getrh();
		} else {
			errorLogic.confusion(543);
		}
	}

	void preparemag() {
		if ((magset > 0) && (eqtb[9580].getInt() != magset)) {
			errorLogic.error("!Incompatible magnification (" + eqtb[9580].getInt() + "); the previous value will be retained. I can handle only one magnification ratio per job. So I've reverted to the magnification you used earlier on this run.");
			geqworddefine(9580, magset);
		}
		if ((eqtb[9580].getInt() <= 0) || (eqtb[9580].getInt() > 32768)) {
			// errorLogic.error("!Illegal magnification has been changed to 1000. The magnification ratio must be between 1 and 32768.");
			printnl(262);
			print(552);
			helpptr = 1;
			helpline[0] = 553;
			errorLogic.interror(eqtb[9580].getInt());
			geqworddefine(9580, 1000);
		}
		magset = eqtb[9580].getInt();
	}

	void begintokenlist(final int p, final int t) {
		inputStack.duplicate();
		curinput.setState(TOKENIZER_STATE_TOKEN_LIST);
		curinput.setStart(p);
		curinput.setIndex(t);
		if (t >= 5) {
			mem[p].setlh(mem[p].getlh() + 1);
			if (t == 5) {
				curinput.setLimit(paramptr);
			} else {
				curinput.setLoc(mem[p].getrh());
				if (eqtb[9593].getInt() > 1) {
					begindiagnostic();
					printnl(338);
					switch (t) {
						case 14:
							printEscapeSequence(351);
							break;
						case 15:
							printEscapeSequence(594);
							break;
						default:
							printcmdchr(72, t + 7707);
							break;
					}
					print(556);
					tokenshow(p);
					enddiagnostic(false);
				}
			}
		} else {
			curinput.setLoc(p);
		}
	}

	void endtokenlist() {
		if (curinput.getIndex() >= 3) {
			if (curinput.getIndex() <= 4) {
				flushlist(curinput.getStart());
			} else {
				deletetokenref(curinput.getStart());
				if (curinput.getIndex() == 5) {
					while (paramptr > curinput.getLimit()) {
						paramptr = paramptr - 1;
						flushlist(paramstack[paramptr]);
					}
				}
			}
		} else if (curinput.getIndex() == 1) {
			if (alignstate > 500000) {
				alignstate = 0;
			} else {
				errorLogic.fatalError("(interwoven alignment preambles are not allowed)");
			}
		}
		inputStack.pop();
	}

	void unreadToken() {
		insertToken(curtok, 3);
	}

	void insertToken(final int token) {
		insertToken(token, 4);
		curtok = token;
	}

	void insertToken(final int token, final int type) {
		inputStack.popFinishedTokenLists();
		final int p = allocateMemoryWord();
		mem[p].setlh(token);
		if (token < 768) {
			if (token < 512) {
				alignstate = alignstate - 1;
			} else {
				alignstate = alignstate + 1;
			}
		}
		inputStack.duplicate();
		curinput.setState(TOKENIZER_STATE_TOKEN_LIST);
		curinput.setStart(p);
		curinput.setIndex(type);
		curinput.setLoc(p);
	}

	void beginfilereading() {
		if (inopen == maxinopen) {
			errorLogic.overflow(596, maxinopen);
		}
		inopen = inopen + 1;
		inputStack.duplicate();
		curinput.setIndex(inopen);
		linestack[curinput.getIndex()] = line;
		curinput.setStart(first);
		curinput.setState(TOKENIZER_STATE_MID_LINE);
		curinput.setName(0);
	}

	void endfilereading() {
		first = curinput.getStart();
		line = linestack[curinput.getIndex()];
		if (curinput.getName() > 17) {
			inputfile[curinput.getIndex()].close();
		}
		inputStack.pop();
		inopen = inopen - 1;
	}

	void checkoutervalidity() {
		int p;
		int q;
		if (scannerstatus != 0) {
			if (curcs != 0) {
				if ((curinput.getState() == TOKENIZER_STATE_TOKEN_LIST) || (curinput.getName() < 1) || (curinput.getName() > 17)) {
					p = allocateMemoryWord();
					mem[p].setlh(4095 + curcs);
					begintokenlist(p, 3);
				}
				curcmd = 10;
				curchr = 32;
			}
			if (scannerstatus > 1) {
				runaway();
				if (curcs == 0) {
					printnl(262);
					print(604);
				} else {
					curcs = 0;
					{
						printnl(262);
						print(605);
					}
				}
				print(606);
				p = allocateMemoryWord();
				switch (scannerstatus) {
					case 2: {
						print(570);
						mem[p].setlh(637);
					}
						break;
					case 3: {
						print(612);
						mem[p].setlh(partoken);
						longstate = 113;
					}
						break;
					case 4: {
						print(572);
						mem[p].setlh(637);
						q = p;
						p = allocateMemoryWord();
						mem[p].setrh(q);
						mem[p].setlh(11010);
						alignstate = -1000000;
					}
						break;
					case 5: {
						print(573);
						mem[p].setlh(637);
					}
						break;
				}
				begintokenlist(p, 4);
				print(607);
				sprintcs(warningindex);
				{
					helpptr = 4;
					helpline[3] = 608;
					helpline[2] = 609;
					helpline[1] = 610;
					helpline[0] = 611;
				}
				errorLogic.error();
			} else {
				{
					printnl(262);
					print(598);
				}
				printcmdchr(105, curif);
				print(599);
				printInt(skipline);
				{
					helpptr = 3;
					helpline[2] = 600;
					helpline[1] = 601;
					helpline[0] = 602;
				}
				if (curcs != 0) {
					curcs = 0;
				} else {
					helpline[2] = 603;
				}
				insertToken(11013);
				errorLogic.error();
			}
		}
	}

	void getnext() {
		/* 20 25 21 26 40 10 */int k;
		int t;
		int cat;
		int c, cc;
		int d;
		cc = 0;
		lab20: while (true) {
			curcs = 0;
			if (curinput.getState() != TOKENIZER_STATE_TOKEN_LIST) {
				lab25: while (true) {
					if (curinput.getLoc() <= curinput.getLimit()) {
						curchr = buffer[curinput.getLoc()];
						curinput.setLoc(curinput.getLoc() + 1);
						lab21: while (true) {
							curcmd = eqtb[8283 + curchr].getrh();
							switch (curinput.getState() + curcmd) {
								case 10:
								case 26:
								case 42:
								case 27:
								case 43:
									continue lab25;
								case 1:
								case 17:
								case 33: {
									if (curinput.getLoc() > curinput.getLimit()) {
										curcs = 513;
									} else {
										lab40: while (true) {
											lab26: while (true) {
												k = curinput.getLoc();
												curchr = buffer[k];
												cat = eqtb[8283 + curchr].getrh();
												k = k + 1;
												if (cat == 11) {
													curinput.setState(TOKENIZER_STATE_SKIP_BLANKS);
												} else if (cat == 10) {
													curinput.setState(TOKENIZER_STATE_SKIP_BLANKS);
												} else {
													curinput.setState(TOKENIZER_STATE_MID_LINE);
												}
												if ((cat == 11) && (k <= curinput.getLimit())) {
													do {
														curchr = buffer[k];
														cat = eqtb[8283 + curchr].getrh();
														k = k + 1;
													} while (!((cat != 11) || (k > curinput.getLimit())));
													{
														if (buffer[k] == curchr) {
															if (cat == 7) {
																if (k < curinput.getLimit()) {
																	c = buffer[k + 1];
																	if (c < 128) {
																		d = 2;
																		if ((((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)))) {
																			if (k + 2 <= curinput.getLimit()) {
																				cc = buffer[k + 2];
																				if ((((cc >= 48) && (cc <= 57)) || ((cc >= 97) && (cc <= 102)))) {
																					d = d + 1;
																				}
																			}
																		}
																		if (d > 2) {
																			if (c <= 57) {
																				curchr = c - 48;
																			} else {
																				curchr = c - 87;
																			}
																			if (cc <= 57) {
																				curchr = 16 * curchr + cc - 48;
																			} else {
																				curchr = 16 * curchr + cc - 87;
																			}
																			buffer[k - 1] = curchr;
																		} else if (c < 64) {
																			buffer[k - 1] = c + 64;
																		} else {
																			buffer[k - 1] = c - 64;
																		}
																		curinput.setLimit(curinput.getLimit() - d);
																		first = first - d;
																		while (k <= curinput.getLimit()) {
																			buffer[k] = buffer[k + d];
																			k = k + 1;
																		}
																		continue lab26;
																	}
																}
															}
														}
													}
													if (cat != 11) {
														k = k - 1;
													}
													if (k > curinput.getLoc() + 1) {
														curcs = idlookup(curinput.getLoc(), k - curinput.getLoc());
														curinput.setLoc(k);
														break lab40;
													}
												} else {
													if (buffer[k] == curchr) {
														if (cat == 7) {
															if (k < curinput.getLimit()) {
																c = buffer[k + 1];
																if (c < 128) {
																	d = 2;
																	if ((((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)))) {
																		if (k + 2 <= curinput.getLimit()) {
																			cc = buffer[k + 2];
																			if ((((cc >= 48) && (cc <= 57)) || ((cc >= 97) && (cc <= 102)))) {
																				d = d + 1;
																			}
																		}
																	}
																	if (d > 2) {
																		if (c <= 57) {
																			curchr = c - 48;
																		} else {
																			curchr = c - 87;
																		}
																		if (cc <= 57) {
																			curchr = 16 * curchr + cc - 48;
																		} else {
																			curchr = 16 * curchr + cc - 87;
																		}
																		buffer[k - 1] = curchr;
																	} else if (c < 64) {
																		buffer[k - 1] = c + 64;
																	} else {
																		buffer[k - 1] = c - 64;
																	}
																	curinput.setLimit(curinput.getLimit() - d);
																	first = first - d;
																	while (k <= curinput.getLimit()) {
																		buffer[k] = buffer[k + d];
																		k = k + 1;
																	}
																	continue lab26;
																}
															}
														}
													}
												}
												curcs = 257 + buffer[curinput.getLoc()];
												curinput.setLoc(curinput.getLoc() + 1);
												break;
											}
											break;
										}
									}
									/* lab40: */curcmd = eqtb[curcs].getb0();
									curchr = eqtb[curcs].getrh();
									if (curcmd >= 113) {
										checkoutervalidity();
									}
								}
									break;
								case 14:
								case 30:
								case 46: {
									curcs = curchr + 1;
									curcmd = eqtb[curcs].getb0();
									curchr = eqtb[curcs].getrh();
									curinput.setState(TOKENIZER_STATE_MID_LINE);
									if (curcmd >= 113) {
										checkoutervalidity();
									}
								}
									break;
								case 8:
								case 24:
								case 40: {
									if (curchr == buffer[curinput.getLoc()]) {
										if (curinput.getLoc() < curinput.getLimit()) {
											c = buffer[curinput.getLoc() + 1];
											if (c < 128) {
												curinput.setLoc(curinput.getLoc() + 2);
												if ((((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102)))) {
													if (curinput.getLoc() <= curinput.getLimit()) {
														cc = buffer[curinput.getLoc()];
														if ((((cc >= 48) && (cc <= 57)) || ((cc >= 97) && (cc <= 102)))) {
															curinput.setLoc(curinput.getLoc() + 1);
															if (c <= 57) {
																curchr = c - 48;
															} else {
																curchr = c - 87;
															}
															if (cc <= 57) {
																curchr = 16 * curchr + cc - 48;
															} else {
																curchr = 16 * curchr + cc - 87;
															}
															continue lab21;
														}
													}
												}
												if (c < 64) {
													curchr = c + 64;
												} else {
													curchr = c - 64;
												}
												continue lab21;
											}
										}
									}
									curinput.setState(TOKENIZER_STATE_MID_LINE);
								}
									break;
								case 16:
								case 32:
								case 48: {
									{
										printnl(262);
										print(613);
									}
									{
										helpptr = 2;
										helpline[1] = 614;
										helpline[0] = 615;
									}
									errorLogic.error();
									continue lab20;
								}
								case 11: {
									curinput.setState(TOKENIZER_STATE_SKIP_BLANKS);
									curchr = 32;
								}
									break;
								case 6: {
									curinput.setLoc(curinput.getLimit() + 1);
									curcmd = 10;
									curchr = 32;
								}
									break;
								case 22:
								case 15:
								case 31:
								case 47: {
									curinput.setLoc(curinput.getLimit() + 1);
									continue lab25;
								}
								case 38: {
									curinput.setLoc(curinput.getLimit() + 1);
									curcs = parloc;
									curcmd = eqtb[curcs].getb0();
									curchr = eqtb[curcs].getrh();
									if (curcmd >= 113) {
										checkoutervalidity();
									}
								}
									break;
								case 2:
									alignstate = alignstate + 1;
									break;
								case 18:
								case 34: {
									curinput.setState(TOKENIZER_STATE_MID_LINE);
									alignstate = alignstate + 1;
								}
									break;
								case 3:
									alignstate = alignstate - 1;
									break;
								case 19:
								case 35: {
									curinput.setState(TOKENIZER_STATE_MID_LINE);
									alignstate = alignstate - 1;
								}
									break;
								case 20:
								case 21:
								case 23:
								case 25:
								case 28:
								case 29:
								case 36:
								case 37:
								case 39:
								case 41:
								case 44:
								case 45:
									curinput.setState(TOKENIZER_STATE_MID_LINE);
									break;
								default:
									;
									break;
							}
							break;
						}
					} else {
						curinput.setState(TOKENIZER_STATE_NEW_LINE);
						if (curinput.getName() > 17) {
							line = line + 1;
							first = curinput.getStart();
							if (!forceeof) {
								if (inputln(inputfile[curinput.getIndex()], true)) {
									firmuptheline();
								} else {
									forceeof = true;
								}
							}
							if (forceeof) {
								printchar(41);
								openparens = openparens - 1;
								termout.flush();
								forceeof = false;
								endfilereading();
								checkoutervalidity();
								continue lab20;
							}
							if ((eqtb[9611].getInt() < 0) || (eqtb[9611].getInt() > 255)) {
								curinput.setLimit(curinput.getLimit() - 1);
							} else {
								buffer[curinput.getLimit()] = eqtb[9611].getInt();
							}
							first = curinput.getLimit() + 1;
							curinput.setLoc(curinput.getStart());
						} else {
							if (!(curinput.getName() == 0)) {
								curcmd = 0;
								curchr = 0;
								return /* lab10 */;
							}
							if (inputptr > 0) {
								endfilereading();
								continue lab20;
							}
							if (selector < 18) {
								openlogfile();
							}
							errorLogic.fatalError("*** (job aborted, no legal \\end found)");
						}
						continue lab25;
					}
					break;
				}
			} else if (curinput.getLoc() != 0) {
				t = mem[curinput.getLoc()].getlh();
				curinput.setLoc(mem[curinput.getLoc()].getrh());
				if (t >= 4095) {
					curcs = t - 4095;
					curcmd = eqtb[curcs].getb0();
					curchr = eqtb[curcs].getrh();
					if (curcmd >= 113) {
						if (curcmd == 116) {
							curcs = mem[curinput.getLoc()].getlh() - 4095;
							curinput.setLoc(0);
							curcmd = eqtb[curcs].getb0();
							curchr = eqtb[curcs].getrh();
							if (curcmd > 100) {
								curcmd = 0;
								curchr = 257;
							}
						} else {
							checkoutervalidity();
						}
					}
				} else {
					curcmd = t / 256;
					curchr = t % 256;
					switch (curcmd) {
						case 1:
							alignstate = alignstate + 1;
							break;
						case 2:
							alignstate = alignstate - 1;
							break;
						case 5: {
							begintokenlist(paramstack[curinput.getLimit() + curchr - 1], 0);
							continue lab20;
						}
						default:
							;
							break;
					}
				}
			} else {
				endtokenlist();
				continue lab20;
			}
			if (curcmd <= 5) {
				if (curcmd >= 4) {
					if (alignstate == 0) {
						if (scannerstatus == 4) {
							errorLogic.fatalError("(interwoven alignment preambles are not allowed)");
						}
						curcmd = mem[curalign + 5].getlh();
						mem[curalign + 5].setlh(curchr);
						if (curcmd == 63) {
							begintokenlist(memtop - 10, 2);
						} else {
							begintokenlist(mem[curalign + 2].getInt(), 2);
						}
						alignstate = 1000000;
						continue lab20;
					}
				}
			}
			break;
		}
	}

	void firmuptheline() {
		curinput.setLimit(last);
	}

	void gettoken() {
		nonewcontrolsequence = false;
		getnext();
		nonewcontrolsequence = true;
		if (curcs == 0) {
			curtok = (curcmd * 256) + curchr;
		} else {
			curtok = 4095 + curcs;
		}
	}

	void macrocall() {
		/* 10 22 30 31 40 */int r;
		int p;
		int q;
		int s;
		int t;
		int u, v;
		int rbraceptr;
		int n;
		int unbalance;
		int m;
		int refcount;
		int savescannerstatus;
		int savewarningindex;
		int matchchr;
		savescannerstatus = scannerstatus;
		savewarningindex = warningindex;
		p = 0;
		m = 0;
		rbraceptr = 0;
		matchchr = 0;
		warningindex = curcs;
		refcount = curchr;
		r = mem[refcount].getrh();
		n = 0;
		if (eqtb[9593].getInt() > 0) {
			begindiagnostic();
			println();
			printcs(warningindex);
			tokenshow(refcount);
			enddiagnostic(false);
		}
		if (mem[r].getlh() != 3584) {
			scannerstatus = 3;
			unbalance = 0;
			longstate = eqtb[curcs].getb0();
			if (longstate >= 113) {
				longstate = longstate - 2;
			}
			do {
				mem[memtop - 3].setrh(0);
				if ((mem[r].getlh() > 3583) || (mem[r].getlh() < 3328)) {
					s = 0;
				} else {
					matchchr = mem[r].getlh() - 3328;
					s = mem[r].getrh();
					r = s;
					p = memtop - 3;
					m = 0;
				}
				lab22: while (true) {
					gettoken();
					lab40: while (true) {
						if (curtok == mem[r].getlh()) {
							r = mem[r].getrh();
							if ((mem[r].getlh() >= 3328) && (mem[r].getlh() <= 3584)) {
								if (curtok < 512) {
									alignstate = alignstate - 1;
								}
								break lab40;
							} else {
								continue lab22;
							}
						}
						if (s != r) {
							if (s == 0) {
								{
									printnl(262);
									print(650);
								}
								sprintcs(warningindex);
								print(651);
								{
									helpptr = 4;
									helpline[3] = 652;
									helpline[2] = 653;
									helpline[1] = 654;
									helpline[0] = 655;
								}
								errorLogic.error();
								return /* lab10 */;
							} else {
								t = s;
								do {
									{
										q = allocateMemoryWord();
										mem[p].setrh(q);
										mem[q].setlh(mem[t].getlh());
										p = q;
									}
									m = m + 1;
									u = mem[t].getrh();
									v = s;
									while (true) {
										if (u == r) {
											if (curtok != mem[v].getlh()) {
												break /* lab30 */;
											} else {
												r = mem[v].getrh();
												continue lab22;
											}
										}
										if (mem[u].getlh() != mem[v].getlh()) {
											break /* lab30 */;
										}
										u = mem[u].getrh();
										v = mem[v].getrh();
									}
									/* lab30: */t = mem[t].getrh();
								} while (!(t == r));
								r = s;
							}
						}
						if (curtok == partoken) {
							if (longstate != 112) {
								if (longstate == 111) {
									runaway();
									{
										printnl(262);
										print(645);
									}
									sprintcs(warningindex);
									print(646);
									{
										helpptr = 3;
										helpline[2] = 647;
										helpline[1] = 648;
										helpline[0] = 649;
									}
									errorLogic.backerror();
								}
								pstack[n] = mem[memtop - 3].getrh();
								alignstate = alignstate - unbalance;
								for (m = 0; m <= n; m++) {
									flushlist(pstack[m]);
								}
								return /* lab10 */;
							}
						}
						if (curtok < 768) {
							if (curtok < 512) {
								unbalance = 1;
								while (true) {
									{
										{
											q = avail;
											if (q == 0) {
												q = allocateMemoryWord();
											} else {
												avail = mem[q].getrh();
												mem[q].setrh(0);
											}
										}
										mem[p].setrh(q);
										mem[q].setlh(curtok);
										p = q;
									}
									gettoken();
									if (curtok == partoken) {
										if (longstate != 112) {
											if (longstate == 111) {
												runaway();
												{
													printnl(262);
													print(645);
												}
												sprintcs(warningindex);
												print(646);
												{
													helpptr = 3;
													helpline[2] = 647;
													helpline[1] = 648;
													helpline[0] = 649;
												}
												errorLogic.backerror();
											}
											pstack[n] = mem[memtop - 3].getrh();
											alignstate = alignstate - unbalance;
											for (m = 0; m <= n; m++) {
												flushlist(pstack[m]);
											}
											return /* lab10 */;
										}
									}
									if (curtok < 768) {
										if (curtok < 512) {
											unbalance = unbalance + 1;
										} else {
											unbalance = unbalance - 1;
											if (unbalance == 0) {
												break /* lab31 */;
											}
										}
									}
								}
								/* lab31: */rbraceptr = p;
								{
									q = allocateMemoryWord();
									mem[p].setrh(q);
									mem[q].setlh(curtok);
									p = q;
								}
							} else {
								unreadToken();
								{
									printnl(262);
									print(637);
								}
								sprintcs(warningindex);
								print(638);
								{
									helpptr = 6;
									helpline[5] = 639;
									helpline[4] = 640;
									helpline[3] = 641;
									helpline[2] = 642;
									helpline[1] = 643;
									helpline[0] = 644;
								}
								alignstate = alignstate + 1;
								longstate = 111;
								insertToken(partoken);
								errorLogic.error();
							}
						} else {
							if (curtok == 2592) {
								if (mem[r].getlh() <= 3584) {
									if (mem[r].getlh() >= 3328) {
										continue lab22;
									}
								}
							}
							{
								q = allocateMemoryWord();
								mem[p].setrh(q);
								mem[q].setlh(curtok);
								p = q;
							}
						}
						m = m + 1;
						if (mem[r].getlh() > 3584) {
							continue lab22;
						}
						if (mem[r].getlh() < 3328) {
							continue lab22;
						}
						break;
					}
					break;
				}
				/* lab40: */if (s != 0) {
					if ((m == 1) && (mem[p].getlh() < 768) && (p != memtop - 3)) {
						mem[rbraceptr].setrh(0);
						{
							mem[p].setrh(avail);
							avail = p;
						}
						p = mem[memtop - 3].getrh();
						pstack[n] = mem[p].getrh();
						{
							mem[p].setrh(avail);
							avail = p;
						}
					} else {
						pstack[n] = mem[memtop - 3].getrh();
					}
					n = n + 1;
					if (eqtb[9593].getInt() > 0) {
						begindiagnostic();
						printnl(matchchr);
						printInt(n);
						print(656);
						showtokenlist(pstack[n - 1], 0, 1000);
						enddiagnostic(false);
					}
				}
			} while (!(mem[r].getlh() == 3584));
		}
		inputStack.popFinishedTokenLists();
		begintokenlist(refcount, 5);
		curinput.setName(warningindex);
		curinput.setLoc(mem[r].getrh());
		if (n > 0) {
			if (paramptr + n > maxparamstack) {
				maxparamstack = paramptr + n;
				if (maxparamstack > paramsize) {
					errorLogic.overflow(636, paramsize);
				}
			}
			for (m = 0; m <= n - 1; m++) {
				paramstack[paramptr + m] = pstack[m];
			}
			paramptr = paramptr + n;
		}
		scannerstatus = savescannerstatus;
		warningindex = savewarningindex;
	}

	void insertrelax() {
		curtok = 4095 + curcs;
		unreadToken();
		curtok = 11016;
		unreadToken();
		curinput.setIndex(4);
	}

	void expand() {
		int t;
		int p, q, r;
		int j;
		int cvbackup;
		int cvlbackup, radixbackup, cobackup;
		int backupbackup;
		int savescannerstatus;
		cvbackup = curval;
		cvlbackup = curvallevel;
		radixbackup = radix;
		cobackup = curorder;
		backupbackup = mem[memtop - 13].getrh();
		if (curcmd < 111) {
			if (eqtb[9599].getInt() > 1) {
				showcurcmdchr();
			}
			switch (curcmd) {
				case 110: {
					if (curmark[curchr] != 0) {
						begintokenlist(curmark[curchr], 14);
					}
				}
					break;
				case 102: {
					gettoken();
					t = curtok;
					gettoken();
					if (curcmd > 100) {
						expand();
					} else {
						unreadToken();
					}
					curtok = t;
					unreadToken();
				}
					break;
				case 103: {
					savescannerstatus = scannerstatus;
					scannerstatus = 0;
					gettoken();
					scannerstatus = savescannerstatus;
					t = curtok;
					unreadToken();
					if (t >= 4095) {
						p = allocateMemoryWord();
						mem[p].setlh(11018);
						mem[p].setrh(curinput.getLoc());
						curinput.setStart(p);
						curinput.setLoc(p);
					}
				}
					break;
				case 107: {
					r = allocateMemoryWord();
					p = r;
					do {
						getxtoken();
						if (curcs == 0) {
							q = allocateMemoryWord();
							mem[p].setrh(q);
							mem[q].setlh(curtok);
							p = q;
						}
					} while (!(curcs != 0));
					if (curcmd != 67) {
						{
							printnl(262);
							print(625);
						}
						printEscapeSequence(505);
						print(626);
						{
							helpptr = 2;
							helpline[1] = 627;
							helpline[0] = 628;
						}
						errorLogic.backerror();
					}
					j = first;
					p = mem[r].getrh();
					while (p != 0) {
						buffer[j] = mem[p].getlh() % 256;
						j = j + 1;
						p = mem[p].getrh();
					}
					if (j > first + 1) {
						nonewcontrolsequence = false;
						curcs = idlookup(first, j - first);
						nonewcontrolsequence = true;
					} else if (j == first) {
						curcs = 513;
					} else {
						curcs = 257 + buffer[first];
					}
					flushlist(r);
					if (eqtb[curcs].getb0() == 101) {
						eqdefine(curcs, 0, 256);
					}
					curtok = curcs + 4095;
					unreadToken();
				}
					break;
				case 108:
					convtoks();
					break;
				case 109:
					insthetoks();
					break;
				case 105:
					conditional();
					break;
				case 106:
					if (curchr > iflimit) {
						if (iflimit == 1) {
							insertrelax();
						} else {
							{
								printnl(262);
								print(776);
							}
							printcmdchr(106, curchr);
							{
								helpptr = 1;
								helpline[0] = 777;
							}
							errorLogic.error();
						}
					} else {
						while (curchr != 2) {
							passtext();
						}
						{
							p = condptr;
							ifline = mem[p + 1].getInt();
							curif = mem[p].getb1();
							iflimit = mem[p].getb0();
							condptr = mem[p].getrh();
							freenode(p, 2);
						}
					}
					break;
				case 104:
					if (curchr > 0) {
						forceeof = true;
					} else if (nameinprogress) {
						insertrelax();
					} else {
						startinput();
					}
					break;
				default: {
					{
						printnl(262);
						print(619);
					}
					{
						helpptr = 5;
						helpline[4] = 620;
						helpline[3] = 621;
						helpline[2] = 622;
						helpline[1] = 623;
						helpline[0] = 624;
					}
					errorLogic.error();
				}
					break;
			}
		} else if (curcmd < 115) {
			macrocall();
		} else {
			curtok = 11015;
			unreadToken();
		}
		curval = cvbackup;
		curvallevel = cvlbackup;
		radix = radixbackup;
		curorder = cobackup;
		mem[memtop - 13].setrh(backupbackup);
	}

	void getxtoken() {
		/* 20 30 */while (true) {
			getnext();
			if (curcmd <= 100) {
				break /* lab30 */;
			}
			if (curcmd >= 111) {
				if (curcmd < 115) {
					macrocall();
				} else {
					curcs = 6920;
					curcmd = 9;
					break /* lab30 */;
				}
			} else {
				expand();
			}
		}
		/* lab30: */if (curcs == 0) {
			curtok = (curcmd * 256) + curchr;
		} else {
			curtok = 4095 + curcs;
		}
	}

	void xtoken() {
		while (curcmd > 100) {
			expand();
			getnext();
		}
		if (curcs == 0) {
			curtok = (curcmd * 256) + curchr;
		} else {
			curtok = 4095 + curcs;
		}
	}

	void scanleftbrace() {
		do {
			getxtoken();
		} while (!((curcmd != 10) && (curcmd != 0)));
		if (curcmd != 1) {
			{
				printnl(262);
				print(657);
			}
			{
				helpptr = 4;
				helpline[3] = 658;
				helpline[2] = 659;
				helpline[1] = 660;
				helpline[0] = 661;
			}
			errorLogic.backerror();
			curtok = 379;
			curcmd = 1;
			curchr = 123;
			alignstate = alignstate + 1;
		}
	}

	void scanoptionalequals() {
		do {
			getxtoken();
		} while (!(curcmd != 10));
		if (curtok != 3133) {
			unreadToken();
		}
	}

	boolean scankeyword(final int stringId) {
		int p;
		int q;
		p = memtop - 13;
		mem[p].setrh(0);
		final String s = stringPool.getString(stringId);
		int i = 0;
		while (i < s.length()) {
			getxtoken();
			if ((curcs == 0) && ((curchr == s.charAt(i)) || (curchr == s.charAt(i) - 32))) {
				q = allocateMemoryWord();
				mem[p].setrh(q);
				mem[q].setlh(curtok);
				p = q;
				i++;
			} else if ((curcmd != 10) || (p != memtop - 13)) {
				unreadToken();
				if (p != memtop - 13) {
					begintokenlist(mem[memtop - 13].getrh(), 3);
				}
				return false;
			}
		}
		flushlist(mem[memtop - 13].getrh());
		return true;
	}

	void scaneightbitint() {
		scanint();
		if ((curval < 0) || (curval > 255)) {
			{
				printnl(262);
				print(687);
			}
			{
				helpptr = 2;
				helpline[1] = 688;
				helpline[0] = 689;
			}
			errorLogic.interror(curval);
			curval = 0;
		}
	}

	void scancharnum() {
		scanint();
		if ((curval < 0) || (curval > 255)) {
			{
				printnl(262);
				print(690);
			}
			{
				helpptr = 2;
				helpline[1] = 691;
				helpline[0] = 689;
			}
			errorLogic.interror(curval);
			curval = 0;
		}
	}

	void scanfourbitint() {
		scanint();
		if ((curval < 0) || (curval > 15)) {
			{
				printnl(262);
				print(692);
			}
			{
				helpptr = 2;
				helpline[1] = 693;
				helpline[0] = 689;
			}
			errorLogic.interror(curval);
			curval = 0;
		}
	}

	void scanfifteenbitint() {
		scanint();
		if ((curval < 0) || (curval > 32767)) {
			{
				printnl(262);
				print(694);
			}
			{
				helpptr = 2;
				helpline[1] = 695;
				helpline[0] = 689;
			}
			errorLogic.interror(curval);
			curval = 0;
		}
	}

	void scantwentysevenbitint() {
		scanint();
		if ((curval < 0) || (curval > 134217727)) {
			{
				printnl(262);
				print(696);
			}
			{
				helpptr = 2;
				helpline[1] = 697;
				helpline[0] = 689;
			}
			errorLogic.interror(curval);
			curval = 0;
		}
	}

	void scanfontident() {
		int f;
		int m;
		do {
			getxtoken();
		} while (!(curcmd != 10));
		if (curcmd == 88) {
			f = eqtb[8234].getrh();
		} else if (curcmd == 87) {
			f = curchr;
		} else if (curcmd == 86) {
			m = curchr;
			scanfourbitint();
			f = eqtb[m + curval].getrh();
		} else {
			{
				printnl(262);
				print(817);
			}
			{
				helpptr = 2;
				helpline[1] = 818;
				helpline[0] = 819;
			}
			errorLogic.backerror();
			f = 0;
		}
		curval = f;
	}

	void findfontdimen(final boolean writing) {
		int f;
		int n;
		scanint();
		n = curval;
		scanfontident();
		f = curval;
		if (n <= 0) {
			curval = fmemptr;
		} else {
			if (writing && (n <= 4) && (n >= 2) && (fontglue[f] != 0)) {
				deleteglueref(fontglue[f]);
				fontglue[f] = 0;
			}
			if (n > fontparams[f]) {
				if (f < fontptr) {
					curval = fmemptr;
				} else {
					do {
						if (fmemptr == fontmemsize) {
							errorLogic.overflow(824, fontmemsize);
						}
						fontinfo[fmemptr].setInt(0);
						fmemptr = fmemptr + 1;
						fontparams[f] = fontparams[f] + 1;
					} while (!(n == fontparams[f]));
					curval = fmemptr - 1;
				}
			} else {
				curval = n + parambase[f];
			}
		}
		if (curval == fmemptr) {
			{
				printnl(262);
				print(802);
			}
			printEscapeSequence(hash[6924 + f - 514].rh);
			print(820);
			printInt(fontparams[f]);
			print(821);
			{
				helpptr = 2;
				helpline[1] = 822;
				helpline[0] = 823;
			}
			errorLogic.error();
		}
	}

	void scansomethinginternal(final int level, final boolean negative) {
		int m;
		int p;
		m = curchr;
		switch (curcmd) {
			case 85: {
				scancharnum();
				if (m == 9307) {
					curval = eqtb[9307 + curval].getrh();
					curvallevel = 0;
				} else if (m < 9307) {
					curval = eqtb[m + curval].getrh();
					curvallevel = 0;
				} else {
					curval = eqtb[m + curval].getInt();
					curvallevel = 0;
				}
			}
				break;
			case 71:
			case 72:
			case 86:
			case 87:
			case 88:
				if (level != 5) {
					{
						printnl(262);
						print(664);
					}
					{
						helpptr = 3;
						helpline[2] = 665;
						helpline[1] = 666;
						helpline[0] = 667;
					}
					errorLogic.backerror();
					{
						curval = 0;
						curvallevel = 1;
					}
				} else if (curcmd <= 72) {
					if (curcmd < 72) {
						scaneightbitint();
						m = 7722 + curval;
					}
					{
						curval = eqtb[m].getrh();
						curvallevel = 5;
					}
				} else {
					unreadToken();
					scanfontident();
					{
						curval = 6924 + curval;
						curvallevel = 4;
					}
				}
				break;
			case 73: {
				curval = eqtb[m].getInt();
				curvallevel = 0;
			}
				break;
			case 74: {
				curval = eqtb[m].getInt();
				curvallevel = 1;
			}
				break;
			case 75: {
				curval = eqtb[m].getrh();
				curvallevel = 2;
			}
				break;
			case 76: {
				curval = eqtb[m].getrh();
				curvallevel = 3;
			}
				break;
			case 79:
				if (Math.abs(curlist.modefield) != m) {
					{
						printnl(262);
						print(680);
					}
					printcmdchr(79, m);
					{
						helpptr = 4;
						helpline[3] = 681;
						helpline[2] = 682;
						helpline[1] = 683;
						helpline[0] = 684;
					}
					errorLogic.error();
					if (level != 5) {
						curval = 0;
						curvallevel = 1;
					} else {
						curval = 0;
						curvallevel = 0;
					}
				} else if (m == 1) {
					curval = curlist.auxfield.getInt();
					curvallevel = 1;
				} else {
					curval = curlist.auxfield.getlh();
					curvallevel = 0;
				}
				break;
			case 80:
				if (curlist.modefield == 0) {
					curval = 0;
					curvallevel = 0;
				} else {
					nest[nestptr].copy(curlist);
					p = nestptr;
					while (Math.abs(nest[p].modefield) != 1) {
						p = p - 1;
					}
					{
						curval = nest[p].pgfield;
						curvallevel = 0;
					}
				}
				break;
			case 82: {
				if (m == 0) {
					curval = deadcycles;
				} else {
					curval = insertpenalties;
				}
				curvallevel = 0;
			}
				break;
			case 81: {
				if ((pagecontents == 0) && (!outputactive)) {
					if (m == 0) {
						curval = 1073741823;
					} else {
						curval = 0;
					}
				} else {
					curval = pagesofar[m];
				}
				curvallevel = 1;
			}
				break;
			case 84: {
				if (eqtb[7712].getrh() == 0) {
					curval = 0;
				} else {
					curval = mem[eqtb[7712].getrh()].getlh();
				}
				curvallevel = 0;
			}
				break;
			case 83: {
				scaneightbitint();
				if (eqtb[7978 + curval].getrh() == 0) {
					curval = 0;
				} else {
					curval = mem[eqtb[7978 + curval].getrh() + m].getInt();
				}
				curvallevel = 1;
			}
				break;
			case 68:
			case 69: {
				curval = curchr;
				curvallevel = 0;
			}
				break;
			case 77: {
				findfontdimen(false);
				fontinfo[fmemptr].setInt(0);
				{
					curval = fontinfo[curval].getInt();
					curvallevel = 1;
				}
			}
				break;
			case 78: {
				scanfontident();
				if (m == 0) {
					curval = hyphenchar[curval];
					curvallevel = 0;
				} else {
					curval = skewchar[curval];
					curvallevel = 0;
				}
			}
				break;
			case 89: {
				scaneightbitint();
				switch (m) {
					case 0:
						curval = eqtb[9618 + curval].getInt();
						break;
					case 1:
						curval = eqtb[10151 + curval].getInt();
						break;
					case 2:
						curval = eqtb[7200 + curval].getrh();
						break;
					case 3:
						curval = eqtb[7456 + curval].getrh();
						break;
				}
				curvallevel = m;
			}
				break;
			case 70:
				if (curchr > 2) {
					if (curchr == 3) {
						curval = line;
					} else {
						curval = lastbadness;
					}
					curvallevel = 0;
				} else {
					if (curchr == 2) {
						curval = 0;
					} else {
						curval = 0;
					}
					curvallevel = curchr;
					if (!(curlist.tailfield >= himemmin) && (curlist.modefield != 0)) {
						switch (curchr) {
							case 0:
								if (mem[curlist.tailfield].getb0() == 12) {
									curval = mem[curlist.tailfield + 1].getInt();
								}
								break;
							case 1:
								if (mem[curlist.tailfield].getb0() == 11) {
									curval = mem[curlist.tailfield + 1].getInt();
								}
								break;
							case 2:
								if (mem[curlist.tailfield].getb0() == 10) {
									curval = mem[curlist.tailfield + 1].getlh();
									if (mem[curlist.tailfield].getb1() == 99) {
										curvallevel = 3;
									}
								}
								break;
						}
					} else if ((curlist.modefield == 1) && (curlist.tailfield == curlist.headfield)) {
						switch (curchr) {
							case 0:
								curval = lastpenalty;
								break;
							case 1:
								curval = lastkern;
								break;
							case 2:
								if (lastglue != maxhalfword) {
									curval = lastglue;
								}
								break;
						}
					}
				}
				break;
			default: {
				{
					printnl(262);
					print(685);
				}
				printcmdchr(curcmd, curchr);
				print(686);
				printEscapeSequence(537);
				{
					helpptr = 1;
					helpline[0] = 684;
				}
				errorLogic.error();
				if (level != 5) {
					curval = 0;
					curvallevel = 1;
				} else {
					curval = 0;
					curvallevel = 0;
				}
			}
				break;
		}
		while (curvallevel > level) {
			if (curvallevel == 2) {
				curval = mem[curval + 1].getInt();
			} else if (curvallevel == 3) {
				errorLogic.muerror();
			}
			curvallevel = curvallevel - 1;
		}
		if (negative) {
			if (curvallevel >= 2) {
				curval = newspec(curval);
				{
					mem[curval + 1].setInt(-mem[curval + 1].getInt());
					mem[curval + 2].setInt(-mem[curval + 2].getInt());
					mem[curval + 3].setInt(-mem[curval + 3].getInt());
				}
			} else {
				curval = -curval;
			}
		} else if ((curvallevel >= 2) && (curvallevel <= 3)) {
			mem[curval].setrh(mem[curval].getrh() + 1);
		}
	}

	void scanint() {
		/* 30 */boolean negative;
		int m;
		int d;
		boolean vacuous;
		boolean OKsofar;
		radix = 0;
		OKsofar = true;
		negative = false;
		do {
			do {
				getxtoken();
			} while (!(curcmd != 10));
			if (curtok == 3117) {
				negative = !negative;
				curtok = 3115;
			}
		} while (!(curtok != 3115));
		if (curtok == 3168) {
			gettoken();
			if (curtok < 4095) {
				curval = curchr;
				if (curcmd <= 2) {
					if (curcmd == 2) {
						alignstate = alignstate + 1;
					} else {
						alignstate = alignstate - 1;
					}
				}
			} else if (curtok < 4352) {
				curval = curtok - 4096;
			} else {
				curval = curtok - 4352;
			}
			if (curval > 255) {
				{
					printnl(262);
					print(698);
				}
				{
					helpptr = 2;
					helpline[1] = 699;
					helpline[0] = 700;
				}
				curval = 48;
				errorLogic.backerror();
			} else {
				getxtoken();
				if (curcmd != 10) {
					unreadToken();
				}
			}
		} else if ((curcmd >= 68) && (curcmd <= 89)) {
			scansomethinginternal(0, false);
		} else {
			radix = 10;
			m = 214748364;
			if (curtok == 3111) {
				radix = 8;
				m = 268435456;
				getxtoken();
			} else if (curtok == 3106) {
				radix = 16;
				m = 134217728;
				getxtoken();
			}
			vacuous = true;
			curval = 0;
			while (true) {
				if ((curtok < 3120 + radix) && (curtok >= 3120) && (curtok <= 3129)) {
					d = curtok - 3120;
				} else if (radix == 16) {
					if ((curtok <= 2886) && (curtok >= 2881)) {
						d = curtok - 2871;
					} else if ((curtok <= 3142) && (curtok >= 3137)) {
						d = curtok - 3127;
					} else {
						break /* lab30 */;
					}
				} else {
					break /* lab30 */;
				}
				vacuous = false;
				if ((curval >= m) && ((curval > m) || (d > 7) || (radix != 10))) {
					if (OKsofar) {
						{
							printnl(262);
							print(701);
						}
						{
							helpptr = 2;
							helpline[1] = 702;
							helpline[0] = 703;
						}
						errorLogic.error();
						curval = 2147483647;
						OKsofar = false;
					}
				} else {
					curval = curval * radix + d;
				}
				getxtoken();
			}
			/* lab30: */if (vacuous) {
				{
					printnl(262);
					print(664);
				}
				{
					helpptr = 3;
					helpline[2] = 665;
					helpline[1] = 666;
					helpline[0] = 667;
				}
				errorLogic.backerror();
			} else if (curcmd != 10) {
				unreadToken();
			}
		}
		if (negative) {
			curval = -curval;
		}
	}

	void scandimen(final boolean mu, final boolean inf, final boolean shortcut) {
		/* 30 31 32 40 45 88 89 */boolean negative;
		int f;
		int num, denom;
		int k, kk;
		int p, q;
		int v;
		int savecurval;
		f = 0;
		aritherror = false;
		curorder = 0;
		negative = false;
		lab89: while (true) {
			if (!shortcut) {
				negative = false;
				do {
					do {
						getxtoken();
					} while (!(curcmd != 10));
					if (curtok == 3117) {
						negative = !negative;
						curtok = 3115;
					}
				} while (!(curtok != 3115));
				if ((curcmd >= 68) && (curcmd <= 89)) {
					if (mu) {
						scansomethinginternal(3, false);
						if (curvallevel >= 2) {
							v = mem[curval + 1].getInt();
							deleteglueref(curval);
							curval = v;
						}
						if (curvallevel == 3) {
							break lab89;
						}
						if (curvallevel != 0) {
							errorLogic.muerror();
						}
					} else {
						scansomethinginternal(1, false);
						if (curvallevel == 1) {
							break lab89;
						}
					}
				} else {
					unreadToken();
					if (curtok == 3116) {
						curtok = 3118;
					}
					if (curtok != 3118) {
						scanint();
					} else {
						radix = 10;
						curval = 0;
					}
					if (curtok == 3116) {
						curtok = 3118;
					}
					if ((radix == 10) && (curtok == 3118)) {
						k = 0;
						p = 0;
						gettoken();
						lab31: while (true) {
							getxtoken();
							if ((curtok > 3129) || (curtok < 3120)) {
								break lab31;
							}
							if (k < 17) {
								q = allocateMemoryWord();
								mem[q].setrh(p);
								mem[q].setlh(curtok - 3120);
								p = q;
								k = k + 1;
							}
						}
						/* lab31: */for (kk = k; kk >= 1; kk--) {
							dig[kk - 1] = mem[p].getlh();
							q = p;
							p = mem[p].getrh();
							{
								mem[q].setrh(avail);
								avail = q;
							}
						}
						f = rounddecimals(k);
						if (curcmd != 10) {
							unreadToken();
						}
					}
				}
			}
			if (curval < 0) {
				negative = !negative;
				curval = -curval;
			}
			lab30: while (true) {
				lab88: while (true) {
					if (inf) {
						if (scankeyword(311)) {
							curorder = 1;
							while (scankeyword(108)) {
								if (curorder == 3) {
									{
										printnl(262);
										print(705);
									}
									print(706);
									{
										helpptr = 1;
										helpline[0] = 707;
									}
									errorLogic.error();
								} else {
									curorder = curorder + 1;
								}
							}
							break lab88;
						}
					}
					savecurval = curval;
					do {
						getxtoken();
					} while (!(curcmd != 10));
					lab45: while (true) {
						;
						lab40: while (true) {
							;
							if ((curcmd < 68) || (curcmd > 89)) {
								unreadToken();
							} else {
								if (mu) {
									scansomethinginternal(3, false);
									if (curvallevel >= 2) {
										v = mem[curval + 1].getInt();
										deleteglueref(curval);
										curval = v;
									}
									if (curvallevel != 3) {
										errorLogic.muerror();
									}
								} else {
									scansomethinginternal(1, false);
								}
								v = curval;
								break lab40;
							}
							if (mu) {
								break lab45;
							}
							if (scankeyword(708)) {
								v = (fontinfo[6 + parambase[eqtb[8234].getrh()]].getInt());
							} else if (scankeyword(709)) {
								v = (fontinfo[5 + parambase[eqtb[8234].getrh()]].getInt());
							} else {
								break lab45;
							}
							{
								getxtoken();
								if (curcmd != 10) {
									unreadToken();
								}
							}
							break;
						}
						/* lab40: */curval = multandadd(savecurval, v, xnoverd(v, f, 65536), 1073741823);
						break lab89;
					}
					/* lab45: */if (mu) {
						if (scankeyword(337)) {
							break lab88;
						} else {
							{
								printnl(262);
								print(705);
							}
							print(710);
							{
								helpptr = 4;
								helpline[3] = 711;
								helpline[2] = 712;
								helpline[1] = 713;
								helpline[0] = 714;
							}
							errorLogic.error();
							break lab88;
						}
					}
					if (scankeyword(704)) {
						preparemag();
						if (eqtb[9580].getInt() != 1000) {
							curval = xnoverd(curval, 1000, eqtb[9580].getInt());
							f = (1000 * f + 65536 * remainder) / eqtb[9580].getInt();
							curval = curval + (f / 65536);
							f = f % 65536;
						}
					}
					if (scankeyword(397)) {
						break lab88;
					}
					lab32: while (true) {
						if (scankeyword(715)) {
							num = 7227;
							denom = 100;
						} else if (scankeyword(716)) {
							num = 12;
							denom = 1;
						} else if (scankeyword(717)) {
							num = 7227;
							denom = 254;
						} else if (scankeyword(718)) {
							num = 7227;
							denom = 2540;
						} else if (scankeyword(719)) {
							num = 7227;
							denom = 7200;
						} else if (scankeyword(720)) {
							num = 1238;
							denom = 1157;
						} else if (scankeyword(721)) {
							num = 14856;
							denom = 1157;
						} else if (scankeyword(722)) {
							break lab30;
						} else {
							{
								printnl(262);
								print(705);
							}
							print(723);
							{
								helpptr = 6;
								helpline[5] = 724;
								helpline[4] = 725;
								helpline[3] = 726;
								helpline[2] = 712;
								helpline[1] = 713;
								helpline[0] = 714;
							}
							errorLogic.error();
							break lab32;
						}
						curval = xnoverd(curval, num, denom);
						f = (num * f + 65536 * remainder) / denom;
						curval = curval + (f / 65536);
						f = f % 65536;
						break;
					}
					/* lab32: */break;
				}
				if (curval >= 16384) {
					aritherror = true;
				} else {
					curval = curval * 65536 + f;
				}
				break;
			}
			/* lab30: */ {
				getxtoken();
				if (curcmd != 10) {
					unreadToken();
				}
			}
			break;
		}
		/* lab89: */if (aritherror || (Math.abs(curval) >= 1073741824)) {
			{
				printnl(262);
				print(727);
			}
			{
				helpptr = 2;
				helpline[1] = 728;
				helpline[0] = 729;
			}
			errorLogic.error();
			curval = 1073741823;
			aritherror = false;
		}
		if (negative) {
			curval = -curval;
		}
	}

	void scanglue(final int level) {
		/* 10 */boolean negative;
		int q;
		boolean mu;
		mu = (level == 3);
		negative = false;
		do {
			do {
				getxtoken();
			} while (!(curcmd != 10));
			if (curtok == 3117) {
				negative = !negative;
				curtok = 3115;
			}
		} while (!(curtok != 3115));
		if ((curcmd >= 68) && (curcmd <= 89)) {
			scansomethinginternal(level, negative);
			if (curvallevel >= 2) {
				if (curvallevel != level) {
					errorLogic.muerror();
				}
				return /* lab10 */;
			}
			if (curvallevel == 0) {
				scandimen(mu, false, true);
			} else if (level == 3) {
				errorLogic.muerror();
			}
		} else {
			unreadToken();
			scandimen(mu, false, false);
			if (negative) {
				curval = -curval;
			}
		}
		q = newspec(0);
		mem[q + 1].setInt(curval);
		if (scankeyword(730)) {
			scandimen(mu, true, false);
			mem[q + 2].setInt(curval);
			mem[q].setb0(curorder);
		}
		if (scankeyword(731)) {
			scandimen(mu, true, false);
			mem[q + 3].setInt(curval);
			mem[q].setb1(curorder);
		}
		curval = q;
	}

	int scanrulespec() {
		/* 21 */int Result;
		int q;
		q = newrule();
		if (curcmd == 35) {
			mem[q + 1].setInt(26214);
		} else {
			mem[q + 3].setInt(26214);
			mem[q + 2].setInt(0);
		}
		lab21: while (true) {
			if (scankeyword(732)) {
				scandimen(false, false, false);
				mem[q + 1].setInt(curval);
				continue lab21;
			}
			if (scankeyword(733)) {
				scandimen(false, false, false);
				mem[q + 3].setInt(curval);
				continue lab21;
			}
			if (scankeyword(734)) {
				scandimen(false, false, false);
				mem[q + 2].setInt(curval);
				continue lab21;
			}
			Result = q;
			break;
		}
		return Result;
	}

	int strtoks(final String s) {
		int p = memtop - 3;
		mem[p].setrh(0);
		for (int i = 0; i < s.length(); i++) {
			int t = s.charAt(i);
			if (t == 32) {
				t = 2592;
			} else {
				t = 3072 + t;
			}
			int q = avail;
			if (q == 0) {
				q = allocateMemoryWord();
			} else {
				avail = mem[q].getrh();
				mem[q].setrh(0);
			}
			mem[p].setrh(q);
			mem[q].setlh(t);
			p = q;
		}
		return p;
	}

	int thetoks() {
		int oldsetting;
		int p, q, r;
		getxtoken();
		scansomethinginternal(5, false);
		if (curvallevel >= 4) {
			p = memtop - 3;
			mem[p].setrh(0);
			if (curvallevel == 4) {
				q = allocateMemoryWord();
				mem[p].setrh(q);
				mem[q].setlh(4095 + curval);
				p = q;
			} else if (curval != 0) {
				r = mem[curval].getrh();
				while (r != 0) {
					{
						{
							q = avail;
							if (q == 0) {
								q = allocateMemoryWord();
							} else {
								avail = mem[q].getrh();
								mem[q].setrh(0);
							}
						}
						mem[p].setrh(q);
						mem[q].setlh(mem[r].getlh());
						p = q;
					}
					r = mem[r].getrh();
				}
			}
			return p;
		} else {
			oldsetting = selector;
			selector = 21;
			final StringBuilder builder = new StringBuilder();
			stringPool.setInterceptingStringBuilder(builder);
			switch (curvallevel) {
				case 0:
					printInt(curval);
					break;
				case 1: {
					printFixed(curval);
					print(397);
				}
					break;
				case 2: {
					printspec(curval, 397);
					deleteglueref(curval);
				}
					break;
				case 3: {
					printspec(curval, 337);
					deleteglueref(curval);
				}
					break;
			}
			selector = oldsetting;
			stringPool.setInterceptingStringBuilder(null);
			return strtoks(builder.toString());
		}
	}

	void insthetoks() {
		mem[memtop - 12].setrh(thetoks());
		begintokenlist(mem[memtop - 3].getrh(), 4);
	}

	void convtoks() {
		int oldsetting;
		int c;
		int savescannerstatus;
		c = curchr;
		switch (c) {
			case 0:
			case 1:
				scanint();
				break;
			case 2:
			case 3: {
				savescannerstatus = scannerstatus;
				scannerstatus = 0;
				gettoken();
				scannerstatus = savescannerstatus;
			}
				break;
			case 4:
				scanfontident();
				break;
			case 5:
				break;
		}
		oldsetting = selector;
		selector = 21;
		final StringBuilder builder = new StringBuilder();
		stringPool.setInterceptingStringBuilder(builder);
		switch (c) {
			case 0:
				printInt(curval);
				break;
			case 1:
				printromanint(curval);
				break;
			case 2:
				if (curcs != 0) {
					sprintcs(curcs);
				} else {
					printchar(curchr);
				}
				break;
			case 3:
				printmeaning();
				break;
			case 4: {
				print(fontname[curval]);
				if (fontsize[curval] != fontdsize[curval]) {
					print(741);
					printFixed(fontsize[curval]);
					print(397);
				}
			}
				break;
			case 5:
				print(jobname);
				break;
		}
		selector = oldsetting;
		stringPool.setInterceptingStringBuilder(null);
		mem[memtop - 12].setrh(strtoks(builder.toString()));
		begintokenlist(mem[memtop - 3].getrh(), 4);
	}

	int scantoks(final boolean macrodef, final boolean xpand) {
		/* 40 30 31 32 */int Result;
		int t;
		int s;
		int p;
		int q;
		int unbalance;
		int hashbrace;
		if (macrodef) {
			scannerstatus = 2;
		} else {
			scannerstatus = 5;
		}
		warningindex = curcs;
		defref = allocateMemoryWord();
		mem[defref].setlh(0);
		p = defref;
		hashbrace = 0;
		t = 3120;
		lab40: while (true) {
			if (macrodef) {
				lab30: while (true) {
					{
						while (true) {
							gettoken();
							if (curtok < 768) {
								break /* lab31 */;
							}
							if (curcmd == 6) {
								s = 3328 + curchr;
								gettoken();
								if (curcmd == 1) {
									hashbrace = curtok;
									{
										q = allocateMemoryWord();
										mem[p].setrh(q);
										mem[q].setlh(curtok);
										p = q;
									}
									{
										q = allocateMemoryWord();
										mem[p].setrh(q);
										mem[q].setlh(3584);
										p = q;
									}
									break lab30;
								}
								if (t == 3129) {
									{
										printnl(262);
										print(744);
									}
									{
										helpptr = 1;
										helpline[0] = 745;
									}
									errorLogic.error();
								} else {
									t = t + 1;
									if (curtok != t) {
										{
											printnl(262);
											print(746);
										}
										{
											helpptr = 2;
											helpline[1] = 747;
											helpline[0] = 748;
										}
										errorLogic.backerror();
									}
									curtok = s;
								}
							}
							{
								q = allocateMemoryWord();
								mem[p].setrh(q);
								mem[q].setlh(curtok);
								p = q;
							}
						}
						/* lab31: */ {
							q = allocateMemoryWord();
							mem[p].setrh(q);
							mem[q].setlh(3584);
							p = q;
						}
						if (curcmd == 2) {
							{
								printnl(262);
								print(657);
							}
							alignstate = alignstate + 1;
							{
								helpptr = 2;
								helpline[1] = 742;
								helpline[0] = 743;
							}
							errorLogic.error();
							break lab40;
						}
						break;
					}
					/* lab30: */}
			} else {
				scanleftbrace();
			}
			unbalance = 1;
			while (true) {
				if (xpand) {
					while (true) {
						getnext();
						if (curcmd <= 100) {
							break /* lab32 */;
						}
						if (curcmd != 109) {
							expand();
						} else {
							q = thetoks();
							if (mem[memtop - 3].getrh() != 0) {
								mem[p].setrh(mem[memtop - 3].getrh());
								p = q;
							}
						}
					}
					/* lab32: */xtoken();
				} else {
					gettoken();
				}
				if (curtok < 768) {
					if (curcmd < 2) {
						unbalance = unbalance + 1;
					} else {
						unbalance = unbalance - 1;
						if (unbalance == 0) {
							break lab40;
						}
					}
				} else if (curcmd == 6) {
					if (macrodef) {
						s = curtok;
						if (xpand) {
							getxtoken();
						} else {
							gettoken();
						}
						if (curcmd != 6) {
							if ((curtok <= 3120) || (curtok > t)) {
								{
									printnl(262);
									print(749);
								}
								sprintcs(warningindex);
								{
									helpptr = 3;
									helpline[2] = 750;
									helpline[1] = 751;
									helpline[0] = 752;
								}
								errorLogic.backerror();
								curtok = s;
							} else {
								curtok = 1232 + curchr;
							}
						}
					}
				}
				{
					q = allocateMemoryWord();
					mem[p].setrh(q);
					mem[q].setlh(curtok);
					p = q;
				}
			}
		}
		/* lab40: */scannerstatus = 0;
		if (hashbrace != 0) {
			q = allocateMemoryWord();
			mem[p].setrh(q);
			mem[q].setlh(hashbrace);
			p = q;
		}
		Result = p;
		return Result;
	}

	void readtoks(final int n, final int r) {
		/* 30 */int p;
		int q;
		int s;
		int m;
		scannerstatus = 2;
		warningindex = r;
		defref = allocateMemoryWord();
		mem[defref].setlh(0);
		p = defref;
		{
			q = allocateMemoryWord();
			mem[p].setrh(q);
			mem[q].setlh(3584);
			p = q;
		}
		if ((n < 0) || (n > 15)) {
			m = 16;
		} else {
			m = n;
		}
		s = alignstate;
		alignstate = 1000000;
		do {
			beginfilereading();
			curinput.setName(m + 1);
			if (readopen[m] == 2) {
				errorLogic.fatalError("*** (cannot \read from terminal in nonstop modes)");
			} else if (readopen[m] == 1) {
				if (inputln(readfile[m], false)) {
					readopen[m] = 0;
				} else {
					readfile[m].close();
					readopen[m] = 2;
				}
			} else {
				if (!inputln(readfile[m], true)) {
					readfile[m].close();
					readopen[m] = 2;
					if (alignstate != 1000000) {
						runaway();
						{
							printnl(262);
							print(754);
						}
						printEscapeSequence(534);
						{
							helpptr = 1;
							helpline[0] = 755;
						}
						alignstate = 1000000;
						errorLogic.error();
					}
				}
			}
			curinput.setLimit(last);
			if ((eqtb[9611].getInt() < 0) || (eqtb[9611].getInt() > 255)) {
				curinput.setLimit(curinput.getLimit() - 1);
			} else {
				buffer[curinput.getLimit()] = eqtb[9611].getInt();
			}
			first = curinput.getLimit() + 1;
			curinput.setLoc(curinput.getStart());
			curinput.setState(TOKENIZER_STATE_NEW_LINE);
			while (true) {
				gettoken();
				if (curtok == 0) {
					break /* lab30 */;
				}
				if (alignstate < 1000000) {
					do {
						gettoken();
					} while (!(curtok == 0));
					alignstate = 1000000;
					break /* lab30 */;
				}
				{
					q = allocateMemoryWord();
					mem[p].setrh(q);
					mem[q].setlh(curtok);
					p = q;
				}
			}
			/* lab30: */endfilereading();
		} while (!(alignstate == 1000000));
		curval = defref;
		scannerstatus = 0;
		alignstate = s;
	}

	void passtext() {
		/* 30 */int l;
		int savescannerstatus;
		savescannerstatus = scannerstatus;
		scannerstatus = 1;
		l = 0;
		skipline = line;
		while (true) {
			getnext();
			if (curcmd == 106) {
				if (l == 0) {
					break /* lab30 */;
				}
				if (curchr == 2) {
					l = l - 1;
				}
			} else if (curcmd == 105) {
				l = l + 1;
			}
		}
		/* lab30: */scannerstatus = savescannerstatus;
	}

	void changeiflimit(final int l, final int p) {
		/* 10 */int q;
		if (p == condptr) {
			iflimit = l;
		} else {
			q = condptr;
			while (true) {
				if (q == 0) {
					errorLogic.confusion(756);
				}
				if (mem[q].getrh() == p) {
					mem[q].setb0(l);
					return /* lab10 */;
				}
				q = mem[q].getrh();
			}
		}
	}

	void conditional() {
		/* 10 50 */boolean b;
		int r;
		int m, n;
		int p, q;
		int savescannerstatus;
		int savecondptr;
		int thisif;
		b = false;
		{
			p = getnode(2);
			mem[p].setrh(condptr);
			mem[p].setb0(iflimit);
			mem[p].setb1(curif);
			mem[p + 1].setInt(ifline);
			condptr = p;
			curif = curchr;
			iflimit = 1;
			ifline = line;
		}
		savecondptr = condptr;
		thisif = curchr;
		lab50: while (true) {
			switch (thisif) {
				case 0:
				case 1: {
					{
						getxtoken();
						if (curcmd == 0) {
							if (curchr == 257) {
								curcmd = 13;
								curchr = curtok - 4096;
							}
						}
					}
					if ((curcmd > 13) || (curchr > 255)) {
						m = 0;
						n = 256;
					} else {
						m = curcmd;
						n = curchr;
					}
					{
						getxtoken();
						if (curcmd == 0) {
							if (curchr == 257) {
								curcmd = 13;
								curchr = curtok - 4096;
							}
						}
					}
					if ((curcmd > 13) || (curchr > 255)) {
						curcmd = 0;
						curchr = 256;
					}
					if (thisif == 0) {
						b = (n == curchr);
					} else {
						b = (m == curcmd);
					}
				}
					break;
				case 2:
				case 3: {
					if (thisif == 2) {
						scanint();
					} else {
						scandimen(false, false, false);
					}
					n = curval;
					do {
						getxtoken();
					} while (!(curcmd != 10));
					if ((curtok >= 3132) && (curtok <= 3134)) {
						r = curtok - 3072;
					} else {
						{
							printnl(262);
							print(780);
						}
						printcmdchr(105, thisif);
						{
							helpptr = 1;
							helpline[0] = 781;
						}
						errorLogic.backerror();
						r = 61;
					}
					if (thisif == 2) {
						scanint();
					} else {
						scandimen(false, false, false);
					}
					switch (r) {
						case 60:
							b = (n < curval);
							break;
						case 61:
							b = (n == curval);
							break;
						case 62:
							b = (n > curval);
							break;
					}
				}
					break;
				case 4: {
					scanint();
					b = ((curval) % 2 == 1);
				}
					break;
				case 5:
					b = (Math.abs(curlist.modefield) == 1);
					break;
				case 6:
					b = (Math.abs(curlist.modefield) == 102);
					break;
				case 7:
					b = (Math.abs(curlist.modefield) == 203);
					break;
				case 8:
					b = (curlist.modefield < 0);
					break;
				case 9:
				case 10:
				case 11: {
					scaneightbitint();
					p = eqtb[7978 + curval].getrh();
					if (thisif == 9) {
						b = (p == 0);
					} else if (p == 0) {
						b = false;
					} else if (thisif == 10) {
						b = (mem[p].getb0() == 0);
					} else {
						b = (mem[p].getb0() == 1);
					}
				}
					break;
				case 12: {
					savescannerstatus = scannerstatus;
					scannerstatus = 0;
					getnext();
					n = curcs;
					p = curcmd;
					q = curchr;
					getnext();
					if (curcmd != p) {
						b = false;
					} else if (curcmd < 111) {
						b = (curchr == q);
					} else {
						p = mem[curchr].getrh();
						q = mem[eqtb[n].getrh()].getrh();
						if (p == q) {
							b = true;
						} else {
							while ((p != 0) && (q != 0)) {
								if (mem[p].getlh() != mem[q].getlh()) {
									p = 0;
								} else {
									p = mem[p].getrh();
									q = mem[q].getrh();
								}
							}
							b = ((p == 0) && (q == 0));
						}
					}
					scannerstatus = savescannerstatus;
				}
					break;
				case 13: {
					scanfourbitint();
					b = (readopen[curval] == 2);
				}
					break;
				case 14:
					b = true;
					break;
				case 15:
					b = false;
					break;
				case 16: {
					scanint();
					n = curval;
					if (eqtb[9599].getInt() > 1) {
						begindiagnostic();
						print(782);
						printInt(n);
						printchar(125);
						enddiagnostic(false);
					}
					while (n != 0) {
						passtext();
						if (condptr == savecondptr) {
							if (curchr == 4) {
								n = n - 1;
							} else {
								break lab50;
							}
						} else if (curchr == 2) {
							p = condptr;
							ifline = mem[p + 1].getInt();
							curif = mem[p].getb1();
							iflimit = mem[p].getb0();
							condptr = mem[p].getrh();
							freenode(p, 2);
						}
					}
					changeiflimit(4, savecondptr);
					return /* lab10 */;
				}
			}
			if (eqtb[9599].getInt() > 1) {
				begindiagnostic();
				if (b) {
					print(778);
				} else {
					print(779);
				}
				enddiagnostic(false);
			}
			if (b) {
				changeiflimit(3, savecondptr);
				return /* lab10 */;
			}
			while (true) {
				passtext();
				if (condptr == savecondptr) {
					if (curchr != 4) {
						break lab50;
					}
					{
						printnl(262);
						print(776);
					}
					printEscapeSequence(774);
					{
						helpptr = 1;
						helpline[0] = 777;
					}
					errorLogic.error();
				} else if (curchr == 2) {
					p = condptr;
					ifline = mem[p + 1].getInt();
					curif = mem[p].getb1();
					iflimit = mem[p].getb0();
					condptr = mem[p].getrh();
					freenode(p, 2);
				}
			}
		}
		/* lab50: */if (curchr == 2) {
			p = condptr;
			ifline = mem[p + 1].getInt();
			curif = mem[p].getb1();
			iflimit = mem[p].getb0();
			condptr = mem[p].getrh();
			freenode(p, 2);
		} else {
			iflimit = 2;
		}
	}

	void beginname() {
		areadelimiter = 0;
		extdelimiter = 0;
	}

	boolean morename(final int c) {
		if (c == 32) {
			return false;
		}
		stringPool.append((char)c);
		if (c == '>' || c == ':') {
			areadelimiter = stringPool.getBuiltLength();
			extdelimiter = 0;
		} else if (c == '.' && extdelimiter == 0) {
			extdelimiter = stringPool.getBuiltLength();
		}
		return true;
	}

	void endname() {
		if (strptr + 3 > maxstrings) {
			errorLogic.overflow(258, maxstrings - initstrptr);
		}
		if (areadelimiter == 0) {
			curarea = 338;
		} else {
			curarea = strptr;
			strstart[strptr + 1] = strstart[strptr] + areadelimiter;
			strptr = strptr + 1;
		}
		if (extdelimiter == 0) {
			curext = 338;
			curname = stringPool.makeString();
		} else {
			curname = strptr;
			strstart[strptr + 1] = strstart[strptr] + extdelimiter - areadelimiter - 1;
			strptr = strptr + 1;
			curext = stringPool.makeString();
		}
	}

	void packfilename(final int n, final int a, final int e) {
		nameoffile = stringPool.getString(a) + stringPool.getString(n) + stringPool.getString(e);
		namelength = nameoffile.length();
	}

	void packbufferedname(final int n, final int a, int b) {
		int j;
		final StringBuilder strbuf = new StringBuilder();
		final String plainFmt = "TEXFORMATS:plain.fmt";
		if (n + b - a + 5 > filenamesize) {
			b = a + filenamesize - n - 5;
		}
		for (j = 0; j <= n - 1; j++) {
			strbuf.append((plainFmt.charAt(j)));
		}
		for (j = a; j <= b; j++) {
			strbuf.append((char)(buffer[j]));
		}
		strbuf.append(".fmt");
		nameoffile = strbuf.toString();
		namelength = nameoffile.length();
	}

	int makenamestring() {
		if (strptr == maxstrings || stringPool.getBuiltLength() > 0) {
			return 63;
		} else {
			for (int k = 0; k <= namelength - 1; k++) {
				stringPool.append(nameoffile.charAt(k));
			}
			return stringPool.makeString();
		}
	}

	void scanfilename() {
		nameinprogress = true;
		beginname();
		do {
			getxtoken();
		} while (!(curcmd != 10));
		while (true) {
			if ((curcmd > 12) || (curchr > 255)) {
				unreadToken();
				break;
			}
			if (!morename(curchr)) {
				break;
			}
			getxtoken();
		}
		endname();
		nameinprogress = false;
	}

	void packjobname(final int s) {
		curarea = 338;
		curext = s;
		curname = jobname;
		packfilename(curname, curarea, curext);
	}

	void openlogfile() {
		int oldsetting;
		int k;
		int l;
		// String months;
		oldsetting = selector;
		packjobname(797);
		try {
			logfile = new TexFilePrintWriter("texlog.txt");
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		makenamestring();
		selector = 18;
		logfile.print("This is TeX, Version 3.14159");
		print(formatident);
		print(800);
		printInt(eqtb[9584].getInt());
		printchar(32);
		// months = "JANFEBMARAPRMAYJUNJULAUGSEPOCTNOVDEC";
		// StringBuffer strbuf = new StringBuffer(months);
		//			for (k = 3 * eqtb[9585].getInt() - 3; k <= 3 * eqtb[9585].getInt() - 1; k++) {
		//				logfile.print(strbuf.charAt(k));
		//			}
		printchar(32);
		printInt(eqtb[9586].getInt());
		printchar(32);
		printTwoDigits(eqtb[9583].getInt() / 60);
		printchar(58);
		printTwoDigits(eqtb[9583].getInt() % 60);
		inputStackBackingArray[inputptr].copyFrom(curinput);
		printnl(798);
		l = inputStackBackingArray[0].getLimit();
		if (buffer[l] == eqtb[9611].getInt()) {
			l = l - 1;
		}
		for (k = 1; k <= l; k++) {
			print(buffer[k]);
		}
		println();
		selector = oldsetting + 2;
	}

	void startinput() {
		scanfilename();
		if (curext == 338) {
			curext = 791;
		}
		packfilename(curname, curarea, curext);
		while (true) {
			beginfilereading();
			thisfile = new File(nameoffile);
			if (thisfile.exists()) {
				inputfile[curinput.getIndex()] = Input.from(thisfile);
				break;
			}
			if (curarea == 338) {
				packfilename(curname, 783, curext);
				thisfile = new File(nameoffile);
				if (thisfile.exists()) {
					inputfile[curinput.getIndex()] = Input.from(thisfile);
					break;
				}
			}
			endfilereading();
			printnl(262);
			print(788);
			printfilename(curname, curarea, curext);
			print(790);
			showcontext();
			printnl(792);
			print(787);
			errorLogic.fatalError("*** (job aborted, file error in nonstop mode)");
		}
		curinput.setName(makenamestring());
		if ((termoffset > 0) || (fileoffset > 0)) {
			printchar(32);
		}
		printchar(40);
		openparens = openparens + 1;
		print(curinput.getName());
		termout.flush();
		curinput.setState(TOKENIZER_STATE_NEW_LINE);
		if (curinput.getName() == strptr - 1) {
			stringPool.unmakeString();
			curinput.setName(curname);
		}
		{
			line = 1;
			inputln(inputfile[curinput.getIndex()], false);
			firmuptheline();
			if ((eqtb[9611].getInt() < 0) || (eqtb[9611].getInt() > 255)) {
				curinput.setLimit(curinput.getLimit() - 1);
			} else {
				buffer[curinput.getLimit()] = eqtb[9611].getInt();
			}
			first = curinput.getLimit() + 1;
			curinput.setLoc(curinput.getStart());
		}
	}

	int readfontinfo(final int u, final int nom, final int aire, final int s) {
		/* 30 11 45 */int Result;
		int k;
		boolean fileopened;
		int lf, lh, bc, ec, nw, nh, nd, ni, nl, nk, ne, np;
		int f;
		int g;
		int a, b, c, d;
		final fourquarters qw = new fourquarters();
		int sw;
		int bchlabel;
		int bchar;
		int z;
		int alpha;
		int beta;
		g = 0;
		np = 0;
		f = 0;
		bchlabel = 0;
		bchar = 0;
		bc = 0;
		ec = 0;
		lf = 0;
		nl = 0;
		fileopened = false;
		qw.copy(nullcharacter);
		lab30: while (true) {
			lab11: while (true) {
				try {
					fileopened = false;
					if (aire == 338) {
						packfilename(nom, 784, 811);
					} else {
						packfilename(nom, aire, 811);
					}
					thisfile = new File(nameoffile);
					if (thisfile.exists()) {
						tfmfile = new TexFileDataInputStream(thisfile);
					} else {
						break lab11;
					}
					fileopened = true;
					{
						lf = tfmfile.read();
						if (lf > 127) {
							break lab11;
						}
						lf = lf * 256 + tfmfile.read();
						lh = tfmfile.read();
						if (lh > 127) {
							break lab11;
						}
						lh = lh * 256 + tfmfile.read();
						bc = tfmfile.read();
						if (bc > 127) {
							break lab11;
						}
						bc = bc * 256 + tfmfile.read();
						ec = tfmfile.read();
						if (ec > 127) {
							break lab11;
						}
						ec = ec * 256 + tfmfile.read();
						if ((bc > ec + 1) || (ec > 255)) {
							break lab11;
						}
						if (bc > 255) {
							bc = 1;
							ec = 0;
						}
						nw = tfmfile.read();
						if (nw > 127) {
							break lab11;
						}
						nw = nw * 256 + tfmfile.read();
						nh = tfmfile.read();
						if (nh > 127) {
							break lab11;
						}
						nh = nh * 256 + tfmfile.read();
						nd = tfmfile.read();
						if (nd > 127) {
							break lab11;
						}
						nd = nd * 256 + tfmfile.read();
						ni = tfmfile.read();
						if (ni > 127) {
							break lab11;
						}
						ni = ni * 256 + tfmfile.read();
						nl = tfmfile.read();
						if (nl > 127) {
							break lab11;
						}
						nl = nl * 256 + tfmfile.read();
						nk = tfmfile.read();
						if (nk > 127) {
							break lab11;
						}
						nk = nk * 256 + tfmfile.read();
						ne = tfmfile.read();
						if (ne > 127) {
							break lab11;
						}
						ne = ne * 256 + tfmfile.read();
						np = tfmfile.read();
						if (np > 127) {
							break lab11;
						}
						np = np * 256 + tfmfile.read();
						if (lf != 6 + lh + (ec - bc + 1) + nw + nh + nd + ni + nl + nk + ne + np) {
							break lab11;
						}
					}
					lf = lf - 6 - lh;
					if (np < 7) {
						lf = lf + 7 - np;
					}
					if ((fontptr == fontmax) || (fmemptr + lf > fontmemsize)) {
						printnl(262);
						print(802);
						sprintcs(u);
						printchar(61);
						printfilename(nom, aire, 338);
						if (s >= 0) {
							print(741);
							printFixed(s);
							print(397);
						} else if (s != -1000) {
							print(803);
							printInt(-s);
						}
						print(812);
						{
							helpptr = 4;
							helpline[3] = 813;
							helpline[2] = 814;
							helpline[1] = 815;
							helpline[0] = 816;
						}
						errorLogic.error();
						break lab30;
					}
					f = fontptr + 1;
					charbase[f] = fmemptr - bc;
					widthbase[f] = charbase[f] + ec + 1;
					heightbase[f] = widthbase[f] + nw;
					depthbase[f] = heightbase[f] + nh;
					italicbase[f] = depthbase[f] + nd;
					ligkernbase[f] = italicbase[f] + ni;
					kernbase[f] = ligkernbase[f] + nl - 256 * (128);
					extenbase[f] = kernbase[f] + 256 * (128) + nk;
					parambase[f] = extenbase[f] + ne;
					{
						if (lh < 2) {
							break lab11;
						}
						{
							a = tfmfile.read();
							qw.b0 = a;
							b = tfmfile.read();
							qw.b1 = b;
							c = tfmfile.read();
							qw.b2 = c;
							d = tfmfile.read();
							qw.b3 = d;
							fontcheck[f].copy(qw);
						}
						{
							z = tfmfile.read();
							if (z > 127) {
								break lab11;
							}
							z = z * 256 + tfmfile.read();
						}
						z = z * 256 + tfmfile.read();
						z = (z * 16) + (tfmfile.read() / 16);
						if (z < 65536) {
							break lab11;
						}
						while (lh > 2) {
							tfmfile.read();
							tfmfile.read();
							tfmfile.read();
							tfmfile.read();
							lh = lh - 1;
						}
						fontdsize[f] = z;
						if (s != -1000) {
							if (s >= 0) {
								z = s;
							} else {
								z = xnoverd(z, -s, 1000);
							}
						}
						fontsize[f] = z;
					}
					for (k = fmemptr; k <= widthbase[f] - 1; k++) {
						{
							a = tfmfile.read();
							qw.b0 = a;
							b = tfmfile.read();
							qw.b1 = b;
							c = tfmfile.read();
							qw.b2 = c;
							d = tfmfile.read();
							qw.b3 = d;
							fontinfo[k].setqqqq(qw);
						}
						if ((a >= nw) || (b / 16 >= nh) || (b % 16 >= nd) || (c / 4 >= ni)) {
							break lab11;
						}
						switch (c % 4) {
							case 1:
								if (d >= nl) {
									break lab11;
								}
								break;
							case 3:
								if (d >= ne) {
									break lab11;
								}
								break;
							case 2: {
								{
									if ((d < bc) || (d > ec)) {
										break lab11;
									}
								}
								lab45: while (true) {
									while (d < k + bc - fmemptr) {
										qw.copy(fontinfo[charbase[f] + d].qqqq());
										if (((qw.b2) % 4) != 2) {
											break lab45;
										}
										d = qw.b3;
									}
									if (d == k + bc - fmemptr) {
										break lab11;
									}
									break;
								}
								/* lab45: */}
								break;
							default:
								;
								break;
						}
					}
					{
						{
							alpha = 16;
							while (z >= 8388608) {
								z = z / 2;
								alpha = alpha + alpha;
							}
							beta = 256 / alpha;
							alpha = alpha * z;
						}
						for (k = widthbase[f]; k <= ligkernbase[f] - 1; k++) {
							a = tfmfile.read();
							b = tfmfile.read();
							c = tfmfile.read();
							d = tfmfile.read();
							sw = (((((d * z) / 256) + (c * z)) / 256) + (b * z)) / beta;
							if (a == 0) {
								fontinfo[k].setInt(sw);
							} else if (a == 255) {
								fontinfo[k].setInt(sw - alpha);
							} else {
								break lab11;
							}
						}
						if (fontinfo[widthbase[f]].getInt() != 0) {
							break lab11;
						}
						if (fontinfo[heightbase[f]].getInt() != 0) {
							break lab11;
						}
						if (fontinfo[depthbase[f]].getInt() != 0) {
							break lab11;
						}
						if (fontinfo[italicbase[f]].getInt() != 0) {
							break lab11;
						}
					}
					bchlabel = 32767;
					bchar = 256;
					if (nl > 0) {
						for (k = ligkernbase[f]; k <= kernbase[f] + 256 * (128) - 1; k++) {
							{
								a = tfmfile.read();
								qw.b0 = a;
								b = tfmfile.read();
								qw.b1 = b;
								c = tfmfile.read();
								qw.b2 = c;
								d = tfmfile.read();
								qw.b3 = d;
								fontinfo[k].setqqqq(qw);
							}
							if (a > 128) {
								if (256 * c + d >= nl) {
									break lab11;
								}
								if (a == 255) {
									if (k == ligkernbase[f]) {
										bchar = b;
									}
								}
							} else {
								if (b != bchar) {
									{
										if ((b < bc) || (b > ec)) {
											break lab11;
										}
									}
									qw.copy(fontinfo[charbase[f] + b].qqqq());
									if (!(qw.b0 > 0)) {
										break lab11;
									}
								}
								if (c < 128) {
									{
										if ((d < bc) || (d > ec)) {
											break lab11;
										}
									}
									qw.copy(fontinfo[charbase[f] + d].qqqq());
									if (!(qw.b0 > 0)) {
										break lab11;
									}
								} else if (256 * (c - 128) + d >= nk) {
									break lab11;
								}
								if (a < 128) {
									if (k - ligkernbase[f] + a + 1 >= nl) {
										break lab11;
									}
								}
							}
						}
						if (a == 255) {
							bchlabel = 256 * c + d;
						}
					}
					for (k = kernbase[f] + 256 * (128); k <= extenbase[f] - 1; k++) {
						a = tfmfile.read();
						b = tfmfile.read();
						c = tfmfile.read();
						d = tfmfile.read();
						sw = (((((d * z) / 256) + (c * z)) / 256) + (b * z)) / beta;
						if (a == 0) {
							fontinfo[k].setInt(sw);
						} else if (a == 255) {
							fontinfo[k].setInt(sw - alpha);
						} else {
							break lab11;
						}
					}
					for (k = extenbase[f]; k <= parambase[f] - 1; k++) {
						{
							a = tfmfile.read();
							qw.b0 = a;
							b = tfmfile.read();
							qw.b1 = b;
							c = tfmfile.read();
							qw.b2 = c;
							d = tfmfile.read();
							qw.b3 = d;
							fontinfo[k].setqqqq(qw);
						}
						if (a != 0) {
							{
								if ((a < bc) || (a > ec)) {
									break lab11;
								}
							}
							qw.copy(fontinfo[charbase[f] + a].qqqq());
							if (!(qw.b0 > 0)) {
								break lab11;
							}
						}
						if (b != 0) {
							{
								if ((b < bc) || (b > ec)) {
									break lab11;
								}
							}
							qw.copy(fontinfo[charbase[f] + b].qqqq());
							if (!(qw.b0 > 0)) {
								break lab11;
							}
						}
						if (c != 0) {
							{
								if ((c < bc) || (c > ec)) {
									break lab11;
								}
							}
							qw.copy(fontinfo[charbase[f] + c].qqqq());
							if (!(qw.b0 > 0)) {
								break lab11;
							}
						}
						{
							{
								if ((d < bc) || (d > ec)) {
									break lab11;
								}
							}
							qw.copy(fontinfo[charbase[f] + d].qqqq());
							if (!(qw.b0 > 0)) {
								break lab11;
							}
						}
					}
					{
						for (k = 1; k <= np; k++) {
							if (k == 1) {
								sw = tfmfile.read();
								if (sw > 127) {
									sw = sw - 256;
								}
								sw = sw * 256 + tfmfile.read();
								sw = sw * 256 + tfmfile.read();
								fontinfo[parambase[f]].setInt((sw * 16) + (tfmfile.read() / 16));
							} else {
								a = tfmfile.read();
								b = tfmfile.read();
								c = tfmfile.read();
								d = tfmfile.read();
								sw = (((((d * z) / 256) + (c * z)) / 256) + (b * z)) / beta;
								if (a == 0) {
									fontinfo[parambase[f] + k - 1].setInt(sw);
								} else if (a == 255) {
									fontinfo[parambase[f] + k - 1].setInt(sw - alpha);
								} else {
									break lab11;
								}
							}
						}
						for (k = np + 1; k <= 7; k++) {
							fontinfo[parambase[f] + k - 1].setInt(0);
						}
					}
				} catch (final IOException ex) {
					break lab11;
				}
				if (np >= 7) {
					fontparams[f] = np;
				} else {
					fontparams[f] = 7;
				}
				hyphenchar[f] = eqtb[9609].getInt();
				skewchar[f] = eqtb[9610].getInt();
				if (bchlabel < nl) {
					bcharlabel[f] = bchlabel + ligkernbase[f];
				} else {
					bcharlabel[f] = 0;
				}
				fontbchar[f] = bchar;
				fontfalsebchar[f] = bchar;
				if (bchar <= ec) {
					if (bchar >= bc) {
						qw.copy(fontinfo[charbase[f] + bchar].qqqq());
						if ((qw.b0 > 0)) {
							fontfalsebchar[f] = 256;
						}
					}
				}
				fontname[f] = nom;
				fontarea[f] = aire;
				fontbc[f] = bc;
				fontec[f] = ec;
				fontglue[f] = 0;
				charbase[f] = charbase[f];
				widthbase[f] = widthbase[f];
				ligkernbase[f] = ligkernbase[f];
				kernbase[f] = kernbase[f];
				extenbase[f] = extenbase[f];
				parambase[f] = parambase[f] - 1;
				fmemptr = fmemptr + lf;
				fontptr = f;
				g = f;
				break lab30;
			}
			{
				printnl(262);
				print(802);
			}
			sprintcs(u);
			printchar(61);
			printfilename(nom, aire, 338);
			if (s >= 0) {
				print(741);
				printFixed(s);
				print(397);
			} else if (s != -1000) {
				print(803);
				printInt(-s);
			}
			if (fileopened) {
				print(804);
			} else {
				print(805);
			}
			{
				helpptr = 5;
				helpline[4] = 806;
				helpline[3] = 807;
				helpline[2] = 808;
				helpline[1] = 809;
				helpline[0] = 810;
			}
			errorLogic.error();
			break;
		}
		/* lab30: */if (fileopened) {
			tfmfile.close();
		}
		Result = g;
		return Result;
	}

	int newcharacter(final int f, final int c) {
		/* 10 */int Result;
		int p;
		if (fontbc[f] <= c) {
			if (fontec[f] >= c) {
				if ((fontinfo[charbase[f] + c].getb0() > 0)) {
					p = allocateMemoryWord();
					mem[p].setb0(f);
					mem[p].setb1(c);
					Result = p;
					return Result /* lab10 */;
				}
			}
		}
		errorLogic.charwarning(f, c);
		Result = 0;
		return Result;
	}

	void writedvi(final int a, final int b) {
		int k;
		try {
			for (k = a; k <= b; k++) {
				dvifile.write(dvibuf[k]);
			}
		} catch (final IOException ex) {
			;
		}
	}

	public void dviswap() {
		if (dvilimit == dvibufsize) {
			writedvi(0, halfbuf - 1);
			dvilimit = halfbuf;
			dvioffset = dvioffset + dvibufsize;
			dviptr = 0;
		} else {
			writedvi(halfbuf, dvibufsize - 1);
			dvilimit = dvibufsize;
		}
		dvigone = dvigone + halfbuf;
	}

	void dvipop(final int l) {
		if ((l == dvioffset + dviptr) && (dviptr > 0)) {
			dviptr = dviptr - 1;
		} else {
			dviWriter.writeByte(142);
		}
	}

	void dvifontdef(final int f) {
		dviWriter.writeByte(243);
		dviWriter.writeByte(f - 1);
		dviWriter.writeByte(fontcheck[f].b0);
		dviWriter.writeByte(fontcheck[f].b1);
		dviWriter.writeByte(fontcheck[f].b2);
		dviWriter.writeByte(fontcheck[f].b3);
		dviWriter.writeInt(fontsize[f]);
		dviWriter.writeInt(fontdsize[f]);
		dviWriter.writeByte(strstart[fontarea[f] + 1] - strstart[fontarea[f]]);
		dviWriter.writeByte(strstart[fontname[f] + 1] - strstart[fontname[f]]);
		dviWriteStringBytes(stringPool.getString(fontarea[f]));
		dviWriteStringBytes(stringPool.getString(fontname[f]));
	}

	void dviWriteStringBytes(final String s) {
		for (int i = 0; i < s.length(); i++) {
			dviWriter.writeByte(s.charAt(i));
		}
	}

	void movement(int w, final int o) {
		int mstate;
		int p, q;
		int k;
		q = getnode(3);
		mem[q + 1].setInt(w);
		mem[q + 2].setInt(dvioffset + dviptr);
		if (o == 157) {
			mem[q].setrh(downptr);
			downptr = q;
		} else {
			mem[q].setrh(rightptr);
			rightptr = q;
		}
		lab40: while (true) {
			p = mem[q].getrh();
			mstate = 0;
			lab45: while (p != 0) {
				if (mem[p + 1].getInt() == w) {
					switch (mstate + mem[p].getlh()) {
						case 3:
						case 4:
						case 15:
						case 16:
							if (mem[p + 2].getInt() < dvigone) {
								break lab45;
							} else {
								k = mem[p + 2].getInt() - dvioffset;
								if (k < 0) {
									k = k + dvibufsize;
								}
								dvibuf[k] = dvibuf[k] + 5;
								mem[p].setlh(1);
								break lab40;
							}
						case 5:
						case 9:
						case 11:
							if (mem[p + 2].getInt() < dvigone) {
								break lab45;
							} else {
								k = mem[p + 2].getInt() - dvioffset;
								if (k < 0) {
									k = k + dvibufsize;
								}
								dvibuf[k] = dvibuf[k] + 10;
								mem[p].setlh(2);
								break lab40;
							}
						case 1:
						case 2:
						case 8:
						case 13:
							break lab40;
						default:
							;
							break;
					}
				} else {
					switch (mstate + mem[p].getlh()) {
						case 1:
							mstate = 6;
							break;
						case 2:
							mstate = 12;
							break;
						case 8:
						case 13:
							break lab45;
						default:
							;
							break;
					}
				}
				p = mem[p].getrh();
			}
			/* lab45: */mem[q].setlh(3);
			if (Math.abs(w) >= 8388608) {
				dviWriter.writeByte(o + 3);
				dviWriter.writeInt(w);
				return /* lab10 */;
			}
			lab1: while (true) {
				if (Math.abs(w) >= 32768) {
					dviWriter.writeByte(o + 2);
					if (w < 0) {
						w = w + 16777216;
					}
					dviWriter.writeByte(w / 65536);
					w = w % 65536;
					dviWriter.writeByte(w / 256);
					break lab1;
				}
				if (Math.abs(w) >= 128) {
					dviWriter.writeByte(o + 1);
					if (w < 0) {
						w = w + 65536;
					}
					dviWriter.writeByte(w / 256);
					break lab1;
				}
				dviWriter.writeByte(o);
				if (w < 0) {
					w = w + 256;
				}
				break;
			}
			dviWriter.writeByte(w % 256);
			return;
		}
		mem[q].setlh(mem[p].getlh());
		if (mem[q].getlh() == 1) {
			dviWriter.writeByte(o + 4);
			while (mem[q].getrh() != p) {
				q = mem[q].getrh();
				switch (mem[q].getlh()) {
					case 3:
						mem[q].setlh(5);
						break;
					case 4:
						mem[q].setlh(6);
						break;
					default:
						;
						break;
				}
			}
		} else {
			dviWriter.writeByte(o + 9);
			while (mem[q].getrh() != p) {
				q = mem[q].getrh();
				switch (mem[q].getlh()) {
					case 3:
						mem[q].setlh(4);
						break;
					case 5:
						mem[q].setlh(6);
						break;
					default:
						;
						break;
				}
			}
		}
	}

	void prunemovements(final int l) {
		int p;
		while (downptr != 0) {
			if (mem[downptr + 2].getInt() < l) {
				break;
			}
			p = downptr;
			downptr = mem[p].getrh();
			freenode(p, 3);
		}
		while (rightptr != 0) {
			if (mem[rightptr + 2].getInt() < l) {
				return;
			}
			p = rightptr;
			rightptr = mem[p].getrh();
			freenode(p, 3);
		}
	}

	void specialout(final int p) {
		int oldsetting;
		if (curh != dvih) {
			movement(curh - dvih, 143);
			dvih = curh;
		}
		if (curv != dviv) {
			movement(curv - dviv, 157);
			dviv = curv;
		}
		oldsetting = selector;
		selector = 21;
		showtokenlist(mem[mem[p + 1].getrh()].getrh(), 0, Integer.MAX_VALUE);
		selector = oldsetting;
		final String s = stringPool.extractPartiallyBuiltString(true);
		if (s.length() < 256) {
			dviWriter.writeByte(239);
			dviWriter.writeByte(s.length());
		} else {
			dviWriter.writeByte(242);
			dviWriter.writeInt(s.length());
		}
		for (int i = 0; i < s.length(); i++) {
			dviWriter.writeByte(s.charAt(i));
		}
	}

	void writeout(final int p) {
		int oldsetting;
		int oldmode;
		int j;
		int q, r;
		q = allocateMemoryWord();
		mem[q].setlh(637);
		r = allocateMemoryWord();
		mem[q].setrh(r);
		mem[r].setlh(11017);
		begintokenlist(q, 4);
		begintokenlist(mem[p + 1].getrh(), 15);
		q = allocateMemoryWord();
		mem[q].setlh(379);
		begintokenlist(q, 4);
		oldmode = curlist.modefield;
		curlist.modefield = 0;
		curcs = writeloc;
		q = scantoks(false, true);
		gettoken();
		if (curtok != 11017) {
			{
				printnl(262);
				print(1297);
			}
			{
				helpptr = 2;
				helpline[1] = 1298;
				helpline[0] = 1012;
			}
			errorLogic.error();
			do {
				gettoken();
			} while (!(curtok == 11017));
		}
		curlist.modefield = oldmode;
		endtokenlist();
		oldsetting = selector;
		j = mem[p + 1].getlh();
		if (writeopen[j]) {
			selector = j;
		} else {
			if ((j == 17) && (selector == 19)) {
				selector = 18;
			}
			printnl(338);
		}
		tokenshow(defref);
		println();
		flushlist(defref);
		selector = oldsetting;
	}

	void outwhat(final int p) {
		int j;
		switch (mem[p].getb1()) {
			case 0:
			case 1:
			case 2:
				if (!doingleaders) {
					j = mem[p + 1].getlh();
					if (mem[p].getb1() == 1) {
						writeout(p);
					} else {
						if (writeopen[j]) {
							writefile[j].close();
						}
						if (mem[p].getb1() == 2) {
							writeopen[j] = false;
						} else if (j < 16) {
							curname = mem[p + 1].getrh();
							curarea = mem[p + 2].getlh();
							curext = mem[p + 2].getrh();
							if (curext == 338) {
								curext = 791;
							}
							packfilename(curname, curarea, curext);
							try {
								writefile[j] = new TexFilePrintWriter(nameoffile);
							} catch (final IOException e) {
								throw new RuntimeException(e);
							}
							writeopen[j] = true;
						}
					}
				}
				break;
			case 3:
				specialout(p);
				break;
			case 4:
				;
				break;
			default:
				errorLogic.confusion(1299);
				break;
		}
	}

	void hlistout() {
		/* 21 13 14 15 */int baseline;
		int leftedge;
		int saveh, savev;
		int thisbox;
		int gorder;
		int gsign;
		int p;
		int saveloc;
		int leaderbox;
		int leaderwd;
		int lx;
		boolean outerdoingleaders;
		int edge;
		double gluetemp;
		thisbox = tempptr;
		gorder = mem[thisbox + 5].getb1();
		gsign = mem[thisbox + 5].getb0();
		p = mem[thisbox + 5].getrh();
		curs = curs + 1;
		if (curs > 0) {
			dviWriter.writeByte(141);
		}
		if (curs > maxpush) {
			maxpush = curs;
		}
		saveloc = dvioffset + dviptr;
		baseline = curv;
		leftedge = curh;
		while (p != 0) {
			lab21: while (true) {
				if ((p >= himemmin)) {
					if (curh != dvih) {
						movement(curh - dvih, 143);
						dvih = curh;
					}
					if (curv != dviv) {
						movement(curv - dviv, 157);
						dviv = curv;
					}
					do {
						f = mem[p].getb0();
						final int c = mem[p].getb1();
						if (f != dvif) {
							if (!fontused[f]) {
								dvifontdef(f);
								fontused[f] = true;
							}
							if (f <= 64) {
								dviWriter.writeByte(f + 170);
							} else {
								dviWriter.writeByte(235);
								dviWriter.writeByte(f - 1);
							}
							dvif = f;
						}
						if (c >= 128) {
							dviWriter.writeByte(128);
						}
						dviWriter.writeByte(c);
						curh = curh + fontinfo[widthbase[f] + fontinfo[charbase[f] + c].getb0()].getInt();
						p = mem[p].getrh();
					} while (!(!(p >= himemmin)));
					dvih = curh;
				} else {
					lab15: while (true) {
						lab13: while (true) {
							lab14: while (true) {
								switch (mem[p].getb0()) {
									case 0:
									case 1:
										if (mem[p + 5].getrh() == 0) {
											curh = curh + mem[p + 1].getInt();
										} else {
											saveh = dvih;
											savev = dviv;
											curv = baseline + mem[p + 4].getInt();
											tempptr = p;
											edge = curh;
											if (mem[p].getb0() == 1) {
												vlistout();
											} else {
												hlistout();
											}
											dvih = saveh;
											dviv = savev;
											curh = edge + mem[p + 1].getInt();
											curv = baseline;
										}
										break;
									case 2: {
										ruleht = mem[p + 3].getInt();
										ruledp = mem[p + 2].getInt();
										rulewd = mem[p + 1].getInt();
										break lab14;
									}
									case 8:
										outwhat(p);
										break;
									case 10: {
										final int g = mem[p + 1].getlh();
										rulewd = mem[g + 1].getInt();
										if (gsign != 0) {
											if (gsign == 1) {
												if (mem[g].getb0() == gorder) {
													gluetemp = mem[thisbox + 6].getglue() * mem[g + 2].getInt();
													if (gluetemp > 1000000000.0) {
														gluetemp = 1000000000.0;
													} else if (gluetemp < -1000000000.0) {
														gluetemp = -1000000000.0;
													}
													rulewd = rulewd + (int)Math.round(gluetemp);
												}
											} else if (mem[g].getb1() == gorder) {
												gluetemp = mem[thisbox + 6].getglue() * mem[g + 3].getInt();
												if (gluetemp > 1000000000.0) {
													gluetemp = 1000000000.0;
												} else if (gluetemp < -1000000000.0) {
													gluetemp = -1000000000.0;
												}
												rulewd = rulewd - (int)Math.round(gluetemp);
											}
										}
										if (mem[p].getb1() >= 100) {
											leaderbox = mem[p + 1].getrh();
											if (mem[leaderbox].getb0() == 2) {
												ruleht = mem[leaderbox + 3].getInt();
												ruledp = mem[leaderbox + 2].getInt();
												break lab14;
											}
											leaderwd = mem[leaderbox + 1].getInt();
											if ((leaderwd > 0) && (rulewd > 0)) {
												rulewd = rulewd + 10;
												edge = curh + rulewd;
												lx = 0;
												if (mem[p].getb1() == 100) {
													saveh = curh;
													curh = leftedge + leaderwd * ((curh - leftedge) / leaderwd);
													if (curh < saveh) {
														curh = curh + leaderwd;
													}
												} else {
													lq = rulewd / leaderwd;
													lr = rulewd % leaderwd;
													if (mem[p].getb1() == 101) {
														curh = curh + (lr / 2);
													} else {
														lx = (2 * lr + lq + 1) / (2 * lq + 2);
														curh = curh + ((lr - (lq - 1) * lx) / 2);
													}
												}
												while (curh + leaderwd <= edge) {
													curv = baseline + mem[leaderbox + 4].getInt();
													if (curv != dviv) {
														movement(curv - dviv, 157);
														dviv = curv;
													}
													savev = dviv;
													if (curh != dvih) {
														movement(curh - dvih, 143);
														dvih = curh;
													}
													saveh = dvih;
													tempptr = leaderbox;
													outerdoingleaders = doingleaders;
													doingleaders = true;
													if (mem[leaderbox].getb0() == 1) {
														vlistout();
													} else {
														hlistout();
													}
													doingleaders = outerdoingleaders;
													dviv = savev;
													dvih = saveh;
													curv = baseline;
													curh = saveh + leaderwd + lx;
												}
												curh = edge - 10;
												break lab15;
											}
										}
										break lab13;
									}
									case 11:
									case 9:
										curh = curh + mem[p + 1].getInt();
										break;
									case 6: {
										mem[memtop - 12].copy(mem[p + 1]);
										mem[memtop - 12].setrh(mem[p].getrh());
										p = memtop - 12;
										continue lab21;
									}
									default:
										;
										break;
								}
								break lab15;
							}
							/* lab14: */if ((ruleht == -1073741824)) {
								ruleht = mem[thisbox + 3].getInt();
							}
							if ((ruledp == -1073741824)) {
								ruledp = mem[thisbox + 2].getInt();
							}
							ruleht = ruleht + ruledp;
							if ((ruleht > 0) && (rulewd > 0)) {
								if (curh != dvih) {
									movement(curh - dvih, 143);
									dvih = curh;
								}
								curv = baseline + ruledp;
								if (curv != dviv) {
									movement(curv - dviv, 157);
									dviv = curv;
								}
								dviWriter.writeByte(132);
								dviWriter.writeInt(ruleht);
								dviWriter.writeInt(rulewd);
								curv = baseline;
								dvih = dvih + rulewd;
							}
							break;
						}
						/* lab13: */curh = curh + rulewd;
						break;
					}
					/* lab15: */p = mem[p].getrh();
				}
				break;
			}
		}
		prunemovements(saveloc);
		if (curs > 0) {
			dvipop(saveloc);
		}
		curs = curs - 1;
	}

	void vlistout() {
		/* 13 14 15 */int leftedge;
		int topedge;
		int saveh, savev;
		int thisbox;
		int gorder;
		int gsign;
		int p;
		int saveloc;
		int leaderbox;
		int leaderht;
		int lx;
		boolean outerdoingleaders;
		int edge;
		double gluetemp;
		thisbox = tempptr;
		gorder = mem[thisbox + 5].getb1();
		gsign = mem[thisbox + 5].getb0();
		p = mem[thisbox + 5].getrh();
		curs = curs + 1;
		if (curs > 0) {
			dviWriter.writeByte(141);
		}
		if (curs > maxpush) {
			maxpush = curs;
		}
		saveloc = dvioffset + dviptr;
		leftedge = curh;
		curv = curv - mem[thisbox + 3].getInt();
		topedge = curv;
		while (p != 0) {
			lab15: while (true) {
				if ((p >= himemmin)) {
					errorLogic.confusion(828);
				} else {
					lab13: while (true) {
						lab14: while (true) {
							switch (mem[p].getb0()) {
								case 0:
								case 1:
									if (mem[p + 5].getrh() == 0) {
										curv = curv + mem[p + 3].getInt() + mem[p + 2].getInt();
									} else {
										curv = curv + mem[p + 3].getInt();
										if (curv != dviv) {
											movement(curv - dviv, 157);
											dviv = curv;
										}
										saveh = dvih;
										savev = dviv;
										curh = leftedge + mem[p + 4].getInt();
										tempptr = p;
										if (mem[p].getb0() == 1) {
											vlistout();
										} else {
											hlistout();
										}
										dvih = saveh;
										dviv = savev;
										curv = savev + mem[p + 2].getInt();
										curh = leftedge;
									}
									break;
								case 2: {
									ruleht = mem[p + 3].getInt();
									ruledp = mem[p + 2].getInt();
									rulewd = mem[p + 1].getInt();
									break lab14;
								}
								case 8:
									outwhat(p);
									break;
								case 10: {
									final int g = mem[p + 1].getlh();
									ruleht = mem[g + 1].getInt();
									if (gsign != 0) {
										if (gsign == 1) {
											if (mem[g].getb0() == gorder) {
												gluetemp = mem[thisbox + 6].getglue() * mem[g + 2].getInt();
												if (gluetemp > 1000000000.0) {
													gluetemp = 1000000000.0;
												} else if (gluetemp < -1000000000.0) {
													gluetemp = -1000000000.0;
												}
												ruleht = ruleht + (int)Math.round(gluetemp);
											}
										} else if (mem[g].getb1() == gorder) {
											gluetemp = mem[thisbox + 6].getglue() * mem[g + 3].getInt();
											if (gluetemp > 1000000000.0) {
												gluetemp = 1000000000.0;
											} else if (gluetemp < -1000000000.0) {
												gluetemp = -1000000000.0;
											}
											ruleht = ruleht - (int)Math.round(gluetemp);
										}
									}
									if (mem[p].getb1() >= 100) {
										leaderbox = mem[p + 1].getrh();
										if (mem[leaderbox].getb0() == 2) {
											rulewd = mem[leaderbox + 1].getInt();
											ruledp = 0;
											break lab14;
										}
										leaderht = mem[leaderbox + 3].getInt() + mem[leaderbox + 2].getInt();
										if ((leaderht > 0) && (ruleht > 0)) {
											ruleht = ruleht + 10;
											edge = curv + ruleht;
											lx = 0;
											if (mem[p].getb1() == 100) {
												savev = curv;
												curv = topedge + leaderht * ((curv - topedge) / leaderht);
												if (curv < savev) {
													curv = curv + leaderht;
												}
											} else {
												lq = ruleht / leaderht;
												lr = ruleht % leaderht;
												if (mem[p].getb1() == 101) {
													curv = curv + (lr / 2);
												} else {
													lx = (2 * lr + lq + 1) / (2 * lq + 2);
													curv = curv + ((lr - (lq - 1) * lx) / 2);
												}
											}
											while (curv + leaderht <= edge) {
												curh = leftedge + mem[leaderbox + 4].getInt();
												if (curh != dvih) {
													movement(curh - dvih, 143);
													dvih = curh;
												}
												saveh = dvih;
												curv = curv + mem[leaderbox + 3].getInt();
												if (curv != dviv) {
													movement(curv - dviv, 157);
													dviv = curv;
												}
												savev = dviv;
												tempptr = leaderbox;
												outerdoingleaders = doingleaders;
												doingleaders = true;
												if (mem[leaderbox].getb0() == 1) {
													vlistout();
												} else {
													hlistout();
												}
												doingleaders = outerdoingleaders;
												dviv = savev;
												dvih = saveh;
												curh = leftedge;
												curv = savev - mem[leaderbox + 3].getInt() + leaderht + lx;
											}
											curv = edge - 10;
											break lab15;
										}
									}
									break lab13;
								}
								case 11:
									curv = curv + mem[p + 1].getInt();
									break;
								default:
									;
									break;
							}
							break lab15;
						}
						/* lab14: */if ((rulewd == -1073741824)) {
							rulewd = mem[thisbox + 1].getInt();
						}
						ruleht = ruleht + ruledp;
						curv = curv + ruleht;
						if ((ruleht > 0) && (rulewd > 0)) {
							if (curh != dvih) {
								movement(curh - dvih, 143);
								dvih = curh;
							}
							if (curv != dviv) {
								movement(curv - dviv, 157);
								dviv = curv;
							}
							{
								dvibuf[dviptr] = 137;
								dviptr = dviptr + 1;
								if (dviptr == dvilimit) {
									dviswap();
								}
							}
							dviWriter.writeInt(ruleht);
							dviWriter.writeInt(rulewd);
						}
						break lab15;
					}
					/* lab13: */curv = curv + ruleht;
				}
				break;
			}
			/* lab15: */p = mem[p].getrh();
		}
		prunemovements(saveloc);
		if (curs > 0) {
			dvipop(saveloc);
		}
		curs = curs - 1;
	}

	void shipout(final int p) {
		int pageloc;
		int j, k;
		int oldsetting;
		if (eqtb[9597].getInt() > 0) {
			printnl(338);
			println();
			print(829);
		}
		if ((termoffset > 0) || (fileoffset > 0)) {
			printchar(32);
		}
		printchar(91);
		j = 9;
		while ((eqtb[9618 + j].getInt() == 0) && (j > 0)) {
			j = j - 1;
		}
		for (k = 0; k <= j; k++) {
			printInt(eqtb[9618 + k].getInt());
			if (k < j) {
				printchar(46);
			}
		}
		termout.flush();
		if (eqtb[9597].getInt() > 0) {
			printchar(93);
			begindiagnostic();
			showbox(p);
			enddiagnostic(true);
		}
		lab30: while (true) {
			if ((mem[p + 3].getInt() > 1073741823) || (mem[p + 2].getInt() > 1073741823) || (mem[p + 3].getInt() + mem[p + 2].getInt() + eqtb[10149].getInt() > 1073741823) || (mem[p + 1].getInt() + eqtb[10148].getInt() > 1073741823)) {
				printnl(262);
				print(833);
				helpptr = 2;
				helpline[1] = 834;
				helpline[0] = 835;
				errorLogic.error();
				if (eqtb[9597].getInt() <= 0) {
					begindiagnostic();
					printnl(836);
					showbox(p);
					enddiagnostic(true);
				}
				break lab30;
			}
			if (mem[p + 3].getInt() + mem[p + 2].getInt() + eqtb[10149].getInt() > maxv) {
				maxv = mem[p + 3].getInt() + mem[p + 2].getInt() + eqtb[10149].getInt();
			}
			if (mem[p + 1].getInt() + eqtb[10148].getInt() > maxh) {
				maxh = mem[p + 1].getInt() + eqtb[10148].getInt();
			}
			dvih = 0;
			dviv = 0;
			curh = eqtb[10148].getInt();
			dvif = 0;
			if (outputfilename == 0) {
				packjobname(794);
				try {
					dvifile = new TexFileDataOutputStream(nameoffile);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				outputfilename = makenamestring();
			}
			if (totalpages == 0) {
				dviWriter.writeByte(247);
				dviWriter.writeByte(2);
				dviWriter.writeInt(25400000);
				dviWriter.writeInt(473628672);
				preparemag();
				dviWriter.writeInt(eqtb[9580].getInt());
				oldsetting = selector;
				selector = 21;
				print(827);
				printInt(eqtb[9586].getInt());
				printchar(46);
				printTwoDigits(eqtb[9585].getInt());
				printchar(46);
				printTwoDigits(eqtb[9584].getInt());
				printchar(58);
				printTwoDigits(eqtb[9583].getInt() / 60);
				printTwoDigits(eqtb[9583].getInt() % 60);
				selector = oldsetting;
				final String s = stringPool.extractPartiallyBuiltString(true);
				dviWriter.writeByte(s.length());
				for (int i = 0; i < s.length(); i++) {
					dviWriter.writeByte(s.charAt(i));
				}
			}
			pageloc = dvioffset + dviptr;
			dviWriter.writeByte(139);
			for (k = 0; k <= 9; k++) {
				dviWriter.writeInt(eqtb[9618 + k].getInt());
			}
			dviWriter.writeInt(lastbop);
			lastbop = pageloc;
			curv = mem[p + 3].getInt() + eqtb[10149].getInt();
			tempptr = p;
			if (mem[p].getb0() == 1) {
				vlistout();
			} else {
				hlistout();
			}
			dviWriter.writeByte(140);
			totalpages = totalpages + 1;
			curs = -1;
			break;
		}
		if (eqtb[9597].getInt() <= 0) {
			printchar(93);
		}
		deadcycles = 0;
		termout.flush();
		flushnodelist(p);
	}

	void scanspec(final int c, final boolean threecodes) {
		/* 40 */int s;
		int speccode;
		if (threecodes) {
			s = savestack[saveptr + 0].getInt();
		} else {
			s = 0;
		}
		lab40: while (true) {
			if (scankeyword(842)) {
				speccode = 0;
			} else if (scankeyword(843)) {
				speccode = 1;
			} else {
				speccode = 1;
				curval = 0;
				break lab40;
			}
			scandimen(false, false, false);
			break;
		}
		/* lab40: */if (threecodes) {
			savestack[saveptr + 0].setInt(s);
			saveptr = saveptr + 1;
		}
		savestack[saveptr + 0].setInt(speccode);
		savestack[saveptr + 1].setInt(curval);
		saveptr = saveptr + 2;
		newsavelevel(c);
		scanleftbrace();
	}

	int hpack(int p, int w, final int m) {
		/* 21 50 10 */int Result;
		int r;
		int q;
		int h, d, x;
		int s;
		int g;
		int o;
		int f;
		final fourquarters i = new fourquarters();
		int hd;
		lastbadness = 0;
		r = getnode(7);
		mem[r].setb0(0);
		mem[r].setb1(0);
		mem[r + 4].setInt(0);
		q = r + 5;
		mem[q].setrh(p);
		h = 0;
		d = 0;
		x = 0;
		totalstretch[0] = 0;
		totalshrink[0] = 0;
		totalstretch[1] = 0;
		totalshrink[1] = 0;
		totalstretch[2] = 0;
		totalshrink[2] = 0;
		totalstretch[3] = 0;
		totalshrink[3] = 0;
		while (p != 0) {
			lab21: while (true) {
				while ((p >= himemmin)) {
					f = mem[p].getb0();
					i.copy(fontinfo[charbase[f] + mem[p].getb1()].qqqq());
					hd = i.b1;
					x = x + fontinfo[widthbase[f] + i.b0].getInt();
					s = fontinfo[heightbase[f] + (hd) / 16].getInt();
					if (s > h) {
						h = s;
					}
					s = fontinfo[depthbase[f] + (hd) % 16].getInt();
					if (s > d) {
						d = s;
					}
					p = mem[p].getrh();
				}
				if (p != 0) {
					switch (mem[p].getb0()) {
						case 0:
						case 1:
						case 2:
						case 13: {
							x = x + mem[p + 1].getInt();
							if (mem[p].getb0() >= 2) {
								s = 0;
							} else {
								s = mem[p + 4].getInt();
							}
							if (mem[p + 3].getInt() - s > h) {
								h = mem[p + 3].getInt() - s;
							}
							if (mem[p + 2].getInt() + s > d) {
								d = mem[p + 2].getInt() + s;
							}
						}
							break;
						case 3:
						case 4:
						case 5:
							if (adjusttail != 0) {
								while (mem[q].getrh() != p) {
									q = mem[q].getrh();
								}
								if (mem[p].getb0() == 5) {
									mem[adjusttail].setrh(mem[p + 1].getInt());
									while (mem[adjusttail].getrh() != 0) {
										adjusttail = mem[adjusttail].getrh();
									}
									p = mem[p].getrh();
									freenode(mem[q].getrh(), 2);
								} else {
									mem[adjusttail].setrh(p);
									adjusttail = p;
									p = mem[p].getrh();
								}
								mem[q].setrh(p);
								p = q;
							}
							break;
						case 8:
							;
							break;
						case 10: {
							g = mem[p + 1].getlh();
							x = x + mem[g + 1].getInt();
							o = mem[g].getb0();
							totalstretch[o] = totalstretch[o] + mem[g + 2].getInt();
							o = mem[g].getb1();
							totalshrink[o] = totalshrink[o] + mem[g + 3].getInt();
							if (mem[p].getb1() >= 100) {
								g = mem[p + 1].getrh();
								if (mem[g + 3].getInt() > h) {
									h = mem[g + 3].getInt();
								}
								if (mem[g + 2].getInt() > d) {
									d = mem[g + 2].getInt();
								}
							}
						}
							break;
						case 11:
						case 9:
							x = x + mem[p + 1].getInt();
							break;
						case 6: {
							mem[memtop - 12].copy(mem[p + 1]);
							mem[memtop - 12].setrh(mem[p].getrh());
							p = memtop - 12;
							continue lab21;
						}
						default:
							;
							break;
					}
					p = mem[p].getrh();
				}
				break;
			}
		}
		if (adjusttail != 0) {
			mem[adjusttail].setrh(0);
		}
		mem[r + 3].setInt(h);
		mem[r + 2].setInt(d);
		lab50: while (true) {
			if (m == 1) {
				w = x + w;
			}
			mem[r + 1].setInt(w);
			x = w - x;
			if (x == 0) {
				mem[r + 5].setb0(0);
				mem[r + 5].setb1(0);
				mem[r + 6].setglue(0.0);
				Result = r;
				return Result /* lab10 */;
			} else if (x > 0) {
				if (totalstretch[3] != 0) {
					o = 3;
				} else if (totalstretch[2] != 0) {
					o = 2;
				} else if (totalstretch[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[r + 5].setb1(o);
				mem[r + 5].setb0(1);
				if (totalstretch[o] != 0) {
					mem[r + 6].setglue(x / ((double)totalstretch[o]));
				} else {
					mem[r + 5].setb0(0);
					mem[r + 6].setglue(0.0);
				}
				if (o == 0) {
					if (mem[r + 5].getrh() != 0) {
						lastbadness = badness(x, totalstretch[0]);
						if (lastbadness > eqtb[9589].getInt()) {
							println();
							if (lastbadness > 100) {
								printnl(844);
							} else {
								printnl(845);
							}
							print(846);
							printInt(lastbadness);
							break lab50;
						}
					}
				}
				Result = r;
				return Result /* lab10 */;
			} else {
				if (totalshrink[3] != 0) {
					o = 3;
				} else if (totalshrink[2] != 0) {
					o = 2;
				} else if (totalshrink[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[r + 5].setb1(o);
				mem[r + 5].setb0(2);
				if (totalshrink[o] != 0) {
					mem[r + 6].setglue((-x) / ((double)totalshrink[o]));
				} else {
					mem[r + 5].setb0(0);
					mem[r + 6].setglue(0.0);
				}
				if ((totalshrink[o] < -x) && (o == 0) && (mem[r + 5].getrh() != 0)) {
					lastbadness = 1000000;
					mem[r + 6].setglue(1.0);
					if ((-x - totalshrink[0] > eqtb[10138].getInt()) || (eqtb[9589].getInt() < 100)) {
						if ((eqtb[10146].getInt() > 0) && (-x - totalshrink[0] > eqtb[10138].getInt())) {
							while (mem[q].getrh() != 0) {
								q = mem[q].getrh();
							}
							mem[q].setrh(newrule());
							mem[mem[q].getrh() + 1].setInt(eqtb[10146].getInt());
						}
						println();
						printnl(852);
						printFixed(-x - totalshrink[0]);
						print(853);
						break lab50;
					}
				} else if (o == 0) {
					if (mem[r + 5].getrh() != 0) {
						lastbadness = badness(-x, totalshrink[0]);
						if (lastbadness > eqtb[9589].getInt()) {
							println();
							printnl(854);
							printInt(lastbadness);
							break lab50;
						}
					}
				}
				Result = r;
				return Result /* lab10 */;
			}
		}
		/* lab50: */if (outputactive) {
			print(847);
		} else {
			if (packbeginline != 0) {
				if (packbeginline > 0) {
					print(848);
				} else {
					print(849);
				}
				printInt(Math.abs(packbeginline));
				print(850);
			} else {
				print(851);
			}
			printInt(line);
		}
		println();
		fontinshortdisplay = 0;
		shortdisplay(mem[r + 5].getrh());
		println();
		begindiagnostic();
		showbox(r);
		enddiagnostic(true);
		Result = r;
		return Result;
	}

	int vpackage(int p, int h, final int m, final int l) {
		/* 50 10 */int Result;
		int r;
		int w, d, x;
		int s;
		int g;
		int o;
		lastbadness = 0;
		r = getnode(7);
		mem[r].setb0(1);
		mem[r].setb1(0);
		mem[r + 4].setInt(0);
		mem[r + 5].setrh(p);
		w = 0;
		d = 0;
		x = 0;
		totalstretch[0] = 0;
		totalshrink[0] = 0;
		totalstretch[1] = 0;
		totalshrink[1] = 0;
		totalstretch[2] = 0;
		totalshrink[2] = 0;
		totalstretch[3] = 0;
		totalshrink[3] = 0;
		while (p != 0) {
			if ((p >= himemmin)) {
				errorLogic.confusion(855);
			} else {
				switch (mem[p].getb0()) {
					case 0:
					case 1:
					case 2:
					case 13: {
						x = x + d + mem[p + 3].getInt();
						d = mem[p + 2].getInt();
						if (mem[p].getb0() >= 2) {
							s = 0;
						} else {
							s = mem[p + 4].getInt();
						}
						if (mem[p + 1].getInt() + s > w) {
							w = mem[p + 1].getInt() + s;
						}
					}
						break;
					case 8:
						;
						break;
					case 10: {
						x = x + d;
						d = 0;
						g = mem[p + 1].getlh();
						x = x + mem[g + 1].getInt();
						o = mem[g].getb0();
						totalstretch[o] = totalstretch[o] + mem[g + 2].getInt();
						o = mem[g].getb1();
						totalshrink[o] = totalshrink[o] + mem[g + 3].getInt();
						if (mem[p].getb1() >= 100) {
							g = mem[p + 1].getrh();
							if (mem[g + 1].getInt() > w) {
								w = mem[g + 1].getInt();
							}
						}
					}
						break;
					case 11: {
						x = x + d + mem[p + 1].getInt();
						d = 0;
					}
						break;
					default:
						;
						break;
				}
			}
			p = mem[p].getrh();
		}
		mem[r + 1].setInt(w);
		if (d > l) {
			x = x + d - l;
			mem[r + 2].setInt(l);
		} else {
			mem[r + 2].setInt(d);
		}
		lab50: while (true) {
			if (m == 1) {
				h = x + h;
			}
			mem[r + 3].setInt(h);
			x = h - x;
			if (x == 0) {
				mem[r + 5].setb0(0);
				mem[r + 5].setb1(0);
				mem[r + 6].setglue(0.0);
				Result = r;
				return Result /* lab10 */;
			} else if (x > 0) {
				if (totalstretch[3] != 0) {
					o = 3;
				} else if (totalstretch[2] != 0) {
					o = 2;
				} else if (totalstretch[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[r + 5].setb1(o);
				mem[r + 5].setb0(1);
				if (totalstretch[o] != 0) {
					mem[r + 6].setglue(x / ((double)totalstretch[o]));
				} else {
					mem[r + 5].setb0(0);
					mem[r + 6].setglue(0.0);
				}
				if (o == 0) {
					if (mem[r + 5].getrh() != 0) {
						lastbadness = badness(x, totalstretch[0]);
						if (lastbadness > eqtb[9590].getInt()) {
							println();
							if (lastbadness > 100) {
								printnl(844);
							} else {
								printnl(845);
							}
							print(856);
							printInt(lastbadness);
							break lab50;
						}
					}
				}
				Result = r;
				return Result /* lab10 */;
			} else {
				if (totalshrink[3] != 0) {
					o = 3;
				} else if (totalshrink[2] != 0) {
					o = 2;
				} else if (totalshrink[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[r + 5].setb1(o);
				mem[r + 5].setb0(2);
				if (totalshrink[o] != 0) {
					mem[r + 6].setglue((-x) / ((double)totalshrink[o]));
				} else {
					mem[r + 5].setb0(0);
					mem[r + 6].setglue(0.0);
				}
				if ((totalshrink[o] < -x) && (o == 0) && (mem[r + 5].getrh() != 0)) {
					lastbadness = 1000000;
					mem[r + 6].setglue(1.0);
					if ((-x - totalshrink[0] > eqtb[10139].getInt()) || (eqtb[9590].getInt() < 100)) {
						println();
						printnl(857);
						printFixed(-x - totalshrink[0]);
						print(858);
						break lab50;
					}
				} else if (o == 0) {
					if (mem[r + 5].getrh() != 0) {
						lastbadness = badness(-x, totalshrink[0]);
						if (lastbadness > eqtb[9590].getInt()) {
							println();
							printnl(859);
							printInt(lastbadness);
							break lab50;
						}
					}
				}
				Result = r;
				return Result /* lab10 */;
			}
		}
		/* lab50: */if (outputactive) {
			print(847);
		} else {
			if (packbeginline != 0) {
				print(849);
				printInt(Math.abs(packbeginline));
				print(850);
			} else {
				print(851);
			}
			printInt(line);
			println();
		}
		begindiagnostic();
		showbox(r);
		enddiagnostic(true);
		Result = r;
		return Result;
	}

	void appendtovlist(final int b) {
		int d;
		int p;
		if (curlist.auxfield.getInt() > -65536000) {
			d = mem[eqtb[7183].getrh() + 1].getInt() - curlist.auxfield.getInt() - mem[b + 3].getInt();
			if (d < eqtb[10132].getInt()) {
				p = newparamglue(0);
			} else {
				p = newskipparam(1);
				mem[tempptr + 1].setInt(d);
			}
			mem[curlist.tailfield].setrh(p);
			curlist.tailfield = p;
		}
		mem[curlist.tailfield].setrh(b);
		curlist.tailfield = b;
		curlist.auxfield.setInt(mem[b + 2].getInt());
	}

	int newnoad() {
		int Result;
		int p;
		p = getnode(4);
		mem[p].setb0(16);
		mem[p].setb1(0);
		mem[p + 1].sethh(emptyfield);
		mem[p + 3].sethh(emptyfield);
		mem[p + 2].sethh(emptyfield);
		Result = p;
		return Result;
	}

	int newstyle(final int s) {
		int Result;
		int p;
		p = getnode(3);
		mem[p].setb0(14);
		mem[p].setb1(s);
		mem[p + 1].setInt(0);
		mem[p + 2].setInt(0);
		Result = p;
		return Result;
	}

	int newchoice() {
		int Result;
		int p;
		p = getnode(3);
		mem[p].setb0(15);
		mem[p].setb1(0);
		mem[p + 1].setlh(0);
		mem[p + 1].setrh(0);
		mem[p + 2].setlh(0);
		mem[p + 2].setrh(0);
		Result = p;
		return Result;
	}

	int fractionrule(final int t) {
		int Result;
		int p;
		p = newrule();
		mem[p + 3].setInt(t);
		mem[p + 2].setInt(0);
		Result = p;
		return Result;
	}

	int overbar(final int b, final int k, final int t) {
		int Result;
		int p, q;
		p = newkern(k);
		mem[p].setrh(b);
		q = fractionrule(t);
		mem[q].setrh(p);
		p = newkern(t);
		mem[p].setrh(q);
		Result = vpackage(p, 0, 1, 1073741823);
		return Result;
	}

	int charbox(final int f, final int c) {
		int Result;
		final fourquarters q = new fourquarters();
		int hd;
		int b, p;
		q.copy(fontinfo[charbase[f] + c].qqqq());
		hd = q.b1;
		b = newnullbox();
		mem[b + 1].setInt(fontinfo[widthbase[f] + q.b0].getInt() + fontinfo[italicbase[f] + (q.b2) / 4].getInt());
		mem[b + 3].setInt(fontinfo[heightbase[f] + (hd) / 16].getInt());
		mem[b + 2].setInt(fontinfo[depthbase[f] + (hd) % 16].getInt());
		p = allocateMemoryWord();
		mem[p].setb1(c);
		mem[p].setb0(f);
		mem[b + 5].setrh(p);
		Result = b;
		return Result;
	}

	void stackintobox(final int b, final int f, final int c) {
		int p;
		p = charbox(f, c);
		mem[p].setrh(mem[b + 5].getrh());
		mem[b + 5].setrh(p);
		mem[b + 3].setInt(mem[p + 3].getInt());
	}

	int heightplusdepth(final int f, final int c) {
		int Result;
		final fourquarters q = new fourquarters();
		int hd;
		q.copy(fontinfo[charbase[f] + c].qqqq());
		hd = q.b1;
		Result = fontinfo[heightbase[f] + (hd) / 16].getInt() + fontinfo[depthbase[f] + (hd) % 16].getInt();
		return Result;
	}

	int vardelimiter(final int d, final int s, final int v) {
		/* 40 22 */int Result;
		int b;
		int f, g;
		int c, x, y;
		int m, n;
		int u;
		int w;
		final fourquarters q = new fourquarters();
		int hd;
		final fourquarters r = new fourquarters();
		int z;
		boolean largeattempt;
		f = 0;
		w = 0;
		largeattempt = false;
		z = mem[d].getb0();
		x = mem[d].getb1();
		c = 0;
		q.copy(nullcharacter);
		lab40: while (true) {
			if ((z != 0) || (x != 0)) {
				z = z + s + 16;
				do {
					z = z - 16;
					g = eqtb[8235 + z].getrh();
					if (g != 0) {
						y = x;
						if ((y >= fontbc[g]) && (y <= fontec[g])) {
							lab22: while (true) {
								q.copy(fontinfo[charbase[g] + y].qqqq());
								if ((q.b0 > 0)) {
									if (((q.b2) % 4) == 3) {
										f = g;
										c = y;
										break lab40;
									}
									hd = q.b1;
									u = fontinfo[heightbase[g] + (hd) / 16].getInt() + fontinfo[depthbase[g] + (hd) % 16].getInt();
									if (u > w) {
										f = g;
										c = y;
										w = u;
										if (u >= v) {
											break lab40;
										}
									}
									if (((q.b2) % 4) == 2) {
										y = q.b3;
										continue lab22;
									}
								}
								break;
							}
						}
					}
				} while (!(z < 16));
			}
			if (largeattempt) {
				break lab40;
			}
			largeattempt = true;
			z = mem[d].getb2();
			x = mem[d].getb3();
		}
		/* lab40: */if (f != 0) {
			if (((q.b2) % 4) == 3) {
				b = newnullbox();
				mem[b].setb0(1);
				r.copy(fontinfo[extenbase[f] + q.b3].qqqq());
				c = r.b3;
				u = heightplusdepth(f, c);
				w = 0;
				q.copy(fontinfo[charbase[f] + c].qqqq());
				mem[b + 1].setInt(fontinfo[widthbase[f] + q.b0].getInt() + fontinfo[italicbase[f] + (q.b2) / 4].getInt());
				c = r.b2;
				if (c != 0) {
					w = w + heightplusdepth(f, c);
				}
				c = r.b1;
				if (c != 0) {
					w = w + heightplusdepth(f, c);
				}
				c = r.b0;
				if (c != 0) {
					w = w + heightplusdepth(f, c);
				}
				n = 0;
				if (u > 0) {
					while (w < v) {
						w = w + u;
						n = n + 1;
						if (r.b1 != 0) {
							w = w + u;
						}
					}
				}
				c = r.b2;
				if (c != 0) {
					stackintobox(b, f, c);
				}
				c = r.b3;
				for (m = 1; m <= n; m++) {
					stackintobox(b, f, c);
				}
				c = r.b1;
				if (c != 0) {
					stackintobox(b, f, c);
					c = r.b3;
					for (m = 1; m <= n; m++) {
						stackintobox(b, f, c);
					}
				}
				c = r.b0;
				if (c != 0) {
					stackintobox(b, f, c);
				}
				mem[b + 2].setInt(w - mem[b + 3].getInt());
			} else {
				b = charbox(f, c);
			}
		} else {
			b = newnullbox();
			mem[b + 1].setInt(eqtb[10141].getInt());
		}
		mem[b + 4].setInt(half(mem[b + 3].getInt() - mem[b + 2].getInt()) - fontinfo[22 + parambase[eqtb[8237 + s].getrh()]].getInt());
		Result = b;
		return Result;
	}

	int rebox(int b, final int w) {
		int Result;
		int p;
		int f;
		int v;
		if ((mem[b + 1].getInt() != w) && (mem[b + 5].getrh() != 0)) {
			if (mem[b].getb0() == 1) {
				b = hpack(b, 0, 1);
			}
			p = mem[b + 5].getrh();
			if (((p >= himemmin)) && (mem[p].getrh() == 0)) {
				f = mem[p].getb0();
				v = fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[p].getb1()].getb0()].getInt();
				if (v != mem[b + 1].getInt()) {
					mem[p].setrh(newkern(mem[b + 1].getInt() - v));
				}
			}
			freenode(b, 7);
			b = newglue(12);
			mem[b].setrh(p);
			while (mem[p].getrh() != 0) {
				p = mem[p].getrh();
			}
			mem[p].setrh(newglue(12));
			Result = hpack(b, w, 0);
		} else {
			mem[b + 1].setInt(w);
			Result = b;
		}
		return Result;
	}

	int mathglue(final int g, final int m) {
		int Result;
		int p;
		int n;
		int f;
		n = xovern(m, 65536);
		f = remainder;
		if (f < 0) {
			n = n - 1;
			f = f + 65536;
		}
		p = getnode(4);
		mem[p + 1].setInt(multandadd(n, mem[g + 1].getInt(), xnoverd(mem[g + 1].getInt(), f, 65536), 1073741823));
		mem[p].setb0(mem[g].getb0());
		if (mem[p].getb0() == 0) {
			mem[p + 2].setInt(multandadd(n, mem[g + 2].getInt(), xnoverd(mem[g + 2].getInt(), f, 65536), 1073741823));
		} else {
			mem[p + 2].setInt(mem[g + 2].getInt());
		}
		mem[p].setb1(mem[g].getb1());
		if (mem[p].getb1() == 0) {
			mem[p + 3].setInt(multandadd(n, mem[g + 3].getInt(), xnoverd(mem[g + 3].getInt(), f, 65536), 1073741823));
		} else {
			mem[p + 3].setInt(mem[g + 3].getInt());
		}
		Result = p;
		return Result;
	}

	void mathkern(final int p, final int m) {
		int n;
		int f;
		if (mem[p].getb1() == 99) {
			n = xovern(m, 65536);
			f = remainder;
			if (f < 0) {
				n = n - 1;
				f = f + 65536;
			}
			mem[p + 1].setInt(multandadd(n, mem[p + 1].getInt(), xnoverd(mem[p + 1].getInt(), f, 65536), 1073741823));
			mem[p].setb1(1);
		}
	}

	void flushmath() {
		flushnodelist(mem[curlist.headfield].getrh());
		flushnodelist(curlist.auxfield.getInt());
		mem[curlist.headfield].setrh(0);
		curlist.tailfield = curlist.headfield;
		curlist.auxfield.setInt(0);
	}

	int cleanbox(final int p, final int s) {
		/* 40 */int Result;
		int q;
		int savestyle;
		int x;
		int r;
		lab40: while (true) {
			switch (mem[p].getrh()) {
				case 1: {
					curmlist = newnoad();
					mem[curmlist + 1].copy(mem[p]);
				}
					break;
				case 2: {
					q = mem[p].getlh();
					break lab40;
				}
				case 3:
					curmlist = mem[p].getlh();
					break;
				default: {
					q = newnullbox();
					break lab40;
				}
			}
			savestyle = curstyle;
			curstyle = s;
			mlistpenalties = false;
			mlisttohlist();
			q = mem[memtop - 3].getrh();
			curstyle = savestyle;
			{
				if (curstyle < 4) {
					cursize = 0;
				} else {
					cursize = 16 * ((curstyle - 2) / 2);
				}
				curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
			}
			break;
		}
		/* lab40: */if ((q >= himemmin) || (q == 0)) {
			x = hpack(q, 0, 1);
		} else if ((mem[q].getrh() == 0) && (mem[q].getb0() <= 1) && (mem[q + 4].getInt() == 0)) {
			x = q;
		} else {
			x = hpack(q, 0, 1);
		}
		q = mem[x + 5].getrh();
		if ((q >= himemmin)) {
			r = mem[q].getrh();
			if (r != 0) {
				if (mem[r].getrh() == 0) {
					if (!(r >= himemmin)) {
						if (mem[r].getb0() == 11) {
							freenode(r, 2);
							mem[q].setrh(0);
						}
					}
				}
			}
		}
		Result = x;
		return Result;
	}

	void fetch(final int a) {
		curc = mem[a].getb1();
		curf = eqtb[8235 + mem[a].getb0() + cursize].getrh();
		if (curf == 0) {
			{
				printnl(262);
				print(338);
			}
			printsize(cursize);
			printchar(32);
			printInt(mem[a].getb0());
			print(884);
			print(curc);
			printchar(41);
			{
				helpptr = 4;
				helpline[3] = 885;
				helpline[2] = 886;
				helpline[1] = 887;
				helpline[0] = 888;
			}
			errorLogic.error();
			curi.copy(nullcharacter);
			mem[a].setrh(0);
		} else {
			if ((curc >= fontbc[curf]) && (curc <= fontec[curf])) {
				curi.copy(fontinfo[charbase[curf] + curc].qqqq());
			} else {
				curi.copy(nullcharacter);
			}
			if (!((curi.b0 > 0))) {
				errorLogic.charwarning(curf, curc);
				mem[a].setrh(0);
			}
		}
	}

	void makeover(final int q) {
		mem[q + 1].setlh(overbar(cleanbox(q + 1, 2 * (curstyle / 2) + 1), 3 * fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt(), fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt()));
		mem[q + 1].setrh(2);
	}

	void makeunder(final int q) {
		int p, x, y;
		int delta;
		x = cleanbox(q + 1, curstyle);
		p = newkern(3 * fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt());
		mem[x].setrh(p);
		mem[p].setrh(fractionrule(fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt()));
		y = vpackage(x, 0, 1, 1073741823);
		delta = mem[y + 3].getInt() + mem[y + 2].getInt() + fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
		mem[y + 3].setInt(mem[x + 3].getInt());
		mem[y + 2].setInt(delta - mem[y + 3].getInt());
		mem[q + 1].setlh(y);
		mem[q + 1].setrh(2);
	}

	void makevcenter(final int q) {
		int v;
		int delta;
		v = mem[q + 1].getlh();
		if (mem[v].getb0() != 1) {
			errorLogic.confusion(539);
		}
		delta = mem[v + 3].getInt() + mem[v + 2].getInt();
		mem[v + 3].setInt(fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt() + half(delta));
		mem[v + 2].setInt(delta - mem[v + 3].getInt());
	}

	void makeradical(final int q) {
		int x, y;
		int delta, clr;
		x = cleanbox(q + 1, 2 * (curstyle / 2) + 1);
		if (curstyle < 2) {
			clr = fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt() + (Math.abs(fontinfo[5 + parambase[eqtb[8237 + cursize].getrh()]].getInt()) / 4);
		} else {
			clr = fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
			clr = clr + (Math.abs(clr) / 4);
		}
		y = vardelimiter(q + 4, cursize, mem[x + 3].getInt() + mem[x + 2].getInt() + clr + fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt());
		delta = mem[y + 2].getInt() - (mem[x + 3].getInt() + mem[x + 2].getInt() + clr);
		if (delta > 0) {
			clr = clr + half(delta);
		}
		mem[y + 4].setInt(-(mem[x + 3].getInt() + clr));
		mem[y].setrh(overbar(x, clr, mem[y + 3].getInt()));
		mem[q + 1].setlh(hpack(y, 0, 1));
		mem[q + 1].setrh(2);
	}

	void makemathaccent(final int q) {
		/* 30 31 */int p, x, y;
		int a;
		int c;
		int f;
		final fourquarters i = new fourquarters();
		int s;
		int h;
		int delta;
		int w;
		fetch(q + 4);
		if ((curi.b0 > 0)) {
			i.copy(curi);
			c = curc;
			f = curf;
			s = 0;
			if (mem[q + 1].getrh() == 1) {
				lab31: while (true) {
					fetch(q + 1);
					if (((curi.b2) % 4) == 1) {
						a = ligkernbase[curf] + curi.b3;
						curi.copy(fontinfo[a].qqqq());
						if (curi.b0 > 128) {
							a = ligkernbase[curf] + 256 * curi.b2 + curi.b3 + 32768 - 256 * (128);
							curi.copy(fontinfo[a].qqqq());
						}
						while (true) {
							if (curi.b1 == skewchar[curf]) {
								if (curi.b2 >= 128) {
									if (curi.b0 <= 128) {
										s = fontinfo[kernbase[curf] + 256 * curi.b2 + curi.b3].getInt();
									}
								}
								break lab31;
							}
							if (curi.b0 >= 128) {
								break lab31;
							}
							a = a + curi.b0 + 1;
							curi.copy(fontinfo[a].qqqq());
						}
					}
					break;
				}
			}
			/* lab31: */x = cleanbox(q + 1, 2 * (curstyle / 2) + 1);
			w = mem[x + 1].getInt();
			h = mem[x + 3].getInt();
			while (true) {
				if (((i.b2) % 4) != 2) {
					break /* lab30 */;
				}
				y = i.b3;
				i.copy(fontinfo[charbase[f] + y].qqqq());
				if (!(i.b0 > 0)) {
					break /* lab30 */;
				}
				if (fontinfo[widthbase[f] + i.b0].getInt() > w) {
					break /* lab30 */;
				}
				c = y;
			}
			/* lab30: */if (h < fontinfo[5 + parambase[f]].getInt()) {
				delta = h;
			} else {
				delta = fontinfo[5 + parambase[f]].getInt();
			}
			if ((mem[q + 2].getrh() != 0) || (mem[q + 3].getrh() != 0)) {
				if (mem[q + 1].getrh() == 1) {
					flushnodelist(x);
					x = newnoad();
					mem[x + 1].copy(mem[q + 1]);
					mem[x + 2].copy(mem[q + 2]);
					mem[x + 3].copy(mem[q + 3]);
					mem[q + 2].sethh(emptyfield);
					mem[q + 3].sethh(emptyfield);
					mem[q + 1].setrh(3);
					mem[q + 1].setlh(x);
					x = cleanbox(q + 1, curstyle);
					delta = delta + mem[x + 3].getInt() - h;
					h = mem[x + 3].getInt();
				}
			}
			y = charbox(f, c);
			mem[y + 4].setInt(s + half(w - mem[y + 1].getInt()));
			mem[y + 1].setInt(0);
			p = newkern(-delta);
			mem[p].setrh(x);
			mem[y].setrh(p);
			y = vpackage(y, 0, 1, 1073741823);
			mem[y + 1].setInt(mem[x + 1].getInt());
			if (mem[y + 3].getInt() < h) {
				p = newkern(h - mem[y + 3].getInt());
				mem[p].setrh(mem[y + 5].getrh());
				mem[y + 5].setrh(p);
				mem[y + 3].setInt(h);
			}
			mem[q + 1].setlh(y);
			mem[q + 1].setrh(2);
		}
	}

	void makefraction(final int q) {
		int p, v, x, y, z;
		int delta, delta1, delta2, shiftup, shiftdown, clr;
		if (mem[q + 1].getInt() == 1073741824) {
			mem[q + 1].setInt(fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt());
		}
		x = cleanbox(q + 2, curstyle + 2 - 2 * (curstyle / 6));
		z = cleanbox(q + 3, 2 * (curstyle / 2) + 3 - 2 * (curstyle / 6));
		if (mem[x + 1].getInt() < mem[z + 1].getInt()) {
			x = rebox(x, mem[z + 1].getInt());
		} else {
			z = rebox(z, mem[x + 1].getInt());
		}
		if (curstyle < 2) {
			shiftup = fontinfo[8 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
			shiftdown = fontinfo[11 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
		} else {
			shiftdown = fontinfo[12 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
			if (mem[q + 1].getInt() != 0) {
				shiftup = fontinfo[9 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
			} else {
				shiftup = fontinfo[10 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
			}
		}
		if (mem[q + 1].getInt() == 0) {
			if (curstyle < 2) {
				clr = 7 * fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
			} else {
				clr = 3 * fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
			}
			delta = half(clr - ((shiftup - mem[x + 2].getInt()) - (mem[z + 3].getInt() - shiftdown)));
			if (delta > 0) {
				shiftup = shiftup + delta;
				shiftdown = shiftdown + delta;
			}
		} else {
			if (curstyle < 2) {
				clr = 3 * mem[q + 1].getInt();
			} else {
				clr = mem[q + 1].getInt();
			}
			delta = half(mem[q + 1].getInt());
			delta1 = clr - ((shiftup - mem[x + 2].getInt()) - (fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt() + delta));
			delta2 = clr - ((fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt() - delta) - (mem[z + 3].getInt() - shiftdown));
			if (delta1 > 0) {
				shiftup = shiftup + delta1;
			}
			if (delta2 > 0) {
				shiftdown = shiftdown + delta2;
			}
		}
		v = newnullbox();
		mem[v].setb0(1);
		mem[v + 3].setInt(shiftup + mem[x + 3].getInt());
		mem[v + 2].setInt(mem[z + 2].getInt() + shiftdown);
		mem[v + 1].setInt(mem[x + 1].getInt());
		if (mem[q + 1].getInt() == 0) {
			p = newkern((shiftup - mem[x + 2].getInt()) - (mem[z + 3].getInt() - shiftdown));
			mem[p].setrh(z);
		} else {
			y = fractionrule(mem[q + 1].getInt());
			p = newkern((fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt() - delta) - (mem[z + 3].getInt() - shiftdown));
			mem[y].setrh(p);
			mem[p].setrh(z);
			p = newkern((shiftup - mem[x + 2].getInt()) - (fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt() + delta));
			mem[p].setrh(y);
		}
		mem[x].setrh(p);
		mem[v + 5].setrh(x);
		if (curstyle < 2) {
			delta = fontinfo[20 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
		} else {
			delta = fontinfo[21 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
		}
		x = vardelimiter(q + 4, cursize, delta);
		mem[x].setrh(v);
		z = vardelimiter(q + 5, cursize, delta);
		mem[v].setrh(z);
		mem[q + 1].setInt(hpack(x, 0, 1));
	}

	int makeop(final int q) {
		int Result;
		int delta;
		int p, v, x, y, z;
		int c;
		final fourquarters i = new fourquarters();
		int shiftup, shiftdown;
		if ((mem[q].getb1() == 0) && (curstyle < 2)) {
			mem[q].setb1(1);
		}
		if (mem[q + 1].getrh() == 1) {
			fetch(q + 1);
			if ((curstyle < 2) && (((curi.b2) % 4) == 2)) {
				c = curi.b3;
				i.copy(fontinfo[charbase[curf] + c].qqqq());
				if ((i.b0 > 0)) {
					curc = c;
					curi.copy(i);
					mem[q + 1].setb1(c);
				}
			}
			delta = fontinfo[italicbase[curf] + (curi.b2) / 4].getInt();
			x = cleanbox(q + 1, curstyle);
			if ((mem[q + 3].getrh() != 0) && (mem[q].getb1() != 1)) {
				mem[x + 1].setInt(mem[x + 1].getInt() - delta);
			}
			mem[x + 4].setInt(half(mem[x + 3].getInt() - mem[x + 2].getInt()) - fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt());
			mem[q + 1].setrh(2);
			mem[q + 1].setlh(x);
		} else {
			delta = 0;
		}
		if (mem[q].getb1() == 1) {
			x = cleanbox(q + 2, 2 * (curstyle / 4) + 4 + (curstyle % 2));
			y = cleanbox(q + 1, curstyle);
			z = cleanbox(q + 3, 2 * (curstyle / 4) + 5);
			v = newnullbox();
			mem[v].setb0(1);
			mem[v + 1].setInt(mem[y + 1].getInt());
			if (mem[x + 1].getInt() > mem[v + 1].getInt()) {
				mem[v + 1].setInt(mem[x + 1].getInt());
			}
			if (mem[z + 1].getInt() > mem[v + 1].getInt()) {
				mem[v + 1].setInt(mem[z + 1].getInt());
			}
			x = rebox(x, mem[v + 1].getInt());
			y = rebox(y, mem[v + 1].getInt());
			z = rebox(z, mem[v + 1].getInt());
			mem[x + 4].setInt(half(delta));
			mem[z + 4].setInt(-mem[x + 4].getInt());
			mem[v + 3].setInt(mem[y + 3].getInt());
			mem[v + 2].setInt(mem[y + 2].getInt());
			if (mem[q + 2].getrh() == 0) {
				freenode(x, 7);
				mem[v + 5].setrh(y);
			} else {
				shiftup = fontinfo[11 + parambase[eqtb[8238 + cursize].getrh()]].getInt() - mem[x + 2].getInt();
				if (shiftup < fontinfo[9 + parambase[eqtb[8238 + cursize].getrh()]].getInt()) {
					shiftup = fontinfo[9 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
				}
				p = newkern(shiftup);
				mem[p].setrh(y);
				mem[x].setrh(p);
				p = newkern(fontinfo[13 + parambase[eqtb[8238 + cursize].getrh()]].getInt());
				mem[p].setrh(x);
				mem[v + 5].setrh(p);
				mem[v + 3].setInt(mem[v + 3].getInt() + fontinfo[13 + parambase[eqtb[8238 + cursize].getrh()]].getInt() + mem[x + 3].getInt() + mem[x + 2].getInt() + shiftup);
			}
			if (mem[q + 3].getrh() == 0) {
				freenode(z, 7);
			} else {
				shiftdown = fontinfo[12 + parambase[eqtb[8238 + cursize].getrh()]].getInt() - mem[z + 3].getInt();
				if (shiftdown < fontinfo[10 + parambase[eqtb[8238 + cursize].getrh()]].getInt()) {
					shiftdown = fontinfo[10 + parambase[eqtb[8238 + cursize].getrh()]].getInt();
				}
				p = newkern(shiftdown);
				mem[y].setrh(p);
				mem[p].setrh(z);
				p = newkern(fontinfo[13 + parambase[eqtb[8238 + cursize].getrh()]].getInt());
				mem[z].setrh(p);
				mem[v + 2].setInt(mem[v + 2].getInt() + fontinfo[13 + parambase[eqtb[8238 + cursize].getrh()]].getInt() + mem[z + 3].getInt() + mem[z + 2].getInt() + shiftdown);
			}
			mem[q + 1].setInt(v);
		}
		Result = delta;
		return Result;
	}

	void makeord(final int q) {
		/* 20 10 */int a;
		int p, r;
		lab20: while (true) {
			if (mem[q + 3].getrh() == 0) {
				if (mem[q + 2].getrh() == 0) {
					if (mem[q + 1].getrh() == 1) {
						p = mem[q].getrh();
						if (p != 0) {
							if ((mem[p].getb0() >= 16) && (mem[p].getb0() <= 22)) {
								if (mem[p + 1].getrh() == 1) {
									if (mem[p + 1].getb0() == mem[q + 1].getb0()) {
										mem[q + 1].setrh(4);
										fetch(q + 1);
										if (((curi.b2) % 4) == 1) {
											a = ligkernbase[curf] + curi.b3;
											curc = mem[p + 1].getb1();
											curi.copy(fontinfo[a].qqqq());
											if (curi.b0 > 128) {
												a = ligkernbase[curf] + 256 * curi.b2 + curi.b3 + 32768 - 256 * (128);
												curi.copy(fontinfo[a].qqqq());
											}
											while (true) {
												if (curi.b1 == curc) {
													if (curi.b0 <= 128) {
														if (curi.b2 >= 128) {
															p = newkern(fontinfo[kernbase[curf] + 256 * curi.b2 + curi.b3].getInt());
															mem[p].setrh(mem[q].getrh());
															mem[q].setrh(p);
															return /* lab10 */;
														} else {
															switch (curi.b2) {
																case 1:
																case 5:
																	mem[q + 1].setb1(curi.b3);
																	break;
																case 2:
																case 6:
																	mem[p + 1].setb1(curi.b3);
																	break;
																case 3:
																case 7:
																case 11: {
																	r = newnoad();
																	mem[r + 1].setb1(curi.b3);
																	mem[r + 1].setb0(mem[q + 1].getb0());
																	mem[q].setrh(r);
																	mem[r].setrh(p);
																	if (curi.b2 < 11) {
																		mem[r + 1].setrh(1);
																	} else {
																		mem[r + 1].setrh(4);
																	}
																}
																	break;
																default: {
																	mem[q].setrh(mem[p].getrh());
																	mem[q + 1].setb1(curi.b3);
																	mem[q + 3].copy(mem[p + 3]);
																	mem[q + 2].copy(mem[p + 2]);
																	freenode(p, 4);
																}
																	break;
															}
															if (curi.b2 > 3) {
																return /* lab10 */;
															}
															mem[q + 1].setrh(1);
															continue lab20;
														}
													}
												}
												if (curi.b0 >= 128) {
													return /* lab10 */;
												}
												a = a + curi.b0 + 1;
												curi.copy(fontinfo[a].qqqq());
											}
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		}
	}

	void makescripts(final int q, final int delta) {
		int p, x, y, z;
		int shiftup, shiftdown, clr;
		int t;
		p = mem[q + 1].getInt();
		if ((p >= himemmin)) {
			shiftup = 0;
			shiftdown = 0;
		} else {
			z = hpack(p, 0, 1);
			if (curstyle < 4) {
				t = 16;
			} else {
				t = 32;
			}
			shiftup = mem[z + 3].getInt() - fontinfo[18 + parambase[eqtb[8237 + t].getrh()]].getInt();
			shiftdown = mem[z + 2].getInt() + fontinfo[19 + parambase[eqtb[8237 + t].getrh()]].getInt();
			freenode(z, 7);
		}
		if (mem[q + 2].getrh() == 0) {
			x = cleanbox(q + 3, 2 * (curstyle / 4) + 5);
			mem[x + 1].setInt(mem[x + 1].getInt() + eqtb[10142].getInt());
			if (shiftdown < fontinfo[16 + parambase[eqtb[8237 + cursize].getrh()]].getInt()) {
				shiftdown = fontinfo[16 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
			}
			clr = mem[x + 3].getInt() - (Math.abs(fontinfo[5 + parambase[eqtb[8237 + cursize].getrh()]].getInt() * 4) / 5);
			if (shiftdown < clr) {
				shiftdown = clr;
			}
			mem[x + 4].setInt(shiftdown);
		} else {
			{
				x = cleanbox(q + 2, 2 * (curstyle / 4) + 4 + (curstyle % 2));
				mem[x + 1].setInt(mem[x + 1].getInt() + eqtb[10142].getInt());
				if (((curstyle) % 2 == 1)) {
					clr = fontinfo[15 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
				} else if (curstyle < 2) {
					clr = fontinfo[13 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
				} else {
					clr = fontinfo[14 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
				}
				if (shiftup < clr) {
					shiftup = clr;
				}
				clr = mem[x + 2].getInt() + (Math.abs(fontinfo[5 + parambase[eqtb[8237 + cursize].getrh()]].getInt()) / 4);
				if (shiftup < clr) {
					shiftup = clr;
				}
			}
			if (mem[q + 3].getrh() == 0) {
				mem[x + 4].setInt(-shiftup);
			} else {
				y = cleanbox(q + 3, 2 * (curstyle / 4) + 5);
				mem[y + 1].setInt(mem[y + 1].getInt() + eqtb[10142].getInt());
				if (shiftdown < fontinfo[17 + parambase[eqtb[8237 + cursize].getrh()]].getInt()) {
					shiftdown = fontinfo[17 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
				}
				clr = 4 * fontinfo[8 + parambase[eqtb[8238 + cursize].getrh()]].getInt() - ((shiftup - mem[x + 2].getInt()) - (mem[y + 3].getInt() - shiftdown));
				if (clr > 0) {
					shiftdown = shiftdown + clr;
					clr = (Math.abs(fontinfo[5 + parambase[eqtb[8237 + cursize].getrh()]].getInt() * 4) / 5) - (shiftup - mem[x + 2].getInt());
					if (clr > 0) {
						shiftup = shiftup + clr;
						shiftdown = shiftdown - clr;
					}
				}
				mem[x + 4].setInt(delta);
				p = newkern((shiftup - mem[x + 2].getInt()) - (mem[y + 3].getInt() - shiftdown));
				mem[x].setrh(p);
				mem[p].setrh(y);
				x = vpackage(x, 0, 1, 1073741823);
				mem[x + 4].setInt(shiftdown);
			}
		}
		if (mem[q + 1].getInt() == 0) {
			mem[q + 1].setInt(x);
		} else {
			p = mem[q + 1].getInt();
			while (mem[p].getrh() != 0) {
				p = mem[p].getrh();
			}
			mem[p].setrh(x);
		}
	}

	int makeleftright(final int q, final int style, final int maxd, final int maxh) {
		int Result;
		int delta, delta1, delta2;
		if (style < 4) {
			cursize = 0;
		} else {
			cursize = 16 * ((style - 2) / 2);
		}
		delta2 = maxd + fontinfo[22 + parambase[eqtb[8237 + cursize].getrh()]].getInt();
		delta1 = maxh + maxd - delta2;
		if (delta2 > delta1) {
			delta1 = delta2;
		}
		delta = (delta1 / 500) * eqtb[9581].getInt();
		delta2 = delta1 + delta1 - eqtb[10140].getInt();
		if (delta < delta2) {
			delta = delta2;
		}
		mem[q + 1].setInt(vardelimiter(q + 1, cursize, delta));
		Result = mem[q].getb0() - (10);
		return Result;
	}

	void mlisttohlist() {
		/* 21 82 80 81 83 30 */int mlist;
		boolean penalties;
		int style;
		int savestyle;
		int q;
		int r;
		int rtype;
		int t;
		int p, x, y, z;
		int pen;
		int s;
		int maxh, maxd;
		int delta;
		x = 0;
		p = 0;
		mlist = curmlist;
		penalties = mlistpenalties;
		style = curstyle;
		q = mlist;
		r = 0;
		rtype = 17;
		maxh = 0;
		maxd = 0;
		{
			if (curstyle < 4) {
				cursize = 0;
			} else {
				cursize = 16 * ((curstyle - 2) / 2);
			}
			curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
		}
		while (q != 0) {
			lab81: while (true) {
				lab80: while (true) {
					lab82: while (true) {
						lab21: while (true) {
							delta = 0;
							switch (mem[q].getb0()) {
								case 18:
									switch (rtype) {
										case 18:
										case 17:
										case 19:
										case 20:
										case 22:
										case 30: {
											mem[q].setb0(16);
											continue lab21;
										}
										default:
											;
											break;
									}
									break;
								case 19:
								case 21:
								case 22:
								case 31: {
									if (rtype == 18) {
										mem[r].setb0(16);
									}
									if (mem[q].getb0() == 31) {
										break lab80;
									}
								}
									break;
								case 30:
									break lab80;
								case 25: {
									makefraction(q);
									break lab82;
								}
								case 17: {
									delta = makeop(q);
									if (mem[q].getb1() == 1) {
										break lab82;
									}
								}
									break;
								case 16:
									makeord(q);
									break;
								case 20:
								case 23:
									;
									break;
								case 24:
									makeradical(q);
									break;
								case 27:
									makeover(q);
									break;
								case 26:
									makeunder(q);
									break;
								case 28:
									makemathaccent(q);
									break;
								case 29:
									makevcenter(q);
									break;
								case 14: {
									curstyle = mem[q].getb1();
									{
										if (curstyle < 4) {
											cursize = 0;
										} else {
											cursize = 16 * ((curstyle - 2) / 2);
										}
										curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
									}
									break lab81;
								}
								case 15: {
									switch (curstyle / 2) {
										case 0: {
											p = mem[q + 1].getlh();
											mem[q + 1].setlh(0);
										}
											break;
										case 1: {
											p = mem[q + 1].getrh();
											mem[q + 1].setrh(0);
										}
											break;
										case 2: {
											p = mem[q + 2].getlh();
											mem[q + 2].setlh(0);
										}
											break;
										case 3: {
											p = mem[q + 2].getrh();
											mem[q + 2].setrh(0);
										}
											break;
									}
									flushnodelist(mem[q + 1].getlh());
									flushnodelist(mem[q + 1].getrh());
									flushnodelist(mem[q + 2].getlh());
									flushnodelist(mem[q + 2].getrh());
									mem[q].setb0(14);
									mem[q].setb1(curstyle);
									mem[q + 1].setInt(0);
									mem[q + 2].setInt(0);
									if (p != 0) {
										z = mem[q].getrh();
										mem[q].setrh(p);
										while (mem[p].getrh() != 0) {
											p = mem[p].getrh();
										}
										mem[p].setrh(z);
									}
									break lab81;
								}
								case 3:
								case 4:
								case 5:
								case 8:
								case 12:
								case 7:
									break lab81;
								case 2: {
									if (mem[q + 3].getInt() > maxh) {
										maxh = mem[q + 3].getInt();
									}
									if (mem[q + 2].getInt() > maxd) {
										maxd = mem[q + 2].getInt();
									}
									break lab81;
								}
								case 10: {
									if (mem[q].getb1() == 99) {
										x = mem[q + 1].getlh();
										y = mathglue(x, curmu);
										deleteglueref(x);
										mem[q + 1].setlh(y);
										mem[q].setb1(0);
									} else if ((cursize != 0) && (mem[q].getb1() == 98)) {
										p = mem[q].getrh();
										if (p != 0) {
											if ((mem[p].getb0() == 10) || (mem[p].getb0() == 11)) {
												mem[q].setrh(mem[p].getrh());
												mem[p].setrh(0);
												flushnodelist(p);
											}
										}
									}
									break lab81;
								}
								case 11: {
									mathkern(q, curmu);
									break lab81;
								}
								default:
									errorLogic.confusion(889);
									break;
							}
							break;
						}
						switch (mem[q + 1].getrh()) {
							case 1:
							case 4: {
								fetch(q + 1);
								if ((curi.b0 > 0)) {
									delta = fontinfo[italicbase[curf] + (curi.b2) / 4].getInt();
									p = newcharacter(curf, curc);
									if ((mem[q + 1].getrh() == 4) && (fontinfo[2 + parambase[curf]].getInt() != 0)) {
										delta = 0;
									}
									if ((mem[q + 3].getrh() == 0) && (delta != 0)) {
										mem[p].setrh(newkern(delta));
										delta = 0;
									}
								} else {
									p = 0;
								}
							}
								break;
							case 0:
								p = 0;
								break;
							case 2:
								p = mem[q + 1].getlh();
								break;
							case 3: {
								curmlist = mem[q + 1].getlh();
								savestyle = curstyle;
								mlistpenalties = false;
								mlisttohlist();
								curstyle = savestyle;
								{
									if (curstyle < 4) {
										cursize = 0;
									} else {
										cursize = 16 * ((curstyle - 2) / 2);
									}
									curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
								}
								p = hpack(mem[memtop - 3].getrh(), 0, 1);
							}
								break;
							default:
								errorLogic.confusion(890);
								break;
						}
						mem[q + 1].setInt(p);
						if ((mem[q + 3].getrh() == 0) && (mem[q + 2].getrh() == 0)) {
							break lab82;
						}
						makescripts(q, delta);
						break;
					}
					/* lab82: */z = hpack(mem[q + 1].getInt(), 0, 1);
					if (mem[z + 3].getInt() > maxh) {
						maxh = mem[z + 3].getInt();
					}
					if (mem[z + 2].getInt() > maxd) {
						maxd = mem[z + 2].getInt();
					}
					freenode(z, 7);
					break;
				}
				/* lab80: */r = q;
				rtype = mem[r].getb0();
				break;
			}
			/* lab81: */q = mem[q].getrh();
		}
		if (rtype == 18) {
			mem[r].setb0(16);
		}
		p = memtop - 3;
		mem[p].setrh(0);
		q = mlist;
		rtype = 0;
		curstyle = style;
		{
			if (curstyle < 4) {
				cursize = 0;
			} else {
				cursize = 16 * ((curstyle - 2) / 2);
			}
			curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
		}
		lab30: while (q != 0) {
			lab83: while (true) {
				t = 16;
				s = 4;
				pen = 10000;
				switch (mem[q].getb0()) {
					case 17:
					case 20:
					case 21:
					case 22:
					case 23:
						t = mem[q].getb0();
						break;
					case 18: {
						t = 18;
						pen = eqtb[9572].getInt();
					}
						break;
					case 19: {
						t = 19;
						pen = eqtb[9573].getInt();
					}
						break;
					case 16:
					case 29:
					case 27:
					case 26:
						;
						break;
					case 24:
						s = 5;
						break;
					case 28:
						s = 5;
						break;
					case 25: {
						t = 23;
						s = 6;
					}
						break;
					case 30:
					case 31:
						t = makeleftright(q, style, maxd, maxh);
						break;
					case 14: {
						curstyle = mem[q].getb1();
						s = 3;
						{
							if (curstyle < 4) {
								cursize = 0;
							} else {
								cursize = 16 * ((curstyle - 2) / 2);
							}
							curmu = xovern(fontinfo[6 + parambase[eqtb[8237 + cursize].getrh()]].getInt(), 18);
						}
						break lab83;
					}
					case 8:
					case 12:
					case 2:
					case 7:
					case 5:
					case 3:
					case 4:
					case 10:
					case 11: {
						mem[p].setrh(q);
						p = q;
						q = mem[q].getrh();
						mem[p].setrh(0);
						break lab30;
					}
					default:
						errorLogic.confusion(891);
						break;
				}
				if (rtype > 0) {
					switch (strpool[rtype * 8 + t + magicoffset]) {
						case 48:
							x = 0;
							break;
						case 49:
							if (curstyle < 4) {
								x = 15;
							} else {
								x = 0;
							}
							break;
						case 50:
							x = 15;
							break;
						case 51:
							if (curstyle < 4) {
								x = 16;
							} else {
								x = 0;
							}
							break;
						case 52:
							if (curstyle < 4) {
								x = 17;
							} else {
								x = 0;
							}
							break;
						default:
							errorLogic.confusion(893);
							break;
					}
					if (x != 0) {
						y = mathglue(eqtb[7182 + x].getrh(), curmu);
						z = newglue(y);
						mem[y].setrh(0);
						mem[p].setrh(z);
						p = z;
						mem[z].setb1(x + 1);
					}
				}
				if (mem[q + 1].getInt() != 0) {
					mem[p].setrh(mem[q + 1].getInt());
					do {
						p = mem[p].getrh();
					} while (!(mem[p].getrh() == 0));
				}
				if (penalties) {
					if (mem[q].getrh() != 0) {
						if (pen < 10000) {
							rtype = mem[mem[q].getrh()].getb0();
							if (rtype != 12) {
								if (rtype != 19) {
									z = newpenalty(pen);
									mem[p].setrh(z);
									p = z;
								}
							}
						}
					}
				}
				rtype = t;
				break;
			}
			/* lab83: */r = q;
			q = mem[q].getrh();
			freenode(r, s);
			/* lab30: */}
	}

	void pushalignment() {
		int p;
		p = getnode(5);
		mem[p].setrh(alignptr);
		mem[p].setlh(curalign);
		mem[p + 1].setlh(mem[memtop - 8].getrh());
		mem[p + 1].setrh(curspan);
		mem[p + 2].setInt(curloop);
		mem[p + 3].setInt(alignstate);
		mem[p + 4].setlh(curhead);
		mem[p + 4].setrh(curtail);
		alignptr = p;
		curhead = allocateMemoryWord();
	}

	void popalignment() {
		int p;
		{
			mem[curhead].setrh(avail);
			avail = curhead;
		}
		p = alignptr;
		curtail = mem[p + 4].getrh();
		curhead = mem[p + 4].getlh();
		alignstate = mem[p + 3].getInt();
		curloop = mem[p + 2].getInt();
		curspan = mem[p + 1].getrh();
		mem[memtop - 8].setrh(mem[p + 1].getlh());
		curalign = mem[p].getlh();
		alignptr = mem[p].getrh();
		freenode(p, 5);
	}

	void getpreambletoken() {
		/* 20 */lab20: while (true) {
			gettoken();
			while ((curchr == 256) && (curcmd == 4)) {
				gettoken();
				if (curcmd > 100) {
					expand();
					gettoken();
				}
			}
			if (curcmd == 9) {
				errorLogic.fatalError("(interwoven alignment preambles are not allowed)");
			}
			if ((curcmd == 75) && (curchr == 7193)) {
				scanoptionalequals();
				scanglue(2);
				if (eqtb[9606].getInt() > 0) {
					geqdefine(7193, 117, curval);
				} else {
					eqdefine(7193, 117, curval);
				}
				continue lab20;
			}
			break;
		}
	}

	void initalign() {
		/* 30 31 32 22 */int savecsptr;
		int p;
		savecsptr = curcs;
		pushalignment();
		alignstate = -1000000;
		if ((curlist.modefield == 203) && ((curlist.tailfield != curlist.headfield) || (curlist.auxfield.getInt() != 0))) {
			{
				printnl(262);
				print(680);
			}
			printEscapeSequence(520);
			print(894);
			{
				helpptr = 3;
				helpline[2] = 895;
				helpline[1] = 896;
				helpline[0] = 897;
			}
			errorLogic.error();
			flushmath();
		}
		pushnest();
		if (curlist.modefield == 203) {
			curlist.modefield = -1;
			curlist.auxfield.setInt(nest[nestptr - 2].auxfield.getInt());
		} else if (curlist.modefield > 0) {
			curlist.modefield = -curlist.modefield;
		}
		scanspec(6, false);
		mem[memtop - 8].setrh(0);
		curalign = memtop - 8;
		curloop = 0;
		scannerstatus = 4;
		warningindex = savecsptr;
		alignstate = -1000000;
		while (true) {
			mem[curalign].setrh(newparamglue(11));
			curalign = mem[curalign].getrh();
			if (curcmd == 5) {
				break /* lab30 */;
			}
			p = memtop - 4;
			mem[p].setrh(0);
			while (true) {
				getpreambletoken();
				if (curcmd == 6) {
					break /* lab31 */;
				}
				if ((curcmd <= 5) && (curcmd >= 4) && (alignstate == -1000000)) {
					if ((p == memtop - 4) && (curloop == 0) && (curcmd == 4)) {
						curloop = curalign;
					} else {
						{
							printnl(262);
							print(903);
						}
						{
							helpptr = 3;
							helpline[2] = 904;
							helpline[1] = 905;
							helpline[0] = 906;
						}
						errorLogic.backerror();
						break /* lab31 */;
					}
				} else if ((curcmd != 10) || (p != memtop - 4)) {
					mem[p].setrh(allocateMemoryWord());
					p = mem[p].getrh();
					mem[p].setlh(curtok);
				}
			}
			/* lab31: */mem[curalign].setrh(newnullbox());
			curalign = mem[curalign].getrh();
			mem[curalign].setlh(memtop - 9);
			mem[curalign + 1].setInt(-1073741824);
			mem[curalign + 3].setInt(mem[memtop - 4].getrh());
			p = memtop - 4;
			mem[p].setrh(0);
			lab22: while (true) {
				getpreambletoken();
				if ((curcmd <= 5) && (curcmd >= 4) && (alignstate == -1000000)) {
					break /* lab32 */;
				}
				if (curcmd == 6) {
					{
						printnl(262);
						print(907);
					}
					{
						helpptr = 3;
						helpline[2] = 904;
						helpline[1] = 905;
						helpline[0] = 908;
					}
					errorLogic.error();
					continue lab22;
				}
				mem[p].setrh(allocateMemoryWord());
				p = mem[p].getrh();
				mem[p].setlh(curtok);
			}
			/* lab32: */mem[p].setrh(allocateMemoryWord());
			p = mem[p].getrh();
			mem[p].setlh(11014);
			mem[curalign + 2].setInt(mem[memtop - 4].getrh());
		}
		/* lab30: */scannerstatus = 0;
		newsavelevel(6);
		if (eqtb[7720].getrh() != 0) {
			begintokenlist(eqtb[7720].getrh(), 13);
		}
		alignpeek();
	}

	void initspan(final int p) {
		pushnest();
		if (curlist.modefield == -102) {
			curlist.auxfield.setlh(1000);
		} else {
			curlist.auxfield.setInt(-65536000);
			normalparagraph();
		}
		curspan = p;
	}

	void initrow() {
		pushnest();
		curlist.modefield = (-103) - curlist.modefield;
		if (curlist.modefield == -102) {
			curlist.auxfield.setlh(0);
		} else {
			curlist.auxfield.setInt(0);
		}
		{
			mem[curlist.tailfield].setrh(newglue(mem[mem[memtop - 8].getrh() + 1].getlh()));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		mem[curlist.tailfield].setb1(12);
		curalign = mem[mem[memtop - 8].getrh()].getrh();
		curtail = curhead;
		initspan(curalign);
	}

	void initcol() {
		mem[curalign + 5].setlh(curcmd);
		if (curcmd == 63) {
			alignstate = 0;
		} else {
			unreadToken();
			begintokenlist(mem[curalign + 3].getInt(), 1);
		}
	}

	boolean fincol() {
		/* 10 */boolean Result;
		int p;
		int q, r;
		int s;
		int u;
		int w;
		int o;
		int n;
		if (curalign == 0) {
			errorLogic.confusion(909);
		}
		q = mem[curalign].getrh();
		if (q == 0) {
			errorLogic.confusion(909);
		}
		if (alignstate < 500000) {
			errorLogic.fatalError("(interwoven alignment preambles are not allowed)");
		}
		p = mem[q].getrh();
		if ((p == 0) && (mem[curalign + 5].getlh() < 257)) {
			if (curloop != 0) {
				mem[q].setrh(newnullbox());
				p = mem[q].getrh();
				mem[p].setlh(memtop - 9);
				mem[p + 1].setInt(-1073741824);
				curloop = mem[curloop].getrh();
				q = memtop - 4;
				r = mem[curloop + 3].getInt();
				while (r != 0) {
					mem[q].setrh(allocateMemoryWord());
					q = mem[q].getrh();
					mem[q].setlh(mem[r].getlh());
					r = mem[r].getrh();
				}
				mem[q].setrh(0);
				mem[p + 3].setInt(mem[memtop - 4].getrh());
				q = memtop - 4;
				r = mem[curloop + 2].getInt();
				while (r != 0) {
					mem[q].setrh(allocateMemoryWord());
					q = mem[q].getrh();
					mem[q].setlh(mem[r].getlh());
					r = mem[r].getrh();
				}
				mem[q].setrh(0);
				mem[p + 2].setInt(mem[memtop - 4].getrh());
				curloop = mem[curloop].getrh();
				mem[p].setrh(newglue(mem[curloop + 1].getlh()));
			} else {
				{
					printnl(262);
					print(910);
				}
				printEscapeSequence(899);
				{
					helpptr = 3;
					helpline[2] = 911;
					helpline[1] = 912;
					helpline[0] = 913;
				}
				mem[curalign + 5].setlh(257);
				errorLogic.error();
			}
		}
		if (mem[curalign + 5].getlh() != 256) {
			unsave();
			newsavelevel(6);
			{
				if (curlist.modefield == -102) {
					adjusttail = curtail;
					u = hpack(mem[curlist.headfield].getrh(), 0, 1);
					w = mem[u + 1].getInt();
					curtail = adjusttail;
					adjusttail = 0;
				} else {
					u = vpackage(mem[curlist.headfield].getrh(), 0, 1, 0);
					w = mem[u + 3].getInt();
				}
				n = 0;
				if (curspan != curalign) {
					q = curspan;
					do {
						n = n + 1;
						q = mem[mem[q].getrh()].getrh();
					} while (!(q == curalign));
					if (n > 255) {
						errorLogic.confusion(914);
					}
					q = curspan;
					while (mem[mem[q].getlh()].getrh() < n) {
						q = mem[q].getlh();
					}
					if (mem[mem[q].getlh()].getrh() > n) {
						s = getnode(2);
						mem[s].setlh(mem[q].getlh());
						mem[s].setrh(n);
						mem[q].setlh(s);
						mem[s + 1].setInt(w);
					} else if (mem[mem[q].getlh() + 1].getInt() < w) {
						mem[mem[q].getlh() + 1].setInt(w);
					}
				} else if (w > mem[curalign + 1].getInt()) {
					mem[curalign + 1].setInt(w);
				}
				mem[u].setb0(13);
				mem[u].setb1(n);
				if (totalstretch[3] != 0) {
					o = 3;
				} else if (totalstretch[2] != 0) {
					o = 2;
				} else if (totalstretch[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[u + 5].setb1(o);
				mem[u + 6].setInt(totalstretch[o]);
				if (totalshrink[3] != 0) {
					o = 3;
				} else if (totalshrink[2] != 0) {
					o = 2;
				} else if (totalshrink[1] != 0) {
					o = 1;
				} else {
					o = 0;
				}
				mem[u + 5].setb0(o);
				mem[u + 4].setInt(totalshrink[o]);
				popnest();
				mem[curlist.tailfield].setrh(u);
				curlist.tailfield = u;
			}
			{
				mem[curlist.tailfield].setrh(newglue(mem[mem[curalign].getrh() + 1].getlh()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			mem[curlist.tailfield].setb1(12);
			if (mem[curalign + 5].getlh() >= 257) {
				Result = true;
				return Result /* lab10 */;
			}
			initspan(p);
		}
		alignstate = 1000000;
		do {
			getxtoken();
		} while (!(curcmd != 10));
		curalign = p;
		initcol();
		Result = false;
		return Result;
	}

	void finrow() {
		int p;
		if (curlist.modefield == -102) {
			p = hpack(mem[curlist.headfield].getrh(), 0, 1);
			popnest();
			appendtovlist(p);
			if (curhead != curtail) {
				mem[curlist.tailfield].setrh(mem[curhead].getrh());
				curlist.tailfield = curtail;
			}
		} else {
			p = vpackage(mem[curlist.headfield].getrh(), 0, 1, 1073741823);
			popnest();
			mem[curlist.tailfield].setrh(p);
			curlist.tailfield = p;
			curlist.auxfield.setlh(1000);
		}
		mem[p].setb0(13);
		mem[p + 6].setInt(0);
		if (eqtb[7720].getrh() != 0) {
			begintokenlist(eqtb[7720].getrh(), 13);
		}
		alignpeek();
	}

	void finalign() {
		int p, q, r, s, u, v;
		int t, w;
		int o;
		int n;
		int rulesave;
		final memoryword auxsave = new memoryword();
		if (curgroup != 6) {
			errorLogic.confusion(915);
		}
		unsave();
		if (curgroup != 6) {
			errorLogic.confusion(916);
		}
		unsave();
		if (nest[nestptr - 1].modefield == 203) {
			o = eqtb[10145].getInt();
		} else {
			o = 0;
		}
		q = mem[mem[memtop - 8].getrh()].getrh();
		do {
			flushlist(mem[q + 3].getInt());
			flushlist(mem[q + 2].getInt());
			p = mem[mem[q].getrh()].getrh();
			if (mem[q + 1].getInt() == -1073741824) {
				mem[q + 1].setInt(0);
				r = mem[q].getrh();
				s = mem[r + 1].getlh();
				if (s != 0) {
					mem[0].setrh(mem[0].getrh() + 1);
					deleteglueref(s);
					mem[r + 1].setlh(0);
				}
			}
			if (mem[q].getlh() != memtop - 9) {
				t = mem[q + 1].getInt() + mem[mem[mem[q].getrh() + 1].getlh() + 1].getInt();
				r = mem[q].getlh();
				s = memtop - 9;
				mem[s].setlh(p);
				n = 1;
				do {
					mem[r + 1].setInt(mem[r + 1].getInt() - t);
					u = mem[r].getlh();
					while (mem[r].getrh() > n) {
						s = mem[s].getlh();
						n = mem[mem[s].getlh()].getrh() + 1;
					}
					if (mem[r].getrh() < n) {
						mem[r].setlh(mem[s].getlh());
						mem[s].setlh(r);
						mem[r].setrh(mem[r].getrh() - 1);
						s = r;
					} else {
						if (mem[r + 1].getInt() > mem[mem[s].getlh() + 1].getInt()) {
							mem[mem[s].getlh() + 1].setInt(mem[r + 1].getInt());
						}
						freenode(r, 2);
					}
					r = u;
				} while (!(r == memtop - 9));
			}
			mem[q].setb0(13);
			mem[q].setb1(0);
			mem[q + 3].setInt(0);
			mem[q + 2].setInt(0);
			mem[q + 5].setb1(0);
			mem[q + 5].setb0(0);
			mem[q + 6].setInt(0);
			mem[q + 4].setInt(0);
			q = p;
		} while (!(q == 0));
		saveptr = saveptr - 2;
		packbeginline = -curlist.mlfield;
		if (curlist.modefield == -1) {
			rulesave = eqtb[10146].getInt();
			eqtb[10146].setInt(0);
			p = hpack(mem[memtop - 8].getrh(), savestack[saveptr + 1].getInt(), savestack[saveptr + 0].getInt());
			eqtb[10146].setInt(rulesave);
		} else {
			q = mem[mem[memtop - 8].getrh()].getrh();
			do {
				mem[q + 3].setInt(mem[q + 1].getInt());
				mem[q + 1].setInt(0);
				q = mem[mem[q].getrh()].getrh();
			} while (!(q == 0));
			p = vpackage(mem[memtop - 8].getrh(), savestack[saveptr + 1].getInt(), savestack[saveptr + 0].getInt(), 1073741823);
			q = mem[mem[memtop - 8].getrh()].getrh();
			do {
				mem[q + 1].setInt(mem[q + 3].getInt());
				mem[q + 3].setInt(0);
				q = mem[mem[q].getrh()].getrh();
			} while (!(q == 0));
		}
		packbeginline = 0;
		q = mem[curlist.headfield].getrh();
		s = curlist.headfield;
		while (q != 0) {
			if (!(q >= himemmin)) {
				if (mem[q].getb0() == 13) {
					if (curlist.modefield == -1) {
						mem[q].setb0(0);
						mem[q + 1].setInt(mem[p + 1].getInt());
					} else {
						mem[q].setb0(1);
						mem[q + 3].setInt(mem[p + 3].getInt());
					}
					mem[q + 5].setb1(mem[p + 5].getb1());
					mem[q + 5].setb0(mem[p + 5].getb0());
					mem[q + 6].setglue(mem[p + 6].getglue());
					mem[q + 4].setInt(o);
					r = mem[mem[q + 5].getrh()].getrh();
					s = mem[mem[p + 5].getrh()].getrh();
					do {
						n = mem[r].getb1();
						t = mem[s + 1].getInt();
						w = t;
						u = memtop - 4;
						while (n > 0) {
							n = n - 1;
							s = mem[s].getrh();
							v = mem[s + 1].getlh();
							mem[u].setrh(newglue(v));
							u = mem[u].getrh();
							mem[u].setb1(12);
							t = t + mem[v + 1].getInt();
							if (mem[p + 5].getb0() == 1) {
								if (mem[v].getb0() == mem[p + 5].getb1()) {
									t = t + (int)Math.round(mem[p + 6].getglue() * mem[v + 2].getInt());
								}
							} else if (mem[p + 5].getb0() == 2) {
								if (mem[v].getb1() == mem[p + 5].getb1()) {
									t = t - (int)Math.round(mem[p + 6].getglue() * mem[v + 3].getInt());
								}
							}
							s = mem[s].getrh();
							mem[u].setrh(newnullbox());
							u = mem[u].getrh();
							t = t + mem[s + 1].getInt();
							if (curlist.modefield == -1) {
								mem[u + 1].setInt(mem[s + 1].getInt());
							} else {
								mem[u].setb0(1);
								mem[u + 3].setInt(mem[s + 1].getInt());
							}
						}
						if (curlist.modefield == -1) {
							mem[r + 3].setInt(mem[q + 3].getInt());
							mem[r + 2].setInt(mem[q + 2].getInt());
							if (t == mem[r + 1].getInt()) {
								mem[r + 5].setb0(0);
								mem[r + 5].setb1(0);
								mem[r + 6].setglue(0.0);
							} else if (t > mem[r + 1].getInt()) {
								mem[r + 5].setb0(1);
								if (mem[r + 6].getInt() == 0) {
									mem[r + 6].setglue(0.0);
								} else {
									mem[r + 6].setglue((t - mem[r + 1].getInt()) / ((double)mem[r + 6].getInt()));
								}
							} else {
								mem[r + 5].setb1(mem[r + 5].getb0());
								mem[r + 5].setb0(2);
								if (mem[r + 4].getInt() == 0) {
									mem[r + 6].setglue(0.0);
								} else if ((mem[r + 5].getb1() == 0) && (mem[r + 1].getInt() - t > mem[r + 4].getInt())) {
									mem[r + 6].setglue(1.0);
								} else {
									mem[r + 6].setglue((mem[r + 1].getInt() - t) / ((double)mem[r + 4].getInt()));
								}
							}
							mem[r + 1].setInt(w);
							mem[r].setb0(0);
						} else {
							mem[r + 1].setInt(mem[q + 1].getInt());
							if (t == mem[r + 3].getInt()) {
								mem[r + 5].setb0(0);
								mem[r + 5].setb1(0);
								mem[r + 6].setglue(0.0);
							} else if (t > mem[r + 3].getInt()) {
								mem[r + 5].setb0(1);
								if (mem[r + 6].getInt() == 0) {
									mem[r + 6].setglue(0.0);
								} else {
									mem[r + 6].setglue((t - mem[r + 3].getInt()) / ((double)mem[r + 6].getInt()));
								}
							} else {
								mem[r + 5].setb1(mem[r + 5].getb0());
								mem[r + 5].setb0(2);
								if (mem[r + 4].getInt() == 0) {
									mem[r + 6].setglue(0.0);
								} else if ((mem[r + 5].getb1() == 0) && (mem[r + 3].getInt() - t > mem[r + 4].getInt())) {
									mem[r + 6].setglue(1.0);
								} else {
									mem[r + 6].setglue((mem[r + 3].getInt() - t) / ((double)mem[r + 4].getInt()));
								}
							}
							mem[r + 3].setInt(w);
							mem[r].setb0(1);
						}
						mem[r + 4].setInt(0);
						if (u != memtop - 4) {
							mem[u].setrh(mem[r].getrh());
							mem[r].setrh(mem[memtop - 4].getrh());
							r = u;
						}
						r = mem[mem[r].getrh()].getrh();
						s = mem[mem[s].getrh()].getrh();
					} while (!(r == 0));
				} else if (mem[q].getb0() == 2) {
					if ((mem[q + 1].getInt() == -1073741824)) {
						mem[q + 1].setInt(mem[p + 1].getInt());
					}
					if ((mem[q + 3].getInt() == -1073741824)) {
						mem[q + 3].setInt(mem[p + 3].getInt());
					}
					if ((mem[q + 2].getInt() == -1073741824)) {
						mem[q + 2].setInt(mem[p + 2].getInt());
					}
					if (o != 0) {
						r = mem[q].getrh();
						mem[q].setrh(0);
						q = hpack(q, 0, 1);
						mem[q + 4].setInt(o);
						mem[q].setrh(r);
						mem[s].setrh(q);
					}
				}
			}
			s = q;
			q = mem[q].getrh();
		}
		flushnodelist(p);
		popalignment();
		auxsave.copy(curlist.auxfield);
		p = mem[curlist.headfield].getrh();
		q = curlist.tailfield;
		popnest();
		if (curlist.modefield == 203) {
			doassignments();
			if (curcmd != 3) {
				{
					printnl(262);
					print(1170);
				}
				{
					helpptr = 2;
					helpline[1] = 895;
					helpline[0] = 896;
				}
				errorLogic.backerror();
			} else {
				getxtoken();
				if (curcmd != 3) {
					{
						printnl(262);
						print(1166);
					}
					{
						helpptr = 2;
						helpline[1] = 1167;
						helpline[0] = 1168;
					}
					errorLogic.backerror();
				}
			}
			popnest();
			{
				mem[curlist.tailfield].setrh(newpenalty(eqtb[9574].getInt()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			{
				mem[curlist.tailfield].setrh(newparamglue(3));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			mem[curlist.tailfield].setrh(p);
			if (p != 0) {
				curlist.tailfield = q;
			}
			{
				mem[curlist.tailfield].setrh(newpenalty(eqtb[9575].getInt()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			{
				mem[curlist.tailfield].setrh(newparamglue(4));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			curlist.auxfield.setInt(auxsave.getInt());
			resumeafterdisplay();
		} else {
			curlist.auxfield.copy(auxsave);
			mem[curlist.tailfield].setrh(p);
			if (p != 0) {
				curlist.tailfield = q;
			}
			if (curlist.modefield == 1) {
				buildpage();
			}
		}
	}

	void alignpeek() {
		/* 20 */lab20: while (true) {
			alignstate = 1000000;
			do {
				getxtoken();
			} while (!(curcmd != 10));
			if (curcmd == 34) {
				scanleftbrace();
				newsavelevel(7);
				if (curlist.modefield == -1) {
					normalparagraph();
				}
			} else if (curcmd == 2) {
				finalign();
			} else if ((curcmd == 5) && (curchr == 258)) {
				continue lab20;
			} else {
				initrow();
				initcol();
			}
			break;
		}
	}

	int finiteshrink(final int p) {
		int Result;
		int q;
		if (noshrinkerroryet) {
			noshrinkerroryet = false;
			{
				printnl(262);
				print(917);
			}
			{
				helpptr = 5;
				helpline[4] = 918;
				helpline[3] = 919;
				helpline[2] = 920;
				helpline[1] = 921;
				helpline[0] = 922;
			}
			errorLogic.error();
		}
		q = newspec(p);
		mem[q].setb1(0);
		deleteglueref(p);
		Result = q;
		return Result;
	}

	void trybreak(int pi, final int breaktype) {
		/* 10 30 31 22 61 */int r;
		int prevr;
		int oldl;
		boolean nobreakyet;
		int prevprevr;
		int s;
		int q;
		int v;
		int t;
		int f;
		int l;
		boolean noderstaysactive;
		int linewidth;
		int fitclass;
		int b;
		int d;
		boolean artificialdemerits;
		int shortfall;
		if (Math.abs(pi) >= 10000) {
			if (pi > 0) {
				return /* lab10 */;
			} else {
				pi = -10000;
			}
		}
		nobreakyet = true;
		prevr = memtop - 7;
		oldl = 0;
		curactivewidth[1] = activewidth[1];
		curactivewidth[2] = activewidth[2];
		curactivewidth[3] = activewidth[3];
		curactivewidth[4] = activewidth[4];
		curactivewidth[5] = activewidth[5];
		curactivewidth[6] = activewidth[6];
		linewidth = 0;
		prevprevr = 0;
		lab22: while (true) {
			r = mem[prevr].getrh();
			if (mem[r].getb0() == 2) {
				curactivewidth[1] = curactivewidth[1] + mem[r + 1].getInt();
				curactivewidth[2] = curactivewidth[2] + mem[r + 2].getInt();
				curactivewidth[3] = curactivewidth[3] + mem[r + 3].getInt();
				curactivewidth[4] = curactivewidth[4] + mem[r + 4].getInt();
				curactivewidth[5] = curactivewidth[5] + mem[r + 5].getInt();
				curactivewidth[6] = curactivewidth[6] + mem[r + 6].getInt();
				prevprevr = prevr;
				prevr = r;
				continue lab22;
			}
			{
				l = mem[r + 1].getlh();
				if (l > oldl) {
					if ((minimumdemerits < 1073741823) && ((oldl != easyline) || (r == memtop - 7))) {
						if (nobreakyet) {
							nobreakyet = false;
							breakwidth[1] = background[1];
							breakwidth[2] = background[2];
							breakwidth[3] = background[3];
							breakwidth[4] = background[4];
							breakwidth[5] = background[5];
							breakwidth[6] = background[6];
							s = curp;
							if (breaktype > 0) {
								if (curp != 0) {
									t = mem[curp].getb1();
									v = curp;
									s = mem[curp + 1].getrh();
									while (t > 0) {
										t = t - 1;
										v = mem[v].getrh();
										if ((v >= himemmin)) {
											f = mem[v].getb0();
											breakwidth[1] = breakwidth[1] - fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[v].getb1()].getb0()].getInt();
										} else {
											switch (mem[v].getb0()) {
												case 6: {
													f = mem[v + 1].getb0();
													breakwidth[1] = breakwidth[1] - fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[v + 1].getb1()].getb0()].getInt();
												}
													break;
												case 0:
												case 1:
												case 2:
												case 11:
													breakwidth[1] = breakwidth[1] - mem[v + 1].getInt();
													break;
												default:
													errorLogic.confusion(923);
													break;
											}
										}
									}
									while (s != 0) {
										if ((s >= himemmin)) {
											f = mem[s].getb0();
											breakwidth[1] = breakwidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s].getb1()].getb0()].getInt();
										} else {
											switch (mem[s].getb0()) {
												case 6: {
													f = mem[s + 1].getb0();
													breakwidth[1] = breakwidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s + 1].getb1()].getb0()].getInt();
												}
													break;
												case 0:
												case 1:
												case 2:
												case 11:
													breakwidth[1] = breakwidth[1] + mem[s + 1].getInt();
													break;
												default:
													errorLogic.confusion(924);
													break;
											}
										}
										s = mem[s].getrh();
									}
									breakwidth[1] = breakwidth[1] + discwidth;
									if (mem[curp + 1].getrh() == 0) {
										s = mem[v].getrh();
									}
								}
							}
							lab30: while (s != 0) {
								if ((s >= himemmin)) {
									break lab30;
								}
								switch (mem[s].getb0()) {
									case 10: {
										v = mem[s + 1].getlh();
										breakwidth[1] = breakwidth[1] - mem[v + 1].getInt();
										breakwidth[2 + mem[v].getb0()] = breakwidth[2 + mem[v].getb0()] - mem[v + 2].getInt();
										breakwidth[6] = breakwidth[6] - mem[v + 3].getInt();
									}
										break;
									case 12:
										;
										break;
									case 9:
										breakwidth[1] = breakwidth[1] - mem[s + 1].getInt();
										break;
									case 11:
										if (mem[s].getb1() != 1) {
											break lab30;
										} else {
											breakwidth[1] = breakwidth[1] - mem[s + 1].getInt();
										}
										break;
									default:
										break lab30;
								}
								s = mem[s].getrh();
							}
							/* lab30: */}
						if (mem[prevr].getb0() == 2) {
							mem[prevr + 1].setInt(mem[prevr + 1].getInt() - curactivewidth[1] + breakwidth[1]);
							mem[prevr + 2].setInt(mem[prevr + 2].getInt() - curactivewidth[2] + breakwidth[2]);
							mem[prevr + 3].setInt(mem[prevr + 3].getInt() - curactivewidth[3] + breakwidth[3]);
							mem[prevr + 4].setInt(mem[prevr + 4].getInt() - curactivewidth[4] + breakwidth[4]);
							mem[prevr + 5].setInt(mem[prevr + 5].getInt() - curactivewidth[5] + breakwidth[5]);
							mem[prevr + 6].setInt(mem[prevr + 6].getInt() - curactivewidth[6] + breakwidth[6]);
						} else if (prevr == memtop - 7) {
							activewidth[1] = breakwidth[1];
							activewidth[2] = breakwidth[2];
							activewidth[3] = breakwidth[3];
							activewidth[4] = breakwidth[4];
							activewidth[5] = breakwidth[5];
							activewidth[6] = breakwidth[6];
						} else {
							q = getnode(7);
							mem[q].setrh(r);
							mem[q].setb0(2);
							mem[q].setb1(0);
							mem[q + 1].setInt(breakwidth[1] - curactivewidth[1]);
							mem[q + 2].setInt(breakwidth[2] - curactivewidth[2]);
							mem[q + 3].setInt(breakwidth[3] - curactivewidth[3]);
							mem[q + 4].setInt(breakwidth[4] - curactivewidth[4]);
							mem[q + 5].setInt(breakwidth[5] - curactivewidth[5]);
							mem[q + 6].setInt(breakwidth[6] - curactivewidth[6]);
							mem[prevr].setrh(q);
							prevprevr = prevr;
							prevr = q;
						}
						if (Math.abs(eqtb[9579].getInt()) >= 1073741823 - minimumdemerits) {
							minimumdemerits = 1073741822;
						} else {
							minimumdemerits = minimumdemerits + Math.abs(eqtb[9579].getInt());
						}
						for (fitclass = 0; fitclass <= 3; fitclass++) {
							if (minimaldemerits[fitclass] <= minimumdemerits) {
								q = getnode(2);
								mem[q].setrh(passive);
								passive = q;
								mem[q + 1].setrh(curp);
								mem[q + 1].setlh(bestplace[fitclass]);
								q = getnode(3);
								mem[q + 1].setrh(passive);
								mem[q + 1].setlh(bestplline[fitclass] + 1);
								mem[q].setb1(fitclass);
								mem[q].setb0(breaktype);
								mem[q + 2].setInt(minimaldemerits[fitclass]);
								mem[q].setrh(r);
								mem[prevr].setrh(q);
								prevr = q;
							}
							minimaldemerits[fitclass] = 1073741823;
						}
						minimumdemerits = 1073741823;
						if (r != memtop - 7) {
							q = getnode(7);
							mem[q].setrh(r);
							mem[q].setb0(2);
							mem[q].setb1(0);
							mem[q + 1].setInt(curactivewidth[1] - breakwidth[1]);
							mem[q + 2].setInt(curactivewidth[2] - breakwidth[2]);
							mem[q + 3].setInt(curactivewidth[3] - breakwidth[3]);
							mem[q + 4].setInt(curactivewidth[4] - breakwidth[4]);
							mem[q + 5].setInt(curactivewidth[5] - breakwidth[5]);
							mem[q + 6].setInt(curactivewidth[6] - breakwidth[6]);
							mem[prevr].setrh(q);
							prevprevr = prevr;
							prevr = q;
						}
					}
					if (r == memtop - 7) {
						return /* lab10 */;
					}
					if (l > easyline) {
						linewidth = secondwidth;
						oldl = maxhalfword - 1;
					} else {
						oldl = l;
						if (l > lastspecialline) {
							linewidth = secondwidth;
						} else if (eqtb[7712].getrh() == 0) {
							linewidth = firstwidth;
						} else {
							linewidth = mem[eqtb[7712].getrh() + 2 * l].getInt();
						}
					}
				}
			}
			{
				artificialdemerits = false;
				shortfall = linewidth - curactivewidth[1];
				if (shortfall > 0) {
					if ((curactivewidth[3] != 0) || (curactivewidth[4] != 0) || (curactivewidth[5] != 0)) {
						b = 0;
						fitclass = 2;
					} else {
						lab31: while (true) {
							if (shortfall > 7230584) {
								if (curactivewidth[2] < 1663497) {
									b = 10000;
									fitclass = 0;
									break lab31;
								}
							}
							b = badness(shortfall, curactivewidth[2]);
							if (b > 12) {
								if (b > 99) {
									fitclass = 0;
								} else {
									fitclass = 1;
								}
							} else {
								fitclass = 2;
							}
							break;
						}
						/* lab31: */}
				} else {
					if (-shortfall > curactivewidth[6]) {
						b = 10001;
					} else {
						b = badness(-shortfall, curactivewidth[6]);
					}
					if (b > 12) {
						fitclass = 3;
					} else {
						fitclass = 2;
					}
				}
				lab61: while (true) {
					if ((b > 10000) || (pi == -10000)) {
						if (finalpass && (minimumdemerits == 1073741823) && (mem[r].getrh() == memtop - 7) && (prevr == memtop - 7)) {
							artificialdemerits = true;
						} else if (b > threshold) {
							break lab61;
						}
						noderstaysactive = false;
					} else {
						prevr = r;
						if (b > threshold) {
							continue lab22;
						}
						noderstaysactive = true;
					}
					if (artificialdemerits) {
						d = 0;
					} else {
						d = eqtb[9565].getInt() + b;
						if (Math.abs(d) >= 10000) {
							d = 100000000;
						} else {
							d = d * d;
						}
						if (pi != 0) {
							if (pi > 0) {
								d = d + pi * pi;
							} else if (pi > -10000) {
								d = d - pi * pi;
							}
						}
						if ((breaktype == 1) && (mem[r].getb0() == 1)) {
							if (curp != 0) {
								d = d + eqtb[9577].getInt();
							} else {
								d = d + eqtb[9578].getInt();
							}
						}
						if (Math.abs(fitclass - mem[r].getb1()) > 1) {
							d = d + eqtb[9579].getInt();
						}
					}
					d = d + mem[r + 2].getInt();
					if (d <= minimaldemerits[fitclass]) {
						minimaldemerits[fitclass] = d;
						bestplace[fitclass] = mem[r + 1].getrh();
						bestplline[fitclass] = l;
						if (d < minimumdemerits) {
							minimumdemerits = d;
						}
					}
					if (noderstaysactive) {
						continue lab22;
					}
					break;
				}
				/* lab61: */mem[prevr].setrh(mem[r].getrh());
				freenode(r, 3);
				if (prevr == memtop - 7) {
					r = mem[memtop - 7].getrh();
					if (mem[r].getb0() == 2) {
						activewidth[1] = activewidth[1] + mem[r + 1].getInt();
						activewidth[2] = activewidth[2] + mem[r + 2].getInt();
						activewidth[3] = activewidth[3] + mem[r + 3].getInt();
						activewidth[4] = activewidth[4] + mem[r + 4].getInt();
						activewidth[5] = activewidth[5] + mem[r + 5].getInt();
						activewidth[6] = activewidth[6] + mem[r + 6].getInt();
						curactivewidth[1] = activewidth[1];
						curactivewidth[2] = activewidth[2];
						curactivewidth[3] = activewidth[3];
						curactivewidth[4] = activewidth[4];
						curactivewidth[5] = activewidth[5];
						curactivewidth[6] = activewidth[6];
						mem[memtop - 7].setrh(mem[r].getrh());
						freenode(r, 7);
					}
				} else if (mem[prevr].getb0() == 2) {
					r = mem[prevr].getrh();
					if (r == memtop - 7) {
						curactivewidth[1] = curactivewidth[1] - mem[prevr + 1].getInt();
						curactivewidth[2] = curactivewidth[2] - mem[prevr + 2].getInt();
						curactivewidth[3] = curactivewidth[3] - mem[prevr + 3].getInt();
						curactivewidth[4] = curactivewidth[4] - mem[prevr + 4].getInt();
						curactivewidth[5] = curactivewidth[5] - mem[prevr + 5].getInt();
						curactivewidth[6] = curactivewidth[6] - mem[prevr + 6].getInt();
						mem[prevprevr].setrh(memtop - 7);
						freenode(prevr, 7);
						prevr = prevprevr;
					} else if (mem[r].getb0() == 2) {
						curactivewidth[1] = curactivewidth[1] + mem[r + 1].getInt();
						curactivewidth[2] = curactivewidth[2] + mem[r + 2].getInt();
						curactivewidth[3] = curactivewidth[3] + mem[r + 3].getInt();
						curactivewidth[4] = curactivewidth[4] + mem[r + 4].getInt();
						curactivewidth[5] = curactivewidth[5] + mem[r + 5].getInt();
						curactivewidth[6] = curactivewidth[6] + mem[r + 6].getInt();
						mem[prevr + 1].setInt(mem[prevr + 1].getInt() + mem[r + 1].getInt());
						mem[prevr + 2].setInt(mem[prevr + 2].getInt() + mem[r + 2].getInt());
						mem[prevr + 3].setInt(mem[prevr + 3].getInt() + mem[r + 3].getInt());
						mem[prevr + 4].setInt(mem[prevr + 4].getInt() + mem[r + 4].getInt());
						mem[prevr + 5].setInt(mem[prevr + 5].getInt() + mem[r + 5].getInt());
						mem[prevr + 6].setInt(mem[prevr + 6].getInt() + mem[r + 6].getInt());
						mem[prevr].setrh(mem[r].getrh());
						freenode(r, 7);
					}
				}
			}
		}
	}

	void postlinebreak(final int finalwidowpenalty) {
		/* 30 31 */int q, r, s;
		boolean discbreak;
		boolean postdiscbreak;
		int curwidth;
		int curindent;
		int t;
		int pen;
		int curline;
		q = mem[bestbet + 1].getrh();
		curp = 0;
		do {
			r = q;
			q = mem[q + 1].getlh();
			mem[r + 1].setlh(curp);
			curp = r;
		} while (!(q == 0));
		curline = curlist.pgfield + 1;
		do {
			q = mem[curp + 1].getrh();
			discbreak = false;
			postdiscbreak = false;
			lab30: while (true) {
				if (q != 0) {
					if (mem[q].getb0() == 10) {
						deleteglueref(mem[q + 1].getlh());
						mem[q + 1].setlh(eqtb[7190].getrh());
						mem[q].setb1(9);
						mem[eqtb[7190].getrh()].setrh(mem[eqtb[7190].getrh()].getrh() + 1);
						break lab30;
					} else {
						if (mem[q].getb0() == 7) {
							t = mem[q].getb1();
							if (t == 0) {
								r = mem[q].getrh();
							} else {
								r = q;
								while (t > 1) {
									r = mem[r].getrh();
									t = t - 1;
								}
								s = mem[r].getrh();
								r = mem[s].getrh();
								mem[s].setrh(0);
								flushnodelist(mem[q].getrh());
								mem[q].setb1(0);
							}
							if (mem[q + 1].getrh() != 0) {
								s = mem[q + 1].getrh();
								while (mem[s].getrh() != 0) {
									s = mem[s].getrh();
								}
								mem[s].setrh(r);
								r = mem[q + 1].getrh();
								mem[q + 1].setrh(0);
								postdiscbreak = true;
							}
							if (mem[q + 1].getlh() != 0) {
								s = mem[q + 1].getlh();
								mem[q].setrh(s);
								while (mem[s].getrh() != 0) {
									s = mem[s].getrh();
								}
								mem[q + 1].setlh(0);
								q = s;
							}
							mem[q].setrh(r);
							discbreak = true;
						} else if ((mem[q].getb0() == 9) || (mem[q].getb0() == 11)) {
							mem[q + 1].setInt(0);
						}
					}
				} else {
					q = memtop - 3;
					while (mem[q].getrh() != 0) {
						q = mem[q].getrh();
					}
				}
				r = newparamglue(8);
				mem[r].setrh(mem[q].getrh());
				mem[q].setrh(r);
				q = r;
				break;
			}
			/* lab30: */r = mem[q].getrh();
			mem[q].setrh(0);
			q = mem[memtop - 3].getrh();
			mem[memtop - 3].setrh(r);
			if (eqtb[7189].getrh() != 0) {
				r = newparamglue(7);
				mem[r].setrh(q);
				q = r;
			}
			if (curline > lastspecialline) {
				curwidth = secondwidth;
				curindent = secondindent;
			} else if (eqtb[7712].getrh() == 0) {
				curwidth = firstwidth;
				curindent = firstindent;
			} else {
				curwidth = mem[eqtb[7712].getrh() + 2 * curline].getInt();
				curindent = mem[eqtb[7712].getrh() + 2 * curline - 1].getInt();
			}
			adjusttail = memtop - 5;
			justbox = hpack(q, curwidth, 0);
			mem[justbox + 4].setInt(curindent);
			appendtovlist(justbox);
			if (memtop - 5 != adjusttail) {
				mem[curlist.tailfield].setrh(mem[memtop - 5].getrh());
				curlist.tailfield = adjusttail;
			}
			adjusttail = 0;
			if (curline + 1 != bestline) {
				pen = eqtb[9576].getInt();
				if (curline == curlist.pgfield + 1) {
					pen = pen + eqtb[9568].getInt();
				}
				if (curline + 2 == bestline) {
					pen = pen + finalwidowpenalty;
				}
				if (discbreak) {
					pen = pen + eqtb[9571].getInt();
				}
				if (pen != 0) {
					r = newpenalty(pen);
					mem[curlist.tailfield].setrh(r);
					curlist.tailfield = r;
				}
			}
			curline = curline + 1;
			curp = mem[curp + 1].getlh();
			if (curp != 0) {
				if (!postdiscbreak) {
					r = memtop - 3;
					while (true) {
						q = mem[r].getrh();
						if (q == mem[curp + 1].getrh()) {
							break /* lab31 */;
						}
						if ((q >= himemmin)) {
							break /* lab31 */;
						}
						if ((mem[q].getb0() < 9)) {
							break /* lab31 */;
						}
						if (mem[q].getb0() == 11) {
							if (mem[q].getb1() != 1) {
								break /* lab31 */;
							}
						}
						r = q;
					}
					/* lab31: */if (r != memtop - 3) {
						mem[r].setrh(0);
						flushnodelist(mem[memtop - 3].getrh());
						mem[memtop - 3].setrh(q);
					}
				}
			}
		} while (!(curp == 0));
		if ((curline != bestline) || (mem[memtop - 3].getrh() != 0)) {
			errorLogic.confusion(939);
		}
		curlist.pgfield = bestline - 1;
	}

	int reconstitute(int j, final int n, int bchar, int hchar) {
		/* 22 30 */int Result;
		int p;
		int t;
		final fourquarters q = new fourquarters();
		int currh;
		int testchar;
		int w;
		int k;
		hyphenpassed = 0;
		t = memtop - 4;
		w = 0;
		mem[memtop - 4].setrh(0);
		curl = hu[j];
		curq = t;
		if (j == 0) {
			ligaturepresent = initlig;
			p = initlist;
			if (ligaturepresent) {
				lfthit = initlft;
			}
			while (p > 0) {
				{
					mem[t].setrh(allocateMemoryWord());
					t = mem[t].getrh();
					mem[t].setb0(hf);
					mem[t].setb1(mem[p].getb1());
				}
				p = mem[p].getrh();
			}
		} else if (curl < 256) {
			mem[t].setrh(allocateMemoryWord());
			t = mem[t].getrh();
			mem[t].setb0(hf);
			mem[t].setb1(curl);
		}
		ligstack = 0;
		{
			if (j < n) {
				curr = hu[j + 1];
			} else {
				curr = bchar;
			}
			if (((hyf[j]) % 2 == 1)) {
				currh = hchar;
			} else {
				currh = 256;
			}
		}
		lab22: while (true) {
			lab30: while (true) {
				if (curl == 256) {
					k = bcharlabel[hf];
					if (k == 0) {
						break lab30;
					} else {
						q.copy(fontinfo[k].qqqq());
					}
				} else {
					q.copy(fontinfo[charbase[hf] + curl].qqqq());
					if (((q.b2) % 4) != 1) {
						break lab30;
					}
					k = ligkernbase[hf] + q.b3;
					q.copy(fontinfo[k].qqqq());
					if (q.b0 > 128) {
						k = ligkernbase[hf] + 256 * q.b2 + q.b3 + 32768 - 256 * (128);
						q.copy(fontinfo[k].qqqq());
					}
				}
				if (currh < 256) {
					testchar = currh;
				} else {
					testchar = curr;
				}
				while (true) {
					if (q.b1 == testchar) {
						if (q.b0 <= 128) {
							if (currh < 256) {
								hyphenpassed = j;
								hchar = 256;
								currh = 256;
								continue lab22;
							} else {
								if (hchar < 256) {
									if (((hyf[j]) % 2 == 1)) {
										hyphenpassed = j;
										hchar = 256;
									}
								}
								if (q.b2 < 128) {
									if (curl == 256) {
										lfthit = true;
									}
									if (j == n) {
										if (ligstack == 0) {
											rthit = true;
										}
									}
									switch (q.b2) {
										case 1:
										case 5: {
											curl = q.b3;
											ligaturepresent = true;
										}
											break;
										case 2:
										case 6: {
											curr = q.b3;
											if (ligstack > 0) {
												mem[ligstack].setb1(curr);
											} else {
												ligstack = newligitem(curr);
												if (j == n) {
													bchar = 256;
												} else {
													p = allocateMemoryWord();
													mem[ligstack + 1].setrh(p);
													mem[p].setb1(hu[j + 1]);
													mem[p].setb0(hf);
												}
											}
										}
											break;
										case 3: {
											curr = q.b3;
											p = ligstack;
											ligstack = newligitem(curr);
											mem[ligstack].setrh(p);
										}
											break;
										case 7:
										case 11: {
											if (ligaturepresent) {
												p = newligature(hf, curl, mem[curq].getrh());
												if (lfthit) {
													mem[p].setb1(2);
													lfthit = false;
												}
												mem[curq].setrh(p);
												t = p;
												ligaturepresent = false;
											}
											curq = t;
											curl = q.b3;
											ligaturepresent = true;
										}
											break;
										default: {
											curl = q.b3;
											ligaturepresent = true;
											if (ligstack > 0) {
												if (mem[ligstack + 1].getrh() > 0) {
													mem[t].setrh(mem[ligstack + 1].getrh());
													t = mem[t].getrh();
													j = j + 1;
												}
												p = ligstack;
												ligstack = mem[p].getrh();
												freenode(p, 2);
												if (ligstack == 0) {
													if (j < n) {
														curr = hu[j + 1];
													} else {
														curr = bchar;
													}
													if (((hyf[j]) % 2 == 1)) {
														currh = hchar;
													} else {
														currh = 256;
													}
												} else {
													curr = mem[ligstack].getb1();
												}
											} else if (j == n) {
												break lab30;
											} else {
												{
													mem[t].setrh(allocateMemoryWord());
													t = mem[t].getrh();
													mem[t].setb0(hf);
													mem[t].setb1(curr);
												}
												j = j + 1;
												{
													if (j < n) {
														curr = hu[j + 1];
													} else {
														curr = bchar;
													}
													if (((hyf[j]) % 2 == 1)) {
														currh = hchar;
													} else {
														currh = 256;
													}
												}
											}
										}
											break;
									}
									if (q.b2 > 4) {
										if (q.b2 != 7) {
											break lab30;
										}
									}
									continue lab22;
								}
								w = fontinfo[kernbase[hf] + 256 * q.b2 + q.b3].getInt();
								break lab30;
							}
						}
					}
					if (q.b0 >= 128) {
						if (currh == 256) {
							break lab30;
						} else {
							currh = 256;
							continue lab22;
						}
					}
					k = k + q.b0 + 1;
					q.copy(fontinfo[k].qqqq());
				}
			}
			/* lab30: */if (ligaturepresent) {
				p = newligature(hf, curl, mem[curq].getrh());
				if (lfthit) {
					mem[p].setb1(2);
					lfthit = false;
				}
				if (rthit) {
					if (ligstack == 0) {
						mem[p].setb1(mem[p].getb1() + 1);
						rthit = false;
					}
				}
				mem[curq].setrh(p);
				t = p;
				ligaturepresent = false;
			}
			if (w != 0) {
				mem[t].setrh(newkern(w));
				t = mem[t].getrh();
				w = 0;
			}
			if (ligstack > 0) {
				curq = t;
				curl = mem[ligstack].getb1();
				ligaturepresent = true;
				{
					if (mem[ligstack + 1].getrh() > 0) {
						mem[t].setrh(mem[ligstack + 1].getrh());
						t = mem[t].getrh();
						j = j + 1;
					}
					p = ligstack;
					ligstack = mem[p].getrh();
					freenode(p, 2);
					if (ligstack == 0) {
						if (j < n) {
							curr = hu[j + 1];
						} else {
							curr = bchar;
						}
						if (((hyf[j]) % 2 == 1)) {
							currh = hchar;
						} else {
							currh = 256;
						}
					} else {
						curr = mem[ligstack].getb1();
					}
				}
				continue lab22;
			}
			break;
		}
		Result = j;
		return Result;
	}

	void hyphenate() {
		/* 50 30 40 41 42 45 10 */int i, j, l;
		int q, r, s;
		int bchar;
		int majortail, minortail;
		int c;
		int cloc;
		int rcount;
		int hyfnode;
		int z;
		int v;
		int h;
		int k;
		int u;
		for (j = 0; j <= hn; j++) {
			hyf[j] = 0;
		}
		c = 0;
		lab40: while (true) {
			h = hc[1];
			hn = hn + 1;
			hc[hn] = curlang;
			for (j = 2; j <= hn; j++) {
				h = (h + h + hc[j]) % 607;
			}
			lab45: while (true) {
				k = hyphword[h];
				if (k == 0) {
					break lab45;
				}
				if ((strstart[k + 1] - strstart[k]) < hn) {
					break lab45;
				}
				lab30: while (true) {
					if ((strstart[k + 1] - strstart[k]) == hn) {
						j = 1;
						u = strstart[k];
						do {
							if (strpool[u] < hc[j]) {
								break lab45;
							}
							if (strpool[u] > hc[j]) {
								break lab30;
							}
							j = j + 1;
							u = u + 1;
						} while (!(j > hn));
						s = hyphlist[h];
						while (s != 0) {
							hyf[mem[s].getlh()] = 1;
							s = mem[s].getrh();
						}
						hn = hn - 1;
						break lab40;
					}
					break;
				}
				/* lab30: */if (h > 0) {
					h = h - 1;
				} else {
					h = 607;
				}
			}
			/* lab45: */hn = hn - 1;
			if ((trie[curlang + 1].lh / 256) != curlang) {
				return /* lab10 */;
			}
			hc[0] = 0;
			hc[hn + 1] = 0;
			hc[hn + 2] = 256;
			for (j = 0; j <= hn - rhyf + 1; j++) {
				z = trie[curlang + 1].rh + hc[j];
				l = j;
				while (hc[l] == (trie[z].lh / 256)) {
					if ((trie[z].lh % 256) != 0) {
						v = (trie[z].lh % 256);
						do {
							v = v + opstart[curlang];
							i = l - hyfdistance[v];
							if (hyfnum[v] > hyf[i]) {
								hyf[i] = hyfnum[v];
							}
							v = hyfnext[v];
						} while (!(v == 0));
					}
					l = l + 1;
					z = trie[z].rh + hc[l];
				}
			}
			break;
		}
		/* lab40: */for (j = 0; j <= lhyf - 1; j++) {
			hyf[j] = 0;
		}
		for (j = 0; j <= rhyf - 1; j++) {
			hyf[hn - j] = 0;
		}
		lab41: while (true) {
			for (j = lhyf; j <= hn - rhyf; j++) {
				if (((hyf[j]) % 2 == 1)) {
					break lab41;
				}
			}
			return /* lab10 */;
		}
		/* lab41: */q = mem[hb].getrh();
		mem[hb].setrh(0);
		r = mem[ha].getrh();
		mem[ha].setrh(0);
		bchar = hyfbchar;
		lab50: while (true) {
			lab42: while (true) {
				if ((ha >= himemmin)) {
					if (mem[ha].getb0() != hf) {
						break lab42;
					} else {
						initlist = ha;
						initlig = false;
						hu[0] = mem[ha].getb1();
					}
				} else if (mem[ha].getb0() == 6) {
					if (mem[ha + 1].getb0() != hf) {
						break lab42;
					} else {
						initlist = mem[ha + 1].getrh();
						initlig = true;
						initlft = (mem[ha].getb1() > 1);
						hu[0] = mem[ha + 1].getb1();
						if (initlist == 0) {
							if (initlft) {
								hu[0] = 256;
								initlig = false;
							}
						}
						freenode(ha, 2);
					}
				} else {
					if (!(r >= himemmin)) {
						if (mem[r].getb0() == 6) {
							if (mem[r].getb1() > 1) {
								break lab42;
							}
						}
					}
					j = 1;
					s = ha;
					initlist = 0;
					break lab50;
				}
				s = curp;
				while (mem[s].getrh() != ha) {
					s = mem[s].getrh();
				}
				j = 0;
				break lab50;
			}
			/* lab42: */s = ha;
			j = 0;
			hu[0] = 256;
			initlig = false;
			initlist = 0;
			break;
		}
		/* lab50: */flushnodelist(r);
		do {
			l = j;
			j = reconstitute(j, hn, bchar, hyfchar) + 1;
			if (hyphenpassed == 0) {
				mem[s].setrh(mem[memtop - 4].getrh());
				while (mem[s].getrh() > 0) {
					s = mem[s].getrh();
				}
				if (((hyf[j - 1]) % 2 == 1)) {
					l = j;
					hyphenpassed = j - 1;
					mem[memtop - 4].setrh(0);
				}
			}
			if (hyphenpassed > 0) {
				do {
					r = getnode(2);
					mem[r].setrh(mem[memtop - 4].getrh());
					mem[r].setb0(7);
					majortail = r;
					rcount = 0;
					while (mem[majortail].getrh() > 0) {
						majortail = mem[majortail].getrh();
						rcount = rcount + 1;
					}
					i = hyphenpassed;
					hyf[i] = 0;
					minortail = 0;
					mem[r + 1].setlh(0);
					hyfnode = newcharacter(hf, hyfchar);
					if (hyfnode != 0) {
						i = i + 1;
						c = hu[i];
						hu[i] = hyfchar;
						{
							mem[hyfnode].setrh(avail);
							avail = hyfnode;
						}
					}
					while (l <= i) {
						l = reconstitute(l, i, fontbchar[hf], 256) + 1;
						if (mem[memtop - 4].getrh() > 0) {
							if (minortail == 0) {
								mem[r + 1].setlh(mem[memtop - 4].getrh());
							} else {
								mem[minortail].setrh(mem[memtop - 4].getrh());
							}
							minortail = mem[memtop - 4].getrh();
							while (mem[minortail].getrh() > 0) {
								minortail = mem[minortail].getrh();
							}
						}
					}
					if (hyfnode != 0) {
						hu[i] = c;
						l = i;
						i = i - 1;
					}
					minortail = 0;
					mem[r + 1].setrh(0);
					cloc = 0;
					if (bcharlabel[hf] != 0) {
						l = l - 1;
						c = hu[l];
						cloc = l;
						hu[l] = 256;
					}
					while (l < j) {
						do {
							l = reconstitute(l, hn, bchar, 256) + 1;
							if (cloc > 0) {
								hu[cloc] = c;
								cloc = 0;
							}
							if (mem[memtop - 4].getrh() > 0) {
								if (minortail == 0) {
									mem[r + 1].setrh(mem[memtop - 4].getrh());
								} else {
									mem[minortail].setrh(mem[memtop - 4].getrh());
								}
								minortail = mem[memtop - 4].getrh();
								while (mem[minortail].getrh() > 0) {
									minortail = mem[minortail].getrh();
								}
							}
						} while (!(l >= j));
						while (l > j) {
							j = reconstitute(j, hn, bchar, 256) + 1;
							mem[majortail].setrh(mem[memtop - 4].getrh());
							while (mem[majortail].getrh() > 0) {
								majortail = mem[majortail].getrh();
								rcount = rcount + 1;
							}
						}
					}
					if (rcount > 127) {
						mem[s].setrh(mem[r].getrh());
						mem[r].setrh(0);
						flushnodelist(r);
					} else {
						mem[s].setrh(r);
						mem[r].setb1(rcount);
					}
					s = majortail;
					hyphenpassed = j - 1;
					mem[memtop - 4].setrh(0);
				} while (!(!((hyf[j - 1]) % 2 == 1)));
			}
		} while (!(j > hn));
		mem[s].setrh(q);
		flushlist(initlist);
	}

	int newtrieop(final int d, final int n, final int v) {
		/* 10 */int Result;
		int h;
		int u;
		int l;
		h = Math.abs(n + 313 * d + 361 * v + 1009 * curlang) % (1502) - 751;
		while (true) {
			l = trieophash[h + 751];
			if (l == 0) {
				if (trieopptr == 751) {
					errorLogic.overflow(949, 751);
				}
				u = trieused[curlang];
				if (u == 255) {
					errorLogic.overflow(950, 255);
				}
				trieopptr = trieopptr + 1;
				u = u + 1;
				trieused[curlang] = u;
				hyfdistance[trieopptr] = d;
				hyfnum[trieopptr] = n;
				hyfnext[trieopptr] = v;
				trieoplang[trieopptr] = curlang;
				trieophash[h + 751] = trieopptr;
				trieopval[trieopptr] = u;
				Result = u;
				return Result /* lab10 */;
			}
			if ((hyfdistance[l] == d) && (hyfnum[l] == n) && (hyfnext[l] == v) && (trieoplang[l] == curlang)) {
				Result = trieopval[l];
				return Result /* lab10 */;
			}
			if (h > -751) {
				h = h - 1;
			} else {
				h = 751;
			}
		}
	}

	int trienode(final int p) {
		/* 10 */int Result;
		int h;
		int q;
		h = Math.abs(triec[p] + 1009 * trieo[p] + 2718 * triel[p] + 3142 * trier[p]) % triesize;
		while (true) {
			q = triehash[h];
			if (q == 0) {
				triehash[h] = p;
				Result = p;
				return Result /* lab10 */;
			}
			if ((triec[q] == triec[p]) && (trieo[q] == trieo[p]) && (triel[q] == triel[p]) && (trier[q] == trier[p])) {
				Result = q;
				return Result /* lab10 */;
			}
			if (h > 0) {
				h = h - 1;
			} else {
				h = triesize;
			}
		}
	}

	int compresstrie(final int p) {
		int Result;
		if (p == 0) {
			Result = 0;
		} else {
			triel[p] = compresstrie(triel[p]);
			trier[p] = compresstrie(trier[p]);
			Result = trienode(p);
		}
		return Result;
	}

	void firstfit(final int p) {
		/* 45 40 */int h;
		int z;
		int q;
		int c;
		int l, r;
		int ll;
		c = triec[p];
		z = triemin[c];
		lab40: while (true) {
			h = z - c;
			if (triemax < h + 256) {
				if (triesize <= h + 256) {
					errorLogic.overflow(951, triesize);
				}
				do {
					triemax = triemax + 1;
					trietaken[triemax] = false;
					trie[triemax].rh = triemax + 1;
					trie[triemax].lh = triemax - 1;
				} while (!(triemax == h + 256));
			}
			lab45: while (true) {
				if (trietaken[h]) {
					break lab45;
				}
				q = trier[p];
				while (q > 0) {
					if (trie[h + triec[q]].rh == 0) {
						break lab45;
					}
					q = trier[q];
				}
				break lab40;
			}
			/* lab45: */z = trie[z].rh;
		}
		/* lab40: */trietaken[h] = true;
		triehash[p] = h;
		q = p;
		do {
			z = h + triec[q];
			l = trie[z].lh;
			r = trie[z].rh;
			trie[r].lh = l;
			trie[l].rh = r;
			trie[z].rh = 0;
			if (l < 256) {
				if (z < 256) {
					ll = z;
				} else {
					ll = 256;
				}
				do {
					triemin[l] = r;
					l = l + 1;
				} while (!(l == ll));
			}
			q = trier[q];
		} while (!(q == 0));
	}

	void triepack(int p) {
		int q;
		do {
			q = triel[p];
			if ((q > 0) && (triehash[q] == 0)) {
				firstfit(q);
				triepack(q);
			}
			p = trier[p];
		} while (!(p == 0));
	}

	void triefix(int p) {
		int q;
		int c;
		int z;
		z = triehash[p];
		do {
			q = triel[p];
			c = triec[p];
			trie[z + c].rh = triehash[q];
			trie[z + c].lh = trie[z + c].lh - (trie[z + c].lh % 256) + c;
			trie[z + c].lh = (trie[z + c].lh % 256) + (trieo[p] * 256);
			if (q > 0) {
				triefix(q);
			}
			p = trier[p];
		} while (!(p == 0));
	}

	void newpatterns() {
		/* 30 31 */int k, l;
		boolean digitsensed;
		int v;
		int p, q;
		boolean firstchild;
		int c;
		if (trienotready) {
			if (eqtb[9613].getInt() <= 0) {
				curlang = 0;
			} else if (eqtb[9613].getInt() > 255) {
				curlang = 0;
			} else {
				curlang = eqtb[9613].getInt();
			}
			scanleftbrace();
			k = 0;
			hyf[0] = 0;
			digitsensed = false;
			lab30: while (true) {
				getxtoken();
				switch (curcmd) {
					case 11:
					case 12:
						if (digitsensed || (curchr < 48) || (curchr > 57)) {
							if (curchr == 46) {
								curchr = 0;
							} else {
								curchr = eqtb[8539 + curchr].getrh();
								if (curchr == 0) {
									{
										printnl(262);
										print(957);
									}
									{
										helpptr = 1;
										helpline[0] = 956;
									}
									errorLogic.error();
								}
							}
							if (k < 63) {
								k = k + 1;
								hc[k] = curchr;
								hyf[k] = 0;
								digitsensed = false;
							}
						} else if (k < 63) {
							hyf[k] = curchr - 48;
							digitsensed = true;
						}
						break;
					case 10:
					case 2: {
						if (k > 0) {
							if (hc[1] == 0) {
								hyf[0] = 0;
							}
							if (hc[k] == 0) {
								hyf[k] = 0;
							}
							l = k;
							v = 0;
							while (true) {
								if (hyf[l] != 0) {
									v = newtrieop(k - l, hyf[l], v);
								}
								if (l > 0) {
									l = l - 1;
								} else {
									break /* lab31 */;
								}
							}
							/* lab31: */q = 0;
							hc[0] = curlang;
							while (l <= k) {
								c = hc[l];
								l = l + 1;
								p = triel[q];
								firstchild = true;
								while ((p > 0) && (c > triec[p])) {
									q = p;
									p = trier[q];
									firstchild = false;
								}
								if ((p == 0) || (c < triec[p])) {
									if (trieptr == triesize) {
										errorLogic.overflow(951, triesize);
									}
									trieptr = trieptr + 1;
									trier[trieptr] = p;
									p = trieptr;
									triel[p] = 0;
									if (firstchild) {
										triel[q] = p;
									} else {
										trier[q] = p;
									}
									triec[p] = c;
									trieo[p] = 0;
								}
								q = p;
							}
							if (trieo[q] != 0) {
								{
									printnl(262);
									print(958);
								}
								{
									helpptr = 1;
									helpline[0] = 956;
								}
								errorLogic.error();
							}
							trieo[q] = v;
						}
						if (curcmd == 2) {
							break lab30;
						}
						k = 0;
						hyf[0] = 0;
						digitsensed = false;
					}
						break;
					default: {
						{
							printnl(262);
							print(955);
						}
						printEscapeSequence(953);
						{
							helpptr = 1;
							helpline[0] = 956;
						}
						errorLogic.error();
					}
						break;
				}
			}
			/* lab30: */} else {
			{
				printnl(262);
				print(952);
			}
			printEscapeSequence(953);
			{
				helpptr = 1;
				helpline[0] = 954;
			}
			errorLogic.error();
			mem[memtop - 12].setrh(scantoks(false, false));
			flushlist(defref);
		}
	}

	void inittrie() {
		int p;
		int j, k, t;
		int r, s;
		twohalves h = new twohalves();
		opstart[0] = -0;
		for (j = 1; j <= 255; j++) {
			opstart[j] = opstart[j - 1] + trieused[j - 1];
		}
		for (j = 1; j <= trieopptr; j++) {
			trieophash[j + 751] = opstart[trieoplang[j]] + trieopval[j];
		}
		for (j = 1; j <= trieopptr; j++) {
			while (trieophash[j + 751] > j) {
				k = trieophash[j + 751];
				t = hyfdistance[k];
				hyfdistance[k] = hyfdistance[j];
				hyfdistance[j] = t;
				t = hyfnum[k];
				hyfnum[k] = hyfnum[j];
				hyfnum[j] = t;
				t = hyfnext[k];
				hyfnext[k] = hyfnext[j];
				hyfnext[j] = t;
				trieophash[j + 751] = trieophash[k + 751];
				trieophash[k + 751] = k;
			}
		}
		for (p = 0; p <= triesize; p++) {
			triehash[p] = 0;
		}
		triel[0] = compresstrie(triel[0]);
		for (p = 0; p <= trieptr; p++) {
			triehash[p] = 0;
		}
		for (p = 0; p <= 255; p++) {
			triemin[p] = p + 1;
		}
		trie[0].rh = 1;
		triemax = 0;
		if (triel[0] != 0) {
			firstfit(triel[0]);
			triepack(triel[0]);
		}
		h = new twohalves();
		h.rh = 0;
		h.lh = 0 + (0 * 256);
		if (triel[0] == 0) {
			for (r = 0; r <= 256; r++) {
				trie[r].copy(h);
			}
			triemax = 256;
		} else {
			triefix(triel[0]);
			r = 0;
			do {
				s = trie[r].rh;
				trie[r].copy(h);
				r = s;
			} while (!(r > triemax));
		}
		trie[0].lh = trie[0].lh - (trie[0].lh % 256) + 63;
		trienotready = false;
	}

	void linebreak(final int finalwidowpenalty) {
		/* 30 31 32 33 34 35 22 */boolean autobreaking;
		int prevp;
		int q, r, s, prevs;
		int f;
		int j;
		int c;
		packbeginline = curlist.mlfield;
		mem[memtop - 3].setrh(mem[curlist.headfield].getrh());
		if ((curlist.tailfield >= himemmin)) {
			mem[curlist.tailfield].setrh(newpenalty(10000));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		} else if (mem[curlist.tailfield].getb0() != 10) {
			mem[curlist.tailfield].setrh(newpenalty(10000));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		} else {
			mem[curlist.tailfield].setb0(12);
			deleteglueref(mem[curlist.tailfield + 1].getlh());
			flushnodelist(mem[curlist.tailfield + 1].getrh());
			mem[curlist.tailfield + 1].setInt(10000);
		}
		mem[curlist.tailfield].setrh(newparamglue(14));
		initcurlang = curlist.pgfield % 65536;
		initlhyf = curlist.pgfield / 4194304;
		initrhyf = (curlist.pgfield / 65536) % 64;
		popnest();
		noshrinkerroryet = true;
		if ((mem[eqtb[7189].getrh()].getb1() != 0) && (mem[eqtb[7189].getrh() + 3].getInt() != 0)) {
			eqtb[7189].setrh(finiteshrink(eqtb[7189].getrh()));
		}
		if ((mem[eqtb[7190].getrh()].getb1() != 0) && (mem[eqtb[7190].getrh() + 3].getInt() != 0)) {
			eqtb[7190].setrh(finiteshrink(eqtb[7190].getrh()));
		}
		q = eqtb[7189].getrh();
		r = eqtb[7190].getrh();
		background[1] = mem[q + 1].getInt() + mem[r + 1].getInt();
		background[2] = 0;
		background[3] = 0;
		background[4] = 0;
		background[5] = 0;
		background[2 + mem[q].getb0()] = mem[q + 2].getInt();
		background[2 + mem[r].getb0()] = background[2 + mem[r].getb0()] + mem[r + 2].getInt();
		background[6] = mem[q + 3].getInt() + mem[r + 3].getInt();
		minimumdemerits = 1073741823;
		minimaldemerits[3] = 1073741823;
		minimaldemerits[2] = 1073741823;
		minimaldemerits[1] = 1073741823;
		minimaldemerits[0] = 1073741823;
		if (eqtb[7712].getrh() == 0) {
			if (eqtb[10147].getInt() == 0) {
				lastspecialline = 0;
				secondwidth = eqtb[10133].getInt();
				secondindent = 0;
			} else {
				lastspecialline = Math.abs(eqtb[9604].getInt());
				if (eqtb[9604].getInt() < 0) {
					firstwidth = eqtb[10133].getInt() - Math.abs(eqtb[10147].getInt());
					if (eqtb[10147].getInt() >= 0) {
						firstindent = eqtb[10147].getInt();
					} else {
						firstindent = 0;
					}
					secondwidth = eqtb[10133].getInt();
					secondindent = 0;
				} else {
					firstwidth = eqtb[10133].getInt();
					firstindent = 0;
					secondwidth = eqtb[10133].getInt() - Math.abs(eqtb[10147].getInt());
					if (eqtb[10147].getInt() >= 0) {
						secondindent = eqtb[10147].getInt();
					} else {
						secondindent = 0;
					}
				}
			}
		} else {
			lastspecialline = mem[eqtb[7712].getrh()].getlh() - 1;
			secondwidth = mem[eqtb[7712].getrh() + 2 * (lastspecialline + 1)].getInt();
			secondindent = mem[eqtb[7712].getrh() + 2 * lastspecialline + 1].getInt();
		}
		if (eqtb[9582].getInt() == 0) {
			easyline = lastspecialline;
		} else {
			easyline = maxhalfword;
		}
		threshold = eqtb[9563].getInt();
		if (threshold >= 0) {
			secondpass = false;
			finalpass = false;
		} else {
			threshold = eqtb[9564].getInt();
			secondpass = true;
			finalpass = (eqtb[10150].getInt() <= 0);
		}
		lab30: while (true) {
			if (threshold > 10000) {
				threshold = 10000;
			}
			if (secondpass) {
				if (initex && trienotready) {
					inittrie();
				}
				curlang = initcurlang;
				lhyf = initlhyf;
				rhyf = initrhyf;
			}
			q = getnode(3);
			mem[q].setb0(0);
			mem[q].setb1(2);
			mem[q].setrh(memtop - 7);
			mem[q + 1].setrh(0);
			mem[q + 1].setlh(curlist.pgfield + 1);
			mem[q + 2].setInt(0);
			mem[memtop - 7].setrh(q);
			activewidth[1] = background[1];
			activewidth[2] = background[2];
			activewidth[3] = background[3];
			activewidth[4] = background[4];
			activewidth[5] = background[5];
			activewidth[6] = background[6];
			passive = 0;
			fontinshortdisplay = 0;
			curp = mem[memtop - 3].getrh();
			autobreaking = true;
			prevp = curp;
			while ((curp != 0) && (mem[memtop - 7].getrh() != memtop - 7)) {
				if ((curp >= himemmin)) {
					prevp = curp;
					do {
						f = mem[curp].getb0();
						activewidth[1] = activewidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[curp].getb1()].getb0()].getInt();
						curp = mem[curp].getrh();
					} while (!(!(curp >= himemmin)));
				}
				lab35: while (true) {
					switch (mem[curp].getb0()) {
						case 0:
						case 1:
						case 2:
							activewidth[1] = activewidth[1] + mem[curp + 1].getInt();
							break;
						case 8:
							if (mem[curp].getb1() == 4) {
								curlang = mem[curp + 1].getrh();
								lhyf = mem[curp + 1].getb0();
								rhyf = mem[curp + 1].getb1();
							}
							break;
						case 10: {
							if (autobreaking) {
								if ((prevp >= himemmin)) {
									trybreak(0, 0);
								} else if ((mem[prevp].getb0() < 9)) {
									trybreak(0, 0);
								} else if ((mem[prevp].getb0() == 11) && (mem[prevp].getb1() != 1)) {
									trybreak(0, 0);
								}
							}
							if ((mem[mem[curp + 1].getlh()].getb1() != 0) && (mem[mem[curp + 1].getlh() + 3].getInt() != 0)) {
								mem[curp + 1].setlh(finiteshrink(mem[curp + 1].getlh()));
							}
							q = mem[curp + 1].getlh();
							activewidth[1] = activewidth[1] + mem[q + 1].getInt();
							activewidth[2 + mem[q].getb0()] = activewidth[2 + mem[q].getb0()] + mem[q + 2].getInt();
							activewidth[6] = activewidth[6] + mem[q + 3].getInt();
							if (secondpass && autobreaking) {
								prevs = curp;
								s = mem[prevs].getrh();
								if (s != 0) {
									lab31: while (true) {
										lab22: while (true) {
											if ((s >= himemmin)) {
												c = mem[s].getb1();
												hf = mem[s].getb0();
											} else if (mem[s].getb0() == 6) {
												if (mem[s + 1].getrh() == 0) {
													prevs = s;
													s = mem[prevs].getrh();
													continue lab22;
												} else {
													q = mem[s + 1].getrh();
													c = mem[q].getb1();
													hf = mem[q].getb0();
												}
											} else if ((mem[s].getb0() == 11) && (mem[s].getb1() == 0)) {
												prevs = s;
												s = mem[prevs].getrh();
												continue lab22;
											} else if (mem[s].getb0() == 8) {
												if (mem[s].getb1() == 4) {
													curlang = mem[s + 1].getrh();
													lhyf = mem[s + 1].getb0();
													rhyf = mem[s + 1].getb1();
												}
												prevs = s;
												s = mem[prevs].getrh();
												continue lab22;
											} else {
												break lab31;
											}
											if (eqtb[8539 + c].getrh() != 0) {
												if ((eqtb[8539 + c].getrh() == c) || (eqtb[9601].getInt() > 0)) {
													break /* lab32 */;
												} else {
													break lab31;
												}
											}
											/* lab22: */prevs = s;
											s = mem[prevs].getrh();
										}
										/* lab32: */hyfchar = hyphenchar[hf];
										if (hyfchar < 0) {
											break lab31;
										}
										if (hyfchar > 255) {
											break lab31;
										}
										ha = prevs;
										if (lhyf + rhyf > 63) {
											break lab31;
										}
										hn = 0;
										lab33: while (true) {
											if ((s >= himemmin)) {
												if (mem[s].getb0() != hf) {
													break lab33;
												}
												hyfbchar = mem[s].getb1();
												c = hyfbchar;
												if (eqtb[8539 + c].getrh() == 0) {
													break lab33;
												}
												if (hn == 63) {
													break lab33;
												}
												hb = s;
												hn = hn + 1;
												hu[hn] = c;
												hc[hn] = eqtb[8539 + c].getrh();
												hyfbchar = 256;
											} else if (mem[s].getb0() == 6) {
												if (mem[s + 1].getb0() != hf) {
													break lab33;
												}
												j = hn;
												q = mem[s + 1].getrh();
												if (q > 0) {
													hyfbchar = mem[q].getb1();
												}
												while (q > 0) {
													c = mem[q].getb1();
													if (eqtb[8539 + c].getrh() == 0) {
														break lab33;
													}
													if (j == 63) {
														break lab33;
													}
													j = j + 1;
													hu[j] = c;
													hc[j] = eqtb[8539 + c].getrh();
													q = mem[q].getrh();
												}
												hb = s;
												hn = j;
												if (((mem[s].getb1()) % 2 == 1)) {
													hyfbchar = fontbchar[hf];
												} else {
													hyfbchar = 256;
												}
											} else if ((mem[s].getb0() == 11) && (mem[s].getb1() == 0)) {
												hb = s;
												hyfbchar = fontbchar[hf];
											} else {
												break lab33;
											}
											s = mem[s].getrh();
										}
										/* lab33: */if (hn < lhyf + rhyf) {
											break lab31;
										}
										lab34: while (true) {
											if (!((s >= himemmin))) {
												switch (mem[s].getb0()) {
													case 6:
														;
														break;
													case 11:
														if (mem[s].getb1() != 0) {
															break lab34;
														}
														break;
													case 8:
													case 10:
													case 12:
													case 3:
													case 5:
													case 4:
														break lab34;
													default:
														break lab31;
												}
											}
											s = mem[s].getrh();
										}
										/* lab34: */hyphenate();
										break;
									}
									/* lab31: */
								}
							}
						}
							break;
						case 11:
							if (mem[curp].getb1() == 1) {
								if (!(mem[curp].getrh() >= himemmin) && autobreaking) {
									if (mem[mem[curp].getrh()].getb0() == 10) {
										trybreak(0, 0);
									}
								}
								activewidth[1] = activewidth[1] + mem[curp + 1].getInt();
							} else {
								activewidth[1] = activewidth[1] + mem[curp + 1].getInt();
							}
							break;
						case 6: {
							f = mem[curp + 1].getb0();
							activewidth[1] = activewidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[curp + 1].getb1()].getb0()].getInt();
						}
							break;
						case 7: {
							s = mem[curp + 1].getlh();
							discwidth = 0;
							if (s == 0) {
								trybreak(eqtb[9567].getInt(), 1);
							} else {
								do {
									if ((s >= himemmin)) {
										f = mem[s].getb0();
										discwidth = discwidth + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s].getb1()].getb0()].getInt();
									} else {
										switch (mem[s].getb0()) {
											case 6: {
												f = mem[s + 1].getb0();
												discwidth = discwidth + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s + 1].getb1()].getb0()].getInt();
											}
												break;
											case 0:
											case 1:
											case 2:
											case 11:
												discwidth = discwidth + mem[s + 1].getInt();
												break;
											default:
												errorLogic.confusion(937);
												break;
										}
									}
									s = mem[s].getrh();
								} while (!(s == 0));
								activewidth[1] = activewidth[1] + discwidth;
								trybreak(eqtb[9566].getInt(), 1);
								activewidth[1] = activewidth[1] - discwidth;
							}
							r = mem[curp].getb1();
							s = mem[curp].getrh();
							while (r > 0) {
								if ((s >= himemmin)) {
									f = mem[s].getb0();
									activewidth[1] = activewidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s].getb1()].getb0()].getInt();
								} else {
									switch (mem[s].getb0()) {
										case 6: {
											f = mem[s + 1].getb0();
											activewidth[1] = activewidth[1] + fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[s + 1].getb1()].getb0()].getInt();
										}
											break;
										case 0:
										case 1:
										case 2:
										case 11:
											activewidth[1] = activewidth[1] + mem[s + 1].getInt();
											break;
										default:
											errorLogic.confusion(938);
											break;
									}
								}
								r = r - 1;
								s = mem[s].getrh();
							}
							prevp = curp;
							curp = s;
							break lab35;
						}
						case 9: {
							autobreaking = (mem[curp].getb1() == 1);
							{
								if (!(mem[curp].getrh() >= himemmin) && autobreaking) {
									if (mem[mem[curp].getrh()].getb0() == 10) {
										trybreak(0, 0);
									}
								}
								activewidth[1] = activewidth[1] + mem[curp + 1].getInt();
							}
						}
							break;
						case 12:
							trybreak(mem[curp + 1].getInt(), 0);
							break;
						case 4:
						case 3:
						case 5:
							;
							break;
						default:
							errorLogic.confusion(936);
							break;
					}
					prevp = curp;
					curp = mem[curp].getrh();
					break;
				}
				/* lab35: */}
			if (curp == 0) {
				trybreak(-10000, 1);
				if (mem[memtop - 7].getrh() != memtop - 7) {
					r = mem[memtop - 7].getrh();
					fewestdemerits = 1073741823;
					do {
						if (mem[r].getb0() != 2) {
							if (mem[r + 2].getInt() < fewestdemerits) {
								fewestdemerits = mem[r + 2].getInt();
								bestbet = r;
							}
						}
						r = mem[r].getrh();
					} while (!(r == memtop - 7));
					bestline = mem[bestbet + 1].getlh();
					if (eqtb[9582].getInt() == 0) {
						break lab30;
					}
					{
						r = mem[memtop - 7].getrh();
						actuallooseness = 0;
						do {
							if (mem[r].getb0() != 2) {
								linediff = mem[r + 1].getlh() - bestline;
								if (((linediff < actuallooseness) && (eqtb[9582].getInt() <= linediff)) || ((linediff > actuallooseness) && (eqtb[9582].getInt() >= linediff))) {
									bestbet = r;
									actuallooseness = linediff;
									fewestdemerits = mem[r + 2].getInt();
								} else if ((linediff == actuallooseness) && (mem[r + 2].getInt() < fewestdemerits)) {
									bestbet = r;
									fewestdemerits = mem[r + 2].getInt();
								}
							}
							r = mem[r].getrh();
						} while (!(r == memtop - 7));
						bestline = mem[bestbet + 1].getlh();
					}
					if ((actuallooseness == eqtb[9582].getInt()) || finalpass) {
						break lab30;
					}
				}
			}
			q = mem[memtop - 7].getrh();
			while (q != memtop - 7) {
				curp = mem[q].getrh();
				if (mem[q].getb0() == 2) {
					freenode(q, 7);
				} else {
					freenode(q, 3);
				}
				q = curp;
			}
			q = passive;
			while (q != 0) {
				curp = mem[q].getrh();
				freenode(q, 2);
				q = curp;
			}
			if (!secondpass) {
				threshold = eqtb[9564].getInt();
				secondpass = true;
				finalpass = (eqtb[10150].getInt() <= 0);
			} else {
				background[2] = background[2] + eqtb[10150].getInt();
				finalpass = true;
			}
		}
		/* lab30: */postlinebreak(finalwidowpenalty);
		q = mem[memtop - 7].getrh();
		while (q != memtop - 7) {
			curp = mem[q].getrh();
			if (mem[q].getb0() == 2) {
				freenode(q, 7);
			} else {
				freenode(q, 3);
			}
			q = curp;
		}
		q = passive;
		while (q != 0) {
			curp = mem[q].getrh();
			freenode(q, 2);
			q = curp;
		}
		packbeginline = 0;
	}

	void newhyphexceptions() {
		/* 21 10 40 45 */int n;
		int j;
		int h;
		int k;
		int p;
		int q;
		int s, t;
		scanleftbrace();
		if (eqtb[9613].getInt() <= 0) {
			curlang = 0;
		} else if (eqtb[9613].getInt() > 255) {
			curlang = 0;
		} else {
			curlang = eqtb[9613].getInt();
		}
		n = 0;
		p = 0;
		while (true) {
			getxtoken();
			lab21: while (true) {
				switch (curcmd) {
					case 11:
					case 12:
					case 68:
						if (curchr == 45) {
							if (n < 63) {
								q = allocateMemoryWord();
								mem[q].setrh(p);
								mem[q].setlh(n);
								p = q;
							}
						} else {
							if (eqtb[8539 + curchr].getrh() == 0) {
								{
									printnl(262);
									print(945);
								}
								{
									helpptr = 2;
									helpline[1] = 946;
									helpline[0] = 947;
								}
								errorLogic.error();
							} else if (n < 63) {
								n = n + 1;
								hc[n] = eqtb[8539 + curchr].getrh();
							}
						}
						break;
					case 16: {
						scancharnum();
						curchr = curval;
						curcmd = 68;
						continue lab21;
					}
					case 10:
					case 2: {
						if (n > 1) {
							n = n + 1;
							hc[n] = curlang;
							h = 0;
							for (j = 1; j <= n; j++) {
								h = (h + h + hc[j]) % 607;
								stringPool.append((char)hc[j]);
							}
							s = stringPool.makeString();
							if (hyphcount == 607) {
								errorLogic.overflow(948, 607);
							}
							hyphcount = hyphcount + 1;
							while (hyphword[h] != 0) {
								k = hyphword[h];
								lab45: while (true) {
									lab40: while (true) {
										if ((strstart[k + 1] - strstart[k]) < (strstart[s + 1] - strstart[s])) {
											break lab40;
										}
										if ((strstart[k + 1] - strstart[k]) > (strstart[s + 1] - strstart[s])) {
											break lab45;
										}
										final int order = stringPool.getString(k).compareTo(stringPool.getString(s));
										if (order < 0) {
											break lab40;
										} else if (order > 0) {
											break lab45;
										} else {
											break;
										}
									}
									/* lab40: */q = hyphlist[h];
									hyphlist[h] = p;
									p = q;
									t = hyphword[h];
									hyphword[h] = s;
									s = t;
									break;
								}
								/* lab45: */if (h > 0) {
									h = h - 1;
								} else {
									h = 607;
								}
							}
							hyphword[h] = s;
							hyphlist[h] = p;
						}
						if (curcmd == 2) {
							return /* lab10 */;
						}
						n = 0;
						p = 0;
					}
						break;
					default: {
						{
							printnl(262);
							print(680);
						}
						printEscapeSequence(941);
						print(942);
						{
							helpptr = 2;
							helpline[1] = 943;
							helpline[0] = 944;
						}
						errorLogic.error();
					}
						break;
				}
				break;
			}
		}
	}

	int prunepagetop(int p) {
		int Result;
		int prevp;
		int q;
		prevp = memtop - 3;
		mem[memtop - 3].setrh(p);
		while (p != 0) {
			switch (mem[p].getb0()) {
				case 0:
				case 1:
				case 2: {
					q = newskipparam(10);
					mem[prevp].setrh(q);
					mem[q].setrh(p);
					if (mem[tempptr + 1].getInt() > mem[p + 3].getInt()) {
						mem[tempptr + 1].setInt(mem[tempptr + 1].getInt() - mem[p + 3].getInt());
					} else {
						mem[tempptr + 1].setInt(0);
					}
					p = 0;
				}
					break;
				case 8:
				case 4:
				case 3: {
					prevp = p;
					p = mem[prevp].getrh();
				}
					break;
				case 10:
				case 11:
				case 12: {
					q = p;
					p = mem[q].getrh();
					mem[q].setrh(0);
					mem[prevp].setrh(p);
					flushnodelist(q);
				}
					break;
				default:
					errorLogic.confusion(959);
					break;
			}
		}
		Result = mem[memtop - 3].getrh();
		return Result;
	}

	int vertbreak(int p, final int h, final int d) {
		/* 30 45 90 */int Result;
		int prevp;
		int q, r;
		int pi;
		int b;
		int leastcost;
		int bestplace;
		int prevdp;
		int t;
		prevp = p;
		leastcost = 1073741823;
		activewidth[1] = 0;
		activewidth[2] = 0;
		activewidth[3] = 0;
		activewidth[4] = 0;
		activewidth[5] = 0;
		activewidth[6] = 0;
		prevdp = 0;
		bestplace = 0;
		pi = 0;
		lab30: while (true) {
			lab45: while (true) {
				lab90: while (true) {
					if (p == 0) {
						pi = -10000;
					} else {
						switch (mem[p].getb0()) {
							case 0:
							case 1:
							case 2: {
								activewidth[1] = activewidth[1] + prevdp + mem[p + 3].getInt();
								prevdp = mem[p + 2].getInt();
								break lab45;
							}
							case 8:
								break lab45;
							case 10:
								if ((mem[prevp].getb0() < 9)) {
									pi = 0;
								} else {
									break lab90;
								}
								break;
							case 11: {
								if (mem[p].getrh() == 0) {
									t = 12;
								} else {
									t = mem[mem[p].getrh()].getb0();
								}
								if (t == 10) {
									pi = 0;
								} else {
									break lab90;
								}
							}
								break;
							case 12:
								pi = mem[p + 1].getInt();
								break;
							case 4:
							case 3:
								break lab45;
							default:
								errorLogic.confusion(960);
								break;
						}
					}
					if (pi < 10000) {
						if (activewidth[1] < h) {
							if ((activewidth[3] != 0) || (activewidth[4] != 0) || (activewidth[5] != 0)) {
								b = 0;
							} else {
								b = badness(h - activewidth[1], activewidth[2]);
							}
						} else if (activewidth[1] - h > activewidth[6]) {
							b = 1073741823;
						} else {
							b = badness(activewidth[1] - h, activewidth[6]);
						}
						if (b < 1073741823) {
							if (pi <= -10000) {
								b = pi;
							} else if (b < 10000) {
								b = b + pi;
							} else {
								b = 100000;
							}
						}
						if (b <= leastcost) {
							bestplace = p;
							leastcost = b;
							bestheightplusdepth = activewidth[1] + prevdp;
						}
						if ((b == 1073741823) || (pi <= -10000)) {
							break lab30;
						}
					}
					if ((mem[p].getb0() < 10) || (mem[p].getb0() > 11)) {
						break lab45;
					}
					break;
				}
				/* lab90: */if (mem[p].getb0() == 11) {
					q = p;
				} else {
					q = mem[p + 1].getlh();
					activewidth[2 + mem[q].getb0()] = activewidth[2 + mem[q].getb0()] + mem[q + 2].getInt();
					activewidth[6] = activewidth[6] + mem[q + 3].getInt();
					if ((mem[q].getb1() != 0) && (mem[q + 3].getInt() != 0)) {
						{
							printnl(262);
							print(961);
						}
						{
							helpptr = 4;
							helpline[3] = 962;
							helpline[2] = 963;
							helpline[1] = 964;
							helpline[0] = 922;
						}
						errorLogic.error();
						r = newspec(q);
						mem[r].setb1(0);
						deleteglueref(q);
						mem[p + 1].setlh(r);
						q = r;
					}
				}
				activewidth[1] = activewidth[1] + prevdp + mem[q + 1].getInt();
				prevdp = 0;
				break;
			}
			/* lab45: */if (prevdp > d) {
				activewidth[1] = activewidth[1] + prevdp - d;
				prevdp = d;
			}
			prevp = p;
			p = mem[prevp].getrh();
		}
		/* lab30: */Result = bestplace;
		return Result;
	}

	int vsplit(final int n, final int h) {
		/* 10 30 */int Result;
		int v;
		int p;
		int q;
		v = eqtb[7978 + n].getrh();
		if (curmark[3] != 0) {
			deletetokenref(curmark[3]);
			curmark[3] = 0;
			deletetokenref(curmark[4]);
			curmark[4] = 0;
		}
		if (v == 0) {
			Result = 0;
			return Result /* lab10 */;
		}
		if (mem[v].getb0() != 1) {
			{
				printnl(262);
				print(338);
			}
			printEscapeSequence(965);
			print(966);
			printEscapeSequence(967);
			{
				helpptr = 2;
				helpline[1] = 968;
				helpline[0] = 969;
			}
			errorLogic.error();
			Result = 0;
			return Result /* lab10 */;
		}
		q = vertbreak(mem[v + 5].getrh(), h, eqtb[10136].getInt());
		p = mem[v + 5].getrh();
		if (p == q) {
			mem[v + 5].setrh(0);
		} else {
			while (true) {
				if (mem[p].getb0() == 4) {
					if (curmark[3] == 0) {
						curmark[3] = mem[p + 1].getInt();
						curmark[4] = curmark[3];
						mem[curmark[3]].setlh(mem[curmark[3]].getlh() + 2);
					} else {
						deletetokenref(curmark[4]);
						curmark[4] = mem[p + 1].getInt();
						mem[curmark[4]].setlh(mem[curmark[4]].getlh() + 1);
					}
				}
				if (mem[p].getrh() == q) {
					mem[p].setrh(0);
					break /* lab30 */;
				}
				p = mem[p].getrh();
			}
		}
		/* lab30: */q = prunepagetop(q);
		p = mem[v + 5].getrh();
		freenode(v, 7);
		if (q == 0) {
			eqtb[7978 + n].setrh(0);
		} else {
			eqtb[7978 + n].setrh(vpackage(q, 0, 1, 1073741823));
		}
		Result = vpackage(p, h, 0, eqtb[10136].getInt());
		return Result;
	}

	void freezepagespecs(final int s) {
		pagecontents = s;
		pagesofar[0] = eqtb[10134].getInt();
		pagemaxdepth = eqtb[10135].getInt();
		pagesofar[7] = 0;
		pagesofar[1] = 0;
		pagesofar[2] = 0;
		pagesofar[3] = 0;
		pagesofar[4] = 0;
		pagesofar[5] = 0;
		pagesofar[6] = 0;
		leastpagecost = 1073741823;
	}

	void ensurevbox(final int n) {
		int p;
		p = eqtb[7978 + n].getrh();
		if (p != 0) {
			if (mem[p].getb0() == 0) {
				printnl(262);
				print(989);
				helpptr = 3;
				helpline[2] = 990;
				helpline[1] = 991;
				helpline[0] = 992;
				boxerror(n);
			}
		}
	}

	void fireup(final int c) {
		/* 10 */int p, q, r, s;
		int prevp;
		int n;
		boolean wait;
		int savevbadness;
		int savevfuzz;
		int savesplittopskip;
		if (mem[bestpagebreak].getb0() == 12) {
			geqworddefine(9602, mem[bestpagebreak + 1].getInt());
			mem[bestpagebreak + 1].setInt(10000);
		} else {
			geqworddefine(9602, 10000);
		}
		if (curmark[2] != 0) {
			if (curmark[0] != 0) {
				deletetokenref(curmark[0]);
			}
			curmark[0] = curmark[2];
			mem[curmark[0]].setlh(mem[curmark[0]].getlh() + 1);
			deletetokenref(curmark[1]);
			curmark[1] = 0;
		}
		if (c == bestpagebreak) {
			bestpagebreak = 0;
		}
		if (eqtb[8233].getrh() != 0) {
			printnl(262);
			print(338);
			printEscapeSequence(409);
			print(1003);
			helpptr = 2;
			helpline[1] = 1004;
			helpline[0] = 992;
			boxerror(255);
		}
		insertpenalties = 0;
		savesplittopskip = eqtb[7192].getrh();
		if (eqtb[9616].getInt() <= 0) {
			r = mem[memtop].getrh();
			while (r != memtop) {
				if (mem[r + 2].getlh() != 0) {
					n = mem[r].getb1();
					ensurevbox(n);
					if (eqtb[7978 + n].getrh() == 0) {
						eqtb[7978 + n].setrh(newnullbox());
					}
					p = eqtb[7978 + n].getrh() + 5;
					while (mem[p].getrh() != 0) {
						p = mem[p].getrh();
					}
					mem[r + 2].setrh(p);
				}
				r = mem[r].getrh();
			}
		}
		q = memtop - 4;
		mem[q].setrh(0);
		prevp = memtop - 2;
		p = mem[prevp].getrh();
		while (p != bestpagebreak) {
			if (mem[p].getb0() == 3) {
				if (eqtb[9616].getInt() <= 0) {
					r = mem[memtop].getrh();
					while (mem[r].getb1() != mem[p].getb1()) {
						r = mem[r].getrh();
					}
					if (mem[r + 2].getlh() == 0) {
						wait = true;
					} else {
						wait = false;
						s = mem[r + 2].getrh();
						mem[s].setrh(mem[p + 4].getlh());
						if (mem[r + 2].getlh() == p) {
							if (mem[r].getb0() == 1) {
								if ((mem[r + 1].getlh() == p) && (mem[r + 1].getrh() != 0)) {
									while (mem[s].getrh() != mem[r + 1].getrh()) {
										s = mem[s].getrh();
									}
									mem[s].setrh(0);
									eqtb[7192].setrh(mem[p + 4].getrh());
									mem[p + 4].setlh(prunepagetop(mem[r + 1].getrh()));
									if (mem[p + 4].getlh() != 0) {
										tempptr = vpackage(mem[p + 4].getlh(), 0, 1, 1073741823);
										mem[p + 3].setInt(mem[tempptr + 3].getInt() + mem[tempptr + 2].getInt());
										freenode(tempptr, 7);
										wait = true;
									}
								}
							}
							mem[r + 2].setlh(0);
							n = mem[r].getb1();
							tempptr = mem[eqtb[7978 + n].getrh() + 5].getrh();
							freenode(eqtb[7978 + n].getrh(), 7);
							eqtb[7978 + n].setrh(vpackage(tempptr, 0, 1, 1073741823));
						} else {
							while (mem[s].getrh() != 0) {
								s = mem[s].getrh();
							}
							mem[r + 2].setrh(s);
						}
					}
					mem[prevp].setrh(mem[p].getrh());
					mem[p].setrh(0);
					if (wait) {
						mem[q].setrh(p);
						q = p;
						insertpenalties = insertpenalties + 1;
					} else {
						deleteglueref(mem[p + 4].getrh());
						freenode(p, 5);
					}
					p = prevp;
				}
			} else if (mem[p].getb0() == 4) {
				if (curmark[1] == 0) {
					curmark[1] = mem[p + 1].getInt();
					mem[curmark[1]].setlh(mem[curmark[1]].getlh() + 1);
				}
				if (curmark[2] != 0) {
					deletetokenref(curmark[2]);
				}
				curmark[2] = mem[p + 1].getInt();
				mem[curmark[2]].setlh(mem[curmark[2]].getlh() + 1);
			}
			prevp = p;
			p = mem[prevp].getrh();
		}
		eqtb[7192].setrh(savesplittopskip);
		if (p != 0) {
			if (mem[memtop - 1].getrh() == 0) {
				if (nestptr == 0) {
					curlist.tailfield = pagetail;
				} else {
					nest[0].tailfield = pagetail;
				}
			}
			mem[pagetail].setrh(mem[memtop - 1].getrh());
			mem[memtop - 1].setrh(p);
			mem[prevp].setrh(0);
		}
		savevbadness = eqtb[9590].getInt();
		eqtb[9590].setInt(10000);
		savevfuzz = eqtb[10139].getInt();
		eqtb[10139].setInt(1073741823);
		eqtb[8233].setrh(vpackage(mem[memtop - 2].getrh(), bestsize, 0, pagemaxdepth));
		eqtb[9590].setInt(savevbadness);
		eqtb[10139].setInt(savevfuzz);
		if (lastglue != maxhalfword) {
			deleteglueref(lastglue);
		}
		pagecontents = 0;
		pagetail = memtop - 2;
		mem[memtop - 2].setrh(0);
		lastglue = maxhalfword;
		lastpenalty = 0;
		lastkern = 0;
		pagesofar[7] = 0;
		pagemaxdepth = 0;
		if (q != memtop - 4) {
			mem[memtop - 2].setrh(mem[memtop - 4].getrh());
			pagetail = q;
		}
		r = mem[memtop].getrh();
		while (r != memtop) {
			q = mem[r].getrh();
			freenode(r, 4);
			r = q;
		}
		mem[memtop].setrh(memtop);
		if ((curmark[0] != 0) && (curmark[1] == 0)) {
			curmark[1] = curmark[0];
			mem[curmark[0]].setlh(mem[curmark[0]].getlh() + 1);
		}
		if (eqtb[7713].getrh() != 0) {
			if (deadcycles >= eqtb[9603].getInt()) {
				printnl(262);
				print(1005);
				printInt(deadcycles);
				print(1006);
				helpptr = 3;
				helpline[2] = 1007;
				helpline[1] = 1008;
				helpline[0] = 1009;
				errorLogic.error();
			} else {
				outputactive = true;
				deadcycles = deadcycles + 1;
				pushnest();
				curlist.modefield = -1;
				curlist.auxfield.setInt(-65536000);
				curlist.mlfield = -line;
				begintokenlist(eqtb[7713].getrh(), 6);
				newsavelevel(8);
				normalparagraph();
				scanleftbrace();
				return /* lab10 */;
			}
		}
		if (mem[memtop - 2].getrh() != 0) {
			if (mem[memtop - 1].getrh() == 0) {
				if (nestptr == 0) {
					curlist.tailfield = pagetail;
				} else {
					nest[0].tailfield = pagetail;
				}
			} else {
				mem[pagetail].setrh(mem[memtop - 1].getrh());
			}
			mem[memtop - 1].setrh(mem[memtop - 2].getrh());
			mem[memtop - 2].setrh(0);
			pagetail = memtop - 2;
		}
		shipout(eqtb[8233].getrh());
		eqtb[8233].setrh(0);
	}

	void buildpage() {
		/* 10 30 31 22 80 90 */int p;
		int q, r;
		int b, c;
		int pi;
		int n;
		int delta, h, w;
		if ((mem[memtop - 1].getrh() == 0) || outputactive) {
			return /* lab10 */;
		}
		pi = 0;
		lab22: do {
			p = mem[memtop - 1].getrh();
			if (lastglue != maxhalfword) {
				deleteglueref(lastglue);
			}
			lastpenalty = 0;
			lastkern = 0;
			if (mem[p].getb0() == 10) {
				lastglue = mem[p + 1].getlh();
				mem[lastglue].setrh(mem[lastglue].getrh() + 1);
			} else {
				lastglue = maxhalfword;
				if (mem[p].getb0() == 12) {
					lastpenalty = mem[p + 1].getInt();
				} else if (mem[p].getb0() == 11) {
					lastkern = mem[p + 1].getInt();
				}
			}
			lab30: while (true) {
				lab31: while (true) {
					lab80: while (true) {
						lab90: while (true) {
							switch (mem[p].getb0()) {
								case 0:
								case 1:
								case 2:
									if (pagecontents < 2) {
										if (pagecontents == 0) {
											freezepagespecs(2);
										} else {
											pagecontents = 2;
										}
										q = newskipparam(9);
										if (mem[tempptr + 1].getInt() > mem[p + 3].getInt()) {
											mem[tempptr + 1].setInt(mem[tempptr + 1].getInt() - mem[p + 3].getInt());
										} else {
											mem[tempptr + 1].setInt(0);
										}
										mem[q].setrh(p);
										mem[memtop - 1].setrh(q);
										continue lab22;
									} else {
										pagesofar[1] = pagesofar[1] + pagesofar[7] + mem[p + 3].getInt();
										pagesofar[7] = mem[p + 2].getInt();
										break lab80;
									}
								case 8:
									break lab80;
								case 10:
									if (pagecontents < 2) {
										break lab31;
									} else if ((mem[pagetail].getb0() < 9)) {
										pi = 0;
									} else {
										break lab90;
									}
									break;
								case 11:
									if (pagecontents < 2) {
										break lab31;
									} else if (mem[p].getrh() == 0) {
										return /* lab10 */;
									} else if (mem[mem[p].getrh()].getb0() == 10) {
										pi = 0;
									} else {
										break lab90;
									}
								case 12:
									if (pagecontents < 2) {
										break lab31;
									} else {
										pi = mem[p + 1].getInt();
									}
									break;
								case 4:
									break lab80;
								case 3: {
									if (pagecontents == 0) {
										freezepagespecs(1);
									}
									n = mem[p].getb1();
									r = memtop;
									while (n >= mem[mem[r].getrh()].getb1()) {
										r = mem[r].getrh();
									}
									if (mem[r].getb1() != n) {
										q = getnode(4);
										mem[q].setrh(mem[r].getrh());
										mem[r].setrh(q);
										r = q;
										mem[r].setb1(n);
										mem[r].setb0(0);
										ensurevbox(n);
										if (eqtb[7978 + n].getrh() == 0) {
											mem[r + 3].setInt(0);
										} else {
											mem[r + 3].setInt(mem[eqtb[7978 + n].getrh() + 3].getInt() + mem[eqtb[7978 + n].getrh() + 2].getInt());
										}
										mem[r + 2].setlh(0);
										q = eqtb[7200 + n].getrh();
										if (eqtb[9618 + n].getInt() == 1000) {
											h = mem[r + 3].getInt();
										} else {
											h = xovern(mem[r + 3].getInt(), 1000) * eqtb[9618 + n].getInt();
										}
										pagesofar[0] = pagesofar[0] - h - mem[q + 1].getInt();
										pagesofar[2 + mem[q].getb0()] = pagesofar[2 + mem[q].getb0()] + mem[q + 2].getInt();
										pagesofar[6] = pagesofar[6] + mem[q + 3].getInt();
										if ((mem[q].getb1() != 0) && (mem[q + 3].getInt() != 0)) {
											{
												printnl(262);
												print(998);
											}
											printEscapeSequence(395);
											printInt(n);
											{
												helpptr = 3;
												helpline[2] = 999;
												helpline[1] = 1000;
												helpline[0] = 922;
											}
											errorLogic.error();
										}
									}
									if (mem[r].getb0() == 1) {
										insertpenalties = insertpenalties + mem[p + 1].getInt();
									} else {
										mem[r + 2].setrh(p);
										delta = pagesofar[0] - pagesofar[1] - pagesofar[7] + pagesofar[6];
										if (eqtb[9618 + n].getInt() == 1000) {
											h = mem[p + 3].getInt();
										} else {
											h = xovern(mem[p + 3].getInt(), 1000) * eqtb[9618 + n].getInt();
										}
										if (((h <= 0) || (h <= delta)) && (mem[p + 3].getInt() + mem[r + 3].getInt() <= eqtb[10151 + n].getInt())) {
											pagesofar[0] = pagesofar[0] - h;
											mem[r + 3].setInt(mem[r + 3].getInt() + mem[p + 3].getInt());
										} else {
											if (eqtb[9618 + n].getInt() <= 0) {
												w = 1073741823;
											} else {
												w = pagesofar[0] - pagesofar[1] - pagesofar[7];
												if (eqtb[9618 + n].getInt() != 1000) {
													w = xovern(w, eqtb[9618 + n].getInt()) * 1000;
												}
											}
											if (w > eqtb[10151 + n].getInt() - mem[r + 3].getInt()) {
												w = eqtb[10151 + n].getInt() - mem[r + 3].getInt();
											}
											q = vertbreak(mem[p + 4].getlh(), w, mem[p + 2].getInt());
											mem[r + 3].setInt(mem[r + 3].getInt() + bestheightplusdepth);
											if (eqtb[9618 + n].getInt() != 1000) {
												bestheightplusdepth = xovern(bestheightplusdepth, 1000) * eqtb[9618 + n].getInt();
											}
											pagesofar[0] = pagesofar[0] - bestheightplusdepth;
											mem[r].setb0(1);
											mem[r + 1].setrh(q);
											mem[r + 1].setlh(p);
											if (q == 0) {
												insertpenalties = insertpenalties - 10000;
											} else if (mem[q].getb0() == 12) {
												insertpenalties = insertpenalties + mem[q + 1].getInt();
											}
										}
									}
									break lab80;
								}
								default:
									errorLogic.confusion(993);
									break;
							}
							if (pi < 10000) {
								if (pagesofar[1] < pagesofar[0]) {
									if ((pagesofar[3] != 0) || (pagesofar[4] != 0) || (pagesofar[5] != 0)) {
										b = 0;
									} else {
										b = badness(pagesofar[0] - pagesofar[1], pagesofar[2]);
									}
								} else if (pagesofar[1] - pagesofar[0] > pagesofar[6]) {
									b = 1073741823;
								} else {
									b = badness(pagesofar[1] - pagesofar[0], pagesofar[6]);
								}
								if (b < 1073741823) {
									if (pi <= -10000) {
										c = pi;
									} else if (b < 10000) {
										c = b + pi + insertpenalties;
									} else {
										c = 100000;
									}
								} else {
									c = b;
								}
								if (insertpenalties >= 10000) {
									c = 1073741823;
								}
								if (c <= leastpagecost) {
									bestpagebreak = p;
									bestsize = pagesofar[0];
									leastpagecost = c;
									r = mem[memtop].getrh();
									while (r != memtop) {
										mem[r + 2].setlh(mem[r + 2].getrh());
										r = mem[r].getrh();
									}
								}
								if ((c == 1073741823) || (pi <= -10000)) {
									fireup(p);
									if (outputactive) {
										return /* lab10 */;
									}
									break lab30;
								}
							}
							if ((mem[p].getb0() < 10) || (mem[p].getb0() > 11)) {
								break lab80;
							}
							break;
						}
						/* lab90: */if (mem[p].getb0() == 11) {
							q = p;
						} else {
							q = mem[p + 1].getlh();
							pagesofar[2 + mem[q].getb0()] = pagesofar[2 + mem[q].getb0()] + mem[q + 2].getInt();
							pagesofar[6] = pagesofar[6] + mem[q + 3].getInt();
							if ((mem[q].getb1() != 0) && (mem[q + 3].getInt() != 0)) {
								{
									printnl(262);
									print(994);
								}
								{
									helpptr = 4;
									helpline[3] = 995;
									helpline[2] = 963;
									helpline[1] = 964;
									helpline[0] = 922;
								}
								errorLogic.error();
								r = newspec(q);
								mem[r].setb1(0);
								deleteglueref(q);
								mem[p + 1].setlh(r);
								q = r;
							}
						}
						pagesofar[1] = pagesofar[1] + pagesofar[7] + mem[q + 1].getInt();
						pagesofar[7] = 0;
						break;
					}
					/* lab80: */if (pagesofar[7] > pagemaxdepth) {
						pagesofar[1] = pagesofar[1] + pagesofar[7] - pagemaxdepth;
						pagesofar[7] = pagemaxdepth;
					}
					mem[pagetail].setrh(p);
					pagetail = p;
					mem[memtop - 1].setrh(mem[p].getrh());
					mem[p].setrh(0);
					break lab30;
				}
				/* lab31: */mem[memtop - 1].setrh(mem[p].getrh());
				mem[p].setrh(0);
				flushnodelist(p);
				break;
			}
			/* lab30: */} while (!(mem[memtop - 1].getrh() == 0));
		if (nestptr == 0) {
			curlist.tailfield = memtop - 1;
		} else {
			nest[0].tailfield = memtop - 1;
		}
	}

	void appspace() {
		int q;
		if ((curlist.auxfield.getlh() >= 2000) && (eqtb[7195].getrh() != 0)) {
			q = newparamglue(13);
		} else {
			if (eqtb[7194].getrh() != 0) {
				mainp = eqtb[7194].getrh();
			} else {
				mainp = fontglue[eqtb[8234].getrh()];
				if (mainp == 0) {
					mainp = newspec(0);
					maink = parambase[eqtb[8234].getrh()] + 2;
					mem[mainp + 1].setInt(fontinfo[maink].getInt());
					mem[mainp + 2].setInt(fontinfo[maink + 1].getInt());
					mem[mainp + 3].setInt(fontinfo[maink + 2].getInt());
					fontglue[eqtb[8234].getrh()] = mainp;
				}
			}
			mainp = newspec(mainp);
			if (curlist.auxfield.getlh() >= 2000) {
				mem[mainp + 1].setInt(mem[mainp + 1].getInt() + fontinfo[7 + parambase[eqtb[8234].getrh()]].getInt());
			}
			mem[mainp + 2].setInt(xnoverd(mem[mainp + 2].getInt(), curlist.auxfield.getlh(), 1000));
			mem[mainp + 3].setInt(xnoverd(mem[mainp + 3].getInt(), 1000, curlist.auxfield.getlh()));
			q = newglue(mainp);
			mem[mainp].setrh(0);
		}
		mem[curlist.tailfield].setrh(q);
		curlist.tailfield = q;
	}

	boolean itsallover() {
		/* 10 */boolean Result;
		if (privileged()) {
			if ((memtop - 2 == pagetail) && (curlist.headfield == curlist.tailfield) && (deadcycles == 0)) {
				Result = true;
				return Result /* lab10 */;
			}
			unreadToken();
			{
				mem[curlist.tailfield].setrh(newnullbox());
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			mem[curlist.tailfield + 1].setInt(eqtb[10133].getInt());
			{
				mem[curlist.tailfield].setrh(newglue(8));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			{
				mem[curlist.tailfield].setrh(newpenalty(-1073741824));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			buildpage();
		}
		Result = false;
		return Result;
	}

	void appendglue() {
		int s;
		s = curchr;
		switch (s) {
			case 0:
				curval = 4;
				break;
			case 1:
				curval = 8;
				break;
			case 2:
				curval = 12;
				break;
			case 3:
				curval = 16;
				break;
			case 4:
				scanglue(2);
				break;
			case 5:
				scanglue(3);
				break;
		}
		{
			mem[curlist.tailfield].setrh(newglue(curval));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		if (s >= 4) {
			mem[curval].setrh(mem[curval].getrh() - 1);
			if (s > 4) {
				mem[curlist.tailfield].setb1(99);
			}
		}
	}

	void appendkern() {
		int s;
		s = curchr;
		scandimen(s == 99, false, false);
		{
			mem[curlist.tailfield].setrh(newkern(curval));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		mem[curlist.tailfield].setb1(s);
	}

	void offsave() {
		int p;
		if (curgroup == 0) {
			{
				printnl(262);
				print(776);
			}
			printcmdchr(curcmd, curchr);
			{
				helpptr = 1;
				helpline[0] = 1043;
			}
			errorLogic.error();
		} else {
			unreadToken();
			p = allocateMemoryWord();
			mem[memtop - 3].setrh(p);
			{
				printnl(262);
				print(625);
			}
			switch (curgroup) {
				case 14: {
					mem[p].setlh(11011);
					printEscapeSequence(516);
				}
					break;
				case 15: {
					mem[p].setlh(804);
					printchar(36);
				}
					break;
				case 16: {
					mem[p].setlh(11012);
					mem[p].setrh(allocateMemoryWord());
					p = mem[p].getrh();
					mem[p].setlh(3118);
					printEscapeSequence(1042);
				}
					break;
				default: {
					mem[p].setlh(637);
					printchar(125);
				}
					break;
			}
			print(626);
			begintokenlist(mem[memtop - 3].getrh(), 4);
			{
				helpptr = 5;
				helpline[4] = 1037;
				helpline[3] = 1038;
				helpline[2] = 1039;
				helpline[1] = 1040;
				helpline[0] = 1041;
			}
			errorLogic.error();
		}
	}

	void normalparagraph() {
		if (eqtb[9582].getInt() != 0) {
			eqworddefine(9582, 0);
		}
		if (eqtb[10147].getInt() != 0) {
			eqworddefine(10147, 0);
		}
		if (eqtb[9604].getInt() != 1) {
			eqworddefine(9604, 1);
		}
		if (eqtb[7712].getrh() != 0) {
			eqdefine(7712, 118, 0);
		}
	}

	void boxend(final int boxcontext) {
		int p;
		if (boxcontext < 1073741824) {
			if (curbox != 0) {
				mem[curbox + 4].setInt(boxcontext);
				if (Math.abs(curlist.modefield) == 1) {
					appendtovlist(curbox);
					if (adjusttail != 0) {
						if (memtop - 5 != adjusttail) {
							mem[curlist.tailfield].setrh(mem[memtop - 5].getrh());
							curlist.tailfield = adjusttail;
						}
						adjusttail = 0;
					}
					if (curlist.modefield > 0) {
						buildpage();
					}
				} else {
					if (Math.abs(curlist.modefield) == 102) {
						curlist.auxfield.setlh(1000);
					} else {
						p = newnoad();
						mem[p + 1].setrh(2);
						mem[p + 1].setlh(curbox);
						curbox = p;
					}
					mem[curlist.tailfield].setrh(curbox);
					curlist.tailfield = curbox;
				}
			}
		} else if (boxcontext < 1073742336) {
			if (boxcontext < 1073742080) {
				eqdefine(-1073733846 + boxcontext, 119, curbox);
			} else {
				geqdefine(-1073734102 + boxcontext, 119, curbox);
			}
		} else if (curbox != 0) {
			if (boxcontext > 1073742336) {
				do {
					getxtoken();
				} while (!((curcmd != 10) && (curcmd != 0)));
				if (((curcmd == 26) && (Math.abs(curlist.modefield) != 1)) || ((curcmd == 27) && (Math.abs(curlist.modefield) == 1)) || ((curcmd == 28) && (Math.abs(curlist.modefield) == 203))) {
					appendglue();
					mem[curlist.tailfield].setb1(boxcontext - (1073742237));
					mem[curlist.tailfield + 1].setrh(curbox);
				} else {
					{
						printnl(262);
						print(1066);
					}
					{
						helpptr = 3;
						helpline[2] = 1067;
						helpline[1] = 1068;
						helpline[0] = 1069;
					}
					errorLogic.backerror();
					flushnodelist(curbox);
				}
			} else {
				shipout(curbox);
			}
		}
	}

	void beginbox(final int boxcontext) {
		/* 10 30 */int p, q;
		int m;
		int k;
		int n;
		switch (curchr) {
			case 0: {
				scaneightbitint();
				curbox = eqtb[7978 + curval].getrh();
				eqtb[7978 + curval].setrh(0);
			}
				break;
			case 1: {
				scaneightbitint();
				curbox = copynodelist(eqtb[7978 + curval].getrh());
			}
				break;
			case 2: {
				curbox = 0;
				if (Math.abs(curlist.modefield) == 203) {
					errorLogic.youCantUse("Sorry; this \\lastbox will be void.");
				} else if ((curlist.modefield == 1) && (curlist.headfield == curlist.tailfield)) {
					errorLogic.youCantUse("Sorry...I usually can't take things from the current page.", "This \\lastbox will therefore be void.");
				} else {
					if (!(curlist.tailfield >= himemmin)) {
						if ((mem[curlist.tailfield].getb0() == 0) || (mem[curlist.tailfield].getb0() == 1)) {
							q = curlist.headfield;
							lab30: while (true) {
								do {
									p = q;
									if (!(q >= himemmin)) {
										if (mem[q].getb0() == 7) {
											for (m = 1; m <= mem[q].getb1(); m++) {
												p = mem[p].getrh();
											}
											if (p == curlist.tailfield) {
												break lab30;
											}
										}
									}
									q = mem[p].getrh();
								} while (!(q == curlist.tailfield));
								curbox = curlist.tailfield;
								mem[curbox + 4].setInt(0);
								curlist.tailfield = p;
								mem[p].setrh(0);
								break;
							}
							/* lab30: */}
					}
				}
			}
				break;
			case 3: {
				scaneightbitint();
				n = curval;
				if (!scankeyword(842)) {
					{
						printnl(262);
						print(1073);
					}
					{
						helpptr = 2;
						helpline[1] = 1074;
						helpline[0] = 1075;
					}
					errorLogic.error();
				}
				scandimen(false, false, false);
				curbox = vsplit(n, curval);
			}
				break;
			default: {
				k = curchr - 4;
				savestack[saveptr + 0].setInt(boxcontext);
				if (k == 102) {
					if ((boxcontext < 1073741824) && (Math.abs(curlist.modefield) == 1)) {
						scanspec(3, true);
					} else {
						scanspec(2, true);
					}
				} else {
					if (k == 1) {
						scanspec(4, true);
					} else {
						scanspec(5, true);
						k = 1;
					}
					normalparagraph();
				}
				pushnest();
				curlist.modefield = -k;
				if (k == 1) {
					curlist.auxfield.setInt(-65536000);
					if (eqtb[7718].getrh() != 0) {
						begintokenlist(eqtb[7718].getrh(), 11);
					}
				} else {
					curlist.auxfield.setlh(1000);
					if (eqtb[7717].getrh() != 0) {
						begintokenlist(eqtb[7717].getrh(), 10);
					}
				}
				return /* lab10 */;
			}
		}
		boxend(boxcontext);
	}

	void scanbox(final int boxcontext) {
		do {
			getxtoken();
		} while (!((curcmd != 10) && (curcmd != 0)));
		if (curcmd == 20) {
			beginbox(boxcontext);
		} else if ((boxcontext >= 1073742337) && ((curcmd == 36) || (curcmd == 35))) {
			curbox = scanrulespec();
			boxend(boxcontext);
		} else {
			{
				printnl(262);
				print(1076);
			}
			{
				helpptr = 3;
				helpline[2] = 1077;
				helpline[1] = 1078;
				helpline[0] = 1079;
			}
			errorLogic.backerror();
		}
	}

	void Package(final int c) {
		int h;
		int p;
		int d;
		d = eqtb[10137].getInt();
		unsave();
		saveptr = saveptr - 3;
		if (curlist.modefield == -102) {
			curbox = hpack(mem[curlist.headfield].getrh(), savestack[saveptr + 2].getInt(), savestack[saveptr + 1].getInt());
		} else {
			curbox = vpackage(mem[curlist.headfield].getrh(), savestack[saveptr + 2].getInt(), savestack[saveptr + 1].getInt(), d);
			if (c == 4) {
				h = 0;
				p = mem[curbox + 5].getrh();
				if (p != 0) {
					if (mem[p].getb0() <= 2) {
						h = mem[p + 3].getInt();
					}
				}
				mem[curbox + 2].setInt(mem[curbox + 2].getInt() - h + mem[curbox + 3].getInt());
				mem[curbox + 3].setInt(h);
			}
		}
		popnest();
		boxend(savestack[saveptr + 0].getInt());
	}

	int normmin(final int h) {
		int Result;
		if (h <= 0) {
			Result = 1;
		} else if (h >= 63) {
			Result = 63;
		} else {
			Result = h;
		}
		return Result;
	}

	void newgraf(final boolean indented) {
		curlist.pgfield = 0;
		if ((curlist.modefield == 1) || (curlist.headfield != curlist.tailfield)) {
			mem[curlist.tailfield].setrh(newparamglue(2));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		pushnest();
		curlist.modefield = 102;
		curlist.auxfield.setlh(1000);
		if (eqtb[9613].getInt() <= 0) {
			curlang = 0;
		} else if (eqtb[9613].getInt() > 255) {
			curlang = 0;
		} else {
			curlang = eqtb[9613].getInt();
		}
		curlist.auxfield.setrh(curlang);
		curlist.pgfield = (normmin(eqtb[9614].getInt()) * 64 + normmin(eqtb[9615].getInt())) * 65536 + curlang;
		if (indented) {
			curlist.tailfield = newnullbox();
			mem[curlist.headfield].setrh(curlist.tailfield);
			mem[curlist.tailfield + 1].setInt(eqtb[10130].getInt());
		}
		if (eqtb[7714].getrh() != 0) {
			begintokenlist(eqtb[7714].getrh(), 7);
		}
		if (nestptr == 1) {
			buildpage();
		}
	}

	void indentinhmode() {
		int p, q;
		if (curchr > 0) {
			p = newnullbox();
			mem[p + 1].setInt(eqtb[10130].getInt());
			if (Math.abs(curlist.modefield) == 102) {
				curlist.auxfield.setlh(1000);
			} else {
				q = newnoad();
				mem[q + 1].setrh(2);
				mem[q + 1].setlh(p);
				p = q;
			}
			{
				mem[curlist.tailfield].setrh(p);
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
		}
	}

	void headforvmode() {
		if (curlist.modefield < 0) {
			if (curcmd != 36) {
				offsave();
			} else {
				{
					printnl(262);
					print(685);
				}
				printEscapeSequence(521);
				print(1082);
				{
					helpptr = 2;
					helpline[1] = 1083;
					helpline[0] = 1084;
				}
				errorLogic.error();
			}
		} else {
			unreadToken();
			curtok = partoken;
			unreadToken();
			curinput.setIndex(4);
		}
	}

	void endgraf() {
		if (curlist.modefield == 102) {
			if (curlist.headfield == curlist.tailfield) {
				popnest();
			} else {
				linebreak(eqtb[9569].getInt());
			}
			normalparagraph();
			errorReporter.resetErrorCount();
		}
	}

	void begininsertoradjust() {
		if (curcmd == 38) {
			curval = 255;
		} else {
			scaneightbitint();
			if (curval == 255) {
				{
					printnl(262);
					print(1085);
				}
				printEscapeSequence(330);
				printInt(255);
				{
					helpptr = 1;
					helpline[0] = 1086;
				}
				errorLogic.error();
				curval = 0;
			}
		}
		savestack[saveptr + 0].setInt(curval);
		saveptr = saveptr + 1;
		newsavelevel(11);
		scanleftbrace();
		normalparagraph();
		pushnest();
		curlist.modefield = -1;
		curlist.auxfield.setInt(-65536000);
	}

	void makemark() {
		int p;
		p = scantoks(false, true);
		p = getnode(2);
		mem[p].setb0(4);
		mem[p].setb1(0);
		mem[p + 1].setInt(defref);
		mem[curlist.tailfield].setrh(p);
		curlist.tailfield = p;
	}

	void appendpenalty() {
		scanint();
		{
			mem[curlist.tailfield].setrh(newpenalty(curval));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		if (curlist.modefield == 1) {
			buildpage();
		}
	}

	void deletelast() {
		/* 10 */int p, q;
		int m;
		if ((curlist.modefield == 1) && (curlist.tailfield == curlist.headfield)) {
			if ((curchr != 10) || (lastglue != maxhalfword)) {
				String line2 = "Try `I\\vskip-\\lastskip' instead.";
				if (curchr == 11) {
					line2 = "Try `I\\kern-\\lastkern' instead.";
				} else if (curchr != 10) {
					line2 = "Perhaps you can make the output routine do it.";
				}
				errorLogic.youCantUse("Sorry...I usually can't take things from the current page.", line2);
			}
		} else {
			if (!(curlist.tailfield >= himemmin)) {
				if (mem[curlist.tailfield].getb0() == curchr) {
					q = curlist.headfield;
					do {
						p = q;
						if (!(q >= himemmin)) {
							if (mem[q].getb0() == 7) {
								for (m = 1; m <= mem[q].getb1(); m++) {
									p = mem[p].getrh();
								}
								if (p == curlist.tailfield) {
									return /* lab10 */;
								}
							}
						}
						q = mem[p].getrh();
					} while (!(q == curlist.tailfield));
					mem[p].setrh(0);
					flushnodelist(curlist.tailfield);
					curlist.tailfield = p;
				}
			}
		}
	}

	void unpackage() {
		/* 10 */int p;
		int c;
		c = curchr;
		scaneightbitint();
		p = eqtb[7978 + curval].getrh();
		if (p == 0) {
			return /* lab10 */;
		}
		if ((Math.abs(curlist.modefield) == 203) || ((Math.abs(curlist.modefield) == 1) && (mem[p].getb0() != 1)) || ((Math.abs(curlist.modefield) == 102) && (mem[p].getb0() != 0))) {
			{
				printnl(262);
				print(1097);
			}
			{
				helpptr = 3;
				helpline[2] = 1098;
				helpline[1] = 1099;
				helpline[0] = 1100;
			}
			errorLogic.error();
			return /* lab10 */;
		}
		if (c == 1) {
			mem[curlist.tailfield].setrh(copynodelist(mem[p + 5].getrh()));
		} else {
			mem[curlist.tailfield].setrh(mem[p + 5].getrh());
			eqtb[7978 + curval].setrh(0);
			freenode(p, 7);
		}
		while (mem[curlist.tailfield].getrh() != 0) {
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
	}

	void appenditaliccorrection() {
		/* 10 */int p;
		int f;
		if (curlist.tailfield != curlist.headfield) {
			if ((curlist.tailfield >= himemmin)) {
				p = curlist.tailfield;
			} else if (mem[curlist.tailfield].getb0() == 6) {
				p = curlist.tailfield + 1;
			} else {
				return /* lab10 */;
			}
			f = mem[p].getb0();
			{
				mem[curlist.tailfield].setrh(newkern(fontinfo[italicbase[f] + (fontinfo[charbase[f] + mem[p].getb1()].getb2()) / 4].getInt()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			mem[curlist.tailfield].setb1(1);
		}
	}

	void appenddiscretionary() {
		int c;
		{
			mem[curlist.tailfield].setrh(newdisc());
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		if (curchr == 1) {
			c = hyphenchar[eqtb[8234].getrh()];
			if (c >= 0) {
				if (c < 256) {
					mem[curlist.tailfield + 1].setlh(newcharacter(eqtb[8234].getrh(), c));
				}
			}
		} else {
			saveptr = saveptr + 1;
			savestack[saveptr - 1].setInt(0);
			newsavelevel(10);
			scanleftbrace();
			pushnest();
			curlist.modefield = -102;
			curlist.auxfield.setlh(1000);
		}
	}

	void builddiscretionary() {
		/* 30 10 */int p, q;
		int n;
		unsave();
		q = curlist.headfield;
		p = mem[q].getrh();
		n = 0;
		while (p != 0) {
			if (!(p >= himemmin)) {
				if (mem[p].getb0() > 2) {
					if (mem[p].getb0() != 11) {
						if (mem[p].getb0() != 6) {
							{
								printnl(262);
								print(1107);
							}
							{
								helpptr = 1;
								helpline[0] = 1108;
							}
							errorLogic.error();
							begindiagnostic();
							printnl(1109);
							showbox(p);
							enddiagnostic(true);
							flushnodelist(p);
							mem[q].setrh(0);
							break /* lab30 */;
						}
					}
				}
			}
			q = p;
			p = mem[q].getrh();
			n = n + 1;
		}
		/* lab30: */p = mem[curlist.headfield].getrh();
		popnest();
		switch (savestack[saveptr - 1].getInt()) {
			case 0:
				mem[curlist.tailfield + 1].setlh(p);
				break;
			case 1:
				mem[curlist.tailfield + 1].setrh(p);
				break;
			case 2: {
				if ((n > 0) && (Math.abs(curlist.modefield) == 203)) {
					{
						printnl(262);
						print(1101);
					}
					printEscapeSequence(349);
					{
						helpptr = 2;
						helpline[1] = 1102;
						helpline[0] = 1103;
					}
					flushnodelist(p);
					n = 0;
					errorLogic.error();
				} else {
					mem[curlist.tailfield].setrh(p);
				}
				if (n <= 255) {
					mem[curlist.tailfield].setb1(n);
				} else {
					{
						printnl(262);
						print(1104);
					}
					{
						helpptr = 2;
						helpline[1] = 1105;
						helpline[0] = 1106;
					}
					errorLogic.error();
				}
				if (n > 0) {
					curlist.tailfield = q;
				}
				saveptr = saveptr - 1;
				return /* lab10 */;
			}
		}
		savestack[saveptr - 1].setInt(savestack[saveptr - 1].getInt() + 1);
		newsavelevel(10);
		scanleftbrace();
		pushnest();
		curlist.modefield = -102;
		curlist.auxfield.setlh(1000);
	}

	void makeaccent() {
		double s, t;
		int p, q, r;
		int f;
		int a, h, x, w, delta;
		final fourquarters i = new fourquarters();
		scancharnum();
		f = eqtb[8234].getrh();
		p = newcharacter(f, curval);
		if (p != 0) {
			x = fontinfo[5 + parambase[f]].getInt();
			s = fontinfo[1 + parambase[f]].getInt() / (65536.0);
			a = fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[p].getb1()].getb0()].getInt();
			doassignments();
			q = 0;
			f = eqtb[8234].getrh();
			if ((curcmd == 11) || (curcmd == 12) || (curcmd == 68)) {
				q = newcharacter(f, curchr);
			} else if (curcmd == 16) {
				scancharnum();
				q = newcharacter(f, curval);
			} else {
				unreadToken();
			}
			if (q != 0) {
				t = fontinfo[1 + parambase[f]].getInt() / (65536.0);
				i.copy(fontinfo[charbase[f] + mem[q].getb1()].qqqq());
				w = fontinfo[widthbase[f] + i.b0].getInt();
				h = fontinfo[heightbase[f] + (i.b1) / 16].getInt();
				if (h != x) {
					p = hpack(p, 0, 1);
					mem[p + 4].setInt(x - h);
				}
				delta = (int)Math.round((w - a) / (2.0) + h * t - x * s);
				r = newkern(delta);
				mem[r].setb1(2);
				mem[curlist.tailfield].setrh(r);
				mem[r].setrh(p);
				curlist.tailfield = newkern(-a - delta);
				mem[curlist.tailfield].setb1(2);
				mem[p].setrh(curlist.tailfield);
				p = q;
			}
			mem[curlist.tailfield].setrh(p);
			curlist.tailfield = p;
			curlist.auxfield.setlh(1000);
		}
	}

	void doendv() {
		if (curgroup == 6) {
			endgraf();
			if (fincol()) {
				finrow();
			}
		} else {
			offsave();
		}
	}

	void pushmath(final int c) {
		pushnest();
		curlist.modefield = -203;
		curlist.auxfield.setInt(0);
		newsavelevel(c);
	}

	void initmath() {
		/* 21 40 45 30 */int w;
		int l;
		int s;
		int p;
		int q;
		int f;
		int n;
		int v;
		int d;
		gettoken();
		if ((curcmd == 3) && (curlist.modefield > 0)) {
			if (curlist.headfield == curlist.tailfield) {
				popnest();
				w = -1073741823;
			} else {
				linebreak(eqtb[9570].getInt());
				v = mem[justbox + 4].getInt() + 2 * fontinfo[6 + parambase[eqtb[8234].getrh()]].getInt();
				w = -1073741823;
				p = mem[justbox + 5].getrh();
				lab30: while (p != 0) {
					lab45: while (true) {
						lab40: while (true) {
							lab21: while (true) {
								if ((p >= himemmin)) {
									f = mem[p].getb0();
									d = fontinfo[widthbase[f] + fontinfo[charbase[f] + mem[p].getb1()].getb0()].getInt();
									break lab40;
								}
								switch (mem[p].getb0()) {
									case 0:
									case 1:
									case 2: {
										d = mem[p + 1].getInt();
										break lab40;
									}
									case 6: {
										mem[memtop - 12].copy(mem[p + 1]);
										mem[memtop - 12].setrh(mem[p].getrh());
										p = memtop - 12;
										continue lab21;
									}
									case 11:
									case 9:
										d = mem[p + 1].getInt();
										break;
									case 10: {
										q = mem[p + 1].getlh();
										d = mem[q + 1].getInt();
										if (mem[justbox + 5].getb0() == 1) {
											if ((mem[justbox + 5].getb1() == mem[q].getb0()) && (mem[q + 2].getInt() != 0)) {
												v = 1073741823;
											}
										} else if (mem[justbox + 5].getb0() == 2) {
											if ((mem[justbox + 5].getb1() == mem[q].getb1()) && (mem[q + 3].getInt() != 0)) {
												v = 1073741823;
											}
										}
										if (mem[p].getb1() >= 100) {
											break lab40;
										}
									}
										break;
									case 8:
										d = 0;
										break;
									default:
										d = 0;
										break;
								}
								break;
							}
							if (v < 1073741823) {
								v = v + d;
							}
							break lab45;
						}
						/* lab40: */if (v < 1073741823) {
							v = v + d;
							w = v;
						} else {
							w = 1073741823;
							break lab30;
						}
						break;
					}
					/* lab45: */p = mem[p].getrh();
				}
				/* lab30: */}
			if (eqtb[7712].getrh() == 0) {
				if ((eqtb[10147].getInt() != 0) && (((eqtb[9604].getInt() >= 0) && (curlist.pgfield + 2 > eqtb[9604].getInt())) || (curlist.pgfield + 1 < -eqtb[9604].getInt()))) {
					l = eqtb[10133].getInt() - Math.abs(eqtb[10147].getInt());
					if (eqtb[10147].getInt() > 0) {
						s = eqtb[10147].getInt();
					} else {
						s = 0;
					}
				} else {
					l = eqtb[10133].getInt();
					s = 0;
				}
			} else {
				n = mem[eqtb[7712].getrh()].getlh();
				if (curlist.pgfield + 2 >= n) {
					p = eqtb[7712].getrh() + 2 * n;
				} else {
					p = eqtb[7712].getrh() + 2 * (curlist.pgfield + 2);
				}
				s = mem[p - 1].getInt();
				l = mem[p].getInt();
			}
			pushmath(15);
			curlist.modefield = 203;
			eqworddefine(9607, -1);
			eqworddefine(10143, w);
			eqworddefine(10144, l);
			eqworddefine(10145, s);
			if (eqtb[7716].getrh() != 0) {
				begintokenlist(eqtb[7716].getrh(), 9);
			}
			if (nestptr == 1) {
				buildpage();
			}
		} else {
			unreadToken();
			{
				pushmath(15);
				eqworddefine(9607, -1);
				if (eqtb[7715].getrh() != 0) {
					begintokenlist(eqtb[7715].getrh(), 8);
				}
			}
		}
	}

	void starteqno() {
		savestack[saveptr + 0].setInt(curchr);
		saveptr = saveptr + 1;
		{
			pushmath(15);
			eqworddefine(9607, -1);
			if (eqtb[7715].getrh() != 0) {
				begintokenlist(eqtb[7715].getrh(), 8);
			}
		}
	}

	void scanmath(final int p) {
		/* 20 21 10 */int c;
		lab20: while (true) {
			do {
				getxtoken();
			} while (!((curcmd != 10) && (curcmd != 0)));
			lab21: while (true) {
				switch (curcmd) {
					case 11:
					case 12:
					case 68: {
						c = eqtb[9307 + curchr].getrh();
						if (c == 32768) {
							{
								curcs = curchr + 1;
								curcmd = eqtb[curcs].getb0();
								curchr = eqtb[curcs].getrh();
								xtoken();
								unreadToken();
							}
							continue lab20;
						}
					}
						break;
					case 16: {
						scancharnum();
						curchr = curval;
						curcmd = 68;
						continue lab21;
					}
					case 17: {
						scanfifteenbitint();
						c = curval;
					}
						break;
					case 69:
						c = curchr;
						break;
					case 15: {
						scantwentysevenbitint();
						c = curval / 4096;
					}
						break;
					default: {
						unreadToken();
						scanleftbrace();
						savestack[saveptr + 0].setInt(p);
						saveptr = saveptr + 1;
						pushmath(9);
						return /* lab10 */;
					}
				}
				break;
			}
			mem[p].setrh(1);
			mem[p].setb1(c % 256);
			if ((c >= 28672) && ((eqtb[9607].getInt() >= 0) && (eqtb[9607].getInt() < 16))) {
				mem[p].setb0(eqtb[9607].getInt());
			} else {
				mem[p].setb0((c / 256) % 16);
			}
			break;
		}
	}

	void setmathchar(final int c) {
		int p;
		if (c >= 32768) {
			curcs = curchr + 1;
			curcmd = eqtb[curcs].getb0();
			curchr = eqtb[curcs].getrh();
			xtoken();
			unreadToken();
		} else {
			p = newnoad();
			mem[p + 1].setrh(1);
			mem[p + 1].setb1(c % 256);
			mem[p + 1].setb0((c / 256) % 16);
			if (c >= 28672) {
				if (((eqtb[9607].getInt() >= 0) && (eqtb[9607].getInt() < 16))) {
					mem[p + 1].setb0(eqtb[9607].getInt());
				}
				mem[p].setb0(16);
			} else {
				mem[p].setb0(16 + (c / 4096));
			}
			mem[curlist.tailfield].setrh(p);
			curlist.tailfield = p;
		}
	}

	void mathlimitswitch() {
		/* 10 */if (curlist.headfield != curlist.tailfield) {
			if (mem[curlist.tailfield].getb0() == 17) {
				mem[curlist.tailfield].setb1(curchr);
				return /* lab10 */;
			}
		}
		{
			printnl(262);
			print(1130);
		}
		{
			helpptr = 1;
			helpline[0] = 1131;
		}
		errorLogic.error();
	}

	void scandelimiter(final int p, final boolean r) {
		if (r) {
			scantwentysevenbitint();
		} else {
			do {
				getxtoken();
			} while (!((curcmd != 10) && (curcmd != 0)));
			switch (curcmd) {
				case 11:
				case 12:
					curval = eqtb[9874 + curchr].getInt();
					break;
				case 15:
					scantwentysevenbitint();
					break;
				default:
					curval = -1;
					break;
			}
		}
		if (curval < 0) {
			{
				printnl(262);
				print(1132);
			}
			{
				helpptr = 6;
				helpline[5] = 1133;
				helpline[4] = 1134;
				helpline[3] = 1135;
				helpline[2] = 1136;
				helpline[1] = 1137;
				helpline[0] = 1138;
			}
			errorLogic.backerror();
			curval = 0;
		}
		mem[p].setb0((curval / 1048576) % 16);
		mem[p].setb1((curval / 4096) % 256);
		mem[p].setb2((curval / 256) % 16);
		mem[p].setb3(curval % 256);
	}

	void mathradical() {
		{
			mem[curlist.tailfield].setrh(getnode(5));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		mem[curlist.tailfield].setb0(24);
		mem[curlist.tailfield].setb1(0);
		mem[curlist.tailfield + 1].sethh(emptyfield);
		mem[curlist.tailfield + 3].sethh(emptyfield);
		mem[curlist.tailfield + 2].sethh(emptyfield);
		scandelimiter(curlist.tailfield + 4, true);
		scanmath(curlist.tailfield + 1);
	}

	void mathac() {
		if (curcmd == 45) {
			{
				printnl(262);
				print(1139);
			}
			printEscapeSequence(523);
			print(1140);
			{
				helpptr = 2;
				helpline[1] = 1141;
				helpline[0] = 1142;
			}
			errorLogic.error();
		}
		{
			mem[curlist.tailfield].setrh(getnode(5));
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		mem[curlist.tailfield].setb0(28);
		mem[curlist.tailfield].setb1(0);
		mem[curlist.tailfield + 1].sethh(emptyfield);
		mem[curlist.tailfield + 3].sethh(emptyfield);
		mem[curlist.tailfield + 2].sethh(emptyfield);
		mem[curlist.tailfield + 4].setrh(1);
		scanfifteenbitint();
		mem[curlist.tailfield + 4].setb1(curval % 256);
		if ((curval >= 28672) && ((eqtb[9607].getInt() >= 0) && (eqtb[9607].getInt() < 16))) {
			mem[curlist.tailfield + 4].setb0(eqtb[9607].getInt());
		} else {
			mem[curlist.tailfield + 4].setb0((curval / 256) % 16);
		}
		scanmath(curlist.tailfield + 1);
	}

	void appendchoices() {
		{
			mem[curlist.tailfield].setrh(newchoice());
			curlist.tailfield = mem[curlist.tailfield].getrh();
		}
		saveptr = saveptr + 1;
		savestack[saveptr - 1].setInt(0);
		pushmath(13);
		scanleftbrace();
	}

	int finmlist(final int p) {
		int Result;
		int q;
		if (curlist.auxfield.getInt() != 0) {
			mem[curlist.auxfield.getInt() + 3].setrh(3);
			mem[curlist.auxfield.getInt() + 3].setlh(mem[curlist.headfield].getrh());
			if (p == 0) {
				q = curlist.auxfield.getInt();
			} else {
				q = mem[curlist.auxfield.getInt() + 2].getlh();
				if (mem[q].getb0() != 30) {
					errorLogic.confusion(877);
				}
				mem[curlist.auxfield.getInt() + 2].setlh(mem[q].getrh());
				mem[q].setrh(curlist.auxfield.getInt());
				mem[curlist.auxfield.getInt()].setrh(p);
			}
		} else {
			mem[curlist.tailfield].setrh(p);
			q = mem[curlist.headfield].getrh();
		}
		popnest();
		Result = q;
		return Result;
	}

	void buildchoices() {
		/* 10 */int p;
		unsave();
		p = finmlist(0);
		switch (savestack[saveptr - 1].getInt()) {
			case 0:
				mem[curlist.tailfield + 1].setlh(p);
				break;
			case 1:
				mem[curlist.tailfield + 1].setrh(p);
				break;
			case 2:
				mem[curlist.tailfield + 2].setlh(p);
				break;
			case 3: {
				mem[curlist.tailfield + 2].setrh(p);
				saveptr = saveptr - 1;
				return /* lab10 */;
			}
		}
		savestack[saveptr - 1].setInt(savestack[saveptr - 1].getInt() + 1);
		pushmath(13);
		scanleftbrace();
	}

	void subsup() {
		int t;
		int p;
		t = 0;
		p = 0;
		if (curlist.tailfield != curlist.headfield) {
			if ((mem[curlist.tailfield].getb0() >= 16) && (mem[curlist.tailfield].getb0() < 30)) {
				p = curlist.tailfield + 2 + curcmd - 7;
				t = mem[p].getrh();
			}
		}
		if ((p == 0) || (t != 0)) {
			{
				mem[curlist.tailfield].setrh(newnoad());
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			p = curlist.tailfield + 2 + curcmd - 7;
			if (t != 0) {
				if (curcmd == 7) {
					{
						printnl(262);
						print(1143);
					}
					{
						helpptr = 1;
						helpline[0] = 1144;
					}
				} else {
					{
						printnl(262);
						print(1145);
					}
					{
						helpptr = 1;
						helpline[0] = 1146;
					}
				}
				errorLogic.error();
			}
		}
		scanmath(p);
	}

	void mathfraction() {
		int c;
		c = curchr;
		if (curlist.auxfield.getInt() != 0) {
			if (c >= 3) {
				scandelimiter(memtop - 12, false);
				scandelimiter(memtop - 12, false);
			}
			if (c % 3 == 0) {
				scandimen(false, false, false);
			}
			{
				printnl(262);
				print(1153);
			}
			{
				helpptr = 3;
				helpline[2] = 1154;
				helpline[1] = 1155;
				helpline[0] = 1156;
			}
			errorLogic.error();
		} else {
			curlist.auxfield.setInt(getnode(6));
			mem[curlist.auxfield.getInt()].setb0(25);
			mem[curlist.auxfield.getInt()].setb1(0);
			mem[curlist.auxfield.getInt() + 2].setrh(3);
			mem[curlist.auxfield.getInt() + 2].setlh(mem[curlist.headfield].getrh());
			mem[curlist.auxfield.getInt() + 3].sethh(emptyfield);
			mem[curlist.auxfield.getInt() + 4].setqqqq(nulldelimiter);
			mem[curlist.auxfield.getInt() + 5].setqqqq(nulldelimiter);
			mem[curlist.headfield].setrh(0);
			curlist.tailfield = curlist.headfield;
			if (c >= 3) {
				scandelimiter(curlist.auxfield.getInt() + 4, false);
				scandelimiter(curlist.auxfield.getInt() + 5, false);
			}
			switch (c % 3) {
				case 0: {
					scandimen(false, false, false);
					mem[curlist.auxfield.getInt() + 1].setInt(curval);
				}
					break;
				case 1:
					mem[curlist.auxfield.getInt() + 1].setInt(1073741824);
					break;
				case 2:
					mem[curlist.auxfield.getInt() + 1].setInt(0);
					break;
			}
		}
	}

	void mathleftright() {
		int t;
		int p;
		t = curchr;
		if ((t == 31) && (curgroup != 16)) {
			if (curgroup == 15) {
				scandelimiter(memtop - 12, false);
				{
					printnl(262);
					print(776);
				}
				printEscapeSequence(877);
				{
					helpptr = 1;
					helpline[0] = 1157;
				}
				errorLogic.error();
			} else {
				offsave();
			}
		} else {
			p = newnoad();
			mem[p].setb0(t);
			scandelimiter(p + 1, false);
			if (t == 30) {
				pushmath(16);
				mem[curlist.headfield].setrh(p);
				curlist.tailfield = p;
			} else {
				p = finmlist(p);
				unsave();
				{
					mem[curlist.tailfield].setrh(newnoad());
					curlist.tailfield = mem[curlist.tailfield].getrh();
				}
				mem[curlist.tailfield].setb0(23);
				mem[curlist.tailfield + 1].setrh(3);
				mem[curlist.tailfield + 1].setlh(p);
			}
		}
	}

	void aftermath() {
		boolean l;
		boolean danger;
		int m;
		int p;
		int a;
		int b;
		int w;
		int z;
		int e;
		int q;
		int d;
		int s;
		int g1, g2;
		int r;
		int t;
		danger = false;
		if ((fontparams[eqtb[8237].getrh()] < 22) || (fontparams[eqtb[8253].getrh()] < 22) || (fontparams[eqtb[8269].getrh()] < 22)) {
			{
				printnl(262);
				print(1158);
			}
			{
				helpptr = 3;
				helpline[2] = 1159;
				helpline[1] = 1160;
				helpline[0] = 1161;
			}
			errorLogic.error();
			flushmath();
			danger = true;
		} else if ((fontparams[eqtb[8238].getrh()] < 13) || (fontparams[eqtb[8254].getrh()] < 13) || (fontparams[eqtb[8270].getrh()] < 13)) {
			{
				printnl(262);
				print(1162);
			}
			{
				helpptr = 3;
				helpline[2] = 1163;
				helpline[1] = 1164;
				helpline[0] = 1165;
			}
			errorLogic.error();
			flushmath();
			danger = true;
		}
		m = curlist.modefield;
		l = false;
		p = finmlist(0);
		if (curlist.modefield == -m) {
			{
				getxtoken();
				if (curcmd != 3) {
					{
						printnl(262);
						print(1166);
					}
					{
						helpptr = 2;
						helpline[1] = 1167;
						helpline[0] = 1168;
					}
					errorLogic.backerror();
				}
			}
			curmlist = p;
			curstyle = 2;
			mlistpenalties = false;
			mlisttohlist();
			a = hpack(mem[memtop - 3].getrh(), 0, 1);
			unsave();
			saveptr = saveptr - 1;
			if (savestack[saveptr + 0].getInt() == 1) {
				l = true;
			}
			danger = false;
			if ((fontparams[eqtb[8237].getrh()] < 22) || (fontparams[eqtb[8253].getrh()] < 22) || (fontparams[eqtb[8269].getrh()] < 22)) {
				{
					printnl(262);
					print(1158);
				}
				{
					helpptr = 3;
					helpline[2] = 1159;
					helpline[1] = 1160;
					helpline[0] = 1161;
				}
				errorLogic.error();
				flushmath();
				danger = true;
			} else if ((fontparams[eqtb[8238].getrh()] < 13) || (fontparams[eqtb[8254].getrh()] < 13) || (fontparams[eqtb[8270].getrh()] < 13)) {
				{
					printnl(262);
					print(1162);
				}
				{
					helpptr = 3;
					helpline[2] = 1163;
					helpline[1] = 1164;
					helpline[0] = 1165;
				}
				errorLogic.error();
				flushmath();
				danger = true;
			}
			m = curlist.modefield;
			p = finmlist(0);
		} else {
			a = 0;
		}
		if (m < 0) {
			{
				mem[curlist.tailfield].setrh(newmath(eqtb[10131].getInt(), 0));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			curmlist = p;
			curstyle = 2;
			mlistpenalties = (curlist.modefield > 0);
			mlisttohlist();
			mem[curlist.tailfield].setrh(mem[memtop - 3].getrh());
			while (mem[curlist.tailfield].getrh() != 0) {
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			{
				mem[curlist.tailfield].setrh(newmath(eqtb[10131].getInt(), 1));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			curlist.auxfield.setlh(1000);
			unsave();
		} else {
			if (a == 0) {
				getxtoken();
				if (curcmd != 3) {
					{
						printnl(262);
						print(1166);
					}
					{
						helpptr = 2;
						helpline[1] = 1167;
						helpline[0] = 1168;
					}
					errorLogic.backerror();
				}
			}
			curmlist = p;
			curstyle = 0;
			mlistpenalties = false;
			mlisttohlist();
			p = mem[memtop - 3].getrh();
			adjusttail = memtop - 5;
			b = hpack(p, 0, 1);
			p = mem[b + 5].getrh();
			t = adjusttail;
			adjusttail = 0;
			w = mem[b + 1].getInt();
			z = eqtb[10144].getInt();
			s = eqtb[10145].getInt();
			if ((a == 0) || danger) {
				e = 0;
				q = 0;
			} else {
				e = mem[a + 1].getInt();
				q = e + fontinfo[6 + parambase[eqtb[8237].getrh()]].getInt();
			}
			if (w + q > z) {
				if ((e != 0) && ((w - totalshrink[0] + q <= z) || (totalshrink[1] != 0) || (totalshrink[2] != 0) || (totalshrink[3] != 0))) {
					freenode(b, 7);
					b = hpack(p, z - q, 0);
				} else {
					e = 0;
					if (w > z) {
						freenode(b, 7);
						b = hpack(p, z, 0);
					}
				}
				w = mem[b + 1].getInt();
			}
			d = half(z - w);
			if ((e > 0) && (d < 2 * e)) {
				d = half(z - w - e);
				if (p != 0) {
					if (!(p >= himemmin)) {
						if (mem[p].getb0() == 10) {
							d = 0;
						}
					}
				}
			}
			{
				mem[curlist.tailfield].setrh(newpenalty(eqtb[9574].getInt()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			if ((d + s <= eqtb[10143].getInt()) || l) {
				g1 = 3;
				g2 = 4;
			} else {
				g1 = 5;
				g2 = 6;
			}
			if (l && (e == 0)) {
				mem[a + 4].setInt(s);
				appendtovlist(a);
				{
					mem[curlist.tailfield].setrh(newpenalty(10000));
					curlist.tailfield = mem[curlist.tailfield].getrh();
				}
			} else {
				mem[curlist.tailfield].setrh(newparamglue(g1));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			if (e != 0) {
				r = newkern(z - w - e - d);
				if (l) {
					mem[a].setrh(r);
					mem[r].setrh(b);
					b = a;
					d = 0;
				} else {
					mem[b].setrh(r);
					mem[r].setrh(a);
				}
				b = hpack(b, 0, 1);
			}
			mem[b + 4].setInt(s + d);
			appendtovlist(b);
			if ((a != 0) && (e == 0) && !l) {
				{
					mem[curlist.tailfield].setrh(newpenalty(10000));
					curlist.tailfield = mem[curlist.tailfield].getrh();
				}
				mem[a + 4].setInt(s + z - mem[a + 1].getInt());
				appendtovlist(a);
				g2 = 0;
			}
			if (t != memtop - 5) {
				mem[curlist.tailfield].setrh(mem[memtop - 5].getrh());
				curlist.tailfield = t;
			}
			{
				mem[curlist.tailfield].setrh(newpenalty(eqtb[9575].getInt()));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			if (g2 > 0) {
				mem[curlist.tailfield].setrh(newparamglue(g2));
				curlist.tailfield = mem[curlist.tailfield].getrh();
			}
			resumeafterdisplay();
		}
	}

	void resumeafterdisplay() {
		if (curgroup != 15) {
			errorLogic.confusion(1169);
		}
		unsave();
		curlist.pgfield = curlist.pgfield + 3;
		pushnest();
		curlist.modefield = 102;
		curlist.auxfield.setlh(1000);
		if (eqtb[9613].getInt() <= 0) {
			curlang = 0;
		} else if (eqtb[9613].getInt() > 255) {
			curlang = 0;
		} else {
			curlang = eqtb[9613].getInt();
		}
		curlist.auxfield.setrh(curlang);
		curlist.pgfield = (normmin(eqtb[9614].getInt()) * 64 + normmin(eqtb[9615].getInt())) * 65536 + curlang;
		{
			getxtoken();
			if (curcmd != 10) {
				unreadToken();
			}
		}
		if (nestptr == 1) {
			buildpage();
		}
	}

	void getrtoken() {
		/* 20 */lab20: while (true) {
			do {
				gettoken();
			} while (!(curtok != 2592));
			if ((curcs == 0) || (curcs > 6914)) {
				{
					printnl(262);
					print(1184);
				}
				{
					helpptr = 5;
					helpline[4] = 1185;
					helpline[3] = 1186;
					helpline[2] = 1187;
					helpline[1] = 1188;
					helpline[0] = 1189;
				}
				if (curcs == 0) {
					unreadToken();
				}
				insertToken(11009);
				errorLogic.error();
				continue lab20;
			}
			break;
		}
	}

	void trapzeroglue() {
		if ((mem[curval + 1].getInt() == 0) && (mem[curval + 2].getInt() == 0) && (mem[curval + 3].getInt() == 0)) {
			mem[0].setrh(mem[0].getrh() + 1);
			deleteglueref(curval);
			curval = 0;
		}
	}

	void doregistercommand(final int a) {
		/* 40 10 */int l, q, r, s;
		int p;
		l = 0;
		q = curcmd;
		lab40: while (true) {
			{
				if (q != 89) {
					getxtoken();
					if ((curcmd >= 73) && (curcmd <= 76)) {
						l = curchr;
						p = curcmd - 73;
						break lab40;
					}
					if (curcmd != 89) {
						{
							printnl(262);
							print(685);
						}
						printcmdchr(curcmd, curchr);
						print(686);
						printcmdchr(q, 0);
						{
							helpptr = 1;
							helpline[0] = 1210;
						}
						errorLogic.error();
						return /* lab10 */;
					}
				}
				p = curchr;
				scaneightbitint();
				switch (p) {
					case 0:
						l = curval + 9618;
						break;
					case 1:
						l = curval + 10151;
						break;
					case 2:
						l = curval + 7200;
						break;
					case 3:
						l = curval + 7456;
						break;
				}
			}
			break;
		}
		/* lab40: */if (q == 89) {
			scanoptionalequals();
		} else if (scankeyword(1206)) {
			;
		}
		aritherror = false;
		if (q < 91) {
			if (p < 2) {
				if (p == 0) {
					scanint();
				} else {
					scandimen(false, false, false);
				}
				if (q == 90) {
					curval = curval + eqtb[l].getInt();
				}
			} else {
				scanglue(p);
				if (q == 90) {
					q = newspec(curval);
					r = eqtb[l].getrh();
					deleteglueref(curval);
					mem[q + 1].setInt(mem[q + 1].getInt() + mem[r + 1].getInt());
					if (mem[q + 2].getInt() == 0) {
						mem[q].setb0(0);
					}
					if (mem[q].getb0() == mem[r].getb0()) {
						mem[q + 2].setInt(mem[q + 2].getInt() + mem[r + 2].getInt());
					} else if ((mem[q].getb0() < mem[r].getb0()) && (mem[r + 2].getInt() != 0)) {
						mem[q + 2].setInt(mem[r + 2].getInt());
						mem[q].setb0(mem[r].getb0());
					}
					if (mem[q + 3].getInt() == 0) {
						mem[q].setb1(0);
					}
					if (mem[q].getb1() == mem[r].getb1()) {
						mem[q + 3].setInt(mem[q + 3].getInt() + mem[r + 3].getInt());
					} else if ((mem[q].getb1() < mem[r].getb1()) && (mem[r + 3].getInt() != 0)) {
						mem[q + 3].setInt(mem[r + 3].getInt());
						mem[q].setb1(mem[r].getb1());
					}
					curval = q;
				}
			}
		} else {
			scanint();
			if (p < 2) {
				if (q == 91) {
					if (p == 0) {
						curval = multandadd(eqtb[l].getInt(), curval, 0, 2147483647);
					} else {
						curval = multandadd(eqtb[l].getInt(), curval, 0, 1073741823);
					}
				} else {
					curval = xovern(eqtb[l].getInt(), curval);
				}
			} else {
				s = eqtb[l].getrh();
				r = newspec(s);
				if (q == 91) {
					mem[r + 1].setInt(multandadd(mem[s + 1].getInt(), curval, 0, 1073741823));
					mem[r + 2].setInt(multandadd(mem[s + 2].getInt(), curval, 0, 1073741823));
					mem[r + 3].setInt(multandadd(mem[s + 3].getInt(), curval, 0, 1073741823));
				} else {
					mem[r + 1].setInt(xovern(mem[s + 1].getInt(), curval));
					mem[r + 2].setInt(xovern(mem[s + 2].getInt(), curval));
					mem[r + 3].setInt(xovern(mem[s + 3].getInt(), curval));
				}
				curval = r;
			}
		}
		if (aritherror) {
			{
				printnl(262);
				print(1207);
			}
			{
				helpptr = 2;
				helpline[1] = 1208;
				helpline[0] = 1209;
			}
			errorLogic.error();
			return /* lab10 */;
		}
		if (p < 2) {
			if ((a >= 4)) {
				geqworddefine(l, curval);
			} else {
				eqworddefine(l, curval);
			}
		} else {
			trapzeroglue();
			if ((a >= 4)) {
				geqdefine(l, 117, curval);
			} else {
				eqdefine(l, 117, curval);
			}
		}
	}

	void alteraux() {
		int c;
		if (curchr != Math.abs(curlist.modefield)) {
			errorLogic.reportillegalcase();
		} else {
			c = curchr;
			scanoptionalequals();
			if (c == 1) {
				scandimen(false, false, false);
				curlist.auxfield.setInt(curval);
			} else {
				scanint();
				if ((curval <= 0) || (curval > 32767)) {
					{
						printnl(262);
						print(1213);
					}
					{
						helpptr = 1;
						helpline[0] = 1214;
					}
					errorLogic.interror(curval);
				} else {
					curlist.auxfield.setlh(curval);
				}
			}
		}
	}

	void alterprevgraf() {
		int p;
		nest[nestptr].copy(curlist);
		p = nestptr;
		while (Math.abs(nest[p].modefield) != 1) {
			p = p - 1;
		}
		scanoptionalequals();
		scanint();
		if (curval < 0) {
			{
				printnl(262);
				print(955);
			}
			printEscapeSequence(532);
			{
				helpptr = 1;
				helpline[0] = 1215;
			}
			errorLogic.interror(curval);
		} else {
			nest[p].pgfield = curval;
			curlist.copy(nest[nestptr]);
		}
	}

	void alterpagesofar() {
		int c;
		c = curchr;
		scanoptionalequals();
		scandimen(false, false, false);
		pagesofar[c] = curval;
	}

	void alterinteger() {
		int c;
		c = curchr;
		scanoptionalequals();
		scanint();
		if (c == 0) {
			deadcycles = curval;
		} else {
			insertpenalties = curval;
		}
	}

	void alterboxdimen() {
		int c;
		int b;
		c = curchr;
		scaneightbitint();
		b = curval;
		scanoptionalequals();
		scandimen(false, false, false);
		if (eqtb[7978 + b].getrh() != 0) {
			mem[eqtb[7978 + b].getrh() + c].setInt(curval);
		}
	}

	void newfont(final int a) {
		int u;
		int s;
		int f;
		int t;
		int oldsetting;
		int flushablestring;
		getrtoken();
		u = curcs;
		if (u >= 514) {
			t = hash[u - 514].rh;
		} else if (u >= 257) {
			if (u == 513) {
				t = 1219;
			} else {
				t = u - 257;
			}
		} else {
			oldsetting = selector;
			selector = 21;
			print(1219);
			print(u - 1);
			selector = oldsetting;
			t = stringPool.makeString();
		}
		if ((a >= 4)) {
			geqdefine(u, 87, 0);
		} else {
			eqdefine(u, 87, 0);
		}
		scanoptionalequals();
		scanfilename();
		nameinprogress = true;
		if (scankeyword(1220)) {
			scandimen(false, false, false);
			s = curval;
			if ((s <= 0) || (s >= 134217728)) {
				{
					printnl(262);
					print(1222);
				}
				printFixed(s);
				print(1223);
				{
					helpptr = 2;
					helpline[1] = 1224;
					helpline[0] = 1225;
				}
				errorLogic.error();
				s = 10 * 65536;
			}
		} else if (scankeyword(1221)) {
			scanint();
			s = -curval;
			if ((curval <= 0) || (curval > 32768)) {
				{
					printnl(262);
					print(552);
				}
				{
					helpptr = 1;
					helpline[0] = 553;
				}
				errorLogic.interror(curval);
				s = -1000;
			}
		} else {
			s = -1000;
		}
		nameinprogress = false;
		lab50: while (true) {
			flushablestring = strptr - 1;
			for (f = 1; f <= fontptr; f++) {
				if (stringPool.getString(fontname[f]).equals(stringPool.getString(curname)) && stringPool.getString(fontarea[f]).equals(stringPool.getString(curarea))) {
					if (curname == flushablestring) {
						stringPool.unmakeString();
						curname = fontname[f];
					}
					if (s > 0) {
						if (s == fontsize[f]) {
							break lab50;
						}
					} else if (fontsize[f] == xnoverd(fontdsize[f], -s, 1000)) {
						break lab50;
					}
				}
			}
			f = readfontinfo(u, curname, curarea, s);
			break;
		}
		/* lab50: */eqtb[u].setrh(f);
		eqtb[6924 + f].copy(eqtb[u]);
		hash[6924 + f - 514].rh = t;
	}

	void prefixedcommand() {
		int a;
		int f;
		int j;
		int k;
		int p, q;
		int n;
		boolean e;
		a = 0;
		while (curcmd == 93) {
			if (!((a / curchr) % 2 == 1)) {
				a = a + curchr;
			}
			do {
				getxtoken();
			} while (!((curcmd != 10) && (curcmd != 0)));
			if (curcmd <= 70) {
				{
					printnl(262);
					print(1179);
				}
				printcmdchr(curcmd, curchr);
				printchar(39);
				{
					helpptr = 1;
					helpline[0] = 1180;
				}
				errorLogic.backerror();
				return /* lab10 */;
			}
		}
		if ((curcmd != 97) && (a % 4 != 0)) {
			{
				printnl(262);
				print(685);
			}
			printEscapeSequence(1171);
			print(1181);
			printEscapeSequence(1172);
			print(1182);
			printcmdchr(curcmd, curchr);
			printchar(39);
			{
				helpptr = 1;
				helpline[0] = 1183;
			}
			errorLogic.error();
		}
		if (eqtb[9606].getInt() != 0) {
			if (eqtb[9606].getInt() < 0) {
				if ((a >= 4)) {
					a = a - 4;
				}
			} else {
				if (!(a >= 4)) {
					a = a + 4;
				}
			}
		}
		lab30: while (true) {
			switch (curcmd) {
				case 87:
					if ((a >= 4)) {
						geqdefine(8234, 120, curchr);
					} else {
						eqdefine(8234, 120, curchr);
					}
					break;
				case 97: {
					if (((curchr) % 2 == 1) && !(a >= 4) && (eqtb[9606].getInt() >= 0)) {
						a = a + 4;
					}
					e = (curchr >= 2);
					getrtoken();
					p = curcs;
					q = scantoks(true, e);
					if ((a >= 4)) {
						geqdefine(p, 111 + (a % 4), defref);
					} else {
						eqdefine(p, 111 + (a % 4), defref);
					}
				}
					break;
				case 94: {
					n = curchr;
					getrtoken();
					p = curcs;
					if (n == 0) {
						do {
							gettoken();
						} while (!(curcmd != 10));
						if (curtok == 3133) {
							gettoken();
							if (curcmd == 10) {
								gettoken();
							}
						}
					} else {
						gettoken();
						q = curtok;
						gettoken();
						unreadToken();
						curtok = q;
						unreadToken();
					}
					if (curcmd >= 111) {
						mem[curchr].setlh(mem[curchr].getlh() + 1);
					}
					if ((a >= 4)) {
						geqdefine(p, curcmd, curchr);
					} else {
						eqdefine(p, curcmd, curchr);
					}
				}
					break;
				case 95: {
					n = curchr;
					getrtoken();
					p = curcs;
					if ((a >= 4)) {
						geqdefine(p, 0, 256);
					} else {
						eqdefine(p, 0, 256);
					}
					scanoptionalequals();
					switch (n) {
						case 0: {
							scancharnum();
							if ((a >= 4)) {
								geqdefine(p, 68, curval);
							} else {
								eqdefine(p, 68, curval);
							}
						}
							break;
						case 1: {
							scanfifteenbitint();
							if ((a >= 4)) {
								geqdefine(p, 69, curval);
							} else {
								eqdefine(p, 69, curval);
							}
						}
							break;
						default: {
							scaneightbitint();
							switch (n) {
								case 2:
									if ((a >= 4)) {
										geqdefine(p, 73, 9618 + curval);
									} else {
										eqdefine(p, 73, 9618 + curval);
									}
									break;
								case 3:
									if ((a >= 4)) {
										geqdefine(p, 74, 10151 + curval);
									} else {
										eqdefine(p, 74, 10151 + curval);
									}
									break;
								case 4:
									if ((a >= 4)) {
										geqdefine(p, 75, 7200 + curval);
									} else {
										eqdefine(p, 75, 7200 + curval);
									}
									break;
								case 5:
									if ((a >= 4)) {
										geqdefine(p, 76, 7456 + curval);
									} else {
										eqdefine(p, 76, 7456 + curval);
									}
									break;
								case 6:
									if ((a >= 4)) {
										geqdefine(p, 72, 7722 + curval);
									} else {
										eqdefine(p, 72, 7722 + curval);
									}
									break;
							}
						}
							break;
					}
				}
					break;
				case 96: {
					scanint();
					n = curval;
					if (!scankeyword(842)) {
						{
							printnl(262);
							print(1073);
						}
						{
							helpptr = 2;
							helpline[1] = 1200;
							helpline[0] = 1201;
						}
						errorLogic.error();
					}
					getrtoken();
					p = curcs;
					readtoks(n, p);
					if ((a >= 4)) {
						geqdefine(p, 111, curval);
					} else {
						eqdefine(p, 111, curval);
					}
				}
					break;
				case 71:
				case 72: {
					q = curcs;
					if (curcmd == 71) {
						scaneightbitint();
						p = 7722 + curval;
					} else {
						p = curchr;
					}
					scanoptionalequals();
					do {
						getxtoken();
					} while (!((curcmd != 10) && (curcmd != 0)));
					if (curcmd != 1) {
						if (curcmd == 71) {
							scaneightbitint();
							curcmd = 72;
							curchr = 7722 + curval;
						}
						if (curcmd == 72) {
							q = eqtb[curchr].getrh();
							if (q == 0) {
								if ((a >= 4)) {
									geqdefine(p, 101, 0);
								} else {
									eqdefine(p, 101, 0);
								}
							} else {
								mem[q].setlh(mem[q].getlh() + 1);
								if ((a >= 4)) {
									geqdefine(p, 111, q);
								} else {
									eqdefine(p, 111, q);
								}
							}
							break lab30;
						}
					}
					unreadToken();
					curcs = q;
					q = scantoks(false, false);
					if (mem[defref].getrh() == 0) {
						if ((a >= 4)) {
							geqdefine(p, 101, 0);
						} else {
							eqdefine(p, 101, 0);
						}
						{
							mem[defref].setrh(avail);
							avail = defref;
						}
					} else {
						if (p == 7713) {
							mem[q].setrh(allocateMemoryWord());
							q = mem[q].getrh();
							mem[q].setlh(637);
							q = allocateMemoryWord();
							mem[q].setlh(379);
							mem[q].setrh(mem[defref].getrh());
							mem[defref].setrh(q);
						}
						if ((a >= 4)) {
							geqdefine(p, 111, defref);
						} else {
							eqdefine(p, 111, defref);
						}
					}
				}
					break;
				case 73: {
					p = curchr;
					scanoptionalequals();
					scanint();
					if ((a >= 4)) {
						geqworddefine(p, curval);
					} else {
						eqworddefine(p, curval);
					}
				}
					break;
				case 74: {
					p = curchr;
					scanoptionalequals();
					scandimen(false, false, false);
					if ((a >= 4)) {
						geqworddefine(p, curval);
					} else {
						eqworddefine(p, curval);
					}
				}
					break;
				case 75:
				case 76: {
					p = curchr;
					n = curcmd;
					scanoptionalequals();
					if (n == 76) {
						scanglue(3);
					} else {
						scanglue(2);
					}
					trapzeroglue();
					if ((a >= 4)) {
						geqdefine(p, 117, curval);
					} else {
						eqdefine(p, 117, curval);
					}
				}
					break;
				case 85: {
					if (curchr == 8283) {
						n = 15;
					} else if (curchr == 9307) {
						n = 32768;
					} else if (curchr == 9051) {
						n = 32767;
					} else if (curchr == 9874) {
						n = 16777215;
					} else {
						n = 255;
					}
					p = curchr;
					scancharnum();
					p = p + curval;
					scanoptionalequals();
					scanint();
					if (((curval < 0) && (p < 9874)) || (curval > n)) {
						{
							printnl(262);
							print(1202);
						}
						printInt(curval);
						if (p < 9874) {
							print(1203);
						} else {
							print(1204);
						}
						printInt(n);
						{
							helpptr = 1;
							helpline[0] = 1205;
						}
						errorLogic.error();
						curval = 0;
					}
					if (p < 9307) {
						if ((a >= 4)) {
							geqdefine(p, 120, curval);
						} else {
							eqdefine(p, 120, curval);
						}
					} else if (p < 9874) {
						if ((a >= 4)) {
							geqdefine(p, 120, curval);
						} else {
							eqdefine(p, 120, curval);
						}
					} else if ((a >= 4)) {
						geqworddefine(p, curval);
					} else {
						eqworddefine(p, curval);
					}
				}
					break;
				case 86: {
					p = curchr;
					scanfourbitint();
					p = p + curval;
					scanoptionalequals();
					scanfontident();
					if ((a >= 4)) {
						geqdefine(p, 120, curval);
					} else {
						eqdefine(p, 120, curval);
					}
				}
					break;
				case 89:
				case 90:
				case 91:
				case 92:
					doregistercommand(a);
					break;
				case 98: {
					scaneightbitint();
					if ((a >= 4)) {
						n = 256 + curval;
					} else {
						n = curval;
					}
					scanoptionalequals();
					if (setboxallowed) {
						scanbox(1073741824 + n);
					} else {
						{
							printnl(262);
							print(680);
						}
						printEscapeSequence(536);
						{
							helpptr = 2;
							helpline[1] = 1211;
							helpline[0] = 1212;
						}
						errorLogic.error();
					}
				}
					break;
				case 79:
					alteraux();
					break;
				case 80:
					alterprevgraf();
					break;
				case 81:
					alterpagesofar();
					break;
				case 82:
					alterinteger();
					break;
				case 83:
					alterboxdimen();
					break;
				case 84: {
					scanoptionalequals();
					scanint();
					n = curval;
					if (n <= 0) {
						p = 0;
					} else {
						p = getnode(2 * n + 1);
						mem[p].setlh(n);
						for (j = 1; j <= n; j++) {
							scandimen(false, false, false);
							mem[p + 2 * j - 1].setInt(curval);
							scandimen(false, false, false);
							mem[p + 2 * j].setInt(curval);
						}
					}
					if ((a >= 4)) {
						geqdefine(7712, 118, p);
					} else {
						eqdefine(7712, 118, p);
					}
				}
					break;
				case 99:
					if (curchr == 1) {
						if (initex) {
							newpatterns();
							break lab30;
						} else {
							{
								printnl(262);
								print(1216);
							}
							helpptr = 0;
							errorLogic.error();
							do {
								gettoken();
							} while (!(curcmd == 2));
							return /* lab10 */;
						}
					} else {
						newhyphexceptions();
						break lab30;
					}
				case 77: {
					findfontdimen(true);
					k = curval;
					scanoptionalequals();
					scandimen(false, false, false);
					fontinfo[k].setInt(curval);
				}
					break;
				case 78: {
					n = curchr;
					scanfontident();
					f = curval;
					scanoptionalequals();
					scanint();
					if (n == 0) {
						hyphenchar[f] = curval;
					} else {
						skewchar[f] = curval;
					}
				}
					break;
				case 88:
					newfont(a);
					break;
				case 100:
					// TODO error: cannot change interaction level
					// should log this as an error but continue processing
					break;
				default:
					errorLogic.confusion(1178);
					break;
			}
			break;
		}
		/* lab30: */if (aftertoken != 0) {
			curtok = aftertoken;
			unreadToken();
			aftertoken = 0;
		}
	}

	void doassignments() {
		/* 10 */while (true) {
			do {
				getxtoken();
			} while (!((curcmd != 10) && (curcmd != 0)));
			if (curcmd <= 70) {
				return /* lab10 */;
			}
			setboxallowed = false;
			prefixedcommand();
			setboxallowed = true;
		}
	}

	void openorclosein() {
		int c;
		int n;
		c = curchr;
		scanfourbitint();
		n = curval;
		if (readopen[n] != 2) {
			readfile[n].close();
			readopen[n] = 2;
		}
		if (c != 0) {
			scanoptionalequals();
			scanfilename();
			if (curext == 338) {
				curext = 791;
			}
			packfilename(curname, curarea, curext);
			thisfile = new File(nameoffile);
			if (thisfile.exists()) {
				readfile[n] = Input.from(thisfile);
				readopen[n] = 1;
			}
		}
	}

	void issuemessage() {
		int oldsetting;
		int c;
		int s;
		c = curchr;
		mem[memtop - 12].setrh(scantoks(false, true));
		oldsetting = selector;
		selector = 21;
		tokenshow(defref);
		selector = oldsetting;
		flushlist(defref);
		s = stringPool.makeString();
		if (c == 0) {
			if ((termoffset > 0) || (fileoffset > 0)) {
				printchar(32);
			}
			print(s);
			termout.flush();
		} else {
			{
				printnl(262);
				print(338);
			}
			print(s);
			boolean useerrhelp = false;
			if (eqtb[7721].getrh() != 0) {
				useerrhelp = true;
			} else if (longhelpseen) {
				helpptr = 1;
				helpline[0] = 1232;
			} else {
				longhelpseen = true;
				helpptr = 4;
				helpline[3] = 1233;
				helpline[2] = 1234;
				helpline[1] = 1235;
				helpline[0] = 1236;
			}

			printchar(46);
			showcontext();
			selector = selector - 1;
			if (useerrhelp) {
				println();
				tokenshow(eqtb[7721].getrh());
			} else {
				while (helpptr > 0) {
					helpptr = helpptr - 1;
					printnl(helpline[helpptr]);
				}
			}
			selector = selector + 1;
			errorReporter.error("ERROR"); // TODO dirty: sets the "worst error level so far" and stops on 100+ errors
		}
		stringPool.unmakeString();
	}

	void shiftcase() {
		int b;
		int p;
		int t;
		int c;
		b = curchr;
		p = scantoks(false, false);
		p = mem[defref].getrh();
		while (p != 0) {
			t = mem[p].getlh();
			if (t < 4352) {
				c = t % 256;
				if (eqtb[b + c].getrh() != 0) {
					mem[p].setlh(t - c + eqtb[b + c].getrh());
				}
			}
			p = mem[p].getrh();
		}
		begintokenlist(mem[defref].getrh(), 3);
		{
			mem[defref].setrh(avail);
			avail = defref;
		}
	}

	void storefmtfile() {
		/* 41 42 31 32 */int j, k, l;
		int p, q;
		int x;
		final fourquarters w = new fourquarters();
		fmtfile = null;
		if (saveptr != 0) {
			printnl(262);
			print(1258);
			helpptr = 1;
			helpline[0] = 1259;
			errorLogic.error();
			errorReporter.fatal("");
		}
		selector = 21;
		print(1272);
		print(jobname);
		printchar(32);
		printInt(eqtb[9586].getInt() % 100);
		printchar(46);
		printInt(eqtb[9585].getInt());
		printchar(46);
		printInt(eqtb[9584].getInt());
		printchar(41);
		selector = 19;
		formatident = stringPool.makeString();
		packjobname(785);
		try (TexFileDataOutputStream fmtfile = new TexFileDataOutputStream(nameoffile)) {
			printnl(1274);
			print(makenamestring());
			stringPool.unmakeString();
			printnl(338);
			print(formatident);
			termout.flush();
			fmtfile.writeInt(270280812);
			fmtfile.writeInt(0);
			fmtfile.writeInt(memtop);
			fmtfile.writeInt(10406);
			fmtfile.writeInt(5437);
			fmtfile.writeInt(607);
			fmtfile.writeInt(poolptr);
			fmtfile.writeInt(strptr);
			for (k = 0; k <= strptr; k++) {
				fmtfile.writeInt(strstart[k]);
			}
			k = 0;
			while (k + 4 < poolptr) {
				w.b0 = strpool[k];
				w.b1 = strpool[k + 1];
				w.b2 = strpool[k + 2];
				w.b3 = strpool[k + 3];
				{
					fmtfile.writeByte(w.b0);
					fmtfile.writeByte(w.b1);
					fmtfile.writeByte(w.b2);
					fmtfile.writeByte(w.b3);
				}
				k = k + 4;
			}
			k = poolptr - 4;
			w.b0 = strpool[k];
			w.b1 = strpool[k + 1];
			w.b2 = strpool[k + 2];
			w.b3 = strpool[k + 3];
			{
				fmtfile.writeByte(w.b0);
				fmtfile.writeByte(w.b1);
				fmtfile.writeByte(w.b2);
				fmtfile.writeByte(w.b3);
			}
			println();
			printInt(strptr);
			print(1260);
			printInt(poolptr);
			termout.flush();
			sortavail();
			varused = 0;
			fmtfile.writeInt(lomemmax);
			fmtfile.writeInt(rover);
			p = 0;
			q = rover;
			x = 0;
			do {
				for (k = p; k <= q + 1; k++) {
					mem[k].memdump(fmtfile);
				}
				x = x + q + 2 - p;
				varused = varused + q - p;
				p = q + mem[q].getlh();
				q = mem[q + 1].getrh();
			} while (!(q == rover));
			varused = varused + lomemmax - p;
			dynused = memend + 1 - himemmin;
			for (k = p; k <= lomemmax; k++) {
				mem[k].memdump(fmtfile);
			}
			x = x + lomemmax + 1 - p;
			fmtfile.writeInt(himemmin);
			fmtfile.writeInt(avail);
			for (k = himemmin; k <= memend; k++) {
				mem[k].memdump(fmtfile);
			}
			x = x + memend + 1 - himemmin;
			p = avail;
			while (p != 0) {
				dynused = dynused - 1;
				p = mem[p].getrh();
			}
			fmtfile.writeInt(varused);
			fmtfile.writeInt(dynused);
			println();
			printInt(x);
			print(1261);
			printInt(varused);
			printchar(38);
			printInt(dynused);
			termout.flush();
			k = 1;
			do {
				j = k;
				lab31: while (true) {
					lab41: while (true) {
						while (j < 9562) {
							if ((eqtb[j].getrh() == eqtb[j + 1].getrh()) && (eqtb[j].getb0() == eqtb[j + 1].getb0()) && (eqtb[j].getb1() == eqtb[j + 1].getb1())) {
								break lab41;
							}
							j = j + 1;
						}
						l = 9563;
						break lab31;
					}
					/* lab41: */j = j + 1;
					l = j;
					while (j < 9562) {
						if ((eqtb[j].getrh() != eqtb[j + 1].getrh()) || (eqtb[j].getb0() != eqtb[j + 1].getb0()) || (eqtb[j].getb1() != eqtb[j + 1].getb1())) {
							break lab31;
						}
						j = j + 1;
					}
					break;
				}
				/* lab31: */fmtfile.writeInt(l - k);
				while (k < l) {
					eqtb[k].memdump(fmtfile);
					k = k + 1;
				}
				k = j + 1;
				fmtfile.writeInt(k - l);
			} while (!(k == 9563));
			do {
				j = k;
				lab32: while (true) {
					lab42: while (true) {
						while (j < 10406) {
							if (eqtb[j].getInt() == eqtb[j + 1].getInt()) {
								break lab42;
							}
							j = j + 1;
						}
						l = 10407;
						break lab32;
					}
					/* lab42: */j = j + 1;
					l = j;
					while (j < 10406) {
						if (eqtb[j].getInt() != eqtb[j + 1].getInt()) {
							break lab32;
						}
						j = j + 1;
					}
					break;
				}
				/* lab32: */fmtfile.writeInt(l - k);
				while (k < l) {
					eqtb[k].memdump(fmtfile);
					k = k + 1;
				}
				k = j + 1;
				fmtfile.writeInt(k - l);
			} while (!(k > 10406));
			fmtfile.writeInt(parloc);
			fmtfile.writeInt(writeloc);
			fmtfile.writeInt(hashused);
			cscount = 6913 - hashused;
			for (p = 514; p <= hashused; p++) {
				if (hash[p - 514].rh != 0) {
					fmtfile.writeInt(p);
					{
						fmtfile.writeShort(hash[p - 514].lh);
						fmtfile.writeShort(hash[p - 514].rh);
					}
					cscount = cscount + 1;
				}
			}
			for (p = hashused + 1; p <= 7180; p++) {
				fmtfile.writeShort(hash[p - 514].lh);
				fmtfile.writeShort(hash[p - 514].rh);
			}
			fmtfile.writeInt(cscount);
			println();
			printInt(cscount);
			print(1262);
			termout.flush();
			fmtfile.writeInt(fmemptr);
			for (k = 0; k <= fmemptr - 1; k++) {
				fontinfo[k].memdump(fmtfile);
			}
			fmtfile.writeInt(fontptr);
			for (k = 0; k <= fontptr; k++) {
				{
					fmtfile.writeByte(fontcheck[k].b0);
					fmtfile.writeByte(fontcheck[k].b1);
					fmtfile.writeByte(fontcheck[k].b2);
					fmtfile.writeByte(fontcheck[k].b3);
				}
				fmtfile.writeInt(fontsize[k]);
				fmtfile.writeInt(fontdsize[k]);
				fmtfile.writeInt(fontparams[k]);
				fmtfile.writeInt(hyphenchar[k]);
				fmtfile.writeInt(skewchar[k]);
				fmtfile.writeInt(fontname[k]);
				fmtfile.writeInt(fontarea[k]);
				fmtfile.writeInt(fontbc[k]);
				fmtfile.writeInt(fontec[k]);
				fmtfile.writeInt(charbase[k]);
				fmtfile.writeInt(widthbase[k]);
				fmtfile.writeInt(heightbase[k]);
				fmtfile.writeInt(depthbase[k]);
				fmtfile.writeInt(italicbase[k]);
				fmtfile.writeInt(ligkernbase[k]);
				fmtfile.writeInt(kernbase[k]);
				fmtfile.writeInt(extenbase[k]);
				fmtfile.writeInt(parambase[k]);
				fmtfile.writeInt(fontglue[k]);
				fmtfile.writeInt(bcharlabel[k]);
				fmtfile.writeInt(fontbchar[k]);
				fmtfile.writeInt(fontfalsebchar[k]);
				printnl(1265);
				printEscapeSequence(hash[6924 + k - 514].rh);
				printchar(61);
				printfilename(fontname[k], fontarea[k], 338);
				if (fontsize[k] != fontdsize[k]) {
					print(741);
					printFixed(fontsize[k]);
					print(397);
				}
				termout.flush();
			}
			println();
			printInt(fmemptr - 7);
			print(1263);
			printInt(fontptr - 0);
			print(1264);
			if (fontptr != 1) {
				printchar(115);
			}
			termout.flush();
			fmtfile.writeInt(hyphcount);
			for (k = 0; k <= 607; k++) {
				if (hyphword[k] != 0) {
					fmtfile.writeInt(k);
					fmtfile.writeInt(hyphword[k]);
					fmtfile.writeInt(hyphlist[k]);
				}
			}
			println();
			printInt(hyphcount);
			print(1266);
			if (hyphcount != 1) {
				printchar(115);
			}
			if (trienotready) {
				inittrie();
			}
			fmtfile.writeInt(triemax);
			for (k = 0; k <= triemax; k++) {
				fmtfile.writeShort(trie[k].lh);
				fmtfile.writeShort(trie[k].rh);
			}
			fmtfile.writeInt(trieopptr);
			for (k = 1; k <= trieopptr; k++) {
				fmtfile.writeInt(hyfdistance[k]);
				fmtfile.writeInt(hyfnum[k]);
				fmtfile.writeInt(hyfnext[k]);
			}
			printnl(1267);
			printInt(triemax);
			print(1268);
			printInt(trieopptr);
			print(1269);
			if (trieopptr != 1) {
				printchar(115);
			}
			print(1270);
			printInt(751);
			for (k = 255; k >= 0; k--) {
				if (trieused[k] > 0) {
					printnl(800);
					printInt(trieused[k]);
					print(1271);
					printInt(k);
					fmtfile.writeInt(k);
					fmtfile.writeInt(trieused[k]);
				}
			}
			termout.flush();
			fmtfile.writeInt(1);
			fmtfile.writeInt(formatident);
			fmtfile.writeInt(69069);
			eqtb[9594].setInt(0);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	void newwhatsit(final int s, final int w) {
		int p;
		p = getnode(w);
		mem[p].setb0(8);
		mem[p].setb1(s);
		mem[curlist.tailfield].setrh(p);
		curlist.tailfield = p;
	}

	void newwritewhatsit(final int w) {
		newwhatsit(curchr, w);
		if (w != 2) {
			scanfourbitint();
		} else {
			scanint();
			if (curval < 0) {
				curval = 17;
			} else if (curval > 15) {
				curval = 16;
			}
		}
		mem[curlist.tailfield + 1].setlh(curval);
	}

	void doextension() {
		int k;
		int p;
		switch (curchr) {
			case 0: {
				newwritewhatsit(3);
				scanoptionalequals();
				scanfilename();
				mem[curlist.tailfield + 1].setrh(curname);
				mem[curlist.tailfield + 2].setlh(curarea);
				mem[curlist.tailfield + 2].setrh(curext);
			}
				break;
			case 1: {
				k = curcs;
				newwritewhatsit(2);
				curcs = k;
				p = scantoks(false, false);
				mem[curlist.tailfield + 1].setrh(defref);
			}
				break;
			case 2: {
				newwritewhatsit(2);
				mem[curlist.tailfield + 1].setrh(0);
			}
				break;
			case 3: {
				newwhatsit(3, 2);
				mem[curlist.tailfield + 1].setlh(0);
				p = scantoks(false, true);
				mem[curlist.tailfield + 1].setrh(defref);
			}
				break;
			case 4: {
				getxtoken();
				if ((curcmd == 59) && (curchr <= 2)) {
					p = curlist.tailfield;
					doextension();
					outwhat(curlist.tailfield);
					flushnodelist(curlist.tailfield);
					curlist.tailfield = p;
					mem[p].setrh(0);
				} else {
					unreadToken();
				}
			}
				break;
			case 5:
				if (Math.abs(curlist.modefield) != 102) {
					errorLogic.reportillegalcase();
				} else {
					newwhatsit(4, 2);
					scanint();
					if (curval <= 0) {
						curlist.auxfield.setrh(0);
					} else if (curval > 255) {
						curlist.auxfield.setrh(0);
					} else {
						curlist.auxfield.setrh(curval);
					}
					mem[curlist.tailfield + 1].setrh(curlist.auxfield.getrh());
					mem[curlist.tailfield + 1].setb0(normmin(eqtb[9614].getInt()));
					mem[curlist.tailfield + 1].setb1(normmin(eqtb[9615].getInt()));
				}
				break;
			default:
				errorLogic.confusion(1291);
				break;
		}
	}

	void fixlanguage() {
		int l;
		if (eqtb[9613].getInt() <= 0) {
			l = 0;
		} else if (eqtb[9613].getInt() > 255) {
			l = 0;
		} else {
			l = eqtb[9613].getInt();
		}
		if (l != curlist.auxfield.getrh()) {
			newwhatsit(4, 2);
			mem[curlist.tailfield + 1].setrh(l);
			curlist.auxfield.setrh(l);
			mem[curlist.tailfield + 1].setb0(normmin(eqtb[9614].getInt()));
			mem[curlist.tailfield + 1].setb1(normmin(eqtb[9615].getInt()));
		}
	}

	void handlerightbrace() {
		int p, q;
		int d;
		int f;
		switch (curgroup) {
			case 1:
				unsave();
				break;
			case 0: {
				{
					printnl(262);
					print(1044);
				}
				{
					helpptr = 2;
					helpline[1] = 1045;
					helpline[0] = 1046;
				}
				errorLogic.error();
			}
				break;
			case 14:
			case 15:
			case 16: {
				String message = "!Extra }, or forgotten ";
				switch (curgroup) {
					case 14:
						message += getCurrentEscapeCharacter() + "endgroup";
						break;
					case 15:
						message += '$';
						break;
					case 16:
						message += getCurrentEscapeCharacter() + "right";
						break;
				}
				errorLogic.error(message, "I've deleted a group-closing symbol because it seems to be", "spurious, as in `$x}$'. But perhaps the } is legitimate and", "you forgot something else, as in `\\hbox{$x}'. In such cases", "the way to recover is to insert both the forgotten and the", "deleted material, e.g., by typing `I$}'.");
				alignstate = alignstate + 1;
				break;
			}
			case 2:
				Package(0);
				break;
			case 3: {
				adjusttail = memtop - 5;
				Package(0);
			}
				break;
			case 4: {
				endgraf();
				Package(0);
			}
				break;
			case 5: {
				endgraf();
				Package(4);
			}
				break;
			case 11: {
				endgraf();
				q = eqtb[7192].getrh();
				mem[q].setrh(mem[q].getrh() + 1);
				d = eqtb[10136].getInt();
				f = eqtb[9605].getInt();
				unsave();
				saveptr = saveptr - 1;
				p = vpackage(mem[curlist.headfield].getrh(), 0, 1, 1073741823);
				popnest();
				if (savestack[saveptr + 0].getInt() < 255) {
					{
						mem[curlist.tailfield].setrh(getnode(5));
						curlist.tailfield = mem[curlist.tailfield].getrh();
					}
					mem[curlist.tailfield].setb0(3);
					mem[curlist.tailfield].setb1(savestack[saveptr + 0].getInt());
					mem[curlist.tailfield + 3].setInt(mem[p + 3].getInt() + mem[p + 2].getInt());
					mem[curlist.tailfield + 4].setlh(mem[p + 5].getrh());
					mem[curlist.tailfield + 4].setrh(q);
					mem[curlist.tailfield + 2].setInt(d);
					mem[curlist.tailfield + 1].setInt(f);
				} else {
					{
						mem[curlist.tailfield].setrh(getnode(2));
						curlist.tailfield = mem[curlist.tailfield].getrh();
					}
					mem[curlist.tailfield].setb0(5);
					mem[curlist.tailfield].setb1(0);
					mem[curlist.tailfield + 1].setInt(mem[p + 5].getrh());
					deleteglueref(q);
				}
				freenode(p, 7);
				if (nestptr == 0) {
					buildpage();
				}
			}
				break;
			case 8: {
				if ((curinput.getLoc() != 0) || ((curinput.getIndex() != 6) && (curinput.getIndex() != 3))) {
					{
						printnl(262);
						print(1010);
					}
					{
						helpptr = 2;
						helpline[1] = 1011;
						helpline[0] = 1012;
					}
					errorLogic.error();
					do {
						gettoken();
					} while (!(curinput.getLoc() == 0));
				}
				endtokenlist();
				endgraf();
				unsave();
				outputactive = false;
				insertpenalties = 0;
				if (eqtb[8233].getrh() != 0) {
					{
						printnl(262);
						print(1013);
					}
					printEscapeSequence(409);
					printInt(255);
					{
						helpptr = 3;
						helpline[2] = 1014;
						helpline[1] = 1015;
						helpline[0] = 1016;
					}
					boxerror(255);
				}
				if (curlist.tailfield != curlist.headfield) {
					mem[pagetail].setrh(mem[curlist.headfield].getrh());
					pagetail = curlist.tailfield;
				}
				if (mem[memtop - 2].getrh() != 0) {
					if (mem[memtop - 1].getrh() == 0) {
						nest[0].tailfield = pagetail;
					}
					mem[pagetail].setrh(mem[memtop - 1].getrh());
					mem[memtop - 1].setrh(mem[memtop - 2].getrh());
					mem[memtop - 2].setrh(0);
					pagetail = memtop - 2;
				}
				popnest();
				buildpage();
			}
				break;
			case 10:
				builddiscretionary();
				break;
			case 6: {
				unreadToken();
				printnl(262);
				print(625);
				printEscapeSequence(899);
				print(626);
				helpptr = 1;
				helpline[0] = 1125;
				insertToken(11010);
				errorLogic.error();
				break;
			}
			case 7: {
				endgraf();
				unsave();
				alignpeek();
			}
				break;
			case 12: {
				endgraf();
				unsave();
				saveptr = saveptr - 2;
				p = vpackage(mem[curlist.headfield].getrh(), savestack[saveptr + 1].getInt(), savestack[saveptr + 0].getInt(), 1073741823);
				popnest();
				{
					mem[curlist.tailfield].setrh(newnoad());
					curlist.tailfield = mem[curlist.tailfield].getrh();
				}
				mem[curlist.tailfield].setb0(29);
				mem[curlist.tailfield + 1].setrh(2);
				mem[curlist.tailfield + 1].setlh(p);
			}
				break;
			case 13:
				buildchoices();
				break;
			case 9: {
				unsave();
				saveptr = saveptr - 1;
				mem[savestack[saveptr + 0].getInt()].setrh(3);
				p = finmlist(0);
				mem[savestack[saveptr + 0].getInt()].setlh(p);
				if (p != 0) {
					if (mem[p].getrh() == 0) {
						if (mem[p].getb0() == 16) {
							if (mem[p + 3].getrh() == 0) {
								if (mem[p + 2].getrh() == 0) {
									mem[savestack[saveptr + 0].getInt()].sethh(mem[p + 1].hh());
									freenode(p, 4);
								}
							}
						} else if (mem[p].getb0() == 28) {
							if (savestack[saveptr + 0].getInt() == curlist.tailfield + 1) {
								if (mem[curlist.tailfield].getb0() == 16) {
									q = curlist.headfield;
									while (mem[q].getrh() != curlist.tailfield) {
										q = mem[q].getrh();
									}
									mem[q].setrh(p);
									freenode(curlist.tailfield, 4);
									curlist.tailfield = p;
								}
							}
						}
					}
				}
			}
				break;
			default:
				errorLogic.confusion(1047);
				break;
		}
	}

	void maincontrol() {
		/* 60 21 120 10 */int t;
		int jcase;
		if (eqtb[7719].getrh() != 0) {
			begintokenlist(eqtb[7719].getrh(), 12);
		}
		lab60: while (true) {
			;
			getxtoken();
			lab21: while (true) {
				if (panicking) {
					checkmem(false);
				}
				if (eqtb[9599].getInt() > 0) {
					showcurcmdchr();
				}
				lab120: while (true) {
					lab70: while (true) {
						switch (Math.abs(curlist.modefield) + curcmd) {
							case 113:
							case 114:
							case 170:
								break lab70;
							case 118: {
								scancharnum();
								curchr = curval;
								break lab70;
							}
							case 167: {
								getxtoken();
								if ((curcmd == 11) || (curcmd == 12) || (curcmd == 68) || (curcmd == 16)) {
									cancelboundary = true;
								}
								continue lab21;
							}
							case 112:
								if (curlist.auxfield.getlh() == 1000) {
									break lab120;
								} else {
									appspace();
								}
								break;
							case 166:
							case 267:
								break lab120;
							case 1:
							case 102:
							case 203:
							case 11:
							case 213:
							case 268:
								;
								break;
							case 40:
							case 141:
							case 242: {
								do {
									getxtoken();
								} while (!(curcmd != 10));
								continue lab21;
							}
							case 15:
								if (itsallover()) {
									return /* lab10 */;
								}
								break;
							case 23:
							case 123:
							case 224:
							case 71:
							case 172:
							case 273:
							case 39:
							case 45:
							case 49:
							case 150:
							case 7:
							case 108:
							case 209:
								errorLogic.reportillegalcase();
								break;
							case 8:
							case 109:
							case 9:
							case 110:
							case 18:
							case 119:
							case 70:
							case 171:
							case 51:
							case 152:
							case 16:
							case 117:
							case 50:
							case 151:
							case 53:
							case 154:
							case 67:
							case 168:
							case 54:
							case 155:
							case 55:
							case 156:
							case 57:
							case 158:
							case 56:
							case 157:
							case 31:
							case 132:
							case 52:
							case 153:
							case 29:
							case 130:
							case 47:
							case 148:
							case 212:
							case 216:
							case 217:
							case 230:
							case 227:
							case 236:
							case 239:
								unreadToken();
								insertToken(804);
								errorLogic.error("!Missing $ inserted", "I've inserted a begin-math/end-math symbol since I think", "you left one out. Proceed, with fingers crossed.");
								break;
							case 37:
							case 137:
							case 238: {
								{
									mem[curlist.tailfield].setrh(scanrulespec());
									curlist.tailfield = mem[curlist.tailfield].getrh();
								}
								if (Math.abs(curlist.modefield) == 1) {
									curlist.auxfield.setInt(-65536000);
								} else if (Math.abs(curlist.modefield) == 102) {
									curlist.auxfield.setlh(1000);
								}
							}
								break;
							case 28:
							case 128:
							case 229:
							case 231:
								appendglue();
								break;
							case 30:
							case 131:
							case 232:
							case 233:
								appendkern();
								break;
							case 2:
							case 103:
								newsavelevel(1);
								break;
							case 62:
							case 163:
							case 264:
								newsavelevel(14);
								break;
							case 63:
							case 164:
							case 265:
								if (curgroup == 14) {
									unsave();
								} else {
									offsave();
								}
								break;
							case 3:
							case 104:
							case 205:
								handlerightbrace();
								break;
							case 22:
							case 124:
							case 225: {
								t = curchr;
								scandimen(false, false, false);
								if (t == 0) {
									scanbox(curval);
								} else {
									scanbox(-curval);
								}
							}
								break;
							case 32:
							case 133:
							case 234:
								scanbox(1073742237 + curchr);
								break;
							case 21:
							case 122:
							case 223:
								beginbox(0);
								break;
							case 44:
								newgraf(curchr > 0);
								break;
							case 12:
							case 13:
							case 17:
							case 69:
							case 4:
							case 24:
							case 36:
							case 46:
							case 48:
							case 27:
							case 34:
							case 65:
							case 66: {
								unreadToken();
								newgraf(true);
							}
								break;
							case 145:
							case 246:
								indentinhmode();
								break;
							case 14: {
								normalparagraph();
								if (curlist.modefield > 0) {
									buildpage();
								}
							}
								break;
							case 115: {
								if (alignstate < 0) {
									offsave();
								}
								endgraf();
								if (curlist.modefield == 1) {
									buildpage();
								}
							}
								break;
							case 116:
							case 129:
							case 138:
							case 126:
							case 134:
								headforvmode();
								break;
							case 38:
							case 139:
							case 240:
							case 140:
							case 241:
								begininsertoradjust();
								break;
							case 19:
							case 120:
							case 221:
								makemark();
								break;
							case 43:
							case 144:
							case 245:
								appendpenalty();
								break;
							case 26:
							case 127:
							case 228:
								deletelast();
								break;
							case 25:
							case 125:
							case 226:
								unpackage();
								break;
							case 146:
								appenditaliccorrection();
								break;
							case 247: {
								mem[curlist.tailfield].setrh(newkern(0));
								curlist.tailfield = mem[curlist.tailfield].getrh();
							}
								break;
							case 149:
							case 250:
								appenddiscretionary();
								break;
							case 147:
								makeaccent();
								break;
							case 6:
							case 107:
							case 208:
							case 5:
							case 106:
							case 207:
								errorLogic.alignError();
								break;
							case 35:
							case 136:
							case 237:
								errorLogic.error("!Misplaced " + getCurrentEscapeCharacter() + "noalign", "I expect to see \\noalign only after the \\cr of", "an alignment. Proceed, and I'll ignore this case.");
								break;
							case 64:
							case 165:
							case 266:
								errorLogic.error("!Misplaced " + getCurrentEscapeCharacter() + "omit", "I expect to see \\omit only after tab marks or the \\cr of", "an alignment. Proceed, and I'll ignore this case.");
								break;
							case 33:
							case 135:
								initalign();
								break;
							case 235:
								if (privileged()) {
									if (curgroup == 15) {
										initalign();
									} else {
										offsave();
									}
								}
								break;
							case 10:
							case 111:
								doendv();
								break;
							case 68:
							case 169:
							case 270:
								errorLogic.error("!Extra " + getCurrentEscapeCharacter() + "endcsname", "I'm ignoring this, since I wasn't doing a \\csname.");
								break;
							case 105:
								initmath();
								break;
							case 251:
								if (privileged()) {
									if (curgroup == 15) {
										starteqno();
									} else {
										offsave();
									}
								}
								break;
							case 204: {
								{
									mem[curlist.tailfield].setrh(newnoad());
									curlist.tailfield = mem[curlist.tailfield].getrh();
								}
								unreadToken();
								scanmath(curlist.tailfield + 1);
							}
								break;
							case 214:
							case 215:
							case 271:
								setmathchar(eqtb[9307 + curchr].getrh());
								break;
							case 219: {
								scancharnum();
								curchr = curval;
								setmathchar(eqtb[9307 + curchr].getrh());
							}
								break;
							case 220: {
								scanfifteenbitint();
								setmathchar(curval);
							}
								break;
							case 272:
								setmathchar(curchr);
								break;
							case 218: {
								scantwentysevenbitint();
								setmathchar(curval / 4096);
							}
								break;
							case 253: {
								{
									mem[curlist.tailfield].setrh(newnoad());
									curlist.tailfield = mem[curlist.tailfield].getrh();
								}
								mem[curlist.tailfield].setb0(curchr);
								scanmath(curlist.tailfield + 1);
							}
								break;
							case 254:
								mathlimitswitch();
								break;
							case 269:
								mathradical();
								break;
							case 248:
							case 249:
								mathac();
								break;
							case 259: {
								scanspec(12, false);
								normalparagraph();
								pushnest();
								curlist.modefield = -1;
								curlist.auxfield.setInt(-65536000);
								if (eqtb[7718].getrh() != 0) {
									begintokenlist(eqtb[7718].getrh(), 11);
								}
							}
								break;
							case 256: {
								mem[curlist.tailfield].setrh(newstyle(curchr));
								curlist.tailfield = mem[curlist.tailfield].getrh();
							}
								break;
							case 258: {
								{
									mem[curlist.tailfield].setrh(newglue(0));
									curlist.tailfield = mem[curlist.tailfield].getrh();
								}
								mem[curlist.tailfield].setb1(98);
							}
								break;
							case 257:
								appendchoices();
								break;
							case 211:
							case 210:
								subsup();
								break;
							case 255:
								mathfraction();
								break;
							case 252:
								mathleftright();
								break;
							case 206:
								if (curgroup == 15) {
									aftermath();
								} else {
									offsave();
								}
								break;
							case 72:
							case 173:
							case 274:
							case 73:
							case 174:
							case 275:
							case 74:
							case 175:
							case 276:
							case 75:
							case 176:
							case 277:
							case 76:
							case 177:
							case 278:
							case 77:
							case 178:
							case 279:
							case 78:
							case 179:
							case 280:
							case 79:
							case 180:
							case 281:
							case 80:
							case 181:
							case 282:
							case 81:
							case 182:
							case 283:
							case 82:
							case 183:
							case 284:
							case 83:
							case 184:
							case 285:
							case 84:
							case 185:
							case 286:
							case 85:
							case 186:
							case 287:
							case 86:
							case 187:
							case 288:
							case 87:
							case 188:
							case 289:
							case 88:
							case 189:
							case 290:
							case 89:
							case 190:
							case 291:
							case 90:
							case 191:
							case 292:
							case 91:
							case 192:
							case 293:
							case 92:
							case 193:
							case 294:
							case 93:
							case 194:
							case 295:
							case 94:
							case 195:
							case 296:
							case 95:
							case 196:
							case 297:
							case 96:
							case 197:
							case 298:
							case 97:
							case 198:
							case 299:
							case 98:
							case 199:
							case 300:
							case 99:
							case 200:
							case 301:
							case 100:
							case 201:
							case 302:
							case 101:
							case 202:
							case 303:
								prefixedcommand();
								break;
							case 41:
							case 142:
							case 243: {
								gettoken();
								aftertoken = curtok;
							}
								break;
							case 42:
							case 143:
							case 244: {
								gettoken();
								saveforafter(curtok);
							}
								break;
							case 61:
							case 162:
							case 263:
								openorclosein();
								break;
							case 59:
							case 160:
							case 261:
								issuemessage();
								break;
							case 58:
							case 159:
							case 260:
								shiftcase();
								break;
							case 20:
							case 121:
							case 222:
								showwhatever();
								break;
							case 60:
							case 161:
							case 262:
								doextension();
								break;
						}
						continue lab60;
					}
					/* lab70: */mains = eqtb[9051 + curchr].getrh();
					if (mains == 1000) {
						curlist.auxfield.setlh(1000);
					} else if (mains < 1000) {
						if (mains > 0) {
							curlist.auxfield.setlh(mains);
						}
					} else if (curlist.auxfield.getlh() < 1000) {
						curlist.auxfield.setlh(1000);
					} else {
						curlist.auxfield.setlh(mains);
					}
					mainf = eqtb[8234].getrh();
					bchar = fontbchar[mainf];
					falsebchar = fontfalsebchar[mainf];
					if (curlist.modefield > 0) {
						if (eqtb[9613].getInt() != curlist.auxfield.getrh()) {
							fixlanguage();
						}
					}
					{
						ligstack = avail;
						if (ligstack == 0) {
							ligstack = allocateMemoryWord();
						} else {
							avail = mem[ligstack].getrh();
							mem[ligstack].setrh(0);
						}
					}
					mem[ligstack].setb0(mainf);
					curl = curchr;
					mem[ligstack].setb1(curl);
					curq = curlist.tailfield;
					if (cancelboundary) {
						cancelboundary = false;
						maink = 0;
					} else {
						maink = bcharlabel[mainf];
					}
					if (maink == 0) {
						jcase = 92;
					} else {
						curr = curl;
						curl = 256;
						jcase = 111;
					}
					lab22: while (true) {
						switch (jcase) {
							case 80: {
								if (curl < 256) {
									if (mem[curlist.tailfield].getb1() == hyphenchar[mainf]) {
										if (mem[curq].getrh() > 0) {
											insdisc = true;
										}
									}
									if (ligaturepresent) {
										mainp = newligature(mainf, curl, mem[curq].getrh());
										if (lfthit) {
											mem[mainp].setb1(2);
											lfthit = false;
										}
										if (rthit) {
											if (ligstack == 0) {
												mem[mainp].setb1(mem[mainp].getb1() + 1);
												rthit = false;
											}
										}
										mem[curq].setrh(mainp);
										curlist.tailfield = mainp;
										ligaturepresent = false;
									}
									if (insdisc) {
										insdisc = false;
										if (curlist.modefield > 0) {
											mem[curlist.tailfield].setrh(newdisc());
											curlist.tailfield = mem[curlist.tailfield].getrh();
										}
									}
								}
							}
							case 90: {
								if (ligstack == 0) {
									continue lab21;
								}
								curq = curlist.tailfield;
								curl = mem[ligstack].getb1();
							}
							case 91: {
								if (!(ligstack >= himemmin)) {
									jcase = 95;
									continue lab22;
								}
							}
							case 92: {
								if ((curchr < fontbc[mainf]) || (curchr > fontec[mainf])) {
									errorLogic.charwarning(mainf, curchr);
									mem[ligstack].setrh(avail);
									avail = ligstack;
									continue lab60;
								}
								maini.copy(fontinfo[charbase[mainf] + curl].qqqq());
								if (!(maini.b0 > 0)) {
									errorLogic.charwarning(mainf, curchr);
									mem[ligstack].setrh(avail);
									avail = ligstack;
									continue lab60;
								}
								mem[curlist.tailfield].setrh(ligstack);
								curlist.tailfield = mem[curlist.tailfield].getrh();
							}
							case 100: {
								getnext();
								if ((curcmd == 11) || (curcmd == 12) || (curcmd == 68)) {
									jcase = 101;
									continue lab22;
								}
								xtoken();
								if ((curcmd == 11) || (curcmd == 12) || (curcmd == 68)) {
									jcase = 101;
									continue lab22;
								}
								if (curcmd == 16) {
									scancharnum();
									curchr = curval;
									{
										jcase = 101;
										continue lab22;
									}
								}
								if (curcmd == 65) {
									bchar = 256;
								}
								curr = bchar;
								ligstack = 0;
								{
									jcase = 110;
									continue lab22;
								}
							}
							case 101: {
								mains = eqtb[9051 + curchr].getrh();
								if (mains == 1000) {
									curlist.auxfield.setlh(1000);
								} else if (mains < 1000) {
									if (mains > 0) {
										curlist.auxfield.setlh(mains);
									}
								} else if (curlist.auxfield.getlh() < 1000) {
									curlist.auxfield.setlh(1000);
								} else {
									curlist.auxfield.setlh(mains);
								}
								{
									ligstack = avail;
									if (ligstack == 0) {
										ligstack = allocateMemoryWord();
									} else {
										avail = mem[ligstack].getrh();
										mem[ligstack].setrh(0);
									}
								}
								mem[ligstack].setb0(mainf);
								curr = curchr;
								mem[ligstack].setb1(curr);
								if (curr == falsebchar) {
									curr = 256;
								}
							}
							case 110: {
								if (((maini.b2) % 4) != 1) {
									jcase = 80;
									continue lab22;
								}
								maink = ligkernbase[mainf] + maini.b3;
								mainj.copy(fontinfo[maink].qqqq());
								if (mainj.b0 <= 128) {
									jcase = 112;
									continue lab22;
								}
								maink = ligkernbase[mainf] + 256 * mainj.b2 + mainj.b3 + 32768 - 256 * (128);
							}
							case 111: {
								mainj.copy(fontinfo[maink].qqqq());
							}
							case 112: {
								if (mainj.b1 == curr) {
									if (mainj.b0 <= 128) {
										if (mainj.b2 >= 128) {
											if (curl < 256) {
												if (mem[curlist.tailfield].getb1() == hyphenchar[mainf]) {
													if (mem[curq].getrh() > 0) {
														insdisc = true;
													}
												}
												if (ligaturepresent) {
													mainp = newligature(mainf, curl, mem[curq].getrh());
													if (lfthit) {
														mem[mainp].setb1(2);
														lfthit = false;
													}
													if (rthit) {
														if (ligstack == 0) {
															mem[mainp].setb1(mem[mainp].getb1() + 1);
															rthit = false;
														}
													}
													mem[curq].setrh(mainp);
													curlist.tailfield = mainp;
													ligaturepresent = false;
												}
												if (insdisc) {
													insdisc = false;
													if (curlist.modefield > 0) {
														mem[curlist.tailfield].setrh(newdisc());
														curlist.tailfield = mem[curlist.tailfield].getrh();
													}
												}
											}
											{
												mem[curlist.tailfield].setrh(newkern(fontinfo[kernbase[mainf] + 256 * mainj.b2 + mainj.b3].getInt()));
												curlist.tailfield = mem[curlist.tailfield].getrh();
											}
											{
												jcase = 90;
												continue lab22;
											}
										}
										if (curl == 256) {
											lfthit = true;
										} else if (ligstack == 0) {
											rthit = true;
										}
										switch (mainj.b2) {
											case 1:
											case 5: {
												curl = mainj.b3;
												maini.copy(fontinfo[charbase[mainf] + curl].qqqq());
												ligaturepresent = true;
											}
												break;
											case 2:
											case 6: {
												curr = mainj.b3;
												if (ligstack == 0) {
													ligstack = newligitem(curr);
													bchar = 256;
												} else if ((ligstack >= himemmin)) {
													mainp = ligstack;
													ligstack = newligitem(curr);
													mem[ligstack + 1].setrh(mainp);
												} else {
													mem[ligstack].setb1(curr);
												}
											}
												break;
											case 3: {
												curr = mainj.b3;
												mainp = ligstack;
												ligstack = newligitem(curr);
												mem[ligstack].setrh(mainp);
											}
												break;
											case 7:
											case 11: {
												if (curl < 256) {
													if (mem[curlist.tailfield].getb1() == hyphenchar[mainf]) {
														if (mem[curq].getrh() > 0) {
															insdisc = true;
														}
													}
													if (ligaturepresent) {
														mainp = newligature(mainf, curl, mem[curq].getrh());
														if (lfthit) {
															mem[mainp].setb1(2);
															lfthit = false;
														}
														mem[curq].setrh(mainp);
														curlist.tailfield = mainp;
														ligaturepresent = false;
													}
													if (insdisc) {
														insdisc = false;
														if (curlist.modefield > 0) {
															mem[curlist.tailfield].setrh(newdisc());
															curlist.tailfield = mem[curlist.tailfield].getrh();
														}
													}
												}
												curq = curlist.tailfield;
												curl = mainj.b3;
												maini.copy(fontinfo[charbase[mainf] + curl].qqqq());
												ligaturepresent = true;
											}
												break;
											default: {
												curl = mainj.b3;
												ligaturepresent = true;
												if (ligstack == 0) {
													jcase = 80;
													continue lab22;
												} else {
													jcase = 91;
													continue lab22;
												}
											}
										}
										if (mainj.b2 > 4) {
											if (mainj.b2 != 7) {
												jcase = 80;
												continue lab22;
											}
										}
										if (curl < 256) {
											jcase = 110;
											continue lab22;
										}
										maink = bcharlabel[mainf];
										{
											jcase = 111;
											continue lab22;
										}
									}
								}
								if (mainj.b0 == 0) {
									maink = maink + 1;
								} else {
									if (mainj.b0 >= 128) {
										jcase = 80;
										continue lab22;
									}
									maink = maink + mainj.b0 + 1;
								}
								{
									jcase = 111;
									continue lab22;
								}
							}
							case 95: {
								mainp = mem[ligstack + 1].getrh();
								if (mainp > 0) {
									mem[curlist.tailfield].setrh(mainp);
									curlist.tailfield = mem[curlist.tailfield].getrh();
								}
								tempptr = ligstack;
								ligstack = mem[tempptr].getrh();
								freenode(tempptr, 2);
								maini.copy(fontinfo[charbase[mainf] + curl].qqqq());
								ligaturepresent = true;
								if (ligstack == 0) {
									if (mainp > 0) {
										jcase = 100;
										continue lab22;
									} else {
										curr = bchar;
									}
								} else {
									curr = mem[ligstack].getb1();
								}
								{
									jcase = 110;
									continue lab22;
								}
							}
						}
					}
				}
				break;
			}
			/* lab120: */
			if (eqtb[7194].getrh() == 0) {
				mainp = fontglue[eqtb[8234].getrh()];
				if (mainp == 0) {
					mainp = newspec(0);
					maink = parambase[eqtb[8234].getrh()] + 2;
					mem[mainp + 1].setInt(fontinfo[maink].getInt());
					mem[mainp + 2].setInt(fontinfo[maink + 1].getInt());
					mem[mainp + 3].setInt(fontinfo[maink + 2].getInt());
					fontglue[eqtb[8234].getrh()] = mainp;
				}
				tempptr = newglue(mainp);
			} else {
				tempptr = newparamglue(12);
			}
			mem[curlist.tailfield].setrh(tempptr);
			curlist.tailfield = tempptr;
			continue lab60;
		}
	}

	boolean openfmtfile() throws IOException {
		int j;
		j = curinput.getLoc();
		lab40: while (true) {
			if (buffer[curinput.getLoc()] == 38) {
				curinput.setLoc(curinput.getLoc() + 1);
				j = curinput.getLoc();
				buffer[last] = 32;
				while (buffer[j] != 32) {
					j = j + 1;
				}
				packbufferedname(0, curinput.getLoc(), j - 1);
				thisfile = new File(nameoffile);
				if (thisfile.exists()) {
					try {
						fmtfile = new TexFileDataInputStream(thisfile);
					} catch (final FileNotFoundException ex) {
						termout.println();
						termout.print("Cannot open ");
						termout.print(nameoffile);
					}
					break lab40;
				}
				packbufferedname(11, curinput.getLoc(), j - 1);
				thisfile = new File(nameoffile);
				if (thisfile.exists()) {
					try {
						fmtfile = new TexFileDataInputStream(thisfile);
					} catch (final FileNotFoundException ex) {
						termout.println();
						termout.print("Cannot open ");
						termout.print(nameoffile);
					}
					break lab40;
				}
				termout.println("Sorry, I can't find that format;" + " will try PLAIN.");
				termout.flush();
			}
			packbufferedname(16, 1, 0);
			thisfile = new File(nameoffile);
			if (thisfile.exists()) {
				try {
					fmtfile = new TexFileDataInputStream(thisfile);
				} catch (final FileNotFoundException ex) {
					termout.println();
					termout.print("Cannot open ");
					termout.print(nameoffile);
				}
			} else {
				;
				termout.println("I can't find the PLAIN format file!");
				return false;
			}
			break;
		}
		/* lab40: */curinput.setLoc(j);
		return true;
	}

	boolean loadfmtfile() {
		/* 125 10 */boolean Result;
		int j, k;
		int p, q;
		int x;
		final fourquarters w = new fourquarters();
		try {
			lab125: while (true) {
				x = fmtfile.readInt();
				if (x != 270280812) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x != 0) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x != memtop) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x != 10406) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x != 5437) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x != 607) {
					break lab125;
				}
				x = fmtfile.readInt();
				if (x < 0) {
					break lab125;
				}
				poolptr = x;
				x = fmtfile.readInt();
				if (x < 0) {
					break lab125;
				}
				if (x > maxstrings) {
					;
					termout.println("---! Must increase the " + "max strings");
					break lab125;
				} else {
					strptr = x;
				}
				for (k = 0; k <= strptr; k++) {
					x = fmtfile.readInt();
					if ((x < 0) || (x > poolptr)) {
						break lab125;
					} else {
						strstart[k] = x;
					}
				}
				k = 0;
				while (k + 4 < poolptr) {
					w.b0 = fmtfile.readByte();
					w.b1 = fmtfile.readByte();
					w.b2 = fmtfile.readByte();
					w.b3 = fmtfile.readByte();
					strpool[k] = w.b0;
					strpool[k + 1] = w.b1;
					strpool[k + 2] = w.b2;
					strpool[k + 3] = w.b3;
					k = k + 4;
				}
				k = poolptr - 4;
				w.b0 = fmtfile.readByte();
				w.b1 = fmtfile.readByte();
				w.b2 = fmtfile.readByte();
				w.b3 = fmtfile.readByte();
				strpool[k] = w.b0;
				strpool[k + 1] = w.b1;
				strpool[k + 2] = w.b2;
				strpool[k + 3] = w.b3;
				initstrptr = strptr;
				x = fmtfile.readInt();
				if ((x < 1019) || (x > memtop - 14)) {
					break lab125;
				} else {
					lomemmax = x;
				}
				x = fmtfile.readInt();
				if ((x < 20) || (x > lomemmax)) {
					break lab125;
				} else {
					rover = x;
				}
				p = 0;
				q = rover;
				do {
					for (k = p; k <= q + 1; k++) {
						mem[k].memundump(fmtfile);
					}
					p = q + mem[q].getlh();
					if ((p > lomemmax) || ((q >= mem[q + 1].getrh()) && (mem[q + 1].getrh() != rover))) {
						break lab125;
					}
					q = mem[q + 1].getrh();
				} while (!(q == rover));
				for (k = p; k <= lomemmax; k++) {
					mem[k].memundump(fmtfile);
				}
				x = fmtfile.readInt();
				if ((x < lomemmax + 1) || (x > memtop - 13)) {
					break lab125;
				} else {
					himemmin = x;
				}
				x = fmtfile.readInt();
				if ((x < 0) || (x > memtop)) {
					break lab125;
				} else {
					avail = x;
				}
				memend = memtop;
				for (k = himemmin; k <= memend; k++) {
					mem[k].memundump(fmtfile);
				}
				varused = fmtfile.readInt();
				dynused = fmtfile.readInt();
				k = 1;
				do {
					x = fmtfile.readInt();
					if ((x < 1) || (k + x > 10407)) {
						break lab125;
					}
					for (j = k; j <= k + x - 1; j++) {
						eqtb[j].memundump(fmtfile);
					}
					k = k + x;
					x = fmtfile.readInt();
					if ((x < 0) || (k + x > 10407)) {
						break lab125;
					}
					for (j = k; j <= k + x - 1; j++) {
						eqtb[j].copy(eqtb[k - 1]);
					}
					k = k + x;
				} while (!(k > 10406));
				x = fmtfile.readInt();
				if ((x < 514) || (x > 6914)) {
					break lab125;
				} else {
					parloc = x;
				}
				partoken = 4095 + parloc;
				x = fmtfile.readInt();
				if ((x < 514) || (x > 6914)) {
					break lab125;
				} else {
					writeloc = x;
				}
				x = fmtfile.readInt();
				if ((x < 514) || (x > 6914)) {
					break lab125;
				} else {
					hashused = x;
				}
				p = 513;
				do {
					x = fmtfile.readInt();
					if ((x < p + 1) || (x > hashused)) {
						break lab125;
					} else {
						p = x;
					}
					hash[p - 514].lh = fmtfile.readShort();
					hash[p - 514].rh = fmtfile.readShort();
				} while (!(p == hashused));
				for (p = hashused + 1; p <= 7180; p++) {
					hash[p - 514].lh = fmtfile.readShort();
					hash[p - 514].rh = fmtfile.readShort();
				}
				cscount = fmtfile.readInt();
				x = fmtfile.readInt();
				if (x < 7) {
					break lab125;
				}
				if (x > fontmemsize) {
					;
					termout.println("---! Must increase the " + "font mem size");
					break lab125;
				} else {
					fmemptr = x;
				}
				for (k = 0; k <= fmemptr - 1; k++) {
					fontinfo[k].memundump(fmtfile);
				}
				x = fmtfile.readInt();
				if (x < 0) {
					break lab125;
				}
				if (x > fontmax) {
					;
					termout.println("---! Must increase the " + "font max");
					break lab125;
				} else {
					fontptr = x;
				}
				for (k = 0; k <= fontptr; k++) {
					fontcheck[k].b0 = fmtfile.readByte();
					fontcheck[k].b1 = fmtfile.readByte();
					fontcheck[k].b2 = fmtfile.readByte();
					fontcheck[k].b3 = fmtfile.readByte();
					fontsize[k] = fmtfile.readInt();
					fontdsize[k] = fmtfile.readInt();
					x = fmtfile.readInt();
					if ((x < 0) || (x > maxhalfword)) {
						break lab125;
					} else {
						fontparams[k] = x;
					}
					hyphenchar[k] = fmtfile.readInt();
					skewchar[k] = fmtfile.readInt();
					x = fmtfile.readInt();
					if ((x < 0) || (x > strptr)) {
						break lab125;
					} else {
						fontname[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > strptr)) {
						break lab125;
					} else {
						fontarea[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 255)) {
						break lab125;
					} else {
						fontbc[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 255)) {
						break lab125;
					} else {
						fontec[k] = x;
					}
					charbase[k] = fmtfile.readInt();
					widthbase[k] = fmtfile.readInt();
					heightbase[k] = fmtfile.readInt();
					depthbase[k] = fmtfile.readInt();
					italicbase[k] = fmtfile.readInt();
					ligkernbase[k] = fmtfile.readInt();
					kernbase[k] = fmtfile.readInt();
					extenbase[k] = fmtfile.readInt();
					parambase[k] = fmtfile.readInt();
					x = fmtfile.readInt();
					if ((x < 0) || (x > lomemmax)) {
						break lab125;
					} else {
						fontglue[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > fmemptr - 1)) {
						break lab125;
					} else {
						bcharlabel[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 256)) {
						break lab125;
					} else {
						fontbchar[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 256)) {
						break lab125;
					} else {
						fontfalsebchar[k] = x;
					}
				}
				x = fmtfile.readInt();
				if ((x < 0) || (x > 607)) {
					break lab125;
				} else {
					hyphcount = x;
				}
				for (k = 1; k <= hyphcount; k++) {
					x = fmtfile.readInt();
					if ((x < 0) || (x > 607)) {
						break lab125;
					} else {
						j = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > strptr)) {
						break lab125;
					} else {
						hyphword[j] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > maxhalfword)) {
						break lab125;
					} else {
						hyphlist[j] = x;
					}
				}
				x = fmtfile.readInt();
				if (x < 0) {
					break lab125;
				}
				if (x > triesize) {
					;
					termout.println("---! Must increase the " + "trie size");
					break lab125;
				} else {
					j = x;
				}
				if (initex) {
					triemax = j;
				}
				for (k = 0; k <= j; k++) {
					trie[k].lh = fmtfile.readShort();
					trie[k].rh = fmtfile.readShort();
				}
				x = fmtfile.readInt();
				if (x < 0) {
					break lab125;
				}
				if (x > 751) {
					;
					termout.println("---! Must increase the " + "trie op size");
					break lab125;
				} else {
					j = x;
				}
				if (initex) {
					trieopptr = j;
				}
				for (k = 1; k <= j; k++) {
					x = fmtfile.readInt();
					if ((x < 0) || (x > 63)) {
						break lab125;
					} else {
						hyfdistance[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 63)) {
						break lab125;
					} else {
						hyfnum[k] = x;
					}
					x = fmtfile.readInt();
					if ((x < 0) || (x > 255)) {
						break lab125;
					} else {
						hyfnext[k] = x;
					}
				}
				if (initex) {
					for (k = 0; k <= 255; k++) {
						trieused[k] = 0;
					}
				}
				k = 256;
				while (j > 0) {
					x = fmtfile.readInt();
					if ((x < 0) || (x > k - 1)) {
						break lab125;
					} else {
						k = x;
					}
					x = fmtfile.readInt();
					if ((x < 1) || (x > j)) {
						break lab125;
					}
					if (initex) {
						trieused[k] = x;
					}
					j = j - x;
					opstart[k] = j;
				}
				if (initex) {
					trienotready = false;
				}
				x = fmtfile.readInt();
				if ((x < 0) || (x > 3)) {
					break lab125;
				}
				x = fmtfile.readInt();
				if ((x < 0) || (x > strptr)) {
					break lab125;
				} else {
					formatident = x;
				}
				x = fmtfile.readInt();
				if (x != 69069) {
					break lab125;
				}
				Result = true;
				return Result /* lab10 */;
			}
		} catch (final IOException ex) {
		}
		/* lab125: */
		termout.println("(Fatal format file error; I'm stymied)");
		Result = false;
		return Result;
	}

	void closeFiles() {
		int k;
		for (k = 0; k <= 15; k++) {
			if (writeopen[k]) {
				writefile[k].close();
			}
		}
		while (curs > -1) {
			if (curs > 0) {
				dviWriter.writeByte(142);
			} else {
				dviWriter.writeByte(140);
				totalpages = totalpages + 1;
			}
			curs = curs - 1;
		}
		if (totalpages == 0) {
			printnl(837);
		} else {
			dviWriter.writeByte(248);
			dviWriter.writeInt(lastbop);
			lastbop = dvioffset + dviptr - 5;
			dviWriter.writeInt(25400000);
			dviWriter.writeInt(473628672);
			preparemag();
			dviWriter.writeInt(eqtb[9580].getInt());
			dviWriter.writeInt(maxv);
			dviWriter.writeInt(maxh);
			dviWriter.writeShort(maxpush);
			dviWriter.writeShort(totalpages);
			while (fontptr > 0) {
				if (fontused[fontptr]) {
					dvifontdef(fontptr);
				}
				fontptr = fontptr - 1;
			}
			dviWriter.writeByte(249);
			dviWriter.writeInt(lastbop);
			dviWriter.writeByte(2);
			k = 4 + ((dvibufsize - dviptr) % 4);
			while (k > 0) {
				dviWriter.writeByte(223);
				k = k - 1;
			}
			if (dvilimit == halfbuf) {
				writedvi(halfbuf, dvibufsize - 1);
			}
			if (dviptr > 0) {
				writedvi(0, dviptr - 1);
			}
			printnl(838);
			print(outputfilename);
			print(286);
			printInt(totalpages);
			print(839);
			if (totalpages != 1) {
				printchar(115);
			}
			print(840);
			printInt(dvioffset + dviptr);
			print(841);
			dvifile.close();
		}
		logfile.println();
		logfile.close();
		selector = selector - 2;
	}

	void finalcleanup() {
		inputStack.close();
		while (openparens > 0) {
			print(1276);
			openparens = openparens - 1;
		}
		if (curlevel > 1) {
			printnl(40);
			printEscapeSequence(1277);
			print(1278);
			printInt(curlevel - 1);
			printchar(41);
		}
		while (condptr != 0) {
			printnl(40);
			printEscapeSequence(1277);
			print(1279);
			printcmdchr(105, curif);
			if (ifline != 0) {
				print(1280);
				printInt(ifline);
			}
			print(1281);
			ifline = mem[condptr + 1].getInt();
			curif = mem[condptr].getb1();
			tempptr = condptr;
			condptr = mem[condptr].getrh();
			freenode(tempptr, 2);
		}
		if (errorReporter.getWorstLevelSoFar() != Level.OK) {
			if (selector == 19) {
				selector = 17;
				printnl(1282);
				selector = 19;
			}
		}
		if (curchr == 1) {
			if (initex) {
				for (int c = 0; c <= 4; c++) {
					if (curmark[c] != 0) {
						deletetokenref(curmark[c]);
					}
				}
				storefmtfile();
			} else {
				printnl(1283);
			}
			return /* lab10 */;
		}
	}

	void initprim() {
		nonewcontrolsequence = false;
		primitive(376, 75, 7182);
		primitive(377, 75, 7183);
		primitive(378, 75, 7184);
		primitive(379, 75, 7185);
		primitive(380, 75, 7186);
		primitive(381, 75, 7187);
		primitive(382, 75, 7188);
		primitive(383, 75, 7189);
		primitive(384, 75, 7190);
		primitive(385, 75, 7191);
		primitive(386, 75, 7192);
		primitive(387, 75, 7193);
		primitive(388, 75, 7194);
		primitive(389, 75, 7195);
		primitive(390, 75, 7196);
		primitive(391, 76, 7197);
		primitive(392, 76, 7198);
		primitive(393, 76, 7199);
		primitive(398, 72, 7713);
		primitive(399, 72, 7714);
		primitive(400, 72, 7715);
		primitive(401, 72, 7716);
		primitive(402, 72, 7717);
		primitive(403, 72, 7718);
		primitive(404, 72, 7719);
		primitive(405, 72, 7720);
		primitive(406, 72, 7721);
		primitive(420, 73, 9563);
		primitive(421, 73, 9564);
		primitive(422, 73, 9565);
		primitive(423, 73, 9566);
		primitive(424, 73, 9567);
		primitive(425, 73, 9568);
		primitive(426, 73, 9569);
		primitive(427, 73, 9570);
		primitive(428, 73, 9571);
		primitive(429, 73, 9572);
		primitive(430, 73, 9573);
		primitive(431, 73, 9574);
		primitive(432, 73, 9575);
		primitive(433, 73, 9576);
		primitive(434, 73, 9577);
		primitive(435, 73, 9578);
		primitive(436, 73, 9579);
		primitive(437, 73, 9580);
		primitive(438, 73, 9581);
		primitive(439, 73, 9582);
		primitive(440, 73, 9583);
		primitive(441, 73, 9584);
		primitive(442, 73, 9585);
		primitive(443, 73, 9586);
		primitive(444, 73, 9587);
		primitive(445, 73, 9588);
		primitive(446, 73, 9589);
		primitive(447, 73, 9590);
		primitive(448, 73, 9591);
		primitive(449, 73, 9592);
		primitive(450, 73, 9593);
		primitive(451, 73, 9594);
		primitive(452, 73, 9595);
		primitive(453, 73, 9596);
		primitive(454, 73, 9597);
		primitive(455, 73, 9598);
		primitive(456, 73, 9599);
		primitive(457, 73, 9600);
		primitive(458, 73, 9601);
		primitive(459, 73, 9602);
		primitive(460, 73, 9603);
		primitive(461, 73, 9604);
		primitive(462, 73, 9605);
		primitive(463, 73, 9606);
		primitive(464, 73, 9607);
		primitive(465, 73, 9608);
		primitive(466, 73, 9609);
		primitive(467, 73, 9610);
		primitive(468, 73, 9611);
		primitive(469, 73, 9612);
		primitive(470, 73, 9613);
		primitive(471, 73, 9614);
		primitive(472, 73, 9615);
		primitive(473, 73, 9616);
		primitive(474, 73, 9617);
		primitive(478, 74, 10130);
		primitive(479, 74, 10131);
		primitive(480, 74, 10132);
		primitive(481, 74, 10133);
		primitive(482, 74, 10134);
		primitive(483, 74, 10135);
		primitive(484, 74, 10136);
		primitive(485, 74, 10137);
		primitive(486, 74, 10138);
		primitive(487, 74, 10139);
		primitive(488, 74, 10140);
		primitive(489, 74, 10141);
		primitive(490, 74, 10142);
		primitive(491, 74, 10143);
		primitive(492, 74, 10144);
		primitive(493, 74, 10145);
		primitive(494, 74, 10146);
		primitive(495, 74, 10147);
		primitive(496, 74, 10148);
		primitive(497, 74, 10149);
		primitive(498, 74, 10150);
		primitive(32, 64, 0);
		primitive(47, 44, 0);
		primitive(508, 45, 0);
		primitive(509, 90, 0);
		primitive(510, 40, 0);
		primitive(511, 41, 0);
		primitive(512, 61, 0);
		primitive(513, 16, 0);
		primitive(504, 107, 0);
		primitive(514, 15, 0);
		primitive(515, 92, 0);
		primitive(505, 67, 0);
		primitive(516, 62, 0);
		hash[6916 - 514].rh = 516;
		eqtb[6916].copy(eqtb[curval]);
		primitive(517, 102, 0);
		primitive(518, 88, 0);
		primitive(519, 77, 0);
		primitive(520, 32, 0);
		primitive(521, 36, 0);
		primitive(522, 39, 0);
		primitive(330, 37, 0);
		primitive(351, 18, 0);
		primitive(523, 46, 0);
		primitive(524, 17, 0);
		primitive(525, 54, 0);
		primitive(526, 91, 0);
		primitive(527, 34, 0);
		primitive(528, 65, 0);
		primitive(529, 103, 0);
		primitive(335, 55, 0);
		primitive(530, 63, 0);
		primitive(408, 84, 0);
		primitive(531, 42, 0);
		primitive(532, 80, 0);
		primitive(533, 66, 0);
		primitive(534, 96, 0);
		primitive(535, 0, 256);
		hash[6921 - 514].rh = 535;
		eqtb[6921].copy(eqtb[curval]);
		primitive(536, 98, 0);
		primitive(537, 109, 0);
		primitive(407, 71, 0);
		primitive(352, 38, 0);
		primitive(538, 33, 0);
		primitive(539, 56, 0);
		primitive(540, 35, 0);
		primitive(597, 13, 256);
		parloc = curval;
		partoken = 4095 + parloc;
		primitive(629, 104, 0);
		primitive(630, 104, 1);
		primitive(631, 110, 0);
		primitive(632, 110, 1);
		primitive(633, 110, 2);
		primitive(634, 110, 3);
		primitive(635, 110, 4);
		primitive(476, 89, 0);
		primitive(500, 89, 1);
		primitive(395, 89, 2);
		primitive(396, 89, 3);
		primitive(668, 79, 102);
		primitive(669, 79, 1);
		primitive(670, 82, 0);
		primitive(671, 82, 1);
		primitive(672, 83, 1);
		primitive(673, 83, 3);
		primitive(674, 83, 2);
		primitive(675, 70, 0);
		primitive(676, 70, 1);
		primitive(677, 70, 2);
		primitive(678, 70, 3);
		primitive(679, 70, 4);
		primitive(735, 108, 0);
		primitive(736, 108, 1);
		primitive(737, 108, 2);
		primitive(738, 108, 3);
		primitive(739, 108, 4);
		primitive(740, 108, 5);
		primitive(756, 105, 0);
		primitive(757, 105, 1);
		primitive(758, 105, 2);
		primitive(759, 105, 3);
		primitive(760, 105, 4);
		primitive(761, 105, 5);
		primitive(762, 105, 6);
		primitive(763, 105, 7);
		primitive(764, 105, 8);
		primitive(765, 105, 9);
		primitive(766, 105, 10);
		primitive(767, 105, 11);
		primitive(768, 105, 12);
		primitive(769, 105, 13);
		primitive(770, 105, 14);
		primitive(771, 105, 15);
		primitive(772, 105, 16);
		primitive(773, 106, 2);
		hash[6918 - 514].rh = 773;
		eqtb[6918].copy(eqtb[curval]);
		primitive(774, 106, 4);
		primitive(775, 106, 3);
		primitive(801, 87, 0);
		hash[6924 - 514].rh = 801;
		eqtb[6924].copy(eqtb[curval]);
		primitive(898, 4, 256);
		primitive(899, 5, 257);
		hash[6915 - 514].rh = 899;
		eqtb[6915].copy(eqtb[curval]);
		primitive(900, 5, 258);
		hash[6919 - 514].rh = 901;
		hash[6920 - 514].rh = 901;
		eqtb[6920].setb0(9);
		eqtb[6920].setrh(memtop - 11);
		eqtb[6920].setb1(1);
		eqtb[6919].copy(eqtb[6920]);
		eqtb[6919].setb0(115);
		primitive(970, 81, 0);
		primitive(971, 81, 1);
		primitive(972, 81, 2);
		primitive(973, 81, 3);
		primitive(974, 81, 4);
		primitive(975, 81, 5);
		primitive(976, 81, 6);
		primitive(977, 81, 7);
		primitive(1025, 14, 0);
		primitive(1026, 14, 1);
		primitive(1027, 26, 4);
		primitive(1028, 26, 0);
		primitive(1029, 26, 1);
		primitive(1030, 26, 2);
		primitive(1031, 26, 3);
		primitive(1032, 27, 4);
		primitive(1033, 27, 0);
		primitive(1034, 27, 1);
		primitive(1035, 27, 2);
		primitive(1036, 27, 3);
		primitive(336, 28, 5);
		primitive(340, 29, 1);
		primitive(342, 30, 99);
		primitive(1054, 21, 1);
		primitive(1055, 21, 0);
		primitive(1056, 22, 1);
		primitive(1057, 22, 0);
		primitive(409, 20, 0);
		primitive(1058, 20, 1);
		primitive(1059, 20, 2);
		primitive(965, 20, 3);
		primitive(1060, 20, 4);
		primitive(967, 20, 5);
		primitive(1061, 20, 106);
		primitive(1062, 31, 99);
		primitive(1063, 31, 100);
		primitive(1064, 31, 101);
		primitive(1065, 31, 102);
		primitive(1080, 43, 1);
		primitive(1081, 43, 0);
		primitive(1090, 25, 12);
		primitive(1091, 25, 11);
		primitive(1092, 25, 10);
		primitive(1093, 23, 0);
		primitive(1094, 23, 1);
		primitive(1095, 24, 0);
		primitive(1096, 24, 1);
		primitive(45, 47, 1);
		primitive(349, 47, 0);
		primitive(1127, 48, 0);
		primitive(1128, 48, 1);
		primitive(866, 50, 16);
		primitive(867, 50, 17);
		primitive(868, 50, 18);
		primitive(869, 50, 19);
		primitive(870, 50, 20);
		primitive(871, 50, 21);
		primitive(872, 50, 22);
		primitive(873, 50, 23);
		primitive(875, 50, 26);
		primitive(874, 50, 27);
		primitive(1129, 51, 0);
		primitive(878, 51, 1);
		primitive(879, 51, 2);
		primitive(861, 53, 0);
		primitive(862, 53, 2);
		primitive(863, 53, 4);
		primitive(864, 53, 6);
		primitive(1147, 52, 0);
		primitive(1148, 52, 1);
		primitive(1149, 52, 2);
		primitive(1150, 52, 3);
		primitive(1151, 52, 4);
		primitive(1152, 52, 5);
		primitive(876, 49, 30);
		primitive(877, 49, 31);
		hash[6917 - 514].rh = 877;
		eqtb[6917].copy(eqtb[curval]);
		primitive(1171, 93, 1);
		primitive(1172, 93, 2);
		primitive(1173, 93, 4);
		primitive(1174, 97, 0);
		primitive(1175, 97, 1);
		primitive(1176, 97, 2);
		primitive(1177, 97, 3);
		primitive(1191, 94, 0);
		primitive(1192, 94, 1);
		primitive(1193, 95, 0);
		primitive(1194, 95, 1);
		primitive(1195, 95, 2);
		primitive(1196, 95, 3);
		primitive(1197, 95, 4);
		primitive(1198, 95, 5);
		primitive(1199, 95, 6);
		primitive(415, 85, 8283);
		primitive(419, 85, 9307);
		primitive(416, 85, 8539);
		primitive(417, 85, 8795);
		primitive(418, 85, 9051);
		primitive(477, 85, 9874);
		primitive(412, 86, 8235);
		primitive(413, 86, 8251);
		primitive(414, 86, 8267);
		primitive(941, 99, 0);
		primitive(953, 99, 1);
		primitive(1217, 78, 0);
		primitive(1218, 78, 1);
		primitive(274, 100, 0);
		primitive(275, 100, 1);
		primitive(276, 100, 2);
		primitive(1227, 100, 3);
		primitive(1228, 60, 1);
		primitive(1229, 60, 0);
		primitive(1230, 58, 0);
		primitive(1231, 58, 1);
		primitive(1237, 57, 8539);
		primitive(1238, 57, 8795);
		primitive(1239, 19, 0);
		primitive(1240, 19, 1);
		primitive(1241, 19, 2);
		primitive(1242, 19, 3);
		primitive(1285, 59, 0);
		primitive(594, 59, 1);
		writeloc = curval;
		primitive(1286, 59, 2);
		primitive(1287, 59, 3);
		primitive(1288, 59, 4);
		primitive(1289, 59, 5);
		nonewcontrolsequence = true;
	}

	/**
	 * Runs the tex engine.
	 */
	public void run() {
		termout = new PrintWriter(System.out);
		bad = 0;
		if ((maxhalfword < 32767)) {
			bad = 12;
		}
		if (255 > maxhalfword) {
			bad = 13;
		}
		if ((memmax >= maxhalfword) || (-0 > maxhalfword + 1)) {
			bad = 14;
		}
		if ((savesize > maxhalfword) || (maxstrings > maxhalfword)) {
			bad = 17;
		}
		if (11276 > maxhalfword) {
			bad = 21;
		}
		if (2 * maxhalfword < memtop - 0) {
			bad = 41;
		}
		if (bad > 0) {
			throw new IllegalStateException("Ouch---my internal constants have been clobbered!" + "---case " + bad);
		}
		initialize();
		this.inputStack = new InputStack(this);
		openlogfile();
		this.errorReporter = new ErrorReporter(logfile);
		this.errorLogic = new ErrorLogic(this);
		if (initex) {
			getstringsstarted();
			initprim();
			initstrptr = strptr;
			setDummyDateTime();
		}
		selector = 17;
		tally = 0;
		termoffset = 0;
		fileoffset = 0;
		termout.print("This is TeX, Version 3.14159");
		if (formatident == 0) {
			termout.print(" (no format preloaded)");
		} else {
			print(formatident);
			termout.print(' ');
			termout.println("[Java version A]");
		}
		termout.flush();
		jobname = 796; // pre-assigned dummy job name
		nameinprogress = false;
		outputfilename = 0;
		inputptr = 0;
		inopen = 0;
		openparens = 0;
		paramptr = 0;
		maxparamstack = 0;
		inputBuffer.initialize();
		scannerstatus = 0;
		warningindex = 0;
		first = 1;
		line = 0;
		forceeof = false;
		alignstate = 1000000;
		if (!initterminal()) {
			throw new RuntimeException("initterminal() failed");
		}
		curinput.setState(TOKENIZER_STATE_NEW_LINE);
		curinput.setStart(1);
		curinput.setIndex(0);
		curinput.setName(0);
		curinput.setLimit(last);
		first = last + 1;
		if ((formatident == 0) || (buffer[curinput.getLoc()] == 38)) {
			if (formatident != 0) {
				initialize();
			}
			try {
				if (!openfmtfile()) {
					throw new RuntimeException("openfmtfile()");
				}
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			if (!loadfmtfile()) {
				fmtfile.close();
				throw new RuntimeException("malformed format file");
			}
			fmtfile.close();
			while ((curinput.getLoc() < curinput.getLimit()) && (buffer[curinput.getLoc()] == 32)) {
				curinput.setLoc(curinput.getLoc() + 1);
			}
		}
		if ((eqtb[9611].getInt() < 0) || (eqtb[9611].getInt() > 255)) {
			curinput.setLimit(curinput.getLimit() - 1);
		} else {
			buffer[curinput.getLimit()] = eqtb[9611].getInt();
		}
		setDummyDateTime();
		magicoffset = strstart[892] - 9 * 16;
		selector = 17;
		if ((curinput.getLoc() < curinput.getLimit()) && (eqtb[8283 + buffer[curinput.getLoc()]].getrh() != 0)) {
			startinput();
		}
		maincontrol();
		finalcleanup();
		closeFiles(); // TODO also in case of exceptions
	}

	// ------------------------------------------------------------------------------------------------
	// level-1 printing routines (actual output and selector handling)
	// ------------------------------------------------------------------------------------------------

	/*
	 * Selectors:
	 *
	 * define no_print = 16 { selector setting that makes data disappear }
	 * define term_only = 17 { printing is destined for the terminal only }
	 * define log_only = 18 { printing is destined for the transcript file only }
	 * define term_and_log = 19 { normal selector setting }
	 * define pseudo = 20 { special selector setting for show context }
	 * define new_string = 21 { printing is deflected to the string pool }
	 *
	 * define max selector = 21 { highest selector setting }
	 *
	 */

	/**
	 * Returns the "current newline character" used for printing.
	 * @return the current newline character
	 */
	int getCurrentNewlineCharacter() {
		return eqtb[9612].getInt();
	}

	/**
	 * Prints a newline character to the currently selected output.
	 */
	void println() {

		// output streams
		if (selector < 16) {
			writefile[selector].print('\n');
		}

		// terminal
		if (selector == 17 || selector == 19) {
			termout.println();
			termoffset = 0;

		}

		// logfile
		if (selector == 18 || selector == 19) {
			logfile.println();
			fileoffset = 0;
		}

	}

	/**
	 * Prints the specified character to the current output
	 * @param c the character to print
	 */
	void printchar(final int c) {

		// replace the "current newline character" by a real newline character except for show_context and new_string
		if (selector < 20 && c == getCurrentNewlineCharacter()) {
			println();
			return;
		}

		// selector-dependent printing code
		switch (selector) {

			case 19: {
				termout.print((char)(c));
				logfile.print((char)(c));
				termoffset++;
				fileoffset++;
				break;
			}

			case 18: {
				logfile.print((char)(c));
				fileoffset++;
				break;
			}

			case 17: {
				termout.print((char)(c));
				termoffset++;
				break;
			}

			case 16: {
				break;
			}

			case 20: {
				if (tally < trickcount) {
					trickbuf[tally % errorline] = c;
				}
				break;
			}

			case 21: {
				stringPool.append((char)c);
				break;
			}

			default: {
				writefile[selector].print((char)(c));
				break;
			}

		}
		tally = tally + 1;
	}

	/**
	 * Prints the specified string to the current output
	 * @param s the string to print
	 */
	void print(final String s) {
		for (int i = 0; i < s.length(); i++) {
			printchar(s.charAt(i));
		}
	}

	/**
	 * Prints the specified string from the string pool to the current output.
	 * @param stringId the string ID for the string to print
	 */
	void print(final int stringId) {
		if (stringId < 0 || stringId >= stringPool.getStringCount()) {
			print("???");
		} else if (stringId < 256) {
			printchar(stringId);
		} else {
			print(stringPool.getString(stringId));
		}
	}

	/**
	 * Starts a new line in case the current one contains any output (this can only be
	 * detected for the terminal or log file), then prints the specified string.
	 * @param s the string to print
	 */
	void printnl(final String s) {
		if (((termoffset > 0) && (((selector) % 2 == 1))) || ((fileoffset > 0) && (selector >= 18))) {
			println();
		}
		print(s);
	}

	/**
	 * Starts a new line in case the current one contains any output (this can only be
	 * detected for the terminal or log file), then prints the specified string.
	 * @param stringId the string ID for the string to print
	 */
	void printnl(final int s) {
		if (((termoffset > 0) && (((selector) % 2 == 1))) || ((fileoffset > 0) && (selector >= 18))) {
			println();
		}
		print(s);
	}

	// ------------------------------------------------------------------------------------------------
	// level-2 printing routines (formatting of various values)
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns the current escape character (usually a backslash). Also returns a backslash
	 * if the current escape character is invalid.
	 */
	int getCurrentEscapeCharacter() {
		final int c = eqtb[9608].getInt();
		if (c >= 0 && c < 256) {
			return c;
		} else {
			return '\\';
		}
	}

	/**
	 * Prints the current escape character (usually a backslash), or nothing if
	 * that character is set to an invalid code.
	 */
	void printCurrentEscapeCharacter() {
		final int c = eqtb[9608].getInt();
		if (c >= 0 && c < 256) {
			printchar(c);
		}
	}

	/**
	 * Prints the specified string, prefixed by the current escape character (usually a
	 * backslash.
	 *
	 * @param s the string to print
	 */
	void printEscapeSequence(final String s) {
		printCurrentEscapeCharacter();
		print(s);
	}

	/**
	 * Prints the specified string, prefixed by the current escape character (usually a
	 * backslash.
	 *
	 * @param s the string pool selector for the string to print
	 */
	void printEscapeSequence(final int s) {
		printCurrentEscapeCharacter();
		print(s);
	}

	/**
	 * Prints a decimal integer to the currently selected output.
	 * @param n the integer to print
	 */
	void printInt(final int n) {
		print(Integer.toString(n));
	}

	/**
	 * Prints a hexadecimal integer to the currently selected output, prefixed by a double-quote
	 * (used by TeX to denote hexadecimal integers).
	 * @param n the integer to print
	 */
	void printHex(final int n) {
		printchar(34);
		print(Integer.toString(n, 16));
	}

	/**
	 * Prints the two lowest-order decimal digits of the specified integer.
	 * @param n the integer to print
	 */
	void printTwoDigits(int n) {
		n = Math.abs(n) % 100;
		printchar(48 + (n / 10));
		printchar(48 + (n % 10));
	}

	/**
	 * Prints a fixed-point number.
	 * @param n the number to print
	 */
	void printFixed(int n) {
		printInt(n >> 16);
		printchar(46);

		int delta;
		n = 10 * (n % 65536) + 5;
		delta = 10;
		do {
			if (delta > 65536) {
				n = n - 17232;
			}
			printchar(48 + (n / 65536));
			n = 10 * (n % 65536);
			delta = delta * 10;
		} while (!(n <= delta));
	}

	// ------------------------------------------------------------------------------------------------
	// high-level printing routines
	// ------------------------------------------------------------------------------------------------

	void printcs(final int p) {
		if (p < 514) {
			if (p >= 257) {
				if (p == 513) {
					printEscapeSequence(504);
					printEscapeSequence(505);
				} else {
					printEscapeSequence(p - 257);
					if (eqtb[8283 + p - 257].getrh() == 11) {
						printchar(32);
					}
				}
			} else if (p < 1) {
				printEscapeSequence(506);
			} else {
				print(p - 1);
			}
		} else if (p >= 7181) {
			printEscapeSequence(506);
		} else if ((hash[p - 514].rh >= strptr)) {
			printEscapeSequence(507);
		} else {
			printEscapeSequence(hash[p - 514].rh);
			printchar(32);
		}
	}

	void sprintcs(final int p) {
		if (p < 514) {
			if (p < 257) {
				print(p - 1);
			} else if (p < 513) {
				printEscapeSequence(p - 257);
			} else {
				printEscapeSequence(504);
				printEscapeSequence(505);
			}
		} else {
			printEscapeSequence(hash[p - 514].rh);
		}
	}

	void printfilename(final int n, final int a, final int e) {
		print(a);
		print(n);
		print(e);
	}

	void printsize(final int s) {
		if (s == 0) {
			printEscapeSequence(412);
		} else if (s == 16) {
			printEscapeSequence(413);
		} else {
			printEscapeSequence(414);
		}
	}

	void printwritewhatsit(final int s, final int p) {
		printEscapeSequence(s);
		if (mem[p + 1].getlh() < 16) {
			printInt(mem[p + 1].getlh());
		} else if (mem[p + 1].getlh() == 16) {
			printchar(42);
		} else {
			printchar(45);
		}
	}

	void printromanint(int n) {
		String s = stringPool.getString(260);
		int j = 0;
		int v = 1000;
		while (true) {
			while (n >= v) {
				printchar(s.charAt(j));
				n = n - v;
			}
			if (n <= 0) {
				return /* lab10 */;
			}
			int k = j + 2;
			int u = v / (s.charAt(k - 1) - 48);
			if (s.charAt(k - 1) == 50) {
				k = k + 2;
				u = u / (s.charAt(k - 1) - 48);
			}
			if (n + u >= v) {
				printchar(s.charAt(k));
				n = n + u;
			} else {
				j = j + 2;
				v = v / (s.charAt(j - 1) - 48);
			}
		}
	}

	void printcurrentstring() {
		print(stringPool.extractPartiallyBuiltString(false));
	}

	void printfontandchar(final int p) {
		if (p > memend) {
			printEscapeSequence(309);
		} else {
			if ((mem[p].getb0() < 0) || (mem[p].getb0() > fontmax)) {
				printchar(42);
			} else {
				printEscapeSequence(hash[6924 + mem[p].getb0() - 514].rh);
			}
			printchar(32);
			print(mem[p].getb1());
		}
	}

	void printmark(final int p) {
		printchar(123);
		if ((p < himemmin) || (p > memend)) {
			printEscapeSequence(309);
		} else {
			showtokenlist(mem[p].getrh(), 0, 10000000);
		}
		printchar(125);
	}

	void printruledimen(final int d) {
		if ((d == -1073741824)) {
			printchar(42);
		} else {
			printFixed(d);
		}
	}

	void printglue(final int d, int order, final int s) {
		printFixed(d);
		if ((order < 0) || (order > 3)) {
			print(310);
		} else if (order > 0) {
			print(311);
			while (order > 1) {
				printchar(108);
				order = order - 1;
			}
		} else if (s != 0) {
			print(s);
		}
	}

	void printspec(final int p, final int s) {
		if ((p < 0) || (p >= lomemmax)) {
			printchar(42);
		} else {
			printFixed(mem[p + 1].getInt());
			if (s != 0) {
				print(s);
			}
			if (mem[p + 2].getInt() != 0) {
				print(312);
				printglue(mem[p + 2].getInt(), mem[p].getb0(), s);
			}
			if (mem[p + 3].getInt() != 0) {
				print(313);
				printglue(mem[p + 3].getInt(), mem[p].getb1(), s);
			}
		}
	}

	void printfamandchar(final int p) {
		printEscapeSequence(464);
		printInt(mem[p].getb0());
		printchar(32);
		print(mem[p].getb1());
	}

	void printdelimiter(final int p) {
		int a;
		a = mem[p].getb0() * 256 + mem[p].getb1();
		a = a * 4096 + mem[p].getb2() * 256 + mem[p].getb3();
		if (a < 0) {
			printInt(a);
		} else {
			printHex(a);
		}
	}

	void printsubsidiarydata(final int p, final int c) {
		if (stringPool.getBuiltLength() >= depththreshold) {
			if (mem[p].getrh() != 0) {
				print(314);
			}
		} else {
			stringPool.append((char)c);
			tempptr = p;
			switch (mem[p].getrh()) {
				case 1: {
					println();
					printcurrentstring();
					printfamandchar(p);
				}
					break;
				case 2:
					showinfo();
					break;
				case 3:
					if (mem[p].getlh() == 0) {
						println();
						printcurrentstring();
						print(860);
					} else {
						showinfo();
					}
					break;
				default:
					;
					break;
			}
			poolptr = poolptr - 1;
		}
	}

	void printstyle(final int c) {
		switch (c / 2) {
			case 0:
				printEscapeSequence(861);
				break;
			case 1:
				printEscapeSequence(862);
				break;
			case 2:
				printEscapeSequence(863);
				break;
			case 3:
				printEscapeSequence(864);
				break;
			default:
				print(865);
				break;
		}
	}

	void printskipparam(final int n) {
		switch (n) {
			case 0:
				printEscapeSequence(376);
				break;
			case 1:
				printEscapeSequence(377);
				break;
			case 2:
				printEscapeSequence(378);
				break;
			case 3:
				printEscapeSequence(379);
				break;
			case 4:
				printEscapeSequence(380);
				break;
			case 5:
				printEscapeSequence(381);
				break;
			case 6:
				printEscapeSequence(382);
				break;
			case 7:
				printEscapeSequence(383);
				break;
			case 8:
				printEscapeSequence(384);
				break;
			case 9:
				printEscapeSequence(385);
				break;
			case 10:
				printEscapeSequence(386);
				break;
			case 11:
				printEscapeSequence(387);
				break;
			case 12:
				printEscapeSequence(388);
				break;
			case 13:
				printEscapeSequence(389);
				break;
			case 14:
				printEscapeSequence(390);
				break;
			case 15:
				printEscapeSequence(391);
				break;
			case 16:
				printEscapeSequence(392);
				break;
			case 17:
				printEscapeSequence(393);
				break;
			default:
				print(394);
				break;
		}
	}

	void printmode(final int m) {
		if (m > 0) {
			switch (m / (101)) {
				case 0:
					print(355);
					break;
				case 1:
					print(356);
					break;
				case 2:
					print(357);
					break;
			}
		} else if (m == 0) {
			print(358);
		} else {
			switch ((-m) / (101)) {
				case 0:
					print(359);
					break;
				case 1:
					print(360);
					break;
				case 2:
					print(343);
					break;
			}
		}
		print(361);
	}

	void printparam(final int n) {
		switch (n) {
			case 0:
				printEscapeSequence(420);
				break;
			case 1:
				printEscapeSequence(421);
				break;
			case 2:
				printEscapeSequence(422);
				break;
			case 3:
				printEscapeSequence(423);
				break;
			case 4:
				printEscapeSequence(424);
				break;
			case 5:
				printEscapeSequence(425);
				break;
			case 6:
				printEscapeSequence(426);
				break;
			case 7:
				printEscapeSequence(427);
				break;
			case 8:
				printEscapeSequence(428);
				break;
			case 9:
				printEscapeSequence(429);
				break;
			case 10:
				printEscapeSequence(430);
				break;
			case 11:
				printEscapeSequence(431);
				break;
			case 12:
				printEscapeSequence(432);
				break;
			case 13:
				printEscapeSequence(433);
				break;
			case 14:
				printEscapeSequence(434);
				break;
			case 15:
				printEscapeSequence(435);
				break;
			case 16:
				printEscapeSequence(436);
				break;
			case 17:
				printEscapeSequence(437);
				break;
			case 18:
				printEscapeSequence(438);
				break;
			case 19:
				printEscapeSequence(439);
				break;
			case 20:
				printEscapeSequence(440);
				break;
			case 21:
				printEscapeSequence(441);
				break;
			case 22:
				printEscapeSequence(442);
				break;
			case 23:
				printEscapeSequence(443);
				break;
			case 24:
				printEscapeSequence(444);
				break;
			case 25:
				printEscapeSequence(445);
				break;
			case 26:
				printEscapeSequence(446);
				break;
			case 27:
				printEscapeSequence(447);
				break;
			case 28:
				printEscapeSequence(448);
				break;
			case 29:
				printEscapeSequence(449);
				break;
			case 30:
				printEscapeSequence(450);
				break;
			case 31:
				printEscapeSequence(451);
				break;
			case 32:
				printEscapeSequence(452);
				break;
			case 33:
				printEscapeSequence(453);
				break;
			case 34:
				printEscapeSequence(454);
				break;
			case 35:
				printEscapeSequence(455);
				break;
			case 36:
				printEscapeSequence(456);
				break;
			case 37:
				printEscapeSequence(457);
				break;
			case 38:
				printEscapeSequence(458);
				break;
			case 39:
				printEscapeSequence(459);
				break;
			case 40:
				printEscapeSequence(460);
				break;
			case 41:
				printEscapeSequence(461);
				break;
			case 42:
				printEscapeSequence(462);
				break;
			case 43:
				printEscapeSequence(463);
				break;
			case 44:
				printEscapeSequence(464);
				break;
			case 45:
				printEscapeSequence(465);
				break;
			case 46:
				printEscapeSequence(466);
				break;
			case 47:
				printEscapeSequence(467);
				break;
			case 48:
				printEscapeSequence(468);
				break;
			case 49:
				printEscapeSequence(469);
				break;
			case 50:
				printEscapeSequence(470);
				break;
			case 51:
				printEscapeSequence(471);
				break;
			case 52:
				printEscapeSequence(472);
				break;
			case 53:
				printEscapeSequence(473);
				break;
			case 54:
				printEscapeSequence(474);
				break;
			default:
				print(475);
				break;
		}
	}

	void printlengthparam(final int n) {
		switch (n) {
			case 0:
				printEscapeSequence(478);
				break;
			case 1:
				printEscapeSequence(479);
				break;
			case 2:
				printEscapeSequence(480);
				break;
			case 3:
				printEscapeSequence(481);
				break;
			case 4:
				printEscapeSequence(482);
				break;
			case 5:
				printEscapeSequence(483);
				break;
			case 6:
				printEscapeSequence(484);
				break;
			case 7:
				printEscapeSequence(485);
				break;
			case 8:
				printEscapeSequence(486);
				break;
			case 9:
				printEscapeSequence(487);
				break;
			case 10:
				printEscapeSequence(488);
				break;
			case 11:
				printEscapeSequence(489);
				break;
			case 12:
				printEscapeSequence(490);
				break;
			case 13:
				printEscapeSequence(491);
				break;
			case 14:
				printEscapeSequence(492);
				break;
			case 15:
				printEscapeSequence(493);
				break;
			case 16:
				printEscapeSequence(494);
				break;
			case 17:
				printEscapeSequence(495);
				break;
			case 18:
				printEscapeSequence(496);
				break;
			case 19:
				printEscapeSequence(497);
				break;
			case 20:
				printEscapeSequence(498);
				break;
			default:
				print(499);
				break;
		}
	}

	void printcmdchr(final int cmd, final int chrcode) {
		switch (cmd) {
			case 1: {
				print(557);
				print(chrcode);
			}
				break;
			case 2: {
				print(558);
				print(chrcode);
			}
				break;
			case 3: {
				print(559);
				print(chrcode);
			}
				break;
			case 6: {
				print(560);
				print(chrcode);
			}
				break;
			case 7: {
				print(561);
				print(chrcode);
			}
				break;
			case 8: {
				print(562);
				print(chrcode);
			}
				break;
			case 9:
				print(563);
				break;
			case 10: {
				print(564);
				print(chrcode);
			}
				break;
			case 11: {
				print(565);
				print(chrcode);
			}
				break;
			case 12: {
				print(566);
				print(chrcode);
			}
				break;
			case 75:
			case 76:
				if (chrcode < 7200) {
					printskipparam(chrcode - 7182);
				} else if (chrcode < 7456) {
					printEscapeSequence(395);
					printInt(chrcode - 7200);
				} else {
					printEscapeSequence(396);
					printInt(chrcode - 7456);
				}
				break;
			case 72:
				if (chrcode >= 7722) {
					printEscapeSequence(407);
					printInt(chrcode - 7722);
				} else {
					switch (chrcode) {
						case 7713:
							printEscapeSequence(398);
							break;
						case 7714:
							printEscapeSequence(399);
							break;
						case 7715:
							printEscapeSequence(400);
							break;
						case 7716:
							printEscapeSequence(401);
							break;
						case 7717:
							printEscapeSequence(402);
							break;
						case 7718:
							printEscapeSequence(403);
							break;
						case 7719:
							printEscapeSequence(404);
							break;
						case 7720:
							printEscapeSequence(405);
							break;
						default:
							printEscapeSequence(406);
							break;
					}
				}
				break;
			case 73:
				if (chrcode < 9618) {
					printparam(chrcode - 9563);
				} else {
					printEscapeSequence(476);
					printInt(chrcode - 9618);
				}
				break;
			case 74:
				if (chrcode < 10151) {
					printlengthparam(chrcode - 10130);
				} else {
					printEscapeSequence(500);
					printInt(chrcode - 10151);
				}
				break;
			case 45:
				printEscapeSequence(508);
				break;
			case 90:
				printEscapeSequence(509);
				break;
			case 40:
				printEscapeSequence(510);
				break;
			case 41:
				printEscapeSequence(511);
				break;
			case 77:
				printEscapeSequence(519);
				break;
			case 61:
				printEscapeSequence(512);
				break;
			case 42:
				printEscapeSequence(531);
				break;
			case 16:
				printEscapeSequence(513);
				break;
			case 107:
				printEscapeSequence(504);
				break;
			case 88:
				printEscapeSequence(518);
				break;
			case 15:
				printEscapeSequence(514);
				break;
			case 92:
				printEscapeSequence(515);
				break;
			case 67:
				printEscapeSequence(505);
				break;
			case 62:
				printEscapeSequence(516);
				break;
			case 64:
				printEscapeSequence(32);
				break;
			case 102:
				printEscapeSequence(517);
				break;
			case 32:
				printEscapeSequence(520);
				break;
			case 36:
				printEscapeSequence(521);
				break;
			case 39:
				printEscapeSequence(522);
				break;
			case 37:
				printEscapeSequence(330);
				break;
			case 44:
				printEscapeSequence(47);
				break;
			case 18:
				printEscapeSequence(351);
				break;
			case 46:
				printEscapeSequence(523);
				break;
			case 17:
				printEscapeSequence(524);
				break;
			case 54:
				printEscapeSequence(525);
				break;
			case 91:
				printEscapeSequence(526);
				break;
			case 34:
				printEscapeSequence(527);
				break;
			case 65:
				printEscapeSequence(528);
				break;
			case 103:
				printEscapeSequence(529);
				break;
			case 55:
				printEscapeSequence(335);
				break;
			case 63:
				printEscapeSequence(530);
				break;
			case 66:
				printEscapeSequence(533);
				break;
			case 96:
				printEscapeSequence(534);
				break;
			case 0:
				printEscapeSequence(535);
				break;
			case 98:
				printEscapeSequence(536);
				break;
			case 80:
				printEscapeSequence(532);
				break;
			case 84:
				printEscapeSequence(408);
				break;
			case 109:
				printEscapeSequence(537);
				break;
			case 71:
				printEscapeSequence(407);
				break;
			case 38:
				printEscapeSequence(352);
				break;
			case 33:
				printEscapeSequence(538);
				break;
			case 56:
				printEscapeSequence(539);
				break;
			case 35:
				printEscapeSequence(540);
				break;
			case 13:
				printEscapeSequence(597);
				break;
			case 104:
				if (chrcode == 0) {
					printEscapeSequence(629);
				} else {
					printEscapeSequence(630);
				}
				break;
			case 110:
				switch (chrcode) {
					case 1:
						printEscapeSequence(632);
						break;
					case 2:
						printEscapeSequence(633);
						break;
					case 3:
						printEscapeSequence(634);
						break;
					case 4:
						printEscapeSequence(635);
						break;
					default:
						printEscapeSequence(631);
						break;
				}
				break;
			case 89:
				if (chrcode == 0) {
					printEscapeSequence(476);
				} else if (chrcode == 1) {
					printEscapeSequence(500);
				} else if (chrcode == 2) {
					printEscapeSequence(395);
				} else {
					printEscapeSequence(396);
				}
				break;
			case 79:
				if (chrcode == 1) {
					printEscapeSequence(669);
				} else {
					printEscapeSequence(668);
				}
				break;
			case 82:
				if (chrcode == 0) {
					printEscapeSequence(670);
				} else {
					printEscapeSequence(671);
				}
				break;
			case 83:
				if (chrcode == 1) {
					printEscapeSequence(672);
				} else if (chrcode == 3) {
					printEscapeSequence(673);
				} else {
					printEscapeSequence(674);
				}
				break;
			case 70:
				switch (chrcode) {
					case 0:
						printEscapeSequence(675);
						break;
					case 1:
						printEscapeSequence(676);
						break;
					case 2:
						printEscapeSequence(677);
						break;
					case 3:
						printEscapeSequence(678);
						break;
					default:
						printEscapeSequence(679);
						break;
				}
				break;
			case 108:
				switch (chrcode) {
					case 0:
						printEscapeSequence(735);
						break;
					case 1:
						printEscapeSequence(736);
						break;
					case 2:
						printEscapeSequence(737);
						break;
					case 3:
						printEscapeSequence(738);
						break;
					case 4:
						printEscapeSequence(739);
						break;
					default:
						printEscapeSequence(740);
						break;
				}
				break;
			case 105:
				switch (chrcode) {
					case 1:
						printEscapeSequence(757);
						break;
					case 2:
						printEscapeSequence(758);
						break;
					case 3:
						printEscapeSequence(759);
						break;
					case 4:
						printEscapeSequence(760);
						break;
					case 5:
						printEscapeSequence(761);
						break;
					case 6:
						printEscapeSequence(762);
						break;
					case 7:
						printEscapeSequence(763);
						break;
					case 8:
						printEscapeSequence(764);
						break;
					case 9:
						printEscapeSequence(765);
						break;
					case 10:
						printEscapeSequence(766);
						break;
					case 11:
						printEscapeSequence(767);
						break;
					case 12:
						printEscapeSequence(768);
						break;
					case 13:
						printEscapeSequence(769);
						break;
					case 14:
						printEscapeSequence(770);
						break;
					case 15:
						printEscapeSequence(771);
						break;
					case 16:
						printEscapeSequence(772);
						break;
					default:
						printEscapeSequence(756);
						break;
				}
				break;
			case 106:
				if (chrcode == 2) {
					printEscapeSequence(773);
				} else if (chrcode == 4) {
					printEscapeSequence(774);
				} else {
					printEscapeSequence(775);
				}
				break;
			case 4:
				if (chrcode == 256) {
					printEscapeSequence(898);
				} else {
					print(902);
					print(chrcode);
				}
				break;
			case 5:
				if (chrcode == 257) {
					printEscapeSequence(899);
				} else {
					printEscapeSequence(900);
				}
				break;
			case 81:
				switch (chrcode) {
					case 0:
						printEscapeSequence(970);
						break;
					case 1:
						printEscapeSequence(971);
						break;
					case 2:
						printEscapeSequence(972);
						break;
					case 3:
						printEscapeSequence(973);
						break;
					case 4:
						printEscapeSequence(974);
						break;
					case 5:
						printEscapeSequence(975);
						break;
					case 6:
						printEscapeSequence(976);
						break;
					default:
						printEscapeSequence(977);
						break;
				}
				break;
			case 14:
				if (chrcode == 1) {
					printEscapeSequence(1026);
				} else {
					printEscapeSequence(1025);
				}
				break;
			case 26:
				switch (chrcode) {
					case 4:
						printEscapeSequence(1027);
						break;
					case 0:
						printEscapeSequence(1028);
						break;
					case 1:
						printEscapeSequence(1029);
						break;
					case 2:
						printEscapeSequence(1030);
						break;
					default:
						printEscapeSequence(1031);
						break;
				}
				break;
			case 27:
				switch (chrcode) {
					case 4:
						printEscapeSequence(1032);
						break;
					case 0:
						printEscapeSequence(1033);
						break;
					case 1:
						printEscapeSequence(1034);
						break;
					case 2:
						printEscapeSequence(1035);
						break;
					default:
						printEscapeSequence(1036);
						break;
				}
				break;
			case 28:
				printEscapeSequence(336);
				break;
			case 29:
				printEscapeSequence(340);
				break;
			case 30:
				printEscapeSequence(342);
				break;
			case 21:
				if (chrcode == 1) {
					printEscapeSequence(1054);
				} else {
					printEscapeSequence(1055);
				}
				break;
			case 22:
				if (chrcode == 1) {
					printEscapeSequence(1056);
				} else {
					printEscapeSequence(1057);
				}
				break;
			case 20:
				switch (chrcode) {
					case 0:
						printEscapeSequence(409);
						break;
					case 1:
						printEscapeSequence(1058);
						break;
					case 2:
						printEscapeSequence(1059);
						break;
					case 3:
						printEscapeSequence(965);
						break;
					case 4:
						printEscapeSequence(1060);
						break;
					case 5:
						printEscapeSequence(967);
						break;
					default:
						printEscapeSequence(1061);
						break;
				}
				break;
			case 31:
				if (chrcode == 100) {
					printEscapeSequence(1063);
				} else if (chrcode == 101) {
					printEscapeSequence(1064);
				} else if (chrcode == 102) {
					printEscapeSequence(1065);
				} else {
					printEscapeSequence(1062);
				}
				break;
			case 43:
				if (chrcode == 0) {
					printEscapeSequence(1081);
				} else {
					printEscapeSequence(1080);
				}
				break;
			case 25:
				if (chrcode == 10) {
					printEscapeSequence(1092);
				} else if (chrcode == 11) {
					printEscapeSequence(1091);
				} else {
					printEscapeSequence(1090);
				}
				break;
			case 23:
				if (chrcode == 1) {
					printEscapeSequence(1094);
				} else {
					printEscapeSequence(1093);
				}
				break;
			case 24:
				if (chrcode == 1) {
					printEscapeSequence(1096);
				} else {
					printEscapeSequence(1095);
				}
				break;
			case 47:
				if (chrcode == 1) {
					printEscapeSequence(45);
				} else {
					printEscapeSequence(349);
				}
				break;
			case 48:
				if (chrcode == 1) {
					printEscapeSequence(1128);
				} else {
					printEscapeSequence(1127);
				}
				break;
			case 50:
				switch (chrcode) {
					case 16:
						printEscapeSequence(866);
						break;
					case 17:
						printEscapeSequence(867);
						break;
					case 18:
						printEscapeSequence(868);
						break;
					case 19:
						printEscapeSequence(869);
						break;
					case 20:
						printEscapeSequence(870);
						break;
					case 21:
						printEscapeSequence(871);
						break;
					case 22:
						printEscapeSequence(872);
						break;
					case 23:
						printEscapeSequence(873);
						break;
					case 26:
						printEscapeSequence(875);
						break;
					default:
						printEscapeSequence(874);
						break;
				}
				break;
			case 51:
				if (chrcode == 1) {
					printEscapeSequence(878);
				} else if (chrcode == 2) {
					printEscapeSequence(879);
				} else {
					printEscapeSequence(1129);
				}
				break;
			case 53:
				printstyle(chrcode);
				break;
			case 52:
				switch (chrcode) {
					case 1:
						printEscapeSequence(1148);
						break;
					case 2:
						printEscapeSequence(1149);
						break;
					case 3:
						printEscapeSequence(1150);
						break;
					case 4:
						printEscapeSequence(1151);
						break;
					case 5:
						printEscapeSequence(1152);
						break;
					default:
						printEscapeSequence(1147);
						break;
				}
				break;
			case 49:
				if (chrcode == 30) {
					printEscapeSequence(876);
				} else {
					printEscapeSequence(877);
				}
				break;
			case 93:
				if (chrcode == 1) {
					printEscapeSequence(1171);
				} else if (chrcode == 2) {
					printEscapeSequence(1172);
				} else {
					printEscapeSequence(1173);
				}
				break;
			case 97:
				if (chrcode == 0) {
					printEscapeSequence(1174);
				} else if (chrcode == 1) {
					printEscapeSequence(1175);
				} else if (chrcode == 2) {
					printEscapeSequence(1176);
				} else {
					printEscapeSequence(1177);
				}
				break;
			case 94:
				if (chrcode != 0) {
					printEscapeSequence(1192);
				} else {
					printEscapeSequence(1191);
				}
				break;
			case 95:
				switch (chrcode) {
					case 0:
						printEscapeSequence(1193);
						break;
					case 1:
						printEscapeSequence(1194);
						break;
					case 2:
						printEscapeSequence(1195);
						break;
					case 3:
						printEscapeSequence(1196);
						break;
					case 4:
						printEscapeSequence(1197);
						break;
					case 5:
						printEscapeSequence(1198);
						break;
					default:
						printEscapeSequence(1199);
						break;
				}
				break;
			case 68: {
				printEscapeSequence(513);
				printHex(chrcode);
			}
				break;
			case 69: {
				printEscapeSequence(524);
				printHex(chrcode);
			}
				break;
			case 85:
				if (chrcode == 8283) {
					printEscapeSequence(415);
				} else if (chrcode == 9307) {
					printEscapeSequence(419);
				} else if (chrcode == 8539) {
					printEscapeSequence(416);
				} else if (chrcode == 8795) {
					printEscapeSequence(417);
				} else if (chrcode == 9051) {
					printEscapeSequence(418);
				} else {
					printEscapeSequence(477);
				}
				break;
			case 86:
				printsize(chrcode - 8235);
				break;
			case 99:
				if (chrcode == 1) {
					printEscapeSequence(953);
				} else {
					printEscapeSequence(941);
				}
				break;
			case 78:
				if (chrcode == 0) {
					printEscapeSequence(1217);
				} else {
					printEscapeSequence(1218);
				}
				break;
			case 87: {
				print(1226);
				print(fontname[chrcode]);
				if (fontsize[chrcode] != fontdsize[chrcode]) {
					print(741);
					printFixed(fontsize[chrcode]);
					print(397);
				}
			}
				break;
			case 100:
				switch (chrcode) {
					case 0:
						printEscapeSequence(274);
						break;
					case 1:
						printEscapeSequence(275);
						break;
					case 2:
						printEscapeSequence(276);
						break;
					default:
						printEscapeSequence(1227);
						break;
				}
				break;
			case 60:
				if (chrcode == 0) {
					printEscapeSequence(1229);
				} else {
					printEscapeSequence(1228);
				}
				break;
			case 58:
				if (chrcode == 0) {
					printEscapeSequence(1230);
				} else {
					printEscapeSequence(1231);
				}
				break;
			case 57:
				if (chrcode == 8539) {
					printEscapeSequence(1237);
				} else {
					printEscapeSequence(1238);
				}
				break;
			case 19:
				switch (chrcode) {
					case 1:
						printEscapeSequence(1240);
						break;
					case 2:
						printEscapeSequence(1241);
						break;
					case 3:
						printEscapeSequence(1242);
						break;
					default:
						printEscapeSequence(1239);
						break;
				}
				break;
			case 101:
				print(1249);
				break;
			case 111:
				print(1250);
				break;
			case 112:
				printEscapeSequence(1251);
				break;
			case 113:
				printEscapeSequence(1252);
				break;
			case 114: {
				printEscapeSequence(1171);
				printEscapeSequence(1252);
			}
				break;
			case 115:
				printEscapeSequence(1253);
				break;
			case 59:
				switch (chrcode) {
					case 0:
						printEscapeSequence(1285);
						break;
					case 1:
						printEscapeSequence(594);
						break;
					case 2:
						printEscapeSequence(1286);
						break;
					case 3:
						printEscapeSequence(1287);
						break;
					case 4:
						printEscapeSequence(1288);
						break;
					case 5:
						printEscapeSequence(1289);
						break;
					default:
						print(1290);
						break;
				}
				break;
			default:
				print(567);
				break;
		}
	}

	void printmeaning() {
		printcmdchr(curcmd, curchr);
		if (curcmd >= 111) {
			printchar(58);
			println();
			tokenshow(curchr);
		} else if (curcmd == 110) {
			printchar(58);
			println();
			tokenshow(curmark[curchr]);
		}
	}

	void printtotals() {
		printFixed(pagesofar[1]);
		if (pagesofar[2] != 0) {
			print(312);
			printFixed(pagesofar[2]);
			print(338);
		}
		if (pagesofar[3] != 0) {
			print(312);
			printFixed(pagesofar[3]);
			print(311);
		}
		if (pagesofar[4] != 0) {
			print(312);
			printFixed(pagesofar[4]);
			print(978);
		}
		if (pagesofar[5] != 0) {
			print(312);
			printFixed(pagesofar[5]);
			print(979);
		}
		if (pagesofar[6] != 0) {
			print(313);
			printFixed(pagesofar[6]);
		}
	}

	void shownodelist(int p) {
		int n;
		double g;
		if (stringPool.getBuiltLength() > depththreshold) {
			if (p > 0) {
				print(314);
			}
			return /* lab10 */;
		}
		n = 0;
		while (p > 0) {
			println();
			printcurrentstring();
			if (p > memend) {
				print(315);
				return /* lab10 */;
			}
			n = n + 1;
			if (n > breadthmax) {
				print(316);
				return /* lab10 */;
			}
			if ((p >= himemmin)) {
				printfontandchar(p);
			} else {
				switch (mem[p].getb0()) {
					case 0:
					case 1:
					case 13: {
						if (mem[p].getb0() == 0) {
							printEscapeSequence(104);
						} else if (mem[p].getb0() == 1) {
							printEscapeSequence(118);
						} else {
							printEscapeSequence(318);
						}
						print(319);
						printFixed(mem[p + 3].getInt());
						printchar(43);
						printFixed(mem[p + 2].getInt());
						print(320);
						printFixed(mem[p + 1].getInt());
						if (mem[p].getb0() == 13) {
							if (mem[p].getb1() != 0) {
								print(286);
								printInt(mem[p].getb1() + 1);
								print(322);
							}
							if (mem[p + 6].getInt() != 0) {
								print(323);
								printglue(mem[p + 6].getInt(), mem[p + 5].getb1(), 0);
							}
							if (mem[p + 4].getInt() != 0) {
								print(324);
								printglue(mem[p + 4].getInt(), mem[p + 5].getb0(), 0);
							}
						} else {
							g = mem[p + 6].getglue();
							if ((g != 0.0) && (mem[p + 5].getb0() != 0)) {
								print(325);
								if (mem[p + 5].getb0() == 2) {
									print(326);
								}
								if (Math.abs(mem[p + 6].getInt()) < 1048576) {
									print(327);
								} else if (Math.abs(g) > 20000.0) {
									if (g > 0.0) {
										printchar(62);
									} else {
										print(328);
									}
									printglue(20000 * 65536, mem[p + 5].getb1(), 0);
								} else {
									printglue((int)Math.round(65536 * g), mem[p + 5].getb1(), 0);
								}
							}
							if (mem[p + 4].getInt() != 0) {
								print(321);
								printFixed(mem[p + 4].getInt());
							}
						}
						stringPool.append((char)46);
						shownodelist(mem[p + 5].getrh());
						poolptr = poolptr - 1;
					}
						break;
					case 2: {
						printEscapeSequence(329);
						printruledimen(mem[p + 3].getInt());
						printchar(43);
						printruledimen(mem[p + 2].getInt());
						print(320);
						printruledimen(mem[p + 1].getInt());
					}
						break;
					case 3: {
						printEscapeSequence(330);
						printInt(mem[p].getb1());
						print(331);
						printFixed(mem[p + 3].getInt());
						print(332);
						printspec(mem[p + 4].getrh(), 0);
						printchar(44);
						printFixed(mem[p + 2].getInt());
						print(333);
						printInt(mem[p + 1].getInt());
						stringPool.append((char)46);
						shownodelist(mem[p + 4].getlh());
						poolptr = poolptr - 1;
					}
						break;
					case 8:
						switch (mem[p].getb1()) {
							case 0: {
								printwritewhatsit(1285, p);
								printchar(61);
								printfilename(mem[p + 1].getrh(), mem[p + 2].getlh(), mem[p + 2].getrh());
							}
								break;
							case 1: {
								printwritewhatsit(594, p);
								printmark(mem[p + 1].getrh());
							}
								break;
							case 2:
								printwritewhatsit(1286, p);
								break;
							case 3: {
								printEscapeSequence(1287);
								printmark(mem[p + 1].getrh());
							}
								break;
							case 4: {
								printEscapeSequence(1289);
								printInt(mem[p + 1].getrh());
								print(1292);
								printInt(mem[p + 1].getb0());
								printchar(44);
								printInt(mem[p + 1].getb1());
								printchar(41);
							}
								break;
							default:
								print(1293);
								break;
						}
						break;
					case 10:
						if (mem[p].getb1() >= 100) {
							printEscapeSequence(338);
							if (mem[p].getb1() == 101) {
								printchar(99);
							} else if (mem[p].getb1() == 102) {
								printchar(120);
							}
							print(339);
							printspec(mem[p + 1].getlh(), 0);
							stringPool.append((char)46);
							shownodelist(mem[p + 1].getrh());
							poolptr = poolptr - 1;
						} else {
							printEscapeSequence(334);
							if (mem[p].getb1() != 0) {
								printchar(40);
								if (mem[p].getb1() < 98) {
									printskipparam(mem[p].getb1() - 1);
								} else if (mem[p].getb1() == 98) {
									printEscapeSequence(335);
								} else {
									printEscapeSequence(336);
								}
								printchar(41);
							}
							if (mem[p].getb1() != 98) {
								printchar(32);
								if (mem[p].getb1() < 98) {
									printspec(mem[p + 1].getlh(), 0);
								} else {
									printspec(mem[p + 1].getlh(), 337);
								}
							}
						}
						break;
					case 11:
						if (mem[p].getb1() != 99) {
							printEscapeSequence(340);
							if (mem[p].getb1() != 0) {
								printchar(32);
							}
							printFixed(mem[p + 1].getInt());
							if (mem[p].getb1() == 2) {
								print(341);
							}
						} else {
							printEscapeSequence(342);
							printFixed(mem[p + 1].getInt());
							print(337);
						}
						break;
					case 9: {
						printEscapeSequence(343);
						if (mem[p].getb1() == 0) {
							print(344);
						} else {
							print(345);
						}
						if (mem[p + 1].getInt() != 0) {
							print(346);
							printFixed(mem[p + 1].getInt());
						}
					}
						break;
					case 6: {
						printfontandchar(p + 1);
						print(347);
						if (mem[p].getb1() > 1) {
							printchar(124);
						}
						fontinshortdisplay = mem[p + 1].getb0();
						shortdisplay(mem[p + 1].getrh());
						if (((mem[p].getb1()) % 2 == 1)) {
							printchar(124);
						}
						printchar(41);
					}
						break;
					case 12: {
						printEscapeSequence(348);
						printInt(mem[p + 1].getInt());
					}
						break;
					case 7: {
						printEscapeSequence(349);
						if (mem[p].getb1() > 0) {
							print(350);
							printInt(mem[p].getb1());
						}
						stringPool.append((char)46);
						shownodelist(mem[p + 1].getlh());
						poolptr = poolptr - 1;
						stringPool.append((char)124);
						shownodelist(mem[p + 1].getrh());
						poolptr = poolptr - 1;
					}
						break;
					case 4: {
						printEscapeSequence(351);
						printmark(mem[p + 1].getInt());
					}
						break;
					case 5: {
						printEscapeSequence(352);
						stringPool.append((char)46);
						shownodelist(mem[p + 1].getInt());
						poolptr = poolptr - 1;
					}
						break;
					case 14:
						printstyle(mem[p].getb1());
						break;
					case 15: {
						printEscapeSequence(525);
						stringPool.append((char)68);
						shownodelist(mem[p + 1].getlh());
						poolptr = poolptr - 1;
						stringPool.append((char)84);
						shownodelist(mem[p + 1].getrh());
						poolptr = poolptr - 1;
						stringPool.append((char)83);
						shownodelist(mem[p + 2].getlh());
						poolptr = poolptr - 1;
						stringPool.append((char)115);
						shownodelist(mem[p + 2].getrh());
						poolptr = poolptr - 1;
					}
						break;
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					case 22:
					case 23:
					case 24:
					case 27:
					case 26:
					case 29:
					case 28:
					case 30:
					case 31: {
						switch (mem[p].getb0()) {
							case 16:
								printEscapeSequence(866);
								break;
							case 17:
								printEscapeSequence(867);
								break;
							case 18:
								printEscapeSequence(868);
								break;
							case 19:
								printEscapeSequence(869);
								break;
							case 20:
								printEscapeSequence(870);
								break;
							case 21:
								printEscapeSequence(871);
								break;
							case 22:
								printEscapeSequence(872);
								break;
							case 23:
								printEscapeSequence(873);
								break;
							case 27:
								printEscapeSequence(874);
								break;
							case 26:
								printEscapeSequence(875);
								break;
							case 29:
								printEscapeSequence(539);
								break;
							case 24: {
								printEscapeSequence(533);
								printdelimiter(p + 4);
							}
								break;
							case 28: {
								printEscapeSequence(508);
								printfamandchar(p + 4);
							}
								break;
							case 30: {
								printEscapeSequence(876);
								printdelimiter(p + 1);
							}
								break;
							case 31: {
								printEscapeSequence(877);
								printdelimiter(p + 1);
							}
								break;
						}
						if (mem[p].getb1() != 0) {
							if (mem[p].getb1() == 1) {
								printEscapeSequence(878);
							} else {
								printEscapeSequence(879);
							}
						}
						if (mem[p].getb0() < 30) {
							printsubsidiarydata(p + 1, 46);
						}
						printsubsidiarydata(p + 2, 94);
						printsubsidiarydata(p + 3, 95);
					}
						break;
					case 25: {
						printEscapeSequence(880);
						if (mem[p + 1].getInt() == 1073741824) {
							print(881);
						} else {
							printFixed(mem[p + 1].getInt());
						}
						if ((mem[p + 4].getb0() != 0) || (mem[p + 4].getb1() != 0) || (mem[p + 4].getb2() != 0) || (mem[p + 4].getb3() != 0)) {
							print(882);
							printdelimiter(p + 4);
						}
						if ((mem[p + 5].getb0() != 0) || (mem[p + 5].getb1() != 0) || (mem[p + 5].getb2() != 0) || (mem[p + 5].getb3() != 0)) {
							print(883);
							printdelimiter(p + 5);
						}
						printsubsidiarydata(p + 2, 92);
						printsubsidiarydata(p + 3, 47);
					}
						break;
					default:
						print(317);
						break;
				}
			}
			p = mem[p].getrh();
		}
	}

	void showbox(final int p) {
		depththreshold = eqtb[9588].getInt();
		breadthmax = eqtb[9587].getInt();
		if (breadthmax <= 0) {
			breadthmax = 5;
		}
		shownodelist(p);
		println();
	}

	void showactivities() {
		int p;
		int m;
		final memoryword a = new memoryword();
		int q, r;
		int t;
		nest[nestptr].copy(curlist);
		printnl(338);
		println();
		for (p = nestptr; p >= 0; p--) {
			m = nest[p].modefield;
			a.copy(nest[p].auxfield);
			printnl(363);
			printmode(m);
			print(364);
			printInt(Math.abs(nest[p].mlfield));
			if (m == 102) {
				if (nest[p].pgfield != 8585216) {
					print(365);
					printInt(nest[p].pgfield % 65536);
					print(366);
					printInt(nest[p].pgfield / 4194304);
					printchar(44);
					printInt((nest[p].pgfield / 65536) % 64);
					printchar(41);
				}
			}
			if (nest[p].mlfield < 0) {
				print(367);
			}
			if (p == 0) {
				if (memtop - 2 != pagetail) {
					printnl(980);
					if (outputactive) {
						print(981);
					}
					showbox(mem[memtop - 2].getrh());
					if (pagecontents > 0) {
						printnl(982);
						printtotals();
						printnl(983);
						printFixed(pagesofar[0]);
						r = mem[memtop].getrh();
						while (r != memtop) {
							println();
							printEscapeSequence(330);
							t = mem[r].getb1();
							printInt(t);
							print(984);
							t = xovern(mem[r + 3].getInt(), 1000) * eqtb[9618 + t].getInt();
							printFixed(t);
							if (mem[r].getb0() == 1) {
								q = memtop - 2;
								t = 0;
								do {
									q = mem[q].getrh();
									if ((mem[q].getb0() == 3) && (mem[q].getb1() == mem[r].getb1())) {
										t = t + 1;
									}
								} while (!(q == mem[r + 1].getlh()));
								print(985);
								printInt(t);
								print(986);
							}
							r = mem[r].getrh();
						}
					}
				}
				if (mem[memtop - 1].getrh() != 0) {
					printnl(368);
				}
			}
			showbox(mem[nest[p].headfield].getrh());
			switch (Math.abs(m) / (101)) {
				case 0: {
					printnl(369);
					if (a.getInt() <= -65536000) {
						print(370);
					} else {
						printFixed(a.getInt());
					}
					if (nest[p].pgfield != 0) {
						print(371);
						printInt(nest[p].pgfield);
						print(372);
						if (nest[p].pgfield != 1) {
							printchar(115);
						}
					}
				}
					break;
				case 1: {
					printnl(373);
					printInt(a.getlh());
					if (m > 0) {
						if (a.getrh() > 0) {
							print(374);
							printInt(a.getrh());
						}
					}
				}
					break;
				case 2:
					if (a.getInt() != 0) {
						print(375);
						showbox(a.getInt());
					}
					break;
			}
		}
	}

	/**
	 * TODO: remove this. Sets the error level to WARNING and does selector handling
	 * for warning output, but the selector should probably be removed altogether.
	 */
	void begindiagnostic() {
		errorReporter.warning("WARNING"); // TODO dirty: sets the worst error level so far
		oldsetting = selector;
		if ((eqtb[9592].getInt() <= 0) && (selector == 19)) {
			selector = selector - 1;
		}
	}

	/**
	 * TODO: see begindiagnostic
	 */
	void enddiagnostic(final boolean blankline) {
		printnl("");
		if (blankline) {
			println();
		}
		selector = oldsetting;
	}

	void tokenshow(final int p) {
		if (p != 0) {
			showtokenlist(mem[p].getrh(), 0, 10000000);
		}
	}

	void showcurcmdchr() {
		begindiagnostic();
		printnl(123);
		if (curlist.modefield != shownmode) {
			printmode(curlist.modefield);
			print(568);
			shownmode = curlist.modefield;
		}
		printcmdchr(curcmd, curchr);
		printchar(125);
		enddiagnostic(false);
	}

	void showcontext() {
		/* 30 */int oldsetting;
		int nn;
		boolean bottomline;
		int i;
		int j;
		int l;
		int m;
		int n;
		int p;
		int q;
		baseptr = inputptr;
		inputStackBackingArray[baseptr].copyFrom(curinput);
		nn = -1;
		bottomline = false;
		while (true) {
			curinput.copyFrom(inputStackBackingArray[baseptr]);
			if ((curinput.getState() != TOKENIZER_STATE_TOKEN_LIST)) {
				if ((curinput.getName() > 17) || (baseptr == 0)) {
					bottomline = true;
				}
			}
			if ((baseptr == inputptr) || bottomline || (nn < eqtb[9617].getInt())) {
				if ((baseptr == inputptr) || (curinput.getState() != TOKENIZER_STATE_TOKEN_LIST) || (curinput.getIndex() != 3) || (curinput.getLoc() != 0)) {
					tally = 0;
					oldsetting = selector;
					if (curinput.getState() != TOKENIZER_STATE_TOKEN_LIST) {
						if (curinput.getName() <= 17) {
							if ((curinput.getName() == 0)) {
								if (baseptr == 0) {
									printnl(574);
								} else {
									printnl(575);
								}
							} else {
								printnl(576);
								if (curinput.getName() == 17) {
									printchar(42);
								} else {
									printInt(curinput.getName() - 1);
								}
								printchar(62);
							}
						} else {
							printnl(577);
							printInt(line);
						}
						printchar(32);
						{
							l = tally;
							tally = 0;
							selector = 20;
							trickcount = 1000000;
						}
						if (buffer[curinput.getLimit()] == eqtb[9611].getInt()) {
							j = curinput.getLimit();
						} else {
							j = curinput.getLimit() + 1;
						}
						if (j > 0) {
							for (i = curinput.getStart(); i <= j - 1; i++) {
								if (i == curinput.getLoc()) {
									firstcount = tally;
									trickcount = tally + 1 + errorline - halferrorline;
									if (trickcount < errorline) {
										trickcount = errorline;
									}
								}
								print(buffer[i]);
							}
						}
					} else {
						switch (curinput.getIndex()) {
							case 0:
								printnl(578);
								break;
							case 1:
							case 2:
								printnl(579);
								break;
							case 3:
								if (curinput.getLoc() == 0) {
									printnl(580);
								} else {
									printnl(581);
								}
								break;
							case 4:
								printnl(582);
								break;
							case 5: {
								println();
								printcs(curinput.getName());
							}
								break;
							case 6:
								printnl(583);
								break;
							case 7:
								printnl(584);
								break;
							case 8:
								printnl(585);
								break;
							case 9:
								printnl(586);
								break;
							case 10:
								printnl(587);
								break;
							case 11:
								printnl(588);
								break;
							case 12:
								printnl(589);
								break;
							case 13:
								printnl(590);
								break;
							case 14:
								printnl(591);
								break;
							case 15:
								printnl(592);
								break;
							default:
								printnl(63);
								break;
						}
						{
							l = tally;
							tally = 0;
							selector = 20;
							trickcount = 1000000;
						}
						if (curinput.getIndex() < 5) {
							showtokenlist(curinput.getStart(), curinput.getLoc(), 100000);
						} else {
							showtokenlist(mem[curinput.getStart()].getrh(), curinput.getLoc(), 100000);
						}
					}
					selector = oldsetting;
					if (trickcount == 1000000) {
						firstcount = tally;
						trickcount = tally + 1 + errorline - halferrorline;
						if (trickcount < errorline) {
							trickcount = errorline;
						}
					}
					if (tally < trickcount) {
						m = tally - firstcount;
					} else {
						m = trickcount - firstcount;
					}
					if (l + firstcount <= halferrorline) {
						p = 0;
						n = l + firstcount;
					} else {
						print(277);
						p = l + firstcount - halferrorline + 3;
						n = halferrorline;
					}
					for (q = p; q <= firstcount - 1; q++) {
						printchar(trickbuf[q % errorline]);
					}
					println();
					for (q = 1; q <= n; q++) {
						printchar(32);
					}
					if (m + n <= errorline) {
						p = firstcount + m;
					} else {
						p = firstcount + (errorline - n - 3);
					}
					for (q = firstcount; q <= p - 1; q++) {
						printchar(trickbuf[q % errorline]);
					}
					if (m + n > errorline) {
						print(277);
					}
					nn = nn + 1;
				}
			} else if (nn == eqtb[9617].getInt()) {
				printnl(277);
				nn = nn + 1;
			}
			if (bottomline) {
				break /* lab30 */;
			}
			baseptr = baseptr - 1;
		}
		/* lab30: */curinput.copyFrom(inputStackBackingArray[inputptr]);
	}

	void showinfo() {
		shownodelist(mem[tempptr].getlh());
	}

	void showwhatever() {
		lab50: while (true) {
			switch (curchr) {
				case 3: {
					begindiagnostic();
					showactivities();
				}
					break;
				case 1: {
					scaneightbitint();
					begindiagnostic();
					printnl(1254);
					printInt(curval);
					printchar(61);
					if (eqtb[7978 + curval].getrh() == 0) {
						print(410);
					} else {
						showbox(eqtb[7978 + curval].getrh());
					}
				}
					break;
				case 0: {
					gettoken();
					printnl(1248);
					if (curcs != 0) {
						sprintcs(curcs);
						printchar(61);
					}
					printmeaning();
					break lab50;
				}
				default: {
					thetoks();
					printnl(1248);
					tokenshow(memtop - 3);
					flushlist(mem[memtop - 3].getrh());
					break lab50;
				}
			}
			enddiagnostic(true);
			{
				printnl(262);
				print(1255);
			}
			if (selector == 19) {
				if (eqtb[9592].getInt() <= 0) {
					selector = 17;
					print(1256);
					selector = 19;
				}
			}
			break;
		}
		helpptr = 0;
		errorReporter.decrementErrorCount();
		errorLogic.error();
	}

	// ------------------------------------------------------------------------------------------------
	// error routines
	// ------------------------------------------------------------------------------------------------

	void showtokenlist(int p, final int q, final int l) {
		/* 10 */int m, c;
		int matchchr;
		int n;
		matchchr = 35;
		n = 48;
		tally = 0;
		while ((p != 0) && (tally < l)) {
			if (p == q) {
				firstcount = tally;
				trickcount = tally + 1 + errorline - halferrorline;
				if (trickcount < errorline) {
					trickcount = errorline;
				}
			}
			if ((p < himemmin) || (p > memend)) {
				printEscapeSequence(309);
				return /* lab10 */;
			}
			if (mem[p].getlh() >= 4095) {
				printcs(mem[p].getlh() - 4095);
			} else {
				m = mem[p].getlh() / 256;
				c = mem[p].getlh() % 256;
				if (mem[p].getlh() < 0) {
					printEscapeSequence(555);
				} else {
					switch (m) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 7:
						case 8:
						case 10:
						case 11:
						case 12:
							print(c);
							break;
						case 6: {
							print(c);
							print(c);
						}
							break;
						case 5: {
							print(matchchr);
							if (c <= 9) {
								printchar(c + 48);
							} else {
								printchar(33);
								return /* lab10 */;
							}
						}
							break;
						case 13: {
							matchchr = c;
							print(c);
							n = n + 1;
							printchar(n);
							if (n > 57) {
								return /* lab10 */;
							}
						}
							break;
						case 14:
							print(556);
							break;
						default:
							printEscapeSequence(555);
							break;
					}
				}
			}
			p = mem[p].getrh();
		}
		if (p != 0) {
			printEscapeSequence(554);
		}
	}

	void runaway() {
		int p;
		p = 0;
		if (scannerstatus > 1) {
			printnl(569);
			switch (scannerstatus) {
				case 2: {
					print(570);
					p = defref;
				}
					break;
				case 3: {
					print(571);
					p = memtop - 3;
				}
					break;
				case 4: {
					print(572);
					p = memtop - 4;
				}
					break;
				case 5: {
					print(573);
					p = defref;
				}
					break;
			}
			printchar(63);
			println();
			showtokenlist(mem[p].getrh(), 0, errorline - 10);
		}
	}

	void boxerror(final int n) {
		errorLogic.error();
		begindiagnostic();
		printnl(836);
		showbox(eqtb[7978 + n].getrh());
		enddiagnostic(true);
		flushnodelist(eqtb[7978 + n].getrh());
		eqtb[7978 + n].setrh(0);
	}

}
