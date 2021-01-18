package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
@Entity
public class UserInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    @Pattern(regexp = "[a-zA-Z]{5,12}",message = "用户名格式错误！")
    private String username;

    @Column(name = "passwd")
    @Pattern(regexp = "\\d{5,12}",message = "密码格式错误！")
    private String passwd;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "age")
    @Max(value = 120,message = "年龄超出最高限制！")
    @Min(value = 1,message = "年龄低于最低限制！")
    private Integer age;

    @Column(name = "gender")
    private String gender;

}