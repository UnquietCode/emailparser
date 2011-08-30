/**
 * @Package: zip
 * @Title: TarList.java
 * @Author: zhangzuoqiang
 * @Time: 4:35:07 PM Aug 29, 2011
 * @Version: 
 */
package zip;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @Description: Demonstrate the Tar archive lister.
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class TarList {

	public static void main(String[] argv) throws IOException, TarException {
//		new TarList("E:\\Dev-Referred\\poi-bin-3.7-20101029.tar.gz").list();
		new TarList("E:\\control.zip").list();
	}

	/** The TarFile we are reading */
	TarFile tf;

	/** Constructor */
	public TarList(String fileName) {
		tf = new TarFile(fileName);
	}

	/** Generate and print the listing */
	public void list() throws IOException, TarException {
		Enumeration<TarEntry> list = tf.entries();
		while (list.hasMoreElements()) {
			TarEntry e = (TarEntry) list.nextElement();
			System.out.println(toListFormat(e));
		}
	}

	protected StringBuffer sb;

	/** Shift used in formatting permissions */
	protected static int[] shft = { 6, 3, 0 };

	/** Format strings used in permissions */
	protected static String rwx[] = { "---", "--x", "-w-", "-wx", "r--", "r-x",
			"rw-", "rwx" };

	/** NumberFormat used in formatting List form string */
	NumberFormat sizeForm = new DecimalFormat("00000000");

	/** Date used in printing mtime */
	Date date = new Date();

	SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/** Format a TarEntry the same way that UNIX tar does */
	public String toListFormat(TarEntry e) {
		sb = new StringBuffer();
		switch (e.type) {
		case TarEntry.LF_OLDNORMAL:
		case TarEntry.LF_NORMAL:
		case TarEntry.LF_CONTIG:
		case TarEntry.LF_LINK: // hard link: same as file
			sb.append('-'); // 'f' would be sensible
			break;
		case TarEntry.LF_DIR:
			sb.append('d');
			break;
		case TarEntry.LF_SYMLINK:
			sb.append('l');
			break;
		case TarEntry.LF_CHR: // UNIX character device file
			sb.append('c');
			break;
		case TarEntry.LF_BLK: // UNIX block device file
			sb.append('b');
			break;
		case TarEntry.LF_FIFO: // UNIX named pipe
			sb.append('p');
			break;
		default: // Can't happen?
			sb.append('?');
			break;
		}

		// Convert e.g., 754 to rwxrw-r--
		int mode = e.getMode();
		for (int i = 0; i < 3; i++) {
			sb.append(rwx[mode >> shft[i] & 007]);
		}
		sb.append(' ');

		// owner and group
		sb.append(e.getUname()).append('/').append(e.getGname()).append(' ');

		// size
		// DecimalFormat can't do "%-9d", so we do part of it ourselves
		sb.append(' ');
		String t = sizeForm.format(e.getSize());
		boolean digit = false;
		char c;
		for (int i = 0; i < 8; i++) {
			c = t.charAt(i);
			if (!digit && i < (8 - 1) && c == '0')
				sb.append(' '); // leading space
			else {
				digit = true;
				sb.append(c);
			}
		}
		sb.append(' ');

		// mtime
		// copy file's mtime into Data object (after scaling
		// from "sec since 1970" to "msec since 1970"), and format it.
		date.setTime(1000 * e.getTime());
		sb.append(dateForm.format(date)).append(' ');

		sb.append(e.getName());
		if (e.isLink())
			sb.append(" link to ").append(e.getLinkName());
		if (e.isSymLink())
			sb.append(" -> ").append(e.getLinkName());

		return sb.toString();
	}
}

/**
 * One entry in an archive file.
 * 
 * @author Ian Darwin
 * @version $Id: TarEntry.java,v 1.7 2004/03/06 21:16:19 ian Exp $
 * @note Tar format info taken from John Gilmore's public domain tar program,
 * @(#)tar.h 1.21 87/05/01 Public Domain, which said: "Created 25 August 1985 by
 *           John Gilmore, ihnp4!hoptoad!gnu." John is now gnu@toad.com, and by
 *           another path tar.h is GPL'd in GNU Tar.
 */

class TarEntry {
	/** Where in the tar archive this entry's HEADER is found. */
	public long fileOffset = 0;

	/** The maximum size of a name */
	public static final int NAMSIZ = 100;

	public static final int TUNMLEN = 32;

	public static final int TGNMLEN = 32;

	// Next fourteen fields constitute one physical record.
	// Padded to TarFile.RECORDSIZE bytes on tape/disk.
	// Lazy Evaluation: just read fields in raw form, only format when asked.

	/** File name */
	byte[] name = new byte[NAMSIZ];

	/** permissions, e.g., rwxr-xr-x? */
	byte[] mode = new byte[8];

	/* user */
	byte[] uid = new byte[8];

	/* group */
	byte[] gid = new byte[8];

	/* size */
	byte[] size = new byte[12];

	/* UNIX modification time */
	byte[] mtime = new byte[12];

	/* checksum field */
	byte[] chksum = new byte[8];

	byte type;

	byte[] linkName = new byte[NAMSIZ];

	byte[] magic = new byte[8];

	byte[] uname = new byte[TUNMLEN];

	byte[] gname = new byte[TGNMLEN];

	byte[] devmajor = new byte[8];

	byte[] devminor = new byte[8];

	// End of the physical data fields.

	/* The magic field is filled with this if uname and gname are valid. */
	public static final byte TMAGIC[] = {
			// 'u', 's', 't', 'a', 'r', ' ', ' ', '\0'
			0, 0, 0, 0, 0, 0, 0x20, 0x20, 0 }; /* 7 chars and a null */

	/* Type value for Normal file, Unix compatibility */
	public static final int LF_OLDNORMAL = '\0';

	/* Type value for Normal file */
	public static final int LF_NORMAL = '0';

	/* Type value for Link to previously dumped file */
	public static final int LF_LINK = '1';

	/* Type value for Symbolic link */
	public static final int LF_SYMLINK = '2';

	/* Type value for Character special file */
	public static final int LF_CHR = '3';

	/* Type value for Block special file */
	public static final int LF_BLK = '4';

	/* Type value for Directory */
	public static final int LF_DIR = '5';

	/* Type value for FIFO special file */
	public static final int LF_FIFO = '6';

	/* Type value for Contiguous file */
	public static final int LF_CONTIG = '7';

	/* Constructor that reads the entry's header. */
	public TarEntry(RandomAccessFile is) throws IOException, TarException {

		fileOffset = is.getFilePointer();

		// read() returns -1 at EOF
		if (is.read(name) < 0)
			throw new EOFException();
		// Tar pads to block boundary with nulls.
		if (name[0] == '\0')
			throw new EOFException();
		// OK, read remaining fields.
		is.read(mode);
		is.read(uid);
		is.read(gid);
		is.read(size);
		is.read(mtime);
		is.read(chksum);
		type = is.readByte();
		is.read(linkName);
		is.read(magic);
		is.read(uname);
		is.read(gname);
		is.read(devmajor);
		is.read(devminor);

		// Since the tar header is < 512, we need to skip it.
		is.skipBytes((int) (TarFile.RECORDSIZE - (is.getFilePointer() % TarFile.RECORDSIZE)));

		// TODO if checksum() fails,
		// throw new TarException("Failed to find next header");

	}

	/** Returns the name of the file this entry represents. */
	public String getName() {
		return new String(name).trim();
	}

	public String getTypeName() {
		switch (type) {
		case LF_OLDNORMAL:
		case LF_NORMAL:
			return "file";
		case LF_LINK:
			return "link w/in archive";
		case LF_SYMLINK:
			return "symlink";
		case LF_CHR:
		case LF_BLK:
		case LF_FIFO:
			return "special file";
		case LF_DIR:
			return "directory";
		case LF_CONTIG:
			return "contig";
		default:
			throw new IllegalStateException("TarEntry.getTypeName: type "
					+ type + " invalid");
		}
	}

	/** Returns the UNIX-specific "mode" (type+permissions) of the entry */
	public int getMode() {
		try {
			return Integer.parseInt(new String(mode).trim(), 8) & 0777;
		} catch (IllegalArgumentException e) {
			return 0;
		}
	}

	/** Returns the size of the entry */
	public int getSize() {
		try {
			return Integer.parseInt(new String(size).trim(), 8);
		} catch (IllegalArgumentException e) {
			return 0;
		}
	}

	/**
	 * Returns the name of the file this entry is a link to, or null if this
	 * entry is not a link.
	 */
	public String getLinkName() {
		// if (isLink())
		// return null;
		return new String(linkName).trim();
	}

	/** Returns the modification time of the entry */
	public long getTime() {
		try {
			return Long.parseLong(new String(mtime).trim(), 8);
		} catch (IllegalArgumentException e) {
			return 0;
		}
	}

	/** Returns the string name of the userid */
	public String getUname() {
		return new String(uname).trim();
	}

	/** Returns the string name of the group id */
	public String getGname() {
		return new String(gname).trim();
	}

	/** Returns the numeric userid of the entry */
	public int getuid() {
		try {
			return Integer.parseInt(new String(uid).trim());
		} catch (IllegalArgumentException e) {
			return -1;
		}
	}

	/** Returns the numeric gid of the entry */
	public int getgid() {
		try {
			return Integer.parseInt(new String(gid).trim());
		} catch (IllegalArgumentException e) {
			return -1;
		}
	}

	/** Returns true if this entry represents a file */
	boolean isFile() {
		return type == LF_NORMAL || type == LF_OLDNORMAL;
	}

	/** Returns true if this entry represents a directory */
	boolean isDirectory() {
		return type == LF_DIR;
	}

	/** Returns true if this a hard link (to a file in the archive) */
	boolean isLink() {
		return type == LF_LINK;
	}

	/** Returns true if this a symbolic link */
	boolean isSymLink() {
		return type == LF_SYMLINK;
	}

	/** Returns true if this entry represents some type of UNIX special file */
	boolean isSpecial() {
		return type == LF_CHR || type == LF_BLK || type == LF_FIFO;
	}

	public String toString() {
		return "TarEntry[" + getName() + ']';
	}
}

/*
 * Exception for TarFile and TarEntry. $Id: TarException.java,v 1.3 1999/10/06
 * 15:13:53 ian Exp $
 */

class TarException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TarException() {
		super();
	}

	public TarException(String msg) {
		super(msg);
	}
}

/**
 * Tape Archive Lister, patterned loosely after java.util.ZipFile. Since, unlike
 * Zip files, there is no central directory, you have to read the entire file
 * either to be sure of having a particular file's entry, or to know how many
 * entries there are in the archive.
 * 
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id: TarFile.java,v 1.12 2004/03/07 17:53:15 ian Exp $
 */

class TarFile {
	/** True after we've done the expensive read. */
	protected boolean read = false;

	/** The list of entries found in the archive */
	protected Vector<TarEntry> list;

	/** Size of header block. */
	public static final int RECORDSIZE = 512;

	/* Size of each block, in records */
	protected int blocking;

	/* Size of each block, in bytes */
	protected int blocksize;

	/** File containing archive */
	protected String fileName;

	/** Construct (open) a Tar file by name */
	public TarFile(String name) {
		fileName = name;
		list = new Vector<TarEntry>();
	}

	/** Construct (open) a Tar file by File */
	public TarFile(java.io.File name) throws IOException {
		this(name.getCanonicalPath());
	}

	/** The main datastream. */
	protected RandomAccessFile is;

	/**
	 * Read the Tar archive in its entirety. This is semi-lazy evaluation, in
	 * that we don't read the file until we need to. A future revision may use
	 * even lazier evaluation: in getEntry, scan the list and, if not found,
	 * continue reading! For now, just read the whole file.
	 */
	protected void readFile() throws IOException, TarException {
		is = new RandomAccessFile(fileName, "r");
		TarEntry hdr;
		try {
			do {
				hdr = new TarEntry(is);
				if (hdr.getSize() < 0) {
					System.out.println("Size < 0");
					break;
				}
				// System.out.println(hdr.toString());
				list.addElement(hdr);
				// Get the size of the entry
				int nbytes = hdr.getSize(), diff;
				// Round it up to blocksize.
				if ((diff = (nbytes % RECORDSIZE)) != 0) {
					nbytes += RECORDSIZE - diff;
				}
				// And skip over the data portion.
				// System.out.println("Skipping " + nbytes + " bytes");
				is.skipBytes(nbytes);
			} while (true);
		} catch (EOFException e) {
			// OK, just stop reading.
		}
		// All done, say we've read the contents.
		read = true;
	}

	/* Close the Tar file. */
	public void close() {
		try {
			is.close();
		} catch (IOException e) {
			// nothing to do
		}
	}

	/* Returns an enumeration of the Tar file entries. */
	public Enumeration<TarEntry> entries() throws IOException, TarException {
		if (!read) {
			readFile();
		}
		return list.elements();
	}

	/** Returns the Tar entry for the specified name, or null if not found. */
	public TarEntry getEntry(String name) {
		for (int i = 0; i < list.size(); i++) {
			TarEntry e = (TarEntry) list.elementAt(i);
			if (name.equals(e.getName()))
				return e;
		}
		return null;
	}

	/**
	 * Returns an InputStream for reading the contents of the specified entry
	 * from the archive. May cause the entire file to be read.
	 */
	public InputStream getInputStream(TarEntry entry) {
		return null;
	}

	/** Returns the path name of the Tar file. */
	public String getName() {
		return fileName;
	}

	/**
	 * Returns the number of entries in the Tar archive. May cause the entire
	 * file to be read. XXX Obviously not written yet, sorry.
	 */
	public int size() {
		return 0;
	}
}