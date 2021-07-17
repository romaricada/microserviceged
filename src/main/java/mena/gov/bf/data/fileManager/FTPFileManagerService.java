package mena.gov.bf.data.fileManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import mena.gov.bf.data.fileManager.model.FileInfo;
import mena.gov.bf.domain.Serveur;
import mena.gov.bf.model.DataFile;
import mena.gov.bf.service.DocumentService;
import mena.gov.bf.service.EntrepotService;
import mena.gov.bf.service.ServeurService;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.EntrepotDTO;

@Service
public class FTPFileManagerService {

    // private final Logger log =
    // LoggerFactory.getLogger(FTPFileManagerService.class);
    private final EntrepotService entrepotService;
    @Autowired
    DocumentService documentService;

    @Autowired
    private ServeurService serveurService;

    Path path;
    String chemin = null;
    String host = "";
    int port = 0;
    String user = "";
    String pswd = "";

    FTPClient client = new FTPClient();

    public FTPFileManagerService(EntrepotService entrepotService) {
        this.entrepotService = entrepotService;
    }

    private void serveurValues() {
        Serveur serveur = serveurService.getFirstServeur().get();
        host = serveur.getAdresse();
        port = serveur.getPort();
        user = serveur.getNomServeur();
        pswd = serveur.getMotPasse();
    }

    public String getPath(Long id) {

        // chemin = getAdressPath();
        chemin = "/archives/";
        DocumentDTO document = documentService.findOne(id).get();
        List<EntrepotDTO> entrepots = entrepotService.findArborecence(document.getEntrepotId());
        int n = 0;
        for (EntrepotDTO entrepotDTO : entrepots) {
            if (n == 0) {
                chemin = chemin + entrepotDTO.getLibelleLocal() + "/";
            }
            chemin = chemin + entrepotDTO.getLibelle() + "/";
            n++;
        }
        return chemin;
    }

    public String getPathByEntrepo(Long id) {

        // chemin = getAdressPath();
        chemin = "/archives/";
        List<EntrepotDTO> entrepots = entrepotService.findArborecence(id);
        int n = 0;
        for (EntrepotDTO entrepotDTO : entrepots) {
            if (n == 0) {
                chemin = chemin + entrepotDTO.getLibelleLocal() + "/";
            }
            chemin = chemin + entrepotDTO.getLibelle() + "/";
            n++;
        }
        return chemin;
    }

    public void init(Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    /**
     * 
     * @param file
     * @param id
     */
    @Transactional
    public void uploadFTP(MultipartFile file, Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        DocumentDTO documentDTO = documentService.findOne(id).get();
        try {
            boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                fileUploading(documentDTO);
                System.out.println("=============== Success login et le chemin est:" + chemin + " ================");
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            ftpDisconnect();
        }

    }

    public boolean ftpConnect() {
        try {
            serveurValues();
            client.connect(host, port);
            boolean success = client.login(user, pswd);
            return success;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public void ftpDisconnect() {
        try {
            if (client.isConnected()) {
                client.logout();
                client.disconnect();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void fileUploading(DocumentDTO documentDTO) {
        InputStream inputStream = null;
        System.out.println("============ DocumentDTO ===============");
        System.out.println(documentDTO);
        System.out.println("================== Fin DTO=====================");
        String path = getPath(documentDTO.getId());
        boolean success = ftpConnect();

        if (success) {
            try {
                success = client.changeWorkingDirectory(path);
                if (!success) {
                    client.makeDirectory(path);
                }
                if (documentDTO.getDataFile().getFile() != null) {
                    inputStream = new ByteArrayInputStream(documentDTO.getDataFile().getFile());
                    client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                    client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                    client.enterLocalPassiveMode();
                    client.storeFile(path + documentDTO.getCode() + "_" + documentDTO.getLibelle() + "."
                            + FilenameUtils.getExtension(documentDTO.getDataFile().getFileName()), inputStream);
                    System.out.println("======================== Document chargé avec success ==============");
                } else {
                    System.out.println("************* Le byte est null et la taille est:"
                            + documentDTO.getDataFile().getFile().length + " ***********************");
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
            ftpDisconnect();
        } else {
            System.out.println("===================== Echec de connexion ==================");
        }

    }

    @Transactional
    public void save(List<MultipartFile> files, Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        try {

            Boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                Boolean success2 = client.changeWorkingDirectory(chemin);
                if (!success2) {
                    client.makeDirectory(chemin);
                }
                for (MultipartFile file : files) {
                    DocumentDTO documentDTO = documentService.findOne(id).get();
                    fileUploading(documentDTO);
                }

            }

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        } finally {
            ftpDisconnect();
        }
    }

    @Transactional
    public Resource load(String filename, Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        try {
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteAll(Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Transactional
    public Stream<Path> loadAll(Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        try {
            return Files.walk(this.path, 1).filter(path -> !path.equals(this.path)).map(this.path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Transactional
    public List<DataFile> findAllFilesByEntrepot(Long id) {
        String chemin1 = getPathByEntrepo(id);
        List<DataFile> datas = new ArrayList<>();
        byte[] byteArray;
        try {
            Boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
                return null;
            } else {
                Boolean success2 = client.changeWorkingDirectory(chemin1);
                if (!success2) {
                    client.makeDirectory(chemin1);
                }
                FTPFile[] liste = client.listFiles();
                System.out.println("================ Nombre de fichiers " + liste.length + " ===============");
                for (int i = 0; i < liste.length; i++) {
                    // DocumentDTO documentDTO =
                    // documentService.findDocumentByCode(file.getName()).get();

                    System.out.println("=========== Nom du fichier= " + chemin1 + liste[i].getName() + " ==========");
                    client.enterLocalPassiveMode();
                    client.setFileType(FTPClient.BINARY_FILE_TYPE);
                    client.setRemoteVerificationEnabled(false);
                    InputStream inputStream = client.retrieveFileStream(chemin1 + liste[i].getName());
                    System.out.println("Valeur de InputStram: " + inputStream);
                    byteArray = new byte[4096];
                    try {
                        int in = inputStream.read(byteArray);
                        if (in != -1) {
                            DataFile dataFile = new DataFile();
                            dataFile.setFileName(liste[i].getName());
                            dataFile.setFile(byteArray);
                            datas.add(dataFile);
                        } else {
                            System.out.println("Aucun octe lu");
                        }
                        inputStream.close();
                        while (!client.completePendingCommand())
                            ;
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("======= Erreur de lecture de fichier =======");
                    }

                    // System.out.println(byteArray);
                }

                return datas;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            ftpDisconnect();
        }
    }

    @Transactional
    public List<DataFile> findAllFiles() {
        // String chemin1 = getPathByEntrepo(id);
        List<DataFile> datas = new ArrayList<>();
        byte[] byteArray;
        try {
            Boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
                return null;
            } else {
                /*
                 * Boolean success2 = client.changeWorkingDirectory(chemin1); if (!success2) {
                 * client.makeDirectory(chemin1); }
                 */
                FTPFile[] liste = client.listFiles();
                System.out.println("================ Nombre de fichiers " + liste.length + " ===============");
                for (int i = 0; i < liste.length; i++) {
                    // DocumentDTO documentDTO =
                    // documentService.findDocumentByCode(file.getName()).get();

                    System.out.println("=========== Nom du fichier= " + liste[i].getName() + " ==========");
                    client.enterLocalPassiveMode();
                    client.setFileType(FTPClient.BINARY_FILE_TYPE);
                    client.setRemoteVerificationEnabled(false);
                    InputStream inputStream = client.retrieveFileStream(liste[i].getName());
                    System.out.println("Valeur de InputStram: " + inputStream);
                    byteArray = new byte[4096];
                    try {
                        int in = inputStream.read(byteArray);
                        if (in != -1) {
                            DataFile dataFile = new DataFile();
                            dataFile.setFileName(liste[i].getName());
                            dataFile.setFile(byteArray);
                            datas.add(dataFile);
                        } else {
                            System.out.println("Aucun octe lu");
                        }
                        inputStream.close();
                        while (!client.completePendingCommand())
                            ;
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("======= Erreur de lecture de fichier =======");
                    }

                    // System.out.println(byteArray);
                }

                return datas;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            ftpDisconnect();
        }
    }

    @Transactional
    public File downloadFTPFile(HttpServletResponse response, Long id) {
        String chemin1 = getPath(id);
        File file = null;
        try {
            Boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {

                String filename = getFileName(id);

                file = getFTPFileByte(chemin1 + filename, "./src/main/resources/documents/" + filename);

                System.out.println("============= Debut du fichier ==================");
                System.out.println(file);
                System.out.println("=============   Fin du fichier   ===============");
                ftpDisconnect();
                FileUtils.deleteQuietly(file);

            }
            return file;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private File getFTPFileByte(String remote, String local) throws IOException {
        File downloadFile2 = new File(local);
        OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
        InputStream inputStream = client.retrieveFileStream(remote);
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            outputStream2.write(bytesArray, 0, bytesRead);
        }
        Boolean success = client.completePendingCommand();
        if (success) {
            System.out.println("File #2 has been downloaded successfully.");
        }
        outputStream2.close();
        inputStream.close();
        System.out.println("==============================");
        System.out.println(bytesArray.length);
        System.out.println("==============================");
        return downloadFile2;
    }

    private String getFileName(Long id) {
        // String filename = null;
        String chem = getPath(id);
        DocumentDTO documentDTO = documentService.findOne(id).get();
        FTPFileFilter filter = new FTPFileFilter() {
            @Override
            public boolean accept(FTPFile ftpFile) {
                return (ftpFile.isFile()
                        && ftpFile.getName().startsWith(documentDTO.getCode() + "_" + documentDTO.getLibelle()));
            }
        };
        System.out.println("================== Début de la recherche du fichier ========================");
        try {
            FTPFile[] liste = client.listFiles(chem, filter);
            System.out.println("================================");
            System.out.println(liste[0].getName());
            System.out.println("================================");
            return liste[0].getName();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    private FTPFile getFile(Long id) {
        // String filename = null;
        String chem = getPath(id);
        DocumentDTO documentDTO = documentService.findOne(id).get();
        FTPFileFilter filter = new FTPFileFilter() {
            @Override
            public boolean accept(FTPFile ftpFile) {
                return (ftpFile.isFile()
                        && ftpFile.getName().startsWith(documentDTO.getCode() + "_" + documentDTO.getLibelle()));
            }
        };
        try {
            FTPFile[] liste = client.listFiles(chem, filter);
            return liste[0];
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public void deleteFile(Long id, boolean success) {
        String chemin1 = getPath(id);
        try {
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                String filename = getFileName(id);
                success = client.deleteFile(chemin1 + filename);
                if (success)
                    System.out.println("Fichier '" + filename + "' Supprimé avec succès...");
                else
                    System.out.println("File '" + filename + "' doesn't exist...");
                System.out.println("=============   Fin du fichier   ===============");
                ftpDisconnect();

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public DataFile getDocumentDataFile(Long id, boolean success) {
        DataFile dataFile = new DataFile();
        String chemin1 = getPath(id);
        byte[] byteArray;
        try {
            // Boolean success = ftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                FTPFile file = getFile(id);
                MimetypesFileTypeMap contenType = new MimetypesFileTypeMap();
                client.enterLocalPassiveMode();
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.setRemoteVerificationEnabled(false);
                InputStream inputStream = client.retrieveFileStream(chemin1 + file.getName());
                byteArray = IOUtils.toByteArray(inputStream);
                try {
                    if (byteArray.length > 0) {
                        dataFile = new DataFile();
                        dataFile.setFileName(file.getName());
                        dataFile.setFile(byteArray);
                        dataFile.setFileContentType(contenType.getContentType(file.getName()));
                    } else {
                        System.out.println("Aucun octe lu");
                    }
                    inputStream.close();
                    while (!client.completePendingCommand())
                        ;
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("======= Erreur de lecture de fichier =======");
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dataFile;
    }

    public InputStream getInputStream(String source) {
        InputStream inputStream = null;
        try {
            inputStream = this.client.retrieveFileStream(source);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(
                    "Failed to obtain InputStream for remote file " + source + ": " + this.client.getReplyCode());

        }

        return inputStream;
    }

}
