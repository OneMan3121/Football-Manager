package ua.oneman.footballmanagerbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teams")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Назва команди

    @Column(nullable = false)
    private Double budget; // Бюджет команди

    @Column(name = "commission_percentage", nullable = false)
    private Double commissionPercentage; // Процент комісії (за трансфер або інші операції)

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Власник команди
    private User owner; // Зв'язок з користувачем

}