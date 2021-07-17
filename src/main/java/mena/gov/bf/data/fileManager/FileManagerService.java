package mena.gov.bf.data.fileManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mena.gov.bf.domain.Serveur;
import mena.gov.bf.model.DataFile;
import mena.gov.bf.service.ServeurService;
import mena.gov.bf.service.dto.DocumentDTO;

@Service
public class FileManagerService {
    @Autowired
    private ServeurService serveurService;

    @Autowired
    FTPFileManagerService ftpManagerService;

    @Autowired
    SFTPFileManagerService sftpManagerService;

    public boolean connect() {
        boolean success = false;
        Serveur serveur = serveurService.getFirstServeur().get();
        if (serveur.getTypeServeur().name().equals("SFTP"))
            try {
                success = sftpManagerService.sftpConnect();
                System.out.println("================= Connection successful =================");
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("================= Connection Denied =================");
            }
        else if (serveur.getTypeServeur().name().equals("FTP"))
            try {
                success = ftpManagerService.ftpConnect();
                System.out.println("================= Connection successful =================");
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("================= Connection Denied =================");
            }
        return success;

    }

    public void disconnect() {
        Serveur serveur = serveurService.getFirstServeur().get();
        if (serveur.getTypeServeur().name().equals("SFTP")) {
            try {
                sftpManagerService.sftDisconnect();
                System.out.println("================= Connection close =================");
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        else if (serveur.getTypeServeur().name().equals("FTP")) {
            try {
                ftpManagerService.ftpDisconnect();
                System.out.println("================= Connection close =================");
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    public void fileUploading(DocumentDTO documentDTO) {
        Serveur serveur = serveurService.getFirstServeur().get();
        if (serveur.getTypeServeur().name().equals("SFTP"))
            sftpManagerService.fileUploading(documentDTO);
        else if (serveur.getTypeServeur().name().equals("FTP"))
            ftpManagerService.fileUploading(documentDTO);
    }

    public DataFile getDocumentDataFile(Long id, boolean success) {
        Serveur serveur = serveurService.getFirstServeur().get();
        DataFile dataFile = null;
        if (serveur.getTypeServeur().name().equals("SFTP")) {
            dataFile = sftpManagerService.getDocumentDataFile(id, success);
        } else if (serveur.getTypeServeur().name().equals("FTP")) {
            dataFile = ftpManagerService.getDocumentDataFile(id, success);
        }
        return dataFile;
    }

    public void deleteFile(Long id, boolean success) {
        Serveur serveur = serveurService.getFirstServeur().get();
        if (serveur.getTypeServeur().name().equals("SFTP")) {
            sftpManagerService.deleteFile(id, success);
        } else {
            ftpManagerService.deleteFile(id, success);
        }
    }

}
