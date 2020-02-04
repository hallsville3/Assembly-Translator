# Assembly-Translator

##### A Java program to interpret AT&T x86 assembly code.

###### A subset of x86 is supported with relatively strict syntax.

Supported instructions are
* movq
* addq, subq, idivq, mulq / imulq
* incq
* andq
* callq
* cmpq
* jg, jge, jl, jle, je, jne, jmp
* pushq, popq
* ret

In general, a file formatted with basic x86 code using these instructions will execute.

Calls to `printf` are supported as shown in the provided source files.


Compile the files in src with `javac *.java`, then use `java Main` to run.
