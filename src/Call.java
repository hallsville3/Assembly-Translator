public class Call extends Instruction{
    public Call(String[] split) {
        super(split);
    }

    public void execute(CPU cpu) {
        if (split[1].equals("printf")) {
            System.out.println(cpu.registers.get("%rsi").get());
        } else { //Must be a user defined function, time to find it and jump there, saving current address to stack
            cpu.stack.push(cpu.registers.get("%rip").get()+1); //Want to jump back to the next location
            cpu.registers.get("%rip").set(cpu.program.labels.get(split[1]));
        }
    }
}
