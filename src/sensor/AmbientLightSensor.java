package sensor;

import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletAmbientLight.IlluminanceReachedListener;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import thunder.Thunder;

public class AmbientLightSensor extends AbstractTinkerforgeApplication
implements IlluminanceReachedListener {
private final Thunder thunder;
private BrickletAmbientLight ambientLightBricklet;
private boolean latestAnswerIsItDark;
private int ambientHistereseMin = 50;
private int ambientHistereseMax = 4000;

/**
* Creates a new instance of {@link AmbientLightApplication}
* 
* @param frdidgeIt
*            The Application to be informed onChange
*/
public AmbientLightSensor(final Thunder thunder) {
this.thunder = thunder;
}

@Override
public void deviceDisconnected(
	final TinkerforgeStackAgent tinkerforgeStackAgent,
	final Device device) {
if ((TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.AmbientLight)
		&& device.equals(this.ambientLightBricklet)) {
	((BrickletAmbientLight) device)
			.removeIlluminanceReachedListener(this);
	this.ambientLightBricklet = null;
}
}

@Override
public void deviceConnected(
	final TinkerforgeStackAgent tinkerforgeStackAgent,
	final Device device) {
if (TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.AmbientLight) {
	if (this.ambientLightBricklet != null) {
		return;
	}
	this.ambientLightBricklet = (BrickletAmbientLight) device;
	this.ambientLightBricklet.addIlluminanceReachedListener(this);
	this.setAmbientHisteres();
//print illuminance	
//	this.ambientLightBricklet.addIlluminanceListener(this);
//	try {
//		this.ambientLightBricklet.setIlluminanceCallbackPeriod(100);
//	} catch (TimeoutException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (NotConnectedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
}
}

/**
* @return The histerese threshold max for 'dark'.
*/
public int getAmbientHistereseMax() {
return this.ambientHistereseMax / 10;
}

/**
* 
* @return The histerese threshold min for 'dark'.
*/
public int getAmbientHistereseMin() {
return this.ambientHistereseMin / 10;
}

/**
* Sets the maximum amount of Lux which is STILL 'dark'. In order to work
* properly, this value must be slightly higher (some Lux) than the
* according minimum. This creates a histerese which avoids 'flickering'.
* 
* @param ambientHistereseMaxInLux
*/
public void setAmbientHistereseMax(final int ambientHistereseMaxInLux) {
this.ambientHistereseMax = ambientHistereseMaxInLux * 10;
this.setAmbientHisteres();
}

/**
* Sets the minimum amount of Lux which is STILL 'bright'. In order to work
* properly, this value must be slightly lower (some Lux) than the according
* maximum. This creates a histerese which avoids 'flickering'.
* 
* @param ambientHistereseMaxInLux
*/
public void setAmbientHistereseMin(final int ambientHistereseMinInLux) {
this.ambientHistereseMin = ambientHistereseMinInLux * 10;
this.setAmbientHisteres();
}

private void setAmbientHisteres() {
if (this.ambientLightBricklet == null) {
	return;
}
try {
	this.ambientLightBricklet.setIlluminanceCallbackThreshold(
			BrickletAmbientLight.THRESHOLD_OPTION_OUTSIDE,
			(short) (this.ambientHistereseMin),
			(short) (this.ambientHistereseMax));
	this.ambientLightBricklet.setDebouncePeriod(10);

	this.thunder.setAmbientDarkState(this.ambientLightBricklet
			.getIlluminance() < this.ambientHistereseMin);

} catch (final TimeoutException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (final NotConnectedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}

@Override
public void illuminanceReached(final int illuminance) {
if (this.latestAnswerIsItDark == (illuminance < this.ambientHistereseMax)) {
	return;
}
this.latestAnswerIsItDark = !this.latestAnswerIsItDark;
this.thunder.setAmbientDarkState(this.latestAnswerIsItDark);
}

@Override
public int hashCode() {
if(ambientLightBricklet!=null)
	try {
		return ambientLightBricklet.getIdentity().hashCode();
	} catch (Exception e) {
	}
return 0;
}

@Override
public boolean equals(final Object obj) {
if (this == obj) {
	return true;
}
if (obj == null) {
	return false;
}
if (this.getClass() != obj.getClass()) {
	return false;
}
final AmbientLightSensor other = (AmbientLightSensor) obj;
return this.thunder==other.thunder;
}

//print illuminance
//@Override
//public void illuminance(int illuminance) {
//	// TODO Auto-generated method stub
//	System.out.println(illuminance);
//}

}

