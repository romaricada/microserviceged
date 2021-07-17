package mena.gov.bf.data.fileManager;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mena.gov.bf.service.ServeurService;

@RestController
@RequestMapping("/api")
public class FileManagerResource {

    private static final Logger log = LoggerFactory.getLogger(FileManagerResource.class);
    @Autowired
    private ServletContext servletContext;

    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    SFTPFileManagerService sftpManagerService;

    @Autowired
    private ServeurService serveurService;

    

}
