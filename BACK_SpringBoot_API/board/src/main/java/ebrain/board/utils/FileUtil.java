package ebrain.board.utils;

import ebrain.board.dto.AttachmentDTO;
import org.apache.commons.io.FilenameUtils;
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

public class FileUtil {
    /**
     * 파일을 업로드하는 메서드
     *
     * @param file        업로드할 파일
     * @param uploadPath  파일 업로드 경로
     * @return 업로드된 파일의 이름
     * @throws Exception 파일 업로드 중 발생한 예외
     */
    //TODO : 이름만 넘겨주기보다 파일로 넘겨서 나중에 사용하기 용이하게 처리
    public static File uploadFile(MultipartFile file, String uploadPath) throws IOException {
        String fileName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(fileName);
        String extension = FilenameUtils.getExtension(fileName);

        // 중복 파일명 처리합니다.
        File uploadedFile = new File(uploadPath + File.separator + fileName);

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
     * 첨부 파일을 다운로드하기 위한 ResponseEntity를 생성합니다.
     *
     * @param attachment   다운로드할 첨부 파일 정보
     * @param UPLOAD_PATH  파일 업로드 경로
     * @return 파일 다운로드를 위한 ResponseEntity
     * @throws IOException IO 예외 발생 시
     */
    public static ResponseEntity<Resource> fileDownload(AttachmentDTO attachment, String UPLOAD_PATH) throws IOException {
        // 파일을 읽어올 InputStream을 생성합니다.
        String filePath = UPLOAD_PATH + File.separator + attachment.getFileName();
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


}
