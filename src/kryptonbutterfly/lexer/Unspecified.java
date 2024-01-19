package kryptonbutterfly.lexer;

/**
 * This type is the fallback for areas that did not match any other token type
 */
public record Unspecified(String value, Location loc) implements Section<String>
{
	@Override
	public String toString()
	{
		return value;
	}
}