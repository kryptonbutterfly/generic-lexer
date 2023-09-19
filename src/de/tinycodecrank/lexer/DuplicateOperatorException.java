package de.tinycodecrank.lexer;

final class DuplicateOperatorException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	DuplicateOperatorException(String msg, Object... args)
	{
		super(msg.formatted(args));
	}
	
	DuplicateOperatorException(IOperators first, IOperators second)
	{
		this("Operator '%s' is defined in %s and %s!", first.op(), first, second);
	}
}