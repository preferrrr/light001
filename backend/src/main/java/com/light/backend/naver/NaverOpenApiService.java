package com.light.backend.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOpenApiService {

    private final NaverOpenApiCaller naverOpenApiCaller;

    public boolean checkIsErrorSlot(String keyword, String originMid) {

        String response = naverOpenApiCaller.call(keyword);

        return response.contains(originMid);
    }
}
