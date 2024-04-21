package com.alvonellos.bug.client;
import jakarta.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

@Service
@Log
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageReaderClient {

    public static final String DATA = "data:";
    public static final String BASE_64 = ";base64,";

    public static final MediaType DEFAULT_MEDIA_TYPE = MediaType.IMAGE_PNG;

    private final RestTemplate restTemplate;

    public BufferedImage getJpgImageFromUrl(String imageUrl) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));

        ResponseEntity<byte[]> responseEntity =
                restTemplate.exchange(imageUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        byte[] imageBytes = responseEntity.getBody();
        if (imageBytes == null) {
            return null;
        }

        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    public byte[] bufferedImageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return imageBytes;
    }

    public byte[] getBytesFromUrl(String url) throws IOException {
        return bufferedImageToBytes(getJpgImageFromUrl(url));
    }

    public String getBase64FromUrl(String url) throws IOException {
        return BASE_64 + Base64.getEncoder().encodeToString(getBytesFromUrl(url));
    }

    public static byte[] getBytesFromBase64(String base64) throws IOException {
        return DatatypeConverter.parseBase64Binary(
                base64.replace(BASE_64, "")
                        .replace(DATA, "")
                        .replace(MediaType.IMAGE_JPEG.toString(), "")
                        .replace(MediaType.IMAGE_PNG.toString(), "")
                );
    }

    public static String getBase64StringFromBytes(byte[] imageData) {
        return new StringBuilder("data:")
                .append(DEFAULT_MEDIA_TYPE.toString())
                .append(BASE_64)
                .append(
                        DatatypeConverter
                                .printBase64Binary(imageData))
                .toString();
    }

}

