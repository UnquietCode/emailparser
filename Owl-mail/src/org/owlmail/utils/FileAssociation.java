/**
 * FileAssociation.java
 * 
 * @author Goldenbogen, Pierre
 *         Created: 04.05.2011 14:47:47
 */
package org.owlmail.utils;

/**
 * 
 * @Description 文件关联工具类
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class FileAssociation {

	/**
	 * Checks current OS to decide how to associate the given file extension to
	 * our application.
	 * 
	 * @param FileExtension
	 * @return
	 */
	public static boolean setFileAssociation(String FileExtension) {
		boolean bOk = true;
		if (!FileExtension.isEmpty()) {
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				if (!SetWindowsFileAssociation(FileExtension)) {
					bOk = false;
				}
			}
		}
		return bOk;
	}

	/**
	 * Method to associate the given file extension to our software on windows.
	 * We use the REG command line tool to do this.
	 * 
	 * @param FileExtension
	 * @return
	 */
	private static boolean SetWindowsFileAssociation(String FileExtension) {
		boolean bOk = true;
		try {
			Process RegistryProcess = null;
			int exitVal = -1;
			RegistryProcess = Runtime.getRuntime().exec(
					"REG ADD HKCR\\" + FileExtension
							+ "\\ /ve /t REG_SZ /d jmsgreader /f");
			exitVal = RegistryProcess.waitFor();
			switch (exitVal) {
			case 0:
				bOk = true;
				break;
			case 1:
				bOk = false;
				break;
			}
			exitVal = -1;
			RegistryProcess = Runtime
					.getRuntime()
					.exec("REG ADD HKCR\\jmsgreader\\ /ve /t REG_SZ /d \"jmsgreader File\" /f");
			exitVal = RegistryProcess.waitFor();
			switch (exitVal) {
			case 0:
				bOk = true;
				break;
			case 1:
				bOk = false;
				break;
			}
			exitVal = -1;
			RegistryProcess = Runtime.getRuntime().exec(
					"REG ADD HKCR\\jmsgreader\\Shell\\Open\\Command /ve /t REG_SZ /d \"javaw -jar "
							+ System.getProperty("user.dir")
							+ "\\jmsgreader.jar \"\"\"%1\"\" /f");
			exitVal = RegistryProcess.waitFor();
			switch (exitVal) {
			case 0:
				bOk = true;
				break;
			case 1:
				bOk = false;
				break;
			}
		} catch (Exception e) {
			bOk = false;
			e.printStackTrace();
		}
		return bOk;
	}
}
