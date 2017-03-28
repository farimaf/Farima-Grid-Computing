package masterinterface;

/**
 * Created by Farima on 7/23/2016.
 */
public class ByteArrayClassLoader extends ClassLoader{

    public Class getClassFromBytes(byte[] classInByte, String className)
    {
        return defineClass(className,classInByte,0,classInByte.length);
    }
}
