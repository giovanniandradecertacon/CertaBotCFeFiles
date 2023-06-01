package br.com.certacon.certabotcfefiles.repositories;

import br.com.certacon.certabotcfefiles.models.ProcessFileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessFileRepository extends JpaRepository<ProcessFileModel, UUID> {
    Optional<ProcessFileModel> findByFileName(String fileName);
}
