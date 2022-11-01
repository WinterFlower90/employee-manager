package com.pje.employeemanager.model.member;


import com.pje.employeemanager.entity.MemberProfileImage;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageResponse {
    @ApiModelProperty(notes = "이미지 이름 (경로포함)")
    private String imageName;

    @ApiModelProperty(notes = "이미지 업로드 시간")
    private String dateUpload;

    private ProfileImageResponse(ProfileImageResponseBuilder builder) {
        this.imageName = builder.imageName;
        this.dateUpload = builder.dateUpload;
    }

    public static class ProfileImageResponseBuilder implements CommonModelBuilder<ProfileImageResponse> {
        private final String imageName;
        private final String dateUpload;

        public ProfileImageResponseBuilder(MemberProfileImage memberProfileImage) {
            // DB에는 이미지이름만 저장되어 있으므로 앞에 도메인 주소와 경로는 수기로 적어주어 앱에서 가져다 쓸 full 경로를 알려 줌.
            this.imageName = "http://192.168.0.21:8080/static/" + memberProfileImage.getImageName();
            this.dateUpload = memberProfileImage.getDateUpload().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        @Override
        public ProfileImageResponse build() {
            return new ProfileImageResponse(this);
        }
    }
}
