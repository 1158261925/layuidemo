package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    //@Size修饰的字段长度不能超过255或者低于1。
    @NotNull(message = "图书作者不能为空")
    @NotBlank(message = "图书作者不能为空")
    @Size(min = 1,max = 255,message = "图书作者字符输入长度不符合要求")
    private String bookAuthor;

    @NotNull(message = "图书名称不能为空")
    @NotBlank(message = "图书名称不能为空")
    @Size(min = 1,max = 255,message = "图书名称字符输入长度不符合要求")
    private String bookName;

    @NotNull(message = "图书页数不能为空")
    @NotBlank(message = "图书页数不能为空")
    @Min(value = 1,message = "页数不能小于1")
    @Max(value = 5000,message = "页数不能大于5000")
    @Pattern(regexp = "[1-9][0-9]{0,3}",message = "图书页数必须为大于0的整数")
    private String bookNumber;

    //@DecimalMin、@DecimalMax 验证小数的最大值和最小值
    //@Digits 验证数字的整数位和小数位的位数是否超过指定的长度。
    //@Digits(integer = 3,fraction = 2,message = "数字的整数位不能超过3位，小数位不能超过2位")
    @NotNull(message = "图书价格不能为空")
    @NotBlank(message = "图书价格不能为空")
    @DecimalMin(value = "1.00",message = "图书价格不能小于1元")
    @DecimalMax(value = "999.00",message = "图书价格不能大于999元")
    @Pattern(regexp = "(0|[1-9]\\d{0,2})(\\.\\d{1,2})?",message = "数字的整数位不能超过3位，小数位不能超过2位")
    private String bookPrice;

}