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
}
