package com.armoury.backend.training;

import com.armoury.backend.config.BaseException;
import com.armoury.backend.training.model.GetPostInfoRes;
import com.armoury.backend.training.model.GetPostSumInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.armoury.backend.config.BaseResponseStatus.*;

@Service
public class TrainingProvider {

    @Autowired
    private final TrainingDao trainingDao;

    public TrainingProvider(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Integer getPageNum(int categoryIdx) throws BaseException{
        try {
            int postNumInPage = 6;
            int postNum = trainingDao.countCategoryPosts(categoryIdx);
            if (postNum == 0)
                return 0;
            return postNum / postNumInPage + 1;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetPostSumInfoRes> getPostsInfo(int categoryIdx, int pageNum) throws BaseException {
        if (pageNum < 0)
            throw new BaseException(EMPTY_CONTENT);
        List<GetPostSumInfoRes> infoList = trainingDao.getPostInfoList(categoryIdx, pageNum);
        if (infoList.size() == 0)
            throw new BaseException(EMPTY_CONTENT);
        return infoList;
    }

    public GetPostInfoRes getSinglePostInfo(int postIdx) throws BaseException {
        GetPostInfoRes toolInfo = null;
        try {
            toolInfo = trainingDao.getPostInfo(postIdx);
            return toolInfo;
        } catch (Exception exception){
            throw new BaseException(EMPTY_CONTENT);
        }
    }
}
