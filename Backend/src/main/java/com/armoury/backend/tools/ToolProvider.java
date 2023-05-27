package com.armoury.backend.tools;

import com.armoury.backend.config.BaseException;
import com.armoury.backend.tools.model.GetToolRes;
import com.armoury.backend.tools.model.GetToolSumInfoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.armoury.backend.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ToolProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ToolDao toolDao;
    @Autowired
    public ToolProvider(ToolDao toolDao){
        this.toolDao = toolDao;
    }

    public GetToolRes getToolByIdx(int toolIdx) throws BaseException{
        try{
            GetToolRes getToolRes = toolDao.getToolByIdx(toolIdx);
            return getToolRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetToolRes getToolByName(String toolName) throws BaseException{
        try{
            GetToolRes getToolRes = toolDao.getToolByName(toolName);
            return getToolRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetToolSumInfoRes> getToolsByCategoryIdx(int categoryIdx) throws BaseException{
        try{
            List<GetToolSumInfoRes> getToolRes = toolDao.getSumInfoByCategory(categoryIdx);
            return getToolRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> getAMLByIdx(int toolIdx) throws BaseException{
        try{
            String aml = toolDao.getAMLByIdx(toolIdx);
            return extractAML(aml);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> getAMLByName(String toolName) throws BaseException{
        try{
            String aml = toolDao.getAMLByName(toolName);
            return extractAML(aml);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> extractAML(String aml) {
        List<String> list = new ArrayList<>();
        String[] sections = aml.split(":");

        for (String section : sections) {
            String prefix = section.substring(0, 2);
            String valuesString = section.substring(3, section.length() - 1);
            String[] values = valuesString.split(",");

            for (String value : values) {
                list.add(prefix + "-" + value);
            }
        }
        return list;
    }
}
