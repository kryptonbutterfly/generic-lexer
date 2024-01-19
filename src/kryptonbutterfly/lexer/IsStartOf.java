package kryptonbutterfly.lexer;

import kryptonbutterfly.functions.ObjIntBiPredicate;
import kryptonbutterfly.functions.applicable.ApplicableLeft;
import kryptonbutterfly.functions.int_.IntBiPredicate;
import kryptonbutterfly.functions.int_.applicable.IntApplicableRight;

@FunctionalInterface
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