package com.eams.mbox2eml;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.TextArea;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class AutoTextArea extends TextArea implements ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean internalUpdate = false;
	private String orgText;

	public AutoTextArea() {
		super();
		setEditable(false);
		setBackground(Color.white);
		addComponentListener(this);
	}

	public AutoTextArea(int rows, int columns) {
		super(rows, columns);
		setEditable(false);
		setBackground(Color.white);
		addComponentListener(this);
	}

	public AutoTextArea(String text) {
		super(text);
		setEditable(false);
		setBackground(Color.white);
		orgText = text;
		addComponentListener(this);
	}

	public AutoTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		setEditable(false);
		setBackground(Color.white);
		orgText = text;
		addComponentListener(this);
	}

	public AutoTextArea(String text, int rows, int columns, int scrollbars) {
		super(text, rows, columns, scrollbars);
		setEditable(false);
		setBackground(Color.white);
		orgText = text;
		addComponentListener(this);
	}

	public void setText(String text) {
		if (!internalUpdate) {
			orgText = text;
			text = reformat(text);
		}
		super.setText(text);
	}

	public String getText() {
		return orgText;
	}

	private String reformat(String text) {
		StringBuffer formattedText = new StringBuffer();
		StringBuffer buf;

		if (text == null) {
			return null;
		}

		int width = getSize().width - 40;

		FontMetrics fm = getFontMetrics(getFont());

		BufferedReader br = new BufferedReader(new StringReader(text));
		String line;
		String separator = "\n";

		try {
			while ((line = br.readLine()) != null) {
				StringTokenizer wt = new StringTokenizer(line, " ");
				buf = new StringBuffer();
				while (wt.hasMoreTokens()) {
					String word = wt.nextToken();
					int textWidth = fm.stringWidth(buf + " " + word);
					if (textWidth > width) {
						formattedText.append(buf);
						formattedText.append(separator);
						buf = new StringBuffer(word);
					} else {
						if (buf.length() > 0) {
							buf.append(" ");
						}
						buf.append(word);
					}
				}
				formattedText.append(buf);
				formattedText.append(separator);
			}
			br.close();
		} catch (IOException e) {
		}
		return formattedText.toString();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		internalUpdate = true;
		setText(reformat(orgText));
		internalUpdate = false;
	}

	public void componentShown(ComponentEvent e) {
	}
}