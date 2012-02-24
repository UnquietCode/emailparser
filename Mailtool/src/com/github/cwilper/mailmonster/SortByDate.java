package com.github.cwilper.mailmonster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.cwilper.ttff.Source;
import com.github.cwilper.ttff.Sources;

public class SortByDate {

	private static void die(String message, boolean showUsage) {
		System.err.println("Error: " + message);
		if (showUsage) {
			System.err.println("Usage: SortByDate MBOX_FILE OUTPUT_DIR");
			System.err.println("   Or: SortByDate MBOX_DIR OUTPUT_DIR");
		}
		System.exit(1);
	}

	public static void main(String[] args) {
		try {
			// if (args.length != 2) {
			// die("Wrong number of arguments", true);
			// }
			
//			String inputFi
			
			File input = new File(args[0]);
			File output = new File(args[1]);
			if (!input.exists()) {
				die("No such file or directory: " + input, false);
			}
			if (!output.exists() && !output.mkdirs()) {
				die("Unable to create output directory: " + output, false);
			}
			Source<Message> source = null;
			if (input.isDirectory()) {
				source = new MBoxDirMessageSource(input);
			} else {
				source = new MBoxStreamMessageSource(new FileInputStream(input));
			}
			long count = Sources.drain(source, new DateSortingMessageSink(
					output));
			System.out.println("Sorted " + count + " messages.");
		} catch (IOException e) {
			e.printStackTrace();
			die(e.getMessage() + " (see stack trace above)", false);
		}
	}

}
