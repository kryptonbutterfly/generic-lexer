package kryptonbutterfly.lexer;

public interface Section<V>
{
	public V value();
	
	public Location loc();
}