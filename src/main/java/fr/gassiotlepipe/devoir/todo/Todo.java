package fr.gassiotlepipe.devoir.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Todo {
    @Id
    private long id;
    private String tache;
    private boolean statut;

    public Todo() {}

    public Todo(long id, String tache, boolean statut) {
        this.id = id;
        this.tache = tache;
        this.statut = statut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTache() {
        return tache;
    }

    public void setTache(String tache) {
        this.tache = tache;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }
}
