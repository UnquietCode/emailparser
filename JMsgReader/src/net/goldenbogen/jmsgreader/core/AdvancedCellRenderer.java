package net.goldenbogen.jmsgreader.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.goldenbogen.jmsgreader.JMsgReader;
import net.goldenbogen.jmsgreader.Resource;

/**
 * 
 * @Description List单元格渲染
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class AdvancedCellRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7414087954856212800L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean iss, boolean chf) {
		super.getListCellRendererComponent(list, value, index, iss, chf);
		if (value instanceof Message) {
			Color color = new Color(224, 239, 255);
			if (!iss) {
				color = Color.white;
			}
			JPanel mypanel = new JPanel();
			int Rows = 2;
			if (JMsgReader.isShowCC_BCC()) {
				if (!((Message) value).getColorizedCC().isEmpty()) {
					Rows++;
				}
				if (!((Message) value).getColorizedBCC().isEmpty()) {
					Rows++;
				}
				if (!((Message) value).getColorizedCC().isEmpty()
						|| !((Message) value).getColorizedBCC().isEmpty()) {
					Rows++;
				}
			}

			if (!(((Message) value).getAttachments().size() == 0)
					&& JMsgReader.isShowAttachments()) {
				Rows++;
			}
			mypanel.setLayout(new GridLayout(Rows, 0, 0, 5));
			TitledBorder title;

			title = BorderFactory.createTitledBorder(DateFormat
					.getDateTimeInstance().format(
							((Message) value).getReceivedDate()));
			mypanel.setBorder(title);
			mypanel.setEnabled(true);

			JPanel FromTo = new JPanel();
			FromTo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

			JLabel lblfrom = new JLabel(
					"<html><font color=maroon><b>" + Resource.getString("JMsgReader.lblFrom") + ":</b></font> " + ((Message) value).getColorizedSender() + "<html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			lblfrom.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblfrom.setPreferredSize(new Dimension(300, 17));
			lblfrom.setMaximumSize(new Dimension(300, 17));
			FromTo.add(lblfrom);

			JLabel lblto = new JLabel("<html><font color=green><b>"
					+ Resource.getString("JMsgReader.lblTo") + ":</b></font> "
					+ ((Message) value).getColorizedReceiver() + "<html>");
			lblto.setFont(new Font("Tahoma", Font.PLAIN, 14));
			FromTo.add(lblto);

			mypanel.add(FromTo);

			if (JMsgReader.isShowCC_BCC()) {
				if (!((Message) value).getColorizedCC().isEmpty()) {
					JPanel CC = new JPanel();
					CC.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

					JLabel lblccspacer = new JLabel("");
					lblccspacer.setFont(new Font("Tahoma", Font.PLAIN, 14));
					lblccspacer.setPreferredSize(new Dimension(300, 17));
					lblccspacer.setMaximumSize(new Dimension(300, 17));
					CC.add(lblccspacer);

					JLabel lblcc = new JLabel("<html><font color=green><b>"
							+ Resource.getString("JMsgReader.lblCC")
							+ ":</b></font> "
							+ ((Message) value).getColorizedCC() + "<html>");
					lblcc.setFont(new Font("Tahoma", Font.PLAIN, 14));
					CC.add(lblcc);

					CC.setBackground(color);

					mypanel.add(CC);
				}

				if (!((Message) value).getColorizedBCC().isEmpty()) {
					JPanel BCC = new JPanel();
					BCC.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

					JLabel lblbccspacer = new JLabel("");
					lblbccspacer.setFont(new Font("Tahoma", Font.PLAIN, 14));
					lblbccspacer.setPreferredSize(new Dimension(300, 17));
					lblbccspacer.setMaximumSize(new Dimension(300, 17));
					BCC.add(lblbccspacer);

					JLabel lblbcc = new JLabel(
							"<html><font color=green><b>" + Resource.getString("JMsgReader.lblBCC") + ":</b></font> " + ((Message) value).getColorizedBCC() + "<html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					lblbcc.setFont(new Font("Tahoma", Font.PLAIN, 14));
					BCC.add(lblbcc);

					BCC.setBackground(color);

					mypanel.add(BCC);
				}

				if (!((Message) value).getColorizedCC().isEmpty()
						|| !((Message) value).getColorizedBCC().isEmpty()) {
					JPanel Spacer = new JPanel();
					Spacer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

					JLabel lblspacer = new JLabel("");
					lblspacer.setFont(new Font("Tahoma", Font.PLAIN, 14));
					lblspacer.setPreferredSize(new Dimension(300, 17));
					lblspacer.setMaximumSize(new Dimension(300, 17));
					Spacer.add(lblspacer);

					Spacer.setBackground(color);

					mypanel.add(Spacer);
				}
			}

			JLabel lblbetreff = new JLabel("<html><font color=#4B0082><b>"
					+ Resource.getString("JMsgReader.lblSubject")
					+ ":</b></font> " + ((Message) value).getColorizedSubject()
					+ "<html>");
			lblbetreff.setFont(new Font("Tahoma", Font.PLAIN, 14));
			mypanel.add(lblbetreff);

			if (JMsgReader.isShowAttachments()) {
				if (!(((Message) value).getAttachments().size() == 0)) {
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 0; i < ((Message) value).getAttachments()
							.size(); i++) {
						stringBuffer.append(((Message) value).getAttachments()
								.get(i) + "   -   ");
					}
					String files = stringBuffer.toString();
					files = files.substring(0, files.lastIndexOf("-") - 2);
					JLabel lblAttachments = new JLabel("<html>"
							+ Resource.getString("JMsgReader.lblAttachments")
							+ ": <font color=blue>" + files + "</font><html>");
					lblAttachments.setFont(new Font("Tahoma", Font.PLAIN, 14));
					mypanel.add(lblAttachments);
				}
			}

			FromTo.setBackground(color);
			mypanel.setBackground(color);

			return mypanel;
		} else {
			return null;
		}
	}
}
