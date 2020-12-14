public class Student {

    /**
     * Variables
     */
    private String firstname, lastname, ID;
    private int grade;

    /**
     * Create a new student with a given name and ID number.
     * @param firstname first name of student
     * @param lastname last name of student
     * @param ID student ID
     */
    public Student(String firstname, String lastname, String ID) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.ID = ID;
        this.grade = 0;
    }

    /**
     * Getter
     * @return firstname
     */
    public String getFirstname() { return firstname; }

    /**
     * Setter
     * @param firstname firstname
     */
    public void setFirstname(String firstname) { this.firstname = firstname; }

    /**
     * Setter
     * @param grade grade
     */
    public void setGrade(int grade) { this.grade = grade; }

    /**
     * Getter
     * @return ID
     */
    public String getID() { return ID; }

    /**
     * Getter
     * @return Grade
     */
    public int getGrade() { return grade; }

    /**
     * Print fields
     */
    public void print() { System.out.format("%s %s, ID: %s, Grade: %d", this.firstname, this.lastname, this.ID, this.grade); }

}