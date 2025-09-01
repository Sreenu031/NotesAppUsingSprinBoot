
package com.srinu.myapp.controller;

import com.srinu.myapp.model.NoteDTO;
import com.srinu.myapp.model.Note;
import com.srinu.myapp.model.User;
import com.srinu.myapp.repo.NoteRepo;
import com.srinu.myapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;

    @Autowired
    public NoteController(NoteRepo noteRepo, UserRepo userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get all notes for logged-in user
    @GetMapping
    public ResponseEntity<List<com.srinu.myapp.model.NoteDTO>> getNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Note> notes = noteRepo.findByUserId(user.getId());

        List<com.srinu.myapp.model.NoteDTO> noteDTOs = notes.stream()
                .map(com.srinu.myapp.model.NoteDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(noteDTOs);
    }

    // ✅ Create a new note
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        note.setUser(user);
        Note savedNote = noteRepo.save(note);

        return ResponseEntity.ok(new NoteDTO(savedNote));
    }

    // ✅ Update existing note
    @PutMapping("/update")
    public ResponseEntity<NoteDTO> updateNote(@RequestBody Note note) {
        Note existing = noteRepo.findById(note.getId())
                .orElseThrow(() -> new RuntimeException("Note not found"));

        existing.setTitle(note.getTitle());
        existing.setDescription(note.getDescription());

        Note updatedNote = noteRepo.save(existing);
        return ResponseEntity.ok(new NoteDTO(updatedNote));
    }

    // ✅ Delete note by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Integer id) {
        noteRepo.deleteById(id);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
