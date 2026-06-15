package entities;

import java.time.LocalTime;

public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalTime createdAt;

    // constructor
    public Base(Long id, boolean eliminado, LocalTime createdat) {
        this.id = id;
        this.eliminado = eliminado;
        this.createdAt = createdat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public abstract String toString();

}
