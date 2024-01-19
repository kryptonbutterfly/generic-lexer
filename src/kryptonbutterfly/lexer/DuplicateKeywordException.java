package kryptonbutterfly.lexer;

final class DuplicateKeywordException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	DuplicateKeywordException(String msg, Object... args)
	{
		super(msg.formatted(args));
	}
	
	DuplicateKeywordException(Keywords first, Keywords second)
	{
		this("Keyword '%s' is defined in %s and %s", first.keyword(), first, second);
	}
}