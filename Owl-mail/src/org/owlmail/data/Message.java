package org.owlmail.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.owlmail.global.SearchType;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4530051446757892751L;

	private ArrayList<String> Attachments;
	private String BCC;
	private String CC;
	private String ColorizedBCC;
	private String ColorizedCC;
	private String ColorizedContent;
	private String ColorizedReceiver;
	private String ColorizedSender;
	private String ColorizedSubject;
	private String Content;
	private String FilePath;
	private Date ReceivedDate;
	private String Receiver;
	private String Sender;
	private String Subject;

	/**
	 * 
	 * @param FilePath
	 * @param ReceivedDate
	 * @param Receiver
	 * @param Sender
	 * @param CC
	 * @param BCC
	 * @param Subject
	 * @param Content
	 * @param Attachments
	 */
	public Message(String FilePath, Date ReceivedDate, String Receiver,
			String Sender, String CC, String BCC, String Subject,
			String Content, ArrayList<String> Attachments) {
		setFilePath(FilePath);
		setReceivedDate(ReceivedDate);
		setReceiver(Receiver);
		setSender(Sender);
		setCC(CC);
		setBCC(BCC);
		setSubject(Subject);
		setContent(Content);
		setAttachments(Attachments);
	}

	/**
	 * 
	 * @param SearchText
	 * @param Text
	 * @return
	 */
	private String colorize(String SearchText, String Text) {
		if (!SearchText.toLowerCase().isEmpty()) {
			int lastIndex = 0;
			int increment = 0;
			Vector<Integer> places = new Vector<Integer>();
			while (lastIndex != -1) {
				lastIndex = Text.toLowerCase().indexOf(
						SearchText.toLowerCase(), lastIndex + increment);
				increment = SearchText.length();
				if (lastIndex != -1) {
					places.add(lastIndex);
				}
			}
			String output = "";
			String htmlfront = "<font bgcolor=yellow color=black>";
			String htmlback = "</font>";
			Collections.reverse(places);
			for (int iRunner = 0; iRunner < places.size(); iRunner++) {
				output = Text.substring(0, places.get(iRunner));
				output = output + htmlfront;
				output = output
						+ Text.substring(places.get(iRunner),
								places.get(iRunner) + SearchText.length());
				output = output + htmlback;
				Text = output
						+ Text.substring(
								places.get(iRunner) + SearchText.length(),
								Text.length());
			}
		}
		return Text;
	}

	/**
	 * 
	 * @param SearchText
	 * @param Type
	 */
	public void colorizeElements(String SearchText, SearchType Type) {
		switch (Type) {
		case PERSON:
			setColorizedReceiver(colorize(SearchText, getReceiver()));
			setColorizedSender(colorize(SearchText, getSender()));
			setColorizedCC(colorize(SearchText, getCC()));
			setColorizedBCC(colorize(SearchText, getBCC()));
			setColorizedSubject(getSubject());
			setColorizedContent(getContent());
			break;
		case SUBJECT:
			setColorizedReceiver(getReceiver());
			setColorizedSender(getSender());
			setColorizedCC(getCC());
			setColorizedBCC(getBCC());
			setColorizedSubject(colorize(SearchText, getSubject()));
			setColorizedContent(getContent());
			break;
		case CONTENT:
			setColorizedReceiver(getReceiver());
			setColorizedSender(getSender());
			setColorizedCC(getCC());
			setColorizedBCC(getBCC());
			setColorizedSubject(getSubject());
			setColorizedContent(colorize(SearchText, getContent()));
			break;
		case SENDER_ONLY:
			setColorizedReceiver(getReceiver());
			setColorizedSender(colorize(SearchText, getSender()));
			setColorizedCC(getCC());
			setColorizedBCC(getBCC());
			setColorizedSubject(getSubject());
			setColorizedContent(getContent());
			break;
		case RECEIVER_ONLY:
			setColorizedReceiver(colorize(SearchText, getReceiver()));
			setColorizedSender(getSender());
			setColorizedCC(getCC());
			setColorizedBCC(getBCC());
			setColorizedSubject(getSubject());
			setColorizedContent(getContent());
			break;
		default:
			setColorizedReceiver(getReceiver());
			setColorizedSender(getSender());
			setColorizedCC(getCC());
			setColorizedBCC(getBCC());
			setColorizedSubject(getSubject());
			setColorizedContent(getContent());
			break;
		}
	}

	/**
	 * 
	 * @return the attachments
	 */
	public ArrayList<String> getAttachments() {
		return Attachments;
	}

	/**
	 * 
	 * @return
	 */
	public String getBCC() {
		return BCC;
	}

	/**
	 * 
	 * @return
	 */
	public String getCC() {
		return CC;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedBCC() {
		return ColorizedBCC;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedCC() {
		return ColorizedCC;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedContent() {
		return ColorizedContent;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedReceiver() {
		return ColorizedReceiver;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedSender() {
		return ColorizedSender;
	}

	/**
	 * 
	 * @return
	 */
	public String getColorizedSubject() {
		return ColorizedSubject;
	}

	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return Content;
	}

	/**
	 * 
	 * @return the filePath
	 */
	public String getFilePath() {
		return FilePath;
	}

	/**
	 * 
	 * @return
	 */
	public Date getReceivedDate() {
		return ReceivedDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getReceiver() {
		return Receiver;
	}

	/**
	 * 
	 * @return
	 */
	public String getSender() {
		return Sender;
	}

	/**
	 * 
	 * @return
	 */
	public String getSubject() {
		return Subject;
	}

	/**
	 * 
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(ArrayList<String> attachments) {
		Attachments = attachments;
	}

	/**
	 * 
	 * @param bCC
	 */
	private void setBCC(String bCC) {
		BCC = bCC;
	}

	/**
	 * 
	 * @param cC
	 */
	private void setCC(String cC) {
		CC = cC;
	}

	/**
	 * 
	 * @param colorizedBCC
	 */
	private void setColorizedBCC(String colorizedBCC) {
		ColorizedBCC = colorizedBCC;
	}

	/**
	 * 
	 * @param colorizedCC
	 */
	private void setColorizedCC(String colorizedCC) {
		ColorizedCC = colorizedCC;
	}

	/**
	 * 
	 * @param colorizedContent
	 */
	private void setColorizedContent(String colorizedContent) {
		ColorizedContent = "<html><head></head><body font face=\"Tahoma\">"
				+ colorizedContent.replaceAll("(\r\n|\r|\n|\n\r)", "<br>")
				+ "</body></html>";
	}

	/**
	 * 
	 * @param colorizedReceiver
	 */
	private void setColorizedReceiver(String colorizedReceiver) {
		ColorizedReceiver = colorizedReceiver;
	}

	/**
	 * 
	 * @param colorizedSender
	 */
	private void setColorizedSender(String colorizedSender) {
		ColorizedSender = colorizedSender;
	}

	/**
	 * 
	 * @param colorizedSubject
	 */
	private void setColorizedSubject(String colorizedSubject) {
		ColorizedSubject = colorizedSubject;
	}

	/**
	 * 
	 * @param content
	 */
	private void setContent(String content) {
		Content = content;
	}

	/**
	 * 
	 * @param filePath
	 *            the filePath to set
	 */
	private void setFilePath(String filePath) {
		FilePath = filePath;
	}

	/**
	 * 
	 * @param receivedDate
	 */
	private void setReceivedDate(Date receivedDate) {
		ReceivedDate = receivedDate;
	}

	/**
	 * 
	 * @param receiver
	 */
	private void setReceiver(String receiver) {
		Receiver = receiver;
	}

	/**
	 * 
	 * @param sender
	 */
	private void setSender(String sender) {
		Sender = sender;
	}

	/**
	 * 
	 * @param subject
	 */
	private void setSubject(String subject) {
		Subject = subject;
	}

}
