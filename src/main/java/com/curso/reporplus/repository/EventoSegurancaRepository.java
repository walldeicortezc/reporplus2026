package com.curso.reporplus.repository;

import com.curso.reporplus.domain.EventoSeguranca;
import com.curso.reporplus.domain.TipoEventoSeguranca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoSegurancaRepository extends JpaRepository<EventoSeguranca, Long> {

    long countByTipo(TipoEventoSeguranca tipo);

    Page<EventoSeguranca> findAllByOrderByOcorridoEmDescIdDesc(Pageable pageable);

    Page<EventoSeguranca> findByTipoOrderByOcorridoEmDescIdDesc(
            TipoEventoSeguranca tipo,
            Pageable pageable);
}
