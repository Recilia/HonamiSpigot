package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;

public interface NoteBlock extends BlockState {

	public Note getNote();

	@Deprecated
	public byte getRawNote();

	public void setNote(Note note);

	@Deprecated
	public void setRawNote(byte note);

	public boolean play();

	@Deprecated
	public boolean play(byte instrument, byte note);

	public boolean play(Instrument instrument, Note note);
}
