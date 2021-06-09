package cn.tedu.straw.faq.service;

import cn.tedu.straw.commons.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
public interface ITagService extends IService<Tag> {

    List<Tag> getTags();

    // 查询所有标签在map中的方法
    Map<String,Tag> getNameToTagMap();

}
