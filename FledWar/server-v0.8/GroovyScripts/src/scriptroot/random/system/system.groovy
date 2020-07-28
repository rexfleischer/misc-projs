
import com.fledwar.groovy.GroovyWrapper;

system_config = [
	[
		script : "random/system/unary.groovy",
		chance : 40,
	],
	[
		script : "random/system/binary.groovy",
		chance : 35,
	],
];




// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}


def max_chance = 0;
for(def i = 0; i < system_config.size(); i++)
{
	max_chance += system_config[i].chance;
}

def choice_number = (new Random()).nextInt(max_chance);
def choice_at = 0;
def choice;

for(def i = 0; i < system_config.size(); i++)
{
	def check = system_config[i];
	def check_high = (choice_at + check.chance);
	if (choice_at <= choice_number && choice_number < check_high)
	{
		choice = check;
		break;
	}
	choice_at = check_high;
}

if (choice == null)
{
	throw new Exception("some stupid stuff went down... couldnt find choice");
}

return GroovyWrapper.runScript(choice.script, [ name : name ]);
