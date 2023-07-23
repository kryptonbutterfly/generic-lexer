package de.tinycodecrank.lexer;

public interface Operators
{
	/**
	 * @return The operators precedence
	 */
	public int precendence();
	
	/**
	 * @return The String representing this operator.
	 */
	public String op();
}