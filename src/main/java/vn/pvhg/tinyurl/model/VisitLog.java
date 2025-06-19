package vn.pvhg.tinyurl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tiny_url_id", nullable = false)
    private TinyUrl tinyUrl;

    @Column(nullable = false)
    private LocalDateTime visitTime;
}
