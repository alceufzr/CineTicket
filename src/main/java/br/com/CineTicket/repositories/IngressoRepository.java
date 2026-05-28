package br.com.CineTicket.repositories;

import br.com.CineTicket.models.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Integer> {

}