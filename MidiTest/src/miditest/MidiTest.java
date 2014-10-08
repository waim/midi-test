package miditest;

import static miditest.音.*;
import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiTest {

	public static void main(String[] args)
			throws MidiUnavailableException, InterruptedException {
		sound(600, ど);
		sound(600, れ);
		sound(600, み);
		sound(1000, ど, み, そ);

		oneSound(600, ど, ふぁ, ら);
	}

	private static void sound(int msec, 音... notes)
			throws InterruptedException {
		List<音> noteList = Arrays.asList(notes);

		Midi midi = new Midi();
		midi.setMsec(msec);
		midi.奏.accept(noteList);
	}

	private static void oneSound(int msec, 音... notes)
			throws InterruptedException {
		Midi midi = new Midi();
		Consumer<音> 単奏 = note -> midi.奏.accept(Arrays.asList(note));

		List<音> noteList = Arrays.asList(notes);
		midi.setMsec(msec);
		noteList.stream().forEach(単奏);
	}
}

class Midi {

	private int msec = 1000;

	public void setMsec(int msec) {
		this.msec = msec;
	}

	//Java関数型オブジェクト
	private ObjIntConsumer<List<音>> SOU = (notes, onOff) -> {
		try (Receiver midireceiver = MidiSystem.getReceiver()) {

			notes.stream().forEach(note -> {
				try {
					ShortMessage smsg = new ShortMessage();
					smsg.setMessage(onOff, note.getNotenum(), 127);
					midireceiver.send(smsg, -1);
				} catch (InvalidMidiDataException e) {
				}
			});

			// 奏のときだけ音を伸ばす
			if (onOff == NOTE_ON) {
				Thread.sleep(this.msec);
			}

		} catch (MidiUnavailableException | InterruptedException e) {
		}
	};

	public Consumer<List<音>> 奏 = notes -> {
		SOU.accept(notes, NOTE_ON);
		SOU.accept(notes, NOTE_OFF);
	};
}
