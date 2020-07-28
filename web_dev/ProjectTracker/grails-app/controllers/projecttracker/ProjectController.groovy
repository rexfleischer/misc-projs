package projecttracker

class ProjectController {
    def scaffold = true;
    
    def index = { 
        render "project tracker video tutorials";
    }
    
    def overdue = {
        render "order stuff too late";
    }
    
    def current = {
        def allprojects = Project.list();
        
        [allprojects : allprojects]
    }
}
