package bibo.notes;

/**
 * Represents a note.
 */
public class Note {
    private String title;
    private String content;

    /**
     * Constructs a note.
     */
    public Note() {
        this.title = "";
        this.content = "";
    }

    /**
     * Constructs a note with title and content.
     *
     * @param description
     */
    public Note(String[] description) {
        this.content = description[0];
        this.title = description[1];
    }

    @Override
    public String toString() {
        if (title.isEmpty()) {
            return content;
        } else {
            return title + ":\n" + content;
        }
    }
}
