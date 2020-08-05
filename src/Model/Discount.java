package Model;

public class Discount {

    public Discount(String condition, double conditionValue, int amount) {
        this.condition = condition;
        this.conditionValue = conditionValue;
        this.amount = amount;
    }

    private String condition;
    private double conditionValue;
    private int amount;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) {
            return false;
        }
        if(obj instanceof Discount) {
            Discount d = (Discount) obj;
            return (d.condition.equals(this.condition) &&
                    d.conditionValue == this.conditionValue &&
                    d.amount == this.amount);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        int hashcode = 0;
        hashcode += condition.hashCode();
        hashcode += conditionValue*20;
        hashcode += amount*20;
        return hashcode;
    }
}
