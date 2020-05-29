package edu.fzu.qujing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.fzu.qujing.bean.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    IPage<Message> listMessageByStudentId(@Param("publisherId") String publisherId,
                                 @Param("receiveId") String receiveId,
                                 @Param("page") Page<Message> page);


}
