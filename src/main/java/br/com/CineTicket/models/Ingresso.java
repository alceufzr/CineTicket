package br.com.CineTicket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingresso")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingresso")
    private Integer idIngresso;

    @ManyToOne
    @JoinColumn(name = "id_sessao", nullable = false)
    private Sessao sessao;

    @ManyToOne
    @JoinColumn(name = "id_item_compra", nullable = false)
    private ItemCompra itemCompra;

    @Column(name = "tipo_ingreso", nullable = false, length = 50)
    private String tipoIngresso;
}