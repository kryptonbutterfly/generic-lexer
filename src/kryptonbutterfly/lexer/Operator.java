package kryptonbutterfly.lexer;

import java.util.function.ToIntFunction;

import kryptonbutterfly.lexer.Matcher.Creator;

public record Operator(IOperators value, Location loc) implements Section<IOperators>
{
	@Override
	public String toString()
	{
		return value.toString();
	}
	
	public static Creator<Operator> create(IOperators op)
	{
		return _value -> loc -> new Operator(op, loc);
	}
	
	public static IsStartOf start(IOperators op)
	{
		return (target, offset, start) -> target.startsWith(op.op(), offset);
	}
	
	public static ToIntFunction<EndData> end(IOperators op)
	{
		return data -> data.start() + op.op().length();
	}
}