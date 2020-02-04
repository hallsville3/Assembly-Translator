import java.util.Arrays;

public class Instruction {
    public String[] split;

    public Instruction(String[] split) {
        this.split = split;
    }

    public static Instruction choose_instruction(String line) {
        String[] split = line.split(" ");
        if (line.startsWith("movq")) {
            return new movq(split);
        } else if (line.startsWith("addq")) {
            return new addq(split);
        } else if (line.startsWith("incq")) {
            return new incq(split);
        } else if (line.startsWith("mulq") || line.startsWith("imulq")) {
            return new mulq(split);
        } else if (line.startsWith("subq")) {
            return new subq(split);
        } else if (line.startsWith("idivq")) {
            return new idivq(split);
        } else if (line.startsWith("andq")) {
            return new andq(split);
        } else if (line.startsWith("callq")) {
            return new Call(split);
        } else if (line.startsWith("cmpq")) {
            return new cmpq(split);
        } else if (line.startsWith("jl")) {
            return new jl(split);
        } else if (line.startsWith("jg")) {
            return new jg(split);
        } else if (line.startsWith("jle")) {
            return new jle(split);
        } else if (line.startsWith("jge")) {
            return new jge(split);
        } else if (line.startsWith("je")) {
            return new je(split);
        } else if (line.startsWith("jne")) {
            return new jne(split);
        } else if (line.startsWith("jmp")) {
            return new jmp(split);
        } else if (line.startsWith("pushq")) {
            return new pushq(split);
        } else if (line.startsWith("popq")) {
            return new popq(split);
        } else if (line.startsWith("ret")) {
            return new ret(split);
        }
        return null;
    }

    public void execute(CPU cpu) throws Exception {

    }
}

class movq extends Instruction {
    public int comma_count;

    public movq(String[] split) {
        super(split);
        if (split.length == 4) { //Memory address with a space in the middle -.-
            if (split[3].endsWith(")")) {
                split[2] += split[3];
            } else {
                split[1] += split[2];
                split[2] = split[3];
            }
        }

        if (split[1].startsWith("(")) {
            comma_count = -1;
            for (int i = 0; i < split[1].length(); i++) {
                if (split[1].charAt(i) == ',') {
                    comma_count++;
                }
            }
        } else if (split[2].startsWith("(")) {
            comma_count = 0;
            for (int i = 0; i < split[2].length(); i++) {
                if (split[2].charAt(i) == ',') {
                    comma_count++;
                }
            }
        }
    }

    public void execute(CPU cpu) throws Exception {
        String src = split[1];
        String dest = split[2];

        if (dest.startsWith("%")) {
            if (src.startsWith("%")) {  //Register to Register
                cpu.registers.get(dest).set(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer to Register
                cpu.registers.get(dest).set(Integer.valueOf((src.substring(1, src.length() - 1))));
            } else if (src.startsWith("(")) {
                if (comma_count == 0) { //Address stored in register to register
                    cpu.registers.get(dest).set(cpu.stack.get(cpu.registers.get(src.substring(1, src.length() - 2)).get()));
                } else if (comma_count == 1) { //Register to address stored in register w/ offset
                    int offset;
                    if (src.charAt(src.indexOf(",") + 1) == '%') {
                        offset = cpu.registers.get(src.substring(src.indexOf(",") + 1, src.length() - 2)).get();
                    } else {
                        offset = Integer.valueOf(src.substring(src.indexOf(",") + 1, src.length() - 2));
                    }
                    cpu.registers.get(dest).set(cpu.stack.get(cpu.registers.get(src.substring(1, src.indexOf(","))).get(), offset)); //-2 for comma and )
                } else if (comma_count == 2) { //Register to address stored in regiseter w/ offset and scale

                }
            }
        } else if (dest.startsWith("(")) {
            if (src.startsWith("%")) {
                if (comma_count == 0) { //Register to address stored in register
                    cpu.stack.set(cpu.registers.get(dest.substring(1, dest.length() - 1)).get(), cpu.registers.get(src.substring(0, src.length() - 1)).get());
                } else if (comma_count == 1) { //Register to address stored in register w/ offset
                    int offset;
                    if (dest.substring(dest.indexOf(",") + 1, dest.length() - 1).startsWith("%")) {
                        offset = cpu.registers.get(dest.substring(dest.indexOf(",") + 1, dest.length() - 1)).get();
                    } else {
                        offset = Integer.valueOf(dest.substring(dest.indexOf(",") + 1, dest.length() - 1));
                    }
                    cpu.stack.set(cpu.registers.get(dest.substring(1, dest.indexOf(","))).get(), cpu.registers.get(src.substring(0, src.length() - 1)).get(), offset);
                } else if (comma_count == 2) { //Register to address stored in regiseter w/ offset and scale

                }
            } else if (src.startsWith("$")) {
                if (comma_count == 0) { //Int to address stored in register
                    cpu.stack.set(cpu.registers.get(dest.substring(1, dest.length() - 1)).get(), Integer.valueOf(src.substring(1, src.length() - 1)));
                } else if (comma_count == 1) { //Register to address stored in register w/ offset
                    int offset = Integer.valueOf(dest.substring(dest.indexOf(",") + 1, dest.length() - 1));
                    cpu.stack.set(cpu.registers.get(dest.substring(1, dest.indexOf(","))).get(), Integer.valueOf(src.substring(1, src.length() - 1)), offset);
                }
            }
        } else { //Dest is raw memory address
            if (src.startsWith("%")) {  //Register to Register
                cpu.registers.get(dest).set(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer to Register
                cpu.registers.get(dest).set(Integer.valueOf((src.substring(1, src.length() - 1))));
            }
        }
    }
}

class addq extends Instruction {

    public addq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String src = split[1];
        String dest = split[2];
        if (dest.startsWith("%")) {
            if (src.startsWith("%")) {  //Register to Register
                cpu.registers.get(dest).add(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer to Register
                cpu.registers.get(dest).add(Integer.valueOf((src.substring(1, src.length() - 1))));
            }
        }
    }
}

class incq extends Instruction {

    public incq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String dest = split[1];
        if (dest.startsWith("%")) {
            cpu.registers.get(dest).add(1);
        }
    }
}

class mulq extends Instruction {

    public mulq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String src = split[1];
        String dest = split[2];
        if (dest.startsWith("%")) {
            if (src.startsWith("%")) {  //Register to Register
                cpu.registers.get(dest).mul(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer to Register
                cpu.registers.get(dest).mul(Integer.valueOf((src.substring(1, src.length() - 1))));
            }
        }
    }
}

class subq extends Instruction {

    public subq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String src = split[1];
        String dest = split[2];
        if (dest.startsWith("%")) {
            if (src.startsWith("%")) {  //Register to Register
                cpu.registers.get(dest).sub(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer to Register
                cpu.registers.get(dest).sub(Integer.valueOf((src.substring(1, src.length() - 1))));
            }
        }
    }
}

class idivq extends Instruction {

    public idivq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String div = split[1];
        int divisor = cpu.registers.get(div).get();
        int dividend = cpu.registers.get("%rax").get();
        cpu.registers.get("%rax").set(dividend / divisor);
        cpu.registers.get("%rdx").set(dividend % divisor);

    }
}

class andq extends Instruction {

    public andq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String src = split[1];
        String dest = split[2];
        if (dest.startsWith("%")) {
            if (src.startsWith("%")) {  //Register and Register
                cpu.registers.get(dest).and(cpu.registers.get(src.substring(0, src.length() - 1)).get());
            } else if (src.startsWith("$")) {  //#Integer and Register
                cpu.registers.get(dest).and(Integer.valueOf((src.substring(1, src.length() - 1))));
            }
        }
    }
}

class cmpq extends Instruction {

    public cmpq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String first = split[1];
        String second = split[2];

        int result = 0;

        if (second.startsWith("%")) {
            if (first.startsWith("%")) {  //Register to Register
                result = cpu.registers.get(second).get() - cpu.registers.get(first.substring(0, first.length() - 1)).get();
            } else if (first.startsWith("$")) {  //#Integer to Register
                result = cpu.registers.get(second).get() - Integer.valueOf((first.substring(1, first.length() - 1)));
            }
        }

        if (result < 0) {
            cpu.comparison_bit = -1;
        } else if (result > 0) {
            cpu.comparison_bit = 1;
        } else {
            cpu.comparison_bit = 0;
        }
    }
}

class jl extends Instruction {

    public jl(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit == -1) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class jg extends Instruction {

    public jg(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit == 1) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class jle extends Instruction {

    public jle(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit != 1) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class jge extends Instruction {

    public jge(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit != -1) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class je extends Instruction {

    public je(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit == 0) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class jne extends Instruction {

    public jne(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (cpu.comparison_bit != 0) {
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}

class jmp extends Instruction {

    public jmp(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
    }
}

class pushq extends Instruction {

    public pushq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        String src = split[1];
        if (src.startsWith("%")) {  //Register to Stack
            cpu.stack.push(cpu.registers.get(src).get());
        } else if (src.startsWith("$")) {  //#Integer to Stack
            cpu.stack.push(Integer.valueOf((src.substring(1))));
        }
        cpu.registers.get("%rsp").set(cpu.stack.getSize() - 1);
    }
}

class popq extends Instruction {

    public popq(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) throws Exception {
        String dest = split[1];
        cpu.registers.get(dest).set(cpu.stack.pop());
        cpu.registers.get("%rsp").set(cpu.stack.getSize() - 1);
    }
}

class ret extends Instruction {

    public ret(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) throws Exception {
        if (cpu.stack.getSize() == 0) { //Program should stop
            cpu.registers.get("%rip").set(cpu.program.instructions.size());
            return;
        }
        int address = cpu.stack.pop();
        cpu.registers.get("%rip").set(address - 1);
    }
}