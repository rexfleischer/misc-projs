
import com.fledwar.mongo.MongoConnect;
import com.fledwar.server.FledWarServer;

// basically we just need to delete the mongo db
def to_delete = FledWarServer.getConfiguration()
			.getAsString("MONGO_DATABASE");
MongoConnect.getDB(to_delete).dropDatabase();