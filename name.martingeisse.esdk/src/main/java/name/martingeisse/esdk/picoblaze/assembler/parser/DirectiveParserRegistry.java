package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.esdk.picoblaze.assembler.PicoblazeAssemblerOpcodes;

/**
 * This class statically holds all {@link DirectiveParser}s and
 * allows to look them up by their name taken from a .psm source
 * file.
 * 
 * This is a singleton class.
 */
public class DirectiveParserRegistry {

	/**
	 * The singleton instance of this class
	 */
	public static final DirectiveParserRegistry instance = new DirectiveParserRegistry();

	/**
	 * the byName
	 */
	private final Map<String, DirectiveParser> byName;

	/**
	 * Constructor.
	 */
	public DirectiveParserRegistry() {
		byName = new HashMap<String, DirectiveParser>();
		byName.put("namereg", new NameregParser());
		byName.put("constant", new ConstantParser());
		byName.put("address", new AddressParser());
		byName.put("load", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_LOAD));
		byName.put("add", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_ADD));
		byName.put("addcy", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_ADDCY));
		byName.put("sub", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_SUB));
		byName.put("subcy", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_SUBCY));
		byName.put("and", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_AND));
		byName.put("or", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_OR));
		byName.put("xor", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_XOR));
		byName.put("compare", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_COMPARE));
		byName.put("test", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_TEST));
		byName.put("input", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_INPUT));
		byName.put("output", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_OUTPUT));
		byName.put("store", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_STORE));
		byName.put("fetch", new InstructionRXParser(PicoblazeAssemblerOpcodes.OPCODE_FETCH));
		byName.put("rl", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_RL));
		byName.put("rr", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_RR));
		byName.put("sl0", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SL0));
		byName.put("sl1", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SL1));
		byName.put("sla", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SLA));
		byName.put("slx", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SLX));
		byName.put("sr0", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SR0));
		byName.put("sr1", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SR1));
		byName.put("sra", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SRA));
		byName.put("srx", new InstructionRParser(PicoblazeAssemblerOpcodes.OPCODE_SRX));
		byName.put("jump", new InstructionJParser(PicoblazeAssemblerOpcodes.OPCODE_JUMP));
		byName.put("call", new InstructionJParser(PicoblazeAssemblerOpcodes.OPCODE_CALL));
		byName.put("return", new InstructionJParser(PicoblazeAssemblerOpcodes.OPCODE_RETURN));
		byName.put("disable", new EnableDisableInterruptParser(PicoblazeAssemblerOpcodes.OPCODE_DISABLE_INTERRUPT));
		byName.put("enable", new EnableDisableInterruptParser(PicoblazeAssemblerOpcodes.OPCODE_ENABLE_INTERRUPT));
		byName.put("returni", new ReturniParser());
	}

	/**
	 * Returns the directive parser with the specified name. The look-up is done in a
	 * case-insensitive way.
	 * @param name the name to look for
	 * @return the directive parser, or null if none was found
	 */
	public DirectiveParser get(final String name) {
		return byName.get(name.toLowerCase());
	}

}
