package br.com.CineTicket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Integer idAvaliacao;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_filme", nullable = false)
    private Filme filme;

    @Column(nullable = false)
    private BigDecimal notaAvaliacao;

    @Column(length = 500)
    private String comentario;

    @Column(name = "data_hora")
    private LocalDateTime dataCompra;
}