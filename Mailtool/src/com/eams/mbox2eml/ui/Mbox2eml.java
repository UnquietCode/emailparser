package com.eams.mbox2eml.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;

public class Mbox2eml extends Frame implements ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AdvancedList messageList;
	List attachments;
	Label fromField;
	Label toField;
	Label subjectField;
	AutoTextArea messageContent;
	MenuBar mainMenu;
	Menu fileMenu, editMenu, optionsMenu, helpMenu;
	MboxHandler mbox = null;
	String sorting;
	String currentDirectory;
	String currentFile;
	String saveDirectory;
	ExtendedFileFilter mbxFilter = new ExtendedFileFilter("*.mbx,*.mbox",
			"Mailbox-Files");
	ExtendedFileFilter mozillaFilter = new ExtendedFileFilter("*",
			"Mozilla-Mail");
	ExtendedFileFilter selectedFileFilter = mozillaFilter;
	Checkbox sortByDate, sortBySender, sortByReceiver, sortBySubject;
	Label stateLabel;
	String appTitle;
	boolean shiftPressed;
	boolean ctrlPressed;
	int lastSelected;
	CheckboxMenuItem fileNameUsesSender;
	CheckboxMenuItem fileNameUsesReceiver;
	CheckboxMenuItem autoCreateDirectory;

	public Mbox2eml() {
		appTitle = "Mbox to Eml Converter";
		setTitle(appTitle);
		setLayout(new BorderLayout());
		setBackground(Color.lightGray);

		mainMenu = new MenuBar();
		fileMenu = new Menu("File");
		fileMenu.add(new MenuItem("Load Mbox..."));
		fileMenu.add(new MenuItem("Export All Messages..."));
		fileMenu.add(new MenuItem("Export Selected Messages..."));
		fileMenu.addSeparator();
		fileMenu.add(new MenuItem("Save All Attachments..."));
		fileMenu.add(new MenuItem("Save Selected Attachments..."));
		fileMenu.addSeparator();
		fileMenu.add(new MenuItem("Exit"));
		fileMenu.addActionListener(this);
		mainMenu.add(fileMenu);
		editMenu = new Menu("Edit");
		editMenu.add(new MenuItem("Select All"));
		editMenu.add(new MenuItem("Select None"));
		editMenu.add(new MenuItem("Reverse Selection"));
		editMenu.addActionListener(this);
		mainMenu.add(editMenu);
		optionsMenu = new Menu("Options");
		autoCreateDirectory = new CheckboxMenuItem(
				"Automatically create subdirectory", false);
		autoCreateDirectory.addItemListener(this);
		fileNameUsesSender = new CheckboxMenuItem(
				"File output: Sender - Subject", true);
		fileNameUsesSender.addItemListener(this);
		fileNameUsesReceiver = new CheckboxMenuItem(
				"File output: Receiver - Subject", false);
		fileNameUsesReceiver.addItemListener(this);
		optionsMenu.add(autoCreateDirectory);
		optionsMenu.addSeparator();
		optionsMenu.add(fileNameUsesSender);
		optionsMenu.add(fileNameUsesReceiver);
		optionsMenu.addActionListener(this);
		mainMenu.add(optionsMenu);
		helpMenu = new Menu("?");
		helpMenu.add(new MenuItem("About"));
		helpMenu.addActionListener(this);
		mainMenu.add(helpMenu);
		setMenuBar(mainMenu);

		fileMenu.getItem(1).setEnabled(false);
		fileMenu.getItem(2).setEnabled(false);
		fileMenu.getItem(4).setEnabled(false);
		fileMenu.getItem(5).setEnabled(false);

		Panel panel1 = new Panel(new BorderLayout());
		Panel panel2 = new Panel(new GridLayout(1, 4, 4, 4));
		CheckboxGroup cbg = new CheckboxGroup();
		panel2.add(new Label("Sorting: "));
		sortBySender = new Checkbox("Sender", cbg, true);
		sortBySender.addItemListener(this);
		panel2.add(sortBySender);
		sortByReceiver = new Checkbox("Receiver", cbg, true);
		sortByReceiver.addItemListener(this);
		panel2.add(sortByReceiver);
		sortBySubject = new Checkbox("Subject", cbg, true);
		sortBySubject.addItemListener(this);
		panel2.add(sortBySubject);
		sortByDate = new Checkbox("Date", cbg, true);
		sortByDate.addItemListener(this);
		panel2.add(sortByDate);

		panel1.add("North", panel2);
		messageList = new AdvancedList(10);
		messageList.setFont(new Font("Courier", Font.PLAIN, 12));
		messageList.addItemListener(this);
		panel1.add("Center", messageList.p);

		add("North", panel1);

		Panel infoPanel = new Panel(new BorderLayout());
		panel1 = new Panel(new GridLayout(3, 1, 0, 0));
		Label label = new Label("From:");
		label.setFont(new Font("Arial", Font.BOLD, 12));
		panel1.add(label);
		label = new Label("To:");
		label.setFont(new Font("Arial", Font.BOLD, 12));
		panel1.add(label);
		label = new Label("Subject:");
		label.setFont(new Font("Arial", Font.BOLD, 12));
		panel1.add(label);
		infoPanel.add("West", panel1);
		panel1 = new Panel(new GridLayout(3, 1, 0, 0));
		fromField = new Label();
		panel1.add(fromField);
		toField = new Label();
		panel1.add(toField);
		subjectField = new Label();
		panel1.add(subjectField);
		infoPanel.add("Center", panel1);

		panel1 = new Panel(new BorderLayout());
		panel1.add("North", infoPanel);
		messageContent = new AutoTextArea(8, 60);
		panel1.add("Center", messageContent);
		attachments = new List();
		attachments.setMultipleMode(true);
		panel1.add("East", attachments);
		stateLabel = new Label("No Mbox Loaded");
		panel1.add("South", stateLabel);

		add("Center", panel1);

		sorting = "Date";

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public static void main(String args[]) {
		Mbox2eml mainFrame = new Mbox2eml();
		mainFrame.setSize(900, 600);
		mainFrame.setVisible(true);
		if (args.length > 0) {
			File file = new File(args[0]);
			if (!file.isAbsolute()) {
				file = new File(file.getAbsolutePath());
			}
			if (file.exists()) {
				mainFrame.currentFile = file.getName();
				mainFrame.currentDirectory = file.getParent();
				if (!mainFrame.currentDirectory.endsWith(File.separator)) {
					mainFrame.currentDirectory = mainFrame.currentDirectory
							+ File.separator;
				}
				mainFrame.loadMbox();
			}
		}
	}

	public void buildMessageList() {
		messageList.setUpdateEnabled(false);
		messageList.removeAll();
		mbox.sort(sorting);
		int numMessages = mbox.getNumMessages();
		for (int i = 0; i < numMessages; i++) {
			String fill = "                                                            ";
			String s = mbox.getSender(i);
			if (s.length() > 20) {
				s = s.substring(0, 17) + "...";
			} else {
				s += fill.substring(s.length(), 20);
			}
			String msg = s + "   ";

			s = mbox.getReceiver(i);
			if (s.length() > 20) {
				s = s.substring(0, 17) + "...";
			} else {
				s += fill.substring(s.length(), 20);
			}
			msg += s + "   ";

			s = mbox.getSubject(i);
			if (s.length() > 40) {
				s = s.substring(0, 37) + "...";
			} else {
				s += fill.substring(s.length(), 40);
			}
			msg += s + "   ";

			s = mbox.getFormattedDate(i);
			msg += s;

			messageList.add(msg);
		}
		messageList.setUpdateEnabled(true);
		stateLabel.setText("0 of " + mbox.getNumMessages()
				+ " Messages Selected");
	}

	public void setSelectedMsgLabel() {
		if (messageList == null)
			return;
		int selected = messageList.getSelectedIndexes().length;
		stateLabel.setText(selected + " of " + mbox.getNumMessages()
				+ " Messages Selected");
	}

	public void loadMbox() {
		String directory;
		directory = currentFile;
		int separatorIndex;
		if ((separatorIndex = directory.lastIndexOf(".")) >= 0) {
			directory = directory.substring(0, separatorIndex);
		}
		saveDirectory = currentDirectory + directory;

		File outFile;
		int j = 1;
		while ((outFile = new File(saveDirectory)).exists()
				&& !outFile.isDirectory()) {
			saveDirectory = currentDirectory + directory + "." + j;
			j++;
		}

		setTitle(appTitle + " - " + currentFile);
		stateLabel.setText("Loading Mbox " + currentDirectory + currentFile
				+ "...");
		if (mbox != null)
			mbox.close();
		try {
			mbox = new MboxHandler(currentDirectory, currentFile);
			fileMenu.getItem(1).setEnabled(true);
			fileMenu.getItem(2).setEnabled(true);
			lastSelected = 0;
			buildMessageList();
		} catch (OutOfMemoryError e) {
			setTitle(appTitle);
			stateLabel.setText("Failed loading Mbox " + currentDirectory
					+ currentFile + "! (Out of Memory)");
			MessageBox
					.doDialog(
							this,
							"Error",
							"stop.gif",
							"Out of Memory!\nTry assigning more memory to your Java VM.",
							"OK");
		}
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof Button) {
			// Button src = (Button) event.getSource();

		}

		if (event.getSource() instanceof Menu) {
			String cmd = event.getActionCommand();
			if (cmd.equals("Load Mbox...")) {
				JFileChooser chooser = new JFileChooser(currentDirectory);
				chooser.addChoosableFileFilter(mbxFilter);
				chooser.addChoosableFileFilter(mozillaFilter);
				chooser.setFileFilter(selectedFileFilter);
				chooser.setDialogTitle("Load Mbox");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFileFilter = (ExtendedFileFilter) chooser
							.getFileFilter();
					currentFile = chooser.getSelectedFile().getName();
					currentDirectory = chooser.getSelectedFile().getParent()
							+ System.getProperty("file.separator");
					loadMbox();
					fromField.setText("");
					toField.setText("");
					subjectField.setText("");
					messageContent.setText("");
					attachments.removeAll();
				}
			} else if (cmd.equals("Export All Messages...")) {
				JFileChooser chooser = new JFileChooser(saveDirectory);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose Export Directory");
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveDirectory = chooser.getSelectedFile()
							+ System.getProperty("file.separator");
					String subDirectory = "";
					if (autoCreateDirectory.getState()) {
						subDirectory = mbox.getFilename();
						if (subDirectory.lastIndexOf(".") >= 0)
							subDirectory = subDirectory.substring(0,
									subDirectory.lastIndexOf("."));
						subDirectory += System.getProperty("file.separator");
					}
					int numMessages = messageList.getItemCount();
					int[] index = new int[numMessages];
					for (int i = 0; i < numMessages; i++) {
						index[i] = i;
					}
					stateLabel.setText("Exporting " + numMessages
							+ " Messages to the Folder " + saveDirectory
							+ subDirectory + "...");
					int exported = mbox.saveAsEml(index,
							fileNameUsesReceiver.getState(), saveDirectory
									+ subDirectory);
					stateLabel.setText(exported
							+ " Messages Successfully Exported to the Folder "
							+ saveDirectory + subDirectory + " !");
					if (numMessages != exported) {
						MessageBox.doDialog(this, "Warning", "exclamation.gif",
								(numMessages - exported)
										+ " Messages were not exported!", "OK");
					}
				}
			} else if (cmd.equals("Export Selected Messages...")) {
				JFileChooser chooser = new JFileChooser(saveDirectory);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose Export Directory");
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveDirectory = chooser.getSelectedFile()
							+ System.getProperty("file.separator");
					String subDirectory = "";
					if (autoCreateDirectory.getState()) {
						subDirectory = mbox.getFilename();
						if (subDirectory.lastIndexOf(".") >= 0)
							subDirectory = subDirectory.substring(0,
									subDirectory.lastIndexOf("."));
						subDirectory += System.getProperty("file.separator");
					}
					int[] selected = messageList.getSelectedIndexes();
					int numMessages = selected.length;
					stateLabel.setText("Exporting " + numMessages
							+ " Messages to the Folder " + saveDirectory
							+ subDirectory + "...");
					int exported = mbox.saveAsEml(selected,
							fileNameUsesReceiver.getState(), saveDirectory
									+ subDirectory);
					stateLabel.setText(exported
							+ " Messages Successfully Exported to the Folder "
							+ saveDirectory + subDirectory + " !");
					if (numMessages != exported) {
						MessageBox.doDialog(this, "Warning", "exclamation.gif",
								(numMessages - exported)
										+ " messages were not exported!", "OK");
					}
				}
			} else if (cmd.equals("Save All Attachments...")) {
				JFileChooser chooser = new JFileChooser(saveDirectory);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose Export Directory");
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveDirectory = chooser.getSelectedFile()
							+ System.getProperty("file.separator");
					int numAttachments = attachments.getItemCount();
					int[] index = new int[numAttachments];
					for (int i = 0; i < numAttachments; i++) {
						index[i] = i;
					}
					stateLabel.setText("Saving " + numAttachments
							+ " Attachments to the Folder " + saveDirectory
							+ "...");
					int saved = mbox.saveAttachments(lastSelected, index,
							saveDirectory);
					stateLabel.setText(saved
							+ " Attachments Successfully Saved to the Folder "
							+ saveDirectory + " !");
					if (numAttachments != saved) {
						MessageBox.doDialog(this, "Warning", "exclamation.gif",
								(numAttachments - saved)
										+ " attachments were not saved!", "OK");
					}
				}
			} else if (cmd.equals("Save Selected Attachments...")) {
				JFileChooser chooser = new JFileChooser(saveDirectory);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose Export Directory");
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveDirectory = chooser.getSelectedFile()
							+ System.getProperty("file.separator");
					int[] selected = attachments.getSelectedIndexes();
					int numAttachments = selected.length;
					stateLabel.setText("Saving " + numAttachments
							+ " Attachments to the Folder " + saveDirectory
							+ "...");
					int saved = mbox.saveAttachments(lastSelected, selected,
							saveDirectory);
					stateLabel.setText(saved
							+ " Attachments Successfully Saved to the Folder "
							+ saveDirectory + " !");
					if (numAttachments != saved) {
						MessageBox.doDialog(this, "Warning", "exclamation.gif",
								(numAttachments - saved)
										+ " attachments were not saved!", "OK");
					}
				}
			} else if (cmd.equals("Exit")) {
				dispose();
				System.exit(0);
			} else if (cmd.equals("Select All")) {
				int numMessages = messageList.getItemCount();
				for (int i = 0; i < numMessages; i++) {
					messageList.select(i);
				}
				setSelectedMsgLabel();
			} else if (cmd.equals("Select None")) {
				int numMessages = messageList.getItemCount();
				for (int i = 0; i < numMessages; i++) {
					messageList.deselect(i);
				}
				setSelectedMsgLabel();
			} else if (cmd.equals("Reverse Selection")) {
				int[] selected = messageList.getSelectedIndexes();
				int numMessages = messageList.getItemCount();
				int j = 0;
				for (int i = 0; i < numMessages; i++) {
					if (j < selected.length && i == selected[j]) {
						messageList.deselect(i);
						j++;
					} else
						messageList.select(i);
				}
				setSelectedMsgLabel();
			} else if (cmd.equals("About")) {
				MessageBox
						.doDialog(
								this,
								"About",
								"info.gif",
								"Mbox2eml Version 1.2.1\n(C) Ulrich Krebs 2002-2009\neMail: ukrebs@freenet.de",
								"OK");
			}
		}
	}

	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() instanceof CheckboxMenuItem) {
			CheckboxMenuItem src = (CheckboxMenuItem) event.getSource();
			if (src == fileNameUsesSender) {
				if (fileNameUsesSender.getState())
					fileNameUsesReceiver.setState(false);
				else
					fileNameUsesSender.setState(true);
			}
			if (src == fileNameUsesReceiver) {
				if (fileNameUsesReceiver.getState())
					fileNameUsesSender.setState(false);
				else
					fileNameUsesReceiver.setState(true);
			}

			System.out.println(src);
		}

		if (event.getSource() instanceof Checkbox) {
			Checkbox src = (Checkbox) event.getSource();

			if (src == sortByDate) {
				sorting = "Date";
			} else if (src == sortBySender) {
				sorting = "Sender";
			} else if (src == sortByReceiver) {
				sorting = "Receiver";
			} else if (src == sortBySubject) {
				sorting = "Subject";
			} else
				return;

			if (mbox == null)
				return;

			buildMessageList();
		}

		if (event.getSource() instanceof AdvancedList) {
			AdvancedList src = (AdvancedList) event.getSource();
			if (src == messageList) {
				if (mbox == null || lastSelected >= mbox.getNumMessages())
					return;

				lastSelected = ((Integer) event.getItem()).intValue();

				fromField.setText(mbox.getSender(lastSelected));
				toField.setText(mbox.getReceiver(lastSelected));
				subjectField.setText(mbox.getSubject(lastSelected));
				messageContent.setText("");
				messageContent.setText(mbox.getMessageContent(lastSelected));
				String[] attachmentFileNames = mbox
						.getAttachmentFileNames(lastSelected);
				attachments.removeAll();
				fileMenu.getItem(4).setEnabled(attachmentFileNames.length > 0);
				fileMenu.getItem(5).setEnabled(attachmentFileNames.length > 0);
				for (int i = 0; i < attachmentFileNames.length; i++) {
					attachments.add(attachmentFileNames[i]);
				}
				setSelectedMsgLabel();
			}
		}
	}
}