package ebrain.board.utils;

import ebrain.board.dto.AttachmentDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * 파일 업로드 및 다운로드에 사용되는 유틸리티 클래스입니다.
 */
public class FileUtil {
    /**
     * 파일을 업로드합니다.
     *
     * @param file        업로드할 파일
     * @param uploadPath  파일을 업로드할 경로
     * @return            업로드된 파일 객체
     * @throws Exception  예외 발생 시
     */
    public static File uploadFile(MultipartFile file, String uploadPath) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(originalFileName);
        String extension = FilenameUtils.getExtension(originalFileName);

        // 파일 크기 제한: 2MB
        long maxSize = 2 * 1024 * 1024; // 2MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기는 최대 2MB까지 업로드 가능합니다.");
        }

        // 확장자 제한: jpg, gif, png, zip
        List<String> allowedExtensions = Arrays.asList("jpg", "gif", "png", "zip");
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("jpg, gif, png, zip 형식의 파일만 업로드 가능합니다.");
        }

        // 중복 파일명 처리합니다.
        File uploadedFile = new File(uploadPath + File.separator + originalFileName);

        int count = 1;
        while (uploadedFile.exists()) {
            // 중복 파일명에 번호 추가합니다.
            String numberedFileName = baseName + "_" + count + "." + extension;
            uploadedFile = new File(uploadPath + File.separator + numberedFileName);
            count++;
        }

        //파일을 업로드 폴더로 업로드합니다.
        file.transferTo(uploadedFile);
        //파일 고유 식별번호를 반환합니다.
        return uploadedFile;
    }
    /**
     * 파일을 다운로드합니다.
     *
     * @param attachment   다운로드할 파일 정보
     * @param uploadPath   파일이 업로드된 경로
     * @return             파일 다운로드 응답 객체
     * @throws IOException 예외 발생 시
     */
    public static ResponseEntity<Resource> fileDownload(AttachmentDTO attachment, String uploadPath) throws IOException {
        String filePath = uploadPath + File.separator + attachment.getFileName();
        File file = new File(filePath);

        // 파일이 존재하는지 확인합니다.
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // 파일 다운로드를 위한 Response Header를 설정합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getFileName());

        // 파일의 MIME 타입을 설정합니다.
        String contentType = Files.probeContentType(file.toPath());
        headers.setContentType(MediaType.parseMediaType(contentType));

        // 파일의 크기를 설정합니다.
        long contentLength = file.length();
        headers.setContentLength(contentLength);
        // 파일 다운로드 응답을 생성하여 반환합니다.
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }


    public static File uploadImage(MultipartFile file, String uploadPath) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(originalFileName);
        String extension = FilenameUtils.getExtension(originalFileName);


        // 파일 크기 제한: 1MB

        long maxSize = 1 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("이미지 크기는 최대 1MB까지 업로드 가능합니다.");
        }

        // 확장자 제한: jpg, gif, png
        List<String> allowedExtensions = Arrays.asList("jpg", "gif", "png");
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("jpg, gif, png");
        }

        // 중복 파일명 처리합니다.
        File uploadedFile = new File(uploadPath + File.separator + originalFileName);

        int count = 1;
        while (uploadedFile.exists()) {
            // 중복 파일명에 번호 추가합니다.
            String numberedFileName = baseName + "_" + count + "." + extension;
            uploadedFile = new File(uploadPath + File.separator + numberedFileName);
            count++;
        }

        //파일을 업로드 폴더로 업로드합니다.
        file.transferTo(uploadedFile);
        //파일 고유 식별번호를 반환합니다.
        return uploadedFile;
    }



}
