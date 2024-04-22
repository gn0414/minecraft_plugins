package cn.simon.simonrpg.attack.entity;

public class Damage {

    public Damage(){
        this.damage = 1.0;
        this.setIsCancel(false);
    }

    private double damage;

    private boolean critical;

    private boolean miss;

    private boolean block;

    private double steal;

    private double blockSize;

    private boolean isCancel;

    public void setIsCancel(boolean cancel) {
        isCancel = cancel;
    }
    public boolean getIsCancel(){
        return isCancel;
    }

    public void setBlockSize(double blockSize) {
        this.blockSize = blockSize;
    }

    public double getBlockSize() {
        return blockSize;
    }

    public void setSteal(double steal) {
        this.steal = steal;
    }

    public double getSteal() {
        return steal;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
    public boolean getBlock(){
        return this.block;
    }

    public void setMiss(boolean miss) {
        this.miss = miss;
    }



    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return this.damage;
    }
    public boolean getMiss(){
        return this.miss;
    }

    public boolean getCritical(){
        return this.critical;
    }

    @Override
    public String toString() {
        return "Damage{" +
                "damage=" + damage +
                ", critical=" + critical +
                ", miss=" + miss +
                ", block=" + block +
                '}';
    }
}
