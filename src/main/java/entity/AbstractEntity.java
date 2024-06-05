package entity;

import java.time.LocalDateTime;

public abstract class AbstractEntity {
    protected long id;
    protected LocalDateTime createAt;

    public long getId() {
        return id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public AbstractEntity() {
    }
}
