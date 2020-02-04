import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Program {
    public ArrayList<Instruction> instructions;
    public String name;
    public ArrayList<String> lines;
    public HashMap<String, Integer> labels;

    public Program(String name) {
        this.name = name;
        lines = new ArrayList<>();
        setLines(name);

        labels = new HashMap<>();
        set_labels();

        instructions = new ArrayList<>();
        set_instructions();
    }

    public void setLines(String name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
            String line = reader.readLine();

            while (line != null) {
                if (line.replace("\n", "").trim().length() > 2 && !line.replace("\n", "").trim().startsWith("#")) {
                    lines.add(line.replace("\n", "").trim());
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines.size());
    }

    public void set_labels() {
        for (int i = 0; i<lines.size(); i++) {
            if (lines.get(i).contains(":")) {
                labels.put(lines.get(i).substring(0, lines.get(i).length()-1), i);
            }
        }
    }

    public void set_instructions() {
        for (String line: lines) {
            Instruction i = Instruction.choose_instruction(line);
            instructions.add(i);
            if (i == null && !line.contains(":")){
                System.out.println(line); //Lines that couldn't be interpreted
            }
        }
    }

   public Instruction get(int index) {
       return instructions.get(index);
   }
}
