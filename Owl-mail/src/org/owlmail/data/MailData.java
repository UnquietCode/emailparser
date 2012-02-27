package org.owlmail.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;
import org.owlmail.global.MailSetting;
import org.owlmail.global.MailType;
import org.owlmail.global.SearchType;
import org.owlmail.res.Resource;
import org.owlmail.utils.FileUtil;
import org.owlmail.view.Owlmail;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class MailData {

	private static String cacheFolderPath = ".owlmail";

	private static String getCacheFolderPath() {
		return cacheFolderPath;
	}

	private ArrayList<Message> Mails = new ArrayList<Message>();
	private ArrayList<Message> Result = new ArrayList<Message>();
	private ArrayList<Message> cacheMessages = new ArrayList<Message>();
	private String SearchFolder;

	/**
	 * 
	 * @param SearchFolderForMessages
	 * @param statusBar
	 * @param statusText
	 */
	public MailData(String SearchFolderForMessages, JProgressBar statusBar,
			JLabel statusText) {
		if (!SearchFolderForMessages.isEmpty()) {
			setSearchFolder(SearchFolderForMessages);
			generateMessageList(statusBar, statusText);
		}
	}

	/**
	 * 
	 * @param statusBar
	 * @param statusText
	 * @return
	 */
	private boolean generateMessageList(JProgressBar statusBar,
			JLabel statusText) {
		boolean bOk = true;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		File root = new File(getSearchFolder());
		try {
			boolean recursive = true;
			statusText.setText(Resource.getValue("MailData.GettingFiles"));
			Collection<File> files = FileUtils.listFiles(root,
					FileUtil.extensions, recursive);
			statusBar.setValue(0);
			statusBar.setMaximum(files.size());
			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
				statusBar.setValue(statusBar.getValue() + 1);
				File file = iterator.next();

				File cacheFile = FileUtil.generateCacheFile(file,
						getSearchFolder(), getCacheFolderPath());
				if (cacheFile.exists()
						&& (cacheFile.lastModified() > cal.getTimeInMillis())) {
					statusText.setText(Resource
							.getValue("MailData.DataFromCache")
							+ cacheFile.getName());
					FileInputStream fis = null;
					ObjectInputStream in = null;
					fis = new FileInputStream(cacheFile);
					in = new ObjectInputStream(fis);
					Message cachedMessage = null;
					cachedMessage = (Message) in.readObject();
					Mails.add(cachedMessage);
					in.close();
				} else {
					statusText.setText(Resource
							.getValue("MailData.DataFromEmailFiles")
							+ file.getName());

					Date date = new Date(file.lastModified());
					String displayTO = "";
					String displayFROM = "";
					String displayCC = "";
					String displayBCC = "";
					String displaySUBJECT = "";
					String textBODY = "";
					ArrayList<String> attachmentFiles = new ArrayList<String>();

					MailType type = MailType.getFileType(file);
					switch (type) {
					case MSG:

						break;
					case EML:

						break;
					case MBOX:

						break;
					case UNKNOWN:

						break;
					default:

						break;
					}

					// 构建SWITCH，替换掉此处的IF...ELSE...
					if (FileUtil.isExtendsequal(file, MailType.MSG)) {
						// Get Date
						// Get To
						// Get From
						// Get CC
						// Get BCC
						// Get Subject
						// Get TextBody
						// Get Attachments
					} else {
						// Get Date
						// Get To
						// Get From
						// Get CC
						// Get BCC
						// Get Subject
						// Get TextBody
					}
					Message myMessage = new Message(file.getAbsolutePath(),
							date, displayTO, displayFROM, displayCC,
							displayBCC, displaySUBJECT, textBODY,
							attachmentFiles);
					cacheMessages.add(myMessage);
					Mails.add(myMessage);
				}
			}
			initResults();
		} catch (Exception e) {
			e.printStackTrace();
			bOk = false;
		}

		if (MailSetting.isEnableCaching()) {
			File hiddenCacheFolder = new File(getSearchFolder() + "\\"
					+ getCacheFolderPath());
			if (!hiddenCacheFolder.exists()) {
				hiddenCacheFolder.mkdir();
			}
			try {
				Process p = Runtime.getRuntime().exec(
						"attrib +h \"" + hiddenCacheFolder.getAbsolutePath()
								+ "\"");
				p.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				bOk = false;
			} catch (IOException e2) {
				e2.printStackTrace();
				bOk = false;
			}

			statusBar.setValue(0);
			statusBar.setMaximum(cacheMessages.size());
			for (int i = 0; i < cacheMessages.size(); i++) {
				statusBar.setValue(statusBar.getValue() + 1);
				String file = cacheMessages
						.get(i)
						.getFilePath()
						.substring(
								cacheMessages.get(i).getFilePath()
										.lastIndexOf("\\") + 1,
								cacheMessages.get(i).getFilePath()
										.lastIndexOf("."))
						+ ".cache";
				File checkFile = new File(getSearchFolder() + "\\"
						+ getCacheFolderPath() + "\\" + file);
				statusText.setText(Resource
						.getValue("MailData.CreatingCacheFile")
						+ checkFile.getName());
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				try {
					fos = new FileOutputStream(checkFile);
					out = new ObjectOutputStream(fos);
					out.writeObject(cacheMessages.get(i));
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					bOk = false;
				}
			}
		}
		return bOk;
	}

	public ArrayList<Message> getResult() {
		return Result;
	}

	private String getSearchFolder() {
		return SearchFolder;
	}

	private void initResults() {
		setResult(Mails);
		SortResults();
	}

	public void searchMails(String SearchValue, SearchType Type,
			JProgressBar statusBar) {
		ArrayList<Message> tmpMessages = new ArrayList<Message>();
		statusBar.setMaximum(Mails.size());
		for (Iterator<Message> iterator = Mails.iterator(); iterator.hasNext();) {
			statusBar.setValue(statusBar.getValue() + 1);
			Message aMessage = iterator.next();
			switch (Type) {
			case PERSON:
				if (Owlmail.isShowCC_BCC()) {
					if (aMessage.getReceiver().toLowerCase()
							.contains(SearchValue.toLowerCase())
							|| aMessage.getSender().toLowerCase()
									.contains(SearchValue.toLowerCase())
							|| aMessage.getCC().toLowerCase()
									.contains(SearchValue.toLowerCase())
							|| aMessage.getBCC().toLowerCase()
									.contains(SearchValue.toLowerCase())) {
						aMessage.colorizeElements(SearchValue, Type);
						tmpMessages.add(aMessage);
					}
				} else {
					if (aMessage.getReceiver().toLowerCase()
							.contains(SearchValue.toLowerCase())
							|| aMessage.getSender().toLowerCase()
									.contains(SearchValue.toLowerCase())) {
						aMessage.colorizeElements(SearchValue, Type);
						tmpMessages.add(aMessage);
					}
				}
				break;
			case SUBJECT:
				if (aMessage.getSubject().toLowerCase()
						.contains(SearchValue.toLowerCase())) {
					aMessage.colorizeElements(SearchValue, Type);
					tmpMessages.add(aMessage);
				}
				break;
			case CONTENT:
				if (aMessage.getContent().toLowerCase()
						.contains(SearchValue.toLowerCase())) {
					aMessage.colorizeElements(SearchValue, Type);
					tmpMessages.add(aMessage);
				}
				break;
			case SENDER_ONLY:
				if (aMessage.getSender().toLowerCase()
						.contains(SearchValue.toLowerCase())) {
					aMessage.colorizeElements(SearchValue, Type);
					tmpMessages.add(aMessage);
				}
				break;
			case RECEIVER_ONLY:
				if (aMessage.getReceiver().toLowerCase()
						.contains(SearchValue.toLowerCase())) {
					aMessage.colorizeElements(SearchValue, Type);
					tmpMessages.add(aMessage);
				}
				break;
			default:
				break;
			}
		}
		setResult(tmpMessages);
	}

	private void setResult(ArrayList<Message> result) {
		Result = result;
	}

	private void setSearchFolder(String searchFolder) {
		SearchFolder = searchFolder;
	}

	private void SortResults() {
		Collections.sort(Result, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Message message1 = (Message) o1;
				Message message2 = (Message) o2;
				return message2.getReceivedDate().compareTo(
						message1.getReceivedDate());
			}
		});
	}

}
