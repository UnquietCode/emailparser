package com.eams.mbox2eml;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class AdvancedList extends List implements ItemListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean shiftPressed;
	private boolean ctrlPressed;
	private int lastSelected;
	private List buf;
	private ItemListener itemListener = null;
	public Panel p;

	public AdvancedList(int rows) {
		super(rows, true);
		buf = new List(rows);
		super.addItemListener(this);
		super.addKeyListener(this);
		p = new Panel(new BorderLayout());
		p.add("Center", this);
	}

	public AdvancedList() {
		this(4);
	}

	public void addItemListener(ItemListener il) {
		itemListener = il;
	}

	public void setUpdateEnabled(boolean update) {
		if (update) {
			p.remove(buf);
			p.add("Center", this);
		} else {
			p.remove(this);
			p.add("Center", buf);
		}
		p.doLayout();
	}

	public void itemStateChanged(ItemEvent event) {
		int newSelected = ((Integer) event.getItem()).intValue();
		int[] curSelected = getSelectedIndexes();

		if (!ctrlPressed) {
			for (int i = 0; i < curSelected.length; i++) {
				deselect(curSelected[i]);
			}
			select(newSelected);
			if (shiftPressed) {
				if (newSelected > lastSelected) {
					for (int i = lastSelected; i <= newSelected; i++) {
						select(i);
					}
				} else {
					for (int i = lastSelected; i >= newSelected; i--) {
						select(i);
					}
				}
			} else {
				lastSelected = newSelected;
			}
		} else {
			lastSelected = newSelected;
		}

		if (itemListener != null) {
			itemListener.itemStateChanged(new ItemEvent(this,
					ItemEvent.ITEM_STATE_CHANGED, new Integer(newSelected),
					ItemEvent.SELECTED));
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlPressed = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}