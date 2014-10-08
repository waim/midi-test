package miditest;

public enum 音 {
	ど(60), れ(62), み(64), ふぁ(65), そ(67), ら(69), し(71);

	private int notenum;

	private 音(int notenum) {
		this.notenum = notenum;
	}

	public int getNotenum() {
		return this.notenum;
	}

}
