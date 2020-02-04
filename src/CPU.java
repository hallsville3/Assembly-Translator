import java.util.HashMap;

public class CPU {
    public HashMap<String, Register> registers;
    public Stack stack = new Stack();
    public int comparison_bit;
    public Program program;

    public CPU() {
        register_init();
        comparison_bit = 0;
    }

    public void register_init() {
        registers = new HashMap<>();
        registers.put("%rax", new Register("%rax"));
        registers.put("%rbx", new Register("%rbx"));
        registers.put("%rcx", new Register("%rcx"));
        registers.put("%rdx", new Register("%rdx"));
        registers.put("%rip", new Register("%rip"));
        registers.put("%rsi", new Register("%rsi"));
        registers.put("%rsp", new Register("%rsp", -1));
        registers.put("%rdi", new Register("%rdi"));
        registers.put("%r8", new Register("%r8"));
        registers.put("%r9", new Register("%r9"));
        registers.put("%r10", new Register("%r10"));
        registers.put("%r11", new Register("%r11"));
    }

    public void run(Program p) throws Exception {
        registers.get("%rip").set(0);
        program = p;
        double MHz = 0;
        double executed = 0.0;
        long s = System.currentTimeMillis();
        long start = s;
        System.out.println("Starting Execution");

        //Main execution
        while (registers.get("%rip").get() < program.instructions.size()) {
            Instruction i = p.instructions.get(registers.get("%rip").get());

            if (i != null) {
                executed++;
                i.execute(this);
            }
            registers.get("%rip").inc();

            if ((executed + 1) % 500000000 == 0) {
                MHz = executed / (1000 * (System.currentTimeMillis() - s));
                executed = 0;
                System.out.println("Clock speed is " + (int) (MHz) + "MHz");
                s = System.currentTimeMillis();
            }
        }

        //Clean-up
        int size = stack.getSize();
        if (size != 0) {
            System.out.println("There are " + stack.getSize() + " un-freed items left in the stack.");
        }
        System.out.println("That took " + (System.currentTimeMillis() - start) / 1000.0 + " seconds.");
    }
}