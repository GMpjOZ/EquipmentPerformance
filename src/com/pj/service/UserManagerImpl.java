package com.pj.service;

import java.util.List;

import com.pj.dao.UserDao;
import com.pj.model.User;


public class UserManagerImpl implements UserManager {

private UserDao userDao;  

public void setUserDao(UserDao userDao) {  
    this.userDao = userDao;  
}  

public User getUser(String id) {  
    return userDao.getUser(id);  
}  

public List<User> getAllUser() {  
    return userDao.getAllUser();  
}  

public void addUser(User user) {  
    userDao.addUser(user);  
}  

public boolean delUser(String id) {  

    return userDao.delUser(id);  
}  

public boolean updateUser(User user) {  
    return userDao.updateUser(user);  
}  

}
