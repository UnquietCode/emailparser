package org.owlmail.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;

import org.owlmail.data.MailData;
import org.owlmail.data.Message;
import org.owlmail.global.MailSetting;
import org.owlmail.global.SearchType;
import org.owlmail.res.Resource;
import org.owlmail.utils.ImageUtil;
import org.owlmail.view.help.Help;
import org.owlmail.view.render.OwlListCellRender;
import org.owlmail.view.setting.Settings;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Owlmail {

	private JFrame frame;
	private Container container;
	private JCheckBox chbxShowCCandBCC;
	private JCheckBox chbxShowAttachedFiles;
	private JEditorPane epMessageBody;
	private JLabel lblInfo;
	private JLabel lblWork;
	private JDialog loadingDialog;
	private JProgressBar progressBar;
	private JSplitPane splitPane;
	private SpringLayout layout;
	private JTextField tfSearchText;
	private JScrollPane scrollPane;
	private JButton btnSettings;
	private JButton btnHelp;
	private Settings settings;
	private Help help;
	private static JComboBox cbSelectSearchType;
	private static JList listResults;
	private static MailData Mails;

	private static boolean showAttachments = false;
	private static boolean showCC_BCC = false;

	private SearchType Type;

	private static String Path = System.getProperty("user.dir");

	public Owlmail() {
		initialize();
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
			@Override
			protected Boolean doInBackground() {
				Boolean bOk = true;
				loadingDialog = new JDialog(frame,
						Resource.getValue("Owlmail.Loading"));
				final JLabel label = new JLabel();
				label.setIcon(ImageUtil
						.getImageIcon("org/owlmail/view/images/generator.gif"));
				final JPanel contentPane = new JPanel();
				contentPane.setBorder(BorderFactory.createEmptyBorder(25, 25,
						25, 25));
				contentPane.setLayout(new BorderLayout());
				contentPane.add(label, BorderLayout.NORTH);
				loadingDialog.setContentPane(contentPane);
				loadingDialog.pack();
				loadingDialog.setLocationRelativeTo(null);
				loadingDialog.setVisible(true);
				setType(SearchType.SUBJECT);
				Mails = new MailData(getPath(), progressBar, lblWork);
				RefreshView("");
				lblWork.setVisible(false);
				progressBar.setVisible(false);
				layout.putConstraint(SpringLayout.SOUTH, splitPane, -10,
						SpringLayout.SOUTH, container);
				return bOk;
			}

			@Override
			protected void done() {
				try {
					get();
					loadingDialog.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(ImageUtil
				.getImage("org/owlmail/view/images/Owlmail.png"));
		frame.setTitle(Resource.getValue("Owlmail.AppName"));
		frame.setBounds(100, 100, 754, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout = new SpringLayout();

		container = frame.getContentPane();
		container.setLayout(layout);

		tfSearchText = new JTextField();
		layout.putConstraint(SpringLayout.WEST, tfSearchText, 10,
				SpringLayout.WEST, container);
		tfSearchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (MailSetting.isManualSearch()) {
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						RefreshView(tfSearchText.getText());
					}
				} else {
					RefreshView(tfSearchText.getText());
				}
			}
		});
		container.add(tfSearchText);
		tfSearchText.setColumns(10);

		JLabel lblSearchtext = new JLabel(
				Resource.getValue("Owlmail.lblSearchText"));
		layout.putConstraint(SpringLayout.NORTH, tfSearchText, 6,
				SpringLayout.SOUTH, lblSearchtext);
		layout.putConstraint(SpringLayout.SOUTH, tfSearchText, 34,
				SpringLayout.SOUTH, lblSearchtext);
		layout.putConstraint(SpringLayout.NORTH, lblSearchtext, 10,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, lblSearchtext, 10,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, lblSearchtext, 26,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, lblSearchtext, 97,
				SpringLayout.WEST, container);
		container.add(lblSearchtext);

		cbSelectSearchType = new JComboBox();
		layout.putConstraint(SpringLayout.WEST, cbSelectSearchType, 6,
				SpringLayout.EAST, tfSearchText);
		layout.putConstraint(SpringLayout.EAST, cbSelectSearchType, -10,
				SpringLayout.EAST, container);
		cbSelectSearchType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String type = (String) cb.getSelectedItem();
				if (type == Resource.getValue("Owlmail.typeSubject")) {
					setType(SearchType.SUBJECT);
				}
				if (type == Resource.getValue("Owlmail.typePerson")) {
					setType(SearchType.PERSON);
				}
				if (type == Resource.getValue("Owlmail.typeText")) {
					setType(SearchType.CONTENT);
				}
				if (type == Resource.getValue("Owlmail.typeSenderOnly")) {
					setType(SearchType.SENDER_ONLY);
				}
				if (type == Resource.getValue("Owlmail.typeReceiverOnly")) {
					setType(SearchType.RECEIVER_ONLY);
				}
				if (!type.equals("")) {
					RefreshView(tfSearchText.getText());
				} else {
					switch (getType()) {
					case SUBJECT:
						cb.setSelectedItem(Resource
								.getValue("Owlmail.typeSubject"));
						break;
					case PERSON:
						cb.setSelectedItem(Resource
								.getValue("Owlmail.typePerson"));
						break;
					case CONTENT:
						cb.setSelectedItem(Resource
								.getValue("Owlmail.typeText"));
						break;
					case SENDER_ONLY:
						cb.setSelectedItem(Resource
								.getValue("Owlmail.typeSenderOnly"));
						break;
					case RECEIVER_ONLY:
						cb.setSelectedItem(Resource
								.getValue("Owlmail.typeReceiverOnly"));
						break;
					}
				}
			}
		});
		cbSelectSearchType.setModel(new DefaultComboBoxModel(new String[] {
				Resource.getValue("Owlmail.typeSubject"),
				Resource.getValue("Owlmail.typePerson"),
				Resource.getValue("Owlmail.typeText"), "",
				Resource.getValue("Owlmail.typeSenderOnly"),
				Resource.getValue("Owlmail.typeReceiverOnly") }));
		container.add(cbSelectSearchType);

		JLabel lblSearchIn = new JLabel(
				Resource.getValue("Owlmail.lblSearchIn"));
		layout.putConstraint(SpringLayout.NORTH, cbSelectSearchType, 7,
				SpringLayout.SOUTH, lblSearchIn);
		layout.putConstraint(SpringLayout.EAST, lblSearchIn, 0,
				SpringLayout.EAST, cbSelectSearchType);
		layout.putConstraint(SpringLayout.NORTH, lblSearchIn, 0,
				SpringLayout.NORTH, lblSearchtext);
		layout.putConstraint(SpringLayout.WEST, lblSearchIn, 0,
				SpringLayout.WEST, cbSelectSearchType);
		layout.putConstraint(SpringLayout.SOUTH, lblSearchIn, 26,
				SpringLayout.NORTH, container);
		container.add(lblSearchIn);

		splitPane = new JSplitPane();
		layout.putConstraint(SpringLayout.WEST, splitPane, 10,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, splitPane, -10,
				SpringLayout.EAST, container);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		container.add(splitPane);

		scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane.setLeftComponent(scrollPane);

		listResults = new JList();
		listResults.setDoubleBuffered(true);
		listResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listResults);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		epMessageBody = new JEditorPane();
		epMessageBody.setEditable(false);
		epMessageBody.setContentType("text/html");
		scrollPane_1.setViewportView(epMessageBody);
		splitPane.setDividerLocation(550);

		chbxShowAttachedFiles = new JCheckBox(
				Resource.getValue("Owlmail.lblShowAttachedFiles"));
		layout.putConstraint(SpringLayout.NORTH, chbxShowAttachedFiles, 6,
				SpringLayout.SOUTH, tfSearchText);
		layout.putConstraint(SpringLayout.WEST, chbxShowAttachedFiles, 10,
				SpringLayout.WEST, container);
		chbxShowAttachedFiles.setMnemonic('D');
		layout.putConstraint(SpringLayout.NORTH, splitPane, 6,
				SpringLayout.SOUTH, chbxShowAttachedFiles);
		chbxShowAttachedFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chbxShowAttachedFiles.isSelected()) {
					setShowAttachments(true);
				} else {
					setShowAttachments(false);
				}
				RefreshView(tfSearchText.getText());
			}
		});
		container.add(chbxShowAttachedFiles);

		chbxShowCCandBCC = new JCheckBox(
				Resource.getValue("Owlmail.lblShowCCbCC"));
		layout.putConstraint(SpringLayout.NORTH, chbxShowCCandBCC, 6,
				SpringLayout.SOUTH, tfSearchText);
		chbxShowCCandBCC.setMnemonic('C');
		chbxShowCCandBCC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chbxShowCCandBCC.isSelected()) {
					setShowCC_BCC(true);
				} else {
					setShowCC_BCC(false);
				}
				RefreshView(tfSearchText.getText());
			}
		});
		layout.putConstraint(SpringLayout.WEST, chbxShowCCandBCC, 6,
				SpringLayout.EAST, chbxShowAttachedFiles);
		container.add(chbxShowCCandBCC);

		btnHelp = new JButton(Resource.getValue("Owlmail.dlgHelpCaption"));
		layout.putConstraint(SpringLayout.SOUTH, cbSelectSearchType, -6,
				SpringLayout.NORTH, btnHelp);
		btnHelp.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/help.png"));
		layout.putConstraint(SpringLayout.NORTH, btnHelp, 66,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, btnHelp, -10,
				SpringLayout.EAST, container);
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				help = new Help();
				help.setVisible(true);
			}
		});
		container.add(btnHelp);

		progressBar = new JProgressBar();
		layout.putConstraint(SpringLayout.WEST, progressBar, 10,
				SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, progressBar, -10,
				SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.EAST, progressBar, -10,
				SpringLayout.EAST, container);
		container.add(progressBar);

		lblInfo = new JLabel("");
		layout.putConstraint(SpringLayout.NORTH, lblInfo, 69,
				SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.SOUTH, lblInfo, -9,
				SpringLayout.NORTH, splitPane);
		layout.putConstraint(SpringLayout.EAST, tfSearchText, 0,
				SpringLayout.EAST, lblInfo);
		lblInfo.setForeground(SystemColor.textHighlight);
		container.add(lblInfo);

		lblWork = new JLabel();
		layout.putConstraint(SpringLayout.SOUTH, splitPane, -6,
				SpringLayout.NORTH, lblWork);
		layout.putConstraint(SpringLayout.WEST, lblWork, 10, SpringLayout.WEST,
				container);
		layout.putConstraint(SpringLayout.SOUTH, lblWork, -6,
				SpringLayout.NORTH, progressBar);
		container.add(lblWork);

		btnSettings = new JButton(Resource.getValue("Owlmail.lblSettings"));
		layout.putConstraint(SpringLayout.EAST, lblInfo, -6, SpringLayout.WEST,
				btnSettings);
		layout.putConstraint(SpringLayout.NORTH, btnSettings, 0,
				SpringLayout.NORTH, btnHelp);
		layout.putConstraint(SpringLayout.EAST, btnSettings, -6,
				SpringLayout.WEST, btnHelp);
		btnSettings.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/setting.png"));
		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings = new Settings();
				settings.setVisible(true);
			}
		});
		container.add(btnSettings);
	}

	private void RefreshView(String SearchText) {
		lblWork.setText(Resource.getValue("Owlmail.lblRendering"));
		progressBar.setValue(0);
		Mails.searchMails(SearchText, getType(), progressBar);
		ArrayList<Message> mails = Mails.getResult();
		DefaultListModel model = new DefaultListModel();
		progressBar.setValue(0);
		progressBar.setMaximum(mails.size());

		for (Iterator<Message> iterator = mails.iterator(); iterator.hasNext();) {
			Message message = iterator.next();
			model.addElement(message);
			progressBar.setValue(progressBar.getValue() + 1);
		}

		for (int i = 0; i < listResults.getMouseListeners().length; i++) {
			listResults.removeMouseListener(listResults.getMouseListeners()[i]);
		}

		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (listResults.getModel().getSize() != -1) {
					if (e.getClickCount() == 1) {
						int index = listResults.locationToIndex(e.getPoint());
						Object item = listResults.getModel()
								.getElementAt(index);
						listResults.setSelectedIndex(index);
						epMessageBody.setText(((Message) item)
								.getColorizedContent());
						epMessageBody.setCaretPosition(0);
					}
					if ((e.getClickCount() == 1)
							&& (e.getModifiers() == InputEvent.BUTTON3_MASK)) {
						int index = listResults.locationToIndex(e.getPoint());
						Object item = listResults.getModel()
								.getElementAt(index);
						try {
							if (Desktop.isDesktopSupported()) {
								Desktop.getDesktop()
										.open(new File(((Message) item)
												.getFilePath()));
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
		listResults.setModel(model);
		listResults.setCellRenderer(new OwlListCellRender());
		listResults.addMouseListener(mouseListener);
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	private SearchType getType() {
		return Type;
	}

	private void setType(SearchType type) {
		Type = type;
	}

	public static boolean isShowAttachments() {
		return showAttachments;
	}

	public static boolean isShowCC_BCC() {
		return showCC_BCC;
	}

	public static void setShowAttachments(boolean showAttachments) {
		Owlmail.showAttachments = showAttachments;
	}

	public static void setShowCC_BCC(boolean showCC_BCC) {
		Owlmail.showCC_BCC = showCC_BCC;
	}

	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}

	public void setExtendedState(int s) {
		frame.setExtendedState(s);
	}

}
