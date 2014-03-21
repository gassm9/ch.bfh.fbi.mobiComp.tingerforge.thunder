package sensor;

import thunder.Thunder;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickIMU;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickIMU.AccelerationListener;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class ImmuAccelerationSensor extends AbstractTinkerforgeApplication implements AccelerationListener {

	

private final Thunder thunder;
private BrickIMU imu;
	
	public ImmuAccelerationSensor(final Thunder thunder) {
		this.thunder = thunder;
		}
	
	@Override
	public void deviceConnected(TinkerforgeStackAgent tinkerforgeStackAgent,
			Device device) {
		if (TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.IMU) {
			if (this.imu != null) {
				return;
			}
			this.imu = (BrickIMU) device;
			this.imu.addAccelerationListener(this);
			try {
				this.imu.setAccelerationPeriod(500L);
				this.thunder.setAcceleration(this.imu.getAcceleration());
			} catch (TimeoutException | NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
	}

	@Override
	public void deviceDisconnected(TinkerforgeStackAgent tinkerforgeStackAgent,
			Device device) {
		if ((TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.IMU)
				&& device.equals(this.imu)) {
			((BrickIMU) device).removeAccelerationListener(this);
			this.imu = null;
		}
		
	}

	@Override
	public void acceleration(short x, short y, short z) {

		try {
			this.thunder.setAcceleration(this.imu.getAcceleration());
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
