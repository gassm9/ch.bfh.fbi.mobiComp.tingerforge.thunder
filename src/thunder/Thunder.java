package thunder;

import com.tinkerforge.BrickIMU.Acceleration;
import com.tinkerforge.Device;

import sensor.AmbientLightSensor;
import sensor.ImmuAccelerationSensor;
import sensor.SoundIntensitySensor;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;


public class Thunder extends AbstractTinkerforgeApplication {
	// A heuristic value representing the illuminance-threshold above which
	// the light in the fridge is lit.
	// Why is it not 0? The sensor gives some noise!
	// Why is it not 0? Old cheese might be glowing in the dark ;-)
	public static final int LIGHT_IS_SWITCHED_ON = 8;
	public static final int LIGHT_IS_SWITCHED_OFF = 7;
	private long startTime;
	private long endTime;
	private boolean started = false;
	private int acc = 0;


	private final AmbientLightSensor ambientLight;
	private final SoundIntensitySensor soundIntensity;
	private final ImmuAccelerationSensor imuAcceleration;

	/**
	 * The {@link FridgeSensor} and the {@link FridgeViewer} are instantiated
	 * and connected. The instance will only exist if everything went fine.
	 * 
	 * @throws Exception
	 *             if one of the
	 */
	public Thunder() {
		this.ambientLight = new AmbientLightSensor(this);
		this.soundIntensity =  new SoundIntensitySensor(this);
		this.imuAcceleration = new ImmuAccelerationSensor(this);
		super.addTinkerforgeApplication(this.ambientLight);
		super.addTinkerforgeApplication(this.soundIntensity);
		super.addTinkerforgeApplication(this.imuAcceleration);
	
	}

	/**
	 * Information if the interieur light of the fridge is lit or not. The info
	 * is passed to the {@link FridgeViewer}
	 * 
	 * The histerese-threshold must be set in the
	 * {@link AmbientLightApplication}.
	 */
	public void setAmbientDarkState(final boolean latestAnswerIsItDark) {
		
		if(latestAnswerIsItDark==false){
			startTime = System.currentTimeMillis();
			started = true;
		}
		System.out.println(!latestAnswerIsItDark);
	}
	
	public void setNoize(final int noize) {
		endTime = System.currentTimeMillis();
		if(started && getAcceleration()>2000){
			long duration = (endTime - startTime) /1000;
			System.out.println("Dauer[s]: "+ duration);
			System.out.println("Entfernung[m]: "+ duration*340);
			started = false;
		}
//		System.out.println(noize);
	}
	
	public void setAcceleration(Acceleration acc){
		this.acc=acc.z;
//		if(Math.pow(acc.z, 2)>2000){
//		System.out.println(Math.pow(acc.z, 2));
//		}
	}
	public int getAcceleration(){
		acc = (int) Math.pow(acc, 2);
		return acc;
	}


	@Override
	public void deviceDisconnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		// We do not care here...

	}

	@Override
	public void deviceConnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		// We do not care here...

	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(final Object obj) {
		return this==obj;
	}

}
