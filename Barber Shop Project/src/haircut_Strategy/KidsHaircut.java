package haircut_Strategy;

import interfaces.Haircut;

public class KidsHaircut implements Haircut {

    private int cost;
    private String name;
    private float duration;
    
    public KidsHaircut(int cost, String name, float duration){
        updateCost(cost);
        this.name = name;
        this.duration = duration;
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public void updateCost(int cost) {
        this.cost = cost;
    }


}
