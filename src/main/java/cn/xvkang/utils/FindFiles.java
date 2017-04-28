package cn.xvkang.utils;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FindFiles {

	public static class Finder extends SimpleFileVisitor<Path> {

		private final PathMatcher matcher;
		// private int numMatches = 0;
		private List<String> result;

		Finder(String pattern) {
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}

		// Compares the glob pattern against
		// the file or directory name.
		void find(Path file) {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				if (result == null)
					result = new ArrayList<String>();
				result.add(file.toString());
				// numMatches++;
				// System.out.println(file);
			}
		}

		// Prints the total number of
		// matches to standard out.
		List<String> done() {
			// System.out.println("Matched: " + numMatches);
			return result;
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			find(file);
			return CONTINUE;
		}

		// Invoke the pattern matching
		// method on each directory.
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			find(dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return CONTINUE;
		}
	}

	static void usage() {
		System.err.println("java Find <path>" + " -name \"<glob_pattern>\"");
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		/*
		 * if (args.length < 3 || !args[1].equals("-name")) usage();
		 * 
		 * Path startingDir = Paths.get(args[0]); String pattern = args[2];
		 * 
		 * Finder finder = new Finder(pattern); Files.walkFileTree(startingDir,
		 * finder); finder.done();
		 */
		List<String> filePaths = findFilesBySuffix("/Users/wu/测试数据", "*.ZIP");
		for (String tmp : filePaths) {
			System.out.println(tmp);
		}
		System.out.println(filePaths.size());

	}

	public static List<String> findFilesBySuffix(String startingDir, String pattern) {
		List<String> result = new ArrayList<String>();
		Finder finder = new Finder(pattern);
		try {
			Files.walkFileTree(Paths.get(startingDir), finder);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = finder.done();
		return result;
	}
}