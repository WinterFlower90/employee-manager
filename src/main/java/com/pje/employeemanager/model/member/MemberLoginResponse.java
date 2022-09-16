package com.pje.employeemanager.model.member;

import com.pje.employeemanager.entity.Member;
import com.pje.employeemanager.interfaces.CommonModelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginResponse {
    @ApiModelProperty(value = "사원 시퀀스", required = true)
    private Long memberId;

    /** 불변 데이터를 같이 보내주기 */
    @ApiModelProperty(value = "사원 아이디", required = true)
    private String username;

    /** 불변 데이터를 같이 보내주기 */
    @ApiModelProperty(value = "사원 입사일", required = true)
    private LocalDate dateJoin;

    private MemberLoginResponse(MemberLoginResponseBuilder builder) {
        this.memberId = builder.memberId;
        this.username = builder.username;
        this.dateJoin = builder.dateJoin;
    }

    public static class MemberLoginResponseBuilder implements CommonModelBuilder<MemberLoginResponse> {
        private final Long memberId;
        private final String username;
        private final LocalDate dateJoin;

        public MemberLoginResponseBuilder(Member member) {
            this.memberId = member.getId();
            this.username = member.getUsername();
            this.dateJoin = member.getDateJoin();
        }

        @Override
        public MemberLoginResponse build() {
            return new MemberLoginResponse(this);
        }
    }
}
