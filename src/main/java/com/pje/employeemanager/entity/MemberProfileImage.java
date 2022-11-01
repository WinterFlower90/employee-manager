package com.pje.employeemanager.entity;

import com.pje.employeemanager.interfaces.CommonModelBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    // 이미지 이름.
    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private LocalDateTime dateUpload;

    public void putImage(String imageName) {
        this.imageName = imageName;
        this.dateUpload = LocalDateTime.now();
    }

    private MemberProfileImage(MemberProfileImageBuilder builder) {
        this.memberId = builder.memberId;
        this.imageName = builder.imageName;
        this.dateUpload = builder.dateUpload;
    }

    public static class MemberProfileImageBuilder implements CommonModelBuilder<MemberProfileImage> {
        private final Long memberId;
        private final String imageName;
        private final LocalDateTime dateUpload;

        public MemberProfileImageBuilder(Long memberId, String imageName) {
            this.memberId = memberId;
            this.imageName = imageName;
            this.dateUpload = LocalDateTime.now();
        }

        @Override
        public MemberProfileImage build() {
            return new MemberProfileImage(this);
        }
    }
}
