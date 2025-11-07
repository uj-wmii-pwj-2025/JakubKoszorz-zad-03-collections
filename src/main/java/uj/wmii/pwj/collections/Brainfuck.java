package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public interface Brainfuck {

    /**
     * Executes uploaded program.
     */
    void execute();

    /**
     * Creates a new instance of Brainfuck interpreter with given program, using standard IO and stack of 1024 size.
     * @param program brainfuck program to interpret
     * @return new instance of the interpreter
     * @throws IllegalArgumentException if program is null or empty
     */
    static Brainfuck createInstance(String program) {
        return createInstance(program, System.out, System.in, 1024);
    }

    /**
     * Creates a new instance of Brainfuck interpreter with given parameters.
     * @param program brainfuck program to interpret
     * @param out output stream to be used by interpreter implementation
     * @param in input stream to be used by interpreter implementation
     * @param stackSize maximum stack size, that is allowed for this interpreter
     * @return new instance of the interpreter
     * @throws IllegalArgumentException if: program is null or empty, OR out is null, OR in is null, OR stackSize is below 1.
     */
    static Brainfuck createInstance(String program, PrintStream out, InputStream in, int stackSize) {
        if (program == null || program.isEmpty() || out == null || in == null || stackSize < 1)
            throw new IllegalArgumentException("Invalid arguments");
        return new BrainFuckImplementation(program, out, in, stackSize);
    }
}

