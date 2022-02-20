package haircut_Strategy;

import interfaces.Haircut;

public class Context implements Haircut {

    private Haircut strategyHaircut;

    public Context(Haircut strategyHaircut){
        this.strategyHaircut = strategyHaircut;
    }

    @Override
    public int getCost() {
        return strategyHaircut.getCost();
    }

    @Override
    public String getName() {
        return strategyHaircut.getName();
    }

    @Override
    public float getDuration() {
        return strategyHaircut.getDuration();
    }

    @Override
    public void updateCost(int cost) throws Exception {
        if(cost < 0 || cost > 1000000)
            throw new Exception("Cost must be positive and under 1000000₪");
        this.strategyHaircut.updateCost(cost);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(strategyHaircut.getName()).append(" - Cost: ").append(strategyHaircut.getCost()).append("₪");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Haircut))
            return false;
        Haircut temp = (Haircut) other;
        return temp.getName().equals(strategyHaircut.getName());
    }
}
