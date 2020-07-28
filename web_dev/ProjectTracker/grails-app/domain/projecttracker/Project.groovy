package projecttracker

class Project {
    
    String name;
    
    String description;
    
    Date duedate;
    
    static belongsTo = [owners : EndUser];
    
    static hasMany = [tasks : Task];

    static constraints = {
        
    }
}
