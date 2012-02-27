/**
 * MailMessages.java
 * 
 * @author Goldenbogen, Pierre
 *         Created: 01.12.2011 09:23:32
 */
package net.goldenbogen.jmsgreader.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.goldenbogen.jmsgreader.JMsgReader;
import net.goldenbogen.jmsgreader.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class MailMessages {

	public enum SearchType {
		CONTENT, PERSON, RECEIVER_ONLY, SENDER_ONLY, SUBJECT;
	}

	private static String cacheFolderPath = ".jmsgreader";

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
	public MailMessages(String SearchFolderForMessages, JProgressBar statusBar,
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
			String[] extensions = { "msg", "eml" };
			statusText.setText(Resource.getString("MailMessages.GettingFiles"));
			Collection<File> files = FileUtils.listFiles(root, extensions,
					recursive);
			statusBar.setValue(0);
			statusBar.setMaximum(files.size());
			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
				statusBar.setValue(statusBar.getValue() + 1);
				File file = iterator.next();

				String fileName = file.getAbsolutePath().substring(
						file.getAbsolutePath().lastIndexOf("\\") + 1,
						file.getAbsolutePath().lastIndexOf("."))
						+ ".cache";
				File cacheFile = new File(getSearchFolder() + "\\"
						+ getCacheFolderPath() + "\\" + fileName);
				if (cacheFile.exists()
						&& (cacheFile.lastModified() > cal.getTimeInMillis())) {
					statusText.setText(Resource
							.getString("MailMessages.DataFromCache")
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
							.getString("MailMessages.DataFromMsgFiles")
							+ file.getName());

					Date date = new Date(file.lastModified());
					String displayTO = "";
					String displayFROM = "";
					String displayCC = "";
					String displayBCC = "";
					String displaySUBJECT = "";
					String textBODY = "";
					ArrayList<String> attachmentFiles = new ArrayList<String>();

					if (file.getAbsolutePath()
							.substring(
									file.getAbsolutePath().lastIndexOf(".") + 1,
									file.getAbsolutePath().length())
							.toLowerCase().equals("msg")) {
						MAPIMessage msg = new MAPIMessage(
								file.getAbsolutePath());

						// Get Date
						try {
							date = msg.getMessageDate().getTime();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageDATE-CHUNK");
						}

						// Get To
						try {
							displayTO = msg.getDisplayTo();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageTO-CHUNK");
						}

						// Get From
						try {
							displayFROM = msg.getDisplayFrom();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageFROM-CHUNK");
						}

						// Get CC
						try {
							displayCC = msg.getDisplayCC();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageCC-CHUNK");
						}

						// Get BCC
						try {
							displayBCC = msg.getDisplayBCC();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageBCC-CHUNK");
						}

						// Get Subject
						try {
							displaySUBJECT = msg.getSubject();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageSUBJECT-CHUNK");
						}

						// Get TextBody
						try {
							textBODY = msg.getTextBody();
						} catch (Exception e) {
							textBODY = Resource
									.getString("MailMessages.AlternativeMsgText");
							System.err.println("Error: " + file.getName()
									+ " no messageTEXTBODY-CHUNK");
						}

						// Get Attachments
						AttachmentChunks[] attachments;
						try {
							attachments = msg.getAttachmentFiles();
							if (attachments.length > 0) {
								for (AttachmentChunks attachment : attachments) {
									attachmentFiles
											.add(attachment.attachLongFileName
													.toString());
								}
							}
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageATTACHMENTS-CHUNK");
						}
					} else {
						Properties props = System.getProperties();
						props.put("mail.host", "smtp.dummydomain.com");
						props.put("mail.transport.protocol", "smtp");

						Session mailSession = Session.getDefaultInstance(props,
								null);
						InputStream source = new FileInputStream(
								file.getAbsolutePath());
						MimeMessage msg = new MimeMessage(mailSession, source);

						// Get Date
						try {
							date = new Date(msg.getReceivedDate().getTime());
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageDATE-CHUNK");
						}

						// Get To
						try {
							Address[] reciptients = msg
									.getRecipients(RecipientType.TO);
							for (int i = 0; i < reciptients.length; i++) {
								displayTO = displayTO + reciptients[i] + ", ";
							}
							displayTO = displayTO.substring(0,
									displayTO.lastIndexOf(","));
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageTO-CHUNK");
						}

						// Get From
						try {
							Address[] senders = msg.getFrom();
							for (int i = 0; i < senders.length; i++) {
								displayFROM = displayFROM + senders[i] + ", ";
							}
							displayFROM = displayFROM.substring(0,
									displayFROM.lastIndexOf(","));
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageFROM-CHUNK");
						}

						// Get CC
						try {
							Address[] reciptients = msg
									.getRecipients(RecipientType.CC);
							for (int i = 0; i < reciptients.length; i++) {
								displayCC = displayCC + reciptients[i] + ", ";
							}
							displayCC = displayCC.substring(0,
									displayCC.lastIndexOf(","));
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageCC-CHUNK");
						}

						// Get BCC
						try {
							Address[] reciptients = msg
									.getRecipients(RecipientType.BCC);
							for (int i = 0; i < reciptients.length; i++) {
								displayBCC = displayBCC + reciptients[i] + ", ";
							}
							displayBCC = displayBCC.substring(0,
									displayBCC.lastIndexOf(","));
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageBCC-CHUNK");
						}

						// Get Subject
						try {
							displaySUBJECT = msg.getSubject();
						} catch (Exception e) {
							System.err.println("Error: " + file.getName()
									+ " no messageSUBJECT-CHUNK");
						}

						// Get TextBody
						try {
							textBODY = msg.getContent().toString();
						} catch (Exception e) {
							textBODY = Resource
									.getString("MailMessages.AlternativeMsgText");
							System.err.println("Error: " + file.getName()
									+ " no messageTEXTBODY-CHUNK");
						}

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

		if (UserSettings.isEnableCaching()) {
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
						.getString("MailMessages.CreatingCacheFile")
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
				if (JMsgReader.isShowCC_BCC()) {
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
