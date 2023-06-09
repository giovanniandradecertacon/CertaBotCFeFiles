package br.com.certacon.certabotcfefiles.models;


import br.com.certacon.certabotcfefiles.utils.ProcessStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProcessFileModel {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "arquivo_nfe_id")
    private UUID id;

    @Column(name = "processo")
    private String process;

    @Column(name = "servidor_Remoto_Download", nullable = false)
    @JsonProperty(required = true)
    private String remoteDriverDownload;

    @Column(name = "servidor_Remoto_Upload", nullable = false)
    @JsonProperty(required = true)
    private String remoteDriverUpload;

    @Column(name = "registros")
    private String Registers;

    @Column(name = "tempo")
    private String time;

    @Column(name = "porcentagem")
    private String percentage;

    @Column(nullable = false, name = "caminho_de_download")
    @JsonProperty(required = true)
    private String downloadPath;

    @Column(name = "usuario")
    private String username;

    @Column(name = "senha")
    private String password;

    @Column(nullable = false, name = "Caminho_de_arquivo")
    @JsonProperty(required = true)
    private String filePath;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessStatus status;

    @Column(name = "nome_arquivo", nullable = false)
    @JsonProperty(required = true)
    private String fileName;

    @Column(name = "criado_em", nullable = false)
    private Date createdAt;

    @Column(name = "atualizado_em")
    private Date updatedAt;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "arquivos_validos")
    private String valid;

    @Column(name = "arquivos_invalidos")
    private String invalid;

    @Column(name = "link_relatorio")
    private String reportLink;

    @OneToMany(mappedBy = "processFileModel")
    private List<CFeFileModel> arquivosCFeModel;

}
