package main;
import thunder.Thunder;
import ch.quantasy.tinkerforge.tinker.agency.implementation.TinkerforgeStackAgency;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgentIdentifier;
import ch.quantasy.tinkerforge.tinker.application.definition.TinkerforgeApplication;

public class ThunderManager {
	// The 'server'-name of the fridge-sensor-stack
	public final TinkerforgeStackAgentIdentifier THUNDER_SENSOR = new TinkerforgeStackAgentIdentifier(
			"MasterBrick69");
	// Assumes to be connected via USB
	public final TinkerforgeStackAgentIdentifier THUNDER_VIEWER = new TinkerforgeStackAgentIdentifier(
			"localhost");
	private final TinkerforgeApplication thunderApp;

	public ThunderManager() {
		this.thunderApp= new Thunder();
	}

	public void start() {
		TinkerforgeStackAgency.getInstance().getStackAgent(THUNDER_VIEWER)
				.addApplication(thunderApp);
		TinkerforgeStackAgency.getInstance().getStackAgent(THUNDER_SENSOR)
				.addApplication(thunderApp);
	}

	public void stop() {
		TinkerforgeStackAgency.getInstance().getStackAgent(THUNDER_VIEWER)
				.removeApplication(thunderApp);
		TinkerforgeStackAgency.getInstance().getStackAgent(THUNDER_SENSOR)
				.removeApplication(thunderApp);
	}

	/**
	 * A simple boot-strap. The program will shut-down gracefully if one hits
	 * 'any' key on the console
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		final ThunderManager manager = new ThunderManager();
		manager.start();
		System.in.read();
		manager.stop();

	}

}
