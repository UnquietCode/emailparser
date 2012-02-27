/**
 * Settings.java
 * 
 * @author Goldenbogen, Pierre
 *         Created: 15.12.2011 13:11:33
 */
package net.goldenbogen.jmsgreader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import net.goldenbogen.jmsgreader.core.UserSettings;

/**
 * 
 * @Description 设置面板
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Settings extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8109284926276490287L;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			Settings dialog = new Settings();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Settings() {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						Settings.class
								.getResource("/net/goldenbogen/jmsgreader/setting_tools.png")));
		setModal(true);
		setTitle(Resource.getString("JMsgReader.AppName") + " - "
				+ Resource.getString("JMsgReader.lblSettings"));
		setBounds(100, 100, 490, 167);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		final JCheckBox chbxSearchAfterRetrunOnly = new JCheckBox(
				Resource.getString("Settings.lblOnlySearchAfterReturnWasHit"));
		chbxSearchAfterRetrunOnly.setMnemonic('S');
		sl_contentPanel
				.putConstraint(SpringLayout.NORTH, chbxSearchAfterRetrunOnly,
						10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST,
				chbxSearchAfterRetrunOnly, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST,
				chbxSearchAfterRetrunOnly, -7, SpringLayout.EAST, contentPanel);
		chbxSearchAfterRetrunOnly.setSelected(UserSettings.isManualSearch());
		contentPanel.add(chbxSearchAfterRetrunOnly);

		JLabel lblSearchAfterReturnInfoText = new JLabel(
				Resource.getString("Settings.lblInfoForlblOnlySearchAfterReturnWasHit"));
		sl_contentPanel.putConstraint(SpringLayout.NORTH,
				lblSearchAfterReturnInfoText, 6, SpringLayout.SOUTH,
				chbxSearchAfterRetrunOnly);
		sl_contentPanel.putConstraint(SpringLayout.EAST,
				lblSearchAfterReturnInfoText, -10, SpringLayout.EAST,
				chbxSearchAfterRetrunOnly);
		lblSearchAfterReturnInfoText.setFont(new Font("Tahoma", Font.BOLD
				| Font.ITALIC, 11));
		lblSearchAfterReturnInfoText.setForeground(new Color(51, 153, 255));
		contentPanel.add(lblSearchAfterReturnInfoText);

		final JCheckBox chbxEnableCaching = new JCheckBox(
				Resource.getString("Settings.lblEnableCaching"));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, chbxEnableCaching, 6,
				SpringLayout.SOUTH, lblSearchAfterReturnInfoText);
		sl_contentPanel.putConstraint(SpringLayout.WEST, chbxEnableCaching, 0,
				SpringLayout.WEST, chbxSearchAfterRetrunOnly);
		sl_contentPanel.putConstraint(SpringLayout.EAST, chbxEnableCaching, 0,
				SpringLayout.EAST, chbxSearchAfterRetrunOnly);
		chbxEnableCaching.setSelected(UserSettings.isEnableCaching());
		chbxEnableCaching.setMnemonic('S');
		contentPanel.add(chbxEnableCaching);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOk = new JButton(Resource.getString("Settings.btnOk"));
		btnOk.setIcon(new ImageIcon(Settings.class
				.getResource("/net/goldenbogen/jmsgreader/accept.png")));
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserSettings.setManualSearch(chbxSearchAfterRetrunOnly
						.isSelected());
				UserSettings.setEnableCaching(chbxEnableCaching.isSelected());
				dispose();
			}
		});

		btnOk.setMnemonic('O');
		btnOk.setActionCommand("OK");
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		JButton btnCancel = new JButton(
				Resource.getString("Settings.btnCancel"));
		btnCancel.setIcon(new ImageIcon(Settings.class
				.getResource("/net/goldenbogen/jmsgreader/cancel.png")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btnCancel.setMnemonic('b');
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
	}
}
