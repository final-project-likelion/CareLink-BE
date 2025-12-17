package com.carelink.backend.chat.service;

import com.carelink.backend.global.exception.BaseException;
import com.carelink.backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class SttService {

    @Value("${clova.stt.client.id}")
    private String sttClientId;

    @Value("${clova.stt.secret}")
    private String sttSecret;

    @Value("${clova.stt.url}")
    private String sttUrl;


    public String speechToText(MultipartFile file) {
        try {
            URL url = new URL(sttUrl);

            // HTTP 연결 설정
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", sttClientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", sttSecret);

            // 음성 파일을 바이트로 STT 서버에 전송
            try (OutputStream os = conn.getOutputStream(); InputStream is = file.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            InputStream responseStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
            if (responseCode == 413) throw new BaseException(ErrorCode.STT_TOO_LARGE);

            // 응답 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.toString());
            return node.get("text").asText();
        } catch (BaseException e) {
            throw new BaseException(ErrorCode.STT_TOO_LARGE, e.getMessage());
        }
        catch (Exception e) {
            log.error("STT 에러 - " + e.getMessage());
            throw new BaseException(ErrorCode.STT_API_ERROR);
        }
    }

}
