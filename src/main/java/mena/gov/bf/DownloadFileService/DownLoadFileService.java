package mena.gov.bf.DownloadFileService;

import mena.gov.bf.data.fileManager.FileManagerService;
import mena.gov.bf.model.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DownLoadFileService {
    /*private final Logger log = LoggerFactory.getLogger( DownLoadFileService.class );
    private final FileManagerService fileManagerService;

    public DownLoadFileService(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    public void uploadFiles(Long id,  List<DataFile> files) {
        if (!files.isEmpty()) {
            files.forEach( dataFile -> {
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream( this.createFile( id, dataFile.getFileName() ) );
                    outputStream.write( dataFile.getFile() );
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } );
        }
        this.exporteFile( id);
    }

    public void exporteFile(Long id) {
        List<DataFile> dataFiles = new ArrayList<>();
        Path path = Paths.get( this.fileManagerService.getPath( id ) );

            try {
                Files.list( path ).filter(Files::isRegularFile).forEach( file -> {
                    log.debug( "======================={}", file.getFileName().toString() );
                    DataFile dataFile = new DataFile();
                    dataFile.setFileName( file.getFileName().toString() );
                   // dataFile.setFile( file.toA );
                    dataFiles.add(dataFile);
                } );

                dataFiles.forEach( val-> {
                    log.debug( "======================={}", val.getFileName() );
                } );
            } catch (IOException e) {
                e.printStackTrace();
            }

        // return null;
    }

    private File createFile(Long id,  String fileName) throws IOException {
        Path path = Paths.get( fileManagerService.getPath( id ) );
        if (!Files.exists( path )) {
            Files.createDirectories( path );
            log.debug( "======== creation du repertoire ==========" );
        }
        return new File( path + "" + fileName );
    }*/
}
