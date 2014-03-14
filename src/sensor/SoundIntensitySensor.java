package sensor;

import thunder.Thunder;

import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletSoundIntensity.IntensityListener;
import com.tinkerforge.BrickletSoundIntensity.IntensityReachedListener;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class SoundIntensitySensor extends AbstractTinkerforgeApplication implements IntensityReachedListener {
	
	private final Thunder thunder;
	private BrickletSoundIntensity soundIntensitiyBricklet;
	private int min = 7;
	private int max = 9;

	public SoundIntensitySensor(final Thunder thunder) {
		this.thunder = thunder;
		}
	
	@Override
	public void deviceDisconnected(
		final TinkerforgeStackAgent tinkerforgeStackAgent,
		final Device device) {
	if ((TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.SoundIntensity)
			&& device.equals(this.soundIntensitiyBricklet)) {
		((BrickletSoundIntensity) device).removeIntensityReachedListener(this);
		this.soundIntensitiyBricklet = null;
	}
	}

	@Override
	public void deviceConnected(
		final TinkerforgeStackAgent tinkerforgeStackAgent,
		final Device device) {
	if (TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.SoundIntensity) {
		if (this.soundIntensitiyBricklet != null) {
			return;
		}
		this.soundIntensitiyBricklet = (BrickletSoundIntensity) device;
		this.soundIntensitiyBricklet.addIntensityReachedListener(this);
		this.setNoize();
	}
	}
	
	private void setNoize() {
		if (this.soundIntensitiyBricklet == null) {
			return;
		}
		try {
			this.soundIntensitiyBricklet.setIntensityCallbackThreshold(
					BrickletSoundIntensity.THRESHOLD_OPTION_OUTSIDE,
					(short) (this.min),
					(short) (this.max));
			this.soundIntensitiyBricklet.setDebouncePeriod(10);
			this.soundIntensitiyBricklet.setIntensityCallbackThreshold('>', (short)(2000), (short)0);

			this.thunder.setNoize(this.soundIntensitiyBricklet.getIntensity());

		} catch (final TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	@Override
	public int hashCode() {
	if(soundIntensitiyBricklet!=null)
		try {
			return soundIntensitiyBricklet.getIdentity().hashCode();
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
	final SoundIntensitySensor other = (SoundIntensitySensor) obj;
	return this.thunder==other.thunder;
	}

	@Override
	public void intensityReached(int intensity) {
		this.thunder.setNoize(intensity);
		
	}
	
}
