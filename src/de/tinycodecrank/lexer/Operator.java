package de.tinycodecrank.lexer;

import java.util.function.ToIntFunction;

import de.tinycodecrank.lexer.Matcher.Creator;

public record Operator(Operators value, Location loc) implements Section<Operators>
{
	@Override
	public String toString()
	{
		return value.toString();
	}
	
	public static Creator<Operator> create(Operators op)
	{
		return _value -> loc -> new Operator(op, loc);
	}
	
	public static IsStartOf start(Operators op)
	{
		return (target, offset, start) -> target.startsWith(op.op(), offset);
	}
	
	public static ToIntFunction<EndData> end(Operators op)
	{
		return data -> data.start() + op.op().length();
	}
}