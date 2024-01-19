package kryptonbutterfly.lexer;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Matcher
{
	private final IsStartOf						startOf;
	private final ToIntFunction<EndData>		end;
	private final Creator<? extends Section<?>>	creator;
	
	public Matcher(IsStartOf startOf, ToIntFunction<EndData> end, Creator<? extends Section<?>> creator)
	{
		this.startOf	= startOf;
		this.end		= end;
		this.creator	= creator;
	}
	
	public int end(EndData data)
	{
		return this.end.applyAsInt(data);
	}
	
	@FunctionalInterface
	public static interface Creator<E>
	{
		Function<Location, E> create(String value);
	}
	
	public IsStartOf startOf()
	{
		return startOf;
	}
	
	public Creator<? extends Section<?>> creator()
	{
		return creator;
	}
}