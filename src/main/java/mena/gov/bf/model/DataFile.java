package mena.gov.bf.model;

import java.util.Arrays;

import javax.persistence.Lob;

public class DataFile {
    private String fileName;
    @Lob
    private byte[] file;

    private String fileContentType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    @Override
    public String toString() {
        return "DataFile [file=" + Arrays.toString(file) + ", fileContentType=" + fileContentType + ", fileName="
                + fileName + "]";
    }

}
