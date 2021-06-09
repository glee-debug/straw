package cn.tedu.straw.sys.service.impl;


import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.sys.mapper.ClassroomMapper;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.mapper.UserRoleMapper;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Override
    public void registerStudent(RegisterVo registerVo) {
        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code", registerVo.getInviteCode());
        Classroom classroom = classroomMapper.selectOne(queryWrapper);
        log.debug("邀请码查询出的班级为:{}", classroom);
        if (classroom == null) {
            throw ServiceException.unprocesabelEntity("邀请码错误");
        }
        User user = userMapper.findUserByUsername(registerVo.getPhone());
        if (user != null) {
            throw ServiceException.unprocesabelEntity("手机号已经被注册");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode(registerVo.getPassword());

        user = new User().setUsername(registerVo.getPhone())
                .setPassword("{bcrypt}" + pwd)
                .setNickname(registerVo.getNickname())
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setLocked(0)
                .setEnabled(1);

        int num = userMapper.insert(user);
        if (num != 1) {
            throw ServiceException.busy();
        }
        UserRole userRole = new UserRole().setUserId(user.getId()).setRoleId(2);
        num = userRoleMapper.insert(userRole);
        if (num != 1) {
            throw ServiceException.busy();
        }
    }

    // 声明老师的list和map缓存属性
    private List<User> teachers = new CopyOnWriteArrayList<>();
    private Map<String,User> teachersMap = new ConcurrentHashMap<>();

    @Override
    public List<User> getMaster() {
        if (teachers.isEmpty()){
            synchronized (teachers){
                if (teachers.isEmpty()){
                    QueryWrapper<User> query = new QueryWrapper<>();
                    query.eq("type",1);
                    List<User> list = userMapper.selectList(query);
                    teachers.addAll(list);
                    for (User u:list){
                        // 因为密码属性属于安全级别较高的属性 不宜长时间保存在内存中
                        // 清除密码等敏感信息
                        u.setPassword("");
                        teachersMap.put(u.getNickname(),u);
                    }
                }
            }
        }
        return teachers;
    }

    private Timer timer = new Timer();

    // 代码块会在构造方法执行之前执行
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (teachers){
                    teachers.clear();
                    teachersMap.clear();
                    log.debug("缓存已清空");
                }
            }
        }, 1000 * 60 * 10, 1000 * 60 * 30);

    }

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Map<String, User> getTeacherMap() {
        if (teachersMap.isEmpty()){
            getMaster();
        }
        return teachersMap;
    }
    @Override
    public UserVo getCurrentUserVo(String username) {
        User user = userMapper.findUserByUsername(username);
        String url = "http://faq-service/v1/questions/count?userId={1}";
        int questions = restTemplate.getForObject(url,Integer.class,user.getId());
        url = "http://sys-service/v1/userCollects/countCollections?userId={1}";
        int collections = restTemplate.getForObject(url,Integer.class,user.getId());
        UserVo userVo = new UserVo()
                .setId(user.getId())
                .setUsername(username)
                .setNickname(user.getNickname())
                .setQuestions(questions)
                .setCollections(collections);
        return userVo;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public List<Permission> getUserPermissions(Integer userId) {
        return userMapper.findUserPermissionById(userId);
    }

    @Override
    public List<Role> getUserRoles(Integer userId) {
        return userMapper.findUserRoles(userId);
    }
}
