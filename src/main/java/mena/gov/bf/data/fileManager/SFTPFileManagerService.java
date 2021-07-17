package mena.gov.bf.data.fileManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.activation.MimetypesFileTypeMap;
import javax.transaction.Transactional;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mena.gov.bf.domain.Document;
import mena.gov.bf.domain.Serveur;
import mena.gov.bf.model.DataFile;
import mena.gov.bf.repository.DocumentRepository;
import mena.gov.bf.service.DocumentService;
import mena.gov.bf.service.EntrepotService;
import mena.gov.bf.service.ServeurService;
import mena.gov.bf.service.dto.DocumentDTO;
import mena.gov.bf.service.dto.EntrepotDTO;

@Service
public class SFTPFileManagerService {

    private final Logger log = LoggerFactory.getLogger(SFTPFileManagerService.class);
    private final EntrepotService entrepotService;
    private final DocumentService documentService;
    @Autowired
    private ServeurService serveurService;
    @Autowired
    private DocumentRepository documentRepository;

    Path path;
    String chemin = null;
    String host = "";
    int port = 0;
    String user = "";
    String pswd = "";

    JSch ssh = new JSch();
    Session session;
    ChannelSftp sftp;

    public SFTPFileManagerService(EntrepotService entrepotService, DocumentService documentService) {
        this.entrepotService = entrepotService;
        this.documentService = documentService;
    }

    private void setServeurValues() {
        Serveur serveur = serveurService.getFirstServeur().get();
        host = serveur.getAdresse();
        port = serveur.getPort();
        user = serveur.getNomServeur();
        pswd = serveur.getMotPasse();
        System.out.println("Informations Serveur: adresse=" + host + ", port=" + port + ", user=" + user);
    }

    public boolean sftpConnect() {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        setServeurValues();
        /*
         * host = "192.162.71.76"; port = 22; user = "odk"; pswd = "O2kp@$$wd";
         */
        try {
            session = ssh.getSession(user, host, port);
            session.setConfig(config);
            session.setPassword(pswd);
            session.setTimeout(600000);
            session.connect();

            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            System.out.println("========== Connection success =============");
            return true;
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public void sftDisconnect() {
        sftp.disconnect();
        session.disconnect();
    }

    @Transactional
    public void sftpConnexion() {
        boolean success = sftpConnect();
        if (success)
            System.out.println("============= Connexion effectuée avec success ========");
        else
            System.out.println("=============== Echec de connexion =================");
    }

    @Transactional
    public void returnChemin(Long id) {
        System.out.println("================ " + getPath(id) + " ======================");
    }

    public void fileUploading(DocumentDTO documentDTO) {
        boolean success = sftpConnect();
        InputStream inputStream = null;
        System.out.println("============ DocumentDTO ===============");
        System.out.println(documentDTO);
        System.out.println("================== Fin DTO=====================");

        if (success) {

            String filePath = createDirectory(documentDTO.getId());

            if (documentDTO.getDataFile().getFile() != null) {
                inputStream = new ByteArrayInputStream(documentDTO.getDataFile().getFile());
                try {

                    sftp.put(inputStream, filePath + documentDTO.getCode() + "_" + documentDTO.getLibelle() + "."
                            + FilenameUtils.getExtension(documentDTO.getDataFile().getFileName()));
                    System.out.println("======================== Document chargé avec success ==============");
                } catch (SftpException e) {
                    // TODO Auto-generated catch block
                    log.debug("================= Problème de SFTP =====================");
                    e.printStackTrace();
                }
            } else {
                System.out.println("************* Le byte est null ***********************");
            }

            // fichier.delete();

        }
    }

    @Transactional
    public void uploadFTP(MultipartFile file, Long id) {
        String chemin = getPath(id);
        path = Paths.get(chemin);
        DocumentDTO documentDTO = documentService.findOne(id).get();
        try {
            boolean success = sftpConnect();
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                fileUploading(documentDTO);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            sftDisconnect();
        }

    }

    @Transactional
    public List<DataFile> findAllFilesByEntrepot(Long id) {
        // String chemin1 = getPathByEntrepo(id);
        List<DataFile> datas = new ArrayList<>();
        String currentDirectory = null;
        byte[] byteArray;

        try {
            boolean success = sftpConnect();
            try {
                currentDirectory = sftp.pwd();
            } catch (SftpException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String chemin1 = currentDirectory + getPathByEntrepo(id);
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                System.out.println("====================== Début ===================");
                Vector filelist = sftp.ls(chemin1);
                System.out.println("============== Nombre de fichiers= " + filelist.size() + " ======================");
                for (int i = 0; i < filelist.size(); i++) {
                    LsEntry entry = (LsEntry) filelist.get(i);
                    if (!entry.getAttrs().isDir()) {
                        InputStream inputStream = sftp.get(chemin1 + entry.getFilename());
                        byteArray = new byte[4096];
                        try {
                            int in = inputStream.read(byteArray);
                            if (in != -1) {
                                DataFile dataFile = new DataFile();
                                dataFile.setFileName(entry.getFilename());
                                dataFile.setFile(byteArray);
                                datas.add(dataFile);
                                System.out.println("===========================");
                                System.out.println("Data File: " + dataFile);
                                System.out.println("===========================");
                            } else {
                                System.out.println("Aucun octe lu");
                            }
                            inputStream.close();
                            // while (!sftp.completePendingCommand())
                            ;
                        } catch (Exception e) {
                            // TODO: handle exception
                            System.out.println("======= Erreur de lecture de fichier =======");
                        }
                    }

                }
                System.out.println("=================== Fin =================");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return datas;
    }

    @Transactional
    public List<DataFile> findAllFiles() {
        // String chemin1 = getPathByEntrepo(id);
        List<DataFile> datas = new ArrayList<>();
        String currentDirectory = null;
        byte[] byteArray;

        try {
            boolean success = sftpConnect();
            try {
                currentDirectory = sftp.pwd();
            } catch (SftpException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String chemin1 = currentDirectory;
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                System.out.println("====================== Début ===================");
                Vector filelist = sftp.ls(chemin1);
                for (int i = 0; i < filelist.size(); i++) {
                    LsEntry entry = (LsEntry) filelist.get(i);
                    if (!entry.getAttrs().isDir()) {
                        InputStream inputStream = sftp.get(chemin1 + entry.getFilename());
                        byteArray = new byte[4096];
                        try {
                            int in = inputStream.read(byteArray);
                            if (in != -1) {
                                DataFile dataFile = new DataFile();
                                dataFile.setFileName(entry.getFilename());
                                dataFile.setFile(byteArray);
                                datas.add(dataFile);
                            } else {
                                System.out.println("Aucun octe lu");
                            }
                            inputStream.close();
                            // while (!sftp.completePendingCommand())
                            ;
                        } catch (Exception e) {
                            // TODO: handle exception
                            System.out.println("======= Erreur de lecture de fichier =======");
                        }
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            sftDisconnect();
        }
        return datas;

    }

    @Transactional
    public DataFile getDocumentDataFile(Long id, boolean success) {
        DataFile dataFile = new DataFile();
        String currentDirectory = null;
        String path = getPath(id);
        byte[] byteArray;
        DocumentDTO documentDTO = documentService.findOne(id).get();
        try {
            try {
                currentDirectory = sftp.pwd();
            } catch (SftpException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String chemin1 = currentDirectory + path;
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {

                LsEntry entry = getFile(id, currentDirectory);
                MimetypesFileTypeMap contenType = new MimetypesFileTypeMap();
                InputStream inputStream = sftp.get(chemin1 + entry.getFilename());
                byteArray = IOUtils.toByteArray(inputStream);
                log.debug("==byteArray : {}", byteArray.length);
                try {
                    if (byteArray.length > 0) {
                        dataFile = new DataFile();
                        dataFile.setFileName(entry.getFilename());
                        dataFile.setFile(byteArray);
                        log.debug("mimeType: {}", contenType.getContentType(entry.getFilename()));
                        dataFile.setFileContentType(contenType.getContentType(entry.getFilename()));

                    } else {
                        System.out.println("Aucun octe lu");
                    }
                    inputStream.close();
                    // while (!sftp.completePendingCommand())
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

    public void deleteFile(Long id, boolean success) {
        String currentDirectory = null;
        String path = getPath(id);
        try {
            try {
                currentDirectory = sftp.pwd();
            } catch (SftpException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String chemin1 = currentDirectory;
            if (!success) {
                System.out.println("============== Could not login to the server ===============");
            } else {
                LsEntry entry = getFile(id, currentDirectory);
                sftp.rm(currentDirectory + path + entry.getFilename());
                System.out.println("Fichier '" + entry.getFilename() + "' Supprimé avec succès...");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("=============== Echec de suppression de document ================");
        }
    }

    private LsEntry getFile(Long id, String currentDirectory) {
        // String filename = null;
        String chem = getPath(id);
        DocumentDTO documentDTO = documentService.findOne(id).get();
        String chemin1 = currentDirectory + chem;
        LsEntry entry = null;

        try {
            Vector filelist = sftp.ls(chemin1);
            for (int i = 0; i < filelist.size(); i++) {
                entry = (LsEntry) filelist.get(i);
                if (!entry.getAttrs().isDir()
                        && entry.getFilename().startsWith(documentDTO.getCode() + "_" + documentDTO.getLibelle()))
                    break;
            }
            return entry;
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    private String getPath(Long id) {

        chemin = "/archives/";
        Document document = documentRepository.findById(id).get();
        List<EntrepotDTO> entrepots = entrepotService.findArborecence(document.getEntrepot().getId());
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

    private String getPathByEntrepo(Long id) {

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

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        file.transferTo(convFile);
        System.out.println("======================");
        System.out.println(convFile.getPath());
        System.out.println("======================");
        return convFile;
    }

    private String createDirectory(Long id) {
        String currentDirectory = null;
        try {
            currentDirectory = sftp.pwd();
        } catch (SftpException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String path = currentDirectory + getPath(id);

        try {
            String[] folders = path.split("/");
            if (folders[0].isEmpty())
                folders[0] = "/";
            String fullPath = folders[0];
            for (int i = 1; i < folders.length; i++) {
                Vector ls = sftp.ls(fullPath);
                boolean isExist = false;
                for (Object o : ls) {
                    if (o instanceof LsEntry) {
                        LsEntry e = (LsEntry) o;
                        if (e.getAttrs().isDir() && e.getFilename().equals(folders[i])) {
                            isExist = true;
                        }
                    }
                }
                if (!isExist && !folders[i].isEmpty()) {
                    sftp.mkdir(fullPath + folders[i]);
                }
                fullPath = fullPath + folders[i] + "/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
