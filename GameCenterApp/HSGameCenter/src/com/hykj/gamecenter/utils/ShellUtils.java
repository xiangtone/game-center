package com.hykj.gamecenter.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.hykj.gamecenter.download.DownloadTask;

/**
 * ShellUtils
 * <ul>
 * <strong>Check root</strong>
 * <li>{@link ShellUtils#checkRootPermission()}</li>
 * </ul>
 * <ul>
 * <strong>Execte command</strong>
 * <li>{@link ShellUtils#execCommand(String, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean, boolean)}</li>
 * </ul>
 * 
 * @author Trinea 2013-5-16
 */
public class ShellUtils {

	public static final String COMMAND_SU = "su";
	public static final String COMMAND_SH = "sh";
	public static final String COMMAND_EXIT = "exit\n";
	public static final String COMMAND_LINE_END = "\n";
	private static final String TAG = ShellUtils.class.getName();

	/**
	 * 
	 * 检测是否具体root权限
	 * 
	 * @author firewang
	 * @param cmd
	 * @return
	 */
	public static boolean checkIsRoot() {
		String commandToExecute = "su";
		return executeShellCommand(commandToExecute);
	}

	private static boolean executeShellCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (process != null) {
				try {
					process.destroy();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 
	 * 检测是否具体root权限
	 * 
	 * @author firewang
	 * @param cmd
	 * @return
	 */
	public static boolean commandHaveRoot(String filePath) {

		if (filePath == null || filePath.length() == 0) {
			Log.d(TAG, "haveRoot filePath == null");
			return false;
		}

		File file = new File(filePath);
		if (file == null || file.length() <= 0 || !file.exists()
				|| !file.isFile()) {
			Log.d(TAG, "haveRoot new file failed");
			return false;
		}

		/**
		 * if context is system app, don't need root permission, but should add
		 * <uses-permission android:name="android.permission.INSTALL_PACKAGES"
		 * /> in mainfest
		 **/
		StringBuilder command = new StringBuilder().append("pm install -r ")
				.append(filePath.replace(" ", "\\ "));
		Log.d(TAG, "commandHaveRoot command = " + command);
		int i = execRootCmdSilent(command.toString());
		return i != -1;
	}

	/**
	 * 
	 * 检测是否具体root权限
	 * 
	 * @author firewang
	 * @param cmd
	 * @return
	 */
	public static boolean haveRoot(String cmd) {
		int i = execRootCmdSilent(cmd);
		return i != -1;
	}

	/**
	 * 执行静默安装
	 * 
	 * @author firewang
	 * @param paramString
	 * @return
	 */
	private static int execRootCmdSilent(String paramString) {
		int result = -1;
		try {
			Process localProcess = Runtime.getRuntime().exec("su");
			OutputStream os = localProcess.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeBytes(paramString + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			localProcess.waitFor();
			result = localProcess.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * check whether has root permission
	 * 
	 * @return
	 */
	public static boolean checkRootPermission() {
		return execCommand("echo root", true, false).result == 0;
	}

	/**
	 * execute shell command, default return result msg
	 * 
	 * @param command
	 *            command
	 * @param isRoot
	 *            whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot) {
		return execCommand(new String[] { command }, isRoot, true);
	}

	/**
	 * execute shell commands, default return result msg
	 * 
	 * @param commands
	 *            command list
	 * @param isRoot
	 *            whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(List<String> commands,
			boolean isRoot) {
		return execCommand(
				commands == null ? null : commands.toArray(new String[] {}),
				isRoot, true);
	}

	/**
	 * execute shell commands, default return result msg
	 * 
	 * @param commands
	 *            command array
	 * @param isRoot
	 *            whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot) {
		return execCommand(commands, isRoot, true);
	}

	/**
	 * execute shell command
	 * 
	 * @param command
	 *            command
	 * @param isRoot
	 *            whether need to run with root
	 * @param isNeedResultMsg
	 *            whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot,
			boolean isNeedResultMsg) {
		return execCommand(new String[] { command }, isRoot, isNeedResultMsg);
	}

	/**
	 * execute shell commands
	 * 
	 * @param commands
	 *            command list
	 * @param isRoot
	 *            whether need to run with root
	 * @param isNeedResultMsg
	 *            whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(List<String> commands,
			boolean isRoot, boolean isNeedResultMsg) {
		return execCommand(
				commands == null ? null : commands.toArray(new String[] {}),
				isRoot, isNeedResultMsg);
	}

	/**
	 * execute shell commands
	 * 
	 * @param commands
	 *            command array
	 * @param isRoot
	 *            whether need to run with root
	 * @param isNeedResultMsg
	 *            whether need result msg
	 * @return <ul>
	 *         <li>if isNeedResultMsg is false, {@link CommandResult#successMsg}
	 *         is null and {@link CommandResult#errorMsg} is null.</li>
	 *         <li>if {@link CommandResult#result} is -1, there maybe some
	 *         excepiton.</li>
	 *         </ul>
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot,
			boolean isNeedResultMsg) {
		int result = -1;
		if (commands == null || commands.length == 0) {
			return new CommandResult(result, null, null);
		}

		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;

		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec(
					isRoot ? COMMAND_SU : COMMAND_SH);
			os = new DataOutputStream(process.getOutputStream());
			for (String command : commands) {
				if (command == null) {
					continue;
				}

				// donnot use os.writeBytes(commmand), avoid chinese charset
				// error
				os.write(command.getBytes());
				os.writeBytes(COMMAND_LINE_END);
				os.flush();
			}
			os.writeBytes(COMMAND_EXIT);
			os.flush();

			// get command result
			if (isNeedResultMsg) {
				successMsg = new StringBuilder();
				errorMsg = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(
						process.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
			}
			result = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (process != null) {
				process.destroy();
			}
		}
		return new CommandResult(result, successMsg == null ? null
				: successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}

	public static CommandResult execCommand(String command, boolean isRoot,
			boolean isNeedResultMsg, DownloadTask dinfo) {
		int result = -1;
		if (TextUtils.isEmpty(command)) {
			return new CommandResult(result, null, null);
		}

		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;

		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec(
					isRoot ? COMMAND_SU : COMMAND_SH);
			os = new DataOutputStream(process.getOutputStream());

			os.write(command.getBytes());
			os.writeBytes(COMMAND_LINE_END);
			os.flush();
			os.writeBytes(COMMAND_EXIT);
			os.flush();

			// get command result
			if (isNeedResultMsg) {
				RuntimeStream errorStream = new RuntimeStream(
						process.getErrorStream(), "ERROR", dinfo);
				errorStream.start();

				RuntimeStream outStream = new RuntimeStream(
						process.getInputStream(), "STDOUT", dinfo);
				outStream.start();
			}
			result = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (process != null) {
				process.destroy();
			}
		}
		return new CommandResult(result, successMsg == null ? null
				: successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}

	/**
	 * result of command,
	 * <ul>
	 * <li>{@link CommandResult#result} means result of command, 0 means normal,
	 * else means error, same to excute in linux shell</li>
	 * <li>{@link CommandResult#successMsg} means success message of command
	 * result</li>
	 * <li>{@link CommandResult#errorMsg} means error message of command result</li>
	 * </ul>
	 * 
	 * @author Trinea 2013-5-16
	 */
	public static class CommandResult {

		/** result of command **/
		public int result;
		/** success message of command result **/
		public String successMsg;
		/** error message of command result **/
		public String errorMsg;

		public CommandResult(int result) {
			this.result = result;
		}

		public CommandResult(int result, String successMsg, String errorMsg) {
			this.result = result;
			this.successMsg = successMsg;
			this.errorMsg = errorMsg;
		}
	}

}
