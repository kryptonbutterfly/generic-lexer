package kryptonbutterfly.lexer;

import java.util.ArrayList;

public record LexedFile(String file, ArrayList<Section<?>> tokens)
{}