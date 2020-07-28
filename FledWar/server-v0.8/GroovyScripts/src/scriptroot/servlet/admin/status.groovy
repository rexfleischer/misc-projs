
import com.fledwar.mongo.MongoConnect;
import java.text.NumberFormat;

def get_dao_stats(def clazz, def dbs, def collections)
{
    def user_dao = engine.getDAOFactoryRegistry()
            .getDAOFactory(clazz);
    def user_factory = engine.getDAOFactoryRegistry()
            .getFactories().get(clazz);
    try
    {
        collections << user_dao.getCollection().getStats();
        def potential_db = user_dao.getCollection().getDB().getName();
        if (!dbs.contains(potential_db))
        {
            dbs << potential_db;
        }

        def dao_stats = [:];
        dao_stats.class = user_dao.getClass().toString();
        dao_stats.pool_size = user_factory.getPool().size();
        dao_stats.last_gc = user_factory.getLastGCTime();
        return dao_stats;
    }
    finally
    {
        if (user_dao != null)
        {
            user_dao.close();
        }
    }
}


def result = [:];

def simulation = engine.getBlackboxSimulation();
def thread_pool = simulation.getThreadPool();
def galaxy_scope = simulation.getScopeIds();
def balancer = simulation.getGalaxyScopeBalancer();
result.simulation = [
	scope : galaxy_scope,
	scope_size : galaxy_scope.size(),
  balancer : balancer.status(),
	thread_max : thread_pool.getCorePoolSize(),
	thread_curr : thread_pool.getPoolSize(),
	task_complete : thread_pool.getCompletedTaskCount(),
	task_queued : thread_pool.getTaskCount()
];




L:{
	def up_time_ms = (System.currentTimeMillis() - engine.getStartTime());
	result.up_time_ms = up_time_ms;
	
	def days = (int)(up_time_ms / (24 * 60 * 60 * 1000));
	up_time_ms -= (days * (24 * 60 * 60 * 1000));
	def hours = (int)(up_time_ms / (60 * 60 * 1000));
	up_time_ms -= (hours * (60 * 60 * 1000));
	def minutes = (int)(up_time_ms / (60 * 1000));
	up_time_ms -= (minutes * (60 * 1000));
	def seconds = (int)(up_time_ms / (1000));
	up_time_ms -= (seconds * (1000));
	
	result.up_time = [
		ms : up_time_ms,
		seconds : seconds,
		minutes : minutes,
		hours : hours,
		days : days,
	];
}


// now get the physical status
L:{
	def runtime = Runtime.getRuntime();
	def format = NumberFormat.getInstance();

	def maxMemory = runtime.maxMemory();
	def allocatedMemory = runtime.totalMemory();
	def freeMemory = runtime.freeMemory();

	result.physical = [:];
	result.physical."free-memory" = 
			format.format((int) freeMemory / 1024);
	result.physical."allocated-memory" = 
			format.format((int) allocatedMemory / 1024);
	result.physical."max-memory" = 
			format.format((int) maxMemory / 1024);
	long total = freeMemory + (maxMemory - allocatedMemory);
	result.physical."total-free-memory" = 
			format.format((int) total / 1024);
}


result.daos = [];
result.collections = [];
def dbs = [];

import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GalaxyMicroUnitDAO;
import com.fledwar.dao.GameUserDAO;
result.daos << get_dao_stats(GalaxyScopeDAO.class, dbs, result.collections);
result.daos << get_dao_stats(GalaxyPointDAO.class, dbs, result.collections);
result.daos << get_dao_stats(GalaxyUnitDAO.class, dbs, result.collections);
result.daos << get_dao_stats(GalaxyMicroUnitDAO.class, dbs, result.collections);
result.daos << get_dao_stats(GameUserDAO.class, dbs, result.collections);



result.databases = [];
for(def db : dbs)
{
	result.databases << MongoConnect.getDB(db).getStats();
}



return result;
