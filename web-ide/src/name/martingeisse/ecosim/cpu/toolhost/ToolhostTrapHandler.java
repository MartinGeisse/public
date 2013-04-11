/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.martingeisse.ecosim.cpu.Cpu;
import name.martingeisse.ecosim.cpu.IGeneralRegisterFile;
import name.martingeisse.ecosim.cpu.usermode.IUsermodeCpuTrapHandler;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;
import name.martingeisse.ecosim.cpu.usermode.unix.IFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.ReadingPipeFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.SeekOrigin;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallErrorCode;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;
import name.martingeisse.ecosim.cpu.usermode.unix.WritingPipeFilePointer;
import name.martingeisse.ecosim.util.ByteQueue;

/**
 * Default implementation of {@link IUsermodeCpuTrapHandler}.
 */
public class ToolhostTrapHandler implements IUsermodeCpuTrapHandler {

	/**
	 * the process
	 */
	private final ToolhostProcess process;

	/**
	 * Constructor.
	 * @param process the process
	 */
	public ToolhostTrapHandler(final ToolhostProcess process) {
		this.process = process;
	}

	/**
	 * Getter method for the process.
	 * @return the process
	 */
	public ToolhostProcess getProcess() {
		return process;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.usermode.IEosUsermodeTrapHandler#handleTrap()
	 */
	@Override
	public void handleTrap() {
		final Cpu cpu = process.getCpu();
		final IGeneralRegisterFile registers = cpu.getGeneralRegisters();
		try {
			final int result = handleTrapInternal();
			registers.write(8, 0, false);
			registers.write(2, result, false);
		} catch (final UnixSyscallException e) {
			// TODO: use correct error code
			registers.write(8, -1, false);
		} catch (SkipTrapReturnException e) {
			return;
		}
		cpu.getPc().setValue(cpu.getPc().getValue() + 4, false);
	}

	/**
	 * 
	 */
	private int handleTrapInternal() throws UnixSyscallException, SkipTrapReturnException {

		final SparseMemory memory = process.getMemory();
		final Cpu cpu = process.getCpu();
		final IGeneralRegisterFile registers = cpu.getGeneralRegisters();
		final int arg0 = registers.read(4, false);
		final int arg1 = registers.read(5, false);
		final int arg2 = registers.read(6, false);
		final int arg3 = registers.read(7, false);
		final int syscallIndex = registers.read(8, false);

		if (syscallIndex < 0 || syscallIndex >= 64) {
			onInvalidSyscallIndex(syscallIndex);
			throw new UnixSyscallException(UnixSyscallErrorCode.UNSUPPORTED_OPERATION);
		}

		switch (syscallIndex) {

		case 0:
			// should not be needed
			onUnsupportedSyscall("getadr");
			break;

		case 1:
			// exit
			process.terminate(arg0);
			return 0;

		case 2:
			// fork
			return process.fork().getId();

		case 3:
			// read
			return process.getFilePointerTable().getValidEntry(arg0).read(process, memory, arg1, arg2);

		case 4:
			// write
			return process.getFilePointerTable().getValidEntry(arg0).write(process, memory, arg1, arg2);

		case 5:
			// open
			return openHelper(syscallIndex, arg0, arg1, arg2, arg3);

		case 6:
			// close
			process.getFilePointerTable().close(arg0);
			return 0;

		case 7: {
			// wait
			final ToolhostProcess result = tryReapAnyZombieChild();
			if (result != null) {
				process.getCpu().getGeneralRegisters().write(3, result.getExitStatus(), false);
				return result.getId();
			}
			process.setSleeping(new IToolhostSleepHandler() {
				@Override
				public void handle(final ToolhostProcess process) {
					final ToolhostProcess result = tryReapAnyZombieChild();
					if (result != null) {
						process.getCpu().getGeneralRegisters().write(2, result.getId(), false);
						process.getCpu().getGeneralRegisters().write(3, result.getExitStatus(), false);
						process.setRunning();
					}
				}
			});
			return 0;
		}

		case 8:
			// creat
			return openHelper(syscallIndex, arg0, arg1, arg2, arg3);

		case 9:
			// should not be needed (not portable)
			onUnsupportedSyscall("link");
			break;

		case 10: {
			// unlink
			String path = process.readString(arg0);
			File file = new File(process.getWorkingDirectory(), path);
			process.getProcessSet().getFileSystem().unlink(file);
			return 0;
		}

		case 11:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 12: {
			// chdir
			String path = process.readString(arg0);
			process.setWorkingDirectory(new File(process.getWorkingDirectory(), path));
			return 0;
		}

		case 13: {
			// time
			int time = (int)(new Date().getTime() / 1000);
			if (arg0 != 0) {
				writeInt32(arg0, time);
			}
			return time;
		}

		case 14:
			// should not be needed
			onUnsupportedSyscall("mknod");
			break;

		case 15:
			// should not be needed
			onUnsupportedSyscall("chmod");
			break;

		case 16:
			// should not be needed
			onUnsupportedSyscall("chown");
			break;

		case 17: {
			// break (arg0 is new size)
			process.increaseMemoryAllocation(arg0);
			return 0;
		}

		case 18: {
			// stat
			String path = process.readString(arg0);
			File file = new File(process.getWorkingDirectory(), path);
			StatResult statResult = process.getProcessSet().getFileSystem().stat(file);
			writeStateResult(arg1, statResult);
			return 0;
		}

		case 19: {
			// seek
			final IFilePointer filePointer = process.getFilePointerTable().getValidEntry(arg0);
			final SeekOrigin origin = decodeSeekOrigin(arg2);
			return filePointer.seek(arg1, origin);
		}

		case 20: {
			// getpid
			ToolhostProcess parent = process.getParent();
			int ppid = (parent == null ? 1 : parent.getId());
			registers.write(3, ppid, false);
			return process.getId();
		}

		case 21:
			// should not be needed
			onUnsupportedSyscall("mount");
			break;

		case 22:
			// should not be needed
			onUnsupportedSyscall("umount");
			break;

		case 23:
			// should not be needed
			onUnsupportedSyscall("setuid");
			break;

		case 24:
			// getuid
			return 42;

		case 25:
			// should not be needed
			onUnsupportedSyscall("stime");
			break;

		case 26:
			// should not be needed
			onUnsupportedSyscall("ptrace");
			break;

		case 27:
			// should not be needed
			onUnsupportedSyscall("alarm");
			break;

		case 28: {
			// fstat
			IFilePointer filePointer = process.getFilePointerTable().getValidEntry(arg0);
			StatResult statResult = filePointer.stat();
			writeStateResult(arg1, statResult);
			return 0;
		}

		case 29:
			// should not be needed
			onUnsupportedSyscall("pause");
			break;

		case 30: {
			// utime
			String path = process.readString(arg0);
			int lastAccessedTime = readInt32(arg1);
			int lastModifiedTime = readInt32(arg1 + 1);
			process.getProcessSet().getFileSystem().setFileTime(new File(process.getWorkingDirectory(), path), lastAccessedTime, lastModifiedTime);
			return 0;
		}

		case 31:
		case 32:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 33:
			// access
			return openHelper(syscallIndex, arg0, arg1, arg2, arg3);

		case 34:
			// "nice" -- ignore
			return 0;

		case 35: {
			// ftime
			int time = (int)(new Date().getTime() / 1000);
			writeInt32(arg0 + 0, time);
			writeInt16(arg0 + 4, 0);
			writeInt16(arg0 + 6, -60);
			writeInt16(arg0 + 8, 1);
			return 0;
		}

		case 36:
			// should not be needed
			onUnsupportedSyscall("sync");
			break;

		case 37: {
			// kill
			final ToolhostProcess targetProcess = process.getProcessSet().getProcess(arg0);
			if (targetProcess == null) {
				throw new UnixSyscallException(UnixSyscallErrorCode.INVALID_PROCESS);
			}
			// don't allow -- if a process tries other signals than 9, we'll accidentally
			// kill a process because there is no proper signal handling, and this is a hard to find bug
			throw new UnixSyscallException(UnixSyscallErrorCode.NO_PERMISSION);
			// targetProcess.terminate(128 + (arg1 & 127));
			// return 0;
		}

		case 38:
		case 39:
		case 40:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 41:
			// dup
			return process.getFilePointerTable().dup(arg0);

		case 42: {
			// pipe
			ByteQueue queue = new ByteQueue();
			int readFd = process.getFilePointerTable().insertEntry(new ReadingPipeFilePointer(queue));
			int writeFd = process.getFilePointerTable().insertEntry(new WritingPipeFilePointer(queue));
			registers.write(3, writeFd, false);
			return readFd;
		}

		case 43:
			// should not be needed
			onUnsupportedSyscall("times");
			break;

		case 44:
			// should not be needed
			onUnsupportedSyscall("profil");
			break;

		case 45:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 46:
			onUnsupportedSyscall("setgid");
			break;

		case 47:
			// getgid
			return 42;

		case 48:
			// should not be needed
			onUnsupportedSyscall("signal");
			break;

		case 49:
			// should not be needed
			onUnsupportedSyscall("sigret");
			break;

		case 50:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 51:
			// should not be needed
			onUnsupportedSyscall("acct");
			break;

		case 52:
		case 53:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 54:
			// ioctl
			return process.getFilePointerTable().getValidEntry(arg0).ioctl(process, memory, arg1, arg2);

		case 55:
		case 56:
		case 57:
		case 58:
			onInvalidSyscallIndex(syscallIndex);
			break;

		case 59: {
			// exece
			String executablePath = process.readString(arg0);
			String[] arguments = readNullTerminatedStringArray(arg1);
			String[] environment = readNullTerminatedStringArray(arg2);
			try {
				process.exec(executablePath, arguments, environment);
			} catch (Exception e) {
				throw new UnixSyscallException(UnixSyscallErrorCode.IO_ERROR);
			}
			throw new SkipTrapReturnException();
		}

		case 60:
			// umask -- ignored because not portable
			break;

		case 61:
			// should not be needed
			onUnsupportedSyscall("chroot");
			break;

		case 62:
		case 63:
			onInvalidSyscallIndex(syscallIndex);
			break;

		}

		throw new UnixSyscallException(UnixSyscallErrorCode.UNSUPPORTED_OPERATION);
	}

	private void onInvalidSyscallIndex(final int syscallIndex) {
		error("invalid syscall index: " + syscallIndex);
	}

	private void onUnsupportedSyscall(final String syscallName) {
		error("unsupported syscall: " + syscallName);
	}

	private void error(final String message) {
		System.out.println(message);
		System.exit(1);
	}

	private ToolhostProcess tryReapAnyZombieChild() {
		final ToolhostProcessSet processSet = process.getProcessSet();
		final ToolhostProcess zombie = processSet.findZombieChild(process);
		if (zombie == null) {
			return null;
		} else {
			processSet.removeZombie(zombie);
			return zombie;
		}
	}

	/**
	 * Common handling for syscalls open (index 5), creat (index 8), access, (index 33).
	 */
	private int openHelper(final int syscallIndex, final int arg0, final int arg1, final int arg2, final int arg3) throws UnixSyscallException {
		String path = process.readString(arg0);
		// consider unix-style and windows-style absolute paths
		boolean absolute = (path.startsWith("/") || (path.length() > 2 && path.charAt(1) == ':'));
		final File file = absolute ? new File(path) : new File(process.getWorkingDirectory(), path);
		final boolean create = (syscallIndex != 33);
		final boolean truncate = (syscallIndex == 8);
		boolean readable;
		boolean writable;
		if (syscallIndex == 5) {
			readable = (((arg1 + 1) & 1) != 0);
			writable = (((arg1 + 1) & 2) != 0);
		} else if (syscallIndex == 8) {
			readable = false;
			writable = true;
		} else {
			// exec is not checked since this is not portable
			// note also that access() uses (I*) flag constants while open() uses (F*) flag constants,
			// hence the different mask values.
			readable = ((arg1 & 4) != 0);
			writable = ((arg1 & 2) != 0);
		}
		final boolean append = false;
		final IFilePointer filePointer = process.getProcessSet().getFileSystem().open(file, create, truncate, readable, writable, append);
		if (syscallIndex == 33) {
			filePointer.releaseReference();
			return 0;
		} else {
			final int fd = process.getFilePointerTable().insertEntry(filePointer);
			filePointer.releaseReference();
			return fd;
		}
	}

	private SeekOrigin decodeSeekOrigin(final int code) {
		if (code == 1) {
			return SeekOrigin.CURRENT;
		} else if (code == 2) {
			return SeekOrigin.END;
		} else {
			return SeekOrigin.START;
		}
	}
	
	private int readInt32(int address) {
		SparseMemory memory = process.getMemory();
		return (memory.readByte(address) << 24) + (memory.readByte(address + 1) << 16) + (memory.readByte(address + 2) << 8) + memory.readByte(address + 3);
	}

	private void writeInt16(int address, int value) {
		SparseMemory memory = process.getMemory();
		memory.writeByte(address, value >> 8);
		memory.writeByte(address + 1, value);
	}

	private void writeInt32(int address, int value) {
		SparseMemory memory = process.getMemory();
		memory.writeByte(address, value >> 24);
		memory.writeByte(address + 1, value >> 16);
		memory.writeByte(address + 2, value >> 8);
		memory.writeByte(address + 3, value);
	}
	
	private void writeStateResult(int address, StatResult statResult) {
		writeInt32(address + 0, statResult.deviceId);
		writeInt32(address + 4, statResult.inodeId);
		writeInt32(address + 8, statResult.mode);
		writeInt32(address + 12, statResult.linkCount);
		writeInt32(address + 16, statResult.userId);
		writeInt32(address + 20, statResult.groupId);
		writeInt32(address + 24, statResult.targetDeviceId);
		writeInt32(address + 28, statResult.size);
		writeInt32(address + 32, statResult.lastAccessedTime);
		writeInt32(address + 36, statResult.lastModifiedTime);
		writeInt32(address + 40, statResult.lastChangedTime);
	}

	private String[] readNullTerminatedStringArray(int arrayAddress) {
		List<String> strings = new ArrayList<String>();
		while (true) {
			int stringAddress = readInt32(arrayAddress);
			if (stringAddress == 0) {
				break;
			}
			strings.add(process.readString(stringAddress));
			arrayAddress += 4;
		}
		return strings.toArray(new String[strings.size()]);
	}

	private static class SkipTrapReturnException extends Exception {
	}
	
}
