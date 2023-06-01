package br.com.certacon.certabotcfefiles.schedules;

import br.com.certacon.certabotcfefiles.configurations.RemoteWebDriverConfig;
import br.com.certacon.certabotcfefiles.helpers.CarachterManipulationHelper;
import br.com.certacon.certabotcfefiles.helpers.FileManipulationHelper;
import br.com.certacon.certabotcfefiles.models.ProcessFileModel;
import br.com.certacon.certabotcfefiles.pages.CertaconHomePage;
import br.com.certacon.certabotcfefiles.pages.DownloadFilePage;
import br.com.certacon.certabotcfefiles.pages.LoginCertaconWebPage;
import br.com.certacon.certabotcfefiles.pages.UploadFilesPage;
import br.com.certacon.certabotcfefiles.repositories.ProcessFileRepository;
import br.com.certacon.certabotcfefiles.utils.ProcessStatus;
import br.com.certacon.certabotcfefiles.vos.DownloadFileVO;
import br.com.certacon.certabotcfefiles.vos.ProcessFileForLoginVO;
import br.com.certacon.certabotcfefiles.vos.ProcessFileForSearchVO;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class SeleniumPagesSchedule {
    private final CarachterManipulationHelper carachterManipulationHelper;
    private final FileManipulationHelper fileManipulationHelper;
    private final DownloadFilePage downloadFilePage;
    private final LoginCertaconWebPage loginPage;
    private final CertaconHomePage homePage;
    private final UploadFilesPage uploadPage;
    private final ProcessFileRepository processFileRepository;
    @Value("${config.ip}")
    private String ip;

    public SeleniumPagesSchedule(CarachterManipulationHelper carachterManipulationHelper, FileManipulationHelper fileManipulationHelper, DownloadFilePage downloadFilePage, LoginCertaconWebPage loginPage, CertaconHomePage homePage, UploadFilesPage filesPage, ProcessFileRepository processFileRepository) {
        this.carachterManipulationHelper = carachterManipulationHelper;
        this.fileManipulationHelper = fileManipulationHelper;
        this.downloadFilePage = downloadFilePage;
        this.loginPage = loginPage;
        this.homePage = homePage;
        this.uploadPage = filesPage;
        this.processFileRepository = processFileRepository;
    }

    @Scheduled(fixedRate = 30000)
    public boolean pagesSchedule() throws MalformedURLException {
        List<ProcessFileModel> cfeFilesList = processFileRepository.findAll();

        if (!cfeFilesList.isEmpty()) {
            RemoteWebDriverConfig config = new RemoteWebDriverConfig();
            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(ip + ":4444/wd/hub"), config.chromeOptions());

            cfeFilesList.forEach(processFileModel -> {
                if (processFileModel.getStatus() == ProcessStatus.CREATED || processFileModel.getStatus() == ProcessStatus.PROXY_ERROR) {
                    try {
                        DownloadFileVO downloadFileVO = DownloadFileVO.builder()
                                .remoteDriverDownload(processFileModel.getRemoteDriverDownload())
                                .fileName(processFileModel.getFileName())
                                .build();
                        ProcessStatus status = downloadFilePage.downloadFiles(downloadFileVO, remoteWebDriver);
                        if (status.equals(ProcessStatus.DOWNLOADED)) {
                            ProcessFileForLoginVO processLoginVO = ProcessFileForLoginVO.builder()
                                    .id(processFileModel.getId())
                                    .remoteDriverUpload(processFileModel.getRemoteDriverUpload())
                                    .username(processFileModel.getUsername())
                                    .password(processFileModel.getPassword())
                                    .build();
                            processFileModel = loginPage.loginInput(processLoginVO, remoteWebDriver);
                            if (processFileModel.getStatus() == ProcessStatus.LOGGED) {
                                ProcessStatus homePageStatus = homePage.closeAndNavigate(remoteWebDriver);
                                processFileModel.setStatus(homePageStatus);
                                if (processFileModel.getStatus() == ProcessStatus.CHANGED) {
                                    ProcessFileForSearchVO processSearchVO = ProcessFileForSearchVO.builder()
                                            .cnpj(processFileModel.getCnpj())
                                            .nomeEmpresa(carachterManipulationHelper.replaceSpecialCharacters(processFileModel.getNomeEmpresa()))
                                            .idForSearch(processFileModel.getId())
                                            .pathToFile(processFileModel.getDownloadPath() + "/" + processFileModel.getFileName())
                                            .build();
                                    processFileModel = uploadPage.navigateOnUploadPage(processSearchVO, remoteWebDriver);
                                    processFileRepository.save(processFileModel);
                                }
                                if (processFileModel.getStatus() == ProcessStatus.OK) {
                                    ProcessStatus statusMove = fileManipulationHelper.moveFiles(processFileModel.getFilePath());
                                    processFileModel.setStatus(statusMove);
                                    processFileRepository.save(processFileModel);
                                }
                            }
                        }
                    } catch (RuntimeException e) {
                        processFileModel.setStatus(ProcessStatus.RUNTIME_ERROR);
                        throw new RuntimeException(e);

                    } catch (MalformedURLException e) {
                        processFileModel.setStatus(ProcessStatus.MALFORMULED_ERROR);
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        processFileModel.setStatus(ProcessStatus.INTERRUPTED_ERROR);
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        processFileModel.setStatus(ProcessStatus.IO_ERROR);
                        throw new RuntimeException(e);
                    } finally {
                        processFileRepository.save(processFileModel);
                        remoteWebDriver.quit();
                    }
                }
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
