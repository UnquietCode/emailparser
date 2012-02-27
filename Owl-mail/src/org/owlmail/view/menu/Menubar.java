package org.owlmail.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.owlmail.res.Resource;
import org.owlmail.utils.ImageUtil;

public class Menubar extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3703550726612035601L;

	public Menubar() {
		//
		JMenu fileMenu = new JMenu(Resource.getValue("Menubar.file"));
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);

		JMenuItem printSetupItem = new JMenuItem(
				Resource.getValue("Menubar.file.printSetup"));
		printSetupItem.addActionListener(this);

		JMenuItem printItem = new JMenuItem(
				Resource.getValue("Menubar.file.print"));
		printItem.setMnemonic(KeyEvent.VK_P);
		printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		printItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/print.png"));
		printItem.addActionListener(this);

		JMenuItem impItem = new JMenuItem(
				Resource.getValue("Menubar.file.import"));
		impItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/import.png"));
		impItem.setMnemonic(KeyEvent.VK_I);
		impItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));
		impItem.addActionListener(this);

		JMenuItem expItem = new JMenuItem(
				Resource.getValue("Menubar.file.export"));
		expItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/export.png"));
		expItem.setMnemonic(KeyEvent.VK_E);
		expItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));
		expItem.addActionListener(this);

		JMenuItem exitItem = new JMenuItem(
				Resource.getValue("Menubar.file.exit"));
		exitItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/exit.png"));
		exitItem.setMnemonic(KeyEvent.VK_Q);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
		exitItem.addActionListener(this);

		//
		JMenu viewMenu = new JMenu(Resource.getValue("Menubar.view"));
		viewMenu.setMnemonic(KeyEvent.VK_V);
		add(viewMenu);

		JMenuItem friendItem = new JMenuItem(
				Resource.getValue("Menubar.view.friend"));
		friendItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/friend.png"));
		friendItem.setMnemonic(KeyEvent.VK_F);
		friendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.ALT_MASK));
		friendItem.addActionListener(this);

		JMenuItem logItem = new JMenuItem(Resource.getValue("Menubar.view.log"));
		logItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/log.png"));
		logItem.setMnemonic(KeyEvent.VK_L);
		logItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));
		logItem.addActionListener(this);

		JMenuItem statItem = new JMenuItem(
				Resource.getValue("Menubar.view.statistics"));
		statItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/statistics.png"));
		statItem.setMnemonic(KeyEvent.VK_S);
		statItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		statItem.addActionListener(this);

		JMenuItem viewToolItem = new JMenuItem(
				Resource.getValue("Menubar.view.ToolSetup"));
		viewToolItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/tool.png"));
		viewToolItem.setMnemonic(KeyEvent.VK_T);
		viewToolItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.ALT_MASK));
		viewToolItem.addActionListener(this);

		//
		JMenu emailMenu = new JMenu(Resource.getValue("Menubar.email"));
		emailMenu.setMnemonic(KeyEvent.VK_E);
		add(emailMenu);

		JMenuItem starItem = new JMenuItem(
				Resource.getValue("Menubar.email.star"));
		starItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/star.png"));
		starItem.setMnemonic(KeyEvent.VK_S);
		starItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		starItem.addActionListener(this);

		JMenuItem labelItem = new JMenuItem(
				Resource.getValue("Menubar.email.label"));
		labelItem.setMnemonic(KeyEvent.VK_L);
		labelItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.ALT_MASK));
		labelItem.addActionListener(this);

		JMenuItem garbageItem = new JMenuItem(
				Resource.getValue("Menubar.email.garbage"));
		garbageItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/garbage.png"));
		garbageItem.setMnemonic(KeyEvent.VK_G);
		garbageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.ALT_MASK));
		garbageItem.addActionListener(this);

		JMenuItem classificationItem = new JMenuItem(
				Resource.getValue("Menubar.email.classification"));
		classificationItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/classification.png"));
		classificationItem.setMnemonic(KeyEvent.VK_C);
		classificationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.ALT_MASK));
		classificationItem.addActionListener(this);

		JMenuItem propItem = new JMenuItem(
				Resource.getValue("Menubar.email.property"));
		propItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/classification.png"));
		propItem.setMnemonic(KeyEvent.VK_P);
		propItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.ALT_MASK));
		propItem.addActionListener(this);

		//
		JMenu safeMenu = new JMenu(Resource.getValue("Menubar.safe"));
		safeMenu.setMnemonic(KeyEvent.VK_S);
		add(safeMenu);

		JMenuItem userItem = new JMenuItem(
				Resource.getValue("Menubar.safe.user"));
		userItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/user.png"));
		userItem.setMnemonic(KeyEvent.VK_U);
		userItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.ALT_MASK));
		userItem.addActionListener(this);

		JMenuItem attachItem = new JMenuItem(
				Resource.getValue("Menubar.safe.attachment"));
		attachItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/attachment.png"));
		attachItem.setMnemonic(KeyEvent.VK_A);
		attachItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		attachItem.addActionListener(this);

		JMenuItem backupItem = new JMenuItem(
				Resource.getValue("Menubar.safe.backup"));
		backupItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/backup.png"));
		backupItem.setMnemonic(KeyEvent.VK_B);
		backupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				ActionEvent.ALT_MASK));
		backupItem.addActionListener(this);

		JMenuItem preferenceItem = new JMenuItem(
				Resource.getValue("Menubar.safe.preference"));
		preferenceItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/preference.png"));
		preferenceItem.setMnemonic(KeyEvent.VK_P);
		preferenceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.ALT_MASK));
		preferenceItem.addActionListener(this);

		JMenuItem indexItem = new JMenuItem(
				Resource.getValue("Menubar.safe.index"));
		indexItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/index.png"));
		indexItem.setMnemonic(KeyEvent.VK_I);
		indexItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));
		indexItem.addActionListener(this);

		JMenuItem languageItem = new JMenuItem(
				Resource.getValue("Menubar.safe.language"));
		languageItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/language.png"));
		languageItem.addActionListener(this);

		//
		JMenu helpMenu = new JMenu(Resource.getValue("Menubar.help"));
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(helpMenu);

		JMenuItem helpCenterItem = new JMenuItem(
				Resource.getValue("Menubar.help.helpCenter"));
		helpCenterItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/help.png"));
		helpCenterItem.addActionListener(this);

		JMenuItem updateItem = new JMenuItem(
				Resource.getValue("Menubar.help.update"));
		updateItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/update.png"));
		updateItem.addActionListener(this);

		JMenuItem aboutItem = new JMenuItem(
				Resource.getValue("Menubar.help.about"));
		aboutItem.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/about.png"));
		aboutItem.addActionListener(this);

		//
		fileMenu.add(impItem);
		fileMenu.add(expItem);
		fileMenu.addSeparator();
		fileMenu.add(printSetupItem);
		fileMenu.add(printItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		viewMenu.add(friendItem);
		viewMenu.add(logItem);
		viewMenu.add(statItem);
		viewMenu.addSeparator();
		viewMenu.add(viewToolItem);

		emailMenu.add(starItem);
		emailMenu.add(labelItem);
		emailMenu.addSeparator();
		emailMenu.add(classificationItem);
		emailMenu.add(garbageItem);
		emailMenu.addSeparator();
		emailMenu.add(propItem);

		safeMenu.add(userItem);
		safeMenu.addSeparator();
		safeMenu.add(attachItem);
		safeMenu.add(backupItem);
		safeMenu.add(indexItem);
		safeMenu.addSeparator();
		safeMenu.add(languageItem);
		safeMenu.add(preferenceItem);

		helpMenu.add(helpCenterItem);
		helpMenu.add(updateItem);
		helpMenu.add(aboutItem);
	}

	// Deals with the Action Events.
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand() + " Selected \n");
	}
}