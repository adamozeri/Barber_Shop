package interfaces;

public interface Haircut {
    int getCost();
    String getName();
    float getDuration();
    void updateCost(int cost) throws Exception;
    boolean equals(Object other);
    String toString();
}
