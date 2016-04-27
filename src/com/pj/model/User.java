package com.pj.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 此实体的定义应用了springmvc的注解，省去了传统的Hibernate跟数据库之间的映射配置文件
 * @author will
 *
 */
@Entity  
@Table(name="USER")  
public class User {  

@Id  
@GeneratedValue(generator="system-uuid")  
@GenericGenerator(name = "system-uuid",strategy="uuid")  
@Column(length=32)  
private String id;  

@Column(length=32)  
private String userName;  

@Column(length=32)  
private String age;  

public String getId() {  
    return id;  
}  

public void setId(String id) {  
    this.id = id;  
}  

public String getUserName() {  
    return userName;  
}  

public void setUserName(String userName) {  
    this.userName = userName;  
}  

public String getAge() {  
    return age;  
}  

public void setAge(String age) {  
    this.age = age;  
}  
}
