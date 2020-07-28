package projecttracker

class Task {
    
    String name;
    
    String description;
    
    Date duedate;
    
    static belongsTo = [
        assignee : EndUser,
        project : Project
    ];

    static constraints = {
    }
}
