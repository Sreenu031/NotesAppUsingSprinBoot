package com.srinu.myapp.repo;

import com.srinu.myapp.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note,Integer> {
    List<Note> findByUserId(Integer userId);
}
