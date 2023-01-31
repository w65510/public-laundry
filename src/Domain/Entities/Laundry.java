package Domain.Entities;

public class Laundry
{
    public static String TableName = "laundry";

    public int id;
    public String name;
    public int weight;

    public String toString() {
        return String.format("%1$-32sWaga: %2$s", name, weight);
    }
}
