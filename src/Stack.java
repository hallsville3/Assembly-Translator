import java.util.ArrayList;

public class Stack {
    ArrayList<Integer> data;

    public Stack() {
        data = new ArrayList<>();
    }

    public void push(int value) {
        data.add(value);
    }

    public int pop() throws Exception {
        if (data.size() == 0) {
            throw new Exception ("Segmentation Fault");
        }
        int i = data.get(data.size()-1);
        data.remove(data.size()-1);
        return i;
    }

    public int get(int index, int offset) throws Exception {
        if (index-offset < data.size() && data.size() != 0) {
            return data.get(index - offset);
        } else {
            throw new Exception ("Segmentation Fault");
        }
    }

    public int get(int index) throws Exception {
        return get(index, 0);
    }

    public int getSize() {
        return data.size();
    }

    public void set(int index, int value) {
        set(index, value, 0);
    }

    public void set(int index, int value, int offset) {
        data.set(index-offset, value);
    }
}
