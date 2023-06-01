package br.com.certacon.certabotcfefiles.repositories;

import br.com.certacon.certabotcfefiles.models.CFeFileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CFeFileRepository extends JpaRepository<CFeFileModel, UUID> {
    Optional<CFeFileModel> findByFileName(String fileName);
}
