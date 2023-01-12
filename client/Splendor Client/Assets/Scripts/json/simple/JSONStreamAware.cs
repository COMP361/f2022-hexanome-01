using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;

public interface IJSONStreamAware
{
    /**
      * write JSON string to out.
      */
    void WriteJSONString(StringWriter stringWriter);
}
