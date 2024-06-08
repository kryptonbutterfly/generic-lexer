package kryptonbutterfly.lexer;

import java.io.File;

public record Location(int line, int col, String file) implements Comparable<Location>
{
	
	public static final Location INTERNAL = new Location(-1, -1, "~INTERNAL~");
	
	@Override
	public String toString()
	{
		return file + "(" + line + ":" + col + ")";
	}
	
	public String errorPos()
	{
		if (file == null)
			return "at (%d:%d)".formatted(line, col);
		
		final var fileName = new File(file).toString();
		return "at (%s:%d:%d)".formatted(fileName, line, col);
	}
	
	public static Location calcEnd(
		Location start,
		String value,
		String lineSeparator,
		String file,
		int tabSize,
		int colStartIndex)
	{
		final var	split		= value.split(lineSeparator, -1);
		final var	lines		= split.length - 1;
		final var	lastLine	= split[lines];
		
		final int	tabless		= lastLine.replace("\t", "").length();
		final int	tabCount	= lastLine.length() - tabless;
		
		final int cols = tabless + tabCount * tabSize;
		
		if (lines == 0)
			return new Location(start.line, start.col + cols, file);
		return new Location(start.line + lines, cols + colStartIndex, file);
	}
	
	public Location addCol(int amount)
	{
		return new Location(line, col + amount, file);
	}
	
	@Override
	public int compareTo(Location o)
	{
		final int lineDiff = o.line - line;
		if (lineDiff == 0)
			return Integer.signum(o.col - col);
		return Integer.signum(lineDiff);
	}
}