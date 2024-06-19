package com.light.backend.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOpenApiService {

    private final NaverOpenApiCaller naverOpenApiCaller;

    private static String PREFIX = "\"productId\":\"";
    private static String POSTFIX = "\"";

    public boolean checkIsErrorSlot(String keyword, String originMid, String mid) {
        String response = naverOpenApiCaller.call(keyword);

        return findMid(response, originMid, mid);
    }

    private boolean findMid(String response, String originMid, String mid) {

        int count = 0;

        String midKeyword = PREFIX + mid + POSTFIX;
        String originMidKeyword = PREFIX + originMid + POSTFIX;

        for (int i = 0; i < response.length() - PREFIX.length(); i++) {

            if (response.substring(i, i + PREFIX.length()).equals(PREFIX)) {

                count++;

                if (count >= 4)
                    return false;
                else if (response.substring(i, i + midKeyword.length()).equals(midKeyword) ||
                        response.substring(i, i + originMidKeyword.length()).equals(originMidKeyword)) {
                    return true;
                }
            }


        }

        return false;
    }
}
