package com.eams.mbox2eml;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.StringTokenizer;

public class MultiLineLabel extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String text;
	private String formattedText;

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	private int alignment;
	private int ratio;

	public MultiLineLabel(String text, int alignment, int ratio) {
		this.text = new String(text);
		setAlignment(alignment);
		setRatio(ratio);
	}

	public MultiLineLabel(String text) {
		this(text, LEFT, 200);
	}

	public MultiLineLabel() {
		this("");
	}

	public Dimension getMinimumSize() {
		FontMetrics fm;
		int textHeight, textWidth;
		int numLines;
		int actualWidth;
		String line;
		String word;
		StringTokenizer st;

		fm = getFontMetrics(getFont());

		textHeight = fm.getHeight();
		textWidth = fm.stringWidth(text);

		if (text.indexOf("\n") == -1) {
			numLines = 0;
			int preferredWidth;
			do {
				numLines++;
				preferredWidth = textWidth / numLines;
			} while (100 * preferredWidth / (numLines * textHeight) > ratio);

			st = new StringTokenizer(text, " ");

			while (st.hasMoreTokens()) {
				word = st.nextToken();
				if (fm.stringWidth(word) > preferredWidth) {
					preferredWidth = fm.stringWidth(word);
				}
			}

			actualWidth = preferredWidth;

			st = new StringTokenizer(text, " ");
			line = new String();
			formattedText = new String();
			numLines = 1;

			while (st.hasMoreTokens()) {
				word = st.nextToken();
				if (fm.stringWidth(line + word) > preferredWidth) {
					int diff1 = preferredWidth - fm.stringWidth(line);
					int diff2 = fm.stringWidth(line + word) - preferredWidth;
					if (diff2 < diff1) {
						line += word;
						if (fm.stringWidth(line) > actualWidth) {
							actualWidth = fm.stringWidth(line);
						}
						formattedText += line + "\n";
						line = "";
					} else {
						formattedText += line + "\n";
						line = word + " ";
					}
					numLines++;
				} else {
					line += word + " ";
				}
			}

			if (line.equals("")) {
				numLines--;
				if (formattedText.endsWith("\n")) {
					formattedText = formattedText.substring(0,
							formattedText.lastIndexOf("\n"));
				}
			} else {
				formattedText += line;
			}
		} else {
			line = new String();
			formattedText = new String(text);
			numLines = 1;
			actualWidth = 0;
			st = new StringTokenizer(formattedText, "\n", true);

			while (st.hasMoreTokens()) {
				line = st.nextToken();
				if (line.equals("\n")) {
					numLines++;
				}
				if (fm.stringWidth(line) > actualWidth) {
					actualWidth = fm.stringWidth(line);
				}
			}
		}

		return new Dimension(actualWidth + 4, numLines * textHeight);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	public void setText(String text) {
		this.text = text;
		if (getParent() != null) {
			getParent().doLayout();
		}
	}

	public String getText() {
		return text;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
		if (getParent() != null) {
			getParent().doLayout();
		}
	}

	public int getRatio() {
		return ratio;
	}

	public void setAlignment(int alignment) {
		if (alignment >= LEFT && alignment <= RIGHT) {
			this.alignment = alignment;
			repaint();
		}
	}

	public int getAlignment() {
		return alignment;
	}

	private void drawAlignedString(Graphics g, String s, int y) {
		int x;
		FontMetrics fm;

		fm = getFontMetrics(getFont());

		switch (alignment) {
		case LEFT:
			x = 0;
			break;
		case CENTER:
			x = (this.getWidth() - fm.stringWidth(s)) / 2;
			break;
		case RIGHT:
			x = this.getWidth() - fm.stringWidth(s);
			break;
		default:
			x = 0;
		}
		g.drawString(s, x, y);
	}

	public void paint(Graphics g) {
		FontMetrics fm = getFontMetrics(getFont());
		int textHeight = fm.getHeight();

		int drawY = fm.getAscent();

		if (formattedText == null) {
			return;
		}

		StringTokenizer st = new StringTokenizer(formattedText, "\n", true);

		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (line.equals("\n")) {
				drawY += textHeight;
			} else {
				drawAlignedString(g, line, drawY);
			}
		}
	}
}