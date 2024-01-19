package kryptonbutterfly.lexer;

public record EndData(int start, String raw, Location loc)
{
	@Override
	public String toString()
	{
		return "EndData:\n\tstart\t"
			+ start
			+ "\n\tloc\t\t"
			+ loc;
	}
}