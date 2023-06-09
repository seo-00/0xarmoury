package com.armoury.backend.tools;

import com.armoury.backend.config.BaseException;
import com.armoury.backend.config.BaseResponse;
import com.armoury.backend.config.BaseResponseStatus;
import com.armoury.backend.tools.model.*;
import com.armoury.backend.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
@Tag(name = "Tools", description = "해킹 도구와 관련된 기능 & 정보 제공")
public class ToolController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ToolProvider toolProvider;
    @Autowired
    private final ToolService toolService;
    @Autowired
    private final JwtService jwtService;

    public ToolController(ToolProvider toolProvider, ToolService toolService, JwtService jwtService){
        this.toolProvider = toolProvider;
        this.toolService = toolService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @Operation(summary = "D3 데이터 조회", description = "MITRE 기술, 잔술과 공격도구 전체 연계 데이터를 조회합니다.")
    @GetMapping("/d3")
    public DataRes getD3data(){
        return new DataRes("root", toolProvider.getD3data());
    }

    @ResponseBody
    @Operation(summary = "공식 공격 도구 개별 정보 조회 by toolIdx", description = "toolIdx 사용하여 공격 도구 정보 조회합니다.")
    @GetMapping("/{toolIdx}")
    public BaseResponse<GetToolRes> getToolByIdx (@PathVariable("toolIdx")int toolIdx){
        try{
            GetToolRes toolRes = toolProvider.getToolByIdx(toolIdx);
            return new BaseResponse<>(toolRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공식 공격 도구 개별 정보 조회 by 도구 이름", description = "도구 이름을 사용하여 공격 도구 정보 조회합니다.")
    @GetMapping("/toolName")
    public BaseResponse<GetToolRes> getToolByIdx (@RequestParam(required = true) String toolName){
        try{
            GetToolRes toolRes = toolProvider.getToolByName(toolName);
            return new BaseResponse<>(toolRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공식 공격 도구 카테고리별 정보 조회", description = "칼리 기능 카테고리을 사용하여 공격 도구 정보 리스트를 조회합니다."
        + "</br>1    Information Gathering</br>2   Vulnerability Analysis</br>3   Web Application Analysis</br>4   Database Assessment"
        +"</br>5   Password Attacks</br>6   Wireless Attacks</br>7   Reverse Engineering</br>8   Exploitation Tools</br>"
        +"9   Sniffing & Spoofing</br>10   Post Exploitation</br>11   Forensics</br>12   Reporting Tools </br>13   Master Tools")
    @GetMapping("/category/{categoryIdx}")
    public BaseResponse<List<GetToolSumInfoRes>> getToolsByCategoryIdx (@PathVariable("categoryIdx")int categoryIdx){
        try{
            if (categoryIdx<1 || categoryIdx > 13)
                return new BaseResponse<>(BaseResponseStatus.WRONG_INPUT_REQ);
            List<GetToolSumInfoRes> infoList = toolProvider.getToolsByCategoryIdx(categoryIdx);
            return new BaseResponse<>(infoList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공식 공격 도구 AML 조회 by toolIdx", description = "AML 정보를 list 형태로 반환합니다.")
    @GetMapping("/AML/{toolIdx}")
    public BaseResponse<List<String>> getAMLlByIdx (@PathVariable("toolIdx")int toolIdx){
        try{
            List<String> list = toolProvider.getAMLByIdx(toolIdx);
            return new BaseResponse<>(list);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공식 공격 도구 AML 조회 by toolName", description = "AML 정보를 list 형태로 반환합니다.")
    @GetMapping("/AML/toolName")
    public BaseResponse<List<String>> getAMLlByIdx (@RequestParam(required = true) String toolName){
        try{
            List<String> list = toolProvider.getAMLByName(toolName);
            return new BaseResponse<>(list);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "카테고리 조회", description = "칼리 기능 카테고리 기반으로 작성되었습니다.")
    @GetMapping("/category")
    public BaseResponse<List<GetCategoryRes>> getCategoryAll (){
        try{
            List<GetCategoryRes> list = toolProvider.getCategoryAll();
            return new BaseResponse<>(list);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공격 도구에 대한 마이터 정보 조회", description = "칼리 기능 카테고리 기반으로 작성되었습니다.")
    @GetMapping("/mitreInfo/{toolIdx}")
    public BaseResponse<List<GetMitreByAmlRes>> getMitreInfo (@PathVariable("toolIdx")int toolIdx){
        try{
            List<GetMitreByAmlRes> list = toolService.getMitreInfo(toolIdx);
            return new BaseResponse<>(list);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "공격 도구 개별 위키 수정", description = "공격 도구에 대한 위키 정보 수정을 요청합니다."
                        + "</br>뱃지 개수가 5개 이상인 Master 유저만 요청이 승인됩니다."
                        + "</br>JWT 토큰이 필요한 요청입니다.")
    @PostMapping ("/update/wiki")
    public BaseResponse<String> updateWiki(@RequestBody PostWikiReq postWikiReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(toolService.updateWiki(userIdxByJwt, postWikiReq))
                return new BaseResponse<>("성공");
            return new BaseResponse<>("실패");
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @Operation(summary = "Matrix Technique 관련 도구 검색하기", description = "AML의 일부를 입력받아 관련된 공격 도구를 조회합니다."
                            + "</br> \\'RC-2\\' 형태로 입력해주세요!")
    @GetMapping ("/mitreInfo")
    public BaseResponse<List<String>> getToolsByTechnique(@RequestParam(required = true) String amlPart) {
        try {
            return new BaseResponse<>(toolProvider.getToolsByTechnique(amlPart));
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

