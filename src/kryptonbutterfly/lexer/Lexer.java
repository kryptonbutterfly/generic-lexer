package kryptonbutterfly.lexer;

import static kryptonbutterfly.math.utils.range.Range.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import kryptonbutterfly.monads.opt.Opt;

public final class Lexer
{
	private final UnspecMatcher[]	unspecMatcher;
	private final Matcher[]			matcher;
	private final IsStartOf[]		STARTER;
	
	Lexer(UnspecMatcher[] unspecMatcher, Matcher[] matcher)
	{
		this.unspecMatcher	= unspecMatcher;
		this.matcher		= matcher;
		this.STARTER		= Arrays.stream(matcher)
			.map(Matcher::startOf)
			.toArray(IsStartOf[]::new);
	}
	
	public LexedFile lexFile(String text, String fileName, int tabWidth, int columnStartIndex)
	{
		return new LexedFile(
			fileName,
			new LexerRun(text, fileName, tabWidth, columnStartIndex)
				.lex());
	}
	
	public ArrayList<Section<?>> lex(String text, int tabWidth, int columnStartIndex)
	{
		return new LexerRun(text, null, tabWidth, columnStartIndex).lex();
	}
	
	private final class LexerRun
	{
		private final String	fileName;
		private final int		tabWidth;
		private final int		columnStartIndex;
		
		private int			offset	= 0;
		private Location	location;
		
		private final String text;
		
		private LexerRun(String text, String fileName, int tabWidth, int columnStartIndex)
		{
			this.text				= text;
			this.fileName			= fileName;
			this.location			= new Location(1, columnStartIndex, fileName);
			this.tabWidth			= tabWidth;
			this.columnStartIndex	= columnStartIndex;
		}
		
		private ArrayList<Section<?>> lex()
		{
			final var tokens = new ArrayList<Section<?>>();
			
			while (offset < text.length())
				findStart().if_(next -> {
					final int start = next.start();
					if (next.start() > offset)
					{
						final var value = text.substring(offset, start);
						buildFromUnspecified(value, location, tokens);
						location = Location.calcEnd(location, value, "\n", fileName, tabWidth, columnStartIndex);
					}
					tokens.add(next.match().apply(location));
					offset		= next.end();
					location	= Location
						.calcEnd(location, text.substring(start, offset), "\n", fileName, tabWidth, columnStartIndex);
				}).else_(() -> {
					buildFromUnspecified(text.substring(offset), location, tokens);
					offset = text.length();
				});
			
			return tokens;
		}
		
		private boolean buildFromUnspecified(String value, Location loc, List<Section<?>> tokens)
		{
			for (final var matcher : range(unspecMatcher).element())
				if (matcher.testValue(value))
					return tokens.add(matcher.create(value, loc));
			return tokens.add(new Unspecified(value, loc));
		}
		
		private Opt<Occurence> findStart()
		{
			return StringUtils.indexOfFirst(text, offset, STARTER)
				.flatmap(find ->
				{
					final var matcher = Lexer.this.matcher[find.index()];
					final int end	= matcher.end(new EndData(find.min(), text, location));
					if (end == -1)
						return Opt.empty();
					final var value = text.substring(find.min(), end);
					return Opt.of(new Occurence(find.min(), end, matcher.creator().create(value)));
				});
		}
		
		private record Occurence(int start, int end, Function<Location, ? extends Section<?>> match)
		{}
	}
}