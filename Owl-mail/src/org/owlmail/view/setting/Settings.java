package org.owlmail.view.setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.owlmail.global.MailSetting;
import org.owlmail.res.Resource;
import org.owlmail.utils.ImageUtil;

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
	private static final long serialVersionUID = 796203678229883959L;

	private final JPanel contentPanel = new JPanel();

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
		setIconImage(ImageUtil.getImage("org/owlmail/view/images/setting.png"));
		setModal(true);
		setTitle(Resource.getValue("Owlmail.AppName") + " - "
				+ Resource.getValue("Owlmail.lblSettings"));
		setBounds(400, 150, 490, 167);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		final JCheckBox chbxSearchAfterRetrunOnly = new JCheckBox(
				Resource.getValue("Settings.lblOnlySearchAfterReturnWasHit"));
		chbxSearchAfterRetrunOnly.setMnemonic('S');
		sl_contentPanel
				.putConstraint(SpringLayout.NORTH, chbxSearchAfterRetrunOnly,
						10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST,
				chbxSearchAfterRetrunOnly, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST,
				chbxSearchAfterRetrunOnly, -7, SpringLayout.EAST, contentPanel);
		chbxSearchAfterRetrunOnly.setSelected(MailSetting.isManualSearch());
		contentPanel.add(chbxSearchAfterRetrunOnly);

		JLabel lblSearchAfterReturnInfoText = new JLabel(
				Resource.getValue("Settings.lblInfoForlblOnlySearchAfterReturnWasHit"));
		sl_contentPanel.putConstraint(SpringLayout.NORTH,
				lblSearchAfterReturnInfoText, 6, SpringLayout.SOUTH,
				chbxSearchAfterRetrunOnly);
		sl_contentPanel.putConstraint(SpringLayout.EAST,
				lblSearchAfterReturnInfoText, -10, SpringLayout.EAST,
				chbxSearchAfterRetrunOnly);
		lblSearchAfterReturnInfoText.setForeground(new Color(51, 153, 255));
		contentPanel.add(lblSearchAfterReturnInfoText);

		final JCheckBox chbxEnableCaching = new JCheckBox(
				Resource.getValue("Settings.lblEnableCaching"));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, chbxEnableCaching, 6,
				SpringLayout.SOUTH, lblSearchAfterReturnInfoText);
		sl_contentPanel.putConstraint(SpringLayout.WEST, chbxEnableCaching, 0,
				SpringLayout.WEST, chbxSearchAfterRetrunOnly);
		sl_contentPanel.putConstraint(SpringLayout.EAST, chbxEnableCaching, 0,
				SpringLayout.EAST, chbxSearchAfterRetrunOnly);
		chbxEnableCaching.setSelected(MailSetting.isEnableCaching());
		chbxEnableCaching.setMnemonic('S');
		contentPanel.add(chbxEnableCaching);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOk = new JButton(Resource.getValue("Settings.btnOk"));
		btnOk.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/accept.png"));
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MailSetting.setManualSearch(chbxSearchAfterRetrunOnly
						.isSelected());
				MailSetting.setEnableCaching(chbxEnableCaching.isSelected());
				dispose();
			}
		});

		btnOk.setMnemonic('O');
		btnOk.setActionCommand("OK");
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		JButton btnCancel = new JButton(Resource.getValue("Settings.btnCancel"));
		btnCancel.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/cancel.png"));
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
