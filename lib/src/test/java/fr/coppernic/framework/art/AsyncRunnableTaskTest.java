package fr.coppernic.framework.art;


import fr.coppernic.framework.utils.core.CpcResult.RESULT;

/**
 * Created by bastien on 16/06/16.
 */
public class AsyncRunnableTaskTest extends AsyncRunnableTask<RESULT> {

	private long timeToSleep = 100;
	private RESULT result = RESULT.OK;

	public void setTimeToSleep(long timeToSleep) {
		this.timeToSleep = timeToSleep;
	}

	public void setResult(RESULT result) {
		this.result = result;
	}

	@Override
	public String getName() {
		return "Name";
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(timeToSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		onDone(result);
	}
}
