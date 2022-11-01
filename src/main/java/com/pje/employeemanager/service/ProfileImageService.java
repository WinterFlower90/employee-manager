package com.pje.employeemanager.service;

import com.pje.employeemanager.entity.MemberProfileImage;
import com.pje.employeemanager.exception.CMissingDataException;
import com.pje.employeemanager.model.member.ProfileImageResponse;
import com.pje.employeemanager.repository.MemberProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final MemberProfileImageRepository memberProfileImageRepository;

    // 등록된 이미지 가져오기
    public ProfileImageResponse getImage(long memberId) {
        MemberProfileImage profileImage = memberProfileImageRepository.findByMemberId(memberId).orElseThrow(CMissingDataException::new);
        return new ProfileImageResponse.ProfileImageResponseBuilder(profileImage).build();
    }

    // 이미지 등록 메서드
    public void setImage(long memberId, MultipartFile file) {
        // 한 회원당 하나의 프로필사진만 등록 가능하게 하기 위해 optional 로 데이터를 가져옴.
        Optional<MemberProfileImage> profileImage = memberProfileImageRepository.findByMemberId(memberId);

        // MultipartFile을 업로드 하고 결과 File을 가져옴.
        File resultFile = convertFile(memberId, file);

        // 업로드된 실제 파일 이름을 가져옴.
        String resultFileName = resultFile.getName();

        // 등록, 수정을 같이 처리하는 메서드이기 때문에 공통적으로 리턴해주는 MemberProfileImage entity 모양의 빈 변수를 만듬.
        MemberProfileImage resultData;

        if (profileImage.isEmpty()) { // 회원이 처음 등록하는 프로필 사진이라면
            MemberProfileImage newData = new MemberProfileImage.MemberProfileImageBuilder(memberId, resultFileName).build();
            resultData = memberProfileImageRepository.save(newData);
        } else { // 회원이 프로필 사진을 두번째 이상 바꾸고 있다면
            MemberProfileImage oldData = profileImage.get();
            oldData.putImage(resultFileName);
            resultData = memberProfileImageRepository.save(oldData);
        }
    }

    /*
    MultipartFile 에서 File 자료형으로 변환하기 위한 메서드.
    MultipartFile 형태를 가진 파일을 받아서 File (우리가 알고있는 파일이라고 부른 것들) 형태로 만들어 줌.
    만듬과 동시에 C:/workspace/java/api-company-manager/src/main/resources/static/ 이 경로에 저장 함.
     */
    private File convertFile(long memberId, MultipartFile file) {
        // 저장될 경로
        String path = "C:/workspace/java/api-company-manager/src/main/resources/static/";

        // 파일의 확장자
        String[] ext = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

        // 저장될 파일 이름
        String resultFileName = memberId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "." + ext[ext.length - 1];

        // 빈 파일 만들 준비 함. (저장될 경로 + 저장될 파일 이름 으로 파일 만들 준비)
        File convFile = new File(path, resultFileName);

        try {
            // 빈 파일을 만듬
            convFile.createNewFile();

            // 파일을 만들기 위해 스트림을 준비함. (구글에 stream 이 무엇인지 검색해보기.)
            FileOutputStream fileOutputStream = new FileOutputStream(convFile);

            // 스트림으로 빈 파일에 주입 시작
            fileOutputStream.write(file.getBytes());

            // 주입이 끝나면 파일이 완성된 것이므로 스트림을 닫음.
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 완성된 파일을 돌려줌.
        return convFile;
    }
}
