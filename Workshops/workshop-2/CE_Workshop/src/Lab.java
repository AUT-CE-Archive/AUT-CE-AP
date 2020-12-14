public class Lab {

    private Student[] students;
    private int avg;
    private String day;
    private int capacity;
    private int currentSize;

    public Lab(int cap, String d) {
    }

    public void enrollStudent(Student std) {
        if (currentSize < capacity) {
            students[currentSize] = std;
            currentSize++;
        } else {
            System.out.println("Lab is full!!!");
        }
    }

    /**
     * Getter
     * @return Student's array
     */
    public Student[] getStudents() { return students; }

    /**
     * Setter
     * @param students Student's array
     */
    public void setStudents(Student[] students) { this.students = students; }

    /**
     * Setter
     * @param avg Average
     */
    public void setAvg(int avg) { this.avg = avg; }

    /**
     * Calculate average
     */
    public void calculateAvg() {
        int sum = 0;
        for (Student student: students)
            sum += student.getGrade();

        this.setAvg(sum / students.length);
    }

    /**
     * Getter
     * @return Day
     */
    public String getDay() { return day; }

    /**
     * Setter
     * @param day Day
     */
    public void setDay(String day) { this.day = day; }

    /**
     * Getter
     * @return Capacity
     */
    public int getCapacity() { return capacity; }

    /**
     * Setter
     * @param capacity Capacity
     */
    public void setCapacity(int capacity) { this.capacity = capacity; }

    /**
     * Print students info
     */
    public void print() {
        for (Student student : students)
            System.out.println("std fname: " + student.getFirstname() + " std id:" + student.getID() + " std grade:" + student.getGrade());

        System.out.println("Lab AVG:" + avg);
    }
}
