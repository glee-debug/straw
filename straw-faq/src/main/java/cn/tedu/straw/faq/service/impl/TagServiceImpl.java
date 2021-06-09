package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.faq.mapper.TagMapper;
import cn.tedu.straw.faq.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaoliu
 * @since 2021-05-20
 */
@Service
// ServiceImpl是mybatisPlus提供可以直接连接数据库的方法
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    // 缓存 多线程安全
    private List<Tag> tags = new CopyOnWriteArrayList<>();
    private Map<String,Tag> nameToTagMap = new ConcurrentHashMap<>();

    @Override
    public List<Tag> getTags() {
        if (tags.isEmpty()){
            synchronized (tags){
                if (tags.isEmpty())
                    tags.addAll(list());
                    for (Tag t:tags){
                        nameToTagMap.put(t.getName(),t);
                    }
            }
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getNameToTagMap() {
        if (nameToTagMap.isEmpty()){
            getTags();
        }
        return nameToTagMap;
    }
}
