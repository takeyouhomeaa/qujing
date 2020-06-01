package edu.fzu.qujing.util;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import edu.fzu.qujing.bean.Task;
import io.jsonwebtoken.lang.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
public class PageUtil {
    public static final Integer PAGES = 10;
}
