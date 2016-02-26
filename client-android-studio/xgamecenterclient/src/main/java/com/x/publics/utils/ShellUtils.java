/**   
* @Title: ShellUtils.java
* @Package com.x.publics.utils
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-10 上午9:45:30
* @version V1.0   
*/


package com.x.publics.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
* @ClassName: ShellUtils
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-10 上午9:45:30
* 
*/

public class ShellUtils {

	public static final String TAG  = "ShellUtils";
	public static final String COMMAND_SU = "su";
	public static final String COMMAND_SH = "sh";
	public static final String COMMAND_EXIT = "exit\n";
	public static final String COMMAND_LINE_END = "\n";


	/**
	 * execute shell command, default return result msg
	 * 
	 * @param command command
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot) {
		return execCommand(new String[] { command }, isRoot, true);
	}

	/**
	 * execute shell commands, default return result msg
	 * 
	 * @param commands command list
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(List<String> commands, boolean isRoot) {
		return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
	}

	/**
	 * execute shell commands, default return result msg
	 * 
	 * @param commands command array
	 * @param isRoot whether need to run with root
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot) {
		return execCommand(commands, isRoot, true);
	}

	/**
	 * execute shell command
	 * 
	 * @param command command
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
		return execCommand(new String[] { command }, isRoot, isNeedResultMsg);
	}

	/**
	 * execute shell commands
	 * 
	 * @param commands command list
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return
	 * @see ShellUtils#execCommand(String[], boolean, boolean)
	 */
	public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
		return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
	}

	/**
	 * execute shell commands
	 * 
	 * @param commands command array
	 * @param isRoot whether need to run with root
	 * @param isNeedResultMsg whether need result msg
	 * @return <ul>
	 *         <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
	 *         {@link CommandResult#errorMsg} is null.</li>
	 *         <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
	 *         </ul>
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
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
			process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
			os = new DataOutputStream(process.getOutputStream());
			for (String command : commands) {
				if (command == null) {
					continue;
				}
				System.out.println(command);
				LogUtil.getLogger().d(TAG, command);
				// donnot use os.writeBytes(commmand), avoid chinese charset error
				os.write(command.getBytes());
				os.writeBytes(COMMAND_LINE_END);
				os.flush();
			}
			os.writeBytes(COMMAND_EXIT);
			os.flush();

			result = process.waitFor();
			// get command result
			if (isNeedResultMsg) {
				successMsg = new StringBuilder();
				errorMsg = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
			}
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
		LogUtil.getLogger().d(TAG, "result:" + result + ",successMsg:" + successMsg == null ? null : successMsg.toString()
				+ ",errorMsg:" + errorMsg == null ? null : errorMsg.toString());
		System.out.println("result:" + result + ",successMsg:" + successMsg == null ? null : successMsg.toString()
				+ ",errorMsg:" + errorMsg == null ? null : errorMsg.toString());
		return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}
	
	/**
	 * 执行命令progArray，同时通过returnKey来获取返回值 returnKey
	 * 
	 * @param progArray
	 * @param returnKey
	 * @return
	 */
	public static CommandResult exeCmd(String[] progArray,  boolean isNeedResultMsg) {
		int result = -1;
		DataOutputStream os = null;
		Process process = null;
		BufferedReader reader = null;
		for (String str : progArray) {
			System.out.println("progArray: "+ str);
		}
		
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;
		
		try {
			process = Runtime.getRuntime().exec(progArray);
			os = new DataOutputStream(process.getOutputStream());
			os.flush();
			result = process.waitFor();
			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String print = null;
			if (isNeedResultMsg) {
				successMsg = new StringBuilder();
				errorMsg = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String s;
				while ((s = successResult.readLine()) != null) {
					successMsg.append(s);
				}
				while ((s = errorResult.readLine()) != null) {
					errorMsg.append(s);
				}
			}
			while ((print = reader.readLine()) != null) {
				System.out.println("print: " + print);
			}
		} catch (IOException e) {
			System.out.println("exeCmd IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out
					.println("exeCmd InterruptedException: " + e.getMessage());
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
		return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
				: errorMsg.toString());
	}

	/**
	 * result of command
	 * <ul>
	 * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
	 * linux shell</li>
	 * <li>{@link CommandResult#successMsg} means success message of command result</li>
	 * <li>{@link CommandResult#errorMsg} means error message of command result</li>
	 * </ul>
	 * 
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
