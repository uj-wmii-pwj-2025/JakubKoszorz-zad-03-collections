package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class BrainFuckImplementation implements Brainfuck {

    private final String program;
    private final PrintStream out;
    private final InputStream in;
    private final byte[] memory;
    private int pointer = 0;

    private Map<Integer, Integer> Loop = new HashMap<>();

    public BrainFuckImplementation(String program, PrintStream out, InputStream in, int stackSize) {
        if (program == null) {
            throw new IllegalArgumentException("Program code cannot be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        if (stackSize <= 0) {
            throw new IllegalArgumentException("Memory size has to be greater than 0");
        }
        this.program = program;
        this.out = out;
        this.in = in;
        this.memory = new byte[stackSize];
        this.Loop = new HashMap<>();
        makeLoopMap(program);
    }

    private void makeLoopMap(String program) {
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < program.length(); i++) {
            char c = program.charAt(i);
            if (c == '[') {
                stack.push(i);
            } else if (c == ']') {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Unmatched ']' at position " + i);
                }
                int start = stack.pop();
                Loop.put(start, i);  // '[' -> odpowiadający ']'
                Loop.put(i, start);  // ']' -> odpowiadający '['
            }
        }

        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("Unmatched '[' at position " + stack.pop());
        }
    }

    @Override
    public void execute() {
        int programCounter = 0;
        try {
            while (programCounter < program.length()) {
                char c = program.charAt(programCounter);

                switch (c) {
                    case '>':
                        pointer++;
                        if (pointer >= memory.length)
                            throw new RuntimeException("Pointer out of bounds");
                        break;
                    case '<':
                        pointer--;
                        if (pointer < 0)
                            throw new RuntimeException("Pointer out of bounds");
                        break;
                    case '+':
                        memory[pointer]++;;
                        break;
                    case '-':
                        memory[pointer]--;;
                        break;
                    case '.':
                        out.print((char) (memory[pointer]));
                        break;
                    case ',':
                        memory[pointer] = (byte) in.read();
                        break;
                    case '[':
                        if (memory[pointer] == 0) {
                            programCounter = Loop.get(programCounter); // Przeskocz do ]
                        }
                        break;
                    case ']':
                        if (memory[pointer] != 0) {
                            programCounter = Loop.get(programCounter); // Przeskocz do [
                        }
                        break;
                }
                programCounter++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Execution error", e);
        }
        out.flush();
    }

}
