package eu.janinko.xmppmuc.commands.dictionary;

import eu.janinko.botninko.api.CommandMessage;
import eu.janinko.botninko.api.PluginHelper;
import eu.janinko.botninko.api.plugin.AbstractCommand;
import java.io.IOException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jbrazdil
 */
public class Dictionary extends AbstractCommand {
	private static Logger logger = Logger.getLogger(Dictionary.class);
	
	private HashMap<String,String> dict;

    @Override
    public void setPluginHelper(PluginHelper pluginHelper) {
		super.setPluginHelper(pluginHelper);

		dict = (HashMap<String, String>) ph.loadData();
		if(dict == null){
			dict = new HashMap<>();
		}
	}

	@Override
	public String getCommand() {
		return "?";
	}

	@Override
	public void handleCommand(CommandMessage m) {
		String[] args = m.getArgs();
		if(args.length < 2){
			ph.sendMessage(this.help(ph.getPrefix()));
		}else if(args.length == 2){
			String key = args[1];
			if(dict.containsKey(key)){
				ph.sendMessage(key + ": " + dict.get(key));
			}else{
				ph.sendMessage("A prd... prostě jsem " + key + " nenašel :/");
			}
		}else{
			String key = args[1];
			String value = implode(args, 2);
			dict.put(key, value);
			try {
				ph.saveData(dict);
			ph.sendMessage(key + " = " + value);
			} catch (IOException ex) {
				logger.warn("Failed to save dictionary", ex);
			}
		}
	}

	@Override
	public String help(String prefix) {
		return prefix + getCommand() + " SLOVO - Vypíše poznámku k zadanému slovu.\n" +
			   prefix + getCommand() + " SLOVO VYZNAM - Nastavi zadany vyznam k zadanemu slovu";
	}
	
	public static String implode(String[] args, int start) {
		String delimiter = " ";
		StringBuilder sb = new StringBuilder();
		for(int i=start; i < args.length; i++){
			if(i != start){
				sb.append(delimiter);
			}
			sb.append(args[i]);
		}
		return sb.toString();
	}
}
