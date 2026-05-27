package br.com.CineTicket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "funcionario")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Integer idFuncionario;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Column(nullable = false, length = 100)
    private String nome;

    private Double comissao;
    private Boolean ativo;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false, length = 60)
    private String senha;
}