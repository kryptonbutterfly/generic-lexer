package kryptonbutterfly.lexer;

import static kryptonbutterfly.math.utils.range.Range.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntPredicate;

import kryptonbutterfly.monads.opt.Opt;

public final class StringUtils
{
	private StringUtils()
	{}
	
	// private static final HashMap<Character, String> escapeMap = new HashMap<>();
	private static final HashMap<String, Character> unescapeMap = new HashMap<>();
	
	private static final HashMap<Character, String> visControll = new HashMap<>();
	
	private static void regEscape(Character c, String esc)
	{
		// escapeMap.put(c, esc);
		unescapeMap.put(esc, c);
	}
	
	private static void visCon(char key, String value)
	{
		visControll.put(key, value);
	}
	
	static
	{
		regEscape('\b', "\\b");
		regEscape('\t', "\\t");
		regEscape('\n', "\\n");
		regEscape('\f', "\\f");
		regEscape('\r', "\\r");
		regEscape('"', "\\\"");
		regEscape('\'', "\\'");
		regEscape('\\', "\\\\");
		
		visCon(' ', "␣");
		visCon('\t', "»   ");
	}
	
	public static char unescape(String s)
	{
		return unescapeMap.getOrDefault(s, s.charAt(0));
	}
	
	public static String unescapeString(String s)
	{
		final var sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c != '\\')
				sb.append(c);
			else
			{
				String escaped = s.substring(i, i + 2);
				sb.append(unescape(escaped));
				i++;
			}
		}
		return sb.toString();
	}
	
	public static String toVisibleControll(char c)
	{
		return visControll.getOrDefault(c, Character.toString(c));
	}
	
	/**
	 * @param target
	 *            the string to search in
	 * @param offset
	 *            where to start in the string
	 * @param isMatch
	 * @return the position in the target string of the first match and it's
	 *         corresponding index in isMatch
	 */
	public static Opt<FindResult> indexOfFirst(String target, int offset, IsStartOf[] isMatch)
	{
		return indexOfFirst(
			target.length(),
			offset,
			Arrays.stream(isMatch)
				.map(m -> m.aptFirst(target))
				.map(m -> m.aptLast(offset))
				.toArray(IntPredicate[]::new));
	}
	
	private static Opt<FindResult> indexOfFirst(int size, int offset, IntPredicate[] isMatch)
	{
		for (int min : range(offset, size))
			for (final var ie : range(isMatch))
				if (ie.element().test(min))
					return Opt.of(new FindResult(min, ie.index()));
		return Opt.empty();
	}
	
	public static record FindResult(int min, int index)
	{
		@Override
		public String toString()
		{
			return "FindResult [min=" + min + ", index=" + index + "]";
		}
	}
}