package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId")
    private Integer employeeId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "hireDate")
    private LocalDate hireDate = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;
}