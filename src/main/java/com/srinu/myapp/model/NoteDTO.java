package com.srinu.myapp.model;

import com.srinu.myapp.model.Note;

public class NoteDTO {
    private Integer id;
    private String title;
    private String description;

    // Constructor from Note entity
    public NoteDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.description = note.getDescription();
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
