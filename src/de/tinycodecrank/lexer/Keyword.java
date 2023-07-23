package de.tinycodecrank.lexer;

public record Keyword(Keywords value, Location loc) implements Section<Keywords>
{
	@Override
	public Keywords value()
	{
		return value;
	}
	
	@Override
	public Location loc()
	{
		return loc;
	}
	
	@Override
	public String toString()
	{
		return value.toString();
	}
}