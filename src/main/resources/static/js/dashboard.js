document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const addNoteBtn = document.getElementById('add-note-btn');
    const noteFormContainer = document.getElementById('note-form-container');
    const saveNoteBtn = document.getElementById('save-note-btn');
    const cancelNoteBtn = document.getElementById('cancel-note-btn');
    const noteTitleInput = document.getElementById('note-title');
    const noteDescriptionInput = document.getElementById('note-description');
    const notesContainer = document.getElementById('notes-container');
    const noNotesMessage = document.getElementById('no-notes-message');

    // Load notes when page loads
    loadNotes();

    // Event listeners
    addNoteBtn.addEventListener('click', toggleNoteForm);
    cancelNoteBtn.addEventListener('click', toggleNoteForm);
    saveNoteBtn.addEventListener('click', saveNote);

    // Functions
    function toggleNoteForm() {
        noteFormContainer.classList.toggle('hidden');

        if (!noteFormContainer.classList.contains('hidden')) {
            noteTitleInput.focus();
        } else {
            // Clear form when hiding
            noteTitleInput.value = '';
            noteDescriptionInput.value = '';
        }
    }

    function saveNote() {
        const title = noteTitleInput.value.trim();
        const description = noteDescriptionInput.value.trim();

        if (!title || !description) {
            showNotification('Please enter both title and description', 'error');
            return;
        }

        const noteData = {
            title: title,
            description: description
        };

        // Send POST request to save note
        fetch('/api/notes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(noteData),
            credentials: 'include'   // ✅ send JSESSIONID cookie
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to save note');
                }
                return response.json();
            })
            .then(data => {
                // Hide form after successful save
                toggleNoteForm();

                // Load all notes to update the view
                loadNotes();

                // Show success notification
                showNotification('Note saved successfully!', 'success');
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification(error.message, 'error');
            });
    }

    function loadNotes() {
        fetch('/api/notes', {
            method: 'GET',
            credentials: 'include'   // ✅ send JSESSIONID cookie
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load notes');
                }
                return response.json();
            })
            .then(notes => {
                displayNotes(notes);
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Error loading notes', 'error');
            });
    }

    function displayNotes(notes) {
        // Clear existing notes
        notesContainer.innerHTML = '';

        if (notes.length === 0) {
            // Show message when no notes
            notesContainer.appendChild(noNotesMessage);
            return;
        }

        // Sort notes by newest first (assuming notes have createdDate)
        notes.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate));

        // Create note cards
        notes.forEach(note => {
            const noteCard = createNoteCard(note);
            notesContainer.appendChild(noteCard);
        });
    }

    function createNoteCard(note) {
        // Format date
        const createdDate = new Date(note.createdDate);
        const formattedDate = createdDate.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });

        // Create note card element
        const noteCard = document.createElement('div');
        noteCard.classList.add('note-card');
        noteCard.dataset.noteId = note.id;

        noteCard.innerHTML = `
            <div class="note-card-header">
                <h3 class="note-title">${escapeHtml(note.title)}</h3>
                <div class="note-actions">
                    <button class="edit-note" title="Edit note">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="delete-note" title="Delete note">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <p class="note-description">${escapeHtml(note.description)}</p>
            <div class="note-footer">
                <span>${formattedDate}</span>
            </div>
        `;

        // Add event listeners for edit and delete buttons
        const editBtn = noteCard.querySelector('.edit-note');
        const deleteBtn = noteCard.querySelector('.delete-note');

        editBtn.addEventListener('click', () => editNote(note));
        deleteBtn.addEventListener('click', () => deleteNote(note.id));

        return noteCard;
    }

    function editNote(note) {
        // Populate form with note data
        noteTitleInput.value = note.title;
        noteDescriptionInput.value = note.description;

        // Show form
        noteFormContainer.classList.remove('hidden');

        // Update save button to handle update
        saveNoteBtn.removeEventListener('click', saveNote);
        saveNoteBtn.addEventListener('click', function updateHandler() {
            updateNote(note.id);
            saveNoteBtn.removeEventListener('click', updateHandler);
            saveNoteBtn.addEventListener('click', saveNote);
        });

        noteTitleInput.focus();
    }

    function updateNote(noteId) {
        const title = noteTitleInput.value.trim();
        const description = noteDescriptionInput.value.trim();

        if (!title || !description) {
            showNotification('Please enter both title and description', 'error');
            return;
        }

        const noteData = {
            id: noteId,
            title: title,
            description: description
        };

        fetch('/api/notes/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(noteData),
            credentials: 'include'   // ✅ send JSESSIONID cookie
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update note');
                }
                return response.json();
            })
            .then(data => {
                toggleNoteForm();
                loadNotes();
                showNotification('Note updated successfully!', 'success');
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification(error.message, 'error');
            });
    }

    function deleteNote(noteId) {
        if (confirm('Are you sure you want to delete this note?')) {
            fetch(`/api/notes/delete/${noteId}`, {
                method: 'DELETE',
                credentials: 'include'   // ✅ send JSESSIONID cookie
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete note');
                    }
                    return response.text();
                })
                .then(() => {
                    loadNotes();
                    showNotification('Note deleted successfully!', 'success');
                })
                .catch(error => {
                    console.error('Error:', error);
                    showNotification(error.message, 'error');
                });
        }
    }

    // Helper functions
    function escapeHtml(unsafe) {
        return unsafe
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    // Simple notification system
    function showNotification(message, type = 'success') {
        // Check if notification container exists, if not create it
        let notificationContainer = document.querySelector('.notification-container');

        if (!notificationContainer) {
            notificationContainer = document.createElement('div');
            notificationContainer.classList.add('notification-container');
            document.body.appendChild(notificationContainer);

            // Add styles for notifications
            const style = document.createElement('style');
            style.textContent = `
                .notification-container {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    z-index: 1000;
                }

                .notification {
                    padding: 12px 20px;
                    margin-bottom: 10px;
                    border-radius: 4px;
                    font-weight: 500;
                    display: flex;
                    align-items: center;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    animation: slideIn 0.3s ease, fadeOut 0.3s ease 2.7s forwards;
                    max-width: 300px;
                }

                .notification.success {
                    background-color: #10b981;
                    color: white;
                }

                .notification.error {
                    background-color: #ef4444;
                    color: white;
                }

                .notification i {
                    margin-right: 10px;
                }

                @keyframes slideIn {
                    from {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }

                @keyframes fadeOut {
                    from {
                        opacity: 1;
                    }
                    to {
                        opacity: 0;
                    }
                }
            `;
            document.head.appendChild(style);
        }

        // Create notification element
        const notification = document.createElement('div');
        notification.classList.add('notification', type);

        // Set icon based on notification type
        const icon = type === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-circle';

        notification.innerHTML = `<i class="${icon}"></i> ${message}`;
        notificationContainer.appendChild(notification);

        // Remove notification after 3 seconds
        setTimeout(() => {
            notification.remove();
        }, 3000);
    }
});
