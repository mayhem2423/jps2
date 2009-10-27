package com.jps2.core.cpu;

import java.math.BigInteger;
import java.text.DecimalFormat;

import com.jps2.core.cpu.r5900.R5900;


/**
 * 
 */
public class Common {

	public static abstract class Instruction {

		private int		        m_count		             = 0;
		private int		        flags		             = 0;
		public final static int	NO_FLAGS		         = 0;
		public final static int	FLAG_INTERPRETED		 = (1 << 0);
		public final static int	FLAG_CANNOT_BE_SPLIT		= (1 << 1);
		public final static int	FLAG_HAS_DELAY_SLOT		 = (1 << 2);
		public final static int	FLAG_IS_BRANCHING		 = (1 << 3);
		public final static int	FLAG_IS_JUMPING		     = (1 << 4);
		public final static int	FLAG_IS_CONDITIONAL		 = (1 << 5);
		public final static int	FLAG_STARTS_NEW_BLOCK		= (1 << 6);
		public final static int	FLAG_ENDS_BLOCK		     = (1 << 7);
		public final static int	FLAGS_BRANCH_INSTRUCTION	= FLAG_CANNOT_BE_SPLIT | FLAG_HAS_DELAY_SLOT | FLAG_IS_BRANCHING | FLAG_IS_CONDITIONAL;
		public final static int	FLAGS_LINK_INSTRUCTION		= FLAG_HAS_DELAY_SLOT | FLAG_STARTS_NEW_BLOCK;

		private int		        instruction;

		public void setInstruction(final int instruction) {
			this.instruction = instruction;
		}

		public int getInstruction() {
			return instruction;
		}

		public abstract void interpret(int insn, boolean delay);

		public abstract String name();

		public abstract String category();

		public void resetCount() {
			m_count = 0;
		}

		public void increaseCount() {
			m_count++;
		}

		public int getCount() {
			return m_count;
		}

		public int count() {
			return m_count;
		}

		public Instruction instance(final int insn) {
			return this;
		}

		public Instruction(final int flags) {
			this.flags = flags;
		}

		public int getFlags() {
			return flags;
		}

		public boolean hasFlags(final int testFlags) {
			return (flags & testFlags) == testFlags;
		}

		private void appendFlagString(final StringBuffer result, final String flagString) {
			if (result.length() > 0) {
				result.append(" | ");
			}
			result.append(flagString);
		}

		private String flagsToString() {
			final StringBuffer result = new StringBuffer();
			if (hasFlags(FLAG_INTERPRETED)) {
				appendFlagString(result, "FLAG_INTERPRETED");
			}
			if (hasFlags(FLAG_CANNOT_BE_SPLIT)) {
				appendFlagString(result, "FLAG_CANNOT_BE_SPLIT");
			}
			if (hasFlags(FLAG_HAS_DELAY_SLOT)) {
				appendFlagString(result, "FLAG_HAS_DELAY_SLOT");
			}
			if (hasFlags(FLAG_IS_BRANCHING)) {
				appendFlagString(result, "FLAG_IS_BRANCHING");
			}
			if (hasFlags(FLAG_IS_JUMPING)) {
				appendFlagString(result, "FLAG_IS_JUMPING");
			}
			if (hasFlags(FLAG_IS_CONDITIONAL)) {
				appendFlagString(result, "FLAG_IS_CONDITIONAL");
			}
			if (hasFlags(FLAG_STARTS_NEW_BLOCK)) {
				appendFlagString(result, "FLAG_STARTS_NEW_BLOCK");
			}
			if (hasFlags(FLAG_ENDS_BLOCK)) {
				appendFlagString(result, "FLAG_ENDS_BLOCK");
			}

			return result.toString();
		}

		@Override
		public String toString() {
			return name() + "(" + flagsToString() + ")";
		}
	}

	/** Decoder for special, regimm, cpo0, cpo1, cpo2, special2 intructions */
	public static abstract class STUB extends Instruction {

		public STUB() {
			super(NO_FLAGS);
		}

		@Override
		public void interpret(final int insn, final boolean delay) {
			instance(insn).interpret(insn, delay);
		}

		@Override
		public abstract Instruction instance(int insn);

		@Override
		public final String name() {
			return null;
		}

		@Override
		public final String category() {
			return null;
		}
	}

	/** Instrution unknow */
	public static final Instruction	UNK	= new Instruction(Instruction.NO_FLAGS) {
		                                    DecimalFormat	format	= new DecimalFormat("00000000000000000000000000000000");

		                                    @Override
		                                    public void interpret(final int insn, final boolean delay) {
			                                    System.err.println("UNK function." + format.format(new BigInteger(Integer.toBinaryString(insn))));
			                                    R5900.getProcessor().psxException(ExcCode.RESERVED_INST, insn, delay);
		                                    }

		                                    @Override
		                                    public final String name() {
			                                    return "UNK";
		                                    }

		                                    @Override
		                                    public final String category() {
			                                    return "UNK";
		                                    }
	                                    };
}
