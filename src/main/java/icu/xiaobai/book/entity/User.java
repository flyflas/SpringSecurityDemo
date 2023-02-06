package icu.xiaobai.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId
    private String email;
    private String password;
    private Integer age;
    private String sex;
    private Integer status;

}
