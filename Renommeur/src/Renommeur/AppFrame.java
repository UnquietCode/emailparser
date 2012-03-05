package Renommeur;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Application.ExitListener;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

public class AppFrame extends FrameView {

	private int NbRen;

	public AppFrame(SingleFrameApplication app) {
		super(app);
		instance = getFrame();

		NbRen = 0;
		initComponents();
		updateStatusMessage(locale
				.getString("INITIALISATION DE L'APPLICATION."));
		try {
			instance.setIconImage(ImageIO.read(Renommeur.class
					.getResourceAsStream("/Renommeur/resources/icon.png")));
		} catch (IOException ex) {
		}

		app.addExitListener(new ConfirmExitListener());
		ConfigureView(false);

		instance.setTitle(ApplicationProperty.getString("Application.title"));
		instance.setPreferredSize(new Dimension(800, 600));
		instance.pack();
		instance.setVisible(false);
		instance.setVisible(true);
		instance.setExtendedState(JFrame.MAXIMIZED_BOTH);

		if (Renommeur.args.length != 0) {
			File F = new File(Renommeur.args[0]);
			if (!F.exists()) {
				JOptionPane
						.showMessageDialog(
								instance,
								locale.getString("IMPOSSIBLE D'OUVRIR LE DOSSIER PASSÉ EN ARGUMENT."),
								locale.getString("IMPOSSIBLE D'OUVRIR LE DOSSIER"),
								JOptionPane.ERROR_MESSAGE);
			} else if (F.isDirectory()) {
				doOpenThisDirectory(F);
			} else {
				doOpenThisDirectory(F.getParentFile());
			}
		}
	}

	/**
	 * 更新文本和图片
	 * 
	 * @param ChangeTexte
	 * @param LoadImage
	 */
	public void doChangeTextAndImage(boolean ChangeTexte, boolean LoadImage) {
		if (isLoading || photos == null) {
			return;
		}
		isLoading = true;

		if (LoadImage) {
			thread.SetCurrentItem(photos.getCurrent());
			doDrawImage(photos.getCurrent());
			exif.setText(photos.getCurrent().getExif());
		}

		if (ChangeTexte) {
			jlname.setEnabled(!photos.getCurrent().isDel());
			jtf.setEnabled(!photos.getCurrent().isDel());
			for (Component c : jPanelDesButtons.getComponents()) {
				c.setEnabled(!photos.getCurrent().isDel());
			}
			if (!photos.getCurrent().isDel()) {
				if (photos.getCurrent().hasChanged()) {
					jtf.setText(photos.getCurrent().getName());
					jlname.setText("<html><font color='black'><b>"
							+ locale.getString("NOM")
							+ " : </b>"
							+ photos.getCurrent().getName(photos.dontDel(),
									isNumPhotos()) + "</font></html>");
				} else {
					jtf.setText("");
					jlname.setText("<html><font color='blue'><b>"
							+ locale.getString("NOM")
							+ " : </b><i>"
							+ photos.getCurrent().getName(photos.dontDel(),
									isNumPhotos()) + "</i></font></html>");
				}
			} else {
				jtf.setText(locale
						.getString("IMAGE MARQUÉE À SUPPRIMER. 'SUPPR' OU BOUTON DROIT POUR LA RESTAURER."));
				jlname.setText("<html><font color='red'><strike><b>"
						+ locale.getString("NOM") + " : </b>"
						+ photos.getCurrent().getName(photos.dontDel(), false)
						+ "</stike></font></html>");
			}
		}
		isLoading = false;
	}

	private void doDrawImage(Photo p) {
		jlphoto.setText(null);
		if (!p.IsEnoughGood(jlphoto)) {
			p.RemoveGde();
		}
		jlphoto.setIcon(p.getBigImage(jlphoto.getWidth(), jlphoto.getHeight()));
		jlphoto.repaint();
	}

	@Action
	public void actionDel() {
		photos.delete();
	}

	@Action
	public void actionNum() {
		if (!isNumPhotos()) {
			photos.ResoudreMemeNom();
		}
		doChangeTextAndImage(true, false);
	}

	@Action
	public void actionCreateButton() {
		String Texte = jtf.getSelectedText();
		if (Texte != null) {
			if (!Texte.trim().equals("")) {
				updateStatusMessage(locale
						.getString("FABRIQUE UN NOUVEAU BOUTON."));
				if (isThisFileNameValid(Texte)) {
					while (Texte.startsWith(" ")) {
						Texte = Texte.substring(1, Texte.length() - 1);
					}
					while (Texte.endsWith(" ")) {
						Texte = Texte.substring(0, Texte.length() - 2);
					}
					JButton b = new JButton(Texte);
					b.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent evt) {
							ActionSurBouton(evt);
						}
					});
					jPanelDesButtons.add(b);
					jPanelDesButtons.repaint();
				}
				updateStatusMessage(locale.getString("NOUVEAU BOUTON CRÉÉ."));
			}
		}
	}

	@Action
	public void actionPrev() {
		doAller(-1);
	}

	@Action
	public void actionNext() {
		doAller(1);
	}

	@Action
	public void ExeSave() {
		NbRen = photos.size();
		// 显示
		tabbedPane.remove(browsePane);
		browsePane = new BrowsePane(this);
		tabbedPane.add(locale.getString("GÉRER TOUTES LES IMAGES"), browsePane);
		ConfigureView(false);
		instance.setTitle(ApplicationProperty.getString("Application.title"));
		updateStatusMessage(locale.getString("OPÉRATION EN COURS..."));

		for (Photo p : photos) {
			try {
				if (p.isDel()) {
					p.getFile().delete();
				} else if (p.hasChanged()) {
					p.getFile().renameTo(
							new File(
									p.getFile().getParent()
											+ File.separator
											+ p.getName(photos.dontDel(),
													isNumPhotos()) + "."
											+ p.getExtension()));
				}
			} catch (Exception err) {
				JOptionPane
						.showMessageDialog(
								instance,
								locale.getString("LORS DE L'OPÉRATION SUR LE FICHIER")
										+ " '"
										+ p.getFile().getName()
										+ "'\n"
										+ locale.getString("ERREUR")
										+ " : "
										+ err.getLocalizedMessage(),
								locale.getString("ERREUR LORS DE L'APPLICATION DES MODIFICATIONS !"),
								JOptionPane.ERROR_MESSAGE);
			}

		}
		try {
			File svg = new File(photos.firstElement().getFile().getParent()
					+ File.separator + NameSaveGuard);
			svg.delete();
		} catch (Exception e) {
		}
		jlphoto.setText("<html><h1 align='center'>"
				+ locale.getString("RENOMMAGE ET SUPPRESSION DES IMAGES TERMINÉ.")
				+ "</h1><center>" + NewVersion() + Language());
		photos = null;
		updateStatusMessage("<html>"
				+ locale.getString("CHARGEZ UN NOUVEAU DOSSIER."));
	};

	@Action
	public void showAbout() {
		JLabel message = new JLabel("<html>"
				+ "<p align='center'><font color='red'><b>"
				+ ApplicationProperty.getString("Application.name")
				+ "</b></font><br><i>"
				+ ApplicationProperty.getString("Application.licence")
				+ "</i></p><br><u>" + locale.getString("VERSION")
				+ " :</u><font color='blue'> "
				+ ApplicationProperty.getString("Application.version") + " ("
				+ ApplicationProperty.getString("Application.date")
				+ ")</font><br><u>" + locale.getString("AUTEUR")
				+ " :</u><font color='blue'> "
				+ ApplicationProperty.getString("Application.vendor")
				+ "</font><br><u>" + locale.getString("SITE INTERNET")
				+ " :</u><font color='blue'> "
				+ ApplicationProperty.getString("Application.homepage")
				+ "</font><br>" + locale.getString("MERCI") + "</html>",
				JLabel.CENTER);
		JOptionPane.showMessageDialog(instance, message,
				locale.getString("À PROPOS DE..."),
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Action
	public void showHelp() {
		try {
			InputStream in = Renommeur.class
					.getResourceAsStream("/Renommeur/resources/"
							+ ApplicationProperty
									.getString("Application.helpFileName"));
			JEditorPane jep = new JEditorPane();
			// 避免中文乱码
			jep.setContentType("text/html;charset=utf-8");
			jep.setEditable(false);
			jep.read(in, jep);
			JScrollPane jsp = new JScrollPane(jep);
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(800, 600));
			JOptionPane.showMessageDialog(instance, jsp,
					locale.getString("AIDE"), JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VecPhotos getVecPhotos() {
		return photos;
	}

	public void switch2BrowsePane() {
		if (getSelectedTab() == VISIONNEUR) {
			browsePane.RefreshAllImages();
		}
	}

	public void refreshPhoto(Photo p) {
		browsePane.refreshThisPhoto(p);
	}

	private void doActiveNextPrev(boolean b) {
		Component[] liste = { jmValide, jmPrev, jmNext, jmAjouter, jmDel };
		for (Component c : liste) {
			c.setEnabled(b);
		}
	}

	/**
	 * 设置缓冲区的优先缩略图
	 * 
	 * @param deb
	 * @param fin
	 */
	public void setBufferZoneThumbnailsPriority(int deb, int fin) {
		thread.setZoneThumbnailsPriority(deb, fin);
	}

	public JProgressBar getProgressCache() {
		return cache;
	}

	public JLabel getPhotoPanel() {
		return jlphoto;
	}

	public void doAller(int i) {
		if (photos == null) {
			return;
		}
		isValideInput();
		photos.setCurrent(photos.photoGoto(i));
	}

	@Action
	public void actionOpenDir() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
			updateStatusMessage(locale
					.getString("CHOISISSEZ LE DOSSIER COMPRENANT VOS PHOTOS"));
			File F = chooser.getSelectedFile();
			doOpenThisDirectory(F);
		}
	}

	private void doOpenThisDirectory(File F) {
		File[] liste = F.listFiles();
		int NbPhotos = 0;
		boolean CanWrite = true;
		for (File fichier : liste) {
			if (Photo.isReadableImage(fichier)) {
				CanWrite &= fichier.canWrite();
				NbPhotos++;
			}
		}
		if (NbPhotos != 0 && CanWrite) {
			boolean saveGuard = false;
			for (File f : liste) {
				saveGuard |= f.getName().equalsIgnoreCase(NameSaveGuard);
			}
			if (saveGuard) {
				if (JOptionPane.NO_OPTION == JOptionPane
						.showConfirmDialog(
								instance,
								locale.getString("VOULEZ-VOUS UTILISER LE FICHIER DE SAUVEGARDE ?"),
								locale.getString("UN FICHIER DE SAUVEGARDE A ÉTÉ TROUVÉ."),
								JOptionPane.YES_NO_OPTION)) {
					saveGuard = false;
				} else {
					try {
						for (File f : liste) {
							if (f.getName().equalsIgnoreCase(NameSaveGuard)) {
								ObjectInputStream ois = new ObjectInputStream(
										new FileInputStream(F.getAbsolutePath()
												+ File.separator
												+ NameSaveGuard));
								photos = (VecPhotos) ois.readObject();
								updateStatusMessage(photos
										.Restaure(this, liste));
							}
						}
					} catch (Exception e) {
						JOptionPane
								.showMessageDialog(
										instance,
										e.getLocalizedMessage()
												+ "\n"
												+ locale.getString("OUBLIE LA SAUVEGARDE."),
										locale.getString("UNE ERREUR EST SURVENUE LORS DE LA LECTURE DE LA SAUVEGARDE."),
										JOptionPane.ERROR_MESSAGE);
						saveGuard = false;
					}
				}
			}

			if (!saveGuard) {
				photos = new VecPhotos(this);
				for (File fichier : liste) {
					if (Photo.isReadableImage(fichier)) {
						photos.add(new Photo(fichier));
					}
				}
				updateStatusMessage(photos.size()
						+ locale.getString(" PHOTO(S) DANS LE DOSSIER."));
				photos.sort();
				photos.FirstIsCurrent();
			}

			instance.setTitle(ApplicationProperty
					.getString("Application.title")
					+ " - \""
					+ photos.getCurrent().getFile().getParent() + "\"");
			int nb = photos.NbDone();
			ConfigureView(true);
			travail.setMaximum(photos.size());
			travail.setValue(nb);
			travail.setStringPainted(true);
			travail.setString(locale.getString("AVANCEMENT ") + " "
					+ (int) (travail.getPercentComplete() * 100) + "%");
			travail.setToolTipText(nb + "/" + photos.size());
			thread = new BufferThread(this);
			thread.start();
			doChangeTextAndImage(true, true);
		} else {
			photos = null;
			if (CanWrite) {
				JOptionPane
						.showMessageDialog(
								null,
								locale.getString("LE DOSSIER CHOISI NE COMPORTE PAS DE PHOTOS JPEG.")
										+ "\n"
										+ locale.getString("VOUS DEVEZ EN OUVRIR UN AUTRE."),
								locale.getString("OUVERTURE D'UN DOSSIER."),
								JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								locale.getString("DES PHOTOS SONT PROTÉGÉES EN ÉCRITURE.")
										+ "\n"
										+ locale.getString("LE PROGRAMME NE PEUT PAS OUVRIR CE DOSSIER."),
								locale.getString("OUVERTURE D'UN DOSSIER."),
								JOptionPane.ERROR_MESSAGE);
			}
			ConfigureView(false);
		}
	}

	@Action
	public void actionHideDelPhoto() {
		photos.dontDel();
		if (getHide() && photos.getCurrent().isDel()) {
			doAller(1);
		}
		switch2BrowsePane();
	}

	/**
	 * 验证输入有效性
	 * 
	 * @return
	 */
	private boolean isValideInput() {
		if (photos == null) {
			return false;
		}
		if (!isThisFileNameValid(jtf.getText())) {
			return false;
		}
		if (!photos.getCurrent().isDel()
				&& !jtf.getText().isEmpty()
				&& !jtf.getText().equals(
						photos.getCurrent().getName(photos.dontDel(), false))) {
			photos.getCurrent().setName(jtf.getText());
			if (!isNumPhotos()) {
				photos.ResoudreMemeNom();
			}
			updateStatusMessage(locale
					.getString("SAISIE VALIDÉE. LES CHANGEMENTS SERONT TOUS EFFECTUÉS À LA FIN."));
			int nb = photos.NbDone();
			travail.setValue(nb);
			travail.setString(locale.getString("AVANCEMENT ")
					+ (int) (travail.getPercentComplete() * 100) + "%");
			travail.setToolTipText(nb + "/" + photos.size());
		}
		return true;
	}

	/**
	 * 更新状态信息
	 * 
	 * @param msg
	 */
	public void updateStatusMessage(String msg) {
		SaveMessage = msg;
		statusMessageLabel.setText(msg);
	}

	public void setStatusMessageOngletVisionneuse(String msg) {
		statusMessageLabel.setText(msg);
	}

	private boolean isThisFileNameValid(String s) {
		byte[] Interdits = { '\\', '/', '"', '*', ':', '|', '?', '`', '~', '>',
				'<' };
		int i = 0;
		boolean ret = true;
		while (i < Interdits.length) {
			ret &= (-1 == s.indexOf(Interdits[i++]));
		}
		if (!ret) {
			JOptionPane
					.showMessageDialog(
							null,
							locale.getString("VOUS AVEZ ENTRÉ DES CARACTÈRES INVALIDES DANS LA CHAINE ")
									+ "\"" + s + "\"", locale
									.getString("CARACTÈRE INVALIDES !"),
							JOptionPane.ERROR_MESSAGE);
		}
		return ret;
	}

	private void ActionSurBouton(MouseEvent evt) {
		if ((evt.getButton() == MouseEvent.BUTTON1 || evt.getButton() == MouseEvent.BUTTON2)
				&& jtf.getText().isEmpty()) {
			jtf.setText(((JButton) evt.getComponent()).getText());
		} else if (evt.getButton() == MouseEvent.BUTTON1) {
			if (jtf.getSelectedText() != null) {
				String debut = jtf.getText().substring(0,
						jtf.getSelectionStart());
				String fin = jtf.getText().substring(jtf.getSelectionEnd(),
						jtf.getText().length());
				jtf.setText(debut + ((JButton) evt.getComponent()).getText()
						+ fin);
			} else {
				String deb, fin;
				deb = jtf.getText().substring(0, jtf.getCaretPosition());
				fin = jtf.getText().substring(jtf.getCaretPosition(),
						jtf.getText().length());
				while (deb.endsWith(" ")) {
					deb.substring(0, deb.length() - 1);
				}
				while (fin.startsWith(" ")) {
					fin.substring(1, deb.length());
				}

				jtf.setText(deb + " "
						+ ((JButton) evt.getComponent()).getText() + " " + fin);
			}
		} else if (evt.getButton() == MouseEvent.BUTTON2) {
			jtf.setText(jtf.getText() + ", "
					+ ((JButton) evt.getComponent()).getText());
		} else if (evt.getButton() == MouseEvent.BUTTON3) {
			jPanelDesButtons.remove(evt.getComponent());
			jPanelDesButtons.repaint();
			jPanelDesButtons.revalidate();
		}
	}

	private void ConfigureView(boolean IsLoaded) {
		Vector<Component> agarder = new Vector<Component>();
		agarder.add(jlphoto);
		agarder.add(statusMessageLabel);
		agarder.add(jmOpen);
		agarder.add(exitMenuItem);
		agarder.add(aboutMenuItem);
		agarder.add(jlname);
		agarder.add(jmHelp);

		Component[] list = { instance.getJMenuBar() };
		doActiveLesComposants(instance.getContentPane().getComponents(),
				agarder, IsLoaded);
		doActiveLesComposants(list, agarder, IsLoaded);

		tabbedPane.setEnabledAt(1, IsLoaded);
		if (!IsLoaded) {
			jlname.setText(locale.getString("PAS DE FICHIER CHARGÉ."));
			jlphoto.setIcon(null);
			jlphoto.setText("<html><h1 align='center'>"
					+ locale.getString("VOUS DEVEZ CHARGER UN DOSSIER.")
					+ "</h1><center>" + NewVersion() + Language());
			jPanelDesButtons.removeAll();
			updateStatusMessage(DefaultMessage);

			jtf.setText("");
			travail.setValue(0);
			travail.setString("");
			travail.setToolTipText("");
		}
	}

	public void doDontHide() {
		jcbHide.setSelected(false);
	}

	public boolean getHide() {
		return jcbHide.isSelected();
	}

	public boolean isNumPhotos() {
		return jcbNum.isSelected();
	}

	public void setButtons(JButton[] tabbuttons) {
		for (JButton b : tabbuttons) {
			jPanelDesButtons.add(b);
			b.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					ActionSurBouton(evt);
				}
			});
		}
	}

	private void doActiveLesComposants(Component[] liste,
			Vector<Component> agarder, boolean IsLoaded) {
		for (Component c : liste) {
			if (c instanceof JPanel || c instanceof JTabbedPane
					|| c instanceof JMenuBar) {
				doActiveLesComposants(((Container) c).getComponents(), agarder,
						IsLoaded);
			} else if (c instanceof JMenu) {
				doActiveLesComposants(((JMenu) c).getMenuComponents(), agarder,
						IsLoaded);
			} else if (!agarder.contains(c)) {
				c.setEnabled(IsLoaded);
				if (c instanceof JCheckBoxMenuItem) {
					((JCheckBoxMenuItem) c).setState(true);
				}
				if (c instanceof JLabel) {
					((JLabel) c).setText("");
					((JLabel) c).setIcon(null);
				}
			}
		}
	}

	public int getSelectedTab() {
		if (tabbedPane.getSelectedComponent() == jPanelRenommeur) {
			return RENOMMEUR;
		}
		if (tabbedPane.getSelectedComponent() == browsePane) {
			return VISIONNEUR;
		}
		return -1;
	}

	class ConfirmExitListener implements ExitListener {
		@Override
		public boolean canExit(EventObject arg0) {
			if (photos != null)
				if (photos.size() != 0) {
					int i = JOptionPane
							.showConfirmDialog(
									instance,
									locale.getString("LES MODIFICATION EFFECTUÉES N'ONT PAS ÉTÉ APPLIQUÉES (CTRL + ALT + ENTRÉE).")
											+ "\n"
											+ locale.getString("VOULEZ-VOUS ENREGISTRER VOTRE TRAVAIL POUR POUVOIR CONTINUER UNE AUTRE FOIS (LES MOFICIATIONS NE SERONT PAS APPLIQUÉES MAINTENANT) ?"),
									locale.getString("QUITTER ?"),
									JOptionPane.YES_NO_CANCEL_OPTION);
					if (i == JOptionPane.CANCEL_OPTION) {
						return false;
					}
					if (i == JOptionPane.NO_OPTION) {
						return true;
					}
					// YES !
					photos.Save(NameSaveGuard, jPanelDesButtons.getComponents());
					return true;
				}
			return true;
		}

		@Override
		public void willExit(EventObject arg0) {
			System.exit(0);
		}
	}

	public void setActiveTab(int onglet) {
		if (onglet == RENOMMEUR) {
			tabbedPane.setSelectedComponent(jPanelRenommeur);
			return;
		}
		if (onglet == VISIONNEUR && browsePane != null) {
			tabbedPane.setSelectedComponent(browsePane);
			return;
		}
	}

	private InputStream GetMajInputStream() {
		try {
			URL url = new URL(
					ApplicationProperty.getString("Application.UpdateURL")
							+ "?nb="
							+ NbRen
							+ "&AppVersion="
							+ ApplicationProperty
									.getString("Application.version") + "&os="
							+ System.getProperty("os.name") + ","
							+ System.getProperty("os.version") + "&lang="
							+ Locale.getDefault().toString());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			// Configuration de la requete.
			connection.setRequestMethod("GET");
			connection.setRequestProperty(
					"User-Agent",
					System.getProperty("os.name") + ","
							+ System.getProperty("os.version"));
			NbRen = 0;
			return connection.getInputStream();

		} catch (Exception ex) {

		}
		return null;
	}

	private String NewVersion() {
		String rep = "";

		try {
			String lastestTxt = "";
			byte[] tb = new byte[150];
			int i;

			InputStream is = GetMajInputStream();
			while ((i = is.read(tb)) != -1) {
				lastestTxt += new String(tb, 0, i);
			}
			String[] tabVersions = lastestTxt.split("\n");
			for (String s : tabVersions) {
				if (!s.startsWith("#")) {
					String[] Version = s.split("##");
					if (Version[0].compareToIgnoreCase(ApplicationProperty
							.getString("Application.version")) > 0) {
						String nouveautés = "";
						// En langue par défaut
						for (String lang : Version) {
							if (lang.startsWith(Locale.getDefault()
									.getLanguage() + "#")) {
								nouveautés = lang.substring(3, lang.length());
							}
						}
						// En anglais
						if (nouveautés.equals("")) {
							for (String lang : Version) {
								if (lang.startsWith("en#")) {
									nouveautés = lang.substring(3,
											lang.length());
								}
							}
						}
						// Le 1er venu
						if (nouveautés.equals("")) {
							if (Version.length > 0) {
								if (Version[1].length() > 3) {
									nouveautés = Version[1].substring(3,
											Version[1].length());
								}
								// Message par défaut
								else {
									nouveautés = locale
											.getString("PAS_D_INFORMATIONS_DE_VERSION");
									nouveautés = nouveautés.replaceAll("\\|",
											"</div></li><li><div align=left>");
								}
							}
						}

						// Pour tous :
						rep += "</center><p align='left'><b><u>" + Version[0]
								+ " :</u></b><br><ul><li><div align=left>"
								+ nouveautés + "</div></li></ul><br>";
					}
				}
			}

		} catch (Exception e) {
		}
		if (rep.equals("")) {
			rep = "<font color='gray'>" + locale.getString("NO_NEW_VERSION")
					+ "</font>";
		} else {
			rep = "<font color='green'>" + locale.getString("NEW_VERSION")
					+ " : </font><br>" + rep;
		}
		return "<br><br><i>" + rep + "</i>";
	}

	private String Language() {
		String rep = "";
		if (!locale.getLocale().getLanguage()
				.equals(Locale.getDefault().getLanguage())) {
			rep = "<br><br><p align=center><font color='gray'><i>Your language isn'thread suported. It's easy to translate this software into your language :<br> See "
					+ ApplicationProperty.getString("Application.homepage")
					+ "</p>";
		}
		return rep + "</html>";
	}

	private void initComponents() {
		jPanelRenommeur = new JPanel();
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu();
		jmOpen = new JMenuItem();
		jcbHide = new JCheckBoxMenuItem();
		jcbNum = new JCheckBoxMenuItem();
		jmPrev = new JMenuItem();
		jmNext = new JMenuItem();
		jmValide = new JMenuItem();
		jmAjouter = new JMenuItem();
		jmHelp = new JMenuItem();
		jmDel = new JMenuItem();
		jmExeSave = new JMenuItem();
		exitMenuItem = new JMenuItem();
		JMenu helpMenu = new JMenu();
		aboutMenuItem = new JMenuItem();
		statusPanel = new JPanel();
		JSeparator statusPanelSeparator = new JSeparator();
		statusMessageLabel = new JLabel();

		fileMenu.setText(locale.getString("FICHIER"));

		ActionMap actionMap = Application.getInstance(Renommeur.class)
				.getContext().getActionMap(AppFrame.class, this);
		jmOpen.setAction(actionMap.get("actionOpenDir"));
		jmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		jmOpen.setText(locale.getString("OUVRIR UN RÉPERTOIRE"));
		fileMenu.add(jmOpen);

		fileMenu.add(new JSeparator());

		// 不显示状态为已删除的照片
		jcbHide.setAction(actionMap.get("actionHideDelPhoto"));
		jcbHide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				InputEvent.CTRL_MASK));
		jcbHide.setText(locale
				.getString("NE PAS AFFICHER LES PHOTOS MARQUÉES SUPRIMÉES'"));
		//
		jcbHide.setSelected(false);
		fileMenu.add(jcbHide);
		jcbHide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionHideDelPhoto();
			}
		});

		fileMenu.add(new JSeparator());

		jmPrev.setAction(actionMap.get("actionPrev"));
		jmPrev.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
		jmPrev.setText(locale.getString("VALIDE PUIS PHOTO PRÉCÉDENTE"));
		fileMenu.add(jmPrev);

		jmNext.setAction(actionMap.get("actionNext"));
		jmNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
		jmNext.setText(locale.getString("VALIDE PUIS PHOTO SUIVANTE"));
		fileMenu.add(jmNext);

		fileMenu.add(new JSeparator());

		// 为选定的文本创建按钮
		jmAjouter.setAction(actionMap.get("actionCreateButton"));
		jmAjouter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		jmAjouter.setText(locale
				.getString("CRÉE UN BOUTON À PARTIR DU TEXTE SÉLECTIONNÉ"));
		fileMenu.add(jmAjouter);

		jmDel.setAction(actionMap.get("actionDel"));
		jmDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		jmDel.setText(locale
				.getString("MARQUE LA PHOTO SUPPRIMÉE/NON SUPPRIMÉE"));
		fileMenu.add(jmDel);

		fileMenu.add(new JSeparator());

		// 序列化照片文件名
		jcbNum.setAction(actionMap.get("actionNum"));
		jcbNum.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		jcbNum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedTab() == RENOMMEUR && jcbNum.isEnabled()) {
					doChangeTextAndImage(true, false);
				}
				switch2BrowsePane();
			}
		});
		jcbNum.setText(locale.getString("NUMÉROTER LE NOM DES PHOTOS"));
		fileMenu.add(jcbNum);

		jmExeSave.setAction(actionMap.get("ExeSave"));
		jmExeSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
				InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
		jmExeSave.setText(locale
				.getString("APPLIQUE LES MODIFICATIONS SUR LES FICHIERS"));
		fileMenu.add(jmExeSave);

		fileMenu.add(new JSeparator());

		exitMenuItem.setAction(actionMap.get("quit"));
		exitMenuItem.setText(locale.getString("QUITTER"));
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		helpMenu.setText(locale.getString("MORE"));

		jmHelp.setAction(actionMap.get("showHelp"));
		jmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		jmHelp.setText(locale.getString("AIDE"));
		helpMenu.add(jmHelp);

		aboutMenuItem.setAction(actionMap.get("showAbout"));
		aboutMenuItem.setText(locale.getString("À PROPOS DE..."));
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		statusPanelSeparator.setName("statusPanelSeparator");
		statusMessageLabel.setName("statusMessageLabel");

		tabbedPane = new JTabbedPane();
		this.setComponent(tabbedPane);
		tabbedPane.add(locale.getString("RENOMMER"), jPanelRenommeur);

		setMenuBar(menuBar);
		setStatusBar(statusPanel);

		jPanelRenommeur.setLayout(new BorderLayout());
		jPanelRenommeur.setLayout(new BorderLayout());
		jbprev = new JButton();
		jbnext = new JButton();
		jbprev.setText("<<");
		jbnext.setText(">>");
		jbnext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				actionNext();
			}
		});

		jbprev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				actionPrev();
			}
		});

		jlname = new JLabel("");
		jlname.setHorizontalAlignment(JLabel.CENTER);
		jlname.setVerticalAlignment(JLabel.CENTER);
		jPanelDesButtons = new JPanel();
		jPanelDesButtons.setLayout(new FlowLayout());
		JPanel basbas = new JPanel();
		basbas.setLayout(new GridLayout(1, 2));
		JPanel basbasgauche = new JPanel();

		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
		statusPanel.add(statusMessageLabel, FlowLayout.LEFT);
		statusPanel.add(Box.createHorizontalGlue());
		cache = new JProgressBar();
		travail = new JProgressBar();
		cache.setMaximumSize(new Dimension(148, 20));
		travail.setMaximumSize(new Dimension(148, 20));
		statusPanel.add(cache);
		statusPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		statusPanel.add(travail);

		jlphoto = new JLabel();
		jlphoto.setHorizontalAlignment(JLabel.CENTER);
		jlphoto.setVerticalAlignment(JLabel.CENTER);
		jPanelPhoto = new JPanel(new BorderLayout());
		exif = new JLabel("", JLabel.CENTER);
		jPanelPhoto.add("Center", jlphoto);
		jPanelPhoto.add("South", exif);
		jPanelNom = new JPanel();
		jPanelNom.setLayout(new BorderLayout());

		jtf = new JTextField("");
		jtf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON3) {
					actionCreateButton();
				}
			}

		});
		jtf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					actionNext();
				}
			}
		});

		basbasgauche.setLayout(new FlowLayout(FlowLayout.RIGHT));
		basbas.add(basbasgauche);
		basbasgauche.add(jlname);
		basbas.add(jtf);

		jPanelNom.add("South", basbas);
		jPanelNom.add("North", jPanelDesButtons);

		jPanelRenommeur.add("West", jbprev);
		jPanelRenommeur.add("East", jbnext);
		jPanelRenommeur.add("Center", jPanelPhoto);
		jPanelRenommeur.add("South", jPanelNom);
		jPanelRenommeur.repaint();
		isLoading = false;

		jlphoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (photos != null) {
					if (evt.getButton() == MouseEvent.BUTTON3) {
						photos.delete();
					}
					if (evt.getButton() == MouseEvent.BUTTON2) {
						setActiveTab(VISIONNEUR);
					}
				}
			}
		});
		jlphoto.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0) {
					actionNext();
				} else if (e.getWheelRotation() < 0) {
					actionPrev();
				}
			}
		});

		jlphoto.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				doChangeTextAndImage(false, true);
				if (thread != null) {
					thread.isChangementTaille();
				}
			}
		});
		jPanelRenommeur.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				updateStatusMessage(SaveMessage);
				doActiveNextPrev(true);
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				super.componentHidden(e);
				doActiveNextPrev(false);
				if (!isValideInput()) {
					setActiveTab(RENOMMEUR);
				}
			}
		});
		browsePane = new BrowsePane(this);
		tabbedPane.add(locale.getString("GÉRER TOUTES LES IMAGES"), browsePane);
	}

	private JFrame instance;
	private JCheckBoxMenuItem jcbHide, jcbNum;
	private JMenuItem jmAjouter, jmValide, jmExeSave, jmDel, jmOpen, jmPrev,
			jmNext, exitMenuItem, aboutMenuItem, jmHelp;
	private JPanel jPanelRenommeur, statusPanel, jPanelNom, jPanelDesButtons,
			jPanelPhoto;
	private JMenuBar menuBar;
	private JLabel statusMessageLabel, jlphoto, jlname, exif;
	private JButton jbnext, jbprev;
	private JTextField jtf;
	private JProgressBar cache, travail;
	private JTabbedPane tabbedPane;

	private VecPhotos photos;
	private BufferThread thread;
	private BrowsePane browsePane;
	private String SaveMessage;
	private boolean isLoading;

	private final String DefaultMessage = locale
			.getString("APPUYEZ SUR F1 POUR AVOIR DE L'AIDE.");

	private final String NameSaveGuard = "Renommeur.sauv";

	// 标签页
	public final int RENOMMEUR = 1, VISIONNEUR = 2;

	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");
	ResourceBundle ApplicationProperty = ResourceBundle
			.getBundle("Renommeur/resources/Renommeur");
}