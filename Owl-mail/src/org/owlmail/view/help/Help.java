package org.owlmail.view.help;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.owlmail.res.Resource;
import org.owlmail.utils.ImageUtil;

/**
 * 
 * @Description 帮助面板
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Help extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2558321882672488917L;

	private final JPanel contentPanel = new JPanel();

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			Help dialog = new Help();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Help() {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setIconImage(ImageUtil.getImage("org/owlmail/view/images/help.png"));
		setModal(true);
		setTitle(Resource.getValue("Owlmail.AppName") + " - "
				+ Resource.getValue("Owlmail.dlgHelpCaption"));
		setBounds(400, 150, 450, 180);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel text = new JLabel("<html>"
				+ Resource.getValue("Owlmail.HelpText") + "</html>");
		contentPanel.add(text);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnOk = new JButton(Resource.getValue("Settings.btnOk"));
		btnOk.setIcon(ImageUtil
				.getImageIcon("org/owlmail/view/images/accept.png"));
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btnOk.setMnemonic('O');
		btnOk.setActionCommand("OK");
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
	}
}
