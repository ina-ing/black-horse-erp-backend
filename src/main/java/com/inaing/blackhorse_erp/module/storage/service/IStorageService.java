package com.inaing.blackhorse_erp.module.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

    String upload(MultipartFile file, String folder);

    void deleteByUrl(String url);
}
