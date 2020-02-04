public class Main {
    public static void main(String args[]) throws Exception {
        CPU cpu = new CPU();
        Program p = new Program("source2.txt");
        cpu.run(p);
    }
}
