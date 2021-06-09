package com.xiaoliu.straw.portal.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoliu.straw.portal.mapper.ClassroomMapper;
import com.xiaoliu.straw.portal.mapper.RoleMapper;
import com.xiaoliu.straw.portal.mapper.UserMapper;
import com.xiaoliu.straw.portal.mapper.UserRoleMapper;
import com.xiaoliu.straw.portal.model.*;
import com.xiaoliu.straw.portal.service.IQuestionService;
import com.xiaoliu.straw.portal.service.IUserCollectService;
import com.xiaoliu.straw.portal.service.IUserService;
import com.xiaoliu.straw.portal.service.ServiceException;
import com.xiaoliu.straw.portal.vo.RegisterVo;
import com.xiaoliu.straw.portal.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    private RoleMapper roleMapper;
    @Override
    public UserDetails getUserDetails(String username) {
        User user=userMapper.findUserByUsername(username);
        if (user==null){
            return null;
        }
        List<Permission> permissions=userMapper.findUserPermissionById(user.getId());
        // 可变参数 用户权限数组
        String[] auth=new String[permissions.size()];
        int i=0;
        for (Permission p:permissions){
            auth[i++] = p.getName();
        }
        List<Role> roles=userMapper.findUserRoles(user.getId());
        // 扩容!!!!
        auth= Arrays.copyOf(auth,auth.length+roles.size());
        for (Role r:roles){
            auth[i++]=r.getName();
        }
        UserDetails userDetails= org.springframework.security.core.userdetails.User
                .builder()
                .username(username)
                .password(user.getPassword())
                .authorities(auth)
                // 锁定true 不锁定false
                .accountLocked(user.getLocked()==1)
                // 禁用true 可用false
                .disabled(user.getEnabled()!=1)
                .build();
        return userDetails;
    }

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

    @Override
    public Map<String, User> getTeacherMap() {
        if (teachersMap.isEmpty()){
            getMaster();
        }
        return teachersMap;
    }

    @Autowired
    IQuestionService questionService;
    @Autowired
    IUserCollectService userCollectService;
    @Override
    public UserVo getCurrentUserVo(String username) {
        User user = userMapper.findUserByUsername(username);
        int questions = questionService.countQuestionsByUserId(user.getId());
        int collections = userCollectService.countCollectionsByUserId(user.getId());
        UserVo userVo = new UserVo()
                .setId(user.getId())
                .setUsername(username)
                .setNickname(user.getNickname())
                .setQuestions(questions)
                .setCollections(collections);
        return userVo;
    }
}
