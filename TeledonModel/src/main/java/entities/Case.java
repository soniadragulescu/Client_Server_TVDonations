package entities;

public class Case extends Entity<Integer> {
    private String name;
    private Double totalSum;

    public Case(Integer id,String name, Double totalSum) {
        this.setId(id);
        this.name = name;
        this.totalSum = totalSum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }
}
