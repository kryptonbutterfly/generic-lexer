package de.tinycodecrank.lexer;

import de.tinycodecrank.functions.ObjIntBiPredicate;
import de.tinycodecrank.functions.applicable.ApplicableLeft;
import de.tinycodecrank.functions.int_.IntBiPredicate;
import de.tinycodecrank.functions.int_.applicable.IntApplicableRight;

public interface IsStartOf extends ApplicableLeft<String, IntBiPredicate>, IntApplicableRight<ObjIntBiPredicate<String>>
{
	boolean test(String target, int offset, int start);
	
	@Override
	default IntBiPredicate aptFirst(String target)
	{
		return (offset, start) -> test(target, offset, start);
	}
	
	@Override
	default ObjIntBiPredicate<String> aptLast(int start)
	{
		return (target, offset) -> test(target, offset, start);
	}
}