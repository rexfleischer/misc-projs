
import com.fledwar.server.FledWarServer;

engine.shutdown();


def destroy = [
	:
];
engine.command("mgt/fledwar_destroy.groovy", destroy);


FledWarServer.shutdown();
FledWarServer.start();
engine = FledWarServer.getEngine();

def init = [
	:
];
engine.command("baseline/baseline_data.groovy", init);

return null;
