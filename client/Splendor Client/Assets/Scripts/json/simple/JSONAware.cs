using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * Beans that support customized output of JSON text shall implement this interface.  
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface IJSONAware {
    /**
	 * @return JSON text
	 */
    string ToJSONString();
}
