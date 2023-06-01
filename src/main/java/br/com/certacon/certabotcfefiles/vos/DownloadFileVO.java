package br.com.certacon.certabotcfefiles.vos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class DownloadFileVO {
    private String remoteDriverDownload;
    private String fileName;
}
