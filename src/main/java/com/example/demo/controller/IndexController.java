package com.example.demo.controller;

import com.example.demo.jpa.BookInfoJpa;
import com.example.demo.jpa.UserInfoJpa;
import com.example.demo.mapper.BookMapper;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.UserInfo;
import com.example.demo.utils.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserInfoJpa userInfoJpa;

    @Autowired
    private BookInfoJpa bookInfoJpa;

    @Resource
    private BookMapper bookMapper;

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "index")
    public String toIndex(){
        return "register";
    }

    /**
     * 转发login.html页面
     * @return
     */
    @RequestMapping(value = "login")
    public String toLogin(){
        return "login";
    }

    /**
     * 转发到book.html
     * @return
     */
    @RequestMapping(value = "book")
    public String toBook(){
        return "book";
    }

    /**
     * 注册表单后端数据校验
     * @param
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> register(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult){
        logger.info("requestBoyd:{}",userInfo);
        boolean flag = bindingResult.hasErrors();
        Map<String,Object> mapGlobal = new HashMap<>();
        if (flag){
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            Map<String,Object> map = new HashMap<>();
            for (FieldError fieldError:fieldErrorList){
               String field = fieldError.getField();
               String message = fieldError.getDefaultMessage();
               map.put(field,message);
            }
            logger.info("mapEoor:{}",map);
            mapGlobal.put("1",map);
        }else{
            if (userInfoJpa.findByUsername(userInfo.getUsername())==null){
                mapGlobal.put("2","表单元素校验成功！");
                userInfoJpa.save(userInfo);
            }else{
                mapGlobal.put("3","该用户账号已经注册过，请重新注册！");
            }
        }
        return mapGlobal;
    }

    /**
     * 用户登录操作
     * @param username
     * @param passwd
     * @return
     */
    @RequestMapping(value = "loginUrl",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginUrl(@RequestParam(value = "username") String username, @RequestParam(value = "passwd") String passwd ){

        Map<String,Object> map = new HashMap<>();
        if (userInfoJpa.findByUsername(username)==null){
            map.put("1","用户名错误或用户不存在！");
        }else {
            if (userInfoJpa.findByUsernameAndPasswd(username,passwd)==null){
                map.put("2","输入密码错误，请重新输入！");
            }else{
                logger.info("用户名和密码正确信息为：{}",userInfoJpa.findByUsernameAndPasswd(username,passwd).toString());
                map.put("3","用户名和密码正确！");
            }
        }
        return map;
    }

    /**
     * 根据关键字查询图书清单
     * @param page
     * @param limit
     * @param bookInfo
     * @return
     */
    @RequestMapping(value = "getBookList",method = RequestMethod.GET)
    @ResponseBody
    public String getBookList(@RequestParam(value = "page") int page,@RequestParam(value = "limit") int limit, @RequestParam(value = "bookInfo") String bookInfo){
        logger.info("page = {}",page);
        logger.info("limit = {}",limit);
        logger.info("bookInfo = {}",bookInfo);
        if (bookInfo.trim().isEmpty()){
          List<Book> allBooks = bookInfoJpa.findAll();
          int count = allBooks.size();
          int lastIndex = page*limit;
          if (count < lastIndex){
              lastIndex = count;
          }
          return DataFormat.transJsonToString(200,"请求成功!",count,allBooks.subList((page-1)*limit,lastIndex));
        }else{
            List<Book> bookLists = bookInfoJpa.findByBookAuthorLikeOrBookNameLike("%"+bookInfo+"%","%"+bookInfo+"%");
            logger.info("bookList = {}",bookLists);
            int count = bookLists.size();
            int lastIndex = page*limit;
            if (count < lastIndex){
                lastIndex = count;
            }
            return DataFormat.transJsonToString(200,"请求成功!",count,bookLists.subList((page-1)*limit,lastIndex));
        }
    }

    /**
     * 更新书单
     * @param book
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "updateBook")
    @ResponseBody
    public Map<String,Object> updateBook(@RequestBody @Valid Book book,BindingResult bindingResult){
        logger.info("book = {}",book);
        logger.info("book id = {}",book.getId());
        boolean flag = bindingResult.hasErrors();
        Map<String,Object> mapGlobal = new HashMap<>();
        if (flag){
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            Map<String,Object> map = new HashMap<>();
            for (FieldError fieldError:fieldErrorList){
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                map.put(field,message);
            }
            mapGlobal.put("1",map);
            return mapGlobal;
        } else {
            int number = bookMapper.updateByPrimaryKey(book);
            if (number == 1){
                mapGlobal.put("2","修改成功！");
            }else {
                mapGlobal.put("3","修改失败！");
            }
            return mapGlobal;
        }
    }

    /**
     * 删除书单
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteByBookId")
    @ResponseBody
    public Map<String, Object> deleteByBookId(@RequestParam(name = "id") Long id){
        logger.info("传入的book_id值为：{}",id);
        int flag = bookMapper.deleteByPrimaryKey(id);
        Map<String,Object> map = new HashMap<>();
        if (flag == 1){
            map.put("flag",1);
        }else{
            map.put("flag",0);
        }
        return map;
    }


    /**
     * 添加新书
     * @param book
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "addBook")
    @ResponseBody
    public Map<String, Object> addBook(@RequestBody @Valid Book book, BindingResult bindingResult){
        Map<String,Object> maps = new HashMap<>();
        boolean flag = bindingResult.hasErrors();
        if (flag){
            Map<String,Object> mapError = new HashMap<>();
            List<FieldError> bookFiledErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : bookFiledErrorList){
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                mapError.put(field,message);
            }
            maps.put("1",mapError);
            return maps;
        }else{
            bookMapper.insert(book);
            maps.put("2","添加成功！");
            return maps;
        }
    }
}
