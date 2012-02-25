package com.eams.mbox2eml;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

public class MessageBox extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String result;

	public static String doDialog(Object owner, String title, String iconFile,
			String msg, String buttons) {
		if (!(owner instanceof Frame) && !(owner instanceof Dialog)) {
			throw new IllegalArgumentException();
		}

		MessageBox dlg;
		Rectangle parentBounds;
		if (owner instanceof Frame) {
			dlg = new MessageBox((Frame) owner, title, iconFile, msg, buttons);
			parentBounds = ((Frame) owner).getBounds();
		} else {
			dlg = new MessageBox((Dialog) owner, title, iconFile, msg, buttons);
			parentBounds = ((Dialog) owner).getBounds();
		}
		Rectangle dlgBounds = dlg.getBounds();
		Dimension screenSize = dlg.getToolkit().getScreenSize();

		int dlgX = parentBounds.x + (parentBounds.width - dlgBounds.width) / 2;
		if (dlgX < 0) {
			dlgX = 0;
		}
		if (dlgX + dlgBounds.width > screenSize.width) {
			dlgX = screenSize.width - dlgBounds.width;
		}
		int dlgY = parentBounds.y + (parentBounds.height - dlgBounds.height)
				/ 2;
		if (dlgY < 0) {
			dlgY = 0;
		}
		if (dlgY + dlgBounds.height > screenSize.height) {
			dlgY = screenSize.height - dlgBounds.height;
		}
		dlg.setLocation(dlgX, dlgY);
		dlg.setVisible(true);
		return dlg.getResult();
	}

	public static String doDialog(Object owner, String title, String msg,
			String buttons) {
		return (doDialog(owner, title, null, msg, buttons));
	}

	public MessageBox(Frame owner, String title, String iconFile, String msg,
			String buttons) {
		super(owner, title, true);
		buildLayout(iconFile, msg, buttons);
	}

	public MessageBox(Dialog owner, String title, String iconFile, String msg,
			String buttons) {
		super(owner, title, true);
		buildLayout(iconFile, msg, buttons);
	}

	private void buildLayout(String iconFile, String msg, String buttons) {
		MultiLineLabel msgLabel;
		ImgPanel iconPanel;
		Panel buttonPanel;
		StringTokenizer st;

		setLayout(new BorderLayout(5, 5));
		setResizable(false);

		if (iconFile != null) {
			iconPanel = new ImgPanel(iconFile);
			add("West", iconPanel);
		}

		msgLabel = new MultiLineLabel(msg, MultiLineLabel.LEFT, 300);
		add("Center", msgLabel);

		buttonPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
		st = new StringTokenizer(buttons, ",");
		while (st.hasMoreTokens()) {
			Button button = new Button(st.nextToken());
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		add("South", buttonPanel);
		pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, "Cancel"));
			}
		});
	}

	public MessageBox(Frame owner, String title, String msg, String buttons) {
		this(owner, title, null, msg, buttons);
	}

	public MessageBox(Dialog owner, String title, String msg, String buttons) {
		this(owner, title, null, msg, buttons);
	}

	public void actionPerformed(ActionEvent event) {
		result = event.getActionCommand();
		setVisible(false);
		dispose();
	}

	public String getResult() {
		return result;
	}

	public Insets getInsets() {
		Insets insets;

		insets = super.getInsets();
		insets.top += 5;
		insets.bottom += 5;
		insets.left += 5;
		insets.right += 5;
		return (insets);
	}
}