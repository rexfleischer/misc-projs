
import com.mongodb.BasicDBObject


if (!binding.variables.containsKey("clazz") || 
    !binding.variables.containsKey("query")) {
  throw new Exception("not all required variables were sent");
}
def view = (binding.variables.containsKey("view") ? view as BasicDBObject : null);
def qclazz = Class.forName(clazz);

def db_results;
def dao;
try {
  dao = engine.getDAOFactoryRegistry().get(qclazz);
  db_results = dao.find(query as BasicDBObject, view);
}
finally {
  if (dao != null) {
    dao.close();
  }
}

def results = [];
for(def obj : db_results) {
  results << obj.getDataAsMap();
}
return results;
