public class Register {
    public String name;
    public int value;

    public Register(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Register(String name) {
        this.name = name;
        value = 0;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }

    public void sub(int value) {
        this.value -= value;
    }

    public void inc() {
        add(1);
    }

    public void mul(int value) {
        this.value *= value;
    }

    public void and(int value) {
        this.value &= value;
    }
}
