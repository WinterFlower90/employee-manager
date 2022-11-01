package com.pje.employeemanager.controller;

import com.pje.employeemanager.model.CommonResult;
import com.pje.employeemanager.model.SingleResult;
import com.pje.employeemanager.model.member.ProfileImageResponse;
import com.pje.employeemanager.service.ProfileImageService;
import com.pje.employeemanager.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "프로필 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profile")
public class ProfileController {
    private final ProfileImageService profileImageService;

    /*
    웹을 통해서 전달받는 파일의 형태는 MultipartFile 임.
     */
    @ApiOperation(value = "프로필 이미지 등록")
    @PostMapping("/image/member-id/{memberId}")
    public CommonResult setProfileImage(@PathVariable long memberId, @RequestParam("file") MultipartFile file) {
        profileImageService.setImage(memberId, file);
        return ResponseService.getSuccessResult();
    }

    @ApiOperation(value = "프로필 이미지 가져오기")
    @GetMapping("/image/member-id/{memberId}")
    public SingleResult<ProfileImageResponse> getProfileImage(@PathVariable long memberId) {
        return ResponseService.getSingleResult(profileImageService.getImage(memberId));
    }
}
