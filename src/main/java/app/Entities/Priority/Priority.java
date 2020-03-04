package app.Entities.Priority;

public class Priority {

    String priorityId;
    String priorityName;

    public Priority() {

    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priority_id) {
        this.priorityId = priority_id;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "priority_id='" + priorityId + '\'' +
                ", priorityName='" + priorityName + '\'' +
                '}';
    }
}
