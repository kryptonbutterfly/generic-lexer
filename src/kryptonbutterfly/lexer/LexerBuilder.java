package kryptonbutterfly.lexer;

import static kryptonbutterfly.math.utils.range.Range.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public final class LexerBuilder<EnumKeywords extends Enum<EnumKeywords> & Keywords, EnumOperators extends Enum<EnumOperators> & IOperators>
{
	private final EnumKeywords[]	keywords;
	private final EnumOperators[]	operators;
	private final UnspecMatcher[]	unspec;
	// private final HashSet<Matcher> matcher = new HashSet<>();
	private final LinkedHashSet<Matcher> matcher = new LinkedHashSet<>();
	
	public LexerBuilder(Class<EnumKeywords> keywords, Class<EnumOperators> operators, UnspecMatcher... unspec)
	{
		this.keywords	= toKeywordsArray(keywords);
		this.operators	= toOperatorsArray(operators);
		this.unspec		= unspec;
	}
	
	@SuppressWarnings("unchecked")
	private static <EnumKeywords extends Enum<EnumKeywords> & Keywords> EnumKeywords[] toKeywordsArray(
		Class<EnumKeywords> keywords)
	{
		if (keywords == null)
			return (EnumKeywords[]) new Object[0];
		return keywords.getEnumConstants();
	}
	
	@SuppressWarnings("unchecked")
	private static <EnumOperators extends Enum<EnumOperators> & IOperators> EnumOperators[] toOperatorsArray(
		Class<EnumOperators> operators)
	{
		if (operators == null)
			return (EnumOperators[]) new Object[0];
		return operators.getEnumConstants();
	}
	
	public LexerBuilder<EnumKeywords, EnumOperators> addMatcher(Matcher... matcher)
	{
		for (var m : matcher)
			this.matcher.add(m);
		return this;
	}
	
	public void validate()
	{
		validateOperators();
		validateKeywords();
	}
	
	private void validateOperators()
	{
		if (operators.length < 2)
			return;
		for (final int x : range(operators.length - 1))
			for (final int y : range(x + 1, operators.length))
				if (operators[x].op().equals(operators[y].op()))
					throw new DuplicateOperatorException(operators[x], operators[y]);
	}
	
	private void validateKeywords()
	{
		if (keywords.length < 2)
			return;
		for (final int x : range(keywords.length - 1))
			for (final int y : range(x + 1, keywords.length))
				if (keywords[x].keyword().equals(keywords[y].keyword()))
					throw new DuplicateKeywordException(keywords[x], keywords[y]);
				
	}
	
	private void sortByLength()
	{
		Arrays.sort(operators, (first, second) -> Integer.compare(second.op().length(), first.op().length()));
		Arrays.sort(keywords, (first, second) -> Integer.compare(second.keyword().length(), first.keyword().length()));
	}
	
	private final Matcher[] createMatcher()
	{
		final var list = new ArrayList<Matcher>();
		for (final var m : matcher)
			list.add(m);
		
		for (final var op : operators)
			list.add(new Matcher(Operator.start(op), Operator.end(op), Operator.create(op)));
		
		return list.toArray(Matcher[]::new);
	}
	
	private final UnspecMatcher[] createUnspecMatcher()
	{
		final var list = new ArrayList<UnspecMatcher>();
		for (final var key : keywords)
			list.add(new UnspecMatcher(key.keyword()::equals, (_val, loc) -> new Keyword(key, loc)));
		
		for (final var matcher : unspec)
			list.add(matcher);
		return list.toArray(UnspecMatcher[]::new);
	}
	
	public Lexer build()
	{
		this.sortByLength();
		return new Lexer(createUnspecMatcher(), createMatcher());
	}
}