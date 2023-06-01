package br.com.certacon.certabotcfefiles.vos;


import br.com.certacon.certabotcfefiles.models.ProcessFileModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class ProcessFileForLoginVO {
    private UUID id;
    private String username;

    private String password;

    private String remoteDriverUpload;

    public ProcessFileModel toModel() {
        return ProcessFileModel.builder()
                .id(id)
                .username(username)
                .password(password)
                .remoteDriverUpload(remoteDriverUpload)
                .build();
    }
}
