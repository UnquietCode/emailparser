import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eams.msgparser.Message;
import com.eams.msgparser.MsgParser;
import com.eams.msgparser.attachment.Attachment;
import com.eams.msgparser.attachment.FileAttachment;
import com.eams.msgparser.attachment.MsgAttachment;

public class ParserTest {

	public static void main(String[] args) {
		try {
			MsgParser msgp = new MsgParser();

			Handler[] handlers = Logger.getLogger("").getHandlers();
			for (int index = 0; index < handlers.length; index++) {
				handlers[index].setLevel(Level.INFO);
			}
			Logger logger = Logger.getLogger(MsgParser.class.getName());
			logger.setLevel(Level.INFO);

			Message msg = null;

			File testDir = new File("C:\\");
			File[] testFiles = testDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".msg");
				}
			});

			for (File testFile : testFiles) {
				msg = msgp.parseMsg(testFile);
				parseDetail(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void parseDetail(Message message) {
		System.out.println("---------------------------------------");
		System.out.println("Parsed message:\n" + message.toString());
		for (Attachment att : message.getAttachments()) {
			if (att instanceof FileAttachment) {
				String output = "D:\\att\\"
						+ ((FileAttachment) att).getFilename();
				bytes2File(((FileAttachment) att).getData(), output);
			} else if (att instanceof MsgAttachment) {
				// 附件为MSG文件
				System.out.println("-------");
				parseDetail(((MsgAttachment) att).getMessage());
			}
		}
	}

	public static File bytes2File(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
