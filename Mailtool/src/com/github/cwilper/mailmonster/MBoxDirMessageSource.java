package com.github.cwilper.mailmonster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.cwilper.ttff.AbstractSource;
import com.github.cwilper.ttff.Source;

public class MBoxDirMessageSource extends AbstractSource<Message> {

	private final Iterator<File> files;

	private Source<Message> currentSource;

	public MBoxDirMessageSource(File dir) throws IOException {
		List<File> list = new ArrayList<File>();
		addFiles(dir, list);
		files = list.iterator();
		currentSource = getNextSource();
	}

	@Override
	protected Message computeNext() throws IOException {
		while (currentSource != null) {
			if (currentSource.hasNext()) {
				return currentSource.next();
			}
			currentSource.close();
			currentSource = getNextSource();
		}
		return endOfData();
	}

	private Source<Message> getNextSource() throws IOException {
		while (files.hasNext()) {
			return new MBoxStreamMessageSource(
					new FileInputStream(files.next()));
		}
		return null;
	}

	private static void addFiles(File dir, List<File> list) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				addFiles(file, list);
			} else {
				list.add(file);
			}
		}
	}

}
