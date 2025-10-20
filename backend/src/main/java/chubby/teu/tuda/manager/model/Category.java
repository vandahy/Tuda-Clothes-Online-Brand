package chubby.teu.tuda.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "categoryCode", length = 10)  // bắt buộc map đúng với DB
    private String categoryCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Status status = Status.ACTIVE;

    // Hibernate không insert/update, DB tự động set
    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    private Timestamp updatedAt;

    public enum Status { ACTIVE, INACTIVE }
}
