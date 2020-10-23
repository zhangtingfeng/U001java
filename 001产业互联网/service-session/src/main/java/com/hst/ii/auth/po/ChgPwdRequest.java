/**
 *
 */
package com.hst.ii.auth.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyh
 *
 */
@Data
@NoArgsConstructor
public class ChgPwdRequest {
    private String pwd1;
    private String pwd2;
    private String pwd3;
}
