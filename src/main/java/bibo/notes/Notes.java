package bibo.notes;

import java.util.ArrayList;
import java.util.stream.Collectors;

import bibo.exceptions.ListIndexException;
import bibo.exceptions.NoteFormatException;
import bibo.utils.InputParser;

/**
 * Represents a list of notes.
 */
public class Notes {
    private ArrayList<Note> notes;

    /**
     * Constructs a list of notes.
     */
    public Notes() {
        notes = new ArrayList<>();
        assert notes != null : "Notes object is null";
    }

    /**
     * Gets the number of notes in the list of notes.
     *
     * @return Number of notes in the list of notes.
     */
    public int getNotesSize() {
        return notes.size();
    }

    /**
     * Adds note to the list of notes.
     *
     * @param args Arguments for note description.
     * @return Note added to the list of notes.
     */
    public Note add(String args) throws NoteFormatException {
        Note note = null;

        try {
            String[] parsedDescription = InputParser.parseNoteDescription(args);
            note = new Note(parsedDescription);
        } catch (NoteFormatException e) {
            throw e;
        }

        notes.add(note);
        return note;
    }

    /**
     * Deletes note from the list of notes.
     *
     * @param index Index of note to be deleted.
     */
    public void delete(String index) throws ListIndexException {
        try {
            int noteIndex = Integer.parseInt(index) - 1;
            notes.remove(noteIndex);
        } catch (NumberFormatException e) {
            throw new ListIndexException(
                ListIndexException.ErrorType.INVALID_INDEX.toString()
            );
        } catch (IndexOutOfBoundsException e) {
            throw new ListIndexException(
                ListIndexException.ErrorType.INDEX_OUT_OF_BOUNDS.toString()
            );
        }
    }

    @Override
    public String toString() {
        if (notes.isEmpty()) {
            return "No notes found!";
        }

        return notes.stream()
                .map(note -> notes.indexOf(note) + 1 + ". " + note.toString())
                .collect(Collectors.joining("\n"));
    }
}
