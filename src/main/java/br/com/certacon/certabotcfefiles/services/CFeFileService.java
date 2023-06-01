package br.com.certacon.certabotcfefiles.services;

import br.com.certacon.certabotcfefiles.models.CFeFileModel;
import br.com.certacon.certabotcfefiles.repositories.CFeFileRepository;
import br.com.certacon.certabotcfefiles.utils.CFeStatus;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class CFeFileService {
    private final CFeFileRepository fileRepository;

    public CFeFileService(CFeFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public CFeFileModel saveOrUpdate(CFeFileModel cFeFileModel) {
        validateNfeFile(cFeFileModel);
        Optional<CFeFileModel> existingFile = fileRepository.findByFileName(cFeFileModel.getFileName());
        if (existingFile.isPresent()) {
            CFeFileModel updatedFile = updateFile(existingFile.get(), cFeFileModel);
            return fileRepository.save(updatedFile);
        } else {
            cFeFileModel.setCreatedAt(new Date());
            cFeFileModel.setStatus(CFeStatus.CREATED);
            return fileRepository.save(cFeFileModel);
        }
    }

    private CFeFileModel updateFile(CFeFileModel existingFile, CFeFileModel updatedFile) {
        existingFile.setFileName(updatedFile.getFileName());
        existingFile.setStatus(CFeStatus.UPDATED);
        existingFile.setCreatedAt(updatedFile.getCreatedAt());
        existingFile.setUpdatedAt(new Date());
        return existingFile;
    }

    private void validateNfeFile(CFeFileModel fileModel) {
        if (fileModel.getFileName() == null || fileModel.getFileName().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
    }

    public Boolean deleteFile(UUID id) {
        Optional<CFeFileModel> modelOptional = fileRepository.findById(id);
        if (!modelOptional.isPresent()) {
            throw new NotFoundException("Arquivo não encontrado");
        } else {
            fileRepository.delete(modelOptional.get());
            return Boolean.TRUE;
        }
    }
}

