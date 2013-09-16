package eu.janinko.xmppmuc.commands.seen;

import eu.janinko.botninko.api.CommandMessage;
import eu.janinko.botninko.api.Message;
import eu.janinko.botninko.api.PluginHelper;
import eu.janinko.botninko.api.Presence;
import eu.janinko.botninko.api.Status;
import eu.janinko.botninko.api.plugin.AbstractCommand;
import eu.janinko.botninko.api.plugin.MessageHandler;
import eu.janinko.botninko.api.plugin.PresenceHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Seen extends AbstractCommand implements PresenceHandler, MessageHandler {
	private static Logger logger = Logger.getLogger(Seen.class);

	private Map<String,Pritomnost> seeny;
	private int counter = 0;

    @Override
    public void setPluginHelper(PluginHelper pluginHelper) {
		super.setPluginHelper(pluginHelper);
		seeny = (Map<String,Pritomnost>) ph.loadData();
		if (seeny == null) {
			seeny = new HashMap<>();
		}
	}

	@Override
	public String getCommand() {
		return "seen";
	}

	@Override
	public void handleCommand(CommandMessage m) {
		String[] args = m.getArgs();
		if(args.length != 2){
			ph.sendMessage("Jsem slepý! Nikoho jsem neviděl!");
		}else{
			String kdo = args[1].toLowerCase();
			if(kdo.equals("botninko")){
				ph.sendMessage("To jsem přeci já!");
			}else if(seeny.containsKey(kdo)){
				ph.sendMessage(seeny.get(args[1].toLowerCase()).toString());
			}else{
				ph.sendMessage("Ten tu nebyl! Přísahám!");
			}
		}
	}

	@Override
	public String help(String prefix) {
		return prefix + getCommand() + " nick";
	}

	@Override
	public int getPrivLevel() {
		return 0;
	}

	@Override
	public void handlePresence(Presence p) {
		String kdo = p.getNick();
		seeny.put(kdo.toLowerCase(), new Pritomnost(p.getType()));
		logger.debug("Seeing " + kdo + " doing " + p.getType());
		if(counter++ % 20 == 0){
			saveConfig();
		}
	}

	@Override
	public void handleMessage(Message m) {
		String kdo = m.getNick();
		seeny.put(kdo.toLowerCase(), new Pritomnost(m.getType()));
		logger.debug("Seeing " + kdo + " doing " + m.getType());
		if(counter++ % 20 == 0){
			saveConfig();
		}
	}
	
	private void saveConfig() {
		try {
			ph.saveData(seeny);
		} catch (IOException ex) {
			logger.warn("Can't save seend data.", ex);
		}
	}

	@Override
	public void handleStatus(Status s) {}
}