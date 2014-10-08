/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.io.PrintWriter;
import java.io.Writer;

import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.builtin.io.BasenameFunction;
import name.martingeisse.phunky.runtime.builtin.io.EchoFunction;
import name.martingeisse.phunky.runtime.builtin.io.PrintFunction;
import name.martingeisse.phunky.runtime.builtin.string.Bin2HexFunction;
import name.martingeisse.phunky.runtime.builtin.string.ChrFuntion;
import name.martingeisse.phunky.runtime.builtin.string.ExplodeFunction;
import name.martingeisse.phunky.runtime.builtin.string.Hex2BinFunction;
import name.martingeisse.phunky.runtime.builtin.string.ImplodeFunction;
import name.martingeisse.phunky.runtime.builtin.string.LevenshteinFunction;
import name.martingeisse.phunky.runtime.builtin.string.OrdFuntion;
import name.martingeisse.phunky.runtime.builtin.string.StrRepeatFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrReplaceFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrlenFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrrevFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrtolowerFunction;
import name.martingeisse.phunky.runtime.builtin.string.StrtoupperFunction;
import name.martingeisse.phunky.runtime.builtin.string.SubstrFunction;
import name.martingeisse.phunky.runtime.builtin.string.TrimFunction;
import name.martingeisse.phunky.runtime.builtin.string.UcLcFirstCharacterFunction;
import name.martingeisse.phunky.runtime.builtin.string.hash.Md5Function;
import name.martingeisse.phunky.runtime.builtin.string.hash.Sha1Function;
import name.martingeisse.phunky.runtime.builtin.system.ConstantFunction;
import name.martingeisse.phunky.runtime.builtin.system.DefineFunction;
import name.martingeisse.phunky.runtime.builtin.system.DefinedFunction;
import name.martingeisse.phunky.runtime.builtin.system.DieFunction;
import name.martingeisse.phunky.runtime.builtin.system.IncludeFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsArrayFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsBoolFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsFloatFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsIntFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsNumericFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsScalarFunction;
import name.martingeisse.phunky.runtime.builtin.var.IsStringFunction;
import name.martingeisse.phunky.runtime.builtin.var.PrintrFunction;
import name.martingeisse.phunky.runtime.builtin.var.UnsetFunction;
import name.martingeisse.phunky.runtime.builtin.var.VarDumpFunction;
import name.martingeisse.phunky.runtime.code.expression.ConstantExpression;

import org.apache.log4j.Logger;

/**
 * This class represents the whole PHP runtime environment.
 */
public final class PhpRuntime {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(PhpRuntime.class);
	
	/**
	 * the globalEnvironment
	 */
	private final Environment globalEnvironment;
	
	/**
	 * the constants
	 */
	private final Constants constants;
	
	/**
	 * the functions
	 */
	private final Functions functions;
	
	/**
	 * the interpreter
	 */
	private final SourceFileInterpreter interpreter;
	
	/**
	 * the log
	 */
	private final RuntimeLog log;
	
	/**
	 * the outputWriter
	 */
	private PrintWriter outputWriter;

	/**
	 * Constructor for a standard PHP runtime.
	 */
	public PhpRuntime() {
		this(true);
	}

	/**
	 * Constructor.
	 * @param standardDefinitions whether to apply standard definitions.
	 * Passing true here has the same effect as calling {@link #applyStandardDefinitions()}.
	 */
	public PhpRuntime(boolean standardDefinitions) {
		this.globalEnvironment = new Environment(this);
		this.constants = new Constants(this);
		this.functions = new Functions(this);
		this.interpreter = new SourceFileInterpreter(this);
		this.log = new RuntimeLog();
		this.outputWriter = new PrintWriter(System.out);
		if (standardDefinitions) {
			applyStandardDefinitions();
		}
	}
	
	/**
	 * Getter method for the globalEnvironment.
	 * @return the globalEnvironment
	 */
	public Environment getGlobalEnvironment() {
		return globalEnvironment;
	}

	/**
	 * Getter method for the constants.
	 * @return the constants
	 */
	public Constants getConstants() {
		return constants;
	}
	
	/**
	 * Getter method for the functions.
	 * @return the functions
	 */
	public Functions getFunctions() {
		return functions;
	}

	/**
	 * Getter method for the interpreter.
	 * @return the interpreter
	 */
	public SourceFileInterpreter getInterpreter() {
		return interpreter;
	}
	
	/**
	 * Getter method for the log.
	 * @return the log
	 */
	public RuntimeLog getLog() {
		return log;
	}
	
	/**
	 * Getter method for the outputWriter.
	 * @return the outputWriter
	 */
	public PrintWriter getOutputWriter() {
		return outputWriter;
	}
	
	/**
	 * Setter method for the outputWriter.
	 * @param outputWriter the outputWriter to set
	 */
	public void setOutputWriter(PrintWriter outputWriter) {
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Setter method for the outputWriter. This method wraps
	 * the specified writer with a {@link PrintWriter}.
	 * @param outputWriter the outputWriter to set
	 */
	public void setOutputWriter(Writer outputWriter) {
		this.outputWriter = new PrintWriter(outputWriter);
	}
	
	/**
	 * Flushes any pending output.
	 */
	public void flushOutputWriter() {
		outputWriter.flush();
	}
	
	/**
	 * Applies standard definitions to this runtime
	 */
	public void applyStandardDefinitions() {
		
		// I/O functions
		addBuiltinCallables(new EchoFunction().setName("echo"));
		addBuiltinCallables(new PrintFunction().setName("print"));
		addBuiltinCallables(new BasenameFunction().setName("basename"));
		
		// string functions
		addBuiltinCallables(new Bin2HexFunction().setName("bin2hex"));
		addBuiltinCallables(new ChrFuntion().setName("chr"));
		addBuiltinCallables(new ExplodeFunction().setName("explode"));
		// TODO find-substring functions
		addBuiltinCallables(new Hex2BinFunction().setName("hex2bin"));
		addBuiltinCallables(new ImplodeFunction().setName("implode"));
		addBuiltinCallables(new LevenshteinFunction().setName("levenshtein"));
		addBuiltinCallables(new ImplodeFunction().setName("join"));
		addBuiltinCallables(new OrdFuntion().setName("ord"));
		addBuiltinCallables(new StrlenFunction().setName("strlen"));
		addBuiltinCallables(new StrRepeatFunction().setName("str_repeat"));
		addBuiltinCallables(new StrReplaceFunction().setName("str_replace"));
		addBuiltinCallables(new StrrevFunction().setName("strrev"));
		addBuiltinCallables(new StrtolowerFunction().setName("strtolower"));
		addBuiltinCallables(new StrtoupperFunction().setName("strtoupper"));
		addBuiltinCallables(new SubstrFunction().setName("substr"));
		addBuiltinCallables(new TrimFunction(true, false).setName("ltrim"));
		addBuiltinCallables(new TrimFunction(false, true).setName("rtrim"));
		addBuiltinCallables(new TrimFunction(false, true).setName("chop"));
		addBuiltinCallables(new TrimFunction(true, true).setName("trim"));
		addBuiltinCallables(new UcLcFirstCharacterFunction(true).setName("ucfirst"));
		addBuiltinCallables(new UcLcFirstCharacterFunction(false).setName("lcfirst"));
		
		// hash functions
		addBuiltinCallables(new Md5Function().setName("md5"));
		addBuiltinCallables(new Sha1Function().setName("sha1"));
		
		// system functions
		addBuiltinCallables(new ConstantFunction().setName("constant"));
		addBuiltinCallables(new DefinedFunction().setName("defined"));
		addBuiltinCallables(new DefineFunction().setName("define"));
		addBuiltinCallables(new DieFunction().setName("die"));
		addBuiltinCallables(new DieFunction().setName("exit"));
		addBuiltinCallables(new IncludeFunction(false, false).setName("include"));
		addBuiltinCallables(new IncludeFunction(true, false).setName("include_once"));
		addBuiltinCallables(new IncludeFunction(false, true).setName("require"));
		addBuiltinCallables(new IncludeFunction(true, true).setName("require_once"));
		
		// variable handling
		addBuiltinCallables(new IsArrayFunction().setName("is_array"));
		addBuiltinCallables(new IsBoolFunction().setName("is_bool"));
		addBuiltinCallables(new IsFloatFunction().setName("is_float"));
		addBuiltinCallables(new IsFloatFunction().setName("is_double"));
		addBuiltinCallables(new IsFloatFunction().setName("is_real"));
		addBuiltinCallables(new IsIntFunction().setName("is_int"));
		addBuiltinCallables(new IsIntFunction().setName("is_integer"));
		addBuiltinCallables(new IsIntFunction().setName("is_long"));
		addBuiltinCallables(new IsNumericFunction().setName("is_numeric"));
		addBuiltinCallables(new IsScalarFunction().setName("is_scalar"));
		addBuiltinCallables(new IsStringFunction().setName("is_string"));
		addBuiltinCallables(new VarDumpFunction().setName("var_dump"));
		addBuiltinCallables(new PrintrFunction().setName("print_r"));
		addBuiltinCallables(new UnsetFunction().setName("unset"));
		
	}
	
	/**
	 * 
	 */
	private void addBuiltinCallables(BuiltinCallable... callables) {
		for (BuiltinCallable callable : callables) {
			this.functions.put(callable.getName(), callable);
		}
	}
	
	/**
	 * Called when an error occurs. This method prints the error to System.err.
	 * @param message the error message
	 */
	public void triggerError(String message) {
		logger.error(message);
	}

	/**
	 * Called when a fatal error occurs. This method prints the error to System.err,
	 * then throws a {@link FatalErrorException}.
	 * @param message the error message
	 */
	public void triggerFatalError(String message) {
		logger.fatal(message);
		throw new FatalErrorException(message);
	}
	
	/**
	 * This method is called when code refers to an undefined constant. It resolves
	 * certain "magic" constants, and if none of them matches the name, triggers
	 * an error.
	 * 
	 * @param constantExpression the constant expression that referred to an undefined constant
	 * @return the value
	 */
	public Object onUndefinedConstant(ConstantExpression constantExpression) {
		if (constantExpression.getName().equals("__FILE__")) {
			return constantExpression.getLocation().getFilePath();
		} else if (constantExpression.getName().equals("__LINE__")) {
			return constantExpression.getLocation().getLine() + 1;
		} else {
			triggerError("undefined constant: " + constantExpression.getName());
			return null;
		}
	}

}
