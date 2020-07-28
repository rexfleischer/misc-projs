package projecttracker

class EndUser {
    
    String username;
    
    String fullname;
    
    String password;
    
    static hasMany = [
        projects : Project,
        tasks : Task
    ];

    static constraints = {
    }
}
