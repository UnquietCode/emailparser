package com.eams.mbox2eml.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.net.URL;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class ImgPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Image img;

	public ImgPanel(String fileName) {
		try {
			URL url = getClass().getResource(fileName);
			img = getToolkit().getImage(url);
		} catch (Exception e) {
		}

		MediaTracker mt = new MediaTracker(this);
		mt.addImage(img, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
		}
	}

	public Dimension getMinimumSize() {
		return (new Dimension(img.getWidth(this), img.getHeight(this)));
	}

	public Dimension getPreferredSize() {
		return (getMinimumSize());
	}

	public Dimension getMaximumSize() {
		return (getMinimumSize());
	}

	public void paint(Graphics g) {
		g.drawImage(img, (this.getSize().width - img.getWidth(this)) / 2,
				(this.getSize().height - img.getHeight(this)) / 2, this);
	}
}
