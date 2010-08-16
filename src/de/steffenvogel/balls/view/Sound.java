package de.steffenvogel.balls.view;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound implements Runnable {
	AudioInputStream stream;
	DataLine.Info info;
	Clip clip;
	private ThreadPoolExecutor executor;

	public Sound(File file) {
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
		executor = new ThreadPoolExecutor(30, 200, 1, TimeUnit.SECONDS, queue);
		
		
		try{
            stream = AudioSystem.getAudioInputStream(file);
            AudioFormat af = stream.getFormat();
            int size = (int) (af.getFrameSize() * stream.getFrameLength());
            byte[] audio = new byte[size];
            info = new DataLine.Info(Clip.class, af, size);
            stream.read(audio, 0, size);
            
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(af, audio, 0, size);
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (executor.getPoolSize() < 200) {
			executor.execute(this);
		}

		System.out.println("triggered" + executor.getTaskCount() );
	}

	@Override
	public void run() {
		try {
			clip.setFramePosition(0);
			clip.start();
			clip.stop();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
