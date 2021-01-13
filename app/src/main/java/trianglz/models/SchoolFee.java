package trianglz.models;

public class SchoolFee {

    private Integer id;
    private String name;
    private String amount;
    private String due_date;
    private String student_name;

    public SchoolFee(Integer id, String name, String amount, String due_date, String student_name) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.due_date = due_date;
        this.student_name = student_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
