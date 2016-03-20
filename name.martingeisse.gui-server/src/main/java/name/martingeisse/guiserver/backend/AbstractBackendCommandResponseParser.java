/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Base implementation of {@link IBackendCommandResponseParser} that
 * keeps a (key, command parser) map to parse commands using a "known"
 * key.  
 */
public class AbstractBackendCommandResponseParser implements IBackendCommandResponseParser {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(AbstractBackendCommandResponseParser.class);
	
	/**
	 * the parsers
	 */
	private final Map<String, IBackendCommandParser> parsers;

	/**
	 * Constructor.
	 * @param parsers the parsers
	 */
	public AbstractBackendCommandResponseParser(Map<String, IBackendCommandParser> parsers) {
		this.parsers = ImmutableMap.copyOf(parsers);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.backend.IBackendCommandResponseParser#parseBackendCommandResponse(name.martingeisse.common.javascript.analyze.JsonAnalyzer)
	 */
	@Override
	public BackendCommandResponse parseBackendCommandResponse(JsonAnalyzer json) {
		List<IBackendCommand> commands = new ArrayList<>();
		for (Map.Entry<String, JsonAnalyzer> entry : json.analyzeMap().entrySet()) {
			IBackendCommand command = parseCommand(entry.getKey(), entry.getValue());
			if (command != null) {
				commands.add(command);
			}
		}
		return createResponse(commands);
	}

	/**
	 * Parses a single command. The default implementation chooses the parser based on
	 * the key and delegates to {@link #parseCommand(String, IBackendCommandParser, JsonAnalyzer)}.
	 * 
	 * @param key the command key
	 * @param json the JSON
	 * @return the parsed command, or null on parse errors
	 */
	protected IBackendCommand parseCommand(String key, JsonAnalyzer json) {
		IBackendCommandParser parser = parsers.get(key);
		if (parser == null) {
			logger.error("unknown backend command key: " + key);
			return null;
		}
		return parseCommand(key, parser, json);
	}

	/**
	 * Parses a single command using the specified parser.
	 * 
	 * @param key the command key
	 * @param parser the parser
	 * @param json the JSON
	 * @return the parsed command, or null on parse errors
	 */
	protected IBackendCommand parseCommand(String key, IBackendCommandParser parser, JsonAnalyzer json) {
		return parser.parseBackendCommand(json);
	}

	/**
	 * Creates the command response from the list of commands.
	 * @param commands the commands
	 * @return the command response
	 */
	protected BackendCommandResponse createResponse(List<IBackendCommand> commands) {
		return new BackendCommandResponse(ImmutableList.copyOf(commands));
	}

}
